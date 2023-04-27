package com.wimoor.erp.stock.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.stock.pojo.entity.ChangeWhFormEntry;

public interface IChangeWhFormEntryService extends IService<ChangeWhFormEntry> {

	void deleteByFormid(String id);

	List<Map<String, Object>> findFormDetailByFormid(String formid);

	List<ChangeWhFormEntry> selectByFormid(String formid);
	
}
