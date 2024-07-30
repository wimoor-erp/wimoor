package com.wimoor.amazon.adv.sp.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvKeywordsMapper extends BaseMapper<AmzAdvKeywords>{
	
	int insertBatch(List<AmzAdvKeywords> list);
	 
	PageList<Map<String, Object>> getKeywordsList(Map<String, Object> map, PageBounds pageBounds);
	 
	PageList<Map<String, Object>> getKeywordsListForSP(Map<String, Object> map, PageBounds pageBounds);
	
	List<Map<String,Object>> getKeywordsChart(Map<String,Object> map);
	
	List<Map<String,Object>> getKeywordsHSAChart(Map<String,Object> map);
	
	PageList<Map<String,Object>> getKeywordQueryList(Map<String,Object> map, PageBounds pageBounds);
	
	List<Map<String,Object>> getKeywordQueryChart(Map<String,Object> map);
	
	List<Map<String,Object>> getKeywordQueryHsaChart(Map<String,Object> map);
	
	Map<String,Object> getKeywordByRemind(Map<String,Object> map);
	
	Map<String,Object> getKeywordByRemindlast(Map<String,Object> map);
	
	Map<String,Object> getKeywordHSAByRemind(Map<String,Object> map);
	
	Map<String,Object> getKeywordHSAByRemindlast(Map<String,Object> map);
	
	Map<String,Object> getSumAdvKeywords(Map<String,Object> map);
	
	Map<String,Object> getSumAdvKeywordsSB(Map<String,Object> map);
}