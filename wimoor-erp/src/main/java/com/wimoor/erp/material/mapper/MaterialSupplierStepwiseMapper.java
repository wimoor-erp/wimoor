package com.wimoor.erp.material.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.material.pojo.entity.MaterialSupplierStepwise;
@Mapper
public interface MaterialSupplierStepwiseMapper extends BaseMapper<MaterialSupplierStepwise>{

	List<Map<String, Object>> selectSupplierByMainId(@Param("id")String id, @Param("supid")String supid);
    
}