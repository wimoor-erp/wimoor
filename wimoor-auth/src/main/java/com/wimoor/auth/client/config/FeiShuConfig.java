package com.wimoor.auth.client.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.auth.client.pojo.AppUserInfo;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Component
@Data
public class FeiShuConfig {
	@Value("${feishu.app-secret}")
	String appSecret;
	@Value("${feishu.app-id}")
	String appID;
	
	public String getInternalToken() {
		String urlToken="https://open.feishu.cn/open-apis/auth/v3/app_access_token/internal";
		Map<String, String> paramToken=new HashMap<String,String>(); 
		Map<String, String> headerToken=new HashMap<String,String>();
		headerToken.put("Content-Type", "application/json; charset=utf-8");
		paramToken.put("app_id",appID);
		paramToken.put("app_secret",appSecret);
		try {
			 String json = HttpClientUtil.postUrl(urlToken, paramToken,headerToken,"application/json");
			 if(StrUtil.isNotEmpty(json)) {
				 JSONObject jsonObject = GeneralUtil.getJsonObject(json);
				 if(jsonObject!=null) {
					 return jsonObject.getString("app_access_token");
				 }
			 }
			 
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public AppUserInfo getLoginInfo(String code) {
		String token=getInternalToken();
		if(token==null)return null;
		String urlOpenId="https://open.feishu.cn/open-apis/mina/v2/tokenLoginValidate";
		Map<String, String> paramOpenID=new HashMap<String,String>(); 
		Map<String, String> headerOpenID=new HashMap<String,String>(); 
		headerOpenID.put("Content-Type", "application/json; charset=utf-8");
		headerOpenID.put("Authorization", "Bearer "+token);
		paramOpenID.put("code", code);
		try {
			 String json = HttpClientUtil.postUrl(urlOpenId, paramOpenID,headerOpenID,"application/json");
			 if(StrUtil.isNotEmpty(json)) {
				 JSONObject jsonObject = GeneralUtil.getJsonObject(json);
				 if(jsonObject!=null) {
					  JSONObject data = jsonObject.getJSONObject("data");
					  AppUserInfo info =new AppUserInfo();
					  info.setAppType("feishuapp");
					  info.setAccessToken(data.getString("access_token"));
					  info.setOpenId(data.getString("open_id"));
					  info.setEmployeeId(data.getString("employee_id"));
					  info.setUserId(data.getString("user_id"));
					  info.setSessionKey(data.getString("session_key"));
					  info.setRefreshToken(data.getString("refresh_token"));
					  info.setTenantKey(data.getString("tenant_key"));
					  info.setExpiresIn(data.getInteger("expires_in"));
					  return info;
				 }
			 }
			 
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	 
}
