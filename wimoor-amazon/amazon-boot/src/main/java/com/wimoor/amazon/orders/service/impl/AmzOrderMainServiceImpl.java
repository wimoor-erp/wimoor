package com.wimoor.amazon.orders.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.amazon.spapi.api.OrdersV0Api;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.orders.Address;
import com.amazon.spapi.model.orders.GetOrdersResponse;
import com.amazon.spapi.model.orders.Order;
import com.amazon.spapi.model.orders.OrderList;
import com.amazon.spapi.model.orders.OrdersList;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmzAuthApiTimelimit;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmzAuthApiTimelimitService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.notifications.service.IAwsSQSMessageHandlerService;
import com.wimoor.amazon.orders.mapper.AmzOrderBuyerShipAddressMapper;
import com.wimoor.amazon.orders.mapper.AmzOrderMainMapper;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderBuyerShipAddress;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderItem;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderMain;
import com.wimoor.amazon.orders.service.IAmzOrderItemService;
import com.wimoor.amazon.orders.service.IAmzOrderMainService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 订单备注 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Service("amazonOrdersService")
public class AmzOrderMainServiceImpl extends ServiceImpl<AmzOrderMainMapper,AmzOrderMain> implements IAmzOrderMainService,IAwsSQSMessageHandlerService {
	
	@Resource
	AmzOrderBuyerShipAddressMapper amzOrderBuyerShipAddressMapper;
	@Resource
	IAmazonAuthorityService amazonAuthorityService;
	@Resource
	IAmzAuthApiTimelimitService iAmzAuthApiTimelimitService;
	@Resource
	IMarketplaceService marketplaceService;
	@Autowired
	ApiBuildService apiBuildService;
	@Resource
	IAmzOrderItemService amzOrderItemService;
	
 
	public int saveAddress(AmzOrderBuyerShipAddress entity) {
		AmzOrderBuyerShipAddress oldone = amzOrderBuyerShipAddressMapper.selectById(entity.getAmazonOrderid());
		if(oldone!=null) {
			return amzOrderBuyerShipAddressMapper.updateById(entity);
		}else {
			return amzOrderBuyerShipAddressMapper.insert(entity);
		}
	}
 
    public void handlerOrderResponse(AmazonAuthority auth,AmzAuthApiTimelimit apilimit,GetOrdersResponse response) {
		String authid=auth.getId();
		//请求成功 返回数据
		OrdersList list = response.getPayload();
		OrderList orders = list.getOrders();
		if(orders!=null && orders.size()>0) {
			for(int i=0;i<orders.size();i++) {
				Order order = orders.get(i);
				String orderid = order.getAmazonOrderId();
				AmzOrderMain oldorder = this.getById(orderid);
				Address address = order.getShippingAddress();
				if(address!=null) {
					AmzOrderBuyerShipAddress addEntity=new AmzOrderBuyerShipAddress();
					addEntity.setShipAddress(address);
					addEntity.setAmazonOrderid(orderid);
					addEntity.setAmazonauthid(authid);
					addEntity.setMarketplaceid(order.getMarketplaceId());
					saveAddress(addEntity);
				}
				String label = order.getCbaDisplayableShippingLabel();
				String eardate = order.getEarliestDeliveryDate();
				String shipdate = order.getEarliestShipDate();
				String channel = order.getFulfillmentChannel()!=null?order.getFulfillmentChannel().getValue():null;
				String tructionid = order.getFulfillmentInstruction()!=null? order.getFulfillmentInstruction().getFulfillmentSupplySourceId():null;
				String lastupdates = order.getLastUpdateDate();
				String laterdate = order.getLatestDeliveryDate();
				String lateshipdate = order.getLatestShipDate();
				String mmarketplaceid = order.getMarketplaceId();
				Integer itemsShiped = order.getNumberOfItemsShipped();
				Integer itemsUnshiped = order.getNumberOfItemsUnshipped();
				String orderchannel = order.getOrderChannel();
				String orderstatus = order.getOrderStatus()==null?null:order.getOrderStatus().getValue();
				BigDecimal ordertotal = order.getOrderTotal()==null?null:new BigDecimal(order.getOrderTotal().getAmount());
				String ordertype = order.getOrderType()==null?null:order.getOrderType().getValue();
				String paymethod = order.getPaymentMethod()==null?null:order.getPaymentMethod().getValue();
				String duedate = order.getPromiseResponseDueDate();
				String purchasedate = order.getPurchaseDate();
				String replaceid = order.getReplacedOrderId();
				String saleschannel = order.getSalesChannel();
				String sellerorderid = order.getSellerOrderId();
				String category = order.getShipmentServiceLevelCategory();
				String shiplevel = order.getShipServiceLevel();
				Marketplace marketplace = marketplaceService.getById(mmarketplaceid);
				AmzOrderMain entity=new AmzOrderMain();
				entity.setAmazonOrderId(orderid);
				entity.setMarketplaceid(mmarketplaceid);
				entity.setAmazonauthid(authid);
				entity.setCbadisplayableshippinglabel(label);
				entity.setEarliestdeliverydate(GeneralUtil.getDatePlus(eardate,marketplace.getMarket()));
				entity.setEarliestshipdate(GeneralUtil.getDatePlus(shipdate,marketplace.getMarket()));
				entity.setFulfillmentChannel(channel);
				entity.setFulfillmentSupplySourceid(tructionid);
				entity.setLastUpdatedDate(GeneralUtil.getDatePlus(lastupdates,marketplace.getMarket()));
				entity.setLatestdeliverydate(GeneralUtil.getDatePlus(laterdate,marketplace.getMarket()));
				entity.setLatestshipdate(GeneralUtil.getDatePlus(lateshipdate,marketplace.getMarket()));
				entity.setNumberofitemsshipped(itemsShiped);
				entity.setNumberofitemsunshipped(itemsUnshiped);
				entity.setOrderChannel(orderchannel);
				entity.setOrderStatus(orderstatus);
				entity.setOrderTotal(ordertotal);
				entity.setOrdertype(ordertype);
				entity.setPaymentmethod(paymethod);
				entity.setPromiseResponseDuedate(GeneralUtil.getDatePlus(duedate,marketplace.getMarket()));
				entity.setPurchaseDate(GeneralUtil.getDatePlus(purchasedate,marketplace.getMarket()));
				entity.setReplacedOrderid(replaceid);
				entity.setSalesChannel(saleschannel);
				entity.setSellerOrderId(sellerorderid);
				entity.setShipmentServicelevelCategory(category);
				entity.setShipServiceLevel(shiplevel);
				entity.setBusinessOrder(order.isIsBusinessOrder());
				entity.setHasItem(false);
				if(oldorder!=null) {
                   LambdaQueryWrapper<AmzOrderMain> query=new LambdaQueryWrapper<AmzOrderMain>();
                   query.eq(AmzOrderMain::getAmazonOrderId, entity.getAmazonOrderId());
                   query.eq(AmzOrderMain::getAmazonauthid, entity.getAmazonauthid());
				   this.update(entity,query);					
				}else {
					save(entity);
				}
			}
		}
		 
		}
 
 
	private void requestOrderByAuth(AmazonAuthority amazonAuthority,List<Marketplace> marketplaceList) {
		   String token=null;
		   Calendar c1=Calendar.getInstance();
		   AmazonAuthority auth =amazonAuthority;
		   if(auth!=null) {
			   auth.setUseApi("getOrders");
			   OrdersV0Api api = apiBuildService.getOrdersV0Api(auth);
			   if(api!=null) {
				   try {
					   AmzAuthApiTimelimit apilimit=auth.getApiRateLimit();
					   if( GeneralUtil.isNotEmpty(apilimit.getNexttoken())) {
						   token=apilimit.getNexttoken();
					   }
					   if(apilimit.getEndTime()!=null) {
						   c1.setTime(apilimit.getEndTime());
					   } else {
						   c1.add(Calendar.HOUR_OF_DAY, -1);
					   }
						List<String> marketplaceIds=new ArrayList<String>();
						for(Marketplace market:marketplaceList) {
							marketplaceIds.add(market.getMarketplaceid());
						}
						c1.add(Calendar.MINUTE, -1);
						String lastUpdatedAfter=AmzDateUtils.getDateToUTCStr(c1.getTime());
						Date startDate = c1.getTime();
			            List<String> fulfillmentChannels=new ArrayList<String>();
			            fulfillmentChannels.add("AFN");
			            fulfillmentChannels.add("MFN");
			            List<String> paymentMethods=new ArrayList<String>();
			            paymentMethods.add("COD");
			            paymentMethods.add("CVS");
			            paymentMethods.add("Other");
			         
			            if(apilimit.apiNotRateLimit()) {
							if(StrUtil.isBlank(token)) {
								ApiCallback<GetOrdersResponse> orderReponseHandler=new ApiCallbackGetOrders(this,auth,apilimit,startDate);
								api.getOrdersAsync(marketplaceIds, null,null, lastUpdatedAfter,
										 null, null, fulfillmentChannels, paymentMethods, 
										 null, null, 100, 
										 null, null, null,null,null,null, orderReponseHandler); 
							}else {
								ApiCallback<GetOrdersResponse> orderReponseHandler=new ApiCallbackGetOrders(this,auth,apilimit,null);
								api.getOrdersAsync(marketplaceIds, null, null, null,
										 null, null, fulfillmentChannels, paymentMethods, 
										 null, null, 100, 
										 null, token, null,null,null,null, orderReponseHandler); 
							}
			            }
					 
				} catch (ApiException e) {
					auth.setApiRateLimit( null, e);
					e.printStackTrace(); 
				} catch (Exception e) {
					e.printStackTrace();
				} 
				    
			   } 
		   } 
	 } 


	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		      try {
		    	  List<Marketplace> marketlist = marketplaceService.selectMarketBySellerid(amazonAuthority.getSellerid(),amazonAuthority.getShopId());
		    	  this.requestOrderByAuth(amazonAuthority,marketlist);
		      }catch(Exception e) {
		    	  e.printStackTrace();
		      }
		  }

	@Override
	public void removeDataArchive() {
		// TODO Auto-generated method stub
		List<AmazonAuthority> authlist =amazonAuthorityService.getAllAuth();
		for(AmazonAuthority auth:authlist) {
			this.baseMapper.removeDataArchive(auth.getId());
		}
	}

	@SuppressWarnings("unused")
	@Override
	public boolean handlerMessage(JSONObject body) {
		// TODO Auto-generated method stub
		JSONObject orderStatusChangeNotification = body.getJSONObject("OrderStatusChangeNotification");
		String sellerId=orderStatusChangeNotification.getString("SellerId");
		String marketplaceId=orderStatusChangeNotification.getString("MarketplaceId");
		String amazonOrderId=orderStatusChangeNotification.getString("AmazonOrderId");
		Date purchaseDate=orderStatusChangeNotification.getDate("PurchaseDate");
		String orderStatus=orderStatusChangeNotification.getString("OrderStatus");
		String destinationPostalCode=orderStatusChangeNotification.getString("DestinationPostalCode");
		String supplySourceId=orderStatusChangeNotification.getString("SupplySourceId");
		String orderItemId=orderStatusChangeNotification.getString("OrderItemId");
		String sellerSKU=orderStatusChangeNotification.getString("SellerSKU");
		Integer quantity=orderStatusChangeNotification.getIntValue("Quantity");
		String fulfillmentChannel=orderStatusChangeNotification.getString("FulfillmentChannel");
	
		AmazonAuthority auth = amazonAuthorityService.selectBySellerId(sellerId);
		if(auth==null||purchaseDate==null) {
			return true;
		}
		AmzOrderMain oldorder = this.lambdaQuery().eq(AmzOrderMain::getAmazonOrderId, amazonOrderId)
				.eq(AmzOrderMain::getAmazonauthid, auth.getId())
				.one();
		Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplaceId);
		String sales_channel="";
		if(market!=null) {
			sales_channel=market.getPointName();
			Calendar c=Calendar.getInstance();
			c.setTime(purchaseDate);
			c.add(Calendar.HOUR_OF_DAY, -8);
			purchaseDate=GeneralUtil.getDatePlus(c, market.getMarket());
		}
		boolean isshipped=false;
		if(oldorder!=null) {
			if(oldorder.getOrderStatus()==null||!oldorder.getOrderStatus().equals(orderStatus)) {
				if(orderStatus!=null&&orderStatus.equals("Shipped")) {
					isshipped=true;
				}
				oldorder.setOrderStatus(orderStatus);
				oldorder.setHasItem(false);
		        LambdaQueryWrapper<AmzOrderMain> query=new LambdaQueryWrapper<AmzOrderMain>();
                query.eq(AmzOrderMain::getAmazonOrderId, oldorder.getAmazonOrderId());
                query.eq(AmzOrderMain::getAmazonauthid, oldorder.getAmazonauthid());
				this.update(oldorder,query);
			}
		}else {
			AmzOrderMain order=new AmzOrderMain();
			order.setAmazonauthid(auth.getId());
			order.setAmazonOrderId(amazonOrderId);
			order.setMarketplaceid(marketplaceId);
			order.setOrderStatus(orderStatus);
			order.setSalesChannel(sales_channel);
			order.setFulfillmentChannel(fulfillmentChannel);
			order.setPurchaseDate(purchaseDate);
			order.setHasItem(false);
			try {
				this.baseMapper.insert(order);
			}catch(Exception e) {
				LambdaQueryWrapper<AmzOrderMain> query=new LambdaQueryWrapper<AmzOrderMain>();
                query.eq(AmzOrderMain::getAmazonOrderId, order.getAmazonOrderId());
                query.eq(AmzOrderMain::getAmazonauthid, order.getAmazonauthid());
                AmzOrderMain morder = this.getOne(query);
                if(morder!=null) {
                	morder.setOrderStatus(order.getOrderStatus());
                	this.baseMapper.update(morder,query);
                }else {
                	this.baseMapper.insert(order);
                }
			}
		}
		QueryWrapper<AmzOrderItem> query=new QueryWrapper<AmzOrderItem>();
		query.eq("amazon_order_id", amazonOrderId);
		query.eq("orderItemId", orderItemId);
		AmzOrderItem oldorderitem = amzOrderItemService.getOne(query);
		if(oldorderitem!=null) {
			if(oldorderitem.getQuantityordered()==null||!oldorderitem.getQuantityordered().equals(quantity)
					||isshipped) {
				oldorderitem.setQuantityordered(quantity);
				if(isshipped&&(oldorderitem.getQuantityshipped()==null||!oldorderitem.getQuantityshipped().equals(quantity))) {
					oldorderitem.setQuantityshipped(quantity);
				}
				amzOrderItemService.update(oldorderitem, query);
			}
		}else {
			AmzOrderItem orderitem =new AmzOrderItem();
			orderitem.setAmazonauthid(auth.getId());
			orderitem.setAmazonOrderId(amazonOrderId);
			orderitem.setOrderitemid(orderItemId);
			orderitem.setSku(sellerSKU);
			orderitem.setMarketplaceid(marketplaceId);
			orderitem.setQuantityordered(quantity);
			if(isshipped) {
				orderitem.setQuantityshipped(quantity);
			}
			orderitem.setPurchaseDate(purchaseDate);
		}
		return true;
	}
 
	
}
