package com.wimoor.amazon.adv.common.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.ProductAdvertReportSummary;

public interface IAmazonReportAdvSummaryService extends IService<ProductAdvertReportSummary>{
	public List<Map<String, Object>> findAdvert(Map<String,Object> param) ;
	public void refreshSummary(String shopid);
	public void confirmSDByDay(String sellerid, String marketplaceid, Date startDate, Date endDate, String string);
	public void confirmSPByDay(String sellerid, String marketplaceid, Date startDate, Date endDate, String string);
}
