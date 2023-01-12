package com.wimoor.amazon.product.controller;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.product.pojo.dto.ShipPlanDTO;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-11-28
 */
@Api(tags = "fba补货计划接口")
@RestController
@RequestMapping("/api/v1/product/salesplan")
@RequiredArgsConstructor
public class AmzProductSalesPlanController {
	final IAmzProductSalesPlanService iAmzProductSalesPlanService;
    @ApiOperation(value = "计划刷新")
    @GetMapping("/refreshPlanData")
    public Result<?> refreshData() {
    	Set<String> param=new HashSet<String>();
    	iAmzProductSalesPlanService.refreshData(param);
    	return Result.success();
    }
    
    @ApiOperation(value = "计划刷新")
    @GetMapping("/refreshDataByGroup")
    public Result<?> refreshDataByGroup(String groupid) {
		//UserInfo user = UserInfoContext.get();
		try {
			iAmzProductSalesPlanService.refreshData(groupid);
		}catch(Exception e) {
			e.printStackTrace();
			throw new BizException("计算异常，请联系管理员");
		}
    	return Result.success();
    }
    
    @ApiOperation(value = "展开的计划")
    @GetMapping("/getExpandCountryData")
    public Result<?> getExpandCountryData(String groupid,String warehouseid,String msku,Boolean iseu) {
		UserInfo user = UserInfoContext.get();
		try {
			return Result.success(iAmzProductSalesPlanService.ExpandCountryDataByGroup(user.getCompanyid(),groupid,warehouseid,msku,iseu));
		}catch(Exception e) {
			e.printStackTrace();
			throw new BizException("计算异常，请联系管理员");
		}
    }
    
    @ApiOperation(value = "计划刷新")
    @PostMapping("/getShipPlanModel")
    public Result<IPage<Map<String, Object>>> getShipPlanModel(@RequestBody ShipPlanDTO dto) {
    	UserInfo user = UserInfoContext.get();
    	dto.setShopid(user.getCompanyid());
    	List<Map<String, Object>> list = iAmzProductSalesPlanService.getShipPlanModel(dto);
    	return Result.success(dto.getListPage(list));
    }
   

    
}

