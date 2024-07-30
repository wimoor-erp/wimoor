package com.wimoor.amazon.adv.common.service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.AmzAdvBrowseNode;
 

public interface IAmzAdvBrowseNodeService extends   IService<AmzAdvBrowseNode>{
	Map<BigInteger,AmzAdvBrowseNode> getAllBrowseNodeMap(String country);
	String setChildrenMap(Map<BigInteger,AmzAdvBrowseNode> allMap,AmzAdvBrowseNode child);
	public  List<HashMap<String, Object>> getTreeMap(List<AmzAdvBrowseNode> list,String country) ;
	public  List<AmzAdvBrowseNode> getRootBroseNode(String country);
	public  List<AmzAdvBrowseNode> getChildRenBroseNode(BigInteger nodeid,String country);
	List<HashMap<String, Object>> searchBroseNode(String name, String country);
}
