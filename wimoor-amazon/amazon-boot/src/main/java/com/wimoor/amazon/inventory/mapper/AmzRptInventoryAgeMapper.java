package com.wimoor.amazon.inventory.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inventory.pojo.entity.AmzRptInventoryAge;

/**
 * <p>
 * GET_FBA_INVENTORY_AGED_DATA Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-08
 */
@Mapper
public interface AmzRptInventoryAgeMapper extends BaseMapper<AmzRptInventoryAge> {

}
