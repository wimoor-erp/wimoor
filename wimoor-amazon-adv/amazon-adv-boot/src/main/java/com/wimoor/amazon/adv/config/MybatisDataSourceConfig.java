package com.wimoor.amazon.adv.config;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.wimoor.amazon.base.OffsetLimitInterceptor;

@Configuration
public class MybatisDataSourceConfig {
	
	@Autowired
	private DataSource dataSource;
    @Autowired
    private ReplaceTableInterceptor mybatisInterceptor;
    
	@Bean(name="sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean() {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			// 添加mybatis mapping文件的目录
			bean.setMapperLocations(resolver.getResources("classpath*:mapper/*/*.xml"));
			OffsetLimitInterceptor it = new OffsetLimitInterceptor();
			it.setDialectClass("com.github.miemiedev.mybatis.paginator.dialect.MySQLDialect");
            Interceptor[] plugins= {it};
			bean.setPlugins(plugins);
			SqlSessionFactory sqlSessionFactory = bean.getObject();
			sqlSessionFactory.getConfiguration().addInterceptor(mybatisInterceptor);
			sqlSessionFactory.getConfiguration().setCacheEnabled(Boolean.TRUE);
			return sqlSessionFactory;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
 
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
 
}
