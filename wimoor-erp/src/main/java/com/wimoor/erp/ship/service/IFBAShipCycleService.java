package com.wimoor.erp.ship.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.ship.pojo.entity.FBAShipCycle;

public interface IFBAShipCycleService extends IService<FBAShipCycle>{

	int updateStockCycle(Map<String, Object> map, UserInfo user);
	
	boolean updateMinCycle(Map<String, Object> map, UserInfo user);
	
	boolean updateFirstLegCharges(Map<String, Object> map, UserInfo user);
}
