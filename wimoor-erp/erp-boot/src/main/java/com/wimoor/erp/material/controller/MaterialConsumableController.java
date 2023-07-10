package com.wimoor.erp.material.controller;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.material.pojo.dto.MaterialConsumableDTO;
import com.wimoor.erp.material.pojo.vo.MaterialConsumableVO;
import com.wimoor.erp.material.service.IMaterialConsumableService;
import com.wimoor.erp.ship.pojo.dto.ConsumableOutFormDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "产品接口")
@RestController
@RequestMapping("/api/v1/material/consumable")
@RequiredArgsConstructor
public class MaterialConsumableController {
	final IMaterialConsumableService iMaterialConsumableService;
	
    @ApiOperation(value = "获取库存耗材列表")
    @PostMapping("/getConsumableList")
	public Result<List<Map<String, Object>>> getConsumableListAction(@RequestBody MaterialConsumableDTO dto) {
		UserInfo user=UserInfoContext.get();
		List<Map<String, Object>> list = iMaterialConsumableService.findConsumableDetailBySkuList(user, dto);
		return Result.success(list);
	}
    
    @ApiOperation(value = "获取库存耗材父亲列表")
    @GetMapping("/getConsumablePList")
	public Result<List<MaterialConsumableVO>> getConsumablePListAction(String materialid,String warehouseid) {
		UserInfo user=UserInfoContext.get();
		List<MaterialConsumableVO> consumablePList=  iMaterialConsumableService.selectConsumableBySubmid(materialid,warehouseid,user.getCompanyid());
		return Result.success(consumablePList);
	}
 
    
    @ApiOperation(value = "保存库存耗材")
    @PostMapping("/saveConsumable")
    @Transactional
	public Result<?> saveInventoryConsumableAction(@RequestBody ConsumableOutFormDTO dto) {
    	UserInfo user=UserInfoContext.get();
    	int res=iMaterialConsumableService.saveInventoryConsumable(user,dto);
		return Result.success(res);
	}
    
}
