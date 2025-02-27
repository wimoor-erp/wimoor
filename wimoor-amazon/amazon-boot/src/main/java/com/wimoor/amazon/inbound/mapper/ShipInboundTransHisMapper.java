package com.wimoor.amazon.inbound.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTransHis;

@Mapper
public interface ShipInboundTransHisMapper extends BaseMapper<ShipInboundTransHis> {
	List<Map<String,Object>> getSelfTransHis(@Param("shopid") String shopid, @Param("shipmentid") String shipmentid);
	
 
}