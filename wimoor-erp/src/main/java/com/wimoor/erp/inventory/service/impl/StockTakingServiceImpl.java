package com.wimoor.erp.inventory.service.impl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.inventory.mapper.StockTakingMapper;
import com.wimoor.erp.inventory.pojo.entity.StockTaking;
import com.wimoor.erp.inventory.pojo.entity.StockTakingItem;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.inventory.service.IStockTakingItemService;
import com.wimoor.erp.inventory.service.IStockTakingService;
import com.wimoor.erp.util.FileUpload;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import lombok.RequiredArgsConstructor;

@Service("stockTakingService")
@RequiredArgsConstructor
public class StockTakingServiceImpl extends  ServiceImpl<StockTakingMapper,StockTaking> implements IStockTakingService {
	 
	IStockTakingItemService stockTakingItemService;
	 
	IInventoryService inventoryService;
	 
	IWarehouseService warehouseService;

	public List<Map<String, Object>> findByCondition(Map<String, Object> map) {
		return this.baseMapper.findByCondition(map);
	}

 
	public boolean save(StockTaking entity) throws BizException {
		int result = this.baseMapper.insert(entity);
		Warehouse warehouse = warehouseService.getById(entity.getWarehouseid());
		if (warehouse.getIsstocktaking()) {
			throw new BizException("该仓库正在盘点，请稍后再试！");
		} else {
			warehouse.setIsstocktaking(true);
			warehouseService.updateById(warehouse);
		}
		return result>0;
	}
 
 
 

	public StockTaking saveStockTakingAndItem(StockTaking stockTaking, StockTakingItem item, boolean isnew) throws BizException {
		if (isnew) {
			stockTakingItemService.save(item);
		} else {
			stockTakingItemService.updateById(item);
		}
		stockTaking = updateTotalProfitAndLoss(stockTaking);
		return stockTaking;
	}

	public StockTaking updateTotalProfitAndLoss(StockTaking stockTaking) throws BizException {
		// 通过盘点单ID获取盘点盈亏数量和货值
		Map<String, BigDecimal> map = getTotalProfitAndLoss(stockTaking.getId());
		if (map != null) {
			stockTaking.setOveramount(Integer.parseInt(map.get("overamount").toString()));
			stockTaking.setLossamount(Integer.parseInt(map.get("lossamount").toString()));
			stockTaking.setOverprice(map.get("overprice"));
			stockTaking.setLossprice(map.get("lossprice"));
		} else {
			stockTaking.setOveramount(0);
			stockTaking.setLossamount(0);
			stockTaking.setOverprice(new BigDecimal("0"));
			stockTaking.setLossprice(new BigDecimal("0"));

		}
		updateById(stockTaking);
		return stockTaking;
	}

	public Map<String, BigDecimal> getTotalProfitAndLoss(String id) {
		return this.baseMapper.getTotalProfitAndLoss(id);
	}

	public List<Map<String, Object>> getItemList(String id, String warehouseid,String materialid) {
		List<Map<String, Object>> list = this.baseMapper.getItemList(id, warehouseid,materialid);
		return FileUpload.covertPictureImage(list);
	}

	public Map<String, Object> getSumQuantity(String id, String warehouseid) {
		return this.baseMapper.getSumQuantity(id, warehouseid);
	}

	@Transactional
	public boolean endAction(StockTaking stocktaking, String warehouseid, UserInfo user) throws BizException {
		////////// 改变库存数量，使得盘点数量=账面数量//////////////
		inventoryService.stockTakingInvOperate(stocktaking, warehouseid, user);

		Warehouse warehouse = warehouseService.getById(warehouseid);
		if(warehouse!=null){
			warehouse.setIsstocktaking(false);
			warehouseService.updateById(warehouse);
		}
		boolean result = updateById(stocktaking);
		return result;
	}

	@Transactional
	public boolean cancelAction(StockTaking stocktaking, String warehouseid) throws BizException {
		Warehouse warehouse = warehouseService.getById(warehouseid);
		warehouse.setIsstocktaking(false);
		warehouseService.updateById(warehouse);

		///////////// 取消盘点/////////////
		boolean result = this.removeById(stocktaking.getId());
		return result;
	}

	public StockTaking updateStockTakingAndItem(String id, String itemid) throws BizException {
		stockTakingItemService.removeById(itemid);
		StockTaking stockTaking = getById(id);
		stockTaking = updateTotalProfitAndLoss(stockTaking);
		return stockTaking;
	}

	@Override
	public Map<String, Object> selectAllStocktakingBySKU(String warehouseid) {
		List<Warehouse> warehouselist = warehouseService.findWarehouselistByParent(warehouseid);
		if(warehouselist!=null && warehouselist.size()>0) {
			Map<String, Object> map=new LinkedHashMap<String, Object>();
			for(int i=0;i<warehouselist.size();i++) {
				Warehouse warehouse = warehouselist.get(i);
				if(warehouse!=null) {
					String wid = warehouse.getId();
					List<Map<String, Object>> itemlist = this.findStockInvByParentId(wid);
					if(itemlist!=null && itemlist.size()>0) {
						map.put(itemlist.get(0).get("wname").toString(), itemlist);
					}
				}
			}
			return map;
		}else {
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> findStockInvByParentId(String wid) {
		return this.baseMapper.findStockInvByParentId(wid);
	}

	 

	
}
