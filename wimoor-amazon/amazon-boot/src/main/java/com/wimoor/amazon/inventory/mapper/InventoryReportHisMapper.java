package com.wimoor.amazon.inventory.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.inventory.pojo.entity.InventoryReportHis;
 
@Mapper
public interface InventoryReportHisMapper extends BaseMapper<InventoryReportHis> {
	List<InventoryReportHis> selectBySkuMarketplace(Map<String,Object> params);

	IPage<Map<String, Object>> getFBAInvDayDetail(Page<?> page,@Param("param")Map<String, Object> parameter);
	List<Map<String, Object>> getFBAInvDayDetail(@Param("param")Map<String, Object> parameter);
	Map<String, Object> getFBAInvDayTotal(Map<String, Object> parameter);
}