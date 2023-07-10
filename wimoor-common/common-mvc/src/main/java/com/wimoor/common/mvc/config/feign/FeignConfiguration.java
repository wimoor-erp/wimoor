package com.wimoor.common.mvc.config.feign;


import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.net.URLEncoder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.nio.charset.Charset;
@Configuration
public class FeignConfiguration implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
//        requestTemplate.header(RootContext.KEY_XID.toLowerCase(), GlobalTransactionContext.getCurrentOrCreate().getXid());
        UserInfo user = UserInfoContext.get();
	    String jsonuser = JSONObject.toJSONString(user);
	    URLEncoder encoder = new URLEncoder();
	    jsonuser=encoder.encode(jsonuser,Charset.forName("utf-8"));
		requestTemplate.header(UserInfoContext.HEADER_USER_INFO,jsonuser);
    }
}
