package com.wimoor.amazon.base;

import java.util.HashMap;
import java.util.Map;


public class UnionItem extends HashMap<String,String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3489384238190126194L;
	
	public void add(String table,String suffix) {
		   this.put(table, suffix);
	}
	public void addUnionItem(UnionItem unionitem) {
		   this.putAll(unionitem.getTableSuffixItem());
	}
	public Map<String,String> getTableSuffixItem(){
		return this;
	}
	public Map<String, String> getTableSuffix() {
		return this;
	}
};
