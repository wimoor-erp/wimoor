package com.wimoor.erp.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.order.pojo.entity.OrderListing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface OrderListingMapper  extends BaseMapper<OrderListing> {

    Map<String, Object> summary(@Param("shopid")String shopid);
}
