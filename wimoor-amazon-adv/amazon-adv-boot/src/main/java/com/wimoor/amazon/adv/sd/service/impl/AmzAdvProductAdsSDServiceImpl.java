package com.wimoor.amazon.adv.sd.service.impl;

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
import com.wimoor.amazon.adv.sd.dao.AmzAdvProductadsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductAdsSDService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductads;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvProductAdsSDService")
public class AmzAdvProductAdsSDServiceImpl extends BaseService<AmzAdvProductadsSD> implements IAmzAdvProductAdsSDService{
	@Resource
	AmzAdvProductadsSDMapper amzAdvProductadsSDMapper;

	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	@Resource
	ApiBuildService apiBuildService;
	public PageList<Map<String, Object>> getProductAdList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		return amzAdvProductadsSDMapper.getProductAdList(map,pageBounds) ;
	}
	public Map<String, Object> getSumProductAd(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return amzAdvProductadsSDMapper.getSumProductAd(map);
	}
	public List<AmzAdvProductadsSD> amzCreateProductAds(UserInfo user,BigInteger profileId, List<AmzAdvProductadsSD> productAdsList) {
		if (productAdsList == null || productAdsList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/productAds";
		JSONArray array = new JSONArray();
		for (int i = 0; i < productAdsList.size(); i++) {
			AmzAdvProductadsSD amzAdvProductads = productAdsList.get(i);
			JSONObject param = new JSONObject();
			//campaignId, adGroupId, SKU or ASIN, and state.
			param.put("campaignId", amzAdvProductads.getCampaignid());
			param.put("adGroupId", amzAdvProductads.getAdgroupid());
			param.put("state", amzAdvProductads.getState());
			param.put("adName", amzAdvProductads.getAdName());
			param.put("landingPageType", amzAdvProductads.getLandingPageType());
			param.put("landingPageURL", amzAdvProductads.getLandingPageURL());
			if("vendor".equals(profile.getType())) {
				param.put("asin", amzAdvProductads.getAsin());
			}else {
				if(amzAdvProductads.getSku() != null) {
					param.put("sku", amzAdvProductads.getSku());
				}else {
					param.put("asin", amzAdvProductads.getAsin());
				}
			}
			array.add(param);
		}
		String response = apiBuildService.amzAdvPost_V2(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < productAdsList.size(); i++) {
				AmzAdvProductadsSD amzAdvProductads = productAdsList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger adId = item.getBigInteger("adId");
					amzAdvProductads.setAdid(adId);
					amzAdvProductads.setProfileid(profile.getId());
					amzAdvProductads.setOpttime(new Date());
					AmzAdvProductadsSD DbamzAdvProductads = this.selectByKey(amzAdvProductads);
					if(DbamzAdvProductads == null) {
						this.save(amzAdvProductads);
					}
				}else {
					String name = amzAdvProductads.getSku();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("商品广告：'"+name+"' 创建失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productAdsList.size(); i++) {
				AmzAdvProductadsSD productAds = productAdsList.get(i);
				BigInteger key = productAds.getCampaignid().subtract(productAds.getAdgroupid());
				List<Object> productList = map.get(key);
				if(productList==null) {
					productList=new ArrayList<Object>();
				    map.put(key, productList);
				}
				productList.add(productAds);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductadsSD", user.getId(), profileId, map, null);
			return productAdsList;
		}else {
			throw new BaseException("创建失败！");
		}
	}
	public List<AmzAdvProductadsSD> amzUpdateProductAds(UserInfo user,BigInteger  profileId, List<AmzAdvProductadsSD> productAdsList) {
		if (productAdsList == null || productAdsList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/productAds";
		JSONArray array = new JSONArray();
		List<AmzAdvProductadsSD> oldproductAdsList = new ArrayList<AmzAdvProductadsSD>();
		for (int i = 0; i < productAdsList.size(); i++) {
			AmzAdvProductadsSD amzAdvProductads = productAdsList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("adId", amzAdvProductads.getAdid());
			param.put("campaignId", amzAdvProductads.getCampaignid());
			param.put("adGroupId", amzAdvProductads.getAdgroupid());
			/*if(amzAdvProductads.getAsin() != null) {
				param.put("asin", amzAdvProductads.getAsin());
			}*/
			param.put("adName", amzAdvProductads.getAdName());
			param.put("landingPageType", amzAdvProductads.getLandingPageType());
			param.put("landingPageURL", amzAdvProductads.getLandingPageURL());
			if(amzAdvProductads.getSku() != null) {
				param.put("sku", amzAdvProductads.getSku());
			}else {
				param.put("asin", amzAdvProductads.getAsin());
			}
			param.put("state", amzAdvProductads.getState());
			array.add(param);
			
			AmzAdvProductadsSD productads = amzAdvProductadsSDMapper.selectByPrimaryKey(amzAdvProductads.getAdid());
			oldproductAdsList.add(productads);
		}
		String response = apiBuildService.amzAdvPut_V2(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < productAdsList.size(); i++) {
				AmzAdvProductadsSD amzAdvProductads = productAdsList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger adId = item.getBigInteger("adId");
					amzAdvProductads.setAdid(adId);
					amzAdvProductads.setProfileid(profile.getId());
					amzAdvProductads.setOpttime(new Date());
					this.updateNotNull(amzAdvProductads);
				}else {
					String name = amzAdvProductads.getSku();
					errormsg = errormsg.equals("") ? "" : errormsg + ",";
					errormsg = errormsg + item.getString("description");
					BaseException exception = new BaseException("商品广告：'"+name+"' 修改失败：" + errormsg);
					exception.setCode("ERROER");
					throw exception;
				}
			}
			Map<BigInteger, List<Object>> map=new HashMap<BigInteger,List<Object>>();
			Map<BigInteger, List<Object>> oldmap=new HashMap<BigInteger,List<Object>>();
			for (int i = 0; i < productAdsList.size(); i++) {
				AmzAdvProductadsSD productAds = productAdsList.get(i);
				BigInteger key = productAds.getCampaignid().subtract(productAds.getAdgroupid());
				List<Object> productList = map.get(key);
				if(productList==null) {
					productList=new ArrayList<Object>();
				    map.put(key, productList);
				}
				productList.add(productAds);
			}
			for (int i = 0; i < oldproductAdsList.size(); i++) {
				AmzAdvProductadsSD oldproductAds = oldproductAdsList.get(i);
				BigInteger key = oldproductAds.getCampaignid().subtract(oldproductAds.getAdgroupid());
				List<Object> productList = oldmap.get(key);
				if(productList==null) {
					productList=new ArrayList<Object>();
					oldmap.put(key, productList);
				}
				productList.add(oldproductAds);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductadsSD", user.getId(), profileId, map, oldmap);
			return productAdsList;
		}
		return null;
	}
	public AmzAdvProductadsSD amzArchiveProductAd(UserInfo user,BigInteger  profileId, String adId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvProductadsSD amzAdvProductads = null;
		AmzAdvProductadsSD oldamzAdvProductads = null;
		String url = "/sd/productAds/" + adId;
		String response = apiBuildService.amzAdvDelete_V2(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example = new Example(AmzAdvProductads.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("adid", adId);
				oldamzAdvProductads = amzAdvProductadsSDMapper.selectOneByExample(example);
				amzAdvProductads = amzAdvProductadsSDMapper.selectOneByExample(example);;
				amzAdvProductads.setState(AdvState.archived);
				amzAdvProductads.setOpttime(new Date());
				this.updateAll(amzAdvProductads);
				amzAdvOperateLogService.saveOperateLog("AmzAdvProductadsSD", user.getId(), profileId, amzAdvProductads, oldamzAdvProductads);
				return amzAdvProductads;
			}else {
				String errormsg = "";
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + a.getString("description");
				BaseException exception = new BaseException("商品广告修改失败："+errormsg);
				exception.setCode("ERROER");
				throw exception;
			}
		}
		return null;
	}
	public List<AmzAdvProductadsSD> getProductadByAdgroupId(UserInfo user, BigInteger profileid, String adgroupid) {
		// TODO Auto-generated method stub
		Example example =new Example(AmzAdvProductadsSD.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("adgroupid",adgroupid);
		crit.andEqualTo("profileid",profileid);
		return amzAdvProductadsSDMapper.selectByExample(example);
	}
	
	public List<AmzAdvProductadsSD> convertJsonToBean(UserInfo user, String profileid, JSONArray productAdArray) {
		if(productAdArray==null)return null;
		List<AmzAdvProductadsSD> list=new ArrayList<AmzAdvProductadsSD>();
		for(int i=0;i<productAdArray.size();i++) {
			JSONObject productAd = productAdArray.getJSONObject(i);
			String state = productAd.getString("state");
			String sku = productAd.getString("sku");
			String asin = productAd.getString("asin");
			String adName = productAd.getString("adName");
			String landingPageType = productAd.getString("landingPageType");
			String landingPageURL = productAd.getString("landingPageURL");
			BigInteger campaignid = productAd.getBigInteger("campaignid");
			BigInteger adGroupid = productAd.getBigInteger("adgroupid");
			BigInteger profileId = productAd.getBigInteger("profileid");
			BigInteger adid = productAd.getBigInteger("adid");
			AmzAdvProductadsSD amzAdvProductads = new AmzAdvProductadsSD();
			amzAdvProductads.setAdid(adid);
			amzAdvProductads.setCampaignid(campaignid);
			amzAdvProductads.setAdgroupid(adGroupid);
			amzAdvProductads.setAdName(adName);
			amzAdvProductads.setLandingPageType(landingPageType);
			amzAdvProductads.setLandingPageURL(landingPageURL);
			amzAdvProductads.setProfileid(profileId);
			amzAdvProductads.setSku(sku);
			amzAdvProductads.setAsin(asin);
			amzAdvProductads.setState(state.toLowerCase());
			amzAdvProductads.setOpttime(new Date());
			list.add(amzAdvProductads);
		}
		return list;
	}
	
	@Override
	public List<AmzAdvProductadsSD> amzUpdateProductAds(UserInfo user, String profileid, JSONArray productAdArray) {
		// TODO Auto-generated method stub
		return amzUpdateProductAds(user,new BigInteger(profileid),this.convertJsonToBean(user, profileid, productAdArray));
	}
	@Override
	public List<AmzAdvProductadsSD> amzCreateProductAds(UserInfo user, String profileid, JSONArray productAdArray) {
		// TODO Auto-generated method stub
		return amzCreateProductAds(user,new BigInteger(profileid),this.convertJsonToBean(user, profileid, productAdArray));
	}
	
	@Override
	public AmzAdvProductadsSD amzListProductAds(UserInfo user, BigInteger profileId,String adid) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/productAds/extended/" + adid;
		String response = apiBuildService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			AmzAdvProductadsSD amzAdvProductads = new AmzAdvProductadsSD();
			amzAdvProductads.setAdid(item.getBigInteger("adId"));
			amzAdvProductads.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvProductads.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvProductads.setSku(item.getString("sku"));
			amzAdvProductads.setAsin(item.getString("asin"));
			amzAdvProductads.setState(item.getString("state"));
			amzAdvProductads.setAdName(item.getString("adName"));
			amzAdvProductads.setLandingPageType(item.getString("landingPageType"));
			amzAdvProductads.setLandingPageURL(item.getString("landingPageURL"));
			amzAdvProductads.setProfileid(profileId);
			amzAdvProductads.setOpttime(new Date());
			String servingStatus=item.getString("servingStatus");
			amzAdvProductads.setServingStatus(servingStatus);
			Date creationDateTime=item.getDate("creationDateTime");
			amzAdvProductads.setCreationDate(creationDateTime);
			Date lastUpdateDateTime=item.getDate("lastUpdateDateTime");
			amzAdvProductads.setLastUpdatedDate(lastUpdateDateTime);
			AmzAdvProductadsSD DbamzAdvProductads = this.selectByKey(amzAdvProductads);
			if(DbamzAdvProductads == null) {
				this.save(amzAdvProductads);
			}else {
				this.updateAll(amzAdvProductads);
			}
			return amzAdvProductads;
		}
		return null;
	}
}
