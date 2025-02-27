package com.wimoor.erp.warehouse.controller;


import java.util.List;
import java.util.Map;

import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.purchase.service.IPurchaseFormReceiveService;
import com.wimoor.erp.stock.pojo.entity.DispatchForm;
import com.wimoor.erp.stock.pojo.entity.DispatchFormEntry;
import com.wimoor.erp.stock.service.IDispatchFormEntryService;
import com.wimoor.erp.stock.service.IDispatchFormService;
import com.wimoor.erp.warehouse.pojo.dto.MaterialShelfInfoDTO;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.pojo.vo.MaterialShelfInfoVo;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfTreeVo;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfService;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.*;

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
	final IPurchaseFormReceiveService iPurchaseFormReceiveService;
	final IWarehouseShelfService iWarehouseShelfService;
	final IWarehouseService warehouseService;
	final IMaterialService materialService;
	final IAssemblyFormService assemblyFormService;
	final IDispatchFormEntryService dispatchFormEntryService;
	final IDispatchFormService iDispatchFormService;
	    
	    @ApiOperation("查询当前库位中操作记录的情况")
	    @GetMapping("/list")
	    public Result<List<WarehouseShelfInventoryOptRecordVo>> getShelfInvAction(String formid,String materialid,String formtype){
	    	UserInfo user = UserInfoContext.get();
	    	String shopid=user.getCompanyid();
	           return  Result.success(iWarehouseShelfInventoryOptRecordService.getRecordVo(shopid, formid,formtype, materialid));
	    }


	@ApiOperation("用于扫描二维码时调用接口获取产品信息查询")
	@PostMapping("/materialInfo")
	public Result<MaterialShelfInfoVo> materialInfoAction(@RequestBody MaterialShelfInfoDTO dto){
		UserInfo user = UserInfoContext.get();
		String shopid=user.getCompanyid();
		MaterialShelfInfoVo vo=new MaterialShelfInfoVo();
		String warehouseid=null;
		//通过单据ID获取信息：
		//采购单入库记录:
		if(dto.getInfotype().equals("rec")){
			Map<String, Object> recinfo = iPurchaseFormReceiveService.receiveInfo(user, dto.getPurchaseFormRecId());
			warehouseid=recinfo.get("warehouseid").toString();
		}
		else if(dto.getInfotype().equals("rec-po")){
			//组装单：
			AssemblyForm asform = assemblyFormService.getById(dto.getAssemblyFormid());
			Material material = materialService.getById(asform.getMainmid());
			Warehouse warehouse = warehouseService.getById(asform.getWarehouseid());
			warehouseid=warehouse.getId();
		}else if(dto.getInfotype().equals("rec-dis")){
			//调库单：
			DispatchFormEntry dispatchFormEntry = dispatchFormEntryService.getById(dto.getDispatchFormid());
			DispatchForm dispatchForm = iDispatchFormService.getById(dispatchFormEntry.getFormid());
			warehouseid=dispatchForm.getToWarehouseid();
		}else if(dto.getInfotype().equals("rec-sku")){
			warehouseid=dto.getWarehouseid();
			String materialid=dto.getMaterialid();
			String quantity=dto.getQuantity();
			//获取仓库信息：
			Warehouse warehouse=warehouseService.getById(warehouseid);
			vo.setWarehousename(warehouse.getName());
			vo.setWarehouseid(warehouse.getId());
			//获取产品信息：
			Material material=materialService.getById(materialid);
			vo.setSku(material.getSku());
			vo.setName(material.getName());
			vo.setQuantity(quantity);
		}
		//获取货架树结构:
		List<WarehouseShelfTreeVo> shelfvo = iWarehouseShelfService.getAllTree(user, warehouseid);
		vo.setShelftree(shelfvo);
		return  Result.success(vo);
	}
 
}

