package com.wimoor.erp.warehouse.controller;


import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.warehouse.pojo.dto.ShelfInvListDto;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptRecord;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryOptRecordService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 操作记录 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Api(tags = "库位库存操作记录接口")
@RestController
@RequestMapping("/api/v1/warehouse/shelfInventoryOptRecord")
@RequiredArgsConstructor
public class WarehouseShelfInventoryOptRecordController {
	final IWarehouseShelfInventoryOptRecordService iWarehouseShelfInventoryOptRecordService;
	
	@GetMapping
	public Result<List<WarehouseShelfInventoryOptRecord>> getRecord(String formid, String formtype,String shelfid) {
		   UserInfo user = UserInfoContext.get();
		   List<WarehouseShelfInventoryOptRecord> result = iWarehouseShelfInventoryOptRecordService.getRecord(user.getCompanyid(), formid, formtype,shelfid);
		return Result.success(result);
		   
	}
	
	@PostMapping("/getOptList")
	public Result<?> getOptListAction(@ApiParam("查询DTO")@RequestBody ShelfInvListDto condition) {
	   UserInfo user = UserInfoContext.get();
	   if(StrUtil.isNotEmpty(condition.getSearch())) {
		   condition.setSearch("%"+condition.getSearch()+"%");
	   }
	   if(StrUtil.isEmpty(condition.getWarehouseid())) {
		   condition.setWarehouseid(null);
	   }
	   IPage<Map<String, Object>> result = iWarehouseShelfInventoryOptRecordService.getOptList(condition,user.getCompanyid());
	   return Result.success(result);
	}
}

