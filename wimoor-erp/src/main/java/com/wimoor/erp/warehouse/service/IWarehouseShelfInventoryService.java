package com.wimoor.erp.warehouse.service;

import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.api.erp.warehouse.pojo.vo.WarehouseShelfInventoryVo;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelf;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventory;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptPro;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptRecord;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventorySummaryVo;

import java.util.List;
import java.util.Map;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

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
	public WarehouseShelfInventoryOptRecord add(WarehouseShelfInventoryOptPro inv);
	public WarehouseShelfInventoryOptRecord sub(WarehouseShelfInventoryOptPro inv);
	public IPage<WarehouseShelfInventoryVo> getUnShelfInventoryList(Page<?> page,Map<String, Object> param);
	public IPage<WarehouseShelfInventoryVo> getShelfInventoryList(Page<?> page,Map<String, Object> param);
	public List<WarehouseShelfInventoryVo> findByMaterial(String shopid,String warehouseid,String materialid) ;
	public ShipInboundShipmenSummarytVo formInvAssemblyShelf(ShipInboundShipmenSummarytVo itemsum);
}
