package com.wimoor.amazon.inbound.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface ShipInboundPlanMapper extends BaseMapper<ShipInboundPlan> {

	IPage<Map<String, Object>> findByCondition(Page<?> page,Map<String, Object> param);

	Map<String, Object> selectShipInfoByformid(@Param("planid") String planid);

	List<Map<String, Object>> selectitemListByPlanid(@Param("planid") String planid, @Param("shipmentid") String shipmentid);

	List<Map<String, Object>> getsumreport(Map<String, Object> param);

	List<Map<String, Object>> selectPlanItemByShipmentid(@Param("shipmentid") String shipmentid);

	List<Map<String, Object>> getShipRecord(@Param("marketplaceid") String marketplaceid, @Param("sku") String sku, @Param("shopid") String shopid);
	List<Map<String, Object>> getShipBadRecord(@Param("marketplaceid") String marketplaceid, @Param("sku") String sku, @Param("shopid") String shopid);

	List<Map<String, Object>> getShipRecordByMarket(String marketplaceid);

	IPage<Map<String, Object>> getShipmentDetailReport(Page<?> page,Map<String, Object> param);
	
	List<Map<String, Object>> getShipmentDetailAssReport(Map<String, Object> param);

	List<Map<String, Object>> getShipmentDetailReport(Map<String, Object> param);

	Map<String, Object> getShipmentDetailReportTotal(Map<String, Object> param);

	IPage<Map<String, Object>> getShipmentReport(Page<?> page,Map<String, Object> param);

	List<Map<String, Object>> getShipmentReport(Map<String, Object> param);

	List<Map<String, Object>> getShipArrivalTimeRecord(@Param("shopid") String shopid, @Param("marketplaceid") String marketplaceid, @Param("sku") String sku);

	List<Map<String, Object>> getShipArrivalTimeRecordForShip(Map<String, Object> param);

}