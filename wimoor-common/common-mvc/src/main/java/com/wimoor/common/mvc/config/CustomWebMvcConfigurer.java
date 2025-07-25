package com.wimoor.common.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

 

@SuppressWarnings("deprecation")
@Configuration
public class CustomWebMvcConfigurer extends WebMvcConfigurerAdapter {
    // 如果继承了WebMvcConfigurationSupport,会覆盖访问静态资源的配置，无法访问静态资源，必须继承WebMvcConfigurerAdapter
    @Override
    public void configurePathMatch(PathMatchConfigurer pathMatchConfigurer) {
        //是否是后缀模式匹配,如果是true,请求download.* 可以映射到download
        pathMatchConfigurer.setUseSuffixPatternMatch(true);
    }
 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
         registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/");
         // 解决静态资源无法访问（可选）
         registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
         // 直接在浏览器访问：根目录/swagger-ui.html
         registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
         // 需要用到的webjars（包含js、css等）
         registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
         // Knife4j 页面访问
         registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
    }
 
    
    

}
 
