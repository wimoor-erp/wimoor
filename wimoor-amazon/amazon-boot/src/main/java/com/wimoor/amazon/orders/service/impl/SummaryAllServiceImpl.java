package com.wimoor.amazon.orders.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.orders.mapper.SummaryAllMapper;
import com.wimoor.amazon.orders.pojo.entity.SummaryAll;
import com.wimoor.amazon.orders.service.ISummaryAllService;
import com.wimoor.amazon.util.ChartPoint;
import com.wimoor.common.GeneralUtil;
@Service("summaryAllService")
public class SummaryAllServiceImpl extends ServiceImpl<SummaryAllMapper,SummaryAll> implements ISummaryAllService  {
	@Resource
	IAmazonAuthorityService amazonAuthorityService;
	@Resource
	IMarketplaceService marketplaceService;
	@Resource
	IExchangeRateHandlerService exchangeRateHandlerService;
	public BigDecimal orderSummaryAll(Map<String, Object> param) {
		Map<String, Marketplace> allmarketplace = marketplaceService.findMapByPoint();
		List<Map<String, Object>> list = this.baseMapper.selectSummary(param);
		BigDecimal result = new BigDecimal("0");
		String currency = "RMB";
		if (param.get("currency") != null && !"".equals(param.get("currency").toString().trim())) {
			currency = param.get("currency").toString();
		}
		for (Map<String, Object> item : list) {
			Object point = item.get("sales_channel");
			Object price = item.get("price");
			Marketplace market = allmarketplace.get(point.toString());
			if (market != null) {
				BigDecimal rmbprice = exchangeRateHandlerService.changeCurrencyByLocal(market.getCurrency(), currency, new BigDecimal(price.toString()));
				result = result.add(rmbprice);
			}
		}
		return result;
	}

	public Map<String, Object> orderMonthsSummaryAll(Map<String, Object> param) {
		Map<String, Marketplace> allmarketplace = marketplaceService.findMapByPoint();
		List<Map<String, Object>> list = this.baseMapper.selectMonthsSummary(param);
		Map<String, ArrayList<Map<String, Object>>> map = GeneralUtil.groupListMapBy(list, "purchase_date");
		Map<String, Object> summaryMap = new HashMap<String, Object>();
		String currency = "RMB";
		if (param.get("currency") != null && !"".equals(param.get("currency").toString().trim())) {
			currency = param.get("currency").toString();
		}
		for (Entry<String, ArrayList<Map<String, Object>>> itemlist : map.entrySet()) {
			String purchase_date = itemlist.getKey();
			BigDecimal result = new BigDecimal("0");
			String newkey = purchase_date;
			for (Map<String, Object> itemMap : itemlist.getValue()) {
				Object point = itemMap.get("sales_channel");
				Object price = itemMap.get("price");
				Marketplace market = allmarketplace.get(point.toString());
				if (market != null) {
					BigDecimal rmbprice = exchangeRateHandlerService.changeCurrencyByLocal(market.getCurrency(), currency, new BigDecimal(price.toString()));
					result = result.add(rmbprice);
				}
			}
			summaryMap.put(newkey, result);
		}
		Date begin;
		try {
			begin = GeneralUtil.FMT_YMD.parse(param.get("beginDate").toString());
			Date end = GeneralUtil.FMT_YMD.parse(param.get("endDate").toString());
			return ChartPoint.generateChartDate(ChartPoint.SumType.Monthly, summaryMap, begin, end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, Object> orderDaysSummaryAll(Map<String, Object> param) {
		Map<String, Marketplace> allmarketplace = marketplaceService.findMapByPoint();
		List<Map<String, Object>> list = this.baseMapper.selectDaysSummary(param);
		Map<String, ArrayList<Map<String, Object>>> map = GeneralUtil.groupListMapBy(list, "purchase_date");
		Map<String, Object> summaryMap = new HashMap<String, Object>();
		String currency = "RMB";
		if (param.get("currency") != null && !"".equals(param.get("currency").toString().trim())) {
			currency = param.get("currency").toString();
		}
		for (Entry<String, ArrayList<Map<String, Object>>> itemlist : map.entrySet()) {
			String purchase_date = itemlist.getKey();
			BigDecimal result = new BigDecimal("0");
			String newkey = purchase_date.substring(purchase_date.length() - 5, purchase_date.length());
			for (Map<String, Object> itemMap : itemlist.getValue()) {
				Object point = itemMap.get("sales_channel");
				Object price = itemMap.get("price");
				Marketplace market = allmarketplace.get(point.toString());
				if (market != null) {
					BigDecimal rmbprice = exchangeRateHandlerService.changeCurrencyByLocal(market.getCurrency(), currency, new BigDecimal(price.toString()));
					result = result.add(rmbprice);
				}
			}
			summaryMap.put(newkey, result);
		}
		Date begin;
		try {
			begin = GeneralUtil.FMT_YMD.parse(param.get("beginDate").toString());
			Date end = GeneralUtil.FMT_YMD.parse(param.get("endDate").toString());
			return ChartPoint.generateChartDate(ChartPoint.SumType.Daily, summaryMap, begin, end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
