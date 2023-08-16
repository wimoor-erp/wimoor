package com.wimoor.amazon.finances.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.finances.pojo.entity.OrdersFinancial;
import com.wimoor.amazon.finances.service.IOrdersFinancialService;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-22
 */
@Api(tags = "亚马逊订单财务接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ordersFinancial")
public class OrdersFinancialController {
	   final IOrdersFinancialService iOrdersFinancialService;
	   final IAmazonAuthorityService iAmazonAuthorityService;
	   
	   @ApiOperation(value = "订单财务")
	   @GetMapping("/list")
	   public Result<List<OrdersFinancial>> listAction(@ApiParam("授权ID")@RequestParam String authid, @ApiParam("订单ID")@RequestParam  String orderid) {
	   	AmazonAuthority auth = iAmazonAuthorityService.getById(authid);
	    return Result.success(iOrdersFinancialService.getOrdersFinancialList(auth, orderid));
	   }
}

