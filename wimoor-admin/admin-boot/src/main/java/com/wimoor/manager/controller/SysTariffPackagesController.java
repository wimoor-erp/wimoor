package com.wimoor.manager.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.manager.pojo.entity.SysTariffPackages;
import com.wimoor.manager.service.ISysTariffPackagesService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

import java.util.List;


/**
 * <p>
 * 套餐表 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/api/v1/sysTariffPackages")
@RequiredArgsConstructor
public class SysTariffPackagesController {
	final ISysTariffPackagesService iSysTariffPackagesService;
	@GetMapping("/list")
	public Result<List<SysTariffPackages>> list()  {
	     return Result.success(iSysTariffPackagesService.list());
	}
	
	@GetMapping("/detail")
	public Result<SysTariffPackages> update(String  id)  {
	     return Result.success(iSysTariffPackagesService.getById(id));
	}
	
	@PostMapping("/save")
	public Result<?> save(@RequestBody SysTariffPackages pkg)  {
	     return Result.success(iSysTariffPackagesService.save(pkg));
	}
	
	@PostMapping("/update")
	public Result<?> update(@RequestBody SysTariffPackages pkg)  {
	     return Result.success(iSysTariffPackagesService.updateById(pkg));
	}
	
	@PostMapping("/delete")
	public Result<?> delete(@RequestBody SysTariffPackages pkg)  {
	     return Result.success(iSysTariffPackagesService.removeById(pkg.getId()));
	}
}

