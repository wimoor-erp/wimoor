package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.wimoor.common.user.UserInfo;

public interface IAmzAdvKeywordsSuggestService{
	Map<String, Object> amzAdGroupSuggestedKeywords(UserInfo user,BigInteger profileId,Map<String, Object> keywordsParam);
	
	List<Map<String, Object>> amzAdGroupSuggestedKeywordsEx(UserInfo user,BigInteger profileId,Map<String, Object> keywordsParam);
	
	Map<String, Object> amzAsinSuggestedKeywords(UserInfo user,BigInteger profileId,Map<String, Object> keywordsParam);
	
	List<Map<String,Object>> bulkGetAsinSuggestedKeywords(UserInfo user,BigInteger profileId,List<String> asinsList,int maxNumSuggestions);
	
	List<Map<String,Object>> amzKeywordRecommendations_HSA(UserInfo user,BigInteger profileId,List<String> asinsList,String pageUrl);
	
}
