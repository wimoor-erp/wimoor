package com.wimoor.erp.warehouse.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelf;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventory;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptRecord;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventorySummaryVo;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventoryVo;

/**
 * <p>
 * 货架产品库存 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
public interface IWarehouseShelfInventoryService extends IService<WarehouseShelfInventory> {

	WarehouseShelfInventorySummaryVo sumByShelf(WarehouseShelf item);
	public WarehouseShelfInventoryOptRecord add(WarehouseShelfInventoryOptRecord inv);
	public WarehouseShelfInventoryOptRecord sub(WarehouseShelfInventoryOptRecord inv);
	public IPage<WarehouseShelfInventoryVo> getUnShelfInventoryList(Page<?> page,Map<String, Object> param);
	public IPage<WarehouseShelfInventoryVo> getShelfInventoryList(Page<?> page,Map<String, Object> param);

	public ShipInboundShipmenSummarytVo formInvAssemblyShelf(ShipInboundShipmenSummarytVo itemsum);
    public List<WarehouseShelfInventoryVo> getShelfInventoryStockList( Map<String, Object> param);
	IPage<WarehouseShelfInventoryVo> getShelfInventoryStockList(Page<?> page, Map<String, Object> param);
	void downloadShelfStockList(SXSSFWorkbook workbook, List<WarehouseShelfInventoryVo> list);
	List<WarehouseShelfInventoryVo> findByMaterial(String shopid, Warehouse warehouse, String materialid);
}
