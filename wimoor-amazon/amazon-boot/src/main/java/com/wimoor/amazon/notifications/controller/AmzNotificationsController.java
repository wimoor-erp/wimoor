package com.wimoor.amazon.notifications.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.notifications.service.IAmzNotificationsDestinationService;
import com.wimoor.amazon.notifications.service.IAmzNotificationsSubscriptionsService;
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
 * @since 2022-06-21
 */
@Api(tags = "Notifications接口")
@RestController
@RequestMapping("/api/v1/amzNotifications")
@RequiredArgsConstructor
public class AmzNotificationsController {
	final IAmazonAuthorityService amazonAuthorityService;
	final IAmzNotificationsDestinationService iAmzNotificationsDestinationService;
	final IAmzNotificationsSubscriptionsService iAmzNotificationsSubscriptionsService;
	
	@ApiOperation(value = "刷新接受信息的Destination")
	@GetMapping("/refreshDestination")
	public Result<?> refreshDestinationAction(){
		iAmzNotificationsDestinationService.executTask();
		return Result.judge(true);
	}
	
	@ApiOperation(value = "刷新接受信息的Subscriptions")
	@GetMapping("/refreshSubscriptions")
	public Result<?> refreshSubscriptionsAction(){
		amazonAuthorityService.executTask(iAmzNotificationsSubscriptionsService);
		return Result.judge(true);
	}
}

