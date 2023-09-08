package com.wimoor.amazon.finances.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.amazon.spapi.api.FinancesApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.finances.FinancialEvents;
import com.amazon.spapi.model.finances.ListFinancialEventsResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.finances.mapper.AmazonSettlementOpenMapper;
import com.wimoor.amazon.finances.pojo.entity.AmazonSettlementOpen;
import com.wimoor.amazon.finances.pojo.entity.AmzFinAccount;
import com.wimoor.amazon.finances.service.IAmazonSettlementOpenService;
import com.wimoor.amazon.finances.service.IAmzFinAccountService;
import com.wimoor.amazon.util.AmzDateUtils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
@Service
public class AmazonSettlementOpenServiceImpl extends ServiceImpl<AmazonSettlementOpenMapper, AmazonSettlementOpen> implements IAmazonSettlementOpenService {
@Autowired
ApiBuildService apiBuildService;
@Autowired
IMarketplaceService marketplaceService;
@Autowired
IAmazonAuthorityService iAmazonAuthorityService;
@Autowired
IAmzFinAccountService iAmzFinAccountService;
	public void getGroupIdData() throws ApiException, InterruptedException {
		   List<AmazonAuthority> list = iAmazonAuthorityService.getAllAuth();
		   for( AmazonAuthority amazonAuthority:list) {
			   List<AmzFinAccount> finlist = iAmzFinAccountService.lambdaQuery().eq(AmzFinAccount::getAmazonAuthid, amazonAuthority.getId())
			                                      .eq(AmzFinAccount::getProcessingStatus,"Open").list();
			   for(AmzFinAccount item:finlist) {
				   getGroupIdData( amazonAuthority, item);
			   }
		   }
	    }
	public void getGroupIdData(AmazonAuthority amazonAuthority) throws ApiException, InterruptedException {
			   List<AmzFinAccount> finlist = iAmzFinAccountService.lambdaQuery().eq(AmzFinAccount::getAmazonAuthid, amazonAuthority.getId())
			                                      .eq(AmzFinAccount::getProcessingStatus,"Open").list();
			   for(AmzFinAccount item:finlist) {
				   getGroupIdData( amazonAuthority, item);
			   }
	    }
	public void getGroupIdData(AmazonAuthority amazonAuthority,AmzFinAccount item) throws ApiException, InterruptedException {
		if(StrUtil.isBlank(item.getNexttoken())) {
			LambdaQueryWrapper<AmazonSettlementOpen> query=new LambdaQueryWrapper<AmazonSettlementOpen>()
			          .eq(AmazonSettlementOpen::getAmazonauthid, amazonAuthority.getId())
			          .eq(AmazonSettlementOpen::getGroupId, item.getGroupid());
			this.remove(query);
		}
		if(item.getProcessingStatus().equals("Open")) {
			     listFinancialEventsByGroupId(amazonAuthority,item,item.getNexttoken());
		      } 
        }
	   ExecutorService executorService = Executors.newSingleThreadExecutor();
 
	    public void listFinancialEventsByGroupId(AmazonAuthority amazonAuthority,AmzFinAccount fin,String nextToken) throws ApiException, InterruptedException {
	    	  FinancesApi api = apiBuildService.getFinancesApi(amazonAuthority);
	          ListFinancialEventsResponse response = api.listFinancialEventsByGroupId(fin.getGroupid(), 100,nextToken);
	          FinancialEvents events = response.getPayload().getFinancialEvents();
	          CompletableFuture.runAsync(() ->{
	            try{
	            System.out.println("异步线程 =====> 开始 =====> " + LocalDateTime.now());
	                saveEvent(amazonAuthority,events,fin);
	            System.out.println("异步线程 =====> 结束 =====> " + LocalDateTime.now());
	            }catch (Exception e){
	                e.printStackTrace();
	            }
	        },executorService);

	        System.out.println("继续调用下一页的数据");
	        // TODO: 打印其他費用
	        if (events.getAffordabilityExpenseEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getAffordabilityExpenseEventList()));
	        }
	        if (events.getShipmentSettleEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getShipmentSettleEventList()));
	        }
	        if (events.getServiceProviderCreditEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getServiceProviderCreditEventList()));
	        }
	        if (events.getDebtRecoveryEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getDebtRecoveryEventList()));
	        }
	        if (events.getTrialShipmentEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getTrialShipmentEventList()));
	        }
	        if (events.getSaFETReimbursementEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getSaFETReimbursementEventList()));
	        }
	      
	        if (events.getTaxWithholdingEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getTaxWithholdingEventList()));
	        }
	        if (events.getRefundEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getRefundEventList()));
	        }
	        if (events.getSaFETReimbursementEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getSaFETReimbursementEventList()));
	        }
	        if (events.getRetrochargeEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getRetrochargeEventList()));
	        }
	        if (events.getAffordabilityExpenseReversalEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getAffordabilityExpenseReversalEventList()));
	        }
	        if (events.getLoanServicingEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getLoanServicingEventList()));
	        }
	        if (events.getNetworkComminglingTransactionEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getNetworkComminglingTransactionEventList()));
	        }
	        if (events.getImagingServicesFeeEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getImagingServicesFeeEventList()));
	        }
	        if (events.getSaFETReimbursementEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getSaFETReimbursementEventList()));
	        }
	        if (events.getSellerReviewEnrollmentPaymentEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getSellerReviewEnrollmentPaymentEventList()));
	        }

	        if (events.getGuaranteeClaimEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getGuaranteeClaimEventList()));
	        }
	        if (events.getPayWithAmazonEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getPayWithAmazonEventList()));
	        }
	        if (events.getRentalTransactionEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getRentalTransactionEventList()));
	        }
	        if (events.getFbALiquidationEventList().size()>0){
	            System.out.println(JSON.toJSONString(events.getFbALiquidationEventList()));
	        }
	     
	        fin.setNexttoken(response.getPayload()!=null?response.getPayload().getNextToken():null);
	        LambdaQueryWrapper<AmzFinAccount> query=new LambdaQueryWrapper<AmzFinAccount>()
			          .eq(AmzFinAccount::getAmazonAuthid, amazonAuthority.getId())
			          .eq(AmzFinAccount::getGroupid, fin.getGroupid());
	        iAmzFinAccountService.update(fin,query);
	        if (response.getPayload()!=null && response.getPayload().getNextToken() !=null){
	            Thread.sleep(2000);
	            listFinancialEventsByGroupId(amazonAuthority,fin,response.getPayload().getNextToken());
	        }else {
	            nextToken=null;
	            Thread.sleep(10000);
	          //  executorService.shutdown(); // 回收线程池
	            System.out.println(fin.getGroupid()+" 结算数据下载完毕");

	        }

	        // TODO: test validations
	    }
		String currencyToMarketplaceName(AmazonAuthority amazonAuthority ,String currency){
			String marketname="";
			if ("USD".equals(currency)) {
				marketname = "Amazon.com";
			} else if ("CAD".equals(currency)) {
				marketname = "Amazon.ca";
			} else if ("GBP".equals(currency)) {
				marketname = "Amazon.co.uk";
			} else if ("INR".equals(currency)) {
				marketname = "Amazon.in";
			} else if ("JPY".equals(currency)) {
				marketname = "Amazon.co.jp";
			} else if ("AUD".equals(currency)) {
				marketname = "Amazon.com.au";
			} else if ("MXN".equals(currency)) {
				marketname = "Amazon.com.mx";
			} else if ("AED".equals(currency)) {
				marketname = "Amazon.ae";
			}else if ("SAR".equals(currency)) {
				marketname = "Amazon.sa";
			}else if("PLN".equals(currency)) {
				marketname = "Amazon.pl";
			}else if("SEK".equals(currency)) {
				marketname = "Amazon.se";
			}else if ("EUR".equals(currency)) {
				List<Marketplace> market = marketplaceService.findbyauth(amazonAuthority.getId());
				for (Marketplace item : market) {
					if (currency != null && currency.equals(item.getCurrency())) {
						marketname = item.getPointName();
						break;
					}
				}
				if (StrUtil.isEmpty(marketname)) {
					if (amazonAuthority.getMarketPlace().getCurrency().equals(currency)) {
						marketname = amazonAuthority.getMarketPlace().getPointName();
					} else {
						marketname = "Amazon.co.uk";
					}
				}
			}
			return marketname;
		}
	 public void saveEvent(AmazonAuthority amazonAuthority,FinancialEvents events,AmzFinAccount fin){
		    String groupId=fin.getGroupid();
	        List<AmazonSettlementOpen> list=new ArrayList<>();
	        //ShipmentEvent
	        if (events.getShipmentEventList().size()>0) {
	            events.getShipmentEventList().forEach(a -> {
	            	DateTime postDate = DateUtil.parse(a.getPostedDate());
	                if (a.getShipmentItemList() != null) {
	                    a.getShipmentItemList().forEach(b -> {
	                        if (b.getItemChargeList() != null) {
	                            b.getItemChargeList().stream().filter(tt -> tt.getChargeAmount().getCurrencyAmount().compareTo(BigDecimal.ZERO) != 0).forEach(c -> {
	                                if (c.getChargeAmount().getCurrencyAmount().compareTo(BigDecimal.ZERO) != 0) {
	                                    AmazonSettlementOpen e = new AmazonSettlementOpen();
	                                    e.setGroupId(groupId);
	                                    e.setAmazonOrderId(a.getAmazonOrderId());
	                                    e.setPostedDate(postDate);
	                                    e.setAmazonauthid(amazonAuthority.getId());
	                                    e.setMarketplaceName(a.getMarketplaceName());
	                                    e.setEventType("Shipment");
	                                    e.setSku(b.getSellerSKU());
	                                    e.setQuantity(b.getQuantityShipped());
	                                    e.setOrderItemId(b.getOrderItemId());
	                                    e.setCurrency(c.getChargeAmount().getCurrencyCode());
	                                    e.setAmount(c.getChargeAmount().getCurrencyAmount());
	                                    e.setFtype(c.getChargeType());
	                                    e.setCreateTime(new Date()); 
	                                    list.add(e);
	                                }
	                            });
	                        }
	                        if (b.getItemFeeList() != null) {
	                            b.getItemFeeList().stream().filter(tt -> tt.getFeeAmount().getCurrencyAmount().compareTo(BigDecimal.ZERO) != 0).forEach(c -> {
	                                AmazonSettlementOpen e = new AmazonSettlementOpen();
	                                e.setGroupId(groupId);
	                                e.setAmazonOrderId(a.getAmazonOrderId());
	                                e.setPostedDate(postDate);
	                                e.setAmazonauthid(amazonAuthority.getId());
	                                e.setMarketplaceName(a.getMarketplaceName());;
	                                e.setEventType("Shipment");
	                                e.setSku(b.getSellerSKU());
	                                e.setQuantity(b.getQuantityShipped());
	                                e.setOrderItemId(b.getOrderItemId());
	                                e.setCurrency(c.getFeeAmount().getCurrencyCode());
	                                e.setAmount(c.getFeeAmount().getCurrencyAmount());
	                                e.setFtype(c.getFeeType());
	                                e.setCreateTime(new Date()); 
	                                list.add(e);

	                            });
	                        }
	                        if (b.getItemTaxWithheldList() != null) {
	                            b.getItemTaxWithheldList().get(0).getTaxesWithheld().forEach(c -> {
	                                if (c.getChargeAmount().getCurrencyAmount().compareTo(BigDecimal.ZERO) != 0) {
	                                    AmazonSettlementOpen e = new AmazonSettlementOpen();
	                                    e.setGroupId(groupId);
	                                    e.setAmazonOrderId(a.getAmazonOrderId());
	                                    e.setPostedDate(postDate);
	                                    e.setAmazonauthid(amazonAuthority.getId());
	                                    e.setMarketplaceName(a.getMarketplaceName());;
	                                    e.setEventType("Shipment");
	                                    e.setSku(b.getSellerSKU());
	                                    e.setQuantity(b.getQuantityShipped());
	                                    e.setOrderItemId(b.getOrderItemId());
	                                    e.setCurrency(c.getChargeAmount().getCurrencyCode());
	                                    e.setAmount(c.getChargeAmount().getCurrencyAmount());
	                                    e.setFtype(c.getChargeType());
	                                    e.setCreateTime(new Date()); 
	                                    list.add(e);
	                                }

	                            });
	                        }
	                        if (b.getPromotionList() != null) {
	                            b.getPromotionList().stream().filter(tt -> tt.getPromotionAmount().getCurrencyAmount().compareTo(BigDecimal.ZERO) != 0).forEach(c -> {
	                                AmazonSettlementOpen e = new AmazonSettlementOpen();
	                                e.setGroupId(groupId);
	                                e.setAmazonOrderId(a.getAmazonOrderId());
	                                e.setPostedDate(postDate);
	                                e.setAmazonauthid(amazonAuthority.getId());
	                                e.setMarketplaceName(a.getMarketplaceName());;
	                                e.setEventType("Shipment");
	                                e.setSku(b.getSellerSKU());
	                                e.setQuantity(b.getQuantityShipped());
	                                e.setOrderItemId(b.getOrderItemId());
	                                e.setCurrency(c.getPromotionAmount().getCurrencyCode());
	                                e.setAmount(c.getPromotionAmount().getCurrencyAmount());
	                                e.setFtype(c.getPromotionType());
	                                e.setCreateTime(new Date()); 
	                                list.add(e);
	                            });
	                        }

	                    });
	                }

	            });

	        }
	        //Refund
	        if (events.getRefundEventList().size()>0){
	            events.getRefundEventList().forEach(a->{
	               	DateTime postDate = DateUtil.parse(a.getPostedDate());
	                if (a.getShipmentItemAdjustmentList()!=null){
	                    a.getShipmentItemAdjustmentList().forEach(b->{
	                        if(b.getItemChargeAdjustmentList()!=null){
	                            b.getItemChargeAdjustmentList().stream().filter(tt->tt.getChargeAmount().getCurrencyAmount().compareTo(BigDecimal.ZERO)!=0).forEach(c->{
	                                AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                                e.setAmazonOrderId(a.getAmazonOrderId());
	                                e.setPostedDate(postDate);
	                                e.setAmazonauthid(amazonAuthority.getId());
	                                e.setMarketplaceName(a.getMarketplaceName());;
	                                e.setEventType("Refund");
	                                e.setSku(b.getSellerSKU());
	                                e.setQuantity(b.getQuantityShipped());
	                                e.setOrderItemId(b.getOrderAdjustmentItemId());
	                                e.setCurrency(c.getChargeAmount().getCurrencyCode());
	                                e.setAmount(c.getChargeAmount().getCurrencyAmount());
	                                e.setFtype(c.getChargeType());
	                                e.setCreateTime(new Date()); 
	                                list.add(e);
	                            });
	                        }
	                        if(b.getItemFeeAdjustmentList()!=null){
	                            b.getItemFeeAdjustmentList().stream().filter(tt->tt.getFeeAmount().getCurrencyAmount().compareTo(BigDecimal.ZERO)!=0).forEach(c->{
	                                AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                                e.setAmazonOrderId(a.getAmazonOrderId());
	                                e.setPostedDate(postDate);
	                                e.setAmazonauthid(amazonAuthority.getId());
	                                e.setMarketplaceName(a.getMarketplaceName());;
	                                e.setEventType("Refund");
	                                e.setSku(b.getSellerSKU());
	                                e.setQuantity(b.getQuantityShipped());
	                                e.setOrderItemId(b.getOrderAdjustmentItemId());
	                                e.setCurrency(c.getFeeAmount().getCurrencyCode());
	                                e.setAmount(c.getFeeAmount().getCurrencyAmount());
	                                e.setFtype(c.getFeeType());
	                                e.setCreateTime(new Date()); 
	                                list.add(e);
	                            });
	                        }
	                        if(b.getItemTaxWithheldList()!=null ){
	                            b.getItemTaxWithheldList().get(0).getTaxesWithheld().forEach(c->{
	                                if (c.getChargeAmount().getCurrencyAmount().compareTo(BigDecimal.ZERO) != 0){
	                                    AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                                    e.setAmazonOrderId(a.getAmazonOrderId());
	                                    e.setPostedDate(postDate);
	                                    e.setAmazonauthid(amazonAuthority.getId());
	                                    e.setMarketplaceName(a.getMarketplaceName());;
	                                    e.setEventType("Refund");
	                                    e.setSku(b.getSellerSKU());
	                                    e.setQuantity(b.getQuantityShipped());
	                                    e.setOrderItemId(b.getOrderItemId());
	                                    e.setCurrency(c.getChargeAmount().getCurrencyCode());
	                                    e.setAmount(c.getChargeAmount().getCurrencyAmount());
	                                    e.setFtype(c.getChargeType());
	                                    e.setCreateTime(new Date()); 
	                                    list.add(e);
	                                }

	                            });
	                        }
	                        if(b.getPromotionAdjustmentList()!=null ){
	                            b.getPromotionAdjustmentList().forEach(c->{
	                                if (c.getPromotionAmount().getCurrencyAmount().compareTo(BigDecimal.ZERO) != 0){
	                                    AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                                    e.setAmazonOrderId(a.getAmazonOrderId());
	                                    e.setPostedDate(postDate);
	                                    e.setAmazonauthid(amazonAuthority.getId());
	                                    e.setMarketplaceName(a.getMarketplaceName());;
	                                    e.setEventType("Refund");
	                                    e.setSku(b.getSellerSKU());
	                                    e.setQuantity(b.getQuantityShipped());
	                                    e.setOrderItemId(b.getOrderItemId());
	                                    e.setCurrency(c.getPromotionAmount().getCurrencyCode());
	                                    e.setAmount(c.getPromotionAmount().getCurrencyAmount());
	                                    e.setFtype(c.getPromotionType());
	                                    e.setCreateTime(new Date()); 
	                                    list.add(e);
	                                }

	                            });
	                        }

	                    });
	                }


	            });

	        }
	        //Adjustment
	        if (events.getAdjustmentEventList().size()>0){
	            events.getAdjustmentEventList().forEach(a->{
	                if (a.getAdjustmentItemList()!=null){
	                    a.getAdjustmentItemList().forEach(b->{
	                       	DateTime postDate = DateUtil.parse(a.getPostedDate());
	                        AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                        e.setPostedDate(postDate);
	                        e.setAmazonauthid(amazonAuthority.getId());
	                        e.setEventType("Adjustment");

	                        e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,b.getTotalAmount().getCurrencyCode()));
	                        e.setSku(b.getSellerSKU());
	                        e.setQuantity(Integer.valueOf(b.getQuantity()));

	                        e.setCurrency(b.getTotalAmount().getCurrencyCode());
	                        e.setAmount(b.getTotalAmount().getCurrencyAmount());
	                        e.setFtype(a.getAdjustmentType());
	                        e.setCreateTime(new Date()); 
	                        list.add(e);
	                    });
	                }

	            });

	        }
	        //SellerDealPaymen
	        if (events.getSellerDealPaymentEventList().size()>0){
	            events.getSellerDealPaymentEventList().forEach(a->{
	               	DateTime postDate = DateUtil.parse(a.getPostedDate());
	                AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                e.setPostedDate(postDate);
	                e.setAmazonauthid(amazonAuthority.getId());
	                e.setEventType("SellerDealPaymen");
	                e.setOrderItemId(a.getDealId());
	                e.setCurrency(a.getFeeAmount().getCurrencyCode());
	                e.setAmount(a.getFeeAmount().getCurrencyAmount());
	                e.setFtype(a.getFeeType());
	                e.setCreateTime(new Date()); 
	                list.add(e);
	            });

	        }
	        //ServiceFeeEvent
	        if (events.getServiceFeeEventList().size()>0){
	            events.getServiceFeeEventList().forEach(a-> {
	               	  Date postDate = AmzDateUtils.getDate(fin.getFinancialEventGroupEnd());
	                if (a.getFeeList()!=null){
	                    AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                    e.setAmazonOrderId(a.getAmazonOrderId());
                        e.setPostedDate(postDate);
	                    e.setAmazonauthid(amazonAuthority.getId());
	                    e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,a.getFeeList().get(0).getFeeAmount().getCurrencyCode()));
	                    e.setEventType("ServiceFeeEvent");
	                    e.setSku(a.getSellerSKU());

	                    e.setCurrency(a.getFeeList().get(0).getFeeAmount().getCurrencyCode());
	                    e.setAmount(a.getFeeList().get(0).getFeeAmount().getCurrencyAmount());
	                    e.setFtype(a.getFeeList().get(0).getFeeType());
	                    e.setCreateTime(new Date()); 
	                    list.add(e);

	                }
	            });
	        }
	        //ChargebackEvent
	        if (events.getChargebackEventList().size()>0){
	            events.getChargebackEventList().forEach(a->{
	                if (a.getShipmentItemAdjustmentList()!=null){
	                   	DateTime postDate = DateUtil.parse(a.getPostedDate());
	                    a.getShipmentItemAdjustmentList().forEach(b->{
	                        if (b.getItemChargeAdjustmentList()!=null){
	                            b.getItemChargeAdjustmentList().forEach(c->{
	                                AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                                e.setAmazonOrderId(a.getAmazonOrderId());
	                                e.setPostedDate(postDate);
	                                e.setAmazonauthid(amazonAuthority.getId());
	                                e.setMarketplaceName(a.getMarketplaceName());;
	                                e.setEventType("ChargebackEvent");
	                                e.setSku(b.getSellerSKU());
	                                e.setQuantity(b.getQuantityShipped());
	                                e.setOrderItemId(b.getOrderItemId());
	                                e.setCurrency(c.getChargeAmount().getCurrencyCode());
	                                e.setAmount(c.getChargeAmount().getCurrencyAmount());
	                                e.setFtype(c.getChargeType());
	                                e.setCreateTime(new Date()); 
	                                list.add(e);

	                            });
	                        }
	                        if (b.getItemFeeAdjustmentList()!=null){
	                            b.getItemFeeAdjustmentList().forEach(c->{
	                                AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                                e.setAmazonOrderId(a.getAmazonOrderId());
	                                e.setPostedDate(postDate);
	                                e.setAmazonauthid(amazonAuthority.getId());
	                                e.setMarketplaceName(a.getMarketplaceName());;
	                                e.setEventType("ChargebackEvent");
	                                e.setSku(b.getSellerSKU());
	                                e.setQuantity(b.getQuantityShipped());
	                                e.setOrderItemId(b.getOrderItemId());
	                                e.setCurrency(c.getFeeAmount().getCurrencyCode());
	                                e.setAmount(c.getFeeAmount().getCurrencyAmount());
	                                e.setFtype(c.getFeeType());
	                                e.setCreateTime(new Date()); 
	                                list.add(e);
	                            });
	                        }
	                        if (b.getItemTaxWithheldList()!=null){
	                            b.getItemTaxWithheldList().forEach(c->{
	                                AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                                e.setAmazonOrderId(a.getAmazonOrderId());
	                                e.setPostedDate(postDate);
	                                e.setAmazonauthid(amazonAuthority.getId());
	                                e.setMarketplaceName(a.getMarketplaceName());;
	                                e.setEventType("ChargebackEvent");
	                                e.setSku(b.getSellerSKU());
	                                e.setQuantity(b.getQuantityShipped());
	                                e.setOrderItemId(b.getOrderItemId());
	                                e.setCurrency(c.getTaxesWithheld().get(0).getChargeAmount().getCurrencyCode());
	                                e.setAmount(c.getTaxesWithheld().get(0).getChargeAmount().getCurrencyAmount());
	                                e.setFtype(c.getTaxesWithheld().get(0).getChargeType());
	                                e.setCreateTime(new Date()); 
	                                list.add(e);
	                            });
	                        }
	                    });
	                }

	                if (a.getShipmentItemList()!=null){
	                   	DateTime postDate = DateUtil.parse(a.getPostedDate());
	                    a.getShipmentItemList().forEach(b->{
	                        if (b.getItemChargeList()!=null){
	                            b.getItemChargeAdjustmentList().forEach(c->{
	                                AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                                e.setAmazonOrderId(a.getAmazonOrderId());
	                                e.setPostedDate(postDate);
	                                e.setAmazonauthid(amazonAuthority.getId());
	                                e.setMarketplaceName(a.getMarketplaceName());;
	                                e.setEventType("ChargebackEvent");
	                                e.setSku(b.getSellerSKU());
	                                e.setQuantity(b.getQuantityShipped());
	                                e.setOrderItemId(b.getOrderItemId());
	                                e.setCurrency(c.getChargeAmount().getCurrencyCode());
	                                e.setAmount(c.getChargeAmount().getCurrencyAmount());
	                                e.setFtype(c.getChargeType());
	                                e.setCreateTime(new Date());
	                                list.add(e);

	                            });
	                        }
	                        if (b.getPromotionList()!=null){
	                            b.getItemFeeAdjustmentList().forEach(c->{
	                                AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                                e.setAmazonOrderId(a.getAmazonOrderId());
	                                e.setPostedDate(postDate);
	                                e.setAmazonauthid(amazonAuthority.getId());
	                                e.setMarketplaceName(a.getMarketplaceName());;
	                                e.setEventType("ChargebackEvent");
	                                e.setSku(b.getSellerSKU());
	                                e.setQuantity(b.getQuantityShipped());
	                                e.setOrderItemId(b.getOrderItemId());
	                                e.setCurrency(c.getFeeAmount().getCurrencyCode());
	                                e.setAmount(c.getFeeAmount().getCurrencyAmount());
	                                e.setFtype(c.getFeeType());
	                                e.setCreateTime(new Date()); 
	                                list.add(e);
	                            });
	                        }
	                        if (b.getItemFeeList()!=null){
	                            b.getItemFeeAdjustmentList().forEach(c->{
	                                AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                                e.setAmazonOrderId(a.getAmazonOrderId());
	                                e.setPostedDate(postDate);
	                                e.setAmazonauthid(amazonAuthority.getId());
	                                e.setMarketplaceName(a.getMarketplaceName());;
	                                e.setEventType("ChargebackEvent");
	                                e.setSku(b.getSellerSKU());
	                                e.setQuantity(b.getQuantityShipped());
	                                e.setOrderItemId(b.getOrderItemId());
	                                e.setCurrency(c.getFeeAmount().getCurrencyCode());
	                                e.setAmount(c.getFeeAmount().getCurrencyAmount());
	                                e.setFtype(c.getFeeType());
	                                e.setCreateTime(new Date()); 
	                                list.add(e);
	                            });
	                        }
	                        if (b.getItemTaxWithheldList()!=null){
	                            b.getItemTaxWithheldList().forEach(c->{
	                                AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                                e.setAmazonOrderId(a.getAmazonOrderId());
	                                e.setPostedDate(postDate);
	                                e.setAmazonauthid(amazonAuthority.getId());
	                                e.setMarketplaceName(a.getMarketplaceName());;
	                                e.setEventType("ChargebackEvent");
	                                e.setSku(b.getSellerSKU());
	                                e.setQuantity(b.getQuantityShipped());
	                                e.setOrderItemId(b.getOrderItemId());
	                                e.setCurrency(c.getTaxesWithheld().get(0).getChargeAmount().getCurrencyCode());
	                                e.setAmount(c.getTaxesWithheld().get(0).getChargeAmount().getCurrencyAmount());
	                                e.setFtype(c.getTaxesWithheld().get(0).getChargeType());
	                                e.setCreateTime(new Date());
	                                list.add(e);
	                            });
	                        }
	                    });

	                }






	            });

	        }
	        //RemovalShipmentAdjustment
	        if (events.getRemovalShipmentAdjustmentEventList().size()>0){
	            events.getRemovalShipmentAdjustmentEventList().forEach(a->{
	               	DateTime postDate = DateUtil.parse(a.getPostedDate());
	                a.getRemovalShipmentItemAdjustmentList().forEach(b->{
	                    if (b.getRevenueAdjustment()!=null){
	                        AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                        e.setAmazonOrderId(a.getOrderId());
	                        e.setPostedDate(postDate);
	                        e.setAmazonauthid(amazonAuthority.getId());
	                        e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,b.getRevenueAdjustment().getCurrencyCode()));
	                        e.setEventType("RemovalShipmentAdjustment");
	                        e.setSku(b.getFulfillmentNetworkSKU());
	                        e.setQuantity(b.getAdjustedQuantity());
	                        e.setOrderItemId(a.getMerchantOrderId());
	                        e.setCurrency(b.getRevenueAdjustment().getCurrencyCode());
	                        e.setAmount(b.getRevenueAdjustment().getCurrencyAmount());
	                        e.setFtype("RevenueAdjustment");
	                        e.setCreateTime(new Date()); 
	                        list.add(e);
	                    }
	                    if (b.getTaxAmountAdjustment()!=null){
	                        AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                        e.setAmazonOrderId(a.getOrderId());
	                        e.setPostedDate(postDate);
	                        e.setAmazonauthid(amazonAuthority.getId());
	                        e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,b.getRevenueAdjustment().getCurrencyCode()));
	                        e.setEventType("RemovalShipmentAdjustment");
	                        e.setSku(b.getFulfillmentNetworkSKU());
	                        e.setQuantity(b.getAdjustedQuantity());
	                        e.setOrderItemId(a.getMerchantOrderId());
	                        e.setCurrency(b.getRevenueAdjustment().getCurrencyCode());
	                        e.setAmount(b.getRevenueAdjustment().getCurrencyAmount());
	                        e.setFtype("TaxAmountAdjustment");
	                        e.setCreateTime(new Date()); 
	                        list.add(e);
	                    }
	                    if (b.getTaxWithheldAdjustment()!=null){
	                        AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                        e.setAmazonOrderId(a.getOrderId());
	                        e.setPostedDate(postDate);
	                        e.setAmazonauthid(amazonAuthority.getId());
	                        e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,b.getRevenueAdjustment().getCurrencyCode()));
	                        e.setEventType("RemovalShipmentAdjustment");
	                        e.setSku(b.getFulfillmentNetworkSKU());
	                        e.setQuantity(b.getAdjustedQuantity());
	                        e.setOrderItemId(a.getMerchantOrderId());
	                        e.setCurrency(b.getRevenueAdjustment().getCurrencyCode());
	                        e.setAmount(b.getRevenueAdjustment().getCurrencyAmount());
	                        e.setFtype("TaxWithheldAdjustment");
	                        e.setCreateTime(new Date()); 
	                        list.add(e);
	                    }
	                });


	            });

	        }
	        //ProductAdsPayment
	        if (events.getProductAdsPaymentEventList().size()>0){
	            events.getProductAdsPaymentEventList().forEach(a->{
	               	DateTime postDate = DateUtil.parse(a.getPostedDate());
	                if (a.getTransactionValue() !=null){
	                    AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                    e.setPostedDate(postDate);
	                    e.setAmazonauthid(amazonAuthority.getId());
	                    e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,a.getTransactionValue().getCurrencyCode()));
	                    e.setEventType("ProductAdsPayment");
	                    e.setOrderItemId(a.getInvoiceId());
	                    e.setCurrency(a.getTransactionValue().getCurrencyCode());
	                    e.setAmount(a.getTransactionValue().getCurrencyAmount());
	                    e.setFtype("transactionValue:"+a.getTransactionType());
	                    e.setCreateTime(new Date()); 
	                    list.add(e);
	                }
	                if (a.getBaseValue() !=null){
	                    AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                    e.setPostedDate(postDate);
	                    e.setAmazonauthid(amazonAuthority.getId());
	                    e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,a.getBaseValue().getCurrencyCode()));
	                    e.setEventType("ProductAdsPayment");
	                    e.setOrderItemId(a.getInvoiceId());
	                    e.setCurrency(a.getBaseValue().getCurrencyCode());
	                    e.setAmount(a.getBaseValue().getCurrencyAmount());
	                    e.setFtype("baseValue:"+a.getTransactionType());
	                    e.setCreateTime(new Date()); 
	                    list.add(e);
	                }
	                if (a.getTaxValue() !=null){
	                    AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                    e.setPostedDate(postDate);
	                    e.setAmazonauthid(amazonAuthority.getId());
	                    e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,a.getTaxValue().getCurrencyCode()));
	                    e.setEventType("ProductAdsPayment");
	                    e.setOrderItemId(a.getInvoiceId());
	                    e.setCurrency(a.getTaxValue().getCurrencyCode());
	                    e.setAmount(a.getTaxValue().getCurrencyAmount());
	                    e.setFtype("taxValue:"+a.getTransactionType());
	                    e.setCreateTime(new Date()); 
	                    list.add(e);
	                }

	            });
	        }
	        //CouponPayment
	        if (events.getCouponPaymentEventList().size()>0){
	            events.getCouponPaymentEventList().forEach(a->{
	               	DateTime postDate = DateUtil.parse(a.getPostedDate());
	                if (a.getChargeComponent()!=null){
	                    AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                    e.setPostedDate(postDate);
	                    e.setAmazonauthid(amazonAuthority.getId());
	                    e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,a.getChargeComponent().getChargeAmount().getCurrencyCode()));
	                    e.setEventType("CouponPayment");

	                    e.setAmazonOrderId(a.getCouponId());
	                    e.setCurrency(a.getChargeComponent().getChargeAmount().getCurrencyCode());
	                    e.setAmount(a.getChargeComponent().getChargeAmount().getCurrencyAmount());
	                    e.setFtype(a.getChargeComponent().getChargeType());
	                    e.setCreateTime(new Date()); 
	                    list.add(e);
	                }
	                if (a.getFeeComponent()!=null){
	                    AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                    e.setPostedDate(postDate);
	                    e.setAmazonauthid(amazonAuthority.getId());
	                    e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,a.getChargeComponent().getChargeAmount().getCurrencyCode()));
	                    e.setEventType("CouponPayment");
	                    e.setAmazonOrderId(a.getCouponId());
	                    e.setCurrency(a.getFeeComponent().getFeeAmount().getCurrencyCode());
	                    e.setAmount(a.getFeeComponent().getFeeAmount().getCurrencyAmount());
	                    e.setFtype(a.getFeeComponent().getFeeType());
	                    e.setCreateTime(new Date()); 
	                    list.add(e);
	                }
	            });
	        }

	        //RemovalShipment
	        if (events.getRemovalShipmentEventList().size()>0){
	            events.getRemovalShipmentEventList().forEach(a->{
	               	DateTime postDate = DateUtil.parse(a.getPostedDate());
	                a.getRemovalShipmentItemList().forEach(b->{
	                    if (b.getFeeAmount()!=null && b.getFeeAmount().getCurrencyAmount().compareTo(BigDecimal.ZERO)!=0){
	                        AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                        e.setAmazonOrderId(a.getOrderId());
	                        e.setPostedDate(postDate);
	                        e.setAmazonauthid(amazonAuthority.getId());
	                        e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,b.getFeeAmount().getCurrencyCode()));
	                        e.setEventType("RemovalShipment");
	                        e.setSku(b.getFulfillmentNetworkSKU());
	                        e.setQuantity(b.getQuantity());
	                        e.setOrderItemId(b.getRemovalShipmentItemId());
	                        e.setCurrency(b.getFeeAmount().getCurrencyCode());
	                        e.setAmount(b.getFeeAmount().getCurrencyAmount());
	                        e.setFtype("FeeAmount");
	                        e.setCreateTime(new Date()); 
	                        list.add(e);
	                    }
	                    if (b.getRevenue()!=null && b.getRevenue().getCurrencyAmount().compareTo(BigDecimal.ZERO)!=0){
	                        AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                        e.setAmazonOrderId(a.getOrderId());
	                        e.setPostedDate(postDate);
	                        e.setAmazonauthid(amazonAuthority.getId());
	                        e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,b.getRevenue().getCurrencyCode()));
	                        e.setEventType("RemovalShipment");
	                        e.setSku(b.getFulfillmentNetworkSKU());
	                        e.setQuantity(b.getQuantity());
	                        e.setOrderItemId(b.getRemovalShipmentItemId());
	                        e.setCurrency(b.getRevenue().getCurrencyCode());
	                        e.setAmount(b.getRevenue().getCurrencyAmount());
	                        e.setFtype("Revenue");
	                        e.setCreateTime(new Date()); 
	                        list.add(e);
	                    }
	                    if (b.getTaxAmount()!=null && b.getTaxAmount().getCurrencyAmount().compareTo(BigDecimal.ZERO)!=0){
	                        AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                        e.setAmazonOrderId(a.getOrderId());
	                        e.setPostedDate(postDate);
	                        e.setAmazonauthid(amazonAuthority.getId());
	                        e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,b.getTaxAmount().getCurrencyCode()));
	                        e.setEventType("RemovalShipment");
	                        e.setSku(b.getFulfillmentNetworkSKU());
	                        e.setQuantity(b.getQuantity());
	                        e.setOrderItemId(b.getRemovalShipmentItemId());
	                        e.setCurrency(b.getTaxAmount().getCurrencyCode());
	                        e.setAmount(b.getTaxAmount().getCurrencyAmount());
	                        e.setFtype("TaxAmount");
	                        e.setCreateTime(new Date()); 
	                        list.add(e);
	                    }
	                    if (b.getTaxWithheld()!=null && b.getTaxWithheld().getCurrencyAmount().compareTo(BigDecimal.ZERO)!=0){
	                        AmazonSettlementOpen e=new AmazonSettlementOpen(); e.setGroupId(groupId);
	                        e.setAmazonOrderId(a.getOrderId());
	                        e.setPostedDate(postDate);
	                        e.setAmazonauthid(amazonAuthority.getId());
	                        e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,b.getTaxWithheld().getCurrencyCode()));
	                        e.setEventType("RemovalShipment");
	                        e.setSku(b.getFulfillmentNetworkSKU());
	                        e.setQuantity(b.getQuantity());
	                        e.setOrderItemId(b.getRemovalShipmentItemId());
	                        e.setCurrency(b.getTaxWithheld().getCurrencyCode());
	                        e.setAmount(b.getTaxWithheld().getCurrencyAmount());
	                        e.setFtype("TaxWithheld");
	                        e.setCreateTime(new Date()); 
	                        list.add(e);
	                    }
	                });

	            });
	        }
	        //Retrocharge
	        if (events.getRetrochargeEventList().size()>0) {
	            events.getRetrochargeEventList().forEach(a -> {
	               	DateTime postDate = DateUtil.parse(a.getPostedDate());
	                if (a.getBaseTax() != null && a.getBaseTax().getCurrencyAmount().compareTo(BigDecimal.ZERO)!=0) {
	                    AmazonSettlementOpen e = new AmazonSettlementOpen();
	                    e.setGroupId(groupId);
	                    e.setAmazonOrderId(a.getAmazonOrderId());
	                    e.setPostedDate(postDate);
	                    e.setAmazonauthid(amazonAuthority.getId());
	                    e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,a.getBaseTax().getCurrencyCode()));
	                    e.setEventType("Retrocharge");
	                    e.setCurrency(a.getBaseTax().getCurrencyCode());
	                    e.setAmount(a.getBaseTax().getCurrencyAmount());
	                    e.setFtype("BaseTax");
	                    e.setCreateTime(new Date()); 
	                    list.add(e);
	                }
	                if (a.getShippingTax() != null && a.getShippingTax().getCurrencyAmount().compareTo(BigDecimal.ZERO)!=0) {
	                    AmazonSettlementOpen e = new AmazonSettlementOpen();
	                    e.setGroupId(groupId);
	                    e.setAmazonOrderId(a.getAmazonOrderId());
	                    e.setPostedDate(postDate);
	                    e.setAmazonauthid(amazonAuthority.getId());
	                    e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,a.getShippingTax().getCurrencyCode()));
	                    e.setEventType("Retrocharge");
	                    e.setCurrency(a.getShippingTax().getCurrencyCode());
	                    e.setAmount(a.getShippingTax().getCurrencyAmount());
	                    e.setFtype("ShippingTax");
	                    e.setCreateTime(new Date()); 
	                    list.add(e);
	                }
	                a.getRetrochargeTaxWithheldList().forEach(b -> {
	                    if (b.getTaxesWithheld() != null) {
	                        if(b.getTaxesWithheld().size()>0){
	                            AmazonSettlementOpen e = new AmazonSettlementOpen();
	                            e.setGroupId(groupId);
	                            e.setAmazonOrderId(a.getAmazonOrderId());
	                            e.setPostedDate(postDate);
	                            e.setAmazonauthid(amazonAuthority.getId());
	                            e.setMarketplaceName(currencyToMarketplaceName(amazonAuthority,b.getTaxesWithheld().get(0).getChargeAmount().getCurrencyCode()));
	                            e.setEventType("Retrocharge");
	                            e.setCurrency(b.getTaxesWithheld().get(0).getChargeAmount().getCurrencyCode());
	                            e.setAmount(b.getTaxesWithheld().get(0).getChargeAmount().getCurrencyAmount());
	                            e.setFtype("TaxesWithheld");
	                            e.setCreateTime(new Date()); 
	                            list.add(e);
	                        }
	                    }

	                });

	            });
	        }
	        List<AmazonSettlementOpen> addlist=new ArrayList<>();
	       	 for(AmazonSettlementOpen item:list) {
	       		 if(item.getPostedDate()!=null) {
	       			 addlist.add(item);
	       		 }
	       	 }
	        if(addlist.size()>0) {
	        	saveBatch(list);
	        }
	    }
}
