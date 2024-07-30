package com.wimoor.amazon.inbound.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.inbound.pojo.entity.AmzShipFulfillmentCenter;
import com.wimoor.amazon.inbound.service.IAmzShipFulfillmentCenterService;
import com.wimoor.amazon.profit.pojo.dto.ProfitSettingDTO;
import com.wimoor.common.result.Result;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-12-29
 */
@Api(tags = "FBA收货中心")
@RestController
@RequestMapping("/api/v1/amzShipFulfillmentCenter")
@RequiredArgsConstructor
public class AmzShipFulfillmentCenterController {
	final IAmzShipFulfillmentCenterService iAmzShipFulfillmentCenterService;
	@ApiOperation(value = "获取列表")
	@GetMapping("/list")
	public Result<List<AmzShipFulfillmentCenter>> list() {
	        return Result.success(iAmzShipFulfillmentCenterService.list());
	    }
	
	@PostMapping("/getpage")
	public Result<Page<AmzShipFulfillmentCenter>> getPageAction(@RequestBody ProfitSettingDTO dto)  {
		LambdaQueryWrapper<AmzShipFulfillmentCenter> query = new LambdaQueryWrapper<AmzShipFulfillmentCenter>();
		if(StrUtil.isNotBlank(dto.getCountry())) {
			query.eq(AmzShipFulfillmentCenter::getCountry, dto.getCountry());
		}
		if(StrUtil.isNotBlank(dto.getSearch())) {
			query.like(AmzShipFulfillmentCenter::getCode,"%"+ dto.getSearch().trim()+"%");
		}
		Page<AmzShipFulfillmentCenter> list = iAmzShipFulfillmentCenterService.page(dto.getPage(),query);
		return Result.success(list);
	}
	
	@PostMapping("/save")
	public Result<?> saveAction(@RequestBody AmzShipFulfillmentCenter fba)  {
		return Result.success(iAmzShipFulfillmentCenterService.save(fba));
	}
	
	@PostMapping("/update")
	public Result<?> updateAction(@RequestBody AmzShipFulfillmentCenter fba)  {
		return Result.success(iAmzShipFulfillmentCenterService.updateById(fba));
	}
	
	@PostMapping("/delete")
	public Result<?> deleteAction(@RequestBody AmzShipFulfillmentCenter fba)  {
		return Result.success(iAmzShipFulfillmentCenterService.removeById(fba.getCode()));
	}
	 
}

