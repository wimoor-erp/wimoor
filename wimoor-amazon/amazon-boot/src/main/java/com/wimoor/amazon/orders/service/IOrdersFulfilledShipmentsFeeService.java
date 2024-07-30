package com.wimoor.amazon.orders.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.orders.pojo.entity.OrdersFulfilledShipmentsFee;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-22
 */
public interface IOrdersFulfilledShipmentsFeeService extends IService<OrdersFulfilledShipmentsFee> {
    public void orderTransFee(String authid);
}
