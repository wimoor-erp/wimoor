package com.wimoor.amazon.adv.sp.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampProductTargeNegativa;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvCampProductTargeNegativaMapper  extends BaseMapper<AmzAdvCampProductTargeNegativa>{
	
	int insertBatch(List<AmzAdvCampProductTargeNegativa> list);
	
	PageList<Map<String,Object>> getProductNegativaTargeList(Map<String,Object> map, PageBounds pageBounds);

	PageList<Map<String, Object>> getAllProductNegativaTargeList(Map<String, Object> map, PageBounds pageBounds);
}