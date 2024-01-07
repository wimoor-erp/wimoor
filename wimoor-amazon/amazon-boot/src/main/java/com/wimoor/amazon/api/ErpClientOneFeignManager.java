package com.wimoor.amazon.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.common.result.Result;
import com.wimoor.erp.api.ErpClientOneFeign;
import com.wimoor.erp.inventory.pojo.dto.InventoryParameter;

import cn.hutool.core.bean.BeanUtil;


 
@Component
public class ErpClientOneFeignManager {
	@Autowired
    ErpClientOneFeign api;
	
	public Result<Map<String,Object>> getMaterialBySKUAction( String sku, String shopid){
		return api.getMaterialBySKUAction(sku, shopid);
	}
     
    public Result<String> getIdBySku( String shopid, String sku){
    	return api.getIdBySku(shopid, sku);
    }
    
	public Result<Map<String,Object>> fullfillableToOutboundAction(  String userid,
			  String formid,  String warehouseid,  String number,  String shopid,
			  String material,  Integer amount,  String ftype){
		return fullfillableToOutboundAction(userid, formid, warehouseid, number, shopid, material, amount, ftype);
	}

	
	public  Result<?> outbound(List<InventoryParameter>   paramList){
		return api.outbound(paramList);
	}
	
	public Result<?> undoOutbound(List<InventoryParameter>   paramList){
		return api.undoOutbound(paramList);
	}
	
	public  Result<?> out( List<InventoryParameter>   paramList){
		return api.out(paramList);
	}
	
	public Result<?> undoOut( List<InventoryParameter>   paramList) {
		return api.undoOut(paramList);
	}
	
	public Result<Integer> getAssemblyCanAction( String materialid, String warehouseid, String shopid){
		return api.getAssemblyCanAction(materialid, warehouseid, shopid);
	}
	
	public Result<List<Map<String, Object>>> findPlanSubDetailAction( String groupid, String planid, String warehouseid, String marketplaceid, String issplit){
		return api.findPlanSubDetailAction(groupid, planid, warehouseid, marketplaceid, issplit);
	}

	public Result<Object> getTransCompanyAPI( String companyid){
		return api.getTransCompanyAPI(companyid);
	}
	
    public Result<Map<String,Object>> getRealityPrice( String shopid, String sku){
    	return api.getRealityPrice(shopid, sku);
    }
	
	public Result<Map<String, Object>> findMaterialMapBySku( List<String> skus){
		return api.findMaterialMapBySku(skus);
	}
    
	public Result<List<String>> findMarterialForColorOwner( String shopid,
			                                                String owner,
			                                                String color){
		return api.findMarterialForColorOwner(shopid, owner, color);
	}
	public Result<List<String>> getMskuByTagList(@RequestBody List<String> taglist){
		return api.getMskuByTagList(taglist);
	}

	public Result<Map<String,String>> getTagsIdsListByMsku( String shopid, List<String> mskulist){
		return api.getTagsIdsListByMsku(shopid, mskulist);
	}
    
	public Result<Map<String,Object>> getMaterialInfoBySkuList(@RequestBody PlanDTO dto){
		com.wimoor.erp.material.pojo.dto.PlanDTO erpdto =new com.wimoor.erp.material.pojo.dto.PlanDTO();
		BeanUtil.copyProperties(dto, erpdto);
		return api.getMaterialInfoBySkuList(erpdto);
	}
    
    public Result<Map<String, Object>> getMSkuDeliveryAndInv(    String shopid,
													    		 String groupid,
													    		 String msku,
													    		 String country,String needDeliveryCycle){
    	return api.getMSkuDeliveryAndInv(shopid, groupid, msku, country,needDeliveryCycle);
    }
    
	public Result<?> getMaterialInventoryInfoAction( String shopid,
			                                         String sku,
			                                         String warehouseid){
		return api.getMaterialInventoryInfoAction(shopid, sku, warehouseid);
	}
    
	public Result<List<Map<String, Object>>> getMskuInventory( PlanDTO dto){
		com.wimoor.erp.material.pojo.dto.PlanDTO erpdto=new com.wimoor.erp.material.pojo.dto.PlanDTO();
		BeanUtil.copyProperties(dto, erpdto);
		return api.getMskuInventory(erpdto);
	}

    public Result<?> afterShipInboundPlanSaveAction( Map<String,Object> param  )  {
    	return api.afterShipInboundPlanSaveAction(param);
    }
    
	public Result<?> getLastRecordAction( String id){
		return api.getLastRecordAction(id);
	}
    
	public Result<List<Map<String,Object>>> getShipArrivalTimeRecord( String shopid, String country, String groupid, String sku){
		return api.getShipArrivalTimeRecord(shopid, country, groupid, sku);
	}
	
	public List<Map<String, Object>> findInventoryNowCostByShopId(String shopid) {
		// TODO Auto-generated method stub
 		 Result<List<Map<String, Object>>> result=api.findInventoryNowCostByShopId(shopid);
 		 if(Result.isSuccess(result)&&result.getData()!=null) {
 			 return result.getData();
 		 }else {
 			 return null;
 		 }
	}

	public Result<?> getLastRecordsAction(List<String> list) {
		// TODO Auto-generated method stub
		return api.getLastRecordsAction(list);
	}
}