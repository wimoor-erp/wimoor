package com.wimoor.amazon.orders.controller;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.orders.service.*;
import com.wimoor.amazon.summary.service.ISummaryOrderReportService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 订单抓取 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Api(tags = "订单消息接口")
@RestController
@Component("ordersMessageController")
@Slf4j
@RequestMapping("/api/v0/orders/message")
public class OrdersMessageController {
	@Resource
	IAmzOrderMessagingService amzOrderMessagingService;
	@Resource
	IAmazonAuthorityService amazonAuthorityService;
	@ApiOperation(value = "获取订单消息Actions")
	@GetMapping("/getMessagingActionsForOrder")
	public Result<?> refreshOrderAction(String amazonauthid,String amazonOrderId,String marketplaceId) {
		AmazonAuthority auth = amazonAuthorityService.getById(amazonauthid);
		return Result.success(amzOrderMessagingService.getMessagingActionsForOrder(auth,amazonOrderId, marketplaceId));
	}


}

