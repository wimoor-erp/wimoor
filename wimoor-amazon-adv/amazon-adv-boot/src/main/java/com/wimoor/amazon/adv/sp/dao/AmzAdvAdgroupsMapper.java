package com.wimoor.amazon.adv.sp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvAdgroupsMapper extends BaseMapper<AmzAdvAdgroups>{

	void insertBatch(List<AmzAdvAdgroups> list);
	
	PageList<Map<String,Object>> getAdgroupList(Map<String,Object> map, PageBounds pageBounds);
	
	List<Map<String,Object>> getAdgroupChart(Map<String,Object> map);
	
	Map<String,Object> getAdgroupByRemind(Map<String,Object> map);
	
	Map<String,Object> getAdgroupByRemindlast(Map<String,Object> map);
	
	int getAdgroupCountByShopId(@Param("shopid")String shopid);
	
	Map<String, Object> getSumAdGroup(Map<String, Object> map);

	PageList<Map<String, Object>> getAllAdgroupList(Map<String, Object> map, PageBounds pageBounds);
	
}