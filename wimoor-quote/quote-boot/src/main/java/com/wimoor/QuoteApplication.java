package com.wimoor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

import com.wimoor.util.SpringUtil;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class QuoteApplication {

    public static void main(String[] args) {
       	ConfigurableApplicationContext context  =SpringApplication.run(QuoteApplication.class, args);
        SpringUtil.set(context);
    }

}
