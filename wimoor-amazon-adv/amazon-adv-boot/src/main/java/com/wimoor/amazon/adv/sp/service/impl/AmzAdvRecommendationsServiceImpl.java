package com.wimoor.amazon.adv.sp.service.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvRecommendationsService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import tk.mybatis.mapper.util.StringUtil;
@Service("amzAdvRecommendationsService")
public class AmzAdvRecommendationsServiceImpl implements IAmzAdvRecommendationsService{
	
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	ApiBuildService apiBuildService;
	
	public JSONArray amzAdvRecommendationsNegativeTargetsBrandsSearch(UserInfo user,BigInteger profileId,JSONObject param) {
			AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
			String url = "/sp/negativeTargets/brands/search"; 
			Map<String,String> header=new HashMap<String,String>();
	    	header.put("Content-Type", "application/vnd.spproducttargeting.v3+json");
	    	header.put("Accept",       "application/vnd.spproducttargeting.v3+json");
			String response = apiBuildService.amzAdvPost(profile, url,param.toString(),header);
			if (StringUtil.isNotEmpty(response)) {
				JSONArray json=GeneralUtil.getJsonArray(response);
				return json;
			}
			return null;
	}

	
	public JSONArray amzAdvRecommendationsNegativeTargetsBrands(UserInfo user,BigInteger profileId,JSONObject param) {
			AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
			String url ="/sp/negativeTargets/brands/recommendations";
			Map<String,String> header=new HashMap<String,String>();
	    	header.put("Accept",       "application/vnd.spproducttargetingresponse.v3+json");
			String response = apiBuildService.amzAdvGet(profile, url,header);
			if (StringUtil.isNotEmpty(response)) {
				JSONArray json=GeneralUtil.getJsonArray(response);
				return json;
			}
			return null;
	}
	
	public String amzAdvRecommendationsTargetsKeywords(UserInfo user,BigInteger profileId,JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url =   "/sp/targets/keywords/recommendations";
		Map<String,String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/vnd.spkeywordsrecommendation.v5+json");
    	header.put("Accept",       "application/vnd.spkeywordsrecommendation.v5+json");
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),header);
		return response;
}
	
	
	public String amzAdvRecommendationsTargetsProducts(UserInfo user,BigInteger profileId,JSONObject param) {
			AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
			String url = "/sp/targets/products/recommendations"; 
			Map<String,String> header=new HashMap<String,String>();
	    	header.put("Content-Type", "application/vnd.spproductrecommendation.v3+json");
	    	header.put("Accept",       "application/vnd.spproductrecommendation.v3+json");
			return  apiBuildService.amzAdvPost(profile, url,param.toString(),header);
	}
	
	public String amzAdvRecommendationsTargetsBid(UserInfo user,BigInteger profileId,JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets/bid/recommendations"; 
		Map<String,String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/vnd.spthemebasedbidrecommendation.v3+json");
    	header.put("Accept",       "application/vnd.spthemebasedbidrecommendation.v3+json");
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),header);
		return response;
}
	
	
	public String amzAdvRecommendationsTargetsCategories(UserInfo user,BigInteger profileId,JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets/categories/recommendations?locale=zh_CN"; 
		Map<String,String> header=new HashMap<String,String>();
		header.put("Content-Type", "application/vnd.spproducttargeting.v3+json");
    	header.put("Accept",       "application/vnd.spproducttargeting.v3+json");
		return   apiBuildService.amzAdvPost(profile, url,param.toString(),header);
	}
    @Cacheable(value="targetCategory",key="#profileId")
	public String amzAdvRecommendationsTargetsCategoriesAll(UserInfo user,BigInteger profileId,JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets/categories?locale=zh_CN"; 
		Map<String,String> header=new HashMap<String,String>();
		return   apiBuildService.amzAdvGet(profile, url,header);
	}
	
	public String amzAdvRecommendationsProductCount(UserInfo user,BigInteger profileId,JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets/products/count"; 
		Map<String,String> header=new HashMap<String,String>();
		header.put("Content-Type", "application/vnd.spproducttargeting.v3+json");
    	header.put("Accept",       "application/vnd.spproducttargeting.v3+json");
		return   apiBuildService.amzAdvPost(profile, url,param.toString(),header);
	}
	
	public String amzAdvRecommendationsTargetsRefine(UserInfo user,BigInteger profileId,JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets/category/"+param.getString("categoryId")+"/refinements?locale=zh_CN"; 
		Map<String,String> header=new HashMap<String,String>();
    	header.put("Accept",       "application/vnd.spproducttargetingresponse.v3+json");
		return   apiBuildService.amzAdvGet(profile, url,header);
	}
	
	public String initialBudgetRecommendation(UserInfo user,BigInteger profileId,JSONObject param) {
 		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaigns/initialBudgetRecommendation"; 
		Map<String,String> header=new HashMap<String,String>();
    	header.put("Content-Type",  "application/vnd.spinitialbudgetrecommendation.v3.4+json");
    	header.put("Accept",        "application/vnd.spinitialbudgetrecommendation.v3.4+json");
    	String response = apiBuildService.amzAdvPost(profile, url,param.toString(),header);
		return  response; 
	}
	
	public String budgetRulesRecommendations(UserInfo user,BigInteger profileId,String campaignId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaigns/budgetRules/recommendations?campaignId="+campaignId; 
		Map<String,String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/vnd.spbudgetrulesrecommendation.v3+json");
    	header.put("Accept",       "application/vnd.spbudgetrulesrecommendation.v3+json");
		return   apiBuildService.amzAdvGet(profile, url,header);
	}
	
	public String budgetRecommendations(UserInfo user,BigInteger profileId,JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaigns/budgetRecommendations"; 
		Map<String,String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/vnd.budgetrecommendation.v3+json");
    	header.put("Accept",       "application/vnd.budgetrecommendation.v3+json");
		return   apiBuildService.amzAdvPost(profile, url,param.toString(),header);
	}
	
	public String campaignRecommendations(UserInfo user,BigInteger profileId,JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaign/recommendations";
		if(param.getString("maxResults")!=null) {
			url=url+"?maxResults="+param.getString("maxResults");
		}
		if(param.getString("nextToken")!=null) {
			if(url.contains("?")) {
				url=url+"&nextToken="+param.getString("nextToken");
			}else {
				url=url+"?nextToken="+param.getString("nextToken");
			}
		}
		Map<String,String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/vnd.spgetcampaignrecommendationsresponse.v1+json");
    	header.put("Accept",       "application/vnd.spgetcampaignrecommendationsresponse.v1+json");
		return   apiBuildService.amzAdvGet(profile, url,header);
	}
	
	public String budgetRulesRecommendation(AmzAdvProfile profile ,JSONObject param) {
		String url = "/sp/campaigns/budgetRules/recommendations";
		Map<String,String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/vnd.spbudgetrulesrecommendation.v3+json");
    	header.put("Accept",       "application/vnd.spbudgetrulesrecommendation.v3+json");
		return   apiBuildService.amzAdvPost(profile, url,param.toString(),header);
	}
	
}
