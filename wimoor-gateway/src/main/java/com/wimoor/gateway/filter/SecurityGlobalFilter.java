package com.wimoor.gateway.filter;



import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.result.ResultCode;
import com.wimoor.common.user.UserInfo;
import com.wimoor.gateway.utils.ResponseUtils;

import reactor.core.publisher.Mono;


@Component
public class SecurityGlobalFilter implements GlobalFilter, Ordered {
	
    public static final String HEADER_USER_INFO = "X-USERINFO";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("jsessionid");
          URI uri = exchange.getRequest().getURI();
          String path=uri.getPath();
          if(path.contains("ssoclient/")||path.contains("auth/")) {
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
        
            JSONObject jsonObject=JSONObject.parseObject(user);
            UserInfo stu=(UserInfo)JSONObject.toJavaObject(jsonObject, UserInfo.class);
            if(stu.getSession().equals(token)) {
            	ServerHttpRequest request = exchange.getRequest().mutate().header(HEADER_USER_INFO, user).build();
                exchange = exchange.mutate().request(request).build();
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