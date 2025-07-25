package com.wimoor.erp.warehouse.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.warehouse.pojo.entity.WhseUnsalableReport;

public interface IWhseReportService extends IService<WhseUnsalableReport> {
	public IPage<Map<String, Object>> findUnsalableReportByCondition(Page<?> page,Map<String, Object> param);

	public void generateReprot();

	public void findLocalInvDead(SXSSFWorkbook workbook, Map<String, Object> params);

	public List<Map<String, Object>> findUnsalableReportByDay(Map<String, Object> param);

	public void setUnsalableReportByDayExcel(SXSSFWorkbook workbook, List<Map<String, Object>> list);
}
