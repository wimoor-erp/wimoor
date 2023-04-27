package com.wimoor.amazon.inbound.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inbound.pojo.entity.FBAShipCycle;
import com.wimoor.amazon.inbound.service.IFBAShipCycleService;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "SKU发货头程配置接口")
@RestController
@SystemControllerLog("SKU发货头程配置")
@RequestMapping("/api/v1/skucycle")
@RequiredArgsConstructor
public class FBAShipCycleController {
	final IFBAShipCycleService iFBAShipCycleService;
	final IAmazonAuthorityService iAmazonAuthorityService;
	final IAmzProductSalesPlanService iAmzProductSalesPlanService;
	final IMarketplaceService iMarketplaceService;
	@ApiOperation("更新SKU安全库")
    @PostMapping("/updateStockCycleTranstype")
	public Result<?> updateStockCycleSku(@RequestBody FBAShipCycle cycle){
	    UserInfo user = UserInfoContext.get();
	    int result = iFBAShipCycleService.updateStockCycleTransType( cycle , user);
		if(result>0) {
			iAmzProductSalesPlanService.refreshData(cycle.getGroupid().toString(),cycle.getMarketplaceid(),cycle.getSku());
			return Result.success();
		}else {
			return Result.failed();
		}
	}
  
	@SystemControllerLog("更新仓库备货周期和最小发货周期")
	@GetMapping("/updateStockByChange")
	public Result<Map<String,Object>> updateStockAction(String marketplaceid,String groupid,String sku,String cycle,String mincycle, String fee){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		Integer stockcycle = 0;
		Integer mincycles=0;
		BigDecimal fees=new BigDecimal(0);
		if(StrUtil.isNotEmpty(cycle)) {
			stockcycle = Integer.parseInt(cycle);
		}
		if(StrUtil.isNotEmpty(mincycle)) {
			mincycles = Integer.parseInt(mincycle);
		}
		if(StrUtil.isNotEmpty(fee)) {
			fees = new BigDecimal(fee);
		}
		int result = iFBAShipCycleService.updateStockCycle(groupid,marketplaceid,sku,stockcycle,mincycles,fees,userinfo);
		if(result>0){
			iAmzProductSalesPlanService.refreshData(groupid,marketplaceid,sku);
			map.put("msg", "操作成功");
			map.put("type", "success");
		}else{
			map.put("msg", "操作失败");
			map.put("type", "error");
		}
		return Result.success(map);
	}
}
