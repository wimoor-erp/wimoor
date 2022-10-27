package com.wimoor.auth.client.shiro;



import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.data.redis.core.StringRedisTemplate;

import cn.hutool.extra.spring.SpringUtil;


public class SystemLogoutFilter extends LogoutFilter {
	    @Override
	    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		    HttpSession session = ((HttpServletRequest)request).getSession();
			String jsessionid = session.getId();
			StringRedisTemplate stringRedisTemplate=SpringUtil.getBean("stringRedisTemplate");
			stringRedisTemplate.delete(jsessionid);
	        Subject subject = getSubject(request, response);
	        String redirectUrl = getRedirectUrl(request, response, subject);
	        try {
	            subject.logout();
	        } catch (Exception ise) {
	            ise.printStackTrace();
	        }
	        issueRedirect(request, response, redirectUrl);
	        return false;
	    }
}