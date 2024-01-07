package com.wimoor.erp.api;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.wimoor.common.result.Result;
import com.wimoor.erp.ship.pojo.dto.ShipInboundPlanDTO;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmentDTO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Component
@FeignClient(value = "wimoor-amazon")
public interface AmazonClientOneFeignApi {

	@GetMapping("/amazon/api/v1/shipForm/info/{shipmentid}")
	public Result<ShipInboundShipmenSummarytVo> infoAction(@PathVariable("shipmentid") String shipmentid);
	
	@GetMapping("/amazon/api/v1/shipForm/getShipment/{shipmentid}")
	public Result<ShipInboundShipmentDTO> getShipmentidAction(@PathVariable("shipmentid") String shipmentid);
	
	@GetMapping("/amazon/api/v1/amzgroup/id/{id}")
	public Result<?> getAmazonGroupByIdAction(@PathVariable String id);
	
	@PostMapping("/amazon/api/v1/shipForm/create")
	public Result<String> createShipmentAction(@ApiParam("货件信息")@RequestBody ShipInboundShipmentDTO dto);
	
	@PostMapping("/amazon/api/v1/shipForm/saveInboundPlan")
	public Result<?> saveInboundPlanAction(@ApiParam("发货计划")@RequestBody ShipInboundPlanDTO inplan);

    @PostMapping("/amazon/api/v1/shipForm/update")
	public Result<String> updateShipmentAction(@ApiParam("货件信息")@RequestBody ShipInboundShipmentDTO dto) ;
 

	@ApiOperation(value = "获取未同步货件")
	@GetMapping("/amazon/api/v1/shipFormSync/getUnSyncShipment")
	public Result<ShipInboundShipmenSummarytVo> getUnSyncShipmentAction(@RequestParam("groupid") String groupid, 
			                                                            @RequestParam("marketplaceid") String marketplaceid, 
			                                           	                @RequestParam("shipmentid") String shipmentid,
			                                                            @RequestParam("warehouseid") String warehouseid);
    @GetMapping("/amazon/api/v1/amzmarketplace/getByName")
    public Result<?> getMarketplaceAction(@RequestParam("name")String name) ;
    
    @GetMapping("/amazon/api/v1/amzmarketplace/getByCountry")
    public Result<?> getMarketplaceByCountryAction(@RequestParam("country")String country);
    
	@GetMapping("/amazon/api/v1/shipFormSync/confirmSyncShipment")
	public Result<?> confirmSyncShipment(@RequestParam String shipmentid );
	
	@GetMapping("/amazon/api/v1/product/salesplan/getPlanModelItem")
	public Result<List<Map<String, Object>>> getPlanItem(@RequestParam String groupid);

	@GetMapping("/amazon/api/v1/report/product/productInOpt/updateProductOwner")
	public void updateProductOwner(@RequestParam("msku")String msku, @RequestParam("owner")String owner, @RequestParam("oldowner")String oldowner);
	
	
	@GetMapping("/amazon/api/v1/shipForm/refreshOutbound")
	public Result<Integer> refreshOutbound(@RequestParam("warehouseid") String warehouseid, 
			                                                            @RequestParam("msku") String msku);
}
