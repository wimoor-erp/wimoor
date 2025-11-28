package com.wimoor.controller;

import java.util.Map;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONException;
import com.wimoor.config.HttpClientUtil;
import com.wimoor.config.HttpException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.open1688.utils.ApiCallService;
@RequestMapping("/api/v1/open1688")
@Controller
public class JuShiTaController {
	JSONObject getErrorJson(Exception e){
		if(e!=null) {
			JSONObject json = new JSONObject();
			json.put("msg",e.getMessage());
			json.put("trace",e.getStackTrace());
			json.put("iserror",true);
			return json;
		}else {
			JSONObject json = new JSONObject();
			json.put("msg","返回结果为空");
			json.put("iserror",true);
			return json;
		}
	}
	@GetMapping("/getUrl")
	@ResponseBody
	public JSONObject getUrlAction(String url) {
		String s = null;
		try {
			s = HttpClientUtil.getUrl(url,null);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			 return getErrorJson(e);
			
		} 
		if(StrUtil.isBlankOrUndefined(s)) {
			return getErrorJson(null);
		}
		JSONObject json = getJsonObject(s);
		return json;
	}

	@PostMapping("/postUrl")
	@ResponseBody
	public JSONObject postUrlAction(@RequestBody Map<String,String> props) {
		String s = null;
		String url=null;
		String contentType = "application/x-www-form-urlencoded";
		try {
			if(props.get("url")!=null) {
				url=props.get("url").toString();
				props.remove("url");
			}
			if(props.get("Content-Type")==null) {
				props.put("Content-Type", contentType);
			}else {
				contentType=props.get("Content-Type").toString();
			}
			s = HttpClientUtil.postUrl(url, props, null, contentType);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			 return getErrorJson(e);
		}
		if(s==null) {
			return getErrorJson(null);
		}
		JSONObject json = getJsonObject(s);
		return json;
	}

	@PostMapping("/callApi")
	@ResponseBody
	public JSONObject callApiAction(@RequestBody Map<String,String> props) {
		String s = null;
		String url=null;
		String appsecret = null;
		if(props.get("url")!=null) {
			url=props.get("url").toString();
			props.remove("url");
		}
		if(props.get("appsecret")!=null) {
			appsecret=props.get("appsecret").toString();
		}
		s = ApiCallService.callApi(url, appsecret, props);
		if(s==null)return null;
		JSONObject json = getJsonObject(s);
		return json;
	}

	public static JSONObject getJsonObject(String value) {
		if(value==null)return null;
		try {
			return JSONObject.parseObject(value);
		} catch (JSONException e) {
			return null;
		}
	}
}
