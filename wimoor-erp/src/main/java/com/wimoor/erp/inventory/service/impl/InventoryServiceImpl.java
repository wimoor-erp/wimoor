package com.wimoor.erp.inventory.service.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.common.pojo.entity.Operate;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.inventory.mapper.InventoryMapper;
import com.wimoor.erp.inventory.mapper.InventoryRecordMapper;
import com.wimoor.erp.inventory.pojo.entity.Inventory;
import com.wimoor.erp.inventory.pojo.entity.InventoryHis;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.pojo.entity.InventoryRecord;
import com.wimoor.erp.inventory.pojo.entity.StockTaking;
import com.wimoor.erp.inventory.service.IInventoryHisService;
import com.wimoor.erp.inventory.service.IInventoryRecordService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.inventory.service.IStockTakingService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.util.FileUpload;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import lombok.RequiredArgsConstructor;
 

//??????(???????????????save???insert???????????????)
@Service("inventoryService")
@RequiredArgsConstructor
public class InventoryServiceImpl  extends ServiceImpl<InventoryMapper,Inventory> implements IInventoryService {
	 
	final InventoryMapper inventoryMapper;
	 
	@Lazy
	@Autowired
	IMaterialService materialService;
	 
	final InventoryRecordMapper inventoryRecordMapper;
	 
	final IWarehouseService warehouseService;
	 
	final IStockTakingService stockTakingService;
	 
	final IInventoryHisService inventoryHisService;
	 
	final IInventoryRecordService inventoryRecordService;
  

	// ?????????????????????
	private InventoryRecord setInitRecord(InventoryParameter para, Status status, Operate operate) {
		InventoryRecord newRecord = new InventoryRecord();
		newRecord.setQuantity(para.getAmount());
		newRecord.setOperate(operate.getValue());
		newRecord.setFormid(para.getFormid());
		newRecord.setFormtype(para.getFormtype());
		newRecord.setMaterialid(para.getMaterial());
		newRecord.setWarehouseid(para.getWarehouse());
		newRecord.setOperator(para.getOperator());
		newRecord.setOpttime(para.getOpttime());
		newRecord.setShopid(para.getShopid());
		newRecord.setNumber(para.getNumber());
		newRecord.setStatus(status.getValue());
		newRecord.setOpttime(new Date());
		newRecord.setInvqty(para.getInvqty());
		return newRecord;
	}

	void insertHis(Inventory addinv) throws BizException {
		InventoryHis his = new InventoryHis();
		his.setNewInventory(addinv);
		InventoryHis invhis = inventoryHisService.selectOne(his);
		if (invhis != null) {
			invhis.setNewInventory(addinv);
			inventoryHisService.updateById(invhis);
		} else {
			inventoryHisService.save(his);
		}
	}

	// ?????????????????????????????????1.?????????????????????2.???????????????
	public Warehouse isOkForOperate(String warehouseid, EnumByInventory enumByInventory) throws BizException {
		Warehouse warehouse = warehouseService.getParentWarehouse(warehouseid);
		if (warehouse == null) {
			throw new BizException("???????????????????????????????????????");
		}
		if (warehouse.getDisabled()) {
			throw new BizException("????????????????????????????????????");
		}
		if (warehouse.getIsstocktaking() && !EnumByInventory.isstocktaking.equals(enumByInventory)) {
			throw new BizException("????????????????????????????????????????????????????????????");
		}
		return warehouse;
	}

	// ?????????????????????
	public int AddStockByStatus(InventoryParameter para, Status status, Operate operate) throws BizException {
		para=para.clone();
		if(para.getAmount()==null)return 0;
		int quantity = para.getAmount();
		if (quantity == 0)
			return 0;
		int count = 0;
		Warehouse warehouseobj = isOkForOperate(para.getWarehouse(), para.getStatus());
//		ServletContext session = para.getSession();
//		if (session == null) {
//			throw new BizException("????????????????????????????????????");
//		}
		try {
			Inventory addinv = inventoryMapper.selectNowInv(para.getWarehouse(), para.getMaterial(), para.getShopid(), para.getInvStatus(status));
			if (addinv != null) {
				Integer oldQuantity = addinv.getQuantity();
				oldQuantity = oldQuantity + quantity;
				setInitOldInventory(para, addinv);
				if(!(warehouseobj.getIshungry()&& status.value.equals(Status.fulfillable.value))) {
					if (oldQuantity == null || oldQuantity < 0) {
						String sku = para.getMaterial();
						String warehouse = para.getWarehouse();
						if (!GeneralUtil.isEmpty(sku) && !GeneralUtil.isEmpty(warehouse)) {
							throw new BizException(sku + "???" + warehouse + "????????????,??????????????????0???");
						} else {
							throw new BizException("????????????,??????????????????0???");
						}
					}
			    }
				addinv.setQuantity(oldQuantity);
				count += this.baseMapper.updateById(addinv);
			} else {
				addinv = setInitInventory(para);
				if(!(warehouseobj.getIshungry()&& status.value.equals(Status.fulfillable.value))) {
					if (quantity < 0) {
						throw new BizException("????????????,??????????????????0???");
					}
				}
				addinv.setQuantity(quantity);
				addinv.setStatus(para.getInvStatus(status));
				count += this.baseMapper.insert(addinv);
			}
			para.setInvqty(addinv.getQuantity());
			InventoryRecord record = setInitRecord(para, status, operate);
			inventoryRecordService.save(record);
			insertHis(addinv);
		} finally {
			//session.removeAttribute("inventory" + para.getWarehouse() + para.getMaterial() + para.getShopid() + para.getInvStatus(status));
		}
		return count;
	}

	// ?????????????????????
	public int SubStockByStatus(InventoryParameter para, Status status, Operate operate) throws BizException {
		para=para.clone();
		int quantity = para.getAmount();
		int count = 0;
		if (quantity == 0)
			return 0;
		Warehouse warehouseobj = isOkForOperate(para.getWarehouse(), para.getStatus());
//		ServletContext session = para.getSession();
//		if (session == null) {
//			throw new BizException("????????????????????????????????????");
//		}
//		Object flag = session.getAttribute("inventory" + para.getWarehouse() + para.getMaterial() + para.getShopid() + para.getInvStatus(status));
//		if (flag != null) {
//			session.setAttribute("inventory" + para.getWarehouse() + para.getMaterial() + para.getShopid() + para.getInvStatus(status), Integer.parseInt(flag.toString())+1);
//			throw new BizException("?????????????????????????????????");
//		} else {
//			session.setAttribute("inventory" + para.getWarehouse() + para.getMaterial() + para.getShopid() + para.getInvStatus(status), 1);
//		}
		try {
			Inventory subinv = inventoryMapper.selectNowInv(para.getWarehouse(), para.getMaterial(), para.getShopid(), para.getInvStatus(status));
			if (subinv != null) {
				Integer oldQuantity = subinv.getQuantity();
				oldQuantity = oldQuantity - quantity;
				if(!(warehouseobj.getIshungry()&& status.value.equals(Status.fulfillable.value))) {
					if (oldQuantity == null || oldQuantity < 0) {
						if (para != null && para.getMaterial() != null) {
							Material material = materialService.getById(para.getMaterial());
							if (material != null) {
								throw new BizException("SKU???" + material.getSku() + "??? ???????????????");
							} else {
								throw new BizException("???????????????");
							}
						} else {
							throw new BizException("???????????????");
						}
					}
				}
				subinv.setQuantity(oldQuantity);
				if(updateById(subinv)) {
					count++ ;
				}
			}else if(warehouseobj.getIshungry()&& status.value.equals(Status.fulfillable.value)) {
				subinv = setInitInventory(para);
				subinv.setStatus(para.getInvStatus(status));
				Integer oldQuantity = subinv.getQuantity();
				oldQuantity = oldQuantity - quantity;
				subinv.setQuantity(oldQuantity);
				count += this.baseMapper.insert(subinv);
			} else {
				if (para != null && para.getMaterial() != null) {
					Material material = materialService.getById(para.getMaterial());
					if (material != null) {
						throw new BizException("SKU???" + material.getSku() + "??? ???????????????");
					} else {
						throw new BizException("???????????????");
					}
				} else {
					throw new BizException("???????????????");
				}
			}
			para.setInvqty(subinv.getQuantity());
			InventoryRecord record = setInitRecord(para, status, operate);
			inventoryRecordService.save(record);
			insertHis(subinv);
		} finally {
			//session.removeAttribute("inventory" + para.getWarehouse() + para.getMaterial() + para.getShopid() + para.getInvStatus(status));
		}
		return count;
	}

	// ?????????inventory??????
	private Inventory setInitInventory(InventoryParameter para) {
		Inventory newInv = new Inventory();
		newInv.setQuantity(0);
		newInv.setWarehouseid(para.getWarehouse());
		newInv.setMaterialid(para.getMaterial());
		newInv.setOperator(para.getOperator());
		newInv.setOpttime(para.getOpttime());
		newInv.setShopid(para.getShopid());
		return newInv;
	}

	// ??????oldinventory??????
	private void setInitOldInventory(InventoryParameter para, Inventory oldInv) {
		oldInv.setWarehouseid(para.getWarehouse());
		oldInv.setMaterialid(para.getMaterial());
		oldInv.setOperator(para.getOperator());
		oldInv.setOpttime(para.getOpttime());
		oldInv.setShopid(para.getShopid());
	}

	// ??????(????????????)
	public Integer inStockByDirect(InventoryParameter para) throws BizException {
		int result = 0;
		// ??????warehouseid???materialid??????????????????SKU
		AddStockByStatus(para, Status.fulfillable, Operate.in);
		return result;
	}

	public Inventory selectNowInv(String warehouseid, String materialid, String shopid, Status status) {
		Inventory inv = inventoryMapper.selectNowInv(warehouseid, materialid, shopid, status.getValue());
		return inv;
	}

	// ?????????
	public Integer inStockByReady(InventoryParameter para) throws BizException {
		if (para.getStatus() == EnumByInventory.Ready) {
			return AddStockByStatus(para, Status.inbound, Operate.readyin);
		}
		if (para.getStatus() == EnumByInventory.alReady) {
			SubStockByStatus(para, Status.inbound, Operate.out);
			return AddStockByStatus(para, Status.fulfillable, Operate.in);
		}
		return 0;
	}

	// ??????(????????????)
	public Integer outStockByDirect(InventoryParameter para) throws BizException {
		return SubStockByStatus(para, Status.fulfillable, Operate.out);
	}

	// ?????????
	public Integer outStockByReady(InventoryParameter para) throws BizException {
		int result = 0;
		if (para.getStatus() == EnumByInventory.Ready) {
			SubStockByStatus(para, Status.fulfillable, Operate.readyout);
			result = AddStockByStatus(para, Status.outbound, Operate.readyout);
		}
		if (para.getStatus() == EnumByInventory.alReady) {
			result = SubStockByStatus(para, Status.outbound, Operate.out);
		}
		return result;
	}

	public Integer outStockByReadyChange(InventoryParameter para) throws BizException {
		int result = 0;
		if (para.getStatus() == EnumByInventory.Ready) {
			SubStockByStatus(para, Status.fulfillable, Operate.changeadd);
			result = AddStockByStatus(para, Status.outbound, Operate.changeadd);
		}
		return result;
	}

	public Integer outStockReadyChange(InventoryParameter para) throws BizException {
		int result = 0;
		if (para.getStatus() == EnumByInventory.Ready) {
			SubStockByStatus(para, Status.outbound, Operate.changesub);
			result = AddStockByStatus(para, Status.fulfillable, Operate.changesub);
		}
		return result;
	}

	// ?????????????????????(?????????????????????inventory??????)
	public Integer inStockDirectCancel(InventoryParameter para) throws BizException {
		return SubStockByStatus(para, Status.fulfillable, Operate.cancel);
	}

	// ?????????????????????
	public Integer inStockReadyCancel(InventoryParameter para) throws BizException {
		int result = 0;
		if (para.getStatus() == EnumByInventory.Ready) {
			result = SubStockByStatus(para, Status.inbound, Operate.cancel);
		}
		if (para.getStatus() == EnumByInventory.alReady) {
			result = SubStockByStatus(para, Status.fulfillable, Operate.out);
		}
		return result;
	}

	// ?????????????????????
	public Integer outStockDirectCancel(InventoryParameter para) throws BizException {
		return AddStockByStatus(para, Status.fulfillable, Operate.cancel);
	}

	// ?????????????????????
	public Integer outStockReadyCancel(InventoryParameter para) throws BizException {
		int result = 0;
		if (para.getStatus() == EnumByInventory.Ready) {
			SubStockByStatus(para, Status.outbound, Operate.cancel);
			result = AddStockByStatus(para, Status.fulfillable, Operate.cancel);
		}
		if (para.getStatus() == EnumByInventory.alReady) {
			result = AddStockByStatus(para, Status.fulfillable, Operate.cancel);
		}
		return result;
	}

 
	public boolean updateById(Inventory entity) throws BizException {
		if (entity.getQuantity() == 0) {
			return this.removeById(entity.getId());
		} else {
			return this.baseMapper.updateById(entity)>0?true:false;
		}
	}

	public IPage<Map<String, Object>> findByTypeWithStockCycle(Page<?> page,String ftype, String id, String shopid) {
		IPage<Map<String, Object>> list = null;
		if (ftype.equals("FBA")) {
			list = inventoryMapper.findFBAWithStockCycle(page,id, shopid);
		} else {
			list = inventoryMapper.findNotFBAWithStockCycle(page,id, shopid);
		}
		return list;
	}

 
 
	public IPage<Map<String,Object>> findLocalInventory(Page<?> page,Map<String,Object> param) {
		return inventoryMapper.findLocalInventory(page,param);
	}
	public Map<String, Object> findSumByType(Map<String,Object> param) {
		Map<String, Object> map = null;
		String ftype=null;
		if(param.get("ftype")!=null) {
			ftype=param.get("ftype").toString();
		}
		if (ftype != null && ftype.equals("FBA")) {
			map = inventoryMapper.findFBASum(param);
		} else {
			map = inventoryMapper.findNotFBASum(param);
		}
		return map;
	}

	public IPage<Map<String, Object>> findInventoryDetail(Page<?> page,Map<String, Object> warehouseMap) {
		return inventoryMapper.findInventoryDetail(page,warehouseMap);
	}

	public List<Map<String, Object>> findInventoryDetailForExport(Map<String, Object> warehouseMap) {
		return inventoryMapper.findInventoryDetail(warehouseMap);
	}

	public Map<String, Object> findInvDetailById(String materialid, String warehouseid, String shopid) {
		return inventoryMapper.findInvDetailById(materialid, warehouseid, shopid);
	}

	public Map<String, Object> findFBAInvDetailById(String sku, String warehouseid, String shopid,String groupid) {
		return inventoryMapper.findFBAInvDetailById(sku, warehouseid, shopid,groupid);
	}

	public List<Map<String, Object>> findInboundDetail(String materialid, String warehouseid, String shopid) {
		return inventoryMapper.findInboundDetail(materialid, warehouseid, shopid);
	}

	public List<Map<String, Object>> findOutboundDetail(String materialid, String warehouseid, String shopid) {
		return inventoryMapper.findOutboundDetail(materialid, warehouseid, shopid);
	}

	public IPage<Map<String, Object>> selectInventoryDetail(Page<?> page,Map<String, Object> param) {
		IPage<Map<String, Object>> list = inventoryMapper.selectInventoryDetail(page,param);
		return list;
	}

	public List<Map<String, Object>> getSelfInvDetail(String warehouseid, String stocktakingid) {
		return inventoryMapper.getSelfInvDetail(warehouseid, stocktakingid);
	}

	public Map<String, Object> getSelfInvBySKU(String warehouseid, String materialid) {
		Map<String, Object> maps = inventoryMapper.getSelfInvBySKU(new BigInteger(warehouseid),new BigInteger( materialid));
		if (maps != null) {
			String image = null;
			if (maps.get("image") != null)
				image = maps.get("image").toString();
			if (maps.get("image") != null)
				maps.put("image", FileUpload.getPictureImage(image));
			else
				maps.put("image", "images/systempicture/noimage40.png");
		}
		return maps;
	}

	public List<Map<String, Object>> findInvChgRateReport(Map<String, Object> param) {
		return inventoryMapper.getInvChangeRate(param);
	}

	public List<Map<String, Object>> localInventoryByDay(Map<String, Object> param) {
		return inventoryMapper.localInventoryByDay(param);
	}

	public List<Map<String, Object>> localOutInventoryByRange(Map<String, Object> param) {
		return inventoryMapper.localOutInventoryByRange(param);
	}

	public List<Map<String, Object>> findNotFbaBySku(String warehouseid, String skuid, String shopid) {
		return inventoryMapper.findNotFBABySku(warehouseid, skuid, shopid);
	}

	@Transactional
	public void stockTakingInvOperate(StockTaking stocktaking, String warehouseid, UserInfo user) throws BizException {
		List<String> skulist = new ArrayList<String>();
		List<Warehouse> warehouseList = warehouseService.getSubWarehouse(warehouseid);// ???????????????????????????
		for (int i = 0; i < warehouseList.size(); i++) {
			List<Map<String, Object>> list = stockTakingService.getItemList(stocktaking.getId(), warehouseList.get(i).getId(),null);
			if (list != null && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					Map<String, Object> map = list.get(j);
					int amount = Integer.parseInt(map.get("amount").toString());// ????????????
					int outbound = Integer.parseInt(map.get("outbound").toString());
					if (amount < outbound) {// ??????????????????<outbound
						skulist.add(map.get("sku").toString());
					}
				}

				if (skulist.size() > 0) {
					throw new BizException(skulist + " ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
				} else {
					for (int j = 0; j < list.size(); j++) {
						Map<String, Object> map = list.get(j);
						int amount = Integer.parseInt(map.get("amount").toString());// ????????????
						int fulfillable = Integer.parseInt(map.get("fulfillable").toString());
						int outbound = Integer.parseInt(map.get("outbound").toString());
						int overamount = Integer.parseInt(map.get("overamount").toString());
						int lossamount = Integer.parseInt(map.get("lossamount").toString());

						InventoryParameter invpara = new InventoryParameter();
					 
						invpara.setWarehouse(warehouseList.get(i).getId());
						if (map.get("materialid") == null)
							continue;
						invpara.setMaterial(map.get("materialid").toString());
						invpara.setShopid(stocktaking.getShopid());
						invpara.setAmount(overamount);
						invpara.setFormid(stocktaking.getId());
						invpara.setFormtype("stocktaking");
						invpara.setNumber(stocktaking.getNumber());
						invpara.setStatus(EnumByInventory.isstocktaking);
						invpara.setOperator(stocktaking.getOperator());
						invpara.setOpttime(new Date());
						if (amount >= fulfillable + outbound) {// ??????????????????>=????????????
							///// ????????????,fulfillable = fulfillable + overamount;
							AddStockByStatus(invpara, Status.fulfillable, Operate.in);

						} else if (amount < fulfillable + outbound && amount >= outbound) {// ??????outbound=<????????????<????????????
							///// ????????????,fulfillable = fulfillable - lossamount;
							invpara.setAmount(lossamount);
							SubStockByStatus(invpara, Status.fulfillable, Operate.out);
						}
					}
				}
			}
		}
	}

	public IPage<Map<String, Object>> selectInventoryCost(Page<?> page,String warehouseid, String sku, String shopid, String byday) {
		if (GeneralUtil.isEmpty(sku)) {
			sku = null;
		} else {
			sku = "%" + sku + "%";
		}
		IPage<Map<String, Object>> pagelist = null;
		Map<String, Object> map = null;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date mbyday = fmt.parse(byday);
			Date today = GeneralUtil.getDateNoTime(new Date());
			if (byday.equals(fmt.format(today)) || mbyday.after(today)) {
				pagelist = inventoryMapper.findInventoryNowCost(page,warehouseid, sku, shopid);
				map = inventoryMapper.findInventoryNowCostTotal(warehouseid, sku, shopid);
			} else {
				pagelist = inventoryMapper.findInventoryCost(page,warehouseid, sku, shopid, byday);
				map = inventoryMapper.findInventoryCostTotal(warehouseid, sku, shopid, byday);
			}
		} catch (ParseException e) {
			throw new BizException("????????????????????????");
		}
		if (pagelist != null && pagelist.getRecords().size() > 0 && map != null) {
			for (Map<String, Object> pagemap : pagelist.getRecords()) {
				String sku_m = pagemap.get("sku").toString();
				Material material = materialService.findBySKU(sku_m, shopid);
				if (material != null) {
					String image_location = materialService.getImage(material);
					pagemap.put("image", image_location);
					pagemap.put("name", material.getName());
				}
			}
			pagelist.getRecords().get(0).put("allotherFeer", map.get("otherFeer"));
			pagelist.getRecords().get(0).put("allprice", map.get("totalprice"));
			pagelist.getRecords().get(0).put("allfull", map.get("fulfillable"));
			pagelist.getRecords().get(0).put("allinbound", map.get("inbound"));
			pagelist.getRecords().get(0).put("allinpasscost", map.get("totalinpasscost"));
			pagelist.getRecords().get(0).put("allinpasspay", map.get("totalinpasspay"));
		}
		return pagelist;
	}
	public Map<String, Object> findInvTUDetailByParentId(String materialid,  String shopid) {
		return inventoryMapper.findInvTUDetailByParentId(materialid, null, shopid);
	}
	public Map<String, Object> findInvTUDetailByParentId(String materialid, String warehouseid, String shopid) {
		String[] warehouseidArray = warehouseid.split(",");
		if (warehouseidArray.length == 1) {
			return inventoryMapper.findInvTUDetailByParentId(materialid, warehouseid, shopid);
		} else {
			Map<String, Object> maps = new HashMap<String, Object>();
			Integer inbound = 0;
			Integer outbound = 0;
			Integer fulfillable = 0;
			for (int i = 0; i < warehouseidArray.length; i++) {
				String wareid = warehouseidArray[i];
				Map<String, Object> result = inventoryMapper.findInvTUDetailByParentId(materialid, wareid, shopid);
				if (result == null) {
					continue;
				}
				if (result.get("inbound") != null) {
					inbound += Integer.parseInt(result.get("inbound").toString());
				}
				if (result.get("outbound") != null) {
					outbound += Integer.parseInt(result.get("outbound").toString());
				}
				if (result.get("fulfillable") != null) {
					fulfillable += Integer.parseInt(result.get("fulfillable").toString());
				}
			}
			maps.put("inbound", inbound);
			maps.put("outbound", outbound);
			maps.put("fulfillable", fulfillable);
			maps.put("materialid", materialid);
			return maps;
		}
	}

 

	public void getExcelBookFBAInventoryReport(SXSSFWorkbook workbook, List<Map<String, Object>> inventoryList) {
		Sheet sheet = workbook.createSheet("sheet1");
		List<String> titlelist = new ArrayList<String>();
		Map<String, String> titlechange = new HashMap<String, String>();
		titlelist.add("??????");
		titlelist.add("FBA??????");
		titlelist.add("SKU");
		titlelist.add("??????");
		titlelist.add("??????");
		titlelist.add("?????????");
		titlelist.add("??????");
		titlelist.add("????????????");
		titlelist.add("?????????");
		titlelist.add("????????????");
		titlelist.add("??????");

		titlechange.put("??????", "groupname");
		titlechange.put("FBA??????", "warehouse");
		titlechange.put("SKU", "sku");
		titlechange.put("??????", "afnWarehouseQuantity");
		titlechange.put("??????", "afnFulfillableQuantity");
		titlechange.put("?????????", "afnUnsellableQuantity");
		titlechange.put("??????", "afnReservedQuantity");
		titlechange.put("????????????", "afnInboundWorkingQuantity");
		titlechange.put("?????????", "afnInboundShippedQuantity");
		titlechange.put("????????????", "afnInboundReceivingQuantity");
		titlechange.put("??????", "afnResearchingQuantity");

		// ?????????0???????????????????????????????????????
		Row row = sheet.createRow(0);
		for (int i = 0; i < titlelist.size(); i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(titlelist.get(i));
		}

		for (int i = 0; i < inventoryList.size(); i++) {
			Row skurow = sheet.createRow(i + 1);
			Map<String, Object> skumap = inventoryList.get(i);
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

	public void getExcelBookNotFBAInventoryReport(SXSSFWorkbook workbook, List<Map<String, Object>> inventoryList) {
		Sheet sheet = workbook.createSheet("sheet1");
		List<String> titlelist = new ArrayList<String>();
		Map<String, String> titlechange = new HashMap<String, String>();
		titlelist.add("????????????");
		titlelist.add("SKU");
		titlelist.add("????????????");
		titlelist.add("?????????");
		titlelist.add("??????");
		titlelist.add("?????????");

		titlechange.put("????????????", "warehouse");
		titlechange.put("SKU", "sku");
		titlechange.put("????????????", "pname");
		titlechange.put("?????????", "inbound");
		titlechange.put("??????", "fulfillable");
		titlechange.put("?????????", "outbound");

		// ?????????0???????????????????????????????????????
		Row row = sheet.createRow(0);
		for (int i = 0; i < titlelist.size(); i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(titlelist.get(i));
		}

		for (int i = 0; i < inventoryList.size(); i++) {
			Row skurow = sheet.createRow(i + 1);
			Map<String, Object> skumap = inventoryList.get(i);
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

	public void setExcelBookInventoryReport(SXSSFWorkbook workbook, List<Map<String, Object>> warehouseList_FBA,
			List<Map<String, Object>> warehouseList, List<Map<String, Object>> warehouseDetailList) {
		Sheet sheet = workbook.createSheet("sheet1");
		Map<String, Integer> titlemap = new HashMap<String, Integer>();
		Map<String, String> titlechange = new HashMap<String, String>();
		titlemap.put("SKU", 0);
		titlemap.put("??????SKU", 1);
		titlemap.put("????????????", 2);
		titlemap.put("?????????", 3);
		titlemap.put("??????", 4);
		titlemap.put("????????????", 5);
		titlemap.put("?????????", 6);
		titlemap.put("????????????", 7);
		titlemap.put("???????????????", 8);

		titlechange.put("sku", "SKU");
		titlechange.put("psku", "??????SKU");
		titlechange.put("name", "????????????");
		titlechange.put("dimension", "?????????");
		titlechange.put("weight", "??????");
		titlechange.put("price", "????????????");
		titlechange.put("supplier", "?????????");
		titlechange.put("effectivedate", "????????????");
		titlechange.put("owner", "???????????????");
		for (int i = 0; i < warehouseList_FBA.size(); i++) {
			String name = warehouseList_FBA.get(i).get("name").toString();
			String id = warehouseList_FBA.get(i).get("id").toString();
			titlemap.put(name, 9  + i);
			titlechange.put(id, name);
		}
		for (int i = 0; i < warehouseList.size(); i++) {
			String name = warehouseList.get(i).get("name").toString();
			String id = warehouseList.get(i).get("id").toString();
			titlemap.put(name, 9 + warehouseList_FBA.size()+ i);
			titlechange.put("self"+id, name);
		}
	

		// ?????????0???????????????????????????????????????
		Row row = sheet.createRow(0);
		for (String key : titlemap.keySet()) {
			Cell cell = row.createCell(titlemap.get(key)); // ?????????0????????????????????????(?????????)
			cell.setCellValue(key);
		}

		for (int i = 0; i < warehouseDetailList.size(); i++) {
			Row skurow = sheet.createRow(i + 1);
			Map<String, Object> skumap = warehouseDetailList.get(i);
			for (String key : titlechange.keySet()) {
				Cell cell = skurow.createCell(titlemap.get(titlechange.get(key)));
				if (key.equals("dimension")) {
					if (skumap.get("length") == null || skumap.get("width") == null || skumap.get("height") == null) {
						cell.setCellValue("-");
					} else {
						cell.setCellValue(skumap.get("length").toString() + "*" + skumap.get("width").toString() + "*" + skumap.get("height").toString());
					}
				} else {
					if (skumap.get(key) == null) {
						cell.setCellValue("-");
					} else {
						cell.setCellValue(skumap.get(key).toString());
					}
				}
			}
		}
	}

	public List<Map<String, Object>> selectInventoryCostAll(String warehouseid, String sku, String shopid, String byday) {
		if (GeneralUtil.isEmpty(sku)) {
			sku = null;
		} else {
			sku = "%" + sku + "%";
		}
		List<Map<String, Object>> pagelist = null;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date mbyday = fmt.parse(byday);
			Date today = GeneralUtil.getDateNoTime(new Date());
			if (byday.equals(fmt.format(today)) || mbyday.after(today)) {
				pagelist = inventoryMapper.findInventoryNowCost(warehouseid, sku, shopid);
			} else {
				pagelist = inventoryMapper.findInventoryCost(warehouseid, sku, shopid, byday);
			}
		} catch (ParseException e) {
			throw new BizException("????????????????????????");
		}
		return pagelist;
	}

 

	public List<String> getInvDayField(Map<String, Date> parameter) {
		List<String> list = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		Date endDate = parameter.get("endDate");
		Date beginDate = parameter.get("beginDate");
		calendar.setTime(beginDate);
		Date end = endDate;
		for (Date step = calendar.getTime(); step.before(end) || step.equals(end); calendar.add(Calendar.DATE, 1), step = calendar.getTime()) {
			String field = GeneralUtil.formatDate(step, GeneralUtil.FMT_YMD);
			list.add(field);
		}
		return list;
	}

	public List<Map<String, String>> getInvDaySumField(Map<String, Date> parameter) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Calendar calendar = Calendar.getInstance();
		Date endDate = parameter.get("endDate");
		Date beginDate = parameter.get("beginDate");
		calendar.setTime(beginDate);
		Date end = endDate;
		for (Date step = calendar.getTime(); step.before(end) || step.equals(end); calendar.add(Calendar.DATE, 1), step = calendar.getTime()) {
			String field = GeneralUtil.formatDate(step, GeneralUtil.FMT_YMD);
			Map<String, String> map = new HashMap<String, String>();
			map.put("byday", field);
			map.put("field", "v" + field);
			list.add(map);
		}
		return list;
	}

 


	public List<Map<String, Object>> findFulByMaterial(String materialid) {
		return inventoryMapper.findFulByMaterial(materialid);
	}

	public List<Map<String, Object>> getInventorydetail(String materialid, String warehouseid) {
		return inventoryMapper.getInventorydetail(materialid,warehouseid);
	}

 
	public Inventory selectAllInvSubWarehouse(String warehouseid, String materialid, String shopid,Status fulfillable) {
		// TODO Auto-generated method stub
		return inventoryMapper.selectAllInvSubWarehouse(warehouseid, materialid, shopid, fulfillable.getValue());
	}

}
