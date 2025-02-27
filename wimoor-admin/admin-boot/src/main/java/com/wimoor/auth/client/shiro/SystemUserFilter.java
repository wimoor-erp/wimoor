package com.wimoor.auth.client.shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wimoor.auth.client.config.ShiroConfig;
 


public class SystemUserFilter extends UserFilter{
  
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String loginUrl = ShiroConfig.loginUrl;
		String iswechat = request.getParameter("iswechat");
		 HttpServletRequest hreq = (HttpServletRequest)request;
		 String userAgent=hreq.getHeader("user-agent");
		 if(userAgent.indexOf("MicroMessenger")>=0) {
			 iswechat="true";
		 }
		if(StringUtils.isNotBlank(iswechat)  &&  "true".equals(iswechat)) {
			loginUrl="/redirectWechatUrl.do?location="+request.getParameter("location");
		} 
		WebUtils.issueRedirect(request, response, loginUrl);
		 
	}

}
