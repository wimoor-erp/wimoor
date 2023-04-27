package com.wimoor.admin.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;


/**
 * <p>
 * 用户数据权限，放在用户信息中，登录后将在所有模块生效 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-29
 */

@Api(tags = "菜单喜好接口")
@RestController
@RequestMapping("/api/v1/sysUserDatalimit")
@RequiredArgsConstructor
public class SysUserDatalimitController {

}

