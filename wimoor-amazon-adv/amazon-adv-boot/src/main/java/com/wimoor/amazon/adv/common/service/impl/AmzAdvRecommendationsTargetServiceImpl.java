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
import com.wimoor.amazon.adv.common.service.IAmzAdvRecommendationsTargetService;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import tk.mybatis.mapper.util.StringUtil;


@Service("amzAdvRecommendationsTargetService")
public class AmzAdvRecommendationsTargetServiceImpl implements IAmzAdvRecommendationsTargetService{

	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	ApiBuildService apiBuildService;
	public Map<String, Object> amzCreateTargetRecommendations(UserInfo user,BigInteger  profileId, List<String> asinsList,int pageSize, int pageNumber) {
		if(asinsList == null) return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sp + "/targets/productRecommendations";
		JSONArray item = new JSONArray();
		JSONObject param = new JSONObject();
		for(int i = 0; i < asinsList.size(); i++) {
			item.add(asinsList.get(i));
		}
		param.put("pageSize", pageSize);
		param.put("pageNumber", pageNumber);
		param.put("asins", item);
		String response = apiBuildService.amzAdvPost_V2(profile, url, param.toString());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject responseObject = GeneralUtil.getJsonObject(response.toString());
			JSONArray recommendedProducts = responseObject.getJSONArray("recommendedProducts");
			Map<String,Object> map = new HashMap<String, Object>();
			List<String> list = new ArrayList<String>();
			map.put("totalResultCount", responseObject.getString("totalResultCount"));
			for(int i = 0; i< recommendedProducts.size(); i++) {
				JSONObject items =  recommendedProducts.getJSONObject(i);
				list.add(items.getString("recommendedTargetAsin"));
			}
			map.put("recommendedTargetAsin", list);
			return map;
		}
		return null;
	}

	public List<Map<String, Object>> amzGetTargetingCategories(UserInfo user, BigInteger profileId, String asins) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sp + "/targets/categories?asins=" + asins;
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONArray array = GeneralUtil.getJsonArray(response.toString());
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject item = array.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", item.getBigInteger("id"));
				map.put("name", item.getString("name"));
				map.put("isTargetable", item.getString("isTargetable"));
				map.put("path", item.getString("path"));
				list.add(map);
			}
			return list;
		}
		return null;
	}

	public List<Map<String, Object>> amzGetBrandRecommendations(UserInfo user,BigInteger profileId,String categoryId,String keyword) {
		String urltype = "";
		if(keyword == null) {
			urltype = "categoryId=" + categoryId;
		}else {
			urltype = "keyword=" + keyword;
		}
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sp + "/targets/brands?" + urltype;
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONArray array = GeneralUtil.getJsonArray(response.toString());
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject item = array.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", item.getBigInteger("id"));
				map.put("name", item.getString("name"));
				list.add(map);
			}
			return list;
		}
		return null;
	}
	
	public List<Map<String, Object>> amzGetRefinementsForCategory(UserInfo user,BigInteger profileId,String categoryId) {
		String urltype = "categoryId=" + categoryId;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sp + "/targets/categories/refinements?" + urltype;
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject object = GeneralUtil.getJsonObject(response.toString());
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("categoryId", object.getBigInteger("categoryId"));
			map.put("brands", object.getString("brands"));
			map.put("ageRanges", object.getString("ageRanges"));
			list.add(map);
			return list;
		}
		return null;
	}
	
	public List<Map<String, Object>> amzTargetRecommendationsProductList_HSA(UserInfo user, BigInteger profileId, List<String> paramList, int maxResults, String nextToken) {
		if(paramList == null || paramList.size() == 0) {
			throw new BaseException("asin数据为空！");
		}
		JSONObject param = new JSONObject();
		JSONArray filtersArray = new JSONArray();
		for(String asin : paramList) {
			JSONObject paramObjec = new JSONObject();
			JSONArray asinsArray = new JSONArray();
			paramObjec.put("filterType", "ASINS");
			String[] asins = asin.split(",");
			for(int i = 0; i < asins.length; i++) {
				asinsArray.add(asins[i]);
			}
			paramObjec.put("values", asinsArray);
			filtersArray.add(paramObjec);
		}
		if(StringUtil.isNotEmpty(nextToken)) {
			param.put("nextToken", nextToken);
		}
		param.put("maxResults", maxResults);
		param.put("filters", filtersArray);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/recommendations/targets/product/list";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = apiBuildService.amzAdvPost(profile, url, param.toString());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject jsonObect = GeneralUtil.getJsonObject(response.toString());
			JSONArray jsonArray = jsonObect.getJSONArray("recommendedProducts");
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonItem = jsonArray.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("recommendedTargetAsin", jsonItem.getString("recommendedTargetAsin"));
				list.add(map);
			}
			String nextTokenback = jsonObect.getString("nextToken");
			if(nextTokenback != null) {
				list.get(0).put("nextToken", nextTokenback);
			}
			return list;
		}
		return null;
	}

	public List<Map<String, Object>> amzRecommendationsCategories_HSA(UserInfo user, BigInteger profileId, List<String> asinsList) {
		if(asinsList == null || asinsList.size() == 0) {
			throw new BaseException("asin数据为空！");
		}
		JSONObject param = new JSONObject();
		JSONArray array = new JSONArray();
		for(String asin : asinsList) {
			array.add(asin);
		}
		param.put("asins", array);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/recommendations/targets/category";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = apiBuildService.amzAdvPost(profile, url, param.toString());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject jsonObect = GeneralUtil.getJsonObject(response.toString());
			JSONArray jsonArray = jsonObect.getJSONArray("categoryRecommendationResults");
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonItem = jsonArray.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", jsonItem.getBigInteger("id"));
				map.put("name", jsonItem.getString("name"));
				map.put("isTargetable", jsonItem.getString("isTargetable"));
				map.put("path", jsonItem.getString("path"));
				list.add(map);
			}
			return list;
		}
		return null;
	}

	public List<Map<String, Object>> amzRecommendationsBrand_HSA(UserInfo user, BigInteger profileId, String categoryId, String keyword) {
		if(StringUtil.isEmpty(categoryId) && StringUtil.isEmpty(keyword)) {
			throw new BaseException("种类或者关键词为空！");
		}
		JSONObject param = new JSONObject();
		if(StringUtil.isNotEmpty(categoryId)) {
			param.put("categoryId", categoryId);
		}else {
			param.put("keyword", keyword);
		}
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/recommendations/targets/brand";
		String response = apiBuildService.amzAdvPost(profile, url, param.toString());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject jsonObect = GeneralUtil.getJsonObject(response.toString());
			JSONArray jsonArray = jsonObect.getJSONArray("brandRecommendationResults");
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonItem = jsonArray.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", jsonItem.getBigInteger("id"));
				map.put("name", jsonItem.getString("name"));
				list.add(map);
			}
			return list;
		}
		return null;
	}

}
