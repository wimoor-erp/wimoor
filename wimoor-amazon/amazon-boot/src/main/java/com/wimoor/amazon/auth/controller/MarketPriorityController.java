package com.wimoor.amazon.auth.controller;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.pojo.entity.MarketPriority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketPriorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "站点次序")
@RestController
@SystemControllerLog("站点次序")
@RequiredArgsConstructor
@RequestMapping("/api/v1/amzmarketplace/priority")
public class MarketPriorityController {
	final IMarketPriorityService iMarketPriorityService;
	final IMarketplaceService iMarketplaceService;
	@ApiOperation("保存")
	@SystemControllerLog("保存")
    @PostMapping("/save")
	@CacheEvict(value = { "marketplaceCache" }, allEntries = true)
    public Result<String> saveAction(@RequestBody List<MarketPriority> priorityList) {
    	UserInfo userinfo = UserInfoContext.get();
    	return Result.success(iMarketPriorityService.saveMarketPriority(userinfo,priorityList)) ;
	}
	
	@ApiOperation("查看")
    @GetMapping("/list")
	@Cacheable(value = "marketplaceCache")
    public Result<List<Marketplace>> listAction(String groupid) {
    	return Result.success(iMarketPriorityService.findMarketplaceByGroup(groupid)) ;
	}
}
