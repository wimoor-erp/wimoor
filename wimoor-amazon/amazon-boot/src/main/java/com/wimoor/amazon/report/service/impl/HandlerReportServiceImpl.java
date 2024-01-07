package com.wimoor.amazon.report.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import com.alibaba.fastjson.JSONObject;
import com.amazon.spapi.api.ReportsApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.GetReportsResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.notifications.service.IAwsSQSMessageHandlerService;
import com.wimoor.amazon.report.mapper.ReportRequestRecordMapper;
import com.wimoor.amazon.report.pojo.entity.ReportRequestRecord;
import com.wimoor.amazon.report.pojo.entity.ReportRequestType;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.report.service.IHandlerReportService;
import com.wimoor.amazon.report.service.IReportRequestTypeService;
import com.wimoor.amazon.report.service.IReportService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;

import cn.hutool.extra.spring.SpringUtil;

 

@Service("reportHandlerService")
public class HandlerReportServiceImpl implements IHandlerReportService,IAwsSQSMessageHandlerService {
	@Resource
	ReportRequestRecordMapper reportRequestRecordMapper;
	@Resource
	IAmazonAuthorityService amazonAuthorityService;
	@Resource
	IMarketplaceService marketplaceService;
	@Autowired
	ApiBuildService apiBuildService;
	@Autowired
	IReportRequestTypeService iReportRequestTypeService;
	
	private IReportService getService(String requestType) {
		ReportRequestType type = iReportRequestTypeService.findByTypeCode(requestType);
		IReportService reportService=SpringUtil.getBean(type.getBean());
		return reportService;
	}

	public void refreshTreatReport(List<ReportRequestRecord> reportRequestRecordList, Marketplace marketplace) throws Exception {
		for (ReportRequestRecord reportRequestRecord : reportRequestRecordList) {
		     IReportService report = this.getService(reportRequestRecord.getReporttype());
		     report.refreshReport(reportRequestRecord);
		}
	}
	

	// 获取状态为'SUBMITTED'和'IN_PROGRESS'的RecordList数量
	public int hasNeedProcessReportList(String reporttype) {
		List<ReportRequestRecord> reportRequestRecordList = reportRequestRecordMapper.selectNeedProcessRequest(reporttype, null);
		return reportRequestRecordList.size();
	}

	// 在这里更新request实体类 的status..状态为'SUBMITTED'和'IN_PROGRESS'需要更新其最新状态..
	public void refreshGetReportList(String reporttype) {
		Map<String, ArrayList<Marketplace>> map = marketplaceService.getRegionMapList();
		List<Runnable> runnables = new ArrayList<Runnable>();
		for (Entry<String, ArrayList<Marketplace>> entry : map.entrySet()) {
			if (entry.getValue() != null && entry.getValue().size() > 0) {
				runnables.add(getReportListRunnable(reporttype, entry.getValue()));
			}
		}
		amazonAuthorityService.executThread(runnables,"refreshGetReportList");
	}
	public void refreshSingleGetReportList(String reporttype) {
		Map<String, ArrayList<Marketplace>> map = marketplaceService.getRegionMapList();
		for (Entry<String, ArrayList<Marketplace>> entry : map.entrySet()) {
			if (entry.getValue() != null && entry.getValue().size() > 0) {
				getReportList(reporttype, entry.getValue());
			}
		}
	}
 
	
	public void refreshProcessReportById(String id) {
		ReportRequestRecord reportReuestRecord = reportRequestRecordMapper.selectById(id);
		ReportRequestType type = iReportRequestTypeService.findByTypeCode(reportReuestRecord.getReporttype());
		IReportService service=SpringUtil.getBean(type.getBean());
		service.refreshReport(reportReuestRecord);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.downloadProcessReport(reportReuestRecord);
	}
	
	// 在这里更新request实体类 的status..状态为'SUBMITTED'和'IN_PROGRESS'需要更新其最新状态..
	public void refreshProcessReportList(String reporttype) {
		List<Runnable> runnables = new ArrayList<Runnable>();
		if(reporttype==null) {
				List<ReportRequestRecord> reportRequestRecordList = reportRequestRecordMapper.selectNeedProcessRequest(null,null);
                if(reportRequestRecordList!=null&&reportRequestRecordList.size()>0) {
                	runnables.addAll(processReportListRunnable(reportRequestRecordList));
                }
                if(reportRequestRecordList==null||reportRequestRecordList.size()<100){
                	List<ReportRequestRecord> reportSettRequestRecordList = reportRequestRecordMapper.selectNeedProcessRequest(ReportType.SettlementReport,null);
    	            if(reportSettRequestRecordList!=null&&reportSettRequestRecordList.size()>0) {
    	            	runnables.addAll(processReportListRunnable(reportSettRequestRecordList));
    	            }
                }
		}else {
			List<ReportRequestRecord> reportRequestRecordList = reportRequestRecordMapper.selectNeedProcessRequest(reporttype,null);
			runnables.addAll(processReportListRunnable(reportRequestRecordList));
		}
		 amazonAuthorityService.executThread(runnables,"refreshProcessReportList");

	}
	
	public List<Runnable> processReportListRunnable(List<ReportRequestRecord> reportRequestRecordList) {
		List<Runnable> runnables=new ArrayList<Runnable>();
		Set<String> set = ReportType.getSingleReport();
		for(ReportRequestRecord item:reportRequestRecordList) {
            if(!set.contains(item.getReporttype())) {
            	ReportRequestType type = iReportRequestTypeService.findByTypeCode(item.getReporttype());
				IReportService reportService=SpringUtil.getBean(type.getBean());
				Runnable runnable = new Runnable() {
					public void run() {
				              reportService.downloadProcessReport(item);
					}
				};
				runnables.add(runnable);
            }
		}
		
		for(String type:set) {
			Runnable runnable = new Runnable() {
				public void run() {
					for(ReportRequestRecord item:reportRequestRecordList) {
	                    if(item.getReporttype().equals(type)) {
	                    	ReportRequestType type = iReportRequestTypeService.findByTypeCode(item.getReporttype());
	    					IReportService reportService=SpringUtil.getBean(type.getBean());
	    					reportService.downloadProcessReport(item);
	                    }
					}
				}
			};
			runnables.add(runnable);
		}
	  return runnables;
	}
	
	public Runnable getReportListRunnable(final String reporttype, final List<Marketplace> region) {
		Runnable runnable = new Runnable() {
			public void run() {
				for (int i = 0; i < region.size(); i++) {
					Marketplace marketplace = region.get(i);
					List<ReportRequestRecord> reportRequestRecordList = reportRequestRecordMapper.selectNeedRefreshRequest(reporttype, marketplace.getMarketplaceid());
					if(reportRequestRecordList!=null&&reportRequestRecordList.size()>0) {
						refreshGetReportList( reportRequestRecordList, marketplace);
					}
				}
			}
		};
		return runnable;
	}
 
	public void getReportList(final String reporttype, final List<Marketplace> region) {
				for (int i = 0; i < region.size(); i++) {
					Marketplace marketplace = region.get(i);
					List<ReportRequestRecord> reportRequestRecordList = reportRequestRecordMapper.selectNeedRefreshRequest(reporttype, marketplace.getMarketplaceid());
					if(reportRequestRecordList!=null&&reportRequestRecordList.size()>0) {
						refreshGetReportList( reportRequestRecordList, marketplace);
					}
				}
	}
	
    public void refreshGetReportList(List<ReportRequestRecord> reportRequestRecordList, Marketplace marketplace) {
		Map<String, ReportRequestRecord> map = new HashMap<String, ReportRequestRecord>();
		if (reportRequestRecordList != null && reportRequestRecordList.size() > 0) {
			for (int i = 0; i < reportRequestRecordList.size(); i++) {
				ReportRequestRecord reportReuestRecord = reportRequestRecordList.get(i);
				ReportRequestType type = iReportRequestTypeService.findByTypeCode(reportReuestRecord.getReporttype());
				if(type==null)return;
				if(reportReuestRecord.getReportDocumentId()!=null) {
					continue;
				}
				IReportService service=SpringUtil.getBean(type.getBean());
				service.refreshReport(reportReuestRecord);
			}
		}
	 
		if(map!=null) {
			map.clear();
			map=null;
		}
		if(reportRequestRecordList!=null) {
			reportRequestRecordList.clear();
			reportRequestRecordList=null;
		}
    }

	public void requestReport(List<AmazonAuthority> amazonAuthorityList, String reportType, Boolean ignore) {
		ReportRequestType type = iReportRequestTypeService.findByTypeCode(reportType);
		if(type==null)return;
		IReportService service=SpringUtil.getBean(type.getBean());
		Calendar cstart = Calendar.getInstance();
		cstart.add(Calendar.DATE, type.getDay()!=null?type.getDay()*-1:0);
		cstart.add(Calendar.HOUR, -2);
		Calendar cend = Calendar.getInstance();
		requestReport(amazonAuthorityList,service,cstart,cend,ignore);
		//Calendar mycstart = Calendar.getInstance();
		//requestReport(amazonAuthorityList,service,mycstart,cend,ignore);
	}
	
	public void requestReport(String reportType,String start,String end, Boolean ignore) {
		ReportRequestType type = iReportRequestTypeService.findByTypeCode(reportType);
		if(type==null)return;
		IReportService service=SpringUtil.getBean(type.getBean());
		List<AmazonAuthority> list =amazonAuthorityService.getAllAuth();
		Calendar cstart = Calendar.getInstance();
		cstart.setTime(GeneralUtil.getDatez(start));
		Calendar cend = Calendar.getInstance();
		cend.setTime(GeneralUtil.getDatez(end));
		requestReport(list,service,cstart,cend,ignore);
	}
	
	public void requestReport(String authid,String reportType,Calendar cstart,Calendar cend, Boolean ignore) {
		ReportRequestType type = iReportRequestTypeService.findByTypeCode(reportType);
		if(type==null)return;
		IReportService service=SpringUtil.getBean(type.getBean());
		QueryWrapper<AmazonAuthority> queryWrapper=new QueryWrapper<AmazonAuthority>();
		queryWrapper.eq("id", authid);
		List<AmazonAuthority> list =amazonAuthorityService.list(queryWrapper);
		requestReport(list,service,cstart,cend,ignore);
	}
	
	public void requestReport(String reportType,Boolean ignore) {
		List<AmazonAuthority> list =amazonAuthorityService.getAllAuth();
		this.requestReport(list, reportType,ignore);
	}
	
	public void requestAuthReport(String authfeild,String id,String reportType,Boolean ignore) {
		QueryWrapper<AmazonAuthority> queryWrapper=new QueryWrapper<AmazonAuthority>();
		queryWrapper.eq(authfeild, id);
		List<AmazonAuthority> list =amazonAuthorityService.list(queryWrapper);
		this.requestReport(list, reportType,ignore);
	}
	
	public void requestReport(List<AmazonAuthority> amazonAuthorityList,IReportService service,Calendar cstart,Calendar cend,Boolean ignore) {
		List<Runnable> runnables = new ArrayList<Runnable>();
		for(AmazonAuthority auth:amazonAuthorityList) {
			Runnable runnable = new Runnable() {
				public void run() {
						try {
							auth.setUseApi("createReport");
							if(auth.apiNotRateLimit()) {
								service.requestReport(auth,cstart,cend,ignore);
							}
						}catch(Exception e) {
							 e.printStackTrace();
						}
				}
			};
			runnables.add(runnable);
		}
		 amazonAuthorityService.executThread(runnables,"requestReport");
	}
	
	public void requestReport(List<AmazonAuthority> amazonAuthorityList, String reportType, Date start, Date end,Boolean ignore) {
		ReportRequestType type = iReportRequestTypeService.findByTypeCode(reportType);
		if(type==null)return;
		IReportService service=SpringUtil.getBean(type.getBean());
		Calendar cstart = Calendar.getInstance();
		cstart.setTime(start);
		Calendar cend = Calendar.getInstance();
		cend.setTime(end);
		requestReport(amazonAuthorityList,service,cstart,cend,ignore);
	}

	public int hasNeedTreatOrderReportList(String sellerid) {
		List<ReportRequestRecord> list = reportRequestRecordMapper.selectNeedTreatOrderReport(sellerid);
		if(list==null){
			return 0;
		}
		return list.size();
	}
	
	public int hasNeedTreatPageViewReportList(String sellerid) {
		List<ReportRequestRecord> list = reportRequestRecordMapper.selectNeedTreatPageViewReport(sellerid);
		if(list==null){
			return 0;
		}
		return list.size();
	}
	
	@Override
	public boolean handlerMessage(JSONObject body) {
		// TODO Auto-generated method stub
		JSONObject reportProcessingFinishedNotification = body.getJSONObject("reportProcessingFinishedNotification");
		if(reportProcessingFinishedNotification!=null) {
			String sellerId=reportProcessingFinishedNotification.getString("sellerId");
			String reportId=reportProcessingFinishedNotification.getString("reportId");
			String reportType=reportProcessingFinishedNotification.getString("reportType");
			String processingStatus=reportProcessingFinishedNotification.getString("processingStatus");
			String reportDocumentId=reportProcessingFinishedNotification.getString("reportDocumentId");
			QueryWrapper<ReportRequestRecord> query=new QueryWrapper<ReportRequestRecord>();
			query.eq("sellerid", sellerId);
			query.eq("reportType", reportType);
			query.eq("reportId", reportId);
			ReportRequestRecord record=reportRequestRecordMapper.selectOne(query);
			if(record!=null) {
				if(record.getReportProcessingStatus()==null||(!record.getReportProcessingStatus().equals("success")&&!record.getReportProcessingStatus().equals("treat"))) {
					record.setReportProcessingStatus(processingStatus);
					record.setReportDocumentId(reportDocumentId);
					reportRequestRecordMapper.update(record, query);
				}
			}
		}
		return true;
	}

	@Override
	public GetReportsResponse listReportList(AmazonAuthority amazonAuthority ,String reporttype,Calendar cstart,Calendar cend,String nextToken) {
		// TODO Auto-generated method stub
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
			  reportTypes.add(reporttype);
			  List<String> processingStatuses=new LinkedList<String>();
			  processingStatuses.add("DONE");
			  OffsetDateTime createdSince=AmzDateUtils.getOffsetDateTimeUTC(cstart);
			  OffsetDateTime createdUntil=AmzDateUtils.getOffsetDateTimeUTC(cend);
			  int pageSize=100;
			  List<String> marketplaceIds=new ArrayList<String>();
			  marketplaceIds.add(market.getMarketplaceid());
			  
			  try {
				return   api.getReports(reportTypes,processingStatuses , marketplaceIds,   pageSize, 
						              createdSince,  createdUntil,   nextToken);
			    } catch (ApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			    }
		  }
		return null;
	}
 
}
