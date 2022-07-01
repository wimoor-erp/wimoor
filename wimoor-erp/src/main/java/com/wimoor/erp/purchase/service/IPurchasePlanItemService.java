package com.wimoor.erp.purchase.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanItem;

public interface IPurchasePlanItemService extends IService<PurchasePlanItem>{
	
	 List<Map<String,Object>> getWarehouseForPlanIdAndMaterialId(String planid, String materialid);

	List<Map<String, Object>> getShipInventory(String groupid, String pidlist);

}
