package com.wimoor.amazon.orders.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.common.service.ISummaryDataService;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrderSummaryDTO;
import com.wimoor.amazon.orders.service.ISummaryAllService;
import com.wimoor.amazon.summary.service.IOrdersSumService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
 
 


@Api(tags = "今日订单接口")
@RestController
@RequestMapping("/api/v0/orders/osum")
@SystemControllerLog( "订单汇总")
public class OrderSummaryController {


	@Resource
	ISummaryDataService summaryDataService;
	@Resource
	IOrdersSumService ordersSumService;
	@Resource
	ISummaryAllService summaryAllService;
	
	@PostMapping("/search")
	public Result<Map<String, Object>> getSearchAction(@RequestBody AmazonOrderSummaryDTO model)  {
		UserInfo userinfo = UserInfoContext.get();
		Map<String, Object> resultMap = ordersSumService.searchOrderSummary(userinfo,model);
		return Result.success(resultMap) ;
	}

	@GetMapping(value = "/getOrderChannel")
	public Result<List<Map<String, Object>>> getOrderChannelAction() {
		UserInfo userinfo = UserInfoContext.get();
		return Result.success(ordersSumService.getOrderChannel(userinfo.getCompanyid()));
	}
	
	@PostMapping("/orderDaysSummaryAll")
	public Result<Map<String, Object>> orderDaysSummaryAll(@RequestBody Map<String,Object> model)  {
		Map<String, Object> resultMap = summaryAllService.orderDaysSummaryAll(model);
		return Result.success(resultMap) ;
	}

	@PostMapping("/orderMonthsSummaryAll")
	public Result<Map<String, Object>> orderMonthsSummaryAll(@RequestBody Map<String,Object> model)  {
		Map<String, Object> resultMap = summaryAllService.orderMonthsSummaryAll(model);
		return Result.success(resultMap) ;
	}

	@PostMapping("/orderSummaryAll")
	public Result<BigDecimal> orderSummaryAll(@RequestBody Map<String,Object> model)  {
		 BigDecimal resultMap = summaryAllService.orderSummaryAll(model);
		return Result.success(resultMap) ;
	}

}
