package com.wimoor.admin.pojo.entity;


public enum NotifyType {
	Announce("公告",1),Remind("提醒",2),Message("消息",3);
	
	private String name;
	private Integer value;
	
	NotifyType(String name,Integer value){
		this.name=name;
		this.value=value;
	}
	
	public Integer getValue(){
		return value;
	}
	
	public String getName(){
		return name;
	}
}
