package com.wimoor.amazon.adv.sb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdsHsa;
import com.wimoor.amazon.base.BaseMapper;
 
@Mapper
public interface AmzAdvAdsHsaMapper extends BaseMapper<AmzAdvAdsHsa>{
	void insertBatch(List<AmzAdvAdsHsa> list);
	PageList<Map<String, Object>> getAdsList(Map<String, Object> map, PageBounds pageBounds);
	Map<String, Object> getSumAds(Map<String, Object> map);
	List<Map<String,Object>> getAdsChart(Map<String,Object> map);
}