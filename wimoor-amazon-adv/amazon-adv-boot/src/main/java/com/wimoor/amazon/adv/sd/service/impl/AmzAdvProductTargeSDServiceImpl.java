package com.wimoor.amazon.adv.sd.service.impl;

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
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sd.dao.AmzAdvProductTargeSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvProductTargeSDService")
public class AmzAdvProductTargeSDServiceImpl  extends BaseService<AmzAdvProductTargeSD> implements IAmzAdvProductTargeSDService{
	@Resource
	AmzAdvProductTargeSDMapper amzAdvProductTargeSDMapper;
	@Resource
	ApiBuildService apiBuildService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	public PageList<Map<String, Object>> getProductTargeList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		return amzAdvProductTargeSDMapper.getProductTargeList(map,pageBounds);
	}

	public List<Map<String,Object>> getProductTargeChart(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return amzAdvProductTargeSDMapper.getProductTargeChart(map);
	}

	public Map<String, Object> getSumProductTarge(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return amzAdvProductTargeSDMapper.getSumProductTarge(map);
	}
	
	public List<AmzAdvProductTargeSD> amzListTargetingClausesEx(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/targets/extended?";
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
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		List<AmzAdvProductTargeSD> list = new LinkedList<AmzAdvProductTargeSD>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvProductTargeSD amzAdvProductTarge = new AmzAdvProductTargeSD();
				amzAdvProductTarge.setTargetid(item.getBigInteger("targetId"));
				amzAdvProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvProductTarge.setState(item.getString("state"));
				amzAdvProductTarge.setExpression(item.getJSONArray("expression").toJSONString());
				amzAdvProductTarge.setExpressiontype(item.getString("expressionType"));
				amzAdvProductTarge.setBid(item.getBigDecimal("bid"));
				amzAdvProductTarge.setProfileid(profileId);
				amzAdvProductTarge.setOpttime(new Date());
				AmzAdvProductTargeSD oldamzAdvProductads = amzAdvProductTargeSDMapper.selectByPrimaryKey(amzAdvProductTarge);
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
	
	public List<AmzAdvProductTargeSD> amzCreateTargetingClauses(UserInfo user,BigInteger  profileId, List<AmzAdvProductTargeSD> productTargeList) {
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/targets";
		JSONArray array = new JSONArray();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvProductTargeSD amzAdvProductTarge = productTargeList.get(i);
			JSONObject param = new JSONObject();
			param.put("state", amzAdvProductTarge.getState());
			param.put("adGroupId", amzAdvProductTarge.getAdgroupid());
			param.put("expressionType", amzAdvProductTarge.getExpressiontype());
			param.put("expression", GeneralUtil.getJsonArray(amzAdvProductTarge.getExpression()));
			if(amzAdvProductTarge.getBid() != null) {
				param.put("bid", amzAdvProductTarge.getBid().floatValue());
			}
			array.add(param);
		}
 
		String response = apiBuildService.amzAdvPost(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray results  = GeneralUtil.getJsonArray(response.toString());
			BaseException exception =null;
			    if(results!=null&&results.size()>0) {
				for(int i = 0; i < results.size(); i++) {
					JSONObject item = results.getJSONObject(i);
					if ("SUCCESS".equals(item.getString("code"))) {
					AmzAdvProductTargeSD amzAdvProductTarge = productTargeList.get(i);
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTarge.setTargetid(targetId);
					amzAdvProductTarge.setProfileid(profileId);
					amzAdvProductTarge.setOpttime(new Date());
					AmzAdvProductTargeSD DbamzAdvProductTarge = this.selectByKey(amzAdvProductTarge);
					if(DbamzAdvProductTarge == null) {
						this.save(amzAdvProductTarge);
					}
					}else{
						errormsg = errormsg.equals("") ? "" : errormsg + ",";
						if(item.getString("description")!=null&&!"null".equals(item.getString("description"))) {
							errormsg = errormsg + item.getString("description");
						}else {
							errormsg = errormsg + item.getString("code");	
						}
						
					}
				}
				if(StrUtil.isNotEmpty(errormsg)) {
					exception = new BaseException("商品投放创建失败："+errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
	
			Map<BigInteger, List<Object>> map = new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTargeSD productTarge = productTargeList.get(i);
				BigInteger key = productTarge.getAdgroupid();
				List<Object> targeList = map.get(key);
				if(targeList==null) {
					targeList=new ArrayList<Object>();
				    map.put(key, targeList);
				}
				targeList.add(productTarge);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTargeSD", user.getId(), profileId, map, null);
		}else {
			throw new BaseException("创建失败！");
		}
			return productTargeList;
	}

	public List<AmzAdvProductTargeSD> amzUpdateTargetingClauses(UserInfo user,BigInteger  profileId, List<AmzAdvProductTargeSD> productTargeList) {
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/targets";
		JSONArray array = new JSONArray();
		List<AmzAdvProductTargeSD> oldProductTargeList = new ArrayList<AmzAdvProductTargeSD>();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvProductTargeSD amzAdvProductTarge = productTargeList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("targetId", amzAdvProductTarge.getTargetid());
			param.put("adGroupId", amzAdvProductTarge.getAdgroupid());
			param.put("state", amzAdvProductTarge.getState());
			if(amzAdvProductTarge.getBid()!=null) {
				param.put("bid", amzAdvProductTarge.getBid().floatValue());
			}
			array.add(param);
			
			Example example = new Example(AmzAdvProductTarge.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("targetid", amzAdvProductTarge.getTargetid());
			AmzAdvProductTargeSD productTarge = amzAdvProductTargeSDMapper.selectOneByExample(example);
			oldProductTargeList.add(productTarge);
		}
		String response = apiBuildService.amzAdvPut(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray successResults = null;
			JSONObject erroritem = null;
			if(response.contains("[")) {
				  successResults = GeneralUtil.getJsonArray(response.toString());
			}else {
				erroritem=GeneralUtil.getJsonObject(response.toString());
			}
			if(erroritem != null && erroritem.size() > 0) {
				JSONObject item = erroritem;
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + item.getString("details");
				BaseException exception = new BaseException("商品投放创建失败："+errormsg);
				exception.setCode("ERROER");
				throw exception;
			}
			if(successResults != null && successResults.size() > 0) {
				for(int i = 0; i < successResults.size(); i++) {
					JSONObject item = successResults.getJSONObject(i);
					AmzAdvProductTargeSD amzAdvProductTarge = productTargeList.get(i);
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTarge.setTargetid(targetId);
					amzAdvProductTarge.setProfileid(profileId);
					amzAdvProductTarge.setOpttime(new Date());
					AmzAdvProductTargeSD DbamzAdvProductTarge = this.selectByKey(amzAdvProductTarge);
					if(DbamzAdvProductTarge == null) {
						this.save(amzAdvProductTarge);
					}else {
						this.updateAll(amzAdvProductTarge);
					}
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			Map<BigInteger, List<Object>> oldmap=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTargeSD productTarge = productTargeList.get(i);
				BigInteger key =  productTarge.getAdgroupid();
				List<Object> targeList = map.get(key);
				if(targeList==null) {
					targeList=new ArrayList<Object>();
				    map.put(key, targeList);
				}
				targeList.add(productTarge);
			}
			for (int i = 0; i < oldProductTargeList.size(); i++) {
				AmzAdvProductTargeSD oldproductTarge = oldProductTargeList.get(i);
				BigInteger key =  oldproductTarge.getAdgroupid();
				List<Object> targeList = oldmap.get(key);
				if(targeList==null) {
					targeList=new ArrayList<Object>();
					oldmap.put(key, targeList);
				}
				targeList.add(oldproductTarge);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTargeSD", user.getId(), profileId, map, oldmap);
			return productTargeList;
		}
		return null;
	}

	public AmzAdvProductTargeSD amzArchiveTargetingClause(UserInfo user,BigInteger  profileId, String targetId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvProductTargeSD productTarge = null;
		AmzAdvProductTargeSD oldproductTarge = null;
		String url = "/sd/targets/" + targetId;
		String response = apiBuildService.amzAdvDelete(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example = new Example(AmzAdvProductTarge.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("targetid", targetId);
				productTarge = amzAdvProductTargeSDMapper.selectOneByExample(example);
				oldproductTarge = amzAdvProductTargeSDMapper.selectOneByExample(example);
				productTarge.setState(AdvState.archived);
				productTarge.setOpttime(new Date());
				this.updateAll(productTarge);
				amzAdvOperateLogService.saveOperateLog("AmzAdvProductTargeSD", user.getId(), profileId, productTarge, oldproductTarge);
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

	 public List<AmzAdvProductTargeSD> convertJsonToBean(UserInfo user, String profileId, JSONArray keywordArray){
	    	List<AmzAdvProductTargeSD> targetList = new ArrayList<AmzAdvProductTargeSD>();
			for (int i = 0; i < keywordArray.size(); i++) {
				JSONObject target = keywordArray.getJSONObject(i);
				//BigInteger campaignId= target.getBigInteger("campaignid");
				BigInteger adGroupId = target.getBigInteger("adgroupid");
				BigInteger targetid= target.getBigInteger("targetid");
				String state= target.getString("state");
				BigDecimal bid= target.getBigDecimal("bid");
				String expressiontype=target.getString("expressiontype");
				String expression=target.getString("expression");
				AmzAdvProductTargeSD targetobj = new AmzAdvProductTargeSD();
				targetobj.setState(state.toLowerCase());
				targetobj.setBid(bid);
				targetobj.setTargetid(targetid);
				targetobj.setExpressiontype(expressiontype);
				targetobj.setExpression(expression);
				targetobj.setAdgroupid(adGroupId);
				targetList.add(targetobj);
			}
			return targetList;
	    }
	 
	@Override
	public List<AmzAdvProductTargeSD> amzUpdateTargets(UserInfo user, String profileid, JSONArray keywords) {
		// TODO Auto-generated method stub
		return this.amzUpdateTargetingClauses(user, new BigInteger(profileid),this.convertJsonToBean(user, profileid, keywords));
	}

	@Override
	public List<AmzAdvProductTargeSD> amzCreateTargets(UserInfo user, String profileid, JSONArray keywords) {
		// TODO Auto-generated method stub
		return this.amzCreateTargetingClauses(user, new BigInteger(profileid), this.convertJsonToBean(user, profileid, keywords));
	}

	@Override
	public AmzAdvProductTargeSD amzGetTargetsExt(UserInfo user, BigInteger profileid, String targetId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileid);
		String url = "/sd/targets/extended/"+targetId;
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
				AmzAdvProductTargeSD amzAdvProductTarge = new AmzAdvProductTargeSD();
				amzAdvProductTarge.setTargetid(item.getBigInteger("targetId"));
				amzAdvProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvProductTarge.setState(item.getString("state"));
				amzAdvProductTarge.setExpression(item.getJSONArray("expression").toJSONString());
				amzAdvProductTarge.setExpressiontype(item.getString("expressionType"));
				amzAdvProductTarge.setBid(item.getBigDecimal("bid"));
				amzAdvProductTarge.setProfileid(profileid);
				amzAdvProductTarge.setOpttime(new Date());
				String servingStatus=item.getString("servingStatus");
				amzAdvProductTarge.setServingStatus(servingStatus);
				Date creationDateTime=item.getDate("creationDateTime");
				amzAdvProductTarge.setCreationDate(creationDateTime);
				Date lastUpdateDateTime=item.getDate("lastUpdateDateTime");
				amzAdvProductTarge.setLastUpdatedDate(lastUpdateDateTime);
				AmzAdvProductTargeSD oldamzAdvProductads = amzAdvProductTargeSDMapper.selectByPrimaryKey(amzAdvProductTarge);
				if (oldamzAdvProductads != null) {
					this.updateAll(amzAdvProductTarge);
				} else {
					this.save(amzAdvProductTarge);
				}
				return amzAdvProductTarge;
		 
		}
		return null;
	}

}
