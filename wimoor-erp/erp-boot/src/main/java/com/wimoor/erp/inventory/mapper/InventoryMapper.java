package com.wimoor.erp.inventory.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.inventory.pojo.entity.Inventory;
import com.wimoor.erp.inventory.pojo.vo.MaterialInventoryVo;
import com.wimoor.erp.material.pojo.entity.Material;
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
	Inventory selectNowInv(@Param("warehouse")String warehouse,@Param("material")String material,@Param("shopid")String shopid,@Param("status")String status);
	Inventory selectAllInvSubWarehouse(@Param("warehouse")String warehouse,@Param("material")String material,@Param("shopid")String shopid,@Param("status")String status);
	/**
	 * @param shopid
	 * @param byday
	 * @param warehouseid
	 * @param materialid
	 * @param parentid
	 * @return
	 */
	List<Map<String, Object>> localInventoryByDay(Map<String,Object> map);
	
	/**
	 * 
	 * @param shopid
	 * @param fromDate
	 * @param toDate
	 * @param warehouseid
	 * @param materialid
	 * @param parentid
	 * @return
	 */
	List<Map<String, Object>> localOutInventoryByRange(Map<String,Object> map);

	List<Map<String, Object>> findLocalInventory(@Param("param")Map<String,Object> param);
	
	IPage<Map<String, Object>> findLocalInventory(Page<?>  page,@Param("param")Map<String, Object> param);
	
	Map<String, Object> findLocalInventorySum(@Param("param")Map<String,Object> param);
	
	IPage<Map<String, Object>> findNotFBAWithStockCycle(Page<?>  page,@Param("id")String id, @Param("shopid")String shopid);
	
	List<Map<String, Object>> findNotFBAWithStockCycle(@Param("id")String id,@Param("ftype")String ftype, @Param("shopid")String shopid);
	
	IPage<Map<String, Object>> findInventoryDetail(Page<?>  page,Map<String,Object> map);
	List<Map<String, Object>> findInventoryDetail(Map<String,Object> map);
	
	Map<String, Object> findInvDetailById(@Param("materialid")String materialid, @Param("warehouseid") String warehouseid, @Param("shopid")String shopid);
	Map<String,Object> getInventory(@Param("materialid")String materialid, @Param("warehouseid") String warehouseid, @Param("shopid")String shopid);
	Map<String, Object> findInvByWarehouseId(@Param("materialid")String materialid, @Param("warehouseid") String warehouseid, @Param("shopid")String shopid);

	Map<String,Object> selectSubASList(@Param("warehouseid")String warehouseid,@Param("materialid")String materialid,@Param("shopid")String shopid);

	List<Map<String, Object>> findInboundDetail(@Param("materialid")String materialid, @Param("warehouseid") String warehouseid, @Param("shopid")String shopid);
	List<Map<String, Object>> findOutboundDetail(@Param("materialid")String materialid, @Param("warehouseid") String warehouseid, @Param("shopid")String shopid);
	IPage<Map<String,Object>> selectInventoryDetail(Page<?>  page,Map<String, Object> param);

	List<Map<String, Object>> getSelfInvDetail(@Param("warehouseid") String warehouseid,@Param("stocktakingid") String stocktakingid);
	Map<String, Object> getSelfInvBySKU(@Param("warehouseid") String warehouseid, @Param("materialid") String materialid);
	List<Map<String,Object>>  getInvChangeRate(Map<String, Object> param);
	List<Map<String,Object>> findNotFBABySku(@Param("whparentid")String warehouseid,@Param("skuid") String skuid,@Param("shopid")String shopid);
	
	IPage<Map<String,Object>> findInventoryCost(Page<?>  page,@Param("warehouseid")String warehouseid,@Param("sku")String sku,@Param("shopid")String shopid,@Param("byday")String byday);
	List<Map<String, Object>> findInventoryCost(@Param("warehouseid")String warehouseid,@Param("sku")String sku,@Param("shopid")String shopid,@Param("byday")String byday);
	Map<String,Object> findInventoryCostTotal(@Param("warehouseid")String warehouseid,@Param("sku")String sku,@Param("shopid")String shopid,@Param("byday")String byday);
	
	IPage<Map<String,Object>> findInventoryNowCost(Page<?>  page,@Param("warehouseid")String warehouseid,@Param("sku")String sku,@Param("shopid")String shopid);
	List<Map<String, Object>> findInventoryNowCost(@Param("warehouseid")String warehouseid,@Param("sku")String sku,@Param("shopid")String shopid);
	Map<String,Object> findInventoryNowCostTotal(@Param("warehouseid")String warehouseid,@Param("sku")String sku,@Param("shopid")String shopid);
	
	List<Map<String, Object>> findInventoryNowCostByShopId(@Param("shopid") String shopid);
	List<Map<String, Object>> findFulByMaterial(String id);

	List<MaterialInventoryVo> findLocalWarehouseInventory(@Param("shopid") String shopid,@Param("materialid") String materialid);
	Material findOverseaById(@Param("sku")String sku,@Param("shopid") String shopid, @Param("groupid")String groupid, @Param("country") String country);
}