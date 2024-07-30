package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.dao.AmzAdvStoresMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.AmzAdvStores;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvStoresService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvStoresService")
public class AmzAdvStoresServiceImpl extends BaseService<AmzAdvStores> implements IAmzAdvStoresService{
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvStoresMapper amzAdvStoresMapper;
	@Resource
	ApiBuildService apiBuildService;
	public List<AmzAdvStores> getStoresForProfileId(BigInteger profileId,String entityid) {
		Example example = new Example(AmzAdvStores.class);
		Criteria cri = example.createCriteria();
		cri.andEqualTo("profileid", profileId);
		cri.andEqualTo("entityid", entityid);
		List<AmzAdvStores> list = this.selectByExample(example);
		return list;
	}
	
	public List<AmzAdvStores> amzGetlistStores(UserInfo user,BigInteger profileId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/stores";
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONArray itemArray = GeneralUtil.getJsonArray(response.toString());
			if(itemArray != null && itemArray.size() > 0) {
				List<AmzAdvStores> list = new ArrayList<AmzAdvStores>();
				for(int j = 0;j < itemArray.size(); j++) {
					JSONObject item = itemArray.getJSONObject(j);
					if(item == null) continue;
					String code = item.getString("code");
					if("SUCCESS".equals(code)) {
						String entityId = item.getString("entityId");
						String storeName = item.getString("storeName");
						String brandEntityId = item.getString("brandEntityId");
						JSONArray storePageInfo = item.getJSONArray("storePageInfo");
						for(int i = 0;i < storePageInfo.size();i++) {
							JSONObject storePage = storePageInfo.getJSONObject(i);
							AmzAdvStores amzAdvStores = new AmzAdvStores();
							amzAdvStores.setEntityid(entityId);
							amzAdvStores.setStorename(storeName);
							amzAdvStores.setProfileid(profileId);
							amzAdvStores.setBrandentityid(brandEntityId);
							amzAdvStores.setOpptime(new Date());
							if(storePage != null) {
								amzAdvStores.setStorepageid(storePage.getString("storePageId"));
								amzAdvStores.setStorepagename(storePage.getString("storePageName"));
								amzAdvStores.setStorepageurl(storePage.getString("storePageUrl"));
							}
							list.add(amzAdvStores);
						}
						if(list.size() > 0) {
							amzAdvStoresMapper.insertBatch(list);
						}
						return list;
					}
				}
			}
		}
		return null;
	}

	public List<AmzAdvStores> amzGetStoresBybrandEntityId(UserInfo user,BigInteger profileId, String brandEntityId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/stores/" + brandEntityId;
		String response = apiBuildService.amzAdvGet_V2(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONArray itemArray = GeneralUtil.getJsonArray(response.toString());
			if(itemArray != null && itemArray.size() > 0) {
				List<AmzAdvStores> list = new ArrayList<AmzAdvStores>();
				for(int j = 0;j < itemArray.size(); j++) {
					JSONObject item = itemArray.getJSONObject(j);
					if(item == null) continue;
					String code = item.getString("code");
					if("SUCCESS".equals(code)) {
						String entityId = item.getString("entityId");
						String storeName = item.getString("storeName");
						JSONArray storePageInfo = item.getJSONArray("storePageInfo");
						for(int i = 0;i < storePageInfo.size();i++) {
							JSONObject storePage = storePageInfo.getJSONObject(i);
							AmzAdvStores amzAdvStores = new AmzAdvStores();
							amzAdvStores.setEntityid(entityId);
							amzAdvStores.setStorename(storeName);
							amzAdvStores.setProfileid(profileId);
							amzAdvStores.setBrandentityid(brandEntityId);
							amzAdvStores.setOpptime(new Date());
							if(storePage != null) {
								amzAdvStores.setStorepageid(storePage.getString("storePageId"));
								amzAdvStores.setStorepagename(storePage.getString("storePageName"));
								amzAdvStores.setStorepageurl(storePage.getString("storePageUrl"));
							}
							list.add(amzAdvStores);
						}
						if(list.size() > 0) {
							amzAdvStoresMapper.insertBatch(list);
						}
						return list;
					}
				}
			}
		}
		return null;
	}
	
	public List<String> amzGetPageAsins(UserInfo user,BigInteger profileId, String storePageUrl) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/pageAsins?pageUrl=" + storePageUrl;
		String response = apiBuildService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			if(item == null) return null;
			List<String> list = new ArrayList<String>();
			String errormsg = "";
			String code = item.getString("code");
			if("SUCCESS".equals(code)) {
				JSONArray items = item.getJSONArray("asinList");
				for(int i = 0; i < items.size(); i++) {
					list.add(items.getString(i));
				}
			}else {
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + item.getString("description");
				BaseException exception = new BaseException("广告活动创建失败："+errormsg);
				exception.setCode("ERROR");
				throw exception;
			}
			return list;
		}
		return null;
	}
	
}
