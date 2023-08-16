package com.wimoor.amazon.adv.sp.service.impl;

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
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsSuggestService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.util.StringUtil;


@Service("amzAdvKeywordsSuggestService")
public class AmzAdvKeywordsSuggestServiceImpl implements IAmzAdvKeywordsSuggestService{
	@Resource
	IAmzAdvAuthService amzAdvAuthService;

	public Map<String, Object> amzAdGroupSuggestedKeywords(UserInfo user,BigInteger profileId,Map<String, Object> keywordsParam) {
		// TODO Auto-generated method stub
		if(keywordsParam == null) throw new BaseException("请输入广告组信息！");
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/adGroups/"+ keywordsParam.get("adGroupId") +"/suggested/keywords/?";
		String param = "";
		param = GeneralUtil.addParamToUrl2(param, keywordsParam, "maxNumSuggestions");
		param = GeneralUtil.addParamToUrl2(param, keywordsParam, "adStateFilter");
		url = url + ("".equals(param) ? "" : param);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			map.put("adGroupId", item.getBigInteger("adGroupId"));
			map.put("suggestedKeywords", item.getJSONArray("suggestedKeywords"));
			return map;
		}
		return null;
	}

	public List<Map<String, Object>> amzAdGroupSuggestedKeywordsEx(UserInfo user,BigInteger  profileId, Map<String, Object> keywordsParam) {
		// TODO Auto-generated method stub
		if(keywordsParam == null) throw new BaseException("请输入广告组信息！");
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/adGroups/"+ keywordsParam.get("adGroupId") +"/suggested/keywords/extended?";
		String param = "";
		param = GeneralUtil.addParamToUrl(param, keywordsParam, "maxNumSuggestions");
		param = GeneralUtil.addParamToUrl(param, keywordsParam, "suggestBids");
		param = GeneralUtil.addParamToUrl(param, keywordsParam, "adStateFilter");
		url = url + ("".equals(param) ? "" : param);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<Map<String, Object>> listmap = new ArrayList<Map<String,Object>>();
		if (StringUtil.isNotEmpty(response)) {
			if("[]".equals(response)) {
				return null;
			}
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for(int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("adGroupId", item.getBigInteger("adGroupId"));
				map.put("campaignId", item.getBigInteger("campaignId"));
				map.put("keywordText", item.getString("keywordText"));
				map.put("matchType", item.getString("matchType"));
				map.put("state", item.getString("state"));
				if(keywordsParam.get("suggestBids") != null) {
					map.put("bid", item.getBigDecimal("bid"));
				}
				listmap.add(map);
			}
			return listmap;
		}
		return null;
	}

	public Map<String, Object> amzAsinSuggestedKeywords(UserInfo user,BigInteger  profileId, Map<String, Object> keywordsParam) {
		// TODO Auto-generated method stub
		if(keywordsParam == null) throw new BaseException("请输入asin信息！");
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/asins/"+ keywordsParam.get("asin") +"/suggested/keywords/?";
		String param = "";
		param = GeneralUtil.addParamToUrl2(param, keywordsParam, "maxNumSuggestions");
		url = url + ("".equals(param) ? "" : param);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			map.put("asin", item.getBigInteger("asin"));
			map.put("suggestedKeywords", item.getJSONArray("suggestedKeywords"));
			return map;
		}
		return null;
	}

	public List<Map<String,Object>> bulkGetAsinSuggestedKeywords(UserInfo user,BigInteger  profileId, List<String> asinsList, int maxNumSuggestions) {
		// TODO Auto-generated method stub
		if(asinsList == null) throw new BaseException("请输入asin信息！");
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/asins/suggested/keywords";
		JSONObject param = new JSONObject();
		JSONArray array = new JSONArray();
		for(int i = 0; i < asinsList.size(); i++) {
			array.add(asinsList.get(i));
		}
		param.put("asins", array);
		param.put("maxNumSuggestions", maxNumSuggestions);
		String response = amzAdvAuthService.amzAdvPost(profile, url, param.toJSONString());
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("keywordText",item.getString("keywordText"));
				map.put("matchType",item.getString("matchType"));
				list.add(map);
			}
			return list;
		}
		return null;
	}

	public List<Map<String, Object>> amzKeywordRecommendations_HSA(UserInfo user, BigInteger profileId, List<String> asinsList, String pageUrl) {
		// TODO Auto-generated method stub
		if (asinsList == null && StringUtil.isEmpty(pageUrl)) {
			throw new BaseException("请输入asin信息或者商店页面的URL！");
		}
		JSONObject param = new JSONObject();
		if(asinsList != null && asinsList.size() >= 3) {
			JSONArray array = new JSONArray();
			for (int i = 0; i < asinsList.size(); i++) {
				String asin = asinsList.get(i).toString();
				array.add(asin);
			}
			param.put("asins", array);
		} else if(StringUtil.isNotEmpty(pageUrl)) {
			param.put("url", pageUrl);
		} else {
			throw new BaseException("请确认输入的asin大于三个或者商店页面的URL正确！");
		}
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/recommendations/keyword";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = amzAdvAuthService.amzAdvPost_V3(profile, url, param.toString());
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("recommendationId",item.getString("recommendationId"));
				map.put("matchType",item.getString("matchType"));
				map.put("value",item.getString("value"));
				list.add(map);
			}
			return list;
		}
		return null;
	}
	
}
