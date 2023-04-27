package com.wimoor.amazon.notifications.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
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

}

