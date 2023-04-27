package com.wimoor.amazon.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.common.pojo.entity.DaysalesFormula;

@Mapper
public interface DaysalesFormulaMapper  extends BaseMapper<DaysalesFormula> {

	DaysalesFormula selectByShopid(String shopid);
    
}