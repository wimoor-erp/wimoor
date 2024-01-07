package com.wimoor.amazon.profit.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.googlecode.aviator.AviatorEvaluator;
import com.wimoor.amazon.profit.pojo.entity.FBAFormat;
import com.wimoor.amazon.profit.pojo.entity.FixedClosingFee;
import com.wimoor.amazon.profit.pojo.entity.ProductTier;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.common.StringFormat;
import com.wimoor.common.mvc.BizException;

import cn.hutool.core.math.Calculator;

 

@Service("INProfitService")  
public class INProfitServiceImpl extends ProfitServiceImpl{

	@Override
	public String determineProductTier(String country, InputDimensions dimensions ,String isMedia) throws BizException {
		String productTier = null;
		BigDecimal length = dimensions.getLength(getDimUnit(country)).getValue();
		BigDecimal width = dimensions.getWidth(getDimUnit(country)).getValue();
		BigDecimal height = dimensions.getHeight(getDimUnit(country)).getValue();
		BigDecimal weight = dimensions.getWeight(getWeightUnit(country)).getValue();
		if(weight==null){
			weight=new BigDecimal("0");
		}
		dimensions = new InputDimensions(length, width, height, getDimUnit(country), weight, getWeightUnit(country));
		
		List<Map<String, Object>> tierList = productFormatService.findTierFormatByCountry(country);
		for (int i = 0; i < tierList.size(); i++) {
			Map<String, Object> tierMap = tierList.get(i);
			String format = tierMap.get("format").toString();
			Boolean result = (Boolean) AviatorEvaluator.exec(format, dimensions);
			if (result) {
				productTier = tierMap.get("producttierId").toString();
				return productTier;
			}
		}
		return null;
	}

	//计算outbound Weight,单位kg
	@Override
	public BigDecimal determineOutboundWeight(String country, ProductTier productTier, InputDimensions dimensions, String isMedia) throws BizException {
		BigDecimal outboundWeight = new BigDecimal("0");
		BigDecimal weight = dimensions.getWeight(getWeightUnit(country)).getValue();
		if(weight==null){
			weight=new BigDecimal("0");
		}
		BigDecimal boxweight = productTier.getBoxWeight();
		if (boxweight==null) {
			boxweight = new BigDecimal("0");
		}
		if (weight.subtract(new BigDecimal("1")).doubleValue()>0) {//大于1kg的，取材积和重量大者计算
			BigDecimal dimensionalWeight = dimensions.getDimensionalWeight(getDimUnit(country)).getValue();
			if (dimensionalWeight.subtract(weight).doubleValue()>0) {
				outboundWeight = dimensionalWeight.add(boxweight).multiply(new BigDecimal("1000"));//g
			} else {
				outboundWeight = weight.add(boxweight).multiply(new BigDecimal("1000"));//g
			}
		} else {
			outboundWeight = weight.add(boxweight).multiply(new BigDecimal("1000"));//g
		}
		
		//进位取整数，模拟不足一克按一克算
		outboundWeight = outboundWeight.setScale(0, RoundingMode.CEILING).divide(new BigDecimal("1000"),3,RoundingMode.CEILING);//kg
		return outboundWeight;
	}
	
	@Override
	public BigDecimal calculateFBA(String country, String productTierId, InputDimensions inputDimension, String isMedia, String type, 
			BigDecimal outboundWeight, ProfitConfigCountry profitConfigX, String shipmentType) {
		BigDecimal FBA = new BigDecimal("0");
		//注意：National shipment option is not available for Oversize Heavy and Bulky units.
		FBAFormat fbaFormat = fbaFormatService.findByProductTierAndType(productTierId,shipmentType,country);
		if (fbaFormat != null) {
			String format = fbaFormat.getFbaFormat();
			if (format.contains("?")) {//如果有逻辑判断
				format = (String) AviatorEvaluator.exec(format, outboundWeight);
			}
			String result = StringFormat.format(format, outboundWeight);
			FBA =  new BigDecimal(Calculator.conversion(result));
		}
		return FBA;
	}
	
	@Override
	public BigDecimal calculateClosingFee(String country, BigDecimal price) {
		BigDecimal closingFee = new BigDecimal("0");
		List<FixedClosingFee> feeList = getFixedClosingFeeList(country);
		if (feeList==null) {
			return closingFee;
		}
		for (int i = 0; i < feeList.size(); i++) {
			FixedClosingFee closingfee = feeList.get(i);
			String format = closingfee.getFormat();
			Boolean result = (Boolean) AviatorEvaluator.exec(format, price);
			if (result) {
				closingFee = closingfee.getFee();
				return closingFee;
			}
		}
		return closingFee;
	}
	

}
