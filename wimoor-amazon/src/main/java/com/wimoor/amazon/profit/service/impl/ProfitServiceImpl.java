package com.wimoor.amazon.profit.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.googlecode.aviator.AviatorEvaluator;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.product.service.IProductCaptureListingsItemService;
import com.wimoor.amazon.profit.mapper.FixedClosingFeeMapper;
import com.wimoor.amazon.profit.mapper.VariableClosingFeeMapper;
import com.wimoor.amazon.profit.pojo.entity.FBALabelingFee;
import com.wimoor.amazon.profit.pojo.entity.FixedClosingFee;
import com.wimoor.amazon.profit.pojo.entity.ProductTier;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.pojo.entity.ReferralFee;
import com.wimoor.amazon.profit.pojo.entity.VariableClosingFee;
import com.wimoor.amazon.profit.pojo.vo.CostDetail;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.amazon.profit.pojo.vo.ItemMeasure;
import com.wimoor.amazon.profit.service.IFbaFormatService;
import com.wimoor.amazon.profit.service.IFbaLabelingFeeService;
import com.wimoor.amazon.profit.service.IInplaceFeeFormatService;
import com.wimoor.amazon.profit.service.IInventoryStorageFeeService;
import com.wimoor.amazon.profit.service.IOutBoundWeightFormatService;
import com.wimoor.amazon.profit.service.IProductFormatService;
import com.wimoor.amazon.profit.service.IProductTierService;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.profit.service.IProfitService;
import com.wimoor.amazon.profit.service.IReferralFeeService;
import com.wimoor.amazon.report.pojo.entity.FBAEstimatedFee;
import com.wimoor.common.StringFormat;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.amazon.profit.pojo.vo.*;
import com.wimoor.util.SpringUtil;
import cn.hutool.core.util.StrUtil;
import com.amazon.spapi.model.listings.Item; 

@Service("profitService")  
public class ProfitServiceImpl implements IProfitService{
	@Resource
	IProductTierService productTierService;
	@Resource
	IProductFormatService productFormatService;
	@Resource
	IFbaFormatService fbaFormatService;
	@Resource
	IMarketplaceService marketplaceService;
	@Resource
	IInplaceFeeFormatService inplaceFeeFormatService;
	@Resource
	FixedClosingFeeMapper fixedClosingFeeMapper;
	@Resource
	VariableClosingFeeMapper VClosingFeeMapper;
	@Resource
	IOutBoundWeightFormatService outboundWeightFormatService;
	@Resource
	IFbaLabelingFeeService fbaLabelingFeeService;
 
	@Resource
	IInventoryStorageFeeService inventoryStorageFeeService;
	@Resource
	public IExchangeRateHandlerService exchangeRateHandlerService;
	@Resource
	public IReferralFeeService referralFeeService;
	@Resource
	public IAmazonAuthorityService amazonAuthorityService;
 	@Resource
 	public IProductCaptureListingsItemService productCaptureService;
	@Resource
	IProfitCfgService  profitCfgService;
	
	DecimalFormat df = new DecimalFormat("0.00");// ??????2?????????
	private static final String mws_groupId = "26138972975530147";
	public Map<String, Map<String, String>> unitMap = null;
	
	public void clearUnitMap(){
		unitMap = null;
	}
	
	public static String[] smlAndLightCountry={"US","UK","DE","FR","IT","ES","JP"};
	
	public static String formatCurrency(String currency) {
		String fcuurency = null;
		if(currency.equalsIgnoreCase("USD")){
			fcuurency = "$";
		}
		if (currency.equalsIgnoreCase("GBP")) {
			fcuurency = "??";
		}
		if (currency.equalsIgnoreCase("EUR")) {
			fcuurency = "???";
		}
		if (currency.equalsIgnoreCase("JPY") || currency.equalsIgnoreCase("RMB")) {
			fcuurency = "??";
		}
		if (currency.equalsIgnoreCase("CAD")) {
			fcuurency = "C$";
		}
		if (currency.equalsIgnoreCase("AUD")) {
			fcuurency = "A$";
		}
		if (currency.equalsIgnoreCase("INR")) {
			fcuurency = "???";
		}
		if (currency.equalsIgnoreCase("MXN")) {
			fcuurency = "M$";
		}
		if (currency.equalsIgnoreCase("AED")) {
			fcuurency = "AE$";
		}
		if (currency.equalsIgnoreCase("SAR")) {
			fcuurency = "S$";
		}
		if (currency.equalsIgnoreCase("PLN")) {
			fcuurency = "z??";
		}
		if (currency.equalsIgnoreCase("SEK")) {
			fcuurency = "Kr";
		}
		return fcuurency;
	}
	
	
	/**
	 * ???????????????????????????
	 * ??????=purchase+shipMent+others+FBA?????????????????????????????????+FBA???GST???+??????+rate??????*??????+???????????????+??????+??????GST???+??????GST???+VAT???+???????????????;
	 * ???????????????=[??????-FBA?????????????????????????????????-??????]*lostrate;
	 * ??????=??????*R>minimum???????*R:minimum; 
	 * ?????? = ??????+??????*margin;??????=??????/(1-margin);
	 * vat??????=??????*(vatRate/(1+vatRate));
	 * ??????GST??????=??????*(selling_GSTRate/(1+selling_GSTRate));
	 * ??????????????? = ??????C * (????????? - ??????????????????(????????????GST???, FBA???GST???));
	 * ????????????1.price=[(1-C)*(purchase+shipMent+others+FBA?????????????????????????????????-FBA??????????????????????????????+fbaTax???*lostrate+??????)+fbaTax+??????GST???]/(1-C)*[1/(1+vatRate)-selling_GSTRate/(1+selling_GSTRate)-rateFee-lostrate-R+R*lostrate]-margin
	 *      2.price=[(1-C)*(purchase+shipMent+others+FBA?????????????????????????????????+ minimum+??????-FBA??????????????????????????????+fbaTax???*lostrate-minimum*lostrate)+fbaTax+??????GST???]/(1-C)*[1/(1+vatRate)-selling_GSTRate/(1+selling_GSTRate)-rateFee-lostrate]-margin
	 * @param referralrate 
	 */
	public List<Map<String, String>> calculateSellingPrice(CostDetail costDetail, int typeId, String country, BigDecimal referralrate) {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<BigDecimal> marginList = findMarginList();//????????????????????????
		ReferralFee resultfee = referralFeeService.getReferralFeeByTypeCountry(typeId, country);//???????????????id???parent_id??????????????????
		BigDecimal referralR = resultfee.getPercent1();//??????Amazon Referral Fee??????????????????????????????
		BigDecimal minimum = resultfee.getLoweast();//Amazon Referral Fee????????????
		if("UK".equals(country)){//2020???09???01????????????UK????????????2%?????????????????????
			referralR = referralR.multiply(new BigDecimal("1.02"));
			minimum = minimum.multiply(new BigDecimal("1.02"));
		}
		if ("IN".equals(country) && referralrate!=null) {
			referralR = referralrate;
			minimum = new BigDecimal("0");
		}
		BigDecimal FBA = getAmazonFeeButRef(costDetail);//???????????????????????????????????????????????????????????????FBA???GST??????UK??????dst???
		BigDecimal temp = new BigDecimal("1").divide(new BigDecimal("1").add(costDetail.getVatRate()),4, BigDecimal.ROUND_HALF_UP);
		BigDecimal temp2 = costDetail.getSelling_GSTRate().divide(new BigDecimal("1").add(costDetail.getSelling_GSTRate()),4, BigDecimal.ROUND_HALF_UP);
		
		for (int i = 0; i < marginList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			Map<String, BigDecimal> priceResult = estimateSellingPrice(costDetail, referralR, minimum, FBA, temp, temp2, marginList.get(i));
			if(priceResult!=null){
				BigDecimal sellingPrice = priceResult.get("sellingPrice");
				BigDecimal referralFee = priceResult.get("referralFee");
				if ("IN".equals(country)) {
					IProfitService profitServiceX = null;
					try {
						profitServiceX = factoryProfitServiceByCountry(country);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (profitServiceX==null) {
						return null;
					}
					//Fixed closing fee,????????????????????????????????????????????????
					BigDecimal closingFee = profitServiceX.calculateClosingFee(country,sellingPrice);
					costDetail.setClosingFee(closingFee);
					//???????????????GST????????????18%, ??????????????????????????????, Fixed Closing Fee, FBA fee???
					BigDecimal fbaTaxFee=costDetail.getFBA().add(closingFee).add(referralFee).multiply(costDetail.getFbaTax())
							.setScale(4, BigDecimal.ROUND_HALF_UP);
					costDetail.setFbaTaxFee(fbaTaxFee);
					FBA = getAmazonFeeButRef(costDetail);
					priceResult = estimateSellingPrice(costDetail, referralR, minimum, FBA, temp, temp2, marginList.get(i));
					sellingPrice = priceResult.get("sellingPrice");
					referralFee = priceResult.get("referralFee");
				} else {
					//???????????????????????????????????????????????????????????????minimum?????????????????????????????????????????????
					if(referralFee.floatValue()>minimum.floatValue() && resultfee.getPercent2()!=null && resultfee.getPercent2().floatValue()>0){
						referralFee = this.calculateReferralFee(typeId, sellingPrice, costDetail.getCountry());
						costDetail.setReferralFee(referralFee);
						if("UK".equals(country)){
							FBA = getAmazonFeeButRef(costDetail);
						}
						sellingPrice = getSellingPrice(costDetail, FBA, temp, temp2, marginList.get(i));
					} else {
						if("UK".equals(country)){
							referralFee = sellingPrice.multiply(resultfee.getPercent1());
							costDetail.setReferralFee(referralFee);
						}
					}
				}
				BigDecimal profit = sellingPrice.multiply(marginList.get(i));
				map.put("margin", marginList.get(i).doubleValue()*100+"%");
				map.put("sellingPrice", df.format(sellingPrice));
				map.put("profit", df.format(profit));
				resultList.add(map);
			}
		}
		return resultList;
	}
	
	/*
	 * ????????????1.price=[(1-C)*(purchase+shipMent+others+FBA?????????????????????????????????-FBA??????????????????????????????+fbaTax???*lostrate+??????)+fbaTax+??????GST???]/(1-C)*[1/(1+vatRate)-selling_GSTRate/(1+selling_GSTRate)-rateFee-lostrate-R+R*lostrate]-margin
	 *        2.price=[(1-C)*(purchase+shipMent+others+FBA?????????????????????????????????+ minimum+??????-FBA??????????????????????????????+fbaTax???*lostrate-minimum*lostrate)+fbaTax+??????GST???]/(1-C)*[1/(1+vatRate)-selling_GSTRate/(1+selling_GSTRate)-rateFee-lostrate]-margin
	 */
	public Map<String, BigDecimal> estimateSellingPrice(CostDetail costDetail,BigDecimal referralR,BigDecimal minimum,BigDecimal FBA,
			BigDecimal temp,BigDecimal temp2,BigDecimal margin) {
		Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
		// ?????????,??????=??????*R
		BigDecimal a = new BigDecimal("1").subtract(costDetail.getCorporateInRate());//(1-C)
		BigDecimal m1 = costDetail.getPurchase().add(costDetail.getShipment()).add(costDetail.getOthers()).add(FBA)
				.subtract(FBA.add(costDetail.getFbaTaxFee()).multiply(costDetail.getCurrencyTransportRate())).add(costDetail.getTax());
		BigDecimal m = a.multiply(m1).add(costDetail.getFbaTaxFee()).add(costDetail.getImport_GST());
		BigDecimal d1 = temp.subtract(temp2).subtract(costDetail.getRateFee())
				.subtract(costDetail.getCurrencyTransportRate()).subtract(referralR)
				.add(referralR.multiply(costDetail.getCurrencyTransportRate()));
		if(d1.floatValue()<0){
			return null;
		}
		BigDecimal d = a.multiply(d1).subtract(margin);
		
		// ??????BigDecimal???divide????????????????????????????????????????????????????????????????????????????????????????????????????????????2?????????
		BigDecimal sellingPrice = m.divide(d, 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal referralFee = sellingPrice.multiply(referralR);

		if (sellingPrice.multiply(referralR).doubleValue() < minimum.doubleValue()) {
			BigDecimal m2 = costDetail.getPurchase().add(costDetail.getShipment()).add(costDetail.getOthers())
					.add(FBA).add(minimum).add(costDetail.getTax()).subtract(FBA.add(costDetail.getFbaTaxFee()).multiply(costDetail.getCurrencyTransportRate()))
					.subtract(minimum.multiply(costDetail.getCurrencyTransportRate()));
			
			m = a.multiply(m2).add(costDetail.getFbaTaxFee()).add(costDetail.getImport_GST());
			
			BigDecimal d2 = temp.subtract(temp2).subtract(costDetail.getRateFee()).subtract(costDetail.getCurrencyTransportRate());
			d = a.multiply(d2).subtract(margin);
			
			sellingPrice = m.divide(d, 2, BigDecimal.ROUND_HALF_UP);
			referralFee = minimum;
		}
		result.put("sellingPrice", sellingPrice);
		result.put("referralFee", referralFee);
		return result;
	}
	
	public BigDecimal getSellingPrice(CostDetail costDetail, BigDecimal FBA, BigDecimal temp, BigDecimal temp2,
			BigDecimal margin) {
		BigDecimal a = new BigDecimal("1").subtract(costDetail.getCorporateInRate());// (1-C)
		BigDecimal m2 = costDetail.getPurchase().add(costDetail.getShipment()).add(costDetail.getOthers()).add(FBA)
				.add(costDetail.getReferralFee()).add(costDetail.getTax())
				.subtract(FBA.add(costDetail.getFbaTaxFee()).multiply(costDetail.getCurrencyTransportRate()))
				.subtract(costDetail.getReferralFee().multiply(costDetail.getCurrencyTransportRate()));
		BigDecimal m = a.multiply(m2).add(costDetail.getFbaTaxFee()).add(costDetail.getImport_GST());
		BigDecimal d2 = temp.subtract(temp2).subtract(costDetail.getRateFee())
				.subtract(costDetail.getCurrencyTransportRate());
		BigDecimal d = a.multiply(d2).subtract(margin);

		BigDecimal sellingPrice = m.divide(d, 2, BigDecimal.ROUND_HALF_UP);
		return sellingPrice;
	}
	
	// ??????????????????????????????????????????????????????
	public BigDecimal getAmazonFeeButRef(CostDetail costDetail) {
		return costDetail.getFBA().add(costDetail.getClosingFee()).add(costDetail.getVCFee())
				.add(costDetail.getStorageFee()).add(costDetail.getInPlaceFee()).add(costDetail.getPrepServiceFee())
				.add(costDetail.getManualProcessingFee()).add(costDetail.getLabelServiceFee());
	}
	
	public List<BigDecimal> findMarginList() {
		List<BigDecimal> marginList = new ArrayList<BigDecimal>();
		marginList.add(new BigDecimal("0.00"));
		marginList.add(new BigDecimal("0.10"));
		marginList.add(new BigDecimal("0.15"));
		marginList.add(new BigDecimal("0.20"));
		marginList.add(new BigDecimal("0.25"));
		marginList.add(new BigDecimal("0.30"));
		marginList.add(new BigDecimal("0.50"));
		return marginList;
	}

//	Map<String,ReferralFee> referralMap=new HashMap<String,ReferralFee>();
//	public ReferralFee getReferralFeeByType(int typeId) {
//		Integer mytypeid=typeId;
//		if(referralMap.containsKey(mytypeid.toString()))return referralMap.get(mytypeid.toString());
//		ReferralFee referralFee = referralFeeMapper.selectByPrimaryKey(mytypeid);
//		referralMap.put(mytypeid.toString(), referralFee);
//		return referralFee;
//	}
	
	public String findTypeById(int typeId) {
		String type = "";
		ReferralFee referralFee = referralFeeService.getReferralFeeByType(typeId);
		if (referralFee != null) {
			type = referralFee.getType();
		}
		return type;
	}

	public String isMedia(int typeId) {
		String isMedia = "";
		ReferralFee referralFee = referralFeeService.getReferralFeeByType(typeId);
		if (referralFee != null)
			isMedia = referralFee.getIsmedia();
		return isMedia;
	}

	public List<String> findCountryList() {
		List<String> countryList = new ArrayList<String>();
		List<Marketplace> list = marketplaceService.findAllMarketplace();
		for (int i = 0; i < list.size(); i++) {
			Marketplace marketplace = list.get(i);
			if (marketplace.getCurrency() != null && marketplace.getDimUnits() != null
					&& marketplace.getWeightUnits() != null) {
				countryList.add(marketplace.getMarket());
			}
		}
		return countryList;
	}
	
	public List<String> findCurrencyUnitList() {
		List<String> currencyUnitList = new ArrayList<String>();
		currencyUnitList.add("RMB");
		currencyUnitList.add("USD");
		currencyUnitList.add("GBP");
		currencyUnitList.add("EUR");
		currencyUnitList.add("INR");
//		currencyUnitList.add("???");
//		currencyUnitList.add("$");
//		currencyUnitList.add("???");
//		currencyUnitList.add("???");
		return currencyUnitList;
	}

	public BigDecimal calculateStorageFee(String country, ProfitConfigCountry profitConfigX, InputDimensions inputDimension, boolean isStandard) 
			throws BizException {//???????????????????????????????????????
		BigDecimal monthlyFee =  profitConfigX.getStoragefee();
		BigDecimal month = profitConfigX.getAmonthBigDecimal();
		BigDecimal volume = inputDimension.getVolume(InputDimensions.unit_in).getValue().divide(new BigDecimal("1728"),
				4, BigDecimal.ROUND_HALF_UP);// ?????????????????????????????????
		BigDecimal storageFee = monthlyFee.multiply(volume).multiply(month);
		return storageFee;
	}
	
	public String determineProductTier(String country, InputDimensions inputDimension, String isMedia) throws BizException {
		// TODO Auto-generated method stub
		return null;
	}

	//??????outbound Weight,?????????Kg
	public BigDecimal determineOutboundWeight(String country, ProductTier productTier, InputDimensions inputDimension, String isMedia) throws BizException {
		BigDecimal outboundWeight = new BigDecimal("0");
		BigDecimal weight = inputDimension.getWeight(getWeightUnit(country)).getValue();
		if (weight == null) {
			weight = new BigDecimal("0");
		}
		BigDecimal boxweight = productTier.getBoxWeight();
		if (boxweight == null) {
			boxweight = new BigDecimal("0");
		}
		outboundWeight = weight.add(boxweight).multiply(new BigDecimal("1000")).setScale(0, BigDecimal.ROUND_CEILING);// ????????????????????????????????????????????????,?????????g
		outboundWeight = outboundWeight.divide(new BigDecimal("1000"),3,BigDecimal.ROUND_CEILING);
		return outboundWeight;
	}

	public BigDecimal calculateFBA(String country, String USProductTier, InputDimensions inputDimension, String isMedia, String type, BigDecimal outboundWeight, ProfitConfigCountry profitConfigX, String shipmentType) throws BizException {
		// TODO Auto-generated method stub
		return new BigDecimal("0");
	}

	public BigDecimal calculateInPlaceFee(String country, ProductTier productTier, InputDimensions inputDimension, String invplaceFee) throws BizException {
		return new BigDecimal("0");
	}

	//??????Variable Closing Fee,?????????????????????????????????
	Map<String,VariableClosingFee> vclosingMap=new HashMap<String,VariableClosingFee>();
	public BigDecimal calculateVCFee(String country, String isMedia, int typeId) {
		BigDecimal VCFee = new BigDecimal("0");
		Integer mytypeid=typeId;
		VariableClosingFee VClosingFee =vclosingMap.get(mytypeid.toString()+country);
		if(  VClosingFee ==null) {
			VClosingFee=VClosingFeeMapper.findByTypeId(country, typeId);
			vclosingMap.put(mytypeid.toString()+country,VClosingFee);
		}
		if (VClosingFee!=null) {
			String format = VClosingFee.getFormat();
			String result = StringFormat.format(format);
			VCFee = new BigDecimal(result);
		}
		return VCFee;
	}

	public Map<String, String> strToMap(String costDetail) {
		Map<String, String> detailMap = new HashMap<String, String>();
		if (costDetail!=null) {
			costDetail = costDetail.substring(1, costDetail.length() - 1).trim();// ??????[]
			if (costDetail.contains("resultList=")) {
				costDetail = costDetail.substring(0, costDetail.indexOf("resultList="));// ??????resultList
				costDetail = costDetail.substring(0, costDetail.lastIndexOf(","));
			}
			if (costDetail.contains(",")) {
				String[] details = costDetail.split(",");
				for (int i = 0; i < details.length; i++) {
					if (details[i] != null && details[i].contains("=")) {
						String key = details[i].substring(0, details[i].indexOf("=")).trim();
						String value = details[i].substring(details[i].indexOf("=") + 1).trim();
						detailMap.put(key, value);
					}
				}
			}
		} 
		return detailMap;
	}
	
	public Map<String, String> jsonToMap(String costDetail) {
		Map<String, String> detailMap = null;
		if (costDetail!=null && !"".equals(costDetail)) {
			detailMap = new HashMap<String, String>();
			costDetail = costDetail.substring(1, costDetail.length() - 1).trim();// ??????[]
			if (costDetail.contains("\"resultList\":[")) {
				// ??????resultList
				String costDetail_before = costDetail.substring(0, costDetail.indexOf("\"resultList\":[")-1);
				String costDetail_after = costDetail.substring(costDetail.indexOf("}]")+2);
				costDetail = costDetail_before+costDetail_after;
			}
			if (costDetail.contains(",")) {
				String[] details = costDetail.split(",");
				for (int i = 0; i < details.length; i++) {
					if (details[i] != null && details[i].contains(":")) {
						String key = details[i].substring(1, details[i].indexOf("\":")).trim();
						String value = details[i].substring(details[i].indexOf(":") + 1).trim();
						if (value.contains("\"")) {
							value = value.substring(1, value.length() - 1).trim();// ??????""
						}
						detailMap.put(key, value);
					}
				}
			}
		} 
		return detailMap;
	}

	public Map<String, Map<String, String>> getUnit() {
		Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String,String>>();
		Map<String, String> cmap = new HashMap<String, String>();
		Map<String, String> dmap = new HashMap<String, String>();
		Map<String, String> wmap = new HashMap<String, String>();
		Map<String, String> slmap = new HashMap<String, String>();
		List<Marketplace> list = marketplaceService.findAllMarketplace();
		for (int i = 0; i < list.size(); i++) {
			Marketplace marketplace = list.get(i);
			cmap.put(marketplace.getMarket(), marketplace.getCurrency());
			dmap.put(marketplace.getMarket(), marketplace.getDimUnits());
			wmap.put(marketplace.getMarket(), marketplace.getWeightUnits());
		}
		resultMap.put("currency", cmap);//????????????
		resultMap.put("dimensionUnit", dmap);//????????????
		resultMap.put("weightUnit", wmap);//????????????
		for (int i = 0; i < smlAndLightCountry.length; i++) {
			if ("US".equals(smlAndLightCountry[i])) {
				slmap.put(smlAndLightCountry[i],"oz");
			} else {
				slmap.put(smlAndLightCountry[i],"g");
			}
		}
		resultMap.put("slweightUnit", slmap);//??????????????????
		return resultMap;
	}

	public String getCurrencyUnit(String country) {
		if (unitMap == null) {
			unitMap = getUnit();
		}
		String currency = unitMap.get("currency").get(country);
		return currency;
	}
	
	public String getDimUnit(String country) {
		if (unitMap == null) {
			unitMap = getUnit();
		}
		String dimUnint = unitMap.get("dimensionUnit").get(country);
		return dimUnint;
	}
	
	public String getWeightUnit(String country) {
		if (unitMap == null) {
			unitMap = getUnit();
		}
		String weightUnint = unitMap.get("weightUnit").get(country);
		return weightUnint;
	}
	
	public String getSLWeightUnit(String country) {
		if (unitMap == null) {
			unitMap = getUnit();
		}
		String weightUnint = unitMap.get("slweightUnit").get(country);
		return weightUnint;
	}

 

 

	public BigDecimal getPrepServiceFee(boolean isStandard, String country, String category) {
		BigDecimal prepServiceFee = new BigDecimal("0");
		return prepServiceFee;
	}
	
	//??????FBA Labeling Service Fees
	public BigDecimal getLabelServiceFee(boolean isStandard, String country, boolean isSmlAndLight){
		BigDecimal labelingFee = new BigDecimal("0");
		FBALabelingFee labeling = null;
		if("US".equals(country)){
			labeling = fbaLabelingFeeService.getPriceByProductTierId(isSmlAndLight, country);
		} else {
			labeling = fbaLabelingFeeService.getPriceByProductTierId(isStandard, country);
		}
		if (labeling!=null) {
			labelingFee = labeling.getPrice();
		}
		return labelingFee;
	}

	List<String> categoryList = new ArrayList<String>();

 
	
	
	public static List<String> eulist = null;
	public boolean isEUNotUK(String country) {
		if(eulist==null){
			eulist = new ArrayList<String>();
			eulist.add("DE");
			eulist.add("FR");
			eulist.add("IT");
			eulist.add("ES");
			eulist.add("NL");
			eulist.add("PL");
			eulist.add("SE");
			eulist.add("TR");
			eulist.add("SA");
			eulist.add("EG");
			
		}
		return eulist.contains(country);
	}

	public IProfitService factoryProfitServiceByCountry(String country) {
		IProfitService result=null;
		if (isEUNotUK(country)) {
			result=(IProfitService) SpringUtil.getBean("EUProfitService");
		} else {
			result=(IProfitService) SpringUtil.getBean(country+"ProfitService");
		}
		return result;
	} 
	
	public InputDimensions initDimensionSort(InputDimensions inputDimension) {
		if (inputDimension != null) {
			List<BigDecimal> list = new ArrayList<BigDecimal>();
			list.add(inputDimension.getLength().getValue());
			list.add(inputDimension.getWidth().getValue());
			list.add(inputDimension.getHeight().getValue());
			Collections.sort(list);
			inputDimension.setLength(new ItemMeasure(list.get(2), inputDimension.getLength().getUnits()));
			inputDimension.setWidth(new ItemMeasure(list.get(1), inputDimension.getWidth().getUnits()));
			inputDimension.setHeight(new ItemMeasure(list.get(0), inputDimension.getHeight().getUnits()));
		}
		return inputDimension;
	}

	public Map<String, String> getProductTier(ProfitConfigCountry profitConfigCountry, InputDimensions inputDimension, String country, String isMedia,
			BigDecimal price, boolean isSmlAndLight, String type) throws BizException {
		Map<String, String> result = new HashMap<String, String>();
		initDimensionSort(inputDimension);// ???????????????????????????????????????
		IProfitService profitServiceX = null;
		try {
			profitServiceX = factoryProfitServiceByCountry(country);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (profitServiceX != null) {
			InputDimensions inputDimensionnew = new InputDimensions(inputDimension.getLength().getValue(),
					inputDimension.getWidth().getValue(), inputDimension.getHeight().getValue(),
					inputDimension.getLength().getUnits(), inputDimension.getWeight().getValue(),
					inputDimension.getWeight().getUnits());
			
			String productTierId = null;
			String productTierName = null;
			BigDecimal outboundWeight = new BigDecimal("0");
			BigDecimal FBA = new BigDecimal("0");
			
			if (isSmlAndLight && Arrays.asList(smlAndLightCountry).contains(country)) {
				productTierId = profitServiceX.determineSmlProductTier(country, inputDimensionnew, profitConfigCountry);// ????????????
				if (productTierId != null) {
					ProductTier productTier = productTierService.selectByPKey(productTierId);
					productTierName = productTier.getAmzName()==null?productTier.getName():productTier.getAmzName();
					outboundWeight = profitServiceX.getSLOutboundWeight(country, productTier, inputDimensionnew);// ????????????
					FBA = profitServiceX.calculateSmlFBA(country, productTierId, outboundWeight,profitConfigCountry);//???????????????????????????
				} else {//????????????????????????????????????????????????????????????
					isSmlAndLight = false;
					productTierId = profitServiceX.determineProductTier(country, inputDimensionnew, isMedia);
					if (productTierId != null) {
						ProductTier productTier = productTierService.selectByPKey(productTierId);
						productTierName = productTier.getAmzName()==null?productTier.getName():productTier.getAmzName();
						outboundWeight = profitServiceX.determineOutboundWeight(country, productTier, inputDimensionnew, isMedia);
						FBA = profitServiceX.calculateFBA(country, productTierId, inputDimensionnew, isMedia, type,
								outboundWeight, profitConfigCountry, "national");
					}
				}

			} else {
				productTierId = profitServiceX.determineProductTier(country, inputDimensionnew, isMedia);
				if (productTierId != null) {
					ProductTier productTier = productTierService.selectByPKey(productTierId);
					productTierName = productTier.getAmzName()==null?productTier.getName():productTier.getAmzName();
					outboundWeight = profitServiceX.determineOutboundWeight(country, productTier, inputDimensionnew,
							isMedia);
					FBA = profitServiceX.calculateFBA(country, productTierId, inputDimensionnew, isMedia, type,
							outboundWeight, profitConfigCountry, "national");
				}
			}
			
			if (productTierName != null) {
				if ("US".equals(country)&&!isSmlAndLight) {
					if (outboundWeight.compareTo(new BigDecimal("1"))==-1) {//outboundWeight??????1
						outboundWeight = outboundWeight.multiply(new BigDecimal("16")).setScale(0, BigDecimal.ROUND_CEILING);//??????????????????
						result.put("productTier", productTierName + "-" + outboundWeight + "oz");
					} else {
						result.put("productTier", productTierName + "-" + outboundWeight + "lb");
					}
				} else if (("UK".equals(country) || isEUNotUK(country))&&!isSmlAndLight) {
					BigDecimal weight = profitServiceX.getFBAFormatWeight(country, productTierId, inputDimension, outboundWeight, profitConfigCountry);
					result.put("productTier", productTierName + "-" + outboundWeight + "-" +weight+ "g");
				} else {
					result.put("productTier", productTierName);
				}
				result.put("productTierId", productTierId);
				result.put("fba", FBA.toString());
				result.put("outboundWeight", outboundWeight.toString());
				String fcurrency = formatCurrency(getCurrencyUnit(country));
				result.put("fcurrency", fcurrency);
			}
		}
		return result;
	}
	

	//????????????????????????
	public CostDetail initCostDetail(String country, ProfitConfig profitCfgAll, InputDimensions inputDimension,
			String isMedia, String type, int typeId, BigDecimal cost, BigDecimal shipment, String currency,
			String category, String shipmentType, BigDecimal declaredValue, String declaredValueCur,
			BigDecimal taxrate, BigDecimal gstrate, BigDecimal sellingGSTRate,BigDecimal referralrate, boolean isSmlAndLight) 
					throws BizException {
		// ???????????????????????????????????????
		initDimensionSort(inputDimension);

		CostDetail costDetail = new CostDetail();
		costDetail.setCountry(country);
		////??????????????????????????????????????????????????????
		String fcurrency = formatCurrency(getCurrencyUnit(country));
		costDetail.setCurrency(fcurrency);//?????????????????????$
		
		ProfitConfigCountry profitConfigX = profitCfgAll.getProfitConfigCountry(country);//?????????????????????????????????
		if(profitConfigX==null){
			return null;
		}
		IProfitService profitServiceX = null;
		try {
			profitServiceX = factoryProfitServiceByCountry(country);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (profitServiceX==null) {
			return null;
		}
		////????????????
		String to = getCurrencyUnit(country);
		cost = this.exchangeRateHandlerService.changeCurrencyByLocal(currency, to, cost);//????????????
		costDetail.setPurchase(cost);
		//// ????????????
		shipment = calculateShipment(profitCfgAll, profitConfigX, currency, to, inputDimension,shipment);
		if (shipment == null) {
			return null;
		}
		costDetail.setShipment(shipment);
		
		String channel = profitCfgAll.getSalesChannel();
		boolean isStandard = true;
		if (channel.contains("fba")) {// ???????????????????????????FBA????????????????????????FBA
			if (isSmlAndLight && Arrays.asList(smlAndLightCountry).contains(country)) {
				String productTierId = profitServiceX.determineSmlProductTier(country, inputDimension,profitConfigX);// ????????????
				if (productTierId == null) {
					return null;
				}
				if(country.equals("UK")&&profitConfigX.getFenpeiType().equals("EFN")) {
					return null;
				}
				costDetail.setProductTierId(productTierId);
				ProductTier productTier = productTierService.selectByPKey(productTierId);
				String productTierName = productTier.getName();
				costDetail.setProductTier(productTierName);
				BigDecimal outboundWeight = profitServiceX.getSLOutboundWeight(country, productTier, inputDimension);
				costDetail.setOutboundWeight(outboundWeight);

				BigDecimal FBA = profitServiceX.calculateSmlFBA(country, productTierId, outboundWeight,profitConfigX);
				costDetail.setFBA(FBA);
				
			} else {
				String productTierId = profitServiceX.determineProductTier(country, inputDimension, isMedia);
				if (productTierId == null) {
					return null;
				}
				costDetail.setProductTierId(productTierId);
				ProductTier productTier = productTierService.selectByPKey(productTierId);
				String productTierName = productTier.getName();
				isStandard = productTier.getIsstandard();
				BigDecimal outboundWeight = profitServiceX.determineOutboundWeight(country, productTier, inputDimension,
						isMedia);
				costDetail.setProductTier(productTierName);
				costDetail.setOutboundWeight(outboundWeight);

				BigDecimal manualProcessingFee = profitConfigX.getManualProcessing();// ???????????????????????????
				costDetail.setManualProcessingFee(manualProcessingFee);

				BigDecimal inPlaceFee = profitServiceX.calculateInPlaceFee(country, productTier, inputDimension,
						profitConfigX.getInvplacefee());// ????????????????????????????????????????????????????????????????????????????????????????????????????????????,??????US???JP?????????????????????
				costDetail.setInPlaceFee(inPlaceFee);

				// boolean prepService = profitConfigX.getPrepservice();
				// if (prepService) {//????????????prep Service fee
				// BigDecimal prepServiceFee =
				// getPrepServiceFee(isStandard,country,category);
				// costDetail.setPrepServiceFee(prepServiceFee);
				// }

				BigDecimal FBA = profitServiceX.calculateFBA(country, productTierId, inputDimension, isMedia, type,
						outboundWeight, profitConfigX, shipmentType);
				costDetail.setFBA(FBA);
			}

			boolean labelService = profitConfigX.getLabelService();
			if (labelService) {
				BigDecimal labelServiceFee = getLabelServiceFee(isStandard, country, isSmlAndLight);
				costDetail.setLabelServiceFee(labelServiceFee);
			}
			BigDecimal storageFee = profitServiceX.calculateStorageFee(country, profitConfigX, inputDimension,
					isStandard);
			costDetail.setStorageFee(storageFee);

			// ???????????????GST ??????
			BigDecimal fbaGSTTax = profitConfigX.getFbaTaxes().divide(new BigDecimal("100"), 4,
					BigDecimal.ROUND_HALF_UP);
			costDetail.setFbaTax(fbaGSTTax);
			if ("CA".equals(country)) {
				BigDecimal fbaTaxFee = costDetail.getFBA().multiply(fbaGSTTax).setScale(4, BigDecimal.ROUND_HALF_UP);
				costDetail.setFbaTaxFee(fbaTaxFee);
			}
			//////////////////////////// ???????????????????????????, Fixed Closing
			//////////////////////////// Fee??????????????????????????????????????????,???????????????????????????///////////////
			if ("IN".equals(country)) {
				// ???????????????GST????????????18%, ??????????????????????????????, Fixed Closing Fee, FBA fee???
				BigDecimal fbaTaxFee = costDetail.getFBA().multiply(fbaGSTTax).setScale(4, BigDecimal.ROUND_HALF_UP);
				costDetail.setFbaTaxFee(fbaTaxFee);
			}

		} else if (channel.contains("byself")) {// ???????????????
			if (isSmlAndLight) {
				return null;
			}
			if ("IN".equals(country)) {// ???????????????FBA
				return null;
			}
			BigDecimal VCFee = profitServiceX.calculateVCFee(country, isMedia, typeId);
			costDetail.setVCFee(VCFee);
		}
		
		String sellerPlan = profitCfgAll.getSellerplan();//????????????????????????????????????
 
		
		//??????????????????????????????100??????????????????????????????
		if (referralrate!=null) {
			referralrate = referralrate.divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		}
		//Currency Transport fee ???????????????????????? ?????? ??????????????????????????????(FBA???, ??????, storage??????) , ??????????????? X ???????????????
		BigDecimal lostrate = profitConfigX.getLostrate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);//Currency Transport fee
		BigDecimal sellerrate = profitConfigX.getSellerrate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);//Marketing
		BigDecimal costrate = profitConfigX.getCostrate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);//????????????????????????????????????
//		BigDecimal promotion = profitConfigX.getPromotion().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);//all store promotion
		BigDecimal rateFee = sellerrate.add(costrate);//????????????*rateFee?????????
		BigDecimal others = profitConfigX.getOtherfee();//OTHERS
		//VAT???????????????
		BigDecimal vatRate=profitConfigX.getVatRate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		if (!"IN".equals(country) || taxrate == null || gstrate == null || sellingGSTRate == null) {
			taxrate = profitConfigX.getTaxrate();
			gstrate = profitConfigX.getGstrate();
			sellingGSTRate = profitConfigX.getSellingGSTRate();
		}
		//??????????????????
		taxrate = taxrate.divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		//??????GST??????
		gstrate = gstrate.divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		//??????GST??????
		sellingGSTRate=sellingGSTRate.divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		//??????????????????
		BigDecimal corporateInRate=profitConfigX.getCorporateInRate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		
		costDetail.setCurrencyTransportRate(lostrate);
		costDetail.setMarketingRate(sellerrate);
		costDetail.setOtherRate(costrate);
		costDetail.setRateFee(rateFee);
//		costDetail.setPromotionRate(promotion);
		costDetail.setOthers(others);
		costDetail.setVatRate(vatRate);
		costDetail.setTaxRate(taxrate);
		costDetail.setGstrate(gstrate);
		costDetail.setSelling_GSTRate(sellingGSTRate);
		costDetail.setCorporateInRate(corporateInRate);
		
		if ("IN".equals(country) && profitConfigX.isHasDeclaredValue()) {
			if (declaredValue == null) {// ??????????????????????????????????????????declaredValue==null
				return null;
			}
			declaredValue = this.exchangeRateHandlerService.changeCurrencyByLocal(declaredValueCur, to, declaredValue);// ????????????
		} else {
			declaredValue = cost.add(shipment);// ???????????? = ?????????+??????
		}
		//????????????=???????????? * ????????????
		BigDecimal tax = declaredValue.multiply(taxrate).setScale(4, BigDecimal.ROUND_HALF_UP);
		costDetail.setTax(tax);
		//??????GST??????=(????????????+??????) * GST??????
		BigDecimal import_gst=(new BigDecimal("1").add(taxrate)).multiply(declaredValue)
				.multiply(gstrate).setScale(4, BigDecimal.ROUND_HALF_UP);
		costDetail.setImport_GST(import_gst);
		
		List<Map<String, String>> resultList = calculateSellingPrice(costDetail, typeId, country, referralrate);// ????????????????????????????????????
		costDetail.setResultList(resultList);
		return costDetail;
	}
	
	BigDecimal calculateShipment(ProfitConfig profitcfg,ProfitConfigCountry profitConfigX,String currency, String to, InputDimensions inputDimension,BigDecimal shipment){
		if (inputDimension==null||profitConfigX==null) {
			return new BigDecimal("0");
		}
		if (profitcfg.getShipmentstyle()!=null&&profitcfg.getShipmentstyle().equals("manually")&&shipment==null) {
			return new BigDecimal("0");
		}
		if(shipment!=null &&shipment.floatValue()>0.0001) {//profitcfg.getShipmentstyle().equals("manually")??????????????????????????????shipment??????????????????????????? 
			shipment = this.exchangeRateHandlerService.changeCurrencyByLocal(currency, to, shipment); 
		} else if (profitcfg.getShipmentstyle().equals("weight")) {//???????????????,RMB/kg
			shipment = inputDimension.getWeight(InputDimensions.unit_kg).getValue().multiply(profitConfigX.getConstantw());
			shipment = this.exchangeRateHandlerService.changeCurrencyByLocal("RMB", to, shipment);
		} else if (profitcfg.getShipmentstyle().equals("dim_weight")) {//??????????????????????????????,RMB/kg
			BigDecimal volum = inputDimension.getVolume(InputDimensions.unit_cm).getValue();
			if (volum==null) {
				return null;
			}
			BigDecimal constantd = profitConfigX.getConstantd();
			BigDecimal dimensionalWeight = volum.divide(constantd,8, BigDecimal.ROUND_HALF_UP);
			BigDecimal weight = inputDimension.getWeight(InputDimensions.unit_kg).getValue();
			BigDecimal greater = null;
			if (weight == null && dimensionalWeight != null) {
				greater = dimensionalWeight;
			} else if (weight != null && dimensionalWeight != null) {
				greater = weight.subtract(dimensionalWeight).doubleValue() > 0 ? weight : dimensionalWeight;
			} else {
				return null;
			}
			shipment = greater.multiply(profitConfigX.getConstantm());
			shipment = this.exchangeRateHandlerService.changeCurrencyByLocal("RMB", to, shipment);
		}else if(profitcfg.getShipmentstyle().equals("volume")) {//???????????????,RMB/cm
			BigDecimal volum = inputDimension.getVolume(InputDimensions.unit_cm).getValue();
			if (volum==null) {
				return null;
			}
			BigDecimal constantd = profitConfigX.getConstantd();
			BigDecimal dimensionalWeight = volum.divide(constantd,4, BigDecimal.ROUND_HALF_UP);
			if(dimensionalWeight==null) {
				return null;
			}
			shipment = dimensionalWeight.multiply(profitConfigX.getConstantm());
			shipment = this.exchangeRateHandlerService.changeCurrencyByLocal("RMB", to, shipment);
		}
		return shipment;
			
	}
	
	//????????????????????????????????????????????????????????????????????????t_fba_estimated_fees???????????????????????????
	public CostDetail getProfitByLocalData(String country, ProfitConfig profitcfg, InputDimensions inputDimension_amz, InputDimensions inputDimension_local,
			String isMedia, String type, int typeid, BigDecimal cost, String currency, BigDecimal price, String shipmentType,
			boolean isSmlAndLight,BigDecimal shipmentfee) throws BizException {
		if (inputDimension_amz == null || inputDimension_local==null)
			return null;
		// ???????????????????????????????????????
		initDimensionSort(inputDimension_amz);
		initDimensionSort(inputDimension_local);

		CostDetail costDetail = new CostDetail();
		costDetail.setCountry(country);
		////??????????????????????????????????????????????????????
		String fcurrency = formatCurrency(getCurrencyUnit(country));
		costDetail.setCurrency(fcurrency);//?????????????????????$
		////????????????
		String to = getCurrencyUnit(country);
		cost = this.exchangeRateHandlerService.changeCurrencyByLocal(currency, to, cost);//????????????
		costDetail.setPurchase(cost);
		
		ProfitConfigCountry profitConfigX = profitcfg.getProfitConfigCountry(country);//?????????????????????????????????
		if(profitConfigX==null){
			return null;
		}
		////????????????
		BigDecimal shipment = calculateShipment(profitcfg, profitConfigX, currency, to, inputDimension_local,shipmentfee);
		if (shipment==null) {
			return null;
		}
		costDetail.setShipment(shipment);
		// ?????????????????? Amazon Referral Fee
		BigDecimal referralFee = this.calculateReferralFee(typeid, price, country);
		costDetail.setReferralFee(referralFee);
		
		IProfitService profitServiceX = null;
		try {
			profitServiceX = factoryProfitServiceByCountry(country);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (profitServiceX==null) {
			return null;
		}
		String channel = profitcfg.getSalesChannel();
		if (channel.contains("fba")) {// ???????????????????????????FBA????????????????????????FBA
			Map<String, String> result = this.getProductTier(profitConfigX, inputDimension_amz, country, isMedia, price, isSmlAndLight,type);
			String productTierId = result.get("productTierId");
			if (productTierId==null) {
				return null;
			} 
			costDetail.setProductTierId(productTierId);
			costDetail.setProductTier(result.get("productTier"));
			costDetail.setOutboundWeight(new BigDecimal(result.get("outboundWeight")));
			BigDecimal fba = new BigDecimal(result.get("fba"));
			costDetail.setFBA(fba);
			
			BigDecimal manualProcessingFee = profitConfigX.getManualProcessing();// ???????????????????????????
			costDetail.setManualProcessingFee(manualProcessingFee);

			ProductTier productTier = productTierService.selectByPKey(productTierId);
			boolean isStandard = productTier.getIsstandard();
			boolean labelService = profitConfigX.getLabelService();
			if (labelService) {
				BigDecimal labelServiceFee = getLabelServiceFee(isStandard, country, isSmlAndLight);
				costDetail.setLabelServiceFee(labelServiceFee);
			}
			
			BigDecimal inPlaceFee = profitServiceX.calculateInPlaceFee(country, productTier, inputDimension_amz,
					profitConfigX.getInvplacefee());// ????????????????????????????????????????????????????????????????????????????????????????????????????????????,??????US???JP?????????????????????
			costDetail.setInPlaceFee(inPlaceFee);

			BigDecimal storageFee = profitServiceX.calculateStorageFee(country, profitConfigX, inputDimension_amz,
					isStandard);
			costDetail.setStorageFee(storageFee);

			//UK???2020-09-01?????????2%???????????????
			if("UK".equals(country)){
				BigDecimal dst = fba.add(storageFee).add(referralFee).multiply(new BigDecimal("0.02"));
			}
			
			// ???????????????GST ??????
			BigDecimal fbaGSTTax = profitConfigX.getFbaTaxes().divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
			costDetail.setFbaTax(fbaGSTTax);
			if ("CA".equals(country)) {
				BigDecimal fbaTaxFee = costDetail.getFBA().multiply(fbaGSTTax).setScale(4, BigDecimal.ROUND_HALF_UP);
				costDetail.setFbaTaxFee(fbaTaxFee);
			}
			if ("IN".equals(country)) {
				//Fixed closing fee,????????????????????????????????????????????????
				BigDecimal closingFee = profitServiceX.calculateClosingFee(costDetail.getCountry(),price);
				costDetail.setClosingFee(closingFee);
				//???????????????GST????????????18%, ??????????????????????????????, Fixed Closing Fee, FBA fee???
				BigDecimal fbaTaxFee=costDetail.getFBA().add(closingFee).add(referralFee).multiply(fbaGSTTax).setScale(4, BigDecimal.ROUND_HALF_UP);
				costDetail.setFbaTaxFee(fbaTaxFee);
			}

		} else if (channel.contains("byself")) {// ???????????????
			if (isSmlAndLight) {
				return null;
			}
			if ("IN".equals(country)) {// ???????????????FBA
				return null;
			}
			BigDecimal VCFee = profitServiceX.calculateVCFee(country, isMedia, typeid);
			costDetail.setVCFee(VCFee);
		}
		
		//Currency Transport fee ???????????????????????? ?????? ??????????????????????????????(FBA???, ??????, storage??????) , ??????????????? X ???????????????
		BigDecimal lostrate = profitConfigX.getLostrate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);//Currency Transport fee
		BigDecimal sellerrate = profitConfigX.getSellerrate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);//Marketing
		BigDecimal costrate = profitConfigX.getCostrate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);//????????????????????????????????????
//		BigDecimal promotion = profitConfigX.getPromotion().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);//all store promotion
		BigDecimal rateFee = sellerrate.add(costrate);//????????????*rateFee?????????
		BigDecimal others = profitConfigX.getOtherfee();//OTHERS
		//VAT???????????????
		BigDecimal vatRate=profitConfigX.getVatRate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		//??????????????????
		BigDecimal taxrate = profitConfigX.getTaxrate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		//??????GST??????
		BigDecimal gstrate=profitConfigX.getGstrate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		//??????GST??????
		BigDecimal sellingGSTRate=profitConfigX.getSellingGSTRate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		//??????????????????
		BigDecimal corporateInRate=profitConfigX.getCorporateInRate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		
		costDetail.setCurrencyTransportRate(lostrate);
		costDetail.setMarketingRate(sellerrate);
		costDetail.setOtherRate(costrate);
		costDetail.setRateFee(rateFee);
//		costDetail.setPromotionRate(promotion);
		costDetail.setOthers(others);
		costDetail.setVatRate(vatRate);
		costDetail.setTaxRate(taxrate);
		costDetail.setGstrate(gstrate);
		costDetail.setSelling_GSTRate(sellingGSTRate);
		costDetail.setCorporateInRate(corporateInRate);
		
		BigDecimal declaredValue=null;
		if("IN".equals(country)&&profitConfigX.isHasDeclaredValue()) {
			return null;
		} else {
			declaredValue = cost.add(shipment);//???????????? = ?????????+??????
		}
		//????????????=???????????? * ????????????
		BigDecimal tax = declaredValue.multiply(taxrate).setScale(4, BigDecimal.ROUND_HALF_UP);
		costDetail.setTax(tax);
		//??????GST??????=(????????????+??????) * GST??????
		BigDecimal import_gst=(new BigDecimal("1").add(taxrate)).multiply(declaredValue)
				.multiply(gstrate).setScale(4, BigDecimal.ROUND_HALF_UP);
		costDetail.setImport_GST(import_gst);
		BigDecimal FBA = getAmazonFeeButRef(costDetail);//??????????????????????????????????????????????????????
		
		// ???????????????=[??????-FBA?????????????????????????????????-??????]*lostrate;
		BigDecimal currencyLostFee = price.subtract(FBA.add(costDetail.getFbaTaxFee())).subtract(referralFee)
				.multiply(costDetail.getCurrencyTransportRate());
		currencyLostFee = currencyLostFee.floatValue()>0?currencyLostFee:new BigDecimal("0");
		// vat????????????=??????*vat/(1+vat)
		BigDecimal vatFee = price.multiply(costDetail.getVatRate()
				.divide(costDetail.getVatRate().add(new BigDecimal("1")), 4, BigDecimal.ROUND_HALF_UP));
		// ??????GST??????=????????????*gst/(1+gst)
		BigDecimal selling_GST = price.multiply(costDetail.getSelling_GSTRate()
				.divide(costDetail.getSelling_GSTRate().add(new BigDecimal("1")), 4, BigDecimal.ROUND_HALF_UP));
		// ??????????????? = ?????? * (????????? - ??????????????????(????????????GST???, FBA???GST???));
		BigDecimal corporateInFee = price
				.subtract(costDetail.getPurchase().add(costDetail.getShipment()).add(costDetail.getOthers())
						.add(FBA).add(referralFee).add(price.multiply(costDetail.getRateFee())).add(currencyLostFee)
						.add(vatFee).add(selling_GST).add(costDetail.getTax()))
				.multiply(costDetail.getCorporateInRate());
		corporateInFee = corporateInFee.floatValue()>0?corporateInFee:new BigDecimal("0");
		// ??????=purchase+shipMengt+others+FBA?????????????????????????????????+??????+??????*??????+???????????????+vat+??????GST???+????????????+??????GST??????;
		BigDecimal totalCost = costDetail.getPurchase().add(costDetail.getShipment()).add(costDetail.getOthers())
				.add(FBA).add(costDetail.getFbaTaxFee()).add(referralFee)
				.add(price.multiply(costDetail.getRateFee())).add(currencyLostFee).add(vatFee)
				.add(costDetail.getTax()).add(costDetail.getImport_GST()).add(selling_GST).add(corporateInFee);// ??????????????????
		if(price!=null) {
			//??????????????????????????????
			DecimalFormat df2 = new DecimalFormat("0.0000");// ??????4?????????
			costDetail.setCurrencyTransportFee(new BigDecimal(df.format(currencyLostFee)+""));
			costDetail.setVatFee(new BigDecimal(df.format(vatFee)+""));
			costDetail.setSelling_GST(new BigDecimal(df.format(selling_GST)+""));
			costDetail.setCorporateInFee(new BigDecimal(df.format(corporateInFee)+""));
			costDetail.setTotalCost(new BigDecimal(df.format(totalCost)+""));
			
			costDetail.setReferralFee(new BigDecimal(df.format(referralFee)+""));
			costDetail.setPurchase(new BigDecimal(df.format(costDetail.getPurchase())+""));
			costDetail.setShipment(new BigDecimal(df.format(costDetail.getShipment())+""));
			costDetail.setVCFee(new BigDecimal(df.format(costDetail.getVCFee())+""));
			costDetail.setInPlaceFee(new BigDecimal(df.format(costDetail.getInPlaceFee())+""));
			costDetail.setStorageFee(new BigDecimal(df2.format(costDetail.getStorageFee())+""));
			costDetail.setFbaTaxFee(new BigDecimal(df.format(costDetail.getFbaTaxFee())+""));
			costDetail.setTax(new BigDecimal(df.format(costDetail.getTax())+""));
			costDetail.setImport_GST(new BigDecimal(df.format(costDetail.getImport_GST())+""));
			costDetail.setMarketing(new BigDecimal(df.format(costDetail.getMarketingRate().multiply(price))+""));
			costDetail.setPromotionFee(new BigDecimal(df.format(costDetail.getPromotionRate().multiply(price))+""));
			costDetail.setOthersFee(new BigDecimal(df.format(costDetail.getOtherRate().multiply(price))+""));
		}
		BigDecimal profit = price.subtract(totalCost);
		BigDecimal margin = profit.multiply(new BigDecimal("100")).divide(price, 2, BigDecimal.ROUND_HALF_EVEN);
		costDetail.setMargin(margin+"%");
		costDetail.setProfit(profit);
		return costDetail;
	}
	
	//????????????????????????????????????????????????????????????????????????t_fba_estimated_fees???????????????????????????
	public CostDetail getProfitByAmazonData(String country, ProfitConfig profitCfgAll, InputDimensions inputDimension_local, String isMedia, 
			BigDecimal cost, String currency, BigDecimal price, FBAEstimatedFee fbaFee, ReferralFee ref,boolean isSmlAndLight, BigDecimal shipmentfee) throws BizException {
		// ?????????????????????????????????
		if(inputDimension_local == null||fbaFee==null) {
			return null;
		}
		InputDimensions inputDimensionNew = new InputDimensions();
		inputDimensionNew.setLength(new ItemMeasure(fbaFee.getLongestSide(), getDimUnit(country)));
		inputDimensionNew.setWidth(new ItemMeasure(fbaFee.getMedianSide(), getDimUnit(country)));
		inputDimensionNew.setHeight(new ItemMeasure(fbaFee.getShortestSide(), getDimUnit(country)));
		if ("grams".equalsIgnoreCase(fbaFee.getUnitOfWeight())) {
			if(fbaFee.getItemPackageWeight()!=null){
				BigDecimal weight = fbaFee.getItemPackageWeight().divide(new BigDecimal("1000"), 4, BigDecimal.ROUND_HALF_UP);
				inputDimensionNew.setWeight(new ItemMeasure(weight,getWeightUnit(country)));
			} else {
				inputDimensionNew.setWeight(inputDimension_local.getWeight());
			}
		} else {
			inputDimensionNew.setWeight(new ItemMeasure(fbaFee.getItemPackageWeight(), getWeightUnit(country)));
		}
		if(fbaFee.getEstimatedFeeTotal()==null||fbaFee.getEstimatedReferralFeePerUnit()==null) return null;
		
		CostDetail costDetail = new CostDetail(); 
		costDetail.setCountry(country);
		////??????????????????????????????????????????????????????
		String fcurrency = formatCurrency(getCurrencyUnit(country));
		costDetail.setCurrency(fcurrency);//?????????????????????$
		////????????????
		String to = getCurrencyUnit(country);
		cost = this.exchangeRateHandlerService.changeCurrencyByLocal(currency, to, cost);//????????????
		costDetail.setPurchase(cost);
		
		ProfitConfigCountry profitConfigX = profitCfgAll.getProfitConfigCountry(country);//?????????????????????????????????
		////????????????
		BigDecimal shipment = calculateShipment(profitCfgAll, profitConfigX, currency, to, inputDimension_local,shipmentfee);
		if (shipment==null) {
			return null;
		}
		costDetail.setShipment(shipment);
		
		// ?????????????????? Amazon Referral Fee
		BigDecimal referralFee = new BigDecimal("0");
//		referralFeeService.calculateReferralFee(typeid, price, country);
		if(price.setScale(2,BigDecimal.ROUND_HALF_EVEN).floatValue()==fbaFee.getYourPrice().floatValue()){
			referralFee = fbaFee.getEstimatedReferralFeePerUnit();
		} else {
			BigDecimal referralRate = fbaFee.getEstimatedReferralFeePerUnit().divide(fbaFee.getYourPrice(),2,BigDecimal.ROUND_HALF_EVEN);//??????=??????/?????????
			referralFee = price.multiply(referralRate).setScale(2,BigDecimal.ROUND_HALF_EVEN);
			if(ref!=null&&ref.getLoweast()!=null&&referralFee!=null&&ref.getLoweast().floatValue()>referralFee.floatValue()){
				referralFee = ref.getLoweast();
			}
		}
		costDetail.setReferralFee(referralFee);
		BigDecimal fba_report = fbaFee.getEstimatedFeeTotal().subtract(fbaFee.getEstimatedReferralFeePerUnit());
		String channel = profitCfgAll.getSalesChannel();
		if (channel.contains("fba")) {// ???????????????????????????FBA
			IProfitService profitServiceX = null;
			try {
				profitServiceX = factoryProfitServiceByCountry(country);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			if (profitServiceX==null) {
				return null;
			}
			String productTierId = profitServiceX.determineProductTier(country,inputDimensionNew, isMedia);
			if (productTierId==null) {
				return null;
			}
			
			BigDecimal edfba = fbaFee.getExpectedDomesticFulfillmentFeePerUnit();// uk
			BigDecimal efnfba = fbaFee.getExpectedEfnFulfilmentFeePerUnitUk();// de,fr,it,es
			BigDecimal fixclosingFee = fbaFee.getEstimatedFixedClosingFee();
			if (edfba == null)
				edfba = new BigDecimal("0");
			if (efnfba == null)
				efnfba = new BigDecimal("0");
			String fenpeiType = profitConfigX.getFenpeiType();
			BigDecimal fba = null;
			if ("EFN".equals(fenpeiType) && !"UK".equals(country)) {
				fba  = fba_report.subtract(edfba).add(efnfba);
			} else {
				fba = fba_report;
			}
			costDetail.setFBA(fba);// ?????????????????????
			
			if ("IN".equals(country)) {
				if (fixclosingFee!=null) {
					fba = fba.subtract(fixclosingFee);
					costDetail.setFBA(fba);
				} else {
					//Fixed closing fee,????????????????????????????????????????????????
					fixclosingFee = profitServiceX.calculateClosingFee(costDetail.getCountry(),price);
				}
				costDetail.setClosingFee(fixclosingFee);
				//???????????????GST????????????18%, ??????????????????????????????, Fixed Closing Fee, FBA fee???
				BigDecimal fbaTaxFee=fba.add(referralFee).add(fixclosingFee).multiply(profitConfigX.getFbaTaxes()).divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
				costDetail.setFbaTaxFee(fbaTaxFee);
			}
			if ("CA".equals(country)) {
				BigDecimal fbaTaxFee = fba.multiply(profitConfigX.getFbaTaxes()).divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
				costDetail.setFbaTaxFee(fbaTaxFee);
			}
			
			ProductTier productTier = productTierService.selectByPKey(productTierId);
			boolean isStandard = productTier.getIsstandard();
			
			BigDecimal manualProcessingFee = profitConfigX.getManualProcessing();// ???????????????????????????
			costDetail.setManualProcessingFee(manualProcessingFee);

			boolean labelService = profitConfigX.getLabelService();
			BigDecimal labelServiceFee = null;
			if (labelService) {
				labelServiceFee = getLabelServiceFee(isStandard, country, isSmlAndLight);
				costDetail.setLabelServiceFee(labelServiceFee);
			}
			
			BigDecimal inPlaceFee = profitServiceX.calculateInPlaceFee(country, productTier, inputDimensionNew,
					profitConfigX.getInvplacefee());// ????????????????????????????????????????????????????????????????????????????????????????????????????????????,??????US???JP?????????????????????
			costDetail.setInPlaceFee(inPlaceFee);

			BigDecimal storageFee = profitServiceX.calculateStorageFee(country, profitConfigX, inputDimensionNew, isStandard);
			costDetail.setStorageFee(storageFee);
			
			//UK???2020-09-01?????????2%???????????????
			if("UK".equals(country)){
				BigDecimal dst = fba.add(storageFee).add(referralFee).multiply(new BigDecimal("0.02"));
			}
			
		} else {
			if ("IN".equals(country)) {// ???????????????FBA
				return null;
			}
			costDetail.setVCFee(fba_report);//?????????????????????
		}
		
		//??????????????????????????????100??????????????????????????????
		//Currency Transport fee ???????????????????????? ?????? ??????????????????????????????(FBA???, ??????, storage??????) , ??????????????? X ???????????????
		BigDecimal lostrate = profitConfigX.getLostrate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);//Currency Transport fee
		BigDecimal sellerrate = profitConfigX.getSellerrate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);//Marketing
		BigDecimal costrate = profitConfigX.getCostrate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);//????????????????????????????????????
		BigDecimal rateFee = sellerrate.add(costrate);//????????????*rateFee?????????
		BigDecimal others = profitConfigX.getOtherfee();//OTHERS
		BigDecimal vatRate=profitConfigX.getVatRate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		//??????????????????
		BigDecimal taxrate = profitConfigX.getTaxrate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		//??????GST??????
		BigDecimal GSTRate=profitConfigX.getGstrate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		//??????GST??????
		BigDecimal selling_GSTRate=profitConfigX.getSellingGSTRate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		//??????????????????
		BigDecimal corporateInRate=profitConfigX.getCorporateInRate().divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		
		costDetail.setCurrencyTransportRate(lostrate);
		costDetail.setMarketingRate(sellerrate);
		costDetail.setOtherRate(costrate);
		costDetail.setRateFee(rateFee);
		costDetail.setOthers(others);
		costDetail.setVatRate(vatRate);
		costDetail.setTaxRate(taxrate);
		costDetail.setGstrate(GSTRate);
		costDetail.setSelling_GSTRate(selling_GSTRate);
		costDetail.setCorporateInRate(corporateInRate);
				
		BigDecimal FBA = getAmazonFeeButRef(costDetail);//??????????????????????????????????????????????????????
		FBA = FBA.add(costDetail.getReferralFee());//???????????????????????????
		BigDecimal declaredValue=null;
		if("IN".equals(country)&&profitConfigX.isHasDeclaredValue()) {
			return null;
		} else {
			declaredValue = cost.add(shipment);//???????????? = ?????????+??????
		}
		//????????????=???????????? * ????????????
		BigDecimal tax = declaredValue.multiply(taxrate).setScale(4, BigDecimal.ROUND_HALF_UP);
		costDetail.setTax(tax);
		//??????GST??????=(????????????+??????) * GST??????
		BigDecimal import_gst=(new BigDecimal("1").add(taxrate)).multiply(declaredValue)
				.multiply(GSTRate).setScale(4, BigDecimal.ROUND_HALF_UP);
		costDetail.setImport_GST(import_gst);
//		???????????????=[??????-FBA?????????????????????????????????-??????]*lostrate;
		BigDecimal currencyLostFee = price.subtract(FBA.add(costDetail.getFbaTaxFee())).multiply(costDetail.getCurrencyTransportRate());
		currencyLostFee = currencyLostFee.floatValue()>0?currencyLostFee:new BigDecimal("0");
//		vat????????????=??????*vat/(1+vat)
		BigDecimal vatFee=price.multiply(costDetail.getVatRate().divide(costDetail.getVatRate().add(new BigDecimal("1")),4,BigDecimal.ROUND_HALF_UP));
//		??????GST??????=????????????*gst/(1+gst)
		BigDecimal selling_GST=price.multiply(costDetail.getSelling_GSTRate().divide(costDetail.getSelling_GSTRate().add(new BigDecimal("1")),4,BigDecimal.ROUND_HALF_UP));
//		??????????????? = ?????? * (????????? - ??????????????????(????????????GST???, FBA???GST???))
		BigDecimal corporateInFee = price.subtract(costDetail.getPurchase().add(costDetail.getShipment())
				.add(costDetail.getOthers()).add(FBA).add(price.multiply(costDetail.getRateFee()))
				.add(currencyLostFee).add(vatFee).add(selling_GST).add(tax)).multiply(corporateInRate);
		corporateInFee = corporateInFee.floatValue()>0?corporateInFee:new BigDecimal("0");
//		??????=purchase+shipMengt+others+FBA??????????????????????????????,????????????+??????*??????+???????????????+vat+??????GST???+????????????+??????GST??????;
		BigDecimal totalCost = costDetail.getPurchase().add(costDetail.getShipment()).add(costDetail.getOthers())
				.add(FBA).add(costDetail.getFbaTaxFee()).add(price.multiply(costDetail.getRateFee()))
				.add(currencyLostFee).add(vatFee).add(selling_GST).add(costDetail.getTax())
				.add(costDetail.getImport_GST()).add(corporateInFee);// ??????????????????
		if(price!=null) {
			//??????????????????????????????
			DecimalFormat df2 = new DecimalFormat("0.0000");// ??????4?????????
			costDetail.setCurrencyTransportFee(new BigDecimal(df.format(currencyLostFee)+""));
			costDetail.setVatFee(new BigDecimal(df.format(vatFee)+""));
			costDetail.setSelling_GST(new BigDecimal(df.format(selling_GST)+""));
			costDetail.setCorporateInFee(new BigDecimal(df.format(corporateInFee)+""));
			costDetail.setTotalCost(new BigDecimal(df.format(totalCost)+""));
			
			costDetail.setReferralFee(new BigDecimal(df.format(referralFee)+""));
			costDetail.setPurchase(new BigDecimal(df.format(costDetail.getPurchase())+""));
			costDetail.setShipment(new BigDecimal(df.format(costDetail.getShipment())+""));
			costDetail.setVCFee(new BigDecimal(df.format(costDetail.getVCFee())+""));
			costDetail.setInPlaceFee(new BigDecimal(df.format(costDetail.getInPlaceFee())+""));
			costDetail.setStorageFee(new BigDecimal(df2.format(costDetail.getStorageFee())+""));
			costDetail.setFbaTaxFee(new BigDecimal(df.format(costDetail.getFbaTaxFee())+""));
			costDetail.setTax(new BigDecimal(df.format(costDetail.getTax())+""));
			costDetail.setImport_GST(new BigDecimal(df.format(costDetail.getImport_GST())+""));
			costDetail.setMarketing(new BigDecimal(df.format(costDetail.getMarketingRate().multiply(price))+""));
			costDetail.setPromotionFee(new BigDecimal(df.format(costDetail.getPromotionRate().multiply(price))+""));
			costDetail.setOthersFee(new BigDecimal(df.format(costDetail.getOtherRate().multiply(price))+""));
		}
		BigDecimal profit = price.subtract(totalCost);
		BigDecimal margin = profit.multiply(new BigDecimal("100")).divide(price, 2, BigDecimal.ROUND_HALF_EVEN);
		costDetail.setMargin(margin+"%");
		costDetail.setProfit(profit);
		return costDetail;
	}
	
	//?????????????????????????????????????????????
	public CostDetail getCostDetail(CostDetail costDetail, int typeId, BigDecimal referralrate, boolean isSmlAndLight,ProfitConfig profitcfg ) {
		BigDecimal price = costDetail.getSellingPrice();
		if (price != null && price.doubleValue()>0) {
			// ?????????????????? Amazon Referral Fee
			BigDecimal referralFee = this.calculateReferralFee(typeId, price, costDetail.getCountry());
			//??????????????????????????????fixed closing fee,fbaTaxFee
			if ("IN".equals(costDetail.getCountry())) {
				if(referralrate!=null){
					referralFee = price.multiply(referralrate.divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP));
				}
				IProfitService profitServiceX = null;
				try {
					profitServiceX = factoryProfitServiceByCountry(costDetail.getCountry());
				} catch (Exception e) {
					e.printStackTrace();
				}
				//Fixed closing fee,????????????????????????????????????????????????
				BigDecimal closingFee = profitServiceX.calculateClosingFee(costDetail.getCountry(),price);
				costDetail.setClosingFee(closingFee);
				//???????????????GST????????????18%, ??????????????????????????????, Fixed Closing Fee, FBA fee???
				BigDecimal fbaTaxFee=costDetail.getFBA().add(closingFee).add(referralFee).multiply(costDetail.getFbaTax())
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				costDetail.setFbaTaxFee(fbaTaxFee);
			}
			///////////????????????????????????????????????FBA???//////////////
			if(profitcfg!=null) {
				ProfitConfigCountry cfgcountry = profitcfg.getProfitConfigCountry(costDetail.getCountry());
				if (isSmlAndLight && Arrays.asList(smlAndLightCountry).contains(costDetail.getCountry())) {
					IProfitService profitServiceX = null;
					try {
						profitServiceX = factoryProfitServiceByCountry(costDetail.getCountry());
					} catch (Exception e) {
						e.printStackTrace();
					}
					BigDecimal FBA = profitServiceX.calculateSmlFBA(costDetail.getCountry(), costDetail.getProductTierId(),
							costDetail.getOutboundWeight(),cfgcountry);
					costDetail.setFBA(FBA);
				}
			}
			BigDecimal FBA = getAmazonFeeButRef(costDetail);//??????????????????????????????????????????????????????
			
			// ???????????????=[??????-FBA?????????????????????????????????-??????]*lostrate;
			BigDecimal currencyLostFee = price.subtract(FBA.add(costDetail.getFbaTaxFee())).subtract(referralFee)
					.multiply(costDetail.getCurrencyTransportRate());
			// vat????????????=??????*vat/(1+vat)
			BigDecimal vatFee = price.multiply(costDetail.getVatRate()
					.divide(costDetail.getVatRate().add(new BigDecimal("1")), 4, BigDecimal.ROUND_HALF_UP));
			// ??????GST??????=????????????*gst/(1+gst)
			BigDecimal selling_GST = price.multiply(costDetail.getSelling_GSTRate()
					.divide(costDetail.getSelling_GSTRate().add(new BigDecimal("1")), 4, BigDecimal.ROUND_HALF_UP));
			// ??????????????? = ?????? * (????????? - ??????????????????(????????????GST???, FBA???GST???))
			BigDecimal corporateInFee = price
					.subtract(costDetail.getPurchase().add(costDetail.getShipment()).add(costDetail.getOthers())
							.add(FBA).add(referralFee).add(price.multiply(costDetail.getRateFee())).add(currencyLostFee)
							.add(vatFee).add(selling_GST).add(costDetail.getTax()))
					.multiply(costDetail.getCorporateInRate());
			corporateInFee = corporateInFee.floatValue()>0?corporateInFee:new BigDecimal("0");
			
			// ??????=purchase+shipMengt+others+FBA?????????????????????????????????+??????+??????*??????+???????????????+vat+??????GST???+????????????+??????GST??????;
			BigDecimal totalCost = costDetail.getPurchase().add(costDetail.getShipment()).add(costDetail.getOthers())
					.add(FBA).add(costDetail.getFbaTaxFee()).add(referralFee)
					.add(price.multiply(costDetail.getRateFee())).add(currencyLostFee).add(vatFee)
					.add(costDetail.getTax()).add(costDetail.getImport_GST()).add(selling_GST).add(corporateInFee);// ??????????????????

			BigDecimal profit = price.subtract(totalCost);
			BigDecimal margin = profit.multiply(new BigDecimal("100")).divide(price, 2, BigDecimal.ROUND_HALF_UP);
			
			DecimalFormat df2 = new DecimalFormat("0.0000");// ??????4?????????
			costDetail.setReferralFee(new BigDecimal(df.format(referralFee)+""));
			costDetail.setCurrencyTransportFee(new BigDecimal(df.format(currencyLostFee)+""));
			costDetail.setVatFee(new BigDecimal(df.format(vatFee)+""));
			costDetail.setSelling_GST(new BigDecimal(df.format(selling_GST)+""));
			costDetail.setCorporateInFee(new BigDecimal(df.format(corporateInFee)+""));
			costDetail.setTotalCost(new BigDecimal(df.format(totalCost)+""));
			costDetail.setProfit(new BigDecimal(df.format(profit)+""));
			costDetail.setMargin(margin + "%");
			//??????????????????????????????
			costDetail.setPurchase(new BigDecimal(df.format(costDetail.getPurchase())+""));
			costDetail.setShipment(new BigDecimal(df.format(costDetail.getShipment())+""));
			costDetail.setVCFee(new BigDecimal(df.format(costDetail.getVCFee())+""));
			costDetail.setInPlaceFee(new BigDecimal(df.format(costDetail.getInPlaceFee())+""));
			costDetail.setStorageFee(new BigDecimal(df2.format(costDetail.getStorageFee())+""));
			
			costDetail.setFbaTaxFee(new BigDecimal(df.format(costDetail.getFbaTaxFee())+""));
			costDetail.setTax(new BigDecimal(df.format(costDetail.getTax())+""));
			costDetail.setImport_GST(new BigDecimal(df.format(costDetail.getImport_GST())+""));
			costDetail.setMarketing(new BigDecimal(df.format(costDetail.getMarketingRate().multiply(price))+""));
			costDetail.setPromotionFee(new BigDecimal(df.format(costDetail.getPromotionRate().multiply(price))+""));
			costDetail.setOthersFee(new BigDecimal(df.format(costDetail.getOtherRate().multiply(price))+""));
		}
		return costDetail;
	}

	public BigDecimal calculateClosingFee(String country, BigDecimal price) {
		BigDecimal value = new BigDecimal("0");
		return value;
	}

	public String determineSmlProductTier(String country, InputDimensions inputDimension, ProfitConfigCountry profitConfigX) {
		String productTier = null;
		String fenpeiType = "PAN_EU";
		if (profitConfigX != null) {
			fenpeiType = profitConfigX.getFenpeiType();
		}
		if (country.equals("UK")&&"EFN".equals(fenpeiType)) {//???????????????UK?????????EFN??????????????????????????????2019-12-16
			return null;
		}
		
		BigDecimal boxweight = new BigDecimal("0");
		BigDecimal length = inputDimension.getLength(getDimUnit(country)).getValue();
		BigDecimal width = inputDimension.getWidth(getDimUnit(country)).getValue();
		BigDecimal height = inputDimension.getHeight(getDimUnit(country)).getValue();
		BigDecimal weight = inputDimension.getWeight(getSLWeightUnit(country)).getValue();
		if (weight == null) {
			weight = new BigDecimal("0");
		}
		InputDimensions inputDimension_new = new InputDimensions(length, width, height, getDimUnit(country), weight,
				getSLWeightUnit(country));
		List<Map<String, Object>> tierList = productFormatService.findSmlProductTierByCountry(country);
		// ??????????????????????????????product tier
		for (int i = 0; i < tierList.size(); i++) {
			Map<String, Object> tierMap = tierList.get(i);
			String format = tierMap.get("format").toString();
			Boolean result = (Boolean) AviatorEvaluator.exec(format, inputDimension_new);
			if (result) {
				productTier = tierMap.get("producttierId").toString();
				if (tierMap.get("box_weight") != null) {
					boxweight = new BigDecimal(tierMap.get("box_weight").toString());
				}
				break;
			}
		}
		// ???????????????????????????????????????product tier
		if (productTier!=null) {
			productTier=null;
			weight = weight.add(boxweight).setScale(0, BigDecimal.ROUND_CEILING);
			inputDimension_new = new InputDimensions(length, width, height, getDimUnit(country), weight,
					getSLWeightUnit(country));
			for (int i = 0; i < tierList.size(); i++) {
				Map<String, Object> tierMap = tierList.get(i);
				String format = tierMap.get("format").toString();
				Boolean result = (Boolean) AviatorEvaluator.exec(format, inputDimension_new);
				if (result) {
					productTier = tierMap.get("producttierId").toString();
					break;
				}
			}
		}
		return productTier;
	}

	public BigDecimal calculateSmlFBA(String country, String productTierId, BigDecimal outboundWeight,ProfitConfigCountry profitConfigCountry) {
		BigDecimal value = new BigDecimal("0");
		return value;
	}

	public BigDecimal getSLOutboundWeight(String country, ProductTier productTier, InputDimensions inputDimension) {
		BigDecimal weight = inputDimension.getWeight(getSLWeightUnit(country)).getValue();
		if (weight == null) {
			weight = new BigDecimal("0");
		}
		BigDecimal boxweight = productTier.getBoxWeight();
		if (boxweight==null) {
			boxweight = new BigDecimal("0");
		}
		BigDecimal outboundWeight = weight.add(boxweight).setScale(0, BigDecimal.ROUND_CEILING);//???????????????????????????????????????/???????????????/??????
		return outboundWeight;
	}
	
	Map<String, List<FixedClosingFee>> feeListMap = new HashMap<String, List<FixedClosingFee>>();

	public List<FixedClosingFee> getFixedClosingFeeList(String country) {
		List<FixedClosingFee> feeList = null;
		if (feeListMap.get(country) == null) {
			feeList = fixedClosingFeeMapper.findByCountry(country);
			feeListMap.put(country, feeList);
		} else {
			feeList = feeListMap.get(country);
		}
		return feeList;
	}
	
    public void clearCache() {
//	   this.referralMap.clear();
	   this.vclosingMap.clear();
	   this.categoryList.clear();
	   this.feeListMap.clear();
    }

	public BigDecimal getFBAFormatWeight(String country, String productTierId, InputDimensions inputDimension,BigDecimal outboundWeight,
			ProfitConfigCountry profitConfigCountry) {
		// TODO Auto-generated method stub
		return new BigDecimal("0");
	}
 
	public BigDecimal calculateReferralFee(int typeId, BigDecimal sellingPrice, String country) {
		BigDecimal referralFee = new BigDecimal("0.0");
		ReferralFee resultfee = referralFeeService.getReferralFeeByTypeCountry(typeId, country);// ???????????????id???parent_id??????????????????
		BigDecimal min = resultfee.getLoweast();
		if(resultfee.getPercent2()==null) {
			//?????????????????????????????????????????????Amazon Device Accessories	????????????45%	????????????$0.30
			//?????????????????????????????????
			referralFee = sellingPrice.multiply(resultfee.getPercent1());
			return referralFee.subtract(min).doubleValue() > 0 ? referralFee : min;
			
		}else if(resultfee.getPercent1().doubleValue()<resultfee.getPercent2().doubleValue()) {
			//????????????????????????????????????????????????????????????????????? 8% for products with a total sales price of $15.00 or less, and 15% for products with a total sales price greater than $15.00
			//?????????????????????????????????????????????????????????15??????????????????8% ,15??????????????????15%
			if(sellingPrice.subtract(resultfee.getTop1()).doubleValue()>0) {
				referralFee = sellingPrice.multiply(resultfee.getPercent2());
				return referralFee;
			}else {
				referralFee = sellingPrice.multiply(resultfee.getPercent1());
				return referralFee.subtract(min).doubleValue() > 0 ? referralFee : min;
			}
		}else   {
			//??????????????????????????????????????????????????????????????? ?????????
			//????????????????????????????????? 100 ???????????????????????? 20% ???????????????
			//???????????????????????? 100 ???????????????????????? 1,000 ???????????????????????? 10% ??????????????????
			//?????????????????????????????? 1,000 ???????????????????????? 6% ??????????????????
			//????????????????????????????????? 500 ????????????????????????????????????????????????20 ?????????????????????????????? 100 ???????????????+ 40 ????????????????????????????????? 400 ???????????????= ??????????????? 60 ?????????
			for (int i = 1; i < 4; i++) {
				if (resultfee.getTop(i) != null && sellingPrice.subtract(resultfee.getTop(i)).doubleValue() > 0) {
					// referralFee+(tp1-0)*p1,referralFee+(top2-top1)*p2,...
					referralFee = referralFee
							.add((resultfee.getTop(i).subtract((i == 1 ? new BigDecimal("0.0") : resultfee.getTop(i - 1))))
									.multiply(resultfee.getPercent(i)));
				} else {
					// referralFee+(price-0)*p1,referralFee+(price-top1)*p2,...
					referralFee = referralFee
							.add((sellingPrice.subtract((i == 1 ? new BigDecimal("0.0") : resultfee.getTop(i - 1))))
									.multiply(resultfee.getPercent(i)));
					break;
				}
			}
			return referralFee.subtract(min).doubleValue() > 0 ? referralFee : min;
		}

	}
	
	public Map<String, Object> calculateAmazonCostDetail(UserInfo user,String marketplaceid,String sku, String profitCfgId, BigDecimal cost,String type,BigDecimal price, BigDecimal shipment) {
			Marketplace marketPlace = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
			AmazonAuthority amazonAuthority = null;
	        amazonAuthority = amazonAuthorityService.selectByGroupAndMarket(mws_groupId, marketPlace.getMarketplaceid());
			amazonAuthority.setMarketPlace(marketPlace);
			List<String> asinList = new ArrayList<String>();
 
		
			Item response = productCaptureService.captureListMatchingProduct(amazonAuthority, sku,Arrays.asList(marketPlace.getMarketplaceid()));
//			List<GetMatchingProductResult> list = response.getGetMatchingProductResult();
		//	Dimensions pkgdimensions = null;
			String amztype=null;
//			for (int i = 0; i < list.size(); i++) {
//				GetMatchingProductResult result = list.get(i);
//				Product product = result.getProduct();
//				if (product == null) {
//					continue;
//				}
//				AttributeSetList attributeSet = product.getAttributeSets();
//				if (attributeSet != null && attributeSet.getAny() != null) {
//					String xml = attributeSet.toXMLFragment();
//					JAXBContext context = null;
//					try {
//						context = JAXBContext.newInstance(ItemAttributes.class);
//						Unmarshaller unmarsheller = context.createUnmarshaller();
//						ItemAttributes itemAttributeses = (ItemAttributes) unmarsheller.unmarshal(new StringReader(xml));
//						amztype=itemAttributeses.getProductGroup();
//						if (itemAttributeses != null && itemAttributeses.getPackageDimensions() != null) {
//							   pkgdimensions = itemAttributeses.getPackageDimensions();
//						}
//					} catch (JAXBException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//				}
//			}
			 

		
		Map<String, Object> map = new HashMap<String, Object>();

		if(StrUtil.isEmpty(profitCfgId)) {
			profitCfgId="26138972982277800";
		}
		ProfitConfig profitcfg = profitCfgService.findConfigAction(profitCfgId);
		if (price==null || price.compareTo(BigDecimal.ZERO)==-1) {
			map.put("costDetail", null);
			map.put("msg", "?????????????????????????????????????????????????????????");
			return map;
		}
		Marketplace marketplace = marketplaceService.selectByPKey(marketplaceid);
		String currency = marketplace.getCurrency();
		String country = marketplace.getMarket();
		InputDimensions inputDimensions=null;//InputDimensions.getTrueInputDimesions(pkgdimensions); 
		if (inputDimensions==null||inputDimensions.getWeight() == null) {
			map.put("costDetail", null);
			map.put("msg", "???????????????????????????????????????????????????");
			return map;
		}
		int typeId = 41;
		List<ReferralFee> typelist = referralFeeService.findAllType();
		List<String> typeall =new ArrayList<String>();
		if(type!=null&&type.contains("-")) {
			typeall.addAll(Arrays.asList(type.split("-")));
		}else if(type!=null&&type.contains("&")) {
			typeall.addAll(Arrays.asList(type.split("&")));
		}else if(type!=null&&type.contains(" ")) {
			typeall.addAll(Arrays.asList(type.split(" ")));
		}else  if(type!=null){
			typeall.add(type);
		} 
		if(amztype!=null&&amztype.contains("-")) {
			typeall.addAll( Arrays.asList(amztype.split("-")));
		}else if(amztype!=null&&amztype.contains("&")) {
			typeall.addAll( Arrays.asList(amztype.split("&")));
		}else  if(amztype!=null){
			typeall.add(amztype);
		} 
		for(String typeitem:typeall) {
		for(ReferralFee item:typelist) {
				if(typeitem!=null&&item.getType().toLowerCase().indexOf(typeitem.toLowerCase().trim())>=0) {
					typeId=item.getId(); break;
				}
			}
			if(typeId!=41) {break;}
		}
		String isMedia = isMedia(typeId);// ???????????????
		CostDetail costDetail = null;
		if(!"manually".equals(profitcfg.getShipmentstyle())&&shipment!=null&&shipment.floatValue()==0.0) {
			shipment=null;
		}
		try {
			costDetail = initCostDetail(country, profitcfg, inputDimensions, isMedia, type, typeId, cost,
					shipment, currency, null, "national", null, "", null, null, null, null, false);
		} catch (BizException e) {
			e.printStackTrace();
			map.put("costDetail", null);
			map.put("msg", e);
			return map;
		}
		costDetail.setSellingPrice(price);
		costDetail =  getCostDetail(costDetail, typeId, null, false,profitcfg);
		List<ProfitConfig> lists=null;
		if(user!=null) {
			lists=profitCfgService.findProfitCfgName(user.getCompanyid());
		}else {
			lists=new ArrayList<ProfitConfig>();
			lists.add(profitcfg);
		}
		map.put("costDetail", costDetail);
		map.put("profitCfgList", lists);
		map.put("profitCfgId", profitCfgId);
		map.put("inputDimensions", inputDimensions);
		
		map.put("msg", "ok");
		return map;
	}

}
