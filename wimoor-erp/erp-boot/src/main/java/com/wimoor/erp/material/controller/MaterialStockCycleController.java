package com.wimoor.erp.material.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.material.pojo.entity.StockCycle;
import com.wimoor.erp.material.service.IStockCycleService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "产品安全库存")
@SystemControllerLog(  "产品安全库存")
@RestController
@RequestMapping("/api/v1/material/stockcycle")
@RequiredArgsConstructor
public class MaterialStockCycleController {
	final IStockCycleService iStockCycleService;

	@SystemControllerLog("更新仓库备货周期和最小补货周期")
	@PostMapping("/updateStockByChange")
	public Result<Boolean> updateStockAction(@RequestBody StockCycle sc){
		UserInfo userinfo = UserInfoContext.get();
		Boolean result=iStockCycleService.updateStockCycle(sc, userinfo);
		return Result.success(result);
	}
}
