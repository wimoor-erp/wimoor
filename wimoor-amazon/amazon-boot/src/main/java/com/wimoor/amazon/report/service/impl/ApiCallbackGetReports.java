package com.wimoor.amazon.report.service.impl;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.GetReportsResponse;
import com.amazon.spapi.model.reports.Report;
import com.amazon.spapi.model.reports.ReportList;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.report.service.IReportService;

public class ApiCallbackGetReports implements ApiCallback<GetReportsResponse> {
	AmazonAuthority amazonAuthority=null;
	IReportService reportService=null;
	ApiCallbackGetReports(IReportService rpt,AmazonAuthority auth){
		 this.amazonAuthority=auth;
		 this.reportService=rpt;
	}
	@Override
	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(GetReportsResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		if(amazonAuthority!=null&&result!=null) {
			   ReportList list = result.getReports();
			if(result!=null) {
				for(Report report:list) {
					  reportService.recordReportRequest(amazonAuthority,report);
				}
		  }
	   }
	}

	@Override
	public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
		// TODO Auto-generated method stub

	}

}
