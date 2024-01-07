package com.wimoor.amazon.profit.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.googlecode.aviator.AviatorEvaluator;
import com.wimoor.amazon.profit.pojo.entity.FBAFormat;
import com.wimoor.amazon.profit.pojo.entity.OutboundWeightFormat;
import com.wimoor.amazon.profit.pojo.entity.ProductTier;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.amazon.profit.pojo.vo.ItemMeasure;
import com.wimoor.common.StringFormat;
import com.wimoor.common.mvc.BizException;

import cn.hutool.core.math.Calculator;

 

@Service("MXProfitService")  
public class MXProfitServiceImpl extends ProfitServiceImpl{

	@Override
	public String determineProductTier(String country, InputDimensions dimensions ,String isMedia) throws BizException {
		String productTier = null;
		BigDecimal outboundWeight = new BigDecimal("0");
		BigDecimal length = dimensions.getLength(getDimUnit(country)).getValue();
		BigDecimal width = dimensions.getWidth(getDimUnit(country)).getValue();
		BigDecimal height = dimensions.getHeight(getDimUnit(country)).getValue();
		BigDecimal weight = dimensions.getWeight(getWeightUnit(country)).getValue();
		if(weight==null){
			weight=new BigDecimal("0");
		}
		dimensions = new InputDimensions(length, width, height, getDimUnit(country), weight, getWeightUnit(country));
		BigDecimal dimensionalWeight = dimensions.getDimensionalWeight(getDimUnit(country)).getValue();
		dimensions.setDimensionalWeight(new ItemMeasure(dimensionalWeight, getDimUnit(country)));
		
		List<Map<String, Object>> tierList = productFormatService.findTierFormatByCountry(country);
		// 第一次用输入重量估算product tier
		for (int i = 0; i < tierList.size(); i++) {
			Map<String, Object> tierMap = tierList.get(i);
			String format = tierMap.get("format").toString();
			Boolean result = (Boolean) AviatorEvaluator.exec(format, dimensions);
			if (result) {
				productTier = tierMap.get("producttierId").toString();
				OutboundWeightFormat outboundWeightFormat = outboundWeightFormatService.findByProductTierId(productTier,false);
				String oformat = outboundWeightFormat.getFormat();
				Object result2 = AviatorEvaluator.exec(oformat, dimensions);
				outboundWeight = new BigDecimal(result2.toString());
				outboundWeight = outboundWeight.divide(new BigDecimal("0.5")).setScale(0, RoundingMode.CEILING).multiply(new BigDecimal("0.5"));//进位取整数，模拟不足500g按500g算；
				break;
			}
		}
		// 第二次用带包装重量重新计算product tier
		dimensions = new InputDimensions(length, width, height, getDimUnit(country), outboundWeight, getWeightUnit(country));
		for (int i = 0; i < tierList.size(); i++) {
			Map<String, Object> tierMap = tierList.get(i);
			String format = tierMap.get("format").toString();
			Boolean result = (Boolean) AviatorEvaluator.exec(format, dimensions);
			if (result) {
				productTier = tierMap.get("producttierId").toString();
				break;
			}
		}
		return productTier;
	}

	//计算outbound Weight
	@Override
	public BigDecimal determineOutboundWeight(String country, ProductTier productTier, InputDimensions dimensions, String isMedia) throws BizException {
		BigDecimal outboundWeight = new BigDecimal("0");
		BigDecimal weight = dimensions.getWeight(getWeightUnit(country)).getValue();
		if(weight==null){
			weight=new BigDecimal("0");
		}
		BigDecimal dimensionalWeight = dimensions.getDimensionalWeight(getDimUnit(country)).getValue();
		dimensions.setWeight(new ItemMeasure(weight,getWeightUnit(country)));
		dimensions.setDimensionalWeight(new ItemMeasure(dimensionalWeight, getDimUnit(country)));
		OutboundWeightFormat outboundWeightFormat = outboundWeightFormatService.findByProductTierId(productTier.getId(),false);
		String format = outboundWeightFormat.getFormat();
		Object result = AviatorEvaluator.exec(format, dimensions);
		outboundWeight = new BigDecimal(result.toString());
		outboundWeight = outboundWeight.divide(new BigDecimal("0.5")).setScale(0, RoundingMode.CEILING).multiply(new BigDecimal("0.5"));//进位取整数，模拟不足500g按500g算；
		return outboundWeight;
	}
	
	@Override
	public BigDecimal calculateFBA(String country, String productTierId, InputDimensions inputDimension, String isMedia, String type, 
			BigDecimal outboundWeight, ProfitConfigCountry profitConfigX, String shipmentType) {
		BigDecimal FBA = new BigDecimal("0");
		boolean media ;
		if (isMedia.equals("1")) {
			media = true;
		} else {
			media = false;
		}
		FBAFormat fbaFormat = fbaFormatService.findByProductTierIdAndIsMedia2(productTierId,media,country);
		String format = fbaFormat.getFbaFormat();
		if (format.contains("?")) {// 如果有逻辑判断
			format = (String) AviatorEvaluator.exec(format, outboundWeight);
		}
		String result = StringFormat.format(format, outboundWeight);
		FBA =  new BigDecimal(Calculator.conversion(result));
		return FBA;
	}
	
	@Override
	public BigDecimal calculateStorageFee(String country, ProfitConfigCountry profitConfigX, InputDimensions inputDimension, boolean isStandard) throws BizException {//每月每立方英尺，按比例收费
		BigDecimal monthlyFee =  profitConfigX.getStoragefee();
		BigDecimal month = profitConfigX.getAmonthBigDecimal();
		BigDecimal volume = inputDimension.getVolume(InputDimensions.unit_cm).getValue().divide(new BigDecimal("1000"),
				6, RoundingMode.HALF_UP);
		BigDecimal storageFee = monthlyFee.multiply(volume).multiply(month);
		return storageFee;
	}

}
