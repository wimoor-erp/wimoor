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
import com.wimoor.amazon.adv.common.dao.AmzAdvProfileMapper;
import com.wimoor.amazon.adv.common.pojo.AdvState;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvProductAdsSDService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductadsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductads;
import com.wimoor.amazon.adv.sp.service.IAmzAdvCampaignService;
import com.wimoor.amazon.adv.sp.service.IAmzAdvProductAdsService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvProductAdsService")
public class AmzAdvProductAdsServiceImpl extends BaseService<AmzAdvProductads> implements IAmzAdvProductAdsService{

	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvProductadsMapper amzAdvProductadsMapper;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
 
 
	@Resource
	AmzAdvProfileMapper amzAdvProfileMapper;
 
	@Resource
	IAmzAdvProductAdsSDService amzAdvProductAdsSDService;
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	public AmzAdvProductads selectOneByExample(Example example) {
		return amzAdvProductadsMapper.selectOneByExample(example);
	}
	
	public AmzAdvProductads selectOne(AmzAdvProductads amzAdvProductads) {
		return amzAdvProductadsMapper.selectOne(amzAdvProductads);
	}
	
	public List<AmzAdvProductads> amzGetProductAdBysku(UserInfo user, BigInteger profileId, String sku) {
		if ( profileId!=null) {
			Example example = new Example(AmzAdvProductads.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("sku", sku);
			List<AmzAdvProductads> list = amzAdvProductadsMapper.selectByExample(example);
			return list;
		}else {
			throw new BaseException("请选择店铺与站点！");
		}
	}
	public List<AmzAdvProductads> amzGetProductAdByasin(UserInfo user, BigInteger profileId, String asin) {
		if ( profileId!=null) {
			Example example = new Example(AmzAdvProductads.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("asin", asin);
			List<AmzAdvProductads> list = amzAdvProductadsMapper.selectByExample(example);
			return list;
		}else {
			throw new BaseException("请选择店铺与站点！");
		}
	}
	
	public AmzAdvProductads amzGetProductAd(UserInfo user,BigInteger  profileId, String adId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/productAds/" + adId;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		AmzAdvProductads amzAdvProductads = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			amzAdvProductads = new AmzAdvProductads();
			amzAdvProductads.setAdid(item.getBigInteger("adId"));
			amzAdvProductads.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvProductads.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvProductads.setSku(item.getString("sku"));
			amzAdvProductads.setAsin(item.getString("asin"));
			amzAdvProductads.setState(item.getString("state"));
			amzAdvProductads.setProfileid(profileId);
			amzAdvProductads.setOpttime(new Date());
			AmzAdvProductads oldamzAdvProductads = amzAdvProductadsMapper.selectByPrimaryKey(amzAdvProductads);
			if (oldamzAdvProductads != null) {
				this.updateAll(amzAdvProductads);
			} else {
				this.save(amzAdvProductads);
			}
			return amzAdvProductads;
		}
		return null;
	}
	public AmzAdvProductads amzGetProductAdEx(UserInfo user,BigInteger  profileId, String adId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/productAds/extended/" + adId;
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		AmzAdvProductads amzAdvProductads = null;
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			amzAdvProductads = new AmzAdvProductads();
			amzAdvProductads.setAdid(item.getBigInteger("adId"));
			amzAdvProductads.setAdgroupid(item.getBigInteger("adGroupId"));
			amzAdvProductads.setCampaignid(item.getBigInteger("campaignId"));
			amzAdvProductads.setSku(item.getString("sku"));
			amzAdvProductads.setAsin(item.getString("asin"));
			amzAdvProductads.setState(item.getString("state"));
			amzAdvProductads.setServingStatus(item.getString("servingStatus"));
			amzAdvProductads.setCreationDate(item.getDate("creationDate"));
			amzAdvProductads.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
			amzAdvProductads.setProfileid(profileId);
			amzAdvProductads.setOpttime(new Date());
			AmzAdvProductads oldamzAdvProductads = amzAdvProductadsMapper.selectByPrimaryKey(amzAdvProductads);
			if (oldamzAdvProductads != null) {
				this.updateAll(amzAdvProductads);
			} else {
				this.save(amzAdvProductads);
			}
			return amzAdvProductads;
		}
		return null;
	}
	public List<AmzAdvProductads> amzCreateProductAds(UserInfo user,BigInteger profileId, List<AmzAdvProductads> productAdsList) {
		if (productAdsList == null || productAdsList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/productAds";
		JSONArray array = new JSONArray();
		for (int i = 0; i < productAdsList.size(); i++) {
			AmzAdvProductads amzAdvProductads = productAdsList.get(i);
			JSONObject param = new JSONObject();
			//campaignId, adGroupId, SKU or ASIN, and state.
			param.put("campaignId", amzAdvProductads.getCampaignid());
			param.put("adGroupId", amzAdvProductads.getAdgroupid());
			param.put("state", amzAdvProductads.getState());
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
		String response = amzAdvAuthService.amzAdvPost(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < productAdsList.size(); i++) {
				AmzAdvProductads amzAdvProductads = productAdsList.get(i);
				JSONObject item = a.getJSONObject(i);
				if ("SUCCESS".equals(item.getString("code"))) {
					BigInteger adId = item.getBigInteger("adId");
					amzAdvProductads.setAdid(adId);
					amzAdvProductads.setProfileid(profile.getId());
					amzAdvProductads.setOpttime(new Date());
					AmzAdvProductads DbamzAdvProductads = this.selectByKey(amzAdvProductads);
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
				AmzAdvProductads productAds = productAdsList.get(i);
				BigInteger key = productAds.getCampaignid().subtract(productAds.getAdgroupid());
				List<Object> productList = map.get(key);
				if(productList==null) {
					productList=new ArrayList<Object>();
				    map.put(key, productList);
				}
				productList.add(productAds);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductads", user.getId(), profileId, map, null);
			return productAdsList;
		}else {
			throw new BaseException("创建失败！");
		}
	}
	public List<AmzAdvProductads> amzUpdateProductAds(UserInfo user,BigInteger  profileId, List<AmzAdvProductads> productAdsList) {
		if (productAdsList == null || productAdsList.size() <= 0)
			return null;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/productAds";
		JSONArray array = new JSONArray();
		List<AmzAdvProductads> oldproductAdsList = new ArrayList<AmzAdvProductads>();
		for (int i = 0; i < productAdsList.size(); i++) {
			AmzAdvProductads amzAdvProductads = productAdsList.get(i);
			JSONObject param = new JSONObject();
			// api 不常改字段
			param.put("adId", amzAdvProductads.getAdid());
			param.put("campaignId", amzAdvProductads.getCampaignid());
			param.put("adGroupId", amzAdvProductads.getAdgroupid());
			/*if(amzAdvProductads.getAsin() != null) {
				param.put("asin", amzAdvProductads.getAsin());
			}*/
			if(amzAdvProductads.getSku() != null) {
				param.put("sku", amzAdvProductads.getSku());
			}else {
				param.put("asin", amzAdvProductads.getAsin());
			}
			param.put("state", amzAdvProductads.getState());
			array.add(param);
			
			Example example = new Example(AmzAdvProductads.class);
			Criteria crit = example.createCriteria();
			crit.andEqualTo("profileid", profileId);
			crit.andEqualTo("adid", amzAdvProductads.getAdid());
			AmzAdvProductads productads = amzAdvProductadsMapper.selectOneByExample(example);
			oldproductAdsList.add(productads);
		}
		String response = amzAdvAuthService.amzAdvPut(profile, url, array.toString());
		if (StringUtil.isNotEmpty(response)) {
			String errormsg = "";
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < productAdsList.size(); i++) {
				AmzAdvProductads amzAdvProductads = productAdsList.get(i);
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
				AmzAdvProductads productAds = productAdsList.get(i);
				BigInteger key = productAds.getCampaignid().subtract(productAds.getAdgroupid());
				List<Object> productList = map.get(key);
				if(productList==null) {
					productList=new ArrayList<Object>();
				    map.put(key, productList);
				}
				productList.add(productAds);
			}
			for (int i = 0; i < oldproductAdsList.size(); i++) {
				AmzAdvProductads oldproductAds = oldproductAdsList.get(i);
				BigInteger key = oldproductAds.getCampaignid().subtract(oldproductAds.getAdgroupid());
				List<Object> productList = oldmap.get(key);
				if(productList==null) {
					productList=new ArrayList<Object>();
					oldmap.put(key, productList);
				}
				productList.add(oldproductAds);
			}
			amzAdvOperateLogService.saveBatchOperateLog("AmzAdvProductads", user.getId(), profileId, map, oldmap);
			return productAdsList;
		}
		return null;
	}
	public AmzAdvProductads amzArchiveProductAd(UserInfo user,BigInteger  profileId, String adId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		AmzAdvProductads amzAdvProductads = null;
		AmzAdvProductads oldamzAdvProductads = null;
		String url = "/sp/productAds/" + adId;
		String response = amzAdvAuthService.amzAdvDelete(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject a = GeneralUtil.getJsonObject(response.toString());
			if ("SUCCESS".equals(a.getString("code"))) {
				Example example = new Example(AmzAdvProductads.class);
				Criteria crit = example.createCriteria();
				crit.andEqualTo("profileid", profileId);
				crit.andEqualTo("adid", adId);
				oldamzAdvProductads = amzAdvProductadsMapper.selectOneByExample(example);
				amzAdvProductads = amzAdvProductadsMapper.selectOneByExample(example);;
				amzAdvProductads.setState(AdvState.archived);
				amzAdvProductads.setOpttime(new Date());
				this.updateAll(amzAdvProductads);
				amzAdvOperateLogService.saveOperateLog("AmzAdvProductads", user.getId(), profileId, amzAdvProductads, oldamzAdvProductads);
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
	public List<AmzAdvProductads> amzListProductAds(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/productAds/?";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "startIndex");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "count");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "sku");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "asin");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "stateFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "campaignIdFilter");
			paramurl = GeneralUtil.addParamToUrl(paramurl, param, "adGroupIdFilter");
		}
		url = url + ("".equals(paramurl) ? "" : paramurl);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvProductads> list = new LinkedList<AmzAdvProductads>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
				amzAdvProductads.setAdid(item.getBigInteger("adId"));
				amzAdvProductads.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvProductads.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvProductads.setSku(item.getString("sku"));
				amzAdvProductads.setAsin(item.getString("asin"));
				amzAdvProductads.setState(item.getString("state"));
				amzAdvProductads.setProfileid(profileId);
				amzAdvProductads.setOpttime(new Date());
				AmzAdvProductads oldamzAdvProductads = amzAdvProductadsMapper.selectByPrimaryKey(amzAdvProductads);
				if (oldamzAdvProductads != null) {
					this.updateAll(amzAdvProductads);
				} else {
					this.save(amzAdvProductads);
				}
				list.add(amzAdvProductads);
			}
			return list;
		}
		return null;
	}
	public List<AmzAdvProductads> amzListProductAdsEx(UserInfo user,BigInteger  profileId, Map<String, Object> param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/productAds/extended?";
		String paramurl = "";
		if(param != null) {
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "startIndex");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "count");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "adGroupId");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "sku");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "asin");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "stateFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "campaignIdFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "adGroupIdFilter");
			paramurl = GeneralUtil.addParamToUrl2(paramurl, param, "adIdFilter");
		}
		url = url + ("".equals(paramurl) ? "" : paramurl);
		String response = amzAdvAuthService.amzAdvGet(profile, url);
		List<AmzAdvProductads> list = new LinkedList<AmzAdvProductads>();
		if (StringUtil.isNotEmpty(response)) {
			JSONArray a = GeneralUtil.getJsonArray(response.toString());
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
				amzAdvProductads.setAdid(item.getBigInteger("adId"));
				amzAdvProductads.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvProductads.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvProductads.setSku(item.getString("sku"));
				amzAdvProductads.setAsin(item.getString("asin"));
				amzAdvProductads.setState(item.getString("state"));
				amzAdvProductads.setServingStatus(item.getString("servingStatus"));
				amzAdvProductads.setCreationDate(item.getDate("creationDate"));
				amzAdvProductads.setLastUpdatedDate(item.getDate("lastUpdatedDate"));
				amzAdvProductads.setProfileid(profileId);
				amzAdvProductads.setOpttime(new Date());
				AmzAdvProductads oldamzAdvProductads = amzAdvProductadsMapper.selectByPrimaryKey(amzAdvProductads);
				if (oldamzAdvProductads != null) {
					this.updateAll(amzAdvProductads);
				} else {
					this.save(amzAdvProductads);
				}
				list.add(amzAdvProductads);
			}
			return list;
		}
		return null;
	}

	public List<Map<String,Object>> amzGetProductInfoByAsin(BigInteger profileid,List<String> asinList){
//		AmzAdvProfile profile = amzAdvProfileMapper.selectByPrimaryKey(profileid);
//		if(profile==null)return null;
//		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
//		for(int i = 0; i < list.size(); i++) {
//					map.put("asin", asin);
//					map.put("currency", currency);
//					map.put("id", UUID.randomUUID().toString());
//					map.put("image", image);
//					map.put("marketplaceid", marketplaceid);
//					map.put("name", name);
//					map.put("price", price);
// 					map.put("sku", sku);
//					listMap.add(map);
//		}
//		return listMap;
		return null;
	}
	
	public List<Map<String,Object>> ListMatchingProducts(BigInteger profileid,String name){
	 
//		AmzAdvProfile profile = amzAdvProfileMapper.selectByPrimaryKey(profileid);
//		for(int i = 0; i < listProduct.size(); i++) {
//			Map<String,Object> map = new HashMap<String, Object>();
//		 
//					map.put("asin", asin);
//					map.put("currency", currency);
//					map.put("id", UUID.randomUUID().toString());
//					map.put("image", image);
//					map.put("marketplaceid", marketplaceid);
//					map.put("name", title);
//					map.put("price", price);
// 					map.put("sku", sku);
//					listMap.add(map);
//		}
		return null;
	}
	
	public List<AmzAdvProductads> getProductadByAdgroupId(UserInfo user, BigInteger profileid, String adgroupid) {
		Example example =new Example(AmzAdvProductads.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("adgroupid",adgroupid);
		crit.andEqualTo("profileid",profileid);
		return amzAdvProductadsMapper.selectByExample(example);
	}
	
	public PageList<Map<String, Object>> getProductAdList(Map<String, Object> map, PageBounds pageBounds){
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("HSA".equals(campaignType)) {
				return null;
			}if("SP".equals(campaignType.toUpperCase())) {
				return amzAdvProductadsMapper.getProductAdList(map, pageBounds);
			}
			if("SD".equals(campaignType.toUpperCase())) {
				return amzAdvProductAdsSDService.getProductAdList(map, pageBounds);
			}
		}
		return amzAdvProductadsMapper.getAllProductAdList(map, pageBounds);
		
	}
	
	public Map<String, Object> getProductAdChart(Map<String, Object> map) {
		getSerchStr(map);
		List<Map<String, Object>> listSP = null;
		List<Map<String, Object>> listSD = null;
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("HSA".equals(campaignType)) {
				return null;
			}else if("SP".equals(campaignType.toUpperCase())) {
				 listSP = amzAdvProductadsMapper.getProductAdChart(map);
			}else if("SD".equals(campaignType.toUpperCase())) {
				 listSD = amzAdvProductadsMapper.getProductAdChart(map);
			}
		}
		return  amzAdvCampaignService.getChartData(listSP, null, listSD, map);
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
	
 
	public List<Map<String, Object>> getProductAdotherAsin(Map<String, Object> map) {
		return amzAdvProductadsMapper.getProductAdotherAsin(map);
		
	}
	
	public List<Map<String, Object>> selectByAdgroupid(BigInteger profileids, BigInteger campaignids,
			BigInteger adgroupids) {
		List<Map<String, Object>> list = amzAdvProductadsMapper.selectByAdgroupid(profileids, campaignids, adgroupids);
		return list;
	}

	public List<Map<String, Object>> getSumProductAd(Map<String, Object> map) {
		Object paralist = map.get("paralist");
		String campaignType = (String) map.get("campaignType");
		if(paralist != null) {
			paralist = paralist.toString().replace("CSRT", "attributedConversions7d / clicks")
				.replace("ACOS", "cost / attributedSales7d").replace("ROAS", "attributedSales7d / cost");
			map.put("paralist", paralist);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if ("SP".equals(campaignType)) {
			Map<String, Object> paramMap = amzAdvProductadsMapper.getSumProductAd(map);
			if(paramMap!=null) {
				list.add(paramMap);
			}
		}else if ("SD".equals(campaignType)) {
			Map<String, Object> paramMap = amzAdvProductAdsSDService.getSumProductAd(map);
			if(paramMap!=null) {
				list.add(paramMap);
			}
		} else if ("HSA".equals(campaignType) || "SB".equals(campaignType)) {
			 return null;
		} else if ("all".equals(campaignType)) {
			Map<String, Object> paramMap1 = amzAdvProductadsMapper.getSumProductAd(map);
			if(paramMap1!=null) {
				list.add(paramMap1);
			}
			Map<String, Object> paramMap2 = amzAdvProductAdsSDService.getSumProductAd(map);
			if(paramMap2!=null) {
				list.add(paramMap2);
			}
		}
		if(list != null && list.size() > 0) {
			return list;
		}else {
			return null;
		}
	 
	}
	 
	
}
