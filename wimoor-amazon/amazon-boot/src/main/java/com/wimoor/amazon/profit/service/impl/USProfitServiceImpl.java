package com.wimoor.amazon.profit.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.googlecode.aviator.AviatorEvaluator;
import com.wimoor.amazon.profit.pojo.entity.FBAFormat;
import com.wimoor.amazon.profit.pojo.entity.InplaceFeeFormat;
import com.wimoor.amazon.profit.pojo.entity.InventoryStorageFee;
import com.wimoor.amazon.profit.pojo.entity.OutboundWeightFormat;
import com.wimoor.amazon.profit.pojo.entity.ProductTier;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.amazon.profit.pojo.vo.ItemMeasure;
import com.wimoor.common.StringFormat;
import com.wimoor.common.mvc.BizException;

import cn.hutool.core.math.Calculator;
 

@Service("USProfitService")
public class USProfitServiceImpl extends ProfitServiceImpl {

	/**
	 * 判断US Product Tier Small Standard-Size,Large Standard-Size,Small Oversize,
	 * Medium Oversize,Large Oversize,Special Oversize
	 * 
	 * @throws BizException
	 */
	@Override
	public String determineProductTier(String country, InputDimensions dimensions, String isMedia)
			throws BizException {
		String USProductTier = null;
		BigDecimal length_in = dimensions.getLength(getDimUnit(country)).getValue();
		BigDecimal width_in = dimensions.getWidth(getDimUnit(country)).getValue();
		BigDecimal height_in = dimensions.getHeight(getDimUnit(country)).getValue();
		BigDecimal weight = dimensions.getWeight(getWeightUnit(country)).getValue();
		if (weight == null) {
			weight = new BigDecimal("0");
		}
		if(length_in == null) {
			length_in = new BigDecimal("0");
		}
		if(width_in == null) {
			width_in = new BigDecimal("0");
		}
		if(height_in == null) {
			height_in = new BigDecimal("0");
		}
		BigDecimal girth = (height_in.add(width_in)).multiply(new BigDecimal("2"));// 计算周长
		BigDecimal dimensionalWeight = dimensions
				.getDimensionalWeight(getDimUnit(country), length_in, width_in, height_in).getValue();// 尺寸重量,单位体积（基于长度x宽度x高度（以英寸计））除以166。
		dimensions = new InputDimensions(length_in, width_in, height_in, getDimUnit(country), weight,
				getWeightUnit(country));
		dimensions.setGirth(new ItemMeasure(girth, getDimUnit(country)));
		dimensions.setDimensionalWeight(new ItemMeasure(dimensionalWeight, getWeightUnit(country)));

		List<Map<String, Object>> tierList = productFormatService.findTierFormatByCountry(country);
		for (int i = 0; i < tierList.size(); i++) {
			Map<String, Object> tierMap = tierList.get(i);
			String format = tierMap.get("format").toString();
			Boolean result =false;
			result = (Boolean) AviatorEvaluator.exec(format, dimensions,isMedia);
			if (result) {
				USProductTier = tierMap.get("producttierId").toString();
				return USProductTier;
			}
		}
		return null;
	}

	@Override
	public BigDecimal getSLOutboundWeight(String country, ProductTier productTier, InputDimensions dimensions) {
		// TODO Auto-generated method stub
		BigDecimal outboundWeight = new BigDecimal("0");
		BigDecimal weight = dimensions.getWeight(getWeightUnit(country)).getValue();
		if (weight == null) {
			weight = new BigDecimal("0");
		}
		BigDecimal dimensionalWeight = dimensions.getDimensionalWeight(getDimUnit(country)).getValue();
		dimensions.setWeight(new ItemMeasure(weight, getWeightUnit(country)));
		dimensions.setDimensionalWeight(new ItemMeasure(dimensionalWeight, getWeightUnit(country)));
		OutboundWeightFormat outboundWeightFormat = outboundWeightFormatService.findByProductTierId(productTier.getId());
		if (outboundWeightFormat != null && outboundWeightFormat.getFormat() != null) {
			String format = outboundWeightFormat.getFormat();
			// 任何以大写字母N结尾的整数都被认为是big int；
			// 任何以大写字母M结尾的数字都被认为是decimal；
			// 其他的任何整数都将被转换为Long，
			// 其他任何浮点数都将被转换为Double。
			// 超过long范围的整数字面量都将自动转换为big int类型
			Object result = AviatorEvaluator.exec(format, dimensions);
			// System.out.println(result+" "+result.getClass());//有时得到BigDecimal类型，有时得到Double类型
			outboundWeight = new BigDecimal(result.toString());
			BigDecimal intOut = outboundWeight.setScale(0,RoundingMode.DOWN);// 进位取整数，模拟不足一磅按一磅算；
			BigDecimal flotOut = outboundWeight.subtract(intOut);
			if(flotOut.compareTo(new BigDecimal("0.5"))>0) {
				outboundWeight=intOut.add(new BigDecimal("1"));
			}else if(flotOut.compareTo(new BigDecimal("0.00001"))>=0) {
				outboundWeight=intOut.add(new BigDecimal("0.5"));
			}
		}else {
			outboundWeight=dimensions.getWeight().getValue();
		}
		return outboundWeight;
	}

	// 计算outbound Weight，单位lb
	@Override
	public BigDecimal determineOutboundWeight(String country, ProductTier productTier, InputDimensions dim,
			String isMedia) throws BizException {
		BigDecimal outboundWeight = new BigDecimal("0");
		BigDecimal weight = dim.getWeight(getWeightUnit(country)).getValue();
		if (weight == null) {
			weight = new BigDecimal("0");
		}
		BigDecimal dimensionalWeight = dim.getDimensionalWeight(getDimUnit(country)).getValue();
		dim.setWeight(new ItemMeasure(weight, getWeightUnit(country)));
		dim.setDimensionalWeight(new ItemMeasure(dimensionalWeight, getWeightUnit(country)));
		OutboundWeightFormat outboundWeightFormat = outboundWeightFormatService.findByProductTierId(productTier.getId());
		if (outboundWeightFormat != null && outboundWeightFormat.getFormat() != null) {
			String format = outboundWeightFormat.getFormat();
			// 任何以大写字母N结尾的整数都被认为是big int；
			// 任何以大写字母M结尾的数字都被认为是decimal；
			// 其他的任何整数都将被转换为Long，
			// 其他任何浮点数都将被转换为Double。
			// 超过long范围的整数字面量都将自动转换为big int类型
			try {
				if (format.contains("?")) { 
					format =  AviatorEvaluator.exec(format, dim).toString();
				}
				if(format.contains("{0}")) {
					String result = StringFormat.format(format, outboundWeight);
					format =  new BigDecimal(Calculator.conversion(result)).toString();
				}
				if(format.contains("value")) {
					format =  AviatorEvaluator.exec(format, dim).toString();
				}
				outboundWeight =new BigDecimal(format);
			}catch(Exception e) {
				e.printStackTrace();
			}
			BigDecimal intOut = outboundWeight.setScale(0,RoundingMode.DOWN);// 进位取整数，模拟不足一磅按一磅算；
			BigDecimal flotOut = outboundWeight.subtract(intOut);
			if(flotOut.compareTo(new BigDecimal("0.5"))>0) {
				outboundWeight=intOut.add(new BigDecimal("1"));
			}else if(flotOut.compareTo(new BigDecimal("0.00001"))>=0) {
				outboundWeight=intOut.add(new BigDecimal("0.5"));
			}
		}
		return outboundWeight;
	}

	@Override
	public BigDecimal calculateSmlFBA(String country, String productTierId, BigDecimal outboundWeight,ProfitConfigCountry profitConfigCountry,String type) {
		BigDecimal FBA = new BigDecimal("0");
		boolean isClothing = false;
		if (type.toLowerCase().contains("clothing")||type.toLowerCase().contains("apparel")) {
			isClothing = true;
		}
		FBAFormat fbaFormat = fbaFormatService.findByProductTierIdNew(productTierId,isClothing,country);
		if(fbaFormat==null) {
			throw new BizException(country+"对于重量【"+outboundWeight+"】找不到对应的FBA费用");
		}
		String format = fbaFormat.getFbaFormat();
		if (format.contains("?")) {// 如果有逻辑判断
			format = (String) AviatorEvaluator.exec(format, outboundWeight);
		}
		if(format.contains("{0}")) {
			String result = StringFormat.format(format, outboundWeight);
			format =  new BigDecimal(Calculator.conversion(result)).toString();
		}
		if(format.contains("value")) {
			format =  AviatorEvaluator.exec(format, outboundWeight).toString();
		}
		FBA = new BigDecimal(format);
		return FBA;
	}

	@Override
	public BigDecimal calculateFBA(String country, String productTierId, InputDimensions inputDimension, String isMedia,
			String type, BigDecimal outboundWeight, ProfitConfigCountry profitConfigX, String shipmentType) {
		BigDecimal FBA = new BigDecimal("0");
		boolean isClothing = false;
		if (type.toLowerCase().contains("clothing")||type.toLowerCase().contains("apparel")) {
			isClothing = true;
		}
		FBAFormat fbaFormat = fbaFormatService.findByProductTierIdNew(productTierId,isClothing,country);
		String format = fbaFormat.getFbaFormat();
		if (format.contains("?")) {// 如果有逻辑判断
			format = (String) AviatorEvaluator.exec(format, outboundWeight);
		}
		if(format.contains("value")) {
			format =  AviatorEvaluator.exec(format, outboundWeight).toString();
		}
		String result = StringFormat.format(format, outboundWeight);
		FBA =  new BigDecimal(Calculator.conversion(result));
//		Lithium batteries and items that contain or are sold with them incur an additional $0.11 per-unit fulfillment fee.
		if(type.toLowerCase().contains("Lithium batteries")){
			FBA = FBA.add(new BigDecimal("0.11"));
		}
		return FBA;
	}

	@Override
	public BigDecimal calculateStorageFee(String country, ProfitConfigCountry profitConfigX,
			InputDimensions inputDimension, boolean isStandard) throws BizException {// 每月每立方英尺，按比例收费
		BigDecimal storageFee = new BigDecimal("0");
		int fbaMonth = profitConfigX.getFbaMonth();// 月份
		String month = FBAFormat.monthConversion[fbaMonth - 1];
		month = "%" + month + "%";
		InventoryStorageFee inventoryStorageFee = inventoryStorageFeeService.getPriceByCountry(country, month,
				isStandard);
		if (inventoryStorageFee != null) {
			BigDecimal monthlyFee = inventoryStorageFee.getPrice();
			BigDecimal monthDay = profitConfigX.getAmonthBigDecimal();
			BigDecimal volume = inputDimension.getVolume(InputDimensions.unit_in).getValue()
					.divide(new BigDecimal("1728"), 4, RoundingMode.HALF_UP);// 立方英寸转换成立方英尺
			storageFee = monthlyFee.multiply(volume).multiply(monthDay);
		}
		return storageFee;
	}
	public BigDecimal getInplaceFee(String invplacefee ,String height,String length,String width,String weight) {
		String country="US";
		InputDimensions inputDimension = new InputDimensions();
		inputDimension.setHeight(new ItemMeasure(new BigDecimal(height),"cm"));
		inputDimension.setLength(new ItemMeasure(new BigDecimal(length),"cm"));
		inputDimension.setWidth(new ItemMeasure(new BigDecimal(width),"cm"));
		inputDimension.setWeight(new ItemMeasure(new BigDecimal(weight),"kg"));
		String productTierId = determineProductTier(country,inputDimension, "0");
		ProductTier productTier = productTierService.selectByPKey(productTierId);
		BigDecimal inPlaceFee = calculateInPlaceFee(country, productTier, inputDimension,invplacefee);
		return inPlaceFee;
	}
	// 计算inventory placement fee
	@Override
	public BigDecimal calculateInPlaceFee(String country, ProductTier productTier, InputDimensions inputDimension,
			String invplaceFee) throws BizException {
		BigDecimal inPlaceFee = new BigDecimal("0");
		boolean isStandard = productTier.getIsstandard();
		BigDecimal weight = inputDimension.getWeight(getWeightUnit(country)).getValue();// 按磅计算
		if (weight == null) {
			weight = new BigDecimal("0");
		}
		if (!invplaceFee.equals("uncalculate")) {
			InplaceFeeFormat inplaceFee = inplaceFeeFormatService.findByInvplaceFeeId(invplaceFee, isStandard, country,productTier.getId());
			if(inplaceFee!=null) {
				String format = inplaceFee.getFormat();// 得到计算公式
				if (format.contains("?")) {// 如果有逻辑判断
					format = (String) AviatorEvaluator.exec(format, weight);
				}
				String result = StringFormat.format(format, weight);// 注意，计算公式里面不能有空格
				inPlaceFee =  new BigDecimal(Calculator.conversion(result));
			}
			
		}
		return inPlaceFee;
	}


}
