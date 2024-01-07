package com.wimoor.amazon.report.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.amazon.spapi.model.reports.GetReportsResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;

public interface IHandlerReportService {
	public void refreshProcessReportList(String reporttype) ;
	public int  hasNeedTreatOrderReportList(String sellerid);
	public int  hasNeedTreatPageViewReportList(String sellerid) ;
	public void refreshProcessReportById(String id);
	public void refreshGetReportList(String reporttype);
	public void refreshSingleGetReportList(String reporttype);
	public void requestAuthReport(String authfeild,String id,String reportType,Boolean ignore) ;
	public void requestReport(List<AmazonAuthority> amazonAuthorityList, String reportType, Date start, Date end,Boolean ignore) ;
	public void requestReport( String reportType,String start,String end, Boolean ignore) ;
	public void requestReport(String reportType, Boolean ignore);
	public void requestReport(String authid,String reportType,Calendar cstart,Calendar cend, Boolean ignore) ;
	GetReportsResponse listReportList(AmazonAuthority amazonAuthority, String reporttype, Calendar cstart, Calendar cend,
			String nextToken);
}
