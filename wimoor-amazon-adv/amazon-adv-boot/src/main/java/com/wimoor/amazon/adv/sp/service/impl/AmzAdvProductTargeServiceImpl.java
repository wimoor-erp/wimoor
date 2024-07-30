package com.wimoor.amazon.adv.sp.service.impl;

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
import com.wimoor.amazon.adv.common.pojo.AmzAdvOperateLog;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvProductTargeHsaService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeSDService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductTargeMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;

import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvProductTargeService")
public class AmzAdvProductTargeServiceImpl extends BaseService<AmzAdvProductTarge> implements IAmzAdvProductTargeService{
	@Resource
	ApiBuildService apiBuildService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvProductTargeMapper amzAdvProductTargeMapper;
	@Resource
	IAmzAdvProductTargeSDService amzAdvProductTargeSDService;
	@Resource
	IAmzAdvProductTargeHsaService amzAdvProductTargeHsaService;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	 

	public List<AmzAdvProductTarge> amzCreateTargetingClauses(UserInfo user,BigInteger  profileId, List<AmzAdvProductTarge> productTargeList) {
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets";
		JSONArray array = new JSONArray();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvProductTarge amzAdvProductTarge = productTargeList.get(i);
			JSONObject param = new JSONObject();
			param.put("campaignId", amzAdvProductTarge.getCampaignid().toString());
			param.put("adGroupId", amzAdvProductTarge.getAdgroupid().toString());
			param.put("state", amzAdvProductTarge.getState());
			param.put("expression", GeneralUtil.getJsonArray(amzAdvProductTarge.getExpression()));
			param.put("expressionType", amzAdvProductTarge.getExpressiontype());
			if(amzAdvProductTarge.getBid() != null) {
				param.put("bid", amzAdvProductTarge.getBid());
			}
			array.add(param);
		}
		JSONObject targetingClauses = new JSONObject();
		targetingClauses.put("targetingClauses", array);
		String response = apiBuildService.amzAdvPost(profile, url, targetingClauses.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) { 
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject targetingClausesjsoin = json.getJSONObject("targetingClauses");
			JSONArray success = targetingClausesjsoin.getJSONArray("success");
			JSONArray error = targetingClausesjsoin.getJSONArray("error");
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				AmzAdvProductTarge amzAdvTarget = productTargeList.get(index);
				BigInteger targetId = item.getBigInteger("targetId");
				amzAdvTarget.setTargetid(targetId);
				amzAdvTarget.setProfileid(profile.getId());
				amzAdvTarget.setOpttime(new Date());
				this.amzAdvProductTargeMapper.insert(amzAdvTarget);
				
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				operateLog.setCampaignid(amzAdvTarget.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvProductTarge");
				String amzAdvProductadsjson = GeneralUtil.toJSON(amzAdvTarget);
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
			param.put("targetId", amzAdvProductTarge.getTargetid().toString());
			param.put("campaignId", amzAdvProductTarge.getCampaignid().toString());
			param.put("adGroupId", amzAdvProductTarge.getAdgroupid().toString());
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
		JSONObject targetingClauses = new JSONObject();
		targetingClauses.put("targetingClauses", array);
		String response = apiBuildService.amzAdvPut(profile, url, targetingClauses.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject keywordsJson = json.getJSONObject("targetingClauses");
			JSONArray success = keywordsJson.getJSONArray("success");
			JSONArray error = keywordsJson.getJSONArray("error");
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				AmzAdvProductTarge target = productTargeList.get(index);
				BigInteger targetId = item.getBigInteger("targetId");
				target.setTargetid(targetId);
				target.setProfileid(profile.getId());
				target.setOpttime(new Date());
				this.updateNotNull(target);
				
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvProductTarge oldtargets = oldProductTargeList.get(index);
				operateLog.setCampaignid(oldtargets.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvProductTarge");
				String amzAdvProductadsjson = GeneralUtil.toJSON(target);
				String oldproductAdsjson = GeneralUtil.toJSON(oldtargets);
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
			return productTargeList;
		}
		return null;
	}

	public PageList<Map<String, Object>> getProductTargeList(Map<String,Object> map, PageBounds pageBounds){
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString().toUpperCase();
			if("HSA".equals(campaignType)||"SB".equals(campaignType)) {
				return amzAdvProductTargeHsaService.getProductTargeList(map,pageBounds);
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
		List<Map<String,Object>> listSB = null;
		getSerchStr(map);
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("HSA".equals(campaignType)||"SB".equals(campaignType)) {
				listSB=amzAdvProductTargeHsaService.getProductTargeChart(map);
			}else if("SP".equals(campaignType.toUpperCase())) {
				listSP = amzAdvProductTargeMapper.getProductTargeChart(map);
			}else if("SD".equals(campaignType.toUpperCase())) {
				listSD =amzAdvProductTargeSDService.getProductTargeChart(map);
			}
		}else {
			listSP = amzAdvProductTargeMapper.getProductTargeChart(map);
		}
	
		return amzAdvCampaignService.getChartData(listSP, listSB, listSD, map);
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
		}else if("sb".equals(campaignType.toLowerCase())||"hsa".equals(campaignType.toLowerCase())){
			Map<String, Object> param = amzAdvProductTargeHsaService.getSumProductTarge(map);
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
	
	@Override
	public List<AmzAdvProductTarge> amzDeleteTargets(UserInfo user, BigInteger profileId, JSONObject param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets/delete";
		String response = apiBuildService.amzAdvPost(profile, url,param.toJSONString(),this.getHeaderExt());
		List<AmzAdvProductTarge> list = new LinkedList<AmzAdvProductTarge>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject targetingClauses = GeneralUtil.getJsonObject(response.toString());
			JSONArray a = targetingClauses.getJSONArray("targetingClauses");
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

	@Override
	public List<AmzAdvProductTarge> amzGetTargets(UserInfo user, BigInteger profileId, JSONObject param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets/list";
		String response = apiBuildService.amzAdvPost(profile, url,param.toJSONString(),this.getHeaderExt());
		List<AmzAdvProductTarge> list = new LinkedList<AmzAdvProductTarge>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject targetingClauses = GeneralUtil.getJsonObject(response.toString());
			JSONArray a = targetingClauses.getJSONArray("targetingClauses");
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
				JSONObject extendedData = item.getJSONObject("extendedData");
				if(extendedData!=null) {
					String servingStatus=extendedData.getString("servingStatus");
					amzAdvProductTarge.setServingStatus(servingStatus);
					Date creationDateTime=extendedData.getDate("creationDateTime");
					amzAdvProductTarge.setCreationDate(creationDateTime);
					Date lastUpdateDateTime=extendedData.getDate("lastUpdateDateTime");
					amzAdvProductTarge.setLastUpdatedDate(lastUpdateDateTime);
				}
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

	private Map<String, String> getHeaderExt() {
		// TODO Auto-generated method stub
	  	Map<String,String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/vnd.spTargetingClause.v3+json");
    	header.put("Accept",       "application/vnd.spTargetingClause.v3+json");
    	return header;
	}
    public List<AmzAdvProductTarge> convertJsonToBean(UserInfo user, String profileId, JSONArray keywordArray){
    	List<AmzAdvProductTarge> targetList = new ArrayList<AmzAdvProductTarge>();
		for (int i = 0; i < keywordArray.size(); i++) {
			JSONObject target = keywordArray.getJSONObject(i);
			BigInteger campaignId= target.getBigInteger("campaignid");
			BigInteger adGroupId = target.getBigInteger("adgroupid");
			BigInteger targetid= target.getBigInteger("targetid");
			String state= target.getString("state");
			BigDecimal bid= target.getBigDecimal("bid");
			String expressiontype=target.getString("expressiontype");
			String expression=target.getString("expression");
			AmzAdvProductTarge targetobj = new AmzAdvProductTarge();
			targetobj.setState(state);
			targetobj.setBid(bid);
			targetobj.setTargetid(targetid);
			targetobj.setExpressiontype(expressiontype);
			targetobj.setExpression(expression);
			targetobj.setAdgroupid(adGroupId);
			targetobj.setCampaignid(campaignId);
			targetList.add(targetobj);
		}
		return targetList;
    }
	@Override
	public List<AmzAdvProductTarge> amzUpdateTargets(UserInfo user, String profileid, JSONArray targets) {
		// TODO Auto-generated method stub
		return this.amzUpdateTargetingClauses(user, new BigInteger(profileid), convertJsonToBean(user,profileid,targets));
		
	}

	@Override
	public List<AmzAdvProductTarge> amzCreateTargets(UserInfo user, String profileid, JSONArray targets) {
		// TODO Auto-generated method stub
		return this.amzCreateTargetingClauses(user, new BigInteger(profileid), convertJsonToBean(user,profileid,targets));
	}

}
