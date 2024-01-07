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

	List<AmzAdvBudgetRules> amzGetBudgetRules(AmzAdvProfile profile, JSONObject param);

	List<AmzAdvBudgetRules> amzCreateBudgetRules(AmzAdvProfile profile, JSONObject param);
	
	List<AmzAdvBudgetRules> amzUpdateBudgetRules(AmzAdvProfile profile, JSONObject param);

	AmzAdvBudgetRules amzGetBudgetRule(AmzAdvProfile profile, String budgetRuleId);

	void amzDeleteCampaignBudgetRule(AmzAdvProfile profile, String campaignId, String budgetRuleId);

	String amzGetCampaignByBudgetRule(AmzAdvProfile profile, String budgetRuleId);

	List<AmzAdvBudgetRules> amzGetBudgetRulesByCampaign(AmzAdvProfile profile, String campaignId);

	PageList<Map<String, Object>> list(AmzAdvProfile profile, QueryForList param);
}
