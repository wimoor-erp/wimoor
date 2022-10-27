package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.amazon.spapi.api.ReportsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.CreateReportResponse;
import com.amazon.spapi.model.reports.CreateReportSpecification;
import com.amazon.spapi.model.reports.ReportOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.report.mapper.FBALongTermStorageFeeReportMapper;
import com.wimoor.amazon.report.pojo.entity.FBALongTermStorageFeeReport;
import com.wimoor.amazon.report.pojo.entity.FBAStorageFeeReport;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;

 

@Service("reportAmzfBALongTermStorageFeeService")
public class ReportAmzFBALongTermStorageFeeServiceImpl extends ReportServiceImpl{

 
	@Resource
	private IMarketplaceService marketplaceService;
	@Resource
	private FBALongTermStorageFeeReportMapper fBALongTermStorageFeeReportMapper;

	@Override
	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
        amazonAuthority.setUseApi("createReport");
		  ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
		  List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		  Set<String> region=new HashSet<String>();
		  for(Marketplace market:marketlist) {
			  CreateReportSpecification body=new CreateReportSpecification();
			  body.setReportType(myReportType());
			  cstart.set(Calendar.DATE, 1);
			  cend.setTime(cstart.getTime());
			  cend.add(Calendar.MONTH, 1);
			  cend.add(Calendar.DATE, -1);
			  body.setDataStartTime(AmzDateUtils.getOffsetDateTimeUTC(cstart));
			  body.setDataEndTime(AmzDateUtils.getOffsetDateTimeUTC(cend));
			  QueryWrapper<FBALongTermStorageFeeReport> query=new QueryWrapper<FBALongTermStorageFeeReport>();
			  query.eq("amazonauthid", amazonAuthority.getId());
			  query.eq("country", market.getMarket());
			  query.gt("snapshot_date",cstart.getTime());
			  query.lt("snapshot_date",cend.getTime());
			  Date today = new Date();
			  double days = Math.floor((today.getTime() - cend.getTime().getTime()) / 1000 / 3600 / 24); 
			  if(days<8) {
				  continue;
			  }
			   Long count = fBALongTermStorageFeeReportMapper.selectCount(query);
			   if(count!=null&&count>0) {
				   continue;
			   }
			   if(region.contains(market.getRegion())) {
				   continue;
			   }else {
				   region.add(market.getRegion());
			   }
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
				  } catch (ApiException e) {
					  amazonAuthority.setApiRateLimit( null,e);
					  e.printStackTrace();
			    }
		  }
    }

	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br )   {
 
 
		int lineNumber = 0;
		String line;
		Date snapshotDate=null;
		StringBuffer log = new StringBuffer();
		Map<String,Integer> titleList = new HashMap<String,Integer>();
		// snapshot-date sku fnsku asin product-name condition
		// qty-charged-12-mo-long-term-storage-fee per-unit-volume currency
		// 12-mo-long-terms-storage-fee qty-charged-6-mo-long-term-storage-fee
		// 6-mo-long-terms-storage-fee volume-unit country
		// enrolled-in-small-and-light
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				if (lineNumber == 0) {
					for (int i = 0; i < info.length; i++) {
						titleList.put(info[i],i);
					}
				}
				if (lineNumber > 0) {
					String snapshotDateStr = GeneralUtil.getStrValue(info,titleList,"snapshot-date");
					if(snapshotDateStr==null){
						continue;
					}
					if(snapshotDateStr!=null && snapshotDateStr.length()>19){
 						snapshotDate = GeneralUtil.getDatePlus(snapshotDateStr,amazonAuthority.getMarketPlace().getMarket());
					}
					if(lineNumber==1){
						deleteByAuthAndSnapshotDate(amazonAuthority.getId(), snapshotDate);
					}
					FBALongTermStorageFeeReport fBALongTermStorageFee = new FBALongTermStorageFeeReport();
					fBALongTermStorageFee.setAmazonauthid(amazonAuthority.getId());
					fBALongTermStorageFee.setSnapshotDate(snapshotDate);
					fBALongTermStorageFee.setSku(GeneralUtil.getStrValue(info,titleList,"sku"));
					fBALongTermStorageFee.setFnsku(GeneralUtil.getStrValue(info,titleList,"fnsku"));
					fBALongTermStorageFee.setAsin(GeneralUtil.getStrValue(info,titleList,"asin"));
					String country = GeneralUtil.getStrValue(info,titleList,"country");
					if(country==null){
						continue;
					}
					if("GB".equalsIgnoreCase(country)){
						country = "UK";
					}
					fBALongTermStorageFee.setCountry(country);
					fBALongTermStorageFee.setQtyCharged12month(GeneralUtil.getBigDecimalValue(info,titleList,"qty-charged-12-mo-long-term-storage-fee").intValue());
					fBALongTermStorageFee.setPerUnitVolume(GeneralUtil.getBigDecimalValue(info,titleList,"per-unit-volume"));
					fBALongTermStorageFee.setQtyCharged6month(GeneralUtil.getBigDecimalValue(info,titleList,"qty-charged-6-mo-long-term-storage-fee").intValue());
					fBALongTermStorageFee.setAmount12(GeneralUtil.getBigDecimalValue(info,titleList,"12-mo-long-terms-storage-fee"));
					fBALongTermStorageFee.setAmount6(GeneralUtil.getBigDecimalValue(info,titleList,"6-mo-long-terms-storage-fee"));
					fBALongTermStorageFee.setVolumeUnit(GeneralUtil.getBigDecimalValue(info,titleList,"volume-unit"));
					fBALongTermStorageFee.setCurrency(GeneralUtil.getStrValue(info,titleList,"currency"));
					fBALongTermStorageFee.setIsSl(GeneralUtil.getBooleanValue(info,titleList,"enrolled-in-small-and-light"));
					fBALongTermStorageFee.setLastupdate(new Date());
					try {
							fBALongTermStorageFeeReportMapper.insert(fBALongTermStorageFee);
					} catch (Exception e) {
						e.printStackTrace();
						if(log.length()==0){
							log.append("fBALongTermStorageFee新增失败！");
						}
						log.append(GeneralUtil.getStrValue(info,titleList,"sku")+","+e.getMessage());
					}finally {
						fBALongTermStorageFee=null;
					}
				}
				lineNumber++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(br!=null) {
				br.close();
				}
		 
			} catch (IOException e) {
				e.printStackTrace();
			}
			titleList.clear();
			titleList=null;
	}
       return log.toString();
	}
	
	public void deleteByAuthAndSnapshotDate(String amazonAuthid, Date snapshotDate) {
		QueryWrapper<FBALongTermStorageFeeReport> query = new QueryWrapper<FBALongTermStorageFeeReport>();
		query.eq("amazonauthid", amazonAuthid);
		query.eq("snapshotDate", snapshotDate);
		fBALongTermStorageFeeReportMapper.delete(query);
	}
	
	public static boolean isNumericzidai(String str) {
		if (str == null)
			return false;
		Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	@Override
	public String myReportType() {
		return ReportType.FbaLongTermStorageFeeReport;
	}

	
}
