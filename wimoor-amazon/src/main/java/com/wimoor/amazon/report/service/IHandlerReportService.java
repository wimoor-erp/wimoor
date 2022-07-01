package com.wimoor.amazon.report.service;

import java.util.Date;
import java.util.List;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;

public interface IHandlerReportService {
	public void requestReport(String reportType, Boolean ignore);
	public void requestReport(List<AmazonAuthority> amazonAuthorityList, String reportType, Date start, Date end,Boolean ignore) ;
	public void refreshProcessReportList(String reporttype) ;
	public int hasNeedTreatOrderReportList(String sellerid);

}
