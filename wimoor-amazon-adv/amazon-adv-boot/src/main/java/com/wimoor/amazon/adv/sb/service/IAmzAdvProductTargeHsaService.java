package com.wimoor.amazon.adv.sb.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvProductTargeHsa;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvProductTargeHsaService extends IService<AmzAdvProductTargeHsa>{
	

	public List<AmzAdvProductTargeHsa> amzGetTargets(AmzAdvProfile profile, JSONObject param);
	public List<AmzAdvProductTargeHsa> amzGetTargets(BigInteger profileId, JSONObject param);

	public List<AmzAdvProductTargeHsa> amzUpdateTargets(UserInfo user, String profileid, JSONArray keywords);

	public List<AmzAdvProductTargeHsa> amzCreateTargets(UserInfo user, String profileid, JSONArray keywords);
	
	public List<AmzAdvProductTargeHsa> amzCreateTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvProductTargeHsa> productTargeList);
	
	public List<AmzAdvProductTargeHsa> amzUpdateTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvProductTargeHsa> productTargeList);
	
	public PageList<Map<String, Object>> getProductTargeList(Map<String, Object> map, PageBounds pageBounds);
	
	public Map<String, Object> getSumProductTarge(Map<String, Object> map);
	
	public List<Map<String, Object>> getProductTargeChart(Map<String, Object> map);
	
	public AmzAdvProductTargeHsa amzGetTargets(UserInfo user, BigInteger profileId, String item);
	AmzAdvProductTargeHsa amzDeleteTargets(UserInfo user, BigInteger profileId, String targetId);
	
}
