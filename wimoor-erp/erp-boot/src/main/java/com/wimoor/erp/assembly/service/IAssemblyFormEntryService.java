package com.wimoor.erp.assembly.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.assembly.pojo.entity.AssemblyFormEntry;

public interface IAssemblyFormEntryService extends IService<AssemblyFormEntry> {
	List<Map<String, Object>> selectByFormid(String formid);

	List<AssemblyFormEntry> selectEntityByFormid(String formid);
	public Integer selectNeedProcess(String materialId,String warehouseid,String shopid);
	List<Map<String, Object>> selectAssemblyFormByPurchaseEntryId(String entryid);
	
	
}
