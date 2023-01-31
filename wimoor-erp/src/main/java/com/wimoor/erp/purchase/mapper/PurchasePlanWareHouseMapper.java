package com.wimoor.erp.purchase.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanWareHouse;
@Mapper
public interface PurchasePlanWareHouseMapper extends BaseMapper<PurchasePlanWareHouse>{
     
	public List<PurchasePlanWareHouse> getWareHouseIdForPlanId(@Param("planid") String planid, @Param("shopid") String shopid);
	
	public List<PurchasePlanWareHouse> getPurchasePlanWareHouseForShopId(@Param("shopid")String shopid);
	
}