package com.wimoor.auth.client.config;

import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wimoor.auth.client.shiro.MyRedisSessionDao;

import lombok.Setter;
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class BootShiroRedisConfiguration {
 
	@Setter
	private int database;
	
    @Setter
    private String host;
    
    @Setter
    private String password;
	 
		/**
		 * RedisManager配置交由springBootRedis管理
		 */
		@Bean
		public RedisManager redisManager() {
		      RedisManager redisManager = new RedisManager();
		        redisManager.setHost(host);
		        redisManager.setPort(6379);
		        redisManager.setExpire(1800);// 配置缓存过期时间
		        redisManager.setTimeout(0);
		        redisManager.setPassword(password);
		        return redisManager;
		}

	    /**
	     * cacheManager 缓存 redis实现
	     * 使用的是shiro-redis开源插件
	     *
	     * @return
	     */
	    @Bean
	    public RedisCacheManager cacheManager() {
	        RedisCacheManager redisCacheManager = new RedisCacheManager();
	        redisCacheManager.setRedisManager(redisManager());
	        return redisCacheManager;
	    }
	    
	    @Bean
	    public SimpleCookie sessionIdCookie() {
	        // 这个参数是cookie的名称，对应前端的checkbox 的name = rememberMe
	        SimpleCookie simpleCookie = new SimpleCookie("sid");
	        // <!-- 记住我cookie生效时间30天（259200） ,单位秒;-->
	        simpleCookie.setMaxAge(-1);
	        return simpleCookie;
	    }
	    /**
	     * Session Manager
	     * 使用的是shiro-redis开源插件
	     */
	    @Bean
	    public DefaultWebSessionManager sessionManager() {
	        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
	        sessionManager.setSessionDAO(redisSessionDAO());
	        sessionManager.setSessionIdCookie(sessionIdCookie());
	        return sessionManager;
	    }

	    /**
	     * RedisSessionDAO shiro sessionDao层的实现 通过redis
	     * 使用的是shiro-redis开源插件
	     */
	    @Bean
	    public MyRedisSessionDao redisSessionDAO() {
	    	MyRedisSessionDao redisSessionDAO = new MyRedisSessionDao();
	        redisSessionDAO.setRedisManager(redisManager());
	        return redisSessionDAO;
	    }

}
