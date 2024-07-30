package com.wimoor.amazon.adv.report.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.api.AmazonClientOneFeignManager;
import com.wimoor.amazon.adv.common.pojo.Marketplace;
import com.wimoor.amazon.adv.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.adv.common.service.IMarketplaceService;
import com.wimoor.amazon.adv.report.dao.AmzAdvSumProductAdsMapper;
import com.wimoor.amazon.adv.report.service.IAmzAdvSumAllTypeService;
import com.wimoor.amazon.adv.report.service.IAmzAdvSumProductAdsService;
import com.wimoor.amazon.adv.utils.ChartPoint;
import com.wimoor.common.GeneralUtil;
import com.wimoor.util.ExchangeRateUtil;

@Service("amzAdvSumProductAdsService")
public class AmzAdvSumProductAdsServiceImpl implements IAmzAdvSumProductAdsService {
	@Resource
	AmzAdvSumProductAdsMapper amzAdvSumProductAdsMapper;
	@Resource
	IMarketplaceService marketplaceService;
    @Resource
    IExchangeRateHandlerService exchangeRateHandlerService;
 
	@Resource
	IAmzAdvSumAllTypeService amzAdvSumAllTypeService;
	@Resource
	AmazonClientOneFeignManager amazonClientOneFeignManager;
//	@Resource
//	AmzProductActiveDayNumMapper amzProductActiveDayNumMapper;

	@Cacheable(value = "amzAdvProfileCache")
	public Map<String, Object> getSumProduct(Map<String, Object> param) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = amzAdvSumProductAdsMapper.getSumProduct(param);
		Integer sumclicks = 0;
		Integer sumOrdered = 0;
		Integer sumImpressions = 0;
		BigDecimal sumcost = new BigDecimal("0");
		BigDecimal sumSales = new BigDecimal("0");
		String currency = "RMB";
		if (param.get("currency") != null && !"".equals(param.get("currency").toString().trim())) {
			currency = param.get("currency").toString();
		}
		Map<String, Marketplace> allmarketplace = marketplaceService.findMapByMarketplaceId();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> item = list.get(i);
				String marketplaceId = item.get("marketplaceId").toString();
				Integer impressions = Integer.parseInt(item.get("impressions").toString());
				Integer clicks = Integer.parseInt(item.get("clicks").toString());
				BigDecimal cost = new BigDecimal(item.get("cost").toString());
				Integer attributedUnitsOrdered = Integer.parseInt(item.get("attributedUnitsOrdered").toString());
				Integer attributedConversions = item.get("attributedConversions") != null
						? Integer.parseInt(item.get("attributedConversions").toString()) : null;
				BigDecimal attributedSales = new BigDecimal(item.get("attributedSales").toString());
				Marketplace market = allmarketplace.get(marketplaceId);
				sumImpressions = sumImpressions + impressions;
				sumclicks = sumclicks + clicks;
				sumOrdered = sumOrdered + (attributedConversions == null ? attributedUnitsOrdered : attributedConversions);
				attributedSales=ExchangeRateUtil.changeCurrencyByLocal(market.getCurrency(), currency, attributedSales);
				cost=ExchangeRateUtil.changeCurrencyByLocal(market.getCurrency(), currency, cost);
				sumcost = sumcost.add(cost);
				sumSales = sumSales.add(attributedSales);
			}
			map.put("impressions", sumImpressions);
			map.put("clicks", sumclicks);
			if (sumImpressions != 0) {
				map.put("ctr", (sumclicks * 100.0) / (sumImpressions * 1.0));
			} else {
				map.put("ctr", 0);
			}
			map.put("cost", sumcost);
			if (sumclicks != null && sumclicks != 0) {
				map.put("cr", new BigDecimal(sumOrdered).multiply(new BigDecimal("100"))
						.divide(new BigDecimal(sumclicks), 2, RoundingMode.HALF_UP));
			} else {
				map.put("cr", 0);
			}
			map.put("attributedUnitsOrdered", sumOrdered);
			map.put("attributedSales", sumSales);
			if (sumSales != null && sumSales.compareTo(new BigDecimal("0")) != 0) {
				map.put("acos", sumcost.multiply(new BigDecimal("100")).divide(sumSales, 2, RoundingMode.HALF_UP));
			} else {
				map.put("acos", 0);
			}
		} else {
			map.put("impressions", 0);
			map.put("clicks", 0);
			map.put("ctr", 0);
			map.put("cost", 0);
			map.put("cr", 0.00);
			map.put("attributedUnitsOrdered", 0);
			map.put("attributedSales", 0.00);
			map.put("acos", 0.00);
		}
		return map;
	}

	public Map<String, Object> getDaysSumProduct(Map<String, Object> param) { 
		List<Map<String, Object>> listMap = amzAdvSumProductAdsMapper.getDaysSumProduct(param);
		Map<String, ArrayList<Map<String, Object>>> mapList = GeneralUtil.groupListMapBy(listMap, "byday");
		Map<String, Marketplace> allmarketplace = marketplaceService.findMapByMarketplaceId();
		Map<String, Object> impressionsMap = new HashMap<String, Object>();
		Map<String, Object> attributedSalesMap = new HashMap<String, Object>();
		Map<String, Object> costMap = new HashMap<String, Object>();
		Map<String, Object> clicksMap = new HashMap<String, Object>();
		Map<String, Object> orderMap = new HashMap<String, Object>();
		Map<String, Object> acosMap = new HashMap<String, Object>();
		Map<String, Object> crMap = new HashMap<String, Object>();
		Map<String, Object> ctrMap = new HashMap<String, Object>();
		String currency = "RMB";
		if (param.get("currency") != null && !"".equals(param.get("currency").toString().trim())) {
			currency = param.get("currency").toString();
		}
		for (Entry<String, ArrayList<Map<String, Object>>> itemEntry : mapList.entrySet()) {
			ArrayList<Map<String, Object>> list = itemEntry.getValue();
			String key = itemEntry.getKey();
			String newkey = key.substring(key.length() - 5, key.length());
			Integer sumclicks = 0;
			Integer sumOrdered = 0;
			Integer sumImpressions = 0;
			BigDecimal sumcost = new BigDecimal("0");
			BigDecimal sumSales = new BigDecimal("0");
			BigDecimal sumAcos = new BigDecimal("0");
			BigDecimal sumCtr = new BigDecimal("0");
			BigDecimal sumCr = new BigDecimal("0");
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> item = list.get(i);
					String marketplaceId = item.get("marketplaceId").toString();
					Integer impressions = Integer.parseInt(item.get("impressions").toString());
					Integer clicks = Integer.parseInt(item.get("clicks").toString());
					BigDecimal cost = new BigDecimal(item.get("cost").toString());
					Integer attributedUnitsOrdered = Integer.parseInt(item.get("attributedUnitsOrdered").toString());
					BigDecimal attributedSales = new BigDecimal(item.get("attributedSales").toString());
					Integer attributedConversions = item.get("attributedConversions") != null 
							? Integer.parseInt(item.get("attributedConversions").toString()) : null;
					sumImpressions = sumImpressions + impressions;
					sumclicks = sumclicks + clicks;
					sumOrdered = sumOrdered + (attributedConversions == null ? attributedUnitsOrdered : attributedConversions);
					Marketplace market = allmarketplace.get(marketplaceId);
					attributedSales=ExchangeRateUtil.changeCurrencyByLocal(market.getCurrency(), currency, attributedSales);
					cost=ExchangeRateUtil.changeCurrencyByLocal(market.getCurrency(), currency, cost);
					sumSales = sumSales.add(attributedSales);
					sumcost = sumcost.add(cost);
					MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
					if(clicks != null && clicks != 0) {
						Integer order = (attributedConversions == null ? attributedUnitsOrdered : attributedConversions);
						if(order != null && order != 0) {
							sumCr = new BigDecimal(order).divide(new BigDecimal(clicks), mc);
						}
					}
					if(impressions != null && impressions != 0) {
						sumCtr = new BigDecimal(clicks).divide(new BigDecimal(impressions), mc);
					}
				}
				impressionsMap.put(newkey, sumImpressions);
				clicksMap.put(newkey, sumclicks);
				costMap.put(newkey, sumcost.setScale(2, RoundingMode.HALF_UP));
				orderMap.put(newkey, sumOrdered);
				attributedSalesMap.put(newkey, sumSales.setScale(2, RoundingMode.HALF_UP));
				if (!(sumSales.compareTo(BigDecimal.ZERO) == 0)) {
					sumAcos = sumcost.divide(sumSales,2,RoundingMode.HALF_UP);
				} 
				acosMap.put(newkey, sumAcos);
				crMap.put(newkey, sumCr);
				ctrMap.put(newkey, sumCtr);
			} else {
				impressionsMap.put(newkey, 0);
				clicksMap.put(newkey, 0);
				costMap.put(newkey, 0);
				orderMap.put(newkey, 0);
				attributedSalesMap.put(newkey, 0);
				acosMap.put(newkey, 0);
				crMap.put(newkey, 0);
				ctrMap.put(newkey, 0);
			}
		}
		Date begin;
		try {
			begin = GeneralUtil.FMT_YMD.parse(param.get("beginDate").toString());
			Date end = GeneralUtil.FMT_YMD.parse(param.get("endDate").toString());
			impressionsMap = ChartPoint.generateChartDate(ChartPoint.SumType.Daily, impressionsMap, begin, end);
			clicksMap = ChartPoint.generateChartDate(ChartPoint.SumType.Daily, clicksMap, begin, end);
			costMap = ChartPoint.generateChartDate(ChartPoint.SumType.Daily, costMap, begin, end);
			attributedSalesMap = ChartPoint.generateChartDate(ChartPoint.SumType.Daily, attributedSalesMap, begin, end);
			orderMap = ChartPoint.generateChartDate(ChartPoint.SumType.Daily, orderMap, begin, end);
			acosMap = ChartPoint.generateChartDate(ChartPoint.SumType.Daily, acosMap, begin, end);
			crMap = ChartPoint.generateChartDate(ChartPoint.SumType.Daily, crMap, begin, end);
			ctrMap = ChartPoint.generateChartDate(ChartPoint.SumType.Daily, ctrMap, begin, end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("impressions", impressionsMap);
		resultMap.put("clicks", clicksMap);
		resultMap.put("cost", costMap);
		resultMap.put("attributedSales", attributedSalesMap);
		resultMap.put("impressions", impressionsMap);
		resultMap.put("order", orderMap);
		Map<String, Object> allsale = orderDaysSummaryAll(param);
		resultMap.put("allsale", allsale);
		resultMap.put("acos", acosMap);
		resultMap.put("cr", crMap);
		resultMap.put("ctr", ctrMap);
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getMonthSumProduct(Map<String, Object> param) {
		String currency = "RMB";
		if (param.get("currency") != null && !"".equals(param.get("currency").toString().trim())) {
			currency = param.get("currency").toString();
		}
		///////////////////////////// 第一次加载
		List<Map<String, Object>> listMap = amzAdvSumProductAdsMapper.getMonthSumProduct(param);
		Map<String, ArrayList<Map<String, Object>>> mapList = GeneralUtil.groupListMapBy(listMap, "byday");
		Map<String, Marketplace> allmarketplace = marketplaceService.findMapByMarketplaceId();
		List<Map<String, Object>> listAdgroupNum = amzAdvSumAllTypeService.getMonthSumAdgroupNum(param);
		List<Map<String, Object>> listAsinNum = amzAdvSumAllTypeService.getMonthSumAsinNum(param);

		///////////////////////////// 变量定义
		Map<String, Object> impressionsMap = new HashMap<String, Object>();
		Map<String, Object> attributedSalesMap = new HashMap<String, Object>();
		Map<String, Object> costMap = new HashMap<String, Object>();
		Map<String, Object> clicksMap = new HashMap<String, Object>();
		Map<String, Object> orderMap = new HashMap<String, Object>();
		Map<String, Object> cpc = new HashMap<String, Object>();
		Map<String, Object> ctr = new HashMap<String, Object>();
		Map<String, Object> cr = new HashMap<String, Object>();
		Map<String, Object> acos = new HashMap<String, Object>();
		Map<String, Object> acoas = new HashMap<String, Object>();
		Map<String, Object> salespall = new HashMap<String, Object>();
		Map<String, Object> adgroupNumMap = new HashMap<String, Object>();
		Map<String, Object> asinNumMap = new HashMap<String, Object>();
		Map<String, Object> skuNumMap = new HashMap<String, Object>();
		Map<String, Object> asinPerMap = new HashMap<String, Object>();

		Integer sumAlladgroupNum = 0;
		Integer sumAllAsinNum = 0;
		Integer sumAllSkuNum = 0;
		Integer sumAllClicks = 0;
		Integer sumAllOrdered = 0;
		Integer sumAllImpressions = 0;
		BigDecimal sumAllCost = new BigDecimal("0");
		BigDecimal sumAllSales = new BigDecimal("0");
		BigDecimal sumAllall = new BigDecimal("0");
		///////////////////////////// 初始化数据
		if (listAdgroupNum != null) {
			for (Map<String, Object> item : listAdgroupNum) {
				String key = (String) item.get("byday");
				BigDecimal quantity = (BigDecimal) item.get("quantity");
				adgroupNumMap.put(key, quantity);
				sumAlladgroupNum = sumAlladgroupNum + quantity.intValue();
			}
		}
		if (listAsinNum != null) {
			for (Map<String, Object> item : listAsinNum) {
				String key = (String) item.get("byday");
				Integer quantity = Integer.parseInt(item.get("quantity").toString());
				asinNumMap.put(key, quantity);
				sumAllAsinNum = sumAllAsinNum + quantity;
			}
		}
 
		///////////////////////////// 汇总数据
		for (Entry<String, ArrayList<Map<String, Object>>> itemEntry : mapList.entrySet()) {
			ArrayList<Map<String, Object>> list = itemEntry.getValue();
			String key = itemEntry.getKey();
			String newkey = key;
			Integer sumclicks = 0;
			Integer sumOrdered = 0;
			Integer sumImpressions = 0;
			BigDecimal sumcost = new BigDecimal("0");
			BigDecimal sumSales = new BigDecimal("0");
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> item = list.get(i);
					String marketplaceId = item.get("marketplaceId").toString();
					Integer impressions = Integer.parseInt(item.get("impressions").toString());
					Integer clicks = Integer.parseInt(item.get("clicks").toString());
					BigDecimal cost = new BigDecimal(item.get("cost").toString());
					Integer attributedUnitsOrdered = Integer.parseInt(item.get("attributedUnitsOrdered").toString());
					BigDecimal attributedSales = new BigDecimal(item.get("attributedSales").toString());
					Integer attributedConversions = item.get("attributedConversions") != null
							? Integer.parseInt(item.get("attributedConversions").toString()) : null;
					sumImpressions = sumImpressions + impressions;
					sumclicks = sumclicks + clicks;
					sumOrdered = sumOrdered + (attributedConversions == null ? attributedUnitsOrdered : attributedConversions);
					Marketplace market = allmarketplace.get(marketplaceId);
					cost = exchangeRateHandlerService.changeCurrencyByLocal(market.getCurrency(), currency, cost);
					sumcost = sumcost.add(cost);
					attributedSales = exchangeRateHandlerService.changeCurrencyByLocal(market.getCurrency(), currency, attributedSales);
					sumSales = sumSales.add(attributedSales);
					sumcost = sumcost.add(cost);
					sumSales = sumSales.add(attributedSales);
				}
				impressionsMap.put(newkey, sumImpressions);
				clicksMap.put(newkey, sumclicks);
				costMap.put(newkey, sumcost);
				orderMap.put(newkey, sumOrdered);
				attributedSalesMap.put(newkey, sumSales);
				if (sumImpressions != 0) {
					ctr.put(newkey, sumclicks * 100.0 / sumImpressions);
				} else {
					ctr.put(newkey, 0);
				}
				if (sumclicks != 0) {
					cr.put(newkey, sumOrdered * 100.0 / sumclicks);
					cpc.put(newkey, sumcost.divide(new BigDecimal(sumclicks), 2,RoundingMode.HALF_UP));
				} else {
					cr.put(newkey, 0);
				}
				if (sumSales.compareTo(new BigDecimal("0")) != 0) {
					acos.put(newkey, sumcost.multiply(new BigDecimal("100")).divide(sumSales, 2,RoundingMode.HALF_UP));
				} else {
					acos.put(newkey, 0);
				}

			} else {
				impressionsMap.put(newkey, 0);
				clicksMap.put(newkey, 0);
				costMap.put(newkey, 0);
				orderMap.put(newkey, 0);
				attributedSalesMap.put(newkey, 0);
				ctr.put(newkey, 0);
				cr.put(newkey, 0);
				acos.put(newkey, 0);
			}
			sumAllClicks = sumAllClicks + sumclicks;
			sumAllOrdered = sumAllOrdered + sumOrdered;
			sumAllImpressions = sumAllImpressions + sumImpressions;
			sumAllCost = sumAllCost.add(sumcost);
			sumAllSales = sumAllSales.add(sumSales);
		}
		Date begin;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			begin = GeneralUtil.FMT_YMD.parse(param.get("beginDate").toString());
			Date end = GeneralUtil.FMT_YMD.parse(param.get("endDate").toString());
			impressionsMap = ChartPoint.generateChartDate(ChartPoint.SumType.Monthly, impressionsMap, begin, end);
			clicksMap = ChartPoint.generateChartDate(ChartPoint.SumType.Monthly, clicksMap, begin, end);
			costMap = ChartPoint.generateChartDate(ChartPoint.SumType.Monthly, costMap, begin, end);
			attributedSalesMap = ChartPoint.generateChartDate(ChartPoint.SumType.Monthly, attributedSalesMap, begin, end);
			orderMap = ChartPoint.generateChartDate(ChartPoint.SumType.Monthly, orderMap, begin, end);
			ctr = ChartPoint.generateChartDate(ChartPoint.SumType.Monthly, ctr, begin, end);
			cr = ChartPoint.generateChartDate(ChartPoint.SumType.Monthly, cr, begin, end);
			cpc = ChartPoint.generateChartDate(ChartPoint.SumType.Monthly, cpc, begin, end);
			acos = ChartPoint.generateChartDate(ChartPoint.SumType.Monthly, acos, begin, end);
			adgroupNumMap = ChartPoint.generateChartDate(ChartPoint.SumType.Monthly, adgroupNumMap, begin, end);
			asinNumMap = ChartPoint.generateChartDate(ChartPoint.SumType.Monthly, asinNumMap, begin, end);
			skuNumMap = ChartPoint.generateChartDate(ChartPoint.SumType.Monthly, skuNumMap, begin, end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		///////////////////////////// 存储结果
		resultMap.put("impressions", impressionsMap);
		resultMap.put("clicks", clicksMap);
		resultMap.put("cost", costMap);
		resultMap.put("attributedSales", attributedSalesMap);
		resultMap.put("impressions", impressionsMap);
		resultMap.put("order", orderMap);
		resultMap.put("ctr", ctr);
		resultMap.put("cr", cr);
		resultMap.put("cpc", cpc);
		resultMap.put("acos", acos);
		resultMap.put("adgroupNum", adgroupNumMap);
		resultMap.put("asinNum", asinNumMap);
		resultMap.put("sumAllasinNum", sumAllAsinNum);
		resultMap.put("sumAlladgroupNum", sumAlladgroupNum);
		resultMap.put("sumAllclicks", sumAllClicks);
		resultMap.put("sumAllimpressions", sumAllImpressions);
		resultMap.put("sumAllcost", sumAllCost);
		resultMap.put("sumAllattributedSales", sumAllSales);
		resultMap.put("sumAllorder", sumAllOrdered);
		if (sumAllImpressions != 0) {
			resultMap.put("sumAllctr", sumAllClicks * 100.0 / sumAllImpressions);
		} else {
			resultMap.put("sumAllctr", 0);
		}
		if (sumAllClicks != 0) {
			resultMap.put("sumAllcr", sumAllOrdered * 100.0 / sumAllClicks);
			resultMap.put("sumAllcpc", sumAllCost.multiply(new BigDecimal("100")).divide(new BigDecimal(sumAllClicks), 2,RoundingMode.HALF_UP));
		} else {
			resultMap.put("sumAllcr", 0);
		}
		if (sumAllSales.compareTo(new BigDecimal("0")) != 0) {
			resultMap.put("sumAllacos", sumAllCost.multiply(new BigDecimal("100")).divide(sumAllSales, 2,RoundingMode.HALF_UP));
		} else {
			resultMap.put("sumAllacos", 0);
		}
		///////////////////////////// 第二次加载
		Map<String, Object> allsale = orderMonthsSummaryAll(param);
		if (allsale != null && allsale.containsKey("listLabel")) {
			List<Object> lebels = (List<Object>) allsale.get("listLabel");
			List<Object> allsalelist = (List<Object>) allsale.get("listData");
			List<Object> salelist = (List<Object>) attributedSalesMap.get("listData");
			List<Object> costlist = (List<Object>) costMap.get("listData");
			List<Object> skulist = (List<Object>) skuNumMap.get("listData");
			List<Object> asinlist = (List<Object>) asinNumMap.get("listData");
			List<Object> acoaslist = new ArrayList<Object>();
			List<Object> salepalllist = new ArrayList<Object>();
			List<Object> asinperlist = new ArrayList<Object>();
			///////////////////////////// 第二次汇总
			for (int i = 0; i < lebels.size(); i++) {
				BigDecimal cost = new BigDecimal(costlist.get(i).toString());
				BigDecimal sale = new BigDecimal(salelist.get(i).toString());
				BigDecimal all = new BigDecimal(allsalelist.get(i).toString());
				Integer sku = new BigDecimal(skulist.get(i).toString()).intValue();
				Integer asin = new BigDecimal(asinlist.get(i).toString()).intValue();
				sumAllall = sumAllall.add(all);
				if (all.compareTo(new BigDecimal("0")) != 0) {
					acoaslist.add(cost.multiply(new BigDecimal("100")).divide(all, 2,RoundingMode.HALF_UP));
					salepalllist.add(sale.multiply(new BigDecimal("100")).divide(all, 2,RoundingMode.HALF_UP));
				} else {
					acoaslist.add(0);
					salepalllist.add(0);
				}
				if (sku != 0) {
					asinperlist.add(asin * 100.0 / sku);
				} else {
					asinperlist.add(0);
				}
			}
			asinPerMap.put("listLabel", lebels);
			asinPerMap.put("listData", asinperlist);
			acoas.put("listLabel", lebels);
			acoas.put("listData", acoaslist);
			salespall.put("listLabel", lebels);
			salespall.put("listData", salepalllist);
		}

		///////////////////////////// 第二次存储结果
		resultMap.put("sumAllallsale", sumAllall);
		if (sumAllall.compareTo(new BigDecimal("0")) != 0) {
			resultMap.put("sumAllsalespall", sumAllSales.multiply(new BigDecimal("100")).divide(sumAllall, 2,RoundingMode.HALF_UP));
			resultMap.put("sumAllacoas", sumAllCost.multiply(new BigDecimal("100")).divide(sumAllall, 2,RoundingMode.HALF_UP));
		} else {
			resultMap.put("sumAllsalespall", 0);
			resultMap.put("sumAllacoas", 0);
		}
		if (sumAllSkuNum != 0) {
			resultMap.put("sumAllasinPer", sumAllAsinNum * 100.00 / sumAllSkuNum);
		} else {
			resultMap.put("sumAllasinPer", 0);
		}
		resultMap.put("asinPer", asinPerMap);
		resultMap.put("salespall", salespall);
		resultMap.put("acoas", acoas);
		resultMap.put("allsale", allsale);
		return resultMap;
	}

	public BigDecimal orderSummaryAll(Map<String, Object> param) {
		try {
			 return amazonClientOneFeignManager.orderSummaryAll(param);
		}catch(Exception e) {
			e.printStackTrace();
			 return new BigDecimal("0");
		}
             
	}

	public Map<String, Object> orderMonthsSummaryAll(Map<String, Object> param) {
		try {
			return amazonClientOneFeignManager.orderMonthsSummaryAll(param);
		}catch(Exception e) {
			e.printStackTrace();
			 return new HashMap<String,Object>();
		}
		
	}

	public Map<String, Object> orderDaysSummaryAll(Map<String, Object> param) {
		try {
			return amazonClientOneFeignManager.orderDaysSummaryAll(param);
		}catch(Exception e) {
			e.printStackTrace();
			 return new HashMap<String,Object>();
		}
		
		
	}
}
