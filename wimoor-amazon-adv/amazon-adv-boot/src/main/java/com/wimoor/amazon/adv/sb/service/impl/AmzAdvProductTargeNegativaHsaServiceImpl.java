package com.wimoor.amazon.adv.sb.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.wimoor.amazon.adv.sb.dao.AmzAdvProductTargeNegativaHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvProductTargeNegativaHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvProductTargeNegativaHsaService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;
@Service("amzAdvProductTargeNegativaHsaService")
public class AmzAdvProductTargeNegativaHsaServiceImpl extends BaseService<AmzAdvProductTargeNegativaHsa> implements IAmzAdvProductTargeNegativaHsaService{
	@Resource
	ApiBuildService apiBuildService;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvProductTargeNegativaHsaMapper amzAdvProductTargeNegativaHsaMapper;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
 
	public AmzAdvProductTargeNegativaHsa amzArchiveNegativeTargetingClause(UserInfo user,BigInteger  profileId, String targetId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvProductTargeNegativaHsa productTarge = null;
		AmzAdvProductTargeNegativaHsa oldproductTarge = null;
		String url = "/sb/negativeTargets/" + targetId;
		String response = apiBuildService.amzAdvDelete(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example = new Example(AmzAdvProductTargeNegativaHsa.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("targetid", targetId);
				productTarge = amzAdvProductTargeNegativaHsaMapper.selectOneByExample(example);
				oldproductTarge = amzAdvProductTargeNegativaHsaMapper.selectOneByExample(example);
				productTarge.setState(AdvState.archived);
				productTarge.setOpttime(new Date());
				amzAdvProductTargeNegativaHsaMapper.updateByPrimaryKey(productTarge);
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
	
	
	public List<AmzAdvProductTargeNegativaHsa> amzCreateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,
			List<AmzAdvProductTargeNegativaHsa> productTargeList) {
		if (productTargeList == null || productTargeList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/negativeTargets";
		JSONObject targetObject = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < productTargeList.size(); i++) {
			AmzAdvProductTargeNegativaHsa amzAdvProductTargeNegativa = productTargeList.get(i);
			JSONObject param = new JSONObject();
			param.put("campaignId", amzAdvProductTargeNegativa.getCampaignid());
			param.put("adGroupId", amzAdvProductTargeNegativa.getAdgroupid());
			param.put("expressions", GeneralUtil.getJsonArray(amzAdvProductTargeNegativa.getExpression()));
			array.add(param);
		}
		targetObject.put("negativeTargets", array);
		String response = apiBuildService.amzAdvPost(profile, url, targetObject.toString());
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
					AmzAdvProductTargeNegativaHsa amzAdvProductTarge = productTargeList.get(i);
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTarge.setTargetid(targetId);
					amzAdvProductTarge.setProfileid(profileId);
					amzAdvProductTarge.setOpttime(new Date());
					AmzAdvProductTargeNegativaHsa DbamzAdvProductTarge = this.selectByKey(amzAdvProductTarge);
					if(DbamzAdvProductTarge == null) {
						this.save(amzAdvProductTarge);
					}
				}
			}
			Map<BigInteger, List<Object>> map = new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeList.size(); i++) {
				AmzAdvProductTargeNegativaHsa productTargeNegativa = productTargeList.get(i);
				BigInteger key = productTargeNegativa.getCampaignid().subtract(productTargeNegativa.getAdgroupid());
				List<Object> targeNegativaList = map.get(key);
				if(targeNegativaList==null) {
					targeNegativaList=new ArrayList<Object>();
				    map.put(key, targeNegativaList);
				}
				targeNegativaList.add(productTargeNegativa);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductTargeNegativaSB", user.getId(), profileId, map, null);
			return productTargeList;
		}else {
			throw new BaseException("创建失败！");
		}
	}

	public List<AmzAdvProductTargeNegativaHsa> amzUpdateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,
			List<AmzAdvProductTargeNegativaHsa> productTargeNegativaList) {
		// TODO Auto-generated method stub
		if (productTargeNegativaList == null || productTargeNegativaList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/negativeTargets";
		JSONArray array = new JSONArray();
		JSONObject targetObject = new JSONObject();
		List<AmzAdvProductTargeNegativaHsa> oldProductTargeNegativaList = new ArrayList<AmzAdvProductTargeNegativaHsa>();
		for (int i = 0; i < productTargeNegativaList.size(); i++) {
			AmzAdvProductTargeNegativaHsa amzAdvProductTargeNegativa = productTargeNegativaList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("targetId", amzAdvProductTargeNegativa.getTargetid());
			param.put("adGroupId", amzAdvProductTargeNegativa.getAdgroupid());
			param.put("state", amzAdvProductTargeNegativa.getState());
			array.add(param);
			
			Example example = new Example(AmzAdvProductTargeNegativaHsa.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("targetid", amzAdvProductTargeNegativa.getTargetid());
			AmzAdvProductTargeNegativaHsa productTargeNegativa = amzAdvProductTargeNegativaHsaMapper.selectOneByExample(example);
			oldProductTargeNegativaList.add(productTargeNegativa);
		}
		targetObject.put("negativeTargets", array);
		String response = apiBuildService.amzAdvPut(profile, url, targetObject.toString());
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
					AmzAdvProductTargeNegativaHsa amzAdvProductTarge = productTargeNegativaList.get(i);
					BigInteger targetId = item.getBigInteger("targetId");
					amzAdvProductTarge.setTargetid(targetId);
					amzAdvProductTarge.setProfileid(profileId);
					amzAdvProductTarge.setOpttime(new Date());
					AmzAdvProductTargeNegativaHsa DbamzAdvProductTarge = this.selectByKey(amzAdvProductTarge);
					if(DbamzAdvProductTarge == null) {
						this.save(amzAdvProductTarge);
					}
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			Map<BigInteger, List<Object>> oldmap=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productTargeNegativaList.size(); i++) {
				AmzAdvProductTargeNegativaHsa productTargeNegativa = productTargeNegativaList.get(i);
				BigInteger key = productTargeNegativa.getCampaignid().subtract(productTargeNegativa.getAdgroupid());
				List<Object> targeNegativaList = map.get(key);
				if(targeNegativaList==null) {
					targeNegativaList=new ArrayList<Object>();
				    map.put(key, targeNegativaList);
				}
				targeNegativaList.add(productTargeNegativa);
			}
			for (int i = 0; i < oldProductTargeNegativaList.size(); i++) {
				AmzAdvProductTargeNegativaHsa oldproductTargeNegativa = oldProductTargeNegativaList.get(i);
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

	@Override
	public AmzAdvProductTargeNegativaHsa amzNegativeTargetingClauses(UserInfo user, BigInteger profileId, String id) {
		// TODO Auto-generated method stub
		String url =  "/sb/negativeTargets/"+id;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String response = apiBuildService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			AmzAdvProductTargeNegativaHsa amzAdvProductTarge = new AmzAdvProductTargeNegativaHsa();
			amzAdvProductTarge.setTargetid(item.getBigInteger("targetId"));
			amzAdvProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvProductTarge.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvProductTarge.setState(item.getString("state"));
			amzAdvProductTarge.setExpression(item.getString("expressions"));
			amzAdvProductTarge.setExpressiontype(item.getString("resolvedExpressions"));
			amzAdvProductTarge.setProfileid(profile.getId());
			amzAdvProductTarge.setOpttime(new Date());
			AmzAdvProductTargeNegativaHsa oldamzAdvProductads = amzAdvProductTargeNegativaHsaMapper.selectByPrimaryKey(amzAdvProductTarge);
			if (oldamzAdvProductads != null) {
				this.updateAll(amzAdvProductTarge);
			} else {
				this.save(amzAdvProductTarge);
			}
		  return 	 amzAdvProductTarge;
		} 
	 
		return null;
	}
    public List<AmzAdvProductTargeNegativaHsa> convertJsonToBean(UserInfo user, String profileId, JSONArray keywordArray){
    	List<AmzAdvProductTargeNegativaHsa> targetList = new ArrayList<AmzAdvProductTargeNegativaHsa>();
		for (int i = 0; i < keywordArray.size(); i++) {
			JSONObject target = keywordArray.getJSONObject(i);
			BigInteger campaignId= target.getBigInteger("campaignid");
			BigInteger adGroupId = target.getBigInteger("adgroupid");
			BigInteger targetid= target.getBigInteger("targetid");
			String state= target.getString("state");
			String expressiontype=target.getString("expressiontype");
			String expression=target.getString("expression");
			AmzAdvProductTargeNegativaHsa targetobj = new AmzAdvProductTargeNegativaHsa();
			targetobj.setState(state);
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
	public List<AmzAdvProductTargeNegativaHsa> amzUpdateNegativeTargetingClauses(UserInfo user, String profileid,
			JSONArray keywords) {
		// TODO Auto-generated method stub
		return this.amzUpdateNegativeTargetingClauses(user, new BigInteger(profileid), this.convertJsonToBean(user, profileid, keywords));
	}


	@Override
	public List<AmzAdvProductTargeNegativaHsa> amzCreateNegativeTargetingClauses(UserInfo user, String profileid,
			JSONArray keywords) {
		// TODO Auto-generated method stub
		return this.amzCreateNegativeTargetingClauses(user, new BigInteger(profileid), this.convertJsonToBean(user, profileid, keywords));
	}


	@Override
	public PageList<Map<String, Object>> getProductNegativaTargeList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		return amzAdvProductTargeNegativaHsaMapper.getProductNegativaTargeList( map, pageBounds);
	}
	
}
