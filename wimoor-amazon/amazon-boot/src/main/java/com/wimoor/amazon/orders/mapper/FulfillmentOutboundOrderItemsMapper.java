package com.wimoor.amazon.orders.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.orders.pojo.entity.FulfillmentOutboundOrder;
import org.apache.ibatis.annotations.Mapper;
import com.wimoor.amazon.orders.pojo.entity.FulfillmentOutboundOrderItems;

@Mapper
public interface FulfillmentOutboundOrderItemsMapper extends BaseMapper<FulfillmentOutboundOrderItems> {

}
