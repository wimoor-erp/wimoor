package com.wimoor.amazon.adv.common.service;

import java.math.BigDecimal;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.BaseException;
 

public interface IExchangeRateHandlerService  {
	public Map<String,BigDecimal> currencyChangeRate(String toCurrency) throws BaseException ;
	public Map<String, BigDecimal> currencyChangeRateByLocal(String shopid,String toCurrency)throws BaseException ;
	public BigDecimal changeCurrencyByLocal(String from, String to, BigDecimal money) throws BaseException;
	
	public BigDecimal changeCurrencyByLocal(String from, String to, BigDecimal money,int point) throws BaseException ;
	
	public BigDecimal changeCurrencyByLocal(String shopid,String from, String to, BigDecimal money) throws BaseException;
	
	public BigDecimal changeCurrencyByLocal(String shopid,String from, String to, BigDecimal money,int point) throws BaseException ;
 
}
