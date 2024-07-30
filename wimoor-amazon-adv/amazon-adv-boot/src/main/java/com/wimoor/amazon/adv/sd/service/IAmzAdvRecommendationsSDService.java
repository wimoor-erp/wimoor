package com.wimoor.amazon.adv.sd.service;

import java.math.BigInteger;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvRecommendationsSDService {
	
	public String amzTargetRecommendationsProductList(UserInfo user, BigInteger profileId,JSONObject param) ;
	
	public String amzTargetRecommendationsHeadline(UserInfo user, BigInteger profileId,JSONObject param);
	
	public String amzAdvRecommendationsTargetsBid(UserInfo user, BigInteger bigInteger, JSONObject param);
}
