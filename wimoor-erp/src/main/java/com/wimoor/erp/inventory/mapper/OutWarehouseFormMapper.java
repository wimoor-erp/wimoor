package com.wimoor.erp.inventory.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.inventory.pojo.entity.OutWarehouseForm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface OutWarehouseFormMapper extends BaseMapper<OutWarehouseForm> {
	
	IPage<Map<String,Object>> findByCondition(Page<?> page,Map<String, Object> map);

	Map<String, Object> findById(String id);

	List<Map<String, Object>> findDetailByCondition(Map<String, Object> map);
	 
}