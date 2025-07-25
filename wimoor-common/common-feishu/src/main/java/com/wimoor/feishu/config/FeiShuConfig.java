package com.wimoor.feishu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lark.oapi.sdk.servlet.ext.ServletAdapter;
@Configuration
public class FeiShuConfig {
	  @Bean
	  public ServletAdapter getServletAdapter() {
	    return new ServletAdapter();
	  }
}
