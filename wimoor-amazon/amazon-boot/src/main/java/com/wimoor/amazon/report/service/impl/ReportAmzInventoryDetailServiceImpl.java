package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.wimoor.amazon.inbound.mapper.ShipInboundItemMapper;
import com.wimoor.amazon.inventory.mapper.InventoryDetailReportMapper;
import com.wimoor.amazon.inventory.pojo.entity.InventoryDetailReport;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("reportAmzInventoryDetailService")
public class ReportAmzInventoryDetailServiceImpl extends ReportServiceImpl{
	
	@Autowired
	InventoryDetailReportMapper inventoryDetailReportMapper;
	@Autowired
	ShipInboundItemMapper shipInboundItemMapper;
	@Autowired
	IMarketplaceService  iMarketplaceService;
	
	@Override
	public String myReportType() {
		return  ReportType.InventoryDetailViewReport;
	}
	 
	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
          amazonAuthority.setUseApi("createReport");
		  ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
		  List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		  Marketplace market=marketlist.get(0);
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
				  if(lastupdate==null) {
					   cstart.add(Calendar.DATE, -120);
					   body.setDataStartTime(AmzDateUtils.getOffsetDateTimeUTC(cstart));
				  }
				  if(lastupdate!=null&&GeneralUtil.distanceOfHour(lastupdate, new Date())<6) {
					  return;
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
	 
	@Override
	public ReportOptions getMyOptions() {
		// TODO Auto-generated method stub
		ReportOptions option=new ReportOptions();
		option.put("eventType", "Receipts");
		return  option;
	}

	@Override
	public synchronized String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br) {
//		Date
//		FNSKU
//		ASIN
//		MSKU
//		Title
//		EventType
//		ReferenceID
//		Quantity
//		FulfillmentCenter
//		Disposition
//		Reason
//		Country
//		ReconciledQuantity
		String mlog="";
		int lineNumber = 0;
		String line;
		SimpleDateFormat sdf6 = new SimpleDateFormat("MM/dd/yyyy");
		Date refreshtime=new Date();
		Map<String,Integer> titleList = Collections.synchronizedMap(new HashMap<String,Integer>());  
		try {
			//删除old
		
			while ((line = br.readLine()) != null) {
				String[] info = line.replace("\"", "").split("\t");
				int length = info.length;
				if(lineNumber==0) {
					for (Integer i = 0; i < length; i++) {
						titleList.put(info[i].toLowerCase(),i);
					}
				}else{
					InventoryDetailReport e=new InventoryDetailReport();
					try {
						e.setByday(sdf6.parse(GeneralUtil.getStrValue(info, titleList, "date")));
					} catch (ParseException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					e.setBytime(AmzDateUtils.getDate(GeneralUtil.getStrValue(info, titleList, "date and time")));
					e.setAsin(GeneralUtil.getStrValue(info, titleList, "asin"));
					e.setCountry(GeneralUtil.getStrValue(info, titleList, "country"));
					e.setDisposition(GeneralUtil.getStrValue(info, titleList, "disposition"));
					e.setEventType(GeneralUtil.getStrValue(info, titleList, "event type"));
					e.setFnsku(GeneralUtil.getStrValue(info, titleList, "fnsku"));
					e.setFulfillmentCenter(GeneralUtil.getStrValue(info, titleList, "fulfillment center"));
					e.setMsku(GeneralUtil.getStrValue(info, titleList, "msku"));
					e.setQuantity(GeneralUtil.getIntegerValue(info, titleList, "quantity"));
					e.setReason(GeneralUtil.getStrValue(info, titleList, "reason"));
					e.setReconciledQuantity(GeneralUtil.getIntegerValue(info, titleList, "reconciled quantity"));
					e.setReferenceID(GeneralUtil.getStrValue(info, titleList, "reference id"));
					e.setUnreconciledQuantity(GeneralUtil.getIntegerValue(info, titleList, "unreconciled quantity"));
					e.setRefreshtime(refreshtime);
					e.setAuthid(amazonAuthority.getId());
					
					
					LambdaQueryWrapper<InventoryDetailReport> query=new LambdaQueryWrapper<InventoryDetailReport>();
					query.eq(InventoryDetailReport::getAuthid, amazonAuthority.getId());
					query.eq(InventoryDetailReport::getByday,e.getByday());
					query.eq(InventoryDetailReport::getMsku,e.getMsku());
					query.eq(InventoryDetailReport::getEventType,e.getEventType());
					query.eq(InventoryDetailReport::getReferenceID,e.getReferenceID());
					query.eq(InventoryDetailReport::getFulfillmentCenter, e.getFulfillmentCenter());
					InventoryDetailReport old = inventoryDetailReportMapper.selectOne(query);
					if(old==null) {
						inventoryDetailReportMapper.insert(e);
					}else {
						e.setId(old.getId());
						inventoryDetailReportMapper.updateById(e);
					}
				}
				lineNumber++;
			}
		} catch (IOException e) {
			log.info("ReportAmzInventoryReceiptsService:"+e.getMessage());
			e.printStackTrace();
		}  finally {
				if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				}
				//同步item的authority id,marketplace id,receive date
				shipInboundItemMapper.updateByInventoryDetail(amazonAuthority.getId());
				Calendar c=Calendar.getInstance();
				c.add(Calendar.DATE, -380);
				LambdaQueryWrapper<InventoryDetailReport> query=new LambdaQueryWrapper<InventoryDetailReport>();
				query.eq(InventoryDetailReport::getAuthid, amazonAuthority.getId());
				query.lt(InventoryDetailReport::getByday,GeneralUtil.formatDate(c.getTime()));
			    inventoryDetailReportMapper.delete(query);
		}
      return mlog;
	
	}
	
	

}
