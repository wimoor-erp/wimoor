package com.wimoor.amazon.adv.task.service;

import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.task.pojo.SysTimeTask;

public interface ISysTimeTaskService extends IService<SysTimeTask>{
	public int saveTimeTask(SysTimeTask timetask);
	public void updateTimeTask(SysTimeTask record);
	public int deleteTimeTask(String taskId);
}
