package com.wimoor.erp.api;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wimoor.erp.inventory.pojo.dto.InventoryParameter;
import com.wimoor.erp.material.pojo.dto.PlanDTO;
import com.wimoor.common.result.Result;

import io.swagger.annotations.ApiParam;

 
@Component
@FeignClient(value = "wimoor-erp")
public interface ErpClientOneFeign {
	
	
	@GetMapping("/erp/api/v1/material/getMaterialBySKU")
	public Result<Map<String,Object>> getMaterialBySKUAction(@RequestParam String sku,@RequestParam String shopid);
     
    @GetMapping("/erp/api/v1/material/getIdByMSku")
    public Result<String> getIdBySku(@RequestParam String shopid,@RequestParam String sku);
    
	@GetMapping("/erp/api/v1/shipForm/fullfillableToOutbound")
	public Result<Map<String,Object>> fullfillableToOutboundAction(@RequestParam String userid,
			@RequestParam String formid,@RequestParam String warehouseid,@RequestParam String number,@RequestParam String shopid,
			@RequestParam String material,@RequestParam Integer amount,@RequestParam String ftype);

	
	@PostMapping("/erp/api/v1/inventory/manager/outbound")
	public  Result<?> outbound(@ApiParam("操作DTO")@RequestBody List<InventoryParameter>   paramList) ;
	
	@PostMapping( "/erp/api/v1/inventory/manager/undo_outbound")
	public Result<?> undoOutbound(@ApiParam("操作DTO")@RequestBody List<InventoryParameter>   paramList) ;
	
	@PostMapping("/erp/api/v1/inventory/manager/out")
	public  Result<?> out(@ApiParam("操作DTO")@RequestBody List<InventoryParameter>   paramList) ;
	
	@PostMapping( "/erp/api/v1/inventory/manager/undo_out")
	public Result<?> undoOut(@ApiParam("操作DTO")@RequestBody List<InventoryParameter>   paramList) ;
	
	@GetMapping("/erp/api/v1/material/getAssemblyCan")
	public Result<Integer> getAssemblyCanAction(@RequestParam String materialid,@RequestParam String warehouseid,@RequestParam String shopid);
	
	@GetMapping("/erp/api/v1/shipForm/findPlanSubDetail")
	public Result<List<Map<String, Object>>> findPlanSubDetailAction(@RequestParam String groupid,@RequestParam String planid,@RequestParam String warehouseid,@RequestParam String marketplaceid,@RequestParam String issplit);

	@GetMapping("/erp/api/v1/shipTransCompany/getTransCompanyAPI")
	public Result<Object> getTransCompanyAPI(@RequestParam String companyid);
	
	@GetMapping("/erp/api/v1/material/getRealityPrice")
    public Result<Map<String,Object>> getRealityPrice(@RequestParam String shopid,@RequestParam String sku);
	
    @PostMapping("/erp/api/v1/material/getMaterialInfos")
  	public Result<Map<String, Object>> findMaterialMapBySku(@RequestBody List<String> skus);
  		
    @GetMapping("/erp/api/v1/material/findMarterialForColorOwner")
	public Result<List<String>> findMarterialForColorOwner(@RequestParam String shopid,
			                                               @RequestParam String owner,
			                                               @RequestParam String color);
    @PostMapping("/erp/api/v1/material/getMskuByTagList")
	public Result<List<String>> getMskuByTagList(@RequestBody List<String> taglist);

    @PostMapping("/erp/api/v1/material/getTagsIdsListByMsku/{shopid}")
	public Result<Map<String,String>> getTagsIdsListByMsku(@PathVariable String shopid,@RequestBody List<String> mskulist);
    
    @PostMapping("/erp/api/v1/material/getMaterialMap")
	public Result<Map<String,Object>> getMaterialInfoBySkuList(@RequestBody PlanDTO dto);
    
    @GetMapping("/erp/api/v1/material/getMSkuDeliveryAndInv")
    public Result<Map<String, Object>> getMSkuDeliveryAndInv(   @RequestParam String shopid,
													    		@RequestParam String groupid,
													    		@RequestParam String msku,
													    		@RequestParam String country,
													    		@RequestParam String needDeliveryCycle);
    
    @GetMapping("/erp/api/v1/material/getMaterialInventoryInfoApi")
	public Result<?> getMaterialInventoryInfoAction(@RequestParam String shopid,
			                                        @RequestParam String sku,
			                                        @RequestParam String warehouseid);
    
    @PostMapping("/erp/api/v1/material/getMskuInventory")
	public Result<List<Map<String, Object>>> getMskuInventory(@RequestBody PlanDTO dto);

    @PostMapping("/erp/api/v1/shipForm/afterShipInboundPlanSave")
    public Result<?> afterShipInboundPlanSaveAction( @ApiParam("参数") @RequestBody Map<String,Object> param  )  ;
    
    @GetMapping("/erp/api/v1/purchase/plan/getLast")
	public Result<?> getLastRecordAction(@RequestParam String id);
    
    @RequestMapping("/erp/api/v1/inventory/dispatch/oversea/getShipArrivalTimeRecord")
	public Result<List<Map<String,Object>>> getShipArrivalTimeRecord(@RequestParam String shopid,@RequestParam String country,@RequestParam String groupid,@RequestParam String sku);

    @RequestMapping("/erp/api/v1/inventory/findInventoryNowCostByShopId")
	public Result<List<Map<String, Object>>> findInventoryNowCostByShopId(@RequestParam String shopid);

    @GetMapping("/erp/api/v1/purchase/plan/getLasts")
	public Result<?> getLastRecordsAction(List<String> list);
}