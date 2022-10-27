package com.wimoor.amazon.profit.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.profit.pojo.entity.IndividualFee;
@Mapper
public interface IndividualFeeMapper extends BaseMapper<IndividualFee> {
	IndividualFee getByCountry(String country);
}