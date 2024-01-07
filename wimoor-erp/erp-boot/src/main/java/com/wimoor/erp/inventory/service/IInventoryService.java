package com.wimoor.erp.inventory.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.Operate;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.inventory.pojo.entity.Inventory;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.pojo.vo.MaterialInventoryVo;
import com.wimoor.erp.material.pojo.entity.Material;

public interface IInventoryService extends IService<Inventory> {

	List<Map<String, Object>> findByTypeWithStockCycle(String id,String ftype, String shopid );

	// 出库入库
	Integer outStockByDirect(InventoryParameter para) throws BizException;

	Integer inStockByDirect(InventoryParameter para) throws BizException;

	Integer inStockByReady(InventoryParameter para) throws BizException;

	Integer outStockByReady(InventoryParameter para) throws BizException;
	
	Integer outStockByReadyChange(InventoryParameter para) throws BizException;
	
	Integer outStockReadyChange(InventoryParameter para) throws BizException;

	// 撤销直接入库
	Integer inStockDirectCancel(InventoryParameter para) throws BizException;

	// 撤销带准备状态的入库
	Integer inStockReadyCancel(InventoryParameter para) throws BizException;

	// 撤销直接出库
	Integer outStockDirectCancel(InventoryParameter para) throws BizException;

	// 撤销带准备状态的出库
	Integer outStockReadyCancel(InventoryParameter para) throws BizException;

 
	IPage<Map<String, Object>> findInventoryDetail(Page<?> page,Map<String, Object> warehouseMap);
	
	List<Map<String, Object>> findInventoryDetailForExport(Map<String, Object> warehouseMap );

	Map<String, Object> findInvDetailById(String materialid, String warehouseid, String shopid);

	int SubStockByStatus(InventoryParameter para, Status status, Operate operate) throws BizException;

	int AddStockByStatus(InventoryParameter para, Status status, Operate operate) throws BizException;

	Inventory selectNowInv(String warehouseid, String materialid, String shopid, Status status);

	List<Map<String, Object>> findInboundDetail(String materialid, String warehouseid, String shopid);

	List<Map<String, Object>> findOutboundDetail(String materialid, String warehouseid, String shopid);

	IPage<Map<String, Object>> selectInventoryDetail(Page<?> page,Map<String, Object> param);
	
	List<Map<String, Object>> getSelfInvDetail(String warehouseid, String stocktakingid);

	Map<String, Object> getSelfInvBySKU(String warehouseid, String sku);
	
	public List<Map<String, Object>> findInvChgRateReport(Map<String, Object> param) ;
	
	 List<Map<String,Object>> findNotFbaBySku(String warehouseid,String skuid,String shopid);

	//库存货值
	IPage<Map<String,Object>> selectInventoryCost(Page<?> page,String warehouseid,String sku,String shopid,String byday);

	Map<String, Object> findInvByWarehouseId(String materialid, String warehouseid, String shopid);

 
	public List<Map<String, Object>> localInventoryByDay(Map<String, Object> param);

	public List<Map<String, Object>> localOutInventoryByRange(Map<String, Object> param);

	void setExcelBookInventoryReport(SXSSFWorkbook workbook,  List<Map<String,Object>> warehouseList, List<Map<String, Object>> warehouseDetailList);

	Map<String, Object> findSum(Map<String,Object> param);

	List<Map<String, Object>> selectInventoryCostAll(String warehouseid, String sku, String shopid,String byday);



	List<Map<String, Object>> findFulByMaterial(String materialid);

	public Map<String, Object> findInvByMaterialId(String materialid,  String shopid) ;

	Inventory selectAllInvSubWarehouse(String warehouseid, String materialid, String shopid, Status fulfillable);
	public IPage<Map<String,Object>> findLocalInventory(Page<?> page,Map<String,Object> param) ;
	public List<Map<String,Object>> findLocalInventory(Map<String,Object> param) ;
	public void getExcelBookInventoryReport(SXSSFWorkbook workbook, List<Map<String, Object>> inventoryList);
	public IPage<Map<String, Object>> findByTypeWithStockCycle(Page<?> page,String ftype, String id, String shopid) ;

	public List<MaterialInventoryVo> findLocalWarehouseInventory(String shopid,String materialid) ;
	public int UndoSubStockByStatus(InventoryParameter para, Status status, Operate operate) throws BizException ;

	Material findOverseaById(String id, String shopid, String groupid, String country);

	public Map<String, Object> getInventory( String materialid, String warehouseid,String shopid) ;

	List<Map<String,Object>> findInventoryNowCostByShopId(String shopid);
	public void repairInventory(UserInfo user,String warehouseid,String materialid,String status,Integer qty);
}
