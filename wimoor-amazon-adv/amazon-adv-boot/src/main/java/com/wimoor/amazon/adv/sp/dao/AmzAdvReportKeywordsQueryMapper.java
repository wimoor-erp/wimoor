package com.wimoor.amazon.adv.sp.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsQuery;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsQueryAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsQueryAttributedSame;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportKeywordsQueryMapper extends BaseMapper<AmzAdvReportKeywordsQuery>{
	
	void insertBatch(List<AmzAdvReportKeywordsQuery> list);
	
	List<Map<String,Object>> getKeywordsQuery(Map<String, Object> param);

	void insertBatchAttributed(List<AmzAdvReportKeywordsQueryAttributed> listAttributed);

	void insertBatchAttributedSame(List<AmzAdvReportKeywordsQueryAttributedSame> listAttributedSame);
}