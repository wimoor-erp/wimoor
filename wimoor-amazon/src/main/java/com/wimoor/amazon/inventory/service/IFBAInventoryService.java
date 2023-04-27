package com.wimoor.amazon.inventory.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inventory.pojo.dto.InventoryPlanningDTO;
import com.wimoor.amazon.inventory.pojo.entity.InventoryReport;
import com.wimoor.amazon.inventory.pojo.vo.AmzInventoryPlanningVo;
import com.wimoor.amazon.inventory.pojo.vo.ProductInventoryVo;

public interface IFBAInventoryService extends IService<InventoryReport>{
	
	public List<Map<String, Object>> findByTypeWithStockCycle(String msku, String shopid) ;
	public Map<String, Object> findSum(Map<String,Object> param) ;
	public Map<String, Object> findFBAInvDetailById(String sku, String warehouseid, String shopid,String groupid) ;
	public IPage<Map<String, Object>> getFBAInvDayDetail(Page<?> page, Map<String, Object> parameter);
	public IPage<AmzInventoryPlanningVo> selectPlanPageList(InventoryPlanningDTO dto);
	public void downloadPlanList(SXSSFWorkbook workbook, InventoryPlanningDTO condition);
	public AmzInventoryPlanningVo summaryPlanning(InventoryPlanningDTO dto);
	public IPage<ProductInventoryVo> findFba(Page<Object> page, Map<String, Object> param);
	public List<ProductInventoryVo> findFba(Map<String, Object> param);
	public void getExcelBookBAInventoryReport(SXSSFWorkbook workbook, List<ProductInventoryVo> inventoryList);
	IPage<Map<String, Object>> findFbaCountry(Page<Object> page, Map<String, Object> param);
	List<Map<String, String>> getInvDaySumField(Map<String, Date> parameter);
	public void downloadFBAInvDayDetail(SXSSFWorkbook workbook, Map<String, Object> parameter);
	public Map<String, Object> getFBAInvDayDetailTotal(Map<String, Object> parameter);
}
