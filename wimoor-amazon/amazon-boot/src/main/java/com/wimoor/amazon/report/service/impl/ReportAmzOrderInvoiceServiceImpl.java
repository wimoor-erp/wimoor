package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.amazon.spapi.api.ReportsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.CreateReportResponse;
import com.amazon.spapi.model.reports.CreateReportSpecification;
import com.amazon.spapi.model.reports.ReportDocument;
import com.amazon.spapi.model.reports.ReportOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.orders.mapper.AmzOrdersInvoiceReportMapper;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersDTO;
import com.wimoor.amazon.orders.pojo.entity.AmzOrdersInvoiceReport;
import com.wimoor.amazon.orders.pojo.entity.OrdersReport;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersVo;
import com.wimoor.amazon.report.pojo.entity.ReportRequestRecord;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.report.service.IReportAmzOrderInvoiceService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;

@Service("reportAmzOrderInvoiceService")
public class ReportAmzOrderInvoiceServiceImpl extends ReportServiceImpl implements IReportAmzOrderInvoiceService{

	@Resource
	AmzOrdersInvoiceReportMapper amzOrdersInvoiceReportMapper;
	
	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
          amazonAuthority.setUseApi("createReport");
		  ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
		  if(amazonAuthority.getAWSRegion()==null||!amazonAuthority.getAWSRegion().equals("eu-west-1")) {
			  return ;
		  }
		  List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		  for(Marketplace market:marketlist) {
			  CreateReportSpecification body=new CreateReportSpecification();
			  body.setReportType(myReportType());
			  body.setDataStartTime(AmzDateUtils.getOffsetDateTimeUTC(cstart));
			  body.setDataEndTime(AmzDateUtils.getOffsetDateTimeUTC(cend));
			  ReportOptions reportOptions=getMyOptions();
			  if(reportOptions!=null) {
				body.setReportOptions(reportOptions);  
			  }
			  List<String> list=new ArrayList<String>();
			  list.add(market.getMarketplaceid());
			  amazonAuthority.setMarketPlace(market);
			  if(ignore==null||ignore==false) {
				  Map<String,Object> param=new HashMap<String,Object>();
				  param.put("sellerid", amazonAuthority.getSellerid());
				  param.put("reporttype", this.myReportType());
				  param.put("marketplacelist", list);
				  Date lastupdate= iReportRequestRecordService.lastUpdateRequestByType(param);  
				  if(lastupdate!=null&&GeneralUtil.distanceOfHour(lastupdate, new Date())<6) {
					  continue;
				  }
			  }
			  body.setMarketplaceIds(list);
			  try {
					  ApiCallback<CreateReportResponse> callback = new ApiCallbackReportCreate(this,amazonAuthority,market,cstart.getTime(),cend.getTime());
				      api.createReportAsync(body,callback);
				  } catch (ApiException e) {
					  amazonAuthority.setApiRateLimit( null,e);
					  e.printStackTrace();
			    }
		  }
}

	public  void getReportDocument(ReportRequestRecord record) {
		AmazonAuthority amazonAuthority = amazonAuthorityService.selectBySellerId(record.getSellerid());
		amazonAuthority.setMarketPlace(marketplaceService.getById(record.getMarketplaceid()));
		// ApiCallback<GetReportDocumentResponse> response =new ApiCallbackGetReportDocument(this,amazonAuthority,record);
		try {
			String path="/reports/2021-06-30/documents/"+record.getReportDocumentId();
			JsonObject json = apiBuildService.getRestrictedData(amazonAuthority, path, null);
			Gson gson=new Gson();
			ReportDocument response = gson.fromJson(json, ReportDocument.class);
			this.downloadReport(amazonAuthority,record,response);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			record.setReportProcessingStatus("DONE");
			record.setIsrun(false);
			record.setLog(e.getMessage());
			iReportRequestRecordService.updateById(record);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
		
	@SuppressWarnings("unused")
	public  String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)  {
		int lineNumber = 0;
		OrdersReport record = null;
		String line;
        Date begin=null;
        Date end=null;
        String mlog="";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("amazonAuthId", amazonAuthority.getId());
		Map<String,Integer> titleList = new HashMap<String,Integer>();
		try {
			try {
				while ((line = br.readLine()) != null) {
					String[] info = line.split("\t");
					int length = info.length;
					if (lineNumber == 0) {
						for (int i = 0; i < length; i++) {
							titleList.put(info[i],i);
						}
					} else {
						AmzOrdersInvoiceReport one=new AmzOrdersInvoiceReport();
						String order_id= GeneralUtil.getStrValue(info, titleList, "order-id");
						String order_item_id= GeneralUtil.getStrValue(info, titleList, "order-item-id");
						String purchase_date= GeneralUtil.getStrValue(info, titleList, "purchase-date");
						String payments_date= GeneralUtil.getStrValue(info, titleList, "payments_date");
						String buyer_email= GeneralUtil.getStrValue(info, titleList, "buyer-email");
						String buyer_name= GeneralUtil.getStrValue(info, titleList, "buyer-name");
						String buyer_phone_number= GeneralUtil.getStrValue(info, titleList, "buyer-phone-number");
						String sku= GeneralUtil.getStrValue(info, titleList, "sku");
						String product_name= GeneralUtil.getStrValue(info, titleList, "product-name");
						String quantity_purchased= GeneralUtil.getStrValue(info, titleList, "quantity-purchased");
						String currency= GeneralUtil.getStrValue(info, titleList, "currency");
						BigDecimal item_price= GeneralUtil.getBigDecimalValue(info, titleList, "item-price");
						BigDecimal item_tax= GeneralUtil.getBigDecimalValue(info, titleList, "item-tax");
						BigDecimal shipping_price= GeneralUtil.getBigDecimalValue(info, titleList, "shipping-price");	
						BigDecimal shipping_tax= GeneralUtil.getBigDecimalValue(info, titleList, "shipping-tax");	
						String ship_service_level= GeneralUtil.getStrValue(info, titleList, "ship-service-level");		
						String recipient_name= GeneralUtil.getStrValue(info, titleList, "recipient_name");			
						String ship_address_1= GeneralUtil.getStrValue(info, titleList, "ship-address-1");			
						String ship_address_2= GeneralUtil.getStrValue(info, titleList, "ship-address-2");		
						String ship_address_3= GeneralUtil.getStrValue(info, titleList, "ship-address-3");
						String ship_city= GeneralUtil.getStrValue(info, titleList, "ship-city");
						String ship_state= GeneralUtil.getStrValue(info, titleList, "ship-state");							
						String ship_postal_code= GeneralUtil.getStrValue(info, titleList, "ship-postal-code");							
						String ship_country= GeneralUtil.getStrValue(info, titleList, "ship-country");			
						String ship_phone_number= GeneralUtil.getStrValue(info, titleList, "ship-phone-number");	
						String delivery_start_date= GeneralUtil.getStrValue(info, titleList, "delivery-start-date");	
						String delivery_end_date= GeneralUtil.getStrValue(info, titleList, "delivery-end-date");	
						String delivery_time_zone= GeneralUtil.getStrValue(info, titleList, "delivery-time-zone");
						String delivery_Instructions= GeneralUtil.getStrValue(info, titleList, "delivery-Instructions");
						Boolean is_business_order= GeneralUtil.getBooleanValue(info, titleList, "is-business-order");
						String price_designation= GeneralUtil.getStrValue(info, titleList, "price_designation");
						String sales_channel = GeneralUtil.getStrValue(info, titleList, "sales-channel");
						Boolean is_iba= GeneralUtil.getBooleanValue(info, titleList, "is-iba");
						one.setAmazonAuthId(new BigInteger(amazonAuthority.getId()));
						one.setRefreshtime(LocalDateTime.now());
						one.setOrderId(order_id);
						one.setOrderItemId(order_item_id);
						one.setPurchaseDate(GeneralUtil.getDatePlus(purchase_date,ship_country));
						one.setPaymentsDate(GeneralUtil.getDatePlus(payments_date,ship_country));
						one.setBuyerEmail(buyer_email);
						one.setBuyerName(buyer_name);
						one.setBuyerPhoneNumber(buyer_phone_number);
						one.setIsIba(is_iba);
						one.setCurrency(currency);
						one.setDeliveryEndDate(GeneralUtil.getDatePlus(delivery_end_date,ship_country));
						one.setDeliveryStartDate(GeneralUtil.getDatePlus(delivery_start_date,ship_country));
						one.setSku(sku);
						one.setIsBusinessOrder(is_business_order);
						one.setShipAddress1(ship_address_1);
						one.setShipAddress2(ship_address_2);
						one.setShipAddress3(ship_address_3);
						one.setQuantityPurchased(Integer.parseInt(quantity_purchased));
					    one.setItemPrice(item_price);
					    one.setShippingPrice(shipping_price);
					    one.setItemTax(item_tax);
					    one.setShippingTax(shipping_tax);
					    one.setShipCity(ship_city);
					    one.setShipCountry(ship_country);
					    one.setShipPhoneNumber(ship_phone_number);
					    one.setShipState(ship_state);
					    one.setShipPostalCode(ship_postal_code);
					    one.setShipServiceLevel(ship_service_level);
					    one.setRecipientName(recipient_name);
					    one.setPriceDesignation(price_designation);
					    one.setDeliveryInstructions(delivery_Instructions);
					    one.setSalesChannel(sales_channel);
					    one.setDeliveryTimeZone(delivery_time_zone);
						QueryWrapper<AmzOrdersInvoiceReport> query = new QueryWrapper<AmzOrdersInvoiceReport>();
						query.eq("order_id", order_id);
						query.eq("order_item_id", order_item_id);
						AmzOrdersInvoiceReport old = amzOrdersInvoiceReportMapper.selectOne(query);
						if(old!=null) {
							amzOrdersInvoiceReportMapper.update(one, query);
						}else {
							amzOrdersInvoiceReportMapper.insert(one);
						}
					} 
					lineNumber++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mlog+=e.getMessage();
			}
 
		} finally {
			titleList.clear();
			titleList=null;
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return mlog.toString();
	}


	@Override
	public String myReportType() {
		// TODO Auto-generated method stub
		return ReportType.OrdersInvoicingReport;
	}
	@Override
	public ReportOptions getMyOptions() {
		// TODO Auto-generated method stub
		ReportOptions reportOptions=new ReportOptions();
		reportOptions.put("ShowSalesChannel", "true");
		return reportOptions;
	}

	@Override
	public IPage<AmazonOrdersVo> selectOrderList(AmazonOrdersDTO condition) {
		// TODO Auto-generated method stub
		return amzOrdersInvoiceReportMapper.selectOrderList(condition.getPage(),condition);
	}
	
}
