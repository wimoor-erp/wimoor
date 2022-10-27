package com.wimoor.amazon.auth.controller;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonSellerMarketService;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
/**
 * <p>
 * 授权对应区域客户所有绑定的站点 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-25
 */
@Api(tags = "授权接口")
@RestController
@RequestMapping("/api/v1/amazonSellerMarket")
@Component("amazonSellerMarketController")
//@Slf4j
@RequiredArgsConstructor
public class AmazonSellerMarketController {
	 private final IAmazonAuthorityService iAmazonAuthorityService;
	 private final IAmazonSellerMarketService iAmazonSellerMarketService;
    /**
     * 刷新授权任务
     */
    @ApiOperation(value = "刷新授权用户的站点")
    @GetMapping("/refreshMarket")
    public Result<?> refreshMarket() {
    	iAmazonAuthorityService.executTask(iAmazonSellerMarketService);
        return Result.judge(true);
    }
    
    @ApiOperation(value = "刷新授权用户的站点")
    @GetMapping("/refreshMarketByAuth")
    public Result<?> refreshMarketByAuth(String authid) {
    	AmazonAuthority auth=iAmazonAuthorityService.getById(authid);
    	iAmazonSellerMarketService.refreshMarketByAuth(auth);
        return Result.judge(true);
    }
}

