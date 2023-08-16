package com.wimoor.amazon.adv.sd.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeSD;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvProductTargeSDMapper  extends BaseMapper<AmzAdvProductTargeSD>{
	void insertBatch(List<AmzAdvProductTargeSD> list);

	PageList<Map<String, Object>> getProductTargeList(Map<String, Object> map, PageBounds pageBounds);
	
	
	public List<Map<String,Object>> getProductTargeChart(Map<String,Object> map);
	
	public Map<String,Object> getSumProductTarge(Map<String,Object> map);
}