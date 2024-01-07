package com.wimoor.erp.inventory.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.common.pojo.entity.FormType;
import com.wimoor.erp.inventory.pojo.entity.InventoryRecord;

public interface IInventoryRecordService extends IService<InventoryRecord>{

	IPage<Map<String, Object>> findRecordList(Page<Object> page, Map<String, Object> maps);

	List<FormType> getFormTypeList();

	List<Map<String, Object>> findDownloadList(Map<String, Object> maps);

	void setExcelBookInventoryRecReport(SXSSFWorkbook workbook, List<Map<String, Object>> recordlist);

	Map<String, Object> findSkuInvHistory(String materialid, String fromDate, String toDate, String warehouseid, String shopid);
	public void downloadOutstockformOut(SXSSFWorkbook workbook,Map<String,Object> param);
}
