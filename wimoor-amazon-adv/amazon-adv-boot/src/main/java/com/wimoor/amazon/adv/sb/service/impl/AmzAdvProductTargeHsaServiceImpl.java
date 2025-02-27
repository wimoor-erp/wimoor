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
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvProductTargeHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvProductTargeHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvProductTargeHsaService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvProductTargeHsaService")
public class AmzAdvProductTargeHsaServiceImpl extends BaseService<AmzAdvProductTargeHsa> implements IAmzAdvProductTargeHsaService{
	@Resource
	ApiBuildService apiBuildService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	@Resource
	AmzAdvProductTargeHsaMapper amzAdvProductTargeHsaMapper;

	public List<AmzAdvProductTargeHsa> amzCreateTargetingClauses(UserInfo user,BigInteger  profileId, List<AmzAdvProductTargeHsa> productTargeList) {
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/targets";
		JSONObject paramTarget = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvProductTargeHsa amzAdvProductTarge = productTargeList.get(i);
			JSONObject param = new JSONObject();
			param.put("campaignId", amzAdvProductTarge.getCampaignid());
			param.put("adGroupId", amzAdvProductTarge.getAdgroupid());
			param.put("expressions", GeneralUtil.getJsonArray(amzAdvProductTarge.getExpression()));
			if(amzAdvProductTarge.getBid() != null) {
				param.put("bid", amzAdvProductTarge.getBid());
			}
			array.add(param);
		}
		paramTarget.put("targets", array);
		String response = apiBuildService.amzAdvPost(profile, url, paramTarget.toString());
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
					AmzAdvProductTargeHsa amzAdvProductTarge = productTargeList.get(i);
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTarge.setTargetid(targetId);
					amzAdvProductTarge.setProfileid(profileId);
					amzAdvProductTarge.setOpttime(new Date());
					AmzAdvProductTargeHsa DbamzAdvProductTarge = this.selectByKey(amzAdvProductTarge);
					if(DbamzAdvProductTarge == null) {
						this.save(amzAdvProductTarge);
					}
				}
			}
			Map<BigInteger, List<Object>> map = new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTargeHsa productTarge = productTargeList.get(i);
				BigInteger key = productTarge.getCampaignid().subtract(productTarge.getAdgroupid());
				List<Object> targeList = map.get(key);
				if(targeList==null) {
					targeList=new ArrayList<Object>();
				    map.put(key, targeList);
				}
				targeList.add(productTarge);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTargeHsa", user.getId(), profileId, map, null);
			return productTargeList;
		}else {
			throw new BaseException("创建失败！");
		}
	}

	public List<AmzAdvProductTargeHsa> amzUpdateTargetingClauses(UserInfo user,BigInteger  profileId, List<AmzAdvProductTargeHsa> productTargeList) {
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/targets";
		JSONArray array = new JSONArray();
		List<AmzAdvProductTargeHsa> oldProductTargeList = new ArrayList<AmzAdvProductTargeHsa>();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvProductTargeHsa amzAdvProductTarge = productTargeList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("targetId", amzAdvProductTarge.getTargetid());
			param.put("campaignId", amzAdvProductTarge.getCampaignid());
			param.put("adGroupId", amzAdvProductTarge.getAdgroupid());
			param.put("state", amzAdvProductTarge.getState());
			param.put("bid", amzAdvProductTarge.getBid());
			array.add(param);
			
			Example example = new Example(AmzAdvProductTargeHsa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("targetid", amzAdvProductTarge.getTargetid());
			AmzAdvProductTargeHsa productTarge = amzAdvProductTargeHsaMapper.selectOneByExample(example);
			oldProductTargeList.add(productTarge);
		}
		String response = apiBuildService.amzAdvPut(profile, url, array.toString());
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
					AmzAdvProductTargeHsa amzAdvProductTarge = productTargeList.get(i);
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTarge.setTargetid(targetId);
					amzAdvProductTarge.setProfileid(profileId);
					amzAdvProductTarge.setOpttime(new Date());
					AmzAdvProductTargeHsa DbamzAdvProductTarge = this.selectByKey(amzAdvProductTarge);
					if(DbamzAdvProductTarge == null) {
						this.save(amzAdvProductTarge);
					}
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			Map<BigInteger, List<Object>> oldmap=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTargeHsa productTarge = productTargeList.get(i);
				BigInteger key = productTarge.getCampaignid().subtract(productTarge.getAdgroupid());
				List<Object> targeList = map.get(key);
				if(targeList==null) {
					targeList=new ArrayList<Object>();
				    map.put(key, targeList);
				}
				targeList.add(productTarge);
			}
			for (int i = 0; i < oldProductTargeList.size(); i++) {
				AmzAdvProductTargeHsa oldproductTarge = oldProductTargeList.get(i);
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

	
	@Override
	public  AmzAdvProductTargeHsa amzDeleteTargets(UserInfo user, BigInteger profileId, String targetId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvProductTargeHsa productTarge = null;
		AmzAdvProductTargeHsa oldproductTarge = null;
		String url = "/sb/targets/" +targetId;
		String response = apiBuildService.amzAdvDelete(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example = new Example(AmzAdvProductTarge.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("targetid", targetId);
				productTarge = amzAdvProductTargeHsaMapper.selectOneByExample(example);
				oldproductTarge = amzAdvProductTargeHsaMapper.selectOneByExample(example);
				productTarge.setState(AdvState.archived);
				productTarge.setOpttime(new Date());
				this.updateAll(productTarge);
				amzAdvOperateLogService.saveOperateLog("AmzAdvProductTarge", user.getId(), profileId, productTarge, oldproductTarge);
				return  productTarge;
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
	public List<AmzAdvProductTargeHsa> amzGetTargets(BigInteger profileId, JSONObject param) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		return amzGetTargets(profile,param);
	}
	
	@Override
	public List<AmzAdvProductTargeHsa> amzGetTargets(AmzAdvProfile profile, JSONObject param) {
		// TODO Auto-generated method stub
		String url = "/sb/targets/list";
		String response = apiBuildService.amzAdvPost(profile, url,param.toJSONString(),this.getHeaderExt());
		List<AmzAdvProductTargeHsa> list = new LinkedList<AmzAdvProductTargeHsa>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject targetingClauses = GeneralUtil.getJsonObject(response.toString());
			String nextToken = targetingClauses.getString("nextToken");
			param.put("nextToken", nextToken);
			JSONArray a = targetingClauses.getJSONArray("targets");
			if(a!=null) {
				for (int i = 0; i < a.size(); i++) {
					JSONObject item = a.getJSONObject(i);
					AmzAdvProductTargeHsa amzAdvProductTarge = new AmzAdvProductTargeHsa();
					amzAdvProductTarge.setTargetid(item.getBigInteger("targetId"));
					amzAdvProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
					amzAdvProductTarge.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvProductTarge.setState(item.getString("state"));
					amzAdvProductTarge.setExpression(item.getString("expressions"));
					amzAdvProductTarge.setExpressiontype(item.getString("resolvedExpressions"));
					amzAdvProductTarge.setBid(item.getBigDecimal("bid"));
					amzAdvProductTarge.setProfileid(profile.getId());
					amzAdvProductTarge.setOpttime(new Date());
					AmzAdvProductTargeHsa oldamzAdvProductads = amzAdvProductTargeHsaMapper.selectByPrimaryKey(amzAdvProductTarge);
					if (oldamzAdvProductads != null) {
						this.updateAll(amzAdvProductTarge);
					} else {
						this.save(amzAdvProductTarge);
					}
					list.add(amzAdvProductTarge);
				}
				return list;
			}
		}else {
			param.put("nextToken", null);
		}
		return null;
	}

	private Map<String, String> getHeaderExt() {
		// TODO Auto-generated method stub
	  	Map<String,String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/json");
    	header.put("Accept",       "application/vnd.sblisttargetsresponse.v3.2+json");
    	return header;
	}
    public List<AmzAdvProductTargeHsa> convertJsonToBean(UserInfo user, String profileId, JSONArray keywordArray){
    	List<AmzAdvProductTargeHsa> targetList = new ArrayList<AmzAdvProductTargeHsa>();
		for (int i = 0; i < keywordArray.size(); i++) {
			JSONObject target = keywordArray.getJSONObject(i);
			BigInteger campaignId= target.getBigInteger("campaignid");
			BigInteger adGroupId = target.getBigInteger("adgroupid");
			BigInteger targetid= target.getBigInteger("targetid");
			String state= target.getString("state");
			BigDecimal bid= target.getBigDecimal("bid");
			String expressiontype=target.getString("expressiontype");
			String expression=target.getString("expression");
			AmzAdvProductTargeHsa targetobj = new AmzAdvProductTargeHsa();
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
	public List<AmzAdvProductTargeHsa> amzUpdateTargets(UserInfo user, String profileid, JSONArray targets) {
		// TODO Auto-generated method stub
		return this.amzUpdateTargetingClauses(user, new BigInteger(profileid), convertJsonToBean(user,profileid,targets));
		
	}

	@Override
	public List<AmzAdvProductTargeHsa> amzCreateTargets(UserInfo user, String profileid, JSONArray targets) {
		// TODO Auto-generated method stub
		return this.amzCreateTargetingClauses(user, new BigInteger(profileid), convertJsonToBean(user,profileid,targets));
	}

	@Override
	public PageList<Map<String, Object>> getProductTargeList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		return this.amzAdvProductTargeHsaMapper.getProductTargeList(map,pageBounds);
	}

	@Override
	public Map<String, Object> getSumProductTarge(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.amzAdvProductTargeHsaMapper.getSumProductTarge(map);
	}

	@Override
	public List<Map<String, Object>> getProductTargeChart(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return  this.amzAdvProductTargeHsaMapper.getProductTargeChart(map);
	}

	@Override
	public AmzAdvProductTargeHsa amzGetTargets(UserInfo user, BigInteger profileId, String id) {
		// TODO Auto-generated method stub
		String url =  "/sb/targets/"+id;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = apiBuildService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			AmzAdvProductTargeHsa amzAdvProductTarge = new AmzAdvProductTargeHsa();
			amzAdvProductTarge.setTargetid(item.getBigInteger("targetId"));
			amzAdvProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvProductTarge.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvProductTarge.setState(item.getString("state"));
			amzAdvProductTarge.setExpression(item.getString("expressions"));
			amzAdvProductTarge.setExpressiontype(item.getString("resolvedExpressions"));
			amzAdvProductTarge.setBid(item.getBigDecimal("bid"));
			amzAdvProductTarge.setProfileid(profile.getId());
			amzAdvProductTarge.setOpttime(new Date());
			AmzAdvProductTargeHsa oldamzAdvProductads = amzAdvProductTargeHsaMapper.selectByPrimaryKey(amzAdvProductTarge);
			if (oldamzAdvProductads != null) {
				this.updateAll(amzAdvProductTarge);
			} else {
				this.save(amzAdvProductTarge);
			}
		  return 	 amzAdvProductTarge;
		} 
	 
		return null;
	}
 
 
 

}
