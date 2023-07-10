package com.wimoor.erp.ship.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.ship.pojo.entity.ShipPlanModelItemSub;
@Mapper
public interface ShipPlanModelItemSubMapper  extends BaseMapper<ShipPlanModelItemSub>{

	void deleteByModelid(String id);
 
}