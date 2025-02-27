package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBidReCommendService;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;

import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;

import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvBidReCommendService")
public class AmzAdvBidReCommendServiceImpl implements IAmzAdvBidReCommendService{

	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	ApiBuildService apiBuildService;
 
	
	public Map<String, Object> amzGetKeywordBidRecommendations(UserInfo user,BigInteger profileId, String keywordId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/keywords/"+ keywordId +"/bidRecommendations";
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			map.put("keywordId", item.getBigInteger("keywordId"));
			map.put("adGroupId", item.getBigInteger("adGroupId"));
			map.put("suggestedBid", item.getJSONObject("suggestedBid"));
			return map;
		}
		return null;
	}
	
	public List<Map<String, Object>> amzGetTargetBidRecommendations(UserInfo user,BigInteger profileId,String adGroupId,JSONArray jsona) {
		if(jsona == null || jsona.size() < 1) return null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets/bidRecommendations";
		JSONObject json = new JSONObject();
		if(StrUtil.isEmpty(adGroupId)) {
			throw new BaseException("请保存广告活动。");
		}
	 	json.put("adGroupId",Long.parseLong(adGroupId));
		json.put("expressions",jsona);
		String response = apiBuildService.amzAdvPost_V2(profile, url, json.toString());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject responseObject = GeneralUtil.getJsonObject(response.toString());
			JSONArray recommendations = responseObject.getJSONArray("recommendations");
			for(int i = 0; i< jsona.size(); i++) {
				JSONObject items =  recommendations.getJSONObject(i);
				if ("SUCCESS".equals(items.getString("code"))) {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("adGroupId", adGroupId);
					map.put("suggestedBid", items.getJSONObject("suggestedBid"));
					list.add(map);
				}else {
					throw new BizException(items.getString("description"));
				}
			}
			return list;
		}
		return null;
	}

	public List<Map<String,Object>> amzCreateKeywordBidRecommendations(UserInfo user,BigInteger profileId,String adGroupId,List<Map<String, Object>> list) {
		if(list == null || StringUtil.isEmpty(adGroupId)) return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/keywords/bidRecommendations";
		JSONArray item = new JSONArray();
		JSONObject param = new JSONObject();
		for(int i = 0; i < list.size(); i++) {
			JSONObject param2 = new JSONObject();
			param2.put("keyword", list.get(i).get("keyword"));
			param2.put("matchType", list.get(i).get("matchType").toString().toLowerCase());
			item.add(param2);
		}
		param.put("adGroupId", Long.parseLong(adGroupId));
		param.put("keywords", item);
		String response = apiBuildService.amzAdvPost_V2(profile, url, param.toString());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject responseObject = GeneralUtil.getJsonObject(response.toString());
			JSONArray recommendations = responseObject.getJSONArray("recommendations");
			Map<String,Object> map = new HashMap<String, Object>();
			for(int i = 0; i< list.size(); i++) {
				JSONObject items =  recommendations.getJSONObject(i);
				if ("SUCCESS".equals(items.getString("code"))) {
					map = list.get(i);
					map.put("adGroupId", adGroupId);
					map.put("keywordText", items.getString("keywordText"));
					map.put("matchType", items.getString("matchType"));
					map.put("suggestedBid", items.getJSONObject("suggestedBid"));
				}
			}
			return list;
		}
		return null;
	}
	
	public String bidRecommendations(BigInteger profileid,String adGroupId,String keywords,String matchType) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileid);
	    if(StringUtil.isEmpty(keywords))return null;
		String url = "/keywords/bidRecommendations";
	    String[] keywordsAry = keywords.split("#");
	 	JSONObject json = new JSONObject(); 
	 	json.put("adGroupId",Long.parseLong(adGroupId));
	 	JSONArray jsona = new JSONArray(); 
	 	for(String key:keywordsAry) {
	 		JSONObject mjson = new JSONObject();
	 		mjson.put("keyword", key);
	 		mjson.put("matchType", matchType);
	 		jsona.add(mjson);
	     }
		json.put("keywords",jsona);
		String  response = apiBuildService.amzAdvPost_V2(profile, url, json.toString());
		return response;
	}
	
	public List<Map<String, Object>> amzRecommendationsBidsForKeyword_HSA(UserInfo user, BigInteger profileId, BigInteger campaignId, 
			List<Map<String, Object>> paramList) {
		if (paramList == null) {
			throw new BaseException("请输入正确的参数！");
		}
		JSONObject param = new JSONObject();
		JSONArray array = new JSONArray();
		for(Map<String, Object> map : paramList) {
			JSONObject paramObject = new JSONObject();
			paramObject.put("matchType", map.get("matchType").toString().toLowerCase());
			paramObject.put("keywordText", map.get("keyword"));
			array.add(paramObject);
		}
		if(campaignId != null) {
			param.put("campaignId", campaignId);
		}
		param.put("keywords", array); 
		String url = "/sb/recommendations/bids";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = apiBuildService.amzAdvPost(profile, url, param.toJSONString());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject jsonObject = GeneralUtil.getJsonObject(response.toString());
			JSONArray jsonSuccessArray = jsonObject.getJSONArray("keywordsBidsRecommendationSuccessResults");
			JSONArray jsonErrorArray = jsonObject.getJSONArray("keywordsBidsRecommendationErrorResults");
			if(jsonErrorArray != null && jsonErrorArray.size() > 0) {
				int index = jsonErrorArray.getJSONObject(0).getInteger("keywordIndex");
				paramList.get(index).put("recommendedBid", null);
			} 
			for(int i = 0; i < jsonSuccessArray.size(); i++) {
				JSONObject successobject = jsonSuccessArray.getJSONObject(i);
				JSONObject bidObject = successobject.getJSONObject("recommendedBid");
				int index = successobject.getInteger("keywordIndex");
				paramList.get(index).put("recommendedBid", bidObject);
			}
			return paramList;
		}
		return null;
	}
	
	public JSONArray amzRecommendationsBidsForTarget_HSA(UserInfo user, BigInteger profileId, BigInteger campaignId, JSONArray jsona) {
		if (jsona == null) {
			throw new BaseException("请输入正确的参数！");
		}
		JSONObject param = new JSONObject();
		if(campaignId != null) {
			param.put("campaignId", campaignId);
		}
		param.put("targets", jsona); 
		String url = "/sb/recommendations/bids";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = apiBuildService.amzAdvPost(profile, url, param.toJSONString());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject jsonObject = GeneralUtil.getJsonObject(response.toString());
			JSONArray jsonSuccessArray = jsonObject.getJSONArray("targetsBidsRecommendationSuccessResults");
			JSONArray jsonErrorArray = jsonObject.getJSONArray("targetsBidsRecommendationErrorResults");
			if(jsonErrorArray != null && jsonErrorArray.size() > 0) {
				int index = jsonErrorArray.getJSONObject(0).getInteger("targetsIndex");
				jsona.getJSONObject(index).put("recommendedBid", null);
			} 
			for(int i = 0; i < jsonSuccessArray.size(); i++) {
				JSONObject successobject = jsonSuccessArray.getJSONObject(i);
				JSONObject bidObject = successobject.getJSONObject("recommendedBid");
				int index = successobject.getInteger("targetsIndex");
				jsona.getJSONArray(index).getJSONObject(index).put("recommendedBid", bidObject);
			}
			return jsona;
		}
		return null;
	}

}
