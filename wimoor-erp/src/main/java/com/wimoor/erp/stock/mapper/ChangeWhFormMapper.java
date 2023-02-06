package com.wimoor.erp.stock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.stock.pojo.entity.ChangeWhForm;
@Mapper
public interface ChangeWhFormMapper extends BaseMapper<ChangeWhForm>{

	IPage<Map<String, Object>> findByCondition(Page<?>  page,@Param("params")Map<String, Object> map);

	Map<String, Object> findById(@Param("id")String id);
	
	List<ChangeWhForm> getChangeWhFormByWarehouseid(@Param("warehouseid")String warehouseid);
}