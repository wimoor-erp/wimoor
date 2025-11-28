package com.wimoor.data.config;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.logging.Logger;

@Aspect
@Component
public class DataSourceAspect implements Ordered {

    @Pointcut("execution(* com.wimoor.*.service..*.*(..))")
    public void dataSourcePointCut() {

    }

    // 前置方法
    @Before("dataSourcePointCut()")
    public void before(JoinPoint point){
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        DataSource dataSource = null;
        // 获取方法注解
        if(method != null && method.isAnnotationPresent(DataSource.class)){
            dataSource =  method.getAnnotation(DataSource.class);
        }
        if (dataSource != null && StringUtils.isNotEmpty(dataSource.name())) {
                DynamicDataSource.setDataSource(dataSource.name());
        }else{
            DynamicDataSource.setDataSource(DataSourceNames.master);
        }
    }

    // 后置方法
    @After("dataSourcePointCut()")
    public void after(JoinPoint point){
        // 清空配置,避免对以后的Service 产生影响
        DynamicDataSource.clearDataSource();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
