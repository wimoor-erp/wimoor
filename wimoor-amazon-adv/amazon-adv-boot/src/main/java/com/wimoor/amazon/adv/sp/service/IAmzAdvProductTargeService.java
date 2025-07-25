package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvProductTargeService extends IService<AmzAdvProductTarge>{
	

	public List<AmzAdvProductTarge> amzGetTargets(UserInfo user, BigInteger bigInteger, JSONObject param);

	public List<AmzAdvProductTarge> amzUpdateTargets(UserInfo user, String profileid, JSONArray keywords);

	public List<AmzAdvProductTarge> amzCreateTargets(UserInfo user, String profileid, JSONArray keywords);
	
	public List<AmzAdvProductTarge> amzCreateTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvProductTarge> productTargeList);
	
	public List<AmzAdvProductTarge> amzUpdateTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvProductTarge> productTargeList);
	
	public List<AmzAdvProductTarge> amzDeleteTargets(UserInfo user, BigInteger profileId, JSONObject param);
	
	public PageList<Map<String, Object>> getProductTargeList(Map<String,Object> map, PageBounds pageBounds);
	
	public Map<String,Object> getProductTargeChart(Map<String,Object> map);
	
	public List<Map<String,Object>> getTargetCategories(Map<String,Object> map);
	
	public List<Map<String,Object>> getSumProductTarge(Map<String,Object> map);
	
	
	




	
}
