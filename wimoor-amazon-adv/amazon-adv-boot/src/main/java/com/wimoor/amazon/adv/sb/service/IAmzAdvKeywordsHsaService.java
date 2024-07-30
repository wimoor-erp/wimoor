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
import com.wimoor.amazon.adv.sb.pojo.AmzAdvKeywordsHsa;
import com.wimoor.common.user.UserInfo;


public interface IAmzAdvKeywordsHsaService extends IService<AmzAdvKeywordsHsa> {
	public List<AmzAdvKeywordsHsa> amzListSBKeywords(AmzAdvProfile profile, Map<String, Object> param) ;
	public List<AmzAdvKeywordsHsa> amzCreateKeywords(UserInfo user, BigInteger profileId, List<AmzAdvKeywordsHsa> list) ;
	public List<AmzAdvKeywordsHsa> amzCreateKeywords(UserInfo user, String profileid, JSONArray keywords);
	public List<AmzAdvKeywordsHsa> amzUpdateKeywords(UserInfo user, String profileid, JSONArray keywords);
	public AmzAdvKeywordsHsa amzDeleteKeywords(UserInfo user,BigInteger  profileId, JSONObject param);
	public List<AmzAdvKeywordsHsa> amzGetKeywords(UserInfo user, BigInteger profileid,Map<String, Object> param);
	public PageList<Map<String, Object>> getKeywordsList(Map<String, Object> map, PageBounds pageBounds);
	public Map<String, Object> getSumAdvKeywords(Map<String, Object> map);
	public List<Map<String, Object>> getKeywordsChart(Map<String, Object> map);
	public AmzAdvKeywordsHsa amzGetKeywords(UserInfo user, BigInteger profileid, String id);
	public PageList<Map<String, Object>> getKeywordQueryList(Map<String, Object> map, PageBounds pageBounds);
	public List<Map<String, Object>> getKeywordQueryChart(Map<String, Object> map);
}
