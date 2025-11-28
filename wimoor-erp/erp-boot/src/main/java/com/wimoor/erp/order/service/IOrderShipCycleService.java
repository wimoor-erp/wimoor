package com.wimoor.erp.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.order.pojo.entity.OrderShipCycle;

import java.math.BigDecimal;

public interface IOrderShipCycleService extends IService<OrderShipCycle> {
    int updateStockCycleTransType(OrderShipCycle cycle, UserInfo user);
    int updateStockCycle(String warehouseid,  String sku, Integer stockcycle,  UserInfo user);
    int updateStockCycle(String warehouseid, String sku, Integer stockcycle, Integer mincycles, BigDecimal fees, UserInfo userinfo);
}
