package com.wimoor.amazon.summary.service;

import java.util.Date;
import java.util.Set;

import com.wimoor.amazon.orders.pojo.entity.OrdersReport;

public interface ISummaryOrderReportService {

	public void summaryOrderReport();
	public void dataAnalysis(Set<String> shopset) ;
	public void addOrder(OrdersReport oldone, OrdersReport newone) ;
	public void refreshAll(Date beginDate);
	public void refreshAll(String authid);
	public void summary(Set<String> shopset);
	public void saveMainReport(Set<String> mshopSet) ;
	public void dataMove();
	public void systemDataHandler();
	public void saveMainReturnReport(Set<String> shopset);
	public Set<String> getAvailableAmazonShop();
}
