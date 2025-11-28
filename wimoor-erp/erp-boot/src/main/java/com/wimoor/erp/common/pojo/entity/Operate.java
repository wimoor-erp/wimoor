package com.wimoor.erp.common.pojo.entity;

public enum Operate {
	in("in", 1), 
	out("out", 2), 
	undoout("undoout", 2), 
	cancel("cancel", 3), 
	stop("stop", 4), 
	readyin("readyin", 5),
	readyout("readyout", 5), 
	changesub("changesub", 6), 
	changeadd("changeadd", 7);

	private String value;
	private int index;

	private Operate(String value, int index) {
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
