package com.wimoor.erp.stock.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.stock.pojo.entity.DispatchFormEntry;

public interface IDispatchFormEntryService extends IService<DispatchFormEntry> {

	List<Map<String, Object>> selectByFormid(String formid);

	void deleteByFormid(Object formid);

	List<Map<String, Object>> findFormDetailByFormid(String formid, String warehouseid,String warehouseid2,String shopid);
	
	public List<DispatchFormEntry> findByFormid(String formid) ;

}
