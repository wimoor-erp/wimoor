package com.wimoor.amazon.adv.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
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
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductAdsSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductads;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductAdsService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tk.mybatis.mapper.util.StringUtil;

@Api(tags = "广告产品接口")
@RestController 
@RequestMapping("/api/v1/advProductAdsManager") 
public class AdvertProductAdsManagetController {
 
	@Resource
	IAmzAdvProductAdsService amzAdvProductAdsService;
	@Resource
	IAmzAdvProductAdsSDService amzAdvProductAdsSDService;
	
	@GetMapping("/getProductAdsDetail/{profileid}/{campaignType}")
	public Result<Object> getloadProductAdsAction(@PathVariable String profileid,@PathVariable String campaignType, String adgroupid) {
		UserInfo user = UserInfoContext.get();
		Object result=null;
		if (StringUtil.isEmpty(profileid))
			result= null;
		if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())) {
			List<AmzAdvProductadsSD> list = amzAdvProductAdsSDService.getProductadByAdgroupId(user, new BigInteger(profileid), adgroupid);
			result= list;
		}else {
			List<AmzAdvProductads> list = amzAdvProductAdsService.getProductadByAdgroupId(user, new BigInteger(profileid), adgroupid);
			result= list;
		}
		return Result.success(result);
	}
	
	@ResponseBody
	@RequestMapping("/getProductAds")
	public List<AmzAdvProductads> getProductAds(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		BigInteger profileid = null;
		if(request.getParameter("profileid") != null) {
			profileid = new BigInteger(request.getParameter("profileid"));
		}
		String sku = request.getParameter("sku");
		String asin = request.getParameter("asin");
		List<AmzAdvProductads> list = null;
		if(StringUtil.isNotEmpty(sku)) {
			list = amzAdvProductAdsService.amzGetProductAdBysku(user, profileid, sku);
		}
		else if(StringUtil.isNotEmpty(asin)) {
			list = amzAdvProductAdsService.amzGetProductAdByasin(user, profileid, asin);
		}
		return list;
	}
	
	 
	
    @ApiOperation("查询广告产品")
	@PostMapping("/getProductAdList")
	public Result<PageList<Map<String,Object>>> getProductAdListAction(@ApiParam("查询广告") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query); 
		String state = query.getState();
		String name =query.getName();
		String sku = query.getSku();
		String asin = query.getAsin();
		String color = query.getColor();
		String creator =query.getCreator();
		String changeRate =query.getChangeRate();
		String remark = query.getRemark();
		String categoryid = query.getCategoryid();
		String skuStr = query.getSkuStr();
		List<String> skuList = new ArrayList<String>();
		if (StringUtil.isEmpty(remark)) {
			remark = null;
		} else {
			remark =  remark + "%";
		}
		if (StringUtil.isEmpty(sku)) {
			sku = null;
		} else {
			sku =  sku + "%";
		}
		if(StringUtil.isEmpty(skuStr)) {
			skuStr = null;
		}else {
			String [] skus = skuStr.split(",");
			for(int i = 0; i < skus.length; i++) {
				if(StringUtil.isEmpty(skus[i])){
					continue;
				}
				skuList.add(skus[i]);
			}
			sku = null;
		}
		if (StringUtil.isEmpty(asin)) {
			asin = null;
		} else {
			asin =  asin + "%";
		}
		if (StringUtil.isEmpty(name)) {
			name = null;
		} else {
			name = name + "%";
		}
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		map.put("name", name);
		map.put("sku", sku);
		map.put("asin", asin);
		map.put("color", color);
		map.put("creator", creator);
		if (StrUtil.isNotEmpty(changeRate)) {
			map.put("changeRate", changeRate);
		} 
		if (StrUtil.isNotEmpty(remark)) {
			map.put("remark", remark);
		}
		if (StrUtil.isNotEmpty(categoryid)) {
			map.put("categoryid", categoryid);
		}
		map.put("skuList", skuList);
		
		PageList<Map<String, Object>> list = amzAdvProductAdsService.getProductAdList(map,query.getPageBounds());
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
			return Result.success(new PageList<Map<String, Object>>());
		}
	}
    
   	@PostMapping("/createProductAdList/{profileid}/{campaignType}")
   	public Result<String> createProductAdListAction(@PathVariable String profileid,@PathVariable String campaignType, @RequestBody Map<String,Object> params ) {
   		UserInfo user = UserInfoContext.get();
		String json = params.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray productAdArray = jsonobject.getJSONArray("productAds");
		if(campaignType!=null&& "sd".equals(campaignType.toLowerCase())) {
			if(amzAdvProductAdsSDService.amzCreateProductAds(user, profileid, productAdArray) == null) {
				throw new BaseException("亚马逊连接异常，修改失败！");
			}
		}else {
				if(amzAdvProductAdsService.amzCreateProductAds(user, profileid, productAdArray) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
		}
   		return Result.success();
   	}
   	
	
	@PostMapping("/updateProductAdList/{profileid}/{campaignType}")
	public Result<String> updateProductAdListAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody Map<String,Object> param) {
		UserInfo user = UserInfoContext.get();
		String json = param.get("jsonstr").toString();
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray productAdArray = jsonobject.getJSONArray("productAds");
		if(campaignType!=null&& "sd".equals(campaignType.toLowerCase())) {
				if(amzAdvProductAdsSDService.amzUpdateProductAds(user, profileid, productAdArray) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
			
		}else {
				if(amzAdvProductAdsService.amzUpdateProductAds(user, profileid, productAdArray) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
			 
		}
		
		return Result.success();
	}
	
	
	@ApiOperation("查询广告产品图表")
	@PostMapping("/getProductAdChart")
	public Result<Map<String,Object>> getProductAdChartAction(@ApiParam("查询广告")@RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query); 
		String state = query.getState();
		String name =query.getName();
		String sku = query.getSku();
		String asin = query.getAsin();
		String color = query.getColor();
		String creator =query.getCreator();
		String changeRate =query.getChangeRate();
		String remark = query.getRemark();
		String categoryid = query.getCategoryid();
		String bytime=query.getBytime();
		String skuStr = query.getSkuStr();
		List<String> skuList = new ArrayList<String>();
		if (StringUtil.isEmpty(remark)) {
			remark = null;
		} else {
			remark =remark + "%";
		}
		if (StringUtil.isEmpty(sku)) {
			sku = null;
		} else {
			sku =  sku + "%";
		}
		if(StringUtil.isEmpty(skuStr)) {
			skuStr = null;
		}else {
			String [] skus = skuStr.split(",");
			for(int i = 0; i < skus.length; i++) {
				if(StringUtil.isEmpty(skus[i])){
					continue;
				}
				skuList.add(skus[i]);
			}
			sku = null;
		}
		if (StringUtil.isEmpty(asin)) {
			asin = null; 
		} else {
			asin = asin + "%";
		}
		if (StringUtil.isEmpty(name)) {
			name = null; 
		} else {
			name =name + "%";
		}
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		map.put("name", name);
		map.put("sku", sku);
		map.put("asin", asin);
		map.put("color", color);
		map.put("creator", creator);
		map.put("changeRate", changeRate);
		map.put("remark", remark);
		map.put("categoryid", categoryid);
		map.put("skuList", skuList);
		map.put("bytime", bytime);
		Map<String,Object> mapList = amzAdvProductAdsService.getProductAdChart(map);
		return Result.success(mapList);
	}
	
	@GetMapping("/getProductAdotherAsin")
	public Result<List<Map<String,Object>>> getProductAdotherAsinAction(String campaignid,String adGroupid,String profileid,
			String asin,String fromDate,String endDate){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("campaignid", campaignid);
		map.put("adGroupid", adGroupid);
		map.put("profileid", profileid);
		map.put("asin", asin);
		map.put("fromDate", fromDate);
		map.put("endDate", endDate);
		return Result.success(amzAdvProductAdsService.getProductAdotherAsin(map));
	}
	
 
	@ResponseBody
	@RequestMapping("/getProductByAdgroup")
	public Object getProductByAdgroupAction(HttpServletRequest request) {
		String profileid = request.getParameter("profileid");
		String campaignid = request.getParameter("campaignid");
		String adgroupid = request.getParameter("adgroupid");
		if (StringUtil.isNotEmpty(profileid) && StringUtil.isNotEmpty(campaignid) && StringUtil.isNotEmpty(adgroupid)) {
			BigInteger profileids=new BigInteger(profileid);
			BigInteger campaignids=new BigInteger(campaignid);
			BigInteger adgroupids=new BigInteger(adgroupid);
			List<Map<String, Object>> list = amzAdvProductAdsService.selectByAdgroupid(profileids, campaignids,
					adgroupids);
			return list;
		} else {
			return null;
		}
	}
	
	@PostMapping("/deleteProductAds/{profileid}/{campaignType}")
	public Result<?> deleteProductAdsListAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist){
		UserInfo user = UserInfoContext.get();
		if(idlist==null)return Result.success(null);
		List<AmzAdvProductads> result =new LinkedList<AmzAdvProductads>();
		JSONObject param = new JSONObject();
		JSONObject campaignIdFilter = new JSONObject();
		campaignIdFilter.put("include", idlist);
		param.put("adIdFilter", campaignIdFilter);
		param.put("includeExtendedDataFields", false);
		List<AmzAdvProductads> list = amzAdvProductAdsService.amzDeleteProductAds(user,new BigInteger(profileid),param);
		if(list != null) {
			result.addAll(list);
		}
		return Result.success(result) ;
	}
	
	@PostMapping("/getProductAds/{profileid}/{campaignType}")
	public Result<?> getProductAdsListAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist){
		UserInfo user = UserInfoContext.get();
		if(campaignType.toLowerCase().equals("sp")) {
			if(idlist==null)return Result.success(null);
			List<AmzAdvProductads> result =new LinkedList<AmzAdvProductads>();
			JSONObject param = new JSONObject();
			JSONObject campaignIdFilter = new JSONObject();
			campaignIdFilter.put("include", idlist);
			param.put("adIdFilter", campaignIdFilter);
			param.put("includeExtendedDataFields", false);
			List<AmzAdvProductads> list = amzAdvProductAdsService.amzListProductAds(user,new BigInteger(profileid),param);
			if(list != null) {
				result.addAll(list);
			}
			return Result.success(result) ;
		}else if(campaignType.toLowerCase().equals("sd")) {
			List<AmzAdvProductadsSD> result =new LinkedList<AmzAdvProductadsSD>();
			for(String item:idlist) {
				AmzAdvProductadsSD ads = amzAdvProductAdsSDService.amzListProductAds(user,new BigInteger(profileid),item);
					if(ads != null) {
						result.add(ads);
					}
			 }
			return Result.success(result) ;
		}else {
			return Result.success() ;
		}
	}
	
	@PostMapping("/getProductAdsExt/{profileid}/{campaignType}/ignoreRepeat")
	public Result<?> getProductAdsExtListAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody List<String> idlist){
		UserInfo user = UserInfoContext.get();
		if(idlist==null)return Result.success(null);
		if(campaignType.toLowerCase().equals("sp")) {
			List<AmzAdvProductads> result =new LinkedList<AmzAdvProductads>();
			JSONObject param = new JSONObject();
			JSONObject campaignIdFilter = new JSONObject();
			campaignIdFilter.put("include", idlist);
			param.put("adIdFilter", campaignIdFilter);
			param.put("includeExtendedDataFields", true);
			List<AmzAdvProductads> list = amzAdvProductAdsService.amzListProductAds(user,new BigInteger(profileid),param);
			if(list != null) {
				result.addAll(list);
			}
			return Result.success(result) ;
		}
		if(campaignType.toLowerCase().equals("sb")) {
			return Result.success() ;
		}
		if(campaignType.toLowerCase().equals("sd")) {
			List<AmzAdvProductadsSD> result =new LinkedList<AmzAdvProductadsSD>();
			 for(String item:idlist) {
				AmzAdvProductadsSD ads = amzAdvProductAdsSDService.amzListProductAds(user,new BigInteger(profileid),item);
					if(ads != null) {
						result.add(ads);
					}
			 }
			return Result.success(result) ;
		}
		return Result.success() ;
	}
	
	@ApiOperation("查询广告产品汇总")
	@PostMapping("/getSumProductAd")
	public Result<Object> getSumProductAdAction(@ApiParam("查询广告") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query); 
		String state = query.getState();
		String name =query.getName();
		String sku = query.getSku();
		String asin = query.getAsin();
		String color = query.getColor();
		String creator =query.getCreator();
		String changeRate =query.getChangeRate();
		String remark = query.getRemark();
		String categoryid = query.getCategoryid();
		String skuStr = query.getSkuStr();
		List<String> skuList = new ArrayList<String>();
		if (StringUtil.isEmpty(remark)) {
			remark = null;
		} else {
			remark =remark + "%";
		}
		if (StringUtil.isEmpty(sku)) {
			sku = null;
		} else {
			sku = sku + "%";
		}
		if(StringUtil.isEmpty(skuStr)) {
			skuStr = null;
		}else {
			String [] skus = skuStr.split(",");
			for(int i = 0; i < skus.length; i++) {
				if(StringUtil.isEmpty(skus[i])){
					continue;
				}
				skuList.add(skus[i]);
			}
			sku = null;
		}
		if (StringUtil.isEmpty(asin)) {
			asin = null;
		} else {
			asin =asin + "%";
		}
		if (StringUtil.isEmpty(name)) {
			name = null;
		} else {
			name = name + "%";
		}
		map.put("shopid", user.getCompanyid());
		map.put("state", state);
		map.put("name", name);
		map.put("sku", sku);
		map.put("asin", asin);
		map.put("color", color);
		map.put("creator", creator);
		if (StrUtil.isNotEmpty(changeRate)) {
			map.put("changeRate", changeRate);
		} 
		map.put("remark", remark);
		map.put("categoryid", categoryid);
		map.put("skuList", skuList);
		List<Map<String, Object>> param = amzAdvProductAdsService.getSumProductAd(map);
		return Result.success(AdvertController.SumAdvertDate(param));
	}
	
}
