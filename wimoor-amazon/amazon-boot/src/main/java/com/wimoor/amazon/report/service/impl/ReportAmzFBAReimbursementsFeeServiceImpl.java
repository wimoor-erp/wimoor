package com.wimoor.amazon.report.service.impl;


import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.amazon.spapi.api.ReportsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.CreateReportResponse;
import com.amazon.spapi.model.reports.CreateReportSpecification;
import com.amazon.spapi.model.reports.ReportOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.finances.mapper.FBAReimbursementsFeeReportMapper;
import com.wimoor.amazon.finances.pojo.entity.FBAReimbursementsFeeReport;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
 

@Service("reportAmzFBAReimbursementsFeeService")
public class ReportAmzFBAReimbursementsFeeServiceImpl extends ReportServiceImpl{
	@Resource
	FBAReimbursementsFeeReportMapper fBAReimbursementsFeeReportMapper;
	@Resource
	IMarketplaceService marketplaceService;
	
	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
        amazonAuthority.setUseApi("createReport");
		  ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
		  List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		  for(Marketplace market:marketlist) {
			  CreateReportSpecification body=new CreateReportSpecification();
			  body.setReportType(myReportType());
			  body.setDataStartTime(AmzDateUtils.getOffsetDateTimeUTC(cstart));
			  body.setDataEndTime(AmzDateUtils.getOffsetDateTimeUTC(cend));
			  ReportOptions reportOptions=getMyOptions();
			  if(reportOptions!=null) {
				body.setReportOptions(reportOptions);  
			  }
			  List<String> list=new ArrayList<String>();
			  list.add(market.getMarketplaceid());
			  amazonAuthority.setMarketPlace(market);
			  if(ignore==null||ignore==false) {
				  Map<String,Object> param=new HashMap<String,Object>();
				  param.put("sellerid", amazonAuthority.getSellerid());
				  param.put("reporttype", this.myReportType());
				  param.put("marketplacelist", list);
				  Date lastupdate= iReportRequestRecordService.lastUpdateRequestByType(param);  
				  if(lastupdate!=null&&GeneralUtil.distanceOfHour(lastupdate, new Date())<6) {
					  continue;
				  }
			  }
			  body.setMarketplaceIds(list);
			  try {
					  ApiCallback<CreateReportResponse> callback = new ApiCallbackReportCreate(this,amazonAuthority,market,cstart.getTime(),cend.getTime());
				      api.createReportAsync(body,callback);
				      break;
				  } catch (ApiException e) {
					  amazonAuthority.setApiRateLimit( null,e);
					  e.printStackTrace();
			    }
		  }
}

	@Override
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br) {
		int lineNumber = 0;
		String line;
		String mlog="";
		Map<String,Integer> titleList = new HashMap<String,Integer>();
		List<FBAReimbursementsFeeReport> itemlist=new LinkedList<FBAReimbursementsFeeReport>();
		try {
			Date nowDate=new Date();
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				if (lineNumber == 0) {
					for (int i = 0; i < info.length; i++) {
						titleList.put(info[i],i);
					}
				}else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					FBAReimbursementsFeeReport report = new FBAReimbursementsFeeReport();
					String approvaldateStr = GeneralUtil.getStrValue(info,titleList,"approval-date");
					approvaldateStr = approvaldateStr.replace("/", "-");
					approvaldateStr = approvaldateStr.replace("UTC", "");
					if(approvaldateStr.length()>19) {
						approvaldateStr=approvaldateStr.replace("T", " ");
						approvaldateStr=approvaldateStr.substring(0, 19);
					}
					report.setApprovalDate(sdf.parse(approvaldateStr));
					report.setCurrencyUnit(GeneralUtil.getStrValue(info,titleList,"currency-unit"));
					String marketplaceid=marketplaceService.getMarketPlaceId(report.getCurrencyUnit());
					if(marketplaceid==null||marketplaceid.equals("EU")) {
						marketplaceid=amazonAuthority.getMarketPlace().getMarketplaceid();
					}
					report.setMarketplaceid(marketplaceid);
					 
					LambdaQueryWrapper<FBAReimbursementsFeeReport> queryWrapper=new LambdaQueryWrapper<FBAReimbursementsFeeReport>();
					queryWrapper.eq(FBAReimbursementsFeeReport::getAmazonauthid, amazonAuthority.getId());
					queryWrapper.eq(FBAReimbursementsFeeReport::getApprovalDate, report.getApprovalDate());
					fBAReimbursementsFeeReportMapper.delete(queryWrapper);
					
					report.setReimbursementId(GeneralUtil.getStrValue(info,titleList,"reimbursement-id"));
					report.setAmazonOrderId(GeneralUtil.getStrValue(info,titleList,"amazon-order-id"));
					report.setCaseId(GeneralUtil.getStrValue(info,titleList,"case-id"));
					report.setReason(GeneralUtil.getStrValue(info,titleList,"reason"));
					report.setSku(GeneralUtil.getStrValue(info,titleList,"sku"));
					if(report.getSku().length()>50) {
						report.setSku(report.getSku().substring(0, 49));
					}
					report.setFnsku(GeneralUtil.getStrValue(info,titleList,"fnsku"));
					report.setAsin(GeneralUtil.getStrValue(info,titleList,"asin"));
					 
					report.setConditions(GeneralUtil.getStrValue(info,titleList,"condition"));
					report.setAmountPerUnit(GeneralUtil.getBigDecimalValue(info,titleList,"amount-per-unit"));
					
					if(StrUtil.isEmpty(GeneralUtil.getStrValue(info,titleList,"amount-total"))) {
						report.setAmountTotal(null);
					}else {
						report.setAmountTotal(GeneralUtil.getBigDecimalValue(info,titleList,"amount-total"));
					}
					if(StrUtil.isEmpty(GeneralUtil.getStrValue(info,titleList,"quantity-reimbursed-cash"))) {
						report.setQuantityReimbursedCash(null);
					}else {
						report.setQuantityReimbursedCash(GeneralUtil.getIntegerValue(info,titleList,"quantity-reimbursed-cash"));
					}
					if(StrUtil.isEmpty(GeneralUtil.getStrValue(info,titleList,"quantity-reimbursed-inventory"))) {
						report.setQuantityReimbursedInventory(null);
					}else {
						report.setQuantityReimbursedInventory(GeneralUtil.getIntegerValue(info,titleList,"quantity-reimbursed-inventory"));
					}
					if(StrUtil.isEmpty(GeneralUtil.getStrValue(info,titleList,"quantity-reimbursed-total"))) {
						report.setQuantityReimbursedTotal(null);
					}else {
						report.setQuantityReimbursedTotal(GeneralUtil.getIntegerValue(info,titleList,"quantity-reimbursed-total"));
					}
					report.setAmazonauthid(amazonAuthority.getId());
					report.setLastupdate(nowDate);
				
					report.setMarketplaceid(marketplaceid);
					itemlist.add(report);
				}
				lineNumber++;
			}
			fBAReimbursementsFeeReportMapper.insertBatch(itemlist);
			fBAReimbursementsFeeReportMapper.repaireMarket();

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mlog+=e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mlog+=e.getMessage();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mlog+=e.getMessage();
		} finally {
				if(br!=null) {
				    try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mlog+=e.getMessage();
					}
				}
	     }
		return mlog;
	}
	 
	@Override
	public String myReportType() {
		return ReportType.FbaReimbursementsFeeReport;
	}
	
}
