package com.wimoor.amazon.notifications.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.notifications.service.IAmzNotificationsDestinationService;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
 * <p>
 * 亚马逊Destination 亚马逊消息接受对象 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-27
 */
@RestController
@Api(tags = "Destination接口")
@RequestMapping("/api/v1/amzNotificationsDestination")
@RequiredArgsConstructor
public class AmzNotificationsDestinationController {
	final  IAmzNotificationsDestinationService  iAmzNotificationsDestinationService; 
	    @ApiOperation(value = "删除Destination")
		@GetMapping("/delete")
		public Result<?> deleteAction(String id){
		    iAmzNotificationsDestinationService.deleteDestination(id);
			return Result.judge(true);
		}
}

