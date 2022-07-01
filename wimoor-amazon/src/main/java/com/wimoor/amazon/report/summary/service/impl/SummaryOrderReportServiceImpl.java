package com.wimoor.amazon.report.summary.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.api.ErpClientOneFeign;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.common.mapper.DimensionsInfoMapper;
import com.wimoor.amazon.common.mapper.FBAShipCycleMapper;
import com.wimoor.amazon.common.mapper.SummaryDataMapper;
import com.wimoor.amazon.common.pojo.entity.DaysalesFormula;
import com.wimoor.amazon.common.pojo.entity.DimensionsInfo;
import com.wimoor.amazon.common.pojo.entity.FBAShipCycle;
import com.wimoor.amazon.common.pojo.entity.SummaryData;
import com.wimoor.amazon.common.service.IDaysalesFormulaService;
import com.wimoor.amazon.common.service.IUserSalesRankService;
import com.wimoor.amazon.common.service.impl.DaysalesFormulaServiceImpl;
import com.wimoor.amazon.product.mapper.ProductCategoryMapper;
import com.wimoor.amazon.product.mapper.ProductInOptMapper;
import com.wimoor.amazon.product.mapper.ProductInOrderMapper;
import com.wimoor.amazon.product.mapper.ProductInProfitMapper;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.pojo.entity.ProductCategory;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductInOrder;
import com.wimoor.amazon.product.pojo.entity.ProductInProfit;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.entity.ReferralFee;
import com.wimoor.amazon.profit.pojo.vo.CostDetail;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.amazon.profit.pojo.vo.ItemMeasure;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.profit.service.IProfitService;
import com.wimoor.amazon.profit.service.IReferralFeeService;
import com.wimoor.amazon.report.mapper.FBAEstimatedFeeMapper;
import com.wimoor.amazon.report.mapper.OrdersReportMapper;
import com.wimoor.amazon.report.mapper.OrdersSummaryMapper;
import com.wimoor.amazon.report.mapper.ReportRequestRecordMapper;
import com.wimoor.amazon.report.pojo.entity.FBAEstimatedFee;
import com.wimoor.amazon.report.pojo.entity.OrdersSummary;
import com.wimoor.amazon.report.pojo.entity.ReportRequestRecord;
import com.wimoor.amazon.report.pojo.entity.ReportRequestType;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.report.service.IHandlerReportService;
import com.wimoor.amazon.report.service.IReportRequestTypeService;
import com.wimoor.amazon.report.summary.service.ISummaryOrderReportService;
import com.wimoor.amazon.util.FileUpload;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;

import cn.hutool.core.util.StrUtil;

@Service("summaryOrderReportService")
public class SummaryOrderReportServiceImpl implements ISummaryOrderReportService{
	@Resource
	OrdersReportMapper ordersReportMapper;
	@Resource
	IAmazonAuthorityService amazonAuthorityService;
	@Resource
	IHandlerReportService handlerReportService;
    @Resource
    OrdersSummaryMapper ordersSummaryMapper;
    @Resource
    IReportRequestTypeService iReportRequestTypeService;
    @Resource
	SummaryDataMapper summaryDataMapper;
    @Resource
    IProductInfoService productInfoService;
    @Resource
    IUserSalesRankService userSalesRankService;
    @Resource
    ReportRequestRecordMapper reportRequestRecordMapper;
    @Resource
    ProductInfoMapper productInfoMapper;
    @Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    IDaysalesFormulaService daysalesFormulaService;
    @Resource
    ProductInOrderMapper productInOrderMapper;
    @Resource
    ProductInProfitMapper productInProfitMapper;
    @Resource
    ProductInOptMapper productInOptMapper;
    @Resource
    IProfitCfgService profitCfgService;
    @Resource
    DimensionsInfoMapper dimensionsInfoMapper;
    @Resource
    FBAShipCycleMapper fBAShipCycleMapper;
    @Resource
    FBAEstimatedFeeMapper fbaEstimatedFeeMapper;
    @Resource
    ProductCategoryMapper productCategoryMapper;
    @Resource
    IReferralFeeService referralFeeService;
    @Resource
    IProfitService profitService;
    @Autowired
    ErpClientOneFeign erpClientOneFeign;
    
	public void deleteSum(String amazonAuthid, String begin) {
		QueryWrapper<OrdersSummary> query = new QueryWrapper<OrdersSummary>();
		query.eq("amazonauthid", amazonAuthid);
		query.ge("purchase_date", begin);
		ordersSummaryMapper.delete(query);
	}

	public void refreshAll(String authid) {
		Calendar c = Calendar.getInstance();
		ReportRequestType type = iReportRequestTypeService.findByTypeCode(ReportType.OrdersByOrderDateReport);
		c.add(Calendar.DATE, type.getDay()* -1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		Date begin = c.getTime();
		AmazonAuthority amazonauth = amazonAuthorityService.getById(authid);
		refreshAll(amazonauth, begin);

	}

	public boolean refreshAll(AmazonAuthority amazonAuthority, Date beginDate) {
		deleteSum(amazonAuthority.getId(), GeneralUtil.formatDate(beginDate));
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("amazonauthid", amazonAuthority.getId());
		map.put("begin", GeneralUtil.formatDate(beginDate));
		ordersSummaryMapper.refreshByOrder(map);
		ordersSummaryMapper.refreshShopSummary(map);
		return true;
	}
	
	public void summaryOrderReport() {
		List<Map<String, Object>> authlist = ordersReportMapper.allDownloadAuth();
		if (authlist.size() > 0) {
			Date minday = null;
			Boolean hasData = false;
			Set<String> shopSet = new HashSet<String>();
			for (Map<String, Object> authmap : authlist) {
				String authid = authmap.get("amazonAuthId").toString();
				AmazonAuthority auth = amazonAuthorityService.getById(authid);
				if(auth!=null) {
					int hasNeedTreatOrder = handlerReportService.hasNeedTreatOrderReportList(auth.getSellerid());
					if (hasNeedTreatOrder > 0) {
						continue;
					} 
					shopSet.add(auth.getShopId()); 
				}
				//如果没有要处理的，则开始汇总订单
				if(authmap.get("purchase_date")!=null) {
					minday = (Date) authmap.get("purchase_date");
				}
				if (minday != null) {
					if (hasData == false) {
						hasData = true;
					}
					shopSet.add(auth.getShopId());
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("amazonAuthId",authid);
					map.put("purchaseDate", minday);
					ordersReportMapper.deleteBeforeDownload(map);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(auth!=null) {
						refreshAll(auth, minday);
					}
				}
			}
			try {
				if (hasData) {
					Thread.sleep(60000);
					//首先同步到report表中 其次汇总summary _day _month _week 然后对data表插入数据
					 this.saveMainReport(shopSet);
					 this.summary(shopSet);
					 this.dataAnalysis(shopSet);
					 Date refreshdate=userSalesRankService.getSummaryLastDate();
					 if(refreshdate==null||!GeneralUtil.formatDate(refreshdate).equals(GeneralUtil.formatDate(new Date()))) {
						 List<ReportRequestRecord> list = reportRequestRecordMapper.selectNoTreatReport(ReportType.OrdersByOrderDateReport,null);
							 if(list==null||list.size()==0) {
									userSalesRankService.summaryAllUserSales();
						     }
					 }
					
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (hasData) {
	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			shopSet.clear();
			shopSet = null;
		}
	}

	public void summary(Set<String> shopset){
		if(shopset==null) {
			 shopset=this.getAvailableAmazonShop();
		}
		for(String shopid:shopset){
			summarySalesOrders(shopid);
		}
	}
	
	private Integer summarySalesOrders(String shopid) {
		QueryWrapper<SummaryData> queryWrapper=new QueryWrapper<SummaryData>();
		queryWrapper.eq("shopid", shopid);
		queryWrapper.eq("ftype", "main-2-sales");
		summaryDataMapper.delete(queryWrapper);
		queryWrapper.eq("ftype","main-2-orders");
		summaryDataMapper.delete(queryWrapper);
		Calendar cale = Calendar.getInstance();  
	             cale.add(Calendar.MONTH, 0);  
	             cale.set(Calendar.DAY_OF_MONTH, 1);  
		  Map<String, Object> result = summaryDataMapper.monthSalesOrders(shopid, cale.getTime());
		  Integer sales=0;
		  
		  if(result!=null&&result.get("quantity")!=null) {
			  sales=Integer.parseInt(result.get("quantity").toString());
		  }
		SummaryData sumtype=new SummaryData();
		sumtype.setFtype("main-2-sales");
		sumtype.setShopid(shopid);
		sumtype.setUpdatetime(new Date());
		sumtype.setValue(new BigDecimal(sales.toString()));
		summaryDataMapper.insert(sumtype);
		  Integer orders=0;
		 if(result!=null&&result.get("ordernumber")!=null) {
			 orders=Integer.parseInt(result.get("ordernumber").toString());
		  }
		sumtype=new SummaryData();
		sumtype.setFtype("main-2-orders");
		sumtype.setShopid(shopid);
		sumtype.setUpdatetime(new Date());
		sumtype.setValue(new BigDecimal(orders.toString()));
		summaryDataMapper.insert(sumtype);
		return sales;
	}

	public Set<String> getAvailableAmazonShop() {
		Set<String> shopset = new HashSet<String>();
		QueryWrapper<AmazonAuthority> queryWrapper=new QueryWrapper<AmazonAuthority>();
		queryWrapper.eq("disable", false);
		List<AmazonAuthority> list=amazonAuthorityService.list(queryWrapper);
		for (AmazonAuthority auth : list) {
			shopset.add(auth.getShopId());
		}
		return shopset;
	}
	
	public void saveMainReport(Set<String> mshopSet) {
		// 遍历所有的shopid
		if (mshopSet == null) {
			mshopSet=getAvailableAmazonShop();
		}
		for (String shopid : mshopSet) {
			if (StringUtils.isNotEmpty(shopid)) {
				// 先delete old的数据
				summaryDataMapper.deleteAllMainRpt(shopid);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("amazonAuthority", null);
				map.put("shopid", shopid);
				// 昨日产品销量前五名
				List<Map<String, Object>> last5 = findLastTop5Byshopid(map);
				for (Map<String, Object> item : last5) {
					Object sku = item.get("sku");
					if (sku != null) {
						List<Map<String, Object>> list = productInfoService.findShopSku(shopid, sku.toString());
						if (list != null && list.size() > 0) {
							Map<String, Object> p = list.get(0);
							item.put("pgroup", p.get("pgroup"));
							item.put("name", p.get("name"));
							for (Map<String, Object> mymap : list) {
								if (mymap.get("location") != null) {
									item.put("location", FileUpload.getPictureImage(mymap.get("location")));
									break;
								}
							}
						}
					}
				}
				SummaryData data = new SummaryData();
				String jsonData = GeneralUtil.parseListForMapsToJsonArrayStr(last5);
				data.setFtype("last5");
				data.setMapdata(jsonData);
				data.setShopid(shopid);
				data.setUpdatetime(new Date());
				summaryDataMapper.insert(data);
				// 昨日市场销量汇总
				List<Map<String, Object>> market = findMarketByshopid(map);
				SummaryData data1 = new SummaryData();
				String jsonData1 = GeneralUtil.parseListForMapsToJsonArrayStr(market);
				data1.setFtype("market");
				data1.setMapdata(jsonData1);
				data1.setUpdatetime(new Date());
				data1.setShopid(shopid);
				summaryDataMapper.insert(data1);
				// 最近一周销量汇总
				SummaryData data2 = new SummaryData();
				List<Map<String, Object>> weekReport = weekReport(map);
				String jsonData2 = GeneralUtil.parseListForMapsToJsonArrayStr(weekReport);
				data2.setFtype("weekdata");
				data2.setMapdata(jsonData2);
				data2.setUpdatetime(new Date());
				data2.setShopid(shopid);
				summaryDataMapper.insert(data2);
				// 昨日销量对比
				List<Map<String, Object>> result3 = lastQtyAmount(map);
				Map<String, Object> result4 = last2Amount(map);
				Map<String, Object> result5 = lastWeekAmount(map);
				BigDecimal q1 = null;
				BigDecimal q2 = null;
				BigDecimal rate = null;
				BigDecimal qt = null;
				if (result3 != null && result3.size() > 0 && result3.get(0) != null
						&& result3.get(0).get("quantity") != null) {
					if (result4 != null && result4.get("quantity") != null) {
						q1 = (BigDecimal) result3.get(0).get("quantity");
						q2 = (BigDecimal) result4.get("quantity");

						if (q1.compareTo(q2) < 0) {
							qt = q2.subtract(q1);
							rate = qt.divide(q2, 3, BigDecimal.ROUND_HALF_UP);
							result3.get(0).put("lastRatev", 1);
						} else if (q1.floatValue() == 0 || q2.floatValue() == 0) {
							rate = new BigDecimal(0);
							result3.get(0).put("lastRatev", 0);
						} else {
							qt = q1.subtract(q2);
							rate = qt.divide(q2, 2, BigDecimal.ROUND_HALF_UP);
							result3.get(0).put("lastRatev", 0);
						}
					}
					if(rate==null){
						rate = new BigDecimal(0);
					}
					rate = rate.multiply(new BigDecimal("100"));
					result3.get(0).put("lastRate", rate.doubleValue());
					result3.get(0).put("OldLast", q2);
				}

				BigDecimal q3 = new BigDecimal("0");
				if (result5 != null && result5.get("quantity") != null) {
					q3 = (BigDecimal) result5.get("quantity");
					if (q1 != null && q1.compareTo(q3) < 0) {
						qt = q3.subtract(q1);
						rate = qt.divide(q3, 3, BigDecimal.ROUND_HALF_UP);
						rate = rate.multiply(new BigDecimal("100"));
						result3.get(0).put("lastweekRate", rate);
						result3.get(0).put("lastweekRatev", 1);
					} else if (q1 != null && q3.floatValue() != 0) {
						qt = q1.subtract(q3);
						rate = qt.divide(q3, 2, BigDecimal.ROUND_HALF_UP);
						rate = rate.multiply(new BigDecimal("100"));
						result3.get(0).put("lastweekRate", rate.doubleValue());
						result3.get(0).put("lastweekRatev", 0);
					}
					if(result3 != null && result3.size() > 0 && result3.get(0) != null){
						result3.get(0).put("OldWeek", q3);
					}
					SummaryData sum = new SummaryData();
					sum.setFtype("lastqty");
					sum.setShopid(shopid);
					String jsonlist = GeneralUtil.parseListForMapsToJsonArrayStr(result3);
					sum.setUpdatetime(new Date());
					sum.setMapdata(jsonlist);
					summaryDataMapper.insert(sum);
				}

			}
		}

	}
	
	public List<Map<String, Object>> findLastTop5Byshopid(Map<String,Object> param){
		return ordersSummaryMapper.lastTop5(param);
	}
	
	public List<Map<String, Object>> findMarketByshopid(Map<String,Object> param){
		return ordersSummaryMapper.lastQtyMarket(param);
	}
	
	public List<Map<String, Object>> lastQtyAmount(Map<String,Object> param) {
		return   ordersSummaryMapper.lastQtyAmount(param);
	}
	public Map<String, Object> last2Amount(Map<String,Object> param) {
		return   ordersSummaryMapper.last2Qty(param);
	}
	public Map<String, Object> lastWeekAmount(Map<String,Object> param) {
		return   ordersSummaryMapper.lastweekQty(param);
	}
	public List<Map<String, Object>> weekReport(Map<String,Object> map) {
		return   ordersSummaryMapper.weekAmount(map) ;
	}
	
	public void dataAnalysis(Set<String> shopset) {
		//productInfoReviewService.updateRunsAsin();
		if(shopset==null) {
			 shopset=getAvailableAmazonShop();
		}
		List<Future<?>> threadList = new ArrayList<Future<?>>();
		List<Runnable> runnables = new ArrayList<Runnable>();
		for(String shopid:shopset) {
		QueryWrapper<AmazonAuthority> authqueryWrapper=new QueryWrapper<AmazonAuthority>();
		authqueryWrapper.eq("shop_id", shopid);
		authqueryWrapper.eq("disable", false);
		List<AmazonAuthority> amazonAuthorityList = amazonAuthorityService.list(authqueryWrapper);
		if(amazonAuthorityList!=null && amazonAuthorityList.size()>0) {
			for(AmazonAuthority auth:amazonAuthorityList) {
				List<Map<String, Object>> list = productInfoMapper.selectByAuth(auth.getId());
				if(list!=null && list.size()>0){
					runnables.add(productDateUpdateThread(list));
				}
			}
		}
		}
		for (int i = 0; i < runnables.size(); i++) {
			if (runnables.get(i) != null) {
				threadList.add(threadPoolTaskExecutor.submit(runnables.get(i)));
			}
		}
		
		boolean alldone=false;
		while (alldone == false) {
			try {
				alldone = true;
				for (int i = 0; i < threadList.size(); i++) {
					if (!threadList.get(i).isDone()) {
						alldone = false;
						break;
					}
				}
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		deleteProductInOrderAndProfitByDate();

	}
	
	public Runnable productDateUpdateThread(final List<Map<String, Object>> list) {
		return new Runnable() {
			public void run() {
				try {
					productDateUpdate(list);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	public void productDateUpdate(List<Map<String, Object>> list) {
		List<ProductInOrder> productInOrderList = new ArrayList<ProductInOrder>();
		List<ProductInProfit> productInProfitList = new ArrayList<ProductInProfit>();
		for (Map<String, Object> map : list) {
			// 更新销量信息表
			try {
			Map<String,Object> mapdetail=productInfoMapper.selectByMapByParams(map);
			if(mapdetail==null) {
				continue;
			}
			map.putAll(mapdetail);
			ProductInOrder productInOrder = new ProductInOrder();
			productInOrder.setPid(new BigInteger(map.get("pid").toString()));
			BigDecimal	price = calculate_Units(map, productInOrder);// 七日平均售价
			BigDecimal buyprice = map.get("landed_amount") == null ? null : new BigDecimal(map.get("landed_amount").toString());
			if (price == null || price.floatValue() == 0.0) {
				if (buyprice != null) {
					price = buyprice;
				} else {
					if (map.get("price") != null) {
						price = new BigDecimal(map.get("price").toString());
					} else {
						price = new BigDecimal("0");
					}
				}
			}
			if(buyprice==null) {
				if (map.get("price") != null) {
					buyprice = new BigDecimal(map.get("price").toString());
				} 
			}
			productInOrder.setLastupdate(LocalDateTime.now());
			if (map.get("rank") != null) {
				productInOrder.setRank((Integer) map.get("rank"));
			}
			productInOrderList.add(productInOrder);
			if (productInOrderList.size() > 200) {
				productInOrderMapper.insertBatch(productInOrderList);
				productInOrderList.clear();
			}
			// 更新利润信息表
			ProductInProfit productInProfit = new ProductInProfit();
			productInProfit.setPid(map.get("pid").toString());
			getProfit(productInProfit, price, map,buyprice);
			productInProfit.setLastupdate(new Date());
			productInProfitList.add(productInProfit);
			if (productInProfitList.size() > 200) {
				productInProfitMapper.insertBatch(productInProfitList);
				productInProfitList.clear();
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		if (productInOrderList.size() > 0) {
			productInOrderMapper.insertBatch(productInOrderList);
			productInOrderList.clear();
			productInOrderList=null;
		}
		if (productInProfitList.size() > 0) {
			productInProfitMapper.insertBatch(productInProfitList);
			productInProfitList.clear();
			productInProfitList=null;
		}
       if(list!=null) {
    		list=null;
    		productInProfitList=null;
    		productInOrderList=null;
       }
    	
	}
	
	public BigDecimal calculate_Units(Map<String, Object> map, ProductInOrder productInOrder) {
		int sumseven = map.get("sales7") == null ? 0 : Integer.parseInt(map.get("sales7").toString());// 七日销量
		int sumweek = map.get("salesWeek") == null ? 0 : Integer.parseInt(map.get("salesWeek").toString());// 往前推2天之后的7日销量
		int summonth = map.get("sales30") == null ? 0 : Integer.parseInt(map.get("sales30").toString());
		int ordermonth = map.get("order30") == null ? 0 : Integer.parseInt(map.get("order30").toString());
		int orderweek = map.get("order7") == null ? 0 : Integer.parseInt(map.get("order7").toString());
		int sum15 = map.get("sales15") == null ? 0 : Integer.parseInt(map.get("sales15").toString());
		int daynum = map.get("daynum") == null ? 0 : Integer.parseInt(map.get("daynum").toString());
		BigDecimal price7 = map.get("orderprice7") == null ? new BigDecimal("0") : new BigDecimal(map.get("orderprice7").toString());
		Integer oldAve = (Integer) map.get("avgsales");
		Integer oldAvgsales = (Integer) map.get("oldavgsales");
		Date nowdate = (Date) map.get("nowdate");
		int day = nowdate.getDay();
		if (oldAve == null) {
			oldAve = 0;
		}
		productInOrder.setDaynum(day);
		productInOrder.setOldavgsales(oldAvgsales);
		if (oldAvgsales == null || day != daynum) {
			productInOrder.setOldavgsales(oldAve);
		}
		Date openDate = (Date) map.get("openDate");
		String shopid = null;
		if(map.containsKey("shopid")) {
			Object shopobj = map.get("shopid");
			if(shopobj!=null) {
				shopid=shopobj.toString()  ;
			}
		}
		if(shopid==null) {
			 shopid=productInfoMapper.getShopidByPid(productInOrder.getPid().toString());
		}
		DaysalesFormula formula = daysalesFormulaService.selectByShopid(shopid);
		int qty = DaysalesFormulaServiceImpl.getAvgSales(formula , summonth, sumseven, sum15, openDate);
		productInOrder.setAvgsales(qty);
		BigDecimal changeRate = new BigDecimal("0");
		double rate =0; 
		if (summonth!=sum15) {
			if(summonth-sum15>0){
				rate=(sum15*2-summonth)*100.0/(summonth-sum15);
			}
		}
		changeRate = new BigDecimal(rate);
		changeRate=changeRate.setScale(2, BigDecimal.ROUND_FLOOR);
		productInOrder.setChangeRate(changeRate);
		productInOrder.setSalesSeven(sumseven);
		productInOrder.setSalesWeek(sumweek);
		productInOrder.setSalesFifteen(sum15);
		productInOrder.setSalesMonth(summonth);
		productInOrder.setOrderWeek(orderweek);
		productInOrder.setOrderMonth(ordermonth);
		productInOrder.setPriceWeek(price7);
		if (sumseven == 0) {
			return new BigDecimal("0");
		}
		return price7.divide(new BigDecimal(sumseven),4);
	}
	
	void deleteProductInOrderAndProfitByDate() {
		// 这个方法是暂时的，以后改成按时间区间来抓取订单后，这里的逻辑也要变化，目前是删除product_in_order和product_in_profit表中lastupdate更新时间为当前时间前一天的数据
		productInProfitMapper.deleteProductInOrderAndProfitByDate();
	}
	
	void getProfit(ProductInProfit sys, BigDecimal price, Map<String, Object> map, BigDecimal buyprice) throws BizException {
		if (price == null || price.floatValue() == 0.0) {
			return;
		}
		Result<Map<String, Object>> materialRes = erpClientOneFeign.getMaterialBySKUAction(map.get("msku").toString(), map.get("shopid").toString());
		Map<String, Object> materialMap = materialRes.getData();
		if(materialMap==null || materialMap.get("code").toString().equals("fail")) {
			return;
		}
		String pageDimensions = map.get("pageDimensions") == null ? null : map.get("pageDimensions").toString();
		DimensionsInfo dim_local =new DimensionsInfo();
		dim_local.setHeight(new BigDecimal(materialMap.get("height").toString()));
		if(materialMap.get("height_unit")!=null)dim_local.setHeightUnits(materialMap.get("height_unit").toString());
		dim_local.setWeight(new BigDecimal(materialMap.get("weight").toString()));
		if(materialMap.get("weight_unit")!=null)dim_local.setWeightUnits(materialMap.get("weight_unit").toString());
		dim_local.setLength(new BigDecimal(materialMap.get("length").toString()));
		if(materialMap.get("length_unit")!=null)dim_local.setLengthUnits(materialMap.get("length_unit").toString());
		dim_local.setWidth(new BigDecimal(materialMap.get("width").toString()));
		if(materialMap.get("width_unit")!=null)dim_local.setWidthUnits(materialMap.get("width_unit").toString());
		// 获取用户输入信息
		InputDimensions inputDimension_local = null;
		InputDimensions inputDimension_amz = null;
		inputDimension_local = dim_local.getInputDimensions();
		if (pageDimensions == null) {
			inputDimension_amz = dim_local.getInputDimensions();
		} else {
			inputDimension_amz = getInputDimensions(map);
		}
		BigDecimal cost = (BigDecimal) materialMap.get("cost");
		String country = map.get("market").toString();
		BigInteger profitcfgid=null;
		
		//使用opt上面带的利润id(假如设置了)
		ProductInOpt proopt = productInOptMapper.selectById(sys.getPid());
		if(proopt!=null) {
			if(proopt.getProfitid()!=null) {
				profitcfgid=proopt.getProfitid();
			}
		} 
		if(profitcfgid==null) {
			String profitcfgids = profitCfgService.findDefaultPlanIdByGroup(map.get("groupid").toString());// 使用店铺默认计算方案
			if(profitcfgids!=null) {
				profitcfgid=new BigInteger(profitcfgids);
			}
		}
		ProfitConfig profitcfg = profitCfgService.findConfigAction(profitcfgid.toString());
		if(inputDimension_amz != null && inputDimension_amz.getLength() != null) {
			String lengthValue = inputDimension_amz.getLength().value.toString() + inputDimension_amz.getLength().units;
			String widthValue = inputDimension_amz.getWidth().value.toString() + inputDimension_amz.getWidth().units;
			String heightValue = inputDimension_amz.getHeight().value.toString() + inputDimension_amz.getHeight().units;
			String weightValue = inputDimension_amz.getWeight().value.toString() + inputDimension_amz.getWeight().units;
			sys.setDimensions(lengthValue + "-" + widthValue + "-" + heightValue);
			sys.setWeight(weightValue);
		}
		sys.setPlanName(profitcfg.getName());
		BigDecimal shipmentfee = null;
		FBAShipCycle stockCycle = null;
		if ("EU".equals(map.get("region").toString())) {
			stockCycle = fBAShipCycleMapper.findShipCycleBySKU(map.get("sku").toString(), "EU", map.get("groupid").toString());
		} else {
			stockCycle = fBAShipCycleMapper.findShipCycleBySKU(map.get("sku").toString(), map.get("marketplaceid").toString(), map.get("groupid").toString());
		}
		if (stockCycle != null) {
			shipmentfee = stockCycle.getFirstLegCharges();// 头程运费
		}
		CostDetail deatail = null;
		CostDetail deatail2=null;
		try {
			QueryWrapper<FBAEstimatedFee> feequeryWrapper=new QueryWrapper<FBAEstimatedFee>();
			feequeryWrapper.eq("sku", map.get("sku").toString());
			feequeryWrapper.eq("asin", map.get("asin").toString());
			feequeryWrapper.eq("amazonAuthId", map.get("id").toString());
			feequeryWrapper.eq("marketplaceid", map.get("marketplaceid").toString());
			FBAEstimatedFee fbaFee = fbaEstimatedFeeMapper.selectOne(feequeryWrapper);
			Boolean inSnl = map.get("inSnl") == null ? false : (Boolean) map.get("inSnl");
			ReferralFee ref = null;
			if(fbaFee != null) {
				if(fbaFee.getEstimatedFeeTotal() == null || fbaFee.getEstimatedReferralFeePerUnit() == null) {
					fbaFee = null;
				}
			}
			if (fbaFee == null) {
				QueryWrapper<ProductCategory> catequeryWrapper=new QueryWrapper<ProductCategory>();
				catequeryWrapper.eq("pid", sys.getPid());
				catequeryWrapper.isNull("parentId");
				List<ProductCategory> categorylist = productCategoryMapper.selectList(catequeryWrapper);
				ProductCategory category=null;
				if(categorylist!=null&&categorylist.size()>0) {
					  category= categorylist.get(0);
				}
				if (category!=null && category.getName()!=null) {
					ref = referralFeeService.findByPgroup(category.getName().trim(), country);
				} else {
					if(materialMap.get("catename")!=null) {
						ref = referralFeeService.findByPgroup(materialMap.get("catename").toString().trim(), country);
					}
				}
				
				if(ref == null || ref.getId() == null) {
					ref = referralFeeService.findCommonOther(country);
				}
				Integer typeid = ref.getId();
				String type = ref.getType();
				String isMedia = this.profitService.isMedia(typeid);// 是否为媒介
				deatail = this.profitService.getProfitByLocalData(country, profitcfg, inputDimension_amz,
						inputDimension_local, isMedia, type, typeid, cost, "RMB", price, "local", inSnl, shipmentfee);
			}else {
				ref = referralFeeService.findByPgroup(fbaFee.getProductGroup(), country);
				if(ref == null || ref.getId() == null) {
					ref = referralFeeService.findCommonOther(country);
				}
				String isMedia ="0";
				if(ref!=null) {
					isMedia = profitService.isMedia(ref.getId());// 是否为媒介
				}
				deatail = this.profitService.getProfitByAmazonData(country, profitcfg, inputDimension_local, isMedia,
						cost, "RMB", price, fbaFee, ref, inSnl, shipmentfee);

			}
			if (fbaFee == null) {
				QueryWrapper<ProductCategory> catequeryWrapper=new QueryWrapper<ProductCategory>();
				catequeryWrapper.eq("pid", sys.getPid());
				catequeryWrapper.isNull("parentId");
				List<ProductCategory> categorylist = productCategoryMapper.selectList(catequeryWrapper);
				ProductCategory category=null;
				if(categorylist!=null&&categorylist.size()>0) {
					  category= categorylist.get(0);
				}
				if (category!=null && category.getName()!=null) {
					ref = referralFeeService.findByPgroup(category.getName().trim(), country);
				}else {
					if(materialMap.get("catename")!=null) {
						ref = referralFeeService.findByPgroup(materialMap.get("catename").toString().trim(), country);
					}
				}  
				if(ref == null || ref.getId() == null) {
					ref = referralFeeService.findCommonOther(country);
				}
				Integer typeid = ref.getId();
				String type = ref.getType();
				String isMedia = this.profitService.isMedia(typeid);// 是否为媒介
				deatail2 = this.profitService.getProfitByLocalData(country, profitcfg, inputDimension_amz,
						inputDimension_local, isMedia, type, typeid, cost, "RMB", buyprice, "local", inSnl, shipmentfee);
			}else {
				ref = referralFeeService.findByPgroup(fbaFee.getProductGroup(), country);
				if(ref == null || ref.getId() == null) {
					ref = referralFeeService.findCommonOther(country);
				}
				String isMedia ="0";
				if(ref!=null) {
					  isMedia = this.profitService.isMedia(ref.getId());// 是否为媒介
				}
				deatail2 = this.profitService.getProfitByAmazonData(country, profitcfg, inputDimension_local, isMedia,
						cost, "RMB", buyprice, fbaFee, ref, inSnl, shipmentfee);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}  
		if (deatail == null) {
			return;
		}
		if(deatail2!=null) {
			if(StrUtil.isNotEmpty(deatail2.getMargin())) {
				String margin=deatail2.getMargin();
				if (margin.length() >= 2) {
					margin = margin.substring(0, margin.length() - 1);
					sys.setMargin(new BigDecimal(margin));
				}
			}
		}else {
			sys.setMargin(new BigDecimal("0"));
		}
		String margin = deatail.getMargin();
		if (margin == null) {
			sys.setProfitWeek(new BigDecimal("0"));
			sys.setMarginWeek(new BigDecimal("0"));
			return;
		}
		sys.setShipmentfee(deatail.getShipment());
 
		BigDecimal othersfee = new BigDecimal("0");
		BigDecimal tax =deatail.getTax();
		BigDecimal transportfee =deatail.getCurrencyTransportFee();
		BigDecimal marketing =deatail.getMarketing();
		BigDecimal other =deatail.getOthersFee();
		BigDecimal otherfee =deatail.getOthers();
		BigDecimal gst =deatail.getImport_GST();;
		BigDecimal sgst =deatail.getSelling_GST();;
		BigDecimal corp =deatail.getCorporateInFee();
		BigDecimal vatf =deatail.getVatFee();
		String marketplace=null;
		if(map!=null&&map.get("marketplaceid")!=null) {
			marketplace=map.get("marketplaceid").toString();
		}
		if(marketplace!=null&&"A1F83G8C2ARO7P".equals(marketplace)) {
			othersfee = tax.add(transportfee).add(marketing).add(other).add(otherfee).add(gst).add(sgst).add(corp);
		}else {
			othersfee = tax.add(transportfee).add(marketing).add(other).add(otherfee).add(gst).add(sgst).add(corp).add(vatf);
		}
		sys.setOthersfee(othersfee);
		if (margin.length() >= 2) {
			margin = margin.substring(0, margin.length() - 1);
			if (map.get("sales7") != null) {
				sys.setProfitWeek(deatail.getProfit().multiply(new BigDecimal(map.get("sales7").toString())).setScale(6,BigDecimal.ROUND_HALF_EVEN));
				sys.setMarginWeek(new BigDecimal(margin));
			} else {
				sys.setProfitWeek(new BigDecimal("0"));
				sys.setMarginWeek(new BigDecimal(margin));
			}
		}
		
		sys.setCostdetail(GeneralUtil.toJSON(deatail));
	}
	
	public InputDimensions getInputDimensions(Map<String, Object> map) {
		InputDimensions item = new InputDimensions();
		String weightUnits = map.get("weight_units") == null ? null : map.get("weight_units").toString();
		String heightUnits = map.get("height_units") == null ? null : map.get("height_units").toString();
		BigDecimal width = map.get("width") == null ? new BigDecimal("0") : new BigDecimal(map.get("width").toString());
		BigDecimal height = map.get("height") == null ? new BigDecimal("0") : new BigDecimal(map.get("height").toString());
		BigDecimal length = map.get("length") == null ? new BigDecimal("0") : new BigDecimal(map.get("length").toString());
		BigDecimal weight = map.get("weight") == null ? new BigDecimal("0") : new BigDecimal(map.get("weight").toString());
		if (weightUnits != null && weightUnits.toLowerCase().contains("pounds")) {
			weightUnits = InputDimensions.unit_lb;
		}
		if (weightUnits != null && weightUnits.toLowerCase().contains("ounces")) {
			weightUnits = InputDimensions.unit_oz;
		}
		if (weightUnits != null && weightUnits.toLowerCase().contains("kilograms")) {
			weightUnits = InputDimensions.unit_kg;
		}
		if (heightUnits != null && heightUnits.toLowerCase().contains("grams")) {
			heightUnits = InputDimensions.unit_g;
		}
		if (heightUnits != null && heightUnits.toLowerCase().contains("inches")) {
			heightUnits = InputDimensions.unit_in;
		}
		if (heightUnits != null && heightUnits.toLowerCase().contains("centimeters")) {
			heightUnits = InputDimensions.unit_cm;
		}
		if (heightUnits == null) {
			heightUnits = InputDimensions.unit_cm;
		}
		if (weightUnits == null) {
			weightUnits = InputDimensions.unit_kg;
		}
		item.setWidth(new ItemMeasure(width, heightUnits));
		item.setHeight(new ItemMeasure(height, heightUnits));
		item.setLength(new ItemMeasure(length, heightUnits));
		item.setWeight(new ItemMeasure(weight, weightUnits));
		return item;
	}
	

}
