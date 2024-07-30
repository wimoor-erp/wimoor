package com.wimoor.erp.ship.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.ship.pojo.entity.ShipPlanItem;
@Mapper
public interface ShipPlanItemMapper extends BaseMapper<ShipPlanItem> {

	List<Map<String, Object>> summaryPlan(String planid);

	ShipPlanItem findItemByCondition(@Param("planid") String planid,@Param("marketplaceid") String marketplaceid, @Param("sku") String sku);
	
	List<ShipPlanItem> getPlanItemByMaterial(@Param("planid") String planid, @Param("materialid") String materialid);
	
	List<Map<String, Object>> summaryPlanByMarketplace(String planid);
	
	List<Map<String, Object>> summaryPlanEUItemByMarketplace(String planid);
	
	List<Map<String, Object>> summaryPlanByMarketplace2(String planid);

	List<Map<String, Object>> findPlanSubDetail(@Param("planid")String planid, @Param("marketplaceid")String marketplaceid, @Param("warehouseid")String warehouseid);
	
	List<Map<String, Object>> findPlanEuSubDetail(@Param("planid")String planid, @Param("marketplaceid")String marketplaceid, @Param("warehouseid")String warehouseid);
	
	List<Map<String, Object>> getSubmitSubPlanItem(String planid);

	List<Map<String, Object>> selectByplansubid(String plansubid);

	void deleteAllPlanItem(String planid);

 }