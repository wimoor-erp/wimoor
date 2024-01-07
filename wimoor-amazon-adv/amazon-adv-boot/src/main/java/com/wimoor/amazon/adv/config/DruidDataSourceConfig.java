package com.wimoor.amazon.adv.config;


import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
 
@Configuration
@SuppressWarnings({"rawtypes","unused","unchecked"})
@EnableTransactionManagement
public class DruidDataSourceConfig {
	/** 驱动名称 */

	private String DEFAULT_DRIVER_CLASSNAME;
 
	private static final Logger LOGGER = LoggerFactory.getLogger(DruidDataSourceConfig.class);
	
	@Autowired
	private DruidDataSourceProperties dataSourceProperties;
	
	/**
	 * Druid数据源监控拦截器
	 * 可以监控数据库的连接情况
	 * @return
	 */

	@Bean
	public ServletRegistrationBean druidRegistrationBean() {
		ServletRegistrationBean registrationBean = new ServletRegistrationBean();
		registrationBean.setServlet(new StatViewServlet());
		registrationBean.addUrlMappings("/druid/*");
		registrationBean.addInitParameter("allow", "127.0.0.1");
		LOGGER.info(" druid console manager info : {} ", registrationBean);
		return registrationBean;
	}
 
	/**
	 * 请求URL过滤器
	 * @return
	 */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico, /druid/*");
        LOGGER.info(" druid filter register : {} ", filterRegistrationBean);
        return filterRegistrationBean;
    }
	
    @Bean
	public DataSource dataSource() throws SQLException {
		DruidDataSource ds = new DruidDataSource();
		ds.setDriverClassName(dataSourceProperties.getDriverClassName());
		DEFAULT_DRIVER_CLASSNAME = dataSourceProperties.getDriverClassName();
		ds.setUrl(dataSourceProperties.getUrl());
		ds.setUsername(dataSourceProperties.getUsername());
		ds.setPassword(dataSourceProperties.getPassword());
		ds.setInitialSize(dataSourceProperties.getInitialSize());
		ds.setMinIdle(dataSourceProperties.getMinIdle());
		ds.setMaxActive(dataSourceProperties.getMaxActive());
		ds.setTimeBetweenEvictionRunsMillis(dataSourceProperties.getTimeBetweenEvictionRunsMillis());
		ds.setMinEvictableIdleTimeMillis(dataSourceProperties.getMinEvictableIdleTimeMillis());
		ds.setValidationQuery(dataSourceProperties.getValidationQuery());
		ds.setTestWhileIdle(dataSourceProperties.isTestWhileIdle());
		ds.setTestOnBorrow(dataSourceProperties.isTestOnBorrow());
		ds.setTestOnReturn(dataSourceProperties.isTestOnReturn());
		ds.setPoolPreparedStatements(dataSourceProperties.isPoolPreparedStatements());
		ds.setMaxPoolPreparedStatementPerConnectionSize(dataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
		ds.setFilters(dataSourceProperties.getFilters());
		ds.setConnectionProperties(dataSourceProperties.getConnectionProperties());
		LOGGER.info(" druid datasource config : {} ", ds);
		return ds;
	}
 
	@Bean
	public PlatformTransactionManager transactionManager() throws Exception {
		DataSourceTransactionManager txManager = new DataSourceTransactionManager();
		txManager.setDataSource(dataSource());
		return txManager;
	}
	
}
