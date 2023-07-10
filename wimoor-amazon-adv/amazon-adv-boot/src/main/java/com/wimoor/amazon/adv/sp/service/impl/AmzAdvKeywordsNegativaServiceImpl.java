package com.wimoor.amazon.adv.sp.service.impl;

import java.math.BigInteger;
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
import com.wimoor.amazon.adv.common.pojo.AdvState;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvKeywordsNegativaMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsNegativaService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvKeywordsNegativaService")
public class AmzAdvKeywordsNegativaServiceImpl extends BaseService<AmzAdvKeywordsNegativa>implements IAmzAdvKeywordsNegativaService {
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvKeywordsNegativaMapper amzAdvKeywordsNegativaMapper;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	
	public AmzAdvKeywordsNegativa selectOneByExample(Example example) {
		return amzAdvKeywordsNegativaMapper.selectOneByExample(example);
	}

	public List<AmzAdvKeywordsNegativa> getKeywordsByAdgroupId(BigInteger profileId, String adGroupId) {
		// TODO Auto-generated method stub
		if ( profileId!=null&&StringUtil.isNotEmpty(adGroupId)) {
			Example example = new Example(AmzAdvKeywordsNegativa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("adgroupid", adGroupId);
			example.setOrderByClause("keywordText asc");
			List<AmzAdvKeywordsNegativa> list = amzAdvKeywordsNegativaMapper.selectByExample(example);
			return list;
		}  
		return null;
	}

	public AmzAdvKeywordsNegativa amzGetNegativaKeyword(UserInfo user,BigInteger  profileId, String keywordId, String campaignType) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = "";
		if(StringUtil.isNotEmpty(campaignType)){
			if("hsa".equals(campaignType)) {
				String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/negativeKeywords/" + keywordId;
				response = amzAdvAuthService.amzAdvGet_V3(profile, url);
			} else {
				String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sp + "/negativeKeywords/" + keywordId;
				response = amzAdvAuthService.amzAdvGet(profile, url);
			}
		}
		AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativa();
			amzAdvKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
			amzAdvKeywordsNegativa.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvKeywordsNegativa.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvKeywordsNegativa.setState(item.getString("state"));
			amzAdvKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
			amzAdvKeywordsNegativa.setMatchtype(item.getString("matchType"));
			amzAdvKeywordsNegativa.setCampaigntype(campaignType);
			amzAdvKeywordsNegativa.setProfileid(profileId);
			amzAdvKeywordsNegativa.setOpttime(new Date());
			AmzAdvKeywordsNegativa oldamzAdvKeywordsNegativa = amzAdvKeywordsNegativaMapper.selectByPrimaryKey(amzAdvKeywordsNegativa);
			if (oldamzAdvKeywordsNegativa != null) {
				this.updateAll(amzAdvKeywordsNegativa);
			} else {
				this.save(amzAdvKeywordsNegativa);
			}
			return amzAdvKeywordsNegativa;
		}
		return null;
	}

	public AmzAdvKeywordsNegativa amzGetNegativaKeywordEx(UserInfo user,BigInteger  profileId, String keywordId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/negativeKeywords/extended/" + keywordId;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativa();
			amzAdvKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
			amzAdvKeywordsNegativa.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvKeywordsNegativa.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvKeywordsNegativa.setState(item.getString("state"));
			amzAdvKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
			amzAdvKeywordsNegativa.setMatchtype(item.getString("matchType"));
			amzAdvKeywordsNegativa.setCreationDate(item.getDate("creationDate"));
			amzAdvKeywordsNegativa.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
			amzAdvKeywordsNegativa.setServingStatus(item.getString("servingStatus"));
			amzAdvKeywordsNegativa.setCampaigntype("sp");
			amzAdvKeywordsNegativa.setProfileid(profileId);
			amzAdvKeywordsNegativa.setOpttime(new Date());
			AmzAdvKeywordsNegativa oldamzAdvKeywordsNegativa = amzAdvKeywordsNegativaMapper.selectByPrimaryKey(amzAdvKeywordsNegativa);
			if (oldamzAdvKeywordsNegativa != null) {
				this.updateAll(amzAdvKeywordsNegativa);
			} else {
				this.save(amzAdvKeywordsNegativa);
			}
			return amzAdvKeywordsNegativa;
		}
		return null;
	}

	public List<AmzAdvKeywordsNegativa> amzCreateNegativaKeywords(UserInfo user,BigInteger profileId,List<AmzAdvKeywordsNegativa> negetivaKeywordList) {
		// TODO Auto-generated method stub
		if (negetivaKeywordList == null || negetivaKeywordList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		JSONArray array = new JSONArray();
		String response = "";
		AmzAdvCampaigns keyObject = new AmzAdvCampaigns();
		keyObject.setCampaignid(negetivaKeywordList.get(0).getCampaignid());
		keyObject.setProfileid(profileId);
		AmzAdvCampaigns campaign = amzAdvCampaignService.selectByKey(keyObject);
		String compaignType = "sp";
		if (campaign == null) {
			compaignType = "hsa";
			for (int i = 0; i < negetivaKeywordList.size(); i++) {
				AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = negetivaKeywordList.get(i);
				amzAdvKeywordsNegativa.setState(AdvState.pending);
				JSONObject param = new JSONObject();
				// campaignId, adGroupId, keywordText, matchType
				param.put("campaignId", amzAdvKeywordsNegativa.getCampaignid());
				param.put("adGroupId", amzAdvKeywordsNegativa.getAdgroupid());
				param.put("keywordText", amzAdvKeywordsNegativa.getKeywordtext());
				param.put("matchType", amzAdvKeywordsNegativa.getMatchtype());
				//param.put("state", amzAdvKeywordsNegativa.getState());

				array.add(param);
			}
			String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/negativeKeywords";
			response = amzAdvAuthService.amzAdvPost_V3(profile, url, array.toString());
		}else {
			for (int i = 0; i < negetivaKeywordList.size(); i++) {
				AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = negetivaKeywordList.get(i);
				amzAdvKeywordsNegativa.setState(AdvState.enabled);
				JSONObject param = new JSONObject();
				// campaignId, adGroupId, keywordText, matchType
				param.put("campaignId", amzAdvKeywordsNegativa.getCampaignid());
				param.put("adGroupId", amzAdvKeywordsNegativa.getAdgroupid());
				param.put("keywordText", amzAdvKeywordsNegativa.getKeywordtext());
				param.put("matchType", amzAdvKeywordsNegativa.getMatchtype());
				param.put("state", amzAdvKeywordsNegativa.getState());

				array.add(param);
			}
			String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sp + "/negativeKeywords";
			response = amzAdvAuthService.amzAdvPost(profile, url, array.toString());
		}
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < negetivaKeywordList.size(); i++) {
				AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = negetivaKeywordList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger keywordId = item.getBigInteger("keywordId");
					amzAdvKeywordsNegativa.setKeywordid(keywordId);
					amzAdvKeywordsNegativa.setCampaigntype(compaignType);
					amzAdvKeywordsNegativa.setProfileid(profile.getId());
					amzAdvKeywordsNegativa.setOpttime(new Date());
					AmzAdvKeywordsNegativa DbamzAdvKeywordsNegativa = this.selectByKey(amzAdvKeywordsNegativa);
					if(DbamzAdvKeywordsNegativa == null) {
						this.save(amzAdvKeywordsNegativa);
					}
				}else {
					String keywordText = amzAdvKeywordsNegativa.getKeywordtext();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("否定关键词：'"+keywordText+"' 创建失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			Map<BigInteger, List<Object>> map = new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < negetivaKeywordList.size(); i++) {
				AmzAdvKeywordsNegativa keywordsNegativa = negetivaKeywordList.get(i);
				BigInteger key = keywordsNegativa.getCampaignid().subtract(keywordsNegativa.getAdgroupid());
				List<Object> keywordslist = map.get(key);
				if(keywordslist==null) {
					keywordslist=new ArrayList<Object>();
				    map.put(key, keywordslist);
				}
				keywordslist.add(keywordsNegativa);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvKeywordsNegativa", user.getId(), profileId, map, null);
			return negetivaKeywordList;
		}else {
			throw new BaseException("否定关键词创建失败！");
		}
	}

	public List<AmzAdvKeywordsNegativa> amzUpdateNegativaKeywords(UserInfo user,BigInteger profileId,List<AmzAdvKeywordsNegativa> negetivaKeywordList) {
		// TODO Auto-generated method stub
		if (negetivaKeywordList == null || negetivaKeywordList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		List<AmzAdvKeywordsNegativa> oldNegetivaKeywordList = new ArrayList<AmzAdvKeywordsNegativa>();
		String campaignType = "";
		JSONArray array = new JSONArray();
		for (int i = 0; i < negetivaKeywordList.size(); i++) {
			AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = negetivaKeywordList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("campaignId", amzAdvKeywordsNegativa.getCampaignid());
			param.put("adGroupId", amzAdvKeywordsNegativa.getAdgroupid());
			param.put("keywordId", amzAdvKeywordsNegativa.getKeywordid());
			if(amzAdvKeywordsNegativa.getKeywordtext() != null) {
				param.put("keywordText", amzAdvKeywordsNegativa.getKeywordtext());
			}
			if(amzAdvKeywordsNegativa.getState() != null) {
				param.put("state", amzAdvKeywordsNegativa.getState());
			}
			param.put("matchType", amzAdvKeywordsNegativa.getMatchtype());
			array.add(param);
			
			Example example = new Example(AmzAdvKeywordsNegativa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("keywordid", amzAdvKeywordsNegativa.getKeywordid());
			AmzAdvKeywordsNegativa oldamzAdvKeywordsNegativa = amzAdvKeywordsNegativaMapper.selectOneByExample(example);
			campaignType = oldamzAdvKeywordsNegativa.getCampaigntype();
			oldNegetivaKeywordList.add(oldamzAdvKeywordsNegativa);
		}
		String response = "";
		if(StringUtil.isNotEmpty(campaignType)){
			if("hsa".equals(campaignType)) {
				String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/negativeKeywords";
				response = amzAdvAuthService.amzAdvPost_V3(profile, url, array.toString());
			}else {
				String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sp + "/negativeKeywords";
				response = amzAdvAuthService.amzAdvPost(profile, url, array.toString());
			}
		}
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < negetivaKeywordList.size(); i++) {
				AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = negetivaKeywordList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger keywordId = item.getBigInteger("keywordId");
					amzAdvKeywordsNegativa.setKeywordid(keywordId);
					amzAdvKeywordsNegativa.setProfileid(profile.getId());
					amzAdvKeywordsNegativa.setOpttime(new Date());
					this.updateNotNull(amzAdvKeywordsNegativa);
				}else {
					String keywordText = amzAdvKeywordsNegativa.getKeywordtext();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("否定关键词：'"+keywordText+"' 修改失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			Map<BigInteger, List<Object>> oldmap=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < negetivaKeywordList.size(); i++) {
				AmzAdvKeywordsNegativa keywordsNegativa = negetivaKeywordList.get(i);
				BigInteger key = keywordsNegativa.getCampaignid().subtract(keywordsNegativa.getAdgroupid());
				List<Object> keywordslist = map.get(key);
				if(keywordslist==null) {
					keywordslist=new ArrayList<Object>();
				    map.put(key, keywordslist);
				}
				keywordslist.add(keywordsNegativa);
			}
			for (int i = 0; i < oldNegetivaKeywordList.size(); i++) {
				AmzAdvKeywordsNegativa oldKeywordsNegativa = oldNegetivaKeywordList.get(i);
				BigInteger key = oldKeywordsNegativa.getCampaignid().subtract(oldKeywordsNegativa.getAdgroupid());
				List<Object> keywordslist = oldmap.get(key);
				if(keywordslist==null) {
					keywordslist=new ArrayList<Object>();
					oldmap.put(key, keywordslist);
				}
				keywordslist.add(oldKeywordsNegativa);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvKeywordsNegativa", user.getId(), profileId, map, oldmap);
			return negetivaKeywordList;
		}
		return null;
	}

	public AmzAdvKeywordsNegativa amzArchiveNegativaKeyword(UserInfo user,BigInteger  profileId, String keywordId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		Example example = new Example(AmzAdvKeywordsNegativa.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("profileid", profileId);
		crit.andEqualTo("keywordid", keywordId);
		example.setOrderByClause("keywordText asc");
		AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = amzAdvKeywordsNegativaMapper.selectOneByExample(example);
		AmzAdvKeywordsNegativa oldKeywordsNegativa = amzAdvKeywordsNegativaMapper.selectOneByExample(example);
		String response = "";
		if(amzAdvKeywordsNegativa == null || oldKeywordsNegativa == null) {
			throw new BaseException("要归档的关键词不存在！");
		}
		if(StringUtil.isNotEmpty(amzAdvKeywordsNegativa.getCampaigntype())) {
			if("hsa".equals(amzAdvKeywordsNegativa.getCampaigntype())){
				String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/negativeKeywords/" + keywordId;
				response = amzAdvAuthService.amzAdvDelete_V3(profile, url);
			}else {
				String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sp + "/negativeKeywords/" + keywordId;
				response = amzAdvAuthService.amzAdvDelete(profile, url);
			}
		}
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				amzAdvKeywordsNegativa.setState(AdvState.archived);
				amzAdvKeywordsNegativa.setOpttime(new Date());
				this.updateAll(amzAdvKeywordsNegativa);
				amzAdvOperateLogService.saveOperateLog("AmzAdvKeywordsNegativa", user.getId(), profileId, amzAdvKeywordsNegativa, oldKeywordsNegativa);
				return amzAdvKeywordsNegativa;
			}else {
				String errormsg = "";
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + a.getString("description");
				BaseException exception = new BaseException("否定关键词修改失败："+errormsg);
				exception.setCode("ERROER");
				throw exception;
			}
		}
		return null;
	}

	public List<AmzAdvKeywordsNegativa> amzListNegativaKeywords(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/negativeKeywords/?";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "startIndex");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "count");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "matchTypeFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "keywordText");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "stateFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "campaignIdFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "adGroupIdFilter");
		}
		url = url + ("".equals(paramurl) ? "" : paramurl);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvKeywordsNegativa> list = new LinkedList<AmzAdvKeywordsNegativa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativa();
				amzAdvKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvKeywordsNegativa.setAdgroupid(item.getBigInteger("campaignId"));
				amzAdvKeywordsNegativa.setCampaignid(item.getBigInteger("adGroupId"));
				amzAdvKeywordsNegativa.setState(item.getString("state"));
				amzAdvKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
				amzAdvKeywordsNegativa.setMatchtype(item.getString("matchType"));
				amzAdvKeywordsNegativa.setCampaigntype("sp");
				amzAdvKeywordsNegativa.setProfileid(profileId);
				amzAdvKeywordsNegativa.setOpttime(new Date());
				AmzAdvKeywordsNegativa oldamzAdvKeywordsNegativa = amzAdvKeywordsNegativaMapper
						.selectByPrimaryKey(amzAdvKeywordsNegativa);
				if (oldamzAdvKeywordsNegativa != null) {
					this.updateAll(amzAdvKeywordsNegativa);
				} else {
					this.save(amzAdvKeywordsNegativa);
				}
				list.add(amzAdvKeywordsNegativa);
			}
			return list;
		}
		return null;
	}

	public List<AmzAdvKeywordsNegativa> amzNegativaKeywordsEx(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/negativeKeywords/extended?";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "startIndex");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "count");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "matchTypeFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "keywordText");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "stateFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "campaignIdFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "adGroupIdFilter");
		}
		url = url + ("".equals(paramurl) ? "" : paramurl);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvKeywordsNegativa> list = new LinkedList<AmzAdvKeywordsNegativa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativa();
				amzAdvKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvKeywordsNegativa.setAdgroupid(item.getBigInteger("campaignId"));
				amzAdvKeywordsNegativa.setCampaignid(item.getBigInteger("adGroupId"));
				amzAdvKeywordsNegativa.setState(item.getString("state"));
				amzAdvKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
				amzAdvKeywordsNegativa.setMatchtype(item.getString("matchType"));
				amzAdvKeywordsNegativa.setCreationDate(item.getDate("creationDate"));
				amzAdvKeywordsNegativa.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				amzAdvKeywordsNegativa.setServingStatus(item.getString("servingStatus"));
				amzAdvKeywordsNegativa.setCampaigntype("sp");
				amzAdvKeywordsNegativa.setProfileid(profileId);
				amzAdvKeywordsNegativa.setOpttime(new Date());
				AmzAdvKeywordsNegativa oldamzAdvKeywordsNegativa = amzAdvKeywordsNegativaMapper
						.selectByPrimaryKey(amzAdvKeywordsNegativa);
				if (oldamzAdvKeywordsNegativa != null) {
					this.updateAll(amzAdvKeywordsNegativa);
				} else {
					this.save(amzAdvKeywordsNegativa);
				}
				list.add(amzAdvKeywordsNegativa);
			}
			return list;
		}
		return null;
	}

	public List<Map<String, Object>> getNegativaKeywordsList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return amzAdvKeywordsNegativaMapper.getNegativaKeywordsList(map);
	}

	public AmzAdvKeywordsNegativa getNegativaKeywordforQuery(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String profileid = map.get("profileid") == null ? null : map.get("profileid").toString();
		String keywordtext = map.get("query") == null ? null : map.get("query").toString();
		//String matchtype = map.get("matchtype") == null ? null : map.get("matchtype").toString();
		String adGroupid = map.get("adGroupid") == null ? null : map.get("adGroupid").toString();
		String campaignid = map.get("campaignid") == null ? null : map.get("campaignid").toString();
		Example example = new Example(AmzAdvKeywordsNegativa.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("state", "enabled");
		if(profileid != null)
			crit.andEqualTo("profileid", profileid);
		if(adGroupid != null)
			crit.andEqualTo("adgroupid", adGroupid);
		if(keywordtext != null)
			crit.andEqualTo("keywordtext", keywordtext);
		/*if(matchtype != null)
			crit.andEqualTo("matchtype", matchtype);*/
		if(campaignid != null)
			crit.andEqualTo("campaignid", campaignid);
		List<AmzAdvKeywordsNegativa> list = this.selectByExample(example);
		if(list != null && list.size() > 0) {
			map.put("keywordNegativaFlag", true);
			return list.get(0);
		}
		return null;
	}
	
}
