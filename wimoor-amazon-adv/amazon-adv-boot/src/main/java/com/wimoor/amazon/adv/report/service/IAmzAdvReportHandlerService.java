package com.wimoor.amazon.adv.report.service;


import java.util.Map;

import com.wimoor.amazon.adv.common.service.IAmzAdvHttpClientResponseHandler;

public interface IAmzAdvReportHandlerService extends IAmzAdvHttpClientResponseHandler {
 
	
	public void readReport();
	
	public void readReport(String reportid);
	
	public Map<String,Object> getNeedRequestReport(String profileid,String non_segment,String isnow);
	
	
	public void requestReport(); 
}
