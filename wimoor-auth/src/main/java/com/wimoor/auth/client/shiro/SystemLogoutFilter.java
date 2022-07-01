package com.wimoor.auth.client.shiro;



import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.stereotype.Service;


@Service
public class SystemLogoutFilter extends LogoutFilter {
	 @Override
	    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
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