package com.wimoor.erp.ship.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.ship.pojo.entity.ShipPlanSub;
@Mapper
public interface ShipPlanSubMapper  extends BaseMapper<ShipPlanSub> {

	void deleteAllPlanSub(String planid);
}