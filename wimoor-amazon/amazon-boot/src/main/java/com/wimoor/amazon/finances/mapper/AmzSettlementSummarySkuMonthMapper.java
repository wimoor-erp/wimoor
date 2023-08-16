package com.wimoor.amazon.finances.mapper;

import com.wimoor.amazon.finances.pojo.entity.AmzSettlementSummarySkuMonth;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
}
