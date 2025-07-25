package com.wimoor.erp.stock.service;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.stock.pojo.entity.InWarehouseForm;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public interface IInWarehouseFormService extends IService<InWarehouseForm> {
	IPage<Map<String, Object>> findByCondition(Page<?> page,Map<String, Object> map);

	Map<String, Object> findById(String id);

	Map<String, Object> saveAction(InWarehouseForm inWarehouseForm, String sku, UserInfo user) throws BizException;

	void deleteOtherInInventory(UserInfo user, String id) throws BizException;

	String uploadInStockByExcel(Sheet sheet,UserInfo user) ;

    void getListDetailForExport(SXSSFWorkbook workbook, Map<String, Object> map);
}
