package com.wimoor.amazon.adv.sp.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvProductTargeMapper extends BaseMapper<AmzAdvProductTarge>{
	int insertBatch(List<AmzAdvProductTarge> list);
	
	Map<String,Object> getProductTargeByRemind(Map<String,Object> map);
	
	Map<String,Object> getProductTargeByRemindlast(Map<String,Object> map);
	
	PageList<Map<String,Object>> getProductTargeList(Map<String,Object> map, PageBounds pageBounds);
	
	List<Map<String,Object>> getProductTargeChart(Map<String,Object> map);
	
	List<Map<String,Object>> getTargetCategories(Map<String,Object> map);
	
	List<Map<String,Object>> getTargetReport(Map<String, Object> param);
	
	Map<String, Object> getSumProductTarge(Map<String, Object> map);

}