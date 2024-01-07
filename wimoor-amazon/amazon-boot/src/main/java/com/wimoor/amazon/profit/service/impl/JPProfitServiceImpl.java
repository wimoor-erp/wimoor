package com.wimoor.amazon.profit.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.googlecode.aviator.AviatorEvaluator;
import com.wimoor.amazon.profit.pojo.entity.FBAFormat;
import com.wimoor.amazon.profit.pojo.entity.InplaceFeeFormat;
import com.wimoor.amazon.profit.pojo.entity.ProductTier;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.common.mvc.BizException;
 
@Service("JPProfitService")  
public class JPProfitServiceImpl extends ProfitServiceImpl{

	/**
	 * 判断 Product Tier
	 * high_value:对于samll和standard size,当售价大于等于45000日元时，产品为high_value
	 * @throws BizException 
	 */
	@Override
	public String determineProductTier(String country, InputDimensions dimensions ,String isMedia) throws BizException {
		String productTier = null;
		BigDecimal boxweight = new BigDecimal("0");
		BigDecimal length = dimensions.getLength(getDimUnit(country)).getValue();
		BigDecimal width = dimensions.getWidth(getDimUnit(country)).getValue();
		BigDecimal height = dimensions.getHeight(getDimUnit(country)).getValue();
		BigDecimal weight = dimensions.getWeight(getWeightUnit(country)).getValue();
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
	public BigDecimal calculateSmlFBA(String country, String productTierId, BigDecimal outboundWeight,ProfitConfigCountry profitConfigCountry,String type) {
		BigDecimal FBA = new BigDecimal("0");
		FBAFormat fbaFormat = fbaFormatService.findByProductTierIdAndWeight(productTierId, outboundWeight,country);
		if(fbaFormat!=null) {
			String format = fbaFormat.getFbaFormat();
			FBA = new BigDecimal(format);
		}
		return FBA;
	}

	@Override
	public BigDecimal calculateFBA(String country, String productTierId, InputDimensions inputDimension, String isMedia,
			String type, BigDecimal outboundWeight, ProfitConfigCountry profitConfigX, String shipmentTypes) throws BizException {
		BigDecimal FBA = new BigDecimal("0");
		FBAFormat fbaFormat = fbaFormatService.findByProductTierIdAndWeight(productTierId, outboundWeight,country);
		String format = fbaFormat.getFbaFormat();
		FBA = new BigDecimal(format);
		return FBA;
	}
	
	@Override
	public BigDecimal calculateStorageFee(String country, ProfitConfigCountry profitConfigX, InputDimensions inputDimension, boolean isStandard) throws BizException {//每月每立方英尺，按比例收费
		BigDecimal monthlyFee =  profitConfigX.getStoragefee();
		BigDecimal month = profitConfigX.getAmonthBigDecimal();
		BigDecimal volume = inputDimension.getVolume(InputDimensions.unit_cm).getValue().divide(new BigDecimal("1000"),
				4, RoundingMode.HALF_UP);// [商品尺寸(cm 3)] / (10cm × 10cm × 10cm)
		BigDecimal storageFee = monthlyFee.multiply(volume).multiply(month);//8.126 日元 × {[商品尺寸(cm 3)] / (10cm × 10cm × 10cm)} × [保管天数/当月的天数]
		return storageFee;
	}
	
	//计算FBA Premium Placement Fees
	@Override
	public BigDecimal calculateInPlaceFee(String country, ProductTier productTier, InputDimensions inputDimension, String invplaceFee) {
		BigDecimal inPlaceFee = new BigDecimal("0");
		if (!invplaceFee.equals("uncalculate")) {
			InplaceFeeFormat inplaceFee = inplaceFeeFormatService.findByProductTierId(productTier.getId());
			if (inplaceFee==null) {
				return inPlaceFee;
			}
			String format = inplaceFee.getFormat();//得到计算公式
//			String result = StringFormat.format(format);//注意，计算公式里面不能有空格
//			inPlaceFee = Calculator.conversion2(result);
			inPlaceFee = new BigDecimal(format);
		}
		return inPlaceFee;
	}
	
//	@Override
//	public BigDecimal getPrepServiceFee(boolean isStandard, String country, String category) {
//		BigDecimal prepServiceFee = new BigDecimal("0");
//		return prepServiceFee;
//	}
	
	//计算Variable Closing Fee,FBA没有这笔费用
//	@Override
//	public BigDecimal calculateVCFee(String country, String isMedia, int typeId, String logistics, InputDimensions inputDimension) {
//		BigDecimal VCFee = new BigDecimal("0");
//		VariableClosingFee VClosingFee = VClosingFeeMapper.findByTypeId(country, typeId);
//		if (VClosingFee!=null) {
//			String format = VClosingFee.getFormat();
//			String result = StringFormat.format(format);
//			VCFee = Calculator.conversion2(result);
//		}
//		return VCFee;
//	}

}
