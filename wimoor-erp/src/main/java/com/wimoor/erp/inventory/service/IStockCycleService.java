package com.wimoor.erp.inventory.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.warehouse.pojo.entity.StockCycle;

public interface IStockCycleService extends IService<StockCycle> {

	List<Map<String, Object>> selectByMaterial(String id);

	void deleteByMaterial(String id);

	boolean updateStockCycle(Map<String, Object> map, UserInfo user);
	
	boolean updateMinCycle(Map<String, Object> map, UserInfo user);
	
	StockCycle findByMaterialAndWarehouse(String materialid, String warehouseid);
}
