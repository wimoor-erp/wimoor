package com.wimoor.erp.material.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.material.mapper.AssemblyFormEntryMapper;
import com.wimoor.erp.material.pojo.entity.AssemblyFormEntry;
import com.wimoor.erp.material.service.IAssemblyFormEntryService;

import lombok.RequiredArgsConstructor;

@Service("assemblyFormEntryService")
@RequiredArgsConstructor
public class AssemblyFormEntryServiceImpl extends  ServiceImpl<AssemblyFormEntryMapper,AssemblyFormEntry>  implements IAssemblyFormEntryService {

 
	public List<Map<String, Object>> selectByFormid(String formid) {
		return this.baseMapper.selectByFormid(formid);
	}

	public List<AssemblyFormEntry> selectEntityByFormid(String formid) {
		return  this.baseMapper.selectEntityByFormid(formid);
	}

	public Integer selectNeedProcess(String materialId,String warehouseid,String shopid) {
		return  this.baseMapper.selectNeedProcess(materialId,warehouseid,shopid);
	}

	public List<Map<String, Object>> selectAssemblyFormByPurchaseEntryId(String entryid) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> formlist =  this.baseMapper.selectAssemblyFormByPurchaseEntryId(entryid);
	    for(Map<String, Object> item:formlist) {
	    	List<Map<String, Object>> entrylist =  this.baseMapper.selectByFormid(item.get("formid").toString());
	    	item.put("entrylist", entrylist);
	    }
	    return formlist;
	}

	

}
