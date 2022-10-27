package com.wimoor.amazon.report.service.impl;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.ReportDocument;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.report.pojo.entity.ReportRequestRecord;
import com.wimoor.amazon.report.service.IReportService;

public class ApiCallbackGetReportDocument  implements ApiCallback<ReportDocument>{
	AmazonAuthority amazonAuthority=null;
	IReportService reportService=null;
	ReportRequestRecord record=null;
	ApiCallbackGetReportDocument(IReportService rpt,AmazonAuthority auth,ReportRequestRecord report){
		 this.amazonAuthority=auth;
		 this.reportService=rpt;
		 this.record=report;
	}
	@Override
	public void onDownloadProgress(long arg0, long arg1, boolean arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onFailure(ApiException arg0, int arg1, Map<String, List<String>> arg2) {
		// TODO Auto-generated method stub
		reportService.recordReportRequest(amazonAuthority,record,arg0);
	}

	@Override
	public void onUploadProgress(long arg0, long arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSuccess(ReportDocument result, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		if(amazonAuthority!=null&&result!=null) {
			if(result!=null) {
				reportService.downloadReport(amazonAuthority,record,result);
			}
		}
	}

}
