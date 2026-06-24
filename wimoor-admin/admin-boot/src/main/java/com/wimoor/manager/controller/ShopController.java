package com.wimoor.manager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.common.result.Result;
import com.wimoor.manager.pojo.entity.Shop;
import com.wimoor.manager.service.IShopService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * <p>
 * 店铺表 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Api(tags = "店铺接口")
@RestController
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor
public class ShopController {
	final IShopService iShopService;
	
	@GetMapping("/list")
	public Result<List<Shop>> list(String search, Integer pageSize)  {
		LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
		if (search != null && !search.isEmpty()) {
			queryWrapper.like(Shop::getName, search.trim()+"%");
		}
		if (pageSize == null || pageSize <= 0) {
			pageSize = 20;
		}
		queryWrapper.last("LIMIT " + pageSize);
		return Result.success(iShopService.list(queryWrapper));
	}
}