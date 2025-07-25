package com.wimoor.amazon.adv.sb.service.impl;

import java.math.BigDecimal;
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
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AdvState;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBidReCommendService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvKeywordsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvKeywordsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvKeywordsHsaService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import cn.hutool.core.bean.BeanUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvKeywordsHsaService")
public class AmzAdvKeywordsHsaServiceImpl extends BaseService<AmzAdvKeywordsHsa>implements IAmzAdvKeywordsHsaService {
	 
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	@Resource
	IAmzAdvBidReCommendService amzAdvBidReCommendService;
	@Resource
	IAmzAdvCampaignHsaService amzAdvCampaignHsaService;
	@Resource
	ApiBuildService apiBuildService;
	@Resource
	AmzAdvKeywordsHsaMapper amzAdvKeywordsHsaMapper;
	public AmzAdvKeywordsHsa selectOneByExample(Example example) {
		return amzAdvKeywordsHsaMapper.selectOneByExample(example);
	}
 
	 
	/*
	 * biddableKeywords 里面的compaignType必须一致 当compaignType为 sp
	 * 时biddableKeywords必须小于1000，为 hsa 时则小于100 hsa状态下还有问题，有待商榷
	 */
	public List<AmzAdvKeywordsHsa> amzUpdateKeywords(UserInfo user,BigInteger  profileId, List<AmzAdvKeywordsHsa> biddableKeywordList) {
		if (biddableKeywordList == null || biddableKeywordList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		List<AmzAdvKeywordsHsa> oldbiddableKeywordList = new ArrayList<AmzAdvKeywordsHsa>();
		String compaignType = "";
		String response = "";
		JSONArray array = new JSONArray();
		for (int i = 0; i < biddableKeywordList.size(); i++) {
			AmzAdvKeywordsHsa amzAdvKeywords = biddableKeywordList.get(i);
			Example example = new Example(AmzAdvKeywords.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("keywordid", amzAdvKeywords.getKeywordid());
			  AmzAdvKeywordsHsa oldamzAdvKeywords = amzAdvKeywordsHsaMapper.selectOneByExample(example);
			oldbiddableKeywordList.add(oldamzAdvKeywords);
			compaignType = oldamzAdvKeywords.getCampaigntype();
			
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("campaignId", amzAdvKeywords.getCampaignid());
			param.put("adGroupId", amzAdvKeywords.getAdgroupid());
			//param.put("keywordText", amzAdvKeywords.getKeywordtext());
			param.put("keywordId", amzAdvKeywords.getKeywordid());
			param.put("state", amzAdvKeywords.getState());
			param.put("bid", amzAdvKeywords.getBid());
			array.add(param);
		}
	
		String url = "/sb/keywords";
		response = apiBuildService.amzAdvPut(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < biddableKeywordList.size(); i++) {
				AmzAdvKeywordsHsa amzAdvKeywords = biddableKeywordList.get(i);
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
				AmzAdvKeywordsHsa keywords = biddableKeywordList.get(i);
				BigInteger key = keywords.getCampaignid().subtract(keywords.getAdgroupid());
				List<Object> keywordslist = map.get(key);
				if(keywordslist==null) {
					keywordslist=new ArrayList<Object>();
				    map.put(key, keywordslist);
				}
				keywordslist.add(keywords);
			}
			for (int i = 0; i < oldbiddableKeywordList.size(); i++) {
				AmzAdvKeywordsHsa oldKeywords = oldbiddableKeywordList.get(i);
				BigInteger key = oldKeywords.getCampaignid().subtract(oldKeywords.getAdgroupid());
				List<Object> keywordslist = oldmap.get(key);
				if(keywordslist==null) {
					keywordslist=new ArrayList<Object>();
					oldmap.put(key, keywordslist);
				}
				keywordslist.add(oldKeywords);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvKeywordsHsa", user.getId(), profileId, map, oldmap);
			return biddableKeywordList;
		}
		return null;
	}
	
	public List<AmzAdvKeywordsHsa> amzCreateKeywords(UserInfo user, BigInteger profileId, List<AmzAdvKeywordsHsa> list) {
		if (list == null || list.size() <= 0)
			return null;
		//先去判断list 有没有词和类型已经加入到这个广告组里
		for (int j = 0; j < list.size(); j++) {
			AmzAdvKeywordsHsa item = list.get(j);
			String text = item.getKeywordtext();
			String matchtype=item.getMatchtype();
			Example example=new Example(AmzAdvKeywords.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("keywordtext",text);
			Criteria crit2=example.and();
			crit2.orEqualTo("matchtype",matchtype.toLowerCase());
			crit2.orEqualTo("matchtype",matchtype.toUpperCase());
			crit.andEqualTo("adgroupid",item.getAdgroupid().toString());
			List<AmzAdvKeywordsHsa> keylist = amzAdvKeywordsHsaMapper.selectByExample(example);
			if(keylist!=null && keylist.size()>0){
				throw new BaseException("关键词创建失败！"+text+"关键词"+matchtype+"类型已存在广告组中！");
			}
		}
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
	
		AmzAdvCampaigns keyObject = new AmzAdvCampaigns();
		keyObject.setCampaignid(list.get(0).getCampaignid());
		keyObject.setProfileid(profileId);
		String compaignType = "sp";
		String response = "";
		JSONArray array = new JSONArray();
			compaignType = "hsa";
			for (int i = 0; i < list.size(); i++) {
				AmzAdvKeywordsHsa amzAdvKeywords = list.get(i);
				amzAdvKeywords.setState(AdvState.pending);
				JSONObject param = new JSONObject();
				param.put("campaignId", amzAdvKeywords.getCampaignid());
				param.put("adGroupId", amzAdvKeywords.getAdgroupid());
				param.put("keywordText", amzAdvKeywords.getKeywordtext());
				param.put("matchType", amzAdvKeywords.getMatchtype().toLowerCase());
				param.put("bid", amzAdvKeywords.getBid());
				array.add(param);
			}
			String url = "/sb/keywords";
			response = apiBuildService.amzAdvPost(profile, url, array.toString());
		   if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < list.size(); i++) {
				AmzAdvKeywordsHsa amzAdvKeywords = list.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger keywordId = item.getBigInteger("keywordId");
					amzAdvKeywords.setKeywordid(keywordId);
					amzAdvKeywords.setCampaigntype(compaignType);
					amzAdvKeywords.setProfileid(profile.getId());
					amzAdvKeywords.setOpttime(new Date());
					 AmzAdvKeywordsHsa DbamzAdvKeywords = this.selectByKey(amzAdvKeywords);
					if (DbamzAdvKeywords == null) {
						this.amzAdvKeywordsHsaMapper.insert(amzAdvKeywords);
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
			for (int i = 0; i < list.size(); i++) {
				AmzAdvKeywordsHsa keywords = list.get(i);
				BigInteger key = keywords.getCampaignid().subtract(keywords.getAdgroupid());
				List<Object> keywordslist = map.get(key);
				if (keywordslist == null) {
					keywordslist = new ArrayList<Object>();
					map.put(key, keywordslist);
				}
				keywordslist.add(keywords);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvKeywordsHsa", user.getId(), profileId, map, null);
			return list;
		} else {
			throw new BaseException("关键词创建失败！");
		}
	}
	
	public List<AmzAdvKeywordsHsa> amzGetKeywords(UserInfo user, BigInteger profileid, Map<String, Object> param) {
		String keywordId=param.get("keywordId").toString();
		String url = "/sb/keywords/"+keywordId;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileid);
		String response = apiBuildService.amzAdvGet(profile, url);
		List<AmzAdvKeywordsHsa> list = new LinkedList<AmzAdvKeywordsHsa>();
		if (StringUtil.isNotEmpty(response)) {
			    JSONObject item = GeneralUtil.getJsonObject(response.toString());
				AmzAdvKeywordsHsa amzAdvKeywords = new AmzAdvKeywordsHsa();
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
				amzAdvKeywords.setProfileid(profile.getId());
				amzAdvKeywords.setOpttime(new Date());
				AmzAdvKeywordsHsa oldamzAdvKeywords = amzAdvKeywordsHsaMapper.selectByPrimaryKey(amzAdvKeywords);
				if (oldamzAdvKeywords != null) {
					this.updateAll(amzAdvKeywords);
				} else {
					this.save(amzAdvKeywords);
				}
				list.add(amzAdvKeywords);
		 
			return list;
		}
		return null;
	}
	
	public List<AmzAdvKeywordsHsa> amzListSBKeywords(AmzAdvProfile profile, Map<String, Object> param) {
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
		String response = apiBuildService.amzAdvGet(profile, url);
		List<AmzAdvKeywordsHsa> list = new LinkedList<AmzAdvKeywordsHsa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvKeywordsHsa amzAdvKeywords = new AmzAdvKeywordsHsa();
				amzAdvKeywords.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvKeywords.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvKeywords.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvKeywords.setKeywordtext(item.getString("keywordText"));
				amzAdvKeywords.setCampaigntype("hsa");
				amzAdvKeywords.setMatchtype(item.getString("matchType"));
				amzAdvKeywords.setBid(item.getBigDecimal("bid"));
				amzAdvKeywords.setState(item.getString("state"));
				amzAdvKeywords.setCreationDate(item.getDate("creationDate"));
				amzAdvKeywords.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				amzAdvKeywords.setServingStatus(item.getString("servingStatus"));
				amzAdvKeywords.setNativeLanguageKeyword(item.getString("nativeLanguageKeyword"));
				amzAdvKeywords.setProfileid(profile.getId());
				amzAdvKeywords.setOpttime(new Date());
				AmzAdvKeywordsHsa oldamzAdvKeywords = amzAdvKeywordsHsaMapper.selectByPrimaryKey(amzAdvKeywords);
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
	 
	public AmzAdvKeywordsHsa amzDeleteKeywords(UserInfo user,BigInteger  profileId, JSONObject param) {
		String keywordId=param.getString("keywordId");
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		Example example = new Example(AmzAdvKeywords.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("profileid", profileId);
		crit.andEqualTo("keywordid", keywordId);
		AmzAdvKeywordsHsa amzAdvKeywords = amzAdvKeywordsHsaMapper.selectOneByExample(example);
		AmzAdvKeywordsHsa oldamzAdvKeywords=new AmzAdvKeywordsHsa();
		BeanUtil.copyProperties(amzAdvKeywords, oldamzAdvKeywords, false);
		String response = "";
		if(amzAdvKeywords == null  ) {
			throw new BaseException("要归档的关键词不存在！");
		}
		String url = "/sb/keywords/" + keywordId;
		response = apiBuildService.amzAdvDelete(profile, url);
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
	
    public List<AmzAdvKeywordsHsa> convertJsonToBean(UserInfo user, String profileId, JSONArray keywordArray){
    	List<AmzAdvKeywordsHsa> keywordList = new ArrayList<AmzAdvKeywordsHsa>();
		for (int i = 0; i < keywordArray.size(); i++) {
			JSONObject keyword = keywordArray.getJSONObject(i);
			BigInteger campaignId= keyword.getBigInteger("campaignid");
			String keywordText= keyword.getString("keywordtext");
			BigInteger adGroupId = keyword.getBigInteger("adgroupid");
			BigInteger keywordId= keyword.getBigInteger("keywordid");
			String state= keyword.getString("state");
			BigDecimal bid= keyword.getBigDecimal("bid");
			String matchType= keyword.getString("matchtype");
			
			AmzAdvKeywordsHsa keywordobj = new AmzAdvKeywordsHsa();
			keywordobj.setState(state.toLowerCase());
			keywordobj.setBid(bid);
			keywordobj.setMatchtype(matchType.toLowerCase());
			keywordobj.setKeywordid(keywordId);
			keywordobj.setKeywordtext(keywordText);
			keywordobj.setAdgroupid(adGroupId);
			keywordobj.setCampaignid(campaignId);
			keywordList.add(keywordobj);
		}
		return keywordList;
    }
    
	@Override
	public List<AmzAdvKeywordsHsa> amzCreateKeywords(UserInfo user, String profileid, JSONArray keywords) {
		// TODO Auto-generated method stub
		return this.amzCreateKeywords(user, new BigInteger(profileid),this.convertJsonToBean(user, profileid, keywords));
	}

	@Override
	public List<AmzAdvKeywordsHsa> amzUpdateKeywords(UserInfo user, String profileid, JSONArray keywords) {
		// TODO Auto-generated method stub
		return this.amzUpdateKeywords(user, new BigInteger(profileid), this.convertJsonToBean(user, profileid, keywords));
	}


	@Override
	public PageList<Map<String, Object>> getKeywordsList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		return amzAdvKeywordsHsaMapper.getKeywordsList(map, pageBounds);
	}


	@Override
	public Map<String, Object> getSumAdvKeywords(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return amzAdvKeywordsHsaMapper.getSumAdvKeywords(map);
	}


	@Override
	public List<Map<String, Object>> getKeywordsChart(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return amzAdvKeywordsHsaMapper.getKeywordsHSAChart(map);
	}


	@Override
	public AmzAdvKeywordsHsa amzGetKeywords(UserInfo user, BigInteger profileid, String id) {
		// TODO Auto-generated method stub
			String url = "/sb/keywords/"+id;
			AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileid);
			String response = apiBuildService.amzAdvGet(profile, url);
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			AmzAdvKeywordsHsa amzAdvKeywords = new AmzAdvKeywordsHsa();
			amzAdvKeywords.setKeywordid(item.getBigInteger("keywordId"));
			amzAdvKeywords.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvKeywords.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvKeywords.setKeywordtext(item.getString("keywordText"));
			amzAdvKeywords.setMatchtype(item.getString("matchType"));
			amzAdvKeywords.setCampaigntype("hsa");
			amzAdvKeywords.setBid(item.getBigDecimal("bid"));
			amzAdvKeywords.setState(item.getString("state"));
			amzAdvKeywords.setCreationDate(item.getDate("creationDate"));
			amzAdvKeywords.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
			amzAdvKeywords.setServingStatus(item.getString("servingStatus"));
			amzAdvKeywords.setNativeLanguageKeyword(item.getString("nativeLanguageKeyword"));
			amzAdvKeywords.setProfileid(profile.getId());
			amzAdvKeywords.setOpttime(new Date());
			AmzAdvKeywordsHsa oldamzAdvKeywords = amzAdvKeywordsHsaMapper.selectByPrimaryKey(amzAdvKeywords);
			if (oldamzAdvKeywords != null) {
				this.updateAll(amzAdvKeywords);
			} else {
				this.save(amzAdvKeywords);
			}
			return amzAdvKeywords;
	}


	@Override
	public PageList<Map<String, Object>> getKeywordQueryList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		return amzAdvKeywordsHsaMapper.getKeywordQueryList(map, pageBounds);
	}


	@Override
	public List<Map<String, Object>> getKeywordQueryChart(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return amzAdvKeywordsHsaMapper.getKeywordQueryChart(map);
	}
	
	  

	
	 
}
