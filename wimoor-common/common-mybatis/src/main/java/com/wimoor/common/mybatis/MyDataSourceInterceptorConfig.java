package com.wimoor.common.mybatis;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
 
import java.util.List;
 
/**
 * @Description
 */
@Component
public class MyDataSourceInterceptorConfig implements ApplicationListener<ContextRefreshedEvent> {
 
    @Autowired
    private ReplaceTableInterceptor mybatisInterceptor;
 
    @Autowired
    private List<SqlSessionFactory> sqlSessionFactories;
 
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        for (SqlSessionFactory factory : sqlSessionFactories) {
            factory.getConfiguration().addInterceptor(mybatisInterceptor);
        }
    }
}
 