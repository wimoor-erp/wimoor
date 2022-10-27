package com.wimoor.erp.config;

import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
import io.seata.tm.api.GlobalTransactionContext;

@Configuration
public class FeignConfiguration implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(RootContext.KEY_XID.toLowerCase(), GlobalTransactionContext.getCurrentOrCreate().getXid());
    }
}
