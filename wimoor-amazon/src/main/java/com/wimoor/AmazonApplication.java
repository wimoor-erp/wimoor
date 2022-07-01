package com.wimoor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import com.wimoor.util.SpringUtil;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
@EnableMPP
@ComponentScan
public class AmazonApplication {

    public static void main(String[] args) {
    	ConfigurableApplicationContext context = SpringApplication.run(AmazonApplication.class, args);
        SpringUtil.set(context);
    }

}
