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
import com.wimoor.amazon.adv.api.AmazonClientOneFeignManager;
import com.wimoor.amazon.adv.common.dao.AmzAdvProfileMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvOperateLog;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
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

import cn.hutool.core.util.StrUtil;
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
	ApiBuildService apiBuildService;
 
	@Resource
	AmzAdvProfileMapper amzAdvProfileMapper;
 
	@Resource
	IAmzAdvProductAdsSDService amzAdvProductAdsSDService;
	@Resource
	IAmzAdvCampaignService amzAdvCampaignService;
	@Resource
	AmazonClientOneFeignManager amazonClientOneFeignManager;
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
			param.put("campaignId", amzAdvProductads.getCampaignid().toString());
			param.put("customText", amzAdvProductads.getCustomText());
			param.put("adGroupId", amzAdvProductads.getAdgroupid().toString());
			param.put("state", amzAdvProductads.getState());
			if("vendor".equals(profile.getType())) {
				param.put("asin", amzAdvProductads.getAsin());
			}else {
			    param.put("sku", amzAdvProductads.getSku());
			}
			array.add(param);
		}
		JSONObject productAds=new JSONObject();
		productAds.put("productAds", array);
		String response = apiBuildService.amzAdvPost(profile, url, productAds.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
		    List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject productAdsRes = json.getJSONObject("productAds");
			JSONArray success = productAdsRes.getJSONArray("success");
			JSONArray error = productAdsRes.getJSONArray("error");
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				AmzAdvProductads amzAdvProductads = productAdsList.get(index);
		 
				BigInteger adId = item.getBigInteger("adId");
				amzAdvProductads.setAdid(adId);
				amzAdvProductads.setProfileid(profile.getId());
				amzAdvProductads.setOpttime(new Date());
				this.amzAdvProductadsMapper.insert(amzAdvProductads);
				
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				operateLog.setCampaignid(amzAdvProductads.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvProductads");
				String amzAdvProductadsjson = GeneralUtil.toJSON(amzAdvProductads);
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
		return productAdsList;
		}else {
			return null;
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
			param.put("adId",amzAdvProductads.getAdid().toString());
			param.put("state", amzAdvProductads.getState());
			array.add(param);
			AmzAdvProductads productads = amzAdvProductadsMapper.selectByPrimaryKey(amzAdvProductads.getAdid());
			oldproductAdsList.add(productads);
		}
		JSONObject dataRaw = new JSONObject();
		dataRaw.put("productAds",array);
		String response = apiBuildService.amzAdvPut(profile, url, dataRaw.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
		    List<AmzAdvOperateLog> operateLogList = new ArrayList<AmzAdvOperateLog>();
			String errormsg = "";
			JSONObject json = GeneralUtil.getJsonObject(response.toString());
			JSONObject productAdsJson = json.getJSONObject("productAds");
			JSONArray success = productAdsJson.getJSONArray("success");
			JSONArray error = productAdsJson.getJSONArray("error");
			for(int i=0;i<success.size();i++) {
				JSONObject item=success.getJSONObject(i);
				Integer index = item.getInteger("index");
				AmzAdvProductads amzAdvProductads = productAdsList.get(index);
		 
				BigInteger adId = item.getBigInteger("adId");
				amzAdvProductads.setAdid(adId);
				amzAdvProductads.setProfileid(profile.getId());
				amzAdvProductads.setOpttime(new Date());
				this.updateNotNull(amzAdvProductads);
				
				AmzAdvOperateLog operateLog = new AmzAdvOperateLog();
				AmzAdvProductads oldproductAds = oldproductAdsList.get(index);
				operateLog.setCampaignid(amzAdvProductads.getCampaignid());
				operateLog.setProfileid(profileId);
				operateLog.setOperator(user.getId());
				operateLog.setOpttime(new Date());
				operateLog.setBeanclasz("AmzAdvCampaigns");
				String amzAdvProductadsjson = GeneralUtil.toJSON(amzAdvProductads);
				String oldproductAdsjson = GeneralUtil.toJSON(oldproductAds);
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
		return productAdsList;
	}
		
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
	private Map<String, String> getHeaderExt() {
		// TODO Auto-generated method stub
		Map<String,String> header=new HashMap<String,String>();
    	header.put("Content-Type", "application/vnd.spProductAd.v3+json");
    	header.put("Accept",       "application/vnd.spProductAd.v3+json");
		return header;
	}
 
	public List<AmzAdvProductads> amzDeleteProductAds(UserInfo user,BigInteger  profileId, JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/productAds/delete";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		List<AmzAdvProductads> list = new LinkedList<AmzAdvProductads>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject res = GeneralUtil.getJsonObject(response.toString());
			JSONArray a =res.getJSONArray("productAds");
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
				amzAdvProductads.setAdid(item.getBigInteger("adId"));
				amzAdvProductads.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvProductads.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvProductads.setSku(item.getString("sku"));
				amzAdvProductads.setAsin(item.getString("asin"));
				amzAdvProductads.setState(item.getString("state"));
				amzAdvProductads.setCustomText(item.getString("customText"));
				amzAdvProductads.setProfileid(profileId);
				amzAdvProductads.setOpttime(new Date());
				JSONObject extendedData = item.getJSONObject("extendedData");
				if(extendedData!=null) {
					String servingStatus=extendedData.getString("servingStatus");
					amzAdvProductads.setServingStatus(servingStatus);
					Date creationDateTime=extendedData.getDate("creationDateTime");
					amzAdvProductads.setCreationDate(creationDateTime);
					Date lastUpdateDateTime=extendedData.getDate("lastUpdateDateTime");
					amzAdvProductads.setLastUpdatedDate(lastUpdateDateTime);
				}
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
	
	public List<AmzAdvProductads> amzListProductAds(UserInfo user,BigInteger  profileId, JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sp/productAds/list";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),this.getHeaderExt());
		List<AmzAdvProductads> list = new LinkedList<AmzAdvProductads>();
		if (StringUtil.isNotEmpty(response)) {
			JSONObject res = GeneralUtil.getJsonObject(response.toString());
			JSONArray a =res.getJSONArray("productAds");
			for (int i = 0; i < a.size(); i++) {
				JSONObject item = a.getJSONObject(i);
				AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
				amzAdvProductads.setAdid(item.getBigInteger("adId"));
				amzAdvProductads.setAdgroupid(item.getBigInteger("adGroupId"));
				amzAdvProductads.setCampaignid(item.getBigInteger("campaignId"));
				amzAdvProductads.setSku(item.getString("sku"));
				amzAdvProductads.setAsin(item.getString("asin"));
				amzAdvProductads.setState(item.getString("state"));
				amzAdvProductads.setCustomText(item.getString("customText"));
				amzAdvProductads.setProfileid(profileId);
				amzAdvProductads.setOpttime(new Date());
				JSONObject extendedData = item.getJSONObject("extendedData");
				if(extendedData!=null) {
					String servingStatus=extendedData.getString("servingStatus");
					amzAdvProductads.setServingStatus(servingStatus);
					Date creationDateTime=extendedData.getDate("creationDateTime");
					amzAdvProductads.setCreationDate(creationDateTime);
					Date lastUpdateDateTime=extendedData.getDate("lastUpdateDateTime");
					amzAdvProductads.setLastUpdatedDate(lastUpdateDateTime);
				}
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
	 


	
	public List<AmzAdvProductads> getProductadByAdgroupId(UserInfo user, BigInteger profileid, String adgroupid) {
		Example example =new Example(AmzAdvProductads.class);
		Criteria crit = example.createCriteria();
		crit.andEqualTo("adgroupid",adgroupid);
		crit.andEqualTo("profileid",profileid);
		return amzAdvProductadsMapper.selectByExample(example);
	}
	public void setListInfo(PageList<Map<String, Object>> list) {
		if(list!=null&&list.size()>0) {
			Map<String,Object> param=new HashMap<String,Object>();
			String sellerid=null;
			String marketplaceid=null;
			List<String> skulist=new ArrayList<String>();
			for(Map<String, Object> item:list) {
				if(sellerid==null) {
					sellerid=item.get("sellerId").toString();
				}
				if(marketplaceid==null) {
					marketplaceid=item.get("marketplaceid").toString();
				}
				if(item.get("sku")!=null) {
					skulist.add(item.get("sku").toString());
				}
				
			}
			param.put("sellerId", sellerid);
			param.put("marketplaceid", marketplaceid);
			param.put("skulist", skulist);
			List<Map<String, Object>> listinfo = amazonClientOneFeignManager.getProductInfoSimple(param);
			if(listinfo!=null) {
				Map<String, Map<String,Object>> infomap=new HashMap<String, Map<String,Object>>();
				for(Map<String,Object> item:listinfo) {
					infomap.put(item.get("sku").toString(), item);
				}
				for(Map<String, Object> item:list) {
					String sku=item.get("sku").toString();
					if(infomap.get(sku)!=null) {
						item.putAll(infomap.get(sku));
					}
				}
			}
			
		}
	}
	public PageList<Map<String, Object>> getProductAdList(Map<String, Object> map, PageBounds pageBounds){
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("HSA".equals(campaignType)) {
				return null;
			}if("SP".equals(campaignType.toUpperCase())) {
				PageList<Map<String, Object>> list = amzAdvProductadsMapper.getProductAdList(map, pageBounds);
				setListInfo(list);
				return list;
				
			}
			if("SD".equals(campaignType.toUpperCase())) {
				PageList<Map<String, Object>> list = amzAdvProductAdsSDService.getProductAdList(map, pageBounds);
				setListInfo(list);
				return list;
			}
		}
		return null;
		
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


	
	
	public List<AmzAdvProductads> convertJsonToBean(UserInfo user, String profileId, JSONArray productAdArray) {
		// TODO Auto-generated method stub
		 List<AmzAdvProductads>list=new ArrayList<AmzAdvProductads>();
		for (int i = 0; i < productAdArray.size(); i++) {
			JSONObject productAd = productAdArray.getJSONObject(i);
			String state = productAd.getString("state");
			String sku = productAd.getString("sku");
			String asin = productAd.getString("asin");
			BigInteger campaignid = productAd.getBigInteger("campaignid");
			BigInteger adGroupid = productAd.getBigInteger("adgroupid");
			BigInteger adid = productAd.getBigInteger("adid");
			AmzAdvProductads amzAdvProductads = new AmzAdvProductads();
			amzAdvProductads.setAdid(adid);
			amzAdvProductads.setCampaignid(campaignid);
			amzAdvProductads.setAdgroupid(adGroupid);
			amzAdvProductads.setProfileid(new BigInteger(profileId));
			amzAdvProductads.setSku(sku);
			amzAdvProductads.setAsin(asin);
			amzAdvProductads.setState(state);
			amzAdvProductads.setOpttime(new Date());
			list.add(amzAdvProductads);
		}
		return list;
	}
	
	@Override
	public List<AmzAdvProductads> amzUpdateProductAds(UserInfo user, String profileId, JSONArray productst) {
		// TODO Auto-generated method stub
		return this.amzUpdateProductAds(user, new BigInteger(profileId),this.convertJsonToBean(user, profileId, productst));
	}

	@Override
	public List<AmzAdvProductads> amzCreateProductAds(UserInfo user, String profileid, JSONArray productAdArray) {
		// TODO Auto-generated method stub
		return this.amzCreateProductAds(user, new BigInteger(profileid),this.convertJsonToBean(user, profileid, productAdArray));
	}
	 
	
}
