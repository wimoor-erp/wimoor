package com.wimoor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.wimoor.util.SpringUtil;

import tk.mybatis.spring.annotation.MapperScan;
 

@SpringBootApplication(scanBasePackages ={"com.wimoor.amazon.adv.*"})
@EnableFeignClients
@EnableCaching
@EnableDiscoveryClient
@EnableScheduling
@MapperScan({"com.wimoor.amazon.adv.*.dao"})
@ComponentScan(value = {"com.wimoor.amazon.adv.*"})
public class AmazonAdvApplication {

    public static void main(String[] args) {
    	ConfigurableApplicationContext context = SpringApplication.run(AmazonAdvApplication.class, args);
        SpringUtil.set(context);
    }

}
