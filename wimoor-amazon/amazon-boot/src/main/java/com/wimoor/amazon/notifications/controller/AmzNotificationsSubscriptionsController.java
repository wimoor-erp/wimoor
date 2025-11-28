package com.wimoor.amazon.notifications.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.notifications.service.IAmzNotificationsSubscriptionsService;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
 * <p>
 * 订阅消息对象 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-27
 */
@RestController
@Api(tags = "Subscriptions接口")
@RequestMapping("/api/v1/amzNotificationsSubscriptions")
@RequiredArgsConstructor
public class AmzNotificationsSubscriptionsController {
	final IAmzNotificationsSubscriptionsService iAmzNotificationsSubscriptionsService;
    
    @ApiOperation(value = "删除Subscriptions")
	@GetMapping("/delete")
	public Result<?> deleteAction(String destinationid){
    	iAmzNotificationsSubscriptionsService.deleteSubscriptions(destinationid);
		return Result.judge(true);
	}
}

