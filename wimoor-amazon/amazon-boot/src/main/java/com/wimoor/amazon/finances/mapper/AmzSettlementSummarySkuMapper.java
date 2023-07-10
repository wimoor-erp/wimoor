package com.wimoor.amazon.finances.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementSummarySku;

 
@Mapper
public interface AmzSettlementSummarySkuMapper extends BaseMapper<AmzSettlementSummarySku>{
 
	List<Map<String, Object>> findByCondition(Map<String, Object> map);
	List<Map<String, Object>> findFinItemDataByCondition(Map<String, Object> map);
	int refreshSummary(Map<String, Object> param);
	List<Map<String, Object>> findSettlementAmountTypeSummary(Map<String, Object> map);
	Map<String, Object> findSettlementSummary(Map<String, Object> map);
	List<Map<String, Object>> findSettlementSummarySku(Map<String, Object> map);
	IPage<Map<String, Object>> findSettlementSummarySku(Page<?> page,@Param("param")Map<String, Object> map);
	BigDecimal findSettlementSummarySku_AdvSpend(Map<String, Object> map);
	List<Map<String, Object>> findSettlementSummarySku_LongtermStorage(Map<String, Object> map);
	List<Map<String, Object>> findSettlementSummarySku_StorageFee(Map<String, Object> map);
	BigDecimal findSettlementSummarySku_FirstShipment(Map<String, Object> map);
	Map<String,Object>  findSettlementSummarySku_Returns(Map<String, Object> map);
	Map<String, Object> findSettlementSummarySku_FinItemData(Map<String, Object> map);
	List<Map<String, Object>> findSettlementOverall(Map<String, Object> map);
	Double summaryData(String settlementid);
}