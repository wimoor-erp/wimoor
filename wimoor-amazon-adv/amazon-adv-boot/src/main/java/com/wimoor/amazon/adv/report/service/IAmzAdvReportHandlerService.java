package com.wimoor.amazon.adv.report.service;


import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.service.IAmzAdvHttpClientResponseHandler;

public interface IAmzAdvReportHandlerService extends IAmzAdvHttpClientResponseHandler {
 
	
	public void readReport();
	
	public void readReport(String reportid);
	
	public Map<String,Object> getNeedRequestReport(String profileid,String non_segment,String isnow);
	
	
	public void requestReport();

	public void requestProductAdsReport();

    void requestReportByDate(String authid, String start,String end);

	List<Map<String, Object>> listReport(String authid);
}
