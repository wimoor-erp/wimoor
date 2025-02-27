package com.wimoor.amazon.adv.sb.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sb.pojo.AmzAdvKeywordsNegativaHsa;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvKeywordsNegativaHsaMapper extends BaseMapper<AmzAdvKeywordsNegativaHsa>{
	int insertBatch(List<AmzAdvKeywordsNegativaHsa> list);
	List<Map<String, Object>> getNegativaKeywordsList(Map<String, Object> map);
}