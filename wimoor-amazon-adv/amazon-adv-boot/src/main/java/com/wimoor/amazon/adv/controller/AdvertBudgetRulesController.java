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
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvBudgetRules;
import com.wimoor.amazon.adv.sp.service.IAmzAdvBudgetRulesService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvRecommendationsService;
import com.wimoor.common.result.Result;
import io.swagger.annotations.Api;

@Api(tags = "广告活动BudgetRule接口")
@RestController 
@RequestMapping("/api/v1/campaign/budgetRules")
public class AdvertBudgetRulesController {
	@Resource
	IAmzAdvBudgetRulesService amzAdvBudgetRulesService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvRecommendationsService iAmzAdvRecommendationsService;
    
	@PostMapping("/budgetRulesRecommendation/{profileid}/{CampaignType}")
	public Result<?> budgetRulesRecommendationAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(iAmzAdvRecommendationsService.budgetRulesRecommendation(  profile, param) );
	}
	
	@PostMapping("/amzCreateBudgetRules/{profileid}/{CampaignType}")
	public Result<List<AmzAdvBudgetRules>> amzCreateBudgetRulesAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	 	List<AmzAdvBudgetRules> result = amzAdvBudgetRulesService.amzCreateBudgetRules(profile, param,CampaignType);
	 	return Result.success(result);
	}
	
	@PostMapping("/amzUpdateBudgetRules/{profileid}/{CampaignType}")
	public Result<?> amzUpdateBudgetRulesAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject param) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvBudgetRulesService.amzUpdateBudgetRules(profile, param,CampaignType));
	}
	
	@PostMapping("/list/{profileid}/{CampaignType}")
	public Result<?> listAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody QueryForList param) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	 	String type="sp";
		if(CampaignType.toUpperCase().equals("HSA") || CampaignType.toUpperCase().equals("SB")) {
			type="sb";
		}
		if(CampaignType.toUpperCase().equals("SD") ) {
			type="sd";
		}
		param.setCampaignType(type);
	 	return Result.success(amzAdvBudgetRulesService.list(profile, param));
	}
	
	@GetMapping("/amzGetBudgetRule/{profileid}/{CampaignType}")
	public Result<?> amzGetBudgetRuleAction(@PathVariable String profileid,@PathVariable String CampaignType,  String  budgetRuleId) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvBudgetRulesService.amzGetBudgetRule(profile, budgetRuleId,CampaignType));
	}
	
	@PostMapping("/amzGetBudgetRules/{profileid}/{CampaignType}")
	public Result<?> amzGetBudgetRulesAction(@PathVariable String profileid,@PathVariable String CampaignType, @RequestBody JSONObject  param) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvBudgetRulesService.amzGetBudgetRules(profile, param,CampaignType));
	}
	
	@GetMapping("/amzGetCampaignByBudgetRule/{profileid}/{CampaignType}")
	public Result<?> amzGetCampaignByBudgetRuleAction(@PathVariable String profileid,@PathVariable String CampaignType,  String  budgetRuleId) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvBudgetRulesService.amzGetCampaignByBudgetRule(profile, budgetRuleId,CampaignType));
	}
	
	@GetMapping("/amzGetBudgetRulesByCampaign/{profileid}/{CampaignType}")
	public Result<?> amzGetBudgetRulesByCampaignAction(@PathVariable String profileid,@PathVariable String CampaignType,  String  campaignId) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	    return Result.success(amzAdvBudgetRulesService.amzGetBudgetRulesByCampaign(profile, campaignId,CampaignType));
	}
	
	@GetMapping("/amzDeleteCampaignBudgetRule/{profileid}/{CampaignType}")
	public Result<?> amzDeleteCampaignBudgetRuleAction(@PathVariable String profileid,@PathVariable String CampaignType,  String  campaignId,String budgetRuleId) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	 	amzAdvBudgetRulesService.amzDeleteCampaignBudgetRule(profile, campaignId,budgetRuleId,CampaignType);
	    return Result.success();
	}
	
	@PostMapping("/amzPostBudgetRulesByCampaign/{profileid}/{CampaignType}")
	public Result<?> amzPostBudgetRulesByCampaignAction(@PathVariable String profileid,@PathVariable String CampaignType,@RequestBody JSONObject param) {
	 	AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
	 	String campaignId=param.getString("campaignId");
	 	String ruleid=param.getString("ruleid");
	 	return Result.success(amzAdvBudgetRulesService.amzPostBudgetRulesByCampaign(profile, campaignId,ruleid,CampaignType));
	}
	
	@GetMapping("/getAdvBudgetRuleByProfileid/{profileid}/{CampaignType}")
	public Result<List<AmzAdvBudgetRules>> getAdvBudgetRuleByProfileidAction(@PathVariable String profileid,@PathVariable String CampaignType) {
	    return Result.success(amzAdvBudgetRulesService.getAdvBudgetRuleByProfileid(profileid,CampaignType));
	}
}
