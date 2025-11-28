package com.wimoor.auth.client.controller;

import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.open1688.utils.ApiCallService;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "采购1688接口")
@RequestMapping("/api/v1/open1688")
@Controller
@RequiredArgsConstructor
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
	@ApiOperation("get1688api")
	@GetMapping("/getUrl")
	@ResponseBody
	public JSONObject getUrlAction(String url) {
		String s = null;
		try {
			s = HttpClientUtil.getUrl(url,null);
		} catch (HttpException | IOException e) {
			// TODO Auto-generated catch block
			 return getErrorJson(e);
			
		} 
		if(StrUtil.isBlankOrUndefined(s)) {
			return getErrorJson(null);
		}
		JSONObject json = GeneralUtil.getJsonObject(s);     
		return json;
	}

	@ApiOperation("post1688api")
	@PostMapping("/postUrl")
	@ResponseBody
	public JSONObject postUrlAction(@RequestBody Map<String,String> props) {
		String s = null;
		String url=null;
		String contentType = "application/x-www-form-urlencoded";
		try {
			if(props.get("url")==null) {
				throw new BizException("无法获取URL");
			}else {
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
		JSONObject json = GeneralUtil.getJsonObject(s);     
		return json;
	}

	@ApiOperation("call1688api")
	@PostMapping("/callApi")
	@ResponseBody
	public JSONObject callApiAction(@RequestBody Map<String,String> props) {
		String s = null;
		String url=null;
		String appsecret = null;
		if(props.get("url")==null) {
			throw new BizException("无法获取URL");
		}else {
			url=props.get("url").toString();
			props.remove("url");
		}
		if(props.get("appsecret")==null) {
			throw new BizException("无法获取授权秘钥");
		}else {
			appsecret=props.get("appsecret").toString();
		}
		s = ApiCallService.callApi(url, appsecret, props);
		if(s==null)return null;
		JSONObject json = GeneralUtil.getJsonObject(s);     
		return json;
	}
	
 
}
