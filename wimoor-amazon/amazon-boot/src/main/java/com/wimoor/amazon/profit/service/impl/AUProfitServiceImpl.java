package com.wimoor.amazon.profit.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.profit.pojo.entity.OutboundWeightFormat;
import com.wimoor.amazon.profit.pojo.entity.ProductTier;
import com.wimoor.amazon.profit.pojo.vo.ItemMeasure;
import org.springframework.stereotype.Service;

import com.googlecode.aviator.AviatorEvaluator;
import com.wimoor.amazon.profit.pojo.entity.FBAFormat;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.common.mvc.BizException;
 

@Service("AUProfitService")  
public class AUProfitServiceImpl extends ProfitServiceImpl{


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
		OutboundWeightFormat outboundWeightFormat = outboundWeightFormatService.findByProductTierId(productTier.getId());
		if(outboundWeightFormat==null){
			return weight;
		}
		String format = outboundWeightFormat.getFormat();
		Object result = AviatorEvaluator.exec(format, dimensions);
		outboundWeight = new BigDecimal(result.toString());
		return outboundWeight;
	}


	@Override
	public String determineProductTier(String country, InputDimensions dimensions ,String isMedia) throws BizException {
		String productTier = null;
		BigDecimal boxweight = new BigDecimal("0");
		BigDecimal length = dimensions.getLength(getDimUnit(country)).getValue();
		BigDecimal width = dimensions.getWidth(getDimUnit(country)).getValue();
		BigDecimal height = dimensions.getHeight(getDimUnit(country)).getValue();
		BigDecimal weight = dimensions.getWeight(getWeightUnit(country)).getValue();
		dimensions.setDimBase(new BigDecimal("4000"));

		if(weight==null){
			weight=new BigDecimal("0");
		}
		dimensions = new InputDimensions(length, width, height, getDimUnit(country), weight, getWeightUnit(country));
		
		List<Map<String, Object>> tierList = productFormatService.findTierFormatByCountry(country);
		// 第一次用输入重量估算product tier
		for (int i = 0; i < tierList.size(); i++) {
			Map<String, Object> tierMap = tierList.get(i);
			String format = tierMap.get("format").toString();
			Boolean result = (Boolean) AviatorEvaluator.exec(format, dimensions);
			if (result) {
				productTier = tierMap.get("producttierId").toString();
				if (tierMap.get("box_weight") != null) {
					boxweight = new BigDecimal(tierMap.get("box_weight").toString());
				}
				break;
			}
		}
		// 第二次用带包装重量重新计算product tier
		if (boxweight.floatValue()>0) {
			weight = weight.add(boxweight).setScale(3, RoundingMode.CEILING);
			dimensions = new InputDimensions(length, width, height, getDimUnit(country), weight, getWeightUnit(country));
			for (int i = 0; i < tierList.size(); i++) {
				Map<String, Object> tierMap = tierList.get(i);
				String format = tierMap.get("format").toString();
				Boolean result = (Boolean) AviatorEvaluator.exec(format, dimensions);
				if (result) {
					productTier = tierMap.get("producttierId").toString();
					break;
				}
			}
		}
		return productTier;
	}


	@Override
	public BigDecimal calculateFBA(String country, String productTierId, InputDimensions inputDimension, String isMedia, String type, 
			BigDecimal outboundWeight, ProfitConfigCountry profitConfigX, String shipmentType) {
		BigDecimal FBA = new BigDecimal("0");
		FBAFormat fbaFormat = fbaFormatService.findByProductTierIdAndWeight(productTierId,outboundWeight,country);
		if (fbaFormat != null) {
			String format = fbaFormat.getFbaFormat();
			FBA = new BigDecimal(format);
		}
		return FBA;
	}

    @Override
    public BigDecimal calculateSmlFBA(String country, String productTierId, BigDecimal outboundWeight, ProfitConfigCountry profitConfigCountry, String type) {
		BigDecimal FBA = new BigDecimal("0");
		FBAFormat fbaFormat = fbaFormatService.findByProductTierIdAndWeight(productTierId,outboundWeight,country);
		if (fbaFormat != null) {
			String format = fbaFormat.getFbaFormat();
			FBA = new BigDecimal(format);
		}
		FBA=FBA.subtract(new BigDecimal("0.91"));
		return FBA;
    }

    @Override
	public BigDecimal calculateStorageFee(String country, ProfitConfigCountry profitConfigX,
			InputDimensions inputDimension, boolean isStandard) throws BizException {// 每月每立方英尺，按比例收费
		BigDecimal monthlyFee = profitConfigX.getStoragefee();
		BigDecimal month = profitConfigX.getAmonthBigDecimal();
		BigDecimal volume = inputDimension.getVolume(InputDimensions.unit_cm).getValue()
				.divide(new BigDecimal("1000000"), 6, RoundingMode.HALF_UP);
		BigDecimal storageFee = monthlyFee.multiply(volume).multiply(month);
		return storageFee;
	}

}
