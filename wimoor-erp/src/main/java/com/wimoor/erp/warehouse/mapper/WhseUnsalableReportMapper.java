package com.wimoor.erp.warehouse.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.warehouse.pojo.entity.WhseUnsalableReport;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface WhseUnsalableReportMapper extends BaseMapper<WhseUnsalableReport> {
	IPage<Map<String,Object>> findByCondition(Page<?> page,@Param("param")Map<String,Object> param);
	List<LinkedHashMap<String, Object>> getLocalDeadRpt(Map<String,Object> param);
	Map<String,Object> findPageTotalSum(Map<String,Object> param);
	List<Map<String,Object>> findByDay(Map<String,Object> param);
	List<Map<String, Object>> selectLocalInventory(Map<String,Object> param);
	List<Map<String, Object>> selectLocalInventoryInwh(Map<String,Object> param);
	Map<String, Object> localByDay(Map<String,Object> param);
	Map<String, Object> localOutInventoryByRange(Map<String,Object> param);
	List<Map<String, Object>> findAllShop();
	List<Map<String, Object>> findAllGroupByShop(@Param("shopid")String shopid);

	void deleteAllData();

}