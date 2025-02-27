package com.wimoor.amazon.adv.sb.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvKeywordsHsa;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvKeywordsHsaMapper extends BaseMapper<AmzAdvKeywordsHsa>{
	
	int insertBatch(List<AmzAdvKeywordsHsa> list);
	
	PageList<Map<String, Object>> getKeywordsList(Map<String, Object> map, PageBounds pageBounds);
	
	List<Map<String, Object>> getKeywordsList(Map<String, Object> map);
 
	List<Map<String,Object>> getKeywordsHSAChart(Map<String,Object> map);
 
	List<Map<String,Object>> getKeywordQueryHsaChart(Map<String,Object> map);
	
	Map<String,Object> getKeywordHSAByRemind(Map<String,Object> map);
	
	Map<String,Object> getKeywordHSAByRemindlast(Map<String,Object> map);
 
	Map<String,Object> getSumAdvKeywords(Map<String,Object> map);
	
	PageList<Map<String,Object>> getKeywordQueryList(Map<String,Object> map, PageBounds pageBounds);
	List<Map<String,Object>> getKeywordQueryChart(Map<String,Object> map);
}