package com.wimoor.erp.warehouse.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.warehouse.pojo.entity.WhseUnsalableReport;

public interface IWhseReportService extends IService<WhseUnsalableReport> {
	public IPage<Map<String, Object>> findUnsalableReportByCondition(Page<?> page,Map<String, Object> param);

	public List<Map<String, Object>> findInvChgRateByCondition(Map<String, Object> param);

	public List<Map<String, Object>> findMaterialSizeByCondition(Map<String, Object> param) throws ERPBizException;

	public void generateReprot();

	public IPage<Map<String, Object>> findFbaUnsalableReportByCondition(Page<?> page,Map<String, Object> param);

	public void findFBAInvDead(SXSSFWorkbook workbook, Map<String, Object> params);

	public void findLocalInvDead(SXSSFWorkbook workbook, Map<String, Object> params);

	public Map<String, Object> getFbaSnapdate(Map<String, Object> params);

	public void setChgRateExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);

	public IPage<Map<String, Object>> findUnsalableReportByDay(Page<?> page,Map<String, Object> param);
}
