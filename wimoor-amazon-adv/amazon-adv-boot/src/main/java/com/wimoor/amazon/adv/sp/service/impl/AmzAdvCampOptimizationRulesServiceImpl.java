package com.wimoor.amazon.adv.sp.service.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvBudgetRulesMapper;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampOptimizationRulesService;

@Service("amzAdvCampOptimizationRulesService")
public class AmzAdvCampOptimizationRulesServiceImpl implements IAmzAdvCampOptimizationRulesService {
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	ApiBuildService apiBuildService;
	@Resource
    AmzAdvBudgetRulesMapper amzAdvBudgetRulesMapper;
	  public Map<String,String> getHeaderExt(){
	    	Map<String,String> header=new HashMap<String,String>();
	    	header.put("Content-Type", "application/vnd.optimizationrules.v1+json");
	    	header.put("Accept",       "application/vnd.optimizationrules.v1+json");
	    	return header;
	    }
	  
	
	public String amzCreateCampaignOptimiztionRules(AmzAdvProfile profile, JSONObject param) {
		 // TODO Auto-generated method stub
		 String url = "/sp/rules/campaignOptimization";
		 String response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		 return response;
	}
	
	public String amzUpdatesCampaignOptimiztionRules(AmzAdvProfile profile, JSONObject param) {
		 // TODO Auto-generated method stub
		 String url = "/sp/rules/campaignOptimization";
		 String response = apiBuildService.amzAdvPut(profile, url,param.toString(),this.getHeaderExt());
		 return response;
	}
	
	public String amzListCampaignOptimiztionRules(AmzAdvProfile profile, JSONObject param) {
		 // TODO Auto-generated method stub
		 String url = "/sp/rules/campaignOptimization/state";
		 String response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		 return response;
	}
	
	public String amzGetCampaignEligibilityRules(AmzAdvProfile profile, JSONObject param) {
		 // TODO Auto-generated method stub
		 String url = "/sp/rules/campaignOptimization/eligibility";
		 String response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		 return response;
	}
	
	public String amzGetCampaignOptimiztionRules(AmzAdvProfile profile, String campaignOptimizationId) {
		 // TODO Auto-generated method stub
		 String url =  "/sp/rules/campaignOptimization/"+campaignOptimizationId;
		 String response = apiBuildService.amzAdvGet(profile, url,this.getHeaderExt());
		 return response;
	}
	
	public String amzDeleteCampaignOptimiztionRules(AmzAdvProfile profile, String campaignOptimizationId) {
		 // TODO Auto-generated method stub
		 String url =  "/sp/rules/campaignOptimization/"+campaignOptimizationId;
		 String response = apiBuildService.amzAdvDelete(profile, url,this.getHeaderExt());
		 return response;
	}
}

