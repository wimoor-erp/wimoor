package com.wimoor.amazon.adv.sp.service;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;

public interface IAmzAdvCampOptimizationRulesService {
	
	public String amzCreateCampaignOptimiztionRules(AmzAdvProfile profile, JSONObject param) ;
	
	public String amzUpdatesCampaignOptimiztionRules(AmzAdvProfile profile, JSONObject param) ;
	
	public String amzListCampaignOptimiztionRules(AmzAdvProfile profile, JSONObject param) ;
	
	public String amzGetCampaignEligibilityRules(AmzAdvProfile profile, JSONObject param) ;
	
	public String amzGetCampaignOptimiztionRules(AmzAdvProfile profile, String campaignOptimizationId) ;
	
	public String amzDeleteCampaignOptimiztionRules(AmzAdvProfile profile, String campaignOptimizationId);
}
