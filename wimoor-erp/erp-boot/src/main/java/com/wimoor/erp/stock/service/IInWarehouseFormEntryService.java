package com.wimoor.erp.stock.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.stock.pojo.entity.InWarehouseFormEntry;

public interface IInWarehouseFormEntryService extends IService<InWarehouseFormEntry> {

	List<Map<String, Object>> selectByFormid(String formid);
	
	List<Map<String, Object>> findFormDetailByFormid(String formid);

	void deleteByFormid(String formid);

}
