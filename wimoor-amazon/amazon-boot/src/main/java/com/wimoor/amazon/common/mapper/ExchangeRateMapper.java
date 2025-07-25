package com.wimoor.amazon.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.common.pojo.entity.ExchangeRate;

@Mapper
public interface ExchangeRateMapper extends BaseMapper<ExchangeRate> {
	ExchangeRate selectByName(String name);
	ExchangeRate selectMineByName(@Param("name")String name,@Param("shopid")String shopid);
    int deleteAll();
	List<ExchangeRate> getExchangeRateLimit();
}
