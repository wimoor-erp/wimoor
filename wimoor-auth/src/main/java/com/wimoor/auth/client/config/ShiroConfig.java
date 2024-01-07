package com.wimoor.auth.client.config;
 


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.wimoor.auth.client.shiro.CasUserRealm;
import com.wimoor.auth.client.shiro.GetCodeFilter;
import com.wimoor.auth.client.shiro.MyCasFilter;
import com.wimoor.auth.client.shiro.SystemLogoutFilter;
 
/**
 * shiro配置类
 */
@SuppressWarnings("deprecation")
@Configuration
public class ShiroConfig {
    //1. realm对象注入
    public static String loginUrl;
    public static String uiserver;
    public static String casserver;
    public static String authserver;
    @Autowired
    RedisCacheManager cacheManager;
	@Value("${config.photo-server}")
	private String photo_server;
	
	@Value("${config.photo-server-url}")
	private String photo_server_url;
	
	@Value("${config.ui-server}")
	private String ui_server;
	
	@Value("${config.authority-server}")
	private String authority_server;
	
	@Value("${config.cas-server}")
	private String cas_server;
	
	@Value("${config.websit}")
	private String websit;
	
	@Value("${config.cas-filter-url-pattern}")
	private String urlpattern;
 
    // Cas登录页面地址
    public   final String getCasLoginUrl() {
    	casserver=cas_server;
    	return cas_server + "/login";
    } 
    // Cas登出页面地址
    public   final String getCasLogoutUrl() {
    	casserver=cas_server;
    	return cas_server+ "/logout";
    } 
 
    // 登录地址
    public   final String getLoginUrl() {
    	loginUrl=getCasLoginUrl() + "?service=" + authority_server + urlpattern;
    	authserver=authority_server;
    	return loginUrl;
    }  
    // 登出地址（casserver启用service跳转功能，需在webapps\cas\WEB-INF\cas.properties文件中启用cas.logout.followServiceRedirects=true）
    public   final String getLogoutUrl() {
    	return getCasLogoutUrl() + "?service=" + authority_server+"/mylogout";
    } 
    // 登录成功地址
    public   final String getLoginSuccessUrl() {
    	return authority_server+ "/getJSession";
    	}
    // 权限认证失败跳转地址
    public static final String unauthorizedUrl = "/403.html";
    
    @Bean
    public EhCacheManager getEhCacheManager() {  
        EhCacheManager em = new EhCacheManager();  
        em.setCacheManagerConfigFile("classpath:shiro-ehcache.xml");  
        return em;  
    }  
    
	@Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }
 
	   
	    @Bean
	    public SimpleCookie rememberMeCookie() {
	        // 这个参数是cookie的名称，对应前端的checkbox 的name = rememberMe
	        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
	        // <!-- 记住我cookie生效时间30天（259200） ,单位秒;-->
	        simpleCookie.setMaxAge(259200);
	        return simpleCookie;
	    }
	    

 
	    
 
		
	    @Bean(name = "hashedCredentialsMatcher")
	    public HashedCredentialsMatcher hashedCredentialsMatcher() {
	        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
	        hashedCredentialsMatcher.setHashAlgorithmName("MD5");// 散列算法:这里使用MD5算法;
	        hashedCredentialsMatcher.setHashIterations(1024);// 散列的次数，比如散列两次，相当于
	        return hashedCredentialsMatcher;
	    }
	    
 
		
	    @Bean(name = "cookieRememberMeManager")
	    public CookieRememberMeManager rememberMeManager(){
	       CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
	       cookieRememberMeManager.setCookie(rememberMeCookie());
	       //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
	       cookieRememberMeManager.setCipherKey(Base64.decode("1AvVhmFLUs1KTA1Kprsdag=="));
	       return cookieRememberMeManager;
	    }
	    
 
		@Bean
	    public CasUserRealm myShiroRealm() {
	    	uiserver=this.ui_server;
	    	CasUserRealm userShiroCasRealm = new CasUserRealm();
	        userShiroCasRealm.setCacheManager(cacheManager);
	        //userShiroCasRealm.setCredentialsMatcher(hashedCredentialsMatcher());
	        userShiroCasRealm.setCachingEnabled(false);
	        casserver=cas_server;
	        userShiroCasRealm.setCasServerUrlPrefix(cas_server);
	        userShiroCasRealm.setCasService(this.authority_server + this.urlpattern);
	        return userShiroCasRealm;
 
	    }

 
		@Bean
	    public SecurityManager securityManager() {
	        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
	        // 设置realm.
	        securityManager.setRealm(myShiroRealm());
	        // 自定义缓存实现 使用redis
	        securityManager.setCacheManager(cacheManager);
	        // 自定义session管理 使用redis
	        //securityManager.setSessionManager(sessionManager());
	        
	        securityManager.setSubjectFactory(new CasSubjectFactory());
	        return securityManager;
	    }


	    /***
	     * 授权所用配置
	     *
	     * @return
	     */
	    @Bean
	    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
	        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
	        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
	        return defaultAdvisorAutoProxyCreator;
	    }

	    /***
	     * 使授权注解起作用不如不想配置可以在pom文件中加入
	     * <dependency>
	     *<groupId>org.springframework.boot</groupId>
	     *<artifactId>spring-boot-starter-aop</artifactId>
	     *</dependency>
	     * @param securityManager
	     * @return
	     */
	    @Bean
	    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
	        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
	        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
	        return authorizationAttributeSourceAdvisor;
	    }
 

	    @SuppressWarnings({ "rawtypes", "unchecked" })
		@Bean
	    @Order(Ordered.HIGHEST_PRECEDENCE)
	    public ServletListenerRegistrationBean singleSignOutHttpSessionListener() {
	        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
	        bean.setListener(new SingleSignOutHttpSessionListener());
	        bean.setEnabled(true);
	        return bean;
	    }
 
	 
	    /**
	     * 注册DelegatingFilterProxy（Shiro）
	     *
	     * @return
	     */
	    @SuppressWarnings({ "rawtypes", "unchecked" })
		@Bean
	    public FilterRegistrationBean delegatingFilterProxy() {
	        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
	        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
	        //  该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
	        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
	        filterRegistration.setEnabled(true);
	        filterRegistration.addUrlPatterns("/*");
	        return filterRegistration;
	    }
	 
	    /**
	     * CAS过滤器
	     *
	     * @return
	     */
	    @Bean(name = "casFilter")
	    public MyCasFilter getCasFilter() {
	    	MyCasFilter casFilter = new MyCasFilter();
	        casFilter.setName("casFilter");
	        casFilter.setEnabled(true);
	        // 登录失败后跳转的URL，也就是 Shiro 执行 CasRealm 的 doGetAuthenticationInfo 方法向CasServer验证tiket
	        casFilter.setFailureUrl(getLoginUrl());// 我们选择认证失败后再打开登录页面
	        casFilter.setSuccessUrl(getLoginSuccessUrl());
	        return casFilter;
	    }

 
    //Filter工厂，设置对应的过滤条件和跳转条件

	@Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        
        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
//        filtersMap.put("myAccessControlFilter", new MyFormAuthenticationFilter());
//        filtersMap.put("userAccessControlFilter", new SystemUserFilter());
        LogoutFilter logout = new SystemLogoutFilter();
        logout.setRedirectUrl(getLogoutUrl());
        filtersMap.put("logoutFilter", logout);
        filtersMap.put("VCodeFilter", new GetCodeFilter());
        filtersMap.put("casFilter", getCasFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
 
        Map<String, String> map = new HashMap<>();
        map.put("/loginWechat", "anon");
 
        map.put("/verifyWechatApp", "anon");
        map.put("/mylogout", "anon");
 
        map.put("/shiro-cas", "casFilter");
 
        map.put("/logout", "logoutFilter");
        //登录
      
        shiroFilterFactoryBean.setLoginUrl(getLoginUrl());
        
        shiroFilterFactoryBean.setUnauthorizedUrl(getLoginUrl());
        //首页
        shiroFilterFactoryBean.setSuccessUrl(getLoginSuccessUrl());
         
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/pages/frame/blank");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }
    
 
 
}
