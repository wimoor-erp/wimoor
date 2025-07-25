package com.wimoor.amazon.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.erp.api.ErpClientOneFeign;
import com.wimoor.erp.inventory.pojo.dto.InventoryParameter;
import com.wimoor.erp.ship.pojo.dto.QuotaInfoDTO;
import com.wimoor.erp.ship.pojo.dto.ShipFormDTO;

import cn.hutool.core.bean.BeanUtil;
import feign.FeignException;


 
@Component
public class ErpClientOneFeignManager {
	@Autowired
    ErpClientOneFeign erpClientOneFeign;
	
	public Map<String,Map<String, Object>> getMaterialBySKUAction(String shopid,List<String> skulist){
		Result<List<Map<String, Object>>> result = erpClientOneFeign.getMaterialBySKUAction(shopid,skulist);
		if(Result.isSuccess(result)&&result.getData()!=null) {
			if(result.getData().size()>0) {
				Map<String,Map<String, Object>> map=new HashMap<String,Map<String, Object>>();
				for(Map<String,Object> item:result.getData()) {
					String sku=item.get("sku")!=null?item.get("sku").toString():null;
					if(sku!=null) {
						map.put(sku, item);
					}
				}
				return map;
			}else {
				return null;
			}
		}else {
			return null;
		}
	}
     
    public Result<String> getIdBySku( String shopid, String sku){
    	return erpClientOneFeign.getIdBySku(shopid, sku);
    }
    
	public Result<Map<String,Object>> fullfillableToOutboundAction(  String userid,
			  String formid,  String warehouseid,  String number,  String shopid,
			  String material,  Integer amount,  String ftype){
		return fullfillableToOutboundAction(userid, formid, warehouseid, number, shopid, material, amount, ftype);
	}

	
	public  Result<?> outbound(List<InventoryParameter>   paramList){
		return erpClientOneFeign.outbound(paramList);
	}
	
	public Result<?> undoOutbound(List<InventoryParameter>   paramList){
		return erpClientOneFeign.undoOutbound(paramList);
	}
	
	public  Result<?> out( List<InventoryParameter>   paramList){
		return erpClientOneFeign.out(paramList);
	}
	
	public Result<?> undoOut( List<InventoryParameter>   paramList) {
		return erpClientOneFeign.undoOut(paramList);
	}
	
	public Result<Integer> getAssemblyCanAction( String materialid, String warehouseid, String shopid){
		return erpClientOneFeign.getAssemblyCanAction(materialid, warehouseid, shopid);
	}
	
	public Result<List<Map<String, Object>>> findPlanSubDetailAction( String groupid, String planid, String warehouseid, String marketplaceid, String issplit){
		return erpClientOneFeign.findPlanSubDetailAction(groupid, planid, warehouseid, marketplaceid, issplit);
	}

	public Result<Object> getTransCompanyAPI( String companyid){
		return erpClientOneFeign.getTransCompanyAPI(companyid);
	}
	
    public Result<Map<String,Object>> getRealityPrice( String shopid, String sku){
    	return erpClientOneFeign.getRealityPrice(shopid, sku);
    }
	
	public Result<Map<String, Object>> findMaterialMapBySku( List<String> skus){
		return erpClientOneFeign.findMaterialMapBySku(skus);
	}
	public  Map<String, Object>  findMaterialMapBySkuV2( List<String> skus){
		Result<Map<String, Object>> result = erpClientOneFeign.findMaterialMapBySku(skus);
		if(result.isSuccess(result)&&result.getData()!=null){
			return result.getData();
		}else{
			return null;
		}
	}
    
	public Result<List<String>> findMarterialForColorOwner( String shopid,
			                                                String owner,
			                                                String color){
		return erpClientOneFeign.findMarterialForColorOwner(shopid, owner, color);
	}
	public Result<List<String>> getMskuByTagList(String without,@RequestBody List<String> taglist){
		return erpClientOneFeign.getMskuByTagList(without,taglist);
	}

	public Result<Map<String,String>> getTagsIdsListByMsku( String shopid, List<String> mskulist){
		return erpClientOneFeign.getTagsIdsListByMsku(shopid, mskulist);
	}
    
	public Result<Map<String,Object>> getMaterialInfoBySkuList(@RequestBody PlanDTO dto){
		com.wimoor.erp.material.pojo.dto.PlanDTO erpdto =new com.wimoor.erp.material.pojo.dto.PlanDTO();
		BeanUtil.copyProperties(dto, erpdto);
		return erpClientOneFeign.getMaterialInfoBySkuList(erpdto);
	}
    
    public Result<Map<String, Object>> getMSkuDeliveryAndInv(    String shopid,
													    		 String groupid,
													    		 String msku,
													    		 String country,String needDeliveryCycle){
    	return erpClientOneFeign.getMSkuDeliveryAndInv(shopid, groupid, msku, country,needDeliveryCycle);
    }
    
	public Result<?> getMaterialInventoryInfoAction( String shopid,
			                                         String sku,
			                                         String warehouseid){
		return erpClientOneFeign.getMaterialInventoryInfoAction(shopid, sku, warehouseid);
	}
    
	public Result<List<Map<String, Object>>> getMskuInventory( PlanDTO dto){
		com.wimoor.erp.material.pojo.dto.PlanDTO erpdto=new com.wimoor.erp.material.pojo.dto.PlanDTO();
		BeanUtil.copyProperties(dto, erpdto);
		return erpClientOneFeign.getMskuInventory(erpdto);
	}

    public Result<?> afterShipInboundPlanSaveAction( Map<String,Object> param  )  {
    	return erpClientOneFeign.afterShipInboundPlanSaveAction(param);
    }
    
	public Result<?> getLastRecordAction( String id){
		return erpClientOneFeign.getLastRecordAction(id);
	}
    
	public Result<List<Map<String,Object>>> getShipArrivalTimeRecord( String shopid, String country, String groupid, String sku){
		return erpClientOneFeign.getShipArrivalTimeRecord(shopid, country, groupid, sku);
	}
	
	public List<Map<String, Object>> findInventoryNowCostByShopId(String shopid) {
		// TODO Auto-generated method stub
 		 Result<List<Map<String, Object>>> result= erpClientOneFeign.findInventoryNowCostByShopId(shopid);
 		 if(Result.isSuccess(result)&&result.getData()!=null) {
 			 return result.getData();
 		 }else {
 			 return null;
 		 }
	}

	public Result<?> getLastRecordsAction(List<String> list) {
		// TODO Auto-generated method stub
		return erpClientOneFeign.getLastRecordsAction(list);
	}
	
	public QuotaInfoDTO quotainfoAction(QuotaInfoDTO dto) {
		// TODO Auto-generated method stub
		Result<QuotaInfoDTO> result = erpClientOneFeign.quotainfoAction(dto);
		if(result!=null&&Result.isSuccess(result)&&result.getData()!=null) {
			return result.getData();
		}else {
			return null;
		}
	}
	
	public  void outBoundShipInventory(ShipFormDTO  dto) {
		// TODO Auto-generated method stub
		try {
			Result<Boolean> result = erpClientOneFeign.outBoundShipInventory(dto);
			if(result!=null&&Result.isSuccess(result)&&result.getData()!=null) {
				if(result.getData()==true) {
					return ;
				}else {
					throw new BizException("本地库存扣除失败"+result.getMsg());
				}
			}else {
				throw new BizException("本地库存扣除失败"+result.getMsg());
			}
		}catch(FeignException e) {
			throw new BizException("本地库存扣除失败"+BizException.getMessage(e, ""));
		}
		
	}
	
	public  void updateDisable(ShipFormDTO  dto) {
		// TODO Auto-generated method stub
		try {
			Result<Boolean> result = erpClientOneFeign.updateDisable(dto);
			if(result!=null&&Result.isSuccess(result)&&result.getData()!=null) {
				if(result.getData()==true) {
					return ;
				}else {
					throw new BizException("本地库存扣除失败"+result.getMsg());
				}
			}else {
				throw new BizException("本地库存扣除失败"+result.getMsg());
			}
		}catch(FeignException e) {
			throw new BizException("本地库存扣除失败"+BizException.getMessage(e, ""));
		}
		
	}
	
	public  void updateItemQty(ShipFormDTO  dto) {
		// TODO Auto-generated method stub
		try {
			Result<Boolean> result = erpClientOneFeign.updateItemQty(dto);
			if(result!=null&&Result.isSuccess(result)&&result.getData()!=null) {
				if(result.getData()==true) {
					return ;
				}else {
					throw new BizException("本地库存扣除失败"+result.getMsg());
				}
			}else {
				throw new BizException("本地库存扣除失败"+result.getMsg());
			}
		}catch(FeignException e) {
			throw new BizException("本地库存扣除失败"+BizException.getMessage(e, ""));
		}
		
	}
	
	public  void outShipInventory(ShipFormDTO  dto) {
		// TODO Auto-generated method stub
		try {
			Result<Boolean> result = erpClientOneFeign.outShipInventory(dto);
			if(result!=null&&Result.isSuccess(result)&&result.getData()!=null) {
				if(result.getData()==true) {
					return ;
				}else {
					throw new BizException("本地库存扣除失败"+result.getMsg());
				}
			}else {
				throw new BizException("本地库存扣除失败"+result.getMsg());
			}
		}catch(FeignException e) {
			throw new BizException("本地库存扣除失败"+BizException.getMessage(e, ""));
		}
		
	}
	public Map<String,Object> getShipTransChannelDetial(String id){
		Result<?> result = erpClientOneFeign.getShipTransChannelDetial(id);
		if(result!=null&&Result.isSuccess(result)&&result.getData()!=null){
			return (Map<String,Object>)result.getData();
		}else{
			return null;
		}
	}

	public Map<String,Object> getCustomsAction(String sku,String country){
		Result<?> result = erpClientOneFeign.getCustomsAction(sku,country);
		if(result!=null&&Result.isSuccess(result)&&result.getData()!=null){
			return (Map<String,Object>)result.getData();
		}else{
			return null;
		}
	}

	public String getQuoteToken(){
		Result<?> result = erpClientOneFeign.getQuoteToken();
		if(result!=null&&Result.isSuccess(result)&&result.getData()!=null){
			if(result.getData()!=null){
				Map<String, Object> map = (Map<String, Object>) result.getData();
				return  map.get("buyertoken")!=null?map.get("buyertoken").toString():null;
			}else{
				return null;
			}

		}else{
			return null;
		}
	}
}