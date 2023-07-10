package com.wimoor.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;

import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class BaseController<S extends IService> {

    @Autowired
    protected S baseService;
    @Autowired
    protected HttpSession session;
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected RestTemplate restTemplate;

    /**
     * 获取分页对象   === mybatis-plus
     * <P>
     *     1、从 Param 参数中获取
     *     2、从 path 参数中获取
     * </P>
     *
     * @return
     */
    protected <T> com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> getPage() {
        // 分页参数
        String current = request.getParameter("currentpage");
        String size = request.getParameter("pagesize");
        if (current == null || size == null) {
            // 从 path 获取
            @SuppressWarnings("unchecked")
			Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            if (pathVariables != null) {
                current = pathVariables.get("currentpage");
                size = pathVariables.get("pagesize");
            }
        }
        // 设置默认分页大小
        int cursor = current == null ? 1 : Integer.parseInt(current);
        int limit = size == null ? 20 : Integer.parseInt(size);
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(cursor, limit);
    }


    /**
     * 获取请求地址
     * @author wang-song
     * @param request
     * @date 2020/7/14 0014 14:16
     * @return java.lang.String
     * @version 1.0.1
     */
    protected static String getIpAddress(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 获取项目的Ip 地址+端口 或者域名
     * @return
     */
    public static String getBaseUrl(HttpServletRequest request) {
        // 接口名
        String interfaceName = request.getServletPath();
        // 完整url
        String url = request.getRequestURL().toString();
        // 去调接口后url
        String baseUrl = url.replace(interfaceName, "");
        return baseUrl;
    }
}
