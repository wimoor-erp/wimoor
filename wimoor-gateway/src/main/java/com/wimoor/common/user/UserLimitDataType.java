package com.wimoor.common.user;

public enum UserLimitDataType {
    owner("onwer"),operations("operations");
	UserLimitDataType(String code) {
		this.code = code;
		// TODO Auto-generated constructor stub
	}
	private final String code;
	public String getCode() {
		return code;
	}
	
}
