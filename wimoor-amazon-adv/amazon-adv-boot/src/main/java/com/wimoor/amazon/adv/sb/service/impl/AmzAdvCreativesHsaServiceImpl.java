package com.wimoor.amazon.adv.sb.service.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.sb.service.IAmzAdvCreativesHsaService;
import com.wimoor.common.user.UserInfo;
@Service("amzAdvCreativesHsaService")
public class AmzAdvCreativesHsaServiceImpl implements IAmzAdvCreativesHsaService{
	
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	ApiBuildService apiBuildService;
	 
	private Map<String,String> getHeader() {
		// TODO Auto-generated method stub
		 Map<String,String> header=new HashMap<String,String>();
	    	header.put("Content-Type", "application/vnd.sbAdCreativeResource.v4+json");
	    	header.put("Accept",       "application/vnd.sbAdCreativeResource.v4+json");
		return header;
		
	}


	public String updatesCreatives(UserInfo user,BigInteger profileId,JSONObject myParam) {
		String creativeId=myParam.getString("creativeId");
		myParam.put("creativeId", new BigInteger(creativeId));
		JSONArray array=new JSONArray();
		array.add(myParam);
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/creatives"; 
		String response = apiBuildService.amzAdvPut(profile, url, array.toString(),this.getHeader());
        return response;
    }
	
	public String createBrandVideo(UserInfo user,BigInteger profileId,JSONObject myParam) {
		JSONArray array=new JSONArray();
		array.add(myParam);
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/ads/creatives/brandVideo"; 
		String response = apiBuildService.amzAdvPost(profile, url, array.toString(),this.getHeader());
        return response;
    }
	
	public String createVideo(UserInfo user,BigInteger profileId,JSONObject myParam) {
		JSONArray array=new JSONArray();
		array.add(myParam);
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/ads/creatives/video"; 
		String response = apiBuildService.amzAdvPost(profile, url, array.toString(),this.getHeader());
        return response;
    }
	 
	public String createProductCollection(UserInfo user,BigInteger profileId,JSONObject myParam) {
		JSONArray array=new JSONArray();
		array.add(myParam);
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/ads/creatives/productCollection"; 
		String response = apiBuildService.amzAdvPost(profile, url, array.toString(),this.getHeader());
        return response;
    }
	public String createStoreSpotlight(UserInfo user,BigInteger profileId,JSONObject myParam) {
		JSONArray array=new JSONArray();
		array.add(myParam);
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/ads/creatives/storeSpotlight"; 
		String response = apiBuildService.amzAdvPost(profile, url, array.toString(),this.getHeader());
        return response;
    }
	
	public String getListOfCreatives(UserInfo user,BigInteger profileId,JSONObject myParam) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sb/ads/creatives/list"; 
		String response = apiBuildService.amzAdvPost(profile, url,myParam.toJSONString(),this.getHeader());
        return response;
}
	
}
