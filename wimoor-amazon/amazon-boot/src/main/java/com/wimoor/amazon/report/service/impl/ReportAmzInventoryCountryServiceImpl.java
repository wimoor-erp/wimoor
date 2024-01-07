package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.inventory.mapper.AmzInventoryCountryReportMapper;
import com.wimoor.amazon.inventory.pojo.entity.AmzInventoryCountryReport;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.report.service.IAmzInventoryCountryReportService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;

import lombok.extern.slf4j.Slf4j;
 
 
@Slf4j
@Service("reportAmzInventoryCountryService")
public class ReportAmzInventoryCountryServiceImpl extends ReportServiceImpl  implements IAmzInventoryCountryReportService {
 
	@Resource
	private AmzInventoryCountryReportMapper amzInventoryCountryReportMapper;
	 
	public  void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
		  ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
		  List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		  boolean iseu=false;
		  for(Marketplace market:marketlist) {
			  CreateReportSpecification body=new CreateReportSpecification();
			  body.setReportType(myReportType());
			  body.setDataStartTime(AmzDateUtils.getOffsetDateTimeUTC(cstart));
			  body.setDataEndTime(AmzDateUtils.getOffsetDateTimeUTC(cend));
			  if(market.getRegion().equals("EU")) {
				  if(iseu) {
					  return;
				  }else {
					  iseu=true;
				  }
			  }else {
				  continue;
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
				  if(lastupdate!=null&&GeneralUtil.distanceOfHour(lastupdate, new Date())<5) {
					  continue;
				  }
			  }
			  body.setMarketplaceIds(list);
			  try {
					  ApiCallback<CreateReportResponse> response = new ApiCallbackReportCreate(this,amazonAuthority,market,cstart.getTime(),cend.getTime());
				      api.createReportAsync(body,response);
				  } catch (ApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			    }
		  }
}
	/**
	 * afn-researching-quantity	afn-reserved-future-supply	afn-future-supply-buyable
	 */
	public synchronized String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)  {
		String mlog="";
		int lineNumber = 0;
		AmzInventoryCountryReport record = null;
		String line;
		try {
			List<AmzInventoryCountryReport> list=new LinkedList<AmzInventoryCountryReport>();
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				int length = info.length;
				if(lineNumber!=0) {
				    record=new AmzInventoryCountryReport();
				    int i = 0;
					String sku = i < length ? info[i++] : null;
					String fnsku = i < length ? info[i++] : null;
					String asin = i < length ? info[i++] : null;
					String condition = i < length ? info[i++] : null;
					String country = i < length ? info[i++] : null;
					String quantity = i < length ? info[i++] : null;
					record.setSku(sku);
					record.setFnsku(fnsku);
					record.setFcondition(condition);
					record.setRefreshtime(new Date());
					record.setAsin(asin);
					record.setCountry(country);
					record.setQuantity(Integer.parseInt(quantity));
					record.setAuthid(new BigInteger(amazonAuthority.getId()));
					if(lineNumber==1) {
						QueryWrapper<AmzInventoryCountryReport> query=new QueryWrapper<AmzInventoryCountryReport>();
						query.eq("authid", record.getAuthid());
						amzInventoryCountryReportMapper.delete(query);
					}
				    list.add(record);
				}
				lineNumber++;
			}
			amzInventoryCountryReportMapper.insertBatch(list);	  
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
		return  ReportType.InventoryByCountry;
	}

 
	
}
