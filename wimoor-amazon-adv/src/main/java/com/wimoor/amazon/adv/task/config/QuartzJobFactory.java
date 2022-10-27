package com.wimoor.amazon.adv.task.config;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wimoor.amazon.adv.task.pojo.ScheduleJob;
import com.wimoor.amazon.adv.utils.TaskUtils;

public class QuartzJobFactory implements Job {
 
 
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					TaskUtils.invokMethod(scheduleJob);
				}
			}).start();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
 
