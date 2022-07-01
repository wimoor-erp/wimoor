package com.wimoor.amazon.feed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.amazon.orders.service.IAmzOrderMainService;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 订单抓取 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Api(tags = "Feed接口")
@RestController
@RequestMapping("/api/v0/feed")
public class FeedController{
	 
	@Autowired
	ISubmitfeedService iSubmitfeedService;
	@Autowired
	IAmazonAuthorityService iAmazonAuthorityService;
	
	   @ApiOperation(value = "更新未出账账期")
	   @GetMapping("/process")
	   public Result<?> processAction() {
	       iAmazonAuthorityService.executTask(iSubmitfeedService);
	       return Result.judge(true);
	   }
 
}

