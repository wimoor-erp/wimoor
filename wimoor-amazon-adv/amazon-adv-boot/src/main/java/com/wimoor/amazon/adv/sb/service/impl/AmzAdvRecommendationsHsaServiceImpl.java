package com.wimoor.amazon.adv.sb.service.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvRecommendationsHsaService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;


@Service("amzAdvRecommendationsHsaService")
public class AmzAdvRecommendationsHsaServiceImpl implements IAmzAdvRecommendationsHsaService{
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	ApiBuildService apiBuildService;
	
	
	public String amzAdvRecommendationsBid(UserInfo user, BigInteger profileId, JSONObject param ) {
		// TODO Auto-generated method stub
		String url = "/sb/recommendations/bids";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = apiBuildService.amzAdvPost(profile, url, param.toString());
		return response;
	}
	
	public String amzKeywordRecommendations(UserInfo user, BigInteger profileId, JSONObject param ) {
		// TODO Auto-generated method stub
		String url = "/sb/recommendations/keyword";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = apiBuildService.amzAdvPost(profile, url, param.toString());
		return response;
	}

	@Override
	public String amzAdvRecommendationsNegativeTargetsBrands(UserInfo user, BigInteger profileId, JSONObject param) {
		// TODO Auto-generated method stub
		String url = "/sb/negativeTargets/brands/recommendations";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		Map<String, String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/vnd.sbtargeting.v4+json");
    	header.put("Accept",       "application/vnd.sbtargeting.v4+json");
		String response = apiBuildService.amzAdvGet(profile, url,header);
		return response;
	}

	@Override
	public String amzAdvRecommendationsTargetsCategories(UserInfo user, BigInteger profileId, JSONObject param) {
		// TODO Auto-generated method stub
		String url =  "/sb/recommendations/targets/category?locale=zh_CN";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = apiBuildService.amzAdvPost(profile, url,param.toString());
		return response;
	}

	@Override
	public String amzTargetRecommendationsProductList(UserInfo user, BigInteger profileId, JSONObject param) {
		// TODO Auto-generated method stub
		String url =  "/sb/recommendations/targets/product/list";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = apiBuildService.amzAdvPost(profile, url,param.toString());
		return response;
	}

	@Override
	public String amzAdvRecommendationsTargetsRefine(UserInfo user, BigInteger profileId, JSONObject param) {
		// TODO Auto-generated method stub
		String url =  "/sb/targets/categories/"+param.getString("categoryId")+"/refinements";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		Map<String, String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/vnd.sbtargeting.v4+json");
    	header.put("Accept",       "application/vnd.sbtargeting.v4+json");
		String response = apiBuildService.amzAdvGet(profile, url,header);
		return response;
	}

	@Override
	public String amzAdvRecommendationsProductCount(UserInfo user, BigInteger profileId, JSONObject param) {
		// TODO Auto-generated method stub
		String url =  "/sb/targets/products/count";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		Map<String, String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/vnd.sbtargeting.v4+json");
    	header.put("Accept",       "application/vnd.sbtargeting.v4+json");
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),header);
		return response;
	}

	@Override
	public String amzAdvRecommendationsTargetsCategoriesAll(UserInfo user, BigInteger profileId, JSONObject param) {
		// TODO Auto-generated method stub
		String url =  "/sb/targets/categories";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "locale");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "supplySource");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "includeOnlyRootCategories");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "parentCategoryRefinementId");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "nextToken");
		}
		url = url+ ("".equals(paramurl) ? "" : "?" + paramurl);
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		Map<String, String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/vnd.sbtargeting.v4+json");
    	header.put("Accept",       "application/vnd.sbtargeting.v4+json");
		String response = apiBuildService.amzAdvGet(profile, url,header);
		return response;
	}
	
}
