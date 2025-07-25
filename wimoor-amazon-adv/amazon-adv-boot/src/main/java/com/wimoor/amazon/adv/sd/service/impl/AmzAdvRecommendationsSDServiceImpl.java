package com.wimoor.amazon.adv.sd.service.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvRecommendationsSDService;
import com.wimoor.common.user.UserInfo;



@Service("amzAdvRecommendationsTargetSDService")
public class AmzAdvRecommendationsSDServiceImpl implements IAmzAdvRecommendationsSDService{

	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	ApiBuildService apiBuildService;
	
	public String amzTargetRecommendationsProductList(UserInfo user, BigInteger profileId,JSONObject param) {
		// TODO Auto-generated method stub
		String url = "/sd/targets/recommendations";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		Map<String,String> headerExt=new HashMap<String,String>();
		headerExt.put("Content-Type","application/vnd.sdtargetingrecommendations.v3.3+json");
		String response = apiBuildService.amzAdvPost(profile, url, param.toString(),headerExt);
		return response;
	}

	public String amzTargetRecommendationsHeadline(UserInfo user, BigInteger profileId,JSONObject param) {
		// TODO Auto-generated method stub
		String url = "/sd/recommendations/creative/headline";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		Map<String,String> headerExt=new HashMap<String,String>();
		headerExt.put("Content-Type","application/vnd.sdheadlinerecommendationrequest.v4.0+json");
		String response = apiBuildService.amzAdvPost(profile, url, param.toString(),headerExt);
		return response;
	}
	
	
	@Override
	public String amzAdvRecommendationsTargetsBid(UserInfo user, BigInteger profileId, JSONObject param) {
		// TODO Auto-generated method stub
		String url = "/sd/targets/bid/recommendations";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		Map<String,String> headerExt=new HashMap<String,String>();
		headerExt.put("Content-Type","application/vnd.sdtargetingrecommendations.v3.3+json");
		String response = apiBuildService.amzAdvPost(profile, url, param.toString(),headerExt);
		return response;
	}
	
}
