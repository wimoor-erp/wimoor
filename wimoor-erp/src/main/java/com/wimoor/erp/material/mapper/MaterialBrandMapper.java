package com.wimoor.erp.material.mapper;



import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.material.pojo.entity.MaterialBrand;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface MaterialBrandMapper extends BaseMapper<MaterialBrand>{
	IPage<MaterialBrand> findByCondition(Page<?> page,@Param("shopid")String shopid,@Param("search")String search);
}