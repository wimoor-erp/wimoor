package com.wimoor.amazon.adv.sb.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvKeywordsNegativaHsa;
import com.wimoor.common.user.UserInfo;
 

import tk.mybatis.mapper.entity.Example;

public interface IAmzAdvKeywordsNegativaHsaService extends IService<AmzAdvKeywordsNegativaHsa>{
	public List<AmzAdvKeywordsNegativaHsa> getKeywordsByAdgroupId(BigInteger profileId, String adGroupId);

	public AmzAdvKeywordsNegativaHsa amzGetNegativaKeyword(UserInfo user,BigInteger  profileId, String keywordId, String campaignType);

	public AmzAdvKeywordsNegativaHsa amzGetNegativaKeywordEx(UserInfo user,BigInteger  profileId, String keywordId);

	public List<AmzAdvKeywordsNegativaHsa> amzCreateNegativaKeywords(UserInfo user,BigInteger  profileId, List<AmzAdvKeywordsNegativaHsa> negetivaKeywords);

	public List<AmzAdvKeywordsNegativaHsa> amzUpdateNegativaKeywords(UserInfo user,BigInteger  profileId, List<AmzAdvKeywordsNegativaHsa> negetivaKeywords);


	public List<AmzAdvKeywordsNegativaHsa> amzNegativaKeywordsEx(UserInfo user,BigInteger  profileId, Map<String, Object> param);
	
	public List<Map<String, Object>> getNegativaKeywordsList(Map<String, Object> map);
	
	public AmzAdvKeywordsNegativaHsa getNegativaKeywordforQuery(Map<String, Object> map);
	
	public AmzAdvKeywordsNegativaHsa selectOneByExample(Example example);

	public List<AmzAdvKeywordsNegativaHsa> amzCreateNegativaKeywords(UserInfo user, String profileid, JSONArray nkeywords);

	public AmzAdvKeywordsNegativaHsa amzDeleteNegativaKeywords(UserInfo user, BigInteger profileid, String id);
}
