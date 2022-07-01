package com.wimoor.amazon.inbound.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface ShipAddressMapper extends BaseMapper<ShipAddress> {
	IPage<ShipAddress> findByCondition(Page<?> page,@Param("shopid")String shopid,@Param("groupid")String groupid,@Param("disable")String disable,@Param("search")String search);
}