package com.wimoor.erp.material.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.material.pojo.entity.MaterialSupplier;
import com.wimoor.erp.material.pojo.vo.MaterialSupplierVO;
@Mapper
public interface MaterialSupplierMapper extends BaseMapper<MaterialSupplier>{

	List<MaterialSupplierVO> selectSupplierByMainId(@Param("id")String id);
	
	List<Map<String, Object>> selectSupplerOtherById(@Param("id")String id);
     
}