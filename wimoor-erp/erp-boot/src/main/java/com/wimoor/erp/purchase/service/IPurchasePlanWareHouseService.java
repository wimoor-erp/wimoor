package com.wimoor.erp.purchase.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanWareHouse;

public interface IPurchasePlanWareHouseService extends IService<PurchasePlanWareHouse>{

	public String getPlanIdForWareHouseId(String warehouseid, String shopid);
	
	public List<PurchasePlanWareHouse> getWareHouseIdForPlanId(String planid, String shopid);
	
	public int updatePlanWareHouse(List<PurchasePlanWareHouse> list, String planid,String shopid);
	
	public List<PurchasePlanWareHouse> getPurchasePlanWareHouseForShopId(String shopid);
	
	public int savePlanWareHouse(List<PurchasePlanWareHouse> list);
	
}
