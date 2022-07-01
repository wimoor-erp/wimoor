package com.wimoor.erp.inventory.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.inventory.pojo.entity.OutWarehouseFormEntry;

public interface IOutWarehouseFormEntryService extends IService<OutWarehouseFormEntry> {

	List<Map<String, Object>> selectByFormid(String formid);

	void deleteByFormid(String formid);

	List<Map<String, Object>> findFormDetailByFormid(String string);

}
