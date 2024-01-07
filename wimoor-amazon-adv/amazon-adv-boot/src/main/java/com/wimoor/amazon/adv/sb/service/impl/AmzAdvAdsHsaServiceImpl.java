package com.wimoor.amazon.adv.sb.service.impl;

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
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvAdsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdsHsa;
import com.wimoor.amazon.adv.sb.service.IAmzAdvAdsHsaService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.util.StringUtil;
@Service("amzAdvAdsHsaService")
public class AmzAdvAdsHsaServiceImpl  extends BaseService<AmzAdvAdsHsa> implements IAmzAdvAdsHsaService {
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	ApiBuildService apiBuildService;
	@Resource
	AmzAdvAdsHsaMapper amzAdvAdsHsaMapper;
	Map<String,String> getHeaderExt(){
		Map<String,String> header=new HashMap<String,String>();
	 	header.put("Content-Type", "application/vnd.sbadresource.v4+json");
    	header.put("Accept",       "application/vnd.sbadresource.v4+json");
		return header;
	}
	@Override
	public List<AmzAdvAdsHsa> createAds(UserInfo user, String profileid, String adsType, JSONObject params) {
		// TODO Auto-generated method stub
		   String url="";
		   if(adsType.equals("brandVideo")) {
		    	url="/sb/v4/ads/brandVideo";
		    }else if(adsType.equals("video")) {
		    	url="/sb/v4/ads/video";
		    }else if(adsType.equals("productCollection")) {
		    	url="/sb/v4/ads/productCollection";
		    }else {
		    	url="/sb/v4/ads/storeSpotlight";
		    }
		   AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
		   String response = apiBuildService.amzAdvPost(profile, url, params.toString(),this.getHeaderExt());
		   List<AmzAdvAdsHsa> result=new LinkedList<AmzAdvAdsHsa>();
		   if (StringUtil.isNotEmpty(response)) {
				String errormsg = "";
				JSONObject json = GeneralUtil.getJsonObject(response.toString());
				JSONObject campaignsJson = json.getJSONObject("ads");
				JSONArray success = campaignsJson.getJSONArray("success");
				JSONArray error = campaignsJson.getJSONArray("error");
				for(int i=0;i<success.size();i++) {
					JSONObject item=success.getJSONObject(i);
					JSONObject aditem=item.getJSONObject("ad");
					AmzAdvAdsHsa adsobj=new AmzAdvAdsHsa();
					adsobj.setAdid(aditem.getBigInteger("adId"));
					adsobj.setCampaignid(aditem.getBigInteger("campaignId"));
					adsobj.setAdgroupid(aditem.getBigInteger("adGroupId"));
					adsobj.setName(aditem.getString("name"));
					adsobj.setState(aditem.getString("state"));
					adsobj.setProfileid(profile.getId());
					if(adsobj.getAdid()==null) {
						if(adsobj.getAdgroupid()!=null) {
							adsobj.setAdid(adsobj.getAdgroupid());
						}else {
							adsobj.setAdid(adsobj.getCampaignid());
							adsobj.setAdgroupid(adsobj.getCampaignid());
						}
					}
					adsobj.setCreative(aditem.getString("creative"));
					adsobj.setLandingPage(aditem.getString("landingPage"));
					adsobj.setOpttime(new Date());
					this.amzAdvAdsHsaMapper.insert(adsobj);
					result.add(adsobj);
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
		   }
		return result;
	}

	@Override
	public List<AmzAdvAdsHsa> updateAds(UserInfo user, String profileid, JSONObject param) {
		// TODO Auto-generated method stub
		 AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
		 String url="/sb/v4/ads";
		   String response = apiBuildService.amzAdvPut(profile, url, param.toString(),this.getHeaderExt());
		   List<AmzAdvAdsHsa> result=new LinkedList<AmzAdvAdsHsa>();
		   if (StringUtil.isNotEmpty(response)) {
				String errormsg = "";
				JSONObject json = GeneralUtil.getJsonObject(response.toString());
				JSONObject campaignsJson = json.getJSONObject("ads");
				JSONArray success = campaignsJson.getJSONArray("success");
				JSONArray error = campaignsJson.getJSONArray("error");
				for(int i=0;i<success.size();i++) {
					JSONObject item=success.getJSONObject(i);
					JSONObject aditem=item.getJSONObject("ad");
					AmzAdvAdsHsa adsobj=new AmzAdvAdsHsa();
					adsobj.setAdid(aditem.getBigInteger("adId"));
					adsobj.setCampaignid(aditem.getBigInteger("campaignId"));
					adsobj.setAdgroupid(aditem.getBigInteger("adGroupId"));
					adsobj.setName(aditem.getString("name"));
					adsobj.setProfileid(profile.getId());
					if(adsobj.getAdid()==null) {
						if(adsobj.getAdgroupid()!=null) {
							adsobj.setAdid(adsobj.getAdgroupid());
						}else {
							adsobj.setAdid(adsobj.getCampaignid());
							adsobj.setAdgroupid(adsobj.getCampaignid());
						}
					}
					adsobj.setState(aditem.getString("state"));
					adsobj.setCreative(aditem.getString("creative"));
					adsobj.setLandingPage(aditem.getString("landingPage"));
					adsobj.setOpttime(new Date());
					this.amzAdvAdsHsaMapper.updateByPrimaryKey(adsobj);
					result.add(adsobj);
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
		   }
		return result;
	}

	public List<AmzAdvAdsHsa> amzGetAdsList(AmzAdvProfile profile, JSONObject adgroupParam) {
		// TODO Auto-generated method stub
		String url = "/sb/v4/ads/list";
		String response = apiBuildService.amzAdvPost(profile, url,adgroupParam.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject result = GeneralUtil.getJsonObject(response.toString());
			adgroupParam.put("nextToken",result.getString("nextToken"));
			JSONArray a=result.getJSONArray("ads");
			List<AmzAdvAdsHsa> list = new ArrayList<AmzAdvAdsHsa>();
			for (int i = 0; i < a.size(); i++) {
				JSONObject aditem = a.getJSONObject(i);
				AmzAdvAdsHsa adsobj=new AmzAdvAdsHsa();
				adsobj.setAdid(aditem.getBigInteger("adId"));
				adsobj.setCampaignid(aditem.getBigInteger("campaignId"));
				adsobj.setAdgroupid(aditem.getBigInteger("adGroupId"));
				if(adsobj.getAdid()==null) {
					if(adsobj.getAdgroupid()!=null) {
						adsobj.setAdid(adsobj.getAdgroupid());
					}else {
						adsobj.setAdid(adsobj.getCampaignid());
						adsobj.setAdgroupid(adsobj.getCampaignid());
					}
				}
				adsobj.setName(aditem.getString("name"));
				adsobj.setProfileid(profile.getId());
				adsobj.setState(aditem.getString("state"));
				adsobj.setCreative(aditem.getString("creative"));
				adsobj.setLandingPage(aditem.getString("landingPage"));
				adsobj.setOpttime(new Date());
				list.add(adsobj);
			}
			if(list!=null&&list.size()>0) {
				amzAdvAdsHsaMapper.insertBatch(list);
			}
			return list;
		}else {
			adgroupParam.put("nextToken",null);
		}
		return null;
	}
	
	
	public List<AmzAdvAdsHsa> amzDeleteAdsList(UserInfo user, AmzAdvProfile profile, JSONObject adgroupParam) {
		// TODO Auto-generated method stub
		String url = "/sb/v4/ads/delete";
		String response = apiBuildService.amzAdvPost(profile, url,adgroupParam.toString(),this.getHeaderExt());
		if (StringUtil.isNotEmpty(response)) {
			JSONObject result = GeneralUtil.getJsonObject(response.toString());
			adgroupParam.put("nextToken",result.getString("nextToken"));
			JSONArray a=result.getJSONArray("ads");
			List<AmzAdvAdsHsa> list = new ArrayList<AmzAdvAdsHsa>();
			for (int i = 0; i < a.size(); i++) {
				JSONObject aditem = a.getJSONObject(i);
				AmzAdvAdsHsa adsobj=new AmzAdvAdsHsa();
				adsobj.setAdid(aditem.getBigInteger("adId"));
				adsobj.setCampaignid(aditem.getBigInteger("campaignId"));
				adsobj.setAdgroupid(aditem.getBigInteger("adGroupId"));
				adsobj.setName(aditem.getString("name"));
				adsobj.setState(aditem.getString("state"));
				adsobj.setCreative(aditem.getString("creative"));
				adsobj.setLandingPage(aditem.getString("landingPage"));
				adsobj.setOpttime(new Date());
				AmzAdvAdsHsa oldAds = amzAdvAdsHsaMapper.selectByPrimaryKey(adsobj);
				if (oldAds != null) {
					amzAdvAdsHsaMapper.updateByPrimaryKey(adsobj);
				} else {
					amzAdvAdsHsaMapper.insert(adsobj);
				}
				list.add(adsobj);
			}
			return list;
		}else {
			adgroupParam.put("nextToken",null);
		}
		return null;
	}
	@Override
	public PageList<Map<String, Object>> getAdsList(Map<String, Object> map, PageBounds pageBounds) {
		// TODO Auto-generated method stub
		return amzAdvAdsHsaMapper.getAdsList(map,pageBounds);
	}
	@Override
	public Map<String, Object> getSumAds(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return amzAdvAdsHsaMapper.getSumAds(map);
	}
	
	@Override
	public List<Map<String, Object>> getAdsChart(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.amzAdvAdsHsaMapper.getAdsChart(map);
	}
}
