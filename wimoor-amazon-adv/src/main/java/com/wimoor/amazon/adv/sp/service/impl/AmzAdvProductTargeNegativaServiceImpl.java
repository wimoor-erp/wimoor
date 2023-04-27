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
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeNegativaSDService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductTargeNegativaMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeNegativaService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvProductTargeNegativaService")
public class AmzAdvProductTargeNegativaServiceImpl extends BaseService<AmzAdvProductTargeNegativa> implements IAmzAdvProductTargeNegativaService{

	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvProductTargeNegativaMapper amzAdvProductTargeNegativaMapper;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
    @Resource
    IAmzAdvProductTargeNegativaSDService amzAdvProductTargeNegativaSDService;
	public AmzAdvProductTargeNegativa amzGetNegativeTargetingClause(UserInfo user,BigInteger  profileId, String targetId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/negativeTargets/" + targetId;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = new AmzAdvProductTargeNegativa();
			amzAdvProductTargeNegativa.setTargetid(item.getBigInteger("targetId"));
			amzAdvProductTargeNegativa.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvProductTargeNegativa.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvProductTargeNegativa.setState(item.getString("state"));
			amzAdvProductTargeNegativa.setExpression(item.getJSONArray("expression").toJSONString());
			amzAdvProductTargeNegativa.setExpressiontype(item.getString("expressionType"));
			amzAdvProductTargeNegativa.setProfileid(profileId);
			amzAdvProductTargeNegativa.setOpttime(new Date());
			AmzAdvProductTargeNegativa oldamzAdvProductads = amzAdvProductTargeNegativaMapper.selectByPrimaryKey(amzAdvProductTargeNegativa);
			if (oldamzAdvProductads != null) {
				this.updateAll(amzAdvProductTargeNegativa);
			} else {
				this.save(amzAdvProductTargeNegativa);
			}
			return amzAdvProductTargeNegativa;
		}
		return null;
	}

	public AmzAdvProductTargeNegativa amzGetNegativeTargetingClauseEx(UserInfo user,BigInteger  profileId, String targetId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/negativeTargets/extended/" + targetId;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = new AmzAdvProductTargeNegativa();
			amzAdvProductTargeNegativa.setTargetid(item.getBigInteger("targetId"));
			amzAdvProductTargeNegativa.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvProductTargeNegativa.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvProductTargeNegativa.setState(item.getString("state"));
			amzAdvProductTargeNegativa.setExpression(item.getJSONArray("expression").toJSONString());
			amzAdvProductTargeNegativa.setExpressiontype(item.getString("expressionType"));
			amzAdvProductTargeNegativa.setCreationDate(item.getDate("creationDate"));
			amzAdvProductTargeNegativa.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
			amzAdvProductTargeNegativa.setServingStatus(item.getString("servingStatus"));
			amzAdvProductTargeNegativa.setProfileid(profileId);
			amzAdvProductTargeNegativa.setOpttime(new Date());
			AmzAdvProductTargeNegativa oldAmzAdvProductTargeNegativa = amzAdvProductTargeNegativaMapper.selectByPrimaryKey(amzAdvProductTargeNegativa);
			if (oldAmzAdvProductTargeNegativa != null) {
				this.updateAll(amzAdvProductTargeNegativa);
			} else {
				this.save(amzAdvProductTargeNegativa);
			}
			return amzAdvProductTargeNegativa;
		}
		return null;
	}

	public List<AmzAdvProductTargeNegativa> amzCreateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,
			List<AmzAdvProductTargeNegativa> productTargeList) {
		// TODO Auto-generated method stub
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/negativeTargets";
		JSONArray array = new JSONArray();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = productTargeList.get(i);
			JSONObject param = new JSONObject();
			param.put("campaignId", amzAdvProductTargeNegativa.getCampaignid());
			param.put("adGroupId", amzAdvProductTargeNegativa.getAdgroupid());
			param.put("state", amzAdvProductTargeNegativa.getState());
			param.put("expression", GeneralUtil.getJsonArray(amzAdvProductTargeNegativa.getExpression()));
			param.put("expressionType", amzAdvProductTargeNegativa.getExpressiontype());
			array.add(param);
		}
		String response = amzAdvAuthService.amzAdvPost(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = productTargeList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTargeNegativa.setTargetid(targetId);
					amzAdvProductTargeNegativa.setProfileid(profileId);
					amzAdvProductTargeNegativa.setOpttime(new Date());
					Example example = new Example(AmzAdvProductTargeNegativa.class);
					Criteria crit = example.createCriteria();
					crit.andEqualTo("targetid", targetId);
					crit.andEqualTo("profileid", profileId);
					crit.andEqualTo("adgroupid", targetId);
					crit.andEqualTo("campaignid", targetId);
					AmzAdvProductTargeNegativa dbAdvProductTargeNegativa = amzAdvProductTargeNegativaMapper.selectOneByExample(example);
					if(dbAdvProductTargeNegativa == null) {
						this.save(amzAdvProductTargeNegativa);
					}
				}else {
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("否定商品投放创建失败："+errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTargeNegativa productTargeNegativa = productTargeList.get(i);
				BigInteger key = productTargeNegativa.getCampaignid().subtract(productTargeNegativa.getAdgroupid());
				List<Object> targeNegativaList = map.get(key);
				if(targeNegativaList==null) {
					targeNegativaList=new ArrayList<Object>();
				    map.put(key, targeNegativaList);
				}
				targeNegativaList.add(productTargeNegativa);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTargeNegativa", user.getId(), profileId, map, null);
			return productTargeList;
		}else {
			throw new BaseException("创建失败！");
		}
	}

	public List<AmzAdvProductTargeNegativa> amzUpdateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,
			List<AmzAdvProductTargeNegativa> productTargeNegativaList) {
		// TODO Auto-generated method stub
		if (productTargeNegativaList == null || productTargeNegativaList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/negativeTargets";
		JSONArray array = new JSONArray();
		List<AmzAdvProductTargeNegativa> oldProductTargeNegativaList = new ArrayList<AmzAdvProductTargeNegativa>();
		for (int i = 0; i < productTargeNegativaList.size(); i++) {
			AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = productTargeNegativaList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("targetId", amzAdvProductTargeNegativa.getTargetid());
			param.put("campaignId", amzAdvProductTargeNegativa.getCampaignid());
			param.put("adGroupId", amzAdvProductTargeNegativa.getAdgroupid());
			param.put("state", amzAdvProductTargeNegativa.getState());
			param.put("expression", GeneralUtil.getJsonArray(amzAdvProductTargeNegativa.getExpression()));
			param.put("expressionType", amzAdvProductTargeNegativa.getExpressiontype());
			array.add(param);
			
			Example example = new Example(AmzAdvProductTargeNegativa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("targetid", amzAdvProductTargeNegativa.getTargetid());
			AmzAdvProductTargeNegativa productTargeNegativa = amzAdvProductTargeNegativaMapper.selectOneByExample(example);
			oldProductTargeNegativaList.add(productTargeNegativa);
		}
		String response = amzAdvAuthService.amzAdvPut(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < productTargeNegativaList.size(); i++) {
				AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = productTargeNegativaList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTargeNegativa.setTargetid(targetId);
					amzAdvProductTargeNegativa.setProfileid(profileId);
					amzAdvProductTargeNegativa.setOpttime(new Date());
					this.updateNotNull(amzAdvProductTargeNegativa);
				}else {
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("否定商品投放修改失败："+errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			Map<BigInteger, List<Object>> oldmap=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeNegativaList.size(); i++) {
				AmzAdvProductTargeNegativa productTargeNegativa = productTargeNegativaList.get(i);
				BigInteger key = productTargeNegativa.getCampaignid().subtract(productTargeNegativa.getAdgroupid());
				List<Object> targeNegativaList = map.get(key);
				if(targeNegativaList==null) {
					targeNegativaList=new ArrayList<Object>();
				    map.put(key, targeNegativaList);
				}
				targeNegativaList.add(productTargeNegativa);
			}
			for (int i = 0; i < oldProductTargeNegativaList.size(); i++) {
				AmzAdvProductTargeNegativa oldproductTargeNegativa = oldProductTargeNegativaList.get(i);
				BigInteger key = oldproductTargeNegativa.getCampaignid().subtract(oldproductTargeNegativa.getAdgroupid());
				List<Object> targeNegativaList = oldmap.get(key);
				if(targeNegativaList==null) {
					targeNegativaList=new ArrayList<Object>();
					oldmap.put(key, targeNegativaList);
				}
				targeNegativaList.add(oldproductTargeNegativa);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTargeNegativa", user.getId(), profileId, map, oldmap);
			return productTargeNegativaList;
		}
		return null;
	}

	public AmzAdvProductTargeNegativa amzArchiveNegativeTargetingClause(UserInfo user,BigInteger  profileId, String targetId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvProductTargeNegativa productTarge = null;
		AmzAdvProductTargeNegativa oldproductTarge = null;
		String url = "/sp/negativeTargets/" + targetId;
		String response = amzAdvAuthService.amzAdvDelete(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example = new Example(AmzAdvProductTargeNegativa.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("targetid", targetId);
				productTarge = amzAdvProductTargeNegativaMapper.selectOneByExample(example);
				oldproductTarge = amzAdvProductTargeNegativaMapper.selectOneByExample(example);
				productTarge.setState(AdvState.archived);
				productTarge.setOpttime(new Date());
				amzAdvProductTargeNegativaMapper.updateByPrimaryKey(productTarge);
				amzAdvOperateLogService.saveOperateLog("AmzAdvProductTargeNegativa", user.getId(), profileId, productTarge, oldproductTarge);
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

	public List<AmzAdvProductTargeNegativa> amzListNegativeTargetingClauses(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/negativeTargets/?";
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
		List<AmzAdvProductTargeNegativa> list = new LinkedList<AmzAdvProductTargeNegativa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = new AmzAdvProductTargeNegativa();
				amzAdvProductTargeNegativa.setTargetid(item.getBigInteger("targetId"));
				amzAdvProductTargeNegativa.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvProductTargeNegativa.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvProductTargeNegativa.setState(item.getString("state"));
				amzAdvProductTargeNegativa.setExpression(item.getJSONArray("expression").toJSONString());
				amzAdvProductTargeNegativa.setExpressiontype(item.getString("expressionType"));
				amzAdvProductTargeNegativa.setProfileid(profileId);
				amzAdvProductTargeNegativa.setOpttime(new Date());
				AmzAdvProductTargeNegativa oldamzAdvProductads = amzAdvProductTargeNegativaMapper.selectByPrimaryKey(amzAdvProductTargeNegativa);
				if (oldamzAdvProductads != null) {
					this.updateAll(amzAdvProductTargeNegativa);
				} else {
					this.save(amzAdvProductTargeNegativa);
				}
				list.add(amzAdvProductTargeNegativa);
			}
			return list;
		}
		return null;
	}

	public List<AmzAdvProductTargeNegativa> amzListNegativeTargetingClausesEx(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/negativeTargets/extended?";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "startIndex");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "count");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "campaignType");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "expressionTypeFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "stateFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "campaignIdFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "adGroupIdFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "creationDate");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "lastUpdatedDate");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "servingStatus");
		}
		url = url + ("".equals(paramurl) ? "" : paramurl);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvProductTargeNegativa> list = new LinkedList<AmzAdvProductTargeNegativa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = new AmzAdvProductTargeNegativa();
				amzAdvProductTargeNegativa.setTargetid(item.getBigInteger("targetId"));
				amzAdvProductTargeNegativa.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvProductTargeNegativa.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvProductTargeNegativa.setState(item.getString("state"));
				amzAdvProductTargeNegativa.setExpression(item.getJSONArray("expression").toJSONString());
				amzAdvProductTargeNegativa.setExpressiontype(item.getString("expressionType"));
				amzAdvProductTargeNegativa.setCreationDate(item.getDate("creationDate"));
				amzAdvProductTargeNegativa.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				amzAdvProductTargeNegativa.setServingStatus(item.getString("servingStatus"));
				amzAdvProductTargeNegativa.setProfileid(profileId);
				amzAdvProductTargeNegativa.setOpttime(new Date());
				AmzAdvProductTargeNegativa oldamzAdvProductads = amzAdvProductTargeNegativaMapper.selectByPrimaryKey(amzAdvProductTargeNegativa);
				if (oldamzAdvProductads != null) {
					this.updateNotNull(amzAdvProductTargeNegativa);
				} else {
					this.save(amzAdvProductTargeNegativa);
				}
				list.add(amzAdvProductTargeNegativa);
			}
			return list;
		}
		return null;
	}

	public List<AmzAdvProductTargeNegativa> amzCreateNegativeTargetingClauses_V3Hsa(UserInfo user,BigInteger  profileId,
			List<AmzAdvProductTargeNegativa> productTargeList) {
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/negativeTargets";
		JSONObject targetObject = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = productTargeList.get(i);
			JSONObject param = new JSONObject();
			param.put("campaignId", amzAdvProductTargeNegativa.getCampaignid());
			param.put("adGroupId", amzAdvProductTargeNegativa.getAdgroupid());
			param.put("expression", GeneralUtil.getJsonArray(amzAdvProductTargeNegativa.getExpression()));
			array.add(param);
		}
		targetObject.put("negativeTargets", array);
		String response = amzAdvAuthService.amzAdvPost(profile, url, targetObject.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONObject tagetObject = GeneralUtil.getJsonObject(response.toString());
			JSONArray successResults = tagetObject.getJSONArray("createTargetSuccessResults");
			JSONArray errorResults = tagetObject.getJSONArray("createTargetErrorResults");
			
			if(errorResults != null && errorResults.size() > 0) {
				JSONObject item = errorResults.getJSONObject(0);
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + item.getString("details");
				BaseException exception = new BaseException("否定商品投放创建失败："+errormsg);
				exception.setCode("ERROER");
				throw exception;
			}
			if(successResults != null && successResults.size() > 0) {
				for(int i = 0; i < successResults.size(); i++) {
					JSONObject item = successResults.getJSONObject(i);
					AmzAdvProductTargeNegativa amzAdvProductTarge = productTargeList.get(i);
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTarge.setTargetid(targetId);
					amzAdvProductTarge.setProfileid(profileId);
					amzAdvProductTarge.setOpttime(new Date());
					AmzAdvProductTargeNegativa DbamzAdvProductTarge = this.selectByKey(amzAdvProductTarge);
					if(DbamzAdvProductTarge == null) {
						this.save(amzAdvProductTarge);
					}
				}
			}
			Map<BigInteger, List<Object>> map = new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTargeNegativa productTargeNegativa = productTargeList.get(i);
				BigInteger key = productTargeNegativa.getCampaignid().subtract(productTargeNegativa.getAdgroupid());
				List<Object> targeNegativaList = map.get(key);
				if(targeNegativaList==null) {
					targeNegativaList=new ArrayList<Object>();
				    map.put(key, targeNegativaList);
				}
				targeNegativaList.add(productTargeNegativa);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTargeNegativa", user.getId(), profileId, map, null);
			return productTargeList;
		}else {
			throw new BaseException("创建失败！");
		}
	}

	public List<AmzAdvProductTargeNegativa> amzUpdateNegativeTargetingClauses_V3Hsa(UserInfo user,BigInteger  profileId,
			List<AmzAdvProductTargeNegativa> productTargeNegativaList) {
		// TODO Auto-generated method stub
		if (productTargeNegativaList == null || productTargeNegativaList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/negativeTargets";
		JSONArray array = new JSONArray();
		JSONObject targetObject = new JSONObject();
		List<AmzAdvProductTargeNegativa> oldProductTargeNegativaList = new ArrayList<AmzAdvProductTargeNegativa>();
		for (int i = 0; i < productTargeNegativaList.size(); i++) {
			AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = productTargeNegativaList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("targetId", amzAdvProductTargeNegativa.getTargetid());
			param.put("adGroupId", amzAdvProductTargeNegativa.getAdgroupid());
			param.put("state", amzAdvProductTargeNegativa.getState());
			array.add(param);
			
			Example example = new Example(AmzAdvProductTargeNegativa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("targetid", amzAdvProductTargeNegativa.getTargetid());
			AmzAdvProductTargeNegativa productTargeNegativa = amzAdvProductTargeNegativaMapper.selectOneByExample(example);
			oldProductTargeNegativaList.add(productTargeNegativa);
		}
		targetObject.put("negativeTargets", array);
		String response = amzAdvAuthService.amzAdvPut_V3(profile, url, targetObject.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONObject tagetObject = GeneralUtil.getJsonObject(response.toString());
			JSONArray successResults = tagetObject.getJSONArray("updateTargetSuccessResults");
			JSONArray errorResults = tagetObject.getJSONArray("updateTargetErrorResults");
			
			if(errorResults != null && errorResults.size() > 0) {
				JSONObject item = errorResults.getJSONObject(0);
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + item.getString("details");
				BaseException exception = new BaseException("否定商品投放创建失败："+errormsg);
				exception.setCode("ERROER");
				throw exception;
			}
			if(successResults != null && successResults.size() > 0) {
				for(int i = 0; i < successResults.size(); i++) {
					JSONObject item = successResults.getJSONObject(i);
					AmzAdvProductTargeNegativa amzAdvProductTarge = productTargeNegativaList.get(i);
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTarge.setTargetid(targetId);
					amzAdvProductTarge.setProfileid(profileId);
					amzAdvProductTarge.setOpttime(new Date());
					AmzAdvProductTargeNegativa DbamzAdvProductTarge = this.selectByKey(amzAdvProductTarge);
					if(DbamzAdvProductTarge == null) {
						this.save(amzAdvProductTarge);
					}
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			Map<BigInteger, List<Object>> oldmap=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeNegativaList.size(); i++) {
				AmzAdvProductTargeNegativa productTargeNegativa = productTargeNegativaList.get(i);
				BigInteger key = productTargeNegativa.getCampaignid().subtract(productTargeNegativa.getAdgroupid());
				List<Object> targeNegativaList = map.get(key);
				if(targeNegativaList==null) {
					targeNegativaList=new ArrayList<Object>();
				    map.put(key, targeNegativaList);
				}
				targeNegativaList.add(productTargeNegativa);
			}
			for (int i = 0; i < oldProductTargeNegativaList.size(); i++) {
				AmzAdvProductTargeNegativa oldproductTargeNegativa = oldProductTargeNegativaList.get(i);
				BigInteger key = oldproductTargeNegativa.getCampaignid().subtract(oldproductTargeNegativa.getAdgroupid());
				List<Object> targeNegativaList = oldmap.get(key);
				if(targeNegativaList==null) {
					targeNegativaList=new ArrayList<Object>();
					oldmap.put(key, targeNegativaList);
				}
				targeNegativaList.add(oldproductTargeNegativa);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTargeNegativa", user.getId(), profileId, map, oldmap);
			return productTargeNegativaList;
		}
		return null;
	}

	public AmzAdvProductTargeNegativa amzArchiveNegativeTargetingClause_V3Hsa(UserInfo user,BigInteger  profileId, String targetId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvProductTargeNegativa productTarge = null;
		AmzAdvProductTargeNegativa oldproductTarge = null;
		String url = "/sb/negativeTargets/" + targetId;
		String response = amzAdvAuthService.amzAdvDelete_V3(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example = new Example(AmzAdvProductTargeNegativa.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("targetid", targetId);
				productTarge = amzAdvProductTargeNegativaMapper.selectOneByExample(example);
				oldproductTarge = amzAdvProductTargeNegativaMapper.selectOneByExample(example);
				productTarge.setState(AdvState.archived);
				productTarge.setOpttime(new Date());
				amzAdvProductTargeNegativaMapper.updateByPrimaryKey(productTarge);
				amzAdvOperateLogService.saveOperateLog("AmzAdvProductTargeNegativa", user.getId(), profileId, productTarge, oldproductTarge);
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
	
	public AmzAdvProductTargeNegativa getNegativaTargetforQuery(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String profileid = map.get("profileid") == null ? null : map.get("profileid").toString();
		String query = map.get("query") == null ? null : map.get("query").toString();
		String adGroupid = map.get("adGroupid") == null ? null : map.get("adGroupid").toString();
		String campaignid = map.get("campaignid") == null ? null : map.get("campaignid").toString();
		Example example = new Example(AmzAdvProductTargeNegativa.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("state", "enabled");
		if(profileid != null)
			crit.andEqualTo("profileid", profileid);
		if(adGroupid != null)
			crit.andEqualTo("adgroupid", adGroupid);
		if(query != null)
			crit.andLike("expression", "%" + query + "%");
		if(campaignid != null)
			crit.andEqualTo("campaignid", campaignid);
		List<AmzAdvProductTargeNegativa> list = this.selectByExample(example);
		if(list != null && list.size() > 0) {
			map.put("TargetNegativaFlag", true);
			return list.get(0);
		}
		return null;
	}
	
	public PageList<Map<String, Object>> getProductNegativaTargeList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("HSA".equals(campaignType)||"SB".equals(campaignType)) {
				return null;
			}else if("SP".equals(campaignType)) {
				return amzAdvProductTargeNegativaMapper.getProductNegativaTargeList(map,pageBounds);
			}else if("SD".equals(campaignType)) {
				return amzAdvProductTargeNegativaSDService.getProductNegativaTargeList(map,pageBounds);
			} 
		}
		return amzAdvProductTargeNegativaMapper.getAllProductNegativaTargeList(map,pageBounds);
	}

}
