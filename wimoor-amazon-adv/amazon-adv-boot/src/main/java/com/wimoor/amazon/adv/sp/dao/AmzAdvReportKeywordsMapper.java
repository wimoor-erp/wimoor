package com.wimoor.amazon.adv.sp.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsAttributedSame;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportKeywordsMapper extends BaseMapper<AmzAdvReportKeywords>{

	void insertBatch(List<AmzAdvReportKeywords> list);
	
	List<Map<String,Object>> getKeywords(Map<String, Object> param);
	
	PageList<Map<String,Object>> getWarningIndicatorDetail(Map<String, Object> param, PageBounds pagebounds);
	
	Map<String,Object> getWarningIndicatorForYesterday(Map<String, Object> param);
	
	Map<String,Object> getWarningIndicatorForSequent(Map<String, Object> param);
	
	Map<String,Object> getWarningIndicatorForCo(Map<String, Object> param);
	
	List<Map<String,Object>> top5(BigInteger profileid);
	
	void refreshAdvertWarningData(Map<String, Object> param);
	
	void moveData(Map<String, Object> param);

	void moveData2(Map<String, Object> param);
	
	void insertBatchAttributed(List<AmzAdvReportKeywordsAttributed> listAttributed);

	void insertBatchAttributedSame(List<AmzAdvReportKeywordsAttributedSame> listAttributedSame);
}