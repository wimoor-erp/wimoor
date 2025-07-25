package com.wimoor.amazon.inboundV2.mapper;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentTemplateFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface ShipInboundShipmentTemplateFileMapper extends BaseMapper<ShipInboundShipmentTemplateFile>{

	ShipInboundShipmentTemplateFile findCustomsByNameAndShopid(@Param("shopid")String shopid, @Param("filename")String filename);

	List<Map<String, Object>> selectCustomsRecordByShop(String shopid);
 
}