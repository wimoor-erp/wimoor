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
    ErpClientOneFeign api;
	
	public Map<String,Map<String, Object>> getMaterialBySKUAction(String shopid,List<String> skulist){
		Result<List<Map<String, Object>>> result = api.getMaterialBySKUAction(shopid,skulist);
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
	public Result<List<String>> getMskuByTagList(String without,@RequestBody List<String> taglist){
		return api.getMskuByTagList(without,taglist);
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
	
	public QuotaInfoDTO quotainfoAction(QuotaInfoDTO dto) {
		// TODO Auto-generated method stub
		Result<QuotaInfoDTO> result = api.quotainfoAction(dto);
		if(result!=null&&Result.isSuccess(result)&&result.getData()!=null) {
			return result.getData();
		}else {
			return null;
		}
	}
	
	public  void outBoundShipInventory(ShipFormDTO  dto) {
		// TODO Auto-generated method stub
		try {
			Result<Boolean> result = api.outBoundShipInventory(dto);
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
			Result<Boolean> result = api.updateDisable(dto);
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
			Result<Boolean> result = api.updateItemQty(dto);
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
			Result<Boolean> result = api.outShipInventory(dto);
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
	
}