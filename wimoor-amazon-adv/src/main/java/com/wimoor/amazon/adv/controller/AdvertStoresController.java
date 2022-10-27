package com.wimoor.amazon.adv.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAssets;
import com.wimoor.amazon.adv.common.pojo.AmzAdvBrand;
import com.wimoor.amazon.adv.common.pojo.AmzAdvStores;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvAssetsService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBrandService;
import com.wimoor.amazon.adv.common.service.IAmzAdvStoresService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvSBMedia;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import tk.mybatis.mapper.util.StringUtil;

@Controller
@RequestMapping("/AdvertStores")
public class AdvertStoresController {

 
	@Resource
	IAmzAdvStoresService amzAdvStoresService;
	@Resource
	IAmzAdvBrandService amzAdvBrandService;
	@Resource
	IAmzAdvAssetsService amzAdvAssetsService;
 
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	
	@ResponseBody
	@RequestMapping("/findStoresForProfileId")
	public List<AmzAdvStores> findStoresForProfileIdAction(HttpServletRequest request, Model model) {
		String profileId = request.getParameter("profileid");
		if(profileId != null && StringUtil.isNotEmpty(profileId)) {
			return amzAdvStoresService.getStoresForProfileId(new BigInteger(profileId));
		}else {
			throw new BaseException("请选择正确的店铺站点！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/createAssets")
	public AmzAdvAssets createAssetsAction(HttpServletRequest request, Model model) {
		String profileId = request.getParameter("profileid");
		String brandEntityId = request.getParameter("brandEntityId");
		String mediaType = request.getParameter("mediaType");
		String name = request.getParameter("name");
		String imageType = request.getParameter("imageType");
		String asset = request.getParameter("asset");
		UserInfo user = UserInfoContext.get();
		if(profileId == null || StringUtil.isEmpty(profileId)) {
			throw new BaseException("请选择正确的店铺站点！");
		}
		if(mediaType == null || StringUtil.isEmpty(mediaType)) {
			throw new BaseException("请选择图像类型！");
		}else {
			if(!"brandLogo".equals(mediaType)){
				throw new BaseException("目前只支持的类型为“brandLogo”！");
			}
		}
		if(name == null || StringUtil.isEmpty(name)) {
			throw new BaseException("请填写图像名称！");
		}
		if(imageType == null || StringUtil.isEmpty(imageType)) {
			throw new BaseException("请选择图像格式类型！");
		}
		if(asset == null || StringUtil.isEmpty(asset)) {
			throw new BaseException("请选择上传图片！");
		}
		Map<String, Object> assetParams = new HashMap<String, Object>();
		assetParams.put("brandEntityId", brandEntityId);
		assetParams.put("mediaType", mediaType);
		assetParams.put("name", name);
		assetParams.put("imageType", imageType);
		assetParams.put("asset", asset);
		
		return amzAdvAssetsService.amzCreateStoresAssets(user, new BigInteger(profileId), assetParams);
	}
	
/*	@ResponseBody
	@RequestMapping("/createAssets")
	public Map<String,Object> createAssetsAction(HttpServletRequest request, Model model) {
		String profileId = request.getParameter("profileid");
		String brandEntityId = request.getParameter("brandEntityId");
		String mediaType = request.getParameter("mediaType");
		JSONObject myjson = new JSONObject();
		myjson.put("brandEntityId", brandEntityId);
		myjson.put("mediaType", mediaType);
		if(profileId == null || StringUtil.isEmpty(profileId)) {
			throw new BaseException("请选择正确的店铺站点！");
		}
		Map<String, Object> assetParams = new HashMap<String, Object>();
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileId));
		AmzAdvAuth amzadvauth = amzAdvAuthService.getAuth(profile);
		if(amzadvauth==null) { 
			throw new BaseException("无法获取所在站点权限");
		}
		AmzRegion regionObject = amzAdvAuthService.getRegion(amzadvauth.getRegion());
		if(regionObject==null) {
			throw new BaseException("无法获取所在区域");
		}
		String url = "https://" + regionObject.getAdvpoint() + "/stores/assets";
		String html = "<form id='submitForm' action='" + url + 
					  "' type='POST' enctype='multipart/form-data'>" + 
					  " File: <input type='file' name='asset'>" + 
					  " <input name='assetInfo'  /> " + myjson +
					  "<input type='submit' value='立即支付' style='display:none' >" +
					  "</form>";
		assetParams.put("profileid", profileId);
		assetParams.put("regionObject", regionObject);
		assetParams.put("html", html);
		return assetParams;
	}*/
	
	@ResponseBody
	@RequestMapping("/findBrandForProfileId")
	public List<AmzAdvBrand> findBrandForProfileIdAction(HttpServletRequest request, Model model) {
		String profileId = request.getParameter("profileid");
		if(profileId != null && StringUtil.isNotEmpty(profileId)) {
			return amzAdvBrandService.getBrandForProfileId(new BigInteger(profileId), null);
		}else {
			throw new BaseException("请选择正确的店铺站点！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/findAssetsForBrandEntityId")
	public List<AmzAdvAssets> findAssetsForBrandEntityIdAction(HttpServletRequest request, Model model) {
		String profileId = request.getParameter("profileid");
		String brandEntityId = request.getParameter("brandEntityId");
		if(profileId == null || StringUtil.isEmpty(profileId)) {
			throw new BaseException("请选择正确的店铺站点！");
		}
		if(brandEntityId == null || StringUtil.isEmpty(brandEntityId)) {
			throw new BaseException("请选择产品品牌！");
		}
		return amzAdvAssetsService.getAssetsForBrandEntityId(new BigInteger(profileId), brandEntityId);
	}
	
	@ResponseBody
	@RequestMapping("/findAsinsForStorePageUrl")
	public Map<String,Object> findAsinsForStorePageUrlAction(HttpServletRequest request, Model model) {
		String storePageUrl = request.getParameter("storePageUrl");
		String profileId = request.getParameter("profileid");
		String brandEntityId = request.getParameter("brandEntityId");
		UserInfo user = UserInfoContext.get();
		if(profileId == null || StringUtil.isEmpty(profileId)) {
			throw new BaseException("请选择正确的店铺站点！");
		}
		if(storePageUrl == null || StringUtil.isEmpty(storePageUrl)) {
			throw new BaseException("请选择正确品牌页面！");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		List<AmzAdvBrand> brandList = amzAdvBrandService.getBrandForProfileId(new BigInteger(profileId), brandEntityId);
		map.put("brandList", brandList);
		List<AmzAdvAssets> assetList = amzAdvAssetsService.getAssetsForBrandEntityId(new BigInteger(profileId), brandEntityId);
		map.put("assetList", assetList);
		List<String> asinsList = amzAdvStoresService.amzGetPageAsins(user, new BigInteger(profileId), storePageUrl);
		List<Map<String,Object>> productInfoList = new ArrayList<Map<String,Object>>();
		for(int i = 0;i < asinsList.size(); i++) {
			String asin = asinsList.get(i);
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("asin", asin);
			param.put("profileid", profileId);
			productInfoList.add(param);
		}
		map.put("asinsList", productInfoList);
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/mediaUploadUrl")
	public JSONObject mediaUploadUrlAction(HttpServletRequest request, Model model) {
		String profileId = request.getParameter("profileid");
		//UserInfo user = UserInfoContext.get();
		if(profileId == null || StringUtil.isEmpty(profileId)) {
		//	dsaDS 
			throw new BaseException("请选择正确的店铺站点！");
		}
		JSONObject url = amzAdvBrandService.getMediaUrl(new BigInteger(profileId));
		return url;
	}
	
	@ResponseBody
	@RequestMapping("/mediaComplete")
	public JSONObject mediaCompleteAction(HttpServletRequest request, Model model) {
		String profileId = request.getParameter("profileid");
		UserInfo user = UserInfoContext.get();
		String uploadLocation=request.getParameter("uploadLocation");
		String version=request.getParameter("version");
		if(profileId == null || StringUtil.isEmpty(profileId)) {
			throw new BaseException("请选择正确的店铺站点！");
		}	
		
		Map<String,String> param=new HashMap<String,String>();
		param.put("uploadLocation", uploadLocation);
		param.put("profileId", profileId);
		param.put("version", version);
		JSONObject url = amzAdvBrandService.completeMedia(user,param);
		return url;
	}
	
	@ResponseBody
	@RequestMapping("/mediaDescribe")
	public JSONObject mediaDescribeAction(HttpServletRequest request, Model model) {
		String profileId = request.getParameter("profileid");
		UserInfo user = UserInfoContext.get();
		String mediaId=request.getParameter("mediaId");
		if(profileId == null || StringUtil.isEmpty(profileId)) {
			throw new BaseException("请选择正确的店铺站点！");
		}	
		
		Map<String,String> param=new HashMap<String,String>();
		param.put("mediaId", mediaId);
		param.put("profileId", profileId);
		JSONObject url = amzAdvBrandService.mediaDescribe(user,param);
		return url;
	}
	
	@ResponseBody
	@RequestMapping("/loadOldMedia")
	public AmzAdvSBMedia loadOldMediaAction(HttpServletRequest request, Model model) {
		String profileId = request.getParameter("profileid");
		UserInfo user = UserInfoContext.get();
		if(profileId == null || StringUtil.isEmpty(profileId)) {
			throw new BaseException("请选择正确的店铺站点！");
		}	
		
		Map<String,String> param=new HashMap<String,String>();
		param.put("profileId", profileId);
		AmzAdvSBMedia media = amzAdvBrandService.loadOldMedia(user,param);
		return media;
	}
	
	
 
}
