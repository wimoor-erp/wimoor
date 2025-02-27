package com.wimoor.amazon.adv.controller;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.service.IAmzAdvAssetsService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCreativesHsaService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvCreativesSDService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.util.StringUtil;

@Api(tags = "广告创意接口")
@RestController 
@RequestMapping("/api/v1/creative")
public class AdvertCreativeController {
	@Resource
	IAmzAdvCreativesSDService amzAdvCreativesSDService;
	@Resource
	IAmzAdvCreativesHsaService amzAdvCreativesHsaService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvAssetsService amzAdvAssetsService;
	
	@PostMapping("/getCreativePreviewHTML/{profileid}/{CampaignType}")
	public Result<?> getCreativePreviewHTMLAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
		UserInfo user = UserInfoContext.get();
		List<Object> result = null;
		if (StringUtil.isEmpty(CampaignType)) {
			throw new BizException("广告类型不能为空");
		}
		if(CampaignType.toLowerCase().equals("sp")) {
			 
				 
		}
		if(CampaignType.toLowerCase().equals("sd")) {
			return Result.success(amzAdvCreativesSDService.getCreativePreviewHTML(user, new BigInteger(profileid), param));
		}
		if(CampaignType.toLowerCase().equals("sb")) {
				 
		}
		return Result.success(result) ;
	}
 
	@PostMapping("/getListCreativeModerations/{profileid}/{CampaignType}")
	public Result<?> getListCreativeModerationsAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
		UserInfo user = UserInfoContext.get();
		List<Object> result = null;
		if (StringUtil.isEmpty(CampaignType)) {
			throw new BizException("广告类型不能为空");
		}
		if(CampaignType.toLowerCase().equals("sp")) {
			 
				 
		}
		if(CampaignType.toLowerCase().equals("sd")) {
			return Result.success(amzAdvCreativesSDService.getListCreativeModerations(user, new BigInteger(profileid), param));
		}
		if(CampaignType.toLowerCase().equals("sb")) {
				 
		}
		return Result.success(result) ;
	}

	@PostMapping("/getListOfCreatives/{profileid}/{CampaignType}")
	public Result<?> getListOfCreativesAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
		UserInfo user = UserInfoContext.get();
		if (StringUtil.isEmpty(CampaignType)) {
			throw new BizException("广告类型不能为空");
		}
		if(CampaignType.toLowerCase().equals("sp")) {
			 
				 
		}
		if(CampaignType.toLowerCase().equals("sd")) {
			return Result.success(amzAdvCreativesSDService.getListOfCreatives(user, new BigInteger(profileid), param));
		}
		if(CampaignType.toLowerCase().equals("sb")) {
		    return Result.success(amzAdvCreativesHsaService.getListOfCreatives(user, new BigInteger(profileid), param));
		}
		return Result.success() ;
	}
	
	@PostMapping("/requestCreatives/{profileid}/{CampaignType}")
	public Result<?> requestCreativesAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
		UserInfo user = UserInfoContext.get();
		if (StringUtil.isEmpty(CampaignType)) {
			throw new BizException("广告类型不能为空");
		}
		if(CampaignType.toLowerCase().equals("sp")) {
			 
				 
		}
		if(CampaignType.toLowerCase().equals("sd")) {
			return Result.success(amzAdvCreativesSDService.requestCreatives(user, new BigInteger(profileid), param));
		}
		if(CampaignType.toLowerCase().equals("sb")) {
				 
		}
		return Result.success() ;
	}
	
	@PostMapping("/updatesCreatives/{profileid}/{CampaignType}")
	public Result<?> updatesCreativesAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
		UserInfo user = UserInfoContext.get();
		if (StringUtil.isEmpty(CampaignType)) {
			throw new BizException("广告类型不能为空");
		}
		if(CampaignType.toLowerCase().equals("sp")) {
			 
				 
		}
		if(CampaignType.toLowerCase().equals("sd")) {
			return Result.success(amzAdvCreativesSDService.updatesCreatives(user, new BigInteger(profileid), param));
		}
		if(CampaignType.toLowerCase().equals("sb")) {
				 
		}
		return Result.success() ;
	}
	
	@ApiOperation("通过文件名称获取上传文件需要的链接 https://advertising.amazon.com/API/docs/en-us/guides/sponsored-display/creatives")
	@PostMapping("/amzAssetsUpload/{profileid}")
	public Result<?> amzAssetsUploadAction(@PathVariable String profileid,@RequestBody JSONObject param) {
		UserInfo user = UserInfoContext.get();
		return Result.success(amzAdvAssetsService.amzAssetsUpload(user, new BigInteger(profileid), param));
		 
	}
	
	@ApiOperation("通过链接已经上传完文件之后将其注册成资源")
	@PostMapping("/amzAssetsRegister/{profileid}")
	public Result<?> amzAssetsRegisterAction(@PathVariable String profileid,@RequestBody JSONObject param) {
		    UserInfo user = UserInfoContext.get();
			return Result.success(amzAdvAssetsService.amzAssetsRegister(user, new BigInteger(profileid), param));
	}
	
	@ApiOperation("查看已经上传的资源")
	@GetMapping("/amzAssets/{profileid}")
	public Result<?> amzAssetsAction(@PathVariable String profileid) {
		UserInfo user = UserInfoContext.get();
		return Result.success(amzAdvAssetsService.amzAssets(user, new BigInteger(profileid)));
	 
	}
	@ApiOperation("搜索已经上传的资源")
	@PostMapping("/amzAssetsSearch/{profileid}")
	public Result<?> amzAssetsSearchAction(@PathVariable String profileid,@RequestBody JSONObject param) {
		    UserInfo user = UserInfoContext.get();
			return Result.success(amzAdvAssetsService.amzAssetsSearch(user, new BigInteger(profileid), param));
	}
	

	@PostMapping("/createBrandVideo/{profileid}/{CampaignType}")
	public Result<?> createBrandVideoAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
		UserInfo user = UserInfoContext.get();
		if (StringUtil.isEmpty(CampaignType)) {
			throw new BizException("广告类型不能为空");
		}
		if(CampaignType.toLowerCase().equals("sp")) {
			 
				 
		}
		if(CampaignType.toLowerCase().equals("sd")) {
			return Result.success(amzAdvCreativesHsaService.createBrandVideo(user, new BigInteger(profileid), param));
		}
		if(CampaignType.toLowerCase().equals("sb")) {
				 
		}
		return Result.success() ;
	}
	
	@PostMapping("/createVideo/{profileid}/{CampaignType}")
	public Result<?> createVideoAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
		UserInfo user = UserInfoContext.get();
		if (StringUtil.isEmpty(CampaignType)) {
			throw new BizException("广告类型不能为空");
		}
		if(CampaignType.toLowerCase().equals("sp")) {
			 
				 
		}
		if(CampaignType.toLowerCase().equals("sd")) {
			return Result.success(amzAdvCreativesHsaService.createVideo(user, new BigInteger(profileid), param));
		}
		if(CampaignType.toLowerCase().equals("sb")) {
				 
		}
		return Result.success() ;
	}
	 
	@PostMapping("/createProductCollection/{profileid}/{CampaignType}")
	public Result<?> createProductCollectionAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
		UserInfo user = UserInfoContext.get();
		if (StringUtil.isEmpty(CampaignType)) {
			throw new BizException("广告类型不能为空");
		}
		if(CampaignType.toLowerCase().equals("sp")) {
			 
				 
		}
		if(CampaignType.toLowerCase().equals("sd")) {
			return Result.success(amzAdvCreativesHsaService.createProductCollection(user, new BigInteger(profileid), param));
		}
		if(CampaignType.toLowerCase().equals("sb")) {
				 
		}
		return Result.success() ;
	}
	
	@PostMapping("/createStoreSpotlight/{profileid}/{CampaignType}")
	public Result<?> createStoreSpotlightAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
		UserInfo user = UserInfoContext.get();
		if (StringUtil.isEmpty(CampaignType)) {
			throw new BizException("广告类型不能为空");
		}
		if(CampaignType.toLowerCase().equals("sp")) {
			 
				 
		}
		if(CampaignType.toLowerCase().equals("sd")) {
			return Result.success(amzAdvCreativesHsaService.createStoreSpotlight(user, new BigInteger(profileid), param));
		}
		if(CampaignType.toLowerCase().equals("sb")) {
				 
		}
		return Result.success() ;
	}
}
