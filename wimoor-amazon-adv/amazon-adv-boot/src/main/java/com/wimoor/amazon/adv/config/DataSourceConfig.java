package com.wimoor.amazon.adv.config;


import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class DataSourceConfig {

    /**
     * 数据源配置的前缀，必须与application.properties中配置的对应数据源的前缀一致
     */
    private static final String BUSINESS_DATASOURCE_PREFIX = "spring.datasource";


    @Primary
    @Bean
    @ConfigurationProperties(prefix = BUSINESS_DATASOURCE_PREFIX)
    public DataSource businessDataSource() {
        return DataSourceBuilder.create().build();
    }

}