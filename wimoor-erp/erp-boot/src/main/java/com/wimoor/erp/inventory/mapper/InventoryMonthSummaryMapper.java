package com.wimoor.erp.inventory.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.inventory.pojo.entity.InventoryMonthSummary;
@Mapper
public interface InventoryMonthSummaryMapper  extends BaseMapper<InventoryMonthSummary> {

	Map<String, Object> getpurchase(Map<String, Object> param);

	Map<String, Object> getOtherOut(Map<String, Object> param);

	Map<String, Object> getOtherIn(Map<String, Object> param);

	Map<String, Object> getShip(Map<String, Object> param);
	
	Map<String, Object> getDispathOut(Map<String,Object> param);
	
	Map<String, Object> getDispathIn(Map<String,Object> param);
	
	Map<String, Object> getAssemblyOut(Map<String,Object> param);
	
	Map<String, Object> getAssemblyIn(Map<String,Object> param);

	List<Map<String, Object>>  findHaveInv(Map<String, Object> param);
	
	Map<String, Object> getStock(Map<String,Object> param);

	Map<String, Object> getChangeOut(Map<String, Object> param);

	Map<String, Object> getChangeIn(Map<String, Object> param);

	IPage<Map<String, Object>> findReport(Page<?>  page,@Param("param")Map<String, Object> param);
	
	List<Map<String, Object>> findReport(@Param("param")Map<String, Object> param);
	
}