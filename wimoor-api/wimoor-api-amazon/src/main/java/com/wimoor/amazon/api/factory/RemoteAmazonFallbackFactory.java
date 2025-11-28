package com.wimoor.amazon.api.factory;

import com.wimoor.common.result.Result;
import com.wimoor.amazon.api.RemoteAmazonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 日志服务降级处理
 * 
 * @author wimoor
 */
@Component
public class RemoteAmazonFallbackFactory implements FallbackFactory<RemoteAmazonService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteAmazonFallbackFactory.class);
    @Override
    public RemoteAmazonService create(Throwable throwable) {
        log.error("亚马逊服务调用失败:{}", throwable.getMessage());
        return new RemoteAmazonService() {
            @Override
            public Result<List<Map<String,Object>>> getAmazonGroupAction() {
                return Result.failed("获取亚马逊组失败:" + throwable.getMessage());
            }
        };
    }

}
