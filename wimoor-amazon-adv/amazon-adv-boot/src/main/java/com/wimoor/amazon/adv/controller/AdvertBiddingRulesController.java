package com.wimoor.amazon.adv.controller;

import java.math.BigInteger;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvBiddingRulesService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampOptimizationRulesService;
import com.wimoor.common.result.Result;
import io.swagger.annotations.Api;

@Api(tags = "广告活动BudgetRule接口")
@RestController 
@RequestMapping("/api/v1/campaign/biddingRules")
public class AdvertBiddingRulesController {
	@Resource
	IAmzAdvBiddingRulesService amzAdvBiddingRulesService;
	@Resource
	IAmzAdvCampOptimizationRulesService amzAdvCampOptimizationRulesService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
    
	@PostMapping("/amzCreateBudgetRules/{profileid}/{CampaignType}")
	public Result<?> amzCreateBudgetRulesAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvBiddingRulesService.amzCreateBiddingRules(profile, param));
	}
	
	@PostMapping("/amzUpdateBudgetRules/{profileid}/{CampaignType}")
	public Result<?> amzUpdateBudgetRulesAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvBiddingRulesService.amzUpdateBiddingRules(profile, param));
	}
	
	@PostMapping("/amzGetBiddingRulesByCampaign/{profileid}/{CampaignType}")
	public Result<?> amzGetBiddingRulesByCampaignAction(@PathVariable String profileid,@PathVariable String CampaignType, String campaignId) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvBiddingRulesService.amzGetBiddingRulesByCampaign(profile, campaignId));
	}
	
	@PostMapping("/amzCreateCampaignOptimiztionRules/{profileid}/{CampaignType}")
	public Result<?> amzCreateCampaignOptimiztionRulesAction(@PathVariable String profileid,@PathVariable String CampaignType,@RequestBody JSONObject param) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvCampOptimizationRulesService.amzCreateCampaignOptimiztionRules(profile, param));
	}
	
	@PostMapping("/amzUpdatesCampaignOptimiztionRules/{profileid}/{CampaignType}")
	public Result<?> amzUpdatesCampaignOptimiztionRulesAction(@PathVariable String profileid,@PathVariable String CampaignType,@RequestBody JSONObject param) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvCampOptimizationRulesService.amzUpdatesCampaignOptimiztionRules(profile, param));
	}
	
	@GetMapping("/amzUpdatesCampaignOptimiztionRules/{profileid}/{CampaignType}")
	public Result<?> amzUpdatesCampaignOptimiztionRulesAction(@PathVariable String profileid,@PathVariable String CampaignType,  String campaignOptimizationId) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvCampOptimizationRulesService.amzDeleteCampaignOptimiztionRules(profile, campaignOptimizationId));
	}
	
	@PostMapping("/amzGetCampaignEligibilityRules/{profileid}/{CampaignType}")
	public Result<?> amzGetCampaignEligibilityRulesAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvCampOptimizationRulesService.amzGetCampaignEligibilityRules(profile, param));
	}
	
	@GetMapping("/amzGetCampaignOptimiztionRules/{profileid}/{CampaignType}")
	public Result<?> amzGetCampaignOptimiztionRulesAction(@PathVariable String profileid,@PathVariable String CampaignType, String campaignOptimizationId) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvCampOptimizationRulesService.amzGetCampaignOptimiztionRules(profile, campaignOptimizationId));
	}
	
	@PostMapping("/amzListCampaignOptimiztionRules/{profileid}/{CampaignType}")
	public Result<?> amzListCampaignOptimiztionRulesAction(@PathVariable String profileid,@PathVariable String CampaignType, JSONObject param) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvCampOptimizationRulesService.amzListCampaignOptimiztionRules(profile, param));
	}
	
}
