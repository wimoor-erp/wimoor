package com.wimoor.auth.client.shiro;


import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.web.filter.AccessControlFilter;

public class GetCodeFilter  extends AccessControlFilter implements SessionListener {

	@Override
	public void onStart(Session session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop(Session session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExpiration(Session session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		// TODO Auto-generated method stub
 
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		HttpServletResponse httpReponse = (HttpServletResponse)response;
		httpReponse.setContentType("image/jpeg");
		// 禁止图像缓存。
		httpReponse.setHeader("Pragma", "no-cache");
		httpReponse.setHeader("Cache-Control", "no-cache");
		httpReponse.setDateHeader("Expires", 0);

		Code vCode = new Code(90, 33, 4, 30);
		try {
			vCode.write(response.getOutputStream());
			httpReponse.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession();
		session.setAttribute("validateCode", vCode.getCode());
		return false;
	}

 
 
}
