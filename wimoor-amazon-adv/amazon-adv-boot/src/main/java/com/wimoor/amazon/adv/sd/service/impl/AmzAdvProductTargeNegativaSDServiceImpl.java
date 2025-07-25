package com.wimoor.amazon.adv.sd.service.impl;

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
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sd.dao.AmzAdvProductTargeNegativaSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeNegativaSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeNegativaSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;
@Service("amzAdvProductTargeNegativaSDService")
public class AmzAdvProductTargeNegativaSDServiceImpl  extends BaseService<AmzAdvProductTargeNegativaSD> implements IAmzAdvProductTargeNegativaSDService{
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvProductTargeNegativaSDMapper amzAdvProductTargeNegativaSDMapper;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	@Resource
	ApiBuildService apiBuildService;
	public PageList<Map<String, Object>> getProductNegativaTargeList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		return amzAdvProductTargeNegativaSDMapper.getProductNegativaTargeList(map,pageBounds);
	}
	public List<AmzAdvProductTargeNegativaSD> amzListNegativeTargetingClauses(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/negativeTargets/?";
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
		String response = apiBuildService.amzAdvGet(profile, url);
		List<AmzAdvProductTargeNegativaSD> list = new LinkedList<AmzAdvProductTargeNegativaSD>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvProductTargeNegativaSD amzAdvProductTargeNegativa = new AmzAdvProductTargeNegativaSD();
				amzAdvProductTargeNegativa.setTargetid(item.getBigInteger("targetId"));
				amzAdvProductTargeNegativa.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvProductTargeNegativa.setState(item.getString("state"));
				amzAdvProductTargeNegativa.setExpression(item.getJSONArray("expression").toJSONString());
				amzAdvProductTargeNegativa.setExpressiontype(item.getString("expressionType"));
				amzAdvProductTargeNegativa.setProfileid(profileId);
				amzAdvProductTargeNegativa.setOpttime(new Date());
				AmzAdvProductTargeNegativaSD oldamzAdvProductads = amzAdvProductTargeNegativaSDMapper.selectByPrimaryKey(amzAdvProductTargeNegativa);
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

	public List<AmzAdvProductTargeNegativaSD> amzListNegativeTargetingClausesEx(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
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
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		List<AmzAdvProductTargeNegativaSD> list = new LinkedList<AmzAdvProductTargeNegativaSD>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvProductTargeNegativaSD amzAdvProductTargeNegativa = new AmzAdvProductTargeNegativaSD();
				amzAdvProductTargeNegativa.setTargetid(item.getBigInteger("targetId"));
				amzAdvProductTargeNegativa.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvProductTargeNegativa.setState(item.getString("state"));
				amzAdvProductTargeNegativa.setExpression(item.getJSONArray("expression").toJSONString());
				amzAdvProductTargeNegativa.setExpressiontype(item.getString("expressionType"));
				amzAdvProductTargeNegativa.setProfileid(profileId);
				amzAdvProductTargeNegativa.setOpttime(new Date());
				AmzAdvProductTargeNegativaSD oldamzAdvProductads = amzAdvProductTargeNegativaSDMapper.selectByPrimaryKey(amzAdvProductTargeNegativa);
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

	public List<AmzAdvProductTargeNegativaSD> amzCreateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,
			List<AmzAdvProductTargeNegativaSD> productTargeList) {
		// TODO Auto-generated method stub
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/negativeTargets";
		JSONArray array = new JSONArray();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvProductTargeNegativaSD amzAdvProductTargeNegativa = productTargeList.get(i);
			JSONObject param = new JSONObject();
			param.put("adGroupId", amzAdvProductTargeNegativa.getAdgroupid());
			param.put("state", amzAdvProductTargeNegativa.getState());
			param.put("expression", GeneralUtil.getJsonArray(amzAdvProductTargeNegativa.getExpression()));
			param.put("expressionType", amzAdvProductTargeNegativa.getExpressiontype());
			array.add(param);
		}
		String response = apiBuildService.amzAdvPost(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray results =null; 
			results = GeneralUtil.getJsonArray(response.toString());
			if(results != null && results.size() > 0) {
				JSONObject item = results.getJSONObject(0);
				if(item.getString("code")!=null&&!"SUCCESS".equals(item.getString("code"))) {
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("否定商品投放创建失败：【"+item.getString("code")+"】"+errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			if(results != null && results.size() > 0) {
				for(int i = 0; i < results.size(); i++) {
					JSONObject item = results.getJSONObject(i);
					AmzAdvProductTargeNegativaSD amzAdvProductTarge = productTargeList.get(i);
					BigInteger targetId = item.getBigInteger("targetId");
					if(targetId==null) {
						continue;
					}
					amzAdvProductTarge.setTargetid(targetId);
					amzAdvProductTarge.setProfileid(profileId);
					amzAdvProductTarge.setOpttime(new Date());
					AmzAdvProductTargeNegativaSD DbamzAdvProductTarge = this.selectByKey(amzAdvProductTarge);
					if(DbamzAdvProductTarge == null) {
						this.save(amzAdvProductTarge);
					}
				}
			}
			Map<BigInteger, List<Object>> map = new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTargeNegativaSD productTargeNegativa = productTargeList.get(i);
				BigInteger key =productTargeNegativa.getAdgroupid();
				List<Object> targeNegativaList = map.get(key);
				if(targeNegativaList==null) {
					targeNegativaList=new ArrayList<Object>();
				    map.put(key, targeNegativaList);
				}
				targeNegativaList.add(productTargeNegativa);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTargeNegativaSD", user.getId(), profileId, map, null);
			return productTargeList;
		}else {
			throw new BaseException("创建失败！");
		}
	}

	public List<AmzAdvProductTargeNegativaSD> amzUpdateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,
			List<AmzAdvProductTargeNegativaSD> productTargeNegativaList) {
		// TODO Auto-generated method stub
		if (productTargeNegativaList == null || productTargeNegativaList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/negativeTargets";
		JSONArray array = new JSONArray();
		List<AmzAdvProductTargeNegativaSD> oldProductTargeNegativaList = new ArrayList<AmzAdvProductTargeNegativaSD>();
		for (int i = 0; i < productTargeNegativaList.size(); i++) {
			AmzAdvProductTargeNegativaSD amzAdvProductTargeNegativa = productTargeNegativaList.get(i);
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
			AmzAdvProductTargeNegativaSD productTargeNegativa = amzAdvProductTargeNegativaSDMapper.selectOneByExample(example);
			oldProductTargeNegativaList.add(productTargeNegativa);
		}
		//targetObject.put("negativeTargets", array);
		String response = apiBuildService.amzAdvPut(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			JSONArray result = GeneralUtil.getJsonArray(response.toString());
			if(result != null && result.size() > 0) {
				for(int i = 0; i < result.size(); i++) {
					JSONObject item = result.getJSONObject(i);
					if(item.getString("code").equals("SUCCESS")) {
						AmzAdvProductTargeNegativaSD amzAdvProductTarge = productTargeNegativaList.get(i);
						BigInteger targetId = item.getBigInteger("targetId");
						amzAdvProductTarge.setTargetid(targetId);
						amzAdvProductTarge.setProfileid(profileId);
						amzAdvProductTarge.setOpttime(new Date());
						AmzAdvProductTargeNegativaSD DbamzAdvProductTarge = this.selectByKey(amzAdvProductTarge);
						if(DbamzAdvProductTarge == null) {
							this.save(amzAdvProductTarge);
						}
					}
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			Map<BigInteger, List<Object>> oldmap=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeNegativaList.size(); i++) {
				AmzAdvProductTargeNegativaSD productTargeNegativa = productTargeNegativaList.get(i);
				BigInteger key =  productTargeNegativa.getAdgroupid();
				List<Object> targeNegativaList = map.get(key);
				if(targeNegativaList==null) {
					targeNegativaList=new ArrayList<Object>();
				    map.put(key, targeNegativaList);
				}
				targeNegativaList.add(productTargeNegativa);
			}
			for (int i = 0; i < oldProductTargeNegativaList.size(); i++) {
				AmzAdvProductTargeNegativaSD oldproductTargeNegativa = oldProductTargeNegativaList.get(i);
				BigInteger key =  oldproductTargeNegativa.getAdgroupid();
				List<Object> targeNegativaList = oldmap.get(key);
				if(targeNegativaList==null) {
					targeNegativaList=new ArrayList<Object>();
					oldmap.put(key, targeNegativaList);
				}
				targeNegativaList.add(oldproductTargeNegativa);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTargeNegativaSD", user.getId(), profileId, map, oldmap);
			return productTargeNegativaList;
		}
		return null;
	}

	public AmzAdvProductTargeNegativaSD amzArchiveNegativeTargetingClause(UserInfo user,BigInteger  profileId, String targetId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvProductTargeNegativaSD productTarge = null;
		AmzAdvProductTargeNegativaSD oldproductTarge = null;
		String url = "/sd/negativeTargets/" + targetId;
		String response = apiBuildService.amzAdvDelete(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example = new Example(AmzAdvProductTargeNegativaSD.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("targetid", targetId);
				productTarge = amzAdvProductTargeNegativaSDMapper.selectOneByExample(example);
				oldproductTarge = amzAdvProductTargeNegativaSDMapper.selectOneByExample(example);
				productTarge.setState(AdvState.archived);
				productTarge.setOpttime(new Date());
				amzAdvProductTargeNegativaSDMapper.updateByPrimaryKey(productTarge);
				amzAdvOperateLogService.saveOperateLog("AmzAdvProductTargeNegativaSD", user.getId(), profileId, productTarge, oldproductTarge);
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
	@Override
	public AmzAdvProductTargeNegativaSD amzNegativeTargetingClauses(UserInfo user, BigInteger profileid,String targetId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileid);
		String url = "/sd/negativeTargets/extended/"+targetId;
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			    AmzAdvProductTargeNegativaSD amzAdvProductTarge = new AmzAdvProductTargeNegativaSD();
				amzAdvProductTarge.setTargetid(item.getBigInteger("targetId"));
				amzAdvProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvProductTarge.setState(item.getString("state"));
				amzAdvProductTarge.setExpression(item.getJSONArray("expression").toJSONString());
				amzAdvProductTarge.setExpressiontype(item.getString("expressionType"));
				amzAdvProductTarge.setProfileid(profileid);
				amzAdvProductTarge.setOpttime(new Date());
				String servingStatus=item.getString("servingStatus");
				amzAdvProductTarge.setServingStatus(servingStatus);
				Date creationDateTime=item.getDate("creationDateTime");
				amzAdvProductTarge.setCreationDate(creationDateTime);
				Date lastUpdateDateTime=item.getDate("lastUpdateDateTime");
				amzAdvProductTarge.setLastUpdatedDate(lastUpdateDateTime);
				AmzAdvProductTargeNegativaSD oldamzAdvProductads = amzAdvProductTargeNegativaSDMapper.selectByPrimaryKey(amzAdvProductTarge);
				if (oldamzAdvProductads != null) {
					this.updateAll(amzAdvProductTarge);
				} else {
					this.save(amzAdvProductTarge);
				}
				return amzAdvProductTarge;
		 
		}
		return null;
	}
	
	 public List<AmzAdvProductTargeNegativaSD> convertJsonToBean(UserInfo user, String profileId, JSONArray keywordArray){
	    	List<AmzAdvProductTargeNegativaSD> targetList = new ArrayList<AmzAdvProductTargeNegativaSD>();
			for (int i = 0; i < keywordArray.size(); i++) {
				JSONObject target = keywordArray.getJSONObject(i);
				//BigInteger campaignId= target.getBigInteger("campaignid");
				BigInteger adGroupId = target.getBigInteger("adgroupid");
				BigInteger targetid= target.getBigInteger("targetid");
				String state= target.getString("state");
				String expressiontype=target.getString("expressiontype");
				String expression=target.getString("expression");
				AmzAdvProductTargeNegativaSD targetobj = new AmzAdvProductTargeNegativaSD();
				targetobj.setState(state.toLowerCase());
				targetobj.setTargetid(targetid);
				targetobj.setExpressiontype(expressiontype);
				targetobj.setExpression(expression);
				targetobj.setAdgroupid(adGroupId);
				targetList.add(targetobj);
			}
			return targetList;
	    }
	 
	@Override
	public List<AmzAdvProductTargeNegativaSD> amzUpdateNegativeTargetingClauses(UserInfo user, String profileid,
			JSONArray keywords) {
		// TODO Auto-generated method stub
		return this.amzUpdateNegativeTargetingClauses(user, new BigInteger(profileid), this.convertJsonToBean(user, profileid, keywords));
	}
	@Override
	public List<AmzAdvProductTargeNegativaSD> amzCreateNegativeTargetingClauses(UserInfo user, String profileid,
			JSONArray keywords) {
		// TODO Auto-generated method stub
		return this.amzCreateNegativeTargetingClauses(user, new BigInteger(profileid), this.convertJsonToBean(user, profileid, keywords));
	}
	
}
