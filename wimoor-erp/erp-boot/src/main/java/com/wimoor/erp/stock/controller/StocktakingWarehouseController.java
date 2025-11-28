package com.wimoor.erp.stock.controller;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.erp.stock.service.IStocktakingWarehouseService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-05
 */
 
@Api(tags = "盘点单接口")
@RestController
@RequestMapping("/api/v1/inventory/stockTaking/stocktakingWarehouse")
@RequiredArgsConstructor
public class StocktakingWarehouseController {
	final IStocktakingWarehouseService iStocktakingWarehouseService;
	final IWarehouseService iWarehouseService;

	    @PostMapping("/listwarehouse")
	  	public Result<?> listWarehouseDataAction(@RequestBody List<String> warehosuelist){
	    	List<Map<String,Object>> result=new LinkedList<Map<String,Object>>();
	      	for( String item:warehosuelist) {
	      		Map<String,Object> resultitem=new HashMap<String,Object>();
	      		Warehouse warehouse = iWarehouseService.getWarehouseByid(item);
	      		resultitem.put("name", warehouse.getName());
	      		resultitem.put("id", warehouse.getId());
	      		resultitem.put("stocktakingid","");
	      		result.add(resultitem);
	      	}
	  		return Result.success(result);
	  	}
}

