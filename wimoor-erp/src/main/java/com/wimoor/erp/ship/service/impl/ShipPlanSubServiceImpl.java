package com.wimoor.erp.ship.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.ship.mapper.ShipPlanSubMapper;
import com.wimoor.erp.ship.pojo.entity.ShipPlanSub;
import com.wimoor.erp.ship.service.IShipPlanSubService;

@Service("shipPlanSubService")
public class ShipPlanSubServiceImpl extends  ServiceImpl<ShipPlanSubMapper,ShipPlanSub> implements IShipPlanSubService {
 
}
