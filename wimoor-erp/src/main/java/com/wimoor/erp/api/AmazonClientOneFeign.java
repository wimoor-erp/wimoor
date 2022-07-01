package com.wimoor.erp.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wimoor.api.amzon.inbound.pojo.dto.ShipInboundPlanDTO;
import com.wimoor.api.amzon.inbound.pojo.dto.ShipInboundShipmentDTO;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.common.result.Result;

import io.swagger.annotations.ApiParam;

@Component
@FeignClient(value = "wimoor-amazon")
public interface AmazonClientOneFeign {

	@GetMapping("/amazon/api/v1/shipForm/info/{shipmentid}")
	public Result<ShipInboundShipmenSummarytVo> infoAction(@PathVariable("shipmentid") String shipmentid);
	
	@GetMapping("/amazon/api/v1/shipForm/getShipment/{shipmentid}")
	public Result<ShipInboundShipmentDTO> getShipmentidAction(@PathVariable("shipmentid") String shipmentid);
	
	@GetMapping("/amazon/api/v1/shipForm/create/{shipmentid}")
	public Result<String> createShipmentAction(@PathVariable("shipmentid") String shipmentid);
	
	@PostMapping("/amazon/api/v1/shipForm/saveInboundPlan")
	public Result<String> saveInboundPlanAction(@ApiParam("发货计划")@RequestBody ShipInboundPlanDTO inplan);

    @PostMapping("/amazon/api/v1/shipForm/update")
	public Result<String> updateShipmentAction(@ApiParam("货件信息")@RequestBody ShipInboundShipmentDTO dto) ;

 
}
