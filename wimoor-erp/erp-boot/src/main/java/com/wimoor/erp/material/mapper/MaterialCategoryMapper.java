package com.wimoor.erp.material.mapper;



import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.material.pojo.entity.MaterialCategory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface MaterialCategoryMapper extends BaseMapper<MaterialCategory>{
	IPage<MaterialCategory> findByCondition(Page<?> page,@Param("shopid")String shopid,@Param("search")String search);
	List<MaterialCategory> findCategory(@Param("shopid")String shopid,@Param("mtype")String mtype);
}