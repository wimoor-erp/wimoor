package com.wimoor.amazon.orders.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.OrdersV0Api;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.orders.Address;
import com.amazon.spapi.model.orders.GetOrdersResponse;
import com.amazon.spapi.model.orders.Order;
import com.amazon.spapi.model.orders.OrderList;
import com.amazon.spapi.model.orders.OrdersList;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmzAuthApiTimelimit;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmzAuthApiTimelimitService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.orders.mapper.AmzOrderBuyerShipAddressMapper;
import com.wimoor.amazon.orders.mapper.AmzOrderMainMapper;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderBuyerShipAddress;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderMain;
import com.wimoor.amazon.orders.service.IAmzOrderItemService;
import com.wimoor.amazon.orders.service.IAmzOrderMainService;
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
public class AmzOrderMainServiceImpl extends ServiceImpl<AmzOrderMainMapper,AmzOrderMain> implements IAmzOrderMainService {
	
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
 
    public void handlerOrderResponse(AmazonAuthority auth,AmzAuthApiTimelimit apilimit,Date startDate,GetOrdersResponse response) {
    	String shopid=auth.getShopId();
		String authid=auth.getId();
		String groupid=auth.getGroupid();
		//请求成功 返回数据
		OrdersList list = response.getPayload();
		OrderList orders = list.getOrders();
		if(orders!=null && orders.size()>0) {
			for(int i=0;i<orders.size();i++) {
				Order order = orders.get(i);
				String orderid = order.getAmazonOrderId();
				AmzOrderMain oldorder = this.getById(orderid);
				Address address = order.getAssignedShipFromLocationAddress();
				if(address!=null) {
					String addressline1 = address.getAddressLine1();
					String addressline2 = address.getAddressLine2();
					String addressline3= address.getAddressLine3();
					String addresstype = address.getAddressType().getValue();
					String city = address.getCity();
					String countrycode = address.getCountryCode();
					String county = address.getCounty();
					String district=address.getDistrict();
					String municipality = address.getMunicipality();
					String addressname = address.getName();
					String phone=address.getPhone();
					String postalcode = address.getPostalCode();
					String stateorRegion = address.getStateOrRegion();
					AmzOrderBuyerShipAddress addEntity=new AmzOrderBuyerShipAddress();
					addEntity.setAddressLine1(addressline1);
					addEntity.setAddressLine2(addressline2);
					addEntity.setAddressLine3(addressline3);
					addEntity.setAddressType(addresstype);
					addEntity.setAmazonauthid(authid);
					addEntity.setCity(city);
					addEntity.setAmazonOrderid(orderid);
					addEntity.setCountryCode(countrycode);
					addEntity.setCounty(county);
					addEntity.setDistrict(district);
					addEntity.setMarketplaceid(order.getMarketplaceId());
					addEntity.setMunicipality(municipality);
					addEntity.setName(addressname);
					addEntity.setOpttime(new Date());
					addEntity.setPhone(phone);
					addEntity.setPostalCode(postalcode);
					addEntity.setStateOrRegion(stateorRegion);
					if(oldorder!=null&&oldorder.getBuyerAdress()!=null) {
						addEntity.setId(orderid);
					}
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
				entity.setGroupid(groupid);
				entity.setShopid(shopid);
				entity.setHasItem(false);
				if(oldorder!=null) {
					entity.setHasItem(oldorder.isHasItem());
                   this.updateById(entity);					
				}else {
					save(entity);
				}
				amzOrderItemService.ordersItem(entity, null);
			}
		}
		 
		}
 
 
	private void requestOrderByAuth(String sellerid,List<Marketplace> marketplaceList) {
		   String token=null;
		   Date startDate=new Date();
		   startDate=GeneralUtil.getYesterday(startDate);
		   AmazonAuthority auth = amazonAuthorityService.selectBySellerId(sellerid);
		   if(auth!=null) {
			   OrdersV0Api api = apiBuildService.getOrdersV0Api(auth);
			   if(api!=null) {
				   try {
					   AmzAuthApiTimelimit apilimit=auth.getApiRateLimit("getOrders");
					   if( GeneralUtil.isNotEmpty(apilimit.getNexttoken())) {
						   token=apilimit.getNexttoken();
					   }
					   if(apilimit.getEndTime()!=null) {
						     startDate=apilimit.getEndTime();
					   }
						List<String> marketplaceIds=new ArrayList<String>();
						for(Marketplace market:marketplaceList) {
							marketplaceIds.add(market.getMarketplaceid());
						}
						Calendar c=Calendar.getInstance();
						c.setTime(startDate);
						c.add(Calendar.HOUR, -8);
						//String createdAfter=GeneralUtil.formatDate(startDate, "yyyy-MM-dd'T'HH:mm:ssZZ");
						String lastUpdatedAfter=GeneralUtil.formatDate(startDate, "yyyy-MM-dd'T'HH:mm:ss'Z'");
			            List<String> fulfillmentChannels=new ArrayList<String>();
			            fulfillmentChannels.add("AFN");
			            fulfillmentChannels.add("MFN");
			            List<String> paymentMethods=new ArrayList<String>();
			            paymentMethods.add("COD");
			            paymentMethods.add("CVS");
			            paymentMethods.add("Other");
			            List<String> orderStatuses=new ArrayList<String>();
			            orderStatuses.add("Pending");
			            orderStatuses.add("Unshipped");
			            orderStatuses.add("PartiallyShipped");
			            orderStatuses.add("Shipped");
			            orderStatuses.add("InvoiceUnconfirmed");
			            orderStatuses.add("Canceled");
			            orderStatuses.add("Unfulfillable"); 
			            if(apilimit.apiNotRateLimit()) {
			            	ApiCallback<GetOrdersResponse> orderReponseHandler=new ApiCallbackGetOrders(this,auth,apilimit,startDate);
							if(StrUtil.isBlank(token)) {
								api.getOrdersAsync(marketplaceIds, null, null, lastUpdatedAfter,
										 null, orderStatuses, fulfillmentChannels, paymentMethods, 
										 null, null, 100, 
										 null, null, null, orderReponseHandler); 
							}else {
								api.getOrdersAsync(marketplaceIds, null, null, null,
										 null, orderStatuses, fulfillmentChannels, paymentMethods, 
										 null, null, 100, 
										 null, token, null, orderReponseHandler); 
							}
			            }
					 
				} catch (ApiException e) {
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
		       List<Marketplace> marketlist = marketplaceService.selectMarketBySellerid(amazonAuthority.getSellerid(),amazonAuthority.getShopId());
		       this.requestOrderByAuth(amazonAuthority.getSellerid(),marketlist);
		  }
 
	
}
