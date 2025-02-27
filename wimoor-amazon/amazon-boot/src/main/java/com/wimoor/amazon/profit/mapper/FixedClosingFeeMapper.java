package com.wimoor.amazon.profit.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.profit.pojo.entity.FixedClosingFee;
@Mapper
public interface FixedClosingFeeMapper  extends BaseMapper<FixedClosingFee> {
	List<FixedClosingFee> findByCountry(String country);
}
