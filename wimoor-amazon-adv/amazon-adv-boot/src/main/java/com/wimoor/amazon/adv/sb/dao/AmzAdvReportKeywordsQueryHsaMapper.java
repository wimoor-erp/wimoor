package com.wimoor.amazon.adv.sb.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsQueryHsa;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportKeywordsQueryHsaMapper extends BaseMapper<AmzAdvReportKeywordsQueryHsa>{
 
	void insertBatch(List<AmzAdvReportKeywordsQueryHsa> list);
	
	List<Map<String,Object>> getKeywordsQueryHsa(Map<String, Object> param);

 
}