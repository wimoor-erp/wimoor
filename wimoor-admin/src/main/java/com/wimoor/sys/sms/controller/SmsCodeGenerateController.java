package com.wimoor.sys.sms.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.wimoor.admin.common.constants.GlobalConstants;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.sys.sms.util.AliyunSmsUtils;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
@Api(tags = "短消息接口")
@RestController
@RequestMapping("/api/v1/sms")
@RequiredArgsConstructor
public class SmsCodeGenerateController {
	
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private final AliyunSmsUtils aliyunSmsUtils;
    
		@GetMapping("/getSmsCode")
		public Result<?> getAliyunSmsCodeAction(String account,String ftype) {
			Map<String,Object> maps=new HashMap<String, Object>();
			try {
				AliyunSmsUtils.setNewcode();
				String code = Integer.toString(AliyunSmsUtils.getNewcode());//4位数验证码
				stringRedisTemplate.delete(GlobalConstants.SMS_CODE+account);
				SendSmsResponse response = aliyunSmsUtils.sendSms(account, code,ftype);
				stringRedisTemplate.opsForValue().set(GlobalConstants.SMS_CODE+account, code, 600, TimeUnit.SECONDS);
				if(response.getCode().toUpperCase().equals("OK")) {
					maps.put("isOK", "true");
					maps.put("code", code);
				}else{
					maps.put("isOK", "false");
				}
			} catch (ClientException e) {
				e.printStackTrace();
			}
			return Result.success(maps);
		}
		
		@GetMapping("/checkSmsCode")
		public Result<?> checkAliyunSmsCodeAction(String account,String code) {
			AliyunSmsUtils.setNewcode();
			String mem_code = stringRedisTemplate.opsForValue().get(GlobalConstants.SMS_CODE+account);
		    if(mem_code!=null&&mem_code.toString().equals(code)) {
		    	stringRedisTemplate.delete(GlobalConstants.SMS_CODE+account);
		    	return Result.success(true);
		    }else {
		    	throw new BizException("验证码错误！");
		    }
		}
}
