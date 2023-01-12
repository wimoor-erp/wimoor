package com.wimoor.erp.inventory.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.inventory.pojo.entity.InWarehouseForm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface InWarehouseFormMapper extends BaseMapper<InWarehouseForm> {
	
	IPage<Map<String, Object>> findByCondition(Page<?> page,@Param("params") Map<String, Object> map);

	Map<String, Object> findById(String id);
	 
}