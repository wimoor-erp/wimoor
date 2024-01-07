package com.wimoor.amazon.finances.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.mapper.AmzAmountDescriptionMapper;
import com.wimoor.amazon.common.pojo.entity.AmzAmountDescription;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.finances.mapper.AmzSettlementAccStatementMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportSummaryDayMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportSummaryMonthMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementSummarySkuMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzFinSettlementFormula;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccStatement;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementReport;
import com.wimoor.amazon.finances.service.IAmzFinSettlementFormulaService;
import com.wimoor.amazon.finances.service.IAmzSettlementAccReportService;
import com.wimoor.amazon.finances.service.IAmzSettlementAccStatementService;
import com.wimoor.amazon.finances.service.IAmzSettlementReportService;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransMapper;
import com.wimoor.amazon.product.service.IProductInOptService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.profit.service.ICalculateProfitService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AmzSettlementReportServiceImpl extends ServiceImpl<AmzSettlementReportMapper, AmzSettlementReport> implements IAmzSettlementReportService {
 	@Resource
 	AmzAmountDescriptionMapper amzAmountDescriptionMapper;
 
 	@Resource
 	IAmzFinSettlementFormulaService iAmzFinSettlementFormulaService;
 	
 	@Resource
 	AmzSettlementSummarySkuMapper amzSettlementSummarySkuMapper;
 	@Resource
 	AmzSettlementReportSummaryMonthMapper amzSettlementReportSummaryMonthMapper;
 	@Resource
 	AmzSettlementReportSummaryDayMapper amzSettlementReportSummaryDayMapper;
 	
 	@Autowired
 	@Lazy
 	IAmzSettlementAccReportService iAmzSettlementAccReportService;
 	
    @Resource
    IMarketplaceService marketplaceService;
    final IAmazonAuthorityService amazonAuthorityService;
    final IExchangeRateHandlerService exchangeRateHandlerService;
    final IAmazonGroupService amazonGroupService;
  
    final AmzSettlementAccStatementMapper amzSettlementAccStatementMapper;
    final IProductInfoService iProductInfoService;
    final ICalculateProfitService iCalculateProfitService;
    final ShipInboundTransMapper shipInboundTransMapper;
    final IProductInOptService iProductInOptService;
    final ErpClientOneFeignManager erpClientOneFeign ; 
 
    final IAmzSettlementAccStatementService iAmzSettlementAccStatementService;
 	AmzSettlementReport findOldOne(AmazonAuthority amazonAuthority, AmzSettlementReport report) {
 		LambdaQueryWrapper<AmzSettlementReport> queryWrapper = new LambdaQueryWrapper<AmzSettlementReport>();
 		queryWrapper.eq(AmzSettlementReport::getPostedDate, report.getPostedDate());
 		queryWrapper.eq(AmzSettlementReport::getSettlementId, report.getSettlementId());
 		queryWrapper.eq(AmzSettlementReport::getOrderId, report.getOrderId());
 		queryWrapper.eq(AmzSettlementReport::getMarketplaceName, report.getMarketplaceName());
 		queryWrapper.eq(AmzSettlementReport::getAmountType, report.getAmountType());
 		queryWrapper.eq(AmzSettlementReport::getAmountDescription, report.getAmountDescription());
 		queryWrapper.eq(AmzSettlementReport::getOrderItemCode, report.getAmountDescription());
 		queryWrapper.eq(AmzSettlementReport::getSku, report.getSku());
 		queryWrapper.eq(AmzSettlementReport::getAmazonAuthId, amazonAuthority.getId());
 		List<AmzSettlementReport> list = this.baseMapper.selectList(queryWrapper);
 		if (list.size() > 0) {
 			return list.get(0);
 		}
 		return null;
 	}
 
 	public HashMap<String, HashMap<String, Object>> getAmzSummaryMonth(Map<String, Object> param) {
 		List<Map<String, Object>> resultItem = amzSettlementReportSummaryMonthMapper.settlementitem(param);
 		String tocurrency = (String) param.get("currency");
 		String shopid = (String) param.get("shopid");
 		HashMap<String, HashMap<String, Object>> result = new HashMap<String, HashMap<String, Object>>();
 		String fromDate = param.get("fromDate").toString().substring(2, 7);
 		for (Map<String, Object> item : resultItem) {
 			String posted_date = GeneralUtil.getStr(item.get("posted_date"));
 			String currency = GeneralUtil.getStr(item.get("currency"));
 			addMap(shopid, result, item, "in_principal", posted_date, fromDate, currency, tocurrency);
 			addMap(shopid, result, item, "in_other", posted_date, fromDate, currency, tocurrency);
 			addMap(shopid, result, item, "out_adv", posted_date, fromDate, currency, tocurrency);
 			addMap(shopid, result, item, "out_commission", posted_date, fromDate, currency, tocurrency);
 			addMap(shopid, result, item, "out_fba", posted_date, fromDate, currency, tocurrency);
 			addMap(shopid, result, item, "out_refund", posted_date, fromDate, currency, tocurrency);
 			addMap(shopid, result, item, "out_other", posted_date, fromDate, currency, tocurrency);
 			addMap(shopid, result, item, "converted_total", posted_date, fromDate, currency, tocurrency);
 			addFieldMap(result, item, "in_sum", posted_date, "in_other,in_principal");
 			addFieldMap(result, item, "out_sum", posted_date, "out_adv,out_commission,out_fba,out_refund,out_other");
 		}
 		sumFieldMap(result, "in_sum", fromDate);
 		sumFieldMap(result, "out_sum", fromDate);
 		convartRate(result, "in_principal");
 		convartRate(result, "out_adv", "in_principal");
 		return result;
 	}
 
 	private void sumFieldMap(HashMap<String, HashMap<String, Object>> result, String field, String fromDate) {
 		HashMap<String, Object> in_sum = result.get(field);
 		if (in_sum == null) {
 			return;
 		}
 		BigDecimal sumvalue = new BigDecimal("0");
 		for (Entry<String, Object> entry : in_sum.entrySet()) {
 			if (!fromDate.equals(entry.getKey())) {
 				BigDecimal allValue = new BigDecimal("0");
 				allValue = (BigDecimal) entry.getValue();
 				sumvalue = sumvalue.add(allValue);
 			}
 		}
 		in_sum.put("all", sumvalue);
 	}
 
 	private void convartRate(HashMap<String, HashMap<String, Object>> result, String key, String me) {
 		HashMap<String, Object> item = result.get(key);
 		HashMap<String, Object> itemMe = result.get(me);
 		HashMap<String, Object> itemRate = result.get(key + "Rate");
 		if (itemRate == null) {
 			itemRate = new HashMap<String, Object>();
 		}
 		if (item != null && itemMe != null) {
 			for (Entry<String, Object> entry : item.entrySet()) {
 				String posted_date = entry.getKey();
 				if (posted_date != null) {
 					BigDecimal value = (BigDecimal) entry.getValue();
 					Object oldv = itemMe.get(posted_date);
 					BigDecimal oldValue = new BigDecimal("0");
 					if (oldv != null) {
 						oldValue = (BigDecimal) oldv;
 					}
 					if (oldValue.compareTo(new BigDecimal("0")) == 0) {
 						value = new BigDecimal("0");
 					} else {
 						value = value.abs().divide(oldValue.abs(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
 					}
 					itemRate.put(posted_date, value);
 				}
 			}
 		}
 		result.put(key + "Rate", itemRate);
 	}
 
 	private void convartRate(HashMap<String, HashMap<String, Object>> result, String key) {
 		HashMap<String, Object> item = result.get(key);
 		HashMap<String, Object> itemRate = result.get(key + "Rate");
 		if (itemRate == null) {
 			itemRate = new HashMap<String, Object>();
 		}
 		if (item != null) {
 			for (Entry<String, Object> entry : item.entrySet()) {
 				String posted_date = entry.getKey();
 				BigDecimal value = (BigDecimal) entry.getValue();
 				if ("all".equals(posted_date)) {
 					continue;
 				}
 				String[] datear = posted_date.split("-");
 				Integer year = Integer.valueOf(datear[0]);
 				Integer month = Integer.valueOf((datear[1]));
 				if (month > 1) {
 					month = month - 1;
 				} else {
 					month = 12;
 					year = year - 1;
 				}
 				String monthstr = month.toString();
 				if (month < 10) {
 					monthstr = "0" + monthstr;
 				}
 				String date = year + "-" + monthstr;
 				Object oldv = item.get(date);
 				BigDecimal oldValue = new BigDecimal("0");
 				if (oldv != null) {
 					oldValue = (BigDecimal) oldv;
 				}
 				if (oldValue.compareTo(new BigDecimal("0")) == 0) {
 					value = new BigDecimal("0");
 				} else {
 					value = value.subtract(oldValue).divide(oldValue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
 				}
 				itemRate.put(posted_date, value);
 			}
 			result.put(key + "Rate", itemRate);
 		}
 	}
 
 	private void addMap(String shopid, HashMap<String, HashMap<String, Object>> result, Map<String, Object> item,
 			String column, String posted_date, String fromDate, String fromcurrency, String tocurrency) {
 		Object object = item.get(column);
 		BigDecimal value = new BigDecimal("0");
 		if (object != null) {
 			value = (BigDecimal) object;
 		}
 		BigDecimal currencyValue = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, value);
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

 	private void addFieldMap(HashMap<String, HashMap<String, Object>> result, Map<String, Object> item, String column,
 			String posted_date, String fields) {
 		HashMap<String, Object> colmap = result.get(column);
 		if (colmap == null) {
 			colmap = new HashMap<String, Object>();
 		}
 		String[] fieldarray = fields.split(",");
 		BigDecimal sumvalue = new BigDecimal("0");
 		for (String field : fieldarray) {
 			HashMap<String, Object> distMap = result.get(field);
 			Object colvalue = distMap.get(posted_date);
 			if (colvalue != null) {
 				BigDecimal oldColvalue = (BigDecimal) colvalue;
 				sumvalue = sumvalue.add(oldColvalue);
 			}
 		}
 		colmap.put(posted_date, sumvalue);
 		result.put(column, colmap);
 	}
 
 	private void addMap(String shopid, HashMap<String, HashMap<String, Object>> result, Map<String, Object> item,
 			String column, String posted_date,String fromcurrency, String tocurrency) {
 		Object object = item.get("amount");
 		BigDecimal value = new BigDecimal("0");
 		if (object != null) {
 			value = (BigDecimal) object;
 		}
 		BigDecimal currencyValue = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, value);
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
 		colmap.put("all", allValue.add(currencyValue));
 		result.put(column, colmap);
 	}
 
 	public HashMap<String, HashMap<String, Object>> getLacolSummaryMonth(Map<String, Object> param) {
 		String tocurrency = (String) param.get("currency");
 		String shopid = (String) param.get("shopid");
 		String groupid=null;
 		String marketplaceid=null;
 		if(param.get("groupid")!=null)groupid=param.get("groupid").toString();
 		if(param.get("marketplaceid")!=null)marketplaceid=param.get("marketplaceid").toString();
 		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
 		if(auth!=null) {
 			param.put("amazonAuthId", auth.getId());
 			param.put("amazonauthid", auth.getId());
 		}
 		HashMap<String, HashMap<String, Object>> result = new HashMap<String, HashMap<String, Object>>();
 		List<Map<String, Object>> resultLocal = amzSettlementReportSummaryMonthMapper.localitem(param);
 		List<Map<String, Object>> resultLocal2 = shipInboundTransMapper.localitem(param);
 		if(resultLocal2 != null && resultLocal2.size() > 0) {
 			resultLocal.addAll(resultLocal2);
 		}
 		
 		String fromDate = param.get("fromDate").toString().substring(2, 7);
 		for (Map<String, Object> item : resultLocal) {
 			String name = GeneralUtil.getStr(item.get("name"));
 			String posted_date = GeneralUtil.getStr(item.get("posted_date"));
 			if (posted_date.equals(fromDate)) {
 				continue;
 			}
 			addMap(shopid, result, item, name, posted_date, "RMB", tocurrency);
 		}
 		return result;
 	}
 
 	public Map<String, Object> getSummaryAll(Map<String, Object> param) {
 		String toCurrency = (String) param.get("currency");
 		String shopid = (String) param.get("shopid");
 		this.setParamsSettlementDate(param);
 		Map<String,BigDecimal> map=exchangeRateHandlerService.currencyChangeRateByLocal(shopid,toCurrency);
 		param.put("currencyrate", map);
 		Map<String, Object> result = amzSettlementReportSummaryDayMapper.summaryall(param);
 		Map<String, Object> result2 = amzSettlementReportSummaryDayMapper.summaryReturnAll(param);
 		if (result == null) {
 			result = result2;
 		} else {
 			if (result2 != null) {
 				result.putAll(result2);
 			}
 		}
 		List<Map<String, Object>> localresult = amzSettlementReportSummaryDayMapper.summaryallp(param);
 		BigDecimal allprincipal = new BigDecimal("0");
 		if (localresult.size() > 0 && localresult != null) {
 			for (Map<String, Object> item : localresult) {
 				if(item!=null&&item.get("allprincipal")!=null) {
 					BigDecimal itemprincipal = (BigDecimal) item.get("allprincipal");
 					if (itemprincipal != null) {
 						allprincipal = allprincipal.add(itemprincipal);
 					}
 				}
 				
 			}
 		} else {
 			allprincipal = new BigDecimal("0");
 		}
 		if (result != null) {
 			result.put("allprincipal", allprincipal);
 		}
 		return result;
 	}
 
 	public Map<String, Object> getSummarySKU(Map<String, Object> param) {
 		String tocurrency = (String) param.get("currency");
 		String shopid = (String) param.get("shopid");
 		Map<String,BigDecimal> map=exchangeRateHandlerService.currencyChangeRateByLocal(shopid,tocurrency);
 		param.put("currencyrate", map);
 		List<Map<String, Object>> list = amzSettlementReportSummaryDayMapper.summaryday(param);
 		Map<String, Object> result = new HashMap<String, Object>();
 		String startTime = param.get("fromDate").toString();
 		String endTime = param.get("endDate").toString();
 		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
 		Date date1 = null;
 		Date date2 = null;
 		try {
 			date1 = format.parse(startTime);
 			date2 = format.parse(endTime);
 		} catch (ParseException e) {
 			e.printStackTrace();
 		}
 		int daysize = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 		Calendar c = Calendar.getInstance();
 		c.setTime(GeneralUtil.getDatez(startTime));
 		for (int i = 1; i <= daysize + 1; i++, c.add(Calendar.DATE, 1)) {
 			String tempkey = sdf.format(c.getTime());
 			result.put(tempkey, new BigDecimal("0"));
 		}
 		for (Map<String, Object> item : list) {
 			String key = item.get("posted_date").toString();
 			if (item.get("amount") != null) {
 				BigDecimal amount = (BigDecimal) item.get("amount");
 				result.put(key, amount.setScale(2, RoundingMode.HALF_UP));
 			} else {
 				result.put(key, 0);
 			}
 		}
 		return result;
 	}
 
 	public Map<String, Object> getSumNumsByDay(Map<String, Object> param, String charttype) {
 		String type = "";
 		Map<String, Object> maps = new HashMap<String, Object>();
 		this.setParamsSettlementDate(param);
 		List<Map<String, Object>> listMap = null;
 		if ("sales".equals(charttype)) {
 			type = "sales";
 			listMap = amzSettlementReportSummaryDayMapper.sumNumsByDay(param);
 		} else if ("refund".equals(charttype)) {
 			type = "refundnum";
 			listMap = amzSettlementReportSummaryDayMapper.sumReturnNumsByDay(param);
 		} else if ("order".equals(charttype)) {
 			type = "order_amount";
 			listMap = amzSettlementReportSummaryDayMapper.sumNumsByDay(param);
 		} else if ("principal".equals(charttype)) {
 			Map<String, Object> map = this.getSummarySKU(param);
 			listMap = new ArrayList<Map<String, Object>>();
 			for (Entry<String, Object> entry : map.entrySet()) {
 				Map<String, Object> item = new HashMap<String, Object>();
 				item.put("postdate", entry.getKey());
 				item.put(type, entry.getValue());
 				listMap.add(item);
 			}
 		}
 		Map<String, Object> result = new HashMap<String, Object>();
 		String startTime = param.get("fromDate").toString();
 		String endTime = param.get("endDate").toString();
 		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
 		Date date1 = null;
 		Date date2 = null;
 		try {
 			date1 = format.parse(startTime);
 			date2 = format.parse(endTime);
 		} catch (ParseException e) {
 			e.printStackTrace();
 		}
 		int daysize = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 		Calendar c = Calendar.getInstance();
 		c.setTime(GeneralUtil.getDatez(startTime));
 		for (int i = 1; i <= daysize + 1; i++, c.add(Calendar.DATE, 1)) {
 			String tempkey = sdf.format(c.getTime());
 			result.put(tempkey, 0);
 		}
 		for (Map<String, Object> item : listMap) {
 			if (item.get(type) != null) {
 				String key = item.get("postdate").toString();
 				String amount = item.get(type).toString();
 				if (amount.indexOf(".") >= 0) {
 					int index = amount.indexOf(".");
 					String newamount = amount.substring(0, index);
 					result.put(key, Integer.parseInt(newamount));
 				} else {
 					result.put(key, Integer.parseInt(amount));
 				}
 			} else {
 				continue;
 			}
 		}
 		List<String> labels = new ArrayList<String>();
 		List<Object> datas = new ArrayList<Object>();
 		SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd");
 		Date datestart = null;
 		Date dateend = null;
 		try {
 			dateend = format.parse(endTime);
 			datestart = format.parse(startTime);
 		} catch (ParseException e) {
 			e.printStackTrace();
 		}
 		int daysize2 = (int) ((dateend.getTime() - datestart.getTime()) / (1000 * 3600 * 24));
 		Calendar c2 = Calendar.getInstance();
 		c2.setTime(GeneralUtil.getDatez(startTime));
 		for (int i = 0; i <= daysize2; i++, c2.add(Calendar.DATE, 1)) {
 			String tempkey = format2.format(c2.getTime());
 			labels.add(i, tempkey);
 			datas.add(i, result.get(tempkey.replaceAll("/", "-")));
 		}
 		maps.put("datas", datas);
 		maps.put("labels", labels);
 		return maps;
 	}
 

 
 	public Map<String, Object> loadSummayMonthMaps(HashMap<String, HashMap<String, Object>> map,
 			HashMap<String, HashMap<String, Object>> map2, List<String> times) {
 		Map<String, Object> maps = new HashMap<String, Object>();
 		eachMaps("converted_total", map, times);
 		eachMaps("accountconverted_total", map, times);
 		eachMaps("in_principal", map, times);
 		eachMaps("in_other", map, times);
 		eachMaps("in_sum", map, times);
 		  eachMaps("original_total",map,times);
 		eachMaps("out_adv", map, times);
 		eachMaps("out_fba", map, times);
 		eachMaps("out_commission", map, times);
 		eachMaps("out_other", map, times);
 		eachMaps("out_sum", map, times);
 		maps.put("Maps", map);
 		List<String> localList = new ArrayList<String>();
 		for (String item : map2.keySet()) {
 			if (item != null && item != "") {
 				localList.add(item);
 			}
 		}
 		maps.put("localName", localList);
 		maps.put("localMaps", map2);
 		return maps;
 	}
 
 	public void eachMaps(String ftype, HashMap<String, HashMap<String, Object>> map, List<String> times) {
 		HashMap<String, Object> maplist = map.get(ftype);
 		if (maplist != null) {
 			for (int i = 0; i < times.size(); i++) {
 				if (maplist.get(times.get(i)) == null) {
 					maplist.put(times.get(i), 0);
 				}
 			}
 			if (maplist.get("all") == null) {
 				maplist.put("all", 0);
 			}
 		}
 	}
 
 	public List<String> initTimes(String endTime1, String startTime1) {
 		List<String> times = new ArrayList<String>();
 		String endTime = endTime1.substring(2, 7);
 		String startTime = startTime1.substring(2, startTime1.length());
 		String startyear = startTime.split("-")[0];
 		int startMonth = Integer.parseInt(startTime.split("-")[1]);
 		String endyear = endTime.split("-")[0];
 		int endMonth = Integer.parseInt(endTime.split("-")[1]);
 		if (startTime.equals(endTime)) {
 			times.add(startTime);
 			return times;
 		}
 		if (startyear.equals(endyear)) {
 			for (Integer i = startMonth; i < (endMonth + 1); i++) {
 				if (i < 10) {
 					times.add(startyear + "-0" + i.toString());
 				} else {
 					times.add(startyear + "-" + i.toString());
 				}
 			}
 		} else {
 			for (Integer i = startMonth; i < 13; i++) {
 				if (i < 10) {
 					times.add(startyear + "-0" + i.toString());
 				} else {
 					times.add(startyear + "-" + i.toString());
 				}
 			}
 			for (Integer i = 1; i < (endMonth + 1); i++) {
 				if (i < 10) {
 					times.add(endyear + "-0" + i.toString());
 				} else {
 					times.add(endyear + "-" + i.toString());
 				}
 			}
 		}
 		return times;
 	}
 
 	public List<Map<String, Object>> selectOtherin(Map<String, Object> param) {
 		String fromdate = param.get("fromDate").toString().replaceAll("/", "-");
 		String enddate = param.get("endDate").toString().replaceAll("/", "-");
 		String shopid = param.get("shopid").toString();
 		param.put("fromDate", fromdate);
 		param.put("endDate", enddate);
 		this.setParamsSettlementDate(param);
 		List<Map<String, Object>> list = amzSettlementReportSummaryMonthMapper.selectOtherin(param);
 		List<AmzAmountDescription> deslist = amzAmountDescriptionMapper.selectList(null);
 		Map<String, String> descName = new HashMap<String, String>();
 		for (AmzAmountDescription item : deslist) {
 			descName.put(item.getEname(), item.getCname());
 		}
 		if (list != null && list.size() > 0) {
 			for (int i = 0; i < list.size(); i++) {
 				Map<String, Object> mapitem = list.get(i);
 				String currency = mapitem.get("currency").toString();
 				String tocurrency = param.get("currency").toString();
 				BigDecimal amount = (BigDecimal) mapitem.get("amount");
 				amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, currency, tocurrency, amount);
 				mapitem.put("amount", amount.setScale(2, RoundingMode.HALF_UP));
 				if (descName.containsKey(mapitem.get("amounttype"))) {
 					mapitem.put("camounttype", descName.get(mapitem.get("amounttype")));
 				}
 			}
 			List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
 			//  再累加
 			for (int i = 0; i < list.size(); i++) {
 				String ftype = list.get(i).get("amounttype").toString();
 				BigDecimal amount = (BigDecimal) list.get(i).get("amount");
 				if (i == 0) {
 					maps.add(list.get(i));
 				} else {
 					if (ftype.equals(list.get(i - 1).get("amounttype").toString())) {
 						BigDecimal oldamount = (BigDecimal) list.get(i - 1).get("amount");
 						amount = amount.add(oldamount);
 						list.get(i).put("amount", amount);
 						maps.remove(list.get(i - 1));
 						maps.add(list.get(i));
 					} else {
 						maps.add(list.get(i));
 					}
 				}
 			}
 			return maps;
 		} else {
 			return null;
 		}
 	}
 
 	public List<Map<String, Object>> selectOtherout(Map<String, Object> param) {
 		String fromdate = param.get("fromDate").toString().replaceAll("/", "-");
 		String enddate = param.get("endDate").toString().replaceAll("/", "-");
 		String shopid = param.get("shopid").toString();
 		param.put("fromDate", fromdate);
 		param.put("endDate", enddate);
 		this.setParamsSettlementDate(param);
 		List<Map<String, Object>> list = amzSettlementReportSummaryMonthMapper.selectOtherout(param);
 		List<AmzAmountDescription> deslist = amzAmountDescriptionMapper.selectList(null);
 		Map<String, String> descName = new HashMap<String, String>();
 		for (AmzAmountDescription item : deslist) {
 			descName.put(item.getEname(), item.getCname());
 		}
 		if (list != null && list.size() > 0) {
 			for (int i = 0; i < list.size(); i++) {
 				Map<String, Object> mapitem = list.get(i);
 				String currency = mapitem.get("currency").toString();
 				String tocurrency = param.get("currency").toString();
 				BigDecimal amount = (BigDecimal) mapitem.get("amount");
 				amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, currency, tocurrency, amount);
 				mapitem.put("amount", amount.setScale(2, RoundingMode.HALF_UP));
 				if (descName.containsKey(mapitem.get("amounttype"))) {
 					mapitem.put("camounttype", descName.get(mapitem.get("amounttype")));
 				}
 			}
 			List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
 			 // 再累加
 			for (int i = 0; i < list.size(); i++) {
 				String ftype = list.get(i).get("amounttype").toString();
 				BigDecimal amount = (BigDecimal) list.get(i).get("amount");
 				if (i == 0) {
 					maps.add(list.get(i));
 				} else {
 					if (ftype.equals(list.get(i - 1).get("amounttype").toString())) {
 						BigDecimal oldamount = (BigDecimal) list.get(i - 1).get("amount");
 						amount = amount.add(oldamount);
 						list.get(i).put("amount", amount);
 						maps.remove(list.get(i - 1));
 						maps.add(list.get(i));
 					} else {
 						maps.add(list.get(i));
 					}
 				}
 			}
 			return maps;
 		} else {
 			return null;
 		}
 	}
 
 	
  
 	public void initAmount(String shopid, Map<String, Object> item, String currency, String tocurrency) {
 		if (item != null && item.get("totalAmount") != null) {
 			BigDecimal amount = (BigDecimal) item.get("totalAmount");
 			amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, currency, tocurrency, amount);
 			item.put("totalAmount_to", amount.setScale(2, RoundingMode.HALF_UP));
 		}
 	}
 
 	public int downLoadSettlementRpt(Sheet sheet ,Map<String, Object> parameter,int line) throws BizException {
 		this.setParamsSettlementDate(parameter);
 		List<Map<String, Object>> list = this.baseMapper.findBySettlementAcc(parameter);
 		if (list != null) {
 			String[] titlemap = { "groupname","settlement","settlement_id", "currency", "transaction_type", "order_id", "merchant_order_id",
 					"adjustment_id", "shipment_id", "marketplace_name", "amount_type", "amount_description", "amount",
 					"fulfillment_id", "posted_date", "posted_date_time", "order_item_code", "merchant_order_item_id",
 					"merchant_adjustment_item_id", "sku", "quantity_purchased", "promotion_id" };
 			for (int i = 0; i < list.size(); i++) {
 				Row row = sheet.createRow(i+line + 1);
 				Map<String, Object> map = list.get(i);
 				for (int j = 0; j < titlemap.length; j++) {
 					Cell cell = row.createCell(j);   //在索引0的位置创建单元格(左上端)
                    if(j==1) {
                    	cell.setCellValue(parameter.get("settlement").toString());
                    }else {
                    	Object value = map.get(titlemap[j]);
     					if (value != null) {
     						cell.setCellValue(value.toString());
     					}
                    }
 					
 				}
 			}
 		} else {
 			throw new BizException("没有数据可导出！");
 		}
 		return list.size();
 	}
 	
 	public SXSSFWorkbook downLoadSettlementRpt(Map<String, Object> parameter) throws BizException {
 		SXSSFWorkbook workbook = new SXSSFWorkbook();
 		this.setParamsSettlementDate(parameter);
 		List<Map<String, Object>> list = this.baseMapper.findBySettlementAcc(parameter);
 		if (list != null&&list.size()>0) {
 			Sheet sheet = workbook.createSheet("sheet1");
 			Row trow = sheet.createRow(0);
 			Map<String, Object> map = list.get(0);
 			 // 在索引0的位置创建行（最顶端的行）
 			String[] titlemap = {"settlement_id", "currency", "transaction_type", "order_id", "merchant_order_id",
 					"adjustment_id", "shipment_id", "marketplace_name", "amount_type", "amount_description", "amount",
 					"fulfillment_id", "posted_date", "posted_date_time", "order_item_code", "merchant_order_item_id",
 					"merchant_adjustment_item_id", "sku", "quantity_purchased", "promotion_id" };
 			for (int j = 0; j < titlemap.length; j++) {
 				Cell cell = trow.createCell(j);  // 在索引0的位置创建单元格(左上端)
 				cell.setCellValue(titlemap[j]);
 			}
 			for (int i = 0; i < list.size(); i++) {
 				Row row = sheet.createRow(i + 1);
 				map = list.get(i);
 				for (int j = 0; j < titlemap.length; j++) {
 					Cell cell = row.createCell(j);   //在索引0的位置创建单元格(左上端)
 					Object value = map.get(titlemap[j]);
 					if (value != null) {
 						cell.setCellValue(value.toString());
 					}
 				}
 			}
 		} else {
 			throw new BizException("没有数据可导出！");
 		}
 		return workbook;
 	}
 	
 	
 	List<Map<String,Object>> putItem(Map<String, Object> map,Map<String, Object> item,String key) {
 		@SuppressWarnings("unchecked")
		List<Map<String,Object>> itemlist=map.get(key)!=null?(List<Map<String, Object>>) map.get(key):new LinkedList<Map<String,Object>>();
		itemlist.add(item);
		map.put("item", itemlist);
		return itemlist;
 	}
 	@SuppressWarnings("unchecked")
	public Map<String, Object> getDetailSettment(Map<String, Object> param) {
 		this.setParamsSettlementDate(param);
 		List<Map<String, Object>> list = null;
        list = this.baseMapper.getDetailSettment(param);
 		List<AmzAmountDescription> deslist = amzAmountDescriptionMapper.selectList(null);
 		Map<String, String> descName = new HashMap<String, String>();
 		for (AmzAmountDescription item : deslist) {
 			descName.put(item.getEname(), item.getCname());
 		}
 		String currency = param.get("currency") != null&&!"undefined".equals(param.get("currency").toString()) ? param.get("currency").toString() : "RMB";
 		String shopid = param.get("shopid") != null ? param.get("shopid").toString() : null;
 		Map<String, Object> detail = new HashMap<String, Object>();
 		BigDecimal totalsummary=new BigDecimal("0");
 		for (Map<String, Object> item : list) {
 			BigDecimal itemamount = item.get("amount") == null ? new BigDecimal("0") : new BigDecimal(item.get("amount").toString());
 			String fromcurrency = item.get("currency").toString();
 			itemamount = exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, currency, itemamount);
 			totalsummary=totalsummary.add(itemamount);
			item.put("amount", itemamount.setScale(2, RoundingMode.DOWN));
 			if (item.get("transaction_type") != null) {
 				String transaction_type = item.get("transaction_type").toString();
 				String ctransaction_type = descName.get(transaction_type);
 				Map<String, Object> transactionTypeMap = detail.get(transaction_type)!=null?(Map<String, Object>)detail.get(transaction_type):new HashMap<String, Object>();
 				transactionTypeMap.put("name", transaction_type);
 				transactionTypeMap.put("cname", ctransaction_type);
 				if(item.get("amount_type")==null&&item.get("amount_description") == null) {
 					putItem(transactionTypeMap,item,"item");
 				}
 				if (item.get("amount_type") != null) {
 	 				String amount_type = item.get("amount_type").toString();
 	 				String camount_type = descName.get(amount_type);
 	 				Map<String, Object> amountTypeMap = transactionTypeMap.get(amount_type)!=null?(Map<String, Object>)transactionTypeMap.get(amount_type):new HashMap<String, Object>();
 	 				amountTypeMap.put("name", amount_type);
 	 				amountTypeMap.put("cname", camount_type);
 	 				if (item.get("amount_description") != null) {
 	 	 	 				String amount_description = item.get("amount_description").toString();
 	 	 	 				String camount_description = descName.get(amount_description);
 	 	 	 			    item.put("name", amount_description);
 	 	 	 			item.put("cname", camount_description);
 	 	 	 		        putItem(amountTypeMap,item,"item");
 	 				}else {
 	 					putItem(amountTypeMap,item,"item");
 	 				}
 	 				transactionTypeMap.put(amount_type, amountTypeMap);
 	 			}
 	 		
 				detail.put(transaction_type,transactionTypeMap);
 				BigDecimal summary =transactionTypeMap.get("summary")!=null?new BigDecimal(transactionTypeMap.get("summary").toString()):new BigDecimal("0");
 				summary=summary.add(itemamount);
 				transactionTypeMap.put("summary", summary);
 			}
 	 
 		}
	    List<Map<String, Object>> itemlist =  getDetailSettmentNoSKU(param,descName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("detail",detail);
		result.put("list",itemlist);
		result.put("totalsummary", totalsummary);
 		return result;
 	}
 
 	public List<Map<String, Object>>  getDetailSettmentNoSKU(Map<String, Object> param,Map<String, String> descName) {
 		List<Map<String, Object>> list = null;
 		String settlement_id=param.get("settlement_id")!=null? param.get("settlement_id").toString():null;
 		String shopid=param.get("shopid")!=null?param.get("shopid").toString():null;
 		String currency=param.get("currency")!=null?param.get("currency").toString():null;
 		if(settlement_id==null) {
 			return null;
 		}
        list = this.amzSettlementReportSummaryDayMapper.summaryFeeBySettment(settlement_id);
    	for (Map<String, Object> item : list) {
 			BigDecimal itemamount = item.get("amount") == null ? new BigDecimal("0") : new BigDecimal(item.get("amount").toString());
 			String fromcurrency = item.get("currency").toString();
 			itemamount = exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, currency, itemamount);
			item.put("amount", itemamount.setScale(2, RoundingMode.DOWN));
			if(item.get("in_principal")!=null) {
				BigDecimal in_principal = item.get("in_principal") == null ? new BigDecimal("0") : new BigDecimal(item.get("in_principal").toString());
				in_principal = exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, currency, in_principal);
				item.put("in_principal", in_principal.setScale(2, RoundingMode.DOWN));
			}
			
			if (item.get("transaction_type") != null) {
 				String transaction_type = item.get("transaction_type").toString();
 				String ctransaction_type = descName.get(transaction_type);
 				item.put("ctransaction_type", ctransaction_type);
 				if (item.get("amount_type") != null) {
 	 				String amount_type = item.get("amount_type").toString();
 	 				String camount_type = descName.get(amount_type);
 	 				item.put("camount_type", camount_type);
 	 				if (item.get("amount_description") != null) {
 	 	 	 				String amount_description = item.get("amount_description").toString();
 	 	 	 				String camount_description = descName.get(amount_description);
 	 	 	 			    item.put("camount_description", camount_description);
 	 				} 
 	 			}
    	    }
    	}
    	return list;
 	}
 	public Map<String, Object> getDetail(Map<String, Object> param) {
 		this.setParamsSettlementDate(param);
 		List<Map<String, Object>> list = null;
 		if (param.get("sku") != null) {
 			if (param.get("isother") != null) {
 				list = this.baseMapper.getDetailDaySKUOther(param);
 			} else {
 				list = this.baseMapper.getDetailDaySKU(param);
 			}
 		} else if (param.get("settlement_id") != null) {
 			list = this.baseMapper.getDetail(param);
 		} else {
 			String shopid=param.get("shopid").toString();
 			List<AmazonAuthority> authlist=new ArrayList<AmazonAuthority> ();
 			if( param.get("market") != null) {
 				String market = param.get("market").toString();
 				Marketplace marketplace = marketplaceService.findMapByPoint().get(market);
 				if(param.get("groupid") != null) {
 					String groupid = param.get("groupid").toString();
 					AmazonAuthority amazonAuth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplace.getMarketplaceid());
 					authlist.add(amazonAuth);
 				}else {
 					authlist = amazonAuthorityService.selectByShopAndMarket(shopid,marketplace.getMarketplaceid());
 				}
 			}else {
                 if(param.get("groupid") != null) {
                 	String groupid = param.get("groupid").toString();
                 	authlist = amazonAuthorityService.selectByGroupId(groupid);
 				}else {
 					authlist = amazonAuthorityService.selectByshopid(shopid);
 				}
 			}
 			param.put("amazonAuths", authlist);
 			list = this.baseMapper.getDetailDay(param);
 		}
 		List<AmzAmountDescription> deslist = amzAmountDescriptionMapper.selectList(null);
 		Map<String, String> descName = new HashMap<String, String>();
 		for (AmzAmountDescription item : deslist) {
 			descName.put(item.getEname(), item.getCname());
 		}
 		String currency = param.get("currency") != null&&!"undefined".equals(param.get("currency").toString()) ? param.get("currency").toString() : "RMB";
 		String shopid = param.get("shopid") != null ? param.get("shopid").toString() : null;
 		Map<String, Object> result = new HashMap<String, Object>();
 		TreeSet<String> date = new TreeSet<String>();
 		date.add("0000sum");
 		for (Map<String, Object> item : list) {
 			String transaction_type = null;
 			String amount_type = null;
 			String amount_description = null;
 			String ctransaction_type = null;
 			String camount_type = null;
 			String camount_description = null;
 			String ckey = "";
 			if (item.get("transaction_type") != null) {
 				transaction_type = item.get("transaction_type").toString();
 				ctransaction_type = descName.get(transaction_type);
 				if (ctransaction_type != null) {
 					ckey = ctransaction_type;
 				}else {
 					ckey=transaction_type;
 				}
 			}
 			if (item.get("amount_type") != null) {
 				amount_type = item.get("amount_type").toString();
 				camount_type = descName.get(amount_type);
 				if (camount_type != null) {
 					if (!"".equals(ckey)) {
 						ckey = ckey + "▶";
 					}
 					ckey = ckey + camount_type;
 				} 
 			}
 			if (item.get("amount_description") != null) {
 				amount_description = item.get("amount_description").toString();
 				camount_description = descName.get(amount_description);
 				if (camount_description != null) {
 					if (!"".equals(ckey)) {
 						ckey = ckey + "▶";
 					}
 					ckey = ckey + camount_description;
 				} 
 			}
 			String key = null;
 			if(!StrUtil.isEmpty(transaction_type)) {
 				key =transaction_type;
 			}
 			if(!StrUtil.isEmpty(amount_type)) {
 				if(!StrUtil.isEmpty(key)) {
 					key=key+">";
 				}
 				if(key.length()+amount_type.length()>20) {
 					key=key+"<br/>";
 				}
 				key=key+amount_type;
 			}
 			if(!StrUtil.isEmpty(amount_description)) {
 				if(!StrUtil.isEmpty(key)) {
 					key=key+">";
 				}
 				if(amount_description.length()>20) {
 					key=key+"<br/>";
 				}
 				key=key+amount_description;
 			}
 			
 			BigDecimal amount = exchangeRateHandlerService.changeCurrencyByLocal(shopid,
 					item.get("currency").toString(), currency, new BigDecimal(item.get("amount").toString()));
 			item.put("curamount", amount);
 			@SuppressWarnings("unchecked")
 			List<Map<String, Object>> itemlist = (List<Map<String, Object>>) result.get(key);
 			if (itemlist == null) {
 				itemlist = new ArrayList<Map<String, Object>>();
 				result.put(key, itemlist);
 			}
 			if (ckey != null) {
 				item.put("camount_description", ckey);
 			}
 			date.add(item.get("posted_date").toString());
 			itemlist.add(item);
 		}
 		Map<String, BigDecimal> totalDate = new HashMap<String, BigDecimal>();
 		BigDecimal sumtotalall = new BigDecimal("0");
 		BigDecimal cursumtotalall = new BigDecimal("0");
 		String fromcurrency = "";
 		for (Entry<String, Object> entry : result.entrySet()) {
 			@SuppressWarnings("unchecked")
 			List<Map<String, Object>> itemlist = (List<Map<String, Object>>) entry.getValue();
 			BigDecimal amounttotal = new BigDecimal("0");
 			for (Map<String, Object> item : itemlist) {
 				BigDecimal itemamount = item.get("amount") == null ? new BigDecimal("0") : new BigDecimal(item.get("amount").toString());
 				amounttotal = amounttotal.add(itemamount);
 				fromcurrency = item.get("currency").toString();
 				if (item.get("posted_date") != null) {
 					BigDecimal daytotal = totalDate.get(item.get("posted_date").toString());
 					if (daytotal == null) {
 						daytotal = new BigDecimal("0");
 					}
 					daytotal = daytotal.add(item.get("amount") == null ? new BigDecimal("0") : new BigDecimal(item.get("amount").toString()));
 					totalDate.put(item.get("posted_date").toString(), daytotal);
 				}
 				itemamount = exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, currency, itemamount);
 				item.put("amount", itemamount.setScale(2, RoundingMode.DOWN));
 			}
 
 			BigDecimal curamounttotal = exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, currency, amounttotal);
 			Map<String, Object> map = new HashMap<String, Object>();
 			map.put("amount", curamounttotal.setScale(2, RoundingMode.DOWN));
 			map.put("posted_date", "0000sum");
 			map.put("amount_description", entry.getKey());
 			sumtotalall = sumtotalall.add(amounttotal);
 			cursumtotalall = cursumtotalall.add(curamounttotal);
 			itemlist.add(map);
 		}
 		Map<String, BigDecimal> curtotalByDay = new HashMap<String, BigDecimal>();
 		for (Entry<String, BigDecimal> entry : totalDate.entrySet()) {
 			curtotalByDay.put(entry.getKey(), exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency,
 					currency, entry.getValue(), 2));
 		}
 		cursumtotalall = cursumtotalall.setScale(2, RoundingMode.DOWN);
 		sumtotalall = sumtotalall.setScale(2, RoundingMode.DOWN);
 		curtotalByDay.put("0000sum", cursumtotalall);
 		totalDate.put("0000sum", cursumtotalall);
 		result.put("posted_date", date);
 		result.put("bydaysum", totalDate);
 		result.put("curbydaysum", curtotalByDay);
 		return result;
 	}
 
  
 	public void changeValueExchangeRate(String shopid, Map<String, Object> item, String ftype, String currency, String tocurrency) {
 		if (item != null && item.get(ftype) != null) {
 			BigDecimal amount = (BigDecimal) item.get(ftype);
 			if (currency != null) {
 				amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, currency, tocurrency, amount, 10);
 			    item.put(ftype, amount.setScale(2, RoundingMode.HALF_UP));
 			}
 		}
 	}
   
 	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Cacheable(value = "findSettlementSKUCache#60",key="#ekey")
 	public   HashMap<String, Object>  findSettlementSummary(String ekey,Map<String, Object> map) {
 		String	groupid= map.get("groupid")!=null?map.get("groupid").toString():null;
 		String	marketplaceid= map.get("marketplaceid")!=null?map.get("marketplaceid").toString():null;
 		HashMap<String,Object> result=new HashMap<String,Object>();
 		ArrayList<String> grouplist = new ArrayList<String>();
 		ArrayList<String> marketlist=new ArrayList<String>();
 		if(groupid!=null) {
 			grouplist.add(groupid);
 		}else {
 			List<AmazonGroup> list = amazonGroupService.selectByShopId(map.get("shopid").toString());
 			for(AmazonGroup item:list) {
 				grouplist.add(item.getId());
 			}
 		}
 	for(String mgroupid:grouplist) {
 		marketlist.clear();
 		if(marketplaceid!=null) {
 			marketlist.add(marketplaceid);
 		}else {
 			  List<Marketplace> mlist = marketplaceService.findMarketplaceByGroup(mgroupid);
 			  for(Marketplace item:mlist) {
 				  marketlist.add(item.getMarketplaceid());
 			  }
 		 }
 		for(String marketplace:marketlist) {
 			Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplace);
 			String marketplace_name=market.getPointName();
 			map.put("groupid", mgroupid);
 			map.put("marketplaceid", marketplace);
 			map.put("marketplace_name", marketplace_name);
 			map.put("country", market.getMarket());
 			if(map.get("groupid")!=null&&map.get("marketplaceid")!=null) {
 				String tempgroupid=map.get("groupid").toString();
 				String tempmarketplaceid=map.get("marketplaceid").toString();
 				AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(tempgroupid, tempmarketplaceid);
 				if(auth==null) {
 					continue;
 				}
 				map.put("amazonAuthId", auth.getId());
 				map.put("amazonauthid", auth.getId());
 				map.put("sellerid", auth.getSellerid());
 			}
 			Map<String, Object> result_temp =  findSettlementSummary2(ekey,map);	
 			if(result_temp==null) {
 				continue;
 			}
 			for(Entry<String, Object> entry:result_temp.entrySet()) {
 				String key = entry.getKey();
 				Object oldvalue = result.get(key);
 				Object newvalue =entry.getValue();
 				if(oldvalue==null) {
 					result.put(key, newvalue);
 				}else {
 					if(newvalue!=null&&newvalue instanceof Integer) {
 						result.put(key, (Integer)oldvalue+(Integer)newvalue);
 					}
 					if(newvalue!=null&&newvalue instanceof BigDecimal) {
 						result.put(key, ((BigDecimal)oldvalue).add((BigDecimal)newvalue));
 					}
 					if(newvalue!=null&&newvalue instanceof List) {
 						 ((List)oldvalue).addAll((List)newvalue);
 						result.put(key,oldvalue);
 					}
 				}
 				
 			}
 		}
 	}
 
 	return result;
 	}
 	
 	
  	public   Map<String, Object>  findSettlementSummary2(String ekey,Map<String, Object> map) {
 		String tocurrency = "RMB";
 		if (map.get("currency") != null) {
 			tocurrency = map.get("currency").toString();
 		}
 		String shopid = map.get("shopid").toString();
 		Map<String,BigDecimal> currencyrate=exchangeRateHandlerService.currencyChangeRateByLocal(shopid,tocurrency);
 		map.put("currencyrate", currencyrate);
 		if(map.get("marketplaceid")!=null) {
 			Marketplace market = marketplaceService.findMapByMarketplaceId().get(map.get("marketplaceid").toString());
 			if(market!=null) {
 				map.put("country", market.getMarket());
 			}
 		}
 		
 		if(map.get("groupid")!=null&&map.get("marketplaceid")!=null) {
 			String groupid=map.get("groupid").toString();
 			String marketplaceid=map.get("marketplaceid").toString();
 			AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
 			if(auth==null)return null;
 			map.put("amazonAuthId", auth.getId());
 			map.put("amazonauthid", auth.getId());
 			map.put("sellerid", auth.getSellerid());
 		}
 		this.setParamsSettlementDate(map);
 		Map<String, Object> result =new HashMap<String,Object>();
 		List<Map<String, Object>> amounttype = amzSettlementSummarySkuMapper.findSettlementAmountTypeSummary(map);
 	     BigDecimal setincome=new BigDecimal("0");
 	     BigDecimal principal=new BigDecimal("0");
 	 	 List<AmzAmountDescription> deslist = amzAmountDescriptionMapper.selectList(null);
 		 Map<String, String> descName = new HashMap<String, String>();
 			for (AmzAmountDescription item : deslist) {
 				descName.put(item.getEname(), item.getCname());
 			}
 	     for(Map<String, Object> amountmap:amounttype) {
 	    	 set_camount_description(amountmap,descName);
 	    	 setincome=setincome.add(new BigDecimal(amountmap.get("amount").toString()));
 	    	 if(amountmap.get("amount_type").equals("ItemPrice")
 	    			 &&amountmap.get("transaction_type").equals("Order")
 	    			 &&amountmap.get("amount_description").equals("Principal")) {
 	    		 principal=principal.add(new BigDecimal(amountmap.get("amount").toString()));
 	    	 }
 	     }
 	     result.put("setincome", setincome);
 	     result.put("principal", principal);
 	     result.put("amounttype", amounttype);
 		 return result;
 	}
 	
 	
 	private void set_camount_description(Map<String, Object> amountmap, Map<String, String> descName) {
 		 String transaction_type = null;
 			String amount_type = null;
 			String amount_description = null;
 			String ctransaction_type = null;
 			String camount_type = null;
 			String camount_description = null;
 			String ckey = "";
 			if (amountmap.get("transaction_type") != null) {
 				transaction_type = amountmap.get("transaction_type").toString();
 				ctransaction_type = descName.get(transaction_type);
 				if (ctransaction_type != null) {
 					ckey = ctransaction_type;
 				}else {
 					ckey=transaction_type;
 				}
 			}
 			if (amountmap.get("amount_type") != null) {
 				amount_type = amountmap.get("amount_type").toString();
 				camount_type = descName.get(amount_type);
 				if (camount_type != null) {
 					if (!"".equals(ckey)) {
 						ckey = ckey + "▶";
 					}
 					ckey = ckey + camount_type;
 				} else {
 					if (!"".equals(ckey)) {
 						ckey = ckey + "▶";
 					}
 					ckey = ckey + amount_type;
 				}
 			}
 			if (amountmap.get("amount_description") != null) {
 				amount_description = amountmap.get("amount_description").toString();
 				camount_description = descName.get(amount_description);
 				if (camount_description != null) {
 					if (!"".equals(ckey)) {
 						ckey = ckey + "▶";
 					}
 					ckey = ckey + camount_description;
 				} else {
 					if (!"".equals(ckey)) {
 						ckey = ckey + "▶";
 					}
 					ckey = ckey + amount_description;
 				}
 			}
 			String key = null;
 			if(!StrUtil.isEmpty(transaction_type)) {
 				key =transaction_type;
 			}
 			if(!StrUtil.isEmpty(amount_type)) {
 				if(!StrUtil.isEmpty(key)) {
 					key=key+">";
 				}
 				if(key.length()+amount_type.length()>20) {
 					key=key+"<br/>";
 				}
 				key=key+amount_type;
 			}
 			if(!StrUtil.isEmpty(amount_description)) {
 				if(!StrUtil.isEmpty(key)) {
 					key=key+">";
 				}
 				if(amount_description.length()>20) {
 					key=key+"<br/>";
 				}
 				key=key+amount_description;
 			}
 			if(amountmap.get("maketpointname")!=null) {
 				ckey=amountmap.get("groupname").toString()+"("+amountmap.get("maketpointname").toString()+"):"+ckey;
 			}else {
 				ckey=amountmap.get("groupname").toString()+":"+ckey;
 			}
 		     amountmap.put("camount_description", ckey);
 	}
 	public void setParamsSettlementDate(Map<String, Object> map) {
 		if(map.get("datetype")!=null){
 			List<Map<String, Object>> datelist=iAmzSettlementAccReportService.findDateByAuth(map);
 			if(datelist!=null && datelist.size()>0) {
 				Map<String, Object> dateitem = datelist.get(0);
 				if(dateitem!=null) {
 					if(dateitem.get("fromdate")!=null) {
 						map.put("setfromDate", GeneralUtil.fmtDate(GeneralUtil.getDate(dateitem.get("fromdate"))));
 					}
 					if(dateitem.get("enddate")!=null) {
 						map.put("setendDate", GeneralUtil.fmtDate(GeneralUtil.getDate(dateitem.get("enddate"))));
 					}
 				}
 			}
 		}
 	}
 
 	public List<Map<String, Object>> getSettlementGroupOverAll(Map<String, Object> map) {
 		   
 		String systocurrency = "";
 		if (map.get("currency") != null) {
 			systocurrency = map.get("currency").toString();
 		}
 		String shopid = map.get("shopid").toString();
 		Map<String, Marketplace> marketplacemap = marketplaceService.findMapByPoint();
 		if(map.get("groupid")!=null&&map.get("marketplaceid")!=null) {
 			String groupid=map.get("groupid").toString();
 			String marketplaceid=map.get("marketplaceid").toString();
 			AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
 			if(auth==null) {
 				return null;
 			}
 			map.put("amazonAuthId", auth.getId());
 			map.put("amazonauthid", auth.getId());
 			map.put("sellerid", auth.getSellerid());
 		}
 		AmzFinSettlementFormula formula = iAmzFinSettlementFormulaService.getAmzFinSettlementFormula(shopid);
 		map.put("pricetype", formula.getPricetype());
 		this.setParamsSettlementDate(map);
 	    List<Map<String, Object>> result = amzSettlementSummarySkuMapper.findSettlementOverall(map);
 	    Map<String, HashMap<String, Object>> group=new HashMap<String,HashMap<String,Object>>();
 		BigDecimal sprincipal =  new BigDecimal("0");
     	BigDecimal scommission =  new BigDecimal("0");
     	BigDecimal sfbafee = new BigDecimal("0");
     	BigDecimal srefund = new BigDecimal("0");
     	BigDecimal sadvfee = new BigDecimal("0");
     	BigDecimal sstoragefee = new BigDecimal("0");
     	BigDecimal ssetin = new BigDecimal("0");
     	BigDecimal sother=new BigDecimal("0");
     	BigDecimal sprice=new BigDecimal("0");
     	BigDecimal sprofit=new BigDecimal("0");
 	    for(Map<String, Object> item:result) {
 	     	String fromcurrency = item.get("currency")==null?null:item.get("currency").toString();
 	     	String tocurrency = "";
 	     	if(StrUtil.isEmpty(systocurrency)) {
 	     		tocurrency=fromcurrency;
 	     	}else {
 	     		tocurrency=systocurrency;
 	     	}
 	
 	     	item.put("tocurrency", tocurrency);
 	    	BigDecimal price=new BigDecimal("0");
 	    	BigDecimal otherCost=new BigDecimal("0");
  			Map<String,BigDecimal> currencyrate=exchangeRateHandlerService.currencyChangeRateByLocal(shopid,tocurrency);
  			map.put("currencyrate", currencyrate);
  	     	
  	     	map.put("amazonAuthId", item.get("amazonauthid"));
  	     	map.put("amazonauthid", item.get("amazonauthid"));
  	     	
  	     	map.put("marketplace_name", item.get("marketplace_name"));
  	   	    map.put("marketplaceid", item.get("marketplace_name"));
 	    	Map<String, Object> mymap = amzSettlementSummarySkuMapper.findSettlementSummary(map);
 	    	if(mymap!=null) {
 	    		if(mymap.get("price")!=null) {
 	    			price=new BigDecimal(mymap.get("price").toString());
 	    		}
 	    		if(mymap.get("otherCost")!=null) {
 	    			otherCost=new BigDecimal(mymap.get("otherCost").toString());
 	    		}
 	    	}
 	    	String groupid = item.get("pid")==null?null:item.get("pid").toString();
 	    	String marketplace_name = item.get("marketplace_name")==null?null:item.get("marketplace_name").toString();
 	    	BigDecimal principal = item.get("principal")==null?new BigDecimal("0"):new BigDecimal(item.get("principal").toString());
 	    	BigDecimal commission = item.get("commission")==null?new BigDecimal("0"):new BigDecimal(item.get("commission").toString());
 	    	BigDecimal fbafee = item.get("fbafee")==null?new BigDecimal("0"):new BigDecimal(item.get("fbafee").toString());
 	    	BigDecimal refund = item.get("refund")==null?new BigDecimal("0"):new BigDecimal(item.get("refund").toString());
 	    	BigDecimal advfee = item.get("advfee")==null?new BigDecimal("0"):new BigDecimal(item.get("advfee").toString());
 	    	BigDecimal storagefee = item.get("storagefee")==null?new BigDecimal("0"):new BigDecimal(item.get("storagefee").toString());
 	    	BigDecimal setin = item.get("setin")==null?new BigDecimal("0"):new BigDecimal(item.get("setin").toString());
 	    	BigDecimal tax = item.get("tax")==null?new BigDecimal("0"):new BigDecimal(item.get("tax").toString());
 	    	BigDecimal other=new BigDecimal("0");
 	    	
 	 
 	    	if("Amazon.fr".equals(marketplace_name)||
 	    	   "Amazon.nl".equals(marketplace_name)||
 	    	   "Amazon.co.uk".equals(marketplace_name)||
 	    	   "Amazon.de".equals(marketplace_name)||
 	    	   "Amazon.es".equals(marketplace_name)||
 	    	   "Amazon.it".equals(marketplace_name) 
 	    		) {
 	    		principal=principal.add(tax);
 	    	}
 	    	other=setin.subtract(storagefee).subtract(advfee).subtract(refund).subtract(fbafee).subtract(commission).subtract(principal);
 	    	
 	    	principal=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, principal, 2);
 	    	commission=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, commission, 2);
 	    	fbafee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, fbafee, 2);
 	    	refund=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, refund, 2);
 	    	advfee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, advfee, 2);
 	    	storagefee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, storagefee, 2);
 	    	setin=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, setin, 2);
 	    	other=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, other, 2);
 	    	price=price.add(otherCost);
 	    	BigDecimal profit = setin.subtract(price);
 	    	sprincipal =sprincipal.add(principal);
 	    	scommission =  scommission.add(commission);
 	    	sfbafee =  sfbafee.add(fbafee);
 	    	srefund = srefund.add(refund);
 	    	sadvfee =  sadvfee.add(advfee);
 	    	sstoragefee =  sstoragefee.add(storagefee);
 	    	ssetin = ssetin.add(setin);
 	    	sother=sother.add(other);
 	    	sprice=sprice.add(price);
 	    	sprofit=sprofit.add(profit);
 	    	item.put("principal", principal);
 	    	item.put("commission", commission);
 	    	item.put("fbafee", fbafee);
 	    	item.put("refund", refund);
 	    	item.put("advfee", advfee);
 	    	item.put("storagefee", storagefee);
 	    	item.put("setin", setin);
 	    	item.put("other", other);
 	    	item.put("price", price);
 	    	item.put("profit", profit);
 	    	Marketplace market = marketplacemap.get(marketplace_name);
 	    	if(market!=null) {
 	    		item.put("name", market.getName());
 	    	}else {
 	    		item.put("name", marketplace_name);
 	    	}
 	    	HashMap<String, Object> mapgroup = group.get(groupid);
 	    	if(mapgroup==null) {
 	    		mapgroup=new HashMap<String, Object>();
 	    		AmazonGroup amzgroups = amazonGroupService.getById(groupid);
 	    		mapgroup.put("id", groupid);
 	    		mapgroup.put("pid", "0");
 	    		mapgroup.put("name", amzgroups.getName());
 	    		group.put(groupid,mapgroup);
 	    	}
 	    	BigDecimal gprincipal = mapgroup.get("principal")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("principal").toString());
 	    	BigDecimal gcommission = mapgroup.get("commission")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("commission").toString());
 	    	BigDecimal gfbafee = mapgroup.get("fbafee")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("fbafee").toString());
 	    	BigDecimal grefund = mapgroup.get("refund")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("refund").toString());
 	    	BigDecimal gadvfee = mapgroup.get("advfee")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("advfee").toString());
 	    	BigDecimal gstoragefee = mapgroup.get("storagefee")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("storagefee").toString());
 	    	BigDecimal gsetin = mapgroup.get("setin")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("setin").toString());
 	    	BigDecimal gother=mapgroup.get("other")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("other").toString());
 	    	BigDecimal gprice=mapgroup.get("price")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("price").toString());
 	    	BigDecimal gprofit=mapgroup.get("profit")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("profit").toString());
 	    	
 	    	mapgroup.put("principal",gprincipal.add(principal));
 	    	mapgroup.put("commission",gcommission.add(commission));
 	    	mapgroup.put("fbafee",gfbafee.add(fbafee));
 	    	mapgroup.put("refund",grefund.add(refund));
 	    	mapgroup.put("advfee",gadvfee.add(advfee));
 	    	mapgroup.put("storagefee",gstoragefee.add(storagefee));
 	    	mapgroup.put("setin",gsetin.add(setin));
 	    	mapgroup.put("other",gother.add(other));
 	    	mapgroup.put("price",gprice.add(price));
 	    	mapgroup.put("profit",gprofit.add(profit));
 	    }
 		for(Entry<String, HashMap<String, Object>> entry:group.entrySet()) {
 			result.add(entry.getValue());
 		}
 		LinkedList<Map<String, Object>> result2 = new LinkedList<Map<String,Object>>();
 		for(int i=0;i<result.size();i++) {
 			Map<String, Object> mgroup = result.get(i);
 			if(mgroup.get("pid").toString().equals("0")) {
 				String id = mgroup.get("id").toString();
 				result2.add(mgroup);
 				for(int j=0;j<result.size();j++) {
 					Map<String, Object> mmgroup = result.get(j);
 					String pid=mmgroup.get("pid").toString();
 				    if(pid.equals(id)) {
 				    	result2.add(mmgroup);
 				    }
 				}
 			}
 		}
 		if(result2.size()>0) {
 			Map<String, Object> firstmap = result2.get(0);
 			firstmap.put("sprincipal", sprincipal);
 			firstmap.put("scommission", scommission);
 			firstmap.put("sfbafee", sfbafee);
 			firstmap.put("srefund", srefund);
 			firstmap.put("sadvfee", sadvfee);
 			firstmap.put("sstoragefee", sstoragefee);
 			firstmap.put("ssetin", ssetin);
 			firstmap.put("sother", sother);
 			firstmap.put("sprice", sprice);
 			firstmap.put("sprofit", sprofit);
 		}
 		return result2;
 	}
 	
 	public List<Map<String, Object>> downloadSettlementGroupOverAll(Map<String, Object> map) {
 		String systocurrency = "";
 		if (map.get("currency") != null) {
 			systocurrency = map.get("currency").toString();
 		}
 		String shopid = map.get("shopid").toString();
 		Map<String, Marketplace> marketplacemap = marketplaceService.findMapByPoint();
 		if(map.get("groupid")!=null&&map.get("marketplaceid")!=null) {
 			String groupid=map.get("groupid").toString();
 			String marketplaceid=map.get("marketplaceid").toString();
 			AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
 			if(auth==null) {
 				return null;
 			}
 			map.put("amazonAuthId", auth.getId());
 			map.put("amazonauthid", auth.getId());
 			map.put("sellerid", auth.getSellerid());
 		}
 		AmzFinSettlementFormula formula = iAmzFinSettlementFormulaService.getAmzFinSettlementFormula(shopid);
 		map.put("pricetype", formula.getPricetype());
 		this.setParamsSettlementDate(map);
 	    List<Map<String, Object>> result = amzSettlementSummarySkuMapper.findSettlementOverall(map);
 	    for(Map<String, Object> item:result) {
 	     	String fromcurrency = item.get("currency")==null?null:item.get("currency").toString();
 	     	String tocurrency = "";
 	     	if(StrUtil.isEmpty(systocurrency)) {
 	     		tocurrency=fromcurrency;
 	     	}else {
 	     		tocurrency=systocurrency;
 	     	}
 	     	item.put("tocurrency", tocurrency);
 	    	BigDecimal price=new BigDecimal("0");
  			Map<String,BigDecimal> currencyrate=exchangeRateHandlerService.currencyChangeRateByLocal(shopid,tocurrency);
  			map.put("currencyrate", currencyrate);
  	     	map.put("amazonAuthId", item.get("amazonauthid"));
  	     	map.put("amazonauthid", item.get("amazonauthid"));
  	     	map.put("marketplace_name", item.get("marketplace_name"));
  	   	    map.put("marketplaceid", item.get("marketplace_name"));
 	    	Map<String, Object> mymap = amzSettlementSummarySkuMapper.findSettlementSummary(map);
 	    	if(mymap!=null) {
 	    		if(mymap.get("price")!=null) {
 	    			price=new BigDecimal(mymap.get("price").toString());
 	    		}
 	    	}
 	    	String marketplace_name = item.get("marketplace_name")==null?null:item.get("marketplace_name").toString();
 	    	BigDecimal principal = item.get("principal")==null?new BigDecimal("0"):new BigDecimal(item.get("principal").toString());
 	    	BigDecimal commission = item.get("commission")==null?new BigDecimal("0"):new BigDecimal(item.get("commission").toString());
 	    	BigDecimal fbafee = item.get("fbafee")==null?new BigDecimal("0"):new BigDecimal(item.get("fbafee").toString());
 	    	BigDecimal refund = item.get("refund")==null?new BigDecimal("0"):new BigDecimal(item.get("refund").toString());
 	    	BigDecimal advfee = item.get("advfee")==null?new BigDecimal("0"):new BigDecimal(item.get("advfee").toString());
 	    	BigDecimal storagefee = item.get("storagefee")==null?new BigDecimal("0"):new BigDecimal(item.get("storagefee").toString());
 	    	BigDecimal setin = item.get("setin")==null?new BigDecimal("0"):new BigDecimal(item.get("setin").toString());
 	    	BigDecimal tax = item.get("tax")==null?new BigDecimal("0"):new BigDecimal(item.get("tax").toString());
 	    	BigDecimal shipcharge = item.get("shipcharge")==null?new BigDecimal("0"):new BigDecimal(item.get("shipcharge").toString());
 	    	BigDecimal reserve = item.get("reserve")==null?new BigDecimal("0"):new BigDecimal(item.get("reserve").toString());
 	    	BigDecimal savefee = item.get("savefee")==null?new BigDecimal("0"):new BigDecimal(item.get("savefee").toString());
 	    	BigDecimal untransfer = item.get("untransfer")==null?new BigDecimal("0"):new BigDecimal(item.get("untransfer").toString());
 			
 	    	BigDecimal other=new BigDecimal("0");
 	    	if("Amazon.fr".equals(marketplace_name)||
 	    	   "Amazon.nl".equals(marketplace_name)||
 	    	   "Amazon.co.uk".equals(marketplace_name)||
 	    	   "Amazon.de".equals(marketplace_name)||
 	    	   "Amazon.es".equals(marketplace_name)||
 	    	   "Amazon.it".equals(marketplace_name) 
 	    		) {
 	    		principal=principal.add(tax);
 	    	}
 	    	other=setin.subtract(storagefee).subtract(advfee).subtract(refund)
 	    			.subtract(fbafee).subtract(commission).subtract(principal)
 	    			.subtract(shipcharge).subtract(reserve).subtract(savefee).subtract(untransfer);
 	    	principal=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, principal, 2);
 	    	commission=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, commission, 2);
 	    	fbafee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, fbafee, 2);
 	    	refund=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, refund, 2);
 	    	advfee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, advfee, 2);
 	    	storagefee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, storagefee, 2);
 	    	setin=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, setin, 2);
 	    	other=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, other, 2);
 	    	shipcharge=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, shipcharge, 2);
 	    	reserve=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, reserve, 2);
 	    	savefee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, savefee, 2);
 	    	untransfer=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, untransfer, 2);
 	    	price=price.setScale(2, RoundingMode.HALF_UP);
 	    	BigDecimal profit = setin.subtract(price);
 	    	item.put("principal", principal);
 	    	item.put("commission", commission);
 	    	item.put("fbafee", fbafee);
 	    	item.put("refund", refund);
 	    	item.put("advfee", advfee);
 	    	item.put("storagefee", storagefee);
 	    	item.put("setin", setin);
 	    	item.put("other", other);
 	    	item.put("price", price);
 	    	item.put("profit", profit);
 	    	item.put("shipcharge", shipcharge);
 	    	item.put("reserve", reserve);
 	    	item.put("savefee", savefee);
 	    	item.put("untransfer", untransfer);
 	    	
 	    	Marketplace market = marketplacemap.get(marketplace_name);
 	    	if(market!=null) {
 	    		item.put("marketname", market.getName());
 	    	}else {
 	    		item.put("marketname", marketplace_name);
 	    	}
 	    	AmazonGroup amzgroups = amazonGroupService.getById(item.get("pid").toString());
 	    	item.put("groupname", amzgroups.getName());
 	    }
 		return result;
 	}
 	
 	public BigDecimal initAmount(String shopid, Map<String, Object> item, String ftype, String currency, String tocurrency) {
 		if (item != null && item.get(ftype) != null) {
 			BigDecimal amount =null;
 			if("0".equals(item.get(ftype).toString())) {
 				amount=new BigDecimal(0);
 			}else {
 				amount = (BigDecimal) item.get(ftype);
 			}
 			if (currency != null) {
 				amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, currency, tocurrency, amount, 6);
 				if (!ftype.equals("setincome")) {
 					item.put(ftype, amount.setScale(6, RoundingMode.HALF_UP));
 				} else {
 					item.put(ftype, amount);
 				}
 			}
 			return amount;
 		}
 		return new BigDecimal("0");
 	}
 	
 	@Cacheable(value = "findSettlementSKUCache#60", key = "#ekey")
 	public List<Map<String, Object>> findCommodity(String ekey,Map<String, Object> map) {
 		 return findCommodity(map);
 	}
  
 	public List<Map<String, Object>> findCommodity(Map<String, Object> map) {
 		String tocurrency = "RMB";
 		if (map.get("currency") != null) {
 			tocurrency = map.get("currency").toString();
 		}
 		  //-----------------目标币种获取---------------
 		//Object isdownload = map.get("isdownload");
 		String shopid = map.get("shopid").toString();
 		Map<String,BigDecimal> currencyrate=exchangeRateHandlerService.currencyChangeRateByLocal(shopid,tocurrency);
 		map.put("currencyrate", currencyrate);
 		AmzFinSettlementFormula formula = iAmzFinSettlementFormulaService.getAmzFinSettlementFormula(shopid);
 		String field = formula.getField();
 		if(field!=null && field.contains("firstShipment")){
 			map.put("hasShipment", "true");
 		} 
 		 
 		Boolean hasspend = true;
 		map.put("hasspend", "true");
 		map.put("pricetype",formula.getPricetype());
 		 //------------------------------------------配置获取
 		
 		String	groupid= map.get("groupid")!=null?map.get("groupid").toString():null;
 		String	marketplaceid= map.get("marketplaceid")!=null?map.get("marketplaceid").toString():null;
 		Set<String> grouplist = new HashSet<String>();
 		Set<String> marketlist=new HashSet<String>();
 		if(StrUtil.isNotBlank(groupid)) {
 			grouplist.add(groupid);
 		}else {
 			List<AmazonGroup> list = amazonGroupService.selectByShopId(map.get("shopid").toString());
 			for(AmazonGroup item:list) {
 				grouplist.add(item.getId());
 			}
 		}
 	 List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
 	 for(String mgroupid:grouplist) {
 		marketlist.clear();
 		if(StrUtil.isNotBlank(marketplaceid)) {
 			marketlist.add(marketplaceid);
 		}else {
 		  List<Marketplace> mlist = marketplaceService.findMarketplaceByGroup(mgroupid);
 		  for(Marketplace item:mlist) {
 			  marketlist.add(item.getMarketplaceid());
 		  }
 		}
 		for(String marketplace:marketlist) {
 			Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplace);
 			String marketplace_name=market.getPointName();
 			map.put("groupid", mgroupid);
 			map.put("marketplaceid", marketplace);
 			map.put("marketplace_name", marketplace_name);
 			if(map.get("marketplaceid")!=null) {
 				Marketplace tmepmarket = marketplaceService.findMapByMarketplaceId().get(map.get("marketplaceid").toString());
 				map.put("country", tmepmarket.getMarket());
 			}
 			if(map.get("groupid")!=null&&map.get("marketplaceid")!=null) {
 				String tempgroupid=map.get("groupid").toString();
 				String tempmarketplaceid=map.get("marketplaceid").toString();
 				AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(tempgroupid, tempmarketplaceid);
 				if(auth==null) {
 					continue;
 				}
 				map.put("amazonAuthId", auth.getId());
 				map.put("amazonauthid", auth.getId());
 				map.put("sellerid", auth.getSellerid());
 			}
 			setParamsSettlementDate(map);
 			List<Map<String, Object>> templist = amzSettlementSummarySkuMapper.findSettlementSummarySku(map);
 			if(templist!=null && templist.size()>0) {
 				list.addAll(templist);
 			  }
 		   }
 	   }
         //-------------多站点多店铺查询合并
 		Map<String, Object> SumData = new HashMap<String, Object>();
 		//  编译表达式
 		List<Map<String, Object>> needremovelist=new ArrayList<Map<String, Object>>();
 		if (list.size() > 0 && list != null) {
 			for (int j=0;j< list.size();j++) {
 				Map<String, Object> item =list.get(j);
 				if(item.get("sku").toString().equals("*")) {
 						needremovelist.add(item);continue;
 				}
 				map.putAll(item);
 				item.put("pricetype",formula.getPricetype());
 				handlerSettlementSKURowData(shopid, item, tocurrency, hasspend, SumData,map);
 		 
 			}
 			for(Map<String, Object> item:needremovelist) {
 				list.remove(item);
 			}
 	 
 			if (list.size() > 0) {
 				sumSettlementSKURowData( SumData, list, hasspend);
 			}
 			return list;
 		} else {
 			return null;
 		}
 	}
 	public void handlerSettlementSKURowData(String shopid, Map<String, Object> item, String tocurrency,
 			boolean hasspend,  Map<String, Object> SumData, Map<String,Object> param) {
 		if (item.get("currency") != null) {
 			String fromCurrency = item.get("currency").toString();
 			BigDecimal principal = initAmount(shopid, item, "principal", fromCurrency, tocurrency);
 			initAmount(shopid, item, "avgprice", fromCurrency, tocurrency);
 			initAmount(shopid, item, "commission", fromCurrency, tocurrency);
 			initAmount(shopid, item, "fbafee", fromCurrency, tocurrency);
 			BigDecimal refund = initAmount(shopid, item, "refund", fromCurrency, tocurrency);
			initAmount(shopid, item, "shipping", fromCurrency, tocurrency);
			initAmount(shopid, item, "promotion", fromCurrency, tocurrency);
 			initAmount(shopid, item, "otherfee", fromCurrency, tocurrency);
 			initAmount(shopid, item, "setincome", fromCurrency, tocurrency);
 			BigDecimal income=initAmount(shopid, item, "income", fromCurrency, tocurrency);
 			initAmount(shopid, item, "share_adv_spend_fee", fromCurrency, tocurrency);
 			initAmount(shopid, item, "share_storage_fee", fromCurrency, tocurrency);
 			initAmount(shopid, item, "share_long_storage_fee", fromCurrency, tocurrency);
 			initAmount(shopid, item, "share_coupon_redemption_fee", fromCurrency, tocurrency);
 			initAmount(shopid, item, "share_reserve_fee", fromCurrency, tocurrency);
 			initAmount(shopid, item, "share_shop_other_fee", fromCurrency, tocurrency);
 			initAmount(shopid, item, "share_reimbursement_fee", fromCurrency, tocurrency);
 			BigDecimal local_other_cost=initAmount(shopid, item, "local_other_cost", "CNY", tocurrency);
 			BigDecimal local_price=initAmount(shopid, item, "local_price", "CNY", tocurrency);
 			BigDecimal local_return_tax=initAmount(shopid, item, "local_return_tax", "CNY", tocurrency);
 			BigDecimal profit_local_shipmentfee=initAmount(shopid, item, "profit_local_shipmentfee", "CNY", tocurrency);
 			BigDecimal profit_marketfee=initAmount(shopid, item, "profit_marketfee", fromCurrency, tocurrency);
 			BigDecimal profit_vat=initAmount(shopid, item, "profit_vat", fromCurrency, tocurrency);
 			BigDecimal profit_companytax=initAmount(shopid, item, "profit_companytax", fromCurrency, tocurrency);
 			BigDecimal profit_customstax=initAmount(shopid, item, "profit_customstax", fromCurrency, tocurrency);
 			BigDecimal profit_exchangelost=initAmount(shopid, item, "profit_exchangelost", fromCurrency, tocurrency);
 			BigDecimal profit_lostrate=initAmount(shopid, item, "profit_lostrate", fromCurrency, tocurrency);
 			BigDecimal profit_otherfee=initAmount(shopid, item, "profit_otherfee", fromCurrency, tocurrency);
 			BigDecimal profit = new BigDecimal(0);
 			profit=income.subtract(local_other_cost)
 					     .subtract(local_price)
 					     .subtract(local_return_tax)
 					     .subtract(profit_local_shipmentfee)
 					     .subtract(profit_marketfee)
 					     .subtract(profit_vat)
 					     .subtract(profit_companytax)
 					     .subtract(profit_customstax)
 					     .subtract(profit_exchangelost)
 					     .subtract(profit_lostrate)
 					     .subtract(profit_otherfee);
  
 			BigDecimal profitrate = new BigDecimal("0");
 			BigDecimal refundrate = new BigDecimal("0"); 
 			if(principal!=null&&(principal.floatValue()>0.000001||principal.floatValue()<-0.000001)) {
 	 			profitrate=profit.divide(principal, 2, RoundingMode.HALF_UP);
 	 			refundrate=refund.divide(principal, 2, RoundingMode.HALF_UP);
 			}
 		    item.put("profitrate", profitrate);
 		    item.put("refundrate", refundrate);
 			item.put("profit", profit);
 			addSumData(item, SumData);
 			 // 合计
 		}else {
 			System.out.println("币种为空了---------------------------------------------");
 		}
 	}
 
 	public BigDecimal addSumData(Map<String, Object> maps, Map<String, Object> SumDatamap) {
 		BigDecimal result=new BigDecimal("0");
 		if (maps != null) {
 			for (Entry<String, Object> entry : maps.entrySet()) {
 				String key = entry.getKey();
 				String value = "0";
 				if (entry.getValue() != null) {
 					value = entry.getValue().toString().trim();
 				}
 				if (SumDatamap.containsKey(key)) {
 					if (key.equals("principal") || key.equals("commission") || key.equals("fbafee")|| key.equals("shipping")
 							|| key.equals("promotion")|| key.equals("refund") || key.equals("setincome") || key.equals("otherfee")
 							|| key.equals("income") || key.equals("share_adv_spend_fee") || key.equals("share_storage_fee") 
 							|| key.equals("share_long_storage_fee")|| key.equals("share_coupon_redemption_fee") 
 							|| key.equals("share_reserve_fee") || key.equals("share_shop_other_fee")
 							|| key.equals("share_reimbursement_fee")
 							|| key.equals("local_other_cost") || key.equals("local_price") || key.equals("local_return_tax")
 							|| key.equals("profit_local_shipmentfee") || key.contains("profit_marketfee") || key.contains("profit_vat")
 							|| key.equals("profit_companytax") || key.equals("profit_customstax")|| key.equals("profit_exchangelost")
 							|| key.equals("profit_lostrate") || key.equals("profit_otherfee")|| key.equals("profit")
 							) {
 						BigDecimal maptemp = new BigDecimal(value);
 						BigDecimal summaptemp = new BigDecimal(SumDatamap.get(key)==null?"0":SumDatamap.get(key).toString().trim());
 						result=result.add(maptemp);
 						SumDatamap.put(key, maptemp.add(summaptemp));
 					}		 
 					if(key.equals("salenum") || key.equals("ordernum") || key.equals("refundsales")|| key.equals("refundorder")) {
 						BigDecimal maptemp = new BigDecimal(value);
 						BigDecimal summaptemp = new BigDecimal(SumDatamap.get(key)==null?"0":SumDatamap.get(key).toString().trim());
 						SumDatamap.put(key, maptemp.add(summaptemp));
 					}
 				}else {
 					SumDatamap.put(key,value);
 				}
 			}
 		}
 		return result;
 	}
 
 
 	public void sumSettlementSKURowData( Map<String, Object> SumData, List<Map<String, Object>> list, boolean hasspend) {
 		BigDecimal sumavgprice = new BigDecimal("0");
 		BigDecimal sumrrate = new BigDecimal("0");
 		BigDecimal sumprate = new BigDecimal("0");
 		//BigDecimal sumgrossprate = new BigDecimal("0");
 		if (SumData.get("principal") != null && !"0.00".equals(SumData.get("principal").toString())) {
 			if (SumData.get("principal") != null && SumData.get("salenum") != null && !"0".equals(SumData.get("salenum").toString())) {
 				sumavgprice = new BigDecimal(SumData.get("principal").toString())
 						.divide(new BigDecimal(SumData.get("salenum").toString()), 2, RoundingMode.HALF_UP);
 				SumData.put("avgprice",sumavgprice);
 			}
 			if (SumData.get("refund") != null) {
 				sumrrate = new BigDecimal(SumData.get("refund").toString()).multiply(new BigDecimal(100))
 						.divide(new BigDecimal(SumData.get("principal").toString()), 2, RoundingMode.HALF_UP);
 				SumData.put("refundrate",sumrrate);
 			}
 			if (SumData.get("profit") != null) {
 				sumprate = new BigDecimal(SumData.get("profit").toString()).multiply(new BigDecimal(100))
 						.divide(new BigDecimal(SumData.get("principal").toString()), 2, RoundingMode.HALF_UP);
 				SumData.put("profitrate",sumprate);
 			}
 		}
 		list.get(0).put("summary", SumData);
 	}
 	
 
 

	public void setExcelBookOverall(SXSSFWorkbook workbook, List<Map<String, Object>> list) {
    	
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("groupname", "店铺名称");
		titlemap.put("marketname", "站点");
		titlemap.put("principal", "销售额");
		titlemap.put("commission", "销售佣金");
		titlemap.put("fbafee", "FBA费用");
		titlemap.put("refund", "退款金额");
		titlemap.put("storagefee", "仓储费");
		titlemap.put("advfee", "广告费");
		titlemap.put("shipcharge", "国际物流");
		titlemap.put("reserve", "预留金差额");
		titlemap.put("savefee", "折扣");
		titlemap.put("untransfer", "转账失败补");
		titlemap.put("other", "其他");
		titlemap.put("setin", "结算收入");		
		titlemap.put("price", "采购成本");
		titlemap.put("profit", "利润");		

		if (list.size() > 0 && list != null) {
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Cell cell = null;
			Object[] titlearray = titlemap.keySet().toArray();
			for (int i = 0; i < titlearray.length; i++) {
				cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
				Object value = titlemap.get(titlearray[i].toString());
				cell.setCellValue(value.toString());
			}
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					Object value = map.get(titlearray[j].toString());
					if (value != null) {
						if(value instanceof BigDecimal) {
							cell.setCellValue(  Double.parseDouble(value.toString()));
						}else {
							cell.setCellValue(value.toString());
						}
					}
				}
			}
		} else {
			try {
				throw new Exception("没有数据可导出！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void setExcelBook(SXSSFWorkbook workbook, Map<String, Object> param,UserInfo user) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		int headi=0;
		String tocurrency = "";
		if(param.get("currency")!=null) {
			tocurrency=param.get("currency").toString()	;
		}
		Sheet sheet = workbook.createSheet("sheet1");
    	titlemap.put("daterange", "时间区间");
		titlemap.put("sku", "SKU");
		titlemap.put("asin", "ASIN");
		titlemap.put("groupname", "店铺");
		titlemap.put("marketname", "站点");
		titlemap.put("currency", "币种");
		titlemap.put("pname", "商品名称");
		titlemap.put("mname", "本地产品名称");
		titlemap.put("salenum", "订单量");
		titlemap.put("salenum", "销量");
		titlemap.put("refundsales", "退款数量");
		titlemap.put("principal", "销售额");
		titlemap.put("avgprice", "平均售价");
		titlemap.put("commission", "佣金");
		titlemap.put("fbafee", "FBA费");
		titlemap.put("shipping", "运费");
		titlemap.put("refund", "退款");
		titlemap.put("promotion", "促销");
		titlemap.put("otherfee", "其它费用");
		titlemap.put("setincome", "SKU结算");
		titlemap.put("share_storage_fee", "店铺分摊-仓储费");
		titlemap.put("share_long_storage_fee", "店铺分摊-长期仓储费");
		titlemap.put("share_adv_spend_fee", "店铺分摊-广告费");
		titlemap.put("share_reserve_fee", "店铺分摊-预留金");
		titlemap.put("share_coupon_redemption_fee", "店铺分摊-折扣券");
		titlemap.put("share_shop_other_fee", "店铺分摊-其它");
		titlemap.put("income", "店铺结算");
		titlemap.put("local_price", "本地-采购成本");
		titlemap.put("local_other_cost", "本地-其它");
		titlemap.put("local_return_tax", "本地-退税");
		titlemap.put("profit_local_shipmentfee", "本地-预估-运费");
		titlemap.put("profit_marketfee", "本地-预估-市场费用");
		titlemap.put("profit_vat", "本地-预估-VAT");
		titlemap.put("profit_companytax", "本地-预估-所得税");
		titlemap.put("profit_customstax", "本地-预估-关税");
		titlemap.put("profit_exchangelost", "本地-预估-汇率损耗");
		titlemap.put("profit_lostrate", "本地-预估-固定费用");
		titlemap.put("profit_otherfee", "本地-预估-其它费用");
		titlemap.put("profit", "利润");
		titlemap.put("profitrate", "利润率");
		Object nowgroupid=param.get("groupid");
		Object nowmarketpalceid=param.get("marketplaceid");
		Object nowmarketplace_name=param.get("marketplace_name");
		String ekey = JSON.toJSONString(param);
		Map<String, Object> summary =null;
		Map<String, Object> skusummary =null;
		List<Map<String, Object>> list =null;
		if(param.get("id")==null) {
			summary = this.findSettlementSummary(ekey,param);
			param.put("summarydata", summary);
			ekey=ekey+"skutype";
			param.put("isdownload", "true");
			param.put("groupid", nowgroupid);
		    param.put("marketplaceid", nowmarketpalceid);
		    param.put("nowmarketplace_name", nowmarketplace_name);
			list = this.findCommodity(param);
		}else {
			String id=param.get("id").toString();
			AmzSettlementAccStatement statement = iAmzSettlementAccStatementService.findCommodityStatement(id);
			byte[] listbyte = statement.getListdata();
			String liststr = null;
			try {
				liststr = new String(listbyte,"utf-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			JSONArray jsonArray =   JSONArray.parseArray(liststr);
	 
			String search=null;
			String color=null;
			String ownerid=null;
			List<Map<String, Object>> restlist = new ArrayList<Map<String,Object>>();
			if(param.get("search")!=null) {
				search=param.get("search").toString();
				search=search.replace("%", "");
			}
			if(param.get("color")!=null) {
				color=param.get("color").toString();
			}
			if(param.get("owner")!=null) {
				ownerid=param.get("owner").toString();
			}
			for(int p=0;p<jsonArray.size();p++) {
				  JSONObject myitem = jsonArray.getJSONObject(p);
				 boolean needadd=true;
				 if(search!=null) {
					Object skuobj=myitem.get("sku");
					 if(skuobj==null||!skuobj.toString().contains(search)) {
						 needadd=false;
					 }
				 }
				 if(color!=null) {
					 Object colorobj=myitem.get("color");
					 if(colorobj==null||!colorobj.toString().equals(color)) {
						 needadd=false;
					 }
				 }
				 if(ownerid!=null) {
					 Object owneridobj=myitem.get("owner");
					 if(owneridobj==null||!owneridobj.toString().equals(ownerid)) {
						 needadd=false;
					 }
				 }
	            if(needadd) {
					 restlist.add(myitem);
				 }
			}
		     list=restlist;
				byte[] summarydatabyte = statement.getSummaryall();
				String summarydatastr = null;
				try {
					summarydatastr = new String(summarydatabyte,"utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Object sumjson = com.alibaba.fastjson.JSON.parse(summarydatastr);
				if(sumjson!=null) {
					Map<String, Object> summarydata = (Map<String, Object>)sumjson;
				 	summary=summarydata;
				}
		}
		 if(list!=null&&list.size()>0) {
			 skusummary=(Map<String, Object>) list.get(0).get("summary");
		 }
		Row srow1 = sheet.createRow(headi++);
		Cell cell = srow1.createCell(0); 
		Object value = " 店铺结算汇总：";
		cell.setCellValue(value.toString());
		CellRangeAddress region = new CellRangeAddress(0, 0, 0, 2);
	    sheet.addMergedRegion(region);
	    
		cell = srow1.createCell(3); 
		value = skusummary!=null?skusummary.get("income"):new BigDecimal("0");
		cell.setCellValue(GeneralUtil.formatterNum(value));
		cell = srow1.createCell(4); 
		cell.setCellValue("");
 
		
		cell = srow1.createCell(5); 
		value ="店铺利润(%)";
		cell.setCellValue(value.toString());
		region = new CellRangeAddress(0, 0, 5, 7);
	    sheet.addMergedRegion(region);
	    
		cell = srow1.createCell(8); 
		if(skusummary!=null&&skusummary.get("profitrate")!=null) {
			value =skusummary.get("profitrate");
			cell.setCellValue(GeneralUtil.formatterNum(value));
		}else {
			cell.setCellValue(0);
		}
		
	 
 
		cell = srow1.createCell(10); 
		value ="店铺利润";
		cell.setCellValue(value.toString());
		region = new CellRangeAddress(0, 0, 10, 12);
	    sheet.addMergedRegion(region);
		cell = srow1.createCell(13); 
		value =skusummary!=null?skusummary.get("profit"):new BigDecimal("0");
		cell.setCellValue(GeneralUtil.formatterNum(value));
	 
 
		cell = srow1.createCell(14); 
		value ="销售数量";
		cell.setCellValue(value.toString());
		region = new CellRangeAddress(0, 0, 14, 16);
	    sheet.addMergedRegion(region);
		cell = srow1.createCell(17); 
		value =skusummary!=null?skusummary.get("salenum"):new BigDecimal("0");
		cell.setCellValue(GeneralUtil.formatterNum(value));
		BigDecimal shopfee = new BigDecimal("0");
		if(summary.get("amounttype")!=null) {
			List<Map<String,Object>> amounttype=(List<Map<String, Object>>) summary.get("amounttype");
			for(int j=0;j<amounttype.size();j++) {
				Map<String, Object> mitem=amounttype.get(j);
				 srow1 = sheet.createRow(headi++);
				 cell = srow1.createCell(0); 
				 value = mitem.get("camount_description");
				 cell.setCellValue(value.toString());
				 region = new CellRangeAddress(headi, headi, 0, 6);
			     sheet.addMergedRegion(region);
				 cell = srow1.createCell(7); 
				 value = mitem.get("amount");
				 BigDecimal bnumber = new BigDecimal("0");
				 if(value!=null) {
					bnumber = new BigDecimal(value.toString());
					cell.setCellValue(GeneralUtil.formatterNum(value));
				 }else {
					cell.setCellValue("0");
				 }
				 if(mitem.get("nosku")!=null&&"1".equals(mitem.get("nosku").toString())) {
					 cell = srow1.createCell(8); 
					 cell.setCellValue("(店铺费用)");
					 shopfee=shopfee.add(bnumber);
				 }
				 if(j>0&&j==amounttype.size()-1) {
					 cell = srow1.createCell(9); 
					 cell.setCellValue("");
					 cell = srow1.createCell(10); 
					 cell.setCellValue("店铺费用汇总：");
					 region = new CellRangeAddress(headi, headi, 10, 11);
				     sheet.addMergedRegion(region);
					 cell = srow1.createCell(12); 
					 cell.setCellValue(GeneralUtil.formatterNum(shopfee.toString()));
					 region = new CellRangeAddress(headi, headi, 12, 13);
				     sheet.addMergedRegion(region);
				 }
			}
		   sheet.createRow(headi++);
		}
		param.put("issummary", "false");
		param.put("isdownload", "true");
	    ekey = JSON.toJSONString(param);
	    param.put("groupid", nowgroupid);
	    param.put("marketplaceid", nowmarketpalceid);
	    param.put("nowmarketplace_name", nowmarketplace_name);
		if(summary!=null) {
			param.put("summarydata", summary);
		}

		if (list.size() > 0 && list != null) {
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(headi++);
			cell = null;
			Object[] titlearray = titlemap.keySet().toArray();
			for (int j = 0; j < titlearray.length; j++) {
				cell = trow.createCell(j); // 在索引0的位置创建单元格(左上端)
				value = titlemap.get(titlearray[j].toString());
				cell.setCellValue(value.toString());
			}
			
			for (int i=0;i < list.size(); i++) {
				Row row = sheet.createRow(headi+i);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					if ("daterange".equals(titlearray[j].toString())) {
						cell.setCellValue(param.get("fromDate") + "到" + param.get("endDate"));
						continue;
					}
					value = map.get(titlearray[j].toString());
					if (value != null) {
						if ("currency".equals(titlearray[j].toString())&&!"".equals(tocurrency)) {
							value = tocurrency;
						}
						
						if(value instanceof BigDecimal) {
								BigDecimal cellvalue = (BigDecimal)value;
								cellvalue=cellvalue.setScale(2, RoundingMode.HALF_UP);
								cell.setCellValue(cellvalue.doubleValue());
						}else if(value instanceof Integer) {
								Integer cellvalue = (Integer)value;
								cell.setCellValue(cellvalue);
						}else {
							   cell.setCellValue(value.toString());
						}
						if("profitrate".equals(titlearray[j].toString())) {
							BigDecimal principal =map.get("principal")!=null? new BigDecimal(map.get("principal").toString()):new BigDecimal("0");
							if(principal.floatValue()>0.0000001||principal.floatValue()<-0.0000001) {
							  	cell.setCellValue(new BigDecimal(map.get("profit").toString()).divide(principal,2,RoundingMode.FLOOR).toString() );
							}else {
								cell.setCellValue("0");
							}
							
						}
				 
					}
				}
			}
		} else {
			try {
				throw new Exception("没有数据可导出！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
 
  
}
