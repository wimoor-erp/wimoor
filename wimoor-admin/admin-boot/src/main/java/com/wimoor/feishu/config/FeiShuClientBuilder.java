package com.wimoor.feishu.config;


import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.lark.oapi.Client;
import com.wimoor.feishu.pojo.entity.Auth;
import com.wimoor.feishu.service.IAuthService;

 
@Configuration
public class FeiShuClientBuilder {
     @Autowired
     IAuthService iAuthService;
	public Client getClient(String appid) {
		UserInfo userinfo = UserInfoContext.get();
		Auth auth = iAuthService.lambdaQuery().eq(Auth::getAppId, appid).eq(Auth::getShopid, userinfo.getCompanyid()).one();
		return Client.newBuilder(auth.getAppId(),auth.getAppSecret()).logReqAtDebug(true).build();
	}

}