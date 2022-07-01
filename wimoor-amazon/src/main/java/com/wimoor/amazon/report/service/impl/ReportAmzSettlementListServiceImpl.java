package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import com.amazon.spapi.api.ReportsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.GetReportsResponse;
import com.amazon.spapi.model.reports.ReportDocument;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.profit.service.IProfitCfgCountryService;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.report.mapper.AmzSettlementAccReportMapper;
import com.wimoor.amazon.report.mapper.AmzSettlementAccStatementMapper;
import com.wimoor.amazon.report.mapper.AmzSettlementReportMapper;
import com.wimoor.amazon.report.mapper.AmzSettlementReportSummaryDayMapper;
import com.wimoor.amazon.report.mapper.AmzSettlementReportSummaryMonthMapper;
import com.wimoor.amazon.report.mapper.AmzSettlementSummarySkuMapper;
import com.wimoor.amazon.report.pojo.entity.AmzSettlementAccReport;
import com.wimoor.amazon.report.pojo.entity.AmzSettlementReport;
import com.wimoor.amazon.report.pojo.entity.ReportRequestRecord;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.report.summary.service.IAmazonSettlementAnalysisService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;

import cn.hutool.core.util.StrUtil;
 
@Service("reportAmzSettlementListService")
public class ReportAmzSettlementListServiceImpl extends ReportServiceImpl  {
	@Resource
	private AmzSettlementReportMapper amzSettlementReportMapper;
	@Resource
	private AmzSettlementAccReportMapper amzSettlementAccReportMapper;
	@Resource
	AmzSettlementReportSummaryDayMapper amzSettlementReportSummaryDayMapper;
	@Resource
	IAmazonAuthorityService amazonAuthorityService;
	@Resource
	private IMarketplaceService marketplaceService;
	@Resource
	AmzSettlementSummarySkuMapper amzSettlementSummarySkuMapper;
	@Resource
	AmzSettlementReportSummaryMonthMapper amzSettlementReportSummaryMonthMapper;
	@Resource
	IExchangeRateHandlerService exchangeRateHandlerService;
	@Resource
	IAmazonGroupService amazonGroupService;
	@Resource
	IProfitCfgService profitCfgService;
	@Resource
	IProfitCfgCountryService profitCfgCountryService;
	@Resource
	AmzSettlementAccStatementMapper amzSettlementAccStatementMapper;
    @Resource
    IAmazonSettlementAnalysisService amazonSettlementAnalysisAgentService;
    
	@Override
	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
		  ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
		  List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		  boolean doneEU = false;
		  for(Marketplace market:marketlist) {
			  if(market.getRegion().equals("EU")) {
				  if(doneEU==false) {
					  doneEU=true;
				  }else {
					  continue;
				  }
			  }
			  List<String> reportTypes=new LinkedList<String>();
			  reportTypes.add( myReportType());
			  List<String> processingStatuses=new LinkedList<String>();
			  processingStatuses.add("DONE");
			  OffsetDateTime createdSince=AmzDateUtils.getOffsetDateTime(cstart);
			  OffsetDateTime createdUntil=AmzDateUtils.getOffsetDateTime(cend);
			  String nextToken=null;
			  int pageSize=100;
			  List<String> marketplaceIds=new ArrayList<String>();
			  marketplaceIds.add(market.getMarketplaceid());
			  final ApiCallback<GetReportsResponse> callback=new ApiCallbackGetReports(this,amazonAuthority,false);
			  try {
				  api.getReportsAsync(reportTypes,processingStatuses , marketplaceIds,   pageSize, 
						              createdSince,  createdUntil,   nextToken, callback);
			   } catch (ApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			    }
		  }
	}
 
	public  void getReportDocument(ReportRequestRecord record) {
		AmazonAuthority amazonAuthority = amazonAuthorityService.selectBySellerId(record.getSellerid());
		ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
		amazonAuthority.setMarketPlace(marketplaceService.getById(record.getMarketplaceid()));
		// ApiCallback<GetReportDocumentResponse> response =new ApiCallbackGetReportDocument(this,amazonAuthority,record);
		try {
			ReportDocument response = api.getReportDocument(record.getReportDocumentId());
			this.downloadReport(amazonAuthority,record,response);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			record.setReportProcessingStatus("DONE");
			record.setIsrun(false);
			record.setLog(e.getMessage());
			iReportRequestRecordService.updateById(record);
			e.printStackTrace();
		}
	}
	
	public  void downloadReport(AmazonAuthority amazonAuthority, ReportRequestRecord record,ReportDocument doc) {
		record.setReportProcessingStatus("treat");
		record.setIsrun(true);
		iReportRequestRecordService.updateById(record);
		String url =doc.getUrl();
	    String compressionAlgorithm = doc.getCompressionAlgorithm()!=null?doc.getCompressionAlgorithm().getValue():null;
	    String mlog=null;
	    try {
	    	  mlog=download(amazonAuthority,url, compressionAlgorithm,record);
	    } catch (IOException e) {
	      //Handle exception here.
	    	mlog=e.getMessage();
	    } catch (IllegalArgumentException e) {
	      //Handle exception here.
	    	mlog=e.getMessage();
	    } finally {
	    	if(mlog.length()>0&&mlog.length()<=25) {
	    		 record.setReportProcessingStatus("success");
					record.setIsrun(false);
					record.setIsnewest(false);
					record.setLog(mlog);
					iReportRequestRecordService.updateById(record);
	    	 }else {
	    			record.setReportProcessingStatus("DONE");
					record.setIsrun(false);
					record.setLog(mlog);
					iReportRequestRecordService.updateById(record);
	    	 }
	    }
    	 
	     
	  
}
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)  {
		String log = "settlementId:";
		int lineNumber = 0;
		String line;
		String marketname = null;
		String currency = null;
		String settlementId = null;
		Date beginDate = null;
		Date endDate = null;
		Set<String> marketnameset = new HashSet<String>();
		AmzSettlementAccReport amzSettlementAccReport = new AmzSettlementAccReport();
		Map<String, Object> param = new HashMap<String, Object>();
		AmzSettlementReport report = null;
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				if (lineNumber >= 1) {
					if ("settlement-id".equals(info[0])) {
						continue;
					}
					if (info.length == 1) {
						continue;
					}
					report = new AmzSettlementReport();
					report.setAmazonAuthId(amazonAuthority.getId());
					report.setSettlementId(GeneralUtil.getIndexString(info, 0));
					report.setCurrency(GeneralUtil.getIndexString(info, 5));
					if (StrUtil.isNotEmpty(report.getCurrency())) {
						currency = report.getCurrency();
					} else {
						report.setCurrency(currency);
					}
					if (settlementId == null && report.getSettlementId() != null) {
						settlementId = report.getSettlementId();
						log += settlementId;
					}
					if (lineNumber == 1) {
						amzSettlementAccReport.setAmazonauthid(amazonAuthority.getId());
						amzSettlementAccReport.setSettlementId(GeneralUtil.getIndexString(info, 0));
						amzSettlementAccReport.setSettlementStartDate(GeneralUtil.getIndexDate(info, 1));
						amzSettlementAccReport.setSettlementEndDate(GeneralUtil.getIndexDate(info, 2));
						amzSettlementAccReport.setDepositDate(GeneralUtil.getIndexDate(info, 3));
						String currency2 = GeneralUtil.getIndexString(info, 5);
						amzSettlementAccReport.setCurrency(currency2);
						if ("EUR".equals(currency2)) {
							amzSettlementAccReport.setTotalAmount(GeneralUtil.getIndexEurBigDecimal(info, 4));
						} else {
							amzSettlementAccReport.setTotalAmount(GeneralUtil.getIndexBigDecimal(info, 4));
						}
						param.put("settlement_id", settlementId);
						param.put("amazonAuthId", amazonAuthority.getId());
						param.put("beginDate", GeneralUtil.formatDate(amzSettlementAccReport.getSettlementStartDate(), "yyyy-MM-dd"));
						param.put("endDate", GeneralUtil.formatDate(amzSettlementAccReport.getSettlementEndDate(), "yyyy-MM-dd"));
						amzSettlementReportMapper.deleteBatch(param);
						lineNumber++;
						continue;
					}
					report.setTransactionType(GeneralUtil.getIndexString(info, 6));
					report.setOrderId(GeneralUtil.getIndexString(info, 7));
					report.setMerchantOrderId(GeneralUtil.getIndexString(info, 8));
					String adjustmentid=GeneralUtil.getIndexString(info, 9);
					if(adjustmentid!=null&&adjustmentid.length()>=40) {
						adjustmentid=adjustmentid.substring(0, 39);
					}
					report.setAdjustmentId(adjustmentid);
					report.setShipmentId(GeneralUtil.getIndexString(info, 10));
					report.setMarketplaceName(GeneralUtil.getIndexString(info, 11));
					if (marketname == null && StrUtil.isNotEmpty(report.getMarketplaceName()) && report.getMarketplaceName().contains("Amazon.")) {
						marketname = report.getMarketplaceName();
					}
					if (StrUtil.isEmpty(report.getMarketplaceName()) && StrUtil.isNotEmpty(marketname)) {
						report.setMarketplaceName(marketname);
					}
					report.setAmountType(GeneralUtil.getIndexString(info, 12));
					String amountdesc = GeneralUtil.getIndexString(info, 13);
					if (amountdesc != null && amountdesc.length() > 100) {
						amountdesc = amountdesc.substring(0, 100);
					}
					report.setAmountDescription(amountdesc);
					if ("EUR".equals(currency)) {
						report.setAmount(GeneralUtil.getIndexEurBigDecimal(info, 14));
					} else {
						report.setAmount(GeneralUtil.getIndexBigDecimal(info, 14));
					}
					report.setFulfillmentId(GeneralUtil.getIndexString(info, 15));
					report.setPostedDate(GeneralUtil.getIndexDate(info, 16));
					if (report.getPostedDate() == null) {
						log += " order_id=" + report.getOrderId() + ",PostedDate is null.";
						lineNumber++;
						continue;
					}
					if (beginDate == null || report.getPostedDate() != null && beginDate.after(report.getPostedDate())) {
						beginDate = report.getPostedDate();
					}
					if (endDate == null || report.getPostedDate() != null && endDate.before(report.getPostedDate())) {
						endDate = report.getPostedDate();
					}
					report.setPostedDateTime(GeneralUtil.getIndexDate(info, 17));
					report.setOrderItemCode(GeneralUtil.getIndexString(info, 18));
					report.setMerchantOrderItemId(GeneralUtil.getIndexString(info, 19));
					report.setMerchantAdjustmentItemId(GeneralUtil.getIndexString(info, 20));
					report.setSku(GeneralUtil.getIndexString(info, 21));
					report.setQuantityPurchased(GeneralUtil.getInteger(GeneralUtil.getIndexString(info, 22)));
					report.setPromotionId(GeneralUtil.getIndexString(info, 23));
					if (report != null && report.getId() != null) {
						amzSettlementReportMapper.insert(report);
					}
					report=null;
				}
				lineNumber++;
			}
		

		// 验证账期总数是否一致
		param.put("beginDate", beginDate);
		param.put("endDate", endDate);
		BigDecimal total = amzSettlementReportMapper.getTotalAmountBySettementId(param);
		if(total==null){
			total = new BigDecimal("0");
		}
		if (amzSettlementAccReport.getTotalAmount() == null) {
			amzSettlementAccReport.setTotalAmount(new BigDecimal("0"));
		}
		if (amzSettlementAccReport.getTotalAmount().doubleValue() - total.doubleValue() > 1
				|| amzSettlementAccReport.getTotalAmount().doubleValue() - total.doubleValue() < -1) {
			return "settlementId:" + settlementId + ",账期总额不一致！";
		}
		if (StrUtil.isEmpty(marketname) && StrUtil.isNotEmpty(currency)) {
			// 此处考虑部分用户，没有在我们系统绑定对应国家，但是却有对应店铺，系统抓取的时候该国家的数据也会过来。
			// 而且有很多数据上面是没有marketplace point信息的。所以此处只能写成静态的根据币种转换。
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
		}
		if (StrUtil.isEmpty(marketname)) {
			marketname = amazonAuthority.getMarketPlace().getPointName();
		}
		amzSettlementAccReport.setMarketplaceName(marketname);
		marketnameset.add(marketname);
		if (amzSettlementAccReport != null && amzSettlementAccReport.getMarketplaceName() != null) {
			if (amzSettlementAccReportMapper.selectById(amzSettlementAccReport.getSettlementId()) != null) {
				amzSettlementAccReportMapper.updateById(amzSettlementAccReport);
			} else {
				amzSettlementAccReportMapper.insert(amzSettlementAccReport);
			}
		}
		// 对report表进行汇总
		 amazonSettlementAnalysisAgentService.confirm(param, marketnameset);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "settlementId:" + settlementId + ","+e.getMessage();
		} finally {
		      marketnameset.clear();
			  param.clear();
			  marketnameset=null;
			  param=null;
			  amzSettlementAccReport=null;
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		 
		}
		return log;
	}
	
	@Override
	public String myReportType() {
		return ReportType.SettlementReport;
	}
}
