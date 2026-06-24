package com.wimoor.amazon.report.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amazon.spapi.model.reports.CreateReportSpecification;
import com.amazon.spapi.model.reports.ReportOptions;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.product.pojo.entity.AmzProductPageviews;
import com.wimoor.amazon.product.service.IAmzProductPageviewsService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
 
 
@Slf4j
@Service("reportAmzSalesAndTrafficBusinessService")
public class ReportAmzSalesAndTrafficBusinessServiceImpl extends ReportServiceImpl{
    @Autowired
    IAmzProductPageviewsService iAmzProductPageviewsService;

	public  void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
		  List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		    Calendar myStart=Calendar.getInstance();
			myStart.setTime(cstart.getTime());
			myStart.set(Calendar.HOUR_OF_DAY, 0);
			myStart.set(Calendar.MINUTE, 0);
			myStart.set(Calendar.SECOND, 0);
		    Calendar myEnd=Calendar.getInstance();
			myEnd.setTime(myStart.getTime());
			myEnd.set(Calendar.HOUR_OF_DAY, 23);
			myEnd.set(Calendar.MINUTE, 59);
			myEnd.set(Calendar.SECOND, 59);
		  for(Marketplace market:marketlist) {
			  CreateReportSpecification body=new CreateReportSpecification();
			  body.setReportType(myReportType());
			  body.setDataStartTime(AmzDateUtils.getOffsetDateTimeUTC(myStart));
			  body.setDataEndTime(AmzDateUtils.getOffsetDateTimeUTC(myEnd));
			  body.setDataStartTime(body.getDataEndTime());
			  ReportOptions reportOptions=getMyOptions();
			  if(reportOptions!=null) {
				body.setReportOptions(reportOptions);  
			  }
			  List<String> list=new ArrayList<String>();
			  list.add(market.getMarketplaceid());
			  amazonAuthority.setMarketPlace(market);
			  if(ignore==null|| !ignore) {
				  Map<String,Object> param=new HashMap<String,Object>();
				  param.put("sellerid", amazonAuthority.getSellerid());
				  param.put("reporttype", this.myReportType());
				  param.put("marketplacelist", list);
				  param.put("starttime", GeneralUtil.formatDate(myStart.getTime()));
				  Date lastupdate= iReportRequestRecordService.lastUpdateRequestByType(param);  
				  if(lastupdate!=null&&GeneralUtil.distanceOfHour(lastupdate, new Date())<6) {
					  continue;
				  }
			  }
			  body.setMarketplaceIds(list);
			  callCreateAPI(this, body, amazonAuthority, market,myStart.getTime(),myEnd.getTime());
			  list.clear();
		  }
}
	@SuppressWarnings("unused")
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)  {
		String mlog="";
		String line;
		//int lineNumber=0;
		try {
			while ((line = br.readLine()) != null) {
				JSONObject json = GeneralUtil.getJsonObject(line);
				JSONArray salesAndTrafficByAsin = json.getJSONArray("salesAndTrafficByAsin");
				JSONObject reportSpecification = json.getJSONObject("reportSpecification");
				String day=reportSpecification.getString("dataStartTime");
				JSONArray salesAndTrafficByDate = json.getJSONArray("salesAndTrafficByDate");
				JSONArray marketplaceIds = reportSpecification.getJSONArray("marketplaceIds");
				String marketplaceid=marketplaceIds.getString(0);
				List<AmzProductPageviews> list=new LinkedList<AmzProductPageviews>();
				for(int i=0;i<salesAndTrafficByAsin.size();i++) {
					JSONObject itemjson = salesAndTrafficByAsin.getJSONObject(i);
					String parentAsin=itemjson.getString("parentAsin");
					String childAsin=itemjson.getString("childAsin");
					String sku=itemjson.getString("sku");
					JSONObject salesByAsin=itemjson.getJSONObject("salesByAsin");
					JSONObject trafficByAsin=itemjson.getJSONObject("trafficByAsin");
					Integer unitsOrdered = salesByAsin.getInteger("unitsOrdered");
					Integer unitsOrderedB2B = salesByAsin.getInteger("unitsOrderedB2B");
					Integer totalOrderItems = salesByAsin.getInteger("totalOrderItems");
					Integer totalOrderItemsB2B = salesByAsin.getInteger("totalOrderItemsB2B");
					JSONObject orderedProductSalesJson=salesByAsin.getJSONObject("orderedProductSales");
					BigDecimal orderedProductSales = orderedProductSalesJson.getBigDecimal("amount");
					JSONObject orderedProductSalesB2BJson=salesByAsin.getJSONObject("orderedProductSalesB2B");
					BigDecimal orderedProductSalesB2B =new BigDecimal("0");
					if(orderedProductSalesB2BJson!=null) {
						orderedProductSalesB2B=orderedProductSalesB2BJson.getBigDecimal("amount");
					}
					Integer browserSessions=trafficByAsin.getInteger("browserSessions");
					Integer mobileAppSessions=trafficByAsin.getInteger("mobileAppSessions");
					Integer sessions=trafficByAsin.getInteger("sessions");
					Integer browserPageViews=trafficByAsin.getInteger("browserPageViews");
					Integer mobileAppPageViews=trafficByAsin.getInteger("mobileAppPageViews");
					Integer mypageViews=trafficByAsin.getInteger("pageViews");
					BigDecimal browserSessionPercentage=trafficByAsin.getBigDecimal("browserSessionPercentage");
					BigDecimal mobileAppSessionPercentage=trafficByAsin.getBigDecimal("mobileAppSessionPercentage");
					BigDecimal sessionPercentage=trafficByAsin.getBigDecimal("sessionPercentage");
					BigDecimal browserPageViewsPercentage=trafficByAsin.getBigDecimal("browserPageViewsPercentage");
					BigDecimal mobileAppPageViewsPercentage=trafficByAsin.getBigDecimal("mobileAppPageViewsPercentage");
					BigDecimal pageViewsPercentage=trafficByAsin.getBigDecimal("pageViewsPercentage");
					BigDecimal unitSessionPercentage=trafficByAsin.getBigDecimal("unitSessionPercentage");
					BigDecimal buyBoxPercentage=trafficByAsin.getBigDecimal("buyBoxPercentage");
					BigDecimal unitSessionPercentageB2B=trafficByAsin.getBigDecimal("unitSessionPercentageB2B");
					AmzProductPageviews pageViews=new AmzProductPageviews();
					pageViews.setMarketplaceid(marketplaceid);
					pageViews.setAmazonAuthid(new BigInteger(amazonAuthority.getId()));
					pageViews.setByday(GeneralUtil.getDatez(day));
					pageViews.setChildAsin(childAsin);
					pageViews.setParentAsin(parentAsin);
					pageViews.setSku(sku);
					pageViews.setSessions(sessions);
					pageViews.setSessionPercentage(sessionPercentage);
					pageViews.setPageViews(mypageViews);
					pageViews.setPageViewsPercentage(pageViewsPercentage);
					pageViews.setBuyBoxPercentage(buyBoxPercentage);
					pageViews.setUnitsOrdered(unitsOrdered);
					pageViews.setUnitsOrderedB2b(unitsOrderedB2B);
					pageViews.setUnitSessionPercentage(unitSessionPercentage);
					pageViews.setUnitSessionPercentageB2b(unitSessionPercentageB2B);
					pageViews.setOrderedProductSales(orderedProductSales);
					pageViews.setOrderedProductSalesB2b(orderedProductSalesB2B);
					pageViews.setTotalOrderItems(totalOrderItems);
					pageViews.setTotalOrderItemsB2b(totalOrderItemsB2B);
					pageViews.setOpttime(new Date());
					list.add(pageViews);
				}
				iAmzProductPageviewsService.uploadSessionFile(marketplaceid, amazonAuthority.getSellerid(), day, list);
			}
			System.out.println("ReportAmzSalesAndTrafficBusinessServiceImpl:"+mlog);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("ReportAmzInventoryService:"+e.getMessage());
			e.printStackTrace();
		}  finally {
				if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
	}
      return mlog;
	}
	
 

	@Override
	public String myReportType() {
		return "GET_SALES_AND_TRAFFIC_REPORT";
	}



	@Override
	public ReportOptions getMyOptions() {
		// TODO Auto-generated method stub
		ReportOptions reportOptions=new ReportOptions();
		reportOptions.put("dateGranularity", "DAY");
		reportOptions.put("asinGranularity", "SKU");
		return reportOptions;
	}

 
	
}
