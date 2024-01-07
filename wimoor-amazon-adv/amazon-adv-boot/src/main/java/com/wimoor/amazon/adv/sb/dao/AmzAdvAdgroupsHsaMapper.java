package com.wimoor.amazon.adv.sb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdgroupsHsa;
import com.wimoor.amazon.base.BaseMapper;
 
@Mapper
public interface AmzAdvAdgroupsHsaMapper extends BaseMapper<AmzAdvAdgroupsHsa>{
	void insertBatch(List<AmzAdvAdgroupsHsa> list);
	PageList<Map<String, Object>> getAdgroupList(Map<String, Object> map, PageBounds pageBounds);
	Map<String, Object> getSumAdGroup(Map<String, Object> map);
	List<Map<String,Object>> getAdgroupChart(Map<String,Object> map);
}