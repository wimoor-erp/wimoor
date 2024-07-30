package com.wimoor.amazon.adv.sb.service;

import java.math.BigInteger;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvRecommendationsHsaService{
	public String amzKeywordRecommendations(UserInfo user, BigInteger profileId, JSONObject param );
	public String amzAdvRecommendationsBid(UserInfo user, BigInteger profileId, JSONObject param );
	public String amzAdvRecommendationsNegativeTargetsBrands(UserInfo user, BigInteger bigInteger, JSONObject param);
	public String amzAdvRecommendationsTargetsCategories(UserInfo user, BigInteger bigInteger, JSONObject param);
	public String amzTargetRecommendationsProductList(UserInfo user, BigInteger bigInteger, JSONObject param);
	public String amzAdvRecommendationsTargetsRefine(UserInfo user, BigInteger bigInteger, JSONObject param);
	public String amzAdvRecommendationsProductCount(UserInfo user, BigInteger bigInteger, JSONObject param);
	public String amzAdvRecommendationsTargetsCategoriesAll(UserInfo user, BigInteger bigInteger, JSONObject param);
}
