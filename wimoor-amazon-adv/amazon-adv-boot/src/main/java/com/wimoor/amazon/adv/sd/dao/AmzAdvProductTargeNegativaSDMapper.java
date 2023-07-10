package com.wimoor.amazon.adv.sd.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeNegativaSD;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvProductTargeNegativaSDMapper  extends BaseMapper<AmzAdvProductTargeNegativaSD>{
	void insertBatch(List<AmzAdvProductTargeNegativaSD> list);
	PageList<Map<String, Object>> getProductNegativaTargeList(Map<String, Object> map, PageBounds pageBounds);
}