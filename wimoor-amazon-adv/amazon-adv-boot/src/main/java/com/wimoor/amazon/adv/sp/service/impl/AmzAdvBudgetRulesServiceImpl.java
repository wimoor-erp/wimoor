package com.wimoor.amazon.adv.sp.service.impl;

import java.math.BigInteger;
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
import com.wimoor.amazon.adv.sp.dao.AmzAdvCampaignBudgetRuleMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvBudgetRules;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaignBudgetRule;
import com.wimoor.amazon.adv.sp.service.IAmzAdvBudgetRulesService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;
@Service("amzAdvBudgetRulesService")
public class AmzAdvBudgetRulesServiceImpl extends BaseService<AmzAdvBudgetRules> implements IAmzAdvBudgetRulesService {
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	ApiBuildService apiBuildService;
	@Resource
    AmzAdvBudgetRulesMapper amzAdvBudgetRulesMapper;
	@Resource
	AmzAdvCampaignBudgetRuleMapper amzAdvCampaignBudgetRuleMapper;
	
	
	@Override
	public List<AmzAdvBudgetRules> amzGetBudgetRulesByCampaign(AmzAdvProfile profile, String campaignId,String campaignType) {
		// TODO Auto-generated method stub
		String type="sp";
		if(campaignType.toUpperCase().equals("HSA") || campaignType.toUpperCase().equals("SB")) {
			type="sb";
		}
		if(campaignType.toUpperCase().equals("SD") ) {
			type="sd";
		}
		String url = "/"+type+"/campaigns/"+campaignId+"/budgetRules";
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
				  budgetrule.setCampaigntype(type);
				  budgetrule.setPerformanceMeasureCondition(ruleDetails.getString("performanceMeasureCondition"));
				  AmzAdvBudgetRules old = amzAdvBudgetRulesMapper.selectByPrimaryKey(budgetrule.getRuleId());
				  if(old!=null) {
					  budgetrule.setCreatedDate(old.getCreatedDate());
					  budgetrule.setRuleStatus(old.getRuleStatus());
					  amzAdvBudgetRulesMapper.updateByPrimaryKey(budgetrule);
				  }else {
					  amzAdvBudgetRulesMapper.insert(budgetrule);
				  }
				  saveCampaignBudgetRule(profile.getId(),item.getString("ruleId"),  campaignId) ;
				  list.add(budgetrule);
			}
			return list;
		}else {
			return null; 
		}
	}
	
	@Override
	public List<AmzAdvBudgetRules> amzGetBudgetRules(AmzAdvProfile profile, JSONObject param,String campaignType) {
		// TODO Auto-generated method stub
		String type="sp";
		if(campaignType.toUpperCase().equals("HSA") || campaignType.toUpperCase().equals("SB")) {
			type="sb";
		}
		if(campaignType.toUpperCase().equals("SD") ) {
			type="sd";
		}
		if(param.getString("pageSize")==null) {
			param.put("pageSize","30");
		}
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "pageSize");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "nextToken");
		}
		String url = "/"+type+"/budgetRules";
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
					  budgetrule.setCampaigntype(old.getCampaigntype());
					  budgetrule.setCreatedDate(old.getCreatedDate());
					  budgetrule.setCreatedDate(old.getCreatedDate());
					  budgetrule.setRuleStatus(old.getRuleStatus());
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

    private void saveCampaignBudgetRule(BigInteger profileid,String ruleid,String campaignId) {
    	Example example=new Example(AmzAdvCampaignBudgetRule.class);
    	example.createCriteria().andEqualTo("ruleId",ruleid).andEqualTo("campaignid", campaignId);
    	int count = amzAdvCampaignBudgetRuleMapper.selectCountByExample(example);
    	if(count==0) {
    		AmzAdvCampaignBudgetRule campaignRule=new AmzAdvCampaignBudgetRule();
			campaignRule.setCampaignid(new BigInteger(campaignId));
			campaignRule.setProfileid(profileid);
			campaignRule.setRuleId(ruleid);
    		amzAdvCampaignBudgetRuleMapper.insert(campaignRule);
    	}
    }
	@Override
	public List<AmzAdvBudgetRules> amzCreateBudgetRules(AmzAdvProfile profile, JSONObject param,String campaignType) {
		// TODO Auto-generated method stub
		String type="sp";
		if(campaignType.toUpperCase().equals("HSA") || campaignType.toUpperCase().equals("SB")) {
			type="sb";
		}
		if(campaignType.toUpperCase().equals("SD") ) {
			type="sd";
		}
		String url = "/"+type+"/budgetRules";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject res = GeneralUtil.getJsonObject(response.toString());
			JSONArray result = res.getJSONArray("responses");
			List<AmzAdvBudgetRules> list=new LinkedList<AmzAdvBudgetRules>();
			JSONArray budgetRulesDetails = param.getJSONArray("budgetRulesDetails");
			for(int i=0;i<result.size();i++) {
				  JSONObject resultitem = result.getJSONObject(i);
				  JSONObject item=budgetRulesDetails.getJSONObject(i);
				  if(!resultitem.getString("code").equals("Ok")) {
					  throw new BizException("亚马逊错误:"+resultitem.getString("details"));
				  }
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
				  budgetrule.setCampaigntype(type);
				  AmzAdvBudgetRules old = amzAdvBudgetRulesMapper.selectByPrimaryKey(budgetrule.getRuleId());
				  if(old!=null) {
					  budgetrule.setCreatedDate(old.getCreatedDate());
					  budgetrule.setRuleStatus(old.getRuleStatus());
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
	public List<AmzAdvBudgetRules> amzUpdateBudgetRules(AmzAdvProfile profile, JSONObject param,String campaignType) {
		// TODO Auto-generated method stub
		String type="sp";
		if(campaignType.toUpperCase().equals("HSA") || campaignType.toUpperCase().equals("SB")) {
			type="sb";
		}
		if(campaignType.toUpperCase().equals("SD") ) {
			type="sd";
		}
		String url = "/"+type+"/budgetRules";
		String response = apiBuildService.amzAdvPut(profile, url,param.toString());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject res = GeneralUtil.getJsonObject(response.toString());
			JSONArray result = res.getJSONArray("responses");
			List<AmzAdvBudgetRules> list=new LinkedList<AmzAdvBudgetRules>();
			JSONArray budgetRulesDetails = param.getJSONArray("budgetRulesDetails");
			for(int i=0;i<result.size();i++) {
				  JSONObject resultitem = result.getJSONObject(i);
				  JSONObject item=budgetRulesDetails.getJSONObject(i);
				  if(!resultitem.getString("code").equals("Ok")) {
					  continue;
				  }
				  AmzAdvBudgetRules budgetrule = new AmzAdvBudgetRules();
				  budgetrule.setRuleId(resultitem.getString("ruleId"));
				  budgetrule.setProfileid(profile.getId());
				  if(item.getString("ruleState")==null) {
					  budgetrule.setRuleState("ACTIVE");
				  }else {
					  budgetrule.setRuleState(item.getString("ruleState"));
				  }
				  if(item.getDate("lastUpdatedDate")==null) {
					  budgetrule.setLastUpdatedDate(new Date());
				  }else {
					  budgetrule.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				  }
				  JSONObject ruleDetails=item.getJSONObject("ruleDetails");
				  budgetrule.setName(ruleDetails.getString("name"));
				  budgetrule.setDuration(ruleDetails.getString("duration"));
				  budgetrule.setRecurrence(ruleDetails.getString("recurrence"));
				  budgetrule.setRuleType(ruleDetails.getString("ruleType"));
				  budgetrule.setBudgetIncreaseBy(ruleDetails.getString("budgetIncreaseBy"));
				  budgetrule.setPerformanceMeasureCondition(ruleDetails.getString("performanceMeasureCondition"));
				  budgetrule.setCampaigntype(type);
				  AmzAdvBudgetRules old = amzAdvBudgetRulesMapper.selectByPrimaryKey(budgetrule.getRuleId());
				  if(old!=null) {
					  budgetrule.setCreatedDate(old.getCreatedDate());
					  budgetrule.setRuleStatus(old.getRuleStatus());
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
	public AmzAdvBudgetRules amzGetBudgetRule(AmzAdvProfile profile, String budgetRuleId,String campaignType) {
		// TODO Auto-generated method stub
		String type="sp";
		if(campaignType.toUpperCase().equals("HSA") || campaignType.toUpperCase().equals("SB")) {
			type="sb";
		}
		if(campaignType.toUpperCase().equals("SD") ) {
			type="sd";
		}
		String url = "/"+type+"/budgetRules/"+budgetRuleId;
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
		  budgetrule.setCampaigntype(type);
		  AmzAdvBudgetRules old = amzAdvBudgetRulesMapper.selectByPrimaryKey(budgetrule.getRuleId());
		  if(old!=null) {
			  budgetrule.setCreatedDate(old.getCreatedDate());
			  budgetrule.setRuleStatus(old.getRuleStatus());
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
	public void amzDeleteCampaignBudgetRule(AmzAdvProfile profile,String campaignId, String budgetRuleId,String campaignType) {
		String type="sp";
		if(campaignType.toUpperCase().equals("HSA") || campaignType.toUpperCase().equals("SB")) {
			type="sb";
		}
		if(campaignType.toUpperCase().equals("SD") ) {
			type="sd";
		}
		String url = "/"+type+"/campaigns/"+campaignId+"/budgetRules/"+budgetRuleId;
		String response = apiBuildService.amzAdvDelete(profile, url);
		if (StringUtil.isNotEmpty(response)) {
		   if(response.contains("Ok")) {
			   //解绑关系
			   Example example=new Example(AmzAdvCampaignBudgetRule.class);
			   Criteria crit = example.createCriteria();
			   crit.andEqualTo("ruleId",budgetRuleId);
			   crit.andEqualTo("campaignid",campaignId);
			   amzAdvCampaignBudgetRuleMapper.deleteByExample(example);
		   }
		}else {
			  
		}
	}
	
	@Override
	public String amzGetCampaignByBudgetRule(AmzAdvProfile profile,  String budgetRuleId,String campaignType) {
		String type="sp";
		if(campaignType.toUpperCase().equals("HSA") || campaignType.toUpperCase().equals("SB")) {
			type="sb";
		}
		if(campaignType.toUpperCase().equals("SD") ) {
			type="sd";
		}
		String url = "/"+type+"/budgetRules/"+budgetRuleId+"/campaigns?pageSize=30";
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
		if(StrUtil.isNotEmpty(param.getCampaignType())) {
			param.setCampaignType( param.getCampaignType() );
		}else {
			param.setCampaignType(null);
		}
		return this.amzAdvBudgetRulesMapper.list(param,param.getPageBounds());
	}

	@Override
	public String amzPostBudgetRulesByCampaign(AmzAdvProfile profile, String campaignId, String ruleid,String campaignType) {
		String type="sp";
		if(campaignType.toUpperCase().equals("HSA") || campaignType.toUpperCase().equals("SB")) {
			type="sb";
		}
		if(campaignType.toUpperCase().equals("SD") ) {
			type="sd";
		}
		String url = "/"+type+"/campaigns/"+campaignId+"/budgetRules";
		JSONObject param=new JSONObject();
		String[] rules={ruleid};
		param.put("budgetRuleIds", rules);
		String response = apiBuildService.amzAdvPost(profile, url,param.toString());
		if(response.contains("Ok")) {
			AmzAdvCampaignBudgetRule campaignRule=new AmzAdvCampaignBudgetRule();
			campaignRule.setCampaignid(new BigInteger(campaignId));
			campaignRule.setProfileid(profile.getId());
			campaignRule.setRuleId(ruleid);
			amzAdvCampaignBudgetRuleMapper.insert(campaignRule);
			return campaignRule.getRuleId();
		}else {
			return null;
		}
		
	}

	@Override
	public List<AmzAdvBudgetRules> getAdvBudgetRuleByProfileid(String profileid,String campaignType) {
		String type="sp";
		if(campaignType.toUpperCase().equals("HSA") || campaignType.toUpperCase().equals("SB")) {
			type="sb";
		}
		if(campaignType.toUpperCase().equals("SD") ) {
			type="sd";
		}
		Example example=new Example(AmzAdvBudgetRules.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("profileid", new BigInteger(profileid));
		crit.andEqualTo("campaigntype",type);
		return amzAdvBudgetRulesMapper.selectByExample(example);
	}

	@Override
	public void amzGetBudgetRules(AmzAdvProfile profile, AmzAdvBudgetRules item) {
		if(item==null) {
			return ;
		}else {
			if(item.getCampaigntype()==null) {
				item.setCampaigntype("sp");
			}
		}
		String response = amzGetCampaignByBudgetRule(profile,item.getRuleId(),item.getCampaigntype());
		JSONObject result = GeneralUtil.getJsonObject(response);
		JSONArray list = result.getJSONArray("associatedCampaigns");
		for(int i =0;i<list.size();i++) {
			JSONObject ruleitem = list.getJSONObject(i);
			String campaignId=ruleitem.getString("campaignId");
			String ruleid=item.getRuleId();
			this.saveCampaignBudgetRule(profile.getId(), ruleid, campaignId);
		}
	}

	@Override
	public List<Map<String, Object>> listRules(List<Map<String, Object>> mylist) {
		List<Map<String, Object>> list=this.amzAdvCampaignBudgetRuleMapper.listRules(mylist);
		return list;
	}
	
}
