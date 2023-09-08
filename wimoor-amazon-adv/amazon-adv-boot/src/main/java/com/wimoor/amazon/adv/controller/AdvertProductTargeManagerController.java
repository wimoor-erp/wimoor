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
import com.wimoor.amazon.adv.common.pojo.AmzAdvBrowseNode;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvBidReCommendService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBrowseNodeService;
import com.wimoor.amazon.adv.common.service.IAmzAdvRecommendationsTargetService;
import com.wimoor.amazon.adv.common.service.IAmzAdvReportTargetQueryService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeNegativaSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductAdsService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeService;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tk.mybatis.mapper.util.StringUtil;


@Api(tags = "广告target接口")
@RestController 
@RequestMapping("/api/v1/advProductTargeManager") 
public class AdvertProductTargeManagerController {
	@Resource
	IAmzAdvProductTargeService amzAdvProductTargeService;
	@Resource
	IAmzAdvProductTargeNegativaService amzAdvProductTargeNegativaService;
	@Resource
	IAmzAdvProductTargeNegativaSDService amzAdvProductTargeNegativaSDService;
	@Resource
	IAmzAdvBidReCommendService amzAdvBidReCommendService;
 
	@Resource
	IAmzAdvBrowseNodeService amzAdvBrowseNodeService;
	@Resource
	IAmzAdvProductAdsService amzAdvProductAdsService;
	@Resource
	IAmzAdvReportTargetQueryService amzAdvReportTargetQueryService;
	@Resource
	IAmzAdvKeywordsNegativaService amzAdvKeywordsNegativaService;
	@Resource
	IAmzAdvRecommendationsTargetService amzAdvRecommendationsTargetService;
	@Resource
	IAmzAdvProductTargeSDService amzAdvProductTargeSDService;
	
	@ResponseBody
	@RequestMapping("/getBrandRecommendations")
	public List<Map<String, Object>> getBrandRecommendations(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		BigInteger profileid = null;
		if(request.getParameter("profileid") != null) {
			profileid = new BigInteger(request.getParameter("profileid"));
		}
		String categoryId = AdvUtils.getRequestValue(request,"categoryId");
		String keyword = request.getParameter("keyword");
		String adtype = request.getParameter("adtype");
		List<Map<String, Object>> list = null;
		if(profileid != null) {
			if("sb".equals(adtype) || "hsa".equals(adtype)) {
				list = amzAdvRecommendationsTargetService.amzRecommendationsBrand_HSA(user, profileid, categoryId, keyword);
			}
			else if("sp".equals(adtype)){
				list = amzAdvRecommendationsTargetService.amzGetBrandRecommendations(user, profileid, categoryId, keyword);
			}
		} 
		return list;
	}
	
	@ResponseBody
	@RequestMapping("/getTargetRecommendations")
	public List<Map<String,Object>> getTargetRecommendations(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		BigInteger profileid = null;
		if(request.getParameter("profileid") != null) {
			profileid = new BigInteger(request.getParameter("profileid"));
		}
		String type = request.getParameter("type");
		String asin = request.getParameter("asin");
		String name = request.getParameter("name");
		String num = request.getParameter("num");
		String adtype = request.getParameter("adtype");
		String nextToken = request.getParameter("nextToken");
		int pageNum = 0;
		if("name".equals(type)) {
			return amzAdvProductAdsService.ListMatchingProducts(profileid, name);
		}else {
			List<String> asinsList = new ArrayList<String>();
			List<Map<String, Object>> backList = null;
			if("sb".equals(adtype) || "hsa".equals(adtype)) {
				asinsList.add(asin);
				List<String> targetList = new ArrayList<String>();
				List<Map<String, Object>> listMap = 
						amzAdvRecommendationsTargetService.amzTargetRecommendationsProductList_HSA(user, profileid, asinsList, 10, nextToken);
				if(listMap != null && listMap.size() > 0) {
					String backToken = "";
					for(int i = 0;i < listMap.size(); i++) {
						if(i == 0) {
							if(listMap.get(i).get("nextToken") != null) {
								backToken = listMap.get(i).get("nextToken").toString();
							}
						}
						targetList.add(listMap.get(i).get("recommendedTargetAsin").toString());
					}
					backList = amzAdvProductAdsService.amzGetProductInfoByAsin(profileid, targetList);
					if(backList != null && backList.get(0) != null) {
						backList.get(0).put("nextToken", backToken);
					}
				}
			}else  if("sd".equals(adtype)) {
				asinsList.add(asin);
				List<String> targetList = new ArrayList<String>();
				List<Map<String, Object>> listMap = 
						amzAdvRecommendationsTargetService.amzTargetRecommendationsProductList_SD(user, profileid, asinsList, 10, nextToken);
				if(listMap != null && listMap.size() > 0) {
					backList=new ArrayList<Map<String,Object>>();
					for(int i = 0;i < listMap.size(); i++) {
						Map<String, Object> item = listMap.get(i);
						targetList.add(item.get("asin").toString());
						if(targetList.size()>=10) {
							List<Map<String, Object>> mList = amzAdvProductAdsService.amzGetProductInfoByAsin(profileid, targetList);
							backList.addAll(mList);
							targetList.clear();
						}
					}
				}
			}
			else if("sp".equals(adtype)) {
				if(StringUtil.isNotEmpty(num)) {
					pageNum = Integer.parseInt(num);
				}
				if(asin != null) {
					String[] asinsArray = asin.split(",");
					for(int i = 0; i < asinsArray.length; i++) {
						asinsList.add(asinsArray[i]);
					}
				}
				Map<String, Object> map = amzAdvRecommendationsTargetService.amzCreateTargetRecommendations(user, profileid, asinsList, 10, pageNum);
				if(map != null) {
					@SuppressWarnings("unchecked")
					List<String> list = (List<String>) map.get("recommendedTargetAsin");
					String pagesize = (String) map.get("totalResultCount");
					if(list != null && list.size() > 0) {
						backList = amzAdvProductAdsService.amzGetProductInfoByAsin(profileid, list);
						if(backList != null && backList.get(0) != null) {
							backList.get(0).put("totalResultCount", pagesize);
						}
					}
				}
			}
			return backList;
		}
	}
	
	@ResponseBody
	@RequestMapping("/getTargetingCategories")
	public List<Map<String, Object>> getTargetingCategories(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		BigInteger profileid = null;
		if(request.getParameter("profileid") != null) {
			profileid = new BigInteger(request.getParameter("profileid"));
		}
		String asins = request.getParameter("asin");
		String adtype = request.getParameter("adtype");
		if(StringUtil.isEmpty(asins)) {
			return null;
		}
		List<Map<String, Object>> list = null;
		if("sb".equals(adtype) || "hsa".equals(adtype)) {
			List<String> asinsList = new ArrayList<String>();
			String[] asinArray = asins.split(",");
			for(int i = 0; i < asinArray.length; i++) {
				asinsList.add(asinArray[i]);
			}
			list = amzAdvRecommendationsTargetService.amzRecommendationsCategories_HSA(user, profileid, asinsList);
		}
		else if("sp".equals(adtype)){
			list = amzAdvRecommendationsTargetService.amzGetTargetingCategories(user, profileid, asins);
		} 
		return list;
	}
	
	@ResponseBody
	@RequestMapping("/getAllCategories")
	public Map<BigInteger, AmzAdvBrowseNode> getAllCategories(HttpServletRequest request, Model model){
		String country = request.getParameter("market");
		Map<BigInteger, AmzAdvBrowseNode> list = amzAdvBrowseNodeService.getAllBrowseNodeMap(country);
		return list;
	}
	
	@ResponseBody
	@RequestMapping("/searchCategories")
	public List<HashMap<String, Object>> searchCategories(HttpServletRequest request, Model model){
		String country = request.getParameter("country");
		String name = request.getParameter("name");
		List<HashMap<String, Object>> map = amzAdvBrowseNodeService.searchBroseNode(name,country);
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/getCategories")
	public Object getCategoriesAction(HttpServletRequest request, Model model){
		String country = request.getParameter("country");
		String parentid = request.getParameter("parentid");
		BigInteger id = new BigInteger(parentid);
		List<AmzAdvBrowseNode> list=amzAdvBrowseNodeService.getChildRenBroseNode(id,country);
		return list;
	}
	
	@PostMapping("/getTargetBidRecommendations")
	public Result<?> getAdGroupBidRecommendations(@RequestBody Map<String,Object> params ){
		//String profileid,String adGroupid,String campaignId,String expression,String adtype
		String profileid=params.get("profileid").toString();
		String adGroupid=params.get("adGroupid").toString();
		String campaignId=params.get("campaignId").toString();
		String expression=params.get("expression").toString();
		String adtype=params.get("adtype").toString();
		UserInfo user = UserInfoContext.get();
		if(StringUtil.isEmpty(profileid) || StringUtil.isEmpty(expression)) {
			return Result.failed();
		}
		JSONObject jsonobject = GeneralUtil.getJsonObject(expression);
		JSONArray expressionArray = jsonobject.getJSONArray("expressions");
		if("sb".equals(adtype) || "hsa".equals(adtype)) {
			BigInteger campaignIdInt = null;
			if(StringUtil.isEmpty(campaignId)) {
				campaignIdInt = null;
			}else {
				campaignIdInt = new BigInteger(campaignId);
			}
			return Result.success(amzAdvBidReCommendService.amzRecommendationsBidsForTarget_HSA(user, new BigInteger(profileid), campaignIdInt, expressionArray));
		}else {
			return Result.success(amzAdvBidReCommendService.amzGetTargetBidRecommendations(user, new BigInteger(profileid), adGroupid, expressionArray));
		}
	}
	
	@ApiOperation("查询广告投放")
	@PostMapping("/getProductTargeList")
	public Result<PageList<Map<String,Object>>> getProductTargeListAction(@ApiParam("查询广告") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
		String state = query.getState();
		String search = query.getSearch();
		if(StringUtil.isEmpty(search))search=null;
		else search="%"+search+"%";
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		map.put("search", search);
		Object campaignType = map.get("campaignType");
		PageList<Map<String,Object>> list = amzAdvProductTargeService.getProductTargeList(map, query.getPageBounds());
		if(list != null && list.size() > 0) {
			for(Map<String, Object> item : list) {
				Object expression = item.get("expression");
	           if(expression!=null&&expression.toString().contains("asinCategorySameAs")){
	       		    JSONArray jsonarr = GeneralUtil.getJsonArray(expression.toString());
	       		    if(campaignType!=null&&"sd".equals(campaignType.toString().toLowerCase())) {
	       		     if(jsonarr.size()>0) {
	       		    	    JSONObject itemtop = jsonarr.getJSONObject(0);
	       		    	    Object value = itemtop.get("value");
	       		    	   //JSONArray value = itemtop.getJSONArray("value");
		       		    	AmzAdvBrowseNode category = amzAdvBrowseNodeService.selectByKey(value);
		       		     if(category!=null) {
			        		   item.put("expressionname","asinCategorySameAs="+category.getName());
			        	   }
		       		    }
	       		    }else {
	       			    if(jsonarr.size()>0) {
		       		    	AmzAdvBrowseNode category = amzAdvBrowseNodeService.selectByKey(jsonarr.getJSONObject(0).get("value"));
		       		     if(category!=null) {
			        		   item.put("expressionname","asinCategorySameAs="+category.getName());
			        	   }
		       		    }
	       		    }
	       	
	        	 
				}
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
			return Result.success( new PageList<Map<String, Object>>());
		}
	}
	
	@ResponseBody
	@RequestMapping("/updateProductTargeList")
	public String updateProductTargeListAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String json = request.getParameter("jsonstr");
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		String campaignType = jsonobject.getString("campaignType");
		JSONArray targeArray = jsonobject.getJSONArray("targeArray");
		if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())) {
			List<AmzAdvProductTargeSD> targeList = new ArrayList<AmzAdvProductTargeSD>();
			for (int i = 0; i < targeArray.size(); i++) {
				JSONObject targe = targeArray.getJSONObject(i);
				String expressionType = targe.getString("expressionType");
				String expression = targe.getString("expression");
				String state = targe.getString("status");
				BigInteger targetid = targe.getBigInteger("id");
				BigInteger adGroupid = targe.getBigInteger("adGroupId");
				BigInteger profileId = targe.getBigInteger("profileid");
				BigDecimal bid = targe.getBigDecimal("bid");
				AmzAdvProductTargeSD amzAdvProductTarge = new AmzAdvProductTargeSD();
				amzAdvProductTarge.setTargetid(targetid);
				amzAdvProductTarge.setAdgroupid(adGroupid);
				amzAdvProductTarge.setProfileid(profileId);
				amzAdvProductTarge.setExpression(expression.toString());
				amzAdvProductTarge.setExpressiontype(expressionType);
				amzAdvProductTarge.setBid(bid);
				amzAdvProductTarge.setState(state);
				amzAdvProductTarge.setOpttime(new Date());
				targeList.add(amzAdvProductTarge);
			}
			if (targeList.size() != 0) {
				Map<BigInteger, List<AmzAdvProductTargeSD>> map = new HashMap<BigInteger, List<AmzAdvProductTargeSD>>();
				for (int i = 0; i < targeList.size(); i++) {
					AmzAdvProductTargeSD amzAdvProductTarge = targeList.get(i);
					BigInteger profileId = amzAdvProductTarge.getProfileid();
					if (map.get(profileId) == null) {
						List<AmzAdvProductTargeSD> list = new ArrayList<AmzAdvProductTargeSD>();
						list.add(amzAdvProductTarge);
						map.put(profileId, list);
					} else {
						map.get(profileId).add(amzAdvProductTarge);
					}
				}
				for (BigInteger key : map.keySet()) {
					if(amzAdvProductTargeSDService.amzUpdateTargetingClauses_V3(user, key, map.get(key)) == null) {
						throw new BaseException("亚马逊连接异常，修改失败！");
					}
				}
			}
		}else {
			List<AmzAdvProductTarge> targeList = new ArrayList<AmzAdvProductTarge>();
			for (int i = 0; i < targeArray.size(); i++) {
				JSONObject targe = targeArray.getJSONObject(i);
				String expressionType = targe.getString("expressionType");
				String expression = targe.getString("expression");
				String state = targe.getString("status");
				BigInteger campaignid = targe.getBigInteger("campaignId");
				BigInteger targetid = targe.getBigInteger("id");
				BigInteger adGroupid = targe.getBigInteger("adGroupId");
				BigInteger profileId = targe.getBigInteger("profileid");
				BigDecimal bid = targe.getBigDecimal("bid");
				AmzAdvProductTarge amzAdvProductTarge = new AmzAdvProductTarge();
				amzAdvProductTarge.setTargetid(targetid);
				amzAdvProductTarge.setCampaignid(campaignid);
				amzAdvProductTarge.setAdgroupid(adGroupid);
				amzAdvProductTarge.setProfileid(profileId);
				amzAdvProductTarge.setExpression(expression.toString());
				amzAdvProductTarge.setExpressiontype(expressionType);
				amzAdvProductTarge.setBid(bid);
				amzAdvProductTarge.setState(state);
				amzAdvProductTarge.setOpttime(new Date());
				targeList.add(amzAdvProductTarge);
			}
			if (targeList.size() != 0) {
				Map<BigInteger, List<AmzAdvProductTarge>> map = new HashMap<BigInteger, List<AmzAdvProductTarge>>();
				for (int i = 0; i < targeList.size(); i++) {
					AmzAdvProductTarge amzAdvProductTarge = targeList.get(i);
					BigInteger profileId = amzAdvProductTarge.getProfileid();
					if (map.get(profileId) == null) {
						List<AmzAdvProductTarge> list = new ArrayList<AmzAdvProductTarge>();
						list.add(amzAdvProductTarge);
						map.put(profileId, list);
					} else {
						map.get(profileId).add(amzAdvProductTarge);
					}
				}
				for (BigInteger key : map.keySet()) {
					if(amzAdvProductTargeService.amzUpdateTargetingClauses(user, key, map.get(key)) == null) {
						throw new BaseException("亚马逊连接异常，修改失败！");
					}
				}
			}
		}
		
		return "SUCCESS"; 
	}
	
	@ResponseBody
	@RequestMapping("/archiveProductTarge")
	public String archiveProductTargeAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String profileId = request.getParameter("profileid");
		String targetId = request.getParameter("id");
		String campaignType=request.getParameter("campaignType");
		if("sd".equals(campaignType)) {
			AmzAdvProductTargeSD amzAdvProductTarge = null;
			amzAdvProductTarge = amzAdvProductTargeSDService.amzArchiveTargetingClause_V3(user, new BigInteger(profileId), targetId);
			if(amzAdvProductTarge == null) {
				return "ERROR";
			}
		}else {
			AmzAdvProductTarge amzAdvProductTarge = null;
			amzAdvProductTarge = amzAdvProductTargeService.amzArchiveTargetingClause(user, new BigInteger(profileId), targetId);
			if(amzAdvProductTarge == null) {
				return "ERROR";
			}
		}
		return "SUCCESS";
	}
	
	@ApiOperation("查询广告投放图表")
	@PostMapping("/getProductTargeChart")
	public Result<Map<String,Object>> getProductTargeChartAction(@ApiParam("查询广告") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		String state = query.getState();
		String bytime = query.getBytime();
		String search =query.getSearch();
		if(StringUtil.isEmpty(search))search=null;
		else search=search+"%";
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		map.put("bytime", bytime);
		map.put("search", search);
		
		return Result.success( amzAdvProductTargeService.getProductTargeChart(map));
	}
	
	@ApiOperation("查询广告否定投放")
	@PostMapping("/getNegativaTargeList")
	public Result<PageList<Map<String,Object>>> getNegativaTargeListAction(@ApiParam("查询广告") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		String state = query.getState();
		String search =query.getSearch();
		String campaignType = query.getCampaignType();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		if(StringUtil.isNotEmpty(search)&&!"".equals(search)) {
			search=search.trim()+"%";
		}else {
			search=null;
		}
		map.put("search", search);
		PageList<Map<String, Object>> pageList = null;
		if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())) {
			pageList = amzAdvProductTargeNegativaSDService.getProductNegativaTargeList(map, query.getPageBounds());
		}else {
			pageList = amzAdvProductTargeNegativaService.getProductNegativaTargeList(map, query.getPageBounds());
		}
		
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
		}else {
			return Result.success( new PageList<Map<String, Object>>());
		}
		return Result.success( pageList);
	}
		
	@ResponseBody
	@RequestMapping("/updateNegativaTargeList")
	public String updateNegativaTargeListAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String json = request.getParameter("jsonstr");
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray targeArray = jsonobject.getJSONArray("targeArray");
		List<AmzAdvProductTargeNegativa> targeList = new ArrayList<AmzAdvProductTargeNegativa>();
		for (int i = 0; i < targeArray.size(); i++) {
			JSONObject targe = targeArray.getJSONObject(i);
			String expressionType = targe.getString("expressionType");
			String expression = targe.getString("expression");
			String state = targe.getString("status");
			BigInteger campaignid = targe.getBigInteger("campaignId");
			BigInteger targetid = targe.getBigInteger("id");
			BigInteger adGroupid = targe.getBigInteger("adGroupId");
			BigInteger profileId = targe.getBigInteger("profileid");
			AmzAdvProductTargeNegativa amzAdvProductTarge = new AmzAdvProductTargeNegativa();
			amzAdvProductTarge.setTargetid(targetid);
			amzAdvProductTarge.setCampaignid(campaignid);
			amzAdvProductTarge.setAdgroupid(adGroupid);
			amzAdvProductTarge.setProfileid(profileId);
			amzAdvProductTarge.setExpression(expression);
			amzAdvProductTarge.setExpressiontype(expressionType);
			amzAdvProductTarge.setState(state);
			amzAdvProductTarge.setOpttime(new Date());
			targeList.add(amzAdvProductTarge);
		}
		if (targeList.size() != 0) {
			Map<BigInteger, List<AmzAdvProductTargeNegativa>> map = new HashMap<BigInteger, List<AmzAdvProductTargeNegativa>>();
			for (int i = 0; i < targeList.size(); i++) {
				AmzAdvProductTargeNegativa amzAdvProductTarge = targeList.get(i);
				BigInteger profileId = amzAdvProductTarge.getProfileid();
				if (map.get(profileId) == null) {
					List<AmzAdvProductTargeNegativa> list = new ArrayList<AmzAdvProductTargeNegativa>();
					list.add(amzAdvProductTarge);
					map.put(profileId, list);
				} else {
					map.get(profileId).add(amzAdvProductTarge);
				}
			}
			for (BigInteger key : map.keySet()) {
				if(amzAdvProductTargeNegativaService.amzUpdateNegativeTargetingClauses(user, key, map.get(key)) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
			}
		}
		return "SUCCESS"; 
	}
	
	@ResponseBody
	@RequestMapping("/archiveNegativaTarge")
	public String archiveNegativaTargeAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String profileId = request.getParameter("profileid");
		String targetId = request.getParameter("id");
		AmzAdvProductTargeNegativa amzAdvProductTarge = null;
		amzAdvProductTarge = amzAdvProductTargeNegativaService.amzArchiveNegativeTargetingClause(user, new BigInteger(profileId), targetId);
		if(amzAdvProductTarge == null) {
			return "ERROR";
		}
		return "SUCCESS";
	}
	
	@ResponseBody
	@RequestMapping("/getExtListTargeting")
	public Object getCampaignExtListAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String ids = request.getParameter("ids");
		JSONObject idsjson = GeneralUtil.getJsonObject(ids);
		List<AmzAdvProductTarge> result =new LinkedList<AmzAdvProductTarge>();
		for(Entry<String, Object> item:idsjson.entrySet()) {
			String profileid = item.getKey();
			ids=(String) item.getValue();
			if(StringUtil.isEmpty(ids)||StringUtil.isEmpty(profileid))return null;
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("targetIdFilter",ids);
			List<AmzAdvProductTarge> list = amzAdvProductTargeService.amzListTargetingClausesEx(user,new BigInteger(profileid),param);
			if(list != null) {
				result.addAll(list);
			}
		}
		return result;
	}
	
	@ApiOperation("查询广告投放Query")
	@PostMapping("/getProductTargeQueryList")
	public Result<PageList<Map<String,Object>>> getTargeQueryListAction(@ApiParam("查询广告") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		String state = query.getState();
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
				name = nameList.get(0) + "%";
			}else {
				name = null;
			}
		}
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		map.put("name", name);
		if(nameList.size() > 1) {
			map.put("nameList", nameList);
		}else {
			map.put("nameList", null);
		}
		PageList<Map<String,Object>> list = amzAdvReportTargetQueryService.getProductTargeQueryList(map, query.getPageBounds());
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
			return Result.success( new PageList<Map<String, Object>>());
		}
	}
	
	@ApiOperation("查询广告投Query图表")
	@PostMapping("/getProductTargeQueryChart")
	public Result<Map<String,Object>> getProductTargeQueryChartAction(@ApiParam("查询广告") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		String state = query.getState();
		String bytime = query.getBytime();
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
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		map.put("bytime", bytime);
		map.put("name", name);
		if(nameList.size() > 1) {
			map.put("nameList", nameList);
		}else {
			map.put("nameList", null);
		}
		return Result.success( amzAdvReportTargetQueryService.getProductTargeQueryChart(map));
	}
	
	@ResponseBody
	@RequestMapping("/getNagetiveTargetforQuery")
	public Object getNagetiveTargetforQueryAction(HttpServletRequest request, Model model){
		//if(IniConfig.isDemo()) return null;
		String rowid = request.getParameter("rowid");
		String adGroupid = request.getParameter("adGroupid");
		String campaignid = request.getParameter("campaignid");
		String profileid = request.getParameter("profileid");
		String query = request.getParameter("query");
		if(StringUtil.isEmpty(query))return null;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rowid", rowid);
		map.put("adGroupid", adGroupid);
		map.put("campaignid", campaignid);
		map.put("profileid", profileid);
		map.put("query", query);
		amzAdvProductTargeNegativaService.getNegativaTargetforQuery(map);
		amzAdvKeywordsNegativaService.getNegativaKeywordforQuery(map);
		return map;
	}
	
	@ApiOperation("查询广告投放汇总")
	@PostMapping("/getSumProductTarge")
	public Result<Object> getSumProductTargeQueryAction(@ApiParam("查询广告") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query); 
		map.put("shopid", user.getCompanyid());
		List<Map<String, Object>> param = amzAdvProductTargeService.getSumProductTarge(map);
		return Result.success( AdvertController.SumAdvertDate(param));
	}
}
