package com.wimoor.amazon.finances.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.finances.mapper.AmzSettlementAccReportMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccReport;
import com.wimoor.amazon.finances.service.IAmzSettlementAccReportService;
import com.wimoor.common.GeneralUtil;

@Service
public class AmzSettlementAccReportServiceImpl extends ServiceImpl<AmzSettlementAccReportMapper, AmzSettlementAccReport> implements IAmzSettlementAccReportService {
    @Autowired
    IExchangeRateHandlerService exchangeRateHandlerService;
	@Override
	public IPage<Map<String, Object>> findSettlementAcc(Page<Object> page, Map<String, Object> map) {
		// TODO Auto-generated method stub
		String tocurrency = "RMB";
		String shopid = map.get("shopid").toString();
		if (map.get("currency") != null) {
			tocurrency = map.get("currency").toString();
		}
		Page<Map<String, Object>> list = this.baseMapper.findSettlementAcc(page, map);
		if(list!=null&&list.getRecords()!=null&&list.getRecords().size()>0) {
			for(Map<String, Object> item:list.getRecords()) {
				if (item.get("currency") != null) {
					String fromCurrency = item.get("currency").toString();
					initAmount(shopid, item, fromCurrency, tocurrency);
				}
			}
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> findSettlementAcc(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = this.baseMapper.findSettlementAcc(map);
		return list;
	}
	
	@Override
	public Map<String, Object> findSettlementAccSum(Map<String, Object> map) {
		List<Map<String, Object>> list = this.baseMapper.findSettlementAccSum(map);
		String tocurrency = "RMB";
		String shopid = map.get("shopid").toString();
		if (map.get("currency") != null) {
			tocurrency = map.get("currency").toString();
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("currency", tocurrency);
		if (list.size() > 0 && list != null) {
			BigDecimal totalamount = new BigDecimal("0");
			for (Map<String, Object> item : list) {
				if (item.get("currency") != null) {
					String fromCurrency = item.get("currency").toString();
					initAmount(shopid, item, fromCurrency, tocurrency);
					BigDecimal rmb = new BigDecimal("0");
					if (item.get("totalAmount_to") != null) {
						rmb = new BigDecimal(item.get("totalAmount_to").toString());
						totalamount = totalamount.add(rmb);
					}
				}
			}
			result.put("acctotalsummary",totalamount);
		}
		return result;
	}
	
	public void initAmount(String shopid, Map<String, Object> item, String currency, String tocurrency) {
		if (item != null && item.get("totalAmount") != null) {
			BigDecimal amount = (BigDecimal) item.get("totalAmount");
			amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, currency, tocurrency, amount);
			item.put("totalAmount_to", amount.setScale(2,RoundingMode.HALF_UP));
		}
	}
	
 	public HashMap<String, HashMap<String, Object>> loadSummayAccMaps(Map<String, Object> param) {
 		String tocurrency = (String) param.get("currency");
 		String shopid = (String) param.get("shopid");
 		String fromDate = param.get("fromDate").toString().substring(2, 7);
 		HashMap<String, HashMap<String, Object>> result = new HashMap<String, HashMap<String, Object>>();
 		Map<String, BigDecimal> echangeRateMap = exchangeRateHandlerService.currencyChangeRateByLocal(shopid, tocurrency);
 		param.put("currencyrate", echangeRateMap);
 		List<Map<String,Object>> list=this.baseMapper.sumSettlementAcc(param);
 		for(Map<String,Object> item:list) {
 			String posted_date = GeneralUtil.getStr(item.get("deposit_date"));
 			addMap(result, item,"accountconverted_total",posted_date, fromDate);
 		}
 		return result;
 	}
 	
 	private void addMap(HashMap<String, HashMap<String, Object>> result, Map<String, Object> item, String column, String posted_date, String fromDate ) {
 		Object object = item.get(column);
 		BigDecimal value = new BigDecimal("0");
 		if (object != null) {
 			value = (BigDecimal) object;
 		}
 		BigDecimal currencyValue =  value;
 		item.put(column, currencyValue);
 		HashMap<String, Object> colmap = result.get(column);
 		if (colmap == null) {
 			colmap = new HashMap<String, Object>();
 		}
 		Object colvalue = colmap.get(posted_date);
 		BigDecimal oldValue = new BigDecimal("0");
 		if (colvalue != null) {
 			oldValue = (BigDecimal) colvalue;
 		}
 		colmap.put(posted_date, oldValue.add(currencyValue));
 		Object colall = colmap.get("all");
 		BigDecimal allValue = new BigDecimal("0");
 		if (colall != null) {
 			allValue = (BigDecimal) colall;
 		}
 		if (!posted_date.equals(fromDate)) {
 			colmap.put("all", allValue.add(currencyValue));
 		}
 		result.put(column, colmap);
 	}
 
 	
 	public AmzSettlementAccReport findSettlementByKey(String settlement_id, String amazonauthid, String marketplace_name) {
 		LambdaQueryWrapper<AmzSettlementAccReport> query=new LambdaQueryWrapper<AmzSettlementAccReport> ();
 		query.eq(AmzSettlementAccReport::getSettlementId, settlement_id);
 		query.eq(AmzSettlementAccReport::getAmazonauthid, amazonauthid);
 		query.eq(AmzSettlementAccReport::getMarketplaceName, marketplace_name);
 		return this.getOne(query);
 	 }

	@Override
	public List<Map<String, Object>> findDateByAuth(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.baseMapper.findDateByAuth(map);
	}
 
}

