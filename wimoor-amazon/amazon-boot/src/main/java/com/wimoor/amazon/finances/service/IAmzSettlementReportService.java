package com.wimoor.amazon.finances.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementReport;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IAmzSettlementReportService extends IService<AmzSettlementReport> {

	HashMap<String, HashMap<String, Object>> getAmzSummaryMonth(Map<String, Object> param);

	HashMap<String, HashMap<String, Object>> getLacolSummaryMonth(Map<String, Object> param);

	Map<String, Object> getSummaryAll(Map<String, Object> param);

	Map<String, Object> getSummarySKU(Map<String, Object> param);

	Map<String, Object> getSumNumsByDay(Map<String, Object> param, String charttype);

	Map<String, Object> loadSummayMonthMaps(HashMap<String, HashMap<String, Object>> map, HashMap<String, HashMap<String, Object>> map2, List<String> times);

	List<String> initTimes(String endTime1, String startTime1);

	List<Map<String, Object>> selectOtherin(Map<String, Object> param);

	List<Map<String, Object>> selectOtherout(Map<String, Object> param);

	public SXSSFWorkbook downLoadSettlementRpt(Map<String, Object> parameter) throws BizException;

	Map<String, Object> getDetail(Map<String, Object> param);
	Map<String, Object> getDetailSettment(Map<String, Object> param);
	HashMap<String, Object> findSettlementSummary(String ekey, Map<String, Object> map);
	
	
	public void setParamsSettlementDate(Map<String, Object> map);

	List<Map<String,Object>> getSettlementGroupOverAll(Map<String, Object> map);
	
	public List<Map<String, Object>> downloadSettlementGroupOverAll(Map<String, Object> map);

	public void setExcelBookOverall(SXSSFWorkbook workbook, List<Map<String, Object>> list);
	public int downLoadSettlementRpt(Sheet sheet ,Map<String, Object> parameter,int line) throws BizException;
	void setExcelBook(SXSSFWorkbook workbook, Map<String, Object> map,UserInfo user);

 	public List<Map<String, Object>> findCommodity(Map<String, Object> map);
 	public List<Map<String,Object>> findCommodity(String ekey, Map<String, Object> map);
	List<Map<String,Object>> findSettlementSummarySku(Map<String, Object> param);
    IPage<Map<String,Object>> findSettlementSummarySku(Page<Object> page, Map<String, Object> param);

	public List<Map<String,Object>> monthDetail(Map<String,Object> param);

	public Map<String,Object> monthReport(Map<String, Object> param);
}
