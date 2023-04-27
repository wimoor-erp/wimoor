package com.wimoor.erp.finance.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.finance.pojo.entity.FinJournalAccount;
@Mapper
public interface FinJournalAccountMapper extends BaseMapper<FinJournalAccount>{

	List<Map<String, Object>> findByCondition(Map<String, Object> param);
	Map<String, Object> findSumByCondition(Map<String, Object> param);
	List<Map<String, Object>> findDetialByCondition(Map<String, Object> param);
	List<Map<String, Object>> paymentSumByCondition(Map<String, Object> param);
	List<Map<String, Object>> findSumDayByCondition(Map<String, Object> param);
	List<Map<String, Object>> paymentSum(String shopid);
	
	BigDecimal getPaySummary(String shopid);
	BigDecimal getFaSummary(String shopid);
	
	List<Map<String,Object>> findAllAcountByType(@Param("acct")String acct,@Param("year")String year,@Param("month")String month);
	
	List<Map<String,Object>> findMonthCharts(@Param("acct")String acct,@Param("year")String year);
	
	List<Map<String,Object>> findMonthDetailCharts(@Param("acct")String acct,@Param("year")String year,@Param("month")String month);
}