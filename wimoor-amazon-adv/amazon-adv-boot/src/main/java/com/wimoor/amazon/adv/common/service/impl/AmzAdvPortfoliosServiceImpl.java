package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.dao.AmzAdvPortfoliosMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvPortfolios;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvPortfoliosService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvPortfoliosService")
public class AmzAdvPortfoliosServiceImpl extends BaseService<AmzAdvPortfolios> implements IAmzAdvPortfoliosService {

	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvPortfoliosMapper amzAdvPortfoliosMapper;
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	@Resource
	@Lazy
	IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
	@Resource
    ApiBuildService apiBuildService;
	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	
	public List<AmzAdvPortfolios> getPortfoliosForProfileId(Map<String, Object> param) {
		 
		return amzAdvPortfoliosMapper.getPortfoliosForProfileId(param);
	}
	

	public List<AmzAdvPortfolios> amzGetListPortfolios(UserInfo user, BigInteger profileId, Map<String, Object> portfoliosParam) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/portfolios?";
		String param = "";
		if (portfoliosParam != null) {
			param = GeneralUtil.addParamToUrl(param, portfoliosParam, "portfolioIdFilter");
			param = GeneralUtil.addParamToUrl(param, portfoliosParam, "portfolioNameFilter");
			param = GeneralUtil.addParamToUrl(param, portfoliosParam, "portfolioStateFilter");
			url = url + ("".equals(param) ? "" : param);
		}
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			List<AmzAdvPortfolios> list = new ArrayList<AmzAdvPortfolios>();
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			if(a != null && a.size() > 0) {
				for (int i = 0; i < a.size(); i++) {
					JSONObject item = a.getJSONObject(i);
					if(item == null) continue;
					AmzAdvPortfolios portfolios = new AmzAdvPortfolios();
					portfolios.setId(item.getBigInteger("portfolioId"));
					portfolios.setProfileid(profileId);
					portfolios.setName(item.getString("name"));
					JSONObject budget = item.getJSONObject("budget");
					if(budget != null) {
						portfolios.setAmount(budget.getBigDecimal("amount"));
						portfolios.setCurrencycode(budget.getString("currencyCode"));
						portfolios.setPolicy(budget.getString("policy"));
						portfolios.setStartdate(budget.getDate("startDate"));
						portfolios.setEnddate(budget.getDate("endDate"));
					}
					portfolios.setInbudget(item.getBoolean("inBudget"));
					portfolios.setState(item.getString("state"));
					portfolios.setOpttime(new Date());
					list.add(portfolios);
				}
				if(list.size() > 0) {
					amzAdvPortfoliosMapper.insertBatch(list);
				}
			}
			return list;
		}
		return null;
	}

	public List<AmzAdvPortfolios> amzGetListPortfoliosEx(UserInfo user, BigInteger profileId, Map<String, Object> portfoliosParam) {
		if (portfoliosParam == null)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/portfolios/extended?";
		String param = "";
		if (portfoliosParam != null) {
			param = GeneralUtil.addParamToUrl(param, portfoliosParam, "portfolioIdFilter");
			param = GeneralUtil.addParamToUrl(param, portfoliosParam, "portfolioNameFilter");
			param = GeneralUtil.addParamToUrl(param, portfoliosParam, "portfolioStateFilter");
			url = url + ("".equals(param) ? "" : param);
		}
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		List<AmzAdvPortfolios> list = new ArrayList<AmzAdvPortfolios>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			if(a != null && a.size() > 0) {
				for (int i = 0; i < a.size(); i++) {
					JSONObject item = a.getJSONObject(i);
					if(item == null) continue;
					AmzAdvPortfolios portfolios = new AmzAdvPortfolios();
					portfolios.setId(item.getBigInteger("portfolioId"));
					portfolios.setProfileid(profileId);
					portfolios.setName(item.getString("name"));
					JSONObject budget = item.getJSONObject("budget");
					if(budget != null) {
						portfolios.setAmount(budget.getBigDecimal("amount"));
						portfolios.setCurrencycode(budget.getString("currencyCode"));
						portfolios.setPolicy(budget.getString("policy"));
						portfolios.setStartdate(budget.getDate("startDate"));
						portfolios.setEnddate(budget.getDate("endDate"));
					}
					portfolios.setInbudget(item.getBoolean("inBudget"));
					portfolios.setState(item.getString("state"));
					portfolios.setCreationDate(item.getDate("creationDate"));
					portfolios.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
					portfolios.setServingStatus(item.getString("servingStatus"));
					portfolios.setOpttime(new Date());
				}
				if(list.size() > 0) {
					amzAdvPortfoliosMapper.insertBatch(list);
				}
			}
			return list;
		}
		return null;
	}

	public AmzAdvPortfolios amzGetPortfolios(UserInfo user, BigInteger profileId, String id) {
		if (StringUtil.isEmpty(id)) return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/portfolios/" + id;
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			if(item == null) return null;
			JSONObject budget = item.getJSONObject("budget");
			AmzAdvPortfolios portfolios = new AmzAdvPortfolios();
			portfolios.setId(item.getBigInteger("portfolioId"));
			portfolios.setProfileid(profileId);
			portfolios.setName(item.getString("name"));
			if(budget != null) {
				portfolios.setAmount(budget.getBigDecimal("amount"));
				portfolios.setCurrencycode(budget.getString("currencyCode"));
				portfolios.setPolicy(budget.getString("policy"));
				portfolios.setStartdate(budget.getDate("startDate"));
				portfolios.setEnddate(budget.getDate("endDate"));
			}
			portfolios.setInbudget(item.getBoolean("inBudget"));
			portfolios.setState(item.getString("state"));
			portfolios.setOpttime(new Date());
			Example example = new Example(AmzAdvPortfolios.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("id", item.getString("portfolioId"));
			crit.andEqualTo("profileid", profileId);
			AmzAdvPortfolios oldportfolios = amzAdvPortfoliosMapper.selectOneByExample(example);
			if (oldportfolios != null) {
				this.updateAll(portfolios);
			} else {
				this.save(portfolios);
			}
			return portfolios;
		}
		return null;
	}
	
	public AmzAdvPortfolios amzGetPortfoliosEx(UserInfo user, BigInteger profileId, String id) {
		if (StringUtil.isEmpty(id)) return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/portfolios/extended/" + id;
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			if(item == null) return null;
			JSONObject budget = item.getJSONObject("budget");
			AmzAdvPortfolios portfolios = new AmzAdvPortfolios();
			portfolios.setId(item.getBigInteger("portfolioId"));
			portfolios.setProfileid(profileId);
			portfolios.setName(item.getString("name"));
			if(budget != null) {
				portfolios.setAmount(budget.getBigDecimal("amount"));
				portfolios.setCurrencycode(budget.getString("currencyCode"));
				portfolios.setPolicy(budget.getString("policy"));
				portfolios.setStartdate(budget.getDate("startDate"));
				portfolios.setEnddate(budget.getDate("endDate"));
			}
			portfolios.setInbudget(item.getBoolean("inBudget"));
			portfolios.setState(item.getString("state"));
			portfolios.setCreationDate(item.getDate("creationDate"));
			portfolios.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
			portfolios.setServingStatus(item.getString("servingStatus"));
			portfolios.setOpttime(new Date());
			Example example = new Example(AmzAdvPortfolios.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("id", item.getString("portfolioId"));
			crit.andEqualTo("profileid", profileId);
			AmzAdvPortfolios oldportfolios = amzAdvPortfoliosMapper.selectOneByExample(example);
			if (oldportfolios != null) {
				this.updateAll(portfolios);
			} else {
				this.save(portfolios);
			}
			return portfolios;
		}
		return null;
	}
	
	public List<AmzAdvPortfolios> amzCreatePortfolios(UserInfo user,BigInteger  profileId, List<AmzAdvPortfolios> portfoliosList) {
		if (portfoliosList == null || portfoliosList.size() <= 0) return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/portfolios";
		JSONArray array = new JSONArray();
		for (int i = 0; i < portfoliosList.size(); i++) {
			AmzAdvPortfolios portfolios = portfoliosList.get(i);
			JSONObject param = new JSONObject();
			JSONObject budget = new JSONObject();
			param.put("name", portfolios.getName());
			param.put("state", portfolios.getState());
			if(portfolios.getInbudget()) {
				budget.put("currencyCode", profile.getCurrencycode());
				portfolios.setCurrencycode(profile.getCurrencycode());
				param.put("budget", budget);
				param.put("inBudget", true);
			    budget.put("policy", portfolios.getPolicy());
				if(portfolios.getPolicy().equals("dateRange")) {
					budget.put("startDate", fmt.format(portfolios.getStartdate()));
					budget.put("endDate", fmt.format(portfolios.getEnddate()));
				}
				budget.put("amount", portfolios.getAmount());
			}else {
				param.put("inBudget", false);
			}
			array.add(param);
		}
		String response = apiBuildService.amzAdvPost_V2(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < portfoliosList.size(); i++) {
				AmzAdvPortfolios portfolios = portfoliosList.get(i);
				JSONObject item = a.getJSONObject(i);
				if(item == null) continue;
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger id = item.getBigInteger("portfolioId");
					portfolios.setId(id);
					portfolios.setProfileid(profileId);
					portfolios.setOpttime(new Date());
					AmzAdvPortfolios Dbportfolios = this.selectByKey(portfolios);
					if(Dbportfolios == null) {
						this.save(portfolios);
					}
				}else {
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告活动创建失败："+errormsg);
					exception.setCode("ERROR");
					throw exception;
				}
			}
			return portfoliosList;
		}
		return null;
	}
	
	public List<AmzAdvPortfolios> amzUpdatePortfolios(UserInfo user,BigInteger  profileId,  List<AmzAdvPortfolios> portfoliosList) {
		if (portfoliosList == null || portfoliosList.size() <= 0) return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/portfolios";
		JSONArray array = new JSONArray();
		Map<BigInteger,AmzAdvPortfolios> map=new HashMap<BigInteger,AmzAdvPortfolios>();
		for (int i = 0; i < portfoliosList.size(); i++) {
			AmzAdvPortfolios portfolios = portfoliosList.get(i);
			portfolios.setCurrencycode(profile.getCurrencycode());
			JSONObject param = new JSONObject();
			JSONObject budget = new JSONObject();
			param.put("name", portfolios.getName());
			param.put("state", portfolios.getState());
			param.put("portfolioId", portfolios.getId());
			if(portfolios.getInbudget()) {
				budget.put("amount", portfolios.getAmount());
				budget.put("currencyCode", profile.getCurrencycode());
			    budget.put("policy", portfolios.getPolicy());
				if(portfolios.getPolicy().equals("dateRange")) {
					budget.put("startDate", fmt.format(portfolios.getStartdate()));
					budget.put("endDate", fmt.format(portfolios.getEnddate()));
				}
				param.put("inBudget", true);
				param.put("budget", budget);
			}else {
				param.put("inBudget", false);
			}
			array.add(param);
			map.put(portfolios.getId(), portfolios);
		}
		String response = apiBuildService.amzAdvPut_V2(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < portfoliosList.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				if(item == null) continue;
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger id = item.getBigInteger("portfolioId");
					AmzAdvPortfolios portfolios=map.get(id);
					portfolios.setId(id);
					portfolios.setProfileid(profileId);
					portfolios.setOpttime(new Date());
					amzAdvPortfoliosMapper.updateByPrimaryKeySelective(portfolios);
				} else {
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告活动修改失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			return portfoliosList;
		}
		return null;
	}


}
