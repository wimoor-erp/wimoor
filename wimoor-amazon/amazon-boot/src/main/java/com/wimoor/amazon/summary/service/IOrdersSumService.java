package com.wimoor.amazon.summary.service;

import com.wimoor.amazon.orders.pojo.dto.AmazonOrderSummaryDTO;
import com.wimoor.amazon.orders.pojo.entity.OrdersSummary;
import com.wimoor.common.user.UserInfo;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;
import java.util.Map;

public interface IOrdersSumService {
	public List<Map<String, String>> getSalesField(Map<String, Object> parameter);
	public List<Map<String, Object>> findordershopReport(Map<String, Object> parameter) ;
	public SXSSFWorkbook setProductSalasExcelBook(List<Map<String, Object>> list,List<Map<String, String>> field) ;
	public  List<Map<String, Object>> getOrderChannel(String shopid);
	public Map<String, Object> searchOrderSummary(UserInfo userinfo, AmazonOrderSummaryDTO model);
	public List<OrdersSummary> orderSummaryBySkuDate(String sku, String marketplace, String beginDate, String endDate,
			String amazonAuthId, UserInfo user, String ftype);
    Map<String, Object> findorderSummaryTotal(String shopid,String startdate);
}
