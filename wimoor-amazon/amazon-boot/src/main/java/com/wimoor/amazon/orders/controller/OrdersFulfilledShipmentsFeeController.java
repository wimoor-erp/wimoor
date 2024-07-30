package com.wimoor.amazon.orders.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.orders.service.IOrdersFulfilledShipmentsFeeService;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-22
 */
@Api(tags = "报表接口（任务专用）")
@RestController
@Slf4j
@RequestMapping("/api/v0/orders/shipmentsfee")
public class OrdersFulfilledShipmentsFeeController {
	@Autowired
	IOrdersFulfilledShipmentsFeeService iOrdersFulfilledShipmentsFeeService;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	
	@ApiOperation(value = "费用更新")
	@GetMapping("/fee")
	public Result<String> shipmentsFeeAction(String authid) {
		iOrdersFulfilledShipmentsFeeService.orderTransFee(authid);
		return Result.success();
	} 
	@ApiOperation(value = "全局更新")
	@GetMapping("/task")
	public Result<String> taskAction(String authid) {
		List<AmazonAuthority> list = amazonAuthorityService.getAllAuth();
		for(AmazonAuthority item:list) {
			iOrdersFulfilledShipmentsFeeService.orderTransFee(item.getId());
		}
		log.debug("全局更新");
		return Result.success();
	}
}

