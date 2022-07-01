package com.wimoor.erp.purchase.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanItem;
@Mapper
public interface PurchasePlanItemMapper   extends BaseMapper<PurchasePlanItem> {
	
	  List<Map<String,Object>> summaryPlan(@Param("planid")String planid);

	  List<Map<String, Object>> summaryPlanOrder(Map<String,Object> params);

	  int updatePOStatusBySubPlan(@Param("id")String id, @Param("warehouseid")String warehouseid);
	  
	  int updatePOStatusByWarehouse(@Param("warehouseid")String warehouseid);
	  
	  int updateAOStatusBySubPlan(@Param("id")String id, @Param("warehouseid")String warehouseid);
	  
	  int updateAOStatusByWarehouse(@Param("warehouseid")String warehouseid);
	  
	
	  List<Map<String,Object>> getSummaryPlanByWarehouse(Map<String,Object> params);
	  
	  List<Map<String,Object>> findPurchaseitemForWarehouseid(@Param("warehouseid")String warehouseid, @Param("ftype")String ftype);
	  
	  List<Map<String,Object>> findPurchaseSubForWarehouseid(@Param("warehouseid")String warehouseid, @Param("ftype")String ftype);
	  
	  List<Map<String,Object>> getWarehouseForPlanIdAndMaterialId(@Param("planid")String planid, @Param("materialid")String materialid);

	List<Map<String, Object>> summaryPlanAssembly(Map<String, Object> params);

	Map<String, Object> getPlanInventoryInbound(@Param("groupid")String groupid, @Param("marketplaceid")String marketplaceid, @Param("sku")String sku);

	Map<String, Object> getShipInventoryInbound(@Param("authid")String authid, @Param("marketplaceid")String marketplaceid,@Param("sku") String sku);
}