package com.wimoor.amazon.finances.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.finances.service.IAmzSettlementReportService;
import com.wimoor.amazon.report.mapper.AmzSettlementReportMapper;
import com.wimoor.amazon.report.pojo.entity.AmzSettlementReport;
@Service
public class AmzSettlementReportServiceImpl extends ServiceImpl<AmzSettlementReportMapper, AmzSettlementReport> implements IAmzSettlementReportService {
//	@Resource
//	AmzAmountDescriptionMapper amzAmountDescriptionMapper;
//
//	@Resource
//	IAmzFinConfigService amzFinConfigService;
//	@Resource
//	AmzAdvSumProductAdsMapper amzAdvSumProductAdsMapper;
//	AmzSettlementReport findOldOne(AmazonAuthority amazonAuthority, AmzSettlementReport report) {
//		Example example = new Example(AmzSettlementReport.class);
//		Criteria crit = example.createCriteria();
//		crit.andEqualTo("postedDate", report.getPostedDate());
//		crit.andEqualTo("settlementId", report.getSettlementId());
//		crit.andEqualTo("orderId", report.getOrderId());
//		crit.andEqualTo("marketplaceName", report.getMarketplaceName());
//		crit.andEqualTo("amountType", report.getAmountType());
//		crit.andEqualTo("amountDescription", report.getAmountDescription());
//		crit.andEqualTo("orderItemCode", report.getAmountDescription());
//		crit.andEqualTo("sku", report.getSku());
//		crit.andEqualTo("amazonAuthId", amazonAuthority.getId());
//		List<AmzSettlementReport> list = amzSettlementReportMapper.selectByExample(example);
//		if (list.size() > 0) {
//			return list.get(0);
//		}
//		return null;
//	}
//
//	public HashMap<String, HashMap<String, Object>> getAmzSummaryMonth(Map<String, Object> param) {
//		List<Map<String, Object>> resultItem = amzSettlementReportSummaryMonthMapper.settlementitem(param);
//		String tocurrency = (String) param.get("currency");
//		String shopid = (String) param.get("shopid");
//		HashMap<String, HashMap<String, Object>> result = new HashMap<String, HashMap<String, Object>>();
//		String fromDate = param.get("fromDate").toString().substring(2, 7);
//		for (Map<String, Object> item : resultItem) {
//			String posted_date = GeneralUtil.getStr(item.get("posted_date"));
//			String currency = GeneralUtil.getStr(item.get("currency"));
//			addMap(shopid, result, item, "in_principal", posted_date, fromDate, currency, tocurrency);
//			addMap(shopid, result, item, "in_other", posted_date, fromDate, currency, tocurrency);
//			addMap(shopid, result, item, "out_adv", posted_date, fromDate, currency, tocurrency);
//			addMap(shopid, result, item, "out_commission", posted_date, fromDate, currency, tocurrency);
//			addMap(shopid, result, item, "out_fba", posted_date, fromDate, currency, tocurrency);
//			addMap(shopid, result, item, "out_refund", posted_date, fromDate, currency, tocurrency);
//			addMap(shopid, result, item, "out_other", posted_date, fromDate, currency, tocurrency);
//			addMap(shopid, result, item, "converted_total", posted_date, fromDate, currency, tocurrency);
//			addFieldMap(result, item, "in_sum", posted_date, "in_other,in_principal");
//			addFieldMap(result, item, "out_sum", posted_date, "out_adv,out_commission,out_fba,out_refund,out_other");
//		}
//		sumFieldMap(result, "in_sum", fromDate);
//		sumFieldMap(result, "out_sum", fromDate);
//		convartRate(result, "in_principal");
//		convartRate(result, "out_adv", "in_principal");
//		return result;
//	}
//
//	private void sumFieldMap(HashMap<String, HashMap<String, Object>> result, String field, String fromDate) {
//		HashMap<String, Object> in_sum = result.get(field);
//		if (in_sum == null) {
//			return;
//		}
//		BigDecimal sumvalue = new BigDecimal("0");
//		for (Entry<String, Object> entry : in_sum.entrySet()) {
//			if (!fromDate.equals(entry.getKey())) {
//				BigDecimal allValue = new BigDecimal("0");
//				allValue = (BigDecimal) entry.getValue();
//				sumvalue = sumvalue.add(allValue);
//			}
//		}
//		in_sum.put("all", sumvalue);
//	}
//
//	private void convartRate(HashMap<String, HashMap<String, Object>> result, String key, String me) {
//		HashMap<String, Object> item = result.get(key);
//		HashMap<String, Object> itemMe = result.get(me);
//		HashMap<String, Object> itemRate = result.get(key + "Rate");
//		if (itemRate == null) {
//			itemRate = new HashMap<String, Object>();
//		}
//		if (item != null && itemMe != null) {
//			for (Entry<String, Object> entry : item.entrySet()) {
//				String posted_date = entry.getKey();
//				if (posted_date != null) {
//					BigDecimal value = (BigDecimal) entry.getValue();
//					Object oldv = itemMe.get(posted_date);
//					BigDecimal oldValue = new BigDecimal("0");
//					if (oldv != null) {
//						oldValue = (BigDecimal) oldv;
//					}
//					if (oldValue.compareTo(new BigDecimal("0")) == 0) {
//						value = new BigDecimal("0");
//					} else {
//						value = value.abs().divide(oldValue.abs(), BigDecimal.ROUND_HALF_UP, 4).multiply(new BigDecimal("100"));
//					}
//					itemRate.put(posted_date, value);
//				}
//			}
//		}
//		result.put(key + "Rate", itemRate);
//	}
//
//	private void convartRate(HashMap<String, HashMap<String, Object>> result, String key) {
//		HashMap<String, Object> item = result.get(key);
//		HashMap<String, Object> itemRate = result.get(key + "Rate");
//		if (itemRate == null) {
//			itemRate = new HashMap<String, Object>();
//		}
//		if (item != null) {
//			for (Entry<String, Object> entry : item.entrySet()) {
//				String posted_date = entry.getKey();
//				BigDecimal value = (BigDecimal) entry.getValue();
//				if ("all".equals(posted_date)) {
//					continue;
//				}
//				String[] datear = posted_date.split("-");
//				Integer year = Integer.valueOf(datear[0]);
//				Integer month = Integer.valueOf((datear[1]));
//				if (month > 1) {
//					month = month - 1;
//				} else {
//					month = 12;
//					year = year - 1;
//				}
//				String monthstr = month.toString();
//				if (month < 10) {
//					monthstr = "0" + monthstr;
//				}
//				String date = year + "-" + monthstr;
//				Object oldv = item.get(date);
//				BigDecimal oldValue = new BigDecimal("0");
//				if (oldv != null) {
//					oldValue = (BigDecimal) oldv;
//				}
//				if (oldValue.compareTo(new BigDecimal("0")) == 0) {
//					value = new BigDecimal("0");
//				} else {
//					value = value.subtract(oldValue).divide(oldValue, BigDecimal.ROUND_HALF_UP, 4).multiply(new BigDecimal("100"));
//				}
//				itemRate.put(posted_date, value);
//			}
//			result.put(key + "Rate", itemRate);
//		}
//	}
//
//	private void addMap(String shopid, HashMap<String, HashMap<String, Object>> result, Map<String, Object> item,
//			String column, String posted_date, String fromDate, String fromcurrency, String tocurrency) {
//		Object object = item.get(column);
//		BigDecimal value = new BigDecimal("0");
//		if (object != null) {
//			value = (BigDecimal) object;
//		}
//		BigDecimal currencyValue = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, value);
//		item.put(column, currencyValue);
//		HashMap<String, Object> colmap = result.get(column);
//		if (colmap == null) {
//			colmap = new HashMap<String, Object>();
//		}
//		Object colvalue = colmap.get(posted_date);
//		BigDecimal oldValue = new BigDecimal("0");
//		if (colvalue != null) {
//			oldValue = (BigDecimal) colvalue;
//		}
//		colmap.put(posted_date, oldValue.add(currencyValue));
//		Object colall = colmap.get("all");
//		BigDecimal allValue = new BigDecimal("0");
//		if (colall != null) {
//			allValue = (BigDecimal) colall;
//		}
//		if (!posted_date.equals(fromDate)) {
//			colmap.put("all", allValue.add(currencyValue));
//		}
//		result.put(column, colmap);
//	}
//	
//	private void addMap(HashMap<String, HashMap<String, Object>> result, Map<String, Object> item, String column, String posted_date, String fromDate ) {
//		Object object = item.get(column);
//		BigDecimal value = new BigDecimal("0");
//		if (object != null) {
//			value = (BigDecimal) object;
//		}
//		BigDecimal currencyValue =  value;
//		item.put(column, currencyValue);
//		HashMap<String, Object> colmap = result.get(column);
//		if (colmap == null) {
//			colmap = new HashMap<String, Object>();
//		}
//		Object colvalue = colmap.get(posted_date);
//		BigDecimal oldValue = new BigDecimal("0");
//		if (colvalue != null) {
//			oldValue = (BigDecimal) colvalue;
//		}
//		colmap.put(posted_date, oldValue.add(currencyValue));
//		Object colall = colmap.get("all");
//		BigDecimal allValue = new BigDecimal("0");
//		if (colall != null) {
//			allValue = (BigDecimal) colall;
//		}
//		if (!posted_date.equals(fromDate)) {
//			colmap.put("all", allValue.add(currencyValue));
//		}
//		result.put(column, colmap);
//	}
//
//	private void addFieldMap(HashMap<String, HashMap<String, Object>> result, Map<String, Object> item, String column,
//			String posted_date, String fields) {
//		HashMap<String, Object> colmap = result.get(column);
//		if (colmap == null) {
//			colmap = new HashMap<String, Object>();
//		}
//		String[] fieldarray = fields.split(",");
//		BigDecimal sumvalue = new BigDecimal("0");
//		for (String field : fieldarray) {
//			HashMap<String, Object> distMap = result.get(field);
//			Object colvalue = distMap.get(posted_date);
//			if (colvalue != null) {
//				BigDecimal oldColvalue = (BigDecimal) colvalue;
//				sumvalue = sumvalue.add(oldColvalue);
//			}
//		}
//		colmap.put(posted_date, sumvalue);
//		result.put(column, colmap);
//	}
//
//	private void addMap(String shopid, HashMap<String, HashMap<String, Object>> result, Map<String, Object> item,
//			String column, String posted_date,String fromcurrency, String tocurrency) {
//		Object object = item.get("amount");
//		BigDecimal value = new BigDecimal("0");
//		if (object != null) {
//			value = (BigDecimal) object;
//		}
//		BigDecimal currencyValue = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, value);
//		HashMap<String, Object> colmap = result.get(column);
//		if (colmap == null) {
//			colmap = new HashMap<String, Object>();
//		}
//		Object colvalue = colmap.get(posted_date);
//		BigDecimal oldValue = new BigDecimal("0");
//		if (colvalue != null) {
//			oldValue = (BigDecimal) colvalue;
//		}
//		colmap.put(posted_date, oldValue.add(currencyValue));
//		Object colall = colmap.get("all");
//		BigDecimal allValue = new BigDecimal("0");
//		if (colall != null) {
//			allValue = (BigDecimal) colall;
//		}
//		colmap.put("all", allValue.add(currencyValue));
//		result.put(column, colmap);
//	}
//
//	public HashMap<String, HashMap<String, Object>> getLacolSummaryMonth(Map<String, Object> param) {
//		String tocurrency = (String) param.get("currency");
//		String shopid = (String) param.get("shopid");
//		String groupid=null;
//		String marketplaceid=null;
//		if(param.get("groupid")!=null)groupid=param.get("groupid").toString();
//		if(param.get("marketplaceid")!=null)marketplaceid=param.get("marketplaceid").toString();
//		AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
//		if(auth!=null) {
//			param.put("amazonAuthId", auth.getId());
//			param.put("amazonauthid", auth.getId());
//		}
//		HashMap<String, HashMap<String, Object>> result = new HashMap<String, HashMap<String, Object>>();
//		List<Map<String, Object>> resultLocal = amzSettlementReportSummaryMonthMapper.localitem(param);
//		List<Map<String, Object>> resultLocal2 = shipInboundTransMapper.localitem(param);
//		if(resultLocal2 != null && resultLocal2.size() > 0) {
//			resultLocal.addAll(resultLocal2);
//		}
//		
//		String fromDate = param.get("fromDate").toString().substring(2, 7);
//		for (Map<String, Object> item : resultLocal) {
//			String name = GeneralUtil.getStr(item.get("name"));
//			String posted_date = GeneralUtil.getStr(item.get("posted_date"));
//			if (posted_date.equals(fromDate)) {
//				continue;
//			}
//			addMap(shopid, result, item, name, posted_date, "RMB", tocurrency);
//		}
//		return result;
//	}
//
//	public Map<String, Object> getSummaryAll(Map<String, Object> param) {
//		String toCurrency = (String) param.get("currency");
//		String shopid = (String) param.get("shopid");
//		this.setParamsSettlementDate(param);
//		Map<String,BigDecimal> map=exchangeRateHandlerService.currencyChangeRateByLocal(shopid,toCurrency);
//		param.put("currencyrate", map);
//		Map<String, Object> result = amzSettlementReportSummaryDayMapper.summaryall(param);
//		Map<String, Object> result2 = amzSettlementReportSummaryDayMapper.summaryReturnAll(param);
//		if (result == null) {
//			result = result2;
//		} else {
//			if (result2 != null) {
//				result.putAll(result2);
//			}
//		}
//		List<Map<String, Object>> localresult = amzSettlementReportSummaryDayMapper.summaryallp(param);
//		BigDecimal allprincipal = new BigDecimal("0");
//		if (localresult.size() > 0 && localresult != null) {
//			for (Map<String, Object> item : localresult) {
//				if(item!=null&&item.get("allprincipal")!=null) {
//					BigDecimal itemprincipal = (BigDecimal) item.get("allprincipal");
//					if (itemprincipal != null) {
//						allprincipal = allprincipal.add(itemprincipal);
//					}
//				}
//				
//			}
//		} else {
//			allprincipal = new BigDecimal("0");
//		}
//		if (result != null) {
//			result.put("allprincipal", allprincipal);
//		}
//		return result;
//	}
//
//	public Map<String, Object> getSummarySKU(Map<String, Object> param) {
//		String tocurrency = (String) param.get("currency");
//		String shopid = (String) param.get("shopid");
//		Map<String,BigDecimal> map=exchangeRateHandlerService.currencyChangeRateByLocal(shopid,tocurrency);
//		param.put("currencyrate", map);
//		List<Map<String, Object>> list = amzSettlementReportSummaryDayMapper.summaryday(param);
//		Map<String, Object> result = new HashMap<String, Object>();
//		String startTime = param.get("fromDate").toString();
//		String endTime = param.get("endDate").toString();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
//		Date date1 = null;
//		Date date2 = null;
//		try {
//			date1 = format.parse(startTime);
//			date2 = format.parse(endTime);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		int daysize = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar c = Calendar.getInstance();
//		c.setTime(GeneralUtil.getDatez(startTime));
//		for (int i = 1; i <= daysize + 1; i++, c.add(Calendar.DATE, 1)) {
//			String tempkey = sdf.format(c.getTime());
//			result.put(tempkey, new BigDecimal("0"));
//		}
//		for (Map<String, Object> item : list) {
//			String key = item.get("posted_date").toString();
//			if (item.get("amount") != null) {
//				BigDecimal amount = (BigDecimal) item.get("amount");
//				result.put(key, amount.setScale(2, BigDecimal.ROUND_HALF_UP));
//			} else {
//				result.put(key, 0);
//			}
//		}
//		return result;
//	}
//
//	public Map<String, Object> getSumNumsByDay(Map<String, Object> param, String charttype) {
//		String type = "";
//		Map<String, Object> maps = new HashMap<String, Object>();
//		this.setParamsSettlementDate(param);
//		List<Map<String, Object>> listMap = null;
//		if ("sales".equals(charttype)) {
//			type = "sales";
//			listMap = amzSettlementReportSummaryDayMapper.sumNumsByDay(param);
//		} else if ("refund".equals(charttype)) {
//			type = "refundnum";
//			listMap = amzSettlementReportSummaryDayMapper.sumReturnNumsByDay(param);
//		} else if ("order".equals(charttype)) {
//			type = "order_amount";
//			listMap = amzSettlementReportSummaryDayMapper.sumNumsByDay(param);
//		} else if ("principal".equals(charttype)) {
//			Map<String, Object> map = this.getSummarySKU(param);
//			listMap = new ArrayList<Map<String, Object>>();
//			for (Entry<String, Object> entry : map.entrySet()) {
//				Map<String, Object> item = new HashMap<String, Object>();
//				item.put("postdate", entry.getKey());
//				item.put(type, entry.getValue());
//				listMap.add(item);
//			}
//		}
//		Map<String, Object> result = new HashMap<String, Object>();
//		String startTime = param.get("fromDate").toString();
//		String endTime = param.get("endDate").toString();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
//		Date date1 = null;
//		Date date2 = null;
//		try {
//			date1 = format.parse(startTime);
//			date2 = format.parse(endTime);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		int daysize = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar c = Calendar.getInstance();
//		c.setTime(GeneralUtil.getDatez(startTime));
//		for (int i = 1; i <= daysize + 1; i++, c.add(Calendar.DATE, 1)) {
//			String tempkey = sdf.format(c.getTime());
//			result.put(tempkey, 0);
//		}
//		for (Map<String, Object> item : listMap) {
//			if (item.get(type) != null) {
//				String key = item.get("postdate").toString();
//				String amount = item.get(type).toString();
//				if (amount.indexOf(".") >= 0) {
//					int index = amount.indexOf(".");
//					String newamount = amount.substring(0, index);
//					result.put(key, Integer.parseInt(newamount));
//				} else {
//					result.put(key, Integer.parseInt(amount));
//				}
//			} else {
//				continue;
//			}
//		}
//		List<String> labels = new ArrayList<String>();
//		List<Object> datas = new ArrayList<Object>();
//		SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd");
//		Date datestart = null;
//		Date dateend = null;
//		try {
//			dateend = format.parse(endTime);
//			datestart = format.parse(startTime);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		int daysize2 = (int) ((dateend.getTime() - datestart.getTime()) / (1000 * 3600 * 24));
//		Calendar c2 = Calendar.getInstance();
//		c2.setTime(GeneralUtil.getDatez(startTime));
//		for (int i = 0; i <= daysize2; i++, c2.add(Calendar.DATE, 1)) {
//			String tempkey = format2.format(c2.getTime());
//			labels.add(i, tempkey);
//			datas.add(i, result.get(tempkey.replaceAll("/", "-")));
//		}
//		maps.put("datas", datas);
//		maps.put("labels", labels);
//		return maps;
//	}
//

//
//	public Map<String, Object> loadSummayMonthMaps(HashMap<String, HashMap<String, Object>> map,
//			HashMap<String, HashMap<String, Object>> map2, List<String> times) {
//		Map<String, Object> maps = new HashMap<String, Object>();
//		eachMaps("converted_total", map, times);
//		eachMaps("accountconverted_total", map, times);
//		eachMaps("in_principal", map, times);
//		eachMaps("in_other", map, times);
//		eachMaps("in_sum", map, times);
//		// eachMaps("original_total",map,times);
//		eachMaps("out_adv", map, times);
//		eachMaps("out_fba", map, times);
//		eachMaps("out_commission", map, times);
//		eachMaps("out_other", map, times);
//		eachMaps("out_sum", map, times);
//		maps.put("Maps", map);
//		List<String> localList = new ArrayList<String>();
//		for (String item : map2.keySet()) {
//			if (item != null && item != "") {
//				localList.add(item);
//			}
//		}
//		maps.put("localName", localList);
//		maps.put("localMaps", map2);
//		return maps;
//	}
//
//	public void eachMaps(String ftype, HashMap<String, HashMap<String, Object>> map, List<String> times) {
//		HashMap<String, Object> maplist = map.get(ftype);
//		if (maplist != null) {
//			for (int i = 0; i < times.size(); i++) {
//				if (maplist.get(times.get(i)) == null) {
//					maplist.put(times.get(i), 0);
//				}
//			}
//			if (maplist.get("all") == null) {
//				maplist.put("all", 0);
//			}
//		}
//	}
//
//	public List<String> initTimes(String endTime1, String startTime1) {
//		List<String> times = new ArrayList<String>();
//		String endTime = endTime1.substring(2, 7);
//		String startTime = startTime1.substring(2, startTime1.length());
//		String startyear = startTime.split("-")[0];
//		int startMonth = Integer.parseInt(startTime.split("-")[1]);
//		String endyear = endTime.split("-")[0];
//		int endMonth = Integer.parseInt(endTime.split("-")[1]);
//		if (startTime.equals(endTime)) {
//			times.add(startTime);
//			return times;
//		}
//		if (startyear.equals(endyear)) {
//			for (Integer i = startMonth; i < (endMonth + 1); i++) {
//				if (i < 10) {
//					times.add(startyear + "-0" + i.toString());
//				} else {
//					times.add(startyear + "-" + i.toString());
//				}
//			}
//		} else {
//			for (Integer i = startMonth; i < 13; i++) {
//				if (i < 10) {
//					times.add(startyear + "-0" + i.toString());
//				} else {
//					times.add(startyear + "-" + i.toString());
//				}
//			}
//			for (Integer i = 1; i < (endMonth + 1); i++) {
//				if (i < 10) {
//					times.add(endyear + "-0" + i.toString());
//				} else {
//					times.add(endyear + "-" + i.toString());
//				}
//			}
//		}
//		return times;
//	}
//
//	public List<Map<String, Object>> selectOtherin(Map<String, Object> param) {
//		String fromdate = param.get("fromDate").toString().replaceAll("/", "-");
//		String enddate = param.get("endDate").toString().replaceAll("/", "-");
//		String shopid = param.get("shopid").toString();
//		param.put("fromDate", fromdate);
//		param.put("endDate", enddate);
//		this.setParamsSettlementDate(param);
//		List<Map<String, Object>> list = amzSettlementReportSummaryMonthMapper.selectOtherin(param);
//		List<AmzAmountDescription> deslist = amzAmountDescriptionMapper.selectAll();
//		Map<String, String> descName = new HashMap<String, String>();
//		for (AmzAmountDescription item : deslist) {
//			descName.put(item.getEname(), item.getCname());
//		}
//		if (list != null && list.size() > 0) {
//			for (int i = 0; i < list.size(); i++) {
//				Map<String, Object> mapitem = list.get(i);
//				String currency = mapitem.get("currency").toString();
//				String tocurrency = param.get("currency").toString();
//				BigDecimal amount = (BigDecimal) mapitem.get("amount");
//				amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, currency, tocurrency, amount);
//				mapitem.put("amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP));
//				if (descName.containsKey(mapitem.get("amounttype"))) {
//					mapitem.put("camounttype", descName.get(mapitem.get("amounttype")));
//				}
//			}
//			List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
//			// 再累加
//			for (int i = 0; i < list.size(); i++) {
//				String ftype = list.get(i).get("amounttype").toString();
//				BigDecimal amount = (BigDecimal) list.get(i).get("amount");
//				if (i == 0) {
//					maps.add(list.get(i));
//				} else {
//					if (ftype.equals(list.get(i - 1).get("amounttype").toString())) {
//						BigDecimal oldamount = (BigDecimal) list.get(i - 1).get("amount");
//						amount = amount.add(oldamount);
//						list.get(i).put("amount", amount);
//						maps.remove(list.get(i - 1));
//						maps.add(list.get(i));
//					} else {
//						maps.add(list.get(i));
//					}
//				}
//			}
//			return maps;
//		} else {
//			return null;
//		}
//	}
//
//	public List<Map<String, Object>> selectOtherout(Map<String, Object> param) {
//		String fromdate = param.get("fromDate").toString().replaceAll("/", "-");
//		String enddate = param.get("endDate").toString().replaceAll("/", "-");
//		String shopid = param.get("shopid").toString();
//		param.put("fromDate", fromdate);
//		param.put("endDate", enddate);
//		this.setParamsSettlementDate(param);
//		List<Map<String, Object>> list = amzSettlementReportSummaryMonthMapper.selectOtherout(param);
//		List<AmzAmountDescription> deslist = amzAmountDescriptionMapper.selectAll();
//		Map<String, String> descName = new HashMap<String, String>();
//		for (AmzAmountDescription item : deslist) {
//			descName.put(item.getEname(), item.getCname());
//		}
//		if (list != null && list.size() > 0) {
//			for (int i = 0; i < list.size(); i++) {
//				Map<String, Object> mapitem = list.get(i);
//				String currency = mapitem.get("currency").toString();
//				String tocurrency = param.get("currency").toString();
//				BigDecimal amount = (BigDecimal) mapitem.get("amount");
//				amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, currency, tocurrency, amount);
//				mapitem.put("amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP));
//				if (descName.containsKey(mapitem.get("amounttype"))) {
//					mapitem.put("camounttype", descName.get(mapitem.get("amounttype")));
//				}
//			}
//			List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
//			// 再累加
//			for (int i = 0; i < list.size(); i++) {
//				String ftype = list.get(i).get("amounttype").toString();
//				BigDecimal amount = (BigDecimal) list.get(i).get("amount");
//				if (i == 0) {
//					maps.add(list.get(i));
//				} else {
//					if (ftype.equals(list.get(i - 1).get("amounttype").toString())) {
//						BigDecimal oldamount = (BigDecimal) list.get(i - 1).get("amount");
//						amount = amount.add(oldamount);
//						list.get(i).put("amount", amount);
//						maps.remove(list.get(i - 1));
//						maps.add(list.get(i));
//					} else {
//						maps.add(list.get(i));
//					}
//				}
//			}
//			return maps;
//		} else {
//			return null;
//		}
//	}
//
//	public PageList<Map<String, Object>> findSettlementAcc(Map<String, Object> map,PageBounds pagebounds) {
//		PageList<Map<String, Object>> list = amzSettlementAccReportMapper.findSettlementAcc(map,pagebounds);
//		String tocurrency = "RMB";
//		String shopid = map.get("shopid").toString();
//		if (map.get("currency") != null) {
//			tocurrency = map.get("currency").toString();
//		}
//		if (list.size() > 0 && list != null) {
//			for (Map<String, Object> item : list) {
//				if (item.get("currency") != null) {
//					String fromCurrency = item.get("currency").toString();
//					initAmount(shopid, item, fromCurrency, tocurrency);
//				}
//			}
//		}
//		return list;
//	}
//	
//	public Map<String, Object> findSettlementAccSum(Map<String, Object> map) {
//		List<Map<String, Object>> list = amzSettlementAccReportMapper.findSettlementAccSum(map);
//		String tocurrency = "RMB";
//		String shopid = map.get("shopid").toString();
//		if (map.get("currency") != null) {
//			tocurrency = map.get("currency").toString();
//		}
//		Map<String,Object> result=new HashMap<String,Object>();
//		result.put("currency", tocurrency);
//		if (list.size() > 0 && list != null) {
//			BigDecimal totalamount = new BigDecimal("0");
//			for (Map<String, Object> item : list) {
//				if (item.get("currency") != null) {
//					String fromCurrency = item.get("currency").toString();
//					initAmount(shopid, item, fromCurrency, tocurrency);
//					BigDecimal rmb = new BigDecimal("0");
//					if (item.get("totalAmount_to") != null) {
//						rmb = new BigDecimal(item.get("totalAmount_to").toString());
//						totalamount = totalamount.add(rmb);
//					}
//				}
//			}
//			result.put("acctotalsummary",totalamount);
//		}
//		return result;
//	}
//	public void initAmount(String shopid, Map<String, Object> item, String currency, String tocurrency) {
//		if (item != null && item.get("totalAmount") != null) {
//			BigDecimal amount = (BigDecimal) item.get("totalAmount");
//			amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, currency, tocurrency, amount);
//			item.put("totalAmount_to", amount.setScale(2, BigDecimal.ROUND_HALF_UP));
//		}
//	}
//
//	public AmzSettlementAccReport findSettlementByKey(String settlement_id, String amazonauthid, String marketplace_name) {
//		AmzSettlementAccReportKey key = new AmzSettlementAccReportKey();
//		key.setSettlementId(settlement_id);
//		key.setAmazonauthid(amazonauthid);
//		key.setMarketplaceName(marketplace_name);
//		return amzSettlementAccReportMapper.selectByPrimaryKey(key);
//	}
//
//	public SXSSFWorkbook downLoadSettlementRpt(Map<String, Object> parameter) throws BaseException {
//		SXSSFWorkbook workbook = new SXSSFWorkbook();
//		this.setParamsSettlementDate(parameter);
//		List<Map<String, Object>> list = amzSettlementReportMapper.findBySettlementAcc(parameter);
//		if (list != null) {
//			Sheet sheet = workbook.createSheet("sheet1");
//			Row trow = sheet.createRow(0);
//			Map<String, Object> map = list.get(0);
//			// 在索引0的位置创建行（最顶端的行）
//			String[] titlemap = { "settlement_id", "currency", "transaction_type", "order_id", "merchant_order_id",
//					"adjustment_id", "shipment_id", "marketplace_name", "amount_type", "amount_description", "amount",
//					"fulfillment_id", "posted_date", "posted_date_time", "order_item_code", "merchant_order_item_id",
//					"merchant_adjustment_item_id", "sku", "quantity_purchased", "promotion_id" };
//			for (int j = 0; j < titlemap.length; j++) {
//				Cell cell = trow.createCell(j); // 在索引0的位置创建单元格(左上端)
//				cell.setCellValue(titlemap[j]);
//			}
//			// Font f = new Font("宋体", Font.PLAIN, 12);
//			// FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(f);
//			for (int i = 0; i < list.size(); i++) {
//				Row row = sheet.createRow(i + 1);
//				map = list.get(i);
//				for (int j = 0; j < titlemap.length; j++) {
//					Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
//					Object value = map.get(titlemap[j]);
//					if (value != null) {
//						cell.setCellValue(value.toString());
//					}
//				}
//			}
//		} else {
//			throw new BaseException("没有数据可导出！");
//		}
//		return workbook;
//	}
//
//	public Map<String, Object> getDetail(Map<String, Object> param) {
//		this.setParamsSettlementDate(param);
//		List<Map<String, Object>> list = null;
//		if (param.get("sku") != null) {
//			if (param.get("isother") != null) {
//				list = amzSettlementReportMapper.getDetailDaySKUOther(param);
//			} else {
//				list = amzSettlementReportMapper.getDetailDaySKU(param);
//			}
//		} else if (param.get("settlement_id") != null) {
//			list = amzSettlementReportMapper.getDetail(param);
//		} else {
//			String shopid=param.get("shopid").toString();
//			List<AmazonAuthority> authlist=new ArrayList<AmazonAuthority> ();
//			if( param.get("market") != null) {
//				String market = param.get("market").toString();
//				Marketplace marketplace = marketplaceService.findMapByPoint().get(market);
//				if(param.get("groupid") != null) {
//					String groupid = param.get("groupid").toString();
//					AmazonAuthority amazonAuth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplace.getMarketplaceid());
//					authlist.add(amazonAuth);
//				}else {
//					authlist = amazonAuthorityService.selectByShopAndMarket(shopid,marketplace.getMarketplaceid());
//				}
//			}else {
//                if(param.get("groupid") != null) {
//                	String groupid = param.get("groupid").toString();
//                	Example example=new Example(AmazonAuthority.class);
//                	Criteria crit = example.createCriteria();
//                	crit.andEqualTo("shopId", shopid);
//                	crit.andEqualTo("groupid", groupid);
//                	authlist = amazonAuthorityService.selectByExample(example);
//                	
//				}else {
//					authlist = amazonAuthorityService.selectByshopid(shopid);
//				}
//			}
//			param.put("amazonAuths", authlist);
//			list = amzSettlementReportMapper.getDetailDay(param);
//		}
//		List<AmzAmountDescription> deslist = amzAmountDescriptionMapper.selectAll();
//		Map<String, String> descName = new HashMap<String, String>();
//		for (AmzAmountDescription item : deslist) {
//			descName.put(item.getEname(), item.getCname());
//		}
//		String currency = param.get("currency") != null&&!"undefined".equals(param.get("currency").toString()) ? param.get("currency").toString() : "RMB";
//		String shopid = param.get("shopid") != null ? param.get("shopid").toString() : null;
//		Map<String, Object> result = new HashMap<String, Object>();
//		TreeSet<String> date = new TreeSet<String>();
//		date.add("0000sum");
//		for (Map<String, Object> item : list) {
//			String transaction_type = null;
//			String amount_type = null;
//			String amount_description = null;
//			String ctransaction_type = null;
//			String camount_type = null;
//			String camount_description = null;
//			String ckey = "";
//			if (item.get("transaction_type") != null) {
//				transaction_type = item.get("transaction_type").toString();
//				ctransaction_type = descName.get(transaction_type);
//				if (ctransaction_type != null) {
//					ckey = ctransaction_type;
//				}else {
//					ckey=transaction_type;
//				}
//			}
//			if (item.get("amount_type") != null) {
//				amount_type = item.get("amount_type").toString();
//				camount_type = descName.get(amount_type);
//				if (camount_type != null) {
//					if (!"".equals(ckey)) {
//						ckey = ckey + "▶";
//					}
//					ckey = ckey + camount_type;
//				} 
//			}
//			if (item.get("amount_description") != null) {
//				amount_description = item.get("amount_description").toString();
//				camount_description = descName.get(amount_description);
//				if (camount_description != null) {
//					if (!"".equals(ckey)) {
//						ckey = ckey + "▶";
//					}
//					ckey = ckey + camount_description;
//				} 
//			}
//			String key = null;
//			if(!StringUtils.isEmpty(transaction_type)) {
//				key =transaction_type;
//			}
//			if(!StringUtils.isEmpty(amount_type)) {
//				if(!StringUtils.isEmpty(key)) {
//					key=key+">";
//				}
//				if(key.length()+amount_type.length()>20) {
//					key=key+"<br/>";
//				}
//				key=key+amount_type;
//			}
//			if(!StringUtils.isEmpty(amount_description)) {
//				if(!StringUtils.isEmpty(key)) {
//					key=key+">";
//				}
//				if(amount_description.length()>20) {
//					key=key+"<br/>";
//				}
//				key=key+amount_description;
//			}
//			
//			BigDecimal amount = exchangeRateHandlerService.changeCurrencyByLocal(shopid,
//					item.get("currency").toString(), currency, new BigDecimal(item.get("amount").toString()));
//			item.put("curamount", amount);
//			@SuppressWarnings("unchecked")
//			List<Map<String, Object>> itemlist = (List<Map<String, Object>>) result.get(key);
//			if (itemlist == null) {
//				itemlist = new ArrayList<Map<String, Object>>();
//				result.put(key, itemlist);
//			}
//			if (ckey != null) {
//				item.put("camount_description", ckey);
//			}
//			date.add(item.get("posted_date").toString());
//			itemlist.add(item);
//		}
//		Map<String, BigDecimal> totalDate = new HashMap<String, BigDecimal>();
//		BigDecimal sumtotalall = new BigDecimal("0");
//		BigDecimal cursumtotalall = new BigDecimal("0");
//		String fromcurrency = "";
//		for (Entry<String, Object> entry : result.entrySet()) {
//			@SuppressWarnings("unchecked")
//			List<Map<String, Object>> itemlist = (List<Map<String, Object>>) entry.getValue();
//			BigDecimal amounttotal = new BigDecimal("0");
//			for (Map<String, Object> item : itemlist) {
//				BigDecimal itemamount = item.get("amount") == null ? new BigDecimal("0") : new BigDecimal(item.get("amount").toString());
//				amounttotal = amounttotal.add(itemamount);
//				fromcurrency = item.get("currency").toString();
//				if (item.get("posted_date") != null) {
//					BigDecimal daytotal = totalDate.get(item.get("posted_date").toString());
//					if (daytotal == null) {
//						daytotal = new BigDecimal("0");
//					}
//					daytotal = daytotal.add(item.get("amount") == null ? new BigDecimal("0") : new BigDecimal(item.get("amount").toString()));
//					totalDate.put(item.get("posted_date").toString(), daytotal);
//				}
//				itemamount = exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, currency, itemamount);
//				item.put("amount", itemamount.setScale(2, BigDecimal.ROUND_DOWN));
//			}
//
//			BigDecimal curamounttotal = exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, currency, amounttotal);
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("amount", curamounttotal.setScale(2, BigDecimal.ROUND_DOWN));
//			map.put("posted_date", "0000sum");
//			map.put("amount_description", entry.getKey());
//			sumtotalall = sumtotalall.add(amounttotal);
//			cursumtotalall = cursumtotalall.add(curamounttotal);
//			itemlist.add(map);
//		}
//		Map<String, BigDecimal> curtotalByDay = new HashMap<String, BigDecimal>();
//		for (Entry<String, BigDecimal> entry : totalDate.entrySet()) {
//			curtotalByDay.put(entry.getKey(), exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency,
//					currency, entry.getValue(), 2));
//		}
//		cursumtotalall = cursumtotalall.setScale(2, BigDecimal.ROUND_DOWN);
//		sumtotalall = sumtotalall.setScale(2, BigDecimal.ROUND_DOWN);
//		curtotalByDay.put("0000sum", cursumtotalall);
//		totalDate.put("0000sum", cursumtotalall);
//		result.put("posted_date", date);
//		result.put("bydaysum", totalDate);
//		result.put("curbydaysum", curtotalByDay);
//		return result;
//	}
//
//	public HashMap<String, HashMap<String, Object>> loadSummayAccMaps(Map<String, Object> param) {
//		// TODO Auto-generated method stub
//		String tocurrency = (String) param.get("currency");
//		String shopid = (String) param.get("shopid");
//		String fromDate = param.get("fromDate").toString().substring(2, 7);
//		HashMap<String, HashMap<String, Object>> result = new HashMap<String, HashMap<String, Object>>();
//		Map<String, BigDecimal> echangeRateMap = exchangeRateHandlerService.currencyChangeRateByLocal(shopid, tocurrency);
//		param.put("currencyrate", echangeRateMap);
//		List<Map<String,Object>> list=amzSettlementAccReportMapper.sumSettlementAcc(param);
//		for(Map<String,Object> item:list) {
//			String posted_date = GeneralUtil.getStr(item.get("deposit_date"));
//			addMap(result, item,"accountconverted_total",posted_date, fromDate);
//		}
//		return result;
//	}
//
//	
//	void evaluatorCompileExpressionProfit(Map<String,Object> item,Expression compiledExp,List<Map<String, String>> fieldlist){
//		BigDecimal principal = item.get("principal") == null ? new BigDecimal("0") : new BigDecimal(item.get("principal").toString());
//		BigDecimal setincome = item.get("setincome") == null ? new BigDecimal("0") : new BigDecimal(item.get("setincome").toString());
//		BigDecimal price = item.get("price") == null ? new BigDecimal("0") : new BigDecimal(item.get("price").toString());
//		BigDecimal spend = item.get("spend") == null ? new BigDecimal("0") : new BigDecimal(item.get("spend").toString());
//		BigDecimal othersfee = new BigDecimal("0.00");
//		BigDecimal refund = item.get("refund") == null ? new BigDecimal("0") : new BigDecimal(item.get("refund").toString());
//		if (principal.floatValue() > 0.00001) {
//			item.put("refundrate", refund.multiply(new BigDecimal("100")).divide(principal, 2, BigDecimal.ROUND_HALF_UP));
//		} else {
//			item.put("refundrate", null);
//		}
//		BigDecimal shipmentfee = new BigDecimal("0");
//		if (item.get("shipmentfee") != null) {
//			shipmentfee = new BigDecimal(item.get("shipmentfee").toString());
//		}
//		BigDecimal firstShipment = new BigDecimal("0");
//		if (item.get("firstShipment") != null) {
//			firstShipment = new BigDecimal(item.get("firstShipment").toString());
//		}
//		if (item.get("othersfee") != null) {
//			othersfee = new BigDecimal(item.get("othersfee").toString());
//			if (item.get("otherCost") != null) {
//				othersfee = othersfee.add(new BigDecimal(item.get("otherCost").toString()));
//			}
//		}
//		item.put("shipmentfee", shipmentfee);
//		item.put("firstShipment", firstShipment);
//		item.put("othersfee", othersfee);
//		// 利润：结算收入-SP广告费用-采购成本-运费预估-其他费用
//		shipmentfee = shipmentfee.setScale(2, BigDecimal.ROUND_HALF_UP);
//		firstShipment = firstShipment.setScale(2, BigDecimal.ROUND_HALF_UP);
//		price = price.setScale(2, BigDecimal.ROUND_HALF_UP);
//		spend = spend.setScale(2, BigDecimal.ROUND_HALF_UP);
//		othersfee = othersfee.setScale(2, BigDecimal.ROUND_HALF_UP);
//		Map<String, Object> env = new HashMap<String, Object>();
//		env.put("setincome", setincome.doubleValue());
//		env.put("othersfee", othersfee.doubleValue());
//		env.put("spend", spend.doubleValue());
//		env.put("price", price.doubleValue());
//		env.put("shipmentfee", shipmentfee.doubleValue());
//		env.put("firstShipment", firstShipment.doubleValue());
//		env.put("storagefee", item.get("storagefee") == null ? Integer.parseInt("0") : new BigDecimal(item.get("storagefee").toString()));
//		env.put("longTermFee", item.get("longTermFee") == null ? Integer.parseInt("0") : new BigDecimal(item.get("longTermFee").toString()));
//		env.put("principal", item.get("principal") == null ? Integer.parseInt("0") : new BigDecimal(item.get("principal").toString()));
//		env.put("avgprice", item.get("avgprice") == null ? Integer.parseInt("0") : new BigDecimal(item.get("avgprice").toString()));
//		env.put("commission", item.get("commission") == null ? Integer.parseInt("0") : new BigDecimal(item.get("commission").toString()));
//		env.put("fbafee", item.get("fbafee") == null ? Integer.parseInt("0") : new BigDecimal(item.get("fbafee").toString()));
//		env.put("refund", item.get("refund") == null ? Integer.parseInt("0") : new BigDecimal(item.get("refund").toString()));
//		env.put("otherfee", item.get("otherfee") == null ? Integer.parseInt("0") : new BigDecimal(item.get("otherfee").toString()));
//		env.put("salenum", item.get("salenum") == null ? Integer.parseInt("0") : new BigDecimal(item.get("salenum").toString()));
//		env.put("ordernum", item.get("ordernum") == null ? Integer.parseInt("0") : new BigDecimal(item.get("ordernum").toString()));
//		env.put("vat", item.get("vat") == null ? Integer.parseInt("0") : new BigDecimal(item.get("vat").toString()));
//		env.put("itemshopfee", item.get("itemshopfee") == null ? Integer.parseInt("0") : new BigDecimal(item.get("itemshopfee").toString()));
//		for (Map<String, String> itemfield : fieldlist) {
//			if (item.get("field" + itemfield.get("value")) != null) {
//				if (item.get("field" + itemfield.get("value")) == null) {
//					throw new BaseException("公式中存在无法识别的字段,请到其他项中修改您的配置");
//				}
//				BigDecimal itemvalue = new BigDecimal(item.get("field" + itemfield.get("value")).toString());
//				env.put("field" + itemfield.get("value"), itemvalue);
//			} else {
//				env.put("field" + itemfield.get("value"), 0);
//			}
//		}
//		// 执行表达式
//		BigDecimal profit = new BigDecimal(compiledExp.execute(env).toString());
//		BigDecimal grossprofit = principal.subtract(price);
//
//		if (grossprofit.floatValue() == 0 || principal.floatValue() == 0) {
//			BigDecimal grossprofitrate = new BigDecimal("0.00");
//			item.put("grossprofitrate", grossprofitrate);
//		} else {
//			// 利润率：利润/销售额
//			BigDecimal grossprofitrate = grossprofit.multiply(new BigDecimal("100")).divide(principal, 2, BigDecimal.ROUND_HALF_UP);
//			item.put("grossprofitrate", grossprofitrate);
//		}
//		item.put("grossprofit", grossprofit);
//
//		if (principal.floatValue() == 0) {
//			BigDecimal profitrate = new BigDecimal("0.00");
//			item.put("profitrate", profitrate);
//		} else {
//			BigDecimal profitrate = profit.multiply(new BigDecimal("100")).divide(principal, 2, BigDecimal.ROUND_HALF_UP);
//			item.put("profitrate", profitrate);
//		}
//		item.put("profit", profit);
//	}
//	
//	public void changeValueExchangeRate(String shopid, Map<String, Object> item, String ftype, String currency, String tocurrency) {
//		if (item != null && item.get(ftype) != null) {
//			BigDecimal amount = (BigDecimal) item.get(ftype);
//			if (currency != null) {
//				amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, currency, tocurrency, amount, 10);
//			    item.put(ftype, amount.setScale(2, BigDecimal.ROUND_HALF_UP));
//			}
//		}
//	}
//	
////	public PageList<Map<String, Object>> findSettlementSummarySku(Map<String, Object> map,PageBounds bounds) {
////		String tocurrency = "RMB";
////		if (map.get("currency") != null) {
////			tocurrency = map.get("currency").toString();
////		}
////		String shopid = map.get("shopid").toString();
////		AmzFinSettlementFormula formula = amzFinConfigService.getAmzFinSettlementFormula(shopid);
////		Map<String,BigDecimal> currencyrate=exchangeRateHandlerService.currencyChangeRateByLocal(shopid,tocurrency);
////		map.put("currencyrate", currencyrate);
////		String field = formula.getField();
////		String expression = "setincome-" + formula.getFormula();
////		Expression compiledExp = AviatorEvaluator.compile(expression,true);
////		String[] fields = field.split(",");
////		List<Map<String, String>> fieldlist = new ArrayList<Map<String, String>>();
////		Map<String, String> finitemmap = AmzFinConfigServiceImpl.findSysFinMap();
////		for (int i = 0; i < fields.length; i++) {
////			if (!finitemmap.containsKey(fields[i])) {
////				Map<String, String> myvalue = new HashMap<String, String>();
////				myvalue.put("value", fields[i].trim());
////				myvalue.put("field", "field" + fields[i].trim());
////				fieldlist.add(myvalue);
////			}
////		}
////		
////		if(map.get("groupid")!=null&&map.get("marketplaceid")!=null) {
////			String groupid=map.get("groupid").toString();
////			String marketplaceid=map.get("marketplaceid").toString();
////			AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
////			map.put("amazonAuthId", auth.getId());
////		}
////
////		PageList<Map<String, Object>> result = amzSettlementSummarySkuMapper.findSettlementSummarySku(map,bounds);
////		for(Map<String, Object> item:result) {
////			map.putAll(item);
////			if(item.get("currency")==null)continue;
////			String currency=item.get("currency").toString();
////			BigDecimal advspend = amzSettlementSummarySkuMapper.findSettlementSummarySku_AdvSpend(map);
////			item.put("spend", advspend);
////			BigDecimal longTermFee = amzSettlementSummarySkuMapper.findSettlementSummarySku_LongtermStorage(map);
////			item.put("longTermFee", longTermFee);
////			BigDecimal storagefee = amzSettlementSummarySkuMapper.findSettlementSummarySku_StorageFee(map);
////			item.put("storagefee", storagefee);
////			Integer refundnum = amzSettlementSummarySkuMapper.findSettlementSummarySku_Returns(map);
////			item.put("refundnum", refundnum);
////			if(field!=null && field.contains("firstShipment")){
////				BigDecimal firstShipment = amzSettlementSummarySkuMapper.findSettlementSummarySku_FirstShipment(map);
////				item.put("firstShipment", firstShipment);
////			} 
////			if (fieldlist.size() > 0) {
////				map.put("fieldlist", fieldlist);
////				Map<String, Object> finItemData  = amzSettlementSummarySkuMapper.findSettlementSummarySku_FinItemData(map);
////				item.putAll(finItemData);
////			}
////			changeValueExchangeRate(shopid,item,"principal", currency,  tocurrency);
////			changeValueExchangeRate(shopid,item,"avgprice", currency,  tocurrency);
////			changeValueExchangeRate(shopid,item,"commission", currency,  tocurrency);
////			changeValueExchangeRate(shopid,item,"fbafee", currency,  tocurrency);
////			changeValueExchangeRate(shopid,item,"refund", currency,  tocurrency);
////			changeValueExchangeRate(shopid,item,"otherfee", currency,  tocurrency);
////			changeValueExchangeRate(shopid,item,"setincome", currency,  tocurrency);
////			changeValueExchangeRate(shopid,item,"price", "RMB",  tocurrency);
////			changeValueExchangeRate(shopid,item,"otherCost", "RMB",  tocurrency);
////			changeValueExchangeRate(shopid,item,"shipmentfee", currency,  tocurrency);
////			changeValueExchangeRate(shopid,item,"othersfee",currency,  tocurrency);
////		   evaluatorCompileExpressionProfit(item,compiledExp,fieldlist);
////		}
////		return result;
////	}
//	@Cacheable(value = "findSettlementSKUCache",key="#ekey")
//	public   HashMap<String, Object>  findSettlementSummary(String ekey,Map<String, Object> map) {
//		String	groupid= map.get("groupid")!=null?map.get("groupid").toString():null;
//		String	marketplaceid= map.get("marketplaceid")!=null?map.get("marketplaceid").toString():null;
//		HashMap<String,Object> result=new HashMap<String,Object>();
//		ArrayList<String> grouplist = new ArrayList<String>();
//		ArrayList<String> marketlist=new ArrayList<String>();
//		BigDecimal sum_advspend = new BigDecimal("0");
//		BigDecimal sum_longTermFee =new BigDecimal("0");
//		BigDecimal sum_storagefee =new BigDecimal("0");
//		BigDecimal sum_shopother=new BigDecimal("0");
//		if(groupid!=null) {
//			grouplist.add(groupid);
//		}else {
//			List<AmazonGroup> list = amazonGroupService.selectByShopId(map.get("shopid").toString());
//			for(AmazonGroup item:list) {
//				grouplist.add(item.getId());
//			}
//		}
//	for(String mgroupid:grouplist) {
//		marketlist.clear();
//		if(marketplaceid!=null) {
//			marketlist.add(marketplaceid);
//		}else {
//			  List<Marketplace> mlist = marketplaceService.findMarketplaceByGroup(mgroupid);
//			  for(Marketplace item:mlist) {
//				  marketlist.add(item.getMarketplaceid());
//			  }
//		 }
//		for(String marketplace:marketlist) {
//			Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplace);
//			String marketplace_name=market.getPointName();
//			map.put("groupid", mgroupid);
//			map.put("marketplaceid", marketplace);
//			map.put("marketplace_name", marketplace_name);
//			map.put("country", market.getMarket());
//			if(map.get("groupid")!=null&&map.get("marketplaceid")!=null) {
//				String tempgroupid=map.get("groupid").toString();
//				String tempmarketplaceid=map.get("marketplaceid").toString();
//				AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(tempgroupid, tempmarketplaceid);
//				if(auth==null) {
//					continue;
//				}
//				map.put("amazonAuthId", auth.getId());
//				map.put("amazonauthid", auth.getId());
//				map.put("sellerid", auth.getSellerid());
//			}
//			Map<String, Object> result_temp =  findSettlementSummary2(ekey,map);	
//			if(result_temp==null) {
//				continue;
//			}
//			for(Entry<String, Object> entry:result_temp.entrySet()) {
//				String key = entry.getKey();
//				Object oldvalue = result.get(key);
//				Object newvalue =entry.getValue();
//				if(oldvalue==null) {
//					result.put(key, newvalue);
//				}else {
//					if(newvalue!=null&&newvalue instanceof Integer) {
//						result.put(key, (Integer)oldvalue+(Integer)newvalue);
//					}
//					if(newvalue!=null&&newvalue instanceof BigDecimal) {
//						result.put(key, ((BigDecimal)oldvalue).add((BigDecimal)newvalue));
//					}
//					if(newvalue!=null&&newvalue instanceof List) {
//						 ((List)oldvalue).addAll((List)newvalue);
//						result.put(key,oldvalue);
//					}
//				}
//				
//			}
//		    List<Map<String, Object>> amounttype =(List<Map<String, Object>>) result_temp.get("amounttype");
//		    BigDecimal shop_othershare=new BigDecimal("0");
//		    BigDecimal shop_long_storage_fee=new BigDecimal("0");
//		    BigDecimal shop_storage_fee=new BigDecimal("0");
//		    BigDecimal shop_advspend_fee=new BigDecimal("0");
//				for(Map<String, Object> mitem:amounttype) {
//					if(mitem.get("nosku").toString().equals("1")) {
//						if("Storage Fee".equals(mitem.get("amount_description"))&& mitem.get("amount")!=null) {
//							shop_storage_fee=shop_storage_fee.add(new BigDecimal(mitem.get("amount").toString()).multiply(new BigDecimal("-1")));
//						}
//						else if("FBA Long-Term Storage".equals(mitem.get("amount_description"))&&mitem.get("amount")!=null) {
//							shop_long_storage_fee=shop_long_storage_fee.add(new BigDecimal(mitem.get("amount").toString()).multiply(new BigDecimal("-1")));
//						}
//						else if("Cost of Advertising".equals(mitem.get("amount_type"))&&mitem.get("amount")!=null) {
//							shop_advspend_fee=shop_advspend_fee.add(new BigDecimal(mitem.get("amount").toString()).multiply(new BigDecimal("-1")));
//						}
//						else if(!"Current Reserve Amount".equals(mitem.get("amount_description"))
//								&&!"Previous Reserve Amount Balance".equals(mitem.get("amount_description"))
//								&&!"Transfer of funds unsuccessful: Amazon has cancelled your transfer of funds.".equals(mitem.get("amount_description"))
//								&&mitem.get("amount")!=null) {
//							shop_othershare=shop_othershare.add(new BigDecimal(mitem.get("amount").toString()).multiply(new BigDecimal("-1")));
//						}
//					}
//				}
//		 
//			sum_advspend=sum_advspend.add(shop_advspend_fee);
//		    sum_longTermFee=sum_longTermFee.add(shop_long_storage_fee);
//		    sum_storagefee=sum_storagefee.add(shop_storage_fee);
//		    sum_shopother=sum_shopother.add(shop_othershare);
//		    
//		}
//	}
//	result.put("sum_advspend", sum_advspend);
//	result.put("sum_longTermFee", sum_longTermFee);
//	result.put("sum_storagefee", sum_storagefee);
//	result.put("sum_shopother", sum_shopother);
//	return result;
//	}
//	
// 	public   Map<String, Object>  findSettlementSummary2(String ekey,Map<String, Object> map) {
//		String tocurrency = "RMB";
//		if (map.get("currency") != null) {
//			tocurrency = map.get("currency").toString();
//		}
//		String shopid = map.get("shopid").toString();
//		Map<String,BigDecimal> currencyrate=exchangeRateHandlerService.currencyChangeRateByLocal(shopid,tocurrency);
//		map.put("currencyrate", currencyrate);
//		if(map.get("marketplaceid")!=null) {
//			Marketplace market = marketplaceService.findMapByMarketplaceId().get(map.get("marketplaceid").toString());
//			if(market!=null) {
//				map.put("country", market.getMarket());
//			}
//		}
//		
//		if(map.get("groupid")!=null&&map.get("marketplaceid")!=null) {
//			String groupid=map.get("groupid").toString();
//			String marketplaceid=map.get("marketplaceid").toString();
//			AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
//			if(auth==null)return null;
//			map.put("amazonAuthId", auth.getId());
//			map.put("amazonauthid", auth.getId());
//			map.put("sellerid", auth.getSellerid());
//		}
//		this.setParamsSettlementDate(map);
//		AmzFinSettlementFormula formula = amzFinConfigService.getAmzFinSettlementFormula(shopid);
//		map.put("pricetype", formula.getPricetype());
//		String field = formula.getField();
//		String expression = "setincome-" + formula.getFormula();
//		if(map.get("country")!=null) {
//			if("UK".equals(map.get("country").toString())) {
//				if(expression.contains("+vat")) {
//					expression=expression.replace("+vat", "");
//				}
//				if(expression.contains("-vat")) {
//					expression=expression.replace("-vat", "");
//				}
//				if(expression.contains("*vat")) {
//					expression=expression.replace("*vat", "");
//				}
//				if(expression.contains("/vat")) {
//					expression=expression.replace("/vat", "");
//				}
//				if(expression.contains("vat")) {
//					expression=expression.replace("vat", "0");
//				}
//			}
//		}
//		
//		Expression compiledExp = AviatorEvaluator.compile(expression,true);
//		String[] fields = field.split(",");
//		List<Map<String, String>> fieldlist = new ArrayList<Map<String, String>>();
//		Map<String, String> finitemmap = AmzFinConfigServiceImpl.findSysFinMap();
//		for (int i = 0; i < fields.length; i++) {
//			if (!finitemmap.containsKey(fields[i])) {
//				Map<String, String> myvalue = new HashMap<String, String>();
//				myvalue.put("value", fields[i].trim());
//				myvalue.put("field", "field" + fields[i].trim());
//				fieldlist.add(myvalue);
//			}
//		}
//
//		Map<String, Object> result = amzSettlementSummarySkuMapper.findSettlementSummary(map);
//		if(result==null)return null;
//		result.put("spend", new BigDecimal("0"));
//		result.put("longTermFee", new BigDecimal("0"));
//		result.put("storagefee", new BigDecimal("0"));
//		result.put("pricetype", formula.getPricetype());
//	    if(field!=null && field.contains("firstShipment")){
//				BigDecimal firstShipment = amzSettlementSummarySkuMapper.findSettlementSummarySku_FirstShipment(map);
//				result.put("firstShipment", firstShipment);
//		  } 
//		 if (fieldlist.size() > 0) {
//				map.put("fieldlist", fieldlist);
//				Map<String, Object> finItemData  = amzSettlementSummarySkuMapper.findSettlementSummarySku_FinItemData(map);
//				if(finItemData!=null&&finItemData.size()>0) {
//					result.putAll(finItemData);
//				}
//		 }
//	     List<Map<String, Object>> amounttype = amzSettlementSummarySkuMapper.findSettlementAmountTypeSummary(map);
// 
//	     BigDecimal setincome=new BigDecimal("0");
//	     BigDecimal principal=new BigDecimal("0");
//	 	 List<AmzAmountDescription> deslist = amzAmountDescriptionMapper.selectAll();
//		 Map<String, String> descName = new HashMap<String, String>();
//			for (AmzAmountDescription item : deslist) {
//				descName.put(item.getEname(), item.getCname());
//			}
//	     for(Map<String, Object> amountmap:amounttype) {
//	    	 set_camount_description(amountmap,descName);
//	    	 setincome=setincome.add(new BigDecimal(amountmap.get("amount").toString()));
//	    	 if(amountmap.get("amount_type").equals("ItemPrice")
//	    			 &&amountmap.get("transaction_type").equals("Order")
//	    			 &&amountmap.get("amount_description").equals("Principal")) {
//	    		 principal=principal.add(new BigDecimal(amountmap.get("amount").toString()));
//	    	 }
//	     }
//	     result.put("setincome", setincome);
//	     result.put("principal", principal);
//		 evaluatorCompileExpressionProfit(result,compiledExp,fieldlist);
//	     result.put("amounttype", amounttype);
//		 return result;
//	}
//	
//	
//	private void set_camount_description(Map<String, Object> amountmap, Map<String, String> descName) {
//		 String transaction_type = null;
//			String amount_type = null;
//			String amount_description = null;
//			String ctransaction_type = null;
//			String camount_type = null;
//			String camount_description = null;
//			String ckey = "";
//			if (amountmap.get("transaction_type") != null) {
//				transaction_type = amountmap.get("transaction_type").toString();
//				ctransaction_type = descName.get(transaction_type);
//				if (ctransaction_type != null) {
//					ckey = ctransaction_type;
//				}else {
//					ckey=transaction_type;
//				}
//			}
//			if (amountmap.get("amount_type") != null) {
//				amount_type = amountmap.get("amount_type").toString();
//				camount_type = descName.get(amount_type);
//				if (camount_type != null) {
//					if (!"".equals(ckey)) {
//						ckey = ckey + "▶";
//					}
//					ckey = ckey + camount_type;
//				} else {
//					if (!"".equals(ckey)) {
//						ckey = ckey + "▶";
//					}
//					ckey = ckey + amount_type;
//				}
//			}
//			if (amountmap.get("amount_description") != null) {
//				amount_description = amountmap.get("amount_description").toString();
//				camount_description = descName.get(amount_description);
//				if (camount_description != null) {
//					if (!"".equals(ckey)) {
//						ckey = ckey + "▶";
//					}
//					ckey = ckey + camount_description;
//				} else {
//					if (!"".equals(ckey)) {
//						ckey = ckey + "▶";
//					}
//					ckey = ckey + amount_description;
//				}
//			}
//			String key = null;
//			if(!StringUtils.isEmpty(transaction_type)) {
//				key =transaction_type;
//			}
//			if(!StringUtils.isEmpty(amount_type)) {
//				if(!StringUtils.isEmpty(key)) {
//					key=key+">";
//				}
//				if(key.length()+amount_type.length()>20) {
//					key=key+"<br/>";
//				}
//				key=key+amount_type;
//			}
//			if(!StringUtils.isEmpty(amount_description)) {
//				if(!StringUtils.isEmpty(key)) {
//					key=key+">";
//				}
//				if(amount_description.length()>20) {
//					key=key+"<br/>";
//				}
//				key=key+amount_description;
//			}
//			if(amountmap.get("maketpointname")!=null) {
//				ckey=amountmap.get("groupname").toString()+"("+amountmap.get("maketpointname").toString()+"):"+ckey;
//			}else {
//				ckey=amountmap.get("groupname").toString()+":"+ckey;
//			}
//		     amountmap.put("camount_description", ckey);
//	}
//	public void setParamsSettlementDate(Map<String, Object> map) {
//		if(map.get("datetype")!=null){
//			List<Map<String, Object>> datelist=amzSettlementAccReportMapper.findDateByAuth(map);
//			if(datelist!=null && datelist.size()>0) {
//				Map<String, Object> dateitem = datelist.get(0);
//				if(dateitem!=null) {
//					if(dateitem.get("fromdate")!=null) {
//						map.put("setfromDate", GeneralUtil.fmtDate((Date)dateitem.get("fromdate")));
//					}
//					if(dateitem.get("enddate")!=null) {
//						map.put("setendDate", GeneralUtil.fmtDate((Date)dateitem.get("enddate")));
//					}
//				}
//			}
//		}
//	}
//
//	public List<Map<String, Object>> getSettlementGroupOverAll(Map<String, Object> map) {
//		// TODO Auto-generated method stub
//		String systocurrency = "";
//		if (map.get("currency") != null) {
//			systocurrency = map.get("currency").toString();
//		}
//		String shopid = map.get("shopid").toString();
//		Map<String, Marketplace> marketplacemap = marketplaceService.findMapByPoint();
//		if(map.get("groupid")!=null&&map.get("marketplaceid")!=null) {
//			String groupid=map.get("groupid").toString();
//			String marketplaceid=map.get("marketplaceid").toString();
//			AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
//			if(auth==null) {
//				return null;
//			}
//			map.put("amazonAuthId", auth.getId());
//			map.put("amazonauthid", auth.getId());
//			map.put("sellerid", auth.getSellerid());
//		}
//		AmzFinSettlementFormula formula = amzFinConfigService.getAmzFinSettlementFormula(shopid);
//		map.put("pricetype", formula.getPricetype());
//		this.setParamsSettlementDate(map);
//	    List<Map<String, Object>> result = amzSettlementSummarySkuMapper.findSettlementOverall(map);
//	    Map<String, HashMap<String, Object>> group=new HashMap<String,HashMap<String,Object>>();
//		BigDecimal sprincipal =  new BigDecimal("0");
//    	BigDecimal scommission =  new BigDecimal("0");
//    	BigDecimal sfbafee = new BigDecimal("0");
//    	BigDecimal srefund = new BigDecimal("0");
//    	BigDecimal sadvfee = new BigDecimal("0");
//    	BigDecimal sstoragefee = new BigDecimal("0");
//    	BigDecimal ssetin = new BigDecimal("0");
//    	BigDecimal sother=new BigDecimal("0");
//    	BigDecimal sprice=new BigDecimal("0");
//    	BigDecimal sprofit=new BigDecimal("0");
//	    for(Map<String, Object> item:result) {
//	     	String fromcurrency = item.get("currency")==null?null:item.get("currency").toString();
//	     	String tocurrency = "";
//	     	if(StringUtils.isEmpty(systocurrency)) {
//	     		tocurrency=fromcurrency;
//	     	}else {
//	     		tocurrency=systocurrency;
//	     	}
//	
//	     	item.put("tocurrency", tocurrency);
//	    	BigDecimal price=new BigDecimal("0");
//	    	BigDecimal otherCost=new BigDecimal("0");
// 			Map<String,BigDecimal> currencyrate=exchangeRateHandlerService.currencyChangeRateByLocal(shopid,tocurrency);
// 			map.put("currencyrate", currencyrate);
// 	     	
// 	     	map.put("amazonAuthId", item.get("amazonauthid"));
// 	     	map.put("amazonauthid", item.get("amazonauthid"));
// 	     	
// 	     	map.put("marketplace_name", item.get("marketplace_name"));
// 	   	    map.put("marketplaceid", item.get("marketplace_name"));
//	    	Map<String, Object> mymap = amzSettlementSummarySkuMapper.findSettlementSummary(map);
//	    	if(mymap!=null) {
//	    		if(mymap.get("price")!=null) {
//	    			price=new BigDecimal(mymap.get("price").toString());
//	    		}
//	    		if(mymap.get("otherCost")!=null) {
//	    			otherCost=new BigDecimal(mymap.get("otherCost").toString());
//	    		}
//	    	}
//	    	String groupid = item.get("pid")==null?null:item.get("pid").toString();
//	    	String marketplace_name = item.get("marketplace_name")==null?null:item.get("marketplace_name").toString();
//	    	BigDecimal principal = item.get("principal")==null?new BigDecimal("0"):new BigDecimal(item.get("principal").toString());
//	    	BigDecimal commission = item.get("commission")==null?new BigDecimal("0"):new BigDecimal(item.get("commission").toString());
//	    	BigDecimal fbafee = item.get("fbafee")==null?new BigDecimal("0"):new BigDecimal(item.get("fbafee").toString());
//	    	BigDecimal refund = item.get("refund")==null?new BigDecimal("0"):new BigDecimal(item.get("refund").toString());
//	    	BigDecimal advfee = item.get("advfee")==null?new BigDecimal("0"):new BigDecimal(item.get("advfee").toString());
//	    	BigDecimal storagefee = item.get("storagefee")==null?new BigDecimal("0"):new BigDecimal(item.get("storagefee").toString());
//	    	BigDecimal setin = item.get("setin")==null?new BigDecimal("0"):new BigDecimal(item.get("setin").toString());
//	    	BigDecimal tax = item.get("tax")==null?new BigDecimal("0"):new BigDecimal(item.get("tax").toString());
//	    	BigDecimal other=new BigDecimal("0");
//	    	
//	 
//	    	if("Amazon.fr".equals(marketplace_name)||
//	    	   "Amazon.nl".equals(marketplace_name)||
//	    	   "Amazon.co.uk".equals(marketplace_name)||
//	    	   "Amazon.de".equals(marketplace_name)||
//	    	   "Amazon.es".equals(marketplace_name)||
//	    	   "Amazon.it".equals(marketplace_name) 
//	    		) {
//	    		principal=principal.add(tax);
//	    	}
//	    	other=setin.subtract(storagefee).subtract(advfee).subtract(refund).subtract(fbafee).subtract(commission).subtract(principal);
//	    	
//	    	principal=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, principal, 2);
//	    	commission=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, commission, 2);
//	    	fbafee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, fbafee, 2);
//	    	refund=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, refund, 2);
//	    	advfee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, advfee, 2);
//	    	storagefee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, storagefee, 2);
//	    	setin=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, setin, 2);
//	    	other=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, other, 2);
//	    	price=price.add(otherCost);
//	    	BigDecimal profit = setin.subtract(price);
//	    	sprincipal =sprincipal.add(principal);
//	    	scommission =  scommission.add(commission);
//	    	sfbafee =  sfbafee.add(fbafee);
//	    	srefund = srefund.add(refund);
//	    	sadvfee =  sadvfee.add(advfee);
//	    	sstoragefee =  sstoragefee.add(storagefee);
//	    	ssetin = ssetin.add(setin);
//	    	sother=sother.add(other);
//	    	sprice=sprice.add(price);
//	    	sprofit=sprofit.add(profit);
//	    	item.put("principal", principal);
//	    	item.put("commission", commission);
//	    	item.put("fbafee", fbafee);
//	    	item.put("refund", refund);
//	    	item.put("advfee", advfee);
//	    	item.put("storagefee", storagefee);
//	    	item.put("setin", setin);
//	    	item.put("other", other);
//	    	item.put("price", price);
//	    	item.put("profit", profit);
//	    	Marketplace market = marketplacemap.get(marketplace_name);
//	    	if(market!=null) {
//	    		item.put("name", market.getName());
//	    	}else {
//	    		item.put("name", marketplace_name);
//	    	}
//	    	HashMap<String, Object> mapgroup = group.get(groupid);
//	    	if(mapgroup==null) {
//	    		mapgroup=new HashMap<String, Object>();
//	    		AmazonGroup amzgroups = amazonGroupService.selectByKey(groupid);
//	    		mapgroup.put("id", groupid);
//	    		mapgroup.put("pid", "0");
//	    		mapgroup.put("name", amzgroups.getName());
//	    		group.put(groupid,mapgroup);
//	    	}
//	    	BigDecimal gprincipal = mapgroup.get("principal")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("principal").toString());
//	    	BigDecimal gcommission = mapgroup.get("commission")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("commission").toString());
//	    	BigDecimal gfbafee = mapgroup.get("fbafee")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("fbafee").toString());
//	    	BigDecimal grefund = mapgroup.get("refund")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("refund").toString());
//	    	BigDecimal gadvfee = mapgroup.get("advfee")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("advfee").toString());
//	    	BigDecimal gstoragefee = mapgroup.get("storagefee")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("storagefee").toString());
//	    	BigDecimal gsetin = mapgroup.get("setin")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("setin").toString());
//	    	BigDecimal gother=mapgroup.get("other")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("other").toString());
//	    	BigDecimal gprice=mapgroup.get("price")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("price").toString());
//	    	BigDecimal gprofit=mapgroup.get("profit")==null?new BigDecimal("0"):new BigDecimal(mapgroup.get("profit").toString());
//	    	
//	    	mapgroup.put("principal",gprincipal.add(principal));
//	    	mapgroup.put("commission",gcommission.add(commission));
//	    	mapgroup.put("fbafee",gfbafee.add(fbafee));
//	    	mapgroup.put("refund",grefund.add(refund));
//	    	mapgroup.put("advfee",gadvfee.add(advfee));
//	    	mapgroup.put("storagefee",gstoragefee.add(storagefee));
//	    	mapgroup.put("setin",gsetin.add(setin));
//	    	mapgroup.put("other",gother.add(other));
//	    	mapgroup.put("price",gprice.add(price));
//	    	mapgroup.put("profit",gprofit.add(profit));
//	    }
//		for(Entry<String, HashMap<String, Object>> entry:group.entrySet()) {
//			result.add(entry.getValue());
//		}
//		LinkedList<Map<String, Object>> result2 = new LinkedList<Map<String,Object>>();
//		for(int i=0;i<result.size();i++) {
//			Map<String, Object> mgroup = result.get(i);
//			if(mgroup.get("pid").toString().equals("0")) {
//				String id = mgroup.get("id").toString();
//				result2.add(mgroup);
//				for(int j=0;j<result.size();j++) {
//					Map<String, Object> mmgroup = result.get(j);
//					String pid=mmgroup.get("pid").toString();
//				    if(pid.equals(id)) {
//				    	result2.add(mmgroup);
//				    }
//				}
//			}
//		}
//		if(result2.size()>0) {
//			Map<String, Object> firstmap = result2.get(0);
//			firstmap.put("sprincipal", sprincipal);
//			firstmap.put("scommission", scommission);
//			firstmap.put("sfbafee", sfbafee);
//			firstmap.put("srefund", srefund);
//			firstmap.put("sadvfee", sadvfee);
//			firstmap.put("sstoragefee", sstoragefee);
//			firstmap.put("ssetin", ssetin);
//			firstmap.put("sother", sother);
//			firstmap.put("sprice", sprice);
//			firstmap.put("sprofit", sprofit);
//		}
//		return result2;
//	}
//	
//	public List<Map<String, Object>> downloadSettlementGroupOverAll(Map<String, Object> map) {
//		// TODO Auto-generated method stub
//		String systocurrency = "";
//		if (map.get("currency") != null) {
//			systocurrency = map.get("currency").toString();
//		}
//		String shopid = map.get("shopid").toString();
//		Map<String, Marketplace> marketplacemap = marketplaceService.findMapByPoint();
//		if(map.get("groupid")!=null&&map.get("marketplaceid")!=null) {
//			String groupid=map.get("groupid").toString();
//			String marketplaceid=map.get("marketplaceid").toString();
//			AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
//			if(auth==null) {
//				return null;
//			}
//			map.put("amazonAuthId", auth.getId());
//			map.put("amazonauthid", auth.getId());
//			map.put("sellerid", auth.getSellerid());
//		}
//		AmzFinSettlementFormula formula = amzFinConfigService.getAmzFinSettlementFormula(shopid);
//		map.put("pricetype", formula.getPricetype());
//		this.setParamsSettlementDate(map);
//	    List<Map<String, Object>> result = amzSettlementSummarySkuMapper.findSettlementOverall(map);
//	    for(Map<String, Object> item:result) {
//	     	String fromcurrency = item.get("currency")==null?null:item.get("currency").toString();
//	     	String tocurrency = "";
//	     	if(StringUtils.isEmpty(systocurrency)) {
//	     		tocurrency=fromcurrency;
//	     	}else {
//	     		tocurrency=systocurrency;
//	     	}
//	     	item.put("tocurrency", tocurrency);
//	    	BigDecimal price=new BigDecimal("0");
// 			Map<String,BigDecimal> currencyrate=exchangeRateHandlerService.currencyChangeRateByLocal(shopid,tocurrency);
// 			map.put("currencyrate", currencyrate);
// 	     	map.put("amazonAuthId", item.get("amazonauthid"));
// 	     	map.put("amazonauthid", item.get("amazonauthid"));
// 	     	map.put("marketplace_name", item.get("marketplace_name"));
// 	   	    map.put("marketplaceid", item.get("marketplace_name"));
//	    	Map<String, Object> mymap = amzSettlementSummarySkuMapper.findSettlementSummary(map);
//	    	if(mymap!=null) {
//	    		if(mymap.get("price")!=null) {
//	    			price=new BigDecimal(mymap.get("price").toString());
//	    		}
//	    	}
//	    	String marketplace_name = item.get("marketplace_name")==null?null:item.get("marketplace_name").toString();
//	    	BigDecimal principal = item.get("principal")==null?new BigDecimal("0"):new BigDecimal(item.get("principal").toString());
//	    	BigDecimal commission = item.get("commission")==null?new BigDecimal("0"):new BigDecimal(item.get("commission").toString());
//	    	BigDecimal fbafee = item.get("fbafee")==null?new BigDecimal("0"):new BigDecimal(item.get("fbafee").toString());
//	    	BigDecimal refund = item.get("refund")==null?new BigDecimal("0"):new BigDecimal(item.get("refund").toString());
//	    	BigDecimal advfee = item.get("advfee")==null?new BigDecimal("0"):new BigDecimal(item.get("advfee").toString());
//	    	BigDecimal storagefee = item.get("storagefee")==null?new BigDecimal("0"):new BigDecimal(item.get("storagefee").toString());
//	    	BigDecimal setin = item.get("setin")==null?new BigDecimal("0"):new BigDecimal(item.get("setin").toString());
//	    	BigDecimal tax = item.get("tax")==null?new BigDecimal("0"):new BigDecimal(item.get("tax").toString());
//	    	BigDecimal shipcharge = item.get("shipcharge")==null?new BigDecimal("0"):new BigDecimal(item.get("shipcharge").toString());
//	    	BigDecimal reserve = item.get("reserve")==null?new BigDecimal("0"):new BigDecimal(item.get("reserve").toString());
//	    	BigDecimal savefee = item.get("savefee")==null?new BigDecimal("0"):new BigDecimal(item.get("savefee").toString());
//	    	BigDecimal untransfer = item.get("untransfer")==null?new BigDecimal("0"):new BigDecimal(item.get("untransfer").toString());
//			
//	    	BigDecimal other=new BigDecimal("0");
//	    	if("Amazon.fr".equals(marketplace_name)||
//	    	   "Amazon.nl".equals(marketplace_name)||
//	    	   "Amazon.co.uk".equals(marketplace_name)||
//	    	   "Amazon.de".equals(marketplace_name)||
//	    	   "Amazon.es".equals(marketplace_name)||
//	    	   "Amazon.it".equals(marketplace_name) 
//	    		) {
//	    		principal=principal.add(tax);
//	    	}
//	    	other=setin.subtract(storagefee).subtract(advfee).subtract(refund)
//	    			.subtract(fbafee).subtract(commission).subtract(principal)
//	    			.subtract(shipcharge).subtract(reserve).subtract(savefee).subtract(untransfer);
//	    	principal=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, principal, 2);
//	    	commission=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, commission, 2);
//	    	fbafee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, fbafee, 2);
//	    	refund=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, refund, 2);
//	    	advfee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, advfee, 2);
//	    	storagefee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, storagefee, 2);
//	    	setin=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, setin, 2);
//	    	other=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, other, 2);
//	    	shipcharge=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, shipcharge, 2);
//	    	reserve=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, reserve, 2);
//	    	savefee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, savefee, 2);
//	    	untransfer=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, untransfer, 2);
//	    	price=price.setScale(2, BigDecimal.ROUND_HALF_UP);
//	    	BigDecimal profit = setin.subtract(price);
//	    	item.put("principal", principal);
//	    	item.put("commission", commission);
//	    	item.put("fbafee", fbafee);
//	    	item.put("refund", refund);
//	    	item.put("advfee", advfee);
//	    	item.put("storagefee", storagefee);
//	    	item.put("setin", setin);
//	    	item.put("other", other);
//	    	item.put("price", price);
//	    	item.put("profit", profit);
//	    	item.put("shipcharge", shipcharge);
//	    	item.put("reserve", reserve);
//	    	item.put("savefee", savefee);
//	    	item.put("untransfer", untransfer);
//	    	
//	    	Marketplace market = marketplacemap.get(marketplace_name);
//	    	if(market!=null) {
//	    		item.put("marketname", market.getName());
//	    	}else {
//	    		item.put("marketname", marketplace_name);
//	    	}
//	    	AmazonGroup amzgroups = amazonGroupService.selectByKey(item.get("pid"));
//	    	item.put("groupname", amzgroups.getName());
//	    }
//		return result;
//	}
//	
//	public void initAmount(String shopid, Map<String, Object> item, String ftype, String currency, String tocurrency) {
//		if (item != null && item.get(ftype) != null) {
//			BigDecimal amount = (BigDecimal) item.get(ftype);
//			if (currency != null) {
//				amount = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, currency, tocurrency, amount, 10);
//				if (!ftype.equals("setincome")) {
//					item.put(ftype, amount.setScale(2, BigDecimal.ROUND_HALF_UP));
//				} else {
//					item.put(ftype, amount);
//				}
//			}
//		}
//	}
//	
//	@Cacheable(value = "findSettlementSKUCache", key = "#ekey")
//	public List<Map<String, Object>> findCommodity(String ekey,Map<String, Object> map) {
//		String tocurrency = "RMB";
//		if (map.get("currency") != null) {
//			tocurrency = map.get("currency").toString();
//		}
//		////-----------------目标币种获取---------------
//		Object isdownload = map.get("isdownload");
//		String shopid = map.get("shopid").toString();
//		Map<String,BigDecimal> currencyrate=exchangeRateHandlerService.currencyChangeRateByLocal(shopid,tocurrency);
//		map.put("currencyrate", currencyrate);
//		AmzFinSettlementFormula formula = amzFinConfigService.getAmzFinSettlementFormula(shopid);
//		String field = formula.getField();
//		if(field!=null && field.contains("firstShipment")){
//			map.put("hasShipment", "true");
//		} 
//		String expression = "setincome-" + formula.getFormula();
//		String[] fields = field.split(",");
//		List<Map<String, String>> fieldlist = new ArrayList<Map<String, String>>();
//		Map<String, String> finitemmap = AmzFinConfigServiceImpl.findSysFinMap();
//		for (int i = 0; i < fields.length; i++) {
//			if (!finitemmap.containsKey(fields[i])) {
//				Map<String, String> myvalue = new HashMap<String, String>();
//				myvalue.put("value", fields[i].trim());
//				myvalue.put("field", "field" + fields[i].trim());
//				fieldlist.add(myvalue);
//			}
//		}
//		Boolean hasspend = true;
//		map.put("hasspend", "true");
//		map.put("pricetype",formula.getPricetype());
//		//------------------------------------------配置获取
//		
//		String	groupid= map.get("groupid")!=null?map.get("groupid").toString():null;
//		String	marketplaceid= map.get("marketplaceid")!=null?map.get("marketplaceid").toString():null;
//		Set<String> grouplist = new HashSet<String>();
//		Set<String> marketlist=new HashSet<String>();
//		if(groupid!=null) {
//			grouplist.add(groupid);
//		}else {
//			List<AmazonGroup> list = amazonGroupService.selectByShopId(map.get("shopid").toString());
//			for(AmazonGroup item:list) {
//				grouplist.add(item.getId());
//			}
//		}
//	 List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
//	 for(String mgroupid:grouplist) {
//		marketlist.clear();
//		if(marketplaceid!=null) {
//			marketlist.add(marketplaceid);
//		}else {
//		  List<Marketplace> mlist = marketplaceService.findMarketplaceByGroup(mgroupid);
//		  for(Marketplace item:mlist) {
//			  marketlist.add(item.getMarketplaceid());
//		  }
//		}
//		for(String marketplace:marketlist) {
//			Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplace);
//			String marketplace_name=market.getPointName();
//			map.put("groupid", mgroupid);
//			map.put("marketplaceid", marketplace);
//			map.put("marketplace_name", marketplace_name);
//			if(map.get("marketplaceid")!=null) {
//				Marketplace tmepmarket = marketplaceService.findMapByMarketplaceId().get(map.get("marketplaceid").toString());
//				map.put("country", tmepmarket.getMarket());
//			}
//			if(map.get("groupid")!=null&&map.get("marketplaceid")!=null) {
//				String tempgroupid=map.get("groupid").toString();
//				String tempmarketplaceid=map.get("marketplaceid").toString();
//				AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(tempgroupid, tempmarketplaceid);
//				if(auth==null) {
//					continue;
//				}
//				map.put("amazonAuthId", auth.getId());
//				map.put("amazonauthid", auth.getId());
//				map.put("sellerid", auth.getSellerid());
//			}
//			setParamsSettlementDate(map);
//			List<Map<String, Object>> templist = amzSettlementSummarySkuMapper.findSettlementSummarySku(map);
//			if(templist!=null && templist.size()>0) {
//				list.addAll(templist);
//			  }
//		   }
//	   }
//       //-------------多站点多店铺查询合并
//		List<Map<String, Object>> finItemDataList = new ArrayList<Map<String, Object>>();
//		if (fieldlist.size() > 0) {
//			map.put("fieldlist", fieldlist);
//			finItemDataList = amzSettlementSummarySkuMapper.findFinItemDataByCondition(map);
//		}
//		Map<String, Map<String, Object>> userfindataMap = new HashMap<String, Map<String, Object>>();
//		for (Map<String, Object> finDataitem : finItemDataList) {
//			for (Map<String, String> itemfield : fieldlist) {
//				if (finDataitem.get("custcurrency") != null) {
//					initAmount(shopid, finDataitem, itemfield.get("field"), finDataitem.get("custcurrency").toString(), tocurrency);
//				}
//			}
//			String key = "";
//			if (finDataitem.get("sku") != null) {
//				key += finDataitem.get("sku").toString();
//			}
//			if (finDataitem.get("groupid") != null) {
//				key += finDataitem.get("groupid").toString();
//			}
//			if (finDataitem.get("marketplaceid") != null) {
//				key += finDataitem.get("marketplaceid").toString();
//			}
//			Map<String, Object> oldmap = userfindataMap.get(key);
//			if (oldmap == null) {
//				userfindataMap.put(key, finDataitem);
//			} else {
//				for (Map<String, String> itemfield : fieldlist) {
//					BigDecimal newone = new BigDecimal("0");
//					BigDecimal oldone = new BigDecimal("0");
//					Object value = finDataitem.get(itemfield.get("field"));
//					if (value != null)
//						newone = new BigDecimal(value.toString());
//					value = oldmap.get(itemfield.get("field"));
//					if (value != null)
//						oldone = new BigDecimal(value.toString());
//					oldmap.put(itemfield.get("field"), newone.add(oldone));
//				}
//			}
//		}
//		Map<String, Object> SumData = new HashMap<String, Object>();
//		// 编译表达式
//		Expression compiledExp = AviatorEvaluator.compile(expression,true);
//		List<Map<String, Object>> needremovelist=new ArrayList<Map<String, Object>>();
//		if (list.size() > 0 && list != null) {
//			User user=(User) map.get("user");
//			BigDecimal sum_storagefee=new BigDecimal("0");
//			BigDecimal sum_longTermFee=new BigDecimal("0");
//			BigDecimal sum_advspend=new BigDecimal("0");
//			for (int j=0;j< list.size();j++) {
//				Map<String, Object> item =list.get(j);
//				if(user!=null) {
//					double progress = (j*1.0)/(list.size()*1.0)*50;
//					user.setProgress(Math.ceil(progress));
//				}
//				if(item.get("sku").toString().equals("*")) {
//					if(isdownload==null||!"true".equals(isdownload.toString())) {
//						needremovelist.add(item);continue;
//					}
//				}
//				String key = "";
//				if (item.get("sku") != null) {
//					key += item.get("sku").toString();
//				}
//				if (item.get("groupid") != null) {
//					key += item.get("groupid").toString();
//				}
//				if (item.get("marketplaceid") != null) {
//					key += item.get("marketplaceid").toString();
//				}
//				if(item.get("asin")==null) {
//					map.put("asin", null);
//				}
//				if(item.get("fnsku")==null) {
//					map.put("fnsku", null);
//				}
//				map.putAll(item);
//				BigDecimal advspend = amzSettlementSummarySkuMapper.findSettlementSummarySku_AdvSpend(map);
//				if(advspend!=null) {
//					item.put("spend", advspend);
//					sum_advspend=sum_advspend.add(advspend);
//				}else {
//					item.put("spend", 0);
//				}
//				BigDecimal longTermFee =findSettlementSummarySku_LongtermStorage(map);
//				if(longTermFee!=null) {
//					item.put("longTermFee", longTermFee);
//					sum_longTermFee=sum_longTermFee.add(longTermFee);
//				}else {
//					item.put("longTermFee", 0);
//				}
//				BigDecimal storagefee =findSettlementSummarySku_StorageFee(map);
//				if(storagefee!=null) {
//					item.put("storagefee", storagefee);
//					sum_storagefee=sum_storagefee.add(storagefee);
//				}else {
//					item.put("storagefee", 0);
//				}
//	
//				if(user!=null) {
//					double progress = 50+(j*1.0)/(list.size()*1.0)*50;
//					user.setProgress(Math.ceil(progress));
//				}
//		        Map<String, Object> refundnumMap = amzSettlementSummarySkuMapper.findSettlementSummarySku_Returns(map);
//				item.put("refundnum", refundnumMap.get("refundnum"));
//				item.put("mfnqty", refundnumMap.get("mfnqty"));
//				if(field!=null && field.contains("firstShipment")){
//					BigDecimal firstShipment = amzSettlementSummarySkuMapper.findSettlementSummarySku_FirstShipment(map);
//					item.put("firstShipment", firstShipment);
//				} 
//				Map<String, Object> userfindata = userfindataMap.get(key);
//				if (userfindata != null) {
//					item.putAll(userfindata);
//					userfindataMap.remove(key);
//				}
//				item.put("pricetype",formula.getPricetype());
//				handlerSettlementSKURowData(shopid, item, tocurrency, hasspend, fieldlist, SumData, compiledExp,map);
//		 
//			}
//			for(Map<String, Object> item:needremovelist) {
//				list.remove(item);
//			}
//			for (Entry<String, Map<String, Object>> itementry : userfindataMap.entrySet()) {
//				Map<String, Object> item = itementry.getValue();
//				item.put("principal", new BigDecimal("0"));
//				item.put("currency", tocurrency);
//				handlerSettlementSKURowData(shopid, item, tocurrency, hasspend, fieldlist, SumData, compiledExp,map);
//				list.add(item);
//			}
//			if (list.size() > 0) {
//				sumSettlementSKURowData(fieldlist, SumData, list, hasspend);
//			}
//			if(user!=null) {
//				user.removeProgress();
//			}
//			return list;
//		} else {
//			for (Entry<String, Map<String, Object>> itementry : userfindataMap.entrySet()) {
//				Map<String, Object> item = itementry.getValue();
//				item.put("principal", new BigDecimal("0"));
//				item.put("currency", tocurrency);
//				handlerSettlementSKURowData(shopid, item, tocurrency, hasspend, fieldlist, SumData, compiledExp,map);
//				list.add(item);
//			}
//			if (list.size() > 0) {
//				sumSettlementSKURowData(fieldlist, SumData, list, hasspend);
//			}
//			return list;
//		}
//	}
// 
// 
//	private BigDecimal handlerSettlementSKUOthersFee( Map<String, Object> item,String shopid, String tocurrency,Map<String,Object> param) {
//		 /*</span><br/>进口GST税 
//				<br/>销售GST税 <br/>企业所得税<br/>VAT增值税(不含英国)<br/>
//				进口关税<br/>汇率损耗<br/>市场营销费用<br/>每单固定成本<br/>
//				其他每单销售固定费用<br/>本地SKU信息中维护的其他采购成本*/
//		//String fromCurrency = item.get("currency").toString();
//		BigDecimal principal=new BigDecimal(item.get("principal").toString());
//		String groupid=item.get("groupid").toString();
//		BigDecimal volume=(BigDecimal)item.get("volume");
//		String configid = profitCfgService.findDefaultPlanIdByGroup(groupid);
//		//add commit
//		BigDecimal itemsalenum=item.get("salenum")!=null?(BigDecimal) item.get("salenum"):new BigDecimal(0);
//		if(param.get("summarydata")!=null) {
//			@SuppressWarnings("unchecked")
//			Map<String, Object> sumdata =(Map<String,Object>)param.get("summarydata") ;
//			BigDecimal shopsum_advspend=sumdata.get("sum_advspend")!=null?(BigDecimal) sumdata.get("sum_advspend"):new BigDecimal(0);
//			BigDecimal shopsum_longTermFee=sumdata.get("sum_longTermFee")!=null?(BigDecimal) sumdata.get("sum_longTermFee"):new BigDecimal(0);
//			BigDecimal shopsum_storagefee=sumdata.get("sum_storagefee")!=null?(BigDecimal) sumdata.get("sum_storagefee"):new BigDecimal(0);
//			BigDecimal shopsum_shopother =sumdata.get("sum_shopother")!=null?(BigDecimal) sumdata.get("sum_shopother"):new BigDecimal(0);
//
//			BigDecimal salenum=(BigDecimal)sumdata.get("salenum");
//		    BigDecimal dim = (BigDecimal) sumdata.get("dim");
//		    BigDecimal itemStoragefee = new BigDecimal("0");
//		    BigDecimal itemlongStoragefee = new BigDecimal("0");
//		    if(volume!=null&&volume.floatValue()>0.00001) {
//		    		itemStoragefee = shopsum_storagefee.multiply(volume).divide(dim,14,BigDecimal.ROUND_FLOOR);
//		    		itemlongStoragefee = shopsum_longTermFee.multiply(volume).divide(dim,14,BigDecimal.ROUND_FLOOR);
//		    }
//	    	item.put("itemStoragefee", itemStoragefee);
//	    	item.put("itemlongStoragefee", itemlongStoragefee);
//		    BigDecimal itemadvspend = new BigDecimal("0");
//		    BigDecimal itemshopother = new BigDecimal("0");
//		    if(salenum!=null&&salenum.floatValue()>0.00001) {
//		    	  itemadvspend =shopsum_advspend.multiply(itemsalenum).divide(salenum,14,BigDecimal.ROUND_FLOOR) ;
//		    	  itemshopother = shopsum_shopother.multiply(itemsalenum).divide(salenum,14,BigDecimal.ROUND_FLOOR) ;
//		    }
//	        item.put("itemshopfee", itemStoragefee.add(itemlongStoragefee).add(itemadvspend).add(itemshopother));
//			item.put("itemadvspend", itemadvspend);
//			item.put("itemshopother", itemshopother);
//		}
//		List<ProfitConfigCountry> list = profitCfgCountryService.findByProfitId(configid);
//		ProfitConfigCountry mycountryconfig=null;
//		if(item.get("country")==null)return new BigDecimal(0);
//		String mycountry=item.get("country").toString();
//		String style=null;
//		for(ProfitConfigCountry mpitem:list) {
//			if(mycountry.equals(mpitem.getCountry())) {
//				mycountryconfig=mpitem;break;
//			}
//		}
//		ProfitConfig config=profitCfgService.selectById(configid);
//		BigDecimal shipmentfee=new BigDecimal("0");
//		BigDecimal salenum = item.get("salenum") == null ? new BigDecimal("0") : new BigDecimal(item.get("salenum").toString());
//		BigDecimal vat =new BigDecimal(0);
//		BigDecimal companytax=new BigDecimal(0);
//		BigDecimal customstax=new BigDecimal(0);
//		BigDecimal exchangelost=new BigDecimal(0);
//		BigDecimal marketfee=new BigDecimal(0);
//		BigDecimal lostrate=new BigDecimal(0);
//		BigDecimal otherfee=new BigDecimal(0);
//		if(mycountryconfig!=null) {
//			  if(mycountryconfig.getVatRate()!=null) {
//				  vat = principal.multiply(mycountryconfig.getVatRate()).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP);
//				  item.put("vat", vat);
//				  item.put("vat2", vat);
//			  }
//			  if(item.get("marketplaceid")!=null && "A1F83G8C2ARO7P".equals(item.get("marketplaceid"))) {
//					item.put("vat", new BigDecimal(0));
//			  }
//			  if(mycountryconfig.getLostrate()!=null) {
//				  exchangelost = principal.multiply(mycountryconfig.getLostrate()).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP);
//				  item.put("exchangelost", exchangelost);
//			  }
//			  if(mycountryconfig.getOtherfee()!=null) {
//				  otherfee=mycountryconfig.getOtherfee().multiply(salenum);
//				  item.put("motherfee", otherfee);
//			  }
//			  if(mycountryconfig.getCostrate()!=null) {
//				 lostrate = principal.multiply(mycountryconfig.getCostrate()).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP);
//				 item.put("lostrate", lostrate);
//			  }
//			  if(mycountryconfig.getSellerrate()!=null) {
//				  marketfee = principal.multiply(mycountryconfig.getSellerrate()).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP);
//				  item.put("marketfee", marketfee);
//			  }
//			  if(mycountryconfig.getCorporateInRate()!=null) {
//				  companytax = principal.multiply(mycountryconfig.getCorporateInRate()).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP);
//				  item.put("companytax", companytax);
//			  }
//			  if(mycountryconfig.getTaxrate()!=null) {
//				  customstax = principal.multiply(mycountryconfig.getTaxrate()).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP);
//				  item.put("customstax", customstax);
//			  }
//			  if(config!=null){
//				  style=config.getShipmentstyle();
//			  }  
//			  
//			  BigDecimal weight=(BigDecimal)item.get("weight");
//			  BigDecimal basedim=mycountryconfig.getConstantd();
//			  BigDecimal baseweight = mycountryconfig.getConstantw();
//			  BigDecimal basem1 = mycountryconfig.getConstantm();
//			  if("weight".equals(style)) {
//				  //重量
//				  shipmentfee=weight.multiply(baseweight).multiply(salenum);
//			  }else if("volume".equals(style)) {
//				  //材积
//				  shipmentfee=volume.divide(basedim,8,BigDecimal.ROUND_HALF_UP).multiply(basem1).multiply(salenum);
//			  }else if(style==null||"dim_weight".equals(style) || "manually".equals(style)) {
//				  //重量材积 取大者
//				  BigDecimal value1 = volume.divide(basedim,8,BigDecimal.ROUND_HALF_UP);
//				  BigDecimal value2 = weight;
//				  if(value1.compareTo(value2)>0) {
//					  shipmentfee=value1.multiply(basem1).multiply(salenum);
//				  }else {
//					  shipmentfee=value2.multiply(basem1).multiply(salenum);
//				  }
//			  } 
//			  shipmentfee = this.exchangeRateHandlerService.changeCurrencyByLocal(shopid, "CNY", tocurrency, shipmentfee, 10);
//			  item.put("shipmentfee", shipmentfee);	    
//		}
//		return   customstax.add(exchangelost).add(otherfee).add(lostrate).add(marketfee)
//				.add(companytax); 
//		
//	}
//	
//	@Cacheable(value = "findSettlementSKUCache", key = "#ekey")
//	public void handlerSettlementSKURowData(String shopid, Map<String, Object> item, String tocurrency,
//			boolean hasspend, List<Map<String, String>> fieldlist, Map<String, Object> SumData, Expression compiledExp,Map<String,Object> param) {
//		if (item.get("currency") != null) {
////			addOtherCostMap(item);
//			BigDecimal commission = item.get("commission") == null ? new BigDecimal("0") : new BigDecimal(item.get("commission").toString());
//            item.put("commission", commission.multiply(new BigDecimal("-1")));
//            item.put("tocurrency", tocurrency);
//			String fromCurrency = item.get("currency").toString();
//			initAmount(shopid, item, "principal", fromCurrency, tocurrency);
//			initAmount(shopid, item, "avgprice", fromCurrency, tocurrency);
//			initAmount(shopid, item, "commission", fromCurrency, tocurrency);
//			initAmount(shopid, item, "fbafee", fromCurrency, tocurrency);
//			initAmount(shopid, item, "refund", fromCurrency, tocurrency);
//			initAmount(shopid, item, "otherfee", fromCurrency, tocurrency);
//			initAmount(shopid, item, "setincome", fromCurrency, tocurrency);
//			initAmount(shopid, item, "otherCost", "RMB", tocurrency);
//			initAmount(shopid, item, "itemprice", "CNY", tocurrency);
////			if (hasspend) {
////				initAmount(shopid, item, "spend", fromCurrency, tocurrency);
////			}
//            initAmount(shopid, item, "firstShipment", "CNY", tocurrency);
//			initAmount(shopid, item, "shipmentfee", fromCurrency, tocurrency);
//			initAmount(shopid, item, "storagefee", fromCurrency, tocurrency);
// 			initAmount(shopid, item, "longTermFee", fromCurrency, tocurrency);
//			initAmount(shopid, item, "othersfee", fromCurrency, tocurrency);
//
//			// principal 销售额 setincome 结算收入 othersfee 其他成本 price 采购成本 spend SP广告费用
//			// shipmentfee 运费预估
//			BigDecimal sales = item.get("salenum") == null ? new BigDecimal("0") : new BigDecimal(item.get("salenum").toString());
//			BigDecimal principal = item.get("principal") == null ? new BigDecimal("0") : new BigDecimal(item.get("principal").toString());
//			if(sales!=null&&sales.floatValue()>0.001) {
//				item.put("avgprice", principal.divide(sales,2,BigDecimal.ROUND_HALF_UP));
//			}
//			BigDecimal setincome = item.get("setincome") == null ? new BigDecimal("0") : new BigDecimal(item.get("setincome").toString());
//			BigDecimal price =  new BigDecimal("0") ;
//			BigDecimal itemprice = item.get("itemprice") == null ? new BigDecimal("0") : new BigDecimal(item.get("itemprice").toString());
//			BigDecimal spend = new BigDecimal("0.00");
//			Integer refundnum = item.get("refundnum") == null ? new Integer("0") : new Integer(item.get("refundnum").toString());
//			Integer mfnqty = item.get("mfnqty") == null ? new Integer("0") : new Integer(item.get("mfnqty").toString());
//			Integer pricetype = item.get("pricetype") == null ? new Integer("1") : new Integer(item.get("pricetype").toString());
//			if(pricetype==1) {
//				price=new BigDecimal((sales.intValue()-refundnum+mfnqty)*itemprice.doubleValue());
//			}else if(pricetype==2) {
//				price=new BigDecimal((sales.intValue()-refundnum)*itemprice.doubleValue());
//			}else if(pricetype==3) {
//				price=new BigDecimal((sales.intValue())*itemprice.doubleValue());
//			}
//			item.put("price",  price.setScale(2, BigDecimal.ROUND_HALF_UP));
//			if (hasspend) {
//				spend = item.get("spend") == null ? new BigDecimal("0") : new BigDecimal(item.get("spend").toString());
//			}
//			BigDecimal othersfee = new BigDecimal("0.00");
//			BigDecimal refund = item.get("refund") == null ? new BigDecimal("0") : new BigDecimal(item.get("refund").toString());
//			if (principal.floatValue() > 0.00001) {
//				item.put("refundrate", refund.multiply(new BigDecimal("100")).divide(principal, 2, BigDecimal.ROUND_HALF_UP));
//			} else {
//				item.put("refundrate", null);
//			}
//
//			BigDecimal shipmentfee = new BigDecimal("0");
//			BigDecimal firstShipment = new BigDecimal("0");
//			if (item.get("firstShipment") != null) {
//				firstShipment = new BigDecimal(item.get("firstShipment").toString());
//			}
//			 othersfee = handlerSettlementSKUOthersFee(item,shopid,tocurrency,param);//刘雨林 加入其他费用
//				if (item.get("otherCost") != null) {
//					BigDecimal othercost = new BigDecimal(item.get("otherCost").toString());
//					BigDecimal salenum = item.get("salenum") == null ? new BigDecimal("0") : new BigDecimal(item.get("salenum").toString());
//					othercost=othercost.multiply(salenum);
//					item.put("otherCost", othercost);
//					othersfee = othersfee.add(othercost);
//				}
//		    initAmount(shopid, item, "motherfee", fromCurrency, tocurrency);
//			sales=sales.subtract(new BigDecimal(refundnum));
//			item.put("firstShipment", firstShipment);
//			item.put("othersfee", othersfee);
//			// 利润：结算收入-SP广告费用-采购成本-运费预估-其他费用
//			if (item.get("shipmentfee") != null) {
//				shipmentfee = new BigDecimal(item.get("shipmentfee").toString());
//				shipmentfee=shipmentfee.setScale(2, BigDecimal.ROUND_HALF_UP);
//				item.put("shipmentfee", shipmentfee);
//			}
//			firstShipment = firstShipment.setScale(2, BigDecimal.ROUND_HALF_UP);
//			price = price.setScale(2, BigDecimal.ROUND_HALF_UP);
//			spend = spend.setScale(2, BigDecimal.ROUND_HALF_UP);
//			othersfee = othersfee.setScale(2, BigDecimal.ROUND_HALF_UP);
//
//			Map<String, Object> env = new HashMap<String, Object>();
//			env.put("setincome", setincome.doubleValue());
//			env.put("othersfee", othersfee.doubleValue());
//			env.put("spend", spend.doubleValue());
//			env.put("price", price.doubleValue());
//			env.put("shipmentfee", shipmentfee.doubleValue());
//			env.put("firstShipment", firstShipment.doubleValue());
//			env.put("storagefee", item.get("storagefee") == null ? Integer.parseInt("0") : new BigDecimal(item.get("storagefee").toString()));
//			env.put("longTermFee", item.get("longTermFee") == null ? Integer.parseInt("0") : new BigDecimal(item.get("longTermFee").toString()));
//			env.put("principal", item.get("principal") == null ? Integer.parseInt("0") : new BigDecimal(item.get("principal").toString()));
//			env.put("avgprice", item.get("avgprice") == null ? Integer.parseInt("0") : new BigDecimal(item.get("avgprice").toString()));
//			env.put("commission", item.get("commission") == null ? Integer.parseInt("0") : new BigDecimal(item.get("commission").toString()));
//			env.put("fbafee", item.get("fbafee") == null ? Integer.parseInt("0") : new BigDecimal(item.get("fbafee").toString()));
//			env.put("refund", item.get("refund") == null ? Integer.parseInt("0") : new BigDecimal(item.get("refund").toString()));
//			env.put("otherfee", item.get("otherfee") == null ? Integer.parseInt("0") : new BigDecimal(item.get("otherfee").toString()));
//			env.put("salenum", item.get("salenum") == null ? Integer.parseInt("0") : new BigDecimal(item.get("salenum").toString()));
//			env.put("ordernum", item.get("ordernum") == null ? Integer.parseInt("0") : new BigDecimal(item.get("ordernum").toString()));
//			env.put("vat", item.get("vat") == null ? Integer.parseInt("0") : new BigDecimal(item.get("vat").toString()));
//			env.put("itemshopfee", item.get("itemshopfee") == null ? Integer.parseInt("0") : new BigDecimal(item.get("itemshopfee").toString()));
//			for (Map<String, String> itemfield : fieldlist) {
//				if (item.get("field" + itemfield.get("value")) != null) {
//					if (item.get("field" + itemfield.get("value")) == null) {
//						throw new BaseException("公式中存在无法识别的字段,请到其他项中修改您的配置");
//					}
//					BigDecimal itemvalue = new BigDecimal(item.get("field" + itemfield.get("value")).toString());
//					env.put("field" + itemfield.get("value"), itemvalue);
//				} else {
//					env.put("field" + itemfield.get("value"), 0);
//				}
//			}
//			// 执行表达式
//			
//			BigDecimal profit = new BigDecimal(compiledExp.execute(env).toString());
//			BigDecimal grossprofit = principal.subtract(price);
//
//			if (grossprofit.floatValue() == 0 || principal.floatValue() == 0) {
//				BigDecimal grossprofitrate = new BigDecimal("0.00");
//				item.put("grossprofitrate", grossprofitrate);
//			} else {
//				// 利润率：利润/销售额
//				BigDecimal grossprofitrate = grossprofit.divide(principal, 2, BigDecimal.ROUND_HALF_UP);
//				item.put("grossprofitrate", grossprofitrate);
//			}
//			item.put("grossprofit", grossprofit);
//
//			if (principal.floatValue() == 0) {
//				BigDecimal profitrate = new BigDecimal("0.00");
//				item.put("profitrate", profitrate);
//			} else {
//				BigDecimal profitrate = profit.divide(principal, 2, BigDecimal.ROUND_HALF_UP);
//				item.put("profitrate", profitrate);
//			}
//			item.put("profit", profit);
//			// 合计
//			addSumData(item, SumData);
//			setincome = setincome.setScale(2, BigDecimal.ROUND_HALF_UP);
//			item.put("setincome", setincome);
//		}else {
//			System.out.println("币种为空了---------------------------------------------");
//		}
//	}
//
//	public void addSumData(Map<String, Object> maps, Map<String, Object> SumDatamap) {
//		if (maps != null) {
//			for (Entry<String, Object> entry : maps.entrySet()) {
//				String key = entry.getKey();
//				String value = "0";
//				if (entry.getValue() != null) {
//					value = entry.getValue().toString().trim();
//				}
//				if (SumDatamap.containsKey(key)) {
//					if (key.equals("principal") || key.equals("commission") || key.equals("fbafee")
//							|| key.equals("refund") || key.equals("setincome") || key.equals("otherfee")
//							|| key.equals("spend") || key.equals("storagefee") || key.equals("longTermFee")
//							|| key.equals("shipmentfee") || key.equals("profit") || key.equals("othersfee")
//							|| key.equals("salenum") || key.equals("ordernum") || key.equals("refundnum")
//							|| key.equals("price") || key.contains("field") || key.contains("grossprofit")
//							|| key.equals("firstShipment") || key.equals("vat")|| key.equals("itemshopfee")) {
//
//						BigDecimal maptemp = new BigDecimal(value);
//						BigDecimal summaptemp = new BigDecimal(SumDatamap.get(key)==null?"0":SumDatamap.get(key).toString().trim());
//						SumDatamap.put(key, maptemp.add(summaptemp));
//					}
//				} else {
//					SumDatamap.put(key, maps.get(key));
//				}
//			}
//		}
//	}
//
//
//	public void sumSettlementSKURowData(List<Map<String, String>> fieldlist, Map<String, Object> SumData, List<Map<String, Object>> list, boolean hasspend) {
//		BigDecimal sumavgprice = new BigDecimal("0");
//		BigDecimal sumrrate = new BigDecimal("0");
//		BigDecimal sumprate = new BigDecimal("0");
//		BigDecimal sumgrossprate = new BigDecimal("0");
//		if (SumData.get("principal") != null && !"0.00".equals(SumData.get("principal").toString())) {
//			if (SumData.get("principal") != null && SumData.get("salenum") != null && !"0".equals(SumData.get("salenum").toString())) {
//				sumavgprice = new BigDecimal(SumData.get("principal").toString())
//						.divide(new BigDecimal(SumData.get("salenum").toString()), 2, BigDecimal.ROUND_HALF_UP);
//			}
//			if (SumData.get("refund") != null) {
//				sumrrate = new BigDecimal(SumData.get("refund").toString()).multiply(new BigDecimal(100))
//						.divide(new BigDecimal(SumData.get("principal").toString()), 2, BigDecimal.ROUND_HALF_UP);
//			}
//			if (SumData.get("profit") != null) {
//				sumprate = new BigDecimal(SumData.get("profit").toString()).multiply(new BigDecimal(100))
//						.divide(new BigDecimal(SumData.get("principal").toString()), 2, BigDecimal.ROUND_HALF_UP);
//			}
//			if (SumData.get("grossprofit") != null) {
//				sumgrossprate = new BigDecimal(SumData.get("grossprofit").toString()).multiply(new BigDecimal(100))
//						.divide(new BigDecimal(SumData.get("principal").toString()), 2, BigDecimal.ROUND_HALF_UP);
//			}
//		}
//		list.get(0).put("sumfrom_principal", SumData.get("principal"));
//		list.get(0).put("sumfrom_commission", SumData.get("commission"));
//		list.get(0).put("sumfrom_fbafee", SumData.get("fbafee"));
//		list.get(0).put("sumfrom_refund", SumData.get("refund"));
//		list.get(0).put("sumfrom_setincome", SumData.get("setincome"));
//		list.get(0).put("sumfrom_otherfee", SumData.get("otherfee"));
//		if (hasspend) {
//			list.get(0).put("sumfrom_spend", SumData.get("spend"));
//		}
//		list.get(0).put("sumfrom_storagefee", SumData.get("storagefee"));
//		list.get(0).put("sumfrom_longTermFee", SumData.get("longTermFee"));
//		list.get(0).put("sumfrom_shipmentfee", SumData.get("shipmentfee"));
//		list.get(0).put("sumfrom_firstShipment", SumData.get("firstShipment"));
//		list.get(0).put("sumfrom_profit", SumData.get("profit"));
//		list.get(0).put("sumfrom_othersfee", SumData.get("othersfee"));
//		list.get(0).put("sumfrom_salenum", SumData.get("salenum"));
//		list.get(0).put("sumfrom_ordernum", SumData.get("ordernum"));
//		list.get(0).put("sumfrom_refundnum", SumData.get("refundnum"));
//		list.get(0).put("sumfrom_refundrate", sumrrate);
//		list.get(0).put("sumfrom_price", SumData.get("price"));
//		list.get(0).put("sumfrom_profitrate", sumprate);
//		list.get(0).put("sumfrom_grossprofit", SumData.get("grossprofit"));
//		list.get(0).put("sumfrom_grossprofitrate", sumgrossprate);
//		list.get(0).put("sumfrom_vat", SumData.get("vat"));
//		list.get(0).put("sumfrom_itemshopfee", SumData.get("itemshopfee"));
//		list.get(0).put("sumfrom_avgprice", sumavgprice);
//		for (Map<String, String> itemfield : fieldlist) {
//			list.get(0).put("sumfrom_" + itemfield.get("field"), SumData.get(itemfield.get("field")));
//		}
//	}
//	private BigDecimal findSettlementSummarySku_LongtermStorage(Map<String, Object> map) {
//		String tocurrency = "RMB";
//		if (map.get("currency") != null) {
//			tocurrency = map.get("currency").toString();
//		}
//		String shopid = map.get("shopid").toString();
//		List<Map<String, Object>> result = amzSettlementSummarySkuMapper.findSettlementSummarySku_LongtermStorage(map);
//		if(result==null||result.size()==0)return new BigDecimal("0");
//		String endDate=(String) map.get("endDate");
//		String fromDate=(String) map.get("fromDate");
//		Calendar c = Calendar.getInstance();
//		Date end = GeneralUtil.getDatez(endDate);
//		c.setTime(end);
//		int hasEndDays = c.get(Calendar.DAY_OF_MONTH);
//		int monthEndDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
//		Date from = GeneralUtil.getDatez(fromDate);
//		c.setTime(GeneralUtil.getDatez(fromDate));
//		int hasFromDays = c.get(Calendar.DAY_OF_MONTH);
//		int monthFromDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
//		hasFromDays=monthFromDays-hasFromDays+1;
//		
//		BigDecimal longTermFee = new BigDecimal("0");
//	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//	    String endMonth=GeneralUtil.formatDate(end, sdf);
//	    String fromMonth=GeneralUtil.formatDate(from, sdf);
//		for(Map<String, Object> item:result) {
//			if(item.get("longTermFee")==null) {
//				continue;
//			}
//			Date snapshot_date=(Date)item.get("snapshot_date");
//			String month=GeneralUtil.formatDate(snapshot_date, sdf);
//			String fromcurrency=(String) item.get("currency");
//			BigDecimal itemlongTermFee = (BigDecimal) item.get("longTermFee");
//			if(month.contentEquals(fromMonth)) {
//				if(hasFromDays>1) {
//					itemlongTermFee=itemlongTermFee.divide(new BigDecimal(monthFromDays),2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(hasFromDays));
//				}
//			}
//			if(month.contentEquals(endMonth)) {
//				if(hasEndDays>1) {
//					itemlongTermFee=itemlongTermFee.divide(new BigDecimal(monthEndDays),2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(hasEndDays));
//				}
//			}		
//			itemlongTermFee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, itemlongTermFee, 2);
//			longTermFee=longTermFee.add(itemlongTermFee);
//		
//		}
//		return longTermFee;
//		
//	}
//	
//	private BigDecimal findSettlementSummarySku_StorageFee(Map<String, Object> map) {
//		String tocurrency = "RMB";
//		if (map.get("currency") != null) {
//			tocurrency = map.get("currency").toString();
//		}
//		String shopid = map.get("shopid").toString();
//		List<Map<String, Object>> result = amzSettlementSummarySkuMapper.findSettlementSummarySku_StorageFee(map);
//		if(result==null||result.size()==0)return new BigDecimal("0");
//		String endDate=(String) map.get("endDate");
//		String fromDate=(String) map.get("fromDate");
//		Calendar c = Calendar.getInstance();
//		Date end = GeneralUtil.getDatez(endDate);
//		c.setTime(end);
//		int hasEndDays = c.get(Calendar.DAY_OF_MONTH);
//		int monthEndDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
//		Date from = GeneralUtil.getDatez(fromDate);
//		c.setTime(GeneralUtil.getDatez(fromDate));
//		Integer hasFromDays = c.get(Calendar.DAY_OF_MONTH);
//		Integer monthFromDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
//		hasFromDays=monthFromDays-hasFromDays+1;
//		
//		BigDecimal longTermFee = new BigDecimal("0");
//	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//	    String endMonth=GeneralUtil.formatDate(end, sdf);
//	    String fromMonth=GeneralUtil.formatDate(from, sdf);
//		for(Map<String, Object> item:result) {
//			String month=(String)item.get("month");
//			String fromcurrency=(String) item.get("currency");
//			BigDecimal itemTermFee =item.get("storagefee")==null?new BigDecimal("0"): (BigDecimal) item.get("storagefee");
//			if(month.contentEquals(fromMonth)) {
//				if(hasFromDays>1&&hasFromDays<monthFromDays) {
//					itemTermFee=itemTermFee.divide(new BigDecimal(monthFromDays) ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(hasFromDays));
//				}
//			}
//			if(month.contentEquals(endMonth)) {
//				if(hasEndDays>1&&hasEndDays<monthEndDays) {
//					itemTermFee=itemTermFee.divide(new BigDecimal(monthEndDays), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(hasEndDays));
//				}
//			}		
//			itemTermFee=exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, itemTermFee, 2);
//			longTermFee=longTermFee.add(itemTermFee);
//		
//		}
//		return longTermFee;
//		
//	}
//
//	@Override
//	public Map<String, Object> saveFinStatement(User user,Map<String, Object> map, List<Map<String, Object>> list, Map<String, String> field,Map<String, Object> saveParam) {
//		Map<String, Object> res=new HashMap<String, Object>();
//		if(list!=null && list.size()>0) {
//			int result=0;
//		    List<Object> myList=new LinkedList<Object>();
//            for(Map<String, Object> item:list) {
//            	myList.add(item);
//            }
//			JSONArray json =new JSONArray(myList);
//			String jsonstr = json.toString();
//			AmzSettlementAccStatement record=new AmzSettlementAccStatement();
//		    try {
//				record.setListdata(jsonstr.getBytes("utf-8"));
//			} catch (UnsupportedEncodingException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		    String currency = saveParam.get("currency").toString();
//			record.setCurrency(currency);
//			record.setShopid(new BigInteger(user.getShopid()));
//			
//			if(saveParam.get("groupid")==null||"all".equals(saveParam.get("groupid").toString())) {
//				record.setGroupid(null);
//			}else {
//				record.setGroupid(new BigInteger(saveParam.get("groupid").toString()));
//			}
//			if(saveParam.get("marketplaceid")==null||"all".equals(saveParam.get("marketplaceid").toString())) {
//				record.setMarketplaceid(null);
//			}else {
//				record.setMarketplaceid(saveParam.get("marketplaceid").toString());
//			}
//			record.setOperator(new BigInteger(user.getId()));
//			record.setOpttime(new Date());
//			Map<String, Object> sumdata=(Map<String, Object>)map.get("summarydata");
//			JSONObject sumjson =new JSONObject(sumdata);
//			 try {
//				record.setSummaryall(sumjson.toString().getBytes("utf-8"));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			 Map<String, Object> fielddata=new HashMap<String,Object>();
//			 for(Entry<String, String> entry:field.entrySet()) {
//				 fielddata.put(entry.getKey(),entry.getValue());
//			 }
//		
//			JSONObject fieldjson =new JSONObject(fielddata);
//			record.setFielddata(fieldjson.toString());
//			 
//			record.setStartdate(GeneralUtil.getDatez(saveParam.get("fromDate").toString()));
//			record.setEnddate(GeneralUtil.getDatez(saveParam.get("endDate").toString()));
//			if(saveParam.get("datetype")==null) {
//				record.setDatetype(null);
//			}else {
//				record.setDatetype(saveParam.get("datetype").toString());
//			}
//			Map<String,Object> param=new HashMap<String,Object>();
//			param.put("groupid", record.getGroupid());
//			param.put("marketplaceid", record.getMarketplaceid());
//			param.put("startdate", record.getStartdate());
//			param.put("enddate", record.getEnddate());
//			param.put("shopid", record.getShopid());
//			System.out.println(record.getCurrency());
//			List<Map<String,Object>> mlist=amzSettlementAccStatementMapper.existByKey(param);
//			if(mlist.size()>0) {
//				throw new BaseException("您的日期区间存在已经结账的记录。");
//			}else {
//				result+=amzSettlementAccStatementMapper.insert(record);
//			}
//			if(result>0) {
//				res.put("code", "isok");
//				res.put("msg", "操作成功!");		
//			}else {
//				res.put("code", "isfail");
//				res.put("msg", "操作失败!");
//			}
//		}else {
//			res.put("code", "isfail");
//			res.put("msg", "暂无匹配记录!");
//		}
//		return res;
//	}
//
//	@Override
//	public AmzSettlementAccStatement findCommodityStatement(String id) {
//		return amzSettlementAccStatementMapper.selectByPrimaryKey(new BigInteger(id));
//	}
//
//	@Override
//	public List<Map<String, Object>> findAmzSettlementAccStatement(String shopid) {
//		return amzSettlementAccStatementMapper.findAll(shopid);
//	}
//
//	@Override
//	public int deleteAmzSettlementAccStatement(String id) {
//		return amzSettlementAccStatementMapper.deleteByPrimaryKey(new BigInteger(id));
//	}
//	
//	public void confirm(Map<String, Object> param, Set<String> marketnameset) {
//		// TODO Auto-generated method stub
//		for(String marketname:marketnameset) {
//			param.put("marketplace_name", marketname);
//			amzSettlementReportSummaryMonthMapper.refreshSummary(param);
//			amzSettlementReportSummaryDayMapper.refreshSummary(param);
//			amzSettlementSummarySkuMapper.refreshSummary(param);
//			amzSettlementSummaryReturnsMapper.refreshSummary(param);
//		}
//		
//	}
}
