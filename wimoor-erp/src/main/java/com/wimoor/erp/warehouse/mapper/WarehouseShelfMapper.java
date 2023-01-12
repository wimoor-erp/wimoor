package com.wimoor.erp.warehouse.mapper;

import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelf;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 仓库货柜 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Mapper
public interface WarehouseShelfMapper extends BaseMapper<WarehouseShelf> {
	Integer getShelfCount(@Param("shopid")String shopid,@Param("addressid")String addressid);
}
