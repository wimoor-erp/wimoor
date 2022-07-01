package com.wimoor.erp.purchase.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanWareHouse;
@Mapper
public interface PurchasePlanWareHouseMapper extends BaseMapper<PurchasePlanWareHouse>{
     
	public List<Map<String, Object>> getWareHouseIdForPlanId(@Param("planid") String planid, @Param("shopid") String shopid);
	
	public List<Map<String, Object>> getPurchasePlanWareHouseForShopId(@Param("shopid")String shopid);
	
	public int deletePlanWarehouseForPlanId(@Param("planid")String planid);
}