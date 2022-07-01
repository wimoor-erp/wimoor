package com.wimoor.amazon.report.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.report.mapper.ReportRequestRecordMapper;
import com.wimoor.amazon.report.pojo.entity.ReportRequestRecord;
import com.wimoor.amazon.report.pojo.entity.ReportRequestType;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.report.service.IHandlerReportService;
import com.wimoor.amazon.report.service.IReportRequestTypeService;
import com.wimoor.amazon.report.service.IReportService;

import cn.hutool.extra.spring.SpringUtil;

 

@Service("reportHandlerService")
public class HandlerReportServiceImpl implements IHandlerReportService {
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
	
 
 
	public void refreshGetReportList(List<ReportRequestRecord> reportRequestRecordList ,Marketplace marketplace) {
		AmazonAuthority amazonAuthority = null;
		if (reportRequestRecordList == null || reportRequestRecordList.size() == 0) {
			return;
		}
		Map<String, ReportRequestRecord> map = new HashMap<String, ReportRequestRecord>();
		for (int i = 0; i < reportRequestRecordList.size(); i++) {
			ReportRequestRecord reportReuestRecord = reportRequestRecordList.get(i);
			amazonAuthority=amazonAuthorityService.selectBySellerId(reportReuestRecord.getSellerid());
			if (amazonAuthority.getRefreshToken() == null) {
				reportReuestRecord.setReportProcessingStatus("Error");
				reportReuestRecord.setIsnewest(false);
				reportReuestRecord.setLog(reportReuestRecord.getSellerid() + "：找不到MWS授权密钥");
				reportReuestRecord.setLastupdate(new Date());
				reportRequestRecordMapper.updateById(reportReuestRecord);
				continue;
			}
			 
		}
 
		for (Entry<String, ReportRequestRecord> entry : map.entrySet()) {
			ReportRequestRecord reportReuestRecord = entry.getValue();
			reportRequestRecordMapper.updateById(reportReuestRecord);
		}
        if(reportRequestRecordList!=null) {
        	reportRequestRecordList.clear();
        	reportRequestRecordList=null;
        }
        if(map!=null) {
        	map.clear();
        	map=null;
        }
		
	}
	

	// 获取状态为'SUBMITTED'和'IN_PROGRESS'的RecordList数量
	public int hasNeedProcessReportList(String reporttype) {
		List<ReportRequestRecord> reportRequestRecordList = reportRequestRecordMapper.selectNoProcessRequest(reporttype, null);
		return reportRequestRecordList.size();
	}

	// 在这里更新request实体类 的status..状态为'SUBMITTED'和'IN_PROGRESS'需要更新其最新状态..
	public void refreshProcessReportList(String reporttype) {
		Map<String, ArrayList<Marketplace>> map = marketplaceService.getRegionMapList();
		List<Runnable> runnables = new ArrayList<Runnable>();
		for (Entry<String, ArrayList<Marketplace>> entry : map.entrySet()) {
			if (entry.getValue() != null && entry.getValue().size() > 0) {
				runnables.add(processReportListRunnable(reporttype, entry.getValue()));
			}
		}
		amazonAuthorityService.executThread(runnables, 20);
		if(reporttype==null) {//settlement报表无法使用多线程同时抓取，只能一个一个的跑
		     processSettlementReportListRunnable(map);
		}
	}

	public Runnable processReportListRunnable(final String reporttype, final List<Marketplace> region) {
		Runnable runnable = new Runnable() {
			public void run() {
				for (int i = 0; i < region.size(); i++) {
					Marketplace marketplace = region.get(i);
					List<ReportRequestRecord> reportRequestRecordList = reportRequestRecordMapper.selectNoProcessRequest(reporttype, marketplace.getMarketplaceid());
					if(reportRequestRecordList!=null&&reportRequestRecordList.size()>0) {
						refreshProcessReportList( reportRequestRecordList, marketplace);
					}
				}
			}
		};
		return runnable;
	}
	public void processSettlementReportListRunnable(Map<String, ArrayList<Marketplace>> map) {
				for (Entry<String, ArrayList<Marketplace>> entry : map.entrySet()) {
					if (entry.getValue() != null && entry.getValue().size() > 0) {
						for (int i = 0; i < entry.getValue().size(); i++) {
							Marketplace marketplace = entry.getValue().get(i);
							List<ReportRequestRecord> reportRequestRecordList = reportRequestRecordMapper.selectNoProcessRequest(ReportType.SettlementReport, marketplace.getMarketplaceid());
							if(reportRequestRecordList!=null&&reportRequestRecordList.size()>0) {
								refreshProcessReportList(reportRequestRecordList, marketplace );
							}
						}
					}
				}
	}
	
    public void refreshProcessReportList(List<ReportRequestRecord> reportRequestRecordList, Marketplace marketplace) {
		Map<String, ReportRequestRecord> map = new HashMap<String, ReportRequestRecord>();
		if (reportRequestRecordList != null && reportRequestRecordList.size() > 0) {
			for (int i = 0; i < reportRequestRecordList.size(); i++) {
				ReportRequestRecord reportReuestRecord = reportRequestRecordList.get(i);
				ReportRequestType type = iReportRequestTypeService.findByTypeCode(reportReuestRecord.getReporttype());
				if(type==null)return;
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
		Calendar cend = Calendar.getInstance();
		for(AmazonAuthority auth:amazonAuthorityList) {
		   service.requestReport(auth,cstart,cend,ignore);
		}
		
	}
	
	public void requestReport(String reportType,Boolean ignore) {
	    LambdaQueryWrapper<AmazonAuthority> query =new LambdaQueryWrapper<AmazonAuthority>();
	    query.isNotNull(AmazonAuthority::getRefreshToken);
		List<AmazonAuthority> list = amazonAuthorityService.list(query);
		this.requestReport(list, reportType,ignore);
	}
	
	public void requestReport(List<AmazonAuthority> amazonAuthorityList, String reportType, Date start, Date end,Boolean ignore) {
		ReportRequestType type = iReportRequestTypeService.findByTypeCode(reportType);
		if(type==null)return;
		IReportService service=SpringUtil.getBean(type.getBean());
		Calendar cstart = Calendar.getInstance();
		cstart.setTime(start);
		Calendar cend = Calendar.getInstance();
		cend.setTime(end);
		for(AmazonAuthority auth:amazonAuthorityList) {
			service.requestReport(auth,cstart,cend,ignore);
		}
	}

	public int hasNeedTreatOrderReportList(String sellerid) {
		List<ReportRequestRecord> list = reportRequestRecordMapper.selectNeedTreatOrderReport(sellerid);
		if(list==null){
			return 0;
		}
		return list.size();
	}
 
}
