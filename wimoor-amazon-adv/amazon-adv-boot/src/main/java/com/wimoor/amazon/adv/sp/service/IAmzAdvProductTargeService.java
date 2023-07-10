package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvProductTargeService extends IService<AmzAdvProductTarge>{
	
	public AmzAdvProductTarge amzGetTargetingClause(UserInfo user,BigInteger  profileId, String targetId);
	
	public AmzAdvProductTarge amzGetTargetingClauseEx(UserInfo user,BigInteger  profileId,String targetId);
	
	public List<AmzAdvProductTarge> amzCreateTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvProductTarge> productTargeList);
	
	public List<AmzAdvProductTarge> amzUpdateTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvProductTarge> productTargeList);
	
	public AmzAdvProductTarge amzArchiveTargetingClause(UserInfo user,BigInteger  profileId,String targetId);
	
	public List<AmzAdvProductTarge> amzListTargetingClauses(UserInfo user,BigInteger  profileId,Map<String,Object> param);
	
	public List<AmzAdvProductTarge> amzListTargetingClausesEx(UserInfo user,BigInteger  profileId,Map<String,Object> param);
	
	public List<AmzAdvProductTarge> amzCreateTargetingClauses_V3Hsa(UserInfo user,BigInteger  profileId,List<AmzAdvProductTarge> productTargeList);
	
	public List<AmzAdvProductTarge> amzUpdateTargetingClauses_V3Hsa(UserInfo user,BigInteger  profileId,List<AmzAdvProductTarge> productTargeList);
	
	public AmzAdvProductTarge amzArchiveTargetingClause_V3Hsa(UserInfo user,BigInteger  profileId,String targetId);
	
	public PageList<Map<String, Object>> getProductTargeList(Map<String,Object> map, PageBounds pageBounds);
	
	public Map<String,Object> getProductTargeChart(Map<String,Object> map);
	
	public List<Map<String,Object>> getTargetCategories(Map<String,Object> map);
	
	public List<Map<String,Object>> getSumProductTarge(Map<String,Object> map);
	
}
