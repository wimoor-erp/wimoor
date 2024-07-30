package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.common.user.UserInfo;

import tk.mybatis.mapper.entity.Example;

public interface IAmzAdvKeywordsService extends IService<AmzAdvKeywords> {

	public List<AmzAdvKeywords> getKeywordsByAdgroupId(BigInteger profileId, String adGroupId, String campaignType);

	public List<AmzAdvKeywords> amzGetKeywords(UserInfo user,BigInteger  profileId,JSONObject param);
	
	public List<AmzAdvKeywords> amzCreateKeywords(UserInfo user,BigInteger  profileId, List<AmzAdvKeywords> keywords);

	public List<AmzAdvKeywords> amzUpdateKeywords(UserInfo user,BigInteger  profileId, List<AmzAdvKeywords> biddableKeywords);

	public List<AmzAdvKeywords> amzListBiddableKeywords(UserInfo user,BigInteger  profileId, Map<String, Object> param);
	
	public PageList<Map<String, Object>> getKeywordsList(Map<String, Object> map, PageBounds pageBounds);
	
	public Map<String, Object> catchKeywordSuggestBid(UserInfo user,Map<String, Object> map);

	public Map<String, Object> getKeywordChart(Map<String, Object> map);
	
	public PageList<Map<String, Object>> getKeywordQueryList(Map<String, Object> map, PageBounds pageBounds);
	
	public Map<String, Object> getKeywordQueryChart(Map<String, Object> map);
	
	public AmzAdvKeywords getKeywordforQuery(Map<String, Object> map);
	
	public AmzAdvKeywords selectOneByExample(Example example);
	
	public List<Map<String, Object>> getSumAdvKeywords(Map<String, Object> map);

	public List<AmzAdvKeywords> amzCreateKeywords(UserInfo user, String profileid, JSONArray keywords);

	public List<AmzAdvKeywords> amzUpdateKeywords(UserInfo user, String profileid, JSONArray keywords);

	public List<AmzAdvKeywords> amzDeleteKeywords(UserInfo user,BigInteger  profileId,JSONObject param);
}
