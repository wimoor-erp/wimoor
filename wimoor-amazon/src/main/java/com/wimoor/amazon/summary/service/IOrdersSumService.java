package com.wimoor.amazon.summary.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public interface IOrdersSumService {
	public List<Map<String, String>> getSalesField(Map<String, Object> parameter);
	public List<Map<String, Object>> findordershopReport(Map<String, Object> parameter) ;
	public SXSSFWorkbook setProductSalasExcelBook(List<Map<String, Object>> list) ;
}
