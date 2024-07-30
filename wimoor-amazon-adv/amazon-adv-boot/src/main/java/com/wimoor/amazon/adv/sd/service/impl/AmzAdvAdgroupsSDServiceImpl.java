package com.wimoor.amazon.adv.sd.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sd.dao.AmzAdvAdgroupsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvAdgroupsSDService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvCampaignsSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

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
	@Autowired
	@Lazy
	IAmzAdvCampaignsSDService amzAdvCampaignSDService;
	@Resource
	ApiBuildService apiBuildService;
	public AmzAdvAdgroupsSD amzGetSDAdGroupExt(UserInfo user, BigInteger profileId, String adgroupid) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/adGroups/extended/"+adgroupid;
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
				AmzAdvAdgroupsSD adgroup = new AmzAdvAdgroupsSD();
				adgroup.setCampaignid(item.getBigInteger("campaignId"));
				adgroup.setName(item.getString("name"));
				adgroup.setAdgroupid(item.getBigInteger("adGroupId"));
				adgroup.setDefaultbid(item.getBigDecimal("defaultBid"));
				adgroup.setBidOptimization(item.getString("bidOptimization"));
				adgroup.setCreativeType(item.getString("creativeType"));
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
				return adgroup;
		}
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
			if( adgroup.getCreativeType()!=null) {
				param.put("creativeType", adgroup.getCreativeType());
			}
			param.put("bidOptimization", adgroup.getBidOptimization());
			Example example = new Example(AmzAdvAdgroups.class);
    		Criteria criter = example.createCriteria();
    		criter.andEqualTo("name", adgroup.getName());
    		criter.andEqualTo("profileid", profileId);
    		criter.andEqualTo("campaignid", adgroup.getCampaignid());
    		criter.andNotEqualTo("state", AdvState.archived);
    		  AmzAdvAdgroupsSD oldamzAdvAdgroups = this.amzAdvAdgroupsSDMapper.selectOneByExample(example);
    		if(oldamzAdvAdgroups != null) {
    			throw new BaseException("在该站点的广告活动下,广告组名称已经存在！");
    		}
			array.add(param);
		}
		String response = apiBuildService.amzAdvPost_V2(profile, url, array.toString());
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
			if( adgroup.getCreativeType()!=null) {
				param.put("creativeType", adgroup.getCreativeType());
			}
			param.put("bidOptimization", adgroup.getBidOptimization());
			param.put("name", adgroup.getName());
			array.add(param);

			Example example = new Example(AmzAdvAdgroups.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("adgroupid", adgroup.getAdgroupid());
			AmzAdvAdgroupsSD oldadgroup = amzAdvAdgroupsSDMapper.selectOneByExample(example);
			oldadgroupList.add(oldadgroup);
		}
		String response = apiBuildService.amzAdvPut(profile, url, array.toString());
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
		String response = apiBuildService.amzAdvDelete(profile, url);
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
		String response = apiBuildService.amzAdvGet_V2(profile, url);
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
				adgroup.setBidOptimization(item.getString("bidOptimization"));
				adgroup.setCreativeType(item.getString("creativeType"));
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
			AmzAdvCampaignsSD advcam = amzAdvCampaignSDService.selectByKey(campaignsid);
			if(list!=null && list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					AmzAdvAdgroupsSD item = list.get(i);
					item.setCamname(advcam.getName());
				}
			}
			return list;
		} 
		return null;
	}
    public List<AmzAdvAdgroupsSD> convertJsonToBean(UserInfo user, String profileid, JSONArray adGroupArray){
    	List<AmzAdvAdgroupsSD> adgroupsList=new ArrayList<AmzAdvAdgroupsSD>();
    	for(int i=0;i<adGroupArray.size();i++) {
    		JSONObject adGroup = adGroupArray.getJSONObject(i);
    		String name = adGroup.getString("name");
    		BigInteger adgroupid = adGroup.getBigInteger("adgroupid");
    		BigInteger campaignid = adGroup.getBigInteger("campaignid");
    		BigDecimal defaultbid = adGroup.getBigDecimal("defaultbid");
    		String state = adGroup.getString("state");
    		String creativeType = adGroup.getString("creativeType");
    		String bidOptimization = adGroup.getString("bidOptimization");
    		
    		AmzAdvAdgroupsSD amzAdvAdgroups = new AmzAdvAdgroupsSD();
    		amzAdvAdgroups.setCampaignid(campaignid);
    		amzAdvAdgroups.setName(name);
    		amzAdvAdgroups.setState(state.toLowerCase());
    		amzAdvAdgroups.setDefaultbid(defaultbid);
    		amzAdvAdgroups.setAdgroupid(adgroupid);
    		amzAdvAdgroups.setBidOptimization(bidOptimization);
    		amzAdvAdgroups.setCreativeType(creativeType);
    		adgroupsList.add(amzAdvAdgroups);
    	}
    	return adgroupsList;
    }
	@Override
	public List<AmzAdvAdgroupsSD> amzCreateAdGroups(UserInfo user, String profileid, JSONArray adGroupArray) {
		// TODO Auto-generated method stub
		List<AmzAdvAdgroupsSD> adgroupsList = this.convertJsonToBean(user, profileid, adGroupArray);
		return this.amzCreateAdGroups(user, new BigInteger(profileid), adgroupsList);
	}

	@Override
	public List<AmzAdvAdgroupsSD> amzUpdateAdGroups(UserInfo user, BigInteger profileid, JSONArray adGroupArray) {
		// TODO Auto-generated method stub
		List<AmzAdvAdgroupsSD> adgroupsList = this.convertJsonToBean(user, profileid.toString(), adGroupArray);
		return this.amzUpdateAdGroups(user, profileid, adgroupsList);
	}

}
