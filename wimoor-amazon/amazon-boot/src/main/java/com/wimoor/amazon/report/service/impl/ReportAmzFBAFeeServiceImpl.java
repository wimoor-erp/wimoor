package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.finances.pojo.entity.FBAEstimatedFee;
import com.wimoor.amazon.finances.service.IFBAEstimatedFeeService;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.common.GeneralUtil;

 

@Service("reportAmzFBAFeeService")
public class ReportAmzFBAFeeServiceImpl extends ReportServiceImpl{

	@Resource
	private IMarketplaceService marketplaceService;
	@Resource
	private IFBAEstimatedFeeService iFBAEstimatedFeeService;
	
 
	//按区域申请的报表
	public String treatResponse(AmazonAuthority amazonAuthority,BufferedReader br)  {
		StringBuffer mlog = new StringBuffer();
 
		int lineNumber = 0;
		String line;
		Map<String,Integer> titleList= new HashMap<String,Integer>();
	    boolean isProductSizeTier=false;
		try {
			try {
				while ((line = br.readLine()) != null) {
					String[] info = line.split("\t");
					if (lineNumber == 0) {
						if(line.contains("product-size-tier")) {
							isProductSizeTier=true;
						}
						for (int i = 0; i < info.length; i++) {
							titleList.put(info[i],i);
						}
					} else {
						FBAEstimatedFee fBAEstimatedFee = new FBAEstimatedFee();
						fBAEstimatedFee.setAmazonauthid(amazonAuthority.getId());
						String sku = getStrValue(info,titleList,"sku");
						if(sku!=null && sku.length()>50){
							sku = sku.substring(0, 50);
						}
						fBAEstimatedFee.setSku(sku);
						fBAEstimatedFee.setFnsku(getStrValue(info,titleList,"fnsku"));
						fBAEstimatedFee.setAsin(getStrValue(info,titleList,"asin"));
						fBAEstimatedFee.setProductName(getStrValue(info,titleList,"product-name"));
						fBAEstimatedFee.setProductGroup(getStrValue(info,titleList,"product-group"));
						fBAEstimatedFee.setBrand(getStrValue(info,titleList,"brand"));
						fBAEstimatedFee.setFulfilledBy(getStrValue(info,titleList,"fulfilled-by"));
						fBAEstimatedFee.setHasLocalInventory(getStrValue(info,titleList,"has-local-inventory"));
					    fBAEstimatedFee.setYourPrice(getBigDecimalValue(info,titleList,"your-price"));
				        fBAEstimatedFee.setSalesPrice(getBigDecimalValue(info,titleList,"sales-price"));
					    fBAEstimatedFee.setLongestSide(getBigDecimalValue(info,titleList,"longest-side"));
						fBAEstimatedFee.setMedianSide(getBigDecimalValue(info,titleList,"median-side"));
						fBAEstimatedFee.setShortestSide(getBigDecimalValue(info,titleList,"shortest-side"));
						fBAEstimatedFee.setLengthAndGirth(getBigDecimalValue(info,titleList,"length-and-girth"));
						fBAEstimatedFee.setUnitOfDimension(getStrValue(info,titleList,"unit-of-dimension"));
						fBAEstimatedFee.setItemPackageWeight(getBigDecimalValue(info,titleList,"item-package-weight"));
						fBAEstimatedFee.setUnitOfWeight(getStrValue(info,titleList,"unit-of-weight"));
						
						String currency = getStrValue(info, titleList, "currency");
						fBAEstimatedFee.setCurrency(currency);
						String marketplaceid = GeneralUtil.getMarketPlaceId(currency);
						if (marketplaceid == null) {
							marketplaceid = amazonAuthority.getMarketPlace().getMarketplaceid();
						}
						fBAEstimatedFee.setMarketplaceid(marketplaceid);
						BigDecimal totalprice = getBigDecimalValue(info,titleList,"estimated-fee-total");
						if(isProductSizeTier) {
							fBAEstimatedFee.setProductSizeTier(getStrValue(info,titleList,"product-size-tier"));
						}else {
							fBAEstimatedFee.setProductSizeTier(getStrValue(info,titleList,"product-size-weight-band"));
						}
						if(totalprice==null){
							continue;
						}
						fBAEstimatedFee.setEstimatedFeeTotal(totalprice);
						BigDecimal referralfee = getBigDecimalValue(info,titleList,"estimated-referral-fee-per-unit");
						if(referralfee==null){
							continue;
						}
						fBAEstimatedFee.setEstimatedReferralFeePerUnit(referralfee);
						fBAEstimatedFee.setEstimatedVariableClosingFee(getBigDecimalValue(info,titleList,"estimated-variable-closing-fee"));
						fBAEstimatedFee.setEstimatedFixedClosingFee(getBigDecimalValue(info,titleList,"estimated-fixed-closing-fee"));//印度独有
						fBAEstimatedFee.setEstimatedFutureOrderHandlingFeePerOrder(getBigDecimalValue(info,titleList,"estimated-order-handling-fee-per-order"));
						fBAEstimatedFee.setExpectedDomesticFulfillmentFeePerUnit(getBigDecimalValue(info,titleList,"expected-domestic-fulfilment-fee-per-unit"));
						fBAEstimatedFee.setEstimatedOrderHandlingFeePerOrder(getBigDecimalValue(info,titleList,"estimated-order-handling-fee-per-order"));
						fBAEstimatedFee.setEstimatedPickPackFeePerUnit(getBigDecimalValue(info,titleList,"estimated-pick-pack-fee-per-unit"));
						fBAEstimatedFee.setEstimatedWeightHandlingFeePerUnit(getBigDecimalValue(info,titleList,"estimated-weight-handling-fee-per-unit"));
						fBAEstimatedFee.setExpectedFulfillmentFeePerUnit(getBigDecimalValue(info,titleList,"expected-fulfillment-fee-per-unit"));
						
						fBAEstimatedFee.setExpectedEfnFulfilmentFeePerUnitUk(getBigDecimalValue(info,titleList,"expected-efn-fulfilment-fee-per-unit-uk"));
						fBAEstimatedFee.setExpectedEfnFulfilmentFeePerUnitDe(getBigDecimalValue(info,titleList,"expected-efn-fulfilment-fee-per-unit-de"));					
						fBAEstimatedFee.setExpectedEfnFulfilmentFeePerUnitFr(getBigDecimalValue(info,titleList,"expected-efn-fulfilment-fee-per-unit-fr"));
						fBAEstimatedFee.setExpectedEfnFulfilmentFeePerUnitIt(getBigDecimalValue(info,titleList,"expected-efn-fulfilment-fee-per-unit-it"));
						fBAEstimatedFee.setExpectedEfnFulfilmentFeePerUnitEs(getBigDecimalValue(info,titleList,"expected-efn-fulfilment-fee-per-unit-es"));
						fBAEstimatedFee.setExpectedEfnFulfilmentFeePerUnitSe(getBigDecimalValue(info,titleList,"expected-efn-fulfilment-fee-per-unit-se"));
						if("PLN".equals(currency)) {
							fBAEstimatedFee.setMarketplaceid("A1C3SOZRARQ6R3");
						}else if("SAR".equals(currency)){
							fBAEstimatedFee.setMarketplaceid("A17E79C6D8DWNP");
						}else if("TRY".equals(currency)){
							fBAEstimatedFee.setMarketplaceid("A33AVAJ2PDY3EV");
						}else if("GBP".equals(currency)) {
							fBAEstimatedFee.setMarketplaceid("A1F83G8C2ARO7P");
						}else if("AED".equals(currency)) {
							fBAEstimatedFee.setMarketplaceid("A2VIGQ35RCS4UG");
						}else if("SEK".equals(currency)) {
							fBAEstimatedFee.setMarketplaceid("A2NODRKZP88ZB9");
						}else if ("EUR".equals(currency)&&fBAEstimatedFee.getYourPrice()!=null) {
								if(fBAEstimatedFee.getExpectedEfnFulfilmentFeePerUnitUk()==null
										&&fBAEstimatedFee.getExpectedEfnFulfilmentFeePerUnitDe()==null
										&&fBAEstimatedFee.getExpectedEfnFulfilmentFeePerUnitFr()==null
										&&fBAEstimatedFee.getExpectedEfnFulfilmentFeePerUnitIt()==null
										&&fBAEstimatedFee.getExpectedEfnFulfilmentFeePerUnitEs()==null){
									continue;
								}
							   if(fBAEstimatedFee.getExpectedEfnFulfilmentFeePerUnitSe()==null) {
									fBAEstimatedFee.setMarketplaceid("A2NODRKZP88ZB9");
							    }	else if(fBAEstimatedFee.getExpectedEfnFulfilmentFeePerUnitEs()==null) {
									fBAEstimatedFee.setMarketplaceid("A1RKKUPIHCS9HS");
								}else if(fBAEstimatedFee.getExpectedEfnFulfilmentFeePerUnitIt()==null) {
									fBAEstimatedFee.setMarketplaceid("APJ6JRA9NG5V4");
								}else if(fBAEstimatedFee.getExpectedEfnFulfilmentFeePerUnitFr()==null) {
									fBAEstimatedFee.setMarketplaceid("A13V1IB3VIYZZH");
								}else if(fBAEstimatedFee.getExpectedEfnFulfilmentFeePerUnitDe()==null) {
									fBAEstimatedFee.setMarketplaceid("A1PA6795UKMFR9");
								}else if(fBAEstimatedFee.getExpectedEfnFulfilmentFeePerUnitUk()==null) {
									fBAEstimatedFee.setMarketplaceid("A1F83G8C2ARO7P");
								}
							
						}
						fBAEstimatedFee.setProductName(""); 
						fBAEstimatedFee.setLastupdate(new Date());
						try {
							QueryWrapper<FBAEstimatedFee> query=new QueryWrapper<FBAEstimatedFee>();
							query.eq("sku", fBAEstimatedFee.getSku());
							query.eq("amazonAuthId", fBAEstimatedFee.getAmazonauthid());
							query.eq("marketplaceid", fBAEstimatedFee.getMarketplaceid());
							query.eq("asin", fBAEstimatedFee.getAsin());
							if(iFBAEstimatedFeeService.selectCount(query)>0) {
								iFBAEstimatedFeeService.update(fBAEstimatedFee,query);
							}else {
								iFBAEstimatedFeeService.save(fBAEstimatedFee);
							}
						} catch (Exception e) {
							if(mlog.length()==0){
								mlog.append("FBAEstimatedFee新增失败。");
							}
							mlog.append(fBAEstimatedFee.getSku()+"-"+fBAEstimatedFee.getMarketplaceid()+","+e.getClass());
						}finally {
							fBAEstimatedFee=null;
						}

					}
					lineNumber++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			try {
				if(br!=null) {
				br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			titleList.clear();
			titleList=null;
	}
       return mlog.toString();
	}
	
	private BigDecimal getBigDecimalValue(String[] info, Map<String, Integer> titleList, String key) {
		// TODO Auto-generated method stub
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			if(isNumericzidai(info[position])) {
				return new BigDecimal(info[position]);
			}
		}
		return null;
	}

	private String getStrValue(String[] info, Map<String, Integer> titleList, String key) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return ReportType.FbaFeeReport;
	}

	
}
