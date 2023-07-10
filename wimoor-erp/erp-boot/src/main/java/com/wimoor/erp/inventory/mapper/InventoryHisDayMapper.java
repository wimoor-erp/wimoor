package com.wimoor.erp.inventory.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.inventory.pojo.entity.InventoryHisDay;
@Mapper
public interface InventoryHisDayMapper extends BaseMapper<InventoryHisDay> {
 
	List<Map<String,Object>> findInvDayList(Map<String,Object> param);

	List<Map<String, Object>> getInvDayDetail(@Param("param")Map<String, Object> parameter);
	
	IPage<Map<String, Object>> getInvDayDetail(Page<?> page,@Param("param")Map<String, Object> parameter);
	
	Map<String, Object> getInvDayTotal(Map<String, Object> parameter);
	
	void summaryInvEveryDay(@Param("byday") String byday,@Param("endday") String endday);
	
	
}