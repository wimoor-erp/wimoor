package com.wimoor.erp.warehouse.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 仓库货柜 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Api(tags = "仓库接口")
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {
	
	final IWarehouseService warehouseService;
	

	@ApiOperation("仓库列表[包含子仓位]")
	@GetMapping("/list")
	public Result<List<Warehouse>> findWarehouseList(){
		UserInfo userinfo = UserInfoContext.get();
		List<Warehouse> list=warehouseService.getWarehouseTreeList(userinfo);
		return Result.success(list);
	}
	
}

