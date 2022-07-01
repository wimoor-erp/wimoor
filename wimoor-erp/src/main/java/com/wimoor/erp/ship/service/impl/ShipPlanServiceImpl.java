package com.wimoor.erp.ship.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.ship.mapper.ShipPlanMapper;
import com.wimoor.erp.ship.pojo.entity.ShipPlan;
import com.wimoor.erp.ship.service.IShipPlanService;

@Service("shipPlanService")
public class ShipPlanServiceImpl extends  ServiceImpl<ShipPlanMapper,ShipPlan> implements IShipPlanService {
	  

}
