package com.wimoor.amazon.adv.task.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.task.pojo.AmzAdvSchedulePlan;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvSchedulePlanMapper extends BaseMapper<AmzAdvSchedulePlan>{
	PageList<Map<String,Object>> getSchedulePlan(Map<String, Object> map, PageBounds pageBounds);
	
	List<Map<String, Object>> selectSubitem(String planid);
	
	List<Map<String, Object>> selectPlanData(String planid);
	
	List<Map<String, Object>> selectAllPlanDate();
}