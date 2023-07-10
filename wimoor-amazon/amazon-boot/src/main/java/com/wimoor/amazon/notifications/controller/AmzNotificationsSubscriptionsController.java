package com.wimoor.amazon.notifications.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
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
@RequestMapping("/amzNotificationsSubscriptions")
@RequiredArgsConstructor
public class AmzNotificationsSubscriptionsController {

}

