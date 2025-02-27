package com.wimoor.amazon.inboundV2.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentBoxItem;

@Mapper
public interface ShipInboundShipmentBoxItemMapper extends BaseMapper<ShipInboundShipmentBoxItem> {

	List<ShipInboundShipmentBoxItem> findByShipment(String shipmentid);

}
