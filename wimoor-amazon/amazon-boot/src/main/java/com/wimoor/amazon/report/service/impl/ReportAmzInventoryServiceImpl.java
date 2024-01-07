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
import com.wimoor.amazon.inventory.mapper.InventoryReportMapper;
import com.wimoor.amazon.inventory.pojo.entity.InventoryReport;
import com.wimoor.amazon.product.service.IProductInOptService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
 
 
@Slf4j
@Service("reportAmzInventoryService")
public class ReportAmzInventoryServiceImpl extends ReportServiceImpl{
 
	@Resource
	private IProductInfoService iProductInfoService;
	@Resource
	private InventoryReportMapper inventoryReportMapper;
	@Resource
	private IProductInOptService iProductInOptService;
	 
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
				  if(lastupdate!=null&&GeneralUtil.distanceOfHour(lastupdate, new Date())<16) {
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
		InventoryReport record = null;
		String line;
		boolean hasremote=false;
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				int length = info.length;
				if(lineNumber==0) {
					if(line.contains("afn-fulfillable-quantity-remote")) {
						hasremote=true;
					}
				}
				if (lineNumber != 0) {
					record = new InventoryReport();
					int i = 0;
					String sku = i < length ? info[i++] : null;
					if(sku!=null && sku.length()>50){
						sku = sku.substring(0, 50);
					}
					String fnsku = i < length ? info[i++] : null;
					String asin = i < length ? info[i++] : null;
					@SuppressWarnings("unused")
					String product_name = i < length ? info[i++] : null;
					String condition = i < length ? info[i++] : null;
					String your_price = i < length ? info[i++] : null;
					String mfn_listing_exists = i < length ? info[i++] : null;
					String mfn_fulfillable_quantity = i < length ? info[i++] : null;
					String afn_listing_exists = i < length ? info[i++] : null;
					String afn_warehouse_quantity = i < length ? info[i++] : null;
					String afn_fulfillable_quantity = i < length ? info[i++] : null;
					String afn_unsellable_quantity = i < length ? info[i++] : null;
					String afn_reserved_quantity = i < length ? info[i++] : null;
					String afn_total_quantity = i < length ? info[i++] : null;
					String per_unit_volume = i < length ? info[i++] : null;
					String afn_inbound_working_quantity = i < length ? info[i++] : null;
					String afn_inbound_shipped_quantity = i < length ? info[i++] : null;
					String afn_inbound_receiving_quantity = i < length ? info[i++] : null;
					String afn_researching_quantity = i < length ? info[i++] : null;
					String afn_reserved_future_supply = i < length ? info[i++] : null;
					String afn_future_supply_buyable = i < length ? info[i++] : null;
					String afn_fulfillable_quantity_remote=hasremote && i < length ? info[i++] : null;
					if(StrUtil.isNotBlank(afn_fulfillable_quantity_remote)) {
						//log.info(afn_fulfillable_quantity_remote);
					}
					record.setAsin(asin);
					record.setSku(sku);
					record.setFnsku(fnsku);
					if(condition.length()>19)condition.substring(0, 18);
					record.setPcondition(condition);
					if(StrUtil.isEmpty(your_price)||"Unknown".equals(your_price)) {
						if((StrUtil.isEmpty(condition)||"Unknown".equals(condition))&&(afn_total_quantity==null)) {
							lineNumber++;
							continue;
						}
					}
					
					if(condition.contains("No Listing")) {
						lineNumber++;
					    continue;
					}
					record.setYourPrice(GeneralUtil.getBigDecimal(your_price));
					record.setMfnListingExists(mfn_listing_exists);
					record.setMfnFulfillableQuantity(GeneralUtil.getInteger(mfn_fulfillable_quantity));
					record.setAfnListingExists(afn_listing_exists);
					record.setAfnWarehouseQuantity(GeneralUtil.getInteger(afn_warehouse_quantity));
					record.setAfnFulfillableQuantity(GeneralUtil.getInteger(afn_fulfillable_quantity));
					record.setAfnUnsellableQuantity(GeneralUtil.getInteger(afn_unsellable_quantity));
					record.setAfnReservedQuantity(GeneralUtil.getInteger(afn_reserved_quantity));
					record.setAfnTotalQuantity(GeneralUtil.getInteger(afn_total_quantity));
					record.setPerUnitVolume(GeneralUtil.getBigDecimal(per_unit_volume));
					record.setAfnInboundWorkingQuantity(GeneralUtil.getInteger(afn_inbound_working_quantity));
					record.setAfnInboundShippedQuantity(GeneralUtil.getInteger(afn_inbound_shipped_quantity));
					record.setAfnInboundReceivingQuantity(GeneralUtil.getInteger(afn_inbound_receiving_quantity));
					record.setAfnResearchingQuantity(GeneralUtil.getInteger(afn_researching_quantity));
					record.setAfnReservedFutureSupply(GeneralUtil.getInteger(afn_reserved_future_supply));
					record.setAfnFutureSupplyBuyable(GeneralUtil.getInteger(afn_future_supply_buyable));
					record.setAmazonAuthId(amazonAuthority.getId());
					if ("EU".equals(amazonAuthority.getMarketPlace().getRegion())) {
						record.setMarketplaceid(amazonAuthority.getMarketPlace().getRegion());
					} else {
						record.setMarketplaceid(amazonAuthority.getMarketPlace().getMarketplaceid());
					}
					record.setByday(amazonAuthority.getCaptureDateTime());
					try {
						record.setIsnewest(true);
						QueryWrapper<InventoryReport> query=new QueryWrapper<InventoryReport>();
						query.eq("sku", record.getSku());
						query.eq("marketplaceid", record.getMarketplaceid());
						query.eq("amazonAuthId", record.getAmazonAuthId());
						InventoryReport oldone = inventoryReportMapper.selectOne(query);
						if(oldone!=null) {
							inventoryReportMapper.update(record, query);
						}else {
							inventoryReportMapper.insert(record);
						}
						inventoryReportMapper.newestInsert(record);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				lineNumber++;
			}
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
		return "GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA";
	}
	
}
