package com.wimoor.amazon.adv.common.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.ProductAdvertReportSummary;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductAdvertReportSummaryMapper  extends BaseMapper<ProductAdvertReportSummary>{
	void insertBatch(List<ProductAdvertReportSummary> list);
	Integer refreshSDByDay(Map<String,Object> param);
	Integer refreshSPByDay(Map<String,Object> param);
	Integer refreshSummary(Map<String,Object> param);
	List<ProductAdvertReportSummary> findSummaryData(Map<String, Object> param);
}