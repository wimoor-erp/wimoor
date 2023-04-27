package com.wimoor.amazon.adv.sd.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sd.dao.AmzAdvAdgroupsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvAdgroupsSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvAdgroupsSDService")
public class AmzAdvAdgroupsSDServiceImpl  extends BaseService<AmzAdvAdgroupsSD> implements IAmzAdvAdgroupsSDService {
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvAdgroupsSDMapper amzAdvAdgroupsSDMapper;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	public AmzAdvAdgroupsSD amzGetSDAdGroup(UserInfo user, BigInteger profileId, String adgroupid) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AmzAdvAdgroupsSD> amzGetSDAdGroupList(UserInfo user, BigInteger profileId,
			Map<String, Object> campaignsParam) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<AmzAdvAdgroupsSD> amzCreateAdGroups(UserInfo user,BigInteger profileId, List<AmzAdvAdgroupsSD> adgroups) {
		if (adgroups == null || adgroups.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/adGroups";
		JSONArray array = new JSONArray();
		for (int i = 0; i < adgroups.size(); i++) {
			AmzAdvAdgroupsSD adgroup = adgroups.get(i);
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
				AmzAdvAdgroupsSD adgroup = adgroups.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger adGroupId = item.getBigInteger("adGroupId");
					adgroup.setAdgroupid(adGroupId);
					adgroup.setProfileid(profileId);
					adgroup.setOpttime(new Date());
					AmzAdvAdgroupsSD Dbadgroup = this.selectByKey(adgroup);
					if(Dbadgroup == null) {
						this.save(adgroup);
						amzAdvOperateLogService.saveOperateLog("AmzAdvAdgroupsSD", user.getId(), profileId, adgroup, null);
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

	public List<AmzAdvAdgroupsSD> amzUpdateAdGroups(UserInfo user,BigInteger profileId, List<AmzAdvAdgroupsSD> adgroupList) {
		if (adgroupList == null || adgroupList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		List<AmzAdvAdgroupsSD> oldadgroupList = new ArrayList<AmzAdvAdgroupsSD>();
		String url = "/sd/adGroups";
		JSONArray array = new JSONArray();
		for (int i = 0; i < adgroupList.size(); i++) {
			AmzAdvAdgroupsSD adgroup = adgroupList.get(i);
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
			AmzAdvAdgroupsSD oldadgroup = amzAdvAdgroupsSDMapper.selectOneByExample(example);
			oldadgroupList.add(oldadgroup);
		}
		String response = amzAdvAuthService.amzAdvPut_V3(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < adgroupList.size(); i++) {
				AmzAdvAdgroupsSD adgroup = adgroupList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger adGroupId = item.getBigInteger("adGroupId");
					adgroup.setAdgroupid(adGroupId);
					adgroup.setProfileid(profileId);
					adgroup.setOpttime(new Date());
					this.updateNotNull(adgroup);
					
					AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
					AmzAdvAdgroupsSD oldadgroup = oldadgroupList.get(i);
					operateLog.setAdgroupid(adgroup.getAdgroupid());
					operateLog.setCampaignid(adgroup.getCampaignid());
					operateLog.setProfileid(profileId);
					operateLog.setOperator(user.getId());
					operateLog.setOpttime(new Date());
					operateLog.setBeanclasz("AmzAdvAdgroupsSD");
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

	public AmzAdvAdgroupsSD archiveAdGroup(UserInfo user,BigInteger profileId, String adgroupid) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvAdgroupsSD adgroup = null;
		String url = "/sd/adGroups/" + adgroupid;
		String response = amzAdvAuthService.amzAdvDelete_V3(profile, url);
		Example example = new Example(AmzAdvAdgroups.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("profileid", profileId);
		crit.andEqualTo("adgroupid", adgroupid);
		AmzAdvAdgroupsSD oldadgroup = amzAdvAdgroupsSDMapper.selectOneByExample(example);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				adgroup = amzAdvAdgroupsSDMapper.selectByPrimaryKey(oldadgroup);
				adgroup.setState(AdvState.archived);
				adgroup.setOpttime(new Date());
				this.updateAll(adgroup);
				int count = 0;
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

	public PageList<Map<String, Object>> getAdgroupList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		return amzAdvAdgroupsSDMapper.getAdgroupList(map,pageBounds);
	}

	public Map<String, Object> getSumAdGroup(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return amzAdvAdgroupsSDMapper.getSumAdGroup(map) ;
	}

	public List<Map<String, Object>> getAdgroupChart(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return amzAdvAdgroupsSDMapper.getAdgroupChart(map);
	}

	public List<AmzAdvAdgroupsSD> amzListAdGroupsExt(UserInfo user, BigInteger profileId, Map<String, Object> param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/adGroups/extended?";
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
		List<AmzAdvAdgroupsSD> list = new LinkedList<AmzAdvAdgroupsSD>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvAdgroupsSD adgroup = new AmzAdvAdgroupsSD();
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
				AmzAdvAdgroupsSD oldadgroup = amzAdvAdgroupsSDMapper.selectByPrimaryKey(adgroup);
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
	public List<AmzAdvAdgroupsSD> getAdGroupByCampaignsId(UserInfo user,BigInteger profileid, String campaignsid) {
		if (profileid!=null) {
			Example example = new Example(AmzAdvAdgroups.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileid);
			crit.andEqualTo("campaignid", campaignsid);
			example.setOrderByClause("name asc");
			List<AmzAdvAdgroupsSD> list = amzAdvAdgroupsSDMapper.selectByExample(example);
			return list;
		} 
		return null;
	}

}
