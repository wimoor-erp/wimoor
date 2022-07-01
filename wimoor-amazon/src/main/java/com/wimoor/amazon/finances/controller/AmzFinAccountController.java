package com.wimoor.amazon.finances.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.finances.service.IAmzFinAccountService;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-22
 */
@Api(tags = "亚马逊财务接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/amzFinAccount")
public class AmzFinAccountController {
   final IAmzFinAccountService iAmzFinAccountService;
   final IAmazonAuthorityService iAmazonAuthorityService;
   
   @ApiOperation(value = "更新未出账账期")
   @GetMapping("/refreshMarketByAuthId")
   public Result<?> refreshMarketByAuthIdAction(@PathVariable String id) {
   	iAmazonAuthorityService.executTask(iAmzFinAccountService);
       return Result.judge(true);
   }


}

