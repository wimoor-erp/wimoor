package com.wimoor.amazon.adv.sb.service.impl;

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
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvKeywordsNegativaHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvKeywordsNegativaHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvKeywordsNegativaHsaService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.bean.BeanUtil;

import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvKeywordsNegativaHsaService")
public class AmzAdvKeywordsNegativaHsaServiceImpl extends BaseService<AmzAdvKeywordsNegativaHsa>implements IAmzAdvKeywordsNegativaHsaService {
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvKeywordsNegativaHsaMapper amzAdvKeywordsNegativaHsaMapper;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	@Resource
	ApiBuildService apiBuildService;
	public AmzAdvKeywordsNegativaHsa selectOneByExample(Example example) {
		return amzAdvKeywordsNegativaHsaMapper.selectOneByExample(example);
	}

	public List<AmzAdvKeywordsNegativaHsa> getKeywordsByAdgroupId(BigInteger profileId, String adGroupId) {
		// TODO Auto-generated method stub
		if ( profileId!=null&&StringUtil.isNotEmpty(adGroupId)) {
			Example example = new Example(AmzAdvKeywordsNegativa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("adgroupid", adGroupId);
			example.setOrderByClause("keywordText asc");
			List<AmzAdvKeywordsNegativaHsa> list = amzAdvKeywordsNegativaHsaMapper.selectByExample(example);
			return list;
		}  
		return null;
	}

	public AmzAdvKeywordsNegativaHsa amzGetNegativaKeyword(UserInfo user,BigInteger  profileId, String keywordId, String campaignType) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = "";
		String url = "/sb/negativeKeywords/" + keywordId;
		response = apiBuildService.amzAdvGet(profile, url);
		AmzAdvKeywordsNegativaHsa amzAdvKeywordsNegativa = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativaHsa();
			amzAdvKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
			amzAdvKeywordsNegativa.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvKeywordsNegativa.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvKeywordsNegativa.setState(item.getString("state"));
			amzAdvKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
			amzAdvKeywordsNegativa.setMatchtype(item.getString("matchType"));
			amzAdvKeywordsNegativa.setCampaigntype(campaignType);
			amzAdvKeywordsNegativa.setProfileid(profileId);
			amzAdvKeywordsNegativa.setOpttime(new Date());
			AmzAdvKeywordsNegativaHsa oldamzAdvKeywordsNegativa = amzAdvKeywordsNegativaHsaMapper.selectByPrimaryKey(amzAdvKeywordsNegativa);
			if (oldamzAdvKeywordsNegativa != null) {
				this.updateAll(amzAdvKeywordsNegativa);
			} else {
				this.save(amzAdvKeywordsNegativa);
			}
			return amzAdvKeywordsNegativa;
		}
		return null;
	}

	public AmzAdvKeywordsNegativaHsa amzGetNegativaKeywordEx(UserInfo user,BigInteger  profileId, String keywordId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/negativeKeywords/" + keywordId;
		String response = apiBuildService.amzAdvGet(profile, url);
		AmzAdvKeywordsNegativaHsa amzAdvKeywordsNegativa = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativaHsa();
			amzAdvKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
			amzAdvKeywordsNegativa.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvKeywordsNegativa.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvKeywordsNegativa.setState(item.getString("state"));
			amzAdvKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
			amzAdvKeywordsNegativa.setMatchtype(item.getString("matchType"));
			amzAdvKeywordsNegativa.setCampaigntype("sb");
			amzAdvKeywordsNegativa.setProfileid(profileId);
			amzAdvKeywordsNegativa.setOpttime(new Date());
			AmzAdvKeywordsNegativaHsa oldamzAdvKeywordsNegativa = amzAdvKeywordsNegativaHsaMapper.selectByPrimaryKey(amzAdvKeywordsNegativa);
			if (oldamzAdvKeywordsNegativa != null) {
				this.updateAll(amzAdvKeywordsNegativa);
			} else {
				this.save(amzAdvKeywordsNegativa);
			}
			return amzAdvKeywordsNegativa;
		}
		return null;
	}

	public List<AmzAdvKeywordsNegativaHsa> amzCreateNegativaKeywords(UserInfo user,BigInteger profileId,List<AmzAdvKeywordsNegativaHsa> negetivaKeywordList) {
		// TODO Auto-generated method stub
		if (negetivaKeywordList == null || negetivaKeywordList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		JSONArray array = new JSONArray();
		String response = "";
		AmzAdvCampaigns keyObject = new AmzAdvCampaigns();
		keyObject.setCampaignid(negetivaKeywordList.get(0).getCampaignid());
		keyObject.setProfileid(profileId);
		String  compaignType = "hsa";
			for (int i = 0; i < negetivaKeywordList.size(); i++) {
				AmzAdvKeywordsNegativaHsa amzAdvKeywordsNegativa = negetivaKeywordList.get(i);
				amzAdvKeywordsNegativa.setState(AdvState.pending);
				JSONObject param = new JSONObject();
				// campaignId, adGroupId, keywordText, matchType
				param.put("campaignId", amzAdvKeywordsNegativa.getCampaignid());
				param.put("adGroupId", amzAdvKeywordsNegativa.getAdgroupid());
				param.put("keywordText", amzAdvKeywordsNegativa.getKeywordtext());
				if( amzAdvKeywordsNegativa.getMatchtype().toLowerCase().contains("exact")) {
					param.put("matchType", "negativeExact");
				}else {
					param.put("matchType", "negativePhrase");
				}
				array.add(param);
			}
			String url = "/sb/negativeKeywords";
			response = apiBuildService.amzAdvPost(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < negetivaKeywordList.size(); i++) {
				AmzAdvKeywordsNegativaHsa amzAdvKeywordsNegativa = negetivaKeywordList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger keywordId = item.getBigInteger("keywordId");
					amzAdvKeywordsNegativa.setKeywordid(keywordId);
					amzAdvKeywordsNegativa.setCampaigntype(compaignType);
					amzAdvKeywordsNegativa.setProfileid(profile.getId());
					amzAdvKeywordsNegativa.setOpttime(new Date());
					AmzAdvKeywordsNegativaHsa DbamzAdvKeywordsNegativa = this.selectByKey(amzAdvKeywordsNegativa);
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
				AmzAdvKeywordsNegativaHsa keywordsNegativa = negetivaKeywordList.get(i);
				BigInteger key = keywordsNegativa.getCampaignid().subtract(keywordsNegativa.getAdgroupid());
				List<Object> keywordslist = map.get(key);
				if(keywordslist==null) {
					keywordslist=new ArrayList<Object>();
				    map.put(key, keywordslist);
				}
				keywordslist.add(keywordsNegativa);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvKeywordsNegativaHsa", user.getId(), profileId, map, null);
			return negetivaKeywordList;
		}else {
			throw new BaseException("否定关键词创建失败！");
		}
	}

	public List<AmzAdvKeywordsNegativaHsa> amzUpdateNegativaKeywords(UserInfo user,BigInteger profileId,List<AmzAdvKeywordsNegativaHsa> negetivaKeywordList) {
		// TODO Auto-generated method stub
		if (negetivaKeywordList == null || negetivaKeywordList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		List<AmzAdvKeywordsNegativaHsa> oldNegetivaKeywordList = new ArrayList<AmzAdvKeywordsNegativaHsa>();
		JSONArray array = new JSONArray();
		for (int i = 0; i < negetivaKeywordList.size(); i++) {
			AmzAdvKeywordsNegativaHsa amzAdvKeywordsNegativa = negetivaKeywordList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("campaignId", amzAdvKeywordsNegativa.getCampaignid());
			param.put("adGroupId", amzAdvKeywordsNegativa.getAdgroupid());
			param.put("keywordId", amzAdvKeywordsNegativa.getKeywordid());
			if(amzAdvKeywordsNegativa.getState() != null) {
				param.put("state", amzAdvKeywordsNegativa.getState());
			}
			array.add(param);
			Example example = new Example(AmzAdvKeywordsNegativa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("keywordid", amzAdvKeywordsNegativa.getKeywordid());
			AmzAdvKeywordsNegativaHsa oldamzAdvKeywordsNegativa = amzAdvKeywordsNegativaHsaMapper.selectOneByExample(example);
			oldNegetivaKeywordList.add(oldamzAdvKeywordsNegativa);
		}
		String response = "";
		String url = "/sb/negativeKeywords";
			response = apiBuildService.amzAdvPut(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < negetivaKeywordList.size(); i++) {
				AmzAdvKeywordsNegativaHsa amzAdvKeywordsNegativa = negetivaKeywordList.get(i);
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
				AmzAdvKeywordsNegativaHsa keywordsNegativa = negetivaKeywordList.get(i);
				BigInteger key = keywordsNegativa.getCampaignid().subtract(keywordsNegativa.getAdgroupid());
				List<Object> keywordslist = map.get(key);
				if(keywordslist==null) {
					keywordslist=new ArrayList<Object>();
				    map.put(key, keywordslist);
				}
				keywordslist.add(keywordsNegativa);
			}
			for (int i = 0; i < oldNegetivaKeywordList.size(); i++) {
				AmzAdvKeywordsNegativaHsa oldKeywordsNegativa = oldNegetivaKeywordList.get(i);
				BigInteger key = oldKeywordsNegativa.getCampaignid().subtract(oldKeywordsNegativa.getAdgroupid());
				List<Object> keywordslist = oldmap.get(key);
				if(keywordslist==null) {
					keywordslist=new ArrayList<Object>();
					oldmap.put(key, keywordslist);
				}
				keywordslist.add(oldKeywordsNegativa);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvKeywordsNegativaHsa", user.getId(), profileId, map, oldmap);
			return negetivaKeywordList;
		}
		return null;
	}

 

	public List<AmzAdvKeywordsNegativaHsa> amzListNegativaKeywords(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
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
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		List<AmzAdvKeywordsNegativaHsa> list = new LinkedList<AmzAdvKeywordsNegativaHsa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvKeywordsNegativaHsa amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativaHsa();
				amzAdvKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvKeywordsNegativa.setAdgroupid(item.getBigInteger("campaignId"));
				amzAdvKeywordsNegativa.setCampaignid(item.getBigInteger("adGroupId"));
				amzAdvKeywordsNegativa.setState(item.getString("state"));
				amzAdvKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
				amzAdvKeywordsNegativa.setMatchtype(item.getString("matchType"));
				amzAdvKeywordsNegativa.setCampaigntype("sp");
				amzAdvKeywordsNegativa.setProfileid(profileId);
				amzAdvKeywordsNegativa.setOpttime(new Date());
				AmzAdvKeywordsNegativaHsa oldamzAdvKeywordsNegativa = amzAdvKeywordsNegativaHsaMapper
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

	public List<AmzAdvKeywordsNegativaHsa> amzNegativaKeywordsEx(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
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
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		List<AmzAdvKeywordsNegativaHsa> list = new LinkedList<AmzAdvKeywordsNegativaHsa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvKeywordsNegativaHsa amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativaHsa();
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
				AmzAdvKeywordsNegativaHsa oldamzAdvKeywordsNegativa = amzAdvKeywordsNegativaHsaMapper
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
		return amzAdvKeywordsNegativaHsaMapper.getNegativaKeywordsList(map);
	}

	public AmzAdvKeywordsNegativaHsa getNegativaKeywordforQuery(Map<String, Object> map) {
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
		List<AmzAdvKeywordsNegativaHsa> list = this.selectByExample(example);
		if(list != null && list.size() > 0) {
			map.put("keywordNegativaFlag", true);
			return list.get(0);
		}
		return null;
	}

	 public List<AmzAdvKeywordsNegativaHsa> convertJsonToBean(UserInfo user, BigInteger profileId, JSONArray nkeywordArray){
			List<AmzAdvKeywordsNegativaHsa> list = new ArrayList<AmzAdvKeywordsNegativaHsa>();
		for (int i = 0; i < nkeywordArray.size(); i++) {
			JSONObject keyword = nkeywordArray.getJSONObject(i);
			String matchType = keyword.getString("matchtype");
			String keywordText = keyword.getString("keywordtext");
			String state = keyword.getString("status");
			BigInteger campaignid = keyword.getBigInteger("campaignid");
			BigInteger keywordid = keyword.getBigInteger("id");
			BigInteger adGroupid = keyword.getBigInteger("adgroupid");
			AmzAdvKeywordsNegativaHsa amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativaHsa();
			amzAdvKeywordsNegativa.setKeywordid(keywordid);
			amzAdvKeywordsNegativa.setCampaignid(campaignid);
			amzAdvKeywordsNegativa.setAdgroupid(adGroupid);
			amzAdvKeywordsNegativa.setProfileid(profileId);
			amzAdvKeywordsNegativa.setMatchtype(matchType);
			amzAdvKeywordsNegativa.setKeywordtext(keywordText);
			amzAdvKeywordsNegativa.setState(state);
			amzAdvKeywordsNegativa.setOpttime(new Date());
		    list.add(amzAdvKeywordsNegativa);
		}
		return list;
	}
	 
	@Override
	public List<AmzAdvKeywordsNegativaHsa> amzCreateNegativaKeywords(UserInfo user, String profileid,
			JSONArray nkeywords) {
		// TODO Auto-generated method stub
		return this.amzCreateNegativaKeywords(user, new BigInteger(profileid), this.convertJsonToBean(user, new BigInteger(profileid), nkeywords));
	}

	@Override
	public  AmzAdvKeywordsNegativaHsa  amzDeleteNegativaKeywords(UserInfo user, BigInteger profileid, String id) {
		// TODO Auto-generated method stub
				AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileid);
				List<AmzAdvKeywordsNegativaHsa> oldNegetivaKeywordList = new ArrayList<AmzAdvKeywordsNegativaHsa>();
				List<AmzAdvKeywordsNegativaHsa> negetivaKeywordList = new ArrayList<AmzAdvKeywordsNegativaHsa>();
				String response = "";
				String url = "/sb/negativeKeywords/"+id;
					response = apiBuildService.amzAdvGet(profile, url);
				if (StringUtil.isNotEmpty(response)) {
					String errormsg = "";
					JSONObject item = GeneralUtil.getJsonObject(response.toString());
						if ("SUCCESS".equals(item.getString("code"))) {
							AmzAdvKeywordsNegativaHsa amzAdvKeywordsNegativa = this.selectByKey(oldNegetivaKeywordList);
							AmzAdvKeywordsNegativaHsa old=new AmzAdvKeywordsNegativaHsa();
							BeanUtil.copyProperties(amzAdvKeywordsNegativa, old, false);
							oldNegetivaKeywordList.add(old);
							BigInteger keywordId = item.getBigInteger("keywordId");
							amzAdvKeywordsNegativa.setKeywordid(keywordId);
							amzAdvKeywordsNegativa.setProfileid(profile.getId());
							amzAdvKeywordsNegativa.setState("archived");
							amzAdvKeywordsNegativa.setOpttime(new Date());
							this.updateNotNull(amzAdvKeywordsNegativa);
							negetivaKeywordList.add(amzAdvKeywordsNegativa);
						}else {
							errormsg = errormsg.equals("") ? "" : errormsg + ",";
							errormsg = errormsg + item.getString("description");
							BaseException exception = new BaseException(" 修改失败：" + errormsg);
							exception.setCode("ERROER");
							throw exception;
						}
					}
					Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
					Map<BigInteger, List<Object>> oldmap=new HashMap<BigInteger,List<Object>>();
					for (int i = 0; i < negetivaKeywordList.size(); i++) {
						AmzAdvKeywordsNegativaHsa keywordsNegativa = negetivaKeywordList.get(i);
						BigInteger key = keywordsNegativa.getCampaignid().subtract(keywordsNegativa.getAdgroupid());
						List<Object> keywordslist = map.get(key);
						if(keywordslist==null) {
							keywordslist=new ArrayList<Object>();
						    map.put(key, keywordslist);
						}
						keywordslist.add(keywordsNegativa);
					}
					for (int i = 0; i < oldNegetivaKeywordList.size(); i++) {
						AmzAdvKeywordsNegativaHsa oldKeywordsNegativa = oldNegetivaKeywordList.get(i);
						BigInteger key = oldKeywordsNegativa.getCampaignid().subtract(oldKeywordsNegativa.getAdgroupid());
						List<Object> keywordslist = oldmap.get(key);
						if(keywordslist==null) {
							keywordslist=new ArrayList<Object>();
							oldmap.put(key, keywordslist);
						}
						keywordslist.add(oldKeywordsNegativa);
					}
					amzAdvOperateLogService.saveBatchOperateLog("AmzAdvKeywordsNegativaHsa", user.getId(), profileid, map, oldmap);
					if(negetivaKeywordList!=null&&negetivaKeywordList.size()>0) {
					    return negetivaKeywordList.get(0);
					}else {
						return null;
					}
				}
}
