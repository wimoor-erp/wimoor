package com.wimoor.amazon.finances.service.impl;

import com.wimoor.amazon.finances.pojo.entity.AmzSettlementReport;
import com.wimoor.amazon.finances.pojo.entity.OrdersFinancial;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportMapper;
import com.wimoor.amazon.finances.mapper.OrdersFinancialMapper;
import com.wimoor.amazon.finances.service.IOrdersFinancialService;
import com.wimoor.amazon.orders.mapper.AmzOrderMainMapper;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderMain;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.amazon.spapi.api.FinancesApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.finances.Currency;
import com.amazon.spapi.model.finances.FinancialEvents;
import com.amazon.spapi.model.finances.ListFinancialEventsResponse;
import com.amazon.spapi.model.finances.ShipmentEvent;
import com.amazon.spapi.model.finances.ShipmentEventList;
import com.amazon.spapi.model.finances.ShipmentItem;
import com.amazon.spapi.model.finances.ShipmentItemList;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-22
 */
@Service
public class OrdersFinancialServiceImpl extends ServiceImpl<OrdersFinancialMapper, OrdersFinancial> implements IOrdersFinancialService {
	@Autowired
	ApiBuildService apiBuildService;
	
	@Autowired
	IMarketplaceService marketplaceService;
	
	@Resource
	AmzOrderMainMapper amzOrderMainMapper;
	@Resource
	private AmzSettlementReportMapper amzSettlementReportMapper;
	
	public List<OrdersFinancial> getOrdersFinancialList(AmazonAuthority amazonAuthority,String orderid){
	    	LambdaQueryWrapper<AmzOrderMain> queryOrder=new LambdaQueryWrapper<AmzOrderMain>();
	    	queryOrder.eq(AmzOrderMain::getAmazonOrderId,orderid);
	    	queryOrder.eq(AmzOrderMain::getAmazonauthid,amazonAuthority.getId());
			AmzOrderMain orderMain = amzOrderMainMapper.selectOne(queryOrder);
		if(orderMain!=null&&GeneralUtil.distanceOfDay(orderMain.getPurchaseDate(), new Date())>14) {
			QueryWrapper<AmzSettlementReport> querySettlement=new QueryWrapper<AmzSettlementReport>();
			querySettlement.eq("order_id", orderid);
			List<AmzSettlementReport> list = amzSettlementReportMapper.selectList(querySettlement);
			if(list!=null&&list.size()>0) {
				List<OrdersFinancial> result=new LinkedList<OrdersFinancial>();
				for(AmzSettlementReport item:list) {
					OrdersFinancial one =new OrdersFinancial();
					one.setAmount(item.getAmount());
					one.setAmazonOrderId(orderid);
					one.setCurrency(item.getCurrency());
					one.setFtype(item.getAmountDescription());
					one.setPostedDate(item.getPostedDate());
					one.setSku(item.getSku());
					one.setOpttime(LocalDateTime.now());
					result.add(one);
				}
				return result;
			}
		
		}
		LambdaQueryWrapper<OrdersFinancial> query = new LambdaQueryWrapper<OrdersFinancial>().eq(OrdersFinancial::getAmazonOrderId, orderid);
		List<OrdersFinancial> result = this.list(query);
	    if(result.size()>0)return result;
	    else {
	    	 return setFinancialByOrderId( amazonAuthority, orderid,null);
	    }
	}
	
    public List<OrdersFinancial> saveFinancialData(List<?> chargeList,Date postdate,ShipmentItem item,String amazonOrderId) {
    	List<OrdersFinancial> result =new ArrayList<OrdersFinancial>();
		if (chargeList != null) {
			for (int k = 0; k < chargeList.size(); k++) {
				Object component = chargeList.get(k);
				OrdersFinancial amzOrderFinancial = new OrdersFinancial();
				Map<String,Object> map = GeneralUtil.objectToMap(component);
				for(Object key:map.keySet()) {
			    	if(key.toString().contains("Type")) {
						amzOrderFinancial.setFtype(map.get(key).toString());
			    	}
			    	if(key.toString().contains("Amount")) {
			    		Currency amount = (Currency) map.get(key);
						amzOrderFinancial.setAmount(amount.getCurrencyAmount());
						amzOrderFinancial.setCurrency(amount.getCurrencyCode());
			    	}
			    	if(map.get(key) instanceof List) {
			    		@SuppressWarnings("unchecked")
						List<Object> listvalue = (List<Object>)map.get(key);
			    		for(int i=0;i<listvalue.size();i++) {
			    			Object itemvalue = listvalue.get(i);
			    			Map<String,Object> valuemap = GeneralUtil.objectToMap(itemvalue);
			    			for(Object keyitem:valuemap.keySet()) {
			    		    	if(keyitem.toString().contains("Type")) {
									amzOrderFinancial.setFtype(valuemap.get(keyitem).toString());
						    	}
						    	if(keyitem.toString().contains("Amount")) {
						    		Currency amount = (Currency) valuemap.get(keyitem);
						    		if(amount.getCurrencyAmount().equals(new BigDecimal("0.0"))) {
						    			continue;
						    		}
									amzOrderFinancial.setAmount(amount.getCurrencyAmount());
									amzOrderFinancial.setCurrency(amount.getCurrencyCode());
						    	}
			    			}
			    		}
			    	}
			    }
				if(amzOrderFinancial.getAmount()==null || amzOrderFinancial.getAmount().equals(new BigDecimal("0.0"))) {
					continue;
				}
				amzOrderFinancial.setPostedDate(postdate);
				amzOrderFinancial.setOrderItemId(item.getOrderItemId());
				amzOrderFinancial.setSku(item.getSellerSKU());
				amzOrderFinancial.setAmazonOrderId(amazonOrderId);
				amzOrderFinancial.setOpttime(LocalDateTime.now());
				this.save(amzOrderFinancial);
				result.add(amzOrderFinancial);
			}
		}
		return result;
    }
    
	public List<OrdersFinancial> setFinancialByOrderId(AmazonAuthority amazonAuthority,String orderid,String nexttoken) {
		// TODO Auto-generated method stub
		FinancesApi api = apiBuildService.getFinancesApi(amazonAuthority);
		List<OrdersFinancial> resultList =new ArrayList<OrdersFinancial>();
		try {
			ListFinancialEventsResponse response = api.listFinancialEventsByOrderId(orderid, 100, nexttoken);
			 FinancialEvents result = response.getPayload().getFinancialEvents();
			 ShipmentEventList list = result.getShipmentEventList();
			 ShipmentEventList refundList = result.getRefundEventList();
			 for(ShipmentEvent item:list) {
				 ShipmentItemList itemlist = item.getShipmentItemList();
				 String marketname = item.getMarketplaceName();
				 Map<String, Marketplace> pointMarket = marketplaceService.findMapByPoint();
				 Marketplace market = pointMarket.get(marketname);
				 Date postedDate=GeneralUtil.getDatePlus(item.getPostedDate(),market.getMarket());
				 for(ShipmentItem shipmentItem:itemlist) {
					 Map<String, Object> map = GeneralUtil.objectToMap(shipmentItem);
					 for(Object key :map.keySet()) {
						 if(key.toString().contains("List")) {
							 List<?> chargeList=(List<?>)map.get(key);
							 List<OrdersFinancial> msublist=saveFinancialData(chargeList,postedDate,shipmentItem,orderid);
							 if(msublist!=null&&msublist.size()>0) {
								 resultList.addAll(msublist);
							 }
						 }
					 }
				 }
			 }
			 
			 for(ShipmentEvent item:refundList) {
				   ShipmentItemList itemlist = item.getShipmentItemAdjustmentList();
				 String marketname = item.getMarketplaceName();
				 Map<String, Marketplace> pointMarket = marketplaceService.findMapByPoint();
				 Marketplace market = pointMarket.get(marketname);
				 Date postedDate=GeneralUtil.getDatePlus(item.getPostedDate(),market.getMarket());
				 for(ShipmentItem shipmentItem:itemlist) {
					 Map<String, Object> map = GeneralUtil.objectToMap(shipmentItem);
					 for(Object key :map.keySet()) {
						 if(key.toString().contains("List")) {
							 List<?> chargeList=(List<?>)map.get(key);
							 List<OrdersFinancial> msublist=saveFinancialData(chargeList,postedDate,shipmentItem,orderid);
							 if(msublist!=null&&msublist.size()>0) {
						     	 resultList.addAll(msublist);
							 }
						 }
					 }
				 }
			 }
			 if(response.getPayload().getNextToken()!=null) {
				 List<OrdersFinancial> msublist=setFinancialByOrderId(amazonAuthority,orderid,nexttoken);
				 resultList.addAll(msublist);
			 }
			 return resultList;
			 
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("Api调用错误："+e.getMessage());
		}
	}
}
