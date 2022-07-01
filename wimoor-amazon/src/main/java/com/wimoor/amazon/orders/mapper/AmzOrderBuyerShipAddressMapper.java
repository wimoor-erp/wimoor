package com.wimoor.amazon.orders.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderBuyerShipAddress;

/**
 * <p>
 * 订单备注 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Mapper
public interface AmzOrderBuyerShipAddressMapper extends BaseMapper<AmzOrderBuyerShipAddress> {

}
