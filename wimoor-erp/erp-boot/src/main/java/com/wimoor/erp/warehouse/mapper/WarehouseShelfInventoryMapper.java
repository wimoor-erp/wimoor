package com.wimoor.erp.warehouse.mapper;

import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventoryVo;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelf;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventory;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventorySummaryVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 货架产品库存 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Mapper
public interface WarehouseShelfInventoryMapper extends BaseMapper<WarehouseShelfInventory> {
	WarehouseShelfInventorySummaryVo sumByShelf(WarehouseShelf item);
	List<WarehouseShelfInventoryVo> findByMaterial(@Param("shopid")String shopid,@Param("addressid")String addressid,@Param("warehouseid")String warehouseid,@Param("materialid")String materialid);
	public IPage<WarehouseShelfInventoryVo> getUnShelfInventoryList(Page<?> page,@Param("param") Map<String, Object> param);
	public IPage<WarehouseShelfInventoryVo> getShelfInventoryList(Page<?> page,@Param("param") Map<String, Object> param);
	public IPage<WarehouseShelfInventoryVo> getShelfInventoryStockList(Page<?> page,@Param("param") Map<String, Object> param);
	public List<WarehouseShelfInventoryVo> getShelfInventoryStockList(@Param("param") Map<String, Object> param);
}
