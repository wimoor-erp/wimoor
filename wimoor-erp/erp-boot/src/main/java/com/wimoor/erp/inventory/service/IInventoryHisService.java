package com.wimoor.erp.inventory.service;

import com.wimoor.common.mvc.BizException;
import com.wimoor.erp.inventory.pojo.dto.InvDayDetailDTO;
import com.wimoor.erp.inventory.pojo.entity.InventoryHis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IInventoryHisService extends IService<InventoryHis>  {
	public boolean updateAll(InventoryHis entity) throws BizException ;
	public InventoryHis selectOne(InventoryHis entity) throws BizException ;
	public void summaryInvEveryDay(String byday,String endday);
	public List<Map<String,Object>> findInvDayList(Map<String,Object> maps);
	public void runTask();
	List<String> getInvDayField(Map<String, Date> parameter);
	public IPage<Map<String, Object>> getInvDayDetail(InvDayDetailDTO query,Map<String, Object> parameter);
	public void downloadFBAInvDayDetail(SXSSFWorkbook workbook, Map<String, Object> parameter);
}
