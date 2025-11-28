package com.wimoor.amazon.orders.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.orders.pojo.entity.FulfillmentOutboundOrder;
import com.wimoor.amazon.orders.pojo.entity.FulfillmentOutboundOrderShipment;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface FulfillmentOutboundOrderShipmentMapper extends BaseMapper<FulfillmentOutboundOrderShipment> {

}
