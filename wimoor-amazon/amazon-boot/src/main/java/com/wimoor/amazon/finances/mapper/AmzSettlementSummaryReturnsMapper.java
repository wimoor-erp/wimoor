package com.wimoor.amazon.finances.mapper;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementSummaryReturns;
 
@Mapper
public interface AmzSettlementSummaryReturnsMapper  extends BaseMapper<AmzSettlementSummaryReturns>{
	void refreshSummary(Map<String,Object> param);

	List<Map<String, Object>> selectTable(@Param("table")String table, RowBounds rowBounds);

	int insertTable(Map<String,Object> param);
}