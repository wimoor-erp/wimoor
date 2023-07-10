package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.ReportsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.CreateReportResponse;
import com.amazon.spapi.model.reports.CreateReportSpecification;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inventory.mapper.InventoryHealthMapper;
import com.wimoor.amazon.inventory.pojo.entity.InventoryHealth;
import com.wimoor.amazon.report.service.IInventoryHealthService;
import com.wimoor.amazon.util.AmzDateUtils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
 

@Service("reportInventoryHealthListService")
public class ReportInventoryHealthListServiceImpl extends ReportServiceImpl implements IInventoryHealthService{
	
	@Resource
	private InventoryHealthMapper inventoryHealthMapper;
    @Autowired
	IMarketplaceService iMarketplaceService;
    
	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
		  ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
		  List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		  for(Marketplace market:marketlist) {
			  CreateReportSpecification body=new CreateReportSpecification();
			  body.setReportType(myReportType());
			  body.setDataStartTime(AmzDateUtils.getOffsetDateTimeUTC(cstart));
			  body.setDataEndTime(AmzDateUtils.getOffsetDateTimeUTC(cend));
			  List<String> list=new ArrayList<String>();
			  list.add(market.getMarketplaceid());
			  body.setMarketplaceIds(list);
			  try {
					  ApiCallback<CreateReportResponse> response = new ApiCallbackReportCreate(this,amazonAuthority,market,cstart.getTime(),cend.getTime());
				      api.createReportAsync(body,response);
				  } catch (ApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			    }
			  if(market.getRegion().equals("EU")) {
				  break;
			  }
		  }
}
	@Override
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br) {
		StringBuffer log = new StringBuffer();
		if(amazonAuthority.getShopId()==null) {
			return null;
		}
		/*
		 * 1snapshot_date 2sku 3fnsku 4asin 5product_name 6condition 7sales_rank 8product_group
		 * 9 total_quantity 10 sellable_quantity 11 unsellable_quantity 12 inv_age_0_to_90_days
		 * 13 inv_age_91_to_180_days 14 inv_age_181_to_270_days 15 inv_age_271_to_365_days
		 * 16 inv_age_365_plus_days 17 units_shipped_last_24_hrs 18 units_shipped_last_7_days
		 * 19 units_shipped_last_30_days 20 units_shipped_last_90_days
		 * 21 units_shipped_last_180_days 22 units_shipped_last_365_days
		 * 23 weeks_of_cover_t7 24 weeks_of_cover_t30 25 weeks_of_cover_t90
		 * 26 weeks_of_cover_t180 27 weeks_of_cover_t365 28 num_afn_new_sellers
		 * 29 num_afn_used_sellers 30 currency 31 your_price 32 sales_price
		 * 33 lowest_afn_new_price 34 lowest_afn_used_price 35 lowest_mfn_new_price
		 * 36 lowest_mfn_used_price 37 qty_to_be_charged_ltsf_12_mo
		 * 38 qty_in_long_term_storage_program 39 qty_with_removals_in_progress
		 * 40 projected_ltsf_12_mo 41 per_unit_volume 42 is_hazmat 43 in_bound_quantity
		 * 44 asin_limit 45 inbound_recommend_quantity 46 qty_to_be_charged_ltsf_6_mo
		 * 47 projected_ltsf_6_mo
		 */
		        Date opttime=new Date();
				int lineNumber = 0;
				String line;
				String asin = null;
				String sku = null;
				String snapshot_date=null;
				String fnsku=null;
				String name=null;
				String condition=null;
				String sales_rank=null;
				String product_group=null;
				String total_quantity=null;
				String sellable_quantity=null;
				String unsellable_quantity=null;
				String inv_age_0_to_90_days=null;
				String inv_age_91_to_180_days=null;
				String inv_age_181_to_270_days=null;
				String inv_age_271_to_365_days=null;
				String inv_age_365_plus_days=null;
				String units_shipped_last_24_hrs=null;
				String units_shipped_last_7_days=null;
				String units_shipped_last_30_days=null;
				String units_shipped_last_90_days=null;
				String units_shipped_last_180_days=null;
				String units_shipped_last_365_days=null;
				String weeks_of_cover_t7=null;
				String weeks_of_cover_t30=null;
				String weeks_of_cover_t90=null;
				String weeks_of_cover_t180=null;
				String weeks_of_cover_t365=null;
				String num_afn_new_sellers=null;
				String num_afn_used_sellers=null;
				String currency=null;
				String your_price=null;
				String sales_price=null;
				String lowest_afn_new_price=null;
				String lowest_afn_used_price=null;
				String lowest_mfn_new_price=null;
				String lowest_mfn_used_price=null;
				String qty_to_be_charged_ltsf_12_mo=null;
				String qty_in_long_term_storage_program=null;
				String qty_with_removals_in_progress=null;
				String projected_ltsf_12_mo=null;
				String per_unit_volume=null;
				String is_hazmat=null;
				String in_bound_quantity=null;
				String asin_limit=null;
				String inbound_recommend_quantity=null;
				String qty_to_be_charged_ltsf_6_mo=null;
				String projected_ltsf_6_mo=null;
				try {
					while ((line = br.readLine()) != null) {
						String[] info = line.split("\t");
						if (lineNumber != 0) {
							if(lineNumber==1){
							    QueryWrapper<InventoryHealth> query=new QueryWrapper<InventoryHealth>();
							    query.eq("shopid",amazonAuthority.getShopId());
							    query.eq("authid",amazonAuthority.getId());
								inventoryHealthMapper.delete(query);
							}
							sku = info[1];
							if(sku!=null && sku.length()>50){
								sku = sku.substring(0, 50);
							}
							asin = info[3];
							//insert  插入记录
							InventoryHealth newhealth=new InventoryHealth();
							snapshot_date=info[0];fnsku=info[2];name=info[4];
							condition=info[5];
							if(condition.contains("No Listing")||condition.contains("Unknown"))continue;
							sales_rank=info[6];
							product_group=info[7];
							total_quantity=info[8];sellable_quantity=info[9];unsellable_quantity=info[10];
							inv_age_0_to_90_days=info[11];inv_age_91_to_180_days=info[12];inv_age_181_to_270_days=info[13];
							inv_age_271_to_365_days=info[14];inv_age_365_plus_days=info[15];units_shipped_last_24_hrs=info[16];
							units_shipped_last_7_days=info[17];units_shipped_last_30_days=info[18];units_shipped_last_90_days=info[19];
							units_shipped_last_180_days=info[20];units_shipped_last_365_days=info[21];weeks_of_cover_t7=info[22];
							weeks_of_cover_t30=info[23];weeks_of_cover_t90=info[24];weeks_of_cover_t180=info[25];
							weeks_of_cover_t365=info[26];num_afn_new_sellers=info[27];num_afn_used_sellers=info[28];
							currency=info[29];your_price=info[30];sales_price=info[31];
							lowest_afn_new_price=info[32];lowest_afn_used_price=info[33];lowest_mfn_new_price=info[34];
							lowest_mfn_used_price=info[35];qty_to_be_charged_ltsf_12_mo=info[36];qty_in_long_term_storage_program=info[37];
							qty_with_removals_in_progress=info[38];projected_ltsf_12_mo=info[39];per_unit_volume=info[40];
							is_hazmat=info[41];in_bound_quantity=info[42];asin_limit=info[43];
							inbound_recommend_quantity=info[44];
							if(info.length>45){
								qty_to_be_charged_ltsf_6_mo=info[45];
								projected_ltsf_6_mo=info[46];
							}
							String marketplaceid = null;
							if(StrUtil.isNotEmpty(currency)){
								marketplaceid = iMarketplaceService.getMarketPlaceId(currency);
							}
							if(marketplaceid==null){
								marketplaceid = amazonAuthority.getMarketPlace().getMarketplaceid();
							}
							newhealth.setShopid(amazonAuthority.getShopId());
							newhealth.setMarketplaceid(marketplaceid);
							newhealth.setAuthid(amazonAuthority.getId());
							newhealth.setAsin(asin);newhealth.setAsinLimit(asin_limit);newhealth.setFcondition(condition);newhealth.setShopid(amazonAuthority.getShopId());
							newhealth.setCurrency(currency);newhealth.setFnsku(fnsku);
							newhealth.setInBoundQuantity(in_bound_quantity);newhealth.setInboundRecommendQuantity(inbound_recommend_quantity);
							newhealth.setInvAge0to90days(Integer.parseInt(inv_age_0_to_90_days));newhealth.setInvAge91to180days(Integer.parseInt(inv_age_91_to_180_days));
							newhealth.setInvAge181to270days(Integer.parseInt(inv_age_181_to_270_days));newhealth.setInvAge271to365days(Integer.parseInt(inv_age_271_to_365_days));
							newhealth.setInvAge365plusdays(Integer.parseInt(inv_age_365_plus_days));newhealth.setIsHazmat(is_hazmat);
							newhealth.setLowestAfnNewPrice(lowest_afn_new_price);newhealth.setLowestAfnUsedPrice(lowest_afn_used_price);
							newhealth.setLowestMfnNewPrice(lowest_mfn_new_price);newhealth.setLowestMfnUsedPrice(lowest_mfn_used_price);
							newhealth.setName(name);newhealth.setNumAfnNewSellers(num_afn_new_sellers);newhealth.setNumAfnUsedSellers(num_afn_used_sellers);
							newhealth.setPerUnitVolume(per_unit_volume);newhealth.setProductGroup(product_group);
							newhealth.setProjectedLtsf12Mo(projected_ltsf_12_mo);newhealth.setProjectedLtsf6mo(projected_ltsf_6_mo);
							newhealth.setQtyInLongTermStorageProgram(qty_in_long_term_storage_program);newhealth.setQtyToBeChargedLtsf12mo(qty_to_be_charged_ltsf_12_mo);
							newhealth.setQtyToBeChargedLtsf6mo(qty_to_be_charged_ltsf_6_mo);newhealth.setQtyWithRemovalsInProgress(qty_with_removals_in_progress);
							newhealth.setUnitsShippedLast180days(units_shipped_last_180_days);newhealth.setUnitsShippedLast24hrs(units_shipped_last_24_hrs);
							newhealth.setUnitsShippedLast30days(units_shipped_last_30_days);newhealth.setUnitsShippedLast365days(units_shipped_last_365_days);
							newhealth.setUnitsShippedLast7days(units_shipped_last_7_days);newhealth.setUnitsShippedLast90days(units_shipped_last_90_days);
							newhealth.setUnsellableQuantity(unsellable_quantity);newhealth.setWeeksOfCoverT180(weeks_of_cover_t180);
							newhealth.setWeeksOfCoverT30(weeks_of_cover_t30);newhealth.setWeeksOfCoverT365(weeks_of_cover_t365);
							newhealth.setWeeksOfCoverT7(weeks_of_cover_t7);newhealth.setWeeksOfCoverT90(weeks_of_cover_t90);
							newhealth.setYourPrice(your_price);newhealth.setSalesRank(sales_rank);newhealth.setTotalQuantity(total_quantity);
							newhealth.setSku(sku);newhealth.setSalesPrice(sales_price);newhealth.setSellableQuantity(sellable_quantity);
							newhealth.setAsinLimit(asin_limit);newhealth.setSnapshotDate(DateUtil.parse(snapshot_date));
							newhealth.setOpttime(opttime);
				            try {
				            	QueryWrapper<InventoryHealth> query=new QueryWrapper<InventoryHealth>();
				            	query.eq("authid", newhealth.getAuthid());
				            	query.eq("marketplaceid", newhealth.getMarketplaceid());
				            	query.eq("sku", newhealth.getSku());
								InventoryHealth oldone = inventoryHealthMapper.selectOne(query);
								if(oldone!=null) {
									inventoryHealthMapper.update(newhealth, query);
								}else {
									inventoryHealthMapper.insert(newhealth);
								}
							} catch (Exception e) {
								e.printStackTrace();
								log.append("SKU:"+sku+","+e.getMessage());
							}
				            newhealth=null;
						}
						info=null;
						lineNumber++;
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
			}
				return log.toString();

	}

	@Override
	public String myReportType() {
		return "GET_FBA_FULFILLMENT_INVENTORY_HEALTH_DATA";
	}
 

	 
	
}
