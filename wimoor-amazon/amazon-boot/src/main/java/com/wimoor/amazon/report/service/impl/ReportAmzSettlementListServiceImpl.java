package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import com.amazon.spapi.api.ReportsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.GetReportsResponse;
import com.amazon.spapi.model.reports.ReportDocument;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.finances.mapper.AmzSettlementAccReportMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementAccStatementMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportSummaryDayMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportSummaryMonthMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementSummarySkuMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccReport;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementReport;
import com.wimoor.amazon.profit.service.IProfitCfgCountryService;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.report.pojo.entity.ReportRequestRecord;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.summary.service.IAmazonSettlementAnalysisService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.util.UUIDUtil;

import cn.hutool.core.lang.UUID;
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
			  OffsetDateTime createdSince=AmzDateUtils.getOffsetDateTimeUTC(cstart);
			  OffsetDateTime createdUntil=AmzDateUtils.getOffsetDateTimeUTC(cend);
			  String nextToken=null;
			  int pageSize=100;
			  List<String> marketplaceIds=new ArrayList<String>();
			  marketplaceIds.add(market.getMarketplaceid());
			  final ApiCallback<GetReportsResponse> callback=new ApiCallbackGetReports(this,amazonAuthority);
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
		try {
			ReportDocument response = api.getReportDocument(record.getReportDocumentId());
			this.downloadReport(amazonAuthority,record,response);
		} catch (Exception e) {
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
	     
	    try {
	    	   download(amazonAuthority,url, compressionAlgorithm,record);
	    } catch (IOException e) {
	      //Handle exception here.
	    	 e.printStackTrace();
	    } catch (IllegalArgumentException e) {
	      //Handle exception here.
	    	 e.printStackTrace();
	    } 
    	 
	     
	  
}
	
	LocalDateTime getLocalTime(String currency,String date){
		SimpleDateFormat sdf2 =null;
		if("USD".equals(currency)) {
			sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}else if("CAD".equals(currency)||"EUR".equals(currency)
				||"GBP".equals(currency)||"SEK".equals(currency)
				||"MXN".equals(currency)||"PLN".equals(currency)
				||"TRY".equals(currency)||"AUD".equals(currency)
				||"AED".equals(currency)||"BRL".equals(currency)
				||"SAR".equals(currency)||"AED".equals(currency)
				||"INR".equals(currency)||"SGD".equals(currency)) {
			sdf2=new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		}else if("JPY".equals(currency) ) {
			sdf2=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		}
		sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			return AmzDateUtils.getLocalTime(sdf2.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
	
	
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)  {
		String log = "settlementId:";
		int lineNumber = 0;
		String line;
		String marketname = null;
		String currency = null;
		String settlementId = null;
		Date beginDate = null;
		Date endDate = null;
		Boolean invalid=false;
		Set<String> marketnameset = new HashSet<String>();
		AmzSettlementAccReport amzSettlementAccReport = new AmzSettlementAccReport();
		Map<String, Object> param = new HashMap<String, Object>();
		AmzSettlementReport report = null;
		List<AmzSettlementReport> settlist=new LinkedList<AmzSettlementReport>();
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
						String startdate=GeneralUtil.getIndexString(info, 1);
						startdate=startdate.replace(" UTC", "");
						String enddate=GeneralUtil.getIndexString(info, 2);
						enddate=enddate.replace(" UTC", "");
						String depositdate=GeneralUtil.getIndexString(info, 3);
						depositdate=depositdate.replace(" UTC", "");
						amzSettlementAccReport.setSettlementStartDate(getLocalTime(currency,startdate));
						amzSettlementAccReport.setSettlementEndDate(getLocalTime(currency,enddate));
						amzSettlementAccReport.setDepositDate(getLocalTime(currency,depositdate));
						String currency2 = GeneralUtil.getIndexString(info, 5);
						amzSettlementAccReport.setCurrency(currency2);
						if ("EUR".equals(currency2)) {
							amzSettlementAccReport.setTotalAmount(GeneralUtil.getIndexEurBigDecimal(info, 4));
						} else {
							amzSettlementAccReport.setTotalAmount(GeneralUtil.getIndexBigDecimal(info, 4));
						}
						param.put("settlement_id", settlementId);
						param.put("amazonAuthId", amazonAuthority.getId());
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
						log += " error:order_id=" + report.getOrderId() + ",PostedDate is null.";
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
					if(report.getMerchantAdjustmentItemId()!=null&&report.getMerchantAdjustmentItemId().length()>15) {
						report.setMerchantAdjustmentItemId(report.getMerchantAdjustmentItemId().substring(0, 14));
					}
					report.setSku(GeneralUtil.getIndexString(info, 21));
					report.setQuantityPurchased(GeneralUtil.getInteger(GeneralUtil.getIndexString(info, 22)));
					report.setQuantityPurchased(GeneralUtil.getInteger(GeneralUtil.getIndexString(info, 22)));
					if(report.getOrderId()!=null&&report.getOrderId().length()>40) {
						report.setOrderId(report.getOrderId().substring(0,40));
					}
					if(report.getShipmentId()!=null&&report.getShipmentId().length()>15) {
						report.setShipmentId(report.getShipmentId().substring(0,15));
					}
					report.setPromotionId(GeneralUtil.getIndexString(info, 23));
					if(report.getAmountDescription()!=null&&report.getAmountDescription().contains("Transfer of funds unsuccessful")) {
						invalid=true;
					}
					if (report != null  ) {
						report.setId("0");
						settlist.add(report);
					}
					if(settlist.size()>1000) {
						amzSettlementReportMapper.insertBatch(settlist);
						settlist.clear();
					}
					report=null;
				}
				lineNumber++;
				
			
			}
		
        if(settlist.size()>1) {
        	amzSettlementReportMapper.insertBatch(settlist);
        }else if(settlist.size()==1) {
                AmzSettlementReport one = settlist.get(0);
                one.setId(UUIDUtil.getUUIDshort());
        	    amzSettlementReportMapper.insert(one);
        	 
        }
    	settlist.clear();
		// 验证账期总数是否一致
		BigDecimal total = amzSettlementReportMapper.getTotalAmountBySettementId(param);
		if(total==null){
			total = new BigDecimal("0");
		}
		if (amzSettlementAccReport.getTotalAmount() == null) {
			amzSettlementAccReport.setTotalAmount(new BigDecimal("0"));
		}
		if (amzSettlementAccReport.getTotalAmount().doubleValue() - total.doubleValue() > 1
				|| amzSettlementAccReport.getTotalAmount().doubleValue() - total.doubleValue() < -1) {
			return "settlementId:" + settlementId + ",error:账期总额不一致！";
		}
		if (StrUtil.isEmpty(marketname) && StrUtil.isNotEmpty(currency)) {
			// 此处考虑部分用户，没有在我们系统绑定对应国家，但是却有对应店铺，系统抓取的时候该国家的数据也会过来。
			// 而且有很多数据上面是没有marketplace point信息的。所以此处只能写成静态的根据币种转换。
			marketname=currencyToMarketplaceName(amazonAuthority,currency);
		}
		if (StrUtil.isEmpty(marketname)) {
			marketname = amazonAuthority.getMarketPlace().getPointName();
		}
		amzSettlementAccReport.setMarketplaceName(marketname);
		marketnameset.add(marketname);
		amzSettlementAccReport.setIsmoved(Boolean.FALSE);
		amzSettlementAccReport.setCapturetime(new Date());
		amzSettlementAccReport.setSumtime(null);
		amzSettlementAccReport.setInvalid(invalid);
		if(amzSettlementAccReport.getTotalAmount().equals(new BigDecimal("0.00"))) {
			amzSettlementAccReport.setInvalid(true);
		}
		if (amzSettlementAccReport != null && amzSettlementAccReport.getMarketplaceName() != null) {
			if (amzSettlementAccReportMapper.selectById(amzSettlementAccReport.getSettlementId()) != null) {
				amzSettlementAccReportMapper.updateById(amzSettlementAccReport);
			} else {
				amzSettlementAccReportMapper.insert(amzSettlementAccReport);
			}
		}
		// 对report表进行汇总
		// amazonSettlementAnalysisAgentService.confirm(param, marketnameset);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "settlementId:" + settlementId + ",error:"+e.getMessage();
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
	
	  public String download(AmazonAuthority amazonAuthority,String url, String compressionAlgorithm,ReportRequestRecord record) throws IOException, IllegalArgumentException {
		    OkHttpClient httpclient = new OkHttpClient();
		    Request request = new Request.Builder().url(url).get().build();
		    Response response = httpclient.newCall(request).execute();
		    String mlog="wait down Response";
		    if (!response.isSuccessful()) {
		    	mlog= String.format("Call to download content was unsuccessful with response code: %d and message: %s",response.code(), response.message());
				record.setReportProcessingStatus("DONE");
				record.setIsrun(false);
				record.setLog(mlog);
				iReportRequestRecordService.updateById(record);
				return mlog;
		    }
		    File tempfile = null;
		    Charset charset =null;
		    Closeable closeThis = null;
		    FileOutputStream fo =null;
		    try (ResponseBody responseBody = response.body()) {
		      mlog="";
		      MediaType mediaType = MediaType.parse(response.header("Content-Type"));
		        charset = mediaType.charset();
		       if (charset == null) {
		    	  charset=Charset.forName("utf-8");
		       }
		    
		        InputStream inputStream = responseBody.byteStream();
		        closeThis = inputStream;

		        if ("GZIP".equals(compressionAlgorithm)) {
		          inputStream = new GZIPInputStream(inputStream);
		          closeThis = inputStream;
		        }
				final String name="settlement_"+UUID.fastUUID();
				
				tempfile = File.createTempFile(name, ".bin");
			    fo = new FileOutputStream(tempfile);
				byte[] buffer=new byte[8192];
				int len=-1;
				while((len=inputStream.read(buffer))!=-1) {
 					fo.write(buffer,0,len);
				}
				fo.flush();
 		        if(responseBody!=null) {
 		        	responseBody.close();
 		        }
		    }catch(Exception e) {
		    	  e.printStackTrace();
		    }finally {
		    	  if (closeThis != null) {
	 		          closeThis.close();
	 		      }
		    	  if(fo!=null) {
		    		  fo.close();
		    	  }
		    }
		    
		        try {
		        // This example assumes that the download content has a charset in the content-type header, e.g.
		        // text/plain; charset=UTF-8
		        	FileInputStream fi = new FileInputStream(tempfile);
					InputStreamReader inputStreamReader = new InputStreamReader(fi, charset);
			        BufferedReader br = new BufferedReader(inputStreamReader);
			        record.setReportProcessingStatus("treat");
					record.setIsrun(true);
				    iReportRequestRecordService.updateById(record);
				    mlog=treatResponse(amazonAuthority, br);
				    br.close();
				    inputStreamReader.close();
				    tempfile.delete();
		      }catch(Exception e) {
		    	  if(StrUtil.isNotBlank(mlog)) {
		    		  mlog=mlog+e.getMessage();
		    	  }else {
		    		  mlog=e.getMessage();
		    	  }
		    	  throw e;
		      } finally {
		          
		    	  if(record!=null&&record.getTreatnumber()!=null) {
		    		  record.setTreatnumber(record.getTreatnumber()+1);
		    	  }
		    	  if(mlog==null||StrUtil.isEmpty(mlog)||(record.getReporttype().equals(ReportType.SettlementReport)&&!mlog.contains("error"))) {
						record.setReportProcessingStatus("success");
						record.setIsrun(false);
						record.setIsnewest(false);
						record.setLog(mlog);
						record.setLastupdate(new Date());
						iReportRequestRecordService.updateById(record);
					}else {
						record.setReportProcessingStatus("DONE");
						record.setIsrun(false);
						record.setLog(mlog);
						record.setLastupdate(new Date());
						iReportRequestRecordService.updateById(record);
					}
		     
		      }
		 
		    return mlog;
		  }
	 
 
	
	@Override
	public String myReportType() {
		return ReportType.SettlementReport;
	}
}
