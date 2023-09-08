package com.wimoor.amazon.profit.pojo.vo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.amazon.spapi.model.catalogitems.Dimensions;
import com.wimoor.common.mvc.BizException;
 
 
public 	class InputDimensions {
	public static final String unit_in = "in";
	public static final String unit_cm = "cm";
	public static final String unit_kg = "kg";
	public static final String unit_g = "g";
	public static final String unit_lb = "lb";
	public static final String unit_oz = "oz";
	
	Map<String, BigDecimal> unitConversionMap = null;
 
	private ItemMeasure length;
 
	private ItemMeasure width;
 
	private ItemMeasure height;
 
	private ItemMeasure weight;
	
	private ItemMeasure girth;// 计算周长
	private ItemMeasure volume;// 计算体积
	private ItemMeasure dimensionalWeight;//维度重量
	
	public InputDimensions(BigDecimal length, BigDecimal width, BigDecimal height, String LUnit, BigDecimal weight, String WUnit) {
		this.setLength(new ItemMeasure(length, LUnit));
		this.setWidth(new ItemMeasure(width, LUnit));
		this.setHeight(new ItemMeasure(height, LUnit));
		this.setWeight(new ItemMeasure(weight, WUnit));
	}

	public InputDimensions(BigDecimal length, BigDecimal width, BigDecimal height, String LUnit) {
		this.setLength(new ItemMeasure(length, LUnit));
		this.setWidth(new ItemMeasure(width, LUnit));
		this.setHeight(new ItemMeasure(height, LUnit));
	}

	public InputDimensions() {
	}
	
	public ItemMeasure getLength() {
		return length;
	}
	public ItemMeasure getLength(String toUnit) throws BizException {
		return ConversionTo(toUnit,length);
	}
	public void setLength(ItemMeasure length) {
		this.length = length;
	}
	public ItemMeasure getWidth() {
		return width;
	}
	public ItemMeasure getWidth(String toUnit) throws BizException {
		return ConversionTo(toUnit,width);
	}
	public void setWidth(ItemMeasure width) {
		this.width = width;
	}
	public ItemMeasure getHeight(String toUnit) throws BizException {
		return ConversionTo(toUnit,height);
	}
	public ItemMeasure getHeight() {
		return height;
	}
	public void setHeight(ItemMeasure height) {
		this.height = height;
	}
	public ItemMeasure getWeight() {
		return weight;
	}
	public ItemMeasure getWeight(String toUnit) throws BizException {
		return ConversionTo(toUnit,weight);
	}
	public void setWeight(ItemMeasure weight) {
		this.weight = weight;
	}
	public ItemMeasure getGirth() {
		return girth;
	}
	public ItemMeasure getGirth(String toUnit) throws BizException {
		BigDecimal d = (getHeight(toUnit).getValue().add(getWidth(toUnit).getValue())).multiply(new BigDecimal("2"));
		girth = new ItemMeasure(d,toUnit);
		return girth;
	}
	public void setGirth(ItemMeasure girth) {
		this.girth = girth;
	}
	public ItemMeasure getVolume() {
		return volume;
	}
	public ItemMeasure getVolume(String toUnit) throws BizException {
		if(getLength(toUnit).getValue()==null||getWidth(toUnit).getValue()==null||getHeight(toUnit).getValue()==null){
			return new ItemMeasure(new BigDecimal("0"),toUnit);
		}
		BigDecimal v = getLength(toUnit).getValue().multiply(getWidth(toUnit).getValue()).multiply(getHeight(toUnit).getValue());
		volume = new ItemMeasure(v, toUnit);
		return volume;
	}
	public void setVolume(ItemMeasure volume) {
		this.volume = volume;
	}
	public ItemMeasure getDimensionalWeight() {
		return dimensionalWeight;
	}
	
	public ItemMeasure getDimensionalWeight(String toUnit) throws BizException {
		if (toUnit!=null&&toUnit.equals(unit_in)) {
			//Effective February 22, 2018, we will calculate dimensional weight as the unit volume (based on length x width x height in inches) divided by 139 (instead of 166). 
			BigDecimal d = getVolume(toUnit).getValue().divide(new BigDecimal("139"),4,BigDecimal.ROUND_HALF_UP);
			dimensionalWeight = new ItemMeasure(d, toUnit);
		} else if (toUnit!=null&&toUnit.equals(unit_cm)) {
			//自 2018 年 8 月 29 日起，我们将采用以下方式计算体积重量：商品体积（根据长 x 宽 x 高，以立方厘米为单位）除以 5,000（而非之前的 6,000）
			BigDecimal d = getVolume(toUnit).getValue().divide(new BigDecimal("5000"),4,BigDecimal.ROUND_HALF_UP);
			dimensionalWeight = new ItemMeasure(d, toUnit);
		}
		return dimensionalWeight;
	}
	
	public ItemMeasure getDimensionalWeight(BigDecimal baseData) throws BizException {
		    BigDecimal d = getVolume(InputDimensions.unit_cm).getValue().divide(baseData,4,BigDecimal.ROUND_HALF_UP);
			dimensionalWeight = new ItemMeasure(d, InputDimensions.unit_cm);
		return dimensionalWeight;
	}
	
	public ItemMeasure getDimensionalWeight(String toUnit,BigDecimal length_in,BigDecimal width_in,BigDecimal height_in) {
		if (toUnit.equals(unit_in)) {
			BigDecimal d = getVolume(toUnit).getValue().divide(new BigDecimal("139"),4,BigDecimal.ROUND_HALF_UP);
			dimensionalWeight = new ItemMeasure(d, toUnit);
		} else if (toUnit.equals(unit_cm)) {
			BigDecimal d = getVolume(toUnit).getValue().divide(new BigDecimal("5000"),4,BigDecimal.ROUND_HALF_UP);
			dimensionalWeight = new ItemMeasure(d, toUnit);
		}
		return dimensionalWeight;
	}
	public void setDimensionalWeight(ItemMeasure dimensionalWeight) {
		this.dimensionalWeight = dimensionalWeight;
	}
	
	@Override
	public String toString() {
		return "InputDimensions [length=" + length + ", width=" + width
				+ ", height=" + height + ", weight=" + weight + ", girth=" + girth + ", volume=" + volume
				+ ", dimensionalWeight=" + dimensionalWeight + "]";
	}

	public ItemMeasure ConversionTo(String toUnit, ItemMeasure measure) throws BizException {// 只支持cm和in转换
		if (unitConversionMap == null) {
			unitConversionMap = new HashMap<String, BigDecimal>();
			// map.put("from-to", value)
			unitConversionMap.put(unit_cm+"-"+unit_in, new BigDecimal("0.3937"));
			unitConversionMap.put(unit_in+"-"+unit_cm, new BigDecimal("2.54"));
			unitConversionMap.put(unit_kg+"-"+unit_lb, new BigDecimal("2.2046"));
			unitConversionMap.put(unit_lb+"-"+unit_kg, new BigDecimal("0.4536"));
			unitConversionMap.put(unit_oz+"-"+unit_kg, new BigDecimal("0.02835"));
			unitConversionMap.put(unit_oz+"-"+unit_lb, new BigDecimal("0.0625"));
			unitConversionMap.put(unit_g+"-"+unit_kg, new BigDecimal("0.001"));
			unitConversionMap.put(unit_g+"-"+unit_lb, new BigDecimal("0.0022"));
			unitConversionMap.put(unit_g+"-"+unit_oz, new BigDecimal("0.0353"));
			unitConversionMap.put(unit_kg+"-"+unit_g, new BigDecimal("1000"));
			unitConversionMap.put(unit_lb+"-"+unit_g, new BigDecimal("453.6"));
			unitConversionMap.put(unit_oz+"-"+unit_g, new BigDecimal("28.35"));
			unitConversionMap.put(unit_kg+"-"+unit_oz, new BigDecimal("35.274"));
			unitConversionMap.put(unit_lb+"-"+unit_oz, new BigDecimal("16"));
		}
		if (measure==null||measure.getValue()==null||measure.getUnits()==null) {
			return new ItemMeasure();
		}
		BigDecimal value = null;
		if (measure.getUnits().equals(toUnit)) {
			value = measure.getValue();
		} else {
			String key = (measure.getUnits() + "-" + toUnit).trim();
			if (unitConversionMap.get(key)!=null) {
				value = measure.getValue().multiply(unitConversionMap.get(key));
			} else{
				throw new BizException(key+"在unitConversionMap里面没有找到对应单位的转换关系！请联系管理员！");
			}
		}
		if("cm".equals(toUnit)) {
			return new ItemMeasure(value.setScale(1, BigDecimal.ROUND_HALF_UP), toUnit);
		}else {
			return new ItemMeasure(value.setScale(2, BigDecimal.ROUND_HALF_UP), toUnit);
		}
	}
	
	public void clearUnitConversionMap(){
		unitConversionMap = null;
	}
	
	public static InputDimensions getTrueInputDimesions(Dimensions dimensions) {
		InputDimensions itemDim = new InputDimensions();
		String LUnit = null;
		if(dimensions==null)return null;
		if (dimensions.getLength() != null && dimensions.getLength().getValue() != null
				&& dimensions.getLength().getUnit() != null) {
			LUnit = dimensions.getLength().getUnit();
			if (LUnit != null && LUnit.toLowerCase().contains("inches")) {
				LUnit = InputDimensions.unit_in;
			}
			if (LUnit != null && LUnit.toLowerCase().contains("centimeters")) {
				LUnit = InputDimensions.unit_cm;
			}
			ItemMeasure lengh = new ItemMeasure(dimensions.getLength().getValue(), LUnit);
			itemDim.setLength(lengh);
		}
		if (dimensions.getWidth() != null && dimensions.getWidth().getValue() != null && LUnit != null) {
			ItemMeasure width = new ItemMeasure(dimensions.getWidth().getValue(), LUnit);
			itemDim.setWidth(width);
		}
		if (dimensions.getHeight() != null && dimensions.getHeight().getValue() != null && LUnit != null) {
			ItemMeasure height = new ItemMeasure(dimensions.getHeight().getValue(), LUnit);
			itemDim.setHeight(height);
		}
		if (dimensions.getWeight() != null && dimensions.getWeight().getValue() != null
				&& dimensions.getWeight().getUnit() != null) {
			String WUnit = dimensions.getWeight().getUnit();
			if (WUnit != null && WUnit.toLowerCase().contains("pounds")) {
				WUnit = InputDimensions.unit_lb;
			}
			if (WUnit != null && WUnit.toLowerCase().contains("ounces")) {
				WUnit = InputDimensions.unit_oz;
			}
			if (WUnit != null && WUnit.toLowerCase().contains("kilograms")) {
				WUnit = InputDimensions.unit_kg;
			}
			if (WUnit != null && WUnit.toLowerCase().contains("grams")) {
				WUnit = InputDimensions.unit_g;
			}
			ItemMeasure weight = new ItemMeasure(dimensions.getWeight().getValue(), WUnit);
			itemDim.setWeight(weight);
		}
		return itemDim;
	}
	
 

}