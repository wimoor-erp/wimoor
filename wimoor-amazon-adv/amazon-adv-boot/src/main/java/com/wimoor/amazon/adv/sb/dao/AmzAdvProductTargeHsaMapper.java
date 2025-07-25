package com.wimoor.amazon.adv.sb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvProductTargeHsa;
import com.wimoor.amazon.base.BaseMapper;

@Mapper
public interface AmzAdvProductTargeHsaMapper extends BaseMapper<AmzAdvProductTargeHsa>{

	PageList<Map<String, Object>> getProductTargeList(Map<String, Object> map, PageBounds pageBounds);

	Map<String, Object> getSumProductTarge(Map<String, Object> map);

	List<Map<String, Object>> getProductTargeChart(Map<String, Object> map);
 

}