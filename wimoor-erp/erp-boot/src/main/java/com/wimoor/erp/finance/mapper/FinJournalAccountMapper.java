package com.wimoor.erp.finance.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.finance.pojo.entity.FinJournalAccount;

@Mapper
public interface FinJournalAccountMapper extends BaseMapper<FinJournalAccount>{

	List<Map<String, Object>> findByCondition(Map<String, Object> param);
	Map<String, Object> findSumByCondition(Map<String, Object> param);
	List<Map<String, Object>> findDetialByCondition(@Param("param")Map<String, Object> param);
	List<Map<String, Object>> paymentSumByCondition(Map<String, Object> param);
	IPage<Map<String, Object>> findSumDayByCondition(Page<?> page ,@Param("param")Map<String, Object> param);
	List<Map<String, Object>> findSumDayByCondition(@Param("param")Map<String, Object> param);
	List<Map<String, Object>> paymentSum(String shopid);
	
	BigDecimal getPaySummary(String shopid);
	BigDecimal getFaSummary(String shopid);
	
	List<Map<String,Object>> findAllAcountByType(@Param("shopid")String shopid,@Param("acc")String acc,@Param("year")String year,@Param("month")String month);
	
	List<Map<String,Object>> findMonthCharts(@Param("shopid")String shopid,@Param("acc")String acc,@Param("year")String year);
	
	List<Map<String,Object>> findMonthDetailCharts(@Param("shopid")String shopid,@Param("acc")String acc,@Param("year")String year,@Param("month")String month);
}