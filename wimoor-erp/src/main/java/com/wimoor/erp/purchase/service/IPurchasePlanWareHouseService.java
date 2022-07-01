package com.wimoor.erp.purchase.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanWareHouse;

public interface IPurchasePlanWareHouseService extends IService<PurchasePlanWareHouse>{

	public String getPlanIdForWareHouseId(String warehouseid, String shopid);
	
	public List<Map<String, Object>> getWareHouseIdForPlanId(String planid, String shopid);
	
	public int updatePlanWareHouse(String warehouseid, String planid, UserInfo user);
	
	public List<Map<String, Object>> getPurchasePlanWareHouseForShopId(String shopid);
	
	public int savaPlanWareHouse(Map<String, Object> map, UserInfo user);
	
	public List<Map<String, Object>> refreshPlanWareHouse(List<Map<String, Object>> list, UserInfo user);
}
