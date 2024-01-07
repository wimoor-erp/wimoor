package com.wimoor.amazon.base;

import java.util.LinkedList;

public class UnionTablesSuffix extends LinkedList<UnionItem>{
/**
	 * 
	 */
 private static final long serialVersionUID = 8349992169155514102L;
String unionSql="union all";
public final static String key="table_suffix";
public final static String self_table_suffix="#";
private boolean isOnlyOneResult=false;
/**
 * @return the key
 */

public String getKey() {
	return key;
}

public String getUnionSql() {
	return unionSql;
}

public void setUnion() {
	this.unionSql = "union";
}

public void setUnionAll() {
	this.unionSql = "union all";
}

public void addTable(UnionItem tablename) {
	this.add(tablename);
}
 

public void addUnionTablesSuffix(UnionTablesSuffix tablesuffix) {
	for(int i=0;i<this.size();i++) {
		UnionItem item=this.get(i);
		UnionItem item2 = tablesuffix.get(i);
		item.addUnionItem(item2);
	}
}

public UnionItem buildUnionItem() {
	// TODO Auto-generated method stub
	return new UnionItem();
}

public boolean isOnlyOneResult() {
	return isOnlyOneResult;
}

public void setOnlyOneResult(boolean isOnlyOneResult) {
	this.isOnlyOneResult = isOnlyOneResult;
}

 
 

}
