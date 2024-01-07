package com.wimoor.amazon.adv.sp.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sp.dao.AmzAdvBudgetRulesMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvBudgetRules;
import com.wimoor.amazon.adv.sp.service.IAmzAdvBudgetRulesService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.util.StringUtil;
@Service("amzAdvBudgetRulesService")
public class AmzAdvBudgetRulesServiceImpl extends BaseService<AmzAdvBudgetRules> implements IAmzAdvBudgetRulesService {
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	ApiBuildService apiBuildService;
	@Resource
    AmzAdvBudgetRulesMapper amzAdvBudgetRulesMapper;
	
	@Override
	public List<AmzAdvBudgetRules> amzGetBudgetRulesByCampaign(AmzAdvProfile profile, String campaignId) {
		// TODO Auto-generated method stub
		String url = "/sp/campaigns/"+campaignId+"/budgetRules";
		String response = apiBuildService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject result = GeneralUtil.getJsonObject(response.toString());
			JSONArray associatedRules = result.getJSONArray("associatedRules");
			List<AmzAdvBudgetRules> list=new LinkedList<AmzAdvBudgetRules>();
			for(int i=0;i<associatedRules.size();i++) {
				  JSONObject item = associatedRules.getJSONObject(i);
				  AmzAdvBudgetRules budgetrule = new AmzAdvBudgetRules();
				  budgetrule.setRuleId(item.getString("ruleId"));
				  budgetrule.setProfileid(profile.getId());
				  budgetrule.setRuleState(item.getString("ruleState"));
				  budgetrule.setCreatedDate(item.getDate("createdDate"));
				  budgetrule.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				  budgetrule.setRuleStatus(item.getString("ruleStatus"));
				  JSONObject ruleDetails = item.getJSONObject("ruleDetails");
				  budgetrule.setName(ruleDetails.getString("name"));
				  budgetrule.setDuration(ruleDetails.getString("duration"));
				  budgetrule.setRecurrence(ruleDetails.getString("recurrence"));
				  budgetrule.setRuleType(ruleDetails.getString("ruleType"));
				  budgetrule.setBudgetIncreaseBy(ruleDetails.getString("budgetIncreaseBy"));
				  budgetrule.setPerformanceMeasureCondition(ruleDetails.getString("performanceMeasureCondition"));
				  AmzAdvBudgetRules old = amzAdvBudgetRulesMapper.selectByPrimaryKey(budgetrule.getRuleId());
				  if(old!=null) {
					  amzAdvBudgetRulesMapper.updateByPrimaryKey(budgetrule);
				  }else {
					  amzAdvBudgetRulesMapper.insert(budgetrule);
				  }
				  list.add(budgetrule);
			}
			return list;
		}else {
			return null; 
		}
	}
	
	@Override
	public List<AmzAdvBudgetRules> amzGetBudgetRules(AmzAdvProfile profile, JSONObject param) {
		// TODO Auto-generated method stub
		if(param.getString("pageSize")==null) {
			param.put("pageSize","30");
		}
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "pageSize");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "nextToken");
		}
		String url = "/sp/budgetRules";
		url = url + ("".equals(paramurl) ? "" : "?"+paramurl);
		String response = apiBuildService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject result = GeneralUtil.getJsonObject(response.toString());
			param.put("nextToken",result.getString("nextToken"));
			JSONArray budgetRulesForAdvertiserResponse = result.getJSONArray("budgetRulesForAdvertiserResponse");
			List<AmzAdvBudgetRules> list=new LinkedList<AmzAdvBudgetRules>();
			for(int i=0;i<budgetRulesForAdvertiserResponse.size();i++) {
				  JSONObject item = budgetRulesForAdvertiserResponse.getJSONObject(i);
				  AmzAdvBudgetRules budgetrule = new AmzAdvBudgetRules();
				  budgetrule.setRuleId(item.getString("ruleId"));
				  budgetrule.setProfileid(profile.getId());
				  budgetrule.setRuleState(item.getString("ruleState"));
				  budgetrule.setCreatedDate(item.getDate("createdDate"));
				  budgetrule.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				  budgetrule.setRuleStatus(item.getString("ruleStatus"));
				  JSONObject ruleDetails = item.getJSONObject("ruleDetails");
				  budgetrule.setName(ruleDetails.getString("name"));
				  budgetrule.setDuration(ruleDetails.getString("duration"));
				  budgetrule.setRecurrence(ruleDetails.getString("recurrence"));
				  budgetrule.setRuleType(ruleDetails.getString("ruleType"));
				  budgetrule.setBudgetIncreaseBy(ruleDetails.getString("budgetIncreaseBy"));
				  budgetrule.setPerformanceMeasureCondition(ruleDetails.getString("performanceMeasureCondition"));
				  AmzAdvBudgetRules old = amzAdvBudgetRulesMapper.selectByPrimaryKey(budgetrule.getRuleId());
				  if(old!=null) {
					  amzAdvBudgetRulesMapper.updateByPrimaryKey(budgetrule);
				  }else {
					  amzAdvBudgetRulesMapper.insert(budgetrule);
				  }
				  list.add(budgetrule);
			}
			return list;
		}else {
			return null; 
		}
	}

	@Override
	public List<AmzAdvBudgetRules> amzCreateBudgetRules(AmzAdvProfile profile, JSONObject param) {
		// TODO Auto-generated method stub
		String url = "/sp/budgetRules";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject res = GeneralUtil.getJsonObject(response.toString());
			JSONArray result = res.getJSONArray("responses");
			List<AmzAdvBudgetRules> list=new LinkedList<AmzAdvBudgetRules>();
			JSONArray budgetRulesDetails = param.getJSONArray("budgetRulesDetails");
			for(int i=0;i<result.size();i++) {
				  JSONObject resultitem = result.getJSONObject(i);
				  JSONObject item=budgetRulesDetails.getJSONObject(i);
				  AmzAdvBudgetRules budgetrule = new AmzAdvBudgetRules();
				  budgetrule.setRuleId(resultitem.getString("ruleId"));
				  budgetrule.setProfileid(profile.getId());
				  if(item.getString("ruleState")==null) {
					  budgetrule.setRuleState("ACTIVE");
				  }else {
					  budgetrule.setRuleState(item.getString("ruleState"));
				  }
				  if(item.getDate("createdDate")==null) {
					  budgetrule.setCreatedDate(new Date());
				  }else {
					  budgetrule.setCreatedDate(item.getDate("createdDate"));
				  }
				  budgetrule.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				  budgetrule.setName(item.getString("name"));
				  budgetrule.setDuration(item.getString("duration"));
				  budgetrule.setRecurrence(item.getString("recurrence"));
				  budgetrule.setRuleType(item.getString("ruleType"));
				  budgetrule.setBudgetIncreaseBy(item.getString("budgetIncreaseBy"));
				  budgetrule.setPerformanceMeasureCondition(item.getString("performanceMeasureCondition"));
				  AmzAdvBudgetRules old = amzAdvBudgetRulesMapper.selectByPrimaryKey(budgetrule.getRuleId());
				  if(old!=null) {
					  amzAdvBudgetRulesMapper.updateByPrimaryKey(budgetrule);
				  }else {
					  amzAdvBudgetRulesMapper.insert(budgetrule);
				  }
				  list.add(budgetrule);
			}
			return list;
		}else {
			return null; 
		}
	}

	@Override
	public List<AmzAdvBudgetRules> amzUpdateBudgetRules(AmzAdvProfile profile, JSONObject param) {
		// TODO Auto-generated method stub
		String url = "/sp/budgetRules";
		String response = apiBuildService.amzAdvPut(profile, url,param.toString());
		if (StringUtil.isNotEmpty(response)) {
			JSONArray result = GeneralUtil.getJsonArray(response.toString());
			List<AmzAdvBudgetRules> list=new LinkedList<AmzAdvBudgetRules>();
			JSONArray budgetRulesDetails = param.getJSONArray("budgetRulesDetails");
			for(int i=0;i<result.size();i++) {
				  JSONObject resultitem = result.getJSONObject(i);
				  JSONObject item=budgetRulesDetails.getJSONObject(i);
				  AmzAdvBudgetRules budgetrule = new AmzAdvBudgetRules();
				  budgetrule.setRuleId(resultitem.getString("ruleId"));
				  budgetrule.setProfileid(profile.getId());
				  budgetrule.setRuleState(item.getString("ruleState"));
				  budgetrule.setCreatedDate(item.getDate("createdDate"));
				  budgetrule.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				  budgetrule.setRuleStatus(item.getString("ruleStatus"));
				  JSONObject ruleDetails = item.getJSONObject("ruleDetails");
				  if(ruleDetails!=null) {
					  budgetrule.setName(ruleDetails.getString("name"));
					  budgetrule.setDuration(ruleDetails.getString("duration"));
					  budgetrule.setRecurrence(ruleDetails.getString("recurrence"));
					  budgetrule.setRuleType(ruleDetails.getString("ruleType"));
					  budgetrule.setBudgetIncreaseBy(ruleDetails.getString("budgetIncreaseBy"));
					  budgetrule.setPerformanceMeasureCondition(ruleDetails.getString("performanceMeasureCondition"));
				  }
				  AmzAdvBudgetRules old = amzAdvBudgetRulesMapper.selectByPrimaryKey(budgetrule.getRuleId());
				  if(old!=null) {
					  amzAdvBudgetRulesMapper.updateByPrimaryKey(budgetrule);
				  }else {
					  amzAdvBudgetRulesMapper.insert(budgetrule);
				  }
				  list.add(budgetrule);
			}
			return list;
		}else {
			return null; 
		}
	}
	
	
	@Override
	public AmzAdvBudgetRules amzGetBudgetRule(AmzAdvProfile profile, String budgetRuleId) {
		// TODO Auto-generated method stub
		String url = "/sp/budgetRules/"+budgetRuleId;
		String response = apiBuildService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
		  JSONObject item = GeneralUtil.getJsonObject(response.toString());
		  AmzAdvBudgetRules budgetrule = new AmzAdvBudgetRules();
		  budgetrule.setRuleId(item.getString("ruleId"));
		  budgetrule.setProfileid(profile.getId());
		  budgetrule.setRuleState(item.getString("ruleState"));
		  budgetrule.setCreatedDate(item.getDate("createdDate"));
		  budgetrule.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
		  budgetrule.setRuleStatus(item.getString("ruleStatus"));
		  JSONObject ruleDetails = item.getJSONObject("ruleDetails");
		  budgetrule.setName(ruleDetails.getString("name"));
		  budgetrule.setDuration(ruleDetails.getString("duration"));
		  budgetrule.setRecurrence(ruleDetails.getString("recurrence"));
		  budgetrule.setRuleType(ruleDetails.getString("ruleType"));
		  budgetrule.setBudgetIncreaseBy(ruleDetails.getString("budgetIncreaseBy"));
		  budgetrule.setPerformanceMeasureCondition(ruleDetails.getString("performanceMeasureCondition"));
		  AmzAdvBudgetRules old = amzAdvBudgetRulesMapper.selectByPrimaryKey(budgetrule.getRuleId());
		  if(old!=null) {
			  amzAdvBudgetRulesMapper.updateByPrimaryKey(budgetrule);
		  }else {
			  amzAdvBudgetRulesMapper.insert(budgetrule);
		  }
		  return budgetrule;
		}else {
			return null; 
		}
	}
	
	@Override
	public void amzDeleteCampaignBudgetRule(AmzAdvProfile profile,String campaignId, String budgetRuleId) {
		// TODO Auto-generated method stub
		String url = "/sp/campaigns/"+campaignId+"/budgetRules/"+budgetRuleId;
		String response = apiBuildService.amzAdvDelete(profile, url);
		if (StringUtil.isNotEmpty(response)) {
		   
		  
		}else {
			 
		}
	}
	
	@Override
	public String amzGetCampaignByBudgetRule(AmzAdvProfile profile,  String budgetRuleId) {
		// TODO Auto-generated method stub
		String url = "/sp/budgetRules/"+budgetRuleId+"/campaigns";
		String response = apiBuildService.amzAdvGet(profile, url);
		return response;
	}

	@Override
	public PageList<Map<String,Object>> list(AmzAdvProfile profile, QueryForList param) {
		if(StrUtil.isNotEmpty(param.getSearch())) {
			param.setSearch("%"+param.getSearch()+"%");
		}else {
			param.setSearch(null);
		}
		return this.amzAdvBudgetRulesMapper.list(param,param.getPageBounds());
	}
	
}
