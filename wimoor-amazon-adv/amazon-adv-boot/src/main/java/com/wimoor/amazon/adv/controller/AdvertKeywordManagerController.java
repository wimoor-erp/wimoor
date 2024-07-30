
package com.wimoor.amazon.adv.controller;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvBidReCommendService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvKeywordsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvKeywordsNegativaHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvKeywordsHsaService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvKeywordsNegativaHsaService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvRecommendationsHsaService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampKeywordsNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampKeywordsNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvRecommendationsService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tk.mybatis.mapper.util.StringUtil;

@Api(tags = "关键词接口")
@RestController 
@RequestMapping("/api/v1/advKeywordManager") 
public class AdvertKeywordManagerController {
	
	@Resource
	IAmzAdvKeywordsService amzAdvKeywordsService;
	@Resource
	IAmzAdvKeywordsHsaService amzAdvKeywordsHsaService;
	@Resource
	IAmzAdvKeywordsNegativaService amzAdvKeywordsNegativaService;
	@Resource
	IAmzAdvKeywordsNegativaHsaService amzAdvKeywordsNegativaHsaService;
	@Resource
	IAmzAdvRecommendationsService amzAdvRecommendationsTargetsKeywordsService;
	
	@Resource
	IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
 
	@Resource
	IAmzAdvBidReCommendService amzAdvBidReCommendService;
	@Resource
	IAmzAdvCampKeywordsNegativaService amzAdvCampKeywordsNegativaService;
	@Resource
	IAmzAdvRecommendationsService amzAdvRecommendationsService;
    @Resource
    IAmzAdvRecommendationsHsaService amzAdvRecommendationsHsaService;
    
   	@PostMapping("/createKeywordList/{profileid}/{campaignType}")
   	public Result<String> createKeywordListAction(@PathVariable String profileid,@PathVariable String campaignType, @RequestBody Map<String,Object> params ) {
   		UserInfo user = UserInfoContext.get();
		String json = params.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray keywords = jsonobject.getJSONArray("keywords");
		if(campaignType!=null&& "sb".equals(campaignType.toLowerCase())) {
			if(amzAdvKeywordsHsaService.amzCreateKeywords(user, profileid, keywords) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}else {
				if(amzAdvKeywordsService.amzCreateKeywords(user, profileid, keywords) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
		}
   		return Result.success();
   	}
   	
	
	@PostMapping("/updateKeywordList/{profileid}/{campaignType}")
	public Result<String> updateKeywordListAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody Map<String,Object> param) {
		UserInfo user = UserInfoContext.get();
		String json = param.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray keywords = jsonobject.getJSONArray("keywords");
		if(campaignType!=null&& "sb".equals(campaignType.toLowerCase())) {
				if(amzAdvKeywordsHsaService.amzUpdateKeywords(user, profileid, keywords) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
			
		}else {
				if(amzAdvKeywordsService.amzUpdateKeywords(user, profileid, keywords) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
			 
		}
		
		return Result.success();
	}
	
	@PostMapping("/deleteKeywordList/{profileid}/{campaignType}")
	public Result<String> deleteKeywordListAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
		if(campaignType!=null&& "sb".equals(campaignType.toLowerCase())) {
			JSONObject param = new JSONObject();
			param.put("keywordId", idlist.get(0));
			if(amzAdvKeywordsHsaService.amzDeleteKeywords(user,new BigInteger(profileid), param) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}else {
			JSONObject param = new JSONObject();
			JSONObject keywordIdFilter = new JSONObject();
			keywordIdFilter.put("include", idlist);
			param.put("negativeKeywordIdFilter", keywordIdFilter);
			param.put("includeExtendedDataFields", false);
			if(amzAdvKeywordsService.amzDeleteKeywords(user, new BigInteger(profileid), param) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
			 
		}
		return Result.success();
	}
	
 
	
	@PostMapping("/getSuggestedkeywords/{profileid}/{campaignType}")
	public Result<?> getSuggestedkeywordsByAdGroup(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody JSONObject param){
		UserInfo user = UserInfoContext.get();
		if(campaignType.toLowerCase().equals("sp")) {
			return Result.success(this.amzAdvRecommendationsService.amzAdvRecommendationsTargetsKeywords(user, new BigInteger(profileid), param)) ;
		}else {
			return Result.success(this.amzAdvRecommendationsHsaService.amzKeywordRecommendations(user, new BigInteger(profileid), param)) ;
		}
	}
	
	@PostMapping("/getAmzKeywordBid/{profileid}/{campaignType}")
	public Result<String> getAmzKeywordBid(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody JSONObject param){
		UserInfo user = UserInfoContext.get();
		return Result.success(this.amzAdvRecommendationsHsaService.amzAdvRecommendationsBid(user, new BigInteger(profileid), param)) ;
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
				name =  "%"+nameList.get(0) + "%";
			}else {
				name = null;
			}
		}
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		
		map.put("name", name);
		if(nameList.size() > 1) {
			map.put("nameList", nameList);
		}else {
			map.put("nameList", null);
		}
		PageList<Map<String,Object>> list =null;
		if(query.getCampaignType().toLowerCase().equals("sb")) {
			list=amzAdvKeywordsHsaService.getKeywordsList(map,query.getPageBounds());
		}else{
			list=amzAdvKeywordsService.getKeywordsList(map,query.getPageBounds());
		}
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

	 
 
	
	@ApiOperation("查询广告关键词图表")
	@PostMapping("/getKeywordChart")
	public Result<Map<String,Object>> getKeywordChartAction(@ApiParam("查询关键词") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
		String state = query.getState();
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
		List<Map<String,Object>> list = null;
		if(query.getCampaignType().toLowerCase().equals("sp")) {
			if(query.getAdGroupid()!=null) {
				  list = amzAdvKeywordsNegativaService.getNegativaKeywordsList(map);
			}else {
				 list = amzAdvCampKeywordsNegativaService.getCampaignNegativaKeywordsList(map);	
			}
		}else {
			list = amzAdvKeywordsNegativaHsaService.getNegativaKeywordsList(map);
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
	
 	@PostMapping("/keywordNegativa/{profileid}/{campaignType}")
	public Result<?> getkeywordNegativaAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
		if(campaignType.toLowerCase().equals("sp")) {
			JSONObject param = new JSONObject();
			JSONObject keywordIdFilter = new JSONObject();
			keywordIdFilter.put("include", idlist);
			param.put("negativeKeywordIdFilter", keywordIdFilter);
			param.put("includeExtendedDataFields", false);
		    return Result.success(amzAdvKeywordsNegativaService.amzListNegativaKeywords(user, new BigInteger(profileid), param));
		}else {
			List<AmzAdvKeywordsNegativaHsa> sb=new LinkedList<AmzAdvKeywordsNegativaHsa>();
		     for(String id:idlist) {
		    	 AmzAdvKeywordsNegativaHsa amzAdvKeywordsNegativaHsa=amzAdvKeywordsNegativaHsaService.amzGetNegativaKeywordEx(user, new BigInteger(profileid), id);
		    	 sb.add(amzAdvKeywordsNegativaHsa);
		     }
		     return Result.success(sb);
		   
		}
			
	}
 	
 
	
 	@PostMapping("/keywordNegativaExt/{profileid}/{campaignType}")
	public Result<?> getkeywordNegativaExtAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		    UserInfo user = UserInfoContext.get();
		    if(campaignType.toLowerCase().equals("sp")) {
		    	JSONObject param = new JSONObject();
				JSONObject adGroupIdFilter = new JSONObject();
				adGroupIdFilter.put("include", idlist);
				param.put("negativeKeywordIdFilter", adGroupIdFilter);
				param.put("includeExtendedDataFields", true);
			    return Result.success(amzAdvKeywordsNegativaService.amzListNegativaKeywords(user, new BigInteger(profileid), param));
		    }else {
		    	List<AmzAdvKeywordsNegativaHsa> sb=new LinkedList<AmzAdvKeywordsNegativaHsa>();
			     for(String id:idlist) {
			    	 AmzAdvKeywordsNegativaHsa amzAdvKeywordsNegativaHsa=amzAdvKeywordsNegativaHsaService.amzGetNegativaKeywordEx(user, new BigInteger(profileid), id);
			    	 sb.add(amzAdvKeywordsNegativaHsa);
			     }
			     return Result.success(sb);
		    }
			
	}
 
	@PostMapping("/amzCreateKeywordNegativa/{profileid}/{campaignType}")
	public Result<?> CreateKeywordNegativaAction(@PathVariable String profileid,@PathVariable String campaignType, @RequestBody Map<String,Object> params ) {
		UserInfo user = UserInfoContext.get();
		String json = params.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray nkeywords = jsonobject.getJSONArray("nkeywords");
		if("hsa".equals(campaignType.toLowerCase()) || "sb".equals(campaignType.toLowerCase())) {
			return Result.success(amzAdvKeywordsNegativaHsaService.amzCreateNegativaKeywords(user, profileid, nkeywords)) ;
		}else {
			return Result.success( amzAdvKeywordsNegativaService.amzCreateNegativaKeywords(user, profileid, nkeywords));
		}
	}
	
	@PostMapping("/updateNegativaKeywordList/{profileid}/{campaignType}")
	public Result<?> updateNegativaKeywordListAction(@PathVariable String profileid,@PathVariable String campaignType,  @RequestBody Map<String,Object> param) {
		UserInfo user = UserInfoContext.get();
		String json = param.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray keywordArray = jsonobject.getJSONArray("nkeywords");
		return Result.success( amzAdvKeywordsNegativaService.amzUpdateNegativaKeywords(user, profileid,keywordArray));
	}
 

	@PostMapping("/deleteNegativaKeywordList/{profileid}/{campaignType}")
	public Result<?> deleteNegativaKeywordListAction(@PathVariable String profileid,@PathVariable String campaignType,  @RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
		if("sp".equals(campaignType.toLowerCase())) {
			JSONObject param = new JSONObject();
			JSONObject adGroupIdFilter = new JSONObject();
			adGroupIdFilter.put("include", idlist);
			param.put("negativeKeywordIdFilter", adGroupIdFilter);
			param.put("includeExtendedDataFields", true);
			return Result.success( amzAdvKeywordsNegativaService.amzDeleteNegativaKeywords(user, new BigInteger(profileid),param));
		}else {
			List<AmzAdvKeywordsNegativaHsa> result=new LinkedList<AmzAdvKeywordsNegativaHsa>();
			for(String id:idlist) {
				AmzAdvKeywordsNegativaHsa item=amzAdvKeywordsNegativaHsaService.amzDeleteNegativaKeywords(user, new BigInteger(profileid),id);
				result.add(item);
			}
			return Result.success(result );
		}
		
	}
 
 	@PostMapping("/campKeywordNegativa/{profileid}/{campaignType}")
	public Result<?> getCampKeywordNegativaAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
			JSONObject param = new JSONObject();
			JSONObject keywordIdFilter = new JSONObject();
			keywordIdFilter.put("include", idlist);
			param.put("campaignNegativeKeywordIdFilter", keywordIdFilter);
			param.put("includeExtendedDataFields", false);
		return Result.success(amzAdvCampKeywordsNegativaService.amzListCampaignNegativeKeywords(user, new BigInteger(profileid), param));
	}
	
 	@PostMapping("/campKeywordNegativaExt/{profileid}/{campaignType}")
	public Result<?> getCampKeywordNegativaExtAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
			JSONObject param = new JSONObject();
			JSONObject keywordIdFilter = new JSONObject();
			keywordIdFilter.put("include", idlist);
			param.put("campaignNegativeKeywordIdFilter", keywordIdFilter);
			param.put("includeExtendedDataFields", true);
		return Result.success(amzAdvCampKeywordsNegativaService.amzListCampaignNegativeKeywords(user, new BigInteger(profileid), param));
	}
	
	@PostMapping("/amzCreateCampKeywordNegativa/{profileid}/{campaignType}")
	public Result<List<AmzAdvCampKeywordsNegativa>> CreateCampKeywordsNegativaAction(@PathVariable String profileid,@PathVariable String campaignType,  @RequestBody Map<String,Object> params) {
		UserInfo user = UserInfoContext.get();
		String json = params.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray nkeywords = jsonobject.getJSONArray("nkeywords");
		return Result.success( amzAdvCampKeywordsNegativaService.amzCreateCampaignNegativeKeywords(user, profileid, nkeywords));
	}

	
	@PostMapping("/updateCampNegativaKeywordList/{profileid}/{campaignType}")
	public Result<?> updateCampNegativaKeywordListAction(@PathVariable String profileid,@PathVariable String campaignType,  @RequestBody Map<String,Object> param) {
		UserInfo user = UserInfoContext.get();
		String json = param.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray keywordArray = jsonobject.getJSONArray("nkeywords");
		return Result.success(amzAdvCampKeywordsNegativaService.amzUpdateCampaignNegativeKeywords(user, profileid,keywordArray));
	}
	@PostMapping("/deleteCampNegativaKeywordList/{profileid}/{campaignType}")
	public Result<?> deleteCampNegativaKeywordListAction(@PathVariable String profileid,@PathVariable String campaignType,  @RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
		JSONObject param = new JSONObject();
		JSONObject adGroupIdFilter = new JSONObject();
		adGroupIdFilter.put("include", idlist);
		param.put("campaignNegativeKeywordIdFilter", adGroupIdFilter);
		return Result.success(amzAdvCampKeywordsNegativaService.amzDeleteCampaignNegativaKeywords(user,new BigInteger(profileid),param));
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
		map.put("name", name);
		if(StrUtil.isNotEmpty(matchType) && !matchType.equals("all")) {
			map.put("matchType", matchType);
		}
		if(nameList.size() > 1) {
			map.put("nameList", nameList);
		}else {
			map.put("nameList", null);
		}
		map.put("campaignType", "sp");
		PageList<Map<String,Object>> list = null;
		if(query.getCampaignType().toLowerCase().equals("sp")) {
	         list=amzAdvKeywordsService.getKeywordQueryList(map, query.getPageBounds());
		}else {
			 list=amzAdvKeywordsHsaService.getKeywordQueryList(map, query.getPageBounds());
		}
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
	
 	@PostMapping("/keywords/{profileid}/{campaignType}")
	public Result<?> getKeywordListAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
			JSONObject param = new JSONObject();
			JSONObject keywordIdFilter = new JSONObject();
			keywordIdFilter.put("include", idlist);
			param.put("keywordIdFilter", keywordIdFilter);
			param.put("includeExtendedDataFields", false);
		return Result.success(amzAdvKeywordsService.amzGetKeywords(user, new BigInteger(profileid), param));
	}
	
 	@PostMapping("/keywordsExt/{profileid}/{campaignType}/ignoreRepeat")
	public Result<?> getKeywordExtListAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
		if(campaignType.toLowerCase().equals("sp")) {
			JSONObject param = new JSONObject();
			JSONObject adGroupIdFilter = new JSONObject();
			adGroupIdFilter.put("include", idlist);
			param.put("keywordIdFilter", adGroupIdFilter);
			param.put("includeExtendedDataFields", true);
		    return Result.success(amzAdvKeywordsService.amzGetKeywords(user, new BigInteger(profileid), param));
		}else {
			List<AmzAdvKeywordsHsa> list=new LinkedList<AmzAdvKeywordsHsa>();
			 for(String item:idlist) {
				AmzAdvKeywordsHsa obj = amzAdvKeywordsHsaService.amzGetKeywords(user, new BigInteger(profileid), item);
				if(obj!=null) {
					list.add(obj);
				}
			 }
		    return Result.success(list);
		}
			
	}
 	
	@PostMapping("/getKeywordforQuery")
	public Object getKeywordforQueryAction(@RequestBody List<Map<String,Object>>list){
		for(Map<String,Object> map:list) {
			amzAdvKeywordsService.getKeywordforQuery(map);
			amzAdvKeywordsNegativaService.getNegativaKeywordforQuery(map);
		}
		return Result.success(list);
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
		if(matchType==null||matchType.toLowerCase().equals("all")) {
			map.put("matchType", null);
		}else {
			map.put("matchType", matchType.toLowerCase());
			map.put("matchTypeUppercase", matchType.toLowerCase());
		}
		
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
