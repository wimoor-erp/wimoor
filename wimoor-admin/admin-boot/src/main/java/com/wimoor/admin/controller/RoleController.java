package com.wimoor.admin.controller;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.admin.pojo.dto.RolePermissionDTO;
import com.wimoor.admin.pojo.entity.SysRole;
import com.wimoor.admin.pojo.entity.SysUserRole;
import com.wimoor.admin.service.ISysMenuService;
import com.wimoor.admin.service.ISysPermissionService;
import com.wimoor.admin.service.ISysRoleMenuService;
import com.wimoor.admin.service.ISysRolePermissionService;
import com.wimoor.admin.service.ISysRoleService;
import com.wimoor.admin.service.ISysUserRoleService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "角色接口")
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final ISysRoleService iSysRoleService;
    private final ISysRoleMenuService iSysRoleMenuService;
    private final ISysUserRoleService iSysUserRoleService;
    private final ISysRolePermissionService iSysRolePermissionService;
    private final ISysPermissionService iSysPermissionService;
    private final ISysMenuService menuService;

    @ApiOperation(value = "列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "limit", value = "每页数量", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "name", value = "角色名称", paramType = "query", dataType = "String"),
    })
    @GetMapping("/page")
    public Result<List<SysRole>> pageList(Integer page, Integer limit, String name) {
        UserInfo user = UserInfoContext.get();
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<SysRole>()
                .like(StrUtil.isNotBlank(name), SysRole::getName, name)
                .eq(SysRole::getShopid, user.getCompanyid())
                .ne(SysRole::getType, "admin")
                .orderByAsc(SysRole::getName);
        Page<SysRole> result = iSysRoleService.page(new Page<>(page, limit), queryWrapper);
        return Result.success(result.getRecords(), result.getTotal());
    }


    @ApiOperation(value = "角色列表")
    @GetMapping
    public Result<List<SysRole>> list() {
        UserInfo user = UserInfoContext.get();
        List<SysRole> list = iSysRoleService.list(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getShopid,user.getCompanyid())
                .ne(SysRole::getType, "admin")
                .orderByAsc(SysRole::getName));
        return Result.success(list);
    }


    @ApiOperation(value = "新增角色")
    @ApiImplicitParam(name = "role", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysRole")
    @PostMapping
    public Result<?> add(@RequestBody SysRole role) {
    	   UserInfo user = UserInfoContext.get();
        long count = iSysRoleService.count(new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getName, role.getName())
                    .eq(SysRole::getShopid, user.getCompanyid())
        );
        Assert.isTrue(count == 0, "角色名称重复，请检查！");
        role.setShopid(new BigInteger(user.getCompanyid()));
        role.setType("actor");
        role.setIssystem(false);
        boolean result = iSysRoleService.save(role);
        if (result) {
            iSysPermissionService.refreshPermRolesRules();
        }
        return Result.judge(result);
    }
    
    @ApiOperation(value = "角色详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "path", dataType = "Long"),
    })
    @GetMapping(value = "/{id}")
    public Result<SysRole> detail(@PathVariable String id) {
        SysRole result = iSysRoleService.getById(id);
        return Result.success(result);
    }


    @ApiOperation(value = "修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "role", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysRole")
    })
    @PutMapping(value = "/{id}")
    public Result<?> update(
            @PathVariable BigInteger id,
            @RequestBody SysRole role) {
    	UserInfo user = UserInfoContext.get();
        long count = iSysRoleService.count(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getShopid, user.getCompanyid())
                .eq(SysRole::getName, role.getName())
                .ne(SysRole::getId, id)
        );
        Assert.isTrue(count == 0, "角色名称，请检查！");
        boolean result = iSysRoleService.updateById(role);
        if (result) {
            iSysPermissionService.refreshPermRolesRules();
        }
        return Result.judge(result);
    }

    @ApiOperation(value = "删除角色")
    @ApiImplicitParam(name = "ids", value = "以,分割拼接字符串", required = true, dataType = "String")
    @DeleteMapping("/{ids}")
    public Result<?> delete(@PathVariable String ids) {
    	List<String> list = Arrays.asList(ids.split(","));
    	List<BigInteger> idsList=new LinkedList<BigInteger>();
    	for(String id:list) {
    		long count=iSysUserRoleService.count(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, id));
    		if(count>0) {
    			throw new BizException("该角色已被用户使用，无法删除。请先将用户上面对应角色权限去掉");
    		}
    		if(StrUtil.isBlankOrUndefined(ids)) {
    			throw new BizException("该角色已被用户使用，无法删除。请先将用户上面对应角色权限去掉");
    		}
    		idsList.add(new BigInteger(id));
    	}
        boolean result = iSysRoleService.delete(idsList);
        if (result) {
            iSysPermissionService.refreshPermRolesRules();
        }
        return Result.judge(result);
    }

    @ApiOperation(value = "选择性修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "role", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysRole")
    })
    @PatchMapping(value = "/{id}")
    public Result<?> patch(@PathVariable BigInteger id, @RequestBody SysRole role) {
        LambdaUpdateWrapper<SysRole> updateWrapper = new LambdaUpdateWrapper<SysRole>()
                .eq(SysRole::getId, id)
                .set(role.getName() != null, SysRole::getName, role.getName());
        boolean result = iSysRoleService.update(updateWrapper);
        if (result) {
            iSysPermissionService.refreshPermRolesRules();
        }
        return Result.judge(result);
    }

    @ApiOperation(value = "获取角色拥有的菜单ID集合")
    @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "path", dataType = "Long")
    @GetMapping("/{id}/menus")
    public Result<List<BigInteger>> listRoleMenu(@PathVariable("id") BigInteger roleId) {
        List<BigInteger> menuIds = iSysRoleMenuService.listMenuIds(roleId);
        return Result.success(menuIds);
    }

    @ApiOperation(value = "获取角色拥有的权限ID集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "path", dataType = "Integer"),
            @ApiImplicitParam(name = "menuId", value = "菜单ID", paramType = "query", dataType = "Integer"),
    })
    @GetMapping("/{id}/permissions")
    public Result<?> listRolePermission(@PathVariable("id") BigInteger roleId, BigInteger menuId) {
        List<BigInteger> permissionIds = iSysRolePermissionService.listPermissionId(menuId, roleId);
        return Result.success(permissionIds);
    }

    @ApiOperation(value = "修改角色菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "role", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysRole")
    })
    @PutMapping(value = "/{id}/menus")
    public Result<?> updateRoleMenu(
            @PathVariable("id") BigInteger roleId,
            @RequestBody SysRole role) {

        List<BigInteger> menuIds = role.getMenuIds();
        boolean result = iSysRoleMenuService.update(roleId, menuIds);
        if (result) {
            iSysPermissionService.refreshPermRolesRules();
            menuService.cleanCache();
        }
        return Result.judge(result);
    }

    @ApiOperation(value = "修改角色权限")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "path", dataType = "Long"),
         @ApiImplicitParam(name = "rolePermission", value = "实体JSON对象", required = true, paramType = "body", dataType = "RolePermissionDTO")
    })
    @PutMapping(value = "/{id}/permissions")
    public Result<?> updateRolePermission(@PathVariable("id") BigInteger roleId, @RequestBody RolePermissionDTO rolePermission) {
        rolePermission.setRoleId(roleId);
        boolean result = iSysRolePermissionService.update(rolePermission);
        if (result) {
            iSysPermissionService.refreshPermRolesRules();
        }
        return Result.judge(result);
    }
}
