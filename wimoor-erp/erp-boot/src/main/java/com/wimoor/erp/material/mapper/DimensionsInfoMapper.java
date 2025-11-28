package com.wimoor.erp.material.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.material.pojo.entity.DimensionsInfo;
@Mapper
public interface DimensionsInfoMapper extends BaseMapper<DimensionsInfo>{
    
    DimensionsInfo findByAuthAsinSKU(@Param("shopId")String shopId, @Param("asinsku")String asinsku);
   
}