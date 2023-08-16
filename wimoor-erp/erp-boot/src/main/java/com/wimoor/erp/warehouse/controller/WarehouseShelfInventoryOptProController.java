package com.wimoor.erp.warehouse.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventoryOptRecordVo;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryOptRecordService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 预操作，此表内不存储任何记录。当预操作结束后自动删除 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Api(tags = "库位库存操作接口")
@RestController
@RequestMapping("/api/v1/warehouse/shelfInventoryOptPro")
@RequiredArgsConstructor
public class WarehouseShelfInventoryOptProController {
	final IWarehouseShelfInventoryOptRecordService iWarehouseShelfInventoryOptRecordService;
	    
	    @ApiOperation("查询当前库位中操作记录的情况")
	    @GetMapping("/list")
	    public Result<List<WarehouseShelfInventoryOptRecordVo>> getShelfInvAction(String formid,String materialid,String formtype){
	    	UserInfo user = UserInfoContext.get();
	    	String shopid=user.getCompanyid();
	           return  Result.success(iWarehouseShelfInventoryOptRecordService.getRecordVo(shopid, formid,formtype, materialid));
	    }
 
}

