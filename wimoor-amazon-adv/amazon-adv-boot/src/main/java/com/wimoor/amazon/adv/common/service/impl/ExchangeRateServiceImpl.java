package com.wimoor.amazon.adv.common.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.wimoor.amazon.adv.common.dao.ExchangeRateCustomerMapper;
import com.wimoor.amazon.adv.common.dao.ExchangeRateMapper;
import com.wimoor.amazon.adv.common.pojo.ExchangeRate;
import com.wimoor.amazon.adv.common.service.IExchangeRateService;
import com.wimoor.amazon.base.BaseService;

@Service("exchangeRateService")
public class ExchangeRateServiceImpl extends BaseService<ExchangeRate> implements IExchangeRateService {
	@Resource
	ExchangeRateMapper exchangeRateMapper;
	@Resource
	ExchangeRateCustomerMapper exchangeRateCustomerMapper;
	 
	public List<ExchangeRate> getExchangeRateLimit() {
		return exchangeRateMapper.getExchangeRateLimit();
	}

	public List<Map<String, Object>> getMyCurrencyRate(String shopid) {
		return exchangeRateCustomerMapper.getMyExchangeRate(shopid);
	}
 

	@Cacheable(value = "exchangeAdvRateCache#60")
	public ExchangeRate selectByName(String currency) {
		// TODO Auto-generated method stub
		return exchangeRateMapper.selectByName(currency);
	}

	@Cacheable(value = "exchangeAdvRateCache#60")
	public ExchangeRate selectMineByName(String currency, String shopid) {
		// TODO Auto-generated method stub
		return exchangeRateMapper.selectMineByName(currency, shopid);
	}

}
