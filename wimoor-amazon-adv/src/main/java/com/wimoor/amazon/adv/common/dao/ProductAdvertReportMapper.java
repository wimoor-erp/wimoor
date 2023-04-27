package com.wimoor.amazon.adv.common.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.ProductAdvertReport;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductAdvertReportMapper extends BaseMapper<ProductAdvertReport> {

	ProductAdvertReport selectByOne(ProductAdvertReport advReport);
	List<Map<String,Object>> findAdvert(Map<String,Object> paramter);
	int insertBatch(List<ProductAdvertReport> list);
}