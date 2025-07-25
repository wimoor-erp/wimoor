package com.wimoor.amazon.adv.sb.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvAdgroupsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvCampaignsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdgroupsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvAdgroupsHsaService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvAdgroupsHsaService")
public class AmzAdvAdgroupsHsaServiceImpl extends BaseService<AmzAdvAdgroupsHsa> implements IAmzAdvAdgroupsHsaService {
	
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvAdgroupsHsaMapper amzAdvAdgroupsHsaMapper;
	@Resource
	ApiBuildService apiBuildService;
	@Resource
	AmzAdvCampaignsHsaMapper amzAdvCampaignsHsaMapper;
	@Resource
	@Lazy
	IAmzAdvOperateLogService amzAdvOperateLogService;
	
 
	
	public List<AmzAdvAdgroupsHsa> amzGetSBAdGroupList(AmzAdvProfile profile, JSONObject adgroupParam) {
		// TODO Auto-generated method stub
		String url = "/sb/v4/adGroups/list";
		String response = apiBuildService.amzAdvPost(profile, url,adgroupParam.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject result = GeneralUtil.getJsonObject(response.toString());
			adgroupParam.put("nextToken",result.getString("nextToken"));
			JSONArray a=result.getJSONArray("adGroups");
			List<AmzAdvAdgroupsHsa> list = new ArrayList<AmzAdvAdgroupsHsa>();
			AmzAdvAdgroupsHsa adgroup = null;
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				adgroup = new AmzAdvAdgroupsHsa();
				adgroup.setAdgroupid(item.getBigInteger("adGroupId"));
				adgroup.setName(item.getString("name"));
				adgroup.setCampaignid(item.getBigInteger("campaignId"));
				adgroup.setProfileid(profile.getId());
				adgroup.setState(item.getString("state"));
				adgroup.setOpttime(new Date());
				AmzAdvAdgroupsHsa oldAdgroup = amzAdvAdgroupsHsaMapper.selectByPrimaryKey(adgroup);
				if (oldAdgroup != null) {
					amzAdvAdgroupsHsaMapper.updateByPrimaryKey(adgroup);
				} else {
					amzAdvAdgroupsHsaMapper.insert(adgroup);
				}
				list.add(adgroup);
			}
			return list;
		}else {
			adgroupParam.put("nextToken",null);
		}
		return null;
	}

	@Override
	public List<AmzAdvAdgroupsHsa> getAdGroupByCampaignsId(UserInfo user, BigInteger profileid, String campaignsid) {
		// TODO Auto-generated method stub
		if (profileid!=null) {
			Example example = new Example(AmzAdvAdgroupsHsa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileid);
			crit.andEqualTo("campaignid", campaignsid);
			example.setOrderByClause("name asc");
			List<AmzAdvAdgroupsHsa> list = amzAdvAdgroupsHsaMapper.selectByExample(example);
			AmzAdvCampaignsHsa advcam = amzAdvCampaignsHsaMapper.selectByPrimaryKey(campaignsid);
			if(list!=null && list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					AmzAdvAdgroupsHsa item = list.get(i);
					item.setCamname(advcam.getName());
				}
			}
			return list;
		} 
		return null;
	}

	@Override
	public PageList<Map<String, Object>> getAdgroupList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		return amzAdvAdgroupsHsaMapper.getAdgroupList(map,pageBounds);
	}
	
	public List<AmzAdvAdgroupsHsa> amzCreateAdGroups(UserInfo user,BigInteger profileId, List<AmzAdvAdgroupsHsa> adgroups) {
		if (adgroups == null || adgroups.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/v4/adGroups";
		JSONObject adGroups=new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < adgroups.size(); i++) {
			AmzAdvAdgroupsHsa adgroup = adgroups.get(i);
			JSONObject param = new JSONObject();
			param.put("name", adgroup.getName());
			param.put("campaignId", adgroup.getCampaignid().toString());
			param.put("state", adgroup.getState());
			Example example = new Example(AmzAdvAdgroupsHsa.class);
			Criteria criter = example.createCriteria();
			criter.andEqualTo("name", adgroup.getName());
			criter.andEqualTo("profileid", profileId);
			criter.andEqualTo("campaignid", adgroup.getCampaignid());
			criter.andNotEqualTo("state", AdvState.archived);
			    AmzAdvAdgroupsHsa oldamzAdvAdgroups = this.amzAdvAdgroupsHsaMapper.selectOneByExample(example);
			if(oldamzAdvAdgroups != null) {
				throw new BaseException("在该站点的广告活动下,广告组名称已经存在！");
			}
			array.add(param);
		}
		adGroups.put("adGroups", array);
		String response = apiBuildService.amzAdvPost(profile, url, adGroups.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject campaignsJson = json.getJSONObject("adGroups");
			JSONArray success = campaignsJson.getJSONArray("success");
			JSONArray error = campaignsJson.getJSONArray("error");
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				AmzAdvAdgroupsHsa adgroup = adgroups.get(index);
				BigInteger adGroupId = item.getBigInteger("adGroupId");
				adgroup.setAdgroupid(adGroupId);
				adgroup.setProfileid(profileId);
				adgroup.setOpttime(new Date());
				AmzAdvAdgroupsHsa Dbadgroup = this.selectByKey(adgroup);
				if (Dbadgroup == null) {
					amzAdvAdgroupsHsaMapper.insert(adgroup);
					amzAdvOperateLogService.saveOperateLog("AmzAdvAdgroupsHsa", user.getId(), profileId, adgroup, null);
				}
			}
			for(int i=0;i<error.size();i++) {
				    JSONObject item=error.getJSONObject(i);
				    errormsg=errormsg+item.toJSONString();
				}
			if(StrUtil.isNotBlank(errormsg)) {
				BaseException exception = new BaseException(errormsg);
				exception.setCode("ERROR");
				throw exception;
			}
			return adgroups;
		}
		return null;
	}
	
	 private Map<String, String> getHeaderExt() {
		// TODO Auto-generated method stub
		 Map<String,String> header=new HashMap<String,String>();
	    	header.put("Content-Type", "application/vnd.sbadgroupresource.v4+json");
	    	header.put("Accept",       "application/vnd.sbadgroupresource.v4+json");
		return header;
	}

	public List<AmzAdvAdgroupsHsa> convertJsonToBean(UserInfo user, BigInteger profileId, JSONArray adGroupArray){
	    	List<AmzAdvAdgroupsHsa> adgroupsList = new ArrayList<AmzAdvAdgroupsHsa>();
			for (int i = 0; i < adGroupArray.size(); i++) {
				JSONObject adGroup = adGroupArray.getJSONObject(i);
				String adGroupName = adGroup.getString("name");
				BigInteger campaignid = adGroup.getBigInteger("campaignid");
				AmzAdvAdgroupsHsa amzAdvAdgroups = new AmzAdvAdgroupsHsa();
				amzAdvAdgroups.setCampaignid(campaignid);
				amzAdvAdgroups.setName(adGroupName);
				amzAdvAdgroups.setState(adGroup.getString("state")!=null?adGroup.getString("state"):AdvState.enabled.toUpperCase());
				adgroupsList.add(amzAdvAdgroups);
			}
			return adgroupsList;
	    }
	 
		
	@Override
	public List<AmzAdvAdgroupsHsa> amzCreateAdGroups(UserInfo user, BigInteger profileId, JSONArray adGroupArray) {
		// TODO Auto-generated method stub
		List<AmzAdvAdgroupsHsa> adgroupsList = this.convertJsonToBean(user, profileId, adGroupArray);
		return this.amzCreateAdGroups(user, profileId, adgroupsList);
	}

	@Override
	public List<AmzAdvAdgroupsHsa> amzUpdateAdGroups(UserInfo user, BigInteger profileId, JSONArray adGroupArray) {
		// TODO Auto-generated method stub
		List<AmzAdvAdgroupsHsa> adgroupsList = this.convertJsonToBean(user, profileId, adGroupArray);
		return this.amzUpdateAdGroups(user, profileId, adgroupsList);
	}

	private List<AmzAdvAdgroupsHsa> amzUpdateAdGroups(UserInfo user, BigInteger profileId,
			List<AmzAdvAdgroupsHsa> adgroupsList) {
		// TODO Auto-generated method stub
		if (adgroupsList == null || adgroupsList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/v4/adGroups";
		JSONObject adGroups=new JSONObject();
		JSONArray array = new JSONArray();
		List<AmzAdvAdgroupsHsa> oldadgroupList=new ArrayList<AmzAdvAdgroupsHsa>();
		for (int i = 0; i < adgroupsList.size(); i++) {
			AmzAdvAdgroupsHsa adgroup = adgroupsList.get(i);
			JSONObject param = new JSONObject();
			param.put("name", adgroup.getName());
			param.put("campaignId", adgroup.getCampaignid());
			param.put("state", adgroup.getState());
			param.put("adGroupId", adgroup.getAdgroupid());
			AmzAdvAdgroupsHsa  oldamzAdvAdgroups = this.amzAdvAdgroupsHsaMapper.selectByPrimaryKey(adgroup.getAdgroupid());
			oldadgroupList.add(oldamzAdvAdgroups);
			array.add(param);
		}
		adGroups.put("adGroups", array);
		String response = apiBuildService.amzAdvPut(profile, url, adGroups.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			if (StringUtil.isNotEmpty(response)) {
				String errormsg = "";
				JSONObject json = GeneralUtil.getJsonObject(response.toString());
				JSONObject campaignsJson = json.getJSONObject("campaigns");
				JSONArray success = campaignsJson.getJSONArray("success");
				JSONArray error = campaignsJson.getJSONArray("error");
				List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
				for(int i=0;i<success.size();i++) {
					JSONObject item=success.getJSONObject(i);
					Integer index = item.getInteger("index");
					AmzAdvAdgroupsHsa adgroup = adgroupsList.get(index);
					BigInteger adGroupId = item.getBigInteger("adGroupId");
					adgroup.setAdgroupid(adGroupId);
					adgroup.setProfileid(profileId);
					adgroup.setOpttime(new Date());
					amzAdvAdgroupsHsaMapper.updateByPrimaryKey(adgroup);
					
					AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
					AmzAdvAdgroupsHsa oldadgroup = oldadgroupList.get(index);
					operateLog.setAdgroupid(adgroup.getAdgroupid());
					operateLog.setCampaignid(adgroup.getCampaignid());
					operateLog.setProfileid(profileId);
					operateLog.setOperator(user.getId());
					operateLog.setOpttime(new Date());
					operateLog.setBeanclasz("AmzAdvAdgroupsHsa");
					String adgroupjson = GeneralUtil.toJSON(adgroup);
					String oldadgroupjson = GeneralUtil.toJSON(oldadgroup);
					operateLog.setAfterobject(adgroupjson);
					operateLog.setBeforeobject(oldadgroupjson);
					operateLogList.add(operateLog);
				}
				for(int i=0;i<error.size();i++) {
				    JSONObject item=error.getJSONObject(i);
				    errormsg=errormsg+item.toJSONString();
				}
				if(StrUtil.isNotBlank(errormsg)) {
					BaseException exception = new BaseException(errormsg);
					exception.setCode("ERROR");
					throw exception;
				}
				amzAdvOperateLogService.insertList(operateLogList);
				return adgroupsList;
			}
			
			
			return adgroupsList;
		}
		return null;
	}

	@Override
	public List<AmzAdvAdgroupsHsa> amzListAdGroups(BigInteger profileid, Map<String, Object> param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileid);
		String url = "/sb/v4/adGroups/list";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		List<AmzAdvAdgroupsHsa> list = new LinkedList<AmzAdvAdgroupsHsa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject json=GeneralUtil.getJsonObject(response);
			JSONArray a = json.getJSONArray("adGroups");
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvAdgroupsHsa adgroup = new AmzAdvAdgroupsHsa();
				adgroup.setCampaignid(item.getBigInteger("campaignId"));
				adgroup.setName(item.getString("name"));
				adgroup.setAdgroupid(item.getBigInteger("adGroupId"));
				adgroup.setState(item.getString("state"));
				adgroup.setProfileid(profileid);
				adgroup.setOpttime(new Date());
				JSONObject extendedData = item.getJSONObject("extendedData");
				if(extendedData!=null) {
					String servingStatus=extendedData.getString("servingStatus");
					adgroup.setServingStatus(servingStatus);
					Date creationDateTime=extendedData.getDate("creationDateTime");
					adgroup.setCreationDate(creationDateTime);
					Date lastUpdateDateTime=extendedData.getDate("lastUpdateDateTime");
					adgroup.setLastUpdatedDate(lastUpdateDateTime);
				}
				AmzAdvAdgroupsHsa oldadgroup = amzAdvAdgroupsHsaMapper.selectByPrimaryKey(adgroup);
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

	@Override
	public List<AmzAdvAdgroupsHsa> amzDeleteAdGroups(UserInfo user,BigInteger profileid, JSONObject param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileid);
		String url = "/sb/v4/adGroups/delete";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		List<AmzAdvAdgroupsHsa> list = new LinkedList<AmzAdvAdgroupsHsa>();
		if (StringUtil.isNotEmpty(response)) {
				String errormsg = "";
				JSONObject json = GeneralUtil.getJsonObject(response.toString());
				JSONObject campaignsJson = json.getJSONObject("adGroups");
				JSONArray success = campaignsJson.getJSONArray("success");
				JSONArray error = campaignsJson.getJSONArray("error");
				List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
				for(int i=0;i<success.size();i++) {
					JSONObject item=success.getJSONObject(i);
					BigInteger adGroupId = item.getBigInteger("adGroupId");
					AmzAdvAdgroupsHsa adgroup = this.amzAdvAdgroupsHsaMapper.selectByPrimaryKey(adGroupId);
					AmzAdvAdgroupsHsa oldadgroup = new AmzAdvAdgroupsHsa();
					BeanUtil.copyProperties(adgroup, oldadgroup);
					adgroup.setAdgroupid(adGroupId);
					adgroup.setProfileid(profileid);
					adgroup.setOpttime(new Date());
					adgroup.setState(item.getString("state"));
					this.updateNotNull(adgroup);
					list.add(adgroup);
					AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
					operateLog.setAdgroupid(adgroup.getAdgroupid());
					operateLog.setCampaignid(adgroup.getCampaignid());
					operateLog.setProfileid(profileid);
					operateLog.setOperator(user.getId());
					operateLog.setOpttime(new Date());
					operateLog.setBeanclasz("AmzAdvAdgroups");
					String adgroupjson = GeneralUtil.toJSON(adgroup);
					String oldadgroupjson = GeneralUtil.toJSON(oldadgroup);
					operateLog.setAfterobject(adgroupjson);
					operateLog.setBeforeobject(oldadgroupjson);
					operateLogList.add(operateLog);
				}
				for(int i=0;i<error.size();i++) {
				    JSONObject item=error.getJSONObject(i);
				    errormsg=errormsg+item.toJSONString();
				}
				if(StrUtil.isNotBlank(errormsg)) {
					BaseException exception = new BaseException(errormsg);
					exception.setCode("ERROR");
					throw exception;
				}
				amzAdvOperateLogService.insertList(operateLogList);
				return list;
		}
		return null;
	}

	@Override
	public Map<String, Object> getSumAdGroup(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.amzAdvAdgroupsHsaMapper.getSumAdGroup(map);
	}

	@Override
	public List<Map<String, Object>> getAdgroupChart(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.amzAdvAdgroupsHsaMapper.getAdgroupChart(map);
	}
 
}
