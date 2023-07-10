package com.wimoor.erp.stock.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.stock.mapper.StockTakingMapper;
import com.wimoor.erp.stock.pojo.entity.StockTaking;
import com.wimoor.erp.stock.pojo.entity.StockTakingItem;
import com.wimoor.erp.stock.pojo.entity.StockTakingItemShelf;
import com.wimoor.erp.stock.pojo.entity.StocktakingShelf;
import com.wimoor.erp.stock.pojo.entity.StocktakingWarehouse;
import com.wimoor.erp.stock.service.IStockTakingItemService;
import com.wimoor.erp.stock.service.IStockTakingItemShelfService;
import com.wimoor.erp.stock.service.IStockTakingService;
import com.wimoor.erp.stock.service.IStocktakingShelfService;
import com.wimoor.erp.stock.service.IStocktakingWarehouseService;
import com.wimoor.erp.warehouse.pojo.entity.ErpWarehouseAddress;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelf;
import com.wimoor.erp.warehouse.service.IErpWarehouseAddressService;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("stockTakingService")
@RequiredArgsConstructor
public class StockTakingServiceImpl extends  ServiceImpl<StockTakingMapper,StockTaking> implements IStockTakingService {
	final IStockTakingItemService stockTakingItemService;
	final IStockTakingItemShelfService iStockTakingItemShelfService;
	final IStocktakingWarehouseService iStocktakingWarehouseService;
	final IStocktakingShelfService iStocktakingShelfService;
	final IWarehouseService warehouseService;
	final IErpWarehouseAddressService iErpWarehouseAddressService;
	final IWarehouseShelfService warehouseShelfService;
    final FileUpload fileUpload;
	
	public IPage<Map<String, Object>> findByCondition(Page<?> page,Map<String, Object> map) {
		IPage<Map<String, Object>> list = this.baseMapper.findByCondition(page,map);
		 if(list!=null && list.getRecords().size()>0) {
			 for (int i = 0; i < list.getRecords().size(); i++) {
				 Map<String, Object> item = list.getRecords().get(i);
				 if(item.get("warehouseids")!=null) {
					 List<Warehouse> warehousenameList=new ArrayList<Warehouse>();
					 String warehouseids = item.get("warehouseids").toString();
					 String[] wareArray = warehouseids.split(",");
					 if(wareArray.length>0) {
						 for (int j = 0; j < wareArray.length; j++) {
							 String warehouseid = wareArray[j];
							 if(StrUtil.isNotEmpty(warehouseid)) {
								 Warehouse warehouse = warehouseService.getById(warehouseid);
								 if(warehouse!=null) {
									 warehousenameList.add(warehouse);
								 }
							 }
						 }
						 item.put("warehouselist", warehousenameList);
					 }
				 }
				 if(item.get("shelfids")!=null) {
					 String shelfids = item.get("shelfids").toString();
					 String[] shelfArray = shelfids.split(",");
					 if(shelfArray.length>0) {
						 List<WarehouseShelf> shelfList=new ArrayList<WarehouseShelf>();
						 for (int k = 0; k < shelfArray.length; k++) {
							 String shelfid = shelfArray[k];
							 if(StrUtil.isNotEmpty(shelfid)) {
								 WarehouseShelf shelf = warehouseShelfService.getById(shelfid);
								 
								 if(shelf!=null) {
									 String name = warehouseShelfService.getAllParentName(shelf.getId());
									 ErpWarehouseAddress address = iErpWarehouseAddressService.getById(shelf.getAddressid());
									 shelf.setName(address.getName()+"/"+name);
									 shelfList.add(shelf);
								 }
							 }
						 }
						 item.put("shelflist", shelfList);
					 }
				 }
			 }
		 }
		 return list;
	}

 
	public boolean save(StockTaking entity) throws BizException {
		if(entity.getFtype()==1&&entity.getWarehouselist()!=null) {
			for(StocktakingWarehouse item:entity.getWarehouselist()) {
				item.setStocktakingid(new BigInteger(entity.getId()));
			}
			this.iStocktakingWarehouseService.saveData(entity.getWarehouselist());
		}else if(entity.getShelflist()!=null){
			for(StocktakingShelf item:entity.getShelflist()) {
				item.setStocktakingid(new BigInteger(entity.getId()));
			}
			this.iStocktakingShelfService.saveData(entity.getShelflist());
		}
		return this.baseMapper.insert(entity)>0;
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

	public List<Map<String, Object>> getItemList(String id, String warehouseid,String search) {
		List<Map<String, Object>> list =stockTakingItemService.getItemList(id, warehouseid,search);
		return fileUpload.covertPictureImage(list);
	}

	public Map<String, Object> getSumQuantity(String id, String warehouseid) {
		return this.baseMapper.getSumQuantity(id, warehouseid);
	}

	
	public boolean endAction(StockTaking stocktaking , UserInfo user) throws BizException {
		////////// 改变库存数量，使得盘点数量=账面数量//////////////
		stockTakingItemService.stockTakingInvOperate(stocktaking, user);
		iStockTakingItemShelfService.stockTakingInvOperate(stocktaking, user);
		this.iStocktakingWarehouseService.end(stocktaking.getId());
		this.iStocktakingShelfService.end(stocktaking.getId());
		boolean result = updateById(stocktaking);
		return result;
	}

	public boolean cancelAction(StockTaking stocktaking) throws BizException {
		iStocktakingShelfService.cancel(stocktaking.getId());
		iStocktakingWarehouseService.cancel(stocktaking.getId());
		//级联删除item
		stockTakingItemService.remove(new LambdaQueryWrapper<StockTakingItem>().eq(StockTakingItem::getStocktakingid, stocktaking.getId()));
		iStockTakingItemShelfService.remove(new LambdaQueryWrapper<StockTakingItemShelf>().eq(StockTakingItemShelf::getStocktakingid, stocktaking.getId()));
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
