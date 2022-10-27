package com.wimoor.gateway.filter;



import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.result.ResultCode;
import com.wimoor.common.user.UserInfo;
import com.wimoor.gateway.utils.ResponseUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;
@ConfigurationProperties(prefix = "security")
@Component
@RequiredArgsConstructor
public class SecurityGlobalFilter implements GlobalFilter, Ordered {

	@Setter
	private List<String> ignoreUrls;
	private Set<String> whiteList=new HashSet<String>();
	private Set<String> authList=new HashSet<String>();
    public static final String HEADER_USER_INFO = "X-USERINFO";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private final RedisTemplate redisTemplate;
    
    boolean filterIsIgnoreUrl(String path) {
    	    if(whiteList.contains(path))return true;
    	    if(authList.contains(path))return false;
    	    boolean ignore=false;
    	    AntPathMatcher antPathMatcher = new AntPathMatcher();
            for(String pattern:ignoreUrls) {
         	  ignore=antPathMatcher.match(pattern, path);
         	  if(ignore) {
         		  break;
         	  }
            }
            if(ignore) {
            	whiteList.add(path);
            }else {
            	authList.add(path);
            }
            return ignore;
    }
    
 
    public Boolean checkAuthority(ServerWebExchange exchange,UserInfo user) {
        ServerHttpRequest request = exchange.getRequest();
        if (request.getMethod() == HttpMethod.OPTIONS) { // 预检请求放行
            return true;
        }
        PathMatcher pathMatcher = new AntPathMatcher();
        //String method = request.getMethodValue();
        String path = request.getURI().getPath();
        String method = request.getMethodValue();
        String restfulPath = method + ":" + path;
        /**
         * 鉴权开始
         *
         * 缓存取 [URL权限-角色集合] 规则数据
         * urlPermRolesRules = [{'key':'GET:/api/v1/users/*','value':['ADMIN','TEST']},...]
         */
        Map<String, Object> urlPermRolesRules = redisTemplate.opsForHash().entries("system:perm_roles_rule:url:");
        // 根据请求路径判断有访问权限的角色列表
        List<String> authorizedRoles = new ArrayList<>(); // 拥有访问权限的角色
        boolean requireCheck = false; // 是否需要鉴权，默认未设置拦截规则不需鉴权

        for (Map.Entry<String, Object> permRoles : urlPermRolesRules.entrySet()) {
            String perm = permRoles.getKey();
            if (pathMatcher.match(perm, restfulPath)) {
                List<String> roles = Convert.toList(String.class, permRoles.getValue());
                authorizedRoles.addAll(Convert.toList(String.class, roles));
                if (requireCheck == false) {
                    requireCheck = true;
                }
            }
        }
        if (requireCheck == false) {
            return true;
        }

        String roles=user.getRoles();
        List<String> list = Arrays.asList(roles.split(","));
        for(String roleCode:list) {
            if ("1".equals(roleCode)) {
                return true; // 如果是超级管理员则放行
            }
            boolean hasAuthorized = CollectionUtil.isNotEmpty(authorizedRoles) && authorizedRoles.contains(roleCode);
            return hasAuthorized;
        }
        return false;
    }
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("jsessionid");
          URI uri = exchange.getRequest().getURI();
          String path=uri.getPath();
          if(filterIsIgnoreUrl(path)) {
        	   return chain.filter(exchange);  
          }
          boolean flag =false;
            if(token!=null) {
            	flag=stringRedisTemplate.hasKey(token);
            }
            if (!flag) {
              	 ServerHttpResponse response = exchange.getResponse();
                return ResponseUtils.writeErrorInfo(response, ResultCode.TOKEN_ACCESS_FORBIDDEN);
            }
            String user = stringRedisTemplate.opsForValue().get(token);
            stringRedisTemplate.expire(token,3600,java.util.concurrent.TimeUnit.SECONDS);
            JSONObject jsonObject=JSONObject.parseObject(user);
            UserInfo stu=(UserInfo)JSONObject.toJavaObject(jsonObject, UserInfo.class);
            if(stu.getSession().equals(token)) {
            	if(checkAuthority(exchange,stu)) {
            		ServerHttpRequest request = exchange.getRequest().mutate().header(HEADER_USER_INFO, user).build();
                    exchange = exchange.mutate().request(request).build();
            	}else {
            		 ServerHttpResponse response = exchange.getResponse();
                     return ResponseUtils.writeErrorInfo(response, ResultCode.ACCESS_UNAUTHORIZED);
            	}
            
            }else {
            	 ServerHttpResponse response = exchange.getResponse();
                 return ResponseUtils.writeErrorInfo(response, ResultCode.TOKEN_ACCESS_FORBIDDEN);
            }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}