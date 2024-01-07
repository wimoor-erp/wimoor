package com.wimoor.erp.stock.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
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
	final IMaterialService iMaterialService;
	final IInventoryService iInventoryService;
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
 
 
 
	@Transactional(rollbackFor = Exception.class)
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

	@Transactional(rollbackFor = Exception.class)
	public boolean endAction(StockTaking stocktaking , UserInfo user) throws BizException {
		////////// 改变库存数量，使得盘点数量=账面数量//////////////
		stockTakingItemService.stockTakingInvOperate(stocktaking, user);
		iStockTakingItemShelfService.stockTakingInvOperate(stocktaking, user);
		this.iStocktakingWarehouseService.end(stocktaking.getId());
		this.iStocktakingShelfService.end(stocktaking.getId());
		boolean result = updateById(stocktaking);
		return result;
	}
	
	@Transactional(rollbackFor = Exception.class)
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




	@SuppressWarnings("unchecked")
	@Override
	public void getExcelStockAllInfoReport(SXSSFWorkbook workbook, Map<String, Object> maps) {
		//下载stocking的模板
		//自动根据主仓库的id 创建多个sheet 每个sheet中带有fullfillable>0库存的产品list
		if(maps!=null && maps.size()>0) {
			for(Entry<String, Object> entry:maps.entrySet()) {
				 //创建sheet
				 String warename=entry.getKey();
				 List<Map<String,Object>> list = (List<Map<String, Object>>) entry.getValue();
				 Sheet sheet = workbook.createSheet(warename);
				 List<String> titlelist = new ArrayList<String>();
				 Map<String, String> titlechange = new HashMap<String, String>();
				 titlelist.add("本地SKU");
				 titlelist.add("产品名称");
				 titlelist.add("可用库存");
				 titlelist.add("盘点数量");
				 titlechange.put("本地SKU", "sku");
				 titlechange.put("产品名称", "name");
				 titlechange.put("可用库存", "quantity");
				 titlechange.put("盘点数量", "quantity");
				 // 在索引0的位置创建行（最顶端的行）
				Row row = sheet.createRow(0);
				for (int i = 0; i < titlelist.size(); i++) {
					Cell cell = row.createCell(i);
					cell.setCellValue(titlelist.get(i));
				}
				for (int i = 0; i < list.size(); i++) {
					Row skurow = sheet.createRow(i + 1);
					Map<String, Object> skumap = list.get(i);
					for (int j = 0; j < titlelist.size(); j++) {
						Cell cell = skurow.createCell(j);
						if (skumap.get(titlechange.get(titlelist.get(j))) == null) {
							cell.setCellValue("-");
						} else {
							cell.setCellValue(skumap.get(titlechange.get(titlelist.get(j))).toString());
						}
					}
				}
				
				
			}
		}
		
	}

	@Override
	public void uploadStockTakingFile(UserInfo user,Workbook workbook,String stockid) {
		//sheet的数量--代表仓位的数量 且仓位有库存SKU
		int sheetNums = workbook.getNumberOfSheets();
		if(sheetNums>0) {
			for(int i=0;i<sheetNums;i++) {
				Sheet sheet = workbook.getSheetAt(i);
				String warehouseName = sheet.getSheetName().trim();
				Warehouse warehouse = warehouseService.getWarehouseByName(user.getCompanyid(), warehouseName);
				if(warehouse!=null) {
					String warehouseid=warehouse.getId();
					for (int j = 1; j <= sheet.getLastRowNum();j++) {
						Row info = sheet.getRow(j);
						boolean isblankrow=true;
						if(info==null) {
							continue;
						}
						Iterator<Cell> iterator = info.cellIterator();
						while(iterator.hasNext()) {
							Cell cell = iterator.next();
							if(cell.getCellTypeEnum() !=CellType.BLANK) {
								isblankrow=false;
								break;
							}
						}
						if (info == null||isblankrow) {
							continue;
						}
						try {
							String sku = null;
							if (info.getCell(0) != null) {
								sku = info.getCell(0).getStringCellValue();
							}
							int amount=0;
							String amountStr=null;
							if (info.getCell(3) != null) {
								Cell cell3 = info.getCell(3);
								cell3.setCellType(CellType.STRING);
								amountStr = cell3.getStringCellValue().trim();
								if(StrUtil.isEmpty(amountStr)) {
									continue;
								}
								if(info.getCell(2) != null) {
									Cell cell2 = info.getCell(2);
									cell2.setCellType(CellType.STRING);
									String invAmount = cell2.getStringCellValue().trim();
									if(invAmount.equals(amountStr)) {
										continue;
									}
								}
							}
							if(StrUtil.isNotEmpty(amountStr)) {
								amount=Integer.parseInt(amountStr);
							}
							if (StrUtil.isEmpty(sku) || amount<0) {
								throw new BizException("Excel文件中必填字段为空！");
							}else {
								Material material = this.iMaterialService.findBySKU(sku, user.getCompanyid());
								if(material!=null) {
									String materialid = material.getId();
									//先删除此盘点单 中已存在的sku的item
									LambdaQueryWrapper<StockTakingItem> query=new LambdaQueryWrapper<StockTakingItem>();
									query.eq(StockTakingItem::getStocktakingid,stockid);
									query.eq(StockTakingItem::getMaterialid,materialid);
									query.eq(StockTakingItem::getWarehouseid,warehouseid);
									stockTakingItemService.remove(query);
									Map<String, Object> maps = this.iInventoryService.getSelfInvBySKU(warehouseid, material.getId());
									BigDecimal fulfillable = new BigDecimal(maps.get("fulfillable").toString());
									StockTakingItem item = new StockTakingItem();;
									int overamount =  amount-fulfillable.intValue();
									boolean isnew = true;
									String itemid = this.warehouseService.getUUID().toString();
									item.setId(itemid);
									item.setStocktakingid(stockid);
									item.setMaterialid(materialid);
									item.setWarehouseid(warehouseid);
									item.setAmount((int)amount);
									if (overamount > 0) {
										item.setOveramount(overamount);
										item.setLossamount(0);
									} else {
										int lossamount = overamount* -1;
										item.setLossamount(lossamount);
										item.setOveramount(0);
									}
									StockTaking stockTaking = this.getById(stockid);
									stockTaking.setOperator(user.getId());
									stockTaking.setOpttime(new Date());
									this.saveStockTakingAndItem(stockTaking, item,isnew);
								}else {
									throw new BizException("未找到对应SKU,请正确填写！");
								}
							}
						} catch (Exception e) {
							
						}
					}
				}else {
					throw new BizException("未找到对应仓库,请正确填写！");
				}
				
			}
			
		}
		
	}
	
	@Override
	public Map<String, Object> selectAllStocktakingBySKU(String stockid) {
			Map<String, Object> map=new LinkedHashMap<String, Object>();
			List<StocktakingWarehouse> list = iStocktakingWarehouseService.lambdaQuery().eq(StocktakingWarehouse::getStocktakingid, stockid).list();
			for(StocktakingWarehouse item:list) {
				List<Map<String, Object>> itemlist = this.findStockInv(item.getWarehouseid().toString());
				if(itemlist!=null && itemlist.size()>0) {
					map.put(itemlist.get(0).get("wname").toString(), itemlist);
				}
			}
			return map;
	}

	@Override
	public List<Map<String, Object>> findStockInv(String wid) {
		return this.baseMapper.findStockInv(wid);
	}
	 

	
}
