package com.wimoor.amazon.report.service;

import java.io.BufferedReader;
import java.util.Calendar;
import java.util.Date;

import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.Report;
import com.amazon.spapi.model.reports.ReportDocument;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.report.pojo.entity.ReportRequestRecord;
 
/**
 * 此類主要用於請求報表和處理報表
 * @author Administrator
 *
 */
public interface IReportService {
	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend, Boolean ignore);
	public abstract String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br);
	public void refreshReport(ReportRequestRecord record);
	
	/**------------------------------------------------------
	 * 回调使用接口
	 * @param amazonAuthority
	 * @param market 
	 * @param record
	 * @param status
	 * @param log
	 */
	public ReportRequestRecord createRecordRequest(AmazonAuthority amazonAuthority,String reportid,Marketplace market, Date start,Date end);
	public ReportRequestRecord recordReportRequest(AmazonAuthority amazonAuthority,Report report);
	public ReportRequestRecord recordReportRequest(AmazonAuthority amazonAuthority, ReportRequestRecord record, ApiException arg0);
	
	public void downloadReport(AmazonAuthority amazonAuthority, ReportRequestRecord report,ReportDocument doc);
	public boolean downloadProcessReport(ReportRequestRecord record);
	public void getReportDocument(ReportRequestRecord report);

}
