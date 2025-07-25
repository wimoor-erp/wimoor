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
import com.wimoor.amazon.adv.sp.dao.AmzAdvCampKeywordsNegativaMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampKeywordsNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampKeywordsNegativaService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;

import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvCampKeywordsNegativaService")
public class AmzAdvCampKeywordsNegativaServiceImpl extends BaseService<AmzAdvCampKeywordsNegativa>implements IAmzAdvCampKeywordsNegativaService {
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvCampKeywordsNegativaMapper amzAdvCampKeywordsNegativaMapper;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	@Resource
	ApiBuildService apiBuildService;
	public AmzAdvCampKeywordsNegativa selectOneByExample(Example example) {
		return amzAdvCampKeywordsNegativaMapper.selectOneByExample(example);
	}

	public List<AmzAdvCampKeywordsNegativa> amzDeleteCampaignNegativaKeywords(UserInfo user,BigInteger profileId,JSONObject param) {
		// TODO Auto-generated method stub
		String url = "/sp/campaignNegativeKeywords/delete";
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = apiBuildService.amzAdvPost(profile, url, param.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			List<AmzAdvCampKeywordsNegativa> negetivaKeywordList=new LinkedList<AmzAdvCampKeywordsNegativa>();
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject negativeKeywordsJson = json.getJSONObject("campaignNegativeKeywords");
			JSONArray success = negativeKeywordsJson.getJSONArray("success");
			JSONArray error = negativeKeywordsJson.getJSONArray("error");
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			for(int i=0;i<success.size();i++) {
				JSONObject onenegativeTargetingClause=success.getJSONObject(i);
				JSONObject item = onenegativeTargetingClause.getJSONObject("campaignNegativeKeyword");
				AmzAdvCampKeywordsNegativa amzAdvKeywordsNegativa = new AmzAdvCampKeywordsNegativa();
				amzAdvKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvKeywordsNegativa.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvKeywordsNegativa.setState(item.getString("state"));
				amzAdvKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
				amzAdvKeywordsNegativa.setMatchtype(item.getString("matchType"));
				amzAdvKeywordsNegativa.setProfileid(profileId);
				amzAdvKeywordsNegativa.setOpttime(new Date());
				AmzAdvCampKeywordsNegativa oldamzAdvKeywordsNegativa = amzAdvCampKeywordsNegativaMapper.selectByPrimaryKey(amzAdvKeywordsNegativa);
				if (oldamzAdvKeywordsNegativa != null) {
					this.updateAll(amzAdvKeywordsNegativa);
				} else {
					this.save(amzAdvKeywordsNegativa);
				}
				this.updateNotNull(amzAdvKeywordsNegativa);
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
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
	
	public List<AmzAdvCampKeywordsNegativa> getCampKeywordsNegativaByCampaignsId(BigInteger profileId, String campaignsId) {
		if ( campaignsId!=null&&StringUtil.isNotEmpty(campaignsId)) {
			Example example = new Example(AmzAdvCampKeywordsNegativa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("campaignid", campaignsId);
			example.setOrderByClause("keywordText asc");
			List<AmzAdvCampKeywordsNegativa> list = amzAdvCampKeywordsNegativaMapper.selectByExample(example);
			return list;
		}
		return null;
	}

	 
 

	public List<AmzAdvCampKeywordsNegativa> amzCreateCampaignNegativeKeywords(UserInfo user,BigInteger  profileId,
			List<AmzAdvCampKeywordsNegativa> campKeywordsNegativaList) {
		// TODO Auto-generated method stub
		if (campKeywordsNegativaList == null || campKeywordsNegativaList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaignNegativeKeywords";
		JSONArray array = new JSONArray();

		for (int i = 0; i < campKeywordsNegativaList.size(); i++) {
			AmzAdvCampKeywordsNegativa amzAdvCampKeywordsNegativa = campKeywordsNegativaList.get(i);
			JSONObject param = new JSONObject();
			// campaignId keywordText matchType state
			param.put("campaignId", amzAdvCampKeywordsNegativa.getCampaignid().toString());
			param.put("state", amzAdvCampKeywordsNegativa.getState());
			param.put("keywordText", amzAdvCampKeywordsNegativa.getKeywordtext());
			param.put("matchType", amzAdvCampKeywordsNegativa.getMatchtype());
			array.add(param);
		}
		JSONObject campaignNegativeTargetingClauses=new JSONObject();
		campaignNegativeTargetingClauses.put("campaignNegativeKeywords", array);
		String response = apiBuildService.amzAdvPost(profile, url, campaignNegativeTargetingClauses.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject campaignNegativeTargetingClausesJson = json.getJSONObject("campaignNegativeKeywords");
			JSONArray success = campaignNegativeTargetingClausesJson.getJSONArray("success");
			JSONArray error = campaignNegativeTargetingClausesJson.getJSONArray("error");
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				BigInteger keywordId = item.getBigInteger("campaignNegativeKeywordId");
				AmzAdvCampKeywordsNegativa amzAdvCampKeywordsNegativa =  campKeywordsNegativaList.get(index);
				amzAdvCampKeywordsNegativa.setKeywordid(keywordId);
				amzAdvCampKeywordsNegativa.setProfileid(profile.getId());
				amzAdvCampKeywordsNegativa.setOpttime(new Date());
				AmzAdvCampKeywordsNegativa DbamzAdvCampKeywordsNegativa = this.selectByKey(amzAdvCampKeywordsNegativa);
				if(DbamzAdvCampKeywordsNegativa == null) {
					this.save(amzAdvCampKeywordsNegativa);
				}
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				operateLog.setCampaignid(amzAdvCampKeywordsNegativa.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvCampKeywordsNegativa");
				String  nkeyjson = GeneralUtil.toJSON(amzAdvCampKeywordsNegativa);
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
			return campKeywordsNegativaList;
		}
		return null;
		 
	}

	public List<AmzAdvCampKeywordsNegativa> amzUpdateCampaignNegativeKeywords(UserInfo user,BigInteger  profileId,
			List<AmzAdvCampKeywordsNegativa> campKeywordsNegativaList) {
		// TODO Auto-generated method stub
		if (campKeywordsNegativaList == null || campKeywordsNegativaList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		List<AmzAdvCampKeywordsNegativa> oldcampKeywordsNegativaList = new ArrayList<AmzAdvCampKeywordsNegativa>();
		String url = "/sp/campaignNegativeKeywords";
		JSONArray array = new JSONArray();
		
		for (int i = 0; i < campKeywordsNegativaList.size(); i++) {
			AmzAdvCampKeywordsNegativa amzAdvCampKeywordsNegativa = campKeywordsNegativaList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("campaignId", amzAdvCampKeywordsNegativa.getCampaignid().toString());
			param.put("keywordText", amzAdvCampKeywordsNegativa.getKeywordtext());
			param.put("matchType", amzAdvCampKeywordsNegativa.getMatchtype());

			param.put("keywordId", amzAdvCampKeywordsNegativa.getKeywordid().toString());
			param.put("state", amzAdvCampKeywordsNegativa.getState());
			array.add(param);
			
			Example example = new Example(AmzAdvCampKeywordsNegativa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("keywordid", amzAdvCampKeywordsNegativa.getKeywordid());
			crit.andEqualTo("campaignid", amzAdvCampKeywordsNegativa.getCampaignid());
			AmzAdvCampKeywordsNegativa oldamzAdvCampKeywordsNegativa = amzAdvCampKeywordsNegativaMapper.selectOneByExample(example);
			oldcampKeywordsNegativaList.add(oldamzAdvCampKeywordsNegativa);
		}
		JSONObject campaignNegativeTargetingClauses=new JSONObject();
		campaignNegativeTargetingClauses.put("campaignNegativeKeywords", array);
		String response = apiBuildService.amzAdvPut(profile, url, campaignNegativeTargetingClauses.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject negativeKeywordsJson = json.getJSONObject("campaignNegativeKeywords");
			JSONArray success = negativeKeywordsJson.getJSONArray("success");
			JSONArray error = negativeKeywordsJson.getJSONArray("error");
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				AmzAdvCampKeywordsNegativa campKeywordsNegativa = campKeywordsNegativaList.get(index);
				BigInteger keywordId = item.getBigInteger("campaignNegativeKeywordId");
				campKeywordsNegativa.setKeywordid(keywordId);
				campKeywordsNegativa.setProfileid(profile.getId());
				campKeywordsNegativa.setOpttime(new Date());
				amzAdvCampKeywordsNegativaMapper.updateByPrimaryKey(campKeywordsNegativa); 
				
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvCampKeywordsNegativa oldcampKeywordsNegativa = oldcampKeywordsNegativaList.get(index);
				operateLog.setAdgroupid(new BigInteger("0"));
				operateLog.setCampaignid(campKeywordsNegativa.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvCampKeywordsNegativa");
				String adgroupjson = GeneralUtil.toJSON(campKeywordsNegativa);
				String oldadgroupjson = GeneralUtil.toJSON(oldcampKeywordsNegativa);
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
			return campKeywordsNegativaList;
	}
		 
		return null;
	}

	 
	  public Map<String,String> getHeaderExt(){
	    	Map<String,String> header=new HashMap<String,String>();
	    	header.put("Content-Type", "application/vnd.spCampaignNegativeKeyword.v3+json");
	    	header.put("Accept",       "application/vnd.spCampaignNegativeKeyword.v3+json");
	    	return header;
	    }
	  
	public List<AmzAdvCampKeywordsNegativa> amzListCampaignNegativeKeywords(UserInfo user,BigInteger  profileId,
			JSONObject param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url =  "/sp/campaignNegativeKeywords/list";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		List<AmzAdvCampKeywordsNegativa> list = new LinkedList<AmzAdvCampKeywordsNegativa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject campaignNegativeKeywords=GeneralUtil.getJsonObject(response.toString());
			JSONArray a = campaignNegativeKeywords.getJSONArray("campaignNegativeKeywords");
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvCampKeywordsNegativa amzAdvCampKeywordsNegativa = new AmzAdvCampKeywordsNegativa();
				amzAdvCampKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvCampKeywordsNegativa.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvCampKeywordsNegativa.setState(item.getString("state"));
				amzAdvCampKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
				amzAdvCampKeywordsNegativa.setMatchtype(item.getString("matchType"));
				amzAdvCampKeywordsNegativa.setProfileid(profileId);
				amzAdvCampKeywordsNegativa.setOpttime(new Date());
				AmzAdvCampKeywordsNegativa oldamzAdvCampKeywordsNegativa = amzAdvCampKeywordsNegativaMapper
						.selectByPrimaryKey(amzAdvCampKeywordsNegativa);
				if (oldamzAdvCampKeywordsNegativa != null) {
					this.updateAll(amzAdvCampKeywordsNegativa);
				} else {
					this.save(amzAdvCampKeywordsNegativa);
				}
				list.add(amzAdvCampKeywordsNegativa);
			}
			return list;
		}
		return null;
	}

	 

	public List<Map<String, Object>> getCampaignNegativaKeywordsList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("HSA".equals(campaignType)) {
				return null;
			}
		}
		return amzAdvCampKeywordsNegativaMapper.getCampaignNegativaKeywordsList(map);
	}

	 public List<AmzAdvCampKeywordsNegativa> convertJsonToBean(UserInfo user, BigInteger profileId, JSONArray nkeywordArray){
			List<AmzAdvCampKeywordsNegativa> list = new ArrayList<AmzAdvCampKeywordsNegativa>();
		for (int i = 0; i < nkeywordArray.size(); i++) {
			JSONObject keyword = nkeywordArray.getJSONObject(i);
			String matchType = keyword.getString("matchtype");
			String keywordText = keyword.getString("keywordtext");
			String state = keyword.getString("state");
			BigInteger campaignid = keyword.getBigInteger("campaignid");
			BigInteger keywordid = keyword.getBigInteger("keywordid");
			AmzAdvCampKeywordsNegativa amzAdvKeywordsNegativa = new AmzAdvCampKeywordsNegativa();
			amzAdvKeywordsNegativa.setKeywordid(keywordid);
			amzAdvKeywordsNegativa.setCampaignid(campaignid);
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
	public List<AmzAdvCampKeywordsNegativa> amzUpdateCampaignNegativeKeywords(UserInfo user, String profileid,
			JSONArray keywordArray) {
		// TODO Auto-generated method stub
		return this.amzUpdateCampaignNegativeKeywords(user, new BigInteger(profileid), this.convertJsonToBean(user,  new BigInteger(profileid), keywordArray));
	}

	@Override
	public List<AmzAdvCampKeywordsNegativa> amzCreateCampaignNegativeKeywords(UserInfo user, String profileid, JSONArray nkeywords) {
		// TODO Auto-generated method stub
		return this.amzCreateCampaignNegativeKeywords(user, new BigInteger(profileid), this.convertJsonToBean(user, new BigInteger(profileid), nkeywords));
	}
	
	

}
