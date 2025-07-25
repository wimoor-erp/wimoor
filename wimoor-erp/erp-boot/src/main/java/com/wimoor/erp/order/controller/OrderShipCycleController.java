package com.wimoor.erp.order.controller;

import cn.hutool.core.util.StrUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.order.pojo.entity.OrderShipCycle;
import com.wimoor.erp.order.service.IOrderShipCycleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "SKU发货头程配置接口")
@RestController
@SystemControllerLog("SKU发货头程配置")
@RequestMapping("/api/v1/skucycle")
@RequiredArgsConstructor
public class OrderShipCycleController {
	final IOrderShipCycleService iOrderShipCycleService;
	@ApiOperation("更新SKU安全库")
    @PostMapping("/updateStockCycleTranstype")
	public Result<?> updateStockCycleSku(@RequestBody OrderShipCycle cycle){
	    UserInfo user = UserInfoContext.get();
	    int result = iOrderShipCycleService.updateStockCycleTransType( cycle , user);
		if(result>0) {
			//iAmzProductSalesPlanService.refreshData(cycle.getGroupid().toString(),cycle.getMarketplaceid(),cycle.getSku());
			return Result.success();
		}else {
			return Result.failed();
		}
	}
	@SystemControllerLog("更新仓库备货周期和最小发货周期")
	@GetMapping("/updateStock")
	public Result<Map<String,Object>> updateStockAction(String warehouseid,String sku,String cycle){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		Integer stockcycle = 0;
		Integer mincycles=0;
		BigDecimal fees=new BigDecimal(0);
		if(StrUtil.isNotEmpty(cycle)) {
			stockcycle = Integer.parseInt(cycle);
		}
		int result = iOrderShipCycleService.updateStockCycle(warehouseid,sku,stockcycle,userinfo);
		if(result>0){
			//iAmzProductSalesPlanService.refreshData(groupid,marketplaceid,sku);
			map.put("msg", "操作成功");
			map.put("type", "success");
		}else{
			map.put("msg", "操作失败");
			map.put("type", "error");
		}
		return Result.success(map);
	}
	@SystemControllerLog("更新仓库备货周期和最小发货周期")
	@GetMapping("/updateStockByChange")
	public Result<Map<String,Object>> updateStockAction(String warehouseid,String sku,String cycle,String mincycle, String fee){
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
		int result = iOrderShipCycleService.updateStockCycle(warehouseid,sku,stockcycle,mincycles,fees,userinfo);
		if(result>0){
			//iAmzProductSalesPlanService.refreshData(groupid,marketplaceid,sku);
			map.put("msg", "操作成功");
			map.put("type", "success");
		}else{
			map.put("msg", "操作失败");
			map.put("type", "error");
		}
		return Result.success(map);
	}
}
