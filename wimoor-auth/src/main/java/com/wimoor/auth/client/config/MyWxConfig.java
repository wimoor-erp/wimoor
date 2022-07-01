package com.wimoor.auth.client.config;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.wxpay.sdk.WXPayConfig;

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

}
