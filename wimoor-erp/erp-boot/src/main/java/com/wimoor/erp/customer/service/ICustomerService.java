package com.wimoor.erp.customer.service;


import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.customer.pojo.entity.Customer;

public interface ICustomerService extends IService<Customer> {

	IPage<Map<String,Object>> findByCondition(Page<?> page,String  search, String shopid);

	List<Customer> findByShopId(String shopid, String name);

	Customer findByName(String string, String trim);

	void setCustomerExcelBook(SXSSFWorkbook workbook, String search, String shopid);
	
	public  List<Map<String, Object>> findByCondition(String search, String shopid);
 
}
