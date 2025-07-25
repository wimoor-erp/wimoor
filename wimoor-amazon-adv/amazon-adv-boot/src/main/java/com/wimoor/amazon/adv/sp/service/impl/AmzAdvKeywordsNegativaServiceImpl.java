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
import com.wimoor.amazon.adv.common.pojo.AmzAdvOperateLog;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvKeywordsNegativaMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsNegativaService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;

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
	@Resource
	ApiBuildService apiBuildService;
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

 

	  public Map<String,String> getHeaderExt(){
	    	Map<String,String> header=new HashMap<String,String>();
	    	header.put("Content-Type", "application/vnd.spNegativeKeyword.v3+json");
	    	header.put("Accept",       "application/vnd.spNegativeKeyword.v3+json");
	    	return header;
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
		for (int i = 0; i < negetivaKeywordList.size(); i++) {
			AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = negetivaKeywordList.get(i);
			JSONObject param = new JSONObject();
			// campaignId, adGroupId, keywordText, matchType
			if(amzAdvKeywordsNegativa.getNativeLanguageKeyword()!=null) {
				param.put("nativeLanguageKeyword", amzAdvKeywordsNegativa.getNativeLanguageKeyword());
			}
			if(amzAdvKeywordsNegativa.getNativeLanguageLocale()!=null) {
				param.put("nativeLanguageLocale", amzAdvKeywordsNegativa.getNativeLanguageLocale());
			}
			param.put("campaignId", amzAdvKeywordsNegativa.getCampaignid().toString());
			param.put("adGroupId", amzAdvKeywordsNegativa.getAdgroupid().toString());
			param.put("keywordText", amzAdvKeywordsNegativa.getKeywordtext());
			param.put("matchType", amzAdvKeywordsNegativa.getMatchtype());
		    param.put("state", amzAdvKeywordsNegativa.getState());
			array.add(param);
		}
			
		JSONObject negativeKeywords=new JSONObject();
		negativeKeywords.put("negativeKeywords", array);
		String url = "/sp/negativeKeywords";
		response = apiBuildService.amzAdvPost(profile, url, negativeKeywords.toString(),this.getHeaderExt());
			if (StringUtil.isNotEmpty(response)) {
				String errormsg = "";
				JSONObject json = GeneralUtil.getJsonObject(response.toString());
				JSONObject negativeKeywordsJson = json.getJSONObject("negativeKeywords");
				JSONArray success = negativeKeywordsJson.getJSONArray("success");
				JSONArray error = negativeKeywordsJson.getJSONArray("error");
				List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
				for(int i=0;i<success.size();i++) {
					JSONObject item=success.getJSONObject(i);
					Integer index = item.getInteger("index");
					BigInteger keywordId = item.getBigInteger("negativeKeywordId");
					AmzAdvKeywordsNegativa amzAdvKeywordsNegativa =  negetivaKeywordList.get(index);
					amzAdvKeywordsNegativa.setKeywordid(keywordId);
					amzAdvKeywordsNegativa.setCampaigntype("sp");
					amzAdvKeywordsNegativa.setProfileid(profile.getId());
					amzAdvKeywordsNegativa.setOpttime(new Date());
					AmzAdvKeywordsNegativa DbamzAdvKeywordsNegativa = this.selectByKey(amzAdvKeywordsNegativa);
					if(DbamzAdvKeywordsNegativa == null) {
						this.save(amzAdvKeywordsNegativa);
					}
					AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
					operateLog.setCampaignid(amzAdvKeywordsNegativa.getCampaignid());
					operateLog.setProfileid(profileId);
					operateLog.setOperator(user.getId());
					operateLog.setOpttime(new Date());
					operateLog.setBeanclasz("AmzAdvKeywordsNegativa");
					String  nkeyjson = GeneralUtil.toJSON(amzAdvKeywordsNegativa);
					operateLog.setAfterobject(nkeyjson);
					operateLog.setBeforeobject(null);
					operateLogList.add(operateLog);
				}
				amzAdvOperateLogService.insertList(operateLogList);
				for(int i=0;i<error.size();i++) {
				    JSONObject item=error.getJSONObject(i);
				    errormsg=errormsg+item.toJSONString();
				}
				if(StrUtil.isNotBlank(errormsg)) {
					BaseException exception = new BaseException(errormsg);
					exception.setCode("ERROR");
					throw exception;
				}
				return negetivaKeywordList;
			}
			return null;
	}

	public List<AmzAdvKeywordsNegativa> amzDeleteNegativaKeywords(UserInfo user,BigInteger profileId,JSONObject param) {
		// TODO Auto-generated method stub
		String url = "/sp/negativeTargets/delete";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = apiBuildService.amzAdvPost(profile, url, param.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			List<AmzAdvKeywordsNegativa> negetivaKeywordList=new LinkedList<AmzAdvKeywordsNegativa>();
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject negativeKeywordsJson = json.getJSONObject("negativeKeywords");
			JSONArray success = negativeKeywordsJson.getJSONArray("success");
			JSONArray error = negativeKeywordsJson.getJSONArray("error");
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			for(int i=0;i<success.size();i++) {
				JSONObject onenegativeTargetingClause=success.getJSONObject(i);
				JSONObject item = onenegativeTargetingClause.getJSONObject("negativeTargetingClause");
				AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativa();
				amzAdvKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvKeywordsNegativa.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvKeywordsNegativa.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvKeywordsNegativa.setState(item.getString("state"));
				amzAdvKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
				amzAdvKeywordsNegativa.setMatchtype(item.getString("matchType"));
				amzAdvKeywordsNegativa.setCampaigntype("sp");
				amzAdvKeywordsNegativa.setProfileid(profileId);
				amzAdvKeywordsNegativa.setOpttime(new Date());
				AmzAdvKeywordsNegativa oldamzAdvKeywordsNegativa = amzAdvKeywordsNegativaMapper.selectByPrimaryKey(amzAdvKeywordsNegativa);
				if (oldamzAdvKeywordsNegativa != null) {
					this.updateAll(amzAdvKeywordsNegativa);
				} else {
					this.save(amzAdvKeywordsNegativa);
				}
				this.updateNotNull(amzAdvKeywordsNegativa);
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
			 	operateLog.setAdgroupid(amzAdvKeywordsNegativa.getAdgroupid());
				operateLog.setCampaignid(amzAdvKeywordsNegativa.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvKeywordsNegativa");
				String adgroupjson = GeneralUtil.toJSON(amzAdvKeywordsNegativa);
				String oldadgroupjson = GeneralUtil.toJSON(oldamzAdvKeywordsNegativa);
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
			return negetivaKeywordList;
	}
   	return null;
		 
	}
	
	public List<AmzAdvKeywordsNegativa> amzUpdateNegativaKeywords(UserInfo user,BigInteger profileId,List<AmzAdvKeywordsNegativa> negetivaKeywordList) {
		// TODO Auto-generated method stub
		if (negetivaKeywordList == null || negetivaKeywordList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		List<AmzAdvKeywordsNegativa> oldNegetivaKeywordList = new ArrayList<AmzAdvKeywordsNegativa>();
		JSONArray array = new JSONArray();
		for (int i = 0; i < negetivaKeywordList.size(); i++) {
			AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = negetivaKeywordList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("campaignId", amzAdvKeywordsNegativa.getCampaignid().toString());
			param.put("adGroupId", amzAdvKeywordsNegativa.getAdgroupid().toString());
			param.put("keywordId", amzAdvKeywordsNegativa.getKeywordid().toString());
			if(amzAdvKeywordsNegativa.getKeywordtext() != null) {
				param.put("keywordText", amzAdvKeywordsNegativa.getKeywordtext());
			}
			if(amzAdvKeywordsNegativa.getState() != null) {
				param.put("state", amzAdvKeywordsNegativa.getState());
			}
			if(amzAdvKeywordsNegativa.getNativeLanguageKeyword()!=null) {
				param.put("nativeLanguageKeyword", amzAdvKeywordsNegativa.getNativeLanguageKeyword());
			}
			if(amzAdvKeywordsNegativa.getNativeLanguageLocale()!=null) {
				param.put("nativeLanguageLocale", amzAdvKeywordsNegativa.getNativeLanguageLocale());
			}
			param.put("matchType", amzAdvKeywordsNegativa.getMatchtype());
			array.add(param);
			
			Example example = new Example(AmzAdvKeywordsNegativa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("keywordid", amzAdvKeywordsNegativa.getKeywordid());
			AmzAdvKeywordsNegativa oldamzAdvKeywordsNegativa = amzAdvKeywordsNegativaMapper.selectOneByExample(example);
			oldNegetivaKeywordList.add(oldamzAdvKeywordsNegativa);
		}
		String response = "";
		JSONObject negativeKeywords=new JSONObject();
		negativeKeywords.put("negativeKeywords", array);
		String url = "/sp/negativeKeywords";
		response = apiBuildService.amzAdvPut(profile, url, negativeKeywords.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject negativeKeywordsJson = json.getJSONObject("negativeKeywords");
			JSONArray success = negativeKeywordsJson.getJSONArray("success");
			JSONArray error = negativeKeywordsJson.getJSONArray("error");
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = negetivaKeywordList.get(index);
				BigInteger keywordId = item.getBigInteger("negativeKeywordId");
				amzAdvKeywordsNegativa.setKeywordid(keywordId);
				amzAdvKeywordsNegativa.setProfileid(profile.getId());
				amzAdvKeywordsNegativa.setOpttime(new Date());
				this.updateNotNull(amzAdvKeywordsNegativa);
				
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvKeywordsNegativa oldamzAdvKeywordsNegativa = oldNegetivaKeywordList.get(index);
				operateLog.setAdgroupid(amzAdvKeywordsNegativa.getAdgroupid());
				operateLog.setCampaignid(amzAdvKeywordsNegativa.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvKeywordsNegativa");
				String adgroupjson = GeneralUtil.toJSON(amzAdvKeywordsNegativa);
				String oldadgroupjson = GeneralUtil.toJSON(oldamzAdvKeywordsNegativa);
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
			return negetivaKeywordList;
	}
   	return null;
		 
	}

 

	public List<AmzAdvKeywordsNegativa> amzListNegativaKeywords(UserInfo user,BigInteger  profileId,JSONObject param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/negativeKeywords/list";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		List<AmzAdvKeywordsNegativa> list = new LinkedList<AmzAdvKeywordsNegativa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject negativeKeywords = GeneralUtil.getJsonObject(response);
			JSONArray a = negativeKeywords.getJSONArray("negativeKeywords");
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativa();
				amzAdvKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvKeywordsNegativa.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvKeywordsNegativa.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvKeywordsNegativa.setState(item.getString("state"));
				amzAdvKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
				amzAdvKeywordsNegativa.setMatchtype(item.getString("matchType"));
				amzAdvKeywordsNegativa.setCampaigntype("sp");
				amzAdvKeywordsNegativa.setProfileid(profileId);
				amzAdvKeywordsNegativa.setOpttime(new Date());
				JSONObject extendedData = item.getJSONObject("extendedData");
				if(extendedData!=null) {
					String servingStatus=extendedData.getString("servingStatus");
					amzAdvKeywordsNegativa.setServingStatus(servingStatus);
					Date creationDateTime=extendedData.getDate("creationDateTime");
					amzAdvKeywordsNegativa.setCreationDate(creationDateTime);
					Date lastUpdateDateTime=extendedData.getDate("lastUpdateDateTime");
					amzAdvKeywordsNegativa.setLastUpdatedDate(lastUpdateDateTime);
				}
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

	 public List<AmzAdvKeywordsNegativa> convertJsonToBean(UserInfo user, BigInteger profileId, JSONArray nkeywordArray){
			List<AmzAdvKeywordsNegativa> list = new ArrayList<AmzAdvKeywordsNegativa>();
		for (int i = 0; i < nkeywordArray.size(); i++) {
			JSONObject keyword = nkeywordArray.getJSONObject(i);
			String matchType = keyword.getString("matchtype");
			String keywordText = keyword.getString("keywordtext");
			String state = keyword.getString("state");
			BigInteger campaignid = keyword.getBigInteger("campaignid");
			BigInteger keywordid = keyword.getBigInteger("keywordid");
			BigInteger adGroupid = keyword.getBigInteger("adgroupid");
			AmzAdvKeywordsNegativa amzAdvKeywordsNegativa = new AmzAdvKeywordsNegativa();
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
	public List<AmzAdvKeywordsNegativa> amzUpdateNegativaKeywords(UserInfo user, String profileid, JSONArray nkeywordArray) {
		// TODO Auto-generated method stub
		return this.amzUpdateNegativaKeywords(user, new BigInteger(profileid), this.convertJsonToBean(user, new BigInteger(profileid), nkeywordArray));
	}

	@Override
	public List<AmzAdvKeywordsNegativa> amzCreateNegativaKeywords(UserInfo user, String profileid,
			JSONArray nkeywords) {
		// TODO Auto-generated method stub
		return this.amzCreateNegativaKeywords(user, new BigInteger(profileid), this.convertJsonToBean(user, new BigInteger(profileid), nkeywords));
	}
	
}
