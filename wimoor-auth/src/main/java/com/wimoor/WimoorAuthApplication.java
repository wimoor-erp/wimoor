package com.wimoor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.wimoor.util.SpringUtil;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(value = {"com.wimoor.auth.*.controller","com.wimoor.auth.*.config","com.wimoor.*.api"})
public class WimoorAuthApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(WimoorAuthApplication.class, args);
		SpringUtil.set(context);
	}

}
