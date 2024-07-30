package com.wimoor.amazon.inboundV2.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundBox;
@Mapper
public interface ShipInboundBoxV2Mapper extends BaseMapper<ShipInboundBox>{

	List<Map<String, Object>> findShipInboundBox(String shipmentid);
	List<Map<String, Object>> findAllBox(String id);
}