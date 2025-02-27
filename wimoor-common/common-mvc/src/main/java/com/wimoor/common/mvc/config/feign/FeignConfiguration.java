package com.wimoor.common.mvc.config.feign;


import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.net.URLEncodeUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
import io.seata.tm.api.GlobalTransactionContext;

import java.nio.charset.Charset;
@Configuration
public class FeignConfiguration implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(RootContext.KEY_XID.toLowerCase(), GlobalTransactionContext.getCurrentOrCreate().getXid());
        UserInfo user = UserInfoContext.get();
	    String jsonuser = JSONObject.toJSONString(user);
	    jsonuser=URLEncodeUtil.encode(jsonuser,Charset.forName("utf-8"));
		requestTemplate.header(UserInfoContext.HEADER_USER_INFO,jsonuser);
    }
}
