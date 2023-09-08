package com.wimoor.erp.inventory.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
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
import com.wimoor.erp.inventory.pojo.vo.MaterialInventoryVo;
import com.wimoor.erp.inventory.service.IInventoryHisService;
import com.wimoor.erp.inventory.service.IInventoryRecordService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.stock.pojo.entity.StockTaking;
import com.wimoor.erp.stock.service.IStockTakingService;
import com.wimoor.erp.util.LockCheckUtils;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import lombok.RequiredArgsConstructor;
 

//仓库(只管能不能save和insert，不管数据)
@Service("inventoryService")
@RequiredArgsConstructor
public class InventoryServiceImpl  extends ServiceImpl<InventoryMapper,Inventory> implements IInventoryService {
	 
	@Lazy
	@Autowired
	IMaterialService materialService;
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	final InventoryRecordMapper inventoryRecordMapper;
	 
	final IWarehouseService warehouseService;
	 
	@Lazy
	@Autowired
	IStockTakingService stockTakingService;
	 
	final IInventoryHisService inventoryHisService;
	 
	final IInventoryRecordService inventoryRecordService;
	
	final FileUpload fileUpload;  

	// 设置初始化信息
	private void setInitRecord(InventoryParameter para,InventoryRecord newRecord, Status status, Operate operate) {
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

	// 判断是否可以操作库存：1.是否正在盘点；2.是否被删除
	public Warehouse isOkForOperate(String warehouseid, EnumByInventory enumByInventory) throws BizException {
		Warehouse warehouse = warehouseService.getById(warehouseid);
		if (warehouse == null) {
			throw new BizException("抱歉，没找到您选择的仓库！");
		}
		if (warehouse.getDisabled()) {
			throw new BizException("抱歉，该仓库已经不可用！");
		}
		if (warehouse.getIsstocktaking() && !EnumByInventory.isstocktaking.equals(enumByInventory)) {
			throw new BizException("抱歉，该仓库正在盘点，暂不支持库存操作！");
		}
		return warehouse;
	}
	
	public void handleStatusRecord(InventoryParameter para,InventoryRecord recform,String ftype) {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("skuid", para.getMaterial());
		param.put("shopid", para.getShopid());
		param.put("warehouseid", para.getWarehouse());
		Map<String, Object> invMap = this.baseMapper.findLocalInventorySum(param);
		if(invMap!=null) {
			if(invMap.get("inbound")!=null) {
				int inbound = Integer.parseInt(invMap.get("inbound").toString());
				if(ftype.equals("start")) {
					recform.setStartinbound(inbound);
				}else {
					recform.setEndinbound(inbound);
				}
			}else {
				if(ftype.equals("start")) {
					recform.setStartinbound(0);
				}else {
					recform.setEndinbound(0);
				}
			}
			if(invMap.get("outbound")!=null) {
				int outbound = Integer.parseInt(invMap.get("outbound").toString());
				if(ftype.equals("start")) {
					recform.setStartoutbound(outbound);
				}else {
					recform.setEndoutbound(outbound);
				}
			}else {
				if(ftype.equals("start")) {
					recform.setStartoutbound(0);
				}else {
					recform.setEndoutbound(0);
				}
			}
			if(invMap.get("fulfillable")!=null) {
				int fulfillable = Integer.parseInt(invMap.get("fulfillable").toString());
				if(ftype.equals("start")) {
					recform.setStartfulfillable(fulfillable);
				}else {
					recform.setEndfulfillable(fulfillable);
				}
			}else {
				if(ftype.equals("start")) {
					recform.setStartfulfillable(0);
				}else {
					recform.setEndfulfillable(0);
				}
			}
		}
	}

	// 根据状态去入库
	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
	public int AddStockByStatus(InventoryParameter para, Status status, Operate operate) throws BizException {
		int count = 0;
		LockCheckUtils lock = new LockCheckUtils(stringRedisTemplate,"StockInventory"+para.getWarehouse()+para.getMaterial());
		try {
			para=para.clone();
			if(para.getAmount()==null)return 0;
			int quantity = para.getAmount();
			if (quantity == 0)return 0;
			//库存初始的量
			InventoryRecord recform=new InventoryRecord();
			handleStatusRecord(para,recform,"start");
			Warehouse warehouseobj = isOkForOperate(para.getWarehouse(), para.getStatus());
			Inventory addinv = this.baseMapper.selectNowInv(para.getWarehouse(), para.getMaterial(), para.getShopid(), para.getInvStatus(status));
			if (addinv != null) {
				Integer oldQuantity = addinv.getQuantity();
				oldQuantity = oldQuantity + quantity;
				setInitOldInventory(para, addinv);
				if(!(warehouseobj.getIshungry()&& status.value.equals(Status.fulfillable.value))) {
					if (oldQuantity == null || oldQuantity < 0) {
						String sku = para.getMaterial();
						String warehouse = para.getWarehouse();
						if (!GeneralUtil.isEmpty(sku) && !GeneralUtil.isEmpty(warehouse)) {
							throw new BizException(sku + "在" + warehouse + "数据异常,库存不能小于0！");
						} else {
							throw new BizException("数据异常,库存不能小于0！");
						}
					}
			    }
				addinv.setQuantity(oldQuantity);
				addinv.setOperator(para.getOperator());
				//库存变动的量
				handleInvRecordFormAmount(status,quantity,recform,"add");
				count += this.baseMapper.updateById(addinv);
				//库存变动之后的量
				handleStatusRecord(para,recform,"end");
			} else {
				addinv = setInitInventory(para);
				if(!(warehouseobj.getIshungry()&& status.value.equals(Status.fulfillable.value))) {
					if (quantity < 0) {
						throw new BizException("数据异常,库存不能小于0！");
					}
				}
				addinv.setQuantity(quantity);
				addinv.setStatus(para.getInvStatus(status));
				addinv.setOperator(para.getOperator());
				//库存变动的量
				handleInvRecordFormAmount(status,quantity,recform,"add");
				count += this.baseMapper.insert(addinv);
				//库存变动之后的量
				handleStatusRecord(para,recform,"end");
			}
			para.setInvqty(addinv.getQuantity());
			setInitRecord(para,recform ,status, operate);
			inventoryRecordService.save(recform);
			insertHis(addinv);
		}finally {
			lock.clear();
		}
		return count;
	}

	// 根据状态去出库
	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
	public int SubStockByStatus(InventoryParameter para, Status status, Operate operate) throws BizException {
		int count = 0;
		LockCheckUtils lock = new LockCheckUtils(stringRedisTemplate,"StockInventory"+para.getWarehouse()+para.getMaterial());
		try {
				para=para.clone();
				int quantity = para.getAmount();
				if (quantity == 0)return 0;
				//库存初始的量
				InventoryRecord recform=new InventoryRecord();
				handleStatusRecord(para,recform,"start");
				Warehouse warehouseobj = isOkForOperate(para.getWarehouse(), para.getStatus());
				Inventory subinv = this.baseMapper.selectNowInv(para.getWarehouse(), para.getMaterial(), para.getShopid(), para.getInvStatus(status));
				if (subinv != null) {
					Integer oldQuantity = subinv.getQuantity();
					oldQuantity = oldQuantity - quantity;
					if(!(warehouseobj.getIshungry()&& status.value.equals(Status.fulfillable.value))) {
						if (oldQuantity == null || oldQuantity < 0) {
							if (para != null && para.getMaterial() != null) {
								Material material = materialService.getById(para.getMaterial());
								if (material != null) {
									throw new BizException("SKU【" + material.getSku() + "】 库存不够！");
								} else {
									throw new BizException("库存不够！");
								}
							} else {
								throw new BizException("库存不够！");
							}
						}
					}
					subinv.setQuantity(oldQuantity);
					subinv.setOpttime(new Date());
					subinv.setOperator(para.getOperator());
					//库存变动的量
					handleInvRecordFormAmount(status,quantity,recform,"sub");
					if(updateById(subinv)) {
						count++ ;
						//库存变动之后的量
						handleStatusRecord(para,recform,"end");
					}
				}else if(warehouseobj.getIshungry()&& status.value.equals(Status.fulfillable.value)) {
					subinv = setInitInventory(para);
					subinv.setStatus(para.getInvStatus(status));
					Integer oldQuantity = subinv.getQuantity();
					oldQuantity = oldQuantity - quantity;
					subinv.setQuantity(oldQuantity);
					subinv.setOperator(para.getOperator());
					//库存变动的量
					handleInvRecordFormAmount(status,quantity,recform,"sub");
					count += this.baseMapper.insert(subinv);
					//库存变动之后的量
					handleStatusRecord(para,recform,"end");
				} else {
					if (para != null && para.getMaterial() != null) {
						Material material = materialService.getById(para.getMaterial());
						if (material != null) {
							throw new BizException("SKU【" + material.getSku() + "】 库存不够！");
						} else {
							throw new BizException("库存不够！");
						}
					} else {
						throw new BizException("库存不够！");
					}
				}
				para.setInvqty(subinv.getQuantity());
				setInitRecord(para,recform ,status, operate);
				inventoryRecordService.save(recform);
				insertHis(subinv);
		}finally {
			lock.clear();
		}
		return count;
	}

	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
	public int UndoSubStockByStatus(InventoryParameter para, Status status, Operate operate) throws BizException {
		int count = 0;
		para=para.clone();
		LockCheckUtils lock = new LockCheckUtils(stringRedisTemplate,"StockInventory"+para.getWarehouse()+para.getMaterial());
		try {
				if(para.getAmount()==null)return 0;
				int quantity = para.getAmount();
				if (quantity == 0)return 0;
				//库存初始的量
				InventoryRecord recform=new InventoryRecord();
				handleStatusRecord(para,recform,"start");
				isOkForOperate(para.getWarehouse(), para.getStatus());
				Inventory addinv = this.baseMapper.selectNowInv(para.getWarehouse(), para.getMaterial(), para.getShopid(), para.getInvStatus(status));
				if (addinv != null) {
					Integer oldQuantity = addinv.getQuantity();
					oldQuantity = oldQuantity + quantity;
					setInitOldInventory(para, addinv);
					addinv.setQuantity(oldQuantity);
					addinv.setOperator(para.getOperator());
					//库存变动的量
					handleInvRecordFormAmount(status,quantity,recform,"add");
					count += this.baseMapper.updateById(addinv);
					//库存变动之后的量
					handleStatusRecord(para,recform,"end");
				} else {
					addinv = setInitInventory(para);
					addinv.setQuantity(quantity);
					addinv.setStatus(para.getInvStatus(status));
					addinv.setOperator(para.getOperator());
					//库存变动的量
					handleInvRecordFormAmount(status,quantity,recform,"add");
					count += this.baseMapper.insert(addinv);
					//库存变动之后的量
					handleStatusRecord(para,recform,"end");
				}
				para.setInvqty(addinv.getQuantity());
				setInitRecord(para,recform ,status, operate);
				inventoryRecordService.save(recform);
				insertHis(addinv);
		}finally {
			lock.clear();
		}
		return count;
	}
	private void handleInvRecordFormAmount(Status status,int quantity,InventoryRecord recform,String ftype) {
		if(status.value.equals(Status.fulfillable.value)) {
			if("add".equals(ftype)) {
				recform.setFulfillable(quantity);
			}else {
				recform.setFulfillable(quantity*-1);
			}
		}
		if(status.value.equals(Status.inbound.value)) {
			if("add".equals(ftype)) {
				recform.setInbound(quantity);
			}else {
				recform.setInbound(quantity*-1);
			}
		}
		if(status.value.equals(Status.outbound.value)) {
			if("add".equals(ftype)) {
				recform.setOutbound(quantity);
			}else {
				recform.setOutbound(quantity*-1);
			}
		}
	}

	// 初始化inventory对象
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

	// 获取oldinventory对象
	private void setInitOldInventory(InventoryParameter para, Inventory oldInv) {
		oldInv.setWarehouseid(para.getWarehouse());
		oldInv.setMaterialid(para.getMaterial());
		oldInv.setOperator(para.getOperator());
		oldInv.setOpttime(para.getOpttime());
		oldInv.setShopid(para.getShopid());
	}

	// 入库(直接入库)
	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
	public Integer inStockByDirect(InventoryParameter para) throws BizException {
		int result = 0;
		// 根据warehouseid和materialid可以确认当前SKU
		AddStockByStatus(para, Status.fulfillable, Operate.in);
		return result;
	}

	public Inventory selectNowInv(String warehouseid, String materialid, String shopid, Status status) {
		Inventory inv = this.baseMapper.selectNowInv(warehouseid, materialid, shopid, status.getValue());
		return inv;
	}

	// 待入库
	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
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

	// 出库(直接出库)
	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
	public Integer outStockByDirect(InventoryParameter para) throws BizException {
		return SubStockByStatus(para, Status.fulfillable, Operate.out);
	}

	// 待出库
	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
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

	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
	public Integer outStockByReadyChange(InventoryParameter para) throws BizException {
		int result = 0;
		if (para.getStatus() == EnumByInventory.Ready) {
			SubStockByStatus(para, Status.fulfillable, Operate.changeadd);
			result = AddStockByStatus(para, Status.outbound, Operate.changeadd);
		}
		return result;
	}

	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
	public Integer outStockReadyChange(InventoryParameter para) throws BizException {
		int result = 0;
		if (para.getStatus() == EnumByInventory.Ready) {
			SubStockByStatus(para, Status.outbound, Operate.changesub);
			result = AddStockByStatus(para, Status.fulfillable, Operate.changesub);
		}
		return result;
	}

	// 撤销直接入库的(说明之前一定有inventory对象)
	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
	public Integer inStockDirectCancel(InventoryParameter para) throws BizException {
		return SubStockByStatus(para, Status.fulfillable, Operate.cancel);
	}

	// 撤销准备入库的
	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
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

	// 撤销直接出库的
	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
	public Integer outStockDirectCancel(InventoryParameter para) throws BizException {
		return AddStockByStatus(para, Status.fulfillable, Operate.cancel);
	}

	// 撤销准备出库的
	@Transactional
	@CacheEvict(value = { "inventoryByMskuCache","materialListCache"}, allEntries = true)
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
			return this.baseMapper.updateById(entity)>0?true:false;
	}

	public IPage<Map<String, Object>> findByTypeWithStockCycle(Page<?> page,String ftype, String id, String shopid) {
		IPage<Map<String, Object>> list = null;
		list = this.baseMapper.findNotFBAWithStockCycle(page,id, shopid);
		return list;
	}

 
 
	public IPage<Map<String,Object>> findLocalInventory(Page<?> page,Map<String,Object> param) {
		return this.baseMapper.findLocalInventory(page,param);
	}
	public Map<String, Object> findSum(Map<String,Object> param) {
		return this.baseMapper.findLocalInventorySum(param);
	}

	public IPage<Map<String, Object>> findInventoryDetail(Page<?> page,Map<String, Object> warehouseMap) {
		return this.baseMapper.findInventoryDetail(page,warehouseMap);
	}

	public List<Map<String, Object>> findInventoryDetailForExport(Map<String, Object> warehouseMap) {
		return this.baseMapper.findInventoryDetail(warehouseMap);
	}

	public Map<String, Object> findInvDetailById(String materialid, String warehouseid, String shopid) {
		return this.baseMapper.findInvDetailById(materialid, warehouseid, shopid);
	}

	public List<Map<String, Object>> findInboundDetail(String materialid, String warehouseid, String shopid) {
		return this.baseMapper.findInboundDetail(materialid, warehouseid, shopid);
	}

	public List<Map<String, Object>> findOutboundDetail(String materialid, String warehouseid, String shopid) {
		return this.baseMapper.findOutboundDetail(materialid, warehouseid, shopid);
	}

	public IPage<Map<String, Object>> selectInventoryDetail(Page<?> page,Map<String, Object> param) {
		IPage<Map<String, Object>> list = this.baseMapper.selectInventoryDetail(page,param);
		return list;
	}

	public List<Map<String, Object>> getSelfInvDetail(String warehouseid, String stocktakingid) {
		return this.baseMapper.getSelfInvDetail(warehouseid, stocktakingid);
	}

	public Map<String, Object> getSelfInvBySKU(String warehouseid, String materialid) {
		Map<String, Object> maps = this.baseMapper.getSelfInvBySKU(warehouseid,materialid);
		if (maps != null) {
			String image = null;
			if (maps.get("image") != null)
				image = maps.get("image").toString();
			if (maps.get("image") != null)
				maps.put("image", fileUpload.getPictureImage(image));
			else
				maps.put("image", "images/systempicture/noimage40.png");
		}
		return maps;
	}

	public List<Map<String, Object>> findInvChgRateReport(Map<String, Object> param) {
		return this.baseMapper.getInvChangeRate(param);
	}

	public List<Map<String, Object>> localInventoryByDay(Map<String, Object> param) {
		return this.baseMapper.localInventoryByDay(param);
	}

	public List<Map<String, Object>> localOutInventoryByRange(Map<String, Object> param) {
		return this.baseMapper.localOutInventoryByRange(param);
	}

	public List<Map<String, Object>> findNotFbaBySku(String warehouseid, String skuid, String shopid) {
		return this.baseMapper.findNotFBABySku(warehouseid, skuid, shopid);
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
				pagelist = this.baseMapper.findInventoryNowCost(page,warehouseid, sku, shopid);
				map = this.baseMapper.findInventoryNowCostTotal(warehouseid, sku, shopid);
			} else {
				pagelist = this.baseMapper.findInventoryCost(page,warehouseid, sku, shopid, byday);
				map = this.baseMapper.findInventoryCostTotal(warehouseid, sku, shopid, byday);
			}
		} catch (ParseException e) {
			throw new BizException("库存日期解析失败");
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
			pagelist.getRecords().get(0).put("summary", map);
		}
		return pagelist;
	}
	public Map<String, Object> findInvByMaterialId(String materialid,  String shopid) {
		return this.baseMapper.findInvByWarehouseId(materialid, null, shopid);
	}
	public Map<String, Object> findInvByWarehouseId(String materialid, String warehouseid, String shopid) {
		String[] warehouseidArray =null;
		if(warehouseid!=null) {
			warehouseidArray=warehouseid.split(",");
		}
		if (warehouseidArray==null||warehouseidArray.length == 1) {
			return this.baseMapper.findInvByWarehouseId(materialid, warehouseid, shopid);
		} else {
			Map<String, Object> maps = new HashMap<String, Object>();
			Integer inbound = 0;
			Integer outbound = 0;
			Integer fulfillable = 0;
			for (int i = 0; i < warehouseidArray.length; i++) {
				String wareid = warehouseidArray[i];
				Map<String, Object> result = this.baseMapper.findInvByWarehouseId(materialid, wareid, shopid);
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

	public void getExcelBookInventoryReport(SXSSFWorkbook workbook, List<Map<String, Object>> inventoryList) {
		Sheet sheet = workbook.createSheet("sheet1");
		List<String> titlelist = new ArrayList<String>();
		Map<String, String> titlechange = new HashMap<String, String>();
		titlelist.add("SKU");
		titlelist.add("商品名称");
		titlelist.add("本地仓库");
		titlelist.add("待入库");
		titlelist.add("可用");
		titlelist.add("待出库");

		titlechange.put("SKU", "sku");
		titlechange.put("商品名称", "name");
		titlechange.put("本地仓库", "warehouse");
		titlechange.put("待入库", "inbound");
		titlechange.put("可用", "fulfillable");
		titlechange.put("待出库", "outbound");

		// 在索引0的位置创建行（最顶端的行）
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

	public void setExcelBookInventoryReport(SXSSFWorkbook workbook,List<Map<String, Object>> warehouseList, List<Map<String, Object>> warehouseDetailList) {
		Sheet sheet = workbook.createSheet("sheet1");
		Map<String, Integer> titlemap = new HashMap<String, Integer>();
		Map<String, String> titlechange = new HashMap<String, String>();
		titlechange.put("sku", "SKU");
		titlechange.put("psku", "平台SKU");
		titlechange.put("name", "产品名称");
		titlechange.put("dimension", "长宽高");
		titlechange.put("weight", "重量");
		titlechange.put("price", "采购单价");
		titlechange.put("supplier", "供应商");
		titlechange.put("effectivedate", "生效日期");
		titlechange.put("owner", "产品负责人");
		for (int i = 0; i < warehouseList.size(); i++) {
			String name = warehouseList.get(i).get("name").toString();
			String id = warehouseList.get(i).get("id").toString();
			titlechange.put("self"+id, name);
		}
		// 在索引0的位置创建行（最顶端的行）
		Row row = sheet.createRow(0);
		for (String key : titlemap.keySet()) {
			Cell cell = row.createCell(titlemap.get(key)); // 在索引0的位置创建单元格(左上端)
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
				pagelist = this.baseMapper.findInventoryNowCost(warehouseid, sku, shopid);
			} else {
				pagelist = this.baseMapper.findInventoryCost(warehouseid, sku, shopid, byday);
			}
		} catch (ParseException e) {
			throw new BizException("库存日期解析失败");
		}
		return pagelist;
	}

 

	public List<Map<String, Object>> findFulByMaterial(String materialid) {
		return this.baseMapper.findFulByMaterial(materialid);
	}

 
	public Inventory selectAllInvSubWarehouse(String warehouseid, String materialid, String shopid,Status fulfillable) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectAllInvSubWarehouse(warehouseid, materialid, shopid, fulfillable.getValue());
	}

	@Override
	public List<Map<String, Object>> findByTypeWithStockCycle(String id,String ftype, String shopid) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = null;
        list = this.baseMapper.findNotFBAWithStockCycle(id, ftype,shopid);
		return list;
	}
	

	
	public List<MaterialInventoryVo> findLocalWarehouseInventory(String shopid,String materialid) {
		return this.baseMapper.findLocalWarehouseInventory(shopid, materialid);
	}

	@Override
	public Material findOverseaById(String materialid, String shopid, String groupid, String country) {
		// TODO Auto-generated method stub
		return this.baseMapper.findOverseaById(materialid,shopid,groupid,country);
	}

	@Override
	public Map<String, Object> getInventory( String materialid, String warehouseid,String shopid) {
		// TODO Auto-generated method stub
		return this.baseMapper.getInventory(materialid, warehouseid, shopid);
	}

	@Override
	public List<Map<String, Object>> findLocalInventory(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return  this.baseMapper.findLocalInventory(param);
	}

	@Override
	public List<Map<String, Object>> findInventoryNowCostByShopId(String shopid) {
		// TODO Auto-generated method stub
		return this.baseMapper.findInventoryNowCostByShopId(shopid);
	}
 
}
