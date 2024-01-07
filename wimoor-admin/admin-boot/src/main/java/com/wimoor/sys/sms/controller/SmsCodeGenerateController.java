package com.wimoor.sys.sms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserType;
import com.wimoor.sys.sms.util.AliyunSmsUtils;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
@Api(tags = "短消息接口")
@RestController
@RequestMapping("/api/v1/sms")
@RequiredArgsConstructor
public class SmsCodeGenerateController {
    private final AliyunSmsUtils aliyunSmsUtils;
    
		@GetMapping("/sendSms")
		public Result<?> getAliyunSmsAction(String phone,String template,String messagejson) {
				SendSmsResponse response=null;
				try {
					UserInfo userinfo = UserInfoContext.get();
					if(userinfo.getUsertype().equals(UserType.admin.getCode())) {
						response = aliyunSmsUtils.sendSms(phone,template, messagejson);
					}
				} catch (ClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return Result.success(response);
		}
		
		 
}
