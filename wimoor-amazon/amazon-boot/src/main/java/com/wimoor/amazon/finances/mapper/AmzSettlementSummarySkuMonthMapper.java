package com.wimoor.amazon.finances.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementSummarySkuMonth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-26
 */
@Mapper
public interface AmzSettlementSummarySkuMonthMapper extends BaseMapper<AmzSettlementSummarySkuMonth> {
	List<Map<String,Object>> findSku_AdvSpend(Map<String, Object> map);
	void deleteMonth(String amazonAuthId, String marketplaceName, String fromDate);
	void insertBatch(List<AmzSettlementSummarySkuMonth> marketcurrency);
	void insertBatchCNY(List<AmzSettlementSummarySkuMonth> cnycurrency);
	void insertBatchUSD(List<AmzSettlementSummarySkuMonth> usdcurrency);
	IPage<Map<String,Object>> findByCondition(Page<?> page,@Param("param") Map<String,Object> param);
	Map<String,Object> findSummaryByCondition(@Param("param") Map<String,Object> param);
	List<Map<String,Object>> findByCondition(@Param("param") Map<String,Object> param);
	BigDecimal summaryPrincipal(String amazonAuthId, String marketplaceName, String fromDate);

    List<Map<String, Object>> summaryQuarter(@Param("params") Map<String, Object> param);

	List<Map<String, Object>> getSummaryMonth(@Param("params") Map<String, Object> param);

	List<Map<String, Object>> getSummaryMonthStorage(@Param("params") Map<String, Object> param);
}
