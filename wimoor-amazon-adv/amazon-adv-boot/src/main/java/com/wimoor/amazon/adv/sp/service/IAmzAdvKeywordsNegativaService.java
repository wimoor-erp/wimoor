package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.common.user.UserInfo;
 

import tk.mybatis.mapper.entity.Example;

public interface IAmzAdvKeywordsNegativaService extends IService<AmzAdvKeywordsNegativa>{
	public List<AmzAdvKeywordsNegativa> getKeywordsByAdgroupId(BigInteger profileId, String adGroupId);

	public AmzAdvKeywordsNegativa amzGetNegativaKeyword(UserInfo user,BigInteger  profileId, String keywordId, String campaignType);

	public AmzAdvKeywordsNegativa amzGetNegativaKeywordEx(UserInfo user,BigInteger  profileId, String keywordId);

	public List<AmzAdvKeywordsNegativa> amzCreateNegativaKeywords(UserInfo user,BigInteger  profileId, List<AmzAdvKeywordsNegativa> negetivaKeywords);

	public List<AmzAdvKeywordsNegativa> amzUpdateNegativaKeywords(UserInfo user,BigInteger  profileId, List<AmzAdvKeywordsNegativa> negetivaKeywords);

	public AmzAdvKeywordsNegativa amzArchiveNegativaKeyword(UserInfo user,BigInteger  profileId, String keywordId);

	public List<AmzAdvKeywordsNegativa> amzListNegativaKeywords(UserInfo user,BigInteger  profileId, Map<String, Object> param);

	public List<AmzAdvKeywordsNegativa> amzNegativaKeywordsEx(UserInfo user,BigInteger  profileId, Map<String, Object> param);
	
	public List<Map<String, Object>> getNegativaKeywordsList(Map<String, Object> map);
	
	public AmzAdvKeywordsNegativa getNegativaKeywordforQuery(Map<String, Object> map);
	
	public AmzAdvKeywordsNegativa selectOneByExample(Example example);
}
