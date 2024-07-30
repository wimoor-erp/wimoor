package com.wimoor.common.mybatis;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
 
@Configuration
@EnableTransactionManagement
public class DruidDataSourceConfig {
 
	 

	@Autowired
	private DruidDataSourceProperties dataSourceProperties;
	
    @Primary
    @Bean
   	public DataSource dataSource() throws SQLException {
   		DruidDataSource ds = new DruidDataSource();
   		ds.setDriverClassName(dataSourceProperties.getDriverClassName());
   		//String DEFAULT_DRIVER_CLASSNAME = dataSourceProperties.getDriverClassName();
   		ds.setDbType(dataSourceProperties.getType());
   		ds.setUrl(dataSourceProperties.getJdbcUrl());
   		ds.setUsername(dataSourceProperties.getUsername());
   		ds.setPassword(dataSourceProperties.getPassword());
   		ds.setInitialSize(dataSourceProperties.getInitialSize());
   		ds.setMinIdle(dataSourceProperties.getMinIdle());
   		ds.setMaxActive(dataSourceProperties.getMaxActive());
   		ds.setTimeBetweenEvictionRunsMillis(dataSourceProperties.getTimeBetweenEvictionRunsMillis());
   		ds.setMinEvictableIdleTimeMillis(dataSourceProperties.getMinEvictableIdleTimeMillis());
   		ds.setValidationQuery(dataSourceProperties.getValidationQuery());
   		ds.setTestWhileIdle(dataSourceProperties.isTestWhileIdle());
   		ds.setTestOnBorrow(dataSourceProperties.isTestOnBorrow());
   		ds.setTestOnReturn(dataSourceProperties.isTestOnReturn());
   		ds.setPoolPreparedStatements(dataSourceProperties.isPoolPreparedStatements());
   		ds.setMaxPoolPreparedStatementPerConnectionSize(dataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
   		ds.setFilters(dataSourceProperties.getFilters());
   		ds.setConnectionProperties(dataSourceProperties.getConnectionProperties());
   		return ds;
   	}
    
   	@Bean
   	public PlatformTransactionManager transactionManager() throws Exception {
   		DataSourceTransactionManager txManager = new DataSourceTransactionManager();
   		txManager.setDataSource(dataSource());
   		return txManager;
   	}
	
}
