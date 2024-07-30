package com.wimoor.amazon.adv.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAssets;
import com.wimoor.amazon.adv.common.pojo.AmzAdvBrand;
import com.wimoor.amazon.adv.common.pojo.AmzAdvStores;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvAssetsService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBrandService;
import com.wimoor.amazon.adv.common.service.IAmzAdvStoresService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvSBMedia;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;
@Api(tags = "品牌资源接口")
@RestController
@RequestMapping("/api/v1/AdvertStores")
public class AdvertStoresController {

 
	@Resource
	IAmzAdvStoresService amzAdvStoresService;
	@Resource
	IAmzAdvBrandService amzAdvBrandService;
	@Resource
	IAmzAdvAssetsService amzAdvAssetsService;
 
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	
 
	@GetMapping("/findStoresForProfileId")
	public Result<List<AmzAdvStores>> findStoresForProfileIdAction(String profileid,String entityid) {
		String profileId = profileid;
		if(profileId != null && StringUtil.isNotEmpty(profileId)) {
			return Result.success(amzAdvStoresService.getStoresForProfileId(new BigInteger(profileId),entityid));
		}else {
			throw new BaseException("请选择正确的店铺站点！");
		}
	}
	
	@PostMapping("/createAssets")
	public Result<AmzAdvAssets> createAssetsAction(@RequestBody Map<String,String> param) {
		String profileId = param.get("profileid");
		String brandEntityId = param.get("brandEntityId");
		String mediaType = param.get("mediaType");
		String name = param.get("name");
		String imageType = param.get("imageType");
		String asset = param.get("asset");
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
		
		return Result.success(amzAdvAssetsService.amzCreateStoresAssets(user, new BigInteger(profileId), assetParams));
	}
	
	@GetMapping("/getAsset")
	public Result<List<AmzAdvAssets>> getAssetsAction(String assetid) {
		Example example=new Example(AmzAdvAssets.class);
		example.createCriteria().andEqualTo("assetid", assetid);
		return Result.success(amzAdvAssetsService.selectByExample(example));
	}
	
  
	@GetMapping("/findBrandForProfileId")
	public Result<List<AmzAdvBrand>> findBrandForProfileIdAction(String profileid) {
		if(profileid != null && StringUtil.isNotEmpty(profileid)) {
			List<AmzAdvBrand> result = amzAdvBrandService.getBrandForProfileId(new BigInteger(profileid), null);
			return Result.success(result);
		}else {
			throw new BaseException("请选择正确的店铺站点！");
		}
	}
	
	@GetMapping("/findAssetsForBrandEntityId")
	public Result<List<AmzAdvAssets>> findAssetsForBrandEntityIdAction(String profileid,String brandEntityId) {
		String profileId=profileid;
		if(profileId == null || StringUtil.isEmpty(profileId)) {
			throw new BaseException("请选择正确的店铺站点！");
		}
		if(brandEntityId == null || StringUtil.isEmpty(brandEntityId)) {
			throw new BaseException("请选择产品品牌！");
		}
		return Result.success(amzAdvAssetsService.getAssetsForBrandEntityId(new BigInteger(profileId), brandEntityId));
	}
	
	@GetMapping("/findAsinsForStorePageUrl")
	public Result<Map<String,Object>> findAsinsForStorePageUrlAction(String profileId ,String brandEntityId,String storePageUrl) {
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
		return Result.success(map);
	}
	
	@GetMapping("/mediaUploadUrl")
	public Result<JSONObject> mediaUploadUrlAction(String profileid) {
		String profileId = profileid;
		//UserInfo user = UserInfoContext.get();
		if(profileId == null || StringUtil.isEmpty(profileId)) {
		//	dsaDS 
			throw new BaseException("请选择正确的店铺站点！");
		}
		JSONObject url = amzAdvBrandService.getMediaUrl(new BigInteger(profileId));
		return Result.success(url);
	}
	
 
	@GetMapping("/mediaComplete")
	public JSONObject mediaCompleteAction(String profileid,String version,String uploadLocation) {
		String profileId = profileid;
		UserInfo user = UserInfoContext.get();
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
	
 
	@GetMapping("/mediaDescribe")
	public JSONObject mediaDescribeAction(String profileid,String mediaId) {
		UserInfo user = UserInfoContext.get();
		if(profileid == null || StringUtil.isEmpty(profileid)) {
			throw new BaseException("请选择正确的店铺站点！");
		}	
		
		Map<String,String> param=new HashMap<String,String>();
		param.put("mediaId", mediaId);
		param.put("profileId", profileid);
		JSONObject url = amzAdvBrandService.mediaDescribe(user,param);
		return url;
	}
	
	@GetMapping("/loadOldMedia")
	public AmzAdvSBMedia loadOldMediaAction(String profileid) {
		UserInfo user = UserInfoContext.get();
		if(profileid == null || StringUtil.isEmpty(profileid)) {
			throw new BaseException("请选择正确的店铺站点！");
		}	
		
		Map<String,String> param=new HashMap<String,String>();
		param.put("profileId", profileid);
		AmzAdvSBMedia media = amzAdvBrandService.loadOldMedia(user,param);
		return media;
	}
	
	
 
}
