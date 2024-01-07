package com.wimoor.amazon.adv.common.service.impl;

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
import com.wimoor.amazon.adv.common.dao.AmzAdvAssetsMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAssets;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAssetsService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.user.UserInfo;
 
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvAssetsService")
public class AmzAdvAssetsServiceImpl extends BaseService<AmzAdvAssets> implements IAmzAdvAssetsService{
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvAssetsMapper amzAdvAssetsMapper;
	@Resource
	ApiBuildService apiBuildService;
	
	public String amzAssetsUpload(UserInfo user,BigInteger profileId, JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/assets/upload";
		Map<String, String> header=new HashMap<String,String>();
		header.put("Content-Type", "text/plain");
		String response = apiBuildService.amzAdvPost(profile, url,param.toString(),header);
		return response;
	}
	
	public String amzAssetsSearch(UserInfo user,BigInteger profileId, JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/assets/search";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString());
		return response;
	}
	
	public String amzAssetsRegister(UserInfo user,BigInteger profileId, JSONObject param) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/assets/register";
		String response = apiBuildService.amzAdvPost(profile, url,param.toString());
		return response;
	}
	
	
	public String amzAssets(UserInfo user,BigInteger profileId) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/assets";
		String response = apiBuildService.amzAdvGet(profile, url);
		return response;
	}
	

	public List<AmzAdvAssets> getAssetsForBrandEntityId(BigInteger profileId, String brandEntityId) {
		Example example = new Example(AmzAdvAssets.class);
		Criteria cri = example.createCriteria();
		cri.andEqualTo("profileid", profileId);
		cri.andEqualTo("brandentityid", brandEntityId);
		return amzAdvAssetsMapper.selectByExample(example);
	}
	
	public List<AmzAdvAssets> amzGetStoresAssets(UserInfo user,BigInteger profileId, String brandEntityId, String mediaType) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/stores/assets/?brandEntityId=" + brandEntityId + "&mediaType=" + mediaType;
		String response = apiBuildService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONArray itemArray = GeneralUtil.getJsonArray(response.toString());
			List<AmzAdvAssets> list = new ArrayList<AmzAdvAssets>();
			if(itemArray != null && itemArray.size() > 0) {
				for (int i = 0; i < itemArray.size(); i++) {
					AmzAdvAssets amzAdvAssets = new AmzAdvAssets();
					JSONObject item = itemArray.getJSONObject(i);
					String assetId = item.getString("assetId");
					String url2 = item.getString("url");
					String mediaType2 = item.getString("mediaType");
					String name = item.getString("name");
					amzAdvAssets.setAssetid(assetId);
					amzAdvAssets.setBrandentityid(brandEntityId);
					amzAdvAssets.setProfileid(profileId);
					amzAdvAssets.setUrl(url2);
					amzAdvAssets.setMediatype(mediaType2);
					amzAdvAssets.setName(name);
					amzAdvAssets.setOpptime(new Date());
					list.add(amzAdvAssets);
				}
				if(list.size() > 0) {
					amzAdvAssetsMapper.insertBatch(list);
				}
			}
			return list;
		}
		return null;
	}
	 

	
	
	public AmzAdvAssets amzCreateStoresAssets(UserInfo user, BigInteger profileId, Map<String, Object> assetParams) {
		if(assetParams == null) {
			return null;
		}
		String brandEntityId = assetParams.get("brandEntityId").toString();
		String mediaType = assetParams.get("mediaType").toString();
		String name = assetParams.get("name").toString();
		String imageType = assetParams.get("imageType").toString();
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/stores/assets";
		Map<String, Object> param = new HashMap<String, Object>();
		JSONObject myjson = new JSONObject();
		myjson.put("brandEntityId", brandEntityId);
		myjson.put("mediaType", mediaType);
		param.put("assetInfo", myjson);
		param.put("asset", assetParams.get("asset"));
		param.put("imageType", imageType);
		param.put("name", name.split("."+imageType.toLowerCase())[0]);
		param.put("fullname", name);
		String response = apiBuildService.amzAdvAssetsPost(profile, url, param);
		if (StringUtil.isNotEmpty(response)) {
			JSONObject item = GeneralUtil.getJsonObject(response.toString());
			String errormsg = "";
			if ("SUCCESS".equals(item.getString("code"))) {
				amzGetStoresAssets(user,profileId, brandEntityId, mediaType);
				String id = item.getString("assetId");
				Example example = new Example(AmzAdvAssets.class);
				Criteria cri = example.createCriteria();
				cri.andEqualTo("assetid", id);
				cri.andEqualTo("profileid", profileId);
				cri.andEqualTo("brandentityid", brandEntityId);
				return amzAdvAssetsMapper.selectOneByExample(example);
			}else {
				errormsg = errormsg.equals("") ? "" : errormsg + ",";
				errormsg = errormsg + item.getString("description");
				BaseException exception = new BaseException("图片创建失败："+errormsg);
				exception.setCode("ERROR");
				throw exception;
			}
		}
		return null;
	}
}
