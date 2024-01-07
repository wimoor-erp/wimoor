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
import com.wimoor.amazon.adv.common.pojo.AmzAdvOperateLog;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductTargeNegativaSDService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvCampProductTargeNegativaMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampProductTargeNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampProductTargeNegativaService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvCampProductTargeNegativaService")
public class AmzAdvCampProductTargeNegativaServiceImpl extends BaseService<AmzAdvCampProductTargeNegativa> implements IAmzAdvCampProductTargeNegativaService{
	@Resource
	ApiBuildService apiBuildService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvCampProductTargeNegativaMapper amzAdvCampProductTargeNegativaMapper;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
    @Resource
    IAmzAdvProductTargeNegativaSDService amzAdvProductTargeNegativaSDService;
	  

	public List<AmzAdvCampProductTargeNegativa> amzCreateNegativeTargetingClauses(UserInfo user,BigInteger  profileId, List<AmzAdvCampProductTargeNegativa> productTargeList) {
		// TODO Auto-generated method stub
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaignNegativeTargets";
		JSONArray array = new JSONArray();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvCampProductTargeNegativa amzAdvProductTargeNegativa = productTargeList.get(i);
			JSONObject param = new JSONObject();
			param.put("campaignId", amzAdvProductTargeNegativa.getCampaignid().toString());
			param.put("state", amzAdvProductTargeNegativa.getState());
			param.put("expression", GeneralUtil.getJsonArray(amzAdvProductTargeNegativa.getExpression()));
			//param.put("expressionType", amzAdvProductTargeNegativa.getExpressiontype());
			array.add(param);
		}
		JSONObject negativeTargetingClauses=new JSONObject();
		negativeTargetingClauses.put("campaignNegativeTargetingClauses", array);
		String response = apiBuildService.amzAdvPost(profile, url, negativeTargetingClauses.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			System.out.println(response);
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject campaignNegativeTargetingClausesJson = json.getJSONObject("campaignNegativeTargetingClauses");
			JSONArray success = campaignNegativeTargetingClausesJson.getJSONArray("success");
			JSONArray error = campaignNegativeTargetingClausesJson.getJSONArray("error");
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				AmzAdvCampProductTargeNegativa amzAdvProductTargeNegativa = productTargeList.get(index);
				BigInteger targetId = item.getBigInteger("campaignNegativeTargetingClauseId");
				amzAdvProductTargeNegativa.setTargetid(targetId);
				amzAdvProductTargeNegativa.setProfileid(profileId);
				amzAdvProductTargeNegativa.setOpttime(new Date());
				Example example = new Example(AmzAdvProductTargeNegativa.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("targetid", targetId);
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("adgroupid", targetId);
				crit.andEqualTo("campaignid", targetId);
				AmzAdvCampProductTargeNegativa dbAdvProductTargeNegativa = amzAdvCampProductTargeNegativaMapper.selectOneByExample(example);
				if(dbAdvProductTargeNegativa == null) {
					this.save(amzAdvProductTargeNegativa);
				}
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				operateLog.setCampaignid(amzAdvProductTargeNegativa.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvCampProductTargeNegativa");
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
	    	header.put("Content-Type", "application/vnd.spCampaignNegativeTargetingClause.v3+json");
	    	header.put("Accept",       "application/vnd.spCampaignNegativeTargetingClause.v3+json");
	    	return header;
	    }
	  
	public List<AmzAdvCampProductTargeNegativa> amzUpdateNegativeTargetingClauses(UserInfo user,BigInteger  profileId, List<AmzAdvCampProductTargeNegativa> productTargeNegativaList) {
		// TODO Auto-generated method stub
		if (productTargeNegativaList == null || productTargeNegativaList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaignNegativeTargets";
		JSONArray array = new JSONArray();
		List<AmzAdvCampProductTargeNegativa> oldProductTargeNegativaList = new ArrayList<AmzAdvCampProductTargeNegativa>();
		for (int i = 0; i < productTargeNegativaList.size(); i++) {
			AmzAdvCampProductTargeNegativa amzAdvProductTargeNegativa = productTargeNegativaList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("targetId", amzAdvProductTargeNegativa.getTargetid().toString());
			param.put("campaignId", amzAdvProductTargeNegativa.getCampaignid().toString());
			param.put("state", amzAdvProductTargeNegativa.getState());
			param.put("expression", GeneralUtil.getJsonArray(amzAdvProductTargeNegativa.getExpression()));
			param.put("expressionType", amzAdvProductTargeNegativa.getExpressiontype());
			array.add(param);
			
			Example example = new Example(AmzAdvProductTargeNegativa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("targetid", amzAdvProductTargeNegativa.getTargetid());
			AmzAdvCampProductTargeNegativa productTargeNegativa = amzAdvCampProductTargeNegativaMapper.selectOneByExample(example);
			oldProductTargeNegativaList.add(productTargeNegativa);
		}
		JSONObject campaignNegativeTargetingClauses=new JSONObject();
		campaignNegativeTargetingClauses.put("campaignNegativeTargetingClauses", array);
		String response = apiBuildService.amzAdvPut(profile, url, campaignNegativeTargetingClauses.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject negativeKeywordsJson = json.getJSONObject("campaignNegativeTargetingClauses");
			JSONArray success = negativeKeywordsJson.getJSONArray("success");
			JSONArray error = negativeKeywordsJson.getJSONArray("error");
			List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				AmzAdvCampProductTargeNegativa amzAdvProductTargeNegativa = productTargeNegativaList.get(index);
				BigInteger targetId = item.getBigInteger("campaignNegativeTargetingClauseId");
				amzAdvProductTargeNegativa.setTargetid(targetId);
				amzAdvProductTargeNegativa.setProfileid(profileId);
				amzAdvProductTargeNegativa.setOpttime(new Date());
				this.updateNotNull(amzAdvProductTargeNegativa);
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvCampProductTargeNegativa oldproductTargeNegativa =oldProductTargeNegativaList.get(index);
				operateLog.setAdgroupid(new BigInteger("0"));
				operateLog.setCampaignid(amzAdvProductTargeNegativa.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvCampProductTargeNegativa");
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

	public AmzAdvCampProductTargeNegativa amzArchiveNegativeTargetingClause(UserInfo user,BigInteger  profileId, String targetId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvCampProductTargeNegativa productTarge = null;
		AmzAdvCampProductTargeNegativa oldproductTarge = null;
		String url = "/sp/negativeTargets/" + targetId;
		String response = apiBuildService.amzAdvDelete_V2(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example = new Example(AmzAdvProductTargeNegativa.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("targetid", targetId);
				productTarge = amzAdvCampProductTargeNegativaMapper.selectOneByExample(example);
				oldproductTarge = amzAdvCampProductTargeNegativaMapper.selectOneByExample(example);
				productTarge.setState(AdvState.archived);
				productTarge.setOpttime(new Date());
				amzAdvCampProductTargeNegativaMapper.updateByPrimaryKey(productTarge);
				amzAdvOperateLogService.saveOperateLog("AmzAdvCampProductTargeNegativa", user.getId(), profileId, productTarge, oldproductTarge);
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

	public List<AmzAdvCampProductTargeNegativa> amzListNegativeTargetingClauses(UserInfo user,BigInteger  profileId, JSONObject param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/campaignNegativeTargets/list";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		List<AmzAdvCampProductTargeNegativa> list = new LinkedList<AmzAdvCampProductTargeNegativa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject negativeKeywords = GeneralUtil.getJsonObject(response);
			String nextToken=negativeKeywords.getString("nextToken");
			param.put("nextToken", nextToken);
			JSONArray a = negativeKeywords.getJSONArray("campaignNegativeTargetingClauses");
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvCampProductTargeNegativa amzAdvProductTargeNegativa = new AmzAdvCampProductTargeNegativa();
				amzAdvProductTargeNegativa.setTargetid(item.getBigInteger("targetId"));
				amzAdvProductTargeNegativa.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvProductTargeNegativa.setState(item.getString("state"));
				amzAdvProductTargeNegativa.setExpression(item.getJSONArray("expression").toJSONString());
				amzAdvProductTargeNegativa.setExpressiontype(item.getString("resolvedExpression"));
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
				AmzAdvCampProductTargeNegativa oldamzAdvProductads = amzAdvCampProductTargeNegativaMapper.selectByPrimaryKey(amzAdvProductTargeNegativa);
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
 
	 
	
	public AmzAdvCampProductTargeNegativa getNegativaTargetforQuery(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String profileid = map.get("profileid") == null ? null : map.get("profileid").toString();
		String query = map.get("query") == null ? null : map.get("query").toString();
		String adGroupid = map.get("adGroupid") == null ? null : map.get("adGroupid").toString();
		String campaignid = map.get("campaignid") == null ? null : map.get("campaignid").toString();
		Example example = new Example(AmzAdvCampProductTargeNegativa.class);
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
		List<AmzAdvCampProductTargeNegativa> list = this.selectByExample(example);
		if(list != null && list.size() > 0) {
			map.put("TargetNegativaFlag", true);
			return list.get(0);
		}
		return null;
	}
	
 

	 public List<AmzAdvCampProductTargeNegativa> convertJsonToBean(UserInfo user, BigInteger profileId, JSONArray nkeywordArray){
			List<AmzAdvCampProductTargeNegativa> list = new ArrayList<AmzAdvCampProductTargeNegativa>();
		for (int i = 0; i < nkeywordArray.size(); i++) {
			JSONObject keyword = nkeywordArray.getJSONObject(i);
			String expressiontype = keyword.getString("expressiontype");
			String expression = keyword.getString("expression");
			String state = keyword.getString("state");
			BigInteger campaignid = keyword.getBigInteger("campaignid");
			BigInteger targetid = keyword.getBigInteger("targetid");
			AmzAdvCampProductTargeNegativa amzAdvKeywordsNegativa = new AmzAdvCampProductTargeNegativa();
			amzAdvKeywordsNegativa.setTargetid(targetid);
			amzAdvKeywordsNegativa.setCampaignid(campaignid);
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
	public List<AmzAdvCampProductTargeNegativa> amzCreateNegativeTargetingClauses(UserInfo user, String profileid,
			JSONArray nkeywords) {
		// TODO Auto-generated method stub
		return this.amzCreateNegativeTargetingClauses(user, new BigInteger(profileid), this.convertJsonToBean(user,new BigInteger(profileid), nkeywords));
	}

	@Override
	public List<AmzAdvCampProductTargeNegativa> amzUpdateNegativeTargetingClauses(UserInfo user, String profileid,
			JSONArray nkeywords) {
		// TODO Auto-generated method stub
		return this.amzUpdateNegativeTargetingClauses(user, new BigInteger(profileid), this.convertJsonToBean(user,new BigInteger(profileid), nkeywords));
	}

	@Override
	public PageList<Map<String, Object>> getProductNegativaTargeList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		return amzAdvCampProductTargeNegativaMapper.getProductNegativaTargeList(map, pageBounds);
	}

}
