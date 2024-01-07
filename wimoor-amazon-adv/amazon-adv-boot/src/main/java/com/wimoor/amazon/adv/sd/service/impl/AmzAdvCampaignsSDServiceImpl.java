package com.wimoor.amazon.adv.sd.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AdvState;
import com.wimoor.amazon.adv.common.pojo.AmzAdvOperateLog;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sd.dao.AmzAdvCampaignsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvAdgroupsSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvCampaignsSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductAdsSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeNegativaSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.service.IAmzAdvAdGroupService;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl.CampaignType;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvCampaignsSDService")
public class AmzAdvCampaignsSDServiceImpl  extends BaseService<AmzAdvCampaignsSD> implements IAmzAdvCampaignsSDService {
	@Resource
	AmzAdvCampaignsSDMapper amzAdvCampaignsSDMapper;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	@Resource
	IAmzAdvAdgroupsSDService amzAdvAdgroupsSDService;
	@Resource
	IAmzAdvProductAdsSDService amzAdvProductAdsSDService;
	@Resource
	IAmzAdvProductTargeSDService amzAdvProductTargeSDService;
	@Resource
	IAmzAdvProductTargeNegativaSDService amzAdvProductTargeNegativaSDService;
	@Resource
	ApiBuildService apiBuildService;
	@Resource
	IAmzAdvAdGroupService amzAdvAdGroupService;
	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	
	public PageList<Map<String, Object>> getCampaignList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		return amzAdvCampaignsSDMapper.getCampaignList(map,pageBounds);
	}
	
	public AmzAdvCampaignsSD amzGetCampaigns(UserInfo user, BigInteger profileId, String campaignId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + CampaignType.sd + "/campaigns/extended/" + campaignId;
		String response = apiBuildService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			AmzAdvCampaignsSD campaigns = new AmzAdvCampaignsSD();
			campaigns.setCampaignid(item.getBigInteger("campaignId"));
			campaigns.setName(item.getString("name"));
			campaigns.setBudgettype(item.getString("budgetType"));
			campaigns.setState(item.getString("state"));
			campaigns.setBudget(item.getBigDecimal("budget"));
			campaigns.setTactic(item.getString("tactic"));
			campaigns.setCosttype(item.getString("costType"));
			
			try {
				if (item.getString("endDate") != null) {
					campaigns.setEndDate(fmt.parse(item.getString("endDate")));
				}
				if (item.getString("startDate") != null) {
					campaigns.setStartDate(fmt.parse(item.getString("startDate")));
				}
				if(item.getString("creationDate") != null) {
					campaigns.setCreationDate(fmt.parse(item.getString("creationDate")));
				}
				if(item.getString("lastUpdatedDate") != null) {
					campaigns.setLastUpdatedDate(fmt.parse(item.getString("lastUpdatedDate")));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			campaigns.setServingStatus(item.getString("servingStatus"));
			campaigns.setProfileid(profileId);
			campaigns.setOpttime(new Date());
			Example example = new Example(AmzAdvCampaigns.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaigns.getCampaignid());
			AmzAdvCampaignsSD oldcampaigns = amzAdvCampaignsSDMapper.selectOneByExample(example);
			if (oldcampaigns != null) {
				this.updateAll(campaigns);
			} else {
				this.save(campaigns);
			}
			return campaigns;
		}
		return null;
	}
	
	public List<AmzAdvCampaignsSD> amzListCampaignsEx(UserInfo user, BigInteger profileId, Map<String, Object> campaignsParam) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/campaigns/extende?";
		String param="";
		if (campaignsParam != null) {
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "startIndex");
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "count");
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "stateFilter");
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "name");
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "campaignIdFilter");
		}
		url = url + ("".equals(param) ? "" : param);
		String response = apiBuildService.amzAdvGet(profile, url);
		List<AmzAdvCampaignsSD> list = new LinkedList<AmzAdvCampaignsSD>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvCampaignsSD campaigns = new AmzAdvCampaignsSD();
				campaigns.setCampaignid(item.getBigInteger("campaignId"));
				campaigns.setName(item.getString("name"));
				campaigns.setBudget(item.getBigDecimal("budget"));
				campaigns.setBudgettype(item.getString("budgettype"));
				campaigns.setTactic(item.getString("tactic"));
				campaigns.setState(item.getString("state"));
				try {
					if (item.getString("endDate") != null) {
						campaigns.setEndDate(fmt.parse(item.getString("endDate")));
					}
					if (item.getString("startDate") != null) {
						campaigns.setStartDate(fmt.parse(item.getString("startDate")));
					}
					if(item.getString("creationDate") != null) {
						campaigns.setCreationDate(fmt.parse(item.getString("creationDate")));
					}
					if(item.getString("lastUpdatedDate") != null) {
						campaigns.setLastUpdatedDate(fmt.parse(item.getString("lastUpdatedDate")));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				campaigns.setServingStatus(item.getString("servingStatus"));
				campaigns.setProfileid(profileId);
				campaigns.setOpttime(new Date());
				Example example = new Example(AmzAdvCampaigns.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("campaignid", campaigns.getCampaignid());
				AmzAdvCampaignsSD oldcampaigns = amzAdvCampaignsSDMapper.selectOneByExample(example);
				if (oldcampaigns != null) {
					this.updateAll(campaigns);
				} else {
					this.save(campaigns);
				}
				list.add(campaigns);
			}
			return list;
		}
		return null;
	}
	public AmzAdvCampaignsSD amzArchiveSpCampaigns(UserInfo user, BigInteger profileId, String campaignId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + CampaignType.sd + "/campaigns/" + campaignId;
		String response = apiBuildService.amzAdvDelete(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			Example example = new Example(AmzAdvCampaignsSD.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaignId);
			AmzAdvCampaignsSD oldCampaign = amzAdvCampaignsSDMapper.selectOneByExample(example);
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example2 = new Example(AmzAdvCampaigns.class);
				Criteria crit2 = example2.createCriteria();
				crit2.andEqualTo("profileid", profileId);
				crit2.andEqualTo("campaignid", campaignId);
				AmzAdvCampaignsSD campaign = amzAdvCampaignsSDMapper.selectOneByExample(example2);
				campaign.setState(AdvState.archived);
				campaign.setOpttime(new Date());
				amzAdvCampaignsSDMapper.updateByPrimaryKey(campaign);
				amzAdvOperateLogService.saveOperateLog("AmzAdvCampaignsSD", user.getId(), profileId, campaign, oldCampaign);
				return campaign;
			} else {
				String errormsg = "";
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + a.getString("description");
				BaseException exception = new BaseException("广告组修改失败：" + errormsg);
				exception.setCode("ERROER");
				throw exception;
			}
		}
		return null;
	}
	public List<AmzAdvCampaignsSD> amzCreateCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaignsSD> campaignsList) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/campaigns";
		JSONArray array = new JSONArray();
		if (campaignsList == null || campaignsList.size() <= 0)
			throw new BaseException("输入参数有误！");
		for (int i = 0; i < campaignsList.size(); i++) {
			AmzAdvCampaignsSD campaigns = campaignsList.get(i);
			JSONObject param = new JSONObject();
			param.put("name", campaigns.getName());
			param.put("tactic", campaigns.getTactic());
			param.put("budgetType", campaigns.getBudgettype());
			param.put("costType", campaigns.getCosttype());
			param.put("budget", campaigns.getBudget());
			param.put("state", campaigns.getState());
			param.put("startDate", fmt.format(campaigns.getStartDate()));
			if (campaigns.getEndDate() != null) {
				param.put("endDate", fmt.format(campaigns.getEndDate()));
			}
			array.add(param);
		}
		String response = apiBuildService.amzAdvPost(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < campaignsList.size(); i++) {
				AmzAdvCampaignsSD campaigns = campaignsList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger campaignId = item.getBigInteger("campaignId");
					campaigns.setCampaignid(campaignId);
					campaigns.setProfileid(profileId);
					campaigns.setOpttime(new Date());
					AmzAdvCampaignsSD Dbcampaigns = this.selectByKey(campaigns);
					if (Dbcampaigns == null) {
						this.save(campaigns);
						amzAdvOperateLogService.saveOperateLog("AmzAdvCampaignsSD", user.getId(), profileId, campaigns, null);
					}
				} else {
					String name = campaigns.getName();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告活动：'" + name + "' 创建失败：" + errormsg);
					exception.setCode("ERROR");
					throw exception;
				}
			}
			return campaignsList;
		}
		return null;
	}
	
	public List<AmzAdvCampaignsSD> amzUpdateSDCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaignsSD> campaignsList) {
		if (campaignsList == null || campaignsList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		List<AmzAdvCampaignsSD> oldcampaignsList = new ArrayList<AmzAdvCampaignsSD>();
		String url = "/" + CampaignType.sd + "/campaigns";
		JSONArray array = new JSONArray();
		for (int i = 0; i < campaignsList.size(); i++) {
			JSONObject param = new JSONObject();
			// name, state, dailyBudget, startDate, premiumBidAdjustment
			AmzAdvCampaignsSD campaigns = campaignsList.get(i);
			// api 不常改字段
			param.put("campaignId", campaigns.getCampaignid());
			param.put("name", campaigns.getName());
			param.put("tactic", campaigns.getTactic());
			param.put("budgetType", campaigns.getBudgettype());
			param.put("budget", campaigns.getBudget());
			param.put("state", campaigns.getState().toLowerCase());
			param.put("startDate", fmt.format(campaigns.getStartDate()));
			if (campaigns.getEndDate() != null) {
				param.put("endDate", fmt.format(campaigns.getEndDate()));
			}
			array.add(param);

			Example example = new Example(AmzAdvCampaigns.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaigns.getCampaignid());
			AmzAdvCampaignsSD oldCampaigns = amzAdvCampaignsSDMapper.selectOneByExample(example);
			oldcampaignsList.add(oldCampaigns);
		}
		String response = apiBuildService.amzAdvPut_V2(profile, url, JSONArray.toJSONString(array, SerializerFeature.WriteMapNullValue));
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			for (int i = 0; i < campaignsList.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvCampaignsSD campaigns = campaignsList.get(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger campaignId = item.getBigInteger("campaignId");
					campaigns.setCampaignid(campaignId);
					campaigns.setProfileid(profileId);
					campaigns.setOpttime(new Date());
					amzAdvCampaignsSDMapper.updateByPrimaryKeySelective(campaigns);

					AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
					AmzAdvCampaignsSD oldCampaigns = oldcampaignsList.get(i);
					operateLog.setCampaignid(campaigns.getCampaignid());
					operateLog.setProfileid(profileId);
					operateLog.setOperator(user.getId());
					operateLog.setOpttime(new Date());
					operateLog.setBeanclasz("AmzAdvCampaignsSD");
					String campaignsjson = GeneralUtil.toJSON(campaigns);
					String oldCampaignsjson = GeneralUtil.toJSON(oldCampaigns);
					operateLog.setAfterobject(campaignsjson);
					operateLog.setBeforeobject(oldCampaignsjson);
					operateLogList.add(operateLog);
				} else {
					String name = campaigns.getName();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告活动：'" + name + "' 修改失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			amzAdvOperateLogService.insertList(operateLogList);
			return campaignsList;
		}
		return null;
	}
	
	public List<AmzAdvCampaignsSD> amzUpdateSDCampaigns(UserInfo user, BigInteger profileId, JSONArray campaignArray) {
		if (campaignArray == null || campaignArray.size() <= 0)
			return null;
		return amzUpdateSDCampaigns(user,profileId,this.convertJsonToBean(user,profileId,campaignArray));
	}

	public AmzAdvCampaignsSD amzArchiveSDCampaigns(UserInfo user, BigInteger profileId, String campaignId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + CampaignType.sd + "/campaigns/" + campaignId;
		String response = apiBuildService.amzAdvDelete(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			Example example = new Example(AmzAdvCampaignsSD.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaignId);
			AmzAdvCampaignsSD oldCampaign = amzAdvCampaignsSDMapper.selectOneByExample(example);
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example2 = new Example(AmzAdvCampaigns.class);
				Criteria crit2 = example2.createCriteria();
				crit2.andEqualTo("profileid", profileId);
				crit2.andEqualTo("campaignid", campaignId);
				AmzAdvCampaignsSD campaign = amzAdvCampaignsSDMapper.selectOneByExample(example2);
				campaign.setState(AdvState.archived);
				campaign.setOpttime(new Date());
				amzAdvCampaignsSDMapper.updateByPrimaryKey(campaign);
				amzAdvOperateLogService.saveOperateLog("AmzAdvCampaignsSD", user.getId(), profileId, campaign, oldCampaign);
				return campaign;
			} else {
				String errormsg = "";
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + a.getString("description");
				BaseException exception = new BaseException("广告组修改失败：" + errormsg);
				exception.setCode("ERROER");
				throw exception;
			}
		}
		return null;
	}
	public Map<String, Object> getSumCampaigns(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return amzAdvCampaignsSDMapper.getSumCampaigns(map);
	}
	public List<Map<String, Object>> getCampaignChart(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return amzAdvCampaignsSDMapper.getCampaignChart(map);
	}
 

	public AmzAdvCampaignsSD selectOneByExample(Example example) {
		// TODO Auto-generated method stub
		return this.amzAdvCampaignsSDMapper.selectOneByExample(example);
	}

	public List<AmzAdvCampaignsSD> getSDCampaignsNotArchivedByprofile(BigInteger profileid) {
		// TODO Auto-generated method stub
			if (profileid != null) {
				Example example = new Example(AmzAdvCampaignsSD.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileid);
				crit.andNotEqualTo("state", "archived");
				example.setOrderByClause("name asc");
				List<AmzAdvCampaignsSD> list = amzAdvCampaignsSDMapper.selectByExample(example);
				return list;
			} else {
				throw new BaseException("请选择店铺与站点！");
			}

	}

	@Override
	public PageList<AmzAdvCampaignsSD> getSDCampaignsNotArchivedByprofile(QueryForList query) {
		BigInteger profileid=new BigInteger(query.getProfileid());
		String name=query.getName();
		PageBounds pageBounds=query.getPageBounds();
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("profileid", profileid);
		if(StrUtil.isNotEmpty(name)) {
			params.put("name", "%"+name+"%");
		}else {
			params.put("name", null);
		}
		if(StrUtil.isNotEmpty(query.getCampaignStatus())&&!query.getCampaignStatus().equals("all")) {
			params.put("campaignStatus", query.getCampaignStatus());
		}else {
			params.put("campaignStatus", null);
		}
		PageList<AmzAdvCampaignsSD> list=amzAdvCampaignsSDMapper.getCampaignsNotArchived(params,pageBounds);
		return list;
	}

	@Override
	public List<AmzAdvProductadsSD> createCampaigns(JSONObject jsonobject, UserInfo user) {
		BigInteger profileid = jsonobject.getBigInteger("profileid");
		BigInteger campaignid = jsonobject.getBigInteger("campaignid");
		JSONArray adGroupArray = jsonobject.getJSONArray("adGroupArray");
		if(campaignid == null) {
			Date startDate = jsonobject.getDate("startDate");
			Date endDate = jsonobject.getDate("endDate");
			BigDecimal budget = jsonobject.getBigDecimal("budget");
			String campaignname = jsonobject.getString("campaignname");
			String targetingType = jsonobject.getString("targetingType");
			Example example = new Example(AmzAdvCampaignsSD.class);
			Criteria criter = example.createCriteria();
			criter.andEqualTo("name", campaignname);
			criter.andEqualTo("profileid", profileid);
			criter.andNotEqualTo("state", AdvState.archived);
			AmzAdvCampaignsSD oldamzAdvCampaigns = this.selectOneByExample(example);
			if(oldamzAdvCampaigns != null) {
				throw new BaseException("在该站点下,广告活动名称已经存在！");
			}
			List<AmzAdvCampaignsSD> campaignsList = new ArrayList<AmzAdvCampaignsSD>();
			AmzAdvCampaignsSD amzAdvCampaigns = new AmzAdvCampaignsSD();
			amzAdvCampaigns.setName(campaignname);
			amzAdvCampaigns.setState(AdvState.enabled);
			amzAdvCampaigns.setStartDate(startDate);
			amzAdvCampaigns.setEndDate(endDate);
			amzAdvCampaigns.setBudget(budget);
			amzAdvCampaigns.setBudgettype(targetingType);
			amzAdvCampaigns.setTactic("T00020");
			campaignsList.add(amzAdvCampaigns);
			campaignsList = this.amzCreateCampaigns(user,profileid,campaignsList);
			if(campaignsList == null) {
				throw new BaseException("亚马逊连接异常，请刷新重试！");
			}
			campaignid = campaignsList.get(0).getCampaignid();
		}
		List<AmzAdvAdgroupsSD> adgroupsList = new ArrayList<AmzAdvAdgroupsSD>();
		List<AmzAdvProductadsSD> productAdsList = new ArrayList<AmzAdvProductadsSD>();
		if (adGroupArray != null) {
			for (int i = 0; i < adGroupArray.size(); i++) {
				JSONObject adGroup = adGroupArray.getJSONObject(i);
				String adGroupName = adGroup.getString("adGroupName");
				BigDecimal defaultbid = adGroup.getBigDecimal("defaultbid");
				Example example = new Example(AmzAdvAdgroups.class);
				Criteria criter = example.createCriteria();
				criter.andEqualTo("name", adGroupName);
				criter.andEqualTo("profileid", profileid);
				criter.andEqualTo("campaignid", campaignid);
				criter.andNotEqualTo("state", AdvState.archived);
				AmzAdvAdgroups oldamzAdvAdgroups = amzAdvAdGroupService.selectOneByExample(example);
				if(oldamzAdvAdgroups != null) {
					throw new BaseException("在该站点的广告活动下,广告组名称已经存在！");
				}
				AmzAdvAdgroupsSD amzAdvAdgroups = new AmzAdvAdgroupsSD();
				amzAdvAdgroups.setCampaignid(campaignid);
				amzAdvAdgroups.setName(adGroupName);
				amzAdvAdgroups.setState(AdvState.enabled);
				amzAdvAdgroups.setDefaultbid(defaultbid);
				adgroupsList.add(amzAdvAdgroups);
			}
			adgroupsList = amzAdvAdgroupsSDService.amzCreateAdGroups(user,profileid,adgroupsList);
			if(adgroupsList!=null && adgroupsList.size()>0){
				for (int i = 0; i < adGroupArray.size(); i++) {
					JSONObject adGroup = adGroupArray.getJSONObject(i);
					JSONArray skuArray = adGroup.getJSONArray("skuArray");
					JSONArray asinArray = adGroup.getJSONArray("asinArray");
					if(skuArray != null) {
						for(int j = 0; j < skuArray.size(); j++) {
							AmzAdvProductadsSD amzAdvProductads = new AmzAdvProductadsSD();
							amzAdvProductads.setCampaignid(campaignid);
							amzAdvProductads.setAdgroupid(adgroupsList.get(0).getAdgroupid());
							amzAdvProductads.setState(AdvState.enabled);
							amzAdvProductads.setSku(skuArray.getJSONObject(j).getString("sku"));
							amzAdvProductads.setAsin(skuArray.getJSONObject(j).getString("asin"));
							productAdsList.add(amzAdvProductads);
						}
					} else {
						for(int j = 0; j < asinArray.size(); j++) {
							AmzAdvProductadsSD amzAdvProductads = new AmzAdvProductadsSD();
							amzAdvProductads.setCampaignid(campaignid);
							amzAdvProductads.setAdgroupid(adgroupsList.get(0).getAdgroupid());
							amzAdvProductads.setState(AdvState.enabled);
							amzAdvProductads.setAsin(asinArray.getJSONObject(j).getString("asin"));
							productAdsList.add(amzAdvProductads);
						}
					}
				}
			}
			productAdsList = amzAdvProductAdsSDService.amzCreateProductAds(user, profileid, productAdsList);
		}
		return  productAdsList;
	}

	 public List<AmzAdvCampaignsSD> convertJsonToBean(UserInfo user, BigInteger profileId, JSONArray campaignsArray){
		 if (campaignsArray == null || campaignsArray.size() <= 0)
				return null;
			List<AmzAdvCampaignsSD> campaignsList = new LinkedList<AmzAdvCampaignsSD>();
			for(int i=0;i<campaignsArray.size();i++) {
				JSONObject campaign = campaignsArray.getJSONObject(i);
				String costType = campaign.getString("costtype");
				String tactic = campaign.getString("tactic");
				String state = campaign.getString("state");
				String budget = campaign.getString("budget");
				BigInteger campaignid = campaign.getBigInteger("campaignid");
				BigInteger portfolioid = campaign.getBigInteger("portfolioid");
				Date startDate = campaign.getDate("startDate");
				Date endDate = campaign.getDate("endDate");
				String name = campaign.getString("name");
 				
				Example example = new Example(AmzAdvCampaigns.class);
				Criteria criter = example.createCriteria();
				criter.andEqualTo("name", name);
				criter.andEqualTo("profileid", profileId);
				List<AmzAdvCampaignsSD> oldlist = this.selectByExample(example);
				for(AmzAdvCampaignsSD oldamzAdvCampaigns:oldlist) {
					if(campaignid!=null) {
						if(!oldamzAdvCampaigns.getCampaignid().equals(campaignid)) {
							throw new BaseException("在该站点下,广告活动名称已存在！");
						}
					}else {
						throw new BaseException("在该站点下,广告活动名称已存在！");
					}
				}
				AmzAdvCampaignsSD amzAdvCampaigns = new AmzAdvCampaignsSD();
				amzAdvCampaigns.setCampaignid(campaignid);
				amzAdvCampaigns.setStartDate(startDate);
				amzAdvCampaigns.setEndDate(endDate);
				amzAdvCampaigns.setTactic(name);
				amzAdvCampaigns.setPortfolioId(portfolioid);
				amzAdvCampaigns.setBudget(new BigDecimal(budget));
				amzAdvCampaigns.setBudgettype("daily");
				amzAdvCampaigns.setTactic(tactic);
				amzAdvCampaigns.setCosttype(costType);
				amzAdvCampaigns.setName(name);
				amzAdvCampaigns.setState(state.toLowerCase());
				amzAdvCampaigns.setOpttime(new Date());
				amzAdvCampaigns.setProfileid(profileId);
				campaignsList.add(amzAdvCampaigns);
			}
			return campaignsList;
	    }

	 
	@Override
	public List<AmzAdvCampaignsSD> amzCreateSDCampaigns(UserInfo user, BigInteger profileid, JSONArray jsonArray) {
		// TODO Auto-generated method stub
		return amzCreateCampaigns(user,profileid,this.convertJsonToBean(user,profileid,jsonArray));
	}

	@Override
	public String amzGetCampaignBudgetUsage(UserInfo user, BigInteger profileid, JSONObject param) {
		// TODO Auto-generated method stub
		String url= "/sd/campaigns/budget/usage";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileid);
		Map<String, String> header=new HashMap<String,String>();
		header.put("Content-Type", "application/vnd.sdcampaignbudgetusage.v1+json");
    	header.put("Accept",       "application/vnd.sdcampaignbudgetusage.v1+json");
		String response = apiBuildService.amzAdvPost(profile, url,param.toJSONString(),header);
		return response;
	}

}
