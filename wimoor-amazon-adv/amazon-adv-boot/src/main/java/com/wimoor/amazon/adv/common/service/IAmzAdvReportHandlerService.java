package com.wimoor.amazon.adv.common.service;


import java.util.Date;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;

public interface IAmzAdvReportHandlerService extends IAmzAdvHttpClientResponseHandler {
 
	public void requestReport();
	
	public void readReport(String shopid);
	
	public void requestReportByProfile(AmzAdvProfile profiles,AmzAdvAuth advauth);
	
	public void requestReportByProfile(AmzAdvProfile profiles, AmzAdvAuth advauth,int [] mydays);

	public void request7DaysReportByProfile(AmzAdvProfile item, AmzAdvAuth advauth);
	
	public Map<String,Object> getNeedRequestReport(String profileid,String non_segment,String isnow);
	
	public void reuqestReportByParam(AmzAdvProfile profile,String non_segment,String isnow,Date byday,Map<String,Object> map );
	
	public void requestReport(Date day,String shopid,boolean isold); 
}
