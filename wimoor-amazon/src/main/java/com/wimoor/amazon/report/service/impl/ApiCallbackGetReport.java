package com.wimoor.amazon.report.service.impl;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.Report;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.report.pojo.entity.ReportRequestRecord;
import com.wimoor.amazon.report.service.IReportService;

public class ApiCallbackGetReport  implements ApiCallback<Report>{
	AmazonAuthority amazonAuthority=null;
	IReportService reportService=null;
	ReportRequestRecord record=null;
	ApiCallbackGetReport(IReportService rpt,AmazonAuthority auth,ReportRequestRecord record){
		 this.amazonAuthority=auth;
		 this.reportService=rpt;
		 this.record=record;
	}
	@Override
	public void onDownloadProgress(long arg0, long arg1, boolean arg2) {
		// TODO Auto-generated method stub
	}
 
	@Override
	public void onFailure(ApiException arg0, int arg1, Map<String, List<String>> arg2) {
		// TODO Auto-generated method stub
		amazonAuthority.setApiRateLimit(arg2, arg0);
        if(record!=null) {
        	reportService.recordReportRequest(amazonAuthority,record,arg0);
        }
	}

	@Override
	public void onSuccess(Report result, int arg1, Map<String, List<String>> arg2) {
		// TODO Auto-generated method stub
		amazonAuthority.setApiRateLimit(arg2, "");
		if(amazonAuthority!=null&&result!=null) {
			if(result!=null) {
			  record=reportService.recordReportRequest(amazonAuthority,result);
		  }
	   }
	}

	@Override
	public void onUploadProgress(long arg0, long arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}

}
