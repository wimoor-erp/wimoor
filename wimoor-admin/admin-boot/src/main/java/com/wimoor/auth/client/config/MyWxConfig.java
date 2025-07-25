package com.wimoor.auth.client.config;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.wimoor.common.mvc.BizException;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayConfig;
import com.wimoor.auth.client.pojo.AppUserInfo;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;

import cn.hutool.core.util.StrUtil;

@Component
public class MyWxConfig implements WXPayConfig{
	
	@Value("${wechat.small-app-id}")
	String smallAppId;
	
	@Value("${wechat.small-app-secret}")
	String smallAppSecret;
	
	@Value("${wechat.open-app-id}")
	String openAppId;
	
	@Value("${wechat.open-app-secret}")
	String openAppSecret;
	
	@Value("${wechat.mch-id}")
	String mchId;
	
	@Value("${wechat.key}")
	String key;
	
	@Value("${wechat.templateurl}")
	String templateurl;
	
	public String getAppID() {
		return openAppId;
	}

	public String getMchID() {
		return mchId;
	}

	public String getKey() {
		return key;
	}
	
	public String getAppSecret() {
		return openAppSecret;
	}
	
	public String getScope() {
		return "snsapi_userinfo";
	}
	
	public String getSmallAppID() {
		return smallAppId;
	}
	
	public String getSmallAppSecret() {
		return smallAppSecret;
	}
	
	//微信模板接口
	public String getSendTemplateUrl() {
		return templateurl;
	}

	public InputStream getCertStream() {
		
		File file=new File(this.getClass().getClassLoader().getResource("cert/apiclient_cert.p12").getPath());
		try {
			FileInputStream stream=new FileInputStream(file);
			return ConvertInputStream(stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getHttpConnectTimeoutMs() {
		return 6*1000;
	}

	public int getHttpReadTimeoutMs() {
		return 8*1000;
	}
	
	public InputStream ConvertInputStream(FileInputStream fileInput) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024*4];
		int n = -1;
		InputStream inputStream = null;
		try {
			while ((n=fileInput.read(buffer)) != -1) {
				baos.write(buffer, 0, n);
				
			}
			byte[] byteArray = baos.toByteArray();
			inputStream = new ByteArrayInputStream(byteArray);
			return inputStream;
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
	}
  public AppUserInfo getLoginInfo(String code) {
		String url = 	"https://api.weixin.qq.com/sns/jscode2session?appid="+ this.getSmallAppID()+"&secret="+this.getSmallAppSecret()+"&js_code="+code+"&grant_type=authorization_code";
		try {
			String json= HttpClientUtil.getUrl(url, null);
            if(StrUtil.isNotEmpty(json)) {
            	JSONObject jsonObject = GeneralUtil.getJsonObject(json);
            	if(jsonObject!=null) {
            		String openid= jsonObject.getString("openid");
        			AppUserInfo info =new AppUserInfo();
        			info.setAppType("app");
        			info.setOpenId(openid);
        			info.setSessionKey(jsonObject.getString("session_key"));
        			info.setUnionid(jsonObject.getString("unionid"));
        			return info;
            	}
    			
            }
		} catch (HttpException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

	public AppUserInfo getLoginInfoMp(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid=" + openAppId
				+ "&secret=" + openAppSecret + "&code=" + code + "&grant_type=authorization_code";
		try {
			String accessTokenObj = HttpClientUtil.getUrl(url, null);
			JSONObject json = JSONObject.parseObject(accessTokenObj);
			if (json != null && json.size() > 0) {
				String accesstoken = json.getString("access_token");
				String refreshtoken = json.getString("refresh_token");
				String openid = json.getString("openid");
				String unionid =json.getString("unionid");
				if (StrUtil.isEmpty(accesstoken) || StrUtil.isEmpty(refreshtoken) || StrUtil.isEmpty(openid)) {
					throw new BizException("微信扫码出错，参数返回异常，请重新尝试！");
				}
				AppUserInfo info = new AppUserInfo();
				info.setAppType("mp");
				info.setSessionKey(unionid);
				info.setUnionid(unionid);
				info.setOpenId(openid);
				info.setAccessToken(accesstoken);
				info.setRefreshToken(refreshtoken);
				return info;
			}
		} catch (HttpException e) {
			throw new RuntimeException(e);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
 }
