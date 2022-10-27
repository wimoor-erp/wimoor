package com.wimoor.admin.pojo.entity;


public enum NotifyTarget {
	purchase("库存补货","BH"),shipment("库存发货","FH"),productAnalysis("商品分析","FX"),
	systemMessage("系统默认订阅","SY"),amazonAuth("店铺授权","SH");
	
	private String name;
	private String value;
	
	NotifyTarget(String name,String value){
		this.name=name;
		this.value=value;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getName(){
		return name;
	}

}
