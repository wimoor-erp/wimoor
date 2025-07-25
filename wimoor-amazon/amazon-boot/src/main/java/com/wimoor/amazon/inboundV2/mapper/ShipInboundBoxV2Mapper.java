package com.wimoor.amazon.inboundV2.mapper;

import java.util.List;
import java.util.Map;

import feign.Param;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundBox;
@Mapper
public interface ShipInboundBoxV2Mapper extends BaseMapper<ShipInboundBox>{

	List<Map<String, Object>> findShipInboundBox(String shipmentid);
	List<Map<String, Object>> findAllBox(String id);
    List<Map<String, Object>> getAreCaseBoxInfo(@Param("formid")String formid,@Param("shipmentid") String shipmentid);
	List<Map<String, Object>> findListByPackageGroupidCase(@Param("formid")String formid,@Param("packingGroupId") String packingGroupId);
    List<Map<String, Object>> findListAllByShipmentid(@Param("formid")String formid, @Param("shipmentid")String shipmentid);
}