package com.wimoor.amazon.report.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.inventory.pojo.vo.ProductInventoryVo;
import com.wimoor.amazon.report.pojo.entity.InventoryReport;
@Mapper
public interface InventoryReportMapper extends BaseMapper<InventoryReport> {
	int newestInsert(InventoryReport record);

	void newestInsertArchive(String amazonAuthId, String marketplaceid, String byday);

	List<Map<String,Object>> FbaOutInventoryByRangeBySku(Map<String,Object> map);
	
	List<Map<String,Object>> FbaInventoryBydayBySku(Map<String,Object> map);
	
	List<Map<String,Object>> FbaInventoryBydayInsku(Map<String,Object> map);
	
	List<Map<String,Object>> FbaOutInventoryByRange(Map<String,Object> map);
	
	List<Map<String,Object>> FbaInventoryByday(Map<String,Object> map);
	
	InventoryReport findbyProductId(String id);

	Date findMinbyProduct(Map<String, Object> map);
	
	IPage<Map<String,Object>> findInventoryCost(Page<Map<String, Object>> page,@Param("groupid")String groupid,@Param("marketplaceid")String warehouseid,@Param("sku")String sku,@Param("shopid")String shopid,@Param("byday")String byday);
	
	Map<String,Object> findInventoryCostTotal(@Param("groupid")String groupid,@Param("marketplaceid")String warehouseid,@Param("sku")String sku,@Param("shopid")String shopid,@Param("byday")String byday);

	List<Map<String, Object>> findInventoryCost(@Param("groupid")String groupid,@Param("marketplaceid")String marketplaceid,@Param("sku")String sku,@Param("shopid")String shopid,@Param("byday")String byday);

	Map<String, Object> getFbaSnapDate(Map<String,Object> map);
	
	List<Map<String, Object>> findFBAInvCostByShopId(String shopid);

	List<Map<String, Object>> getInvDayDetail(Map<String, Object> parameter);

	IPage<Map<String, Object>> getFBAInvDayDetail(Page<?> page,Map<String, Object> parameter);

	Map<String, Object> getFBAInvDayTotal(Map<String, Object> parameter);

	List<Map<String, Object>> getUnionFinAndInvCost(@Param("shopid")String shopid, @Param("beginDate")String beginDate, @Param("endDate")String endDate);

	List<Map<String, Object>> getDiffrentReservedData();
	
	List<Map<String, Object>> getDiffrentInboundData();
 
	List<ProductInventoryVo> findFBA(@Param("groupid")String groupid,
									 @Param("marketplaceid")String marketplaceid,
									 @Param("sku")String sku,
									 @Param("myself")String myself,
									 @Param("shopid")String shopid);
 
}
