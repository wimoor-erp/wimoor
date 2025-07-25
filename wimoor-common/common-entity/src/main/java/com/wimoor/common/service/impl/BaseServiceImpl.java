package com.wimoor.common.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    /**
     * 当前继承this类传递的Mapper 类 (提供Api操作，及Mapper.xml自定义sql)
     */
    @Autowired
    protected M baseMapper;
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;

    /**
     * rpc工具,可以直接发起http请求
     */
    @Autowired
    protected RestTemplate restTemplate;
    /**
     * 数据库操作
     * execute：可以执行所有SQL语句，没有返回值, 一般用于执行DDL语句。
     * update：用于执行INSERT、UPDATE、DELETE等DML语句。
     * queryXxx：用于DQL数据查询语句。
     */
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    /**
     * this = 当前继承BaseIServiceImpl+T 类的 Iservice类 （对 baseMapper 进行增强, 提供批量操作方法）
     */
    /// protected this;


    /**
     * 获取项目的Ip 地址+端口 或者域名
     *
     * @param request request
     * @return java.lang.String
     * @version 1.0.0
     */
    public static String getBaseUrl(HttpServletRequest request) {
        // 接口名
        String interfaceName = request.getServletPath();
        // 完整url
        String url = request.getRequestURL().toString();
        // 去调接口后url
        return url.replace(interfaceName, "");
    }
}



