package com.wimoor.erp.customer.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.erp.customer.mapper.CustomerMapper;
import com.wimoor.erp.customer.pojo.entity.Customer;
import com.wimoor.erp.customer.service.ICustomerService;
import com.wimoor.erp.material.mapper.MaterialSupplierMapper;

import lombok.RequiredArgsConstructor;
 

@Service("customerService")
@RequiredArgsConstructor
public class CustomerServiceImpl extends  ServiceImpl<CustomerMapper,Customer> implements ICustomerService {
 
	ISerialNumService serialNumService;
     
    MaterialSupplierMapper materialSupplierMapper;
 
	public  IPage<Map<String, Object>> findByCondition(Page<?> page,String search, String shopid) {
			return this.baseMapper.findByCondition(page,search, shopid);
	}
	
	public  List<Map<String, Object>> findByCondition(String search, String shopid) {
		return this.baseMapper.findByCondition(search, shopid);
	}
 
	public List<Customer> findByShopId(String shopid, String name) {
		return this.baseMapper.findByShopId(shopid, name);
	}

 
	
	public Customer findByName(String shopid, String name) {
		return this.baseMapper.findByName(shopid, name);
	}

	public void setCustomerExcelBook(SXSSFWorkbook workbook, String search, String shopid) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("name", "客户名称");
		titlemap.put("number", "客户编码");
		titlemap.put("ftype", "客户分类");
		titlemap.put("contacts", "联系人");
		titlemap.put("phone_num", "联系电话");
		titlemap.put("contact_info", "其它联系信息");
		titlemap.put("address", "地址");
		titlemap.put("operator", "操作人");
		titlemap.put("opttime", "修改时间");
		List<Map<String, Object>> list = this.baseMapper.findByCondition(search, shopid);
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					String key = titlearray[j].toString();
					Object value = map.get(key);
					if(key.equals("opttime")){
						if(value!=null && value.toString().endsWith(".0")){
							value = value.toString().substring(0,value.toString().length()-2);
						}
					}
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
	}

}
