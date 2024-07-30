package com.wimoor.erp.stock.service;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseForm;

public interface IOutWarehouseFormService extends IService<OutWarehouseForm> {
	IPage<Map<String,Object>> findByCondition(Page<?> page,Map<String, Object> map);

	Map<String, Object> findById(String id);

	Map<String,Object> saveAction(OutWarehouseForm outWarehouseForm, String sku, UserInfo user) throws BizException ;

	void deleteOtherOutInventory(UserInfo user, String id) throws BizException;

	String uploadOutStockByExcel(Sheet sheet, UserInfo user) throws BizException;

	void getListDetailForExport(SXSSFWorkbook workbook, Map<String, Object> map);
}
