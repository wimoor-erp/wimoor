package com.wimoor.amazon.product.controller;

import com.wimoor.amazon.product.service.IAmzAplusContentService;
import com.wimoor.common.result.Result;
import io.seata.common.util.StringUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "产品任务接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product/aplus")
public class AmzAplusContentController {
    private final IAmzAplusContentService amzAplusContentService;
    @GetMapping("/searchContentPublishRecords")
    public Result<?> searchContentPublishRecordsAction(String amazonAuthId, String marketplaceid, String asin, String token) {
        if(StringUtils.isBlank(token)){
            token=null;
        }
        return Result.success(amzAplusContentService.searchContentPublishRecords(amazonAuthId,marketplaceid,asin,token));
    }
}
