package com.wimoor.amazon.inbound.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;

public interface IShipInboundPlanService extends IService<ShipInboundPlan> {

	void saveShipInboundPlan(ShipInboundPlan inplan);
 
 
}
