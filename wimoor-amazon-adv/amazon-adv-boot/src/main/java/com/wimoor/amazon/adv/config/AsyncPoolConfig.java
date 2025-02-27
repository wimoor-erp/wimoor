package com.wimoor.amazon.adv.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xinyi
 * @desc: 自定义异步线程池
 * @date 2021/6/27
 */
@Slf4j
@EnableAsync
@Configuration
public class AsyncPoolConfig implements AsyncConfigurer {

    @Bean("threadPoolTaskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1000); // 核心池大小
        executor.setMaxPoolSize(20000); // 最大线程数
        executor.setQueueCapacity(2000000); // 队列程度 
        executor.setThreadPriority(Thread.MAX_PRIORITY);
        executor.setDaemon(false);
        executor.setKeepAliveSeconds(600);// 线程空闲时间
        executor.setThreadNamePrefix("wimoor-Executor-"); // 线程名字前缀
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(600);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    @SuppressWarnings("all")
    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            // 1、打印异常堆栈
            throwable.printStackTrace();
            // 2、日志记录错误信息
            log.error("AsyncError:{}, Method:{}, Param:{}", throwable.getMessage(), method.getName(), Arrays.asList(objects));
            // 3、TODO 发生异常后通知管理人员（邮件，短信）进一步处理
        }
    }
}
