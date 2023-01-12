package com.wimoor.amazon.inventory.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inventory.pojo.entity.InventoryReportHis;
 
@Mapper
public interface InventoryReportHisMapper extends BaseMapper<InventoryReportHis> {
	List<InventoryReportHis> selectBySkuMarketplace(Map<String,Object> params);
}