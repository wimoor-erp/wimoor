package com.wimoor.amazon.adv.sp.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvBudgetRules;

public interface IAmzAdvBudgetRulesService extends IService<AmzAdvBudgetRules>{

	List<AmzAdvBudgetRules> amzGetBudgetRules(AmzAdvProfile profile, JSONObject param,String campaignType);

	List<AmzAdvBudgetRules> amzCreateBudgetRules(AmzAdvProfile profile, JSONObject param,String campaignType);
	
	List<AmzAdvBudgetRules> amzUpdateBudgetRules(AmzAdvProfile profile, JSONObject param,String campaignType);

	AmzAdvBudgetRules amzGetBudgetRule(AmzAdvProfile profile, String budgetRuleId,String campaignType);

	void amzDeleteCampaignBudgetRule(AmzAdvProfile profile, String campaignId, String budgetRuleId, String campaignType);

	String amzGetCampaignByBudgetRule(AmzAdvProfile profile, String budgetRuleId, String campaignType);

	List<AmzAdvBudgetRules> amzGetBudgetRulesByCampaign(AmzAdvProfile profile, String campaignId,String campaignType);

	PageList<Map<String, Object>> list(AmzAdvProfile profile, QueryForList param);

	String amzPostBudgetRulesByCampaign(AmzAdvProfile profile, String campaignId, String ruleid,String campaignType);

	List<AmzAdvBudgetRules> getAdvBudgetRuleByProfileid(String profileid, String campaignType);

	void amzGetBudgetRules(AmzAdvProfile profile, AmzAdvBudgetRules item);

	List<Map<String, Object>> listRules(List<Map<String, Object>> list);
}
