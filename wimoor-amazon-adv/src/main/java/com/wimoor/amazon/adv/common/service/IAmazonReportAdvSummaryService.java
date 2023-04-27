package com.wimoor.amazon.adv.common.service;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.ProductAdvertReportSummary;
import com.wimoor.amazon.adv.common.pojo.SummaryObject;


public interface IAmazonReportAdvSummaryService extends IService<ProductAdvertReportSummary>{
	void addAdvReportSummary(ProductAdvertReportSummary padv);
	public void confirm();
	public void  confirm(Map<String,Map<String ,SummaryObject>>  summap,String ctype);
    void init();
    public void refreshSummary(String shopid)  ;
	public void confirmSDByDay(String sellerid, String marketplaceid, String byday, String profileid);
	public void confirmSPByDay(String sellerid, String marketplaceid, String byday, String profileid);
	public List<Map<String, Object>> findAdvert(Map<String,Object> param) ;
}
