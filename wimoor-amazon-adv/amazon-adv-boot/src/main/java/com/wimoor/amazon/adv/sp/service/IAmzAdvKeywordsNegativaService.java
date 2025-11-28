package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.common.user.UserInfo;
 

import tk.mybatis.mapper.entity.Example;

public interface IAmzAdvKeywordsNegativaService extends IService<AmzAdvKeywordsNegativa>{
	public List<AmzAdvKeywordsNegativa> getKeywordsByAdgroupId(BigInteger profileId, String adGroupId);

	public List<AmzAdvKeywordsNegativa> amzCreateNegativaKeywords(UserInfo user,BigInteger  profileId, List<AmzAdvKeywordsNegativa> negetivaKeywords);

	public List<AmzAdvKeywordsNegativa> amzUpdateNegativaKeywords(UserInfo user,BigInteger  profileId, List<AmzAdvKeywordsNegativa> negetivaKeywords);

	public List<AmzAdvKeywordsNegativa> amzDeleteNegativaKeywords(UserInfo user,BigInteger profileId,JSONObject param);
	
	public List<AmzAdvKeywordsNegativa> amzListNegativaKeywords(UserInfo user,BigInteger  profileId, JSONObject param);

	public List<Map<String, Object>> getNegativaKeywordsList(Map<String, Object> map);
	
	public AmzAdvKeywordsNegativa getNegativaKeywordforQuery(Map<String, Object> map);
	
	public AmzAdvKeywordsNegativa selectOneByExample(Example example);

	public List<AmzAdvKeywordsNegativa> amzUpdateNegativaKeywords(UserInfo user, String profileid, JSONArray keywordArray);

	public List<AmzAdvKeywordsNegativa> amzCreateNegativaKeywords(UserInfo user, String profileid, JSONArray nkeywords);
}
