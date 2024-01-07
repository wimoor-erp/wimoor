package com.wimoor.amazon.inbound.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.inbound.pojo.dto.ShipPlanListDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inbound.pojo.vo.ShipPlanVo;
import com.wimoor.amazon.inbound.pojo.vo.SummaryShipmentVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface ShipInboundPlanMapper extends BaseMapper<ShipInboundPlan> {

	IPage<ShipPlanVo> findByCondition(Page<?> page,@Param("param") ShipPlanListDTO param);

	List<SummaryShipmentVo> selectitemListByPlanid(@Param("planid") String planid, @Param("shipmentid") String shipmentid);

	List<Map<String, Object>> getsumreport(Map<String, Object> param);

	List<Map<String, Object>> getShipRecordByMarket(@Param("marketplaceid")String marketplaceid,@Param("groupid")String groupid);

	IPage<Map<String, Object>> getShipmentDetailReport(Page<?> page,@Param("param")Map<String, Object> param);
	
	List<Map<String, Object>> getShipmentDetailAssReport(Map<String, Object> param);

	List<Map<String, Object>> getShipmentDetailReport(@Param("param")Map<String, Object> param);

	Map<String, Object> getShipmentDetailReportTotal(Map<String, Object> param);

	IPage<Map<String, Object>> getShipmentReport(Page<?> page,@Param("param")Map<String, Object> param);

	List<Map<String, Object>> getShipmentReport(@Param("param")Map<String, Object> param);

	List<Map<String, Object>> getShipRecord(@Param("groupid") String groupid,@Param("marketplaceid") String marketplaceid, @Param("sku") String sku, @Param("shopid") String shopid);
	
	List<Map<String, Object>> getShipBadRecord(@Param("marketplaceid") String marketplaceid, @Param("sku") String sku, @Param("shopid") String shopid);
	List<Map<String, Object>> getShipArrivalTimeRecord(@Param("shopid") String shopid, @Param("marketplaceid") String marketplaceid, @Param("sku") String sku);

}