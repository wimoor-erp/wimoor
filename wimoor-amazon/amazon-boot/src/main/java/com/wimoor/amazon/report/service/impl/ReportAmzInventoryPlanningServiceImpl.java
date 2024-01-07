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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.inventory.mapper.AmzInventoryPlanningMapper;
import com.wimoor.amazon.inventory.pojo.entity.AmzInventoryPlanning;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
 
 
@Slf4j
@Service("reportAmzInventoryPlanningService")
public class ReportAmzInventoryPlanningServiceImpl extends ReportServiceImpl{
  
	@Resource
	private AmzInventoryPlanningMapper amzInventoryPlanningMapper;
	 
	 
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
				  if(lastupdate!=null&&GeneralUtil.distanceOfDay(lastupdate, new Date())<5) {
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
	 * snapshot-date	
	 * sku	fnsku	asin	
	 * product-name	condition	available	pending-removal-quantity	
	 * inv-age-0-to-90-days	inv-age-91-to-180-days	inv-age-181-to-270-days	
	 * inv-age-271-to-365-days	inv-age-365-plus-days	currency	
	 * qty-to-be-charged-ltsf-11-mo	projected-ltsf-11-mo	
	 * qty-to-be-charged-ltsf-12-mo	estimated-ltsf-next-charge	
	 * units-shipped-t7	units-shipped-t30	units-shipped-t60	
	 * units-shipped-t90	alert	your-price	sales-price	lowest-price-new-plus-shipping	
	 * lowest-price-used	recommended-action	healthy-inventory-level	
	 * recommended-sales-price	recommended-sale-duration-days	
	 * recommended-removal-quantity	estimated-cost-savings-of-recommended-actions	
	 * sell-through	item-volume	volume-unit-measurement	storage-type	
	 * storage-volume	marketplace	product-group	sales-rank	
	 * days-of-supply	estimated-excess-quantity	weeks-of-cover-t30	weeks-of-cover-t90	
	 * featuredoffer-price	sales-shipped-last-7-days	
	 * sales-shipped-last-30-days	sales-shipped-last-60-days	
	 * sales-shipped-last-90-days	
	 * inv-age-0-to-30-days	inv-age-31-to-60-days	inv-age-61-to-90-days	
	 * inv-age-181-to-330-days	inv-age-331-to-365-days	estimated-storage-cost-next-month	
	 * inbound-quantity	inbound-working	inbound-shipped	inbound-received	
	 * no-sale-last-6-months	reserved-quantity	unfulfillable-quantity
	 * afn-researching-quantity	afn-reserved-future-supply	afn-future-supply-buyable
	 */
	public synchronized String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)  {
		String mlog="";
		int lineNumber = 0;
		String line;
		Map<String,Integer> titleList = new HashMap<String,Integer>();
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				int length = info.length;
				if (lineNumber == 0) {
					for (int i = 0; i < length; i++) {
						titleList.put(info[i].toLowerCase(),i);
					}
				} else {
					AmzInventoryPlanning e=new AmzInventoryPlanning();
					e.setSku(GeneralUtil.getStrValue(info, titleList, "sku"));
					e.setSnapshotDate(GeneralUtil.getDatez(GeneralUtil.getStrValue(info, titleList, "snapshot-date")));
					e.setAmazonauthid(amazonAuthority.getId());
					e.setAlert(GeneralUtil.getStrValue(info, titleList, "alert"));
					e.setAsin(GeneralUtil.getStrValue(info, titleList, "asin"));
					e.setFnsku(GeneralUtil.getStrValue(info, titleList, "fnsku"));
					e.setCondition(GeneralUtil.getStrValue(info, titleList, "condition"));
					e.setCountrycode(GeneralUtil.getStrValue(info, titleList, "marketplace"));
					e.setAvailable(GeneralUtil.getIntegerValue(info, titleList, "available"));
					e.setCurrency(GeneralUtil.getStrValue(info, titleList, "currency"));
					e.setPendingRemovalQuantity(GeneralUtil.getIntegerValue(info, titleList, "pending-removal-quantity"));
					e.setInvAge0To90Days(GeneralUtil.getIntegerValue(info, titleList, "inv-age-0-to-90-days"));
					e.setInvAge91To180Days(GeneralUtil.getIntegerValue(info, titleList, "inv-age-91-to-180-days"));	
					e.setInvAge181To270Days(GeneralUtil.getIntegerValue(info, titleList, "inv-age-181-to-270-days"));
					e.setInvAge271To365Days(GeneralUtil.getIntegerValue(info, titleList, "inv-age-271-to-365-days"));
					e.setInvAge365PlusDays(GeneralUtil.getIntegerValue(info, titleList, "inv-age-365-plus-days"));
					e.setQtyToBeChargedLtsf11Mo(GeneralUtil.getBigDecimalValue(info, titleList, "qty-to-be-charged-ltsf-11-mo"));
					e.setProjectedLtsf11Mo(GeneralUtil.getBigDecimalValue(info, titleList, "projected-ltsf-11-mo"));
					e.setQtyToBeChargedLtsf12Mo(GeneralUtil.getBigDecimalValue(info, titleList, "qty-to-be-charged-ltsf-12-mo"));
					e.setEstimatedLtsfNextCharge(GeneralUtil.getBigDecimalValue(info, titleList, "estimated-ltsf-next-charge"));
					e.setUnitsShippedT7(GeneralUtil.getIntegerValue(info, titleList, "units-shipped-t7"));
					e.setUnitsShippedT30(GeneralUtil.getIntegerValue(info, titleList, "units-shipped-t30"));
					e.setUnitsShippedT60(GeneralUtil.getIntegerValue(info, titleList, "units-shipped-t60"));
					e.setUnitsShippedT90(GeneralUtil.getIntegerValue(info, titleList, "units-shipped-t90"));
					e.setYourPrice(GeneralUtil.getBigDecimalValue(info, titleList, "your-price"));
					e.setSalesPrice(GeneralUtil.getBigDecimalValue(info, titleList, "sales-price"));
					e.setLowestPriceNewPlusShipping(GeneralUtil.getBigDecimalValue(info, titleList, "lowest-price-new-plus-shipping"));
					e.setLowestPriceUsed(GeneralUtil.getBigDecimalValue(info, titleList, "lowest-price-used"));
					e.setRecommendedAction(GeneralUtil.getStrValue(info, titleList, "recommended-action"));
					e.setHealthyInventoryLevel(GeneralUtil.getIntegerValue(info, titleList, "healthy-inventory-level"));
					e.setRecommendedSalesPrice(GeneralUtil.getBigDecimalValue(info, titleList, "recommended-sales-price"));
					e.setRecommendedSaleDurationDays(GeneralUtil.getIntegerValue(info, titleList, "recommended-sale-duration-days"));
					e.setRecommendedRemovalQuantity(GeneralUtil.getIntegerValue(info, titleList, "recommended-removal-quantity"));
					e.setEstimatedCostSavingsOfRecommendedActions(GeneralUtil.getBigDecimalValue(info, titleList, "estimated-cost-savings-of-recommended-actions"));
					e.setSellThrough(GeneralUtil.getBigDecimalValue(info, titleList,"sell-through"));
					e.setItemVolume(GeneralUtil.getBigDecimalValue(info, titleList,"item-volume"));
					e.setVolumeUnitMeasurement(GeneralUtil.getStrValue(info, titleList, "volume-unit-measurement"));
					e.setStorageType(GeneralUtil.getStrValue(info, titleList, "storage-type"));
					e.setStorageVolume(GeneralUtil.getBigDecimalValue(info, titleList,"storage-volume"));
					e.setProductGroup(GeneralUtil.getStrValue(info, titleList, "product-group"));
					e.setSalesRank(GeneralUtil.getIntegerValue(info, titleList, "sales-rank"));
					e.setDaysOfSupply(GeneralUtil.getIntegerValue(info, titleList, "days-of-supply"));
					e.setEstimatedExcessQuantity(GeneralUtil.getIntegerValue(info, titleList, "estimated-excess-quantity"));
					e.setWeeksOfCoverT30(GeneralUtil.getIntegerValue(info, titleList, "weeks-of-cover-t30"));
					e.setWeeksOfCoverT90(GeneralUtil.getIntegerValue(info, titleList, "weeks-of-cover-t90"));
					e.setFeaturedofferPrice(GeneralUtil.getBigDecimalValue(info, titleList,"featuredoffer-price"));
					e.setSalesShippedLast7Days(GeneralUtil.getBigDecimalValue(info, titleList,"sales-shipped-last-7-days"));
					e.setSalesShippedLast30Days(GeneralUtil.getBigDecimalValue(info, titleList,"sales-shipped-last-30-days"));
					e.setSalesShippedLast60Days(GeneralUtil.getBigDecimalValue(info, titleList,"sales-shipped-last-60-days"));
					e.setSalesShippedLast90Days(GeneralUtil.getBigDecimalValue(info, titleList,"sales-shipped-last-90-days"));
					e.setInvAge0To30Days(GeneralUtil.getIntegerValue(info, titleList,"inv-age-0-to-30-days"));
					e.setInvAge31To60Days(GeneralUtil.getIntegerValue(info, titleList,"inv-age-31-to-60-days"));
					e.setInvAge61To90Days(GeneralUtil.getIntegerValue(info, titleList,"inv-age-61-to-90-days"));
					e.setInvAge181To330Days(GeneralUtil.getIntegerValue(info, titleList,"inv-age-181-to-330-days"));
					e.setInvAge331To365Days(GeneralUtil.getIntegerValue(info, titleList,"inv-age-331-to-365-days"));
					e.setEstimatedStorageCostNextMonth(GeneralUtil.getBigDecimalValue(info, titleList,"estimated-storage-cost-next-month"));
					e.setInboundQuantity(GeneralUtil.getIntegerValue(info, titleList,"inbound-quantity"));
					e.setInboundWorking(GeneralUtil.getIntegerValue(info, titleList,"inbound-working"));
					e.setInboundShipped(GeneralUtil.getIntegerValue(info, titleList,"inbound-shipped"));
					e.setInboundReceived(GeneralUtil.getIntegerValue(info, titleList,"inbound-received"));
					e.setNoSaleLast6Months(GeneralUtil.getIntegerValue(info, titleList,"no-sale-last-6-months"));
					e.setReservedQuantity(GeneralUtil.getIntegerValue(info, titleList,"reserved-quantity")); 
					e.setUnfulfillableQuantity(GeneralUtil.getIntegerValue(info, titleList,"unfulfillable-quantity"));
					e.setAfnResearchingQuantity(GeneralUtil.getIntegerValue(info, titleList,"afn-researching-quantity"));
					e.setAfnReservedFutureSupply(GeneralUtil.getIntegerValue(info, titleList,"afn-reserved-future-supply"));
					e.setAfnFutureSupplyBuyable(GeneralUtil.getIntegerValue(info, titleList,"afn-future-supply-buyable"));
					
					e.setQuantityToBeChargedAis181T210Days(GeneralUtil.getIntegerValue(info, titleList,"quantity-to-be-charged-ais-181-210-days"));
					e.setQuantityToBeChargedAis211T240Days(GeneralUtil.getIntegerValue(info, titleList,"quantity-to-be-charged-ais-211-240-days"));
					e.setQuantityToBeChargedAis241T270Days(GeneralUtil.getIntegerValue(info, titleList,"quantity-to-be-charged-ais-241-270-days"));
					e.setQuantityToBeChargedAis271T300Days(GeneralUtil.getIntegerValue(info, titleList,"quantity-to-be-charged-ais-271-300-days"));
					e.setQuantityToBeChargedAis301T330Days(GeneralUtil.getIntegerValue(info, titleList,"quantity-to-be-charged-ais-301-330-days"));
					e.setQuantityToBeChargedAis331T365Days(GeneralUtil.getIntegerValue(info, titleList,"quantity-to-be-charged-ais-331-365-days"));
					e.setQuantityToBeChargedAis365PlusDays(GeneralUtil.getIntegerValue(info, titleList,"quantity-to-be-charged-ais-365-plus-days"));
					
					e.setEstimatedAis181T210Days(GeneralUtil.getBigDecimalValue(info, titleList,"estimated-ais-181-210-days"));
					e.setEstimatedAis211T240Days(GeneralUtil.getBigDecimalValue(info, titleList,"estimated-ais-211-240-days"));
					e.setEstimatedAis241T270Days(GeneralUtil.getBigDecimalValue(info, titleList,"estimated-ais-241-270-days"));
					e.setEstimatedAis271T300Days(GeneralUtil.getBigDecimalValue(info, titleList,"estimated-ais-271-300-days"));
					e.setEstimatedAis301T330Days(GeneralUtil.getBigDecimalValue(info, titleList,"estimated-ais-301-330-days"));
					e.setEstimatedAis331T365Days(GeneralUtil.getBigDecimalValue(info, titleList,"estimated-ais-331-356-days"));
					e.setEstimatedAis365PlusDays(GeneralUtil.getBigDecimalValue(info, titleList,"estimated-ais-365-plus-days"));
					
					if(lineNumber==1) {
						LambdaQueryWrapper<AmzInventoryPlanning> query=new LambdaQueryWrapper<AmzInventoryPlanning>();
						query.eq(AmzInventoryPlanning::getAmazonauthid,amazonAuthority.getId());
						query.eq(AmzInventoryPlanning::getCountrycode,e.getCountrycode());
						query.lt(AmzInventoryPlanning::getSnapshotDate, e.getSnapshotDate());
						amzInventoryPlanningMapper.delete(query);
					}
					LambdaQueryWrapper<AmzInventoryPlanning> query=new LambdaQueryWrapper<AmzInventoryPlanning>();
					query.eq(AmzInventoryPlanning::getAmazonauthid,e.getAmazonauthid());
					query.eq(AmzInventoryPlanning::getSku,e.getSku());
					query.eq(AmzInventoryPlanning::getCountrycode,e.getCountrycode());
					query.eq(AmzInventoryPlanning::getCondition,e.getCondition());
					AmzInventoryPlanning old=amzInventoryPlanningMapper.selectOne(query);
					if(old!=null) {
						amzInventoryPlanningMapper.update(e, query);
					}else {
						amzInventoryPlanningMapper.insert(e);
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
		return "GET_FBA_INVENTORY_PLANNING_DATA";
	}
	
}
