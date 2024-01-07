package com.wimoor.amazon.adv.sp.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvProductTargeNegativaMapper  extends BaseMapper<AmzAdvProductTargeNegativa>{
	
	int insertBatch(List<AmzAdvProductTargeNegativa> list);
	
	PageList<Map<String,Object>> getProductNegativaTargeList(Map<String,Object> map, PageBounds pageBounds);

}