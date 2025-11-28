package com.wimoor.amazon.adv.sd.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvProductadsSDMapper extends BaseMapper<AmzAdvProductadsSD>{
	
	void insertBatch(List<AmzAdvProductadsSD> list);
	List<Map<String,Object>> getProductAdChart(Map<String,Object> map);
	Map<String, Object> getSumProductAd(Map<String, Object> map);
	PageList<Map<String, Object>> getProductAdList(Map<String, Object> map, PageBounds pageBounds);
	Map<String, Object> getProductAdByRemind(Map<String, Object> param);
	Map<String, Object> getProductAdByRemindlast(Map<String, Object> param);
}