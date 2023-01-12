package com.wimoor.erp.common.pojo.entity;


//枚举类(单据的状态)
 
public enum EnumByFormType {
		//准备     已准备完毕   正在盘点
	purchase("purchase","采购单"),
	assembly("assembly","组装单"),
	dispatch("dispatch","调库但"),
	dispatch_inner("dispatch-inner","商品互调"),
	outstockform("outstockform","FBA出库单"),
	stocktaking("stocktaking","盘点单");

	String value;
	String name;
	EnumByFormType(String value, String name) {
		// TODO Auto-generated constructor stub
	   this.value=value;
	   this.name=name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
 

}
