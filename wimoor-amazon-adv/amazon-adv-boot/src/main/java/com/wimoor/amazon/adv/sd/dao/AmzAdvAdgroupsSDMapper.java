package com.wimoor.amazon.adv.sd.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvAdgroupsSDMapper  extends BaseMapper<AmzAdvAdgroupsSD>{
	void insertBatch(List<AmzAdvAdgroupsSD> list);

	PageList<Map<String, Object>> getAdgroupList(Map<String, Object> map, PageBounds pageBounds);

	Map<String, Object> getSumAdGroup(Map<String, Object> map);

	List<Map<String, Object>> getAdgroupChart(Map<String, Object> map);

	Map<String, Object> getAdgroupByRemind(Map<String, Object> param);

	Map<String, Object> getAdgroupByRemindlast(Map<String, Object> param);
}