package com.wimoor.erp.stock.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.erp.stock.pojo.entity.StockTakingItemShelf;
import com.wimoor.erp.stock.service.IStockTakingItemShelfService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-03
 */
@Api(tags = "盘点单接口")
@RestController
@RequestMapping("/api/v1/inventory/stockTaking/shelf")
@RequiredArgsConstructor
public class StocktakingItemShelfController {
	final IStockTakingItemShelfService iStockTakingItemShelfService;
	@PostMapping("/save")
	public Result<?> saveData(@RequestBody StockTakingItemShelf dto){
		iStockTakingItemShelfService.save(dto);
		return Result.success(dto);
	}
	@PostMapping("/update")
	public Result<?> updateData(@RequestBody StockTakingItemShelf dto){
		iStockTakingItemShelfService.updateById(dto);
		return Result.success(dto);
	}
	
	@PostMapping("/delete")
	public Result<?> deleteData(@RequestBody StockTakingItemShelf dto){
		iStockTakingItemShelfService.removeById(dto.getId());
		return Result.success(dto);
	}
}

