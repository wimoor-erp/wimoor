package com.wimoor.amazon.report.service.impl;

import com.amazon.spapi.SellingPartnerAPIAA.LWAException;
import com.amazon.spapi.api.ReportsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.GetReportsResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.finances.mapper.PaymentReportMapper;
import com.wimoor.amazon.finances.pojo.entity.PaymentReport;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service("reportAmzPaymentService")
public class ReportAmzPaymentServiceImpl extends ReportServiceImpl {
	@Resource
	private PaymentReportMapper paymentReportMapper;

	@Override
	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
		    ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
		    List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		    boolean doneEU = false;
		    List<String> marketplaceIds=new ArrayList<String>();
			for(Marketplace market:marketlist) {
				marketplaceIds.add(market.getMarketplaceid());
			}
			List<String> reportTypes=new LinkedList<String>();
			reportTypes.add(this.myReportType());
			List<String> processingStatuses=new LinkedList<String>();
			processingStatuses.add("DONE");
			processingStatuses.add("CANCELLED");
			processingStatuses.add("FATAL");
			processingStatuses.add("IN_PROGRESS");
			processingStatuses.add("IN_QUEUE");
			OffsetDateTime createdSince=AmzDateUtils.getOffsetDateTimeUTC(cstart);
			OffsetDateTime createdUntil=AmzDateUtils.getOffsetDateTimeUTC(cend);
			String nextToken=null;
			int pageSize=100;
			final ApiCallback<GetReportsResponse> callback=new ApiCallbackGetReports(this,amazonAuthority);
			try {
				api.getReportsAsync(reportTypes,processingStatuses,marketplaceIds,pageSize,createdSince,createdUntil,nextToken,callback);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LWAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


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

	private String getStrValue(String[] info, Map<String, Integer> titleList, String key) {
		Integer position = titleList.get(key);
		if(position==null)return null;
		if(position<info.length) {
			return info[position];
		}
		return null;
	}

	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)   {
		int lineNumber = 0;
		String line;
		String mlog="";
		Map<String,Integer> titleList = new HashMap<String,Integer>();
		List<PaymentReport> list=new ArrayList<PaymentReport>();
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				if (lineNumber == 0) {
					for (int i = 0; i < info.length; i++) {
						titleList.put(info[i],i);
					}
				}else {
					PaymentReport report = new PaymentReport();
					report.setReportType(getStrValue(info,titleList,"ReportType"));
					String datetime = getStrValue(info,titleList,"datetime");
					report.setDateTime(GeneralUtil.getDatez(datetime));
					report.setSettlementId(getStrValue(info,titleList,"settlement-id"));
					report.setTransactionType(getStrValue(info,titleList,"type"));
					report.setOrderId(getStrValue(info,titleList,"order-id"));
					report.setSku(getStrValue(info,titleList,"sku"));
					String quantity = getStrValue(info,titleList,"quantity");
					if(quantity!=null && isNumericzidai(quantity)) {
						report.setQuantity(Integer.parseInt(quantity));
					}
					report.setStore(getStrValue(info,titleList,"store"));
					report.setFulfillment(getStrValue(info,titleList,"fulfillment"));
					report.setOrderCity(getStrValue(info,titleList,"order-city"));
					report.setOrderState(getStrValue(info,titleList,"order-state"));
					report.setOrderPostCode(getStrValue(info,titleList,"order-post-code"));
					report.setTaxCollectionModel(getStrValue(info,titleList,"tax-collection-model"));
					report.setProductSales(getBigDecimalValue(info,titleList,"product-sales"));
					report.setProductSalesTax(getBigDecimalValue(info,titleList,"product-sales-tax"));
					report.setShippingCredits(getBigDecimalValue(info,titleList,"shipping-credits"));
					report.setShippingCreditsTax(getBigDecimalValue(info,titleList,"shipping-credits-tax"));
					report.setGiftWrapCredits(getBigDecimalValue(info,titleList,"gift-wrap-credits"));
					report.setGiftWrapCreditsTax(getBigDecimalValue(info,titleList,"gift-wrap-credits-tax"));
					report.setRegulatoryFee(getBigDecimalValue(info,titleList,"regulatory-fee"));
					report.setTaxOnRegulatoryFee(getBigDecimalValue(info,titleList,"tax-on-regulatory-fee"));
					report.setPromotionalRebate(getBigDecimalValue(info,titleList,"promotional-rebate"));
					report.setPromotionalRebateTax(getBigDecimalValue(info,titleList,"promotional-rebate-tax"));
					report.setMarketplaceWithheldTax(getBigDecimalValue(info,titleList,"marketplace-withheld-tax"));
					report.setSellingFees(getBigDecimalValue(info,titleList,"selling-fees"));
					report.setFbaFees(getBigDecimalValue(info,titleList,"fba-fees"));
					report.setOtherTransactionFees(getBigDecimalValue(info,titleList,"other-transaction-fees"));
					report.setOtherFees(getBigDecimalValue(info,titleList,"other"));
					report.setTotal(getBigDecimalValue(info,titleList,"total"));
					report.setTaxAmount(getBigDecimalValue(info,titleList,"Tax Amount"));
					report.setTotalAmount(getBigDecimalValue(info,titleList,"Total Amount"));
					report.setCreatetime(new Date());
					report.setOpttime(new Date());
					list.add(report);
				}
				if(list.size()>200) {
					paymentReportMapper.insertBatch(list);
					list.clear();
				}
				lineNumber++;
			}
			if(list.size()>0) {
				paymentReportMapper.insertBatch(list);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			mlog=mlog+e.getMessage();
		} catch (IOException e) {
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
					e.printStackTrace();
				}
			}
		}
		return mlog;
	}

	@Override
	public String myReportType() {
		return ReportType.FBAPaymentReport;
	}

}
