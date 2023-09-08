
package com.wimoor.amazon.adv.controller;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AdvState;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvBidReCommendService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampKeywordsNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsSuggestService;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tk.mybatis.mapper.util.StringUtil;

@Api(tags = "广告产品接口")
@RestController 
@RequestMapping("/api/v1/advKeywordManager") 
public class AdvertKeywordManagerController {
	
	@Resource
	IAmzAdvKeywordsService amzAdvKeywordsService;
	@Resource
	IAmzAdvKeywordsNegativaService amzAdvKeywordsNegativaService;
	@Resource
	IAmzAdvKeywordsSuggestService amzAdvKeywordsSuggestService;
	@Resource
	IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
 
	@Resource
	IAmzAdvBidReCommendService amzAdvBidReCommendService;
	@Resource
	IAmzAdvCampKeywordsNegativaService amzAdvCampKeywordsNegativaService;
	
	@ResponseBody
	@RequestMapping("/getAmzKeywordRecommendations")
	public List<Map<String, Object>> getAmzKeywordRecommendations(HttpServletRequest request, Model model){
		BigInteger profileid = null;
		UserInfo user = UserInfoContext.get();
		if(StringUtil.isNotEmpty(request.getParameter("profileid"))) {
			profileid = new BigInteger(request.getParameter("profileid"));
		}
		String pageUrl = request.getParameter("storePageUrl");
		String asins = request.getParameter("asins");
		List<String> asinsList = null;
		if(StringUtil.isEmpty(asins)) {
			return null;
		} 
		asinsList = new ArrayList<String>();
		String[] asinsArray = asins.split(",");
		for(int i = 0; i < asinsArray.length; i++) {
			asinsList.add(asinsArray[i]);
		}
		return amzAdvKeywordsSuggestService.amzKeywordRecommendations_HSA(user, profileid, asinsList, pageUrl);
	}

	@ResponseBody
	@RequestMapping("/getSuggestedkeywordsByAdGroup")
	public List<Map<String, Object>> getSuggestedkeywordsByAdGroup(HttpServletRequest request, Model model){
		BigInteger profileid = null;
		UserInfo user = UserInfoContext.get();
		if(StringUtil.isNotEmpty(request.getParameter("profileid"))) {
			profileid = new BigInteger(request.getParameter("profileid"));
		}
		String adGroupId = AdvUtils.getRequestValue(request,"adGroupId");
		String campaignid = AdvUtils.getRequestValue(request,"campaignid");
		String campaignType = AdvUtils.getRequestValue(request,"campaignType");
		if("hsa".equals(campaignType) && StringUtil.isEmpty(adGroupId)) {
			AmzAdvCampaignsHsa campaignHsa = amzAdvCampaignHsaService.amzGetSBCampaigns(user, profileid, campaignid);
			return amzAdvKeywordsSuggestService.amzKeywordRecommendations_HSA(user, profileid, campaignHsa.getAsins(), campaignHsa.getUrl());
		}
		if(profileid != null && adGroupId != null) {
			Map<String, Object> keywordsParam = new HashMap<String, Object>();
			keywordsParam.put("adGroupId", adGroupId);
			keywordsParam.put("maxNumSuggestions", 100);
			keywordsParam.put("suggestBids", "yes");
			keywordsParam.put("adStateFilter", AdvState.enabled);
			List<Map<String, Object>> listmap = amzAdvKeywordsSuggestService.amzAdGroupSuggestedKeywordsEx(user,profileid,keywordsParam);
			return listmap;
		}else {
			return null;
		}
	}
	
	@ResponseBody
	@RequestMapping("/getSuggestedkeywordsByAsin")
	public List<Map<String,Object>> getSuggestedkeywordsByAsin(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		BigInteger profileid = null;
		if(request.getParameter("profileid") != null) {
			profileid = new BigInteger(request.getParameter("profileid"));
		}
		String asin = request.getParameter("asin");
		if(StringUtil.isEmpty(asin)) {
			return null;
		}
		String[] asinArray = null;
		asinArray = asin.split(",");
		List<String> asinList = new ArrayList<String>();
		for(int i = 0; i < asinArray.length; i++) {
			asinList.add(asinArray[i]);
		}
		List<Map<String,Object>> list = amzAdvKeywordsSuggestService.bulkGetAsinSuggestedKeywords(user,profileid,asinList,100);
		return list;
	}
	
	@ResponseBody
	@RequestMapping("/getCreateKeywordBidRecommend")
	public List<Map<String,Object>> getCreateKeywordBidRecommendations(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		BigInteger profileid = null;
		if(StringUtil.isNotEmpty(request.getParameter("profileid"))) {
			profileid = new BigInteger(request.getParameter("profileid"));
		}
		String adGroupid = request.getParameter("adGroupid");
		String campaignType = request.getParameter("campaignType");
		String campaignId = request.getParameter("campaignId");
		String keyword = request.getParameter("keyword");
		String matchType = request.getParameter("matchType");
		String rowid = request.getParameter("rowid");
		String[] keywordArray = null;
		String[] matchTypeArray = null;
		if(StringUtil.isEmpty(keyword)) {
			return null;
		}
		if(StringUtil.isEmpty(matchType)) {
			return null;
		}
		keywordArray = keyword.split(",");
		matchTypeArray = matchType.split(",");
		List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
		for(int i = 0; i < keywordArray.length; i++) {
			Map<String,Object> map = new HashMap<String, Object>();
			if(StringUtil.isNotEmpty(keywordArray[i].trim())){
				map.put("keyword", keywordArray[i].trim());
				map.put("matchType", matchTypeArray[i].trim());
				paramList.add(map);
			}
		}
		if("hsa".equals(campaignType) || "sb".equals(campaignType)) {
			BigInteger  camId = null;
			if(StringUtil.isNotEmpty(campaignId)) {
				camId = new BigInteger(campaignId);
			}
			paramList = amzAdvBidReCommendService.amzRecommendationsBidsForKeyword_HSA(user, profileid, camId, paramList);
		}else {
			if(StringUtil.isEmpty(campaignId)) {
				return paramList;
			}
			paramList = amzAdvBidReCommendService.amzCreateKeywordBidRecommendations(user, profileid, adGroupid, paramList);
		}
		if(paramList != null && paramList.size() > 0 && rowid != null) {
			paramList.get(0).put("rowid", rowid);
		}
		return paramList;
	}

	@GetMapping("/getKeywordSuggestBid")
	public Result<Map<String,Object>> getKeywordSuggestBidAction(String profileid,String id){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = new HashMap<String, Object>();
		if(profileid != null && id != null) {
			map.put("profileid",profileid);
			map.put("id",id);
			return Result.success(amzAdvKeywordsService.catchKeywordSuggestBid(user, map));
		}
		return Result.failed();
	}
	
	@ApiOperation("查询广告关键词")
	@PostMapping("/getKeywordList")
	public Result<PageList<Map<String,Object>>> getKeywordListAction(@ApiParam("查询关键词") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
		String state =query.getState();
		String matchType =query.getMatchType();
		String name =query.getName();
		List<String> nameList = new ArrayList<String>();
		if (StringUtil.isEmpty(name)) {
			name = null; 
		} else {
			String [] names = name.replaceAll("\n", "").split(",");
			for(int i = 0; i < names.length; i++) {
				if(StringUtil.isEmpty(names[i])){
					continue;
				}
				nameList.add(names[i].toString().trim());
			}
			if(nameList.size() == 1) {
				name = nameList.get(0) + "%";
			}else {
				name = null;
			}
		}
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		map.put("matchType", matchType);
		map.put("name", name);
		if(nameList.size() > 1) {
			map.put("nameList", nameList);
		}else {
			map.put("nameList", null);
		}
		PageList<Map<String,Object>> list = amzAdvKeywordsService.getKeywordsList(map,query.getPageBounds());
		if(list != null && list.size() > 0) {
			for(Map<String, Object> item : list) {
				if("paused".equals(item.get("campaignStatus")) || "paused".equals(item.get("adGroupStatus"))) {
					if("enabled".equals(item.get("status"))) {
						item.put("passiveState", "paused");
					}
				}
				if("archived".equals(item.get("campaignStatus")) || "archived".equals(item.get("adGroupStatus"))) {
					item.put("passiveState", "archived");
				}
			}
			AdvertController.convertMapRemoveNA(list);
			Boolean isZeroPage = (Boolean) map.get("isZeroPage");
			list.get(0).put("isZeroPage", isZeroPage);
			return Result.success(list);
		}else {
			return Result.success(new PageList<Map<String,Object>>());
		}
	}

	@ResponseBody
	@RequestMapping("/updateKeywordList")
	public String updateKeywordListAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String json = request.getParameter("jsonstr");
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray keywordArray = jsonobject.getJSONArray("keywordArray");
		List<AmzAdvKeywords> keywordList = new ArrayList<AmzAdvKeywords>();
		for (int i = 0; i < keywordArray.size(); i++) {
			JSONObject keyword = keywordArray.getJSONObject(i);
			String campaignType = keyword.getString("campaignType");
			String matchType = keyword.getString("matchType");
			String keywordText = keyword.getString("keywordText");
			String state = keyword.getString("status");
			BigInteger campaignid = keyword.getBigInteger("campaignId");
			BigInteger keywordid =null;
			if(keyword.containsKey("keywordId")) {
				keywordid = keyword.getBigInteger("keywordId");
			}else {
				keywordid = keyword.getBigInteger("id");
			}
			BigInteger adGroupid = keyword.getBigInteger("adGroupId");
			BigInteger profileId = keyword.getBigInteger("profileid");
			BigDecimal bid = keyword.getBigDecimal("bid");
			AmzAdvKeywords amzAdvKeywords = new AmzAdvKeywords();
			amzAdvKeywords.setKeywordid(keywordid);
			amzAdvKeywords.setCampaignid(campaignid);
			amzAdvKeywords.setAdgroupid(adGroupid);
			amzAdvKeywords.setProfileid(profileId);
			amzAdvKeywords.setCampaigntype(campaignType);
			amzAdvKeywords.setMatchtype(matchType);
			amzAdvKeywords.setKeywordtext(keywordText);
			amzAdvKeywords.setBid(bid);
			amzAdvKeywords.setState(state);
			amzAdvKeywords.setOpttime(new Date());
			keywordList.add(amzAdvKeywords);
		}
		if (keywordList.size() != 0) {
			Map<BigInteger, List<AmzAdvKeywords>> map = new HashMap<BigInteger, List<AmzAdvKeywords>>();
			for (int i = 0; i < keywordList.size(); i++) {
				AmzAdvKeywords amzAdvKeywords = keywordList.get(i);
				BigInteger profileId = amzAdvKeywords.getProfileid();
				if (map.get(profileId) == null) {
					List<AmzAdvKeywords> list = new ArrayList<AmzAdvKeywords>();
					list.add(amzAdvKeywords);
					map.put(profileId, list);
				} else {
					map.get(profileId).add(amzAdvKeywords);
				}
			}
			for (BigInteger key : map.keySet()) {
				if(amzAdvKeywordsService.amzUpdateBiddableKeywords(user, key, map.get(key)) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
			}
		}
		return "SUCCESS";
	}
	
	@ResponseBody
	@RequestMapping("/archiveKeyword")
	public String archiveKeywordAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String profileId = AdvUtils.getRequestValue(request,"profileid");
		String keywordId = AdvUtils.getRequestValue(request,"id");
		AmzAdvKeywords amzAdvKeywords = null;
		if(profileId != null && keywordId != null) {
			amzAdvKeywords = amzAdvKeywordsService.amzArchiveBiddableKeyword(user, new BigInteger(profileId), keywordId);
		}
		if(amzAdvKeywords == null) {
			return "ERROR";
		}
		return "SUCCESS";
	}
	
	@ApiOperation("查询广告关键词图表")
	@PostMapping("/getKeywordChart")
	public Result<Map<String,Object>> getKeywordChartAction(@ApiParam("查询关键词") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
		String state = query.getState();
		String matchType = query.getMatchType();
		String name = query.getName();
		String bytime = query.getBytime();
		List<String> nameList = new ArrayList<String>();
		if (StringUtil.isEmpty(name)) {
			name = null; 
		} else {
			String [] names = name.replaceAll("\n", "").split(",");
			for(int i = 0; i < names.length; i++) {
				if(StringUtil.isEmpty(names[i])){
					continue;
				}
				  nameList.add(names[i].toString().trim());
			}
			if(nameList.size() == 1) {
				name = nameList.get(0) + "%";
			}else {
				name = null;
			}
		}
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		map.put("matchType", matchType);
		map.put("name", name);
		if(nameList.size() > 1) {
			map.put("nameList", nameList);
		}else {
			map.put("nameList", null);
		}
		map.put("bytime", bytime);
		return Result.success( amzAdvKeywordsService.getKeywordChart(map));
	}
	
	@ApiOperation("查询广告否定关键词")
	@PostMapping("/getNegativaKeywordsList")
	public Result<PageList<Map<String,Object>>> getNegativaKeywordsListAction(@ApiParam("查询关键词") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
		String state = query.getState();
		String name = query.getName();
		if (StringUtil.isEmpty(name)) {
			name = null; 
		} else {
			name = name + "%";
		}
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		map.put("name", name);
		List<Map<String,Object>> list = amzAdvKeywordsNegativaService.getNegativaKeywordsList(map);
		List<Map<String,Object>> list2 = amzAdvCampKeywordsNegativaService.getCampaignNegativaKeywordsList(map);
		if(list == null || list.size() == 0){
			list = list2;
		}
		if(list != null && list2 != null){
			list.addAll(list2);
		}
		if(list != null) {
			PageList<Map<String, Object>> pageList = ERPBaseController.getPage(list, query.getPageBounds());
			if(pageList != null) {
				for(Map<String, Object> item:pageList) {
					if("paused".equals(item.get("campaignStatus")) || "paused".equals(item.get("adGroupStatus"))) {
						if("enabled".equals(item.get("status"))) {
							item.put("passiveState", "paused");
						}
					}
					if("archived".equals(item.get("campaignStatus")) || "archived".equals(item.get("adGroupStatus"))) {
						item.put("passiveState", "archived");
					}
				}
			}
			return Result.success(pageList);
		} else {
			return Result.success(new PageList<Map<String,Object>>());
		}
	}
	
	@ResponseBody
	@RequestMapping("/updateNegativaKeywordList")
	public String updateNegativaKeywordListAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String json = request.getParameter("jsonstr");
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray keywordArray = jsonobject.getJSONArray("keywordArray");
		Map<BigInteger, List<AmzAdvKeywordsNegativa>> map = new HashMap<BigInteger, List<AmzAdvKeywordsNegativa>>();
		Map<BigInteger, List<AmzAdvCampKeywordsNegativa>> mapCamp = new HashMap<BigInteger, List<AmzAdvCampKeywordsNegativa>>();
		for (int i = 0; i < keywordArray.size(); i++) {
			JSONObject keyword = keywordArray.getJSONObject(i);
			String matchType = keyword.getString("matchType");
			String keywordText = keyword.getString("keywordText");
			String state = keyword.getString("status");
			BigInteger campaignid = keyword.getBigInteger("campaignId");
			BigInteger keywordid = keyword.getBigInteger("id");
			BigInteger adGroupid = keyword.getBigInteger("adGroupId");
			BigInteger profileId = keyword.getBigInteger("profileid");
			if(adGroupid != null) {
				AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativa();
				amzAdvKeywordsNegativa.setKeywordid(keywordid);
				amzAdvKeywordsNegativa.setCampaignid(campaignid);
				amzAdvKeywordsNegativa.setAdgroupid(adGroupid);
				amzAdvKeywordsNegativa.setProfileid(profileId);
				amzAdvKeywordsNegativa.setMatchtype(matchType);
				amzAdvKeywordsNegativa.setKeywordtext(keywordText);
				amzAdvKeywordsNegativa.setState(state);
				amzAdvKeywordsNegativa.setOpttime(new Date());
				if (map.get(profileId) == null) {
					List<AmzAdvKeywordsNegativa> list = new ArrayList<AmzAdvKeywordsNegativa>();
					list.add(amzAdvKeywordsNegativa);
					map.put(profileId, list);
				} else {
					map.get(profileId).add(amzAdvKeywordsNegativa);
				}
			}else {
				AmzAdvCampKeywordsNegativa amzAdvCampKeywordsNegativa = new AmzAdvCampKeywordsNegativa();
				amzAdvCampKeywordsNegativa.setKeywordid(keywordid);
				amzAdvCampKeywordsNegativa.setCampaignid(campaignid);
				amzAdvCampKeywordsNegativa.setProfileid(profileId);
				amzAdvCampKeywordsNegativa.setMatchtype(matchType);
				amzAdvCampKeywordsNegativa.setKeywordtext(keywordText);
				amzAdvCampKeywordsNegativa.setState(state);
				amzAdvCampKeywordsNegativa.setOpttime(new Date());
				if (mapCamp.get(profileId) == null) {
					List<AmzAdvCampKeywordsNegativa> list = new ArrayList<AmzAdvCampKeywordsNegativa>();
					list.add(amzAdvCampKeywordsNegativa);
					mapCamp.put(profileId, list);
				} else {
					mapCamp.get(profileId).add(amzAdvCampKeywordsNegativa);
				}
			}
		}
		
		for (BigInteger key : map.keySet()) {
			if(amzAdvKeywordsNegativaService.amzUpdateNegativaKeywords(user, key, map.get(key)) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}
		for (BigInteger key : mapCamp.keySet()) {
			if(amzAdvCampKeywordsNegativaService.amzUpdateCampaignNegativeKeywords(user, key, mapCamp.get(key)) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}
		return "SUCCESS";
	}
	
	@ResponseBody
	@RequestMapping("/archiveKeywordNegativa")
	public String archiveKeywordNegativaAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String profileId = request.getParameter("profileid");
		String keywordId = request.getParameter("id");
		String adGroupId = request.getParameter("adGroupId");
		AmzAdvKeywordsNegativa  amzAdvKeywordsNegativa  = null;
		AmzAdvCampKeywordsNegativa amzAdvCampKeywordsNegativa = null;
		if(adGroupId == null) {
			amzAdvCampKeywordsNegativa = amzAdvCampKeywordsNegativaService.amzRemoveCampaignNegativeKeyword(user, new BigInteger(profileId), keywordId);
			if(amzAdvCampKeywordsNegativa  == null) {
				return "ERROR";
			}
		}else {
			amzAdvKeywordsNegativa = amzAdvKeywordsNegativaService.amzArchiveNegativaKeyword(user, new BigInteger(profileId), keywordId);
			if(amzAdvKeywordsNegativa  == null) {
				return "ERROR";
			}
		}
		return "SUCCESS";
	}
	
	@ApiOperation("查询广告关键词query")
	@PostMapping("/getKeywordQueryList")
	public Result<PageList<Map<String,Object>>> getKeywordQueryListAction(@ApiParam("查询关键词") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
		String keywordid =query.getKeywordid();
		String state =query.getState();
		String matchType = query.getMatchType();
		String name = query.getName();
		List<String> nameList = new ArrayList<String>();
		if (StringUtil.isEmpty(name)) {
			name = null; 
		} else {
			String [] names = name.replaceAll("\n", "").split(",");
			for(int i = 0; i < names.length; i++) {
				if(StringUtil.isEmpty(names[i])){
					continue;
				}
			    nameList.add(names[i].toString().trim());
			}
			if(nameList.size() == 1) {
				name =  nameList.get(0) + "%";
			}else {
				name = null;
			}
		}
		map.put("keywordid", keywordid);
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		map.put("matchType", matchType);
		map.put("name", name);
		if(nameList.size() > 1) {
			map.put("nameList", nameList);
		}else {
			map.put("nameList", null);
		}
		PageList<Map<String,Object>> list = amzAdvKeywordsService.getKeywordQueryList(map, query.getPageBounds());
		if(list != null) {
			for(Map<String, Object> item : list) {
				if("paused".equals(item.get("campaignStatus")) || "paused".equals(item.get("adGroupStatus"))) {
					if("enabled".equals(item.get("status"))) {
						item.put("passiveState", "paused");
					}
				}
				if("archived".equals(item.get("campaignStatus")) || "archived".equals(item.get("adGroupStatus"))) {
					item.put("passiveState", "archived");
				}
			}
			AdvertController.convertMapRemoveNA(list);
			return Result.success( list);
		}else {
			return Result.success(new PageList<Map<String,Object>>());
		}
	}
	
	@PostMapping("/getKeywordQueryChart")
	public Result<Map<String,Object>> getKeywordQueryChartAction(@ApiParam("查询关键词") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		String state = query.getState();
		String matchType = query.getMatchType();
		String name =query.getName();
		String bytime = query.getBytime();
		List<String> nameList = new ArrayList<String>();
		if (StringUtil.isEmpty(name)) {
			name = null; 
		} else {
			String [] names = name.replaceAll("\n", "").split(",");
			for(int i = 0; i < names.length; i++) {
				if(StringUtil.isEmpty(names[i])){
					continue;
				}
				  nameList.add(names[i].toString().trim());
			}
			if(nameList.size() == 1) {
				name = nameList.get(0) + "%";
			}else {
				name = null;
			}
		}
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		map.put("matchType", matchType);
		map.put("name", name);
		if(nameList.size() > 1) {
			map.put("nameList", nameList);
		}else {
			map.put("nameList", null);
		}
		map.put("bytime", bytime);
		return Result.success(amzAdvKeywordsService.getKeywordQueryChart(map));
	}
	
	@ResponseBody
	@RequestMapping("/getKeywordExtList")
	public Object getKeywordExtListAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String ids = request.getParameter("ids");
		if(StringUtil.isEmpty(ids))return null;
		JSONObject idsjson = GeneralUtil.getJsonObject(ids);
		List<AmzAdvKeywords> result =new LinkedList<AmzAdvKeywords>();
		for(Entry<String, Object> item:idsjson.entrySet()) {
			String profileid = item.getKey();
			ids=(String) item.getValue();
			if(StringUtil.isEmpty(ids)||StringUtil.isEmpty(profileid))return null;
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("keywordIdFilter",ids);
			List<AmzAdvKeywords> list = amzAdvKeywordsService.amzListBiddableKeywordsEx(user,new BigInteger(profileid),param);
			if(list != null && list.size()>0) {
				result.addAll(list);
			}
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/getKeywordforQuery")
	public Object getKeywordforQueryAction(HttpServletRequest request, Model model){
		//if(IniConfig.isDemo()) return null;
		String rowid = request.getParameter("rowid");
		String adGroupid = request.getParameter("adGroupid");
		String campaignid = request.getParameter("campaignid");
		String profileid = request.getParameter("profileid");
		String keywordtext = request.getParameter("keyword");
		String matchtype = request.getParameter("matchType");
		if(StringUtil.isEmpty(keywordtext))return null;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rowid", rowid);
		map.put("adGroupid", adGroupid);
		map.put("campaignid", campaignid);
		map.put("profileid", profileid);
		map.put("query", keywordtext);
		map.put("matchtype", matchtype);
		amzAdvKeywordsService.getKeywordforQuery(map);
		amzAdvKeywordsNegativaService.getNegativaKeywordforQuery(map);
		return map;
	}
	
	@ApiOperation("查询广告关键词汇总")
	@PostMapping("/getSumAdvKeywords")
	public Result<Object> getSumAdvKeywordsAction(@ApiParam("查询关键词") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query); 
		String state = query.getState();
		String matchType = query.getMatchType();
		String name =query.getName();
		List<String> nameList = new ArrayList<String>();
		if (StringUtil.isEmpty(name)) {
			name = null; 
		} else {
			String [] names = name.replaceAll("\n", "").split(",");
			for(int i = 0; i < names.length; i++) {
				if(StringUtil.isEmpty(names[i])){
					continue;
				}
				nameList.add(names[i].toString().trim());
			}
			if(nameList.size() == 1) {
				name =  nameList.get(0) + "%";
			}else {
				name = null;
			}
		}
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		map.put("matchType", matchType);
		map.put("name", name);
		if(nameList.size() > 1) {
			map.put("nameList", nameList);
		}else {
			map.put("nameList", null);
		}
		List<Map<String, Object>> param = amzAdvKeywordsService.getSumAdvKeywords(map);
		return Result.success(AdvertController.SumAdvertDate(param));
	}
	
}
