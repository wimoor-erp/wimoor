package com.wimoor.amazon.adv.task.config;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wimoor.amazon.adv.task.pojo.ScheduleJob;
import com.wimoor.amazon.adv.utils.TaskUtils;

@DisallowConcurrentExecution
public class QuartzJobFactoryDisallowConcurrentExecution implements Job {
 
 
  public void execute(JobExecutionContext context) throws JobExecutionException {
      ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
      TaskUtils.invokMethod(scheduleJob);
 
 
  }
}
