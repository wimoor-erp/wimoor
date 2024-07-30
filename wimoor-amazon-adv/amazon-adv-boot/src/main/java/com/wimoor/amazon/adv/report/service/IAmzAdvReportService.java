package com.wimoor.amazon.adv.report.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvReportService{
	
	public SXSSFWorkbook setExcelBook(Map<String,Object> map);

	Map<String, Object> getTypeNumber(UserInfo user);
	
	public Map<String, Object> getKeywordsWarningIndicator(Map<String,Object> map);
	
	public Map<String, Object> getProductAdsWarningIndicator(Map<String,Object> map);
	
	public List<Map<String, Object>> getTop5(BigInteger profileid,String ftype);
	
	public PageList<Map<String, Object>> getProductAdsWarningIndicator(Map<String,Object> map, PageBounds pagebounds);
	
	public PageList<Map<String, Object>> getKeywordsWarningIndicator(Map<String,Object> map, PageBounds pagebounds);

	public void refreshAmzadvertWarningData();
	
}
