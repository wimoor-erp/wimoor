package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.orders.mapper.OrdersFulfilledShipmentsReportMapper;
import com.wimoor.amazon.orders.pojo.entity.OrdersFulfilledShipmentsReport;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.EmojiFilterUtils;
import com.wimoor.common.GeneralUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
 
@Slf4j
@Service("reportAmzFulFilledShipmentsService")
public class ReportAmzFulFilledShipmentsServiceImpl extends ReportServiceImpl{
	@Resource
	private OrdersFulfilledShipmentsReportMapper ordersFulfilledShipmentsReportMapper;
	
	private BigDecimal getBigDecimalValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			if(isNumericzidai(info[position])) {
				return new BigDecimal(info[position]);
			}
		}
		return null;
	}

	private String getStrValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			return info[position];
		}
		return null;
	}

	
	public static boolean isNumericzidai(String str) {
		if (str == null)
			return false;
		Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)   {
		// TODO Auto-generated method stub
		//      amazon-order-id	
		//      merchant-order-id	
		//      shipment-id	
		//		shipment-item-id	
		//		amazon-order-item-id	
		//		merchant-order-item-id	
		//		purchase-date	
		//		payments-date	
		//		shipment-date	
		//		reporting-date	
		//		buyer-email	
		//		buyer-name	
		//		buyer-phone-number	
		//		sku	
		//		product-name	
		//		quantity-shipped	
		//		currency	
		//		item-price	
		//		item-tax	
		//		shipping-price	
		//		shipping-tax	
		//		gift-wrap-price	
		//		gift-wrap-tax	
		//		ship-service-level	
		//		recipient-name	
		//		ship-address-1	
		//		ship-address-2	
		//		ship-address-3	
		//		ship-city	
		//		ship-state	
		//		ship-postal-code	
		//		ship-country	
		//		ship-phone-number	
		//		bill-address-1	
		//		bill-address-2	
		//		bill-address-3	
		//		bill-city	
		//		bill-state	
		//		bill-postal-code	
		//		bill-country	
		//		item-promotion-discount	
		//		ship-promotion-discount	
		//		carrier	
		//		tracking-number	
		//		estimated-arrival-date	
		//		fulfillment-center-id	
		//		fulfillment-channel	
		//		sales-channel
				int lineNumber = 0;
				String line;
				String mlog="";
				Map<String,Integer> titleList = new HashMap<String,Integer>();
				List<OrdersFulfilledShipmentsReport> list=new ArrayList<OrdersFulfilledShipmentsReport>();
				try {
					while ((line = br.readLine()) != null) {
						String[] info = line.split("\t");
						if (lineNumber == 0) {
							for (int i = 0; i < info.length; i++) {
								titleList.put(info[i],i);
							}
						}else {
							OrdersFulfilledShipmentsReport report = new OrdersFulfilledShipmentsReport();
							report.setAmazonOrderId(getStrValue(info,titleList,"amazon-order-id"));
							report.setMerchantOrderId(getStrValue(info,titleList,"merchant-order-id"));
							if(report.getMerchantOrderId().length()>=50) {
								report.setMerchantOrderId(report.getMerchantOrderId().substring(0, 49));
							}
							report.setShipmentId(getStrValue(info,titleList,"shipment-id"));
							report.setShipmentItemId(getStrValue(info,titleList,"shipment-item-id"));
							report.setAmazonOrderItemId(getStrValue(info,titleList,"amazon-order-item-id"));
							report.setMerchantOrderItemId(getStrValue(info,titleList,"merchant-order-item-id"));
							if(report.getMerchantOrderItemId().length()>=30) {
								report.setMerchantOrderItemId(report.getMerchantOrderItemId().substring(0, 29));
							}
						
					
							String paymentdate=info[titleList.get("payments-date")];
							String purchasedate=info[titleList.get("purchase-date")];
							String shipdate=info[titleList.get("shipment-date")];
						    String reportingdate=info[titleList.get("reporting-date")];
							report.setBuyerEmail(getStrValue(info,titleList,"buyer-email"));
							report.setBuyerName(getStrValue(info,titleList,"buyer-name"));
						
							report.setBuyerPhoneNumber(getStrValue(info,titleList,"buyer-phone-number"));
							report.setSku(getStrValue(info,titleList,"sku"));
							if(report.getSku()!=null&&report.getSku().length()>=50) {
								report.setSku(report.getSku().substring(0, 49));
							}
							//report.setProductName(EmojiFilterUtils.filterEmoji(getStrValue(info,titleList,"product-name")));
							report.setProductName("");
							report.setQuantityShipped(  Integer.parseInt(info[titleList.get("quantity-shipped")]));
							report.setCurrency(getStrValue(info,titleList,"currency"));
							report.setItemPrice(getBigDecimalValue(info,titleList,"item-price"));
							report.setItemTax(getBigDecimalValue(info,titleList,"item-tax"));
							report.setShippingPrice(getBigDecimalValue(info,titleList,"shipping-price"));
							report.setShippingTax(getBigDecimalValue(info,titleList,"shipping-tax"));
							report.setGiftWrapPrice(getBigDecimalValue(info,titleList,"gift-wrap-price"));
							report.setGiftWrapTax(getBigDecimalValue(info,titleList,"gift-wrap-tax"));
							report.setShipServiceLevel(getStrValue(info,titleList,"ship-service-level"));
							report.setRecipientName(getStrValue(info,titleList,"recipient-name"));
							report.setShipAddress1(EmojiFilterUtils.filterEmoji(getStrValue(info,titleList,"ship-address-1")));
							report.setShipAddress2(EmojiFilterUtils.filterEmoji(getStrValue(info,titleList,"ship-address-2")));
							report.setShipAddress3(EmojiFilterUtils.filterEmoji(getStrValue(info,titleList,"ship-address-3")));
							report.setShipCity(getStrValue(info,titleList,"ship-city"));
							report.setShipState(getStrValue(info,titleList,"ship-state"));
							report.setShipPostalCode(getStrValue(info,titleList,"ship-postal-code"));
							report.setShipCountry(getStrValue(info,titleList,"ship-country"));
							report.setShipPhoneNumber(getStrValue(info,titleList,"ship-phone-number"));
							report.setBillAddress1(getStrValue(info,titleList,"bill-address-1"));
							report.setBillAddress2(getStrValue(info,titleList,"bill-address-2"));
							report.setBillAddress3(getStrValue(info,titleList,"bill-address-3"));
							report.setBillCity(getStrValue(info,titleList,"bill-city"));
							report.setBillState(getStrValue(info,titleList,"bill-state"));
							report.setBillPostalCode(getStrValue(info,titleList,"bill-postal-code"));
							report.setBillCountry(getStrValue(info,titleList,"bill-country"));
							report.setItemPromotionDiscount(getBigDecimalValue(info,titleList,"item-promotion-discount"));
							report.setShipPromotionDiscount(getBigDecimalValue(info,titleList,"ship-promotion-discount"));
							report.setCarrier(getStrValue(info,titleList,"carrier"));
							report.setTrackingNumber(getStrValue(info,titleList,"tracking-number"));
							report.setEstimatedArrivalDate(GeneralUtil.getDatez(info[titleList.get("estimated-arrival-date")]));
							report.setFulfillmentCenterId(getStrValue(info,titleList,"fulfillment-center-id"));
							report.setFulfillmentChannel(getStrValue(info,titleList,"fulfillment-channel"));
							report.setSalesChannel(getStrValue(info,titleList,"sales-channel"));
							Marketplace marketPlace = marketplaceService.selectBySalesChannel(report.getSalesChannel());
							if(marketPlace==null ) {
								if(StrUtil.isNotEmpty(report.getCurrency())&&!"EUR".equals(report.getCurrency())){
									String marketplaceid = GeneralUtil.getMarketPlaceId(report.getCurrency());
									marketPlace = marketplaceService.selectByPKey(marketplaceid);
								} else if(StrUtil.isNotEmpty(report.getShipCountry())){
									marketPlace = marketplaceService.findMarketplaceByCountry(report.getShipCountry());
								}
								if(marketPlace==null){
									marketPlace = amazonAuthority.getMarketPlace();
								}
							}
							report.setPaymentsDate(GeneralUtil.getDatePlus(paymentdate,marketPlace.getMarket()));
							report.setReportingDate(GeneralUtil.getDatePlus(reportingdate,marketPlace.getMarket()));
							report.setPurchaseDate(GeneralUtil.getDatePlus(purchasedate,marketPlace.getMarket()));
							report.setShipmentDate(GeneralUtil.getDatePlus(shipdate,marketPlace.getMarket()));
							report.setAmazonauthid(amazonAuthority.getId());
							list.add(report);
						}
						if(list.size()>200) {
							ordersFulfilledShipmentsReportMapper.insertBatch(list);
							list.clear();
						}
						lineNumber++;
					}
					if(list.size()>0) {
						ordersFulfilledShipmentsReportMapper.insertBatch(list);
					}
					ordersFulfilledShipmentsReportMapper.moveBatch(amazonAuthority.getId());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mlog=mlog+e.getMessage();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mlog=mlog+e.getMessage();
				} finally {
						if(list!=null) {
							list.clear();
						}
						if(br!=null) {
						    try {
								br.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							    log.info(e.getMessage());
							}
						}
				 
			}
		  return mlog;

	}

	@Override
	public String myReportType() {
		// TODO Auto-generated method stub
		return ReportType.FulfilledShipmentsReport;
	}
	
	
}
