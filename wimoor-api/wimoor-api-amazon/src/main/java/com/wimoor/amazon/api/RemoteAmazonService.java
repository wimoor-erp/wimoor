package com.wimoor.amazon.api;

import com.wimoor.amazon.api.factory.RemoteAmazonFallbackFactory;
import com.wimoor.common.ServiceNameConstants;
import com.wimoor.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;



import java.util.List;
import java.util.Map;

@FeignClient(contextId = "remoteAmazonService", value = ServiceNameConstants.AMAZON_SERVICE, fallbackFactory = RemoteAmazonFallbackFactory.class)
public interface RemoteAmazonService
{
    @GetMapping("/amazon/api/v1/amzgroup/list")
    public Result<List<Map<String,Object>>> getAmazonGroupAction();

}
