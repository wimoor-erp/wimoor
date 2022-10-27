package com.wimoor.amazon.api;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.wimoor.api.amzon.inbound.pojo.dto.ShipInboundShipmentDTO;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;

 
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

	
	@PostMapping("/erp/api/v1/shipForm/outbound")
	public void outboundAction(@RequestBody ShipInboundShipmentDTO shipment) throws BizException;
	
	@GetMapping("/erp/api/v1/material/getAssemblyCan")
	public Result<Integer> getAssemblyCanAction(@RequestParam String materialid,@RequestParam String warehouseid,@RequestParam String shopid);
	
	@GetMapping("/erp/api/v1/shipForm/findPlanSubDetail")
	public Result<List<Map<String, Object>>> findPlanSubDetailAction(@RequestParam String groupid,@RequestParam String planid,@RequestParam String warehouseid,@RequestParam String marketplaceid,@RequestParam String issplit);

	@GetMapping("/erp/api/v1/shipTransCompany/getTransCompanyAPI")
	public Result<Object> getTransCompanyAPI(@RequestParam String companyid);
	
	@GetMapping("/erp/api/v1/material/getRealityPrice")
    public Result<Map<String,Object>> getRealityPrice(@RequestParam String shopid,@RequestParam String sku);
	
    @GetMapping("/erp/api/v1/material/getMaterialOneByInfo")
	public Result<Map<String, Object>> findMaterialMapBySku(@RequestParam String sku, @RequestParam String shopid);
    
    @GetMapping("/erp/api/v1/material/findMarterialForColorOwner")
	public Result<List<String>> findMarterialForColorOwner(@RequestParam String shopid,
			                                               @RequestParam String owner,
			                                               @RequestParam String color);
}