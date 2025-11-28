package com.wimoor.manager.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *   前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/api/v1/sysCustomerOrder")
@RequiredArgsConstructor
public class SysCustomerOrderController {

}

