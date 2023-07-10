package com.wimoor.admin.controller;


import java.math.BigInteger;
import java.util.Arrays;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.admin.pojo.entity.SysUserGroup;
import com.wimoor.admin.service.ISysUserGroupService;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
 * <p>
 * 用户客户对店铺的权限绑定 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-29
 */

@Api(tags = "菜单喜好接口")
@RestController
@RequestMapping("/api/v1/sysUserGroup")
@RequiredArgsConstructor
public class SysUserGroupController {
	
final ISysUserGroupService iSysUserGroupService;

@ApiOperation(value = "详情")
@ApiImplicitParam(name = "id", value = "权限ID", required = true, paramType = "path", dataType = "Long")
@GetMapping("/{id}")
public Result<?> detail(@PathVariable BigInteger id) {
      SysUserGroup sysUserGroup = iSysUserGroupService.getById(id);
    return Result.success(sysUserGroup);
}

@ApiOperation(value = "新增")
@PostMapping
public Result<?> add(@RequestBody SysUserGroup permission) {
	
    boolean result = iSysUserGroupService.save(permission);
    return Result.judge(result);
}

 
@ApiOperation(value = "删除")
@ApiImplicitParam(name = "ids", value = "id集合", required = true, paramType = "query", dataType = "Long")
@DeleteMapping("/{ids}")
public Result<?> delete(@PathVariable String ids) {
    boolean status = iSysUserGroupService.removeByIds(Arrays.asList(ids.split(",")));
    return Result.judge(status);
}

}

