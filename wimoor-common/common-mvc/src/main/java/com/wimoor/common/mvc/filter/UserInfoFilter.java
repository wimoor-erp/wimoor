package com.wimoor.common.mvc.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
 
    public static String getHeader(HttpServletRequest request, String name)
    {
        String value = request.getHeader(name);
        if (StrUtil.isEmpty(value))
        {
            return "";
        }
        try {
            return java.net.URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String userInfoJson = getHeader((HttpServletRequest) request,UserInfoContext.HEADER_USER_INFO)  ;
        if (StrUtil.isNotBlank(userInfoJson)) {
            UserInfo userInfo = JSON.parseObject(userInfoJson, UserInfo.class);
            UserInfoContext.set(userInfo);
        }
        chain.doFilter(request, response);
        log.debug("获取登录人信息"+userInfoJson);
    }
}