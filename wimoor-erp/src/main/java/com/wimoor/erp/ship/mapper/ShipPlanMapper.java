package com.wimoor.erp.ship.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.ship.pojo.entity.ShipPlan;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface ShipPlanMapper extends BaseMapper<ShipPlan> {


	ShipPlan selectByGroupAndWarehouse(@Param("amazongroupid")String amazongroupid, @Param("warehouseid")String warehouseid);

	Integer getPlanAmountByMaterial(@Param("shopid")String shopid, @Param("materialid")String materialid, @Param("warehouseid")String warehouseid, @Param("planid")String planid);

}