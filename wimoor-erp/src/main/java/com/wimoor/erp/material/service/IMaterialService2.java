package com.wimoor.erp.material.service;

 
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.material.pojo.entity.Material;
 
 

public interface IMaterialService2  extends IService<Material> {
	
	Map<String, Object> findMaterialById(String id);
 
	IPage<Map<String, Object>> findByCondition(Map<String, Object> map, Page<?>  page);
 

	 
}
