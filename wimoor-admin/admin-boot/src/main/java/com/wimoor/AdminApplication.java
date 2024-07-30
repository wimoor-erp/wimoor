package com.wimoor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.wimoor.util.SpringUtil;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@ComponentScan
public class AdminApplication {

    public static void main(String[] args) {
       	ConfigurableApplicationContext context  =SpringApplication.run(AdminApplication.class, args);
        SpringUtil.set(context);
    }

}
