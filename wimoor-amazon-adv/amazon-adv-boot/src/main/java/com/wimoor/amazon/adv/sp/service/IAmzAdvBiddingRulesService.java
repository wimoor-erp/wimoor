package com.wimoor.amazon.adv.sp.service;

import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvBiddingRules;

public interface IAmzAdvBiddingRulesService extends IService<AmzAdvBiddingRules>{
	
	public String amzGetBiddingRulesByCampaign(AmzAdvProfile profile, String campaignId) ;
	  
	public List<AmzAdvBiddingRules> amzSearchBiddingRules(AmzAdvProfile profile, JSONObject param);
	  
	public List<AmzAdvBiddingRules> amzCreateBiddingRules(AmzAdvProfile profile, JSONObject param);

	public List<AmzAdvBiddingRules> amzUpdateBiddingRules(AmzAdvProfile profile, JSONObject param);
	
}
