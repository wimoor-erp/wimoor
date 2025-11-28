package com.wimoor.amazon.inboundV2.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentBox;

@Mapper
public interface ShipInboundShipmentBoxMapper extends BaseMapper<ShipInboundShipmentBox> {

	List<Map<String, Object>> findShipInboundBox(String shipmentid);

}
