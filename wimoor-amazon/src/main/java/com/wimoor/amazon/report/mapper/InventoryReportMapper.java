package com.wimoor.amazon.report.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.report.pojo.entity.InventoryReport;
@Mapper
public interface InventoryReportMapper extends BaseMapper<InventoryReport> {
	int newestInsert(InventoryReport record);
}
