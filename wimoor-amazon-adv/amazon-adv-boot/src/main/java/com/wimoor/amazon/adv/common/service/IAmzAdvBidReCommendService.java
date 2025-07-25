package com.wimoor.amazon.adv.common.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvBidReCommendService {
	

	Map<String, Object> amzGetKeywordBidRecommendations(UserInfo user,BigInteger  profileId, String keywordId);
	
	List<Map<String,Object>> amzCreateKeywordBidRecommendations(UserInfo user,BigInteger  profileId,String adGroupId,List<Map<String,Object>> list);
	
	String bidRecommendations(BigInteger profileid,String adGroupId,String keywords,String matchType);
	
	List<Map<String, Object>> amzGetTargetBidRecommendations(UserInfo user,BigInteger profileId,String adGroupId,JSONArray jsona);
	
	List<Map<String, Object>> amzRecommendationsBidsForKeyword_HSA(UserInfo user, BigInteger profileId, BigInteger campaignId, 
			List<Map<String, Object>> paramList);
	
	JSONArray amzRecommendationsBidsForTarget_HSA(UserInfo user, BigInteger profileId, BigInteger campaignId, JSONArray jsona);
}
