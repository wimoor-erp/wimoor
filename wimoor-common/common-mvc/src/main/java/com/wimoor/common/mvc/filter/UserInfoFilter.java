package com.wimoor.common.mvc.filter;

import java.io.IOException;
import java.nio.charset.Charset;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.StrUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
 

@Slf4j
@Order(1)
@Component
public class UserInfoFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String userInfoJson = ((HttpServletRequest) request).getHeader(UserInfoContext.HEADER_USER_INFO);
        if (StrUtil.isNotBlank(userInfoJson)) {
        	userInfoJson=URLDecoder.decode(userInfoJson,Charset.forName("utf-8"));
            UserInfo userInfo = JSON.parseObject(userInfoJson, UserInfo.class);
            UserInfoContext.set(userInfo);
        }
        chain.doFilter(request, response);
        log.debug("获取登录人信息"+userInfoJson);
    }
}