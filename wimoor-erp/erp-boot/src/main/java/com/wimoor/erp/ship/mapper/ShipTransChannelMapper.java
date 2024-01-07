package com.wimoor.erp.ship.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.ship.pojo.entity.ShipTransChannel;
@Mapper
public interface ShipTransChannelMapper  extends BaseMapper<ShipTransChannel> {
	List<ShipTransChannel> selectListByshopid(@Param("shopid")String shopid,@Param("name")String name);
}