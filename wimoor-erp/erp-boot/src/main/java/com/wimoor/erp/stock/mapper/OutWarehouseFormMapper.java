package com.wimoor.erp.stock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseForm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface OutWarehouseFormMapper extends BaseMapper<OutWarehouseForm> {
	
	IPage<Map<String,Object>> findByCondition(Page<?> page,@Param("params")Map<String, Object> params);

	Map<String, Object> findById(String id);

	List<Map<String, Object>> findDetailByCondition(@Param("params") Map<String, Object> map);
	 
}