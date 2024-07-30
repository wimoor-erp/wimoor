package com.wimoor.erp.inventory.mapper;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.inventory.pojo.entity.InventoryRecord;
@Mapper
public interface InventoryRecordMapper  extends BaseMapper<InventoryRecord> {
  	//根据信息确认当前SKU的record对象
	IPage<Map<String, Object>> findByCondition(Page<Object> page,@Param("param") Map<String, Object> maps);
	
	List<Map<String, Object>> findByCondition(@Param("param") Map<String, Object> maps);

	List<Map<String, Object>> findSkuInvHistory(@Param("param")Map<String, Object> param);
	
	List<Map<String, Object>>  findOutstockformOut(Map<String, Object> param);
	
	List<Map<String, Object>> findSkuNowInventory(@Param("shopid")String shopid,@Param("materialid")String materialid,
			@Param("warehouseid")String warehouseid);
}