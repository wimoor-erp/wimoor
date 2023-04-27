package com.wimoor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.wimoor.util.SpringUtil;


@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients
@EnableCaching
@ComponentScan
public class ERPApplication {

    public static void main(String[] args) {
    	ConfigurableApplicationContext context = SpringApplication.run(ERPApplication.class, args);
        SpringUtil.set(context);
    }

}
