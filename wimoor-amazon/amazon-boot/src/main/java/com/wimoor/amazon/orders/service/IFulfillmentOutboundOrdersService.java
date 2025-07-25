package com.wimoor.amazon.orders.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.orders.pojo.dto.OrdersFulfillmentDTO;
import com.wimoor.amazon.orders.pojo.entity.FulfillmentOutboundOrder;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IFulfillmentOutboundOrdersService extends IService<FulfillmentOutboundOrder>, IRunAmazonService {
    String getFulfillmentOrders(String authid);

    IPage<FulfillmentOutboundOrder> getList(OrdersFulfillmentDTO dto);
}
