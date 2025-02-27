package com.wimoor.amazon.adv.common.service.impl;

 

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.common.pojo.AmzAdvBrowseNode;
import com.wimoor.amazon.adv.common.service.IAmzAdvBrowseNodeService;
import com.wimoor.amazon.base.BaseService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
 
@Service("amzAdvBrowseNodeService")
public class AmzAdvBrowseNodeServiceImpl extends BaseService<AmzAdvBrowseNode> implements  IAmzAdvBrowseNodeService {
   
	public Map<BigInteger, AmzAdvBrowseNode> getAllBrowseNodeMap(String country) {
		Example example=new Example(AmzAdvBrowseNode.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("country",country);
		List<AmzAdvBrowseNode> list = this.mapper.selectByExample(example);
		if(list==null)return null;
		Map<BigInteger, AmzAdvBrowseNode> result=new HashMap<BigInteger, AmzAdvBrowseNode>(); 
		for(AmzAdvBrowseNode item:list) {
			result.put(item.getId(), item);
		}
		return result;
	}

	public  String setChildrenMap(Map<BigInteger, AmzAdvBrowseNode> allMap,
			AmzAdvBrowseNode child) {
		// TODO Auto-generated method stub
		AmzAdvBrowseNode parent = allMap.get(child.getParentid());
		String parentstr=child.getName();
		while(parent!=null) {
			child=parent;
			parentstr=child.getName()+"/"+parentstr;
			parent= allMap.get(parent.getParentid());;
		}
		return parentstr;
	}

	public   List<HashMap<String, Object>> getTreeMap(List<AmzAdvBrowseNode> list,String country) {
		// TODO Auto-generated method stub
		Map<BigInteger, AmzAdvBrowseNode> allMap = getAllBrowseNodeMap(country) ;
		List<HashMap<String, Object>> resultMap=  new ArrayList<HashMap<String,Object>>();
		for(AmzAdvBrowseNode item:list) {
			String path = this.setChildrenMap(allMap, item);
			HashMap<String,Object> result=new HashMap<String,Object>();
			result.put("id",item.getId());
			result.put("path",path);
			result.put("name", item.getName());
			resultMap.add(result);
		}
		return resultMap;
	}
	
	
	public  List<HashMap<String, Object>> searchBroseNode(String search,String country) {
		// TODO Auto-generated method stub
		Example example=new Example(AmzAdvBrowseNode.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("country",country);
		crit.andLike("name", "%"+search+"%");
		List<AmzAdvBrowseNode> list = this.mapper.selectByExample(example);
		if(list==null)return null;
		return this.getTreeMap(list, country);
	}
	
	public  List<AmzAdvBrowseNode> getRootBroseNode(String country) {
		// TODO Auto-generated method stub
		Example example=new Example(AmzAdvBrowseNode.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("country",country);
		crit.andEqualTo("parentid",0);
		List<AmzAdvBrowseNode> list = this.mapper.selectByExample(example);
		if(list==null)return null;
		return list;
	}
	
	public  List<AmzAdvBrowseNode> getChildRenBroseNode(BigInteger nodeid,String country) {
		// TODO Auto-generated method stub
		Example example=new Example(AmzAdvBrowseNode.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("country",country);
		crit.andEqualTo("parentid",nodeid);
		List<AmzAdvBrowseNode> list = this.mapper.selectByExample(example);
		if(list==null)return null;
		return list;
	}
}
