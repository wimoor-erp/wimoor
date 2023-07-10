package com.wimoor.amazon.report.service.impl;

 
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.ReportsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.documents.DownloadHelper;
import com.amazon.spapi.model.reports.CreateReportResponse;
import com.amazon.spapi.model.reports.CreateReportSpecification;
import com.amazon.spapi.model.reports.Report;
import com.amazon.spapi.model.reports.ReportDocument;
import com.amazon.spapi.model.reports.ReportOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.wimoor.amazon.auth.mapper.AmazonGroupMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.report.pojo.entity.ReportRequestRecord;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.report.service.IReportRequestRecordService;
import com.wimoor.amazon.report.service.IReportRequestTypeService;
import com.wimoor.amazon.report.service.IReportService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;

@Service("reportCaptureService")
public abstract class ReportServiceImpl  implements IReportService {
	@Autowired
	IReportRequestRecordService iReportRequestRecordService;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Autowired
	IMarketplaceService marketplaceService;
	@Autowired
	AmazonGroupMapper amazonGroupMapper;
	@Autowired
	ApiBuildService apiBuildService;
	@Autowired
	IReportRequestTypeService iReportRequestTypeService;
	final DownloadHelper downloadHelper = new DownloadHelper.Builder().build();
	/**
	 * 获取子类中的具体报表类型
	 * 
	 * @return
	 */
	public abstract String myReportType();

	/**
	 * 获取报表的最后更新时间
	 * 
	 * @param amazonAuthority
	 * @param array
	 * @return
	 */
	Date getLastDate(AmazonAuthority amazonAuthority, List<String> array) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reporttype", this.myReportType());
		map.put("marketplacelist", array);
		map.put("sellerid", amazonAuthority.getSellerid());
		Date lastupdate =null;// reportRequestRecordMapper.lastUpdateRequestByType(map);
		map.clear();
		map=null;
		return lastupdate;
	}

	public void getReport(ReportRequestRecord record) {
		try {
			AmazonAuthority auth = amazonAuthorityService.selectBySellerId(record.getSellerid());
			auth.setMarketPlace(marketplaceService.selectByPKey(record.getMarketplaceid()));
			auth.setUseApi("getReport");
			ReportsApi api = apiBuildService.getReportsApi(auth);
			if(auth.apiNotRateLimit()) {
				ApiCallback<Report> response = new ApiCallbackGetReport(this,auth,record);
				api.getReportAsync(record.getReportid(), response);
			}
		} catch (Exception e) {
				// TODO Auto-generated catch block
			System.out.println(record.getSellerid());
			e.printStackTrace();
		}
	}
	
	public void refreshReport(ReportRequestRecord record) {
		if(record.getReportDocumentId()==null) {
				getReport(record);
		}
	}
	
	public boolean downloadProcessReport(ReportRequestRecord record) {
		if(record.getReportDocumentId()!=null) {
			getReportDocument(record);
			return true;
		}else {
			return false;
		}
		 
}
	
	
	public  void getReportDocument(ReportRequestRecord record) {
		AmazonAuthority amazonAuthority = amazonAuthorityService.selectBySellerId(record.getSellerid());
		ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
		amazonAuthority.setMarketPlace(marketplaceService.getById(record.getMarketplaceid())); 
		
		try { 
			  if(ReportType.getSingleReport().contains(record.getReporttype())) {
				    ReportDocument response = api.getReportDocument(record.getReportDocumentId());
					downloadReport(amazonAuthority,record,response);
			  }else {
				  ApiCallback<ReportDocument> callback=new ApiCallbackGetReportDocument(this,amazonAuthority,record);
				  api.getReportDocumentAsync(record.getReportDocumentId(), callback);
			  }
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 if(record!=null&&record.getTreatnumber()!=null) {
	    		  record.setTreatnumber(record.getTreatnumber()+2);
	    	 }else {
	    		 record.setTreatnumber(1);
	    	 }
			record.setReportProcessingStatus("DONE");
			record.setIsrun(false);
			record.setLog(e.getMessage());
			record.setLastupdate(new Date());
			iReportRequestRecordService.updateById(record);
			System.out.println(e.getMessage()); 
			e.printStackTrace();
		}
	}
    
	/**
	   * Download and optionally decompress the document retrieved from the given url.
	   *
	   * @param url                  the url pointing to a document
	   * @param compressionAlgorithm the compressionAlgorithm used for the document
	   * @throws IOException              when there is an error reading the response
	   * @throws IllegalArgumentException when the charset is missing
	   */
	  public String download(AmazonAuthority amazonAuthority,String url, String compressionAlgorithm,ReportRequestRecord record) throws IOException, IllegalArgumentException {
	    OkHttpClient httpclient = new OkHttpClient();
	    Request request = new Request.Builder().url(url).get().build();
	    httpclient.setConnectTimeout(30, TimeUnit.MINUTES);
	    httpclient.setReadTimeout(30, TimeUnit.MINUTES);
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
	    try (ResponseBody responseBody = response.body()) {
	      mlog="";
	      MediaType mediaType = MediaType.parse(response.header("Content-Type"));
	      Charset charset = mediaType.charset();
	      if (charset == null) {
	    	  charset=Charset.forName("utf-8");
	      }
	      Closeable closeThis = null;
	      try {
	        InputStream inputStream = responseBody.byteStream();
	        closeThis = inputStream;

	        if ("GZIP".equals(compressionAlgorithm)) {
	          inputStream = new GZIPInputStream(inputStream);
	          closeThis = inputStream;
	        }
	        // This example assumes that the download content has a charset in the content-type header, e.g.
	        // text/plain; charset=UTF-8
	            if ("text".equals(mediaType.type()) && "plain".equals(mediaType.subtype())) {
			          InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
			          closeThis = inputStreamReader;
			          BufferedReader reader = new BufferedReader(inputStreamReader);
			          record.setReportProcessingStatus("treat");
					  record.setIsrun(true);
					  iReportRequestRecordService.updateById(record);
			           mlog=treatResponse(amazonAuthority, reader);
		        } else {
		            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,charset);
			        closeThis = inputStreamReader;
			        BufferedReader reader = new BufferedReader(inputStreamReader);
		    		record.setReportProcessingStatus("treat");
					record.setIsrun(true);
					iReportRequestRecordService.updateById(record);
		            mlog=treatResponse(amazonAuthority, reader);
		        }
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
	        if (closeThis != null) {
	          closeThis.close();
	        }
	        if(responseBody!=null) {
	        	responseBody.close();
	        }
	      }
	    }
	    return mlog;
	  }
 
	  
	public  void downloadReport(AmazonAuthority amazonAuthority, ReportRequestRecord record,ReportDocument doc) {
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
	
	public abstract String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br);
	
    public ReportOptions getMyOptions() {
    	return null;
    }
	 
	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
		          amazonAuthority.setUseApi("createReport");
				  ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
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
    
	
	public ReportRequestRecord recordReportRequest(AmazonAuthority amazonAuthority, ReportRequestRecord record, ApiException arg0) {
		record.setLog(arg0.getMessage());
		iReportRequestRecordService.updateById(record);
		return record;
	}
	
    public ReportRequestRecord recordReportRequest(AmazonAuthority amazonAuthority,Report report) {
		// TODO Auto-generated method stub
    	if(report==null)return null;
    	String reportid=report.getReportId();
		if(StrUtil.isEmpty(reportid))return null;
		QueryWrapper<ReportRequestRecord> query=new QueryWrapper<ReportRequestRecord>();
		query.eq("sellerid", amazonAuthority.getSellerid());
		query.eq("reportId", reportid);
		query.eq("reportType", report.getReportType());
		List<ReportRequestRecord> list = iReportRequestRecordService.list(query);
		ReportRequestRecord report_requestinfo =null;
		if(list!=null&&list.size()>0) {
				report_requestinfo=list.get(0);
		}
		if(report_requestinfo!=null) {
			if(report_requestinfo.getReportProcessingStatus()!=null&&report_requestinfo.getReportProcessingStatus().equals("success")) {
				return report_requestinfo;
			}
			if(report.getProcessingStatus().getValue().equals("DONE")) {
				report_requestinfo.setTreatnumber(report_requestinfo.getTreatnumber()==null?1:report_requestinfo.getTreatnumber()+1);
			}else {
				report_requestinfo.setGetnumber(report_requestinfo.getGetnumber()==null?1:report_requestinfo.getGetnumber()+1);
			}
			report_requestinfo.setStartdate(AmzDateUtils.getUTCToDate(report.getDataStartTime()));
			report_requestinfo.setEndDate(AmzDateUtils.getUTCToDate(report.getDataEndTime()));
			report_requestinfo.setIsrun(false);
			if(report_requestinfo.getReportProcessingStatus()!=null
					&&(report_requestinfo.getReportProcessingStatus().equals("success")||
							report_requestinfo.getReportProcessingStatus().equals("treat")||
							report_requestinfo.getReportProcessingStatus().equals("error"))) {
				return report_requestinfo;
			}
			report_requestinfo.setReportProcessingStatus(report.getProcessingStatus().getValue());
			report_requestinfo.setReportDocumentId(report.getReportDocumentId());
			report_requestinfo.setLastupdate(new Date());
			iReportRequestRecordService.updateById(report_requestinfo);
		}else {
			report_requestinfo = new ReportRequestRecord();
			report_requestinfo.setMarketplaceid(report.getMarketplaceIds().get(0));
			report_requestinfo.setSellerid(amazonAuthority.getSellerid());
			report_requestinfo.setIsnewest(true);
			report_requestinfo.setReporttype(this.myReportType());
			report_requestinfo.setReportid(reportid);
			report_requestinfo.setTreatnumber(0);
			report_requestinfo.setIsrun(false);
			report_requestinfo.setGetnumber(1);
			report_requestinfo.setStartdate(AmzDateUtils.getUTCToDate(report.getDataStartTime()));
			report_requestinfo.setEndDate(AmzDateUtils.getUTCToDate(report.getDataEndTime()));
			report_requestinfo.setReportDocumentId(report.getReportDocumentId());
			report_requestinfo.setReportProcessingStatus(report.getProcessingStatus().getValue());
			if(report.getProcessingStatus().getValue().toUpperCase().equals("DONE")) {
				report_requestinfo.setTreatnumber(report_requestinfo.getTreatnumber()==null?1:report_requestinfo.getTreatnumber()+1);
			}else {
				report_requestinfo.setGetnumber(report_requestinfo.getGetnumber()==null?1:report_requestinfo.getGetnumber()+1);
			}
			report_requestinfo.setReportProcessingStatus(report.getProcessingStatus().getValue());
			report_requestinfo.setLastupdate(new Date());
			iReportRequestRecordService.save(report_requestinfo);
		}
		return report_requestinfo;
		
	}
    

	public ReportRequestRecord createRecordRequest(AmazonAuthority amazonAuthority,String reportid,Marketplace market,Date start,Date end) {
		// TODO Auto-generated method stub
		if(StrUtil.isEmpty(reportid))return null;
		ReportRequestRecord report_requestinfo = new ReportRequestRecord();
		report_requestinfo.setMarketplaceid(market.getMarketplaceid());
		report_requestinfo.setSellerid(amazonAuthority.getSellerid());
		report_requestinfo.setIsnewest(true);
		report_requestinfo.setReporttype(this.myReportType());
		report_requestinfo.setGetnumber(0);
		report_requestinfo.setTreatnumber(0);
		report_requestinfo.setReportid(reportid);
		report_requestinfo.setStartdate(start); 
		report_requestinfo.setIsrun(false);
		report_requestinfo.setEndDate(end);
		report_requestinfo.setReportProcessingStatus(null);
		report_requestinfo.setLog("");
		report_requestinfo.setLastupdate(new Date());
		iReportRequestRecordService.save(report_requestinfo);
		return report_requestinfo;
	}


		




}
