package com.wimoor.amazon.adv.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTargetQueryService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvProductTargeHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvProductTargeNegativaHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvProductTargeHsaService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvProductTargeNegativaHsaService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvRecommendationsHsaService;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeNegativaSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeNegativaSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvRecommendationsSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampProductTargeNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampProductTargeNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductAdsService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeNegativaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeService;
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


@Api(tags = "广告投放接口")
@RestController 
@RequestMapping("/api/v1/advProductTargeManager") 
public class AdvertProductTargeManagerController {
	@Resource
	IAmzAdvProductTargeService amzAdvProductTargeService;
	@Resource
	IAmzAdvProductTargeNegativaService amzAdvProductTargeNegativaService;
	@Resource
	IAmzAdvCampProductTargeNegativaService amzAdvCampProductTargeNegativaService;
	@Resource
	IAmzAdvProductTargeNegativaSDService amzAdvProductTargeNegativaSDService;
	@Resource
	IAmzAdvBidReCommendService amzAdvBidReCommendService;
	@Resource
	IAmzAdvRecommendationsService amzAdvProductTargeSuggestService;
	@Resource
	IAmzAdvBrowseNodeService amzAdvBrowseNodeService;
	@Resource
	IAmzAdvProductAdsService amzAdvProductAdsService;
	@Resource
	IAmzAdvReportTargetQueryService amzAdvReportTargetQueryService;
	@Resource
	IAmzAdvKeywordsNegativaService amzAdvKeywordsNegativaService;
	@Resource
	IAmzAdvProductTargeSDService amzAdvProductTargeSDService;
	@Resource
	IAmzAdvProductTargeHsaService amzAdvProductTargeHsaService;
	@Resource
	IAmzAdvRecommendationsTargetService amzAdvRecommendationsTargetService;
	@Resource
	IAmzAdvRecommendationsSDService amzAdvRecommendationsSDService;
	@Resource
	IAmzAdvRecommendationsHsaService amzAdvRecommendationsHsaService;
	@Resource 
	IAmzAdvRecommendationsService amzAdvRecommendationsService;
    @Resource
    IAmzAdvProductTargeNegativaHsaService amzAdvProductTargeNegativaHsaService;
	
	@ResponseBody
	@RequestMapping("/getBrandRecommendations")
	public Result<List<Map<String, Object>>> getBrandRecommendations(String profileid,String categoryId,String keyword,String adtype ){
		UserInfo user = UserInfoContext.get();
		List<Map<String, Object>> list = null;
		if(profileid != null) {
			if("sb".equals(adtype) || "hsa".equals(adtype)) {
				list = amzAdvRecommendationsTargetService.amzRecommendationsBrand_HSA(user,new BigInteger(profileid), categoryId, keyword);
			}
			else if("sp".equals(adtype)){
				list = amzAdvRecommendationsTargetService.amzGetBrandRecommendations(user, new BigInteger(profileid), categoryId, keyword);
			}
		} 
		return Result.success(list);
	}
	
	@PostMapping("/getRecommendationsProductCount")
	public Result<?> getRecommendationsProductCount(@RequestBody JSONObject param){
		UserInfo user = UserInfoContext.get();
		String profileid=param.get("profileid").toString();
		String campaignType=param.getString("campaignType");
		if(campaignType!=null&&campaignType.toLowerCase().equals("sp")) {
			param.remove("profileid");
			param.remove("campaignType");
			return Result.success(this.amzAdvRecommendationsService.amzAdvRecommendationsProductCount(user, new BigInteger(profileid), param)) ;
		}else {
			return Result.success(this.amzAdvRecommendationsHsaService.amzAdvRecommendationsProductCount(user, new BigInteger(profileid), param)) ;
		}
	}
	
	 
	
	@PostMapping("/getTargetsCategoriesRecommendations/{profileid}/{campaignType}")
	public Result<?> getSuggestedTargets(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody JSONObject param){
		UserInfo user = UserInfoContext.get();
		if(campaignType!=null&&campaignType.toLowerCase().equals("sp")) {
			return Result.success(this.amzAdvRecommendationsService.amzAdvRecommendationsTargetsCategories(user, new BigInteger(profileid), param)) ;
		}else {
			return Result.success(this.amzAdvRecommendationsHsaService.amzAdvRecommendationsTargetsCategories(user, new BigInteger(profileid), param)) ;
		}
	}
	
	@PostMapping("/getTargetsCategoriesAllRecommendations/{profileid}/{campaignType}")
	public Result<?> getRecommendationsTargetsCategoriesAll(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody JSONObject param){
		UserInfo user = UserInfoContext.get();
		if(campaignType!=null&&campaignType.toLowerCase().equals("sp")) {
			return Result.success(this.amzAdvRecommendationsService.amzAdvRecommendationsTargetsCategoriesAll(user, new BigInteger(profileid), param)) ;
		}else {
			return Result.success(this.amzAdvRecommendationsHsaService.amzAdvRecommendationsTargetsCategoriesAll(user, new BigInteger(profileid), param)) ;
		}
		
	}
	
	@PostMapping("/getTargetsRefineRecommendations/{profileid}/{campaignType}")
	public Result<?> getRecommendationsTargetsRefine(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody JSONObject param){
		UserInfo user = UserInfoContext.get();
		if(campaignType!=null&&campaignType.toLowerCase().equals("sp")) {
		    return Result.success(this.amzAdvRecommendationsService.amzAdvRecommendationsTargetsRefine(user, new BigInteger(profileid), param)) ;
		}else {
			return Result.success(this.amzAdvRecommendationsHsaService.amzAdvRecommendationsTargetsRefine(user, new BigInteger(profileid), param)) ;
		}
	}
	
	
	@PostMapping("/getTargetsProductsRecommendations/{profileid}/{campaignType}")
	public Result<?> getTargetsProducts(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody JSONObject param){
		UserInfo user = UserInfoContext.get();
		if(campaignType.toLowerCase().equals("sp")) {
			return Result.success(this.amzAdvRecommendationsService.amzAdvRecommendationsTargetsProducts(user, new BigInteger(profileid), param)) ;
		}else if(campaignType.toLowerCase().equals("sd")) {
			return Result.success(this.amzAdvRecommendationsSDService.amzTargetRecommendationsProductList(user, new BigInteger(profileid),param));
		}else {
			return Result.success(this.amzAdvRecommendationsHsaService.amzTargetRecommendationsProductList(user,new BigInteger(profileid),param));
		}
		
	}
	
	@PostMapping("/getTargetsBidRecommendations/{profileid}/{campaignType}/ignoreRepeat")
	public Result<?> getTargetsBidRecommendations(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody JSONObject param){
		UserInfo user = UserInfoContext.get();
		if(campaignType.toLowerCase().equals("sp")) {
			return Result.success(this.amzAdvRecommendationsService.amzAdvRecommendationsTargetsBid(user, new BigInteger(profileid), param)) ;
		}else if(campaignType.toLowerCase().equals("sd")) {
			return Result.success(this.amzAdvRecommendationsSDService.amzAdvRecommendationsTargetsBid(user, new BigInteger(profileid), param)) ;
		}else {
			return Result.success(this.amzAdvRecommendationsHsaService.amzAdvRecommendationsBid(user, new BigInteger(profileid), param));
		}
	}
	
	@PostMapping("/getHeadlineRecommendations/{profileid}/{campaignType}")
	public Result<?> getHeadlineRecommendations(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody JSONObject param){
		    UserInfo user = UserInfoContext.get();
			return Result.success(this.amzAdvRecommendationsSDService.amzTargetRecommendationsHeadline(user, new BigInteger(profileid), param)) ;
	}
	
	@GetMapping("/getNegativeTargetsBrandsRecommendations/{profileid}/{campaignType}")
	public Result<?> getNegativaTargetBrandRecommendations(@PathVariable String profileid,@PathVariable String campaignType,String keyword){
		UserInfo user = UserInfoContext.get();
		if(campaignType.toLowerCase().equals("sp")) {
			if(StrUtil.isBlank(keyword)) {
				return Result.success(this.amzAdvRecommendationsService.amzAdvRecommendationsNegativeTargetsBrands(user, new BigInteger(profileid), null)) ;
			}else {
				JSONObject param=new JSONObject();
				param.put("keyword", keyword);
				return Result.success(this.amzAdvRecommendationsService.amzAdvRecommendationsNegativeTargetsBrandsSearch(user, new BigInteger(profileid), param));
			}
		}else {
				JSONObject param=new JSONObject();
				param.put("nextToken", keyword);
				return Result.success(this.amzAdvRecommendationsHsaService.amzAdvRecommendationsNegativeTargetsBrands(user, new BigInteger(profileid), param));
		}
		
	}
	
	 


	
	@PostMapping("/getTargetBidRecommendations")
	public Result<?> getAdGroupBidRecommendations(@RequestBody JSONObject params ){
		//String profileid,String adGroupid,String campaignId,String expression,String adtype
		String profileid=params.getString("profileid");
		String adGroupid=params.getString("adGroupid");
		String campaignId=params.getString("campaignId");
		String adtype=params.getString("adtype");
		UserInfo user = UserInfoContext.get();
		if(StringUtil.isEmpty(profileid) || !params.containsKey("expression")) {
			return Result.failed();
		}
		JSONArray expressionArray = params.getJSONArray("expression");
		if("sb".equals(adtype.toLowerCase()) || "hsa".equals(adtype.toLowerCase())) {
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
	
	 
	@PostMapping("/archiveCampNegativaTargets/{profileid}/{campaignType}")
	public Result<?> archiveCampNegativaTargetsAction(@PathVariable String profileid,@PathVariable String campaignType, @RequestBody List<String> ids ) {
		UserInfo user = UserInfoContext.get();
		if(campaignType!=null&& "sp".equals(campaignType.toLowerCase())) {
			List<AmzAdvCampProductTargeNegativa> result=new LinkedList<AmzAdvCampProductTargeNegativa>();
			for(String id:ids) {
				AmzAdvCampProductTargeNegativa sd = amzAdvCampProductTargeNegativaService.amzArchiveNegativeTargetingClause(user, new BigInteger(profileid), id);
			    if(sd!=null) {
			    	result.add(sd);
			    }
			}
			return Result.success(result);
		}
		return Result.success();
	}
 
	
	@PostMapping("/archiveNegativaTargets/{profileid}/{campaignType}")
	public Result<?> archiveNegativaTargetsAction(@PathVariable String profileid,@PathVariable String campaignType, @RequestBody List<String> ids ) {
		UserInfo user = UserInfoContext.get();
		if(campaignType!=null&& "sd".equals(campaignType.toLowerCase())) {
			List<AmzAdvProductTargeNegativaSD> result=new LinkedList<AmzAdvProductTargeNegativaSD>();
			for(String id:ids) {
				AmzAdvProductTargeNegativaSD sd = amzAdvProductTargeNegativaSDService.amzArchiveNegativeTargetingClause(user, new BigInteger(profileid), id);
			    if(sd!=null) {
			    	result.add(sd);
			    }
			}
			return Result.success(result);
		}else if(campaignType!=null&& "sb".equals(campaignType.toLowerCase())) {
			List<AmzAdvProductTargeNegativaHsa> result=new LinkedList<AmzAdvProductTargeNegativaHsa>();
			for(String id:ids) {
				AmzAdvProductTargeNegativaHsa sb = amzAdvProductTargeNegativaHsaService.amzArchiveNegativeTargetingClause(user, new BigInteger(profileid), id);
			    if(sb!=null) {
			    	result.add(sb);
			    }
			}
			return Result.success(result);
		}
		if(campaignType!=null&& "sp".equals(campaignType.toLowerCase())) {
			JSONObject param=new JSONObject();
			JSONObject negativeTargetIdFilte=new JSONObject();
			negativeTargetIdFilte.put("include", ids);
			param.put("negativeTargetIdFilte", negativeTargetIdFilte);
			List<AmzAdvProductTargeNegativa> result = amzAdvProductTargeNegativaService.amzArchiveNegativeTargetingClause(user, new BigInteger(profileid), param);
			return Result.success(result);
		}
		return Result.success();
	}
	

	public void getSerchStr(Map<String, Object> map) {
		String campaignType =  map.get("campaignType").toString().toUpperCase();
		String serch = (String) map.get("searchlist");
		String[] serchArray = serch.split(",");
		String serchlist = "";
		String HSAcsrt = "";
		for (int i = 0; i < serchArray.length; i++) {
				if ("ACOS".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull(sum(cost) / sum(attributedSales14d),0) ACOS ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull(sum(cost) / sum(attributedSales7d),0) ACOS ,";
				} else if ("ROAS".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull(sum(attributedSales14d) / sum(cost),0) ROAS ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull(sum(attributedSales7d) / sum(cost),0) ROAS ,";
				} else if ("CSRT".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull(sum(attributedConversions14d) / sum(clicks),0) CSRT ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull(sum(attributedConversions7d) / sum(clicks),0) CSRT ,";
				} else if ("avgcost".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull((sum(cost) / sum(clicks)),0) avgcost ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull((sum(cost) / sum(clicks)),0) avgcost ,";
				} else if ("CTR".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull(sum(clicks) / sum(impressions),0) CTR ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull(sum(clicks) / sum(impressions),0) CTR ,";
				}else if ("sumUnits".equals(serchArray[i])) {
					if ("all".equals(campaignType)) {
						serchlist = serchlist +"sum(attributedUnitsOrdered7d) sumUnits,";
						HSAcsrt = HSAcsrt+"sum(attributedConversions14d) sumUnits,";
					} else if ("HSA".equals(campaignType)||"SB".equals(campaignType)) {
						HSAcsrt = HSAcsrt+"sum(attributedConversions14d) sumUnits,";
					} else if ("SP".equals(campaignType)) {
						serchlist = serchlist +"sum(attributedUnitsOrdered7d) sumUnits,";
					}
				} else {
					serchlist = serchlist +"sum(" + serchArray[i] + ") " + serchArray[i] + ",";
					HSAcsrt = HSAcsrt+"sum(" + serchArray[i] + ") " + serchArray[i] + ",";
				}
		}
		if(serchlist.contains(",")) {
			serchlist=serchlist.substring(0, serchlist.length()-1);
		}
		if(HSAcsrt.contains(",")) {
			HSAcsrt=HSAcsrt.substring(0, HSAcsrt.length()-1);
		}
		map.put("HSAserchlist", HSAcsrt);
		map.put("serchlist", serchlist);
		map.put("value1", serchArray[0]);
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
		getSerchStr(map);
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
		}else if(campaignType!=null&&"sp".equals(campaignType.toLowerCase())){
			if(query.getAdGroupid()!=null) {
				pageList = amzAdvProductTargeNegativaService.getProductNegativaTargeList(map, query.getPageBounds());
			}else {
				pageList = amzAdvCampProductTargeNegativaService.getProductNegativaTargeList(map, query.getPageBounds());
			}
		}else {
			pageList=amzAdvProductTargeNegativaHsaService.getProductNegativaTargeList(map, query.getPageBounds());
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
		
 	@PostMapping("/createNegativaTargets/{profileid}/{campaignType}")
   	public Result<String> createNegativaTargeListAction(@PathVariable String profileid,@PathVariable String campaignType, @RequestBody Map<String,Object> params ) {
   		UserInfo user = UserInfoContext.get();
		String json = params.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray keywords = jsonobject.getJSONArray("ntargets");
		if(campaignType!=null&& "sd".equals(campaignType.toLowerCase())) {
			if(amzAdvProductTargeNegativaSDService.amzCreateNegativeTargetingClauses(user, profileid, keywords) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}else if(campaignType!=null&& ("sb".equals(campaignType.toLowerCase())||"hsa".equals(campaignType.toLowerCase()))){
			if(amzAdvProductTargeNegativaHsaService.amzCreateNegativeTargetingClauses(user, profileid, keywords) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}else {
				if(amzAdvProductTargeNegativaService.amzCreateNegativeTargetingClauses(user, profileid, keywords) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
		}
   		return Result.success();
   	}
   	
	
	@PostMapping("/updateNegativaTargets/{profileid}/{campaignType}")
	public Result<String> updateNegativaTargeListAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody Map<String,Object> param) {
		UserInfo user = UserInfoContext.get();
		String json = param.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray keywords = jsonobject.getJSONArray("ntargets");
		if(campaignType!=null&& "sd".equals(campaignType.toLowerCase())) {
				if(amzAdvProductTargeNegativaSDService.amzUpdateNegativeTargetingClauses(user, profileid, keywords) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
			
		}else if(campaignType!=null&& ("sb".equals(campaignType.toLowerCase())||"hsa".equals(campaignType.toLowerCase()))){
			if(amzAdvProductTargeNegativaHsaService.amzUpdateNegativeTargetingClauses(user, profileid, keywords) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}else {
				if(amzAdvProductTargeNegativaService.amzUpdateNegativeTargetingClauses(user, profileid, keywords) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
			 
		}
		return Result.success();
	}
	
	@PostMapping("/negativaTargets/{profileid}/{campaignType}")
	public Result<?> negativaTargetsAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
		if(campaignType.toLowerCase().equals("sp")) {
			JSONObject param = new JSONObject();
			JSONObject keywordIdFilter = new JSONObject();
			keywordIdFilter.put("include", idlist);
			param.put("negativeTargetIdFilter", keywordIdFilter);
			param.put("includeExtendedDataFields", false);
			return Result.success(amzAdvProductTargeNegativaService.amzListNegativeTargetingClauses(user, new BigInteger(profileid), param));
		}else if(campaignType.toLowerCase().equals("sd")) {
			List<AmzAdvProductTargeNegativaSD> list=new LinkedList<AmzAdvProductTargeNegativaSD>();
			for(String item:idlist) {
				AmzAdvProductTargeNegativaSD sd=amzAdvProductTargeNegativaSDService.amzNegativeTargetingClauses(user, new BigInteger(profileid),item);
				list.add(sd);
			}
			return Result.success(list);
		}else {
			List<AmzAdvProductTargeNegativaHsa> list=new LinkedList<AmzAdvProductTargeNegativaHsa>();
			for(String item:idlist) {
				AmzAdvProductTargeNegativaHsa sd=amzAdvProductTargeNegativaHsaService.amzNegativeTargetingClauses(user, new BigInteger(profileid),item);
				list.add(sd);
			}
			return Result.success(list);
		}
	}
	
 	@PostMapping("/negativaTargetsExt/{profileid}/{campaignType}/ignoreRepeat")
	public Result<?> getNegativaTargetsListAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
			if(campaignType.toLowerCase().equals("sp")) {
				  JSONObject param = new JSONObject();
					JSONObject adGroupIdFilter = new JSONObject();
					adGroupIdFilter.put("include", idlist);
					param.put("negativeTargetIdFilter", adGroupIdFilter);
					param.put("includeExtendedDataFields", true);
				return Result.success(amzAdvProductTargeNegativaService.amzListNegativeTargetingClauses(user, new BigInteger(profileid), param));
			}else if(campaignType.toLowerCase().equals("sd")) {
				List<AmzAdvProductTargeNegativaSD> list=new LinkedList<AmzAdvProductTargeNegativaSD>();
				for(String item:idlist) {
					AmzAdvProductTargeNegativaSD sd=amzAdvProductTargeNegativaSDService.amzNegativeTargetingClauses(user, new BigInteger(profileid),item);
					list.add(sd);
				}
				return Result.success(list);
			}else {
				List<AmzAdvProductTargeNegativaHsa> list=new LinkedList<AmzAdvProductTargeNegativaHsa>();
				for(String item:idlist) {
					AmzAdvProductTargeNegativaHsa sd=amzAdvProductTargeNegativaHsaService.amzNegativeTargetingClauses(user, new BigInteger(profileid),item);
					list.add(sd);
				}
				return Result.success(list);
			}
	}
 	

 	@PostMapping("/campNegativaTargets/{profileid}/{campaignType}")
	public Result<?> getCampNegativaTargetsAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
			JSONObject param = new JSONObject();
			JSONObject keywordIdFilter = new JSONObject();
			keywordIdFilter.put("include", idlist);
			param.put("campaignNegativeKeywordIdFilter", keywordIdFilter);
			param.put("includeExtendedDataFields", false);
		return Result.success(amzAdvCampProductTargeNegativaService.amzListNegativeTargetingClauses(user, new BigInteger(profileid), param));
	}
	
 	@PostMapping("/campNegativaTargetsExt/{profileid}/{campaignType}")
	public Result<?> getCampNegativaTargetsExtAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
			JSONObject param = new JSONObject();
			JSONObject adGroupIdFilter = new JSONObject();
			adGroupIdFilter.put("include", idlist);
			param.put("campaignNegativeKeywordIdFilter", adGroupIdFilter);
			param.put("includeExtendedDataFields", true);
		return Result.success(amzAdvCampProductTargeNegativaService.amzListNegativeTargetingClauses(user, new BigInteger(profileid), param));
	}
	
	@PostMapping("/createCampNegativaTargets/{profileid}/{campaignType}")
	public Result<List<AmzAdvCampProductTargeNegativa>> amzCreateCampNegativaTargetsAction(@PathVariable String profileid,@PathVariable String campaignType,  @RequestBody Map<String,Object> params) {
		UserInfo user = UserInfoContext.get();
		String json = params.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray nkeywords = jsonobject.getJSONArray("ntargets");
		return Result.success( amzAdvCampProductTargeNegativaService.amzCreateNegativeTargetingClauses(user, profileid, nkeywords));
	}

	
	@PostMapping("/updateCampNegativaTargets/{profileid}/{campaignType}")
	public Result<?> updateCampNegativaTargetsAction(@PathVariable String profileid,@PathVariable String campaignType,  @RequestBody Map<String,Object> param) {
		UserInfo user = UserInfoContext.get();
		String json = param.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray keywordArray = jsonobject.getJSONArray("ntargets");
		return Result.success(amzAdvCampProductTargeNegativaService.amzUpdateNegativeTargetingClauses(user, profileid,keywordArray));
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
		PageList<Map<String,Object>> list =amzAdvReportTargetQueryService.getProductTargeQueryList(map, query.getPageBounds());
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
	
	 
	@PostMapping("/getNagetiveTargetforQuery")
	public Result<?> getNagetiveTargetforQueryAction(@RequestBody Map<String,Object> map){
		//if(IniConfig.isDemo()) return null;
		amzAdvProductTargeNegativaService.getNegativaTargetforQuery(map);
		amzAdvKeywordsNegativaService.getNegativaKeywordforQuery(map);
		return Result.success(map) ;
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
	
	 
	
   	@PostMapping("/createTargetList/{profileid}/{campaignType}")
   	public Result<String> createTargetListAction(@PathVariable String profileid,@PathVariable String campaignType, @RequestBody Map<String,Object> params ) {
   		UserInfo user = UserInfoContext.get();
		String json = params.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray keywords = jsonobject.getJSONArray("targets");
		if(campaignType!=null&& "sd".equals(campaignType.toLowerCase())) {
			if(amzAdvProductTargeSDService.amzCreateTargets(user, profileid, keywords) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}else if(campaignType!=null&& ("sb".equals(campaignType.toLowerCase())|| "hsa".equals(campaignType.toLowerCase()))){
			if(amzAdvProductTargeHsaService.amzCreateTargets(user, profileid, keywords) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}else {
				if(amzAdvProductTargeService.amzCreateTargets(user, profileid, keywords) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
		}
   		return Result.success();
   	}
   	
	
	@PostMapping("/updateTargetList/{profileid}/{campaignType}")
	public Result<String> updateTargetListListAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody Map<String,Object> param) {
		UserInfo user = UserInfoContext.get();
		String json = param.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray keywords = jsonobject.getJSONArray("targets");
		if(campaignType!=null&& "sd".equals(campaignType.toLowerCase())) {
				if(amzAdvProductTargeSDService.amzUpdateTargets(user, profileid, keywords) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
			
		}else if(campaignType!=null&& ("sb".equals(campaignType.toLowerCase())|| "hsa".equals(campaignType.toLowerCase()))){
			if(amzAdvProductTargeHsaService.amzUpdateTargets(user, profileid, keywords) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}else {
				if(amzAdvProductTargeService.amzUpdateTargets(user, profileid, keywords) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
			 
		}
		
		return Result.success();
	}
	
	@PostMapping("/archiveTargets/{profileid}/{campaignType}")
	public Result<?> deleteTargetsAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		 UserInfo user = UserInfoContext.get();
		if("sd".equals(campaignType.toLowerCase())) {
			AmzAdvProductTargeSD amzAdvProductTarge = null;
			amzAdvProductTarge = amzAdvProductTargeSDService.amzArchiveTargetingClause(user, new BigInteger(profileid), idlist.get(0));
			return Result.success(Arrays.asList(amzAdvProductTarge));
		}else if("sb".equals(campaignType.toLowerCase())) {
			AmzAdvProductTargeHsa amzAdvProductTarge = null;
			amzAdvProductTarge = amzAdvProductTargeHsaService.amzDeleteTargets(user, new BigInteger(profileid), idlist.get(0));
			return Result.success(Arrays.asList(amzAdvProductTarge));
		}else {
			JSONObject param = new JSONObject();
			JSONObject keywordIdFilter = new JSONObject();
			keywordIdFilter.put("include", idlist);
			param.put("targetIdFilter", keywordIdFilter);
			param.put("includeExtendedDataFields", false);
			return Result.success(amzAdvProductTargeService.amzDeleteTargets(user, new BigInteger(profileid), param));
		}
		
		   
			
	}
	
	@PostMapping("/targets/{profileid}/{campaignType}")
	public Result<?> getTargetsAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
		if(campaignType.toLowerCase().equals("sp")) {
			JSONObject param = new JSONObject();
			JSONObject adGroupIdFilter = new JSONObject();
			adGroupIdFilter.put("include", idlist);
			param.put("targetIdFilter", adGroupIdFilter);
			param.put("includeExtendedDataFields", false);
		    return Result.success(amzAdvProductTargeService.amzGetTargets(user, new BigInteger(profileid), param));
		}
		else if(campaignType.toLowerCase().equals("sd")) {	
			List<AmzAdvProductTargeSD> result=new ArrayList<AmzAdvProductTargeSD>();
			for(String item:idlist) {
				AmzAdvProductTargeSD target = amzAdvProductTargeSDService.amzGetTargetsExt(user, new BigInteger(profileid), item);
				result.add(target);
			}
		    return Result.success(result);
		}else  {
			List<AmzAdvProductTargeHsa> result=new ArrayList<AmzAdvProductTargeHsa>();
			for(String item:idlist) {
				AmzAdvProductTargeHsa target = amzAdvProductTargeHsaService.amzGetTargets(user, new BigInteger(profileid), item);
				result.add(target);
			}
			return Result.success(result);
		}
	}
	
	@PostMapping("/targetsByParam/{profileid}/{campaignType}")
	public Result<?> targetsByParamAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody JSONObject param) {
		UserInfo user = UserInfoContext.get();
		if(campaignType.toLowerCase().equals("sp")) {
		    return Result.success(amzAdvProductTargeService.amzGetTargets(user, new BigInteger(profileid), param));
		}
		else if(campaignType.toLowerCase().equals("sd")) {	
                Map<String,Object> paramobj=param;
                return Result.success(amzAdvProductTargeSDService.amzListTargetingClausesEx(user, new BigInteger(profileid),paramobj ));
		}else {
			return Result.success();
		}
	}
	
 	@PostMapping("/targetsExt/{profileid}/{campaignType}/ignoreRepeat")
	public Result<?> getTargetsExtListAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist) {
		UserInfo user = UserInfoContext.get();
		if(campaignType.toLowerCase().equals("sp")) {
			JSONObject param = new JSONObject();
			JSONObject adGroupIdFilter = new JSONObject();
			adGroupIdFilter.put("include", idlist);
			param.put("targetIdFilter", adGroupIdFilter);
			param.put("includeExtendedDataFields", true);
		    return Result.success(amzAdvProductTargeService.amzGetTargets(user, new BigInteger(profileid), param));
		}
		else if(campaignType.toLowerCase().equals("sd")) {	
			List<AmzAdvProductTargeSD> result=new ArrayList<AmzAdvProductTargeSD>();
			for(String item:idlist) {
				AmzAdvProductTargeSD target = amzAdvProductTargeSDService.amzGetTargetsExt(user, new BigInteger(profileid), item);
				result.add(target);
			}
		    return Result.success(result);
		}else {
			List<AmzAdvProductTargeHsa> result=new ArrayList<AmzAdvProductTargeHsa>();
			for(String item:idlist) {
				AmzAdvProductTargeHsa target = amzAdvProductTargeHsaService.amzGetTargets(user, new BigInteger(profileid), item);
				result.add(target);
			}
			return Result.success(result);
		}
	}
}
