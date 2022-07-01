package com.wimoor.erp.inventory.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.inventory.pojo.entity.ChangeWhForm;
@Mapper
public interface ChangeWhFormMapper extends BaseMapper<ChangeWhForm>{

	IPage<Map<String, Object>> findByCondition(Page<?>  page,Map<String, Object> map);

	Map<String, Object> findById(String id);
	
	List<ChangeWhForm> getChangeWhFormByWarehouseid(@Param("warehouseid")String warehouseid);
}