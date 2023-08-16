package com.wimoor.amazon.adv.sd.service.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sd.dao.AmzAdvCampaignsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeNegativaSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvAdgroupsSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvCampaignsSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductAdsSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeNegativaSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl.CampaignType;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
import com.wimoor.common.GeneralUtil;

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
	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	
	public PageList<Map<String, Object>> getCampaignList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		return amzAdvCampaignsSDMapper.getCampaignList(map,pageBounds);
	}
	
	public AmzAdvCampaignsSD amzGetSpCampaigns(UserInfo user, BigInteger profileId, String campaignId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + CampaignType.sd + "/campaigns/extended/" + campaignId;
		String response = amzAdvAuthService.amzAdvGet_V3(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			AmzAdvCampaignsSD campaigns = new AmzAdvCampaignsSD();
			campaigns.setCampaignid(item.getBigInteger("campaignId"));
			campaigns.setName(item.getString("name"));
			campaigns.setBudgettype(item.getString("budgetType"));
			campaigns.setState(item.getString("state"));
			campaigns.setBudget(item.getBigDecimal("budget"));
			campaigns.setTactic(item.getString("tactic"));
			try {
				campaigns.setStartdate(fmt.parse(item.getString("startDate")));
				if (item.getString("endDate") != null) {
					campaigns.setEnddate(fmt.parse(item.getString("endDate")));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
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
		String url = "​/"+ AmzAdvCampaignServiceImpl.CampaignType.sd +"/campaigns/extended?";
		String param="";
		if (campaignsParam != null) {
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "startIndex");
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "count");
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "stateFilter");
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "name");
			param = GeneralUtil.addParamToUrl2(param, campaignsParam, "campaignIdFilter");
		}
		url = url + ("".equals(param) ? "" : param);
		String response = amzAdvAuthService.amzAdvGet_V3(profile, url);
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
						campaigns.setEnddate(fmt.parse(item.getString("endDate")));
					}
					if (item.getString("startDate") != null) {
						campaigns.setStartdate(fmt.parse(item.getString("startDate")));
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
		String response = amzAdvAuthService.amzAdvDelete_V3(profile, url);
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
			param.put("budget", campaigns.getBudget());
			param.put("state", campaigns.getState());
			param.put("startDate", fmt.format(campaigns.getStartdate()));
			if (campaigns.getEnddate() != null) {
				param.put("endDate", fmt.format(campaigns.getEnddate()));
			}
			array.add(param);
		}
		String response = amzAdvAuthService.amzAdvPost_V3(profile, url, array.toString());
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
			param.put("state", campaigns.getState());
			param.put("startDate", fmt.format(campaigns.getStartdate()));
			if (campaigns.getEnddate() != null) {
				param.put("endDate", fmt.format(campaigns.getEnddate()));
			}
			array.add(param);

			Example example = new Example(AmzAdvCampaigns.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaigns.getCampaignid());
			AmzAdvCampaignsSD oldCampaigns = amzAdvCampaignsSDMapper.selectOneByExample(example);
			oldcampaignsList.add(oldCampaigns);
		}
		String response = amzAdvAuthService.amzAdvPut(profile, url, JSONArray.toJSONString(array, SerializerFeature.WriteMapNullValue));
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

	public AmzAdvCampaignsSD amzArchiveSDCampaigns(UserInfo user, BigInteger profileId, String campaignId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/" + CampaignType.sd + "/campaigns/" + campaignId;
		String response = amzAdvAuthService.amzAdvDelete_V3(profile, url);
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

	public int amzCreateSDCampaignsWithTarget(UserInfo user, 
			BigInteger profileid, 
			AmzAdvCampaignsSD campaigns,
			AmzAdvAdgroupsSD adgroups,
			List<AmzAdvProductadsSD> productAdsList, 
			List<AmzAdvProductTargeSD> productTargeList, 
			List<AmzAdvProductTargeNegativaSD> negetivaProductTargeList) {
		// TODO Auto-generated method stub
		int result=0;
		List<AmzAdvCampaignsSD> cpresult =null;
		if("isold".equals(campaigns.getState())) {
			cpresult = new ArrayList<AmzAdvCampaignsSD>();
			cpresult.add(campaigns);
		}else {
			ArrayList<AmzAdvCampaignsSD> campaignsList = new ArrayList<AmzAdvCampaignsSD>();
			campaignsList.add(campaigns);
			cpresult = this.amzCreateCampaigns(user, profileid, campaignsList);
			if(cpresult==null) {
				throw new BaseException("SD广告活动新建失败");
			}
			result=result+cpresult.size();
		}
		AmzAdvCampaignsSD mycampaigns = cpresult.get(0);
		adgroups.setCampaignid(mycampaigns.getCampaignid());
		ArrayList<AmzAdvAdgroupsSD> adgroupsList = new ArrayList<AmzAdvAdgroupsSD>();
		adgroupsList.add(adgroups);
		List<AmzAdvAdgroupsSD> gpresult = amzAdvAdgroupsSDService.amzCreateAdGroups(user, profileid, adgroupsList);
		if(gpresult==null) {
			throw new BaseException("SD广告组新建失败");
		}
		result=result+gpresult.size();
		AmzAdvAdgroupsSD myadgroups = gpresult.get(0);
		if(productAdsList!=null&&productAdsList.size()>0) {
			for(AmzAdvProductadsSD psd :productAdsList) {
				psd.setAdgroupid(myadgroups.getAdgroupid());
				psd.setProfileid(profileid);
				psd.setCampaignid(mycampaigns.getCampaignid());
				psd.setOpttime(new Date());
				psd.setState(AdvState.enabled);
			}
			List<AmzAdvProductadsSD> adsresult = amzAdvProductAdsSDService.amzCreateProductAds(user, profileid, productAdsList);
			result=result+adsresult.size();
		}
		if(productTargeList!=null&&productTargeList.size()>0) {
			for(AmzAdvProductTargeSD pt:productTargeList) {
				pt.setAdgroupid(myadgroups.getAdgroupid());
				pt.setProfileid(profileid);
				pt.setState(AdvState.enabled);
			}
			amzAdvProductTargeSDService.amzCreateTargetingClauses_V3(user, profileid, productTargeList);
		}
		if(negetivaProductTargeList!=null&&negetivaProductTargeList.size()>0) {
			for(AmzAdvProductTargeNegativaSD ntp:negetivaProductTargeList) {
				ntp.setAdgroupid(myadgroups.getAdgroupid());
				ntp.setProfileid(profileid);
				ntp.setState(AdvState.enabled);
			}
			amzAdvProductTargeNegativaSDService.amzCreateNegativeTargetingClauses_V3(user, profileid, negetivaProductTargeList);
		}
		return result;
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

}
