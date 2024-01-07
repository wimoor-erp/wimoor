package com.wimoor.amazon.finances.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.finances.mapper.AmzSettlementSummarySkuMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementSummarySkuMonthMapper;
import com.wimoor.amazon.finances.mapper.FBALongTermStorageFeeReportMapper;
import com.wimoor.amazon.finances.mapper.FBAReimbursementsFeeReportMapper;
import com.wimoor.amazon.finances.mapper.FBAStorageFeeReportMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzFinSettlementFormula;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementSummarySkuMonth;
import com.wimoor.amazon.finances.service.IAmzFinSettlementFormulaService;
import com.wimoor.amazon.finances.service.IAmzFinUserItemDataService;
import com.wimoor.amazon.finances.service.IAmzSettlementSummarySkuMonthService;
import com.wimoor.amazon.inbound.mapper.ShipInboundTransMapper;
import com.wimoor.amazon.orders.mapper.OrdersFulfilledShipmentsFeeMapper;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-26
 */
@Service
@RequiredArgsConstructor
public class AmzSettlementSummarySkuMonthServiceImpl extends ServiceImpl<AmzSettlementSummarySkuMonthMapper, AmzSettlementSummarySkuMonth> implements IAmzSettlementSummarySkuMonthService {
@Autowired
AmzSettlementSummarySkuMapper amzSettlementSummarySkuMapper;
@Autowired
IAmazonAuthorityService iAmazonAuthorityService;	
@Autowired
IMarketplaceService iMarketplaceService;	
final FBALongTermStorageFeeReportMapper fBALongTermStorageFeeReportMapper;
final IAmzFinUserItemDataService iAmzFinUserItemDataService;
final IExchangeRateHandlerService exchangeRateHandlerService;
final FBAStorageFeeReportMapper fBAStorageFeeReportMapper;
final ShipInboundTransMapper shipInboundTransMapper;
final IAmzFinSettlementFormulaService iAmzFinSettlementFormulaService;
final FBAReimbursementsFeeReportMapper fBAReimbursementsFeeReportMapper;
final OrdersFulfilledShipmentsFeeMapper ordersFulfilledShipmentsFeeMapper;
	void summaryMonth(String groupid,String marketplaceid,String month){
		  
	}


 	private Map<String,BigDecimal> findSku_LongtermStorage(Map<String, Object> map) {
 		List<Map<String, Object>> list = fBALongTermStorageFeeReportMapper.findSku_LongtermStorage(map);
 		Map<String,BigDecimal> result=new HashMap<String,BigDecimal>();
 		for(Map<String, Object> item:list) {
 			if(item.get("sku")!=null) {
 				result.put(item.get("sku").toString(),new BigDecimal(item.get("longTermFee")==null?"0":item.get("longTermFee").toString()));
 			}
 		}
 		return  result;
 		
 	}
 	
 	private Map<String,BigDecimal> findSku_StorageFee(Map<String, Object> map) {
 		List<Map<String, Object>> list = fBAStorageFeeReportMapper.findSku_StorageFee(map);
 		Map<String,BigDecimal> result=new HashMap<String,BigDecimal>();
 		for(Map<String, Object> item:list) {
 			if(item.get("asin")!=null) {
 				result.put(item.get("asin").toString(),new BigDecimal(item.get("storagefee")==null?"0":item.get("storagefee").toString()));
 			}
 		}
 		return  result;
 	}
 	private Map<String,Map<String,Object>> findSku_AdvSpend(Map<String, Object> map) {
 		List<Map<String, Object>> list = this.baseMapper.findSku_AdvSpend(map);
 		Map<String,Map<String,Object>> result=new HashMap<String,Map<String,Object>>();
 		for(Map<String, Object> item:list) {
 			if(item.get("sku")!=null) {
 				result.put(item.get("sku").toString(),item);
 			}
 		}
 		return  result;
 		
 	}


	private Map<String, BigDecimal> findSku_Reimbursements(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list =  fBAReimbursementsFeeReportMapper.findSku_Reimbursements(param);
		Map<String,BigDecimal> result=new HashMap<String,BigDecimal>();
 		for(Map<String, Object> item:list) {
 			if(item.get("sku")!=null) {
 				result.put(item.get("sku").toString(),new BigDecimal(item.get("amount")==null?"0":item.get("amount").toString()));
 			}
 		}
 		return  result;
	}

 	private Map<String,BigDecimal> findSku_FirstShipment(Map<String, Object> map) {
 		  List<Map<String, Object>> list = shipInboundTransMapper.findSku_FirstShipment(map);
 		Map<String,BigDecimal> result=new HashMap<String,BigDecimal>();
 		for(Map<String, Object> item:list) {
 			if(item.get("sellersku")!=null) {
 				result.put(item.get("sellersku").toString(),new BigDecimal(item.get("skufee")==null?"0":item.get("skufee").toString()));
 			}
 		}
 		return  result;
 		
 	}
 	
 	private Map<String,Map<String,Object>> findSku_InOutShipment(Map<String, Object> map) {
		List<Map<String, Object>> list = ordersFulfilledShipmentsFeeMapper.findSku_InOutShipment(map);
		Map<String,Map<String,Object>> result=new HashMap<String,Map<String,Object>>();
 		for(Map<String, Object> item:list) {
 			if(item.get("sku")!=null) {
 				result.put(item.get("sku").toString(),item);
 			}
 		}
 		return  result;
		
	}
 	
 	private Map<String,List<Map<String,Object>>> findSku_finItemData(Map<String, Object> map,List<Map<String,String>> fieldlist) {
 		List<Map<String, Object>> finItemDataList = new ArrayList<Map<String, Object>>();
		if (fieldlist.size() > 0) {
			map.put("fieldlist", fieldlist);
			finItemDataList = amzSettlementSummarySkuMapper.findFinItemDataByCondition(map);
		}
		Map<String,List<Map<String,Object>>> result=new HashMap<String,List<Map<String,Object>>>();
		for(Map<String, Object> item:finItemDataList) {
 			if(item.get("sku")!=null) {
 				String sku=item.get("sku").toString();
 				 List<Map<String, Object>> list = result.get(sku);
 				 if(list==null) {
 					 list=new LinkedList<Map<String,Object>>();
 				 }
 				 list.add(item);
 			}
 		}
 		return  result;
		
	}
 	BigDecimal getValue(BigDecimal value){
 		if(value==null)return new BigDecimal("0.0");
 		else return value;
 	}
 	
 	
    private AmzSettlementSummarySkuMonth currencyExchange(
												    		AmzSettlementSummarySkuMonth item,
												    		List<Map<String, String>> fieldlist,
												    		String shopid,
												    		String tocurrency,
												    		Expression compiledExp) {
    	
		String fromcurrency=item.getCurrency();
    	AmzSettlementSummarySkuMonth one=new AmzSettlementSummarySkuMonth();
    	//============================================基本数据=====================================
    	one.setAmazonAuthId(item.getAmazonAuthId());
    	one.setAsin(item.getAsin()!=null?item.getAsin():"Non-ASIN");
    	one.setParentasin(item.getParentasin()!=null?item.getParentasin():item.getAsin());
    	one.setCategoryid(item.getCategoryid()!=null?item.getCategoryid():"0");
    	one.setGroupid(item.getGroupid());
    	one.setMsku(item.getMsku());
    	one.setId(item.getId());
    	one.setCurrency(tocurrency);
    	one.setPostedDate(item.getPostedDate());
    	one.setMarketplaceName(item.getMarketplaceName());
    	one.setPid(StrUtil.isBlankOrUndefined(item.getPid())?null:item.getPid());
    	one.setMid(StrUtil.isBlankOrUndefined(item.getMid())?null:item.getMid());
    	one.setOwner(StrUtil.isBlankOrUndefined(item.getOwner())?null:item.getOwner());
    	one.setSku(item.getSku());
    	one.setSales(item.getSales());
    	one.setOrderAmount(item.getOrderAmount());
    	one.setRefundsales(item.getRefundsales());
    	one.setRefundorder(item.getRefundorder());
    	one.setPrincipal(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getPrincipal(), 2));
    	one.setCommission(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getCommission(), 2));
    	one.setFbafee(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getFbafee(), 2));
    	one.setPromotion(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getPromotion(), 2));
    	one.setRefund(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getRefund(), 2));
    	one.setShipping(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getShipping(), 2));
    	one.setTax(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getTax(), 2));
    	one.setOtherfee(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getOtherfee(), 2));
    	BigDecimal setincome=new BigDecimal("0");
    	setincome=setincome.add(one.getPrincipal())
    			           .add(one.getCommission())
    			           .add(one.getFbafee())
    			           .add(one.getPromotion())
    			           .add(one.getRefund())
    	                   .add(one.getShipping())
    	                   .add(one.getTax())
    	                   .add(one.getOtherfee());
    	//============================================分摊店铺数据=====================================
    	one.setShareStorageFee(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getShareStorageFee(), 2));
    	one.setShareLongStorageFee(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getShareLongStorageFee(), 2));
    	one.setShareAdvSpendFee(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getShareAdvSpendFee(), 2));
    	one.setShareCouponRedemptionFee(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getShareCouponRedemptionFee(), 2)); 
    	one.setShareReserveFee(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getShareReserveFee(), 2));
    	one.setShareReimbursementFee(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getShareReimbursementFee(), 2));
    	one.setShareShopOtherFee(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getShareShopOtherFee(), 2));

    	BigDecimal  avgprice =one.getSales()!=null&&one.getSales()>0?one.getPrincipal().divide(new BigDecimal(one.getSales()),2,RoundingMode.HALF_UP):new BigDecimal("0");
    	
    	//============================================报表数据=====================================
    	one.setRptAdvSales(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getRptAdvSales(), 2));
    	one.setRptAdvSpendFee(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getRptAdvSpendFee(), 2));
    	one.setRptLongStorageFee(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getRptLongStorageFee(), 2));
    	one.setRptStorageFee(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency,item.getRptStorageFee() , 2));
    	one.setRptReimbursementsFee(exchangeRateHandlerService.changeCurrencyByLocal(shopid, fromcurrency, tocurrency, item.getRptReimbursementsFee(), 2));
    	one.setRptAdvUnits(item.getRptAdvUnits());
    	//============================================本地利润计算数据=====================================
    	one.setProfitCompanytax(exchangeRateHandlerService.changeCurrencyByLocal(shopid,fromcurrency, tocurrency,item.getProfitCompanytax(), 2));
    	one.setProfitCustomstax(exchangeRateHandlerService.changeCurrencyByLocal(shopid,fromcurrency, tocurrency,item.getProfitCustomstax(), 2));
    	one.setProfitExchangelost(exchangeRateHandlerService.changeCurrencyByLocal(shopid,fromcurrency, tocurrency,item.getProfitExchangelost(), 2));
    	one.setProfitLostrate(exchangeRateHandlerService.changeCurrencyByLocal(shopid,fromcurrency, tocurrency,item.getProfitLostrate(), 2));
    	one.setProfitMarketfee(exchangeRateHandlerService.changeCurrencyByLocal(shopid,fromcurrency, tocurrency,item.getProfitMarketfee(), 2));
    	one.setProfitOtherfee(exchangeRateHandlerService.changeCurrencyByLocal(shopid,fromcurrency, tocurrency,item.getProfitOtherfee(), 2));
    	one.setProfitVat(exchangeRateHandlerService.changeCurrencyByLocal(shopid,fromcurrency, tocurrency,item.getProfitVat(), 2));
    	
    	//============================================本地成本数据=====================================
    	one.setProfitLocalShipmentfee(exchangeRateHandlerService.changeCurrencyByLocal(shopid,"CNY", tocurrency,item.getProfitLocalShipmentfee(), 2));
    	one.setLocalPrice(exchangeRateHandlerService.changeCurrencyByLocal(shopid,"CNY", tocurrency,item.getLocalPrice(), 2));
    	one.setLocalOtherCost(exchangeRateHandlerService.changeCurrencyByLocal(shopid,"CNY", tocurrency,item.getLocalOtherCost(), 2));
    	one.setLocalReturnTax(exchangeRateHandlerService.changeCurrencyByLocal(shopid,"CNY", tocurrency,item.getLocalReturnTax(), 2));
    	one.setLocalShipmentItemFee(exchangeRateHandlerService.changeCurrencyByLocal(shopid,"CNY", tocurrency,item.getLocalShipmentItemFee(), 2));
    	one.setLocalFifoShipmentFee(exchangeRateHandlerService.changeCurrencyByLocal(shopid,"CNY", tocurrency,item.getLocalFifoShipmentFee(), 2));
    	one.setLocalFifoCost(exchangeRateHandlerService.changeCurrencyByLocal(shopid,"CNY", tocurrency,item.getLocalFifoCost(), 2));
    	
    	Map<String, BigDecimal> finitem=new HashMap<String,BigDecimal>();
    	//============================================自定义字段数据=====================================
    	BigDecimal finSumFee=new BigDecimal(0);
        List<Map<String, Object>> finlist = one.getFinDataList();
        if(finlist!=null) {
        	for(Map<String, Object> fin:finlist) {//当前SKU下面各个币种的动态字段进行汇率转换和汇总到finlist中
        		String myfromcurrency=fin.get("currency")!=null?fin.get("currency").toString():"CNY";
        		for (Map<String, String> itemfield : fieldlist) {
         			if (fin.get("field" + itemfield.get("value")) != null) {
         				BigDecimal itemvalue = new BigDecimal(fin.get("field" + itemfield.get("value")).toString());
         				itemvalue=exchangeRateHandlerService.changeCurrencyByLocal(shopid,myfromcurrency, tocurrency,itemvalue, 2);
         				BigDecimal value=finitem.get("field" + itemfield.get("value"))!=null? finitem.get("field" + itemfield.get("value")):new BigDecimal("0");
         				value=value.add(itemvalue);
         				finSumFee=finSumFee.add(itemvalue);
         				finitem.put("field" + itemfield.get("value"), value);
         			} 
         		}
        	}
        }
        
        //利润：结算收入-SP广告费用-采购成本-运费预估-其他费用
 		Map<String, Object> env = new HashMap<String, Object>();
 		
 		env.put("refund", getValue(one.getRefund()));
 		env.put("fbafee",getValue(one.getFbafee()));
 		env.put("commission",getValue(one.getCommission()));
 		env.put("shipping",getValue(one.getShipping()));
 		env.put("promotion", getValue(one.getPromotion()));
 		if("Amazon.fr".equals(one.getMarketplaceName())
 			|| "Amazon.nl".equals(one.getMarketplaceName())
			||"Amazon.co.uk".equals(one.getMarketplaceName())
			||"Amazon.de".equals(one.getMarketplaceName())
			||"Amazon.es".equals(one.getMarketplaceName())
            ||"Amazon.it".equals(one.getMarketplaceName())) {
 			env.put("principal", getValue(one.getPrincipal().add(one.getTax())));
 			env.put("otherfee", getValue(one.getOtherfee()));
 		}else {
 			env.put("otherfee",getValue( one.getOtherfee().add(one.getTax())));
 			env.put("principal",getValue( one.getPrincipal()));
 		}
 		
 		env.put("setincome", getValue(setincome));
 		
 		env.put("share_storagefee",getValue( one.getShareStorageFee()));
 		env.put("share_longstoragefee", getValue(one.getShareLongStorageFee()));
 		env.put("share_advspendfee", getValue(one.getShareAdvSpendFee()));
 		env.put("share_couponredemptionfee", getValue(one.getShareCouponRedemptionFee()));
 		env.put("share_reservefee", getValue(one.getShareReserveFee()));
 		env.put("share_reimbursementfee",getValue( one.getShareReimbursementFee()));
 		env.put("share_shopotherfee", getValue(one.getShareShopOtherFee()));
      
    			
 		env.put("spend",getValue( one.getRptAdvSpendFee()));
		env.put("storagefee",getValue(one.getRptStorageFee()));
		env.put("longTermFee", getValue(one.getRptLongStorageFee()));
		env.put("reimbursementsFee", getValue(one.getRptReimbursementsFee()));
		
		
 		env.put("shipmentfee", getValue(one.getProfitLocalShipmentfee()));
 		env.put("firstShipment",getValue(one.getLocalShipmentItemFee()));
 
 		

 		env.put("price", getValue(one.getLocalPrice()));
 		env.put("othersfee", getValue(one.getLocalOtherCost()));
 		env.put("returntax",getValue( one.getLocalReturnTax()));
 		env.put("avgprice",getValue( avgprice));
 		
  
    	env.put("profit_vat",getValue( one.getProfitVat()));
    	env.put("vat",getValue( one.getProfitVat()));
    	env.put("profit_otherfee", getValue(one.getProfitOtherfee()));
    	env.put("profit_marketfee",getValue( one.getProfitMarketfee()));
    	env.put("profit_costrate",getValue( one.getProfitLostrate()));
    	env.put("profit_exchangelost",getValue( one.getProfitExchangelost()));
    	env.put("profit_customstax", getValue(one.getProfitCustomstax()));
    	env.put("profit_companytax", getValue(one.getProfitCompanytax()));
    	
    	env.put("fifo_shipmentFee", getValue(one.getLocalFifoShipmentFee()));
    	env.put("fifo_cost",getValue( one.getLocalFifoCost()));
    			
 		env.put("salenum",one.getSales()==null?new BigDecimal(0):new BigDecimal(one.getSales()));
 		env.put("refundnum", one.getRefundsales()==null?new BigDecimal(0):new BigDecimal(one.getRefundsales()));
 		env.put("ordernum",one.getOrderAmount()==null?new BigDecimal(0):new BigDecimal(one.getOrderAmount()));
 	
 		
 		
 		for (Map<String, String> itemfield : fieldlist) {
 			if (finitem.get("field" + itemfield.get("value")) != null) {
 				BigDecimal itemvalue = new BigDecimal(finitem.get("field" + itemfield.get("value")).toString());
 				env.put("field" + itemfield.get("value"), getValue(itemvalue));
 			} else {
 				env.put("field" + itemfield.get("value"), new BigDecimal("0"));
 			}
 		}
 		  //执行表达式
        one.setFinSumFee(finSumFee!=null?finSumFee:new BigDecimal("0"));
        try {
        	BigDecimal profit = new BigDecimal(compiledExp.execute(env).toString());
            one.setProfit(profit);
        }catch(Exception e) {
        	one.setProfit(new BigDecimal(0));
        	e.printStackTrace();
        }

    	return one;
    }
    
    
	@Override
	@CacheEvict(value = { "findSettlementSKUCache"}, allEntries = true)
	public void summaryMonthly(String amazonauthid, String marketplaceName, Date settlementStartDate) {
		// TODO Auto-generated method stub
		AmazonAuthority auth = iAmazonAuthorityService.getById(amazonauthid);
		Calendar c=Calendar.getInstance();
		c.setTime(settlementStartDate);
		c.set(Calendar.DAY_OF_MONTH, 1);
		String startdate = GeneralUtil.formatDate(c.getTime());
		c.add(Calendar.MONTH, 1);
		String enddate = GeneralUtil.formatDate(c.getTime());
		Marketplace market = iMarketplaceService.findMapByPoint().get(marketplaceName);
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("amazonAuthId", amazonauthid);
		param.put("marketplace_name", marketplaceName);
		param.put("marketplaceid", market.getMarketplaceid());
		param.put("sellerid", auth.getSellerid());
		param.put("country",market.getMarket());
		param.put("fromDate", startdate);
		param.put("endDate", enddate);
		List<AmzSettlementSummarySkuMonth> list = amzSettlementSummarySkuMapper.summarySkuMonthly(param);
		this.baseMapper.deleteMonth(amazonauthid,marketplaceName,startdate);
		List<AmzSettlementSummarySkuMonth> marketcurrency=new LinkedList<AmzSettlementSummarySkuMonth>();
		List<AmzSettlementSummarySkuMonth> usdcurrency=new LinkedList<AmzSettlementSummarySkuMonth>();
		List<AmzSettlementSummarySkuMonth> cnycurrency=new LinkedList<AmzSettlementSummarySkuMonth>();
		
		String shopid=auth.getShopId();
		AmzFinSettlementFormula formula = iAmzFinSettlementFormulaService.getAmzFinSettlementFormula(shopid);
 		String field = formula.getField();
		String expression = "setincome-" + formula.getFormula();
 		String[] fields = field.split(",");
 		List<Map<String, String>> fieldlist = new ArrayList<Map<String, String>>();
 		Map<String, String> finitemmap = AmzFinSettlementFormulaServiceImpl.findSysFinMap();
 		for (int i = 0; i < fields.length; i++) {
 			if (!finitemmap.containsKey(fields[i])) {
 				Map<String, String> myvalue = new HashMap<String, String>();
 				myvalue.put("value", fields[i].trim());
 				myvalue.put("field", "field" + fields[i].trim());
 				fieldlist.add(myvalue);
 			}
 		}
 	    Expression compiledExp = AviatorEvaluator.compile(expression);
		Map<String, BigDecimal> longfee = findSku_LongtermStorage(param);
		Map<String, BigDecimal> storagefee = findSku_StorageFee(param);
		Map<String, Map<String, Object>> advfee = findSku_AdvSpend(param);
		Map<String, BigDecimal> firstShipmentfee = findSku_FirstShipment(param);
		Map<String,List<Map<String,Object>>> finDataFee = findSku_finItemData(param,fieldlist);
		Map<String,BigDecimal> reimbursementsFee=findSku_Reimbursements(param); 
		Map<String, Map<String, Object>> inoutShipment=findSku_InOutShipment(param);
		
		settlementStartDate=GeneralUtil.getDatez(startdate);
		for(AmzSettlementSummarySkuMonth item:list) {
			if(item.getSku()!=null&&item.getSku().equals("*")) {
				continue;
			}
			String fromcurrency=item.getCurrency();
			item.setPostedDate(settlementStartDate);
			setExtendsData(item,storagefee,longfee,firstShipmentfee,advfee,reimbursementsFee,inoutShipment);
			item.setFinDataList(finDataFee.get(item.getSku()));
			AmzSettlementSummarySkuMonth cny=currencyExchange(item,fieldlist,shopid,"CNY",compiledExp);
			AmzSettlementSummarySkuMonth usd=currencyExchange(item,fieldlist,shopid,"USD",compiledExp);
			AmzSettlementSummarySkuMonth countrycurrency=currencyExchange(item,fieldlist,shopid,fromcurrency,compiledExp);
			marketcurrency.add(countrycurrency);
			usdcurrency.add(usd);
			cnycurrency.add(cny);
		}
		if(marketcurrency.size()>0) {
			this.baseMapper.insertBatch(marketcurrency);
			this.baseMapper.insertBatchUSD(usdcurrency);
			this.baseMapper.insertBatchCNY(cnycurrency);
		}
		finDataFee.clear();
		advfee.clear();
		firstShipmentfee.clear();	
		storagefee.clear();
		longfee.clear();
		list.clear();
	}


 

	private void setExtendsData(AmzSettlementSummarySkuMonth item, Map<String, BigDecimal> storagefee,
			Map<String, BigDecimal> longfee, Map<String, BigDecimal> firstShipmentfee,
			Map<String, Map<String, Object>> advfee,Map<String, BigDecimal> reimbursementsFee,
			Map<String, Map<String, Object>> inoutShipment) {
		// TODO Auto-generated method stub
		String sku=item.getSku();
		String asin=item.getAsin();
		item.setRptStorageFee(storagefee.get(asin));
		if(item.getRptStorageFee()==null) {
			item.setRptStorageFee(new BigDecimal("0"));
		}
		item.setRptLongStorageFee(longfee.get(sku));
		if(item.getRptLongStorageFee()==null) {
			item.setRptLongStorageFee(new BigDecimal("0"));
		}
		
		item.setLocalShipmentItemFee(firstShipmentfee.get(sku));
		if(item.getLocalShipmentItemFee()==null) {
			item.setLocalShipmentItemFee(new BigDecimal("0"));
		}
		
		item.setRptReimbursementsFee(reimbursementsFee.get(sku));
		if(item.getRptReimbursementsFee()==null) {
			item.setRptReimbursementsFee(new BigDecimal("0"));
		}
		
		Map<String, Object> advfeeitem = advfee.get(sku);
		if(advfeeitem!=null) {
			item.setRptAdvSpendFee(advfeeitem.get("spend")!=null?new BigDecimal(advfeeitem.get("spend").toString()):new BigDecimal("0"));
			item.setRptAdvSales(advfeeitem.get("totalsales")!=null?new BigDecimal(advfeeitem.get("totalsales").toString()):new BigDecimal("0"));
			item.setRptAdvUnits(advfeeitem.get("units")!=null?  Integer.parseInt(advfeeitem.get("units").toString()): Integer.parseInt("0"));
		}else {
			item.setRptAdvSpendFee(new BigDecimal("0"));
			item.setRptAdvSales(new BigDecimal("0"));
			item.setRptAdvUnits( Integer.parseInt("0"));
		}
		Map<String,Object> inoutfee=inoutShipment.get(sku);
		if(inoutfee!=null) {
			item.setLocalFifoShipmentFee(inoutfee.get("transfee")!=null?new BigDecimal(inoutfee.get("transfee").toString()):new BigDecimal("0"));
			item.setLocalFifoCost(inoutfee.get("cost")!=null?new BigDecimal(inoutfee.get("cost").toString()):new BigDecimal("0"));
		}else {
			item.setLocalFifoShipmentFee(new BigDecimal("0"));
			item.setLocalFifoCost(new BigDecimal("0"));
		}
		
	}
	public void calcuteRefundRate(Map<String, Object> item){
	   if(item.get("principal")!=null&&item.get("refund")!=null) {
			BigDecimal sumprincipal = new BigDecimal(item.get("principal").toString());
			BigDecimal sumrefund = new BigDecimal(item.get("refund").toString());
			if(sumprincipal.floatValue()>=0.001) {
				BigDecimal refundrate = sumrefund.multiply(new BigDecimal("-100")).divide(sumprincipal,2,RoundingMode.HALF_UP);
				item.put("refundrate",refundrate+"%");
			}
			
		}
   }

	@Override
	public IPage<Map<String, Object>> findByCondition(Page<Object> page, Map<String, Object> map) {
		// TODO Auto-generated method stub
		IPage<Map<String, Object>> result = this.baseMapper.findByCondition(page, map);
		if(result!=null&&result.getRecords()!=null&&result.getRecords().size()>0) {
			for(Map<String, Object> item:result.getRecords()) {
				calcuteRefundRate(item);
			}
			if(page.getCurrent()==1) {
				Map<String, Object> summary = this.baseMapper.findSummaryByCondition(map);
				calcuteRefundRate(summary);
			    result.getRecords().get(0).put("summary", summary);
			}
		}
		return result;
	}


	@Override
	public void getDownloadList(SXSSFWorkbook workbook, Map<String, Object> params) {
		List<Map<String, Object>> list = this.baseMapper.findByCondition(params);
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		String ftype=params.get("type").toString();
		if("sku".equals(ftype)) {
			titlemap.put("sku", "SKU");
			titlemap.put("asin", "ASIN");
			titlemap.put("groupname", "店铺");
			titlemap.put("marketname", "站点");
			titlemap.put("ownername", "负责人名字");
		}
		if("msku".equals(ftype)) {
			titlemap.put("msku", "本地SKU");
			titlemap.put("ownername", "负责人名字");
			titlemap.put("currency", "币种");
			titlemap.put("pname", "商品名称");
			titlemap.put("mname", "本地产品名称");
		}
		if("categoryid".equals(ftype)) {
			titlemap.put("categoryname", "品类名称");
		}
		if("owner".equals(ftype)) {
			titlemap.put("ownername", "负责人名字");
		}
		if("groupid".equals(ftype)) {
			titlemap.put("groupname", "店铺名称");
		}
		if("asin".equals(ftype)) {
			titlemap.put("asin", "ASIN");
			titlemap.put("ownername", "负责人名字");
			titlemap.put("currency", "币种");
			titlemap.put("pname", "商品名称");
			titlemap.put("mname", "本地产品名称");
		}
		
		titlemap.put("order_amount", "订单量");
		titlemap.put("sales", "销量");
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
		titlemap.put("share_storage_fee", "店铺分摊_仓储费");
		titlemap.put("share_long_storage_fee", "店铺分摊_长期仓储费");
		titlemap.put("share_adv_spend_fee", "店铺分摊_广告费");
		titlemap.put("share_reserve_fee", "店铺分摊_预留金");
		titlemap.put("share_coupon_redemption_fee", "折扣券");
		titlemap.put("share_shop_other_fee", "其它店铺分摊");
		titlemap.put("income", "店铺结算");
		titlemap.put("local_price", "本地采购成本");
		titlemap.put("local_other_cost", "本地其它");
		titlemap.put("local_return_tax", "本地退税");
		titlemap.put("profit_local_shipmentfee", "本地预估运费");
		titlemap.put("profit_marketfee", "市场费用");
		titlemap.put("profit_vat", "VAT");
		titlemap.put("profit_companytax", "所得税");
		titlemap.put("profit_customstax", "关税");
		titlemap.put("profit_exchangelost", "汇率损耗");
		titlemap.put("profit_lostrate", "固定费用");
		titlemap.put("profit_otherfee", "预估其它费用");
		titlemap.put("rpt_storage_fee", "报表_仓储费");
		titlemap.put("rpt_long_storage_fee", "报表_长期仓储");
		titlemap.put("rpt_adv_spend_fee", "报表_广告费");
		titlemap.put("fin_sum_fee", "报表_其它自定义");
		titlemap.put("profit", "利润");
		titlemap.put("profitrate", "利润率");
		
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
						if("avgprice".equals(titlearray[j].toString())) {
							if(map.get("principal")!=null && map.get("sales")!=null) {
								cell.setCellValue(Float.parseFloat(map.get("principal").toString())/Float.parseFloat(map.get("sales").toString()) );
							}else {
								cell.setCellValue("0");
							}
						}else if("profitrate".equals(titlearray[j].toString())){
							if(map.get("profit")!=null && map.get("principal")!=null) {
								cell.setCellValue(Float.parseFloat(map.get("profit").toString())/Float.parseFloat(map.get("principal").toString()) );
							}else {
								cell.setCellValue("0");
							}
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
	
}
