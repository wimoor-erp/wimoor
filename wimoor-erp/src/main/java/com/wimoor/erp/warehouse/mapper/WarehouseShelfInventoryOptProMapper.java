package com.wimoor.erp.warehouse.mapper;

import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptPro;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 预操作，此表内不存储任何记录。当预操作结束后自动删除 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Mapper
public interface WarehouseShelfInventoryOptProMapper extends BaseMapper<WarehouseShelfInventoryOptPro> {

}
