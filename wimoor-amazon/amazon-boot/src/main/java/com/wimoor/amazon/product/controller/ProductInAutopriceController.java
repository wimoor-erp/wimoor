package com.wimoor.amazon.product.controller;


import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.product.pojo.entity.ProductInAutoprice;
import com.wimoor.amazon.product.service.IProductInAutopriceService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-20
 */
 
@Api(tags = "产品自动调价改价接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product/autoprice")
public class ProductInAutopriceController {
	final IProductInAutopriceService iProductInAutopriceService;
	
    @ApiOperation(value = "保存调价")
    @PostMapping("/save")
    public Result<?> saveAction(@RequestBody  ProductInAutoprice  dto) {
    	UserInfo user = UserInfoContext.get();
    	dto.setCreator(user.getId());
    	dto.setOperator(user.getId());
    	dto.setOpttime(new Date());
    	dto.setCreatetime(new Date());
        return Result.success(iProductInAutopriceService.save(dto));
    }
    
    @ApiOperation(value = "保存调价")
    @PostMapping("/savebatch")
    public Result<?> saveBatchAction(@RequestBody  List<ProductInAutoprice>  dto) {
    	UserInfo user = UserInfoContext.get();
    	for(ProductInAutoprice item:dto) {
    		item.setCreator(user.getId());
    		item.setOperator(user.getId());
        	item.setOpttime(new Date());
        	item.setCreatetime(new Date());
    	}
        return Result.success(iProductInAutopriceService.saveOrUpdateBatch(dto));
    }
    
    @ApiOperation(value = "保存调价")
    @PostMapping("/update")
    public Result<?> updateAction(@RequestBody ProductInAutoprice  dto) {
    	UserInfo user = UserInfoContext.get();
    	dto.setCreator(user.getId());
    	dto.setOperator(user.getId());
    	dto.setOpttime(new Date());
    	dto.setCreatetime(new Date());
        return Result.success(iProductInAutopriceService.save(dto));
    }
    
    @ApiOperation(value = "保存调价")
    @GetMapping
    public Result<?> getAction(String pid) {
        return Result.success(iProductInAutopriceService.getById(pid));
    }
}

