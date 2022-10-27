package com.wimoor.auth.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

 

@SuppressWarnings("deprecation")
@Configuration
public class CustomwebMvcConfigurer extends WebMvcConfigurerAdapter {
    // 如果继承了WebMvcConfigurationSupport,会覆盖访问静态资源的配置，无法访问静态资源，必须继承WebMvcConfigurerAdapter
    @Override
    public void configurePathMatch(PathMatchConfigurer pathMatchConfigurer) {
        //是否是后缀模式匹配,如果是true,请求download.* 可以映射到download
        pathMatchConfigurer.setUseSuffixPatternMatch(true);
    }
 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
         registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/");
    }
 
    
    

}
 
