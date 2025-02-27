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
import com.wimoor.erp.stock.service.IStocktakingShelfService;
import com.wimoor.erp.stock.service.IStocktakingWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfService;

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
@RequestMapping("/api/v1/inventory/stockTaking/stocktakingShelf")
@RequiredArgsConstructor
public class StocktakingShelfController {
    final IStocktakingShelfService iStocktakingShelfService;
    final IStocktakingWarehouseService iStocktakingWarehouseService;
    final IWarehouseShelfService iWarehouseShelfService;
    
    @PostMapping("/listshelf")
  	public Result<?> listShelfAction(@RequestBody List<String> shelflist){
    	List<Map<String,Object>> result=new LinkedList<Map<String,Object>>();
      	for(String item:shelflist) {
      		Map<String,Object> resultitem=new HashMap<String,Object>();
      		String name=iWarehouseShelfService.getAllParentName(item);
      		resultitem.put("name", name);
      		resultitem.put("id",item);
      		resultitem.put("stocktakingid","");
      		result.add(resultitem);
      	}
  		return Result.success(result);
  	}
}

