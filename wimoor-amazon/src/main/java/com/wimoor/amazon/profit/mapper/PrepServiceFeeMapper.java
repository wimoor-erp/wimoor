package com.wimoor.amazon.profit.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.profit.pojo.entity.PrepServiceFee;

@Mapper
public interface PrepServiceFeeMapper  extends BaseMapper<PrepServiceFee> {

	PrepServiceFee getPrepServiceFeeByCategory(@Param(value = "isStandard") boolean isStandard,
			@Param(value = "country") String country, @Param(value = "category") String category);

	List<Map<String,String>> getAllCategory();
}