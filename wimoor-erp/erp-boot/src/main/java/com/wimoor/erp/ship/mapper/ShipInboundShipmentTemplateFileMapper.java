package com.wimoor.erp.ship.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.ship.pojo.entity.ShipInboundShipmentTemplateFile;
@Mapper
public interface ShipInboundShipmentTemplateFileMapper extends BaseMapper<ShipInboundShipmentTemplateFile>{

	ShipInboundShipmentTemplateFile findCustomsByNameAndShopid(@Param("shopid")String shopid, @Param("filename")String filename);

	List<Map<String, Object>> selectCustomsRecordByShop(String shopid);
 
}