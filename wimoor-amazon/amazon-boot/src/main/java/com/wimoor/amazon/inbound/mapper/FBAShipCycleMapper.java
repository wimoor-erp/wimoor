package com.wimoor.amazon.inbound.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inbound.pojo.entity.FBAShipCycle;
@Mapper
public interface FBAShipCycleMapper extends BaseMapper<FBAShipCycle>{
	
	FBAShipCycle findShipCycleBySKU(@Param("sku")String sku, 
			@Param("marketplaceid")String marketplaceid, @Param("groupid")String groupid);
	
	Map<String, Object> findGroupBySKU(@Param("shopid")String shopid, 
			@Param("sku")String sku, @Param("marketplaceid")String marketplaceid);
	
	List<Map<String, Object>> findGroupByMaterilId(@Param("shopid")String shopid, 
			@Param("materialid")String materialid, @Param("marketplaceid")String marketplaceid);
}