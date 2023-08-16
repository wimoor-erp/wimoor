package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.wimoor.amazon.inventory.mapper.InventoryReservedReportMapper;
import com.wimoor.amazon.inventory.pojo.entity.InventoryReservedReport;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;

 

@Service("reportAmzReservedInventoryService")
public class ReportAmzReservedInventoryServiceImpl extends ReportServiceImpl{

	@Resource
	private InventoryReservedReportMapper inventoryReservedReportMapper;
	
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
	
	public String treatResponse(AmazonAuthority amazonAuthority,BufferedReader br)   {
		String mlog="";
		int lineNumber = 0;
		String line;
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				int length = info.length;
				if (lineNumber != 0) {//第一行为空
					if(lineNumber==1){
						QueryWrapper<InventoryReservedReport> query = new QueryWrapper<InventoryReservedReport>();
						query.eq("amazonAuthId",amazonAuthority.getId());
						if ("EU".equals(amazonAuthority.getMarketPlace().getRegion())) {
							query.eq("marketplaceid","EU");
						}else {
							query.eq("marketplaceid",amazonAuthority.getMarketPlace().getMarketplaceid());
						}
						inventoryReservedReportMapper.delete(query);
					    } 
					InventoryReservedReport record = new InventoryReservedReport();
					int i = 0;
					String sku = i < length ? info[i++] : null;
					if(sku!=null && sku.length()>50){
						sku = sku.substring(0, 50);
					}
					String fnsku = i < length ? info[i++] : null;
					String asin = i < length ? info[i++] : null;
					@SuppressWarnings("unused")
					String product_name = i < length ? info[i++] : null;
					String reserved_qty = i < length ? info[i++] : null;
					String reserved_customerorders = i < length ? info[i++] : null;
					String reserved_fc_transfers = i < length ? info[i++] : null;
					String reserved_fc_processing = i < length ? info[i++] : null;
					record.setSku(sku);
					record.setFnsku(fnsku);
					record.setAsin(asin);
					record.setReservedQty(GeneralUtil.getInteger(reserved_qty));
					record.setReservedCustomerorders(GeneralUtil.getInteger(reserved_customerorders));
					record.setReservedFcTransfers(GeneralUtil.getInteger(reserved_fc_transfers));
					record.setReservedFcProcessing(GeneralUtil.getInteger(reserved_fc_processing));
					record.setAmazonAuthId(amazonAuthority.getId());
					if ("EU".equals(amazonAuthority.getMarketPlace().getRegion())) {
						record.setMarketplaceid(amazonAuthority.getMarketPlace().getRegion());
					} else {
						record.setMarketplaceid(amazonAuthority.getMarketPlace().getMarketplaceid());
					}
					record.setByday(amazonAuthority.getCaptureDateTime());
					try {
						QueryWrapper<InventoryReservedReport> query=new QueryWrapper<InventoryReservedReport>();
						query.eq("amazonAuthId", record.getAmazonAuthId());
						query.eq("marketplaceid", record.getMarketplaceid());
						query.eq("sku", record.getSku());
						InventoryReservedReport oldone = inventoryReservedReportMapper.selectOne(query);
						if(oldone!=null) {
							inventoryReservedReportMapper.update(record, query);
						}else {
							inventoryReservedReportMapper.insert(record);
						}
					} catch (Exception e) {
						e.printStackTrace();
						mlog+="SKU:"+sku+","+e.getMessage();
					}finally {
						record=null;
					}
				}
				info=null;
				lineNumber++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
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
		return ReportType.ReservedInventoryReport;
	}

 
	
}
