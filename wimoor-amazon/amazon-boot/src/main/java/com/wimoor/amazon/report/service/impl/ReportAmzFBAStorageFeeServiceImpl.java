package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.finances.mapper.FBAStorageFeeReportMapper;
import com.wimoor.amazon.finances.pojo.entity.FBAStorageFeeReport;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
 

@Service("reportAmzFBAStorageFeeService")
public class ReportAmzFBAStorageFeeServiceImpl extends ReportServiceImpl{

	@Resource
	private IMarketplaceService marketplaceService;
	@Resource
	private FBAStorageFeeReportMapper fBAStorageFeeReportMapper;
	
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
			  QueryWrapper<FBAStorageFeeReport> query=new QueryWrapper<FBAStorageFeeReport>();
			  query.eq("amazonauthid", amazonAuthority.getId());
			  query.eq("country", market.getMarket());
			  SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM");
			  query.eq("month",fmt.format(cstart.getTime()));
			  Long count = fBAStorageFeeReportMapper.selectCount(query);
			   if(count!=null&&count>0) {
				   continue;
			   }
			   if(region.contains(market.getRegion())) {
				   continue;
			   }else {
				   region.add(market.getRegion());
			   }
			   Date today = new Date();
				  double days = Math.floor((today.getTime() - cend.getTime().getTime()) / 1000 / 3600 / 24); 
				  if(days<8) {
					  continue;
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
				      break;
				  } catch (ApiException e) {
					  amazonAuthority.setApiRateLimit( null,e);
					  e.printStackTrace();
			    }
			 
		  }
}

	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)  {
		int lineNumber = 0;
		String line;
		String month=null;
		String log = null;
		Map<String,Integer> titleList = new HashMap<String,Integer>();
		List<FBAStorageFeeReport> linkedlist=new LinkedList<FBAStorageFeeReport>();
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				if (lineNumber == 0) {
					for (int i = 0; i < info.length; i++) {
						titleList.put(info[i],i);
					}
				}
				if (lineNumber > 0) {
					month = getStrValue(info,titleList,"month_of_charge");
					if(month==null||month.equals("--")||!month.startsWith("20")||!month.contains("-")){
						continue;
					}
					if(lineNumber==1){
						deleteByAuthAndMonth(amazonAuthority.getId(), month);
					}
					FBAStorageFeeReport fBAStorageFee = new FBAStorageFeeReport();
					fBAStorageFee.setAmazonauthid(amazonAuthority.getId());
					fBAStorageFee.setAsin(getStrValue(info,titleList,"asin"));
					fBAStorageFee.setFnsku(getStrValue(info,titleList,"fnsku"));
					fBAStorageFee.setFulfillmentCenter(getStrValue(info,titleList,"fulfillment_center"));
					String country = getStrValue(info,titleList,"country_code");
					if("GB".equalsIgnoreCase(country)){
						country = "UK";
					}
					fBAStorageFee.setCountryCode(country);
					fBAStorageFee.setLongestSide(getBigDecimalValue(info,titleList,"longest_side"));
					fBAStorageFee.setMedianSide(getBigDecimalValue(info,titleList,"median_side"));
					fBAStorageFee.setShortestSide(getBigDecimalValue(info,titleList,"shortest_side"));
					fBAStorageFee.setMeasurementUnits(getStrValue(info,titleList,"measurement_units"));
					fBAStorageFee.setWeight(getBigDecimalValue(info,titleList,"weight"));
					fBAStorageFee.setWeightUnits(getStrValue(info,titleList,"weight_units"));
					fBAStorageFee.setItemVolume(getBigDecimalValue(info,titleList,"item_volume"));
					fBAStorageFee.setVolumeUnits(getStrValue(info,titleList,"volume_units"));
				    fBAStorageFee.setProductSizeTier(getStrValue(info,titleList,"product_size_tier"));
				    fBAStorageFee.setAverageQuantityOnHand(getBigDecimalValue(info,titleList,"average_quantity_on_hand"));
				    fBAStorageFee.setAverageQuantityPendingRemoval(getBigDecimalValue(info,titleList,"average_quantity_pending_removal"));
				    fBAStorageFee.setEstimatedTotalItemVolume(getBigDecimalValue(info,titleList,"estimated_total_item_volume"));
				    fBAStorageFee.setMonthOfCharge(month);
				    fBAStorageFee.setStorageUtilizationRatio(getStrValue(info,titleList,"storage_utilization_ratio"));
				    fBAStorageFee.setStorageUtilizationRatioUnits(getStrValue(info,titleList,"storage_utilization_ratio_units"));
					fBAStorageFee.setCurrency(getStrValue(info,titleList,"currency"));
					fBAStorageFee.setQualifiesForInventoryDiscount(getStrValue(info,titleList,"qualifies_for_inventory_discount"));
					fBAStorageFee.setEligibleForInventoryDiscount(getStrValue(info,titleList,"eligible_for_inventory_discount"));
					fBAStorageFee.setBaseRate(getBigDecimalValue(info,titleList,"base_rate"));
					fBAStorageFee.setUtilizationSurchargeRate(getBigDecimalValue(info,titleList,"utilization_surcharge_rate"));
					fBAStorageFee.setEstimatedMonthlyStorageFee(getBigDecimalValue(info,titleList,"estimated_monthly_storage_fee"));
					fBAStorageFee.setDangerousGoodsStorageType(getStrValue(info,titleList,"dangerous_goods_storage_type"));
					fBAStorageFee.setTotalIncentiveFeeAmount(getBigDecimalValue(info,titleList,"total_incentive_fee_amount"));
					fBAStorageFee.setBreakdownIncentiveFeeAmount(getStrValue(info,titleList,"breakdown_incentive_fee_amount"));
					fBAStorageFee.setAverageQuantityCustomerOrders(getBigDecimalValue(info,titleList,"average_quantity_customer_orders"));
					fBAStorageFee.setLastupdate(new Date());
					linkedlist.add(fBAStorageFee);
					if(linkedlist.size()>1000) {
						 fBAStorageFeeReportMapper.insertBatch(linkedlist);
						 linkedlist.clear();
					}
				}
				lineNumber++;
			}
			if(br!=null) {
				br.close();
				}
			try {
				if(linkedlist.size()>0) {
					fBAStorageFeeReportMapper.insertBatch(linkedlist);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(log==null){
					log = "FBAStorageFee新增失敗！";
				}
				log+= e.getMessage();
			}finally {
				 
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
				titleList.clear();
				titleList=null;
	     }
	  
       return log;
	}
	
	public void deleteByAuthAndMonth(String amazonAuthid, String month) {
		QueryWrapper<FBAStorageFeeReport> query = new QueryWrapper<FBAStorageFeeReport>();
		query.eq("amazonauthid", amazonAuthid);
		query.eq("`month`", month);
		fBAStorageFeeReportMapper.delete(query);
	}
	
	private BigDecimal getBigDecimalValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			try {
				if(info[position]==null||info[position].equals("--")) {
					return null;
				}
				return new BigDecimal(info[position]);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private String getStrValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			return info[position];
		}
		return null;
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
		return ReportType.FbaStorageFeeReport;
	}

	
}
