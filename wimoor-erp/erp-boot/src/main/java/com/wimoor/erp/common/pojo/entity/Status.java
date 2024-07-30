package com.wimoor.erp.common.pojo.entity;

public enum Status {
	inbound("inbound", 1), fulfillable("fulfillable", 2), outbound("outbound", 3);
	public String value;
	private int index;
	private Status(String value, int index) {
		this.value = value;
		this.index = index;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
