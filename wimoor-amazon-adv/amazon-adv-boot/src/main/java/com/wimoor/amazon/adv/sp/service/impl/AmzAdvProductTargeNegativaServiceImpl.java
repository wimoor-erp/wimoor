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
import com.wimoor.amazon.adv.common.pojo.AmzAdvOperateLog;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeNegativaSDService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductTargeNegativaMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductTargeNegativaService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
import cn.hutool.core.util.StrUtil;
import com.wimoor.common.GeneralUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvProductTargeNegativaService")
public class AmzAdvProductTargeNegativaServiceImpl extends BaseService<AmzAdvProductTargeNegativa> implements IAmzAdvProductTargeNegativaService{
	@Resource
	ApiBuildService apiBuildService;
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
		String response = apiBuildService.amzAdvGet_V2(profile, url);
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
		String response = apiBuildService.amzAdvGet_V2(profile, url);
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
			param.put("campaignId", amzAdvProductTargeNegativa.getCampaignid().toString());
			param.put("adGroupId", amzAdvProductTargeNegativa.getAdgroupid().toString());
			param.put("state", amzAdvProductTargeNegativa.getState());
			param.put("expression", GeneralUtil.getJsonArray(amzAdvProductTargeNegativa.getExpression()));
			param.put("expressionType", amzAdvProductTargeNegativa.getExpressiontype());
			array.add(param);
		}
		JSONObject negativeTargetingClauses=new JSONObject();
		negativeTargetingClauses.put("negativeTargetingClauses", array);
		String response = apiBuildService.amzAdvPost(profile, url, negativeTargetingClauses.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject campaignNegativeTargetingClausesJson = json.getJSONObject("negativeTargetingClauses");
			JSONArray success = campaignNegativeTargetingClausesJson.getJSONArray("success");
			JSONArray error = campaignNegativeTargetingClausesJson.getJSONArray("error");
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = productTargeList.get(index);
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
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				operateLog.setCampaignid(amzAdvProductTargeNegativa.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvProductTargeNegativa");
				String  nkeyjson = GeneralUtil.toJSON(amzAdvProductTargeNegativa);
				operateLog.setAfterobject(nkeyjson);
				operateLog.setBeforeobject(null);
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

	  public Map<String,String> getHeaderExt(){
	    	Map<String,String> header=new HashMap<String,String>();
	    	header.put("Content-Type", "application/vnd.spNegativeTargetingClause.v3+json");
	    	header.put("Accept",       "application/vnd.spNegativeTargetingClause.v3+json");
	    	return header;
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
			param.put("targetId", amzAdvProductTargeNegativa.getTargetid().toString());
			param.put("campaignId", amzAdvProductTargeNegativa.getCampaignid().toString());
			param.put("adGroupId", amzAdvProductTargeNegativa.getAdgroupid().toString());
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
		JSONObject campaignNegativeTargetingClauses=new JSONObject();
		campaignNegativeTargetingClauses.put("negativeTargetingClauses", array);
		String response = apiBuildService.amzAdvPut(profile, url, campaignNegativeTargetingClauses.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject negativeKeywordsJson = json.getJSONObject("negativeTargetingClauses");
			JSONArray success = negativeKeywordsJson.getJSONArray("success");
			JSONArray error = negativeKeywordsJson.getJSONArray("error");
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				 AmzAdvProductTargeNegativa amzAdvProductTargeNegativa = productTargeNegativaList.get(index);
				BigInteger targetId = item.getBigInteger("targetId");
				amzAdvProductTargeNegativa.setTargetid(targetId);
				amzAdvProductTargeNegativa.setProfileid(profileId);
				amzAdvProductTargeNegativa.setOpttime(new Date());
				this.updateNotNull(amzAdvProductTargeNegativa);
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvProductTargeNegativa oldproductTargeNegativa =oldProductTargeNegativaList.get(index);
				operateLog.setAdgroupid(new BigInteger("0"));
				operateLog.setCampaignid(amzAdvProductTargeNegativa.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvProductTargeNegativa");
				String adgroupjson = GeneralUtil.toJSON(amzAdvProductTargeNegativa);
				String oldadgroupjson = GeneralUtil.toJSON(oldproductTargeNegativa);
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
			return productTargeNegativaList;
	}
		
		return null;
	}

	public List<AmzAdvProductTargeNegativa> amzArchiveNegativeTargetingClause(UserInfo user,BigInteger  profileId, JSONObject param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/targets/delete";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		List<AmzAdvProductTargeNegativa> list = new LinkedList<AmzAdvProductTargeNegativa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject negativeKeywords = GeneralUtil.getJsonObject(response);
			JSONObject result = negativeKeywords.getJSONObject("negativeTargetingClauses");
            JSONArray a =result.getJSONArray("success");
			for (int i = 0; i < a.size(); i++) {
				JSONObject one = a.getJSONObject(i);
				JSONObject item=one.getJSONObject("negativeTargetingClause");
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

	public List<AmzAdvProductTargeNegativa> amzListNegativeTargetingClauses(UserInfo user,BigInteger  profileId, JSONObject param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/negativeTargets/list";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		List<AmzAdvProductTargeNegativa> list = new LinkedList<AmzAdvProductTargeNegativa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject negativeKeywords = GeneralUtil.getJsonObject(response);
			JSONArray a = negativeKeywords.getJSONArray("negativeTargetingClauses");
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
				JSONObject extendedData = item.getJSONObject("extendedData");
				if(extendedData!=null) {
					String servingStatus=extendedData.getString("servingStatus");
					amzAdvProductTargeNegativa.setServingStatus(servingStatus);
					Date creationDateTime=extendedData.getDate("creationDateTime");
					amzAdvProductTargeNegativa.setCreationDate(creationDateTime);
					Date lastUpdateDateTime=extendedData.getDate("lastUpdateDateTime");
					amzAdvProductTargeNegativa.setLastUpdatedDate(lastUpdateDateTime);
				}
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
				return amzAdvProductTargeNegativaMapper.getProductNegativaTargeList(map,pageBounds);
	}

	 public List<AmzAdvProductTargeNegativa> convertJsonToBean(UserInfo user, BigInteger profileId, JSONArray nkeywordArray){
			List<AmzAdvProductTargeNegativa> list = new ArrayList<AmzAdvProductTargeNegativa>();
		for (int i = 0; i < nkeywordArray.size(); i++) {
			JSONObject keyword = nkeywordArray.getJSONObject(i);
			String expressiontype = keyword.getString("expressiontype");
			String expression = keyword.getString("expression");
			String state = keyword.getString("state");
			BigInteger campaignid = keyword.getBigInteger("campaignid");
			BigInteger targetid = keyword.getBigInteger("targetid");
			BigInteger adGroupid = keyword.getBigInteger("adgroupid");
			AmzAdvProductTargeNegativa amzAdvKeywordsNegativa = new AmzAdvProductTargeNegativa();
			amzAdvKeywordsNegativa.setTargetid(targetid);
			amzAdvKeywordsNegativa.setCampaignid(campaignid);
			amzAdvKeywordsNegativa.setAdgroupid(adGroupid);
			amzAdvKeywordsNegativa.setProfileid(profileId);
			amzAdvKeywordsNegativa.setExpressiontype(expressiontype);
			amzAdvKeywordsNegativa.setExpression(expression);
			amzAdvKeywordsNegativa.setState(state);
			amzAdvKeywordsNegativa.setOpttime(new Date());
		    list.add(amzAdvKeywordsNegativa);
		}
		return list;
	}
	 
	@Override
	public List<AmzAdvProductTargeNegativa> amzCreateNegativeTargetingClauses(UserInfo user, String profileid,
			JSONArray nkeywords) {
		// TODO Auto-generated method stub
		return this.amzCreateNegativeTargetingClauses(user, new BigInteger(profileid), this.convertJsonToBean(user,new BigInteger(profileid), nkeywords));
	}

	@Override
	public List<AmzAdvProductTargeNegativa> amzUpdateNegativeTargetingClauses(UserInfo user, String profileid,
			JSONArray nkeywords) {
		// TODO Auto-generated method stub
		return this.amzUpdateNegativeTargetingClauses(user, new BigInteger(profileid), this.convertJsonToBean(user,new BigInteger(profileid), nkeywords));
	}

}
