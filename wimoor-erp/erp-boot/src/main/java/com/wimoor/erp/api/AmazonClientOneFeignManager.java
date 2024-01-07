package com.wimoor.erp.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.erp.ship.pojo.dto.ShipInboundPlanDTO;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmentDTO;

@Component
public class AmazonClientOneFeignManager {
    @Autowired
    AmazonClientOneFeignApi api;
	public Result<ShipInboundShipmenSummarytVo> infoAction(String shipmentid){
		return api.infoAction(shipmentid);
	}
	
	public Result<ShipInboundShipmentDTO> getShipmentidAction( String shipmentid){
		return api.getShipmentidAction(shipmentid);
	}
	
	public Result<?> getAmazonGroupByIdAction(  String id){
		return api.getAmazonGroupByIdAction(id);
	}
	
	public Result<String> createShipmentAction( ShipInboundShipmentDTO dto){
		return api.createShipmentAction(dto);
	}
	
	public Result<?> saveInboundPlanAction( ShipInboundPlanDTO inplan){
		return saveInboundPlanAction(inplan);
	}

	public Result<String> updateShipmentAction(  ShipInboundShipmentDTO dto) {
		return api.updateShipmentAction(dto);
	}
 

 
	public Result<ShipInboundShipmenSummarytVo> getUnSyncShipmentAction( String groupid, 
			                                                             String marketplaceid, 
			                                           	                 String shipmentid,
			                                                             String warehouseid){
		return api.getUnSyncShipmentAction(groupid, marketplaceid, shipmentid, warehouseid);
	}
    public Result<?> getMarketplaceAction(String name) {
    	return api.getMarketplaceAction(name);
    }
    
    public Result<?> getMarketplaceByCountryAction( String country){
    	return api.getMarketplaceAction(country);
    }
    
	public Result<?> confirmSyncShipment(  String shipmentid ){
		return api.confirmSyncShipment(shipmentid);
	}
	
	public Result<List<Map<String, Object>>> getPlanItem( String groupid){
		return api.getPlanItem(groupid);
	}

	public void updateProductOwner(String msku,String owner,String oldowner) {
		api.updateProductOwner(msku, owner, oldowner);
	}
	
	public Integer refreshOutbound(String warehouseid,String msku) {
		Result<Integer> result = api.refreshOutbound(warehouseid, msku);
		if(Result.isSuccess(result)&&result.getData()!=null) {
			return result.getData();
		}else {
			throw new BizException("获取货件待入库失败");
		}
	}
}
