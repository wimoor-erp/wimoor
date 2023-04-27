package com.wimoor.common.service;


import java.util.List;

import com.wimoor.common.pojo.entity.QuartzJobsVO;
import com.wimoor.common.pojo.entity.QuartzTask;


public interface SystemSchedulerService {
    /**
     * 添加
     *
     * @param name
     * @param group
     * @param cron
     * @return
     */
    boolean addScheduler(QuartzTask quartzTask);
 
    /**
     * 修改
     *
     * @param jobDetailName
     * @param jobDetailGroup
     * @param cron
     * @return
     */
    boolean updateScheduler(String jobDetailName, String jobDetailGroup, String cron);
 
    /**
     * 删除
     *
     * @param jobDetailName
     * @param jobDetailGroup
     * @return
     */
    boolean deleteScheduler(String jobDetailName, String jobDetailGroup);
 
    /**
     * 暂停
     *
     * @param jobDetailName
     * @param jobDetailGroup
     * @return
     */
    boolean puaseScheduler(String jobDetailName, String jobDetailGroup);
 
    /**
     * 恢复
     *
     * @param jobDetailName
     * @param jobDetailGroup
     * @return
     */
    boolean resumeScheduler(String jobDetailName, String jobDetailGroup);
    public boolean findScheduler(String jobDetailName, String jobDetailGroup) ;

	List<QuartzJobsVO> listScheduler();
	  
	public void refreshTask();
	public void deleteAllTask() ;
	public void insertTask();
}
 