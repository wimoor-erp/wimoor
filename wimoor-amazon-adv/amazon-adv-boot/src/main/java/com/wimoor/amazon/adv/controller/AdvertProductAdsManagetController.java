package com.wimoor.amazon.adv.controller;

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
	
	@ResponseBody
	@RequestMapping("/getProductInfoByAsin")
	public Object getProductInfoByAsin(HttpServletRequest request, Model model){
		BigInteger profileid = null;
		if(StringUtil.isNotEmpty(request.getParameter("profileid")) ){
			profileid = new BigInteger(request.getParameter("profileid"));
		}
		String asin = request.getParameter("asin");
		if(StringUtil.isEmpty(asin)) {
			return null;
		}
		String[] asinArray = null;
		asinArray = asin.split(",");
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < asinArray.length; i++) {
			list.add(asinArray[i]);
		}
		//amzAdvProductAdsService.getProductInfoByAsin(profileid, list);
		List<Map<String,Object>> list2 = amzAdvProductAdsService.amzGetProductInfoByAsin(profileid, list);
        if(list2==null) {
        	list2=new ArrayList<Map<String,Object>>();
        }
		PageList<Map<String, Object>> pagelist = ERPBaseController.getPage(list2, ListController.getPageBounds(request));
		return pagelist;
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
		map.put("changeRate", changeRate);
		map.put("remark", remark);
		map.put("categoryid", categoryid);
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
	
	@ResponseBody
	@RequestMapping("/updateProductAdList")
	public String updateProductAdListAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String json = request.getParameter("jsonstr");
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		String campaignType = jsonobject.getString("campaignType");
		JSONArray productAdArray = jsonobject.getJSONArray("productAdArray");
		if(campaignType!=null&& "sd".equals(campaignType.toLowerCase())) {
			Map<BigInteger, List<AmzAdvProductadsSD>> map = new HashMap<BigInteger, List<AmzAdvProductadsSD>>();
			for (int i = 0; i < productAdArray.size(); i++) {
				JSONObject productAd = productAdArray.getJSONObject(i);
				String state = productAd.getString("status");
				String sku = productAd.getString("sku");
				String asin = productAd.getString("asin");
				BigInteger campaignid = productAd.getBigInteger("campaignId");
				BigInteger adGroupid = productAd.getBigInteger("adGroupId");
				BigInteger profileId = productAd.getBigInteger("profileid");
				BigInteger adid = productAd.getBigInteger("id");
				AmzAdvProductadsSD amzAdvProductads = new AmzAdvProductadsSD();
				amzAdvProductads.setAdid(adid);
				amzAdvProductads.setCampaignid(campaignid);
				amzAdvProductads.setAdgroupid(adGroupid);
				amzAdvProductads.setProfileid(profileId);
				amzAdvProductads.setSku(sku);
				amzAdvProductads.setAsin(asin);
				amzAdvProductads.setState(state);
				amzAdvProductads.setOpttime(new Date());
				if (map.get(profileId) == null) {
					List<AmzAdvProductadsSD> list = new ArrayList<AmzAdvProductadsSD>();
					list.add(amzAdvProductads);
					map.put(profileId, list);
				} else {
					map.get(profileId).add(amzAdvProductads);
				}
			}
			
			for (BigInteger key : map.keySet()) {
				if(amzAdvProductAdsSDService.amzUpdateProductAds(user, key, map.get(key)) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
			}
		}else {
			Map<BigInteger, List<AmzAdvProductads>> map = new HashMap<BigInteger, List<AmzAdvProductads>>();
			for (int i = 0; i < productAdArray.size(); i++) {
				JSONObject productAd = productAdArray.getJSONObject(i);
				String state = productAd.getString("status");
				String sku = productAd.getString("sku");
				String asin = productAd.getString("asin");
				BigInteger campaignid = productAd.getBigInteger("campaignId");
				BigInteger adGroupid = productAd.getBigInteger("adGroupId");
				BigInteger profileId = productAd.getBigInteger("profileid");
				BigInteger adid = productAd.getBigInteger("id");
				AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
				amzAdvProductads.setAdid(adid);
				amzAdvProductads.setCampaignid(campaignid);
				amzAdvProductads.setAdgroupid(adGroupid);
				amzAdvProductads.setProfileid(profileId);
				amzAdvProductads.setSku(sku);
				amzAdvProductads.setAsin(asin);
				amzAdvProductads.setState(state);
				amzAdvProductads.setOpttime(new Date());
				if (map.get(profileId) == null) {
					List<AmzAdvProductads> list = new ArrayList<AmzAdvProductads>();
					list.add(amzAdvProductads);
					map.put(profileId, list);
				} else {
					map.get(profileId).add(amzAdvProductads);
				}
			}
			
			for (BigInteger key : map.keySet()) {
				if(amzAdvProductAdsService.amzUpdateProductAds(user, key, map.get(key)) == null) {
					throw new BaseException("亚马逊连接异常，修改失败！");
				}
			}
		}
		
		return "SUCCESS";
	}
	
	@ResponseBody
	@RequestMapping("/archiveProductAd")
	public String archiveProductAdAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String profileId = request.getParameter("profileid");
		String adId = request.getParameter("id");
		String campaignType = request.getParameter("campaignType");
		if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())) {
			AmzAdvProductadsSD amzAdvProductads = null;
			amzAdvProductads = amzAdvProductAdsSDService.amzArchiveProductAd(user, new BigInteger(profileId), adId);
			if(amzAdvProductads == null) {
				return "ERROR";
			}
		}else {
			AmzAdvProductads amzAdvProductads = null;
			amzAdvProductads = amzAdvProductAdsService.amzArchiveProductAd(user, new BigInteger(profileId), adId);
			if(amzAdvProductads == null) {
				return "ERROR";
			}
		}
		
		return "SUCCESS";
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
	
	@ResponseBody
	@RequestMapping("/getProductAdsExtList")
	public Object getCampaignExtListAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String ids = request.getParameter("ids");
		if(StringUtil.isEmpty(ids))return null;
		JSONObject idsjson = GeneralUtil.getJsonObject(ids);
		List<AmzAdvProductads> result =new LinkedList<AmzAdvProductads>();
		for(Entry<String, Object> item:idsjson.entrySet()) {
			String profileid = item.getKey();
			ids=(String) item.getValue();
			if(StringUtil.isEmpty(ids)||StringUtil.isEmpty(profileid))return null;
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("adIdFilter",ids);
			List<AmzAdvProductads> list = amzAdvProductAdsService.amzListProductAdsEx(user,new BigInteger(profileid),param);
			if(list != null) {
				result.addAll(list);
			}
		}
		return result;
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
		map.put("changeRate", changeRate);
		map.put("remark", remark);
		map.put("categoryid", categoryid);
		map.put("skuList", skuList);
		List<Map<String, Object>> param = amzAdvProductAdsService.getSumProductAd(map);
		return Result.success(AdvertController.SumAdvertDate(param));
	}
	
}
