package com.wimoor.amazon.report.service.impl;

import com.amazon.spapi.model.reports.ReportOptions;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;


@Service("reportAmzInvoiceDataService")
public class ReportAmzInvoiceDataServiceImpl extends ReportServiceImpl {

	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)   {
		String log = "";
		int lineNumber = 0;
		String line;
		try {
			while ((line = br.readLine()) != null) {
				if(lineNumber==0){//放弃这个报表，存储负载大，但是没有明细的需求
					System.out.println(line);
				}
				lineNumber++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
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

	@Override
	public String myReportType() {
		return "GET_FLAT_FILE_VAT_INVOICE_DATA_REPORT";
	}

	@Override
	public ReportOptions getMyOptions() {
		ReportOptions options=new ReportOptions();
		options.put("ReportOption=pendingInvoices","true");
		return options;
	}
}
