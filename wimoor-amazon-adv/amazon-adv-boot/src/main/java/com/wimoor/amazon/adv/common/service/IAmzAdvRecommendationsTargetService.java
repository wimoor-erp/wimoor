package com.wimoor.amazon.adv.common.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.wimoor.common.user.UserInfo;

public interface IAmzAdvRecommendationsTargetService {
	
	public Map<String,Object> amzCreateTargetRecommendations(UserInfo user,BigInteger  profileId,List<String> asinsList,int pageSize, int pageNumber);
	
	public List<Map<String,Object>> amzGetTargetingCategories(UserInfo user,BigInteger profileId  ,String asins);
	
	public List<Map<String,Object>> amzGetBrandRecommendations(UserInfo user,BigInteger profileId  , String keyword,String categoryId);
	
	public List<Map<String, Object>> amzTargetRecommendationsProductList_HSA(UserInfo user, BigInteger profileId, List<String> paramList, int maxResults, String nextToken);
	
	public List<Map<String, Object>> amzRecommendationsCategories_HSA(UserInfo user, BigInteger profileId, List<String> asinsList);
	
	public List<Map<String, Object>> amzRecommendationsBrand_HSA(UserInfo user, BigInteger profileId, String categoryId, String keyword);
	
	public List<Map<String, Object>> amzGetRefinementsForCategory(UserInfo user,BigInteger profileId,String categoryId);

}
