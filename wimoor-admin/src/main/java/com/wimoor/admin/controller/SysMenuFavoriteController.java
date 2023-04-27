package com.wimoor.admin.controller;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.admin.pojo.entity.SysMenu;
import com.wimoor.admin.pojo.entity.SysMenuFavorite;
import com.wimoor.admin.service.ISysMenuFavoriteService;
import com.wimoor.admin.service.ISysMenuService;
import com.wimoor.common.result.Result;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 菜单收藏 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-26
 */
@Api(tags = "菜单喜好接口")
@RestController
@RequestMapping("/api/v1/sysMenuFavorite")
@RequiredArgsConstructor
public class SysMenuFavoriteController {

	private final ISysMenuFavoriteService iSysMenuFavriteService ;
    private final ISysMenuService menuService;
    
    @ApiOperation(value = "菜单表格（Table）列表")
    @GetMapping("/table")
    public Result<List<SysMenu>> getList() {
        List<SysMenuFavorite> tableList = iSysMenuFavriteService.list();
        List<SysMenu> menuList=new ArrayList<SysMenu>();
        Optional.ofNullable(tableList).orElse(new ArrayList<>())
        .stream()
        .forEach(menuFavorite -> {
        	SysMenu menu = menuService.getById(menuFavorite.getMenuid());
        	menuList.add(menu);
        });
        
        return Result.success(menuList);
    }

 

    @ApiOperation(value = "新增")
    @PostMapping
    public Result<String> add(@RequestBody SysMenuFavorite menuFavorite) {
         iSysMenuFavriteService.save(menuFavorite);
        return Result.success(menuFavorite.getId().toString());
    }

    @ApiOperation(value = "修改")
    @PutMapping(value = "/{id}")
    public Result<String> update(@PathVariable String id, @RequestBody SysMenuFavorite menuFavorite) {
    	menuFavorite.setId(new BigInteger(id));
         iSysMenuFavriteService.updateById(menuFavorite);
        return Result.success(menuFavorite.getId().toString());
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "ids", value = "ID，多个以英文逗号,分割拼接", required = true, paramType = "query", dataType = "String")
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@PathVariable("ids") String ids) {
    	List<String> idlist = null;
        if(StrUtil.isEmpty(ids)) {
        	return Result.failed();
        }else {
        	idlist=Arrays.asList(ids.split(","));
        }
        boolean status= iSysMenuFavriteService.removeBatchByIds(idlist);
        return Result.judge(status);
    }
}

