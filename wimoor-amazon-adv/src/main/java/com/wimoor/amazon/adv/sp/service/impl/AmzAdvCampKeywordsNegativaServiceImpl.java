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
import com.wimoor.amazon.adv.sp.dao.AmzAdvCampKeywordsNegativaMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampKeywordsNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampKeywordsNegativaService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
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
	
	public AmzAdvCampKeywordsNegativa selectOneByExample(Example example) {
		return amzAdvCampKeywordsNegativaMapper.selectOneByExample(example);
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

	public AmzAdvCampKeywordsNegativa amzGetCampaignNegativeKeyword(UserInfo user,BigInteger  profileId, String keywordId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaignNegativeKeywords/" + keywordId;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		AmzAdvCampKeywordsNegativa amzAdvCampKeywordsNegativa = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			amzAdvCampKeywordsNegativa = new AmzAdvCampKeywordsNegativa();
			amzAdvCampKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
			amzAdvCampKeywordsNegativa.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvCampKeywordsNegativa.setState(item.getString("state"));
			amzAdvCampKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
			amzAdvCampKeywordsNegativa.setMatchtype(item.getString("matchType"));
			amzAdvCampKeywordsNegativa.setProfileid(profileId);
			amzAdvCampKeywordsNegativa.setOpttime(new Date());
			AmzAdvCampKeywordsNegativa oldCampKeywordsNegativa = amzAdvCampKeywordsNegativaMapper
					.selectByPrimaryKey(amzAdvCampKeywordsNegativa);
			if (oldCampKeywordsNegativa != null) {
				this.updateAll(amzAdvCampKeywordsNegativa);
			} else {
				this.save(amzAdvCampKeywordsNegativa);
			}
			return amzAdvCampKeywordsNegativa;
		}
		return null;
	}

	public AmzAdvCampKeywordsNegativa amzGetCampaignNegativeKeywordEx(UserInfo user,BigInteger  profileId, String keywordId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaignNegativeKeywords/extended/" + keywordId;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		AmzAdvCampKeywordsNegativa amzAdvCampKeywordsNegativa = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			amzAdvCampKeywordsNegativa = new AmzAdvCampKeywordsNegativa();
			amzAdvCampKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
			amzAdvCampKeywordsNegativa.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvCampKeywordsNegativa.setState(item.getString("state"));
			amzAdvCampKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
			amzAdvCampKeywordsNegativa.setMatchtype(item.getString("matchType"));
			amzAdvCampKeywordsNegativa.setCreationDate(item.getDate("creationDate"));
			amzAdvCampKeywordsNegativa.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
			amzAdvCampKeywordsNegativa.setServingStatus(item.getString("servingStatus"));
			amzAdvCampKeywordsNegativa.setProfileid(profileId);
			amzAdvCampKeywordsNegativa.setOpttime(new Date());
			AmzAdvCampKeywordsNegativa oldCampKeywordsNegativa = amzAdvCampKeywordsNegativaMapper
					.selectByPrimaryKey(amzAdvCampKeywordsNegativa);
			if (oldCampKeywordsNegativa != null) {
				this.updateAll(amzAdvCampKeywordsNegativa);
			} else {
				this.save(amzAdvCampKeywordsNegativa);
			}
			return amzAdvCampKeywordsNegativa;
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
			param.put("campaignId", amzAdvCampKeywordsNegativa.getCampaignid());
			param.put("state", amzAdvCampKeywordsNegativa.getState());
			param.put("keywordText", amzAdvCampKeywordsNegativa.getKeywordtext());
			param.put("matchType", amzAdvCampKeywordsNegativa.getMatchtype());
			array.add(param);
		}
		String response = amzAdvAuthService.amzAdvPost(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < campKeywordsNegativaList.size(); i++) {
				AmzAdvCampKeywordsNegativa amzAdvCampKeywordsNegativa = campKeywordsNegativaList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger keywordId = item.getBigInteger("keywordId");
					amzAdvCampKeywordsNegativa.setKeywordid(keywordId);
					amzAdvCampKeywordsNegativa.setProfileid(profile.getId());
					amzAdvCampKeywordsNegativa.setOpttime(new Date());
					AmzAdvCampKeywordsNegativa DbamzAdvCampKeywordsNegativa = this.selectByKey(amzAdvCampKeywordsNegativa);
					if(DbamzAdvCampKeywordsNegativa == null) {
						this.save(amzAdvCampKeywordsNegativa);
					}
				}else {
					String name = amzAdvCampKeywordsNegativa.getKeywordtext();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告活动否定关键词：'"+name+"' 创建失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < campKeywordsNegativaList.size(); i++) {
				AmzAdvCampKeywordsNegativa campKeywordsNegativa = campKeywordsNegativaList.get(i);
				List<Object> keywordslist = map.get(campKeywordsNegativa.getCampaignid());
				if(keywordslist==null) {
					keywordslist=new ArrayList<Object>();
				    map.put(campKeywordsNegativa.getCampaignid(), keywordslist);
				}
				keywordslist.add(campKeywordsNegativa);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvCampKeywordsNegativa", user.getId(), profileId, map, null);
			return campKeywordsNegativaList;
		}else {
			throw new BaseException("创建失败！");
		}
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
			param.put("campaignId", amzAdvCampKeywordsNegativa.getCampaignid());
			param.put("keywordText", amzAdvCampKeywordsNegativa.getKeywordtext());
			param.put("matchType", amzAdvCampKeywordsNegativa.getMatchtype());

			param.put("keywordId", amzAdvCampKeywordsNegativa.getKeywordid());
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
		String response = amzAdvAuthService.amzAdvPut(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < campKeywordsNegativaList.size(); i++) {
				AmzAdvCampKeywordsNegativa campKeywordsNegativa = campKeywordsNegativaList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger keywordId = item.getBigInteger("keywordId");
					campKeywordsNegativa.setKeywordid(keywordId);
					campKeywordsNegativa.setProfileid(profile.getId());
					campKeywordsNegativa.setOpttime(new Date());
					amzAdvCampKeywordsNegativaMapper.updateByPrimaryKeySelective(campKeywordsNegativa); 
				}else{
					String name = campKeywordsNegativa.getKeywordtext();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("广告活动否定关键词：'"+name+"' 修改失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			Map<BigInteger, List<Object>> oldmap=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < campKeywordsNegativaList.size(); i++) {
				AmzAdvCampKeywordsNegativa campKeywordsNegativa = campKeywordsNegativaList.get(i);
				List<Object> keywordslist = map.get(campKeywordsNegativa.getCampaignid());
				if(keywordslist==null) {
					keywordslist=new ArrayList<Object>();
				    map.put(campKeywordsNegativa.getCampaignid(), keywordslist);
				}
				keywordslist.add(campKeywordsNegativa);
			}
			for (int i = 0; i < oldcampKeywordsNegativaList.size(); i++) {
				AmzAdvCampKeywordsNegativa oldcampKeywordsNegativa = oldcampKeywordsNegativaList.get(i);
				List<Object> keywordslist = oldmap.get(oldcampKeywordsNegativa.getCampaignid());
				if(keywordslist==null) {
					keywordslist=new ArrayList<Object>();
					oldmap.put(oldcampKeywordsNegativa.getCampaignid(), keywordslist);
				}
				keywordslist.add(oldcampKeywordsNegativa);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvCampKeywordsNegativa", user.getId(), profileId, map, oldmap);
			return campKeywordsNegativaList;
		}
		return null;
	}

	/*
	 * 是否直接从本地删除，因为亚马逊已经删除该id
	 */
	public AmzAdvCampKeywordsNegativa amzRemoveCampaignNegativeKeyword(UserInfo user,BigInteger profileId, String keywordId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvCampKeywordsNegativa amzAdvCampKeywordsNegativa = null;
		AmzAdvCampKeywordsNegativa oldamzAdvCampKeywordsNegativa = null;
		String url = "/sp/campaignNegativeKeywords/" + keywordId;
		String response = amzAdvAuthService.amzAdvDelete(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example = new Example(AmzAdvCampKeywordsNegativa.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("keywordid", keywordId);
				oldamzAdvCampKeywordsNegativa = amzAdvCampKeywordsNegativaMapper.selectOneByExample(example);
				amzAdvCampKeywordsNegativa = amzAdvCampKeywordsNegativaMapper.selectOneByExample(example);
				amzAdvCampKeywordsNegativa.setOpttime(new Date());
				amzAdvCampKeywordsNegativa.setState(AdvState.deleted);
				// amzAdvCampKeywordsNegativaMapper.deleteByPrimaryKey(amzAdvCampKeywordsNegativa);
				this.updateAll(amzAdvCampKeywordsNegativa);
				amzAdvOperateLogService.saveOperateLog("AmzAdvCampKeywordsNegativa", user.getId(), profileId, amzAdvCampKeywordsNegativa, oldamzAdvCampKeywordsNegativa);
				return amzAdvCampKeywordsNegativa;
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

	public List<AmzAdvCampKeywordsNegativa> amzListCampaignNegativeKeywords(UserInfo user,BigInteger  profileId,
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaignNegativeKeywords/?";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "startIndex");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "count");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "matchTypeFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "keywordText");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "campaignIdFilter");
		}
		url = url + ("".equals(paramurl) ? "" : paramurl);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvCampKeywordsNegativa> list = new LinkedList<AmzAdvCampKeywordsNegativa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
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

	public List<AmzAdvCampKeywordsNegativa> amzListCampaignNegativeKeywordsEx(UserInfo user,BigInteger  profileId,
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaignNegativeKeywords/extended?";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "startIndex");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "count");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "matchTypeFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "keywordText");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "campaignIdFilter");
		}
		url = url + ("".equals(paramurl) ? "" : paramurl);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvCampKeywordsNegativa> list = new LinkedList<AmzAdvCampKeywordsNegativa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvCampKeywordsNegativa amzAdvCampKeywordsNegativa = new AmzAdvCampKeywordsNegativa();
				amzAdvCampKeywordsNegativa.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvCampKeywordsNegativa.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvCampKeywordsNegativa.setState(item.getString("state"));
				amzAdvCampKeywordsNegativa.setKeywordtext(item.getString("keywordText"));
				amzAdvCampKeywordsNegativa.setMatchtype(item.getString("matchType"));
				amzAdvCampKeywordsNegativa.setCreationDate(item.getDate("creationDate"));
				amzAdvCampKeywordsNegativa.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				amzAdvCampKeywordsNegativa.setServingStatus(item.getString("servingStatus"));
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
	
	

}
