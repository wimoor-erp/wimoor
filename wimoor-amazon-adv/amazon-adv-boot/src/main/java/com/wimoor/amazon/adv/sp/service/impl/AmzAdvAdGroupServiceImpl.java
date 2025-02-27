package com.wimoor.amazon.adv.sp.service.impl;

import java.math.BigDecimal;
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
import com.wimoor.amazon.adv.common.service.IAmzAdvBidReCommendService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvAdgroupsHsaService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvAdgroupsSDService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvAdgroupsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductTargeMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.adv.sp.service.IAmzAdvAdGroupService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvRecommendationsService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
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
	@Resource
	IAmzAdvAdgroupsHsaService amzAdvAdgroupsHsaService;
	@Resource
	ApiBuildService apiBuildService;
	@Resource
	AmzAdvProductTargeMapper amzAdvProductTargeMapper;
	@Resource
	@Lazy
	IAmzAdvKeywordsService amzAdvKeywordsService ;
	@Resource
	@Lazy
	IAmzAdvProductTargeService amzAdvProductTargeService ;
	
	@Resource
	@Lazy
	IAmzAdvRecommendationsService amzAdvRecommendationsService;
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
			AmzAdvCampaigns advcam = amzAdvCampaignService.selectByKey(campaignsid);
			if(list!=null && list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					AmzAdvAdgroups item = list.get(i);
					item.setCamname(advcam.getName());
				}
			}
			return list;
		} 
		return null;
	}

	  
	  public Map<String,String> getHeaderExt(){
	    	Map<String,String> header=new HashMap<String,String>();
	    	header.put("Content-Type", "application/vnd.spAdGroup.v3+json");
	    	header.put("Accept",       "application/vnd.spAdGroup.v3+json");
	    	return header;
	    }
	
	
	public List<AmzAdvAdgroups> amzCreateAdGroups(UserInfo user,BigInteger profileId, List<AmzAdvAdgroups> adgroups) {
		if (adgroups == null || adgroups.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/adGroups";
		JSONObject adGroups=new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < adgroups.size(); i++) {
			AmzAdvAdgroups adgroup = adgroups.get(i);
			JSONObject param = new JSONObject();
			param.put("name", adgroup.getName());
			param.put("campaignId", adgroup.getCampaignid().toString());
			param.put("state", adgroup.getState());
			param.put("defaultBid", adgroup.getDefaultbid());
			Example example = new Example(AmzAdvAdgroups.class);
			Criteria criter = example.createCriteria();
			criter.andEqualTo("name", adgroup.getName());
			criter.andEqualTo("profileid", profileId);
			criter.andEqualTo("campaignid", adgroup.getCampaignid());
			criter.andNotEqualTo("state", AdvState.archived);
			AmzAdvAdgroups oldamzAdvAdgroups = this.selectOneByExample(example);
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
				AmzAdvAdgroups adgroup = adgroups.get(index);
				BigInteger adGroupId = item.getBigInteger("adGroupId");
				adgroup.setAdgroupid(adGroupId);
				adgroup.setProfileid(profileId);
				adgroup.setOpttime(new Date());
				AmzAdvAdgroups Dbadgroup = this.selectByKey(adgroup);
				if(Dbadgroup == null) {
					this.save(adgroup);
					amzAdvAdgroupsMapper.getAdgroupCountByShopId(user.getCompanyid());
					amzAdvOperateLogService.saveOperateLog("AmzAdvAdgroups", user.getId(), profileId, adgroup, null);
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
			param.put("campaignId", adgroup.getCampaignid().toString());
			if (adgroup.getDefaultbid() != null) {
				param.put("defaultBid", adgroup.getDefaultbid());
			}
			param.put("adGroupId", adgroup.getAdgroupid().toString());
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
		JSONObject adGroups=new JSONObject();
		adGroups.put("adGroups", array);
		String response = apiBuildService.amzAdvPut(profile, url, adGroups.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
				String errormsg = "";
				JSONObject json = GeneralUtil.getJsonObject(response.toString());
				JSONObject campaignsJson = json.getJSONObject("adGroups");
				JSONArray success = campaignsJson.getJSONArray("success");
				JSONArray error = campaignsJson.getJSONArray("error");
				List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
				for(int i=0;i<success.size();i++) {
					JSONObject item=success.getJSONObject(i);
					Integer index = item.getInteger("index");
					AmzAdvAdgroups adgroup = adgroupList.get(index);
					BigInteger adGroupId = item.getBigInteger("adGroupId");
					adgroup.setAdgroupid(adGroupId);
					adgroup.setProfileid(profileId);
					adgroup.setOpttime(new Date());
					this.updateNotNull(adgroup);
					
					AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
					AmzAdvAdgroups oldadgroup = oldadgroupList.get(index);
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
				return adgroupList;
		}
		return null;
	}

 
	
	
	public List<AmzAdvAdgroups> amzDeleteAdGroups(UserInfo user,BigInteger profileId, JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/adGroups/delete";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		List<AmzAdvAdgroups> list = new LinkedList<AmzAdvAdgroups>();
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
					AmzAdvAdgroups adgroup = this.amzAdvAdgroupsMapper.selectByPrimaryKey(adGroupId);
					AmzAdvAdgroups oldadgroup = new AmzAdvAdgroups();
					BeanUtil.copyProperties(adgroup, oldadgroup);
					adgroup.setAdgroupid(adGroupId);
					adgroup.setProfileid(profileId);
					adgroup.setOpttime(new Date());
					adgroup.setState(item.getString("state"));
					this.updateNotNull(adgroup);
					list.add(adgroup);
					AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
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
 
	
	public List<AmzAdvAdgroups> amzListAdGroups(BigInteger profileId, JSONObject campaignsParam) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/adGroups/list";
		String response = apiBuildService.amzAdvPost(profile, url,campaignsParam.toString(),this.getHeaderExt());
		List<AmzAdvAdgroups> list = new LinkedList<AmzAdvAdgroups>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject json=GeneralUtil.getJsonObject(response);
			JSONArray a = json.getJSONArray("adGroups");
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
				JSONObject extendedData = item.getJSONObject("extendedData");
				if(extendedData!=null) {
					String servingStatus=extendedData.getString("servingStatus");
					adgroup.setServingStatus(servingStatus);
					Date creationDateTime=extendedData.getDate("creationDateTime");
					adgroup.setCreationDate(creationDateTime);
					Date lastUpdateDateTime=extendedData.getDate("lastUpdateDateTime");
					adgroup.setLastUpdatedDate(lastUpdateDateTime);
				}
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
			if("HSA".equals(campaignType)||"SB".equals(campaignType)) {
				return amzAdvAdgroupsHsaService.getAdgroupList(map, pageBounds);
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
				listSB = amzAdvAdgroupsHsaService.getAdgroupChart(map);
			}else if("SP".equals(campaignType.toUpperCase())) {
				listSP = amzAdvAdgroupsMapper.getAdgroupChart(map);
			}else if("SD".equals(campaignType.toUpperCase())) {
				listSD=amzAdvAdgroupsSDService.getAdgroupChart(map);
			}else if("ALL".equals(campaignType.toUpperCase())) {
				listSP = amzAdvAdgroupsMapper.getAdgroupChart(map);
				listSD=amzAdvAdgroupsSDService.getAdgroupChart(map);
				listSB = amzAdvAdgroupsHsaService.getAdgroupChart(map);
			}
		}
		return amzAdvCampaignService.getChartData(listSP,listSB,listSD, map);
	}
 
	
	public void getSerchStr(Map<String,Object> map) {
		String serch = (String) map.get("searchlist");
		String campaignType=map.get("campaignType").toString().toLowerCase();
		String[] serchArray = serch.split(",");
		String serchlist = "";
		for (int i = 0; i < serchArray.length; i++) {
			   if(campaignType.equals("sp")||campaignType.equals("sd")) {
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
			   }else {
				   if ("ACOS".equals(serchArray[i])) {
						serchlist = serchlist + "ifnull(sum(cost) / sum(attributedSales14d),0) ACOS ,";
					} else if ("ROAS".equals(serchArray[i])) {
						serchlist = serchlist + "ifnull(sum(attributedSales14d) / sum(cost),0) ROAS ,";
					} else if ("CSRT".equals(serchArray[i])) {
						serchlist = serchlist + "ifnull(sum(attributedConversions14d) / sum(clicks),0) CSRT ,";
					} else if ("avgcost".equals(serchArray[i])) {
						serchlist = serchlist + "ifnull((sum(cost) / sum(clicks)),0) avgcost ,";
					} else if ("CTR".equals(serchArray[i])) {
						serchlist = serchlist + "ifnull(sum(clicks) / sum(impressions),0) CTR ,";
					}else if ("sumUnits".equals(serchArray[i])) {
						 serchlist = serchlist +"sum(attributedConversions14d) sumUnits,";
					} else {
						serchlist = serchlist +"sum(" + serchArray[i] + ") " + serchArray[i] + ",";
					}
			   }
				
		}
		if(serchlist.contains(",")) {
			serchlist=serchlist.substring(0, serchlist.length()-1);
		}
		map.put("serchlist", serchlist);
		map.put("value1", serchArray[0]);
	}


	public List<Map<String, Object>> getSumAdGroup(Map<String, Object> map) {
		String campaignType = (String) map.get("campaignType");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Object paralist = map.get("paralist");
		if(paralist != null) {
			if(campaignType.toLowerCase().equals("sb")) {
				paralist = paralist.toString().replace("CSRT", "attributedConversions14d / clicks")
						.replace("ACOS", "cost / attributedSales14d").replace("ROAS", "attributedSales14d / cost");
			}else {
				paralist = paralist.toString().replace("CSRT", "attributedConversions7d / clicks")
						.replace("ACOS", "cost / attributedSales7d").replace("ROAS", "attributedSales7d / cost");
			}
			
			map.put("paralist", paralist);
		}
		if("HSA".equals(campaignType.toUpperCase()) || "SB".equals(campaignType.toUpperCase())) {
			Map<String, Object> sumMap =amzAdvAdgroupsHsaService.getSumAdGroup(map);
			if(sumMap != null && sumMap.size() > 0) {
				list.add(sumMap);
			}
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
			Map<String, Object> sumMap3 =amzAdvAdgroupsHsaService.getSumAdGroup(map);
			if(sumMap1 != null && sumMap1.size() > 0) {
				list.add(sumMap1);
			}
			if(sumMap2 != null && sumMap2.size() > 0) {
				list.add(sumMap2);
			}
			if(sumMap3 != null && sumMap3.size() > 0) {
				list.add(sumMap3);
			}
		}
		return list;
	}
    public List<AmzAdvAdgroups> convertJsonToBean(UserInfo user, BigInteger profileId, JSONArray adGroupArray){
    	List<AmzAdvAdgroups> adgroupsList = new ArrayList<AmzAdvAdgroups>();
		for (int i = 0; i < adGroupArray.size(); i++) {
			JSONObject adGroup = adGroupArray.getJSONObject(i);
			String adGroupName = adGroup.getString("name");
			BigInteger campaignid = adGroup.getBigInteger("campaignid");
			BigDecimal defaultbid = adGroup.getBigDecimal("defaultbid");
			String state = adGroup.getString("state");
			String id=adGroup.getString("adgroupid");
			AmzAdvAdgroups amzAdvAdgroups = new AmzAdvAdgroups();
			amzAdvAdgroups.setCampaignid(campaignid);
			amzAdvAdgroups.setName(adGroupName);
			amzAdvAdgroups.setState(state);
			if(id!=null) {
				amzAdvAdgroups.setAdgroupid(new BigInteger(id));
			}
			amzAdvAdgroups.setDefaultbid(defaultbid);
			adgroupsList.add(amzAdvAdgroups);
		}
		return adgroupsList;
    }
	@Override
	public List<AmzAdvAdgroups> amzCreateAdGroups(UserInfo user, BigInteger profileId, JSONArray adGroupArray) {
		// TODO Auto-generated method stub
		List<AmzAdvAdgroups> adgroupsList = this.convertJsonToBean(user, profileId, adGroupArray);
		return this.amzCreateAdGroups(user, profileId, adgroupsList);
	}

	@Override
	public  List<AmzAdvAdgroups> amzUpdateAdGroups(UserInfo user, BigInteger profileId, JSONArray adGroupArray) {
		// TODO Auto-generated method stub
		List<AmzAdvAdgroups> adgroupsList = this.convertJsonToBean(user, profileId, adGroupArray);
		return this.amzUpdateAdGroups(user, profileId, adgroupsList);
	}

	@Override
	public String getAdGroupsBid(UserInfo user, BigInteger profileid, BigInteger adgroupid) {
		// TODO Auto-generated method stub
		AmzAdvAdgroups adgroup = amzAdvAdgroupsMapper.selectByPrimaryKey(adgroupid);
		Example keywordExp=new Example(AmzAdvKeywords.class);
		Criteria crit = keywordExp.createCriteria();
		crit.andEqualTo("adgroupid", adgroupid);
		crit.andEqualTo("profileid", profileid);
		crit.andEqualTo("campaignid", adgroup.getCampaignid());
		List<AmzAdvKeywords> keylist = amzAdvKeywordsService.selectByExample(keywordExp);
		JSONObject param=new JSONObject();
		param.put("adGroupId", adgroupid);
		param.put("recommendationType",  "BIDS_FOR_EXISTING_AD_GROUP");
		param.put("campaignId",  adgroup.getCampaignid());
		JSONArray targetingExpressions=new JSONArray();
		if(keylist!=null && keylist.size()>0) {
			for(AmzAdvKeywords key:keylist) {
				JSONObject item=new JSONObject();
				if(key.getMatchtype().toLowerCase().equals("exact")) {
					item.put("type","KEYWORD_EXACT_MATCH");
				}
				if(key.getMatchtype().toLowerCase().equals("broad")) {
					item.put("type","KEYWORD_BROAD_MATCH");
				}
				if(key.getMatchtype().toLowerCase().equals("phrase")) {
					item.put("type","KEYWORD_PHRASE_MATCH");
				}
				item.put("value", key.getKeywordtext());
				targetingExpressions.add(item);
			}
			param.put("targetingExpressions", targetingExpressions);
			return amzAdvRecommendationsService.amzAdvRecommendationsTargetsBid(user, profileid, param);
		}else {
			AmzAdvCampaigns camp = amzAdvCampaignService.selectByKey(adgroup.getCampaignid());
			if(camp.getTargetingType().toLowerCase().equals("auto")) {
				Example targetExp=new Example(AmzAdvProductTarge.class);
				Criteria tcrit = targetExp.createCriteria();
				tcrit.andEqualTo("adgroupid", adgroupid);
				tcrit.andEqualTo("profileid", profileid);
				tcrit.andEqualTo("campaignid", adgroup.getCampaignid());
				List<AmzAdvProductTarge> targetlist = amzAdvProductTargeService.selectByExample(targetExp);
				for(AmzAdvProductTarge target:targetlist) {
					JSONObject item=new JSONObject();
				    String expression = target.getExpression().toUpperCase();
				    expression= expression.replace("_", "");
					if(expression.contains("QUERYBROADRELMATCHES")) {
						item.put("type","CLOSE_MATCH");
					}
					if(expression.contains("QUERYHIGHRELMATCHES")) {
						item.put("type","LOOSE_MATCH");
					}
					if(expression.contains("ASINSUBSTITUTERELATED")) {
						item.put("type","SUBSTITUTES");
					}
					if(expression.contains("ASINACCESSORYRELATED")) {
						item.put("type","COMPLEMENTS");
					}
					targetingExpressions.add(item);
				}
				param.put("targetingExpressions", targetingExpressions);
				return amzAdvRecommendationsService.amzAdvRecommendationsTargetsBid(user, profileid, param);
			}else{
				return null;
			}
			
		}
		
	}

}
