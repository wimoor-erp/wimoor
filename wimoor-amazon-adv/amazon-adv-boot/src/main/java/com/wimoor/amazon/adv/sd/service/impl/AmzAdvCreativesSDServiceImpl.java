package com.wimoor.amazon.adv.sd.service.impl;

import java.math.BigInteger;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.sd.service.IAmzAdvCreativesSDService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
@Service("amzAdvCreativesSDService")
public class AmzAdvCreativesSDServiceImpl implements IAmzAdvCreativesSDService{
	
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	ApiBuildService apiBuildService;
	 
	
	public String getListOfCreatives(UserInfo user,BigInteger profileId,JSONObject myParam) {
			AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
			String url = "/sd/creatives"; 
			String param="";
			if (myParam != null) {
				param = GeneralUtil.addParamToUrl2(param, myParam, "startIndex");
				param = GeneralUtil.addParamToUrl2(param, myParam, "count");
				param = GeneralUtil.addParamToUrl2(param, myParam, "adGroupIdFilter");
				param = GeneralUtil.addParamToUrl2(param, myParam, "creativeIdFilter");
			}
			url = url+"?" + ("".equals(param) ? "" : param);
			String response = apiBuildService.amzAdvGet(profile, url);
	        return response;
	}
	 
	
	public String updatesCreatives(UserInfo user,BigInteger profileId,JSONObject myParam) {
		String creativeId=myParam.getString("creativeId");
		myParam.put("creativeId", new BigInteger(creativeId));
		JSONArray array=new JSONArray();
		array.add(myParam);
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/creatives"; 
		String response = apiBuildService.amzAdvPut(profile, url, array.toString());
        return response;
    }
	
	public String requestCreatives(UserInfo user,BigInteger profileId,JSONObject myParam) {
		JSONArray array=new JSONArray();
		array.add(myParam);
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/creatives"; 
		String response = apiBuildService.amzAdvPost(profile, url, array.toString());
        return response;
    }
	 
	public String getCreativePreviewHTML(UserInfo user,BigInteger profileId,JSONObject myParam) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/creatives/preview"; 
		String response = apiBuildService.amzAdvPost(profile, url, myParam.toString());
        return response;
    }
	public String getListCreativeModerations(UserInfo user,BigInteger profileId,JSONObject myParam) {
		AmzAdvProfile profile = amzAdvAuthService.getAmzAdvProfileByKey(profileId);
		String url = "/sd/moderation/creatives"; 
		String param="";
		if (myParam != null) {
			param = GeneralUtil.addParamToUrl2(param, myParam, "language");
			param = GeneralUtil.addParamToUrl2(param, myParam, "startIndex");
			param = GeneralUtil.addParamToUrl2(param, myParam, "count");
			param = GeneralUtil.addParamToUrl2(param, myParam, "adGroupIdFilter");
			param = GeneralUtil.addParamToUrl2(param, myParam, "creativeIdFilter");
		}
		url = url + ("".equals(param) ? "" : param);
		String response = apiBuildService.amzAdvGet(profile, url);
        return response;
    }
	
}
