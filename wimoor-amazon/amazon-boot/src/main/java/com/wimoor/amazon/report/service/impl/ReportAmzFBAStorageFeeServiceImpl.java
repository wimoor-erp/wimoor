package com.wimoor.amazon.report.service.impl;

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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("reportAmzFBAStorageFeeService")
public class ReportAmzFBAStorageFeeServiceImpl extends ReportServiceImpl{

	@Resource
	private IMarketplaceService marketplaceService;
	@Resource
	private FBAStorageFeeReportMapper fBAStorageFeeReportMapper;

	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
		amazonAuthority.setUseApi("createReport");
		// 设置为整月查询：开始时间为月份第一天，结束时间为月份最后一天
		// 重置开始时间为当月第一天
		cstart.set(Calendar.DAY_OF_MONTH, 1);
		cstart.set(Calendar.HOUR_OF_DAY, 0);
		cstart.set(Calendar.MINUTE, 0);
		cstart.set(Calendar.SECOND, 0);
		// 先将cend的年份和月份设置为与cstart相同
		cend.set(Calendar.YEAR, cstart.get(Calendar.YEAR));
		cend.set(Calendar.MONTH, cstart.get(Calendar.MONTH));
		// 然后设置为该月的最后一天
		cend.set(Calendar.DAY_OF_MONTH, cstart.getActualMaximum(Calendar.DAY_OF_MONTH));
		cend.set(Calendar.HOUR_OF_DAY, 23);
		cend.set(Calendar.MINUTE, 59);
		cend.set(Calendar.SECOND, 59);
		List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		List<String> list=new ArrayList<String>();
		CreateReportSpecification body=new CreateReportSpecification();
		body.setReportType(myReportType());
		body.setDataStartTime(AmzDateUtils.getOffsetDateTimeUTC(cstart));
		body.setDataEndTime(AmzDateUtils.getOffsetDateTimeUTC(cend));
		ReportOptions reportOptions=getMyOptions();
		if(reportOptions!=null) {
			body.setReportOptions(reportOptions);
		}
		Marketplace mymarket=null;
		for(Marketplace market:marketlist) {
			list.add(market.getMarketplaceid());
			if(mymarket==null){ mymarket=market;}
		}
		if(list.size()==0) {return;}
		if(ignore==null||ignore==false) {
			//时间在早上8点到18点之间的申请，不执行以下代码
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("sellerid", amazonAuthority.getSellerid());
			param.put("reporttype", this.myReportType());
			param.put("marketplacelist", list);
			Date lastupdate= iReportRequestRecordService.lastUpdateRequestByType(param);
			if(lastupdate!=null&&GeneralUtil.distanceOfHour(lastupdate, new Date())<72) {
				return;
			}
		}
		amazonAuthority.setMarketPlace(mymarket);
		body.setMarketplaceIds(list);
		callCreateAPI(this, body, amazonAuthority, mymarket,cstart.getTime(),cend.getTime());
	}


	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)  {
		int lineNumber = 0;
		String line;
		String month=null;
		String log = null;
		FBAStorageFeeReport fBAStorageFee =null;
		Map<String,Integer> titleList = new HashMap<String,Integer>();
		List<FBAStorageFeeReport> linkedlist=new LinkedList<FBAStorageFeeReport>();
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				//System.out.println(line);
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
					if(month.length()>=10) {
						month=month.substring(0,7);
					}
					fBAStorageFee = new FBAStorageFeeReport();
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
					if(fBAStorageFee.getDangerousGoodsStorageType()!=null&&fBAStorageFee.getDangerousGoodsStorageType().length()>=9){
						fBAStorageFee.setDangerousGoodsStorageType(fBAStorageFee.getDangerousGoodsStorageType().substring(0,9));
					}
					fBAStorageFee.setTotalIncentiveFeeAmount(getBigDecimalValue(info,titleList,"total_incentive_fee_amount"));
					fBAStorageFee.setBreakdownIncentiveFeeAmount(getStrValue(info,titleList,"breakdown_incentive_fee_amount"));
					fBAStorageFee.setAverageQuantityCustomerOrders(getBigDecimalValue(info,titleList,"average_quantity_customer_orders"));
					fBAStorageFee.setLastupdate(new Date());
					linkedlist.add(fBAStorageFee);
					if(linkedlist.size()>800) {
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
			
		} catch (Exception e) {
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
