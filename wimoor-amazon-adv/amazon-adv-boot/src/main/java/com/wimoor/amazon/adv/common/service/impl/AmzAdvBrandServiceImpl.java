package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.dao.AmzAdvBrandMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvBrand;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvBrandService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvSBMediaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvSBMedia;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvBrandService")
public class AmzAdvBrandServiceImpl extends BaseService<AmzAdvBrand> implements IAmzAdvBrandService{

	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	AmzAdvBrandMapper amzAdvBrandMapper;
	@Resource
	AmzAdvSBMediaMapper amzAdvSBMediaMapper;
	@Resource
	ApiBuildService apiBuildService;
	public List<AmzAdvBrand> getBrandForProfileId(BigInteger profileId, String brandEntityId){
		Example example = new Example(AmzAdvBrand.class);
		Criteria cri = example.createCriteria();
		cri.andEqualTo("profileid", profileId);
		if(StringUtil.isNotEmpty(brandEntityId)) {
			cri.andEqualTo("brandentityid", brandEntityId);
		}
		List<AmzAdvBrand> list = amzAdvBrandMapper.selectByExample(example);
		return list;
	}
	
	public List<AmzAdvBrand> amzGetBrands(UserInfo user,BigInteger profileId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/brands";
		String response = apiBuildService.amzAdvGet(profile, url);
		if (StringUtil.isNotEmpty(response)) {
			JSONArray items = GeneralUtil.getJsonArray(response.toString());
			List<AmzAdvBrand> list = new ArrayList<AmzAdvBrand>();
			if(items != null && items.size() > 0) {
				for(int i =0; i < items.size(); i++) {
					JSONObject item = items.getJSONObject(i);
					if(item == null) continue;
					String brandId = item.getString("brandId");
					String brandEntityId = item.getString("brandEntityId");
					String brandRegistryName = item.getString("brandRegistryName");
					AmzAdvBrand amzAdvBrand = new AmzAdvBrand();
					amzAdvBrand.setProfileid(profileId);
					amzAdvBrand.setBrandid(brandId);
					amzAdvBrand.setBrandentityid(brandEntityId);
					amzAdvBrand.setBrandregistryname(brandRegistryName);
					amzAdvBrand.setOpttime(new Date());
					list.add(amzAdvBrand);
				}
				if(list.size() > 0) {
					amzAdvBrandMapper.insertBatch(list);
				}
			}
			return list;
		}
		return null;
	}

	@Override
	public  JSONObject getMediaUrl(BigInteger profileId) {
		// TODO Auto-generated method stub
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/media/upload";
	    JSONObject param = new JSONObject();
	    param.put("programType", "SponsoredBrands");
	    param.put("creativeType", "Video");
	    String response= apiBuildService.amzAdvPost(profile, url, param.toString());
		if(StringUtils.isNoneBlank(response)) {
			return GeneralUtil.getJsonObject(response);
		}
		return null;
	}

	@Override
	public JSONObject completeMedia(UserInfo user, Map<String, String> param) {
		// TODO Auto-generated method stub
		String uploadLocation=param.get("uploadLocation");
		String profileId=param.get("profileId");
		String version=param.get("version");
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileId));
		String url = "/media/complete";
	    JSONObject paramjson = new JSONObject();
	    paramjson.put("uploadLocation", uploadLocation);
	    paramjson.put("version",version);
	    if(StringUtil.isEmpty(version)) {
	    	throw new BaseException("上传失败，未能正确获取到上传视频的版本");
	    }
	    String response= apiBuildService.amzAdvPut(profile, url, paramjson.toString());
		if(StringUtils.isNoneBlank(response)) {
			JSONObject jsonResult = GeneralUtil.getJsonObject(response);
			String mediaId = jsonResult.getString("mediaId");
			String url2="/media/describe?mediaId="+mediaId;
		    String response2= apiBuildService.amzAdvGet(profile, url2);
		    JSONObject jsonResult2 = GeneralUtil.getJsonObject(response2);
		    //String mediaId2=jsonResult2.getString("mediaId");
		    String status=jsonResult2.getString("status");
		    String statusMetadata=jsonResult2.getString("statusMetadata");
		    String publishedMediaUrl=jsonResult2.getString("publishedMediaUrl");
		    AmzAdvSBMedia sbmedia=new AmzAdvSBMedia();
		    sbmedia.setMediaid(mediaId);
		    sbmedia.setPublishedmediaurl(publishedMediaUrl);
		    sbmedia.setStatus(status);
		    sbmedia.setStatusmetadata(statusMetadata);
		    sbmedia.setProfileid(new BigInteger(profileId));
		    sbmedia.setOperator(user.getId());
		    sbmedia.setOpttime(new Date());
		    amzAdvSBMediaMapper.insert(sbmedia);
		    return jsonResult2;
		}
		return null;
	}

	@Override
	public JSONObject mediaDescribe(UserInfo user, Map<String, String> param) {
		// TODO Auto-generated method stub
		String profileId=param.get("profileId");
		String mediaId=param.get("mediaId");
		if(StringUtils.isEmpty(mediaId)) {
			throw new BaseException("没有找到对应的MediaID");
		}
		String url="/media/describe?mediaId="+mediaId;
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileId));
	    String response= apiBuildService.amzAdvGet(profile, url);
	    JSONObject jsonResult = GeneralUtil.getJsonObject(response);
	    if(jsonResult==null)return null;
	    String status=jsonResult.getString("status");
	    String statusMetadata=jsonResult.getString("statusMetadata");
	    String publishedMediaUrl=jsonResult.getString("publishedMediaUrl");
	    AmzAdvSBMedia sbmedia=new AmzAdvSBMedia();
	    sbmedia.setMediaid(mediaId);
	    sbmedia.setPublishedmediaurl(publishedMediaUrl);
	    sbmedia.setStatus(status);
	    sbmedia.setStatusmetadata(statusMetadata);
	    sbmedia.setProfileid(new BigInteger(profileId));
	    sbmedia.setOperator(user.getId());
	    sbmedia.setOpttime(new Date());
	    AmzAdvSBMedia oldsbmedia = amzAdvSBMediaMapper.selectByPrimaryKey(mediaId);
	    if(oldsbmedia!=null) {
	    	amzAdvSBMediaMapper.updateByPrimaryKey(sbmedia);
	    }else {
	    	amzAdvSBMediaMapper.insert(sbmedia);
	    }
		return jsonResult;
	}

	@Override
	public AmzAdvSBMedia loadOldMedia(UserInfo user, Map<String, String> param) {
		// TODO Auto-generated method stub
		String profileId=param.get("profileId");
		AmzAdvSBMedia oldone =amzAdvSBMediaMapper.loadOldMedia(profileId);
		if(oldone!=null&&oldone.getCampaignid()==null) {
			return oldone;
		}else {
			return null;
		}
	}
}
