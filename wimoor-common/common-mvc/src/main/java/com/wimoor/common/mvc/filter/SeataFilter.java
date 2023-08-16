package com.wimoor.common.mvc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.stereotype.Component;



@Component
public class SeataFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		chain.doFilter(request, response);
	}
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        String xid = req.getHeader(RootContext.KEY_XID.toLowerCase());
//        boolean isBind = false;
//        if (StrUtil.isNotBlank(xid)) {
//            RootContext.bind(xid);
//            isBind = true;
//        }
//        try {
//            filterChain.doFilter(servletRequest, servletResponse);
//        } finally {
//            if (isBind) {
//                RootContext.unbind();
//            }
//        }
//    }
//
//    @Override
//    public void destroy() {
//    }
}
