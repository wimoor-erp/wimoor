package com.wimoor.erp.inventory.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.material.service.IMaterialService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
@Api(tags = "仓存接口")
@RestController
@RequestMapping("/api/v1/inventory/manager")
@RequiredArgsConstructor
public class InventoryManagerController {
	@Resource
	IInventoryFormAgentService iInventoryFormAgentService;
	final IMaterialService  iMaterialService;
	@PostMapping( "outbound")
	@Transactional
	public  Result<?> outbound(@ApiParam("操作DTO")@RequestBody List<InventoryParameter>   paramList) throws BizException {
		return Result.judge(iInventoryFormAgentService.outbound(paramList));
	}
	
	@PostMapping( "undo_outbound")
	@Transactional
	public Result<?> undoOutbound(@ApiParam("操作DTO")@RequestBody List<InventoryParameter>   paramList) throws BizException {
		return Result.judge(iInventoryFormAgentService.undoOutbound(paramList));
	}
	
	@PostMapping( "out")
	@Transactional
	public  Result<?> out(@ApiParam("操作DTO")@RequestBody List<InventoryParameter>   paramList) throws BizException {
		return Result.judge(iInventoryFormAgentService.out(paramList));
	}
	
	@PostMapping( "undo_out")
	@Transactional
	public Result<?> undoOut(@ApiParam("操作DTO")@RequestBody List<InventoryParameter>   paramList) throws BizException {
		return Result.judge(iInventoryFormAgentService.undoOut(paramList));
	}
	 
}
