package com.wimoor.amazon.inbound.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundItemVo;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
 

@Mapper
public interface ShipInboundItemMapper extends BaseMapper<ShipInboundItem> {
	List<Map<String, Object>> selectByShipmentid(String id);

	List<Map<String, Object>> shipmentReportBySKU(Map<String, Object> param);
	
	List<Map<String, Object>> shipmentReportByLoistics(Map<String, Object> param);

	List<ShipInboundItemVo> selectObjByShipmentid(String id);
	
	ShipInboundShipmenSummarytVo summaryShipmentItem(String id);
	
	Map<String, Object> shipmentReportBySKUTotal(Map<String, Object> param);

	Map<String, Object> shipmentReportByLoisticsTotal(Map<String, Object> param);
	
	List<Map<String, Object>> shipArrivalTimeChart(Map<String, Object> param);
	
	List<ShipInboundItem>  getOneByShipmentid(String id);
	List<Map<String, Object>> getshotTime(@Param("shopid")String shopid,@Param("groupid")String groupid,
			@Param("marketplaceid")String marketplaceid,@Param("sku")String sku);
	Integer summaryShipmentSku(@Param("groupid")String groupid,@Param("marketplaceid")String marketplaceid,@Param("sku")String sku);
	Map<String, Object> findMaterielByShipPlanId(@Param("id")String id,@Param("sku") String sku);

}