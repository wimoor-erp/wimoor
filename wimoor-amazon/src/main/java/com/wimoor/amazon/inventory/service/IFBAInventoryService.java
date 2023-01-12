package com.wimoor.amazon.inventory.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inventory.pojo.dto.InventoryPlanningDTO;
import com.wimoor.amazon.inventory.pojo.entity.InventoryReport;
import com.wimoor.amazon.inventory.pojo.vo.AmzInventoryPlanningVo;

public interface IFBAInventoryService extends IService<InventoryReport>{
	
	public List<Map<String, Object>> findByTypeWithStockCycle(String msku, String shopid) ;
	public Map<String, Object> findSumByType(Map<String,Object> param) ;
	public Map<String, Object> findFBAInvDetailById(String sku, String warehouseid, String shopid,String groupid) ;
	public IPage<Map<String, Object>> getFBAInvDayDetail(Page<?> page, Map<String, Object> parameter);
	public IPage<AmzInventoryPlanningVo> selectPlanPageList(InventoryPlanningDTO dto);
	public void downloadPlanList(SXSSFWorkbook workbook, InventoryPlanningDTO condition);
	public AmzInventoryPlanningVo summaryPlanning(InventoryPlanningDTO dto);

}
