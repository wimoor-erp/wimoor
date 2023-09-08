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
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AdvState;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeSDService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductTargeMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvProductTargeService")
public class AmzAdvProductTargeServiceImpl extends BaseService<AmzAdvProductTarge> implements IAmzAdvProductTargeService{

	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvProductTargeMapper amzAdvProductTargeMapper;
	@Resource
	IAmzAdvProductTargeSDService amzAdvProductTargeSDService;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	public AmzAdvProductTarge amzGetTargetingClause(UserInfo user,BigInteger  profileId, String targetId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets/" + targetId;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		AmzAdvProductTarge amzAdvProductTarge = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			amzAdvProductTarge = new AmzAdvProductTarge();
			amzAdvProductTarge.setTargetid(item.getBigInteger("targetId"));
			amzAdvProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvProductTarge.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvProductTarge.setState(item.getString("state"));
			amzAdvProductTarge.setExpression(item.getJSONArray("expression").toJSONString());
			amzAdvProductTarge.setExpressiontype(item.getString("expressionType"));
			amzAdvProductTarge.setBid(item.getBigDecimal("bid"));
			amzAdvProductTarge.setProfileid(profileId);
			amzAdvProductTarge.setOpttime(new Date());
			AmzAdvProductTarge oldamzAdvProductads = amzAdvProductTargeMapper.selectByPrimaryKey(amzAdvProductTarge);
			if (oldamzAdvProductads != null) {
				this.updateAll(amzAdvProductTarge);
			} else {
				this.save(amzAdvProductTarge);
			}
			return amzAdvProductTarge;
		}
		return null;
	}

	public AmzAdvProductTarge amzGetTargetingClauseEx(UserInfo user,BigInteger  profileId, String targetId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets/extended/" + targetId;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		AmzAdvProductTarge amzAdvProductTarge = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			amzAdvProductTarge = new AmzAdvProductTarge();
			amzAdvProductTarge.setTargetid(item.getBigInteger("targetId"));
			amzAdvProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvProductTarge.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvProductTarge.setState(item.getString("state"));
			amzAdvProductTarge.setExpression(item.getJSONArray("expression").toJSONString());
			amzAdvProductTarge.setExpressiontype(item.getString("expressionType"));
			amzAdvProductTarge.setBid(item.getBigDecimal("bid"));
			amzAdvProductTarge.setCreationDate(item.getDate("creationDate"));
			amzAdvProductTarge.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
			amzAdvProductTarge.setServingStatus(item.getString("servingStatus"));
			amzAdvProductTarge.setProfileid(profileId);
			amzAdvProductTarge.setOpttime(new Date());
			AmzAdvProductTarge oldamzAdvProductTarge = amzAdvProductTargeMapper.selectByPrimaryKey(amzAdvProductTarge);
			if (oldamzAdvProductTarge != null) {
				this.updateAll(amzAdvProductTarge);
			} else {
				this.save(amzAdvProductTarge);
			}
			return amzAdvProductTarge;
		}
		return null;
	}

	public List<AmzAdvProductTarge> amzCreateTargetingClauses(UserInfo user,BigInteger  profileId, List<AmzAdvProductTarge> productTargeList) {
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets";
		JSONArray array = new JSONArray();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvProductTarge amzAdvProductTarge = productTargeList.get(i);
			JSONObject param = new JSONObject();
			param.put("campaignId", amzAdvProductTarge.getCampaignid());
			param.put("adGroupId", amzAdvProductTarge.getAdgroupid());
			param.put("state", amzAdvProductTarge.getState());
			param.put("expression", GeneralUtil.getJsonArray(amzAdvProductTarge.getExpression()));
			param.put("expressionType", amzAdvProductTarge.getExpressiontype());
			if(amzAdvProductTarge.getBid() != null) {
				param.put("bid", amzAdvProductTarge.getBid());
			}
			array.add(param);
		}
		String response = amzAdvAuthService.amzAdvPost(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTarge amzAdvProductTarge = productTargeList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTarge.setTargetid(targetId);
					amzAdvProductTarge.setProfileid(profileId);
					amzAdvProductTarge.setOpttime(new Date());
					AmzAdvProductTarge DbamzAdvProductTarge = this.selectByKey(amzAdvProductTarge);
					if(DbamzAdvProductTarge == null) {
						this.save(amzAdvProductTarge);
					}
				}else {
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("商品投放创建失败："+errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			Map<BigInteger, List<Object>> map = new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTarge productTarge = productTargeList.get(i);
				BigInteger key = productTarge.getCampaignid().subtract(productTarge.getAdgroupid());
				List<Object> targeList = map.get(key);
				if(targeList==null) {
					targeList=new ArrayList<Object>();
				    map.put(key, targeList);
				}
				targeList.add(productTarge);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTarge", user.getId(), profileId, map, null);
			return productTargeList;
		}else {
			throw new BaseException("创建失败！");
		}
	}

	public List<AmzAdvProductTarge> amzUpdateTargetingClauses(UserInfo user,BigInteger  profileId, List<AmzAdvProductTarge> productTargeList) {
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets";
		JSONArray array = new JSONArray();
		List<AmzAdvProductTarge> oldProductTargeList = new ArrayList<AmzAdvProductTarge>();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvProductTarge amzAdvProductTarge = productTargeList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("targetId", amzAdvProductTarge.getTargetid());
			param.put("campaignId", amzAdvProductTarge.getCampaignid());
			param.put("adGroupId", amzAdvProductTarge.getAdgroupid());
			param.put("state", amzAdvProductTarge.getState());
			param.put("expressionType", amzAdvProductTarge.getExpressiontype());
			if(!"auto".equals(amzAdvProductTarge.getExpressiontype())) {
				param.put("expression", GeneralUtil.getJsonArray(amzAdvProductTarge.getExpression()));
			}
			param.put("bid", amzAdvProductTarge.getBid());
			array.add(param);
			
			Example example = new Example(AmzAdvProductTarge.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("targetid", amzAdvProductTarge.getTargetid());
			AmzAdvProductTarge productTarge = amzAdvProductTargeMapper.selectOneByExample(example);
			oldProductTargeList.add(productTarge);
		}
		String response = amzAdvAuthService.amzAdvPut(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTarge amzAdvProductTarge = productTargeList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTarge.setTargetid(targetId);
					amzAdvProductTarge.setProfileid(profileId);
					amzAdvProductTarge.setOpttime(new Date());
					this.updateNotNull(amzAdvProductTarge);
				}else {
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("商品投放修改失败："+errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			Map<BigInteger, List<Object>> oldmap=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTarge productTarge = productTargeList.get(i);
				BigInteger key = productTarge.getCampaignid().subtract(productTarge.getAdgroupid());
				List<Object> targeList = map.get(key);
				if(targeList==null) {
					targeList=new ArrayList<Object>();
				    map.put(key, targeList);
				}
				targeList.add(productTarge);
			}
			for (int i = 0; i < oldProductTargeList.size(); i++) {
				AmzAdvProductTarge oldproductTarge = oldProductTargeList.get(i);
				BigInteger key = oldproductTarge.getCampaignid().subtract(oldproductTarge.getAdgroupid());
				List<Object> targeList = oldmap.get(key);
				if(targeList==null) {
					targeList=new ArrayList<Object>();
					oldmap.put(key, targeList);
				}
				targeList.add(oldproductTarge);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTarge", user.getId(), profileId, map, oldmap);
			return productTargeList;
		}
		return null;
	}

	public AmzAdvProductTarge amzArchiveTargetingClause(UserInfo user,BigInteger  profileId, String targetId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvProductTarge productTarge = null;
		AmzAdvProductTarge oldproductTarge = null;
		String url = "/sp/targets/" + targetId;
		String response = amzAdvAuthService.amzAdvDelete(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example = new Example(AmzAdvProductTarge.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("targetid", targetId);
				productTarge = amzAdvProductTargeMapper.selectOneByExample(example);
				oldproductTarge = amzAdvProductTargeMapper.selectOneByExample(example);
				productTarge.setState(AdvState.archived);
				productTarge.setOpttime(new Date());
				this.updateAll(productTarge);
				amzAdvOperateLogService.saveOperateLog("AmzAdvProductTarge", user.getId(), profileId, productTarge, oldproductTarge);
				return productTarge;
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

	public List<AmzAdvProductTarge> amzListTargetingClauses(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets/?";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "startIndex");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "count");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "expressionTypeFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "expressionText");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "stateFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "campaignIdFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "adGroupIdFilter");
		}
		url = url + ("".equals(paramurl) ? "" : paramurl);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvProductTarge> list = new LinkedList<AmzAdvProductTarge>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvProductTarge amzAdvProductTarge = new AmzAdvProductTarge();
				amzAdvProductTarge.setTargetid(item.getBigInteger("targetId"));
				amzAdvProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvProductTarge.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvProductTarge.setState(item.getString("state"));
				amzAdvProductTarge.setExpression(item.getJSONArray("expression").toJSONString());
				amzAdvProductTarge.setExpressiontype(item.getString("expressionType"));
				amzAdvProductTarge.setBid(item.getBigDecimal("bid"));
				amzAdvProductTarge.setProfileid(profileId);
				amzAdvProductTarge.setOpttime(new Date());
				AmzAdvProductTarge oldamzAdvProductads = amzAdvProductTargeMapper.selectByPrimaryKey(amzAdvProductTarge);
				if (oldamzAdvProductads != null) {
					this.updateAll(amzAdvProductTarge);
				} else {
					this.save(amzAdvProductTarge);
				}
				list.add(amzAdvProductTarge);
			}
			return list;
		}
		return null;
	}

	public List<AmzAdvProductTarge> amzListTargetingClausesEx(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets/extended?";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "startIndex");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "count");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "campaignType");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "expressionTypeFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "stateFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "campaignIdFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "adGroupIdFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "targetIdFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "creationDate");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "lastUpdatedDate");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "servingStatus");
		}
		url = url + ("".equals(paramurl) ? "" : paramurl);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvProductTarge> list = new LinkedList<AmzAdvProductTarge>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvProductTarge amzAdvProductTarge = new AmzAdvProductTarge();
				amzAdvProductTarge.setTargetid(item.getBigInteger("targetId"));
				amzAdvProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvProductTarge.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvProductTarge.setState(item.getString("state"));
				amzAdvProductTarge.setExpression(item.getJSONArray("expression").toJSONString());
				amzAdvProductTarge.setExpressiontype(item.getString("expressionType"));
				amzAdvProductTarge.setBid(item.getBigDecimal("bid"));
				amzAdvProductTarge.setCreationDate(item.getDate("creationDate"));
				amzAdvProductTarge.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				amzAdvProductTarge.setServingStatus(item.getString("servingStatus"));
				amzAdvProductTarge.setProfileid(profileId);
				amzAdvProductTarge.setOpttime(new Date());
				AmzAdvProductTarge oldamzAdvProductads = amzAdvProductTargeMapper.selectByPrimaryKey(amzAdvProductTarge);
				if (oldamzAdvProductads != null) {
					this.updateAll(amzAdvProductTarge);
				} else {
					this.save(amzAdvProductTarge);
				}
				list.add(amzAdvProductTarge);
			}
			return list;
		}
		return null;
	}
	
	public List<AmzAdvProductTarge> amzCreateTargetingClauses_V3Hsa(UserInfo user,BigInteger  profileId, List<AmzAdvProductTarge> productTargeList) {
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/targets";
		JSONObject paramTarget = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvProductTarge amzAdvProductTarge = productTargeList.get(i);
			JSONObject param = new JSONObject();
			param.put("campaignId", amzAdvProductTarge.getCampaignid());
			param.put("adGroupId", amzAdvProductTarge.getAdgroupid());
			param.put("expression", GeneralUtil.getJsonArray(amzAdvProductTarge.getExpression()));
			if(amzAdvProductTarge.getBid() != null) {
				param.put("bid", amzAdvProductTarge.getBid());
			}
			array.add(param);
		}
		paramTarget.put("targets", array);
		String response = amzAdvAuthService.amzAdvPost_V3(profile, url, paramTarget.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONObject tagetObject = GeneralUtil.getJsonObject(response.toString());
			JSONArray successResults = tagetObject.getJSONArray("createTargetSuccessResults");
			JSONArray errorResults = tagetObject.getJSONArray("createTargetErrorResults");
			
			if(errorResults != null && errorResults.size() > 0) {
				JSONObject item = errorResults.getJSONObject(0);
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + item.getString("details");
				BaseException exception = new BaseException("商品投放创建失败："+errormsg);
				exception.setCode("ERROER");
				throw exception;
			}
			if(successResults != null && successResults.size() > 0) {
				for(int i = 0; i < successResults.size(); i++) {
					JSONObject item = successResults.getJSONObject(i);
					AmzAdvProductTarge amzAdvProductTarge = productTargeList.get(i);
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTarge.setTargetid(targetId);
					amzAdvProductTarge.setProfileid(profileId);
					amzAdvProductTarge.setOpttime(new Date());
					AmzAdvProductTarge DbamzAdvProductTarge = this.selectByKey(amzAdvProductTarge);
					if(DbamzAdvProductTarge == null) {
						this.save(amzAdvProductTarge);
					}
				}
			}
			Map<BigInteger, List<Object>> map = new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTarge productTarge = productTargeList.get(i);
				BigInteger key = productTarge.getCampaignid().subtract(productTarge.getAdgroupid());
				List<Object> targeList = map.get(key);
				if(targeList==null) {
					targeList=new ArrayList<Object>();
				    map.put(key, targeList);
				}
				targeList.add(productTarge);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTarge", user.getId(), profileId, map, null);
			return productTargeList;
		}else {
			throw new BaseException("创建失败！");
		}
	}

	public List<AmzAdvProductTarge> amzUpdateTargetingClauses_V3Hsa(UserInfo user,BigInteger  profileId, List<AmzAdvProductTarge> productTargeList) {
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/targets";
		JSONArray array = new JSONArray();
		List<AmzAdvProductTarge> oldProductTargeList = new ArrayList<AmzAdvProductTarge>();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvProductTarge amzAdvProductTarge = productTargeList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("targetId", amzAdvProductTarge.getTargetid());
			param.put("campaignId", amzAdvProductTarge.getCampaignid());
			param.put("adGroupId", amzAdvProductTarge.getAdgroupid());
			param.put("state", amzAdvProductTarge.getState());
			param.put("bid", amzAdvProductTarge.getBid());
			array.add(param);
			
			Example example = new Example(AmzAdvProductTarge.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("targetid", amzAdvProductTarge.getTargetid());
			AmzAdvProductTarge productTarge = amzAdvProductTargeMapper.selectOneByExample(example);
			oldProductTargeList.add(productTarge);
		}
		String response = amzAdvAuthService.amzAdvPut_V3(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONObject targetObject = GeneralUtil.getJsonObject(response.toString());
			JSONArray successResults = targetObject.getJSONArray("updateTargetSuccessResults");
			JSONArray errorResults = targetObject.getJSONArray("updateTargetErrorResults");
			
			if(errorResults != null && errorResults.size() > 0) {
				JSONObject item = errorResults.getJSONObject(0);
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + item.getString("details");
				BaseException exception = new BaseException("商品投放创建失败："+errormsg);
				exception.setCode("ERROER");
				throw exception;
			}
			if(successResults != null && successResults.size() > 0) {
				for(int i = 0; i < successResults.size(); i++) {
					JSONObject item = successResults.getJSONObject(i);
					AmzAdvProductTarge amzAdvProductTarge = productTargeList.get(i);
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTarge.setTargetid(targetId);
					amzAdvProductTarge.setProfileid(profileId);
					amzAdvProductTarge.setOpttime(new Date());
					AmzAdvProductTarge DbamzAdvProductTarge = this.selectByKey(amzAdvProductTarge);
					if(DbamzAdvProductTarge == null) {
						this.save(amzAdvProductTarge);
					}
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			Map<BigInteger, List<Object>> oldmap=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTarge productTarge = productTargeList.get(i);
				BigInteger key = productTarge.getCampaignid().subtract(productTarge.getAdgroupid());
				List<Object> targeList = map.get(key);
				if(targeList==null) {
					targeList=new ArrayList<Object>();
				    map.put(key, targeList);
				}
				targeList.add(productTarge);
			}
			for (int i = 0; i < oldProductTargeList.size(); i++) {
				AmzAdvProductTarge oldproductTarge = oldProductTargeList.get(i);
				BigInteger key = oldproductTarge.getCampaignid().subtract(oldproductTarge.getAdgroupid());
				List<Object> targeList = oldmap.get(key);
				if(targeList==null) {
					targeList=new ArrayList<Object>();
					oldmap.put(key, targeList);
				}
				targeList.add(oldproductTarge);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTarge", user.getId(), profileId, map, oldmap);
			return productTargeList;
		}
		return null;
	}

	public AmzAdvProductTarge amzArchiveTargetingClause_V3Hsa(UserInfo user,BigInteger  profileId, String targetId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvProductTarge productTarge = null;
		AmzAdvProductTarge oldproductTarge = null;
		String url = "/sb/targets/" + targetId;
		String response = amzAdvAuthService.amzAdvDelete_V3(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example = new Example(AmzAdvProductTarge.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("targetid", targetId);
				productTarge = amzAdvProductTargeMapper.selectOneByExample(example);
				oldproductTarge = amzAdvProductTargeMapper.selectOneByExample(example);
				productTarge.setState(AdvState.archived);
				productTarge.setOpttime(new Date());
				this.updateAll(productTarge);
				amzAdvOperateLogService.saveOperateLog("AmzAdvProductTarge", user.getId(), profileId, productTarge, oldproductTarge);
				return productTarge;
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

	public PageList<Map<String, Object>> getProductTargeList(Map<String,Object> map, PageBounds pageBounds){
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("HSA".equals(campaignType)) {
				return null;
			}else if("SP".equals(campaignType)) {
				return amzAdvProductTargeMapper.getProductTargeList(map, pageBounds);
			}else if("SD".equals(campaignType)) {
				return amzAdvProductTargeSDService.getProductTargeList(map,pageBounds);
			}
		}
		return amzAdvProductTargeMapper.getProductTargeList(map, pageBounds);
	}

	public Map<String, Object> getProductTargeChart(Map<String, Object> map) {
		List<Map<String,Object>> listSP = null;
		List<Map<String,Object>> listSD = null;
		getSerchStr(map);
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("HSA".equals(campaignType)||"SB".equals(campaignType)) {
				return null;
			}else if("SP".equals(campaignType.toUpperCase())) {
				listSP = amzAdvProductTargeMapper.getProductTargeChart(map);
			}else if("SD".equals(campaignType.toUpperCase())) {
				listSD =amzAdvProductTargeSDService.getProductTargeChart(map);
			}
		}else {
			listSP = amzAdvProductTargeMapper.getProductTargeChart(map);
		}
	
		return amzAdvCampaignService.getChartData(listSP, null, listSD, map);
	}
	
	
	public void getSerchStr(Map<String,Object> map) {
		String serch = (String) map.get("searchlist");
		String[] serchArray = serch.split(",");
		String serchlist = "";
		for (int i = 0; i < serchArray.length; i++) {
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
		}
		if(serchlist.contains(",")) {
			serchlist=serchlist.substring(0, serchlist.length()-1);
		}
		map.put("serchlist", serchlist);
		map.put("value1", serchArray[0]);
	}

	public List<Map<String, Object>> getTargetCategories(Map<String, Object> map) {
		return amzAdvProductTargeMapper.getTargetCategories(map);
	}

	public List<Map<String, Object>> getSumProductTarge(Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Object paralist = map.get("paralist");
		if(paralist != null) {
			paralist = paralist.toString().replace("CSRT", "attributedConversions7d / clicks")
				.replace("ACOS", "cost / attributedSales7d").replace("ROAS", "attributedSales7d / cost");
			map.put("paralist", paralist);
		}
		String campaignType=null;
		if(map.get("campaignType")!=null) {
			campaignType=(String) map.get("campaignType");
		}
		if(campaignType!=null&&"sd".equals(campaignType.toLowerCase())) {
			Map<String, Object> param = amzAdvProductTargeSDService.getSumProductTarge(map);
			if(param != null && param.size() > 0) {
				list.add(param);
			}
		}else {
			Map<String, Object> param = amzAdvProductTargeMapper.getSumProductTarge(map);
			if(param != null && param.size() > 0) {
				list.add(param);
			}
		}
		return list;
	}

}
