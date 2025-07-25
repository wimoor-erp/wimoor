package com.wimoor.admin.config;


import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DataSourceConfig {

    /**
     * 数据源配置的前缀，必须与application.properties中配置的对应数据源的前缀一致
     */
    private static final String BUSINESS_DATASOURCE_PREFIX = "spring.datasource";

    private static final String QUARTZ_DATASOURCE_PREFIX = "spring.datasource.quartz";

    @Bean
    @ConfigurationProperties(prefix = BUSINESS_DATASOURCE_PREFIX)
    public DataSource businessDataSource() {
        return DataSourceBuilder.create().build();
    }


    /**
     * @QuartzDataSource 注解则是配置Quartz独立数据源的配置
     */
    @Bean("quartzDataSource")
    @QuartzDataSource
    @ConfigurationProperties(prefix = QUARTZ_DATASOURCE_PREFIX)
    public DataSource quartzDataSource(){
    	  return DataSourceBuilder.create().build();
     }
}