package com.wimoor.erp.ship.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.ship.mapper.ShipPlanItemMapper;
import com.wimoor.erp.ship.pojo.entity.ShipPlanItem;
import com.wimoor.erp.ship.service.IShipPlanItemService;

@Service("shipPlanItemService")
public class ShipPlanItemServiceImpl extends  ServiceImpl<ShipPlanItemMapper,ShipPlanItem> implements IShipPlanItemService {

}
