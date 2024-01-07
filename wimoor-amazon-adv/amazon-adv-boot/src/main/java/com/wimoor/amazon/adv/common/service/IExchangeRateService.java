package com.wimoor.amazon.adv.common.service;

import java.util.List;

import com.wimoor.amazon.adv.common.pojo.ExchangeRate;

public interface IExchangeRateService extends IService<ExchangeRate> {
	public List<ExchangeRate> getExchangeRateLimit();
	public ExchangeRate selectByName(String from);
	public ExchangeRate selectMineByName(String from, String shopid);
}
