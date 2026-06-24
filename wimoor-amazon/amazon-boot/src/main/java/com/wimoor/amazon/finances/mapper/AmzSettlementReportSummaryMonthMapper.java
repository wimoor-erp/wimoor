package com.wimoor.amazon.finances.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementReportSummaryMonth;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
 

@Mapper
public interface AmzSettlementReportSummaryMonthMapper  extends BaseMapper<AmzSettlementReportSummaryMonth>{
	  List<Map<String,Object>> settlementitem(Map<String,Object> param);
	  List<Map<String,Object>> localitem(Map<String,Object> param);
	  List<Map<String,Object>> advfee(Map<String,Object> param);
	  List<Map<String,Object>> selectOtherin(Map<String,Object> param);
	  List<Map<String,Object>> selectOtherout(Map<String,Object> param);
	  int  refreshSummary(Map<String, Object> param);
	  int  insertBatch(List<AmzSettlementReportSummaryMonth> monthlist);
	  List<Map<String,Object>> monthDetail(Map<String,Object> param);
	  List<Map<String,Object>> monthReport(Map<String,Object> param);
	  BigDecimal TransfersToBankAccount(Map<String,Object> param);
	  BigDecimal FailedTransfersToBankAccount(Map<String,Object> param);
}