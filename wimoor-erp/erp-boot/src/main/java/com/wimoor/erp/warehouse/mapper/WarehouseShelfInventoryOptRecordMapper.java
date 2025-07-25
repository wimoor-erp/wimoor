package com.wimoor.erp.warehouse.mapper;

import java.util.List;
import java.util.Map;

import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptRecord;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventoryOptRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 操作记录 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Mapper
public interface WarehouseShelfInventoryOptRecordMapper extends BaseMapper<WarehouseShelfInventoryOptRecord> {

	IPage<Map<String, Object>> getOptList(Page<?> page,@Param("shopid")String shopid,@Param("warehouseid")String warehouseid,@Param("search")String search,
			@Param("startDate")String startDate,@Param("endDate")String endDate);


    List<WarehouseShelfInventoryOptRecordVo> selectRecordsByParams(@Param("param")Map<String, Object> param);
}
