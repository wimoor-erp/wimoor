package com.wimoor.amazon.adv.sp.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBidReCommendService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvKeywordsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsService;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl.CampaignType;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvKeywordsService")
public class AmzAdvKeywordsServiceImpl extends BaseService<AmzAdvKeywords>implements IAmzAdvKeywordsService {
	@Resource
	AmzAdvKeywordsMapper amzAdvKeywordsMapper;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	@Resource
	IAmzAdvBidReCommendService amzAdvBidReCommendService;
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	@Resource
	IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
	public AmzAdvKeywords selectOneByExample(Example example) {
		return amzAdvKeywordsMapper.selectOneByExample(example);
	}
	
	public List<AmzAdvKeywords> getKeywordsByAdgroupId(BigInteger profileId, String adGroupId, String campaignType) {
		if ( profileId!=null&& StringUtil.isNotEmpty(adGroupId)) {
			Example example = new Example(AmzAdvKeywords.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("adgroupid", adGroupId);
			if(StringUtil.isNotEmpty(campaignType)) {
				crit.andEqualTo("campaigntype", campaignType);
			}
			example.setOrderByClause("keywordText asc");
			List<AmzAdvKeywords> list = amzAdvKeywordsMapper.selectByExample(example);
			return list;
		}  
		return null;
	}

	public AmzAdvKeywords amzGetBiddableKeyword(UserInfo user,BigInteger  profileId, String keywordId, String compaignType) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = "";
		if(StringUtil.isNotEmpty(compaignType)){
			if("hsa".equals(compaignType)) {
				String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/keywords/" + keywordId;
				response = amzAdvAuthService.amzAdvGet_V3(profile, url);
			}else {
				String url = "/" + compaignType + "/keywords/" + keywordId;
				response = amzAdvAuthService.amzAdvGet(profile, url);
			}
		}
		AmzAdvKeywords amzAdvKeywords = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			amzAdvKeywords = new AmzAdvKeywords();
			amzAdvKeywords.setKeywordid(item.getBigInteger("keywordId"));
			amzAdvKeywords.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvKeywords.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvKeywords.setState(item.getString("state"));
			amzAdvKeywords.setKeywordtext(item.getString("keywordText"));
			amzAdvKeywords.setMatchtype(item.getString("matchType"));
			amzAdvKeywords.setBid(item.getBigDecimal("bid"));
			amzAdvKeywords.setCampaigntype(compaignType);
			amzAdvKeywords.setProfileid(profileId);
			amzAdvKeywords.setOpttime(new Date());
			AmzAdvKeywords oldamzAdvKeywords = amzAdvKeywordsMapper.selectByPrimaryKey(amzAdvKeywords);
			if (oldamzAdvKeywords != null) {
				this.updateAll(amzAdvKeywords);
			} else {
				this.save(amzAdvKeywords);
			}
			return amzAdvKeywords;
		}
		return null;
	}

	public AmzAdvKeywords amzGetBiddableKeywordEx(UserInfo user,BigInteger  profileId, String keywordId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/keywords/extended/" + keywordId;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		AmzAdvKeywords amzAdvKeywords = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			amzAdvKeywords = new AmzAdvKeywords();
			amzAdvKeywords.setKeywordid(item.getBigInteger("keywordId"));
			amzAdvKeywords.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvKeywords.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvKeywords.setState(item.getString("state"));
			amzAdvKeywords.setKeywordtext(item.getString("keywordText"));
			amzAdvKeywords.setMatchtype(item.getString("matchType"));
			amzAdvKeywords.setBid(item.getBigDecimal("bid"));
			amzAdvKeywords.setCreationDate(item.getDate("creationDate"));
			amzAdvKeywords.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
			amzAdvKeywords.setServingStatus(item.getString("servingStatus"));
			amzAdvKeywords.setCampaigntype(CampaignType.sp);
			amzAdvKeywords.setProfileid(profileId);
			amzAdvKeywords.setOpttime(new Date());
			AmzAdvKeywords oldamzAdvKeywords = amzAdvKeywordsMapper.selectByPrimaryKey(amzAdvKeywords);
			if (oldamzAdvKeywords != null) {
				this.updateAll(amzAdvKeywords);
			} else {
				this.save(amzAdvKeywords);
			}
			return amzAdvKeywords;
		}
		return null;
	}

	/*
	 * biddableKeywords 里面的compaignType必须一致 当compaignType为 sp
	 * 时biddableKeywords必须小于1000，为 hsa 时则小于100 hsa状态下还有问题，有待商榷
	 */
	public List<AmzAdvKeywords> amzCreateBiddableKeywords(UserInfo user, BigInteger profileId, List<AmzAdvKeywords> biddableKeywordsList) {
		if (biddableKeywordsList == null || biddableKeywordsList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
	
		AmzAdvCampaigns keyObject = new AmzAdvCampaigns();
		keyObject.setCampaignid(biddableKeywordsList.get(0).getCampaignid());
		keyObject.setProfileid(profileId);
		AmzAdvCampaigns campaign = amzAdvCampaignService.selectByKey(keyObject);
		String compaignType = "sp";
		String response = "";
		JSONArray array = new JSONArray();
		if (campaign == null) {
			compaignType = "hsa";
			for (int i = 0; i < biddableKeywordsList.size(); i++) {
				AmzAdvKeywords amzAdvKeywords = biddableKeywordsList.get(i);
				amzAdvKeywords.setState(AdvState.pending);
				JSONObject param = new JSONObject();
				param.put("campaignId", amzAdvKeywords.getCampaignid());
				param.put("adGroupId", amzAdvKeywords.getAdgroupid());
				param.put("keywordText", amzAdvKeywords.getKeywordtext());
				param.put("matchType", amzAdvKeywords.getMatchtype().toLowerCase());
				//param.put("state", amzAdvKeywords.getState());
				param.put("bid", amzAdvKeywords.getBid());
				array.add(param);
			}
			String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/keywords";
			response = amzAdvAuthService.amzAdvPost_V3(profile, url, array.toString());
		}else {
			for (int i = 0; i < biddableKeywordsList.size(); i++) {
				AmzAdvKeywords amzAdvKeywords = biddableKeywordsList.get(i);
				amzAdvKeywords.setState(AdvState.enabled);
				JSONObject param = new JSONObject();
				param.put("campaignId", amzAdvKeywords.getCampaignid());
				param.put("adGroupId", amzAdvKeywords.getAdgroupid());
				param.put("keywordText", amzAdvKeywords.getKeywordtext());
				param.put("matchType", amzAdvKeywords.getMatchtype().toLowerCase());
				param.put("state", amzAdvKeywords.getState());
				param.put("bid", amzAdvKeywords.getBid());
				array.add(param);
			}
			String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sp + "/keywords";
			response = amzAdvAuthService.amzAdvPost(profile, url, array.toString());
		}
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < biddableKeywordsList.size(); i++) {
				AmzAdvKeywords amzAdvKeywords = biddableKeywordsList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger keywordId = item.getBigInteger("keywordId");
					amzAdvKeywords.setKeywordid(keywordId);
					amzAdvKeywords.setCampaigntype(compaignType);
					amzAdvKeywords.setProfileid(profile.getId());
					amzAdvKeywords.setOpttime(new Date());
					AmzAdvKeywords DbamzAdvKeywords = this.selectByKey(amzAdvKeywords);
					if (DbamzAdvKeywords == null) {
						this.save(amzAdvKeywords);
					}
				} else {
					String keywordText = amzAdvKeywords.getKeywordtext();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("关键词：'"+keywordText+"' 创建失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			Map<BigInteger, List<Object>> map = new HashMap<BigInteger, List<Object>>();
			for (int i = 0; i < biddableKeywordsList.size(); i++) {
				AmzAdvKeywords keywords = biddableKeywordsList.get(i);
				BigInteger key = keywords.getCampaignid().subtract(keywords.getAdgroupid());
				List<Object> keywordslist = map.get(key);
				if (keywordslist == null) {
					keywordslist = new ArrayList<Object>();
					map.put(key, keywordslist);
				}
				keywordslist.add(keywords);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvKeywords", user.getId(), profileId, map, null);
			return biddableKeywordsList;
		} else {
			throw new BaseException("关键词创建失败！");
		}
	}

	/*
	 * biddableKeywords 里面的compaignType必须一致 当compaignType为 sp
	 * 时biddableKeywords必须小于1000，为 hsa 时则小于100 hsa状态下还有问题，有待商榷
	 */
	public List<AmzAdvKeywords> amzUpdateBiddableKeywords(UserInfo user,BigInteger  profileId, List<AmzAdvKeywords> biddableKeywordList) {
		if (biddableKeywordList == null || biddableKeywordList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		List<AmzAdvKeywords> oldbiddableKeywordList = new ArrayList<AmzAdvKeywords>();
		String compaignType = "";
		String response = "";
		JSONArray array = new JSONArray();
		for (int i = 0; i < biddableKeywordList.size(); i++) {
			AmzAdvKeywords amzAdvKeywords = biddableKeywordList.get(i);
			Example example = new Example(AmzAdvKeywords.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("keywordid", amzAdvKeywords.getKeywordid());
			AmzAdvKeywords oldamzAdvKeywords = amzAdvKeywordsMapper.selectOneByExample(example);
			oldbiddableKeywordList.add(oldamzAdvKeywords);
			compaignType = oldamzAdvKeywords.getCampaigntype();
			
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("campaignId", amzAdvKeywords.getCampaignid());
			param.put("adGroupId", amzAdvKeywords.getAdgroupid());
			param.put("keywordText", amzAdvKeywords.getKeywordtext());
			param.put("keywordId", amzAdvKeywords.getKeywordid());
			param.put("state", amzAdvKeywords.getState());
			param.put("bid", amzAdvKeywords.getBid());
			array.add(param);
		}
		if(StringUtil.isNotEmpty(compaignType)){
			if("hsa".equals(compaignType)) {
				String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/keywords";
				response = amzAdvAuthService.amzAdvPut_V3(profile, url, array.toString());
			}else {
				String url = "/" + compaignType + "/keywords";
				response = amzAdvAuthService.amzAdvPut(profile, url, array.toString());
			}
		}
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < biddableKeywordList.size(); i++) {
				AmzAdvKeywords amzAdvKeywords = biddableKeywordList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger keywordId = item.getBigInteger("keywordId");
					amzAdvKeywords.setKeywordid(keywordId);
					amzAdvKeywords.setCampaigntype(compaignType);
					amzAdvKeywords.setProfileid(profile.getId());
					amzAdvKeywords.setOpttime(new Date());
					this.updateNotNull(amzAdvKeywords);
				}else {
					String keywordText = amzAdvKeywords.getKeywordtext();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("关键词：'"+keywordText+"' 修改失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			Map<BigInteger, List<Object>> oldmap=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < biddableKeywordList.size(); i++) {
				AmzAdvKeywords keywords = biddableKeywordList.get(i);
				BigInteger key = keywords.getCampaignid().subtract(keywords.getAdgroupid());
				List<Object> keywordslist = map.get(key);
				if(keywordslist==null) {
					keywordslist=new ArrayList<Object>();
				    map.put(key, keywordslist);
				}
				keywordslist.add(keywords);
			}
			for (int i = 0; i < oldbiddableKeywordList.size(); i++) {
				AmzAdvKeywords oldKeywords = oldbiddableKeywordList.get(i);
				BigInteger key = oldKeywords.getCampaignid().subtract(oldKeywords.getAdgroupid());
				List<Object> keywordslist = oldmap.get(key);
				if(keywordslist==null) {
					keywordslist=new ArrayList<Object>();
					oldmap.put(key, keywordslist);
				}
				keywordslist.add(oldKeywords);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvKeywords", user.getId(), profileId, map, oldmap);
			return biddableKeywordList;
		}
		return null;
	}

	public AmzAdvKeywords amzArchiveBiddableKeyword(UserInfo user,BigInteger  profileId, String keywordId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		Example example = new Example(AmzAdvKeywords.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("profileid", profileId);
		crit.andEqualTo("keywordid", keywordId);
		AmzAdvKeywords amzAdvKeywords = amzAdvKeywordsMapper.selectOneByExample(example);
		AmzAdvKeywords oldamzAdvKeywords = amzAdvKeywordsMapper.selectOneByExample(example);
		String response = "";
		if(amzAdvKeywords == null || oldamzAdvKeywords == null) {
			throw new BaseException("要归档的关键词不存在！");
		}
		if(StringUtil.isNotEmpty(amzAdvKeywords.getCampaigntype())){
			if("hsa".equals(amzAdvKeywords.getCampaigntype())) {
				String url = "/" + AmzAdvCampaignServiceImpl.CampaignType.sb + "/keywords/" + keywordId;
				response = amzAdvAuthService.amzAdvDelete_V3(profile, url);
			}else {
				String url = "/" + amzAdvKeywords.getCampaigntype() + "/keywords/" + keywordId;
				response = amzAdvAuthService.amzAdvDelete(profile, url);
			}
		}
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				amzAdvKeywords.setState(AdvState.archived);
				amzAdvKeywords.setOpttime(new Date());
				this.updateAll(amzAdvKeywords);
				amzAdvOperateLogService.saveOperateLog("AmzAdvKeywords", user.getId(), profileId, amzAdvKeywords, oldamzAdvKeywords);
				return amzAdvKeywords;
			}else {
				String errormsg = "";
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + a.getString("description");
				BaseException exception = new BaseException("关键词修改失败："+errormsg);
				exception.setCode("ERROER");
				throw exception;
			}
		}
		return null;
	}

	public List<AmzAdvKeywords> amzListBiddableKeywords(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/keywords/?";
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
		List<AmzAdvKeywords> list = new LinkedList<AmzAdvKeywords>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvKeywords amzAdvKeywords = new AmzAdvKeywords();
				amzAdvKeywords.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvKeywords.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvKeywords.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvKeywords.setKeywordtext(item.getString("keywordText"));
				amzAdvKeywords.setMatchtype(item.getString("matchType"));
				amzAdvKeywords.setBid(item.getBigDecimal("bid"));
				amzAdvKeywords.setState(item.getString("state"));
				amzAdvKeywords.setCampaigntype(CampaignType.sp);
				amzAdvKeywords.setProfileid(profileId);
				amzAdvKeywords.setOpttime(new Date());
				AmzAdvKeywords oldamzAdvKeywords = amzAdvKeywordsMapper.selectByPrimaryKey(amzAdvKeywords);
				if (oldamzAdvKeywords != null) {
					this.updateAll(amzAdvKeywords);
				} else {
					this.save(amzAdvKeywords);
				}
				list.add(amzAdvKeywords);
			}
			return list;
		}
		return null;
	}

	public List<AmzAdvKeywords> amzListBiddableKeywordsEx(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/keywords/extended?";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "startIndex");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "count");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "campaignType");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "matchTypeFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "keywordText");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "stateFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "campaignIdFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "adGroupIdFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "keywordIdFilter");
		}
		url = url + ("".equals(paramurl) ? "" : paramurl);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvKeywords> list = new LinkedList<AmzAdvKeywords>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvKeywords amzAdvKeywords = new AmzAdvKeywords();
				amzAdvKeywords.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvKeywords.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvKeywords.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvKeywords.setKeywordtext(item.getString("keywordText"));
				amzAdvKeywords.setMatchtype(item.getString("matchType"));
				amzAdvKeywords.setBid(item.getBigDecimal("bid"));
				amzAdvKeywords.setState(item.getString("state"));
				amzAdvKeywords.setCreationDate(item.getDate("creationDate"));
				amzAdvKeywords.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				amzAdvKeywords.setServingStatus(item.getString("servingStatus"));
				amzAdvKeywords.setCampaigntype(CampaignType.sp);
				amzAdvKeywords.setProfileid(profileId);
				amzAdvKeywords.setOpttime(new Date());
				AmzAdvKeywords oldamzAdvKeywords = amzAdvKeywordsMapper.selectByPrimaryKey(amzAdvKeywords);
				if (oldamzAdvKeywords != null) {
					this.updateAll(amzAdvKeywords);
				} else {
					this.save(amzAdvKeywords);
				}
				list.add(amzAdvKeywords);
			}
			return list;
		}
		return null;
	}


		

		
	public List<AmzAdvKeywords> amzListSBKeywords(AmzAdvProfile profile, Map<String, Object> param) {
		String url = "/sb/keywords/?";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "startIndex");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "count");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "campaignType");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "matchTypeFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "keywordText");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "stateFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "campaignIdFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "adGroupIdFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "keywordIdFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "creativeType");
			
		}
		url = url + ("".equals(paramurl) ? "" : paramurl);
		String response = amzAdvAuthService.amzAdvGet_V3(profile, url);
		List<AmzAdvKeywords> list = new LinkedList<AmzAdvKeywords>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvKeywords amzAdvKeywords = new AmzAdvKeywords();
				amzAdvKeywords.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvKeywords.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvKeywords.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvKeywords.setKeywordtext(item.getString("keywordText"));
				amzAdvKeywords.setMatchtype(item.getString("matchType"));
				amzAdvKeywords.setBid(item.getBigDecimal("bid"));
				amzAdvKeywords.setState(item.getString("state"));
				amzAdvKeywords.setCreationDate(item.getDate("creationDate"));
				amzAdvKeywords.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				amzAdvKeywords.setServingStatus(item.getString("servingStatus"));
				amzAdvKeywords.setNativeLanguageKeyword(item.getString("nativeLanguageKeyword"));
				amzAdvKeywords.setCampaigntype(CampaignType.hsa);
				amzAdvKeywords.setProfileid(profile.getId());
				amzAdvKeywords.setOpttime(new Date());
				AmzAdvKeywords oldamzAdvKeywords = amzAdvKeywordsMapper.selectByPrimaryKey(amzAdvKeywords);
				if (oldamzAdvKeywords != null) {
					this.updateAll(amzAdvKeywords);
				} else {
					this.save(amzAdvKeywords);
				}
				list.add(amzAdvKeywords);
			}
			if(a.size()==10) {
				Integer startIndex = (Integer)param.get("startIndex");
				param.put("startIndex", startIndex+1);
				amzListSBKeywords(profile,param);
			}
			return list;
		}
		return null;
	}
	public PageList<Map<String, Object>> getKeywordsList(Map<String, Object> map, PageBounds pageBounds){
		String campaignType = (String) map.get("campaignType");
		if(map.get("adGroupid")!=null) {
			campaignType="SP";
		} else if(map.get("campaignid")!=null) {
			String campaignid=(String) map.get("campaignid");
			AmzAdvCampaigns campaign = amzAdvCampaignService.selectByKey(campaignid);
			campaignType="SP";
			if(campaign==null) {
				AmzAdvCampaignsHsa campaign2 = amzAdvCampaignHsaService.selectByKey(campaignid);
				if(campaign2!=null) {
					campaignType="HSA";
				}
			}
		}
		PageList<Map<String,Object>> list = null;
		if("SP".equals(campaignType)) {
			list = amzAdvKeywordsMapper.getKeywordsListForSP(map, pageBounds);
		}
		else if("HSA".equals(campaignType)) {
			list = amzAdvKeywordsMapper.getKeywordsListForSB(map, pageBounds);
		}
		else if("all".equals(campaignType)) {
			list = amzAdvKeywordsMapper.getKeywordsList(map, pageBounds);
		}
		return list;
	}
	
	public Map<String, Object> catchKeywordSuggestBid(UserInfo user,Map<String, Object> map) {
		String profileid = map.get("profileid").toString();
		String keywordid = map.get("id").toString();
		Example example = new Example(AmzAdvKeywords.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("profileid", new BigInteger(profileid));
		crit.andEqualTo("keywordid", new BigInteger(keywordid));
		AmzAdvKeywords amzAdvKeywords = amzAdvKeywordsMapper.selectOneByExample(example);
		if (amzAdvKeywords != null) {
			Map<String, Object> mapBid = null;
			if ("sp".equals(amzAdvKeywords.getCampaigntype())) {
				mapBid = amzAdvBidReCommendService.amzGetKeywordBidRecommendations(user, new BigInteger(profileid),
						keywordid);
				if (mapBid != null) {
					map.put("suggestedBid", mapBid.get("suggestedBid"));
				}
			} else {
				List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("keyword", amzAdvKeywords.getKeywordtext());
				paramMap.put("matchType", amzAdvKeywords.getMatchtype());
				paramList.add(paramMap);
				List<Map<String, Object>> templist = amzAdvBidReCommendService.amzRecommendationsBidsForKeyword_HSA(
						user, amzAdvKeywords.getProfileid(), amzAdvKeywords.getCampaignid(), paramList);
				if (templist != null) {
					mapBid = templist.get(0);
				}
				if (mapBid != null) {
					map.put("suggestedBid", mapBid.get("recommendedBid"));
				}
			}
		}
		return map;
	}
	
	public Map<String, Object> getKeywordChart(Map<String, Object> map) {
		List<Map<String,Object>> list = null;
		List<Map<String, Object>> listHsa = null;
		getSerchStr(map);
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("SP".equals(campaignType)) {
				list = amzAdvKeywordsMapper.getKeywordsChart(map);
			}
			else if("HSA".equals(campaignType)) {
				listHsa = amzAdvKeywordsMapper.getKeywordsHSAChart(map);
			}
			else if("all".equals(campaignType)) {
				list = amzAdvKeywordsMapper.getKeywordsChart(map);
				listHsa = amzAdvKeywordsMapper.getKeywordsHSAChart(map);
			}
		}
		return getChartData(list,listHsa,map);
	}
	
	public Map<String, Object> getChartData(List<Map<String, Object>> list, List<Map<String, Object>> listHsa, Map<String, Object> map) {
		if (list == null && listHsa == null ) {
			return null;
		}
		String serch1 = (String) map.get("value1");
	
		Calendar c = Calendar.getInstance();
		Calendar cTime = Calendar.getInstance();
		String fromDate = (String) map.get("fromDate");
		String endDate = (String) map.get("endDate");
		if(fromDate!=null&&fromDate.contains("-")) {
			cTime.setTime(GeneralUtil.StringfromDate(fromDate, "yyyy-MM-dd"));
		}else {
			cTime.setTime(GeneralUtil.StringfromDate(fromDate, "yyyy/MM/dd"));
		}
		Date beginDateplus = null;
		beginDateplus = cTime.getTime();
		if(endDate!=null&&endDate.contains("-")) {
			cTime.setTime(GeneralUtil.StringfromDate(endDate, "yyyy-MM-dd"));
		}else {
			cTime.setTime(GeneralUtil.StringfromDate(endDate, "yyyy/MM/dd"));
		}
		Date endDateplus = null;
		endDateplus = cTime.getTime();
		List<String> listLabel = new ArrayList<String>();
		List<Object> listData1 = new ArrayList<Object>();
	 
		Map<String, Object> mapSp = new HashMap<String, Object>();
		Map<String, Object> mapHsa = new HashMap<String, Object>();
		Map<String, Object> mapSD = new HashMap<String, Object>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				String bydate = list.get(i).get("bydate") == null ? null : list.get(i).get("bydate").toString();
				String value = list.get(i).get(serch1) == null ? null : list.get(i).get(serch1).toString();
				 
				if (value == null) {
					mapSp.put(bydate, null);
				} else {
					mapSp.put(bydate, value );
				}
			}
		}
		if (listHsa != null) {
			for (int i = 0; i < listHsa.size(); i++) {
				String bydate = listHsa.get(i).get("bydate") == null ? null : listHsa.get(i).get("bydate").toString();
				String value = listHsa.get(i).get(serch1) == null ? null : listHsa.get(i).get(serch1).toString();
				if (value == null) {
					mapHsa.put(bydate, null);
				} else {
					mapHsa.put(bydate, value );
				}
			}
		}
		for (c.setTime(beginDateplus); AdvUtils.checkTimeType(map, c, beginDateplus,
				endDateplus); AdvUtils.step(map, c, beginDateplus, endDateplus)) {
			String tempkey = AdvUtils.getKeyByTimeType(map, c);
			String value = (String) mapSp.get(tempkey);
			String value2 = (String) mapHsa.get(tempkey);
			String value3 = (String) mapSD.get(tempkey);
			listLabel.add(tempkey);
			BigDecimal sp=new BigDecimal("0");
			BigDecimal sb=new BigDecimal("0");
			BigDecimal sd=new BigDecimal("0");
            if ( value != null) {
					sp=new BigDecimal(value);
			} 
			if ( value2 != null) {
					sb=new BigDecimal(value2);
			} 
			if (value3 != null) {
				sd=new BigDecimal(value3);
			} 
		    listData1.add(sp.add(sb).add(sd));
		}
		Map<String, Object> allmap = new HashMap<String, Object>();
		allmap.put("labels", listLabel);
		allmap.put("listdata1", listData1);
		return allmap;
	}
	
	public void getSerchStr(Map<String, Object> map) {
		String campaignType = (String) map.get("campaignType");
		String serch = (String) map.get("searchlist");
		String[] serchArray = serch.split(",");
		String serchlist = "";
		String HSAcsrt = "";
		for (int i = 0; i < serchArray.length; i++) {
				if ("ACOS".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull(sum(cost) / sum(attributedSales14d),0) ACOS ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull(sum(cost) / sum(attributedSales7d),0) ACOS ,";
				} else if ("ROAS".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull(sum(attributedSales14d) / sum(cost),0) ROAS ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull(sum(attributedSales7d) / sum(cost),0) ROAS ,";
				} else if ("CSRT".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull(sum(attributedConversions14d) / sum(clicks),0) CSRT ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull(sum(attributedConversions7d) / sum(clicks),0) CSRT ,";
				} else if ("avgcost".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull((sum(cost) / sum(clicks)),0) avgcost ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull((sum(cost) / sum(clicks)),0) avgcost ,";
				} else if ("CTR".equals(serchArray[i])) {
					HSAcsrt = HSAcsrt + "ifnull(sum(clicks) / sum(impressions),0) CTR ,";
					HSAcsrt= HSAcsrt.replace("7", "14");
					serchlist = serchlist + "ifnull(sum(clicks) / sum(impressions),0) CTR ,";
				}else if ("sumUnits".equals(serchArray[i])) {
					if ("all".equals(campaignType)) {
						serchlist = serchlist +"sum(attributedUnitsOrdered7d) sumUnits,";
						HSAcsrt = HSAcsrt+"sum(attributedConversions14d) sumUnits,";
					} else if ("HSA".equals(campaignType)) {
						HSAcsrt = HSAcsrt+"sum(attributedConversions14d) sumUnits,";
					} else if ("SP".equals(campaignType)) {
						serchlist = serchlist +"sum(attributedUnitsOrdered7d) sumUnits,";
					}
				} else {
					serchlist = serchlist +"sum(" + serchArray[i] + ") " + serchArray[i] + ",";
					HSAcsrt = HSAcsrt+"sum(" + serchArray[i] + ") " + serchArray[i] + ",";
				}
		}
		if(serchlist.contains(",")) {
			serchlist=serchlist.substring(0, serchlist.length()-1);
		}
		if(HSAcsrt.contains(",")) {
			HSAcsrt=HSAcsrt.substring(0, HSAcsrt.length()-1);
		}
		map.put("HSAserchlist", HSAcsrt);
		map.put("serchlist", serchlist);
		map.put("value1", serchArray[0]);
	}


	public PageList<Map<String, Object>> getKeywordQueryList(Map<String, Object> map, PageBounds pageBounds){
		return amzAdvKeywordsMapper.getKeywordQueryList(map, pageBounds);
	}

	public Map<String, Object> getKeywordQueryChart(Map<String, Object> map) {
		List<Map<String,Object>> list = null;
		List<Map<String, Object>> listHsa = null;
		getSerchStr(map);
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("SP".equals(campaignType)) {
				list = amzAdvKeywordsMapper.getKeywordQueryChart(map);
			}
			else if("HSA".equals(campaignType)) {
				listHsa = amzAdvKeywordsMapper.getKeywordQueryHsaChart(map);
			}
			else if("all".equals(campaignType)) {
				list = amzAdvKeywordsMapper.getKeywordQueryChart(map);
				listHsa = amzAdvKeywordsMapper.getKeywordQueryHsaChart(map);
			}
		}
		return getChartData(list,listHsa,map);
	}

	public AmzAdvKeywords getKeywordforQuery(Map<String, Object> map) {
		String profileid = map.get("profileid") == null ? null : map.get("profileid").toString();
		String keywordtext = map.get("query") == null ? null : map.get("query").toString();
		String matchtype = map.get("matchtype") == null ? null : map.get("matchtype").toString();
		String adGroupid = map.get("adGroupid") == null ? null : map.get("adGroupid").toString();
		String campaignid = map.get("campaignid") == null ? null : map.get("campaignid").toString();
		Example example = new Example(AmzAdvKeywords.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("state", "enabled");
		if(profileid != null) {
			crit.andEqualTo("profileid", profileid);
		}
		if(adGroupid != null) {
			crit.andEqualTo("adgroupid", adGroupid);
		}
		if(keywordtext != null) {
			crit.andEqualTo("keywordtext", keywordtext);
		}
		if(matchtype != null) {
			crit.andEqualTo("matchtype", matchtype);
		}
		if(campaignid != null) {
			crit.andEqualTo("campaignid", campaignid);
		}
		List<AmzAdvKeywords> list = this.selectByExample(example);
		if(list != null && list.size() > 0) {
			map.put("keywordFlag", true);
			return list.get(0);
		}
		return null;
	}

 
	public List<Map<String, Object>> getSumAdvKeywords(Map<String, Object> map) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			Object paralist = map.get("paralist");
			if(paralist != null) {
				paralist = paralist.toString().replace("CSRT", "attributedConversions7d / clicks")
					.replace("ACOS", "cost / attributedSales7d").replace("ROAS", "attributedSales7d / cost");
				map.put("paralist", paralist);
			}
			if("SP".equals(campaignType)) {
				Map<String, Object> paraMap = amzAdvKeywordsMapper.getSumAdvKeywords(map);
				if(paraMap != null && paraMap.size() > 0) {
					list.add(paraMap);
					return list;
				}
			}
			else if("HSA".equals(campaignType) || "SB".equals(campaignType)) {
				paralist = map.get("paralist");
				if(paralist != null) {
					if(paralist != null) {
						map.put("paralist", map.get("paralist").toString().replace("7d", "14d"));
					}
				}
				Map<String, Object> paraMap = amzAdvKeywordsMapper.getSumAdvKeywordsSB(map);
				if(paraMap != null && paraMap.size() > 0) {
					list.add(paraMap);
					return list;
				}
			}
			else if("all".equals(campaignType)) {
				Map<String, Object> paraMap = amzAdvKeywordsMapper.getSumAdvKeywords(map);
				paralist = map.get("paralist");
				if(paralist != null) {
					if(paralist != null) {
						map.put("paralist", map.get("paralist").toString().replace("7d", "14d"));
					}
				}
				Map<String, Object> paraMapSB = amzAdvKeywordsMapper.getSumAdvKeywordsSB(map);
				if(paraMap != null && paraMap.size() > 0) {
					list.add(paraMap);
				}
				if(paraMapSB != null && paraMapSB.size() > 0) {
					list.add(paraMapSB);
				}
				return list;
			}
		}
		return null;
	}
	
}
