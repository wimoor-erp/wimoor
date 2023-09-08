package com.wimoor.amazon.adv.sp.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AdvState;
import com.wimoor.amazon.adv.common.pojo.AmzAdvOperateLog;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBidReCommendService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvAdgroupsSDService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvAdgroupsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.service.IAmzAdvAdGroupService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
 

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvAdGroupService")
public class AmzAdvAdGroupServiceImpl extends BaseService<AmzAdvAdgroups> implements IAmzAdvAdGroupService {
	@Resource
	AmzAdvAdgroupsMapper amzAdvAdgroupsMapper;
	@Resource
	@Lazy
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	@Lazy
	IAmzAdvOperateLogService amzAdvOperateLogService;
	@Resource
	@Lazy
	IAmzAdvBidReCommendService amzAdvBidReCommendService;
	@Resource
	@Lazy
	IAmzAdvCampaignService amzAdvCampaignService;
	@Resource
	@Lazy
	IAmzAdvAdgroupsSDService amzAdvAdgroupsSDService ;
	
	public AmzAdvAdgroups selectOneByExample(Example example){
		return amzAdvAdgroupsMapper.selectOneByExample(example);
	}
	
	public List<AmzAdvAdgroups> getAdGroupByCampaignsId(UserInfo user,BigInteger profileid, String campaignsid) {
		if (profileid!=null) {
			Example example = new Example(AmzAdvAdgroups.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileid);
			crit.andEqualTo("campaignid", campaignsid);
			example.setOrderByClause("name asc");
			List<AmzAdvAdgroups> list = amzAdvAdgroupsMapper.selectByExample(example);
			return list;
		} 
		return null;
	}

	public AmzAdvAdgroups amzGetAdGroup(UserInfo user,BigInteger profileId, String adgroupid) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/adGroups/" + adgroupid;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		AmzAdvAdgroups adgroup = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			adgroup = new AmzAdvAdgroups();
			adgroup.setAdgroupid(item.getBigInteger("adGroupId"));
			adgroup.setName(item.getString("name"));
			adgroup.setCampaignid(item.getBigInteger("campaignId"));
			adgroup.setDefaultbid(item.getBigDecimal("defaultBid"));
			adgroup.setState(item.getString("state"));
			adgroup.setProfileid(profileId);
			adgroup.setOpttime(new Date());
			AmzAdvAdgroups oldAdgroup = amzAdvAdgroupsMapper.selectByPrimaryKey(adgroup);
			if (oldAdgroup != null) {
				this.updateAll(adgroup);
			} else {
				this.save(adgroup);
			}
			amzAdvOperateLogService.saveOperateLog("AmzAdvAdgroups", user.getId(), profileId, adgroup, null);
			return adgroup;
		}
		return null;
	}

	public AmzAdvAdgroups amzGetAdGroupEx(UserInfo user,BigInteger profileId, String adgroupid) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/adGroups/extended/" + adgroupid;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		AmzAdvAdgroups adgroup = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			adgroup = new AmzAdvAdgroups();
			adgroup.setAdgroupid(item.getBigInteger("adGroupId"));
			adgroup.setName(item.getString("name"));
			adgroup.setCampaignid(item.getBigInteger("campaignId"));
			adgroup.setDefaultbid(item.getBigDecimal("defaultBid"));
			adgroup.setState(item.getString("state"));
			adgroup.setServingStatus(item.getString("servingStatus"));
			adgroup.setCreationDate(item.getDate("creationDate"));
			adgroup.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
			adgroup.setProfileid(profileId);
			adgroup.setOpttime(new Date());
			AmzAdvAdgroups oldAdgroup = amzAdvAdgroupsMapper.selectByPrimaryKey(adgroup);
			if (oldAdgroup != null) {
				this.updateAll(adgroup);
			} else {
				this.save(adgroup);
			}
			return adgroup;
		}
		return null;
	}

	public List<AmzAdvAdgroups> amzCreateAdGroups(UserInfo user,BigInteger profileId, List<AmzAdvAdgroups> adgroups) {
		if (adgroups == null || adgroups.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/adGroups";
		JSONArray array = new JSONArray();
		for (int i = 0; i < adgroups.size(); i++) {
			AmzAdvAdgroups adgroup = adgroups.get(i);
			JSONObject param = new JSONObject();
			param.put("name", adgroup.getName());
			param.put("campaignId", adgroup.getCampaignid());
			param.put("state", adgroup.getState());
			param.put("defaultBid", adgroup.getDefaultbid());
			array.add(param);
		}
		String response = amzAdvAuthService.amzAdvPost(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < adgroups.size(); i++) {
				AmzAdvAdgroups adgroup = adgroups.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger adGroupId = item.getBigInteger("adGroupId");
					adgroup.setAdgroupid(adGroupId);
					adgroup.setProfileid(profileId);
					adgroup.setOpttime(new Date());
					AmzAdvAdgroups Dbadgroup = this.selectByKey(adgroup);
					if(Dbadgroup == null) {
						this.save(adgroup);
						int num= amzAdvAdgroupsMapper.getAdgroupCountByShopId(user.getCompanyid());
						amzAdvOperateLogService.saveOperateLog("AmzAdvAdgroups", user.getId(), profileId, adgroup, null);
					}
				}else {
					String name = adgroup.getName();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告组：'"+name+"' 创建失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			return adgroups;
		}
		return null;
	}

	public List<AmzAdvAdgroups> amzUpdateAdGroups(UserInfo user,BigInteger profileId, List<AmzAdvAdgroups> adgroupList) {
		if (adgroupList == null || adgroupList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		List<AmzAdvAdgroups> oldadgroupList = new ArrayList<AmzAdvAdgroups>();
		String url = "/sp/adGroups";
		JSONArray array = new JSONArray();
		for (int i = 0; i < adgroupList.size(); i++) {
			AmzAdvAdgroups adgroup = adgroupList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("campaignId", adgroup.getCampaignid());
			if (adgroup.getDefaultbid() != null) {
				param.put("defaultBid", adgroup.getDefaultbid());
			}
			param.put("adGroupId", adgroup.getAdgroupid());
			if(!"vendor".equals(profile.getType())) {
				param.put("state", adgroup.getState());
			}
			param.put("name", adgroup.getName());
			array.add(param);

			Example example = new Example(AmzAdvAdgroups.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("adgroupid", adgroup.getAdgroupid());
			AmzAdvAdgroups oldadgroup = amzAdvAdgroupsMapper.selectOneByExample(example);
			oldadgroupList.add(oldadgroup);
		}
		String response = amzAdvAuthService.amzAdvPut(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < adgroupList.size(); i++) {
				AmzAdvAdgroups adgroup = adgroupList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger adGroupId = item.getBigInteger("adGroupId");
					adgroup.setAdgroupid(adGroupId);
					adgroup.setProfileid(profileId);
					adgroup.setOpttime(new Date());
					this.updateNotNull(adgroup);
					
					AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
					AmzAdvAdgroups oldadgroup = oldadgroupList.get(i);
					operateLog.setAdgroupid(adgroup.getAdgroupid());
					operateLog.setCampaignid(adgroup.getCampaignid());
					operateLog.setProfileid(profileId);
					operateLog.setOperator(user.getId());
					operateLog.setOpttime(new Date());
					operateLog.setBeanclasz("AmzAdvAdgroups");
					String adgroupjson = GeneralUtil.toJSON(adgroup);
					String oldadgroupjson = GeneralUtil.toJSON(oldadgroup);
					operateLog.setAfterobject(adgroupjson);
					operateLog.setBeforeobject(oldadgroupjson);
					operateLogList.add(operateLog);
				}else {
					String name = adgroup.getName();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告组：'"+name+"' 修改失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			amzAdvOperateLogService.insertList(operateLogList);
			return adgroupList;
		}
		return null;
	}

	public AmzAdvAdgroups archiveAdGroup(UserInfo user,BigInteger profileId, String adgroupid) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvAdgroups adgroup = null;
		String url = "/sp/adGroups/" + adgroupid;
		String response = amzAdvAuthService.amzAdvDelete(profile, url);
		Example example = new Example(AmzAdvAdgroups.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("profileid", profileId);
		crit.andEqualTo("adgroupid", adgroupid);
		AmzAdvAdgroups oldadgroup = amzAdvAdgroupsMapper.selectOneByExample(example);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				adgroup = amzAdvAdgroupsMapper.selectByPrimaryKey(oldadgroup);
				adgroup.setState(AdvState.archived);
				adgroup.setOpttime(new Date());
				this.updateAll(adgroup);
				amzAdvOperateLogService.saveOperateLog("AmzAdvAdgroups", user.getId(), profileId, adgroup, oldadgroup);
				return adgroup;
			}else {
				String errormsg = "";
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + a.getString("description");
				BaseException exception = new BaseException("广告组修改失败："+errormsg);
				exception.setCode("ERROER");
				throw exception;
			}
		}
		return null;
	}

	public List<AmzAdvAdgroups> amzListAdGroups(UserInfo user,BigInteger profileId, Map<String, Object> param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/adGroups/?";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "startIndex");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "count");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "stateFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "name");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "adGroupIdFilter");
		}
		url = url + ("".equals(paramurl) ? "" : paramurl);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvAdgroups> list = new LinkedList<AmzAdvAdgroups>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvAdgroups adgroup = new AmzAdvAdgroups();
				adgroup.setCampaignid(item.getBigInteger("campaignId"));
				adgroup.setName(item.getString("name"));
				adgroup.setAdgroupid(item.getBigInteger("adGroupId"));
				adgroup.setDefaultbid(item.getBigDecimal("defaultBid"));
				adgroup.setState(item.getString("state"));
				adgroup.setProfileid(profileId);
				adgroup.setOpttime(new Date());
				AmzAdvAdgroups oldadgroup = amzAdvAdgroupsMapper.selectByPrimaryKey(adgroup);
				if (oldadgroup != null) {
					this.updateAll(adgroup);
				} else {
					this.save(adgroup);
				}
				list.add(adgroup);
			}
			return list;
		}
		return null;
	}

	public List<AmzAdvAdgroups> amzListAdGroupsExt(UserInfo user,BigInteger profileId, Map<String, Object> param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/adGroups/extended?";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "startIndex");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "count");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "stateFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "name");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "adGroupIdFilter");
		}
		url = url + ("".equals(paramurl) ? "" : paramurl);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvAdgroups> list = new LinkedList<AmzAdvAdgroups>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvAdgroups adgroup = new AmzAdvAdgroups();
				adgroup.setCampaignid(item.getBigInteger("campaignId"));
				adgroup.setName(item.getString("name"));
				adgroup.setAdgroupid(item.getBigInteger("adGroupId"));
				adgroup.setDefaultbid(item.getBigDecimal("defaultBid"));
				adgroup.setState(item.getString("state"));
				adgroup.setServingStatus(item.getString("servingStatus"));
				adgroup.setCreationDate(item.getDate("creationDate"));
				adgroup.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				adgroup.setProfileid(profileId);
				adgroup.setOpttime(new Date());
				AmzAdvAdgroups oldadgroup = amzAdvAdgroupsMapper.selectByPrimaryKey(adgroup);
				if (oldadgroup != null) {
					this.updateAll(adgroup);
				} else {
					this.save(adgroup);
				}
				list.add(adgroup);
			}
			return list;
		}
		return null;
	}

	public PageList<Map<String, Object>> getAdGroupList(Map<String, Object> map, PageBounds pageBounds) {
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("HSA".equals(campaignType)) {
			    return null;
			}else if("SD".equals(campaignType)) {
				return amzAdvAdgroupsSDService.getAdgroupList(map, pageBounds);
			}else if("SP".equals(campaignType)) {
				return amzAdvAdgroupsMapper.getAdgroupList(map, pageBounds);
			}
		}
		return amzAdvAdgroupsMapper.getAllAdgroupList(map, pageBounds);
	}

	public Map<String, Object> getAdGroupChart(Map<String, Object> map) {
		List<Map<String, Object>>  listSP = null;
		List<Map<String, Object>>  listSD = null;
		List<Map<String, Object>>  listSB = null;
		getSerchStr(map);
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("HSA".equals(campaignType) || "SB".equals(campaignType)) {
				return null;
			}else if("SP".equals(campaignType.toUpperCase())) {
				listSP = amzAdvAdgroupsMapper.getAdgroupChart(map);
			}else if("SD".equals(campaignType.toUpperCase())) {
				listSD=amzAdvAdgroupsSDService.getAdgroupChart(map);
			}else if("ALL".equals(campaignType.toUpperCase())) {
				listSP = amzAdvAdgroupsMapper.getAdgroupChart(map);
				listSD=amzAdvAdgroupsSDService.getAdgroupChart(map);
			}
		}
		return amzAdvCampaignService.getChartData(listSP,listSB,listSD, map);
	}
 
	
	public void getSerchStr(Map<String,Object> map) {
		String serch = (String) map.get("searchlist");
		String[] serchArray = serch.split(",");
		String serchlist = "";
		for (int i = 0; i < serchArray.length; i++) {
				if ("ACOS".equals(serchArray[i])) {
					serchlist = serchlist + "ifnull(sum(cost) / sum(attributedSales7d),0) ACOS ,";
				} else if ("ROAS".equals(serchArray[i])) {
					serchlist = serchlist + "ifnull(sum(attributedSales7d) / sum(cost),0) ROAS ,";
				} else if ("CSRT".equals(serchArray[i])) {
					serchlist = serchlist + "ifnull(sum(attributedConversions7d) / sum(clicks),0) CSRT ,";
				} else if ("avgcost".equals(serchArray[i])) {
					serchlist = serchlist + "ifnull((sum(cost) / sum(clicks)),0) avgcost ,";
				} else if ("CTR".equals(serchArray[i])) {
					serchlist = serchlist + "ifnull(sum(clicks) / sum(impressions),0) CTR ,";
				}else if ("sumUnits".equals(serchArray[i])) {
					 serchlist = serchlist +"sum(attributedUnitsOrdered7d) sumUnits,";
				} else {
					serchlist = serchlist +"sum(" + serchArray[i] + ") " + serchArray[i] + ",";
				}
		}
		if(serchlist.contains(",")) {
			serchlist=serchlist.substring(0, serchlist.length()-1);
		}
		map.put("serchlist", serchlist);
		map.put("value1", serchArray[0]);
	}

	public Map<String, Object> catchAdGroupSuggestBid(UserInfo user,Map<String, Object> map) {
		String profileid = (String) map.get("profileid");
		String adGroupid = (String) map.get("id");
		String campaignid = (String) map.get("campaignId");
		Example example = new Example(AmzAdvCampaigns.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("profileid", new BigInteger(profileid));
		crit.andEqualTo("campaignid", new BigInteger(campaignid));
		crit.andEqualTo("targetingtype", "auto");
		crit.andEqualTo("campaigntype", "sponsoredProducts");
		AmzAdvCampaigns amzAdvCampaigns = amzAdvCampaignService.selectOneByExample(example);
		if(amzAdvCampaigns != null) {
			Map<String,Object> mapBid = amzAdvBidReCommendService.amzGetAdGroupBidRecommendations(user, new BigInteger(profileid), adGroupid);
			if(mapBid != null) {
				map.put("suggestedBid", mapBid.get("suggestedBid"));
			}
		}
		return map;
	}

	public List<Map<String, Object>> getSumAdGroup(Map<String, Object> map) {
		String campaignType = (String) map.get("campaignType");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Object paralist = map.get("paralist");
		if(paralist != null) {
			paralist = paralist.toString().replace("CSRT", "attributedConversions7d / clicks")
				.replace("ACOS", "cost / attributedSales7d").replace("ROAS", "attributedSales7d / cost");
			map.put("paralist", paralist);
		}
		if("HSA".equals(campaignType) || "SB".equals(campaignType)) {
			return null;
		}
		else if("sp".equals(campaignType.toLowerCase())) {
			Map<String, Object> sumMap = amzAdvAdgroupsMapper.getSumAdGroup(map);
			if(sumMap != null && sumMap.size() > 0) {
				list.add(sumMap);
			}
		}else if("sd".equals(campaignType.toLowerCase())) {
			Map<String, Object> sumMap = amzAdvAdgroupsSDService.getSumAdGroup(map);
			if(sumMap != null && sumMap.size() > 0) {
				list.add(sumMap);
			}
		}else if("all".equals(campaignType.toLowerCase())) {
			Map<String, Object> sumMap1 = amzAdvAdgroupsMapper.getSumAdGroup(map);
			Map<String, Object> sumMap2 = amzAdvAdgroupsSDService.getSumAdGroup(map);
			if(sumMap1 != null && sumMap1.size() > 0) {
				list.add(sumMap1);
			}
			if(sumMap2 != null && sumMap2.size() > 0) {
				list.add(sumMap2);
			}
		}
		return list;
	}

}
