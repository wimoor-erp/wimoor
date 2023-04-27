package com.wimoor.erp.ship.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.ship.pojo.entity.ShipPlan;
import com.wimoor.erp.ship.pojo.entity.ShipPlanSub;

public interface  IShipPlanService extends IService<ShipPlan> {
	void afterShipInboundPlanSave(String plansubid, String planmarketplaceid, Boolean issplit, List<String> list);
	public void afterShipPlanItemShiped(ShipPlanSub plansub,String operate,List<String> skulist);
}
