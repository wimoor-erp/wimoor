package com.wimoor.amazon.inbound.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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
	@PostMapping("/updateStockByChange")
	public Result<Map<String,Object>> updateStockAction(@RequestBody List<Map<String,Object>> list){
		for(Map<String,Object> param:list){
			String marketplaceid=param.get("marketplaceid")!=null?param.get("marketplaceid").toString():null;
			String groupid=param.get("groupid")!=null?param.get("groupid").toString():null;
			String sku=param.get("sku")!=null?param.get("sku").toString():null;
			String cycle=param.get("cycle")!=null?param.get("cycle").toString():null;
			String mincycle=param.get("mincycle")!=null?param.get("mincycle").toString():null;
			String fee=param.get("fee")!=null?param.get("fee").toString():null;
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
			}
		}
		return Result.success();
	}
}
