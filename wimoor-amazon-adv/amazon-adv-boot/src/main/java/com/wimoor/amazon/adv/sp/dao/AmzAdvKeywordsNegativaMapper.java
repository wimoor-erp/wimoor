package com.wimoor.amazon.adv.sp.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvKeywordsNegativaMapper extends BaseMapper<AmzAdvKeywordsNegativa>{
	int insertBatch(List<AmzAdvKeywordsNegativa> list);
	List<Map<String, Object>> getNegativaKeywordsList(Map<String, Object> map);
}