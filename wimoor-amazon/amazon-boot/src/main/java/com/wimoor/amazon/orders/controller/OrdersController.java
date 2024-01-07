package com.wimoor.amazon.orders.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.orders.service.IAmzOrderItemService;
import com.wimoor.amazon.orders.service.IAmzOrderMainService;
import com.wimoor.amazon.orders.service.IAmzOrderSolicitationsService;
import com.wimoor.amazon.orders.service.IOrderManagerService;
import com.wimoor.amazon.summary.service.ISummaryOrderReportService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.OSSApiService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单抓取 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Api(tags = "报表接口（任务专用）")
@RestController
@Component("ordersController")
@Slf4j
@RequestMapping("/api/v0/orders")
public class OrdersController{
	 
	@Autowired
	IAmzOrderMainService amzOrderMainService;
	@Autowired
	IAmzOrderItemService amzOrderItemService;
	@Autowired
	IMarketplaceService marketplaceService;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Resource
	IOrderManagerService orderManagerService;
	@Resource
	IAmazonGroupService amazonGroupService;
	@Resource
	ISerialNumService serialNumService;
	@Resource
	IPictureService  pictureService;
	@Resource
	OSSApiService ossApiService;
	@Resource
	IAmzOrderSolicitationsService iAmzOrderSolicitationsService;
	@Resource
	ISummaryOrderReportService summaryOrderReportService;
	@ApiOperation(value = "抓取产品订单")
	@GetMapping("/refreshOrder")
	public Result<String> refreshOrderAction() {
		log.info("抓取产品订单requestReportAction-----"+new Date());
		amazonAuthorityService.executTask(amzOrderMainService);
		return Result.success();
	}
	@ApiOperation(value = "抓取产品订单")
	@GetMapping("/refreshOrderByAuth")
	public Result<String> refreshOrderByAuthAction(String authid) {
		log.info("抓取产品订单requestReportAction-----"+new Date());
		AmazonAuthority auth = amazonAuthorityService.getById(authid);
		amzOrderMainService.runApi(auth);
		return Result.success();
	}
	
	@GetMapping("/refreshOrdersItem")
	public Result<?> refreshOrdersItemAction() {
		log.info("抓取产品订单Item ordersItemAction-----"+new Date());
		amazonAuthorityService.executTask(amzOrderItemService);
		return Result.success();
	}
	@GetMapping("/refreshOrdersItemByAuth")
	public Result<?> refreshOrdersItemByAuthAction(String authid) {
		log.info("抓取产品订单Item ordersItemAction-----"+new Date());
		AmazonAuthority auth = amazonAuthorityService.getById(authid);
		amzOrderItemService.runApi(auth);
		return Result.success();
	}
	
	@GetMapping("/removeOrdersDataArchive")
	public Result<?> removeOrdersDataArchiveAction() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				amzOrderMainService.removeDataArchive();
				amzOrderItemService.removeDataArchive();
				summaryOrderReportService.systemDataHandler();
			}
		}).start();
		return Result.success();
	}

}

