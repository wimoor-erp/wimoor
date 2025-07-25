package com.wimoor.amazon.orders.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.orders.pojo.dto.OrdersFulfillmentDTO;
import com.wimoor.amazon.orders.pojo.entity.FulfillmentOutboundOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface FulfillmentOutboundOrderMapper extends BaseMapper<FulfillmentOutboundOrder> {

    @Select("<script>" +
            "select  * from t_amz_fulfillment_order t "+
            "where  t.fulfillmentOrderStatus in ('New','Received','Planning','Processing','CompletePartialled','Unfulfillable') "+
            " and t.amazonauthid=#{amazonauthid} "+
            " order by  ifnull(t.refreshtime,date_sub(now(), interval 100 day))  limit 1"+
            "</script>")
    FulfillmentOutboundOrder getOrders(String amazonauthid);

    IPage<FulfillmentOutboundOrder> findList(Page<Object> page,@Param("param") OrdersFulfillmentDTO dto);
}
