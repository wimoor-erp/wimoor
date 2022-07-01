package com.wimoor.amazon.report.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.report.pojo.entity.AmzSettlementReportSummaryDay;

 

@Mapper
public interface AmzSettlementReportSummaryDayMapper extends BaseMapper<AmzSettlementReportSummaryDay>{

	 List<Map<String,Object>>  summaryday(Map<String,Object> param);
	 Map<String,Object>   summaryall(Map<String,Object> param);
	 Map<String,Object>   summaryReturnAll(Map<String,Object> param);
	 List<Map<String,Object>> summaryallp(Map<String,Object> param);
	 List<Map<String, Object>> sumNumsByDay(Map<String,Object> param);
	 List<Map<String, Object>> sumReturnNumsByDay(Map<String,Object> param);
 
	int refreshSummary(Map<String, Object> param);
	int insertBatch(List<AmzSettlementReportSummaryDay> daylist);
}