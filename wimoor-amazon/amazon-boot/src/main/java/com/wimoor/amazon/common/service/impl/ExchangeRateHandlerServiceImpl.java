package com.wimoor.amazon.common.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.pojo.entity.ExchangeRate;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.common.service.IExchangeRateService;
import com.wimoor.common.mvc.BizException;

import cn.hutool.core.util.StrUtil;

@Service("exchangeRateHandlerService")
public class ExchangeRateHandlerServiceImpl  implements IExchangeRateHandlerService {
 
	@Resource
	IExchangeRateService exchangeRateService;
    @Resource
    IMarketplaceService marketplaceService;
	public BigDecimal changeCurrencyByLocal(String from, String to, BigDecimal money) throws BizException {
		if (from == null || to == null) {
			throw new BizException("换算的币别不能为空");
		}
		if (money == null)
			return new BigDecimal("0");
		if (from.equals(to)) {
			return money;
		}
		if ("RMB".equals(from))
			from = "CNY";
		if ("RMB".equals(to))
			to = "CNY";
		ExchangeRate exchangeRateFrom  = exchangeRateService.selectByName(from);
		ExchangeRate exchangeRateTo =  exchangeRateService.selectByName(to);
		 
		if (null == exchangeRateFrom || null == exchangeRateTo) {
			if (exchangeRateFrom == null) {
				throw new BizException("找不到对应汇率[" + from + "]，无法完成计算");
			} else {
				throw new BizException("找不到对应汇率[" + to + "]，无法完成计算");
			}
		}
		BigDecimal tempmoney = money;
		BigDecimal rate = exchangeRateFrom.getPrice().multiply(new BigDecimal("1000.000000"))
				.divide(exchangeRateTo.getPrice().multiply(new BigDecimal("1000.000000")), 6,RoundingMode.DOWN);
		BigDecimal result = tempmoney.multiply(rate);
		return result.setScale(6, RoundingMode.DOWN);
	}
	public Map<String,BigDecimal> currencyChangeRateByLocal(String shopid,String toCurrency) throws BizException {
		List<Marketplace> marketlist = marketplaceService.findAllMarketplace();
		Marketplace cn=marketplaceService.getById("AAHKV2X7AFYLW");
		marketlist.add(cn);
	    Map<String,BigDecimal> marketplace_rate=new HashMap<String,BigDecimal>();
		for(Marketplace market:marketlist) {
			ExchangeRate exchangeRateFrom  = null;
			ExchangeRate exchangeRateTo = null;
			if(StrUtil.isNotEmpty(shopid)) {
				exchangeRateFrom = exchangeRateService.selectMineByName(market.getCurrency(),shopid);
				exchangeRateTo = exchangeRateService.selectMineByName(toCurrency,shopid);
			}else {
				exchangeRateFrom = exchangeRateService.selectByName(market.getCurrency());
				exchangeRateTo = exchangeRateService.selectByName(toCurrency);
			}
			if (null == exchangeRateFrom ) {
				if (exchangeRateFrom == null) {
					throw new BizException("找不到对应汇率[" + market.getCurrency() + "]，无法完成计算");
				}  
			}
			BigDecimal rate = exchangeRateFrom.getPrice().multiply(new BigDecimal("1000.000000"))
					.divide(exchangeRateTo.getPrice().multiply(new BigDecimal("1000.000000")), 6,RoundingMode.DOWN);
			marketplace_rate.put(market.getCurrency(), rate);
		}
	 return marketplace_rate;
 
	}
	
	public Map<String,BigDecimal> currencyChangeRate(String toCurrency) throws BizException {
		List<Marketplace> marketlist = marketplaceService.findAllMarketplace();
	    Map<String,BigDecimal> marketplace_rate=new HashMap<String,BigDecimal>();
		for(Marketplace market:marketlist) {
			ExchangeRate exchangeRateFrom  = exchangeRateService.selectByName(market.getCurrency());
			ExchangeRate exchangeRateTo =  exchangeRateService.selectByName(toCurrency);
			if (null == exchangeRateFrom ) {
				if (exchangeRateFrom == null) {
					throw new BizException("找不到对应汇率[" + market.getCurrency() + "]，无法完成计算");
				}  
			}
			BigDecimal rate = exchangeRateFrom.getPrice().multiply(new BigDecimal("1000.000000"))
					.divide(exchangeRateTo.getPrice().multiply(new BigDecimal("1000.000000")), 6,RoundingMode.DOWN);
			marketplace_rate.put(market.getCurrency(), rate);
		}
	 return marketplace_rate;
	}
	
	public BigDecimal changeCurrencyByLocal(String from, String to, BigDecimal money,int point) throws BizException {
		if (from == null || to == null) {
			throw new BizException("换算的币别不能为空");
		}
		if (money == null)
			return new BigDecimal("0");
		if (from.equals(to)) {
			return money;
		}
		if ("RMB".equals(from))
			from = "CNY";
		if ("RMB".equals(to))
			to = "CNY";
		ExchangeRate exchangeRateFrom  = exchangeRateService.selectByName(from);
		ExchangeRate exchangeRateTo   = exchangeRateService.selectByName(to);
		 
		if (null == exchangeRateFrom || null == exchangeRateTo) {
			if (exchangeRateFrom == null) {
				throw new BizException("找不到对应汇率[" + from + "]，无法完成计算");
			} else {
				throw new BizException("找不到对应汇率[" + to + "]，无法完成计算");
			}
		}
		BigDecimal tempmoney = money;
		BigDecimal rate = exchangeRateFrom.getPrice().multiply(new BigDecimal("1000.000000"))
				.divide(exchangeRateTo.getPrice().multiply(new BigDecimal("1000.000000")), 6,RoundingMode.DOWN);
		BigDecimal result = tempmoney.multiply(rate);
		return result.setScale(point, RoundingMode.DOWN);
	}
	
	public BigDecimal changeCurrencyByLocal(String shopid,String from, String to, BigDecimal money) throws BizException {
		if (from == null || to == null) {
			throw new BizException("换算的币别不能为空");
		}
		if (money == null)
			return new BigDecimal("0");
		if (from.equals(to)) {
			return money;
		}
		if ("RMB".equals(from))
			from = "CNY";
		if ("RMB".equals(to))
			to = "CNY";
		ExchangeRate exchangeRateFrom = null;
		ExchangeRate exchangeRateTo = null;
			if(StrUtil.isNotEmpty(shopid)) {
				exchangeRateFrom = exchangeRateService.selectMineByName(from,shopid);
				exchangeRateTo = exchangeRateService.selectMineByName(to,shopid);
			}else {
				exchangeRateFrom = exchangeRateService.selectByName(from);
				exchangeRateTo = exchangeRateService.selectByName(to);
			}
		if (null == exchangeRateFrom || null == exchangeRateTo) {
			if (exchangeRateFrom == null) {
				throw new BizException("找不到对应汇率[" + from + "]，无法完成计算");
			} else {
				throw new BizException("找不到对应汇率[" + to + "]，无法完成计算");
			}
		}
		BigDecimal tempmoney = money;
		BigDecimal rate = exchangeRateFrom.getPrice().multiply(new BigDecimal("1000.000000"))
				.divide(exchangeRateTo.getPrice().multiply(new BigDecimal("1000.000000")), 6, RoundingMode.DOWN);
		BigDecimal result = tempmoney.multiply(rate);
		return result.setScale(6, RoundingMode.DOWN);
	}
	
	public BigDecimal changeCurrencyByLocal(String shopid,String from, String to, BigDecimal money,int point) throws BizException {
		if (from == null || to == null) {
			throw new BizException("换算的币别不能为空");
		}
		if (money == null)
			return new BigDecimal("0");
		if (from.equals(to)) {
			return money;
		}
		if ("RMB".equals(from))
			from = "CNY";
		if ("RMB".equals(to))
			to = "CNY";
		ExchangeRate exchangeRateFrom = null;
	    ExchangeRate exchangeRateTo = exchangeRateService.selectMineByName(to,shopid);
		if(StrUtil.isNotEmpty(shopid)) {
			exchangeRateFrom = exchangeRateService.selectMineByName(from,shopid);
		    exchangeRateTo = exchangeRateService.selectMineByName(to,shopid);
		}else {
			exchangeRateFrom = exchangeRateService.selectByName(from);
			exchangeRateTo = exchangeRateService.selectByName(to);
		}
        
		if (null == exchangeRateFrom || null == exchangeRateTo) {
			if (exchangeRateFrom == null) {
				throw new BizException("找不到对应汇率[" + from + "]，无法完成计算");
			} else {
				throw new BizException("找不到对应汇率[" + to + "]，无法完成计算");
			}
		}
		if(exchangeRateTo.getPrice().compareTo(new BigDecimal("0"))==0) {
			return new BigDecimal("0");
		}
		BigDecimal tempmoney = money;
		BigDecimal rate = exchangeRateFrom.getPrice().multiply(new BigDecimal("1000.000000"))
				.divide(exchangeRateTo.getPrice().multiply(new BigDecimal("1000.000000")), 6,RoundingMode.DOWN);
		BigDecimal result = tempmoney.multiply(rate);
		return result.setScale(point, RoundingMode.DOWN);
	}
 
 
 
	
 
}
