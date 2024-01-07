package com.wimoor.erp.inventory.service;

import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.inventory.pojo.entity.InventoryMonthSummary;

public interface IInventoryMonthSummaryService extends IService<InventoryMonthSummary>  {
	public void generateReprot();

	public IPage<Map<String, Object>> findReport(Page<?> page,Map<String, Object> param);

	public void setExcelfindReport(SXSSFWorkbook workbook, Map<String, Object> param);
}
