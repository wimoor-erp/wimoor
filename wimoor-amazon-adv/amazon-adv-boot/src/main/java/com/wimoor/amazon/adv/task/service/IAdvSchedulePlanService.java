package com.wimoor.amazon.adv.task.service;

import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.common.user.UserInfo;

public interface IAdvSchedulePlanService {
	public void updatePlanStatus(Map<String,Object> map);
	
	public void updatePlanRemark(Map<String,Object> map);
	
	public int insertPlan(Map<String, Object> map);
	
	public int updatePlan(Map<String, Object> map);
	
	public PageList<Map<String,Object>> getSchedulePlan(Map<String, Object> map, PageBounds pageBounds);
	
	public Object getSchedulePlanInfo(UserInfo user, String planid);
	
	public void deletePlan(String planId);
	
	public void deletePlanDateForInvalid();
}
