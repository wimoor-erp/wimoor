package com.wimoor.amazon.inbound.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundBox;
@Mapper
public interface ShipInboundBoxMapper extends BaseMapper<ShipInboundBox>{

	List<Map<String, Object>> findShipInboundBox(String shipmentid);

}