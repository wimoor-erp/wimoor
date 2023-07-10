package com.wimoor.amazon.profit.pojo.vo;

import java.math.BigDecimal;
 

public class ItemMeasure{
	public BigDecimal value;
	
	public String units;

	public ItemMeasure(BigDecimal value, String units) {
		this.value = value;
		this.units = units;
	}
	public ItemMeasure( ) {
	 
	}
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public void setValue(String value) {
		this.value = new BigDecimal(value);
	}
	@Override
	public String toString() {
		return "ItemMeasure [value=" + value + ", units=" + units + "]";
	}
	
}