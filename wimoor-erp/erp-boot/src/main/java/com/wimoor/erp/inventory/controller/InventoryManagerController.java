package com.wimoor.erp.inventory.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.api.AmazonClientOneFeignManager;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.inventory.service.IInventoryRecordService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.purchase.service.IPurchaseFormReceiveService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
@Api(tags = "仓存接口")
@SystemControllerLog("仓存库存管理接口")
@RestController
@RequestMapping("/api/v1/inventory/manager")
@RequiredArgsConstructor
public class InventoryManagerController {
	@Resource
	IInventoryFormAgentService iInventoryFormAgentService;
	final IMaterialService  iMaterialService;
	final IInventoryRecordService iInventoryRecordService;
	final IAssemblyFormService assemblyFormService;
	final IInventoryService iInventoryService;
	final AmazonClientOneFeignManager amazonClientOneFeignManager;
	final IPurchaseFormReceiveService iPurchaseFormReceiveService;
	@PostMapping( "outbound")
	public  Result<?> outbound(@ApiParam("操作DTO")@RequestBody List<InventoryParameter>   paramList) throws BizException {
		return Result.judge(iInventoryFormAgentService.outbound(paramList));
//		boolean result=false;
//		try {
//			result=iInventoryFormAgentService.outbound(paramList);
//		}catch(BizException be) {
//			if(be!=null&&be.getMessage().contains("库存不够")) {
//				LambdaQueryChainWrapper<InventoryRecord> query = iInventoryRecordService.lambdaQuery();
//				for(InventoryParameter item:paramList) {
//					query.eq(InventoryRecord::getShopid, item.getShopid());
//					query.eq(InventoryRecord::getNumber, item.getNumber());
//					query.eq(InventoryRecord::getWarehouseid, item.getWarehouse());
//					query.eq(InventoryRecord::getMaterialid, item.getMaterial());
//					query.eq(InventoryRecord::getFormtype, "outstockform");
//					query.eq(InventoryRecord::getOperate, "out");
//					query.eq(InventoryRecord::getStatus, "outbound");
//					InventoryRecord record = query.one();
//					if(record==null) {
//						throw be;
//					}
//				}
//				return Result.success();
//			}else {
//				throw be;
//			}
//		}catch(Exception e) {
//			throw e;
//		}
//		
//		return Result.judge(result);
	}
	
	@PostMapping( "undo_outbound")
	public Result<?> undoOutbound(@ApiParam("操作DTO")@RequestBody List<InventoryParameter>   paramList) throws BizException {
		return Result.judge(iInventoryFormAgentService.undoOutbound(paramList));
	}
	
	@PostMapping( "out")
	public  Result<?> out(@ApiParam("操作DTO")@RequestBody List<InventoryParameter>   paramList) throws BizException {
		return Result.judge(iInventoryFormAgentService.out(paramList));
	}
	
	@PostMapping( "undo_out")
	public Result<?> undoOut(@ApiParam("操作DTO")@RequestBody List<InventoryParameter>   paramList) throws BizException {
		return Result.judge(iInventoryFormAgentService.undoOut(paramList));
	}
	 
	@SystemControllerLog( "修复待待入库待出库库存量")
	@ApiOperation(value = "修复待待入库待出库库存量")
	@GetMapping("/refreshInventory")
	@Transactional
	public Result<?> refreshInventory(String warehouseid,String materialid,String status) throws BizException {
		 UserInfo user = UserInfoContext.get();
		 if(status.equals("inbound_assembly")) {
			 Integer qty = assemblyFormService.refreshInbound(user.getCompanyid(), warehouseid, materialid);
			 iInventoryService.repairInventory(user, warehouseid, materialid, status, qty);
		 }else if(status.equals("outbound_outstockform")){
			 Material material = iMaterialService.getById(materialid);
			 Integer qty = amazonClientOneFeignManager.refreshOutbound(warehouseid, material.getSku());
			 iInventoryService.repairInventory(user, warehouseid, materialid, status, qty);
		 }else if(status.equals("inbound_purchase")){
			 Integer qty=iPurchaseFormReceiveService.refreshInbound(warehouseid, materialid);
			 iInventoryService.repairInventory(user, warehouseid, materialid, status, qty); 
		 }else {
			 throw new BizException("暂不支持此类型修复");
		 }
		
		return Result.success();
	}
	
}
