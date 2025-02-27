package com.wimoor.amazon.adv.controller;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sb.service.IAmzAdvAdsHsaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "广告格式接口")
@RestController 
@RequestMapping("/api/v1/ads")
public class AdvertAdsController {
	@Resource
	IAmzAdvAdsHsaService amzAdvAdsHsaService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	
	@PostMapping("/createAds/{profileid}/{adsType}")
	public Result<?> createAdsAction(@PathVariable String profileid,@PathVariable String adsType, @RequestBody JSONObject params ) {
		UserInfo user = UserInfoContext.get();
		return Result.success(amzAdvAdsHsaService.createAds(user,profileid,adsType,params));
	}
	
    
	@PostMapping("/updateAds/{profileid}/{campaignType}") 
	public Result<?> updateAdGroupsAction(@PathVariable String profileid,@RequestBody JSONObject params ) {
		UserInfo user = UserInfoContext.get();
		return Result.success(amzAdvAdsHsaService.updateAds(user,profileid,params));
	}
	
    
	
 
	
	@PostMapping("/getAds/{profileid}/{campaignType}")
	public Result<?> getAdsAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody JSONObject params){
		 AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
		return Result.success(amzAdvAdsHsaService.amzGetAdsList(profile,params));
	}
	
	@PostMapping("/deleteAds/{profileid}/{campaignType}")
	public Result<?> deleteAdsAction(@PathVariable String profileid,@PathVariable String campaignType,@RequestBody JSONObject params){
		    AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
		    UserInfo user = UserInfoContext.get();
			return Result.success(amzAdvAdsHsaService.amzDeleteAdsList(user,profile,params));
	}
	
	@ApiOperation("查询广告活动图表")
	@PostMapping("/getAdsChart")
	public Result<Map<String,Object>> getAdsChartAction(@ApiParam("查询广告活动") @RequestBody QueryForList query){
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map = AdvertController.amzAdvParameterMap(query); 
		String bytime =query.getBytime();
		map.put("bytime", bytime);
		map.put("shopid", user.getCompanyid());
	     List<Map<String, Object>> mapList = amzAdvAdsHsaService.getAdsChart(map);
		return Result.success(amzAdvCampaignService.getChartData(null,mapList,null, map));
	}

	 @ApiOperation("查询广告组")
		@PostMapping("/getAdsList")
		public Result<PageList<Map<String,Object>>> getAdsListAction(@ApiParam("查询广告组") @RequestBody QueryForList query){
		 	UserInfo user = UserInfoContext.get();
			Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
			map.put("shopid", user.getCompanyid());
			PageList<Map<String,Object>> list = amzAdvAdsHsaService.getAdsList(map, query.getPageBounds());
			if(list != null && list.size() > 0) {
				for(Map<String, Object> item : list) {
					if("paused".equals(item.get("campaignStatus")) && "enabled".equals(item.get("status"))) {
						item.put("passiveState", "paused");
					}
					if("archived".equals(item.get("campaignStatus"))) {
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

	 
	 	@ApiOperation("查询广告组汇总")
		@PostMapping("/getSumAds")
		public Result<Map<String, Object>> getSumAdsAction(@ApiParam("查询广告组") @RequestBody QueryForList query){
		 	UserInfo user = UserInfoContext.get();
			Map<String,Object> map = AdvertController.amzAdvParameterMap(query);
			map.put("shopid", user.getCompanyid());
			Map<String, Object> list = amzAdvAdsHsaService.getSumAds(map);
			return Result.success(list);
		}
	 
}
