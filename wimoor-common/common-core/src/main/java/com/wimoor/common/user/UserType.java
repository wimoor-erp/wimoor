package com.wimoor.common.user;

public enum UserType {
    admin("admin"),manager("manager"),actor("actor"), support("support");
	UserType(String code) {
		this.code = code;
		// TODO Auto-generated constructor stub
	}
	private final String code;
	public String getCode() {
		return code;
	}
	
}
