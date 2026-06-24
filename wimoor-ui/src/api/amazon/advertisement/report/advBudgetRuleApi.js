import request from "@/utils/request";

function budgetRulesRecommendation(profileid,type,data){
	return request.post('/amazonadv/api/v1/campaign/budgetRules/budgetRulesRecommendation/'+profileid+"/"+type,data);
}

function amzCreateBudgetRules(profileid,type,data){
	return request.post('/amazonadv/api/v1/campaign/budgetRules/amzCreateBudgetRules/'+profileid+"/"+type,data);
}

function initialBudgetRecommendation(profileid,type,data){
	return request.post('/amazonadv/api/v1/advCampaignManager/initialBudgetRecommendation/'+profileid+"/"+type,data);
}
function list(profileid,type,data){
	return request.post('/amazonadv/api/v1/campaign/budgetRules/list/'+profileid+"/"+type,data);
}
function amzPostBudgetRulesByCampaign(profileid,type,data){
	return request.post('/amazonadv/api/v1/campaign/budgetRules/amzPostBudgetRulesByCampaign/'+profileid+"/"+type,data);
}
function amzGetBudgetRulesByCampaign(profileid,type,data){
	return request.get('/amazonadv/api/v1/campaign/budgetRules/amzGetBudgetRulesByCampaign/'+profileid+"/"+type,{params:data});
}
function getAdvBudgetRuleByProfileid(profileid,type){
	return request.get('/amazonadv/api/v1/campaign/budgetRules/getAdvBudgetRuleByProfileid/'+profileid+"/"+type);
}
function amzDeleteCampaignBudgetRule(profileid,type,data){
	return request.get('/amazonadv/api/v1/campaign/budgetRules/amzDeleteCampaignBudgetRule/'+profileid+"/"+type,{params:data});
}
function amzUpdateBudgetRules(profileid,type,data){
	return request.post('/amazonadv/api/v1/campaign/budgetRules/amzUpdateBudgetRules/'+profileid+"/"+type,data);
}





export default{
	budgetRulesRecommendation,amzCreateBudgetRules,initialBudgetRecommendation,list,
	amzPostBudgetRulesByCampaign,amzGetBudgetRulesByCampaign,getAdvBudgetRuleByProfileid,
	amzDeleteCampaignBudgetRule,amzUpdateBudgetRules,
}