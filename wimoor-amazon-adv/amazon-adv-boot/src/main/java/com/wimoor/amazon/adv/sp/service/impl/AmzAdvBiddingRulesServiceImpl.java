package com.wimoor.amazon.adv.sp.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvBiddingRulesMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvBiddingRules;
import com.wimoor.amazon.adv.sp.service.IAmzAdvBiddingRulesService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvBiddingRulesService")
public class AmzAdvBiddingRulesServiceImpl extends BaseService<AmzAdvBiddingRules> implements  IAmzAdvBiddingRulesService {
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	ApiBuildService apiBuildService;
	@Resource
    AmzAdvBiddingRulesMapper amzAdvBiddingRulesMapper;
	
	public String amzGetBiddingRulesByCampaign(AmzAdvProfile profile, String campaignId) {
		// TODO Auto-generated method stub
		String url = "/sp/campaigns/"+campaignId+"/optimizationRules";
		String response = apiBuildService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
		    return response;
		}else {
			return null; 
		}
	}
	  public Map<String,String> getHeaderExt(){
	    	Map<String,String> header=new HashMap<String,String>();
	    	header.put("Content-Type", "application/vnd.spoptimizationrules.v1+json");
	    	header.put("Accept",       "application/vnd.spoptimizationrules.v1+json");
	    	return header;
	    }
	  
	  public List<AmzAdvBiddingRules> amzSearchBiddingRules(AmzAdvProfile profile, JSONObject param) {
			// TODO Auto-generated method stub
			String url = "/sp/rules/optimization/search";
			String response = apiBuildService.amzAdvPost(profile, url,param.toString(),getHeaderExt());
			if (StringUtil.isNotEmpty(response)) {
				JSONObject result = GeneralUtil.getJsonObject(response.toString());
				JSONArray optimizationRule=result.getJSONArray("optimizationRule");
				List<AmzAdvBiddingRules> list=new LinkedList<AmzAdvBiddingRules>();
				for(int i=0;i<optimizationRule.size();i++) {
					  JSONObject item = optimizationRule.getJSONObject(i);
					  AmzAdvBiddingRules biddingrule = new AmzAdvBiddingRules();
					  biddingrule.setOptimizationRuleId(item.getString("optimizationRuleId"));
					  biddingrule.setProfileid(profile.getId());
					  biddingrule.setStatus(item.getString("status"));
					  biddingrule.setConditions(item.getString("conditions"));
					  biddingrule.setAction(item.getString("action"));
					  biddingrule.setRuleName(item.getString("ruleName"));
					  biddingrule.setRuleSubCategory(item.getString("ruleSubCategory"));
					  biddingrule.setRuleCategory(item.getString("ruleCategory"));
					  biddingrule.setRecurrence(item.getString("recurrence"));
					  AmzAdvBiddingRules  old = amzAdvBiddingRulesMapper.selectByPrimaryKey(biddingrule.getOptimizationRuleId());
					  if(old!=null) {
						  amzAdvBiddingRulesMapper.updateByPrimaryKey(biddingrule);
					  }else {
						  amzAdvBiddingRulesMapper.insert(biddingrule);
					  }
					  list.add(biddingrule);
				}
				return list;
			}else {
				return null; 
			}
		}
	  
	public List<AmzAdvBiddingRules> amzCreateBiddingRules(AmzAdvProfile profile, JSONObject param) {
		// TODO Auto-generated method stub
		String url = "/sp/rules/optimization";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			JSONArray result = GeneralUtil.getJsonArray(response.toString());
			List<AmzAdvBiddingRules> list=new LinkedList<AmzAdvBiddingRules>();
			for(int i=0;i<result.size();i++) {
				  JSONObject resultitem = result.getJSONObject(i);
				  JSONObject optimizationRule = resultitem.getJSONObject("optimizationRule");
				  AmzAdvBiddingRules biddingrule = new AmzAdvBiddingRules();
				  biddingrule.setOptimizationRuleId(optimizationRule.getString("optimizationRuleId"));
				  biddingrule.setProfileid(profile.getId());
				  biddingrule.setStatus(optimizationRule.getString("status"));
				  biddingrule.setConditions(optimizationRule.getString("conditions"));
				  biddingrule.setAction(optimizationRule.getString("action"));
				  biddingrule.setRuleName(optimizationRule.getString("ruleName"));
				  biddingrule.setRuleSubCategory(optimizationRule.getString("ruleSubCategory"));
				  biddingrule.setRuleCategory(optimizationRule.getString("ruleCategory"));
				  biddingrule.setRecurrence(optimizationRule.getString("recurrence"));
				  AmzAdvBiddingRules  old = amzAdvBiddingRulesMapper.selectByPrimaryKey(biddingrule.getOptimizationRuleId());
				  if(old!=null) {
					  amzAdvBiddingRulesMapper.updateByPrimaryKey(biddingrule);
				  }else {
					  amzAdvBiddingRulesMapper.insert(biddingrule);
				  }
				  list.add(biddingrule);
			}
			return list;
		}else {
			return null; 
		}
	}

	public List<AmzAdvBiddingRules> amzUpdateBiddingRules(AmzAdvProfile profile, JSONObject param) {
		// TODO Auto-generated method stub
		String url = "/sp/rules/optimization";
		String response = apiBuildService.amzAdvPut(profile, url,param.toString(),getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			JSONArray result = GeneralUtil.getJsonArray(response.toString());
			List<AmzAdvBiddingRules> list=new LinkedList<AmzAdvBiddingRules>();
			for(int i=0;i<result.size();i++) {
				  JSONObject item = result.getJSONObject(i);
				  JSONObject optimizationRule = item.getJSONObject("optimizationRule");
				  AmzAdvBiddingRules biddingrule = new AmzAdvBiddingRules();
				  biddingrule.setOptimizationRuleId(optimizationRule.getString("optimizationRuleId"));
				  biddingrule.setProfileid(profile.getId());
				  biddingrule.setStatus(optimizationRule.getString("status"));
				  biddingrule.setConditions(optimizationRule.getString("conditions"));
				  biddingrule.setAction(optimizationRule.getString("action"));
				  biddingrule.setRuleName(optimizationRule.getString("ruleName"));
				  biddingrule.setRuleSubCategory(optimizationRule.getString("ruleSubCategory"));
				  biddingrule.setRuleCategory(optimizationRule.getString("ruleCategory"));
				  biddingrule.setRecurrence(optimizationRule.getString("recurrence"));
				  AmzAdvBiddingRules  old = amzAdvBiddingRulesMapper.selectByPrimaryKey(biddingrule.getOptimizationRuleId());
				  if(old!=null) {
					  amzAdvBiddingRulesMapper.updateByPrimaryKey(biddingrule);
				  }else {
					  amzAdvBiddingRulesMapper.insert(biddingrule);
				  }
				  list.add(biddingrule);
			}
			return list;
		}else {
			return null; 
		}
	}
	

	
}
