package com.wimoor.amazon.adv.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.wimoor.amazon.adv.common.pojo.ExchangeRateCustomer;

import com.wimoor.amazon.base.BaseMapper;

@Mapper
public interface ExchangeRateCustomerMapper extends BaseMapper<ExchangeRateCustomer> {
	List<Map<String,Object>> getMyExchangeRate(String shopid);
}