package com.wimoor.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.wimoor.common.service.SystemSchedulerService;

 

@Configuration
public class SystemSchedulerInit implements ApplicationRunner {

   @Value("${spring.profiles.active}")
   String profile;
   @Value("${spring.application.name}")
   String server;
   
   @Autowired
   SystemSchedulerService systemSchedulerService;

   
   public String getProfile() {
	return profile;
   }

 
	public String getServer() {
		return server;
	}
	
 

@Override
   public void run(ApplicationArguments args) throws Exception {
	   if("prod".equals(profile)) {
		    // systemSchedulerService.insertTask();
		    systemSchedulerService.refreshTask();
	   }

   }
}
 