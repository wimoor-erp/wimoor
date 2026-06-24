package com.wimoor.amazon.finances.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccReport;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
 
@Mapper
public interface AmzSettlementReportMapper extends BaseMapper<AmzSettlementReport> {
 
	List<Map<String, Object>> settlement(Map<String, Object> param);

	Map<String, Object> settlement_sum(Map<String, Object> param);

	Map<String, Object> settlement_sump(Map<String, Object> param);

	int deleteBatch(Map<String, Object> param);

	int insertBatch(List<AmzSettlementReport> list);

	int insertReplaceBatch(List<AmzSettlementReport> list);
	
	List<Long> findSettlementID(@Param(value = "amazonAuthId") String id, @Param(value = "pointname") String pointName);

	List<AmzSettlementReport> findSettlementByPostDateAndOrder(@Param("orderid") String orderid,
			@Param("startDate") String startDate, @Param("endDate") String endDate);

	List<Map<String, Object>> findBySettlementAcc(Map<String, Object> parameter);

	BigDecimal getTotalAmountBySettementId(Map<String, Object> param);

	List<Map<String, Object>> getDetail(Map<String, Object> param);
	List<Map<String, Object>> getDetailDay(Map<String, Object> param);
	List<Map<String, Object>> getDetailDaySKU(Map<String, Object> param);
	List<Map<String, Object>> getDetailDaySKUOther(Map<String, Object> param);
	List<Map<String, Object>> getDetailSettment(Map<String, Object> param);
	List<Map<String, Object>> getDescNotSKU(Map<String, Object> param);

	void moveData(AmzSettlementAccReport acc);
    Integer hasData(AmzSettlementAccReport acc);
    Integer hasDataArchive(AmzSettlementAccReport acc);
    void moveDataArchive(AmzSettlementAccReport acc);

    IPage<Map<String,Object>> findSettlementSummarySku(Page<Object> page, @Param("params")Map<String, Object> param);
	List<Map<String,Object>> findSettlementSummarySku( @Param("params")Map<String, Object> param);
}