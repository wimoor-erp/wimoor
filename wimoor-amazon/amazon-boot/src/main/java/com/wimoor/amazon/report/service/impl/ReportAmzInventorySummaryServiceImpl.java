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
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;
import com.amazon.spapi.api.ReportsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.CreateReportResponse;
import com.amazon.spapi.model.reports.CreateReportSpecification;
import com.amazon.spapi.model.reports.ReportOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.inventory.pojo.entity.InventorySummaryReport;
import com.wimoor.amazon.inventory.service.IInventorySummaryReportService;
import com.wimoor.amazon.orders.service.IOrdersFulfilledShipmentsFeeService;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.util.UUIDUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("reportAmzInventorySummaryService")
public class ReportAmzInventorySummaryServiceImpl extends ReportServiceImpl{
	@Autowired
	IInventorySummaryReportService iInventorySummaryReportService;
	@Autowired
	IOrdersFulfilledShipmentsFeeService iOrdersFulfilledShipmentsFeeService;
	@Override
	public String myReportType() {
		return  ReportType.InventorySummaryViewReport;
	}
	
	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
	  amazonAuthority.setUseApi("createReport");
	  ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
	  List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
	  Calendar myday=Calendar.getInstance();
	  myday.setTime(cstart.getTime());
	   Date day = myday.getTime();
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    String str=sdf.format(day);
	    str=str.substring(0,10)+"T00:00:00+00:00";
		OffsetDateTime ofstart = OffsetDateTime.parse(str);
	    String end=str.substring(0,10)+"T23:59:59+00:00";
				OffsetDateTime ofend = OffsetDateTime.parse(end);
	      Marketplace market=marketlist.get(0);
		  CreateReportSpecification body=new CreateReportSpecification();
		  body.setReportType(myReportType());
		  body.setDataStartTime(ofstart);
		  body.setDataEndTime(ofend);
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
				  return;
			  }
		  }
		  body.setMarketplaceIds(list);
		  try {
				  ApiCallback<CreateReportResponse> callback = new ApiCallbackReportCreate(this,amazonAuthority,market,
						  AmzDateUtils.getUTCToDate(ofstart),
						  AmzDateUtils.getUTCToDate(ofend));
			      api.createReportAsync(body,callback);
			  } catch (ApiException e) {
				  amazonAuthority.setApiRateLimit( null,e);
				  e.printStackTrace();
		    }
}
	
	@Override
	public ReportOptions getMyOptions() {
		ReportOptions option=new ReportOptions();
		option.put("aggregatedByTimePeriod", "DAILY");
		//option.put("eventType", "Shipments");
		//option.put("aggregateByLocation", "COUNTRY");
		return  option;
	}
	 
	
	@Override
	public synchronized String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br) {
//		Date
//		FNSKU
//		ASIN
//		MSKU
//		Title
//		Disposition
//		StartingWarehouseBalance
//		InTransitBetweenWarehouses
//		Receipts
//		CustomerShipments
//		CustomerReturns
//		VendorReturns
//		WarehouseTransferIn/Out
//		Found
//		Lost
//		Damaged
//		Disposed
//		OtherEvents
//		EndingWarehouseBalance
//		UnknownEvents
//		Location
		String mlog="";
		int lineNumber = 0;
		String line;
		Date nowdate =null;
		Map<String,Integer> titleList = new HashMap<String,Integer>();
		List<InventorySummaryReport> itemlist=new LinkedList<InventorySummaryReport>();
		try {
			SimpleDateFormat sdf6 = new SimpleDateFormat("MM/dd/yyyy");
			while ((line = br.readLine()) != null) {
				String[] info = line.replace("\"", "").split("\t");
				int length = info.length;
				if (lineNumber == 0) {
					for (int i = 0; i < length; i++) {
						titleList.put(info[i].toLowerCase(),i);
					}
				}else {
					InventorySummaryReport e=new InventorySummaryReport();
					try {
						e.setByday(sdf6.parse(GeneralUtil.getStrValue(info, titleList, "date")));
					} catch (ParseException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					if(nowdate==null||!nowdate.equals(e.getByday())) {
						nowdate = e.getByday();
						LambdaQueryWrapper<InventorySummaryReport> delqueryWrapper=new LambdaQueryWrapper<InventorySummaryReport>();
						delqueryWrapper.eq(InventorySummaryReport::getByday, nowdate);
						delqueryWrapper.eq(InventorySummaryReport::getAuthid, amazonAuthority.getId());
						//delete refresh data
						iInventorySummaryReportService.remove(delqueryWrapper);
						
						//delete old data
						Calendar c =Calendar.getInstance();
						c.setTime(nowdate);
						c.add(Calendar.DATE, -400);
						LambdaQueryWrapper<InventorySummaryReport> delqueryWrapperold=new LambdaQueryWrapper<InventorySummaryReport>();
						delqueryWrapperold.eq(InventorySummaryReport::getByday, GeneralUtil.formatDate(c.getTime()));
						delqueryWrapperold.eq(InventorySummaryReport::getAuthid, amazonAuthority.getId());
						iInventorySummaryReportService.remove(delqueryWrapperold);
					}
					e.setFnsku(GeneralUtil.getStrValue(info, titleList, "fnsku"));
					e.setAsin(GeneralUtil.getStrValue(info, titleList, "asin"));
					e.setMsku(GeneralUtil.getStrValue(info, titleList, "msku"));
					e.setDisposition(GeneralUtil.getStrValue(info, titleList, "disposition"));
					e.setStartingWarehouseBalance(GeneralUtil.getIntegerValue(info, titleList, "starting warehouse balance"));
					e.setInTransitBetweenWarehouses(GeneralUtil.getStrValue(info, titleList, "in transit between warehouses"));
					e.setReceipts(GeneralUtil.getIntegerValue(info, titleList, "receipts"));
					e.setCustomerShipments(GeneralUtil.getIntegerValue(info, titleList, "customer shipments"));
					e.setCustomerReturns(GeneralUtil.getIntegerValue(info, titleList, "customer returns"));
					e.setVendorReturns(GeneralUtil.getIntegerValue(info, titleList, "vendor returns"));
					e.setWarehouseTransferInOut(GeneralUtil.getIntegerValue(info, titleList, "warehouse transfer in/out"));
					e.setFound(GeneralUtil.getIntegerValue(info, titleList, "found"));
					e.setLost(GeneralUtil.getIntegerValue(info, titleList, "lost"));
					e.setDamaged(GeneralUtil.getIntegerValue(info, titleList, "damaged"));
					e.setDisposed(GeneralUtil.getIntegerValue(info, titleList, "disposed"));
					e.setOtherEvents(GeneralUtil.getStrValue(info, titleList, "other events"));
					e.setEndingWarehouseBalance(GeneralUtil.getIntegerValue(info, titleList, "ending warehouse balance"));
					e.setUnknownEvents(GeneralUtil.getStrValue(info, titleList, "unknown events"));
					e.setLocation(GeneralUtil.getStrValue(info, titleList, "location"));
					e.setAuthid(amazonAuthority.getId());
					e.setId(UUIDUtil.getUUIDshort());
					if(e.getByday()!=null) {
						itemlist.add(e);
						if(itemlist.size()>=1000) {
							iInventorySummaryReportService.saveBatch(itemlist);
							itemlist.clear();
						}
						
					}
				}
				lineNumber++;
			}
			if(itemlist.size()>0) {
				iInventorySummaryReportService.saveBatch(itemlist);
			}
		} catch (IOException e) {
			log.info("reportAmzInventorySummaryService:"+e.getMessage());
			e.printStackTrace();
		}  finally {
				if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				}
				new Thread(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						iOrdersFulfilledShipmentsFeeService.orderTransFee(amazonAuthority.getId());
					}}).start();
		}
      return mlog;
	
	}
	
	

}
