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
import com.wimoor.amazon.adv.common.pojo.AmzAdvOperateLog;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBidReCommendService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCampaignHsaService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvKeywordsHsaService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvKeywordsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvKeywordsService;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl.CampaignType;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
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
	@Resource
	IAmzAdvKeywordsHsaService amzAdvKeywordsHsaService;
	@Resource
	ApiBuildService apiBuildService;
 
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

	public List<AmzAdvKeywords> amzGetKeywords(UserInfo user,BigInteger  profileId,JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = "";
		String url = "/sp/keywords/list";
		response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		 List<AmzAdvKeywords> list=new ArrayList<AmzAdvKeywords>();
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONArray keywords = json.getJSONArray("keywords");
			AmzAdvKeywords amzAdvKeywords;
			for(int i=0;i<keywords.size();i++) {
				JSONObject item=keywords.getJSONObject(i);
				amzAdvKeywords = new AmzAdvKeywords();
				amzAdvKeywords.setKeywordid(item.getBigInteger("keywordId"));
				amzAdvKeywords.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvKeywords.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvKeywords.setState(item.getString("state").toLowerCase());
				amzAdvKeywords.setKeywordtext(item.getString("keywordText"));
				amzAdvKeywords.setMatchtype(item.getString("matchType").toLowerCase());
				amzAdvKeywords.setBid(item.getBigDecimal("bid"));
				amzAdvKeywords.setCampaigntype("sp");
				amzAdvKeywords.setProfileid(profileId);
				amzAdvKeywords.setOpttime(new Date());
				JSONObject extendedData = item.getJSONObject("extendedData");
				if(extendedData!=null) {
					String servingStatus=extendedData.getString("servingStatus");
					amzAdvKeywords.setServingStatus(servingStatus);
					Date creationDateTime=extendedData.getDate("creationDateTime");
					amzAdvKeywords.setCreationDate(creationDateTime);
					Date lastUpdateDateTime=extendedData.getDate("lastUpdateDateTime");
					amzAdvKeywords.setLastUpdatedDate(lastUpdateDateTime);
				}
				AmzAdvKeywords oldamzAdvKeywords = amzAdvKeywordsMapper.selectByPrimaryKey(amzAdvKeywords);
				if (oldamzAdvKeywords != null) {
					this.updateAll(amzAdvKeywords);
				} else {
					this.save(amzAdvKeywords);
				}
				list.add(amzAdvKeywords);
			}
	 
			if(StrUtil.isNotBlank(errormsg)) {
				BaseException exception = new BaseException(errormsg);
				exception.setCode("ERROR");
				throw exception;
			}
			return list;
		}
		return null;
	}

	public List<AmzAdvKeywords> amzDeleteKeywords(UserInfo user,BigInteger  profileId,JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = "";
		String url = "/sp/keywords/delete";
		response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		 List<AmzAdvKeywords> list=new ArrayList<AmzAdvKeywords>();
		if (StringUtil.isNotEmpty(response)) {
				List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
				String errormsg = "";
				JSONObject json = GeneralUtil.getJsonObject(response.toString());
				JSONObject keywordsJson = json.getJSONObject("keywords");
				JSONArray success = keywordsJson.getJSONArray("success");
				JSONArray error = keywordsJson.getJSONArray("error");
				for(int i=0;i<success.size();i++) {
					JSONObject item=success.getJSONObject(i);
					AmzAdvKeywords amzAdvKeywords = new AmzAdvKeywords();
					amzAdvKeywords.setKeywordid(item.getBigInteger("keywordId"));
					amzAdvKeywords.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvKeywords.setAdgroupid(item.getBigInteger("adGroupId"));
					amzAdvKeywords.setState(item.getString("state").toLowerCase());
					amzAdvKeywords.setKeywordtext(item.getString("keywordText"));
					amzAdvKeywords.setMatchtype(item.getString("matchType").toLowerCase());
					amzAdvKeywords.setBid(item.getBigDecimal("bid"));
					amzAdvKeywords.setCampaigntype("sp");
					amzAdvKeywords.setProfileid(profileId);
					amzAdvKeywords.setOpttime(new Date());
					AmzAdvKeywords oldkeywords = amzAdvKeywordsMapper.selectByPrimaryKey(amzAdvKeywords);
					if (oldkeywords != null) {
						this.updateAll(amzAdvKeywords);
					} else {
						this.save(amzAdvKeywords);
					}
					list.add(amzAdvKeywords);
					AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
					operateLog.setCampaignid(oldkeywords.getCampaignid());
					operateLog.setProfileid(profileId);
					operateLog.setOperator(user.getId());
					operateLog.setOpttime(new Date());
					operateLog.setBeanclasz("AmzAdvKeywords");
					String amzAdvProductadsjson = GeneralUtil.toJSON(amzAdvKeywords);
					String oldproductAdsjson = GeneralUtil.toJSON(oldkeywords);
					operateLog.setAfterobject(amzAdvProductadsjson);
					operateLog.setBeforeobject(oldproductAdsjson);
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

	/*
	 * biddableKeywords 里面的compaignType必须一致 当compaignType为 sp
	 * 时biddableKeywords必须小于1000，为 hsa 时则小于100 hsa状态下还有问题，有待商榷
	 */
	public List<AmzAdvKeywords> amzCreateKeywords(UserInfo user, BigInteger profileId, List<AmzAdvKeywords> list) {
		if (list == null || list.size() <= 0)
			return null;
		//先去判断list 有没有词和类型已经加入到这个广告组里
		for (int j = 0; j < list.size(); j++) {
			AmzAdvKeywords item = list.get(j);
			String text = item.getKeywordtext();
			String matchtype=item.getMatchtype();
			Example example=new Example(AmzAdvKeywords.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("keywordtext",text);
			Criteria crit2=example.and();
			crit2.orEqualTo("matchtype",matchtype.toLowerCase());
			crit2.orEqualTo("matchtype",matchtype.toUpperCase());
			crit.andEqualTo("adgroupid",item.getAdgroupid().toString());
			List<AmzAdvKeywords> keylist = amzAdvKeywordsMapper.selectByExample(example);
			if(keylist!=null && keylist.size()>0){
				throw new BaseException("关键词创建失败！"+text+"关键词"+matchtype+"类型已存在广告组中！");
			}
		}
		
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvCampaigns keyObject = new AmzAdvCampaigns();
		keyObject.setCampaignid(list.get(0).getCampaignid());
		keyObject.setProfileid(profileId);
		String response = "";
		JSONArray array = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				AmzAdvKeywords amzAdvKeywords = list.get(i);
				amzAdvKeywords.setState(AdvState.enabled);
				JSONObject param = new JSONObject();
				param.put("campaignId", amzAdvKeywords.getCampaignid().toString());
				param.put("adGroupId", amzAdvKeywords.getAdgroupid().toString());
				param.put("keywordText", amzAdvKeywords.getKeywordtext());
				param.put("matchType", amzAdvKeywords.getMatchtype().toUpperCase());
				param.put("state", amzAdvKeywords.getState().toUpperCase());
				param.put("bid", amzAdvKeywords.getBid());
				array.add(param);
			}
			JSONObject keywords=new JSONObject();
			keywords.put("keywords", array);
			String url = "/sp/keywords";
			response = apiBuildService.amzAdvPost(profile, url, keywords.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject keywordjsoin = json.getJSONObject("keywords");
			JSONArray success = keywordjsoin.getJSONArray("success");
			JSONArray error = keywordjsoin.getJSONArray("error");
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				AmzAdvKeywords amzAdvKeywords = list.get(index);
				BigInteger keywordId = item.getBigInteger("keywordId");
				amzAdvKeywords.setKeywordid(keywordId);
				amzAdvKeywords.setProfileid(profile.getId());
				amzAdvKeywords.setOpttime(new Date());
				amzAdvKeywords.setCampaigntype("sp");
				this.amzAdvKeywordsMapper.insert(amzAdvKeywords);
				
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				operateLog.setCampaignid(amzAdvKeywords.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvKeywords");
				String amzAdvProductadsjson = GeneralUtil.toJSON(amzAdvKeywords);
				operateLog.setAfterobject(amzAdvProductadsjson);
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
		} else {
			throw new BaseException("关键词创建失败！");
		}
	}

	/*
	 * biddableKeywords 里面的compaignType必须一致 当compaignType为 sp
	 * 时biddableKeywords必须小于1000，为 hsa 时则小于100 hsa状态下还有问题，有待商榷
	 */
	public List<AmzAdvKeywords> amzUpdateKeywords(UserInfo user,BigInteger  profileId, List<AmzAdvKeywords> list) {
		if (list == null || list.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		List<AmzAdvKeywords> oldbiddableKeywordList = new ArrayList<AmzAdvKeywords>();
		String response = "";
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			AmzAdvKeywords amzAdvKeywords = list.get(i);
			AmzAdvKeywords oldamzAdvKeywords = amzAdvKeywordsMapper.selectByPrimaryKey(amzAdvKeywords.getKeywordid());
			oldbiddableKeywordList.add(oldamzAdvKeywords);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("campaignId", amzAdvKeywords.getCampaignid().toString());
			param.put("adGroupId", amzAdvKeywords.getAdgroupid().toString());
			param.put("keywordText", amzAdvKeywords.getKeywordtext());
			param.put("matchType", amzAdvKeywords.getMatchtype().toUpperCase());
			param.put("keywordId", amzAdvKeywords.getKeywordid().toString());
			param.put("state", amzAdvKeywords.getState().toUpperCase());
			param.put("bid", amzAdvKeywords.getBid());
			array.add(param);
		}
		JSONObject keywords=new JSONObject();
		keywords.put("keywords", array);
		    String url = "/sp/keywords";
			response = apiBuildService.amzAdvPut(profile, url, keywords.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject keywordsJson = json.getJSONObject("keywords");
			JSONArray success = keywordsJson.getJSONArray("success");
			JSONArray error = keywordsJson.getJSONArray("error");
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				AmzAdvKeywords keyword = list.get(index);
				BigInteger keywordId = item.getBigInteger("keywordId");
				keyword.setKeywordid(keywordId);
				keyword.setProfileid(profile.getId());
				keyword.setOpttime(new Date());
				this.updateNotNull(keyword);
				
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvKeywords oldkeywords = oldbiddableKeywordList.get(index);
				operateLog.setCampaignid(oldkeywords.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvKeywords");
				String amzAdvProductadsjson = GeneralUtil.toJSON(keyword);
				String oldproductAdsjson = GeneralUtil.toJSON(oldkeywords);
				operateLog.setAfterobject(amzAdvProductadsjson);
				operateLog.setBeforeobject(oldproductAdsjson);
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
		String response = apiBuildService.amzAdvGet_V2(profile, url);
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

	 
 
	public PageList<Map<String, Object>> getKeywordsList(Map<String, Object> map, PageBounds pageBounds){
		String campaignType =map.get("campaignType")!=null? (String) map.get("campaignType"):null;
	   if(map.get("campaignid")!=null&&campaignType==null) {
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
			map.put("campaignType", "sp");
			list = amzAdvKeywordsMapper.getKeywordsListForSP(map, pageBounds);
		}
		else if("HSA".equals(campaignType)||"SB".equals(campaignType)) {
			map.put("campaignType", "hsa");
			//list = amzAdvKeywordsHsaMapper.getKeywordsListForSB(map, pageBounds);
		}
		else if("SD".equals(campaignType)) {
			map.put("campaignType", "sd");
			//list = amzAdvKeywordsMapper.getKeywordsList(map, pageBounds);
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
			if("SP".equals(campaignType.toUpperCase())) {
				list = amzAdvKeywordsMapper.getKeywordsChart(map);
			}
			else if("HSA".equals(campaignType.toUpperCase())||"SB".equals(campaignType.toUpperCase())) {
				listHsa = amzAdvKeywordsHsaService.getKeywordsChart(map);
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
		String campaignType =  map.get("campaignType").toString().toUpperCase();
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
					} else if ("HSA".equals(campaignType)||"SB".equals(campaignType)) {
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
			String campaignType = map.get("campaignType").toString().toUpperCase();
			if("SP".equals(campaignType)) {
				map.put("campaignType","sp");
				list = amzAdvKeywordsMapper.getKeywordQueryChart(map);
			}
			else if("HSA".equals(campaignType)||"SB".equals(campaignType)) {
				map.put("campaignType","hsa");
				if(map.get("matchType")!=null&&map.get("matchType").toString().equals("all")) {
					map.put("matchType",null);
				}
				listHsa = amzAdvKeywordsHsaService.getKeywordQueryChart(map);
			}
			else if("all".equals(campaignType)) {
				list = amzAdvKeywordsMapper.getKeywordQueryChart(map);
				listHsa = amzAdvKeywordsHsaService.getKeywordQueryChart(map);
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
			if("SP".equals(campaignType.toUpperCase())) {
				Map<String, Object> paraMap = amzAdvKeywordsMapper.getSumAdvKeywords(map);
				if(paraMap != null && paraMap.size() > 0) {
					list.add(paraMap);
					return list;
				}
			}
			else if("HSA".equals(campaignType.toUpperCase()) || "SB".equals(campaignType.toUpperCase())) {
				paralist = map.get("paralist");
				if(paralist != null) {
					if(paralist != null) {
						map.put("paralist", map.get("paralist").toString().replace("7d", "14d"));
					}
				}
				Map<String, Object> paraMap = amzAdvKeywordsHsaService.getSumAdvKeywords(map);
				if(paraMap != null && paraMap.size() > 0) {
					list.add(paraMap);
					return list;
				}
			}
		}
		return null;
	}

    public List<AmzAdvKeywords> convertJsonToBean(UserInfo user, String profileId, JSONArray keywordArray){
    	List<AmzAdvKeywords> keywordList = new ArrayList<AmzAdvKeywords>();
		for (int i = 0; i < keywordArray.size(); i++) {
			JSONObject keyword = keywordArray.getJSONObject(i);
			BigInteger campaignId= keyword.getBigInteger("campaignid");
			String keywordText= keyword.getString("keywordtext");
			  BigInteger adGroupId = keyword.getBigInteger("adgroupid");
			  BigInteger keywordId= keyword.getBigInteger("keywordid");
			String state= keyword.getString("state");
			BigDecimal bid= keyword.getBigDecimal("bid");
			String matchType= keyword.getString("matchtype");
			
			AmzAdvKeywords keywordobj = new AmzAdvKeywords();
			keywordobj.setState(state);
			keywordobj.setBid(bid);
			keywordobj.setMatchtype(matchType);
			keywordobj.setKeywordid(keywordId);
			keywordobj.setKeywordtext(keywordText);
			keywordobj.setAdgroupid(adGroupId);
			keywordobj.setCampaignid(campaignId);
			keywordList.add(keywordobj);
		}
		return keywordList;
    }
    
    private Map<String, String> getHeaderExt() {
		// TODO Auto-generated method stub
		Map<String,String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/vnd.spKeyword.v3+json");
    	header.put("Accept",       "application/vnd.spKeyword.v3+json");
		return header;
	}
    
	@Override
	public List<AmzAdvKeywords> amzCreateKeywords(UserInfo user, String profileid, JSONArray keywords) {
		// TODO Auto-generated method stub
		return this.amzCreateKeywords(user, new BigInteger(profileid), this.convertJsonToBean(user, profileid, keywords));
	}

	@Override
	public List<AmzAdvKeywords> amzUpdateKeywords(UserInfo user, String profileid, JSONArray keywords) {
		// TODO Auto-generated method stub
		return this.amzUpdateKeywords(user, new BigInteger(profileid), this.convertJsonToBean(user, profileid, keywords));
	}
	
}
