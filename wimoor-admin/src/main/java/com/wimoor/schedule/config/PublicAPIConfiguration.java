package com.wimoor.schedule.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PublicAPIConfiguration {
    @LoadBalanced
    @Bean(value="restTemplateApi")
    RestTemplate restTemplateApi() {
        return new RestTemplate();
    }
}