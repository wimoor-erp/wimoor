package com.wimoor.amazon.adv.task.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.task.config.InitQuartzJob;
import com.wimoor.amazon.adv.task.dao.SysTimeTaskMapper;
import com.wimoor.amazon.adv.task.pojo.SysTimeTask;
import com.wimoor.amazon.adv.task.service.ISysTimeTaskService;
import com.wimoor.amazon.base.BaseService;

import lombok.extern.slf4j.Slf4j;

 

@Slf4j
@Service("sysTimeTaskService")
public class SysTimeTaskServiceImpl extends BaseService<SysTimeTask> implements ISysTimeTaskService {
 
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	@Resource
	SysTimeTaskMapper sysTimeTaskMapper;
	@Resource
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	public int saveTimeTask(SysTimeTask record) {
		// TODO Auto-generated method stub
		try {
			InitQuartzJob.addJob(record);
			int temp = sysTimeTaskMapper.insert(record);
			if (temp < 1) {
				throw new BaseException("请求成功但未发生任何更改!");
			}
			return temp;
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BaseException("创建失败!"+e.getMessage());
		}
	}

	public void updateTimeTask(final SysTimeTask systask) {
		// TODO Auto-generated method stub
		threadPoolTaskExecutor.execute(new Thread(systask.getId()){
			public void run() {
				Scheduler scheduler = schedulerFactoryBean.getScheduler();
				JobKey jobKey = JobKey.jobKey(systask.getName(), systask.getGroupName());
				try {
					scheduler.deleteJob(jobKey);
					InitQuartzJob.addJob(systask);
					int temp = sysTimeTaskMapper.updateByPrimaryKey(systask);
					if (temp < 1) {
						throw new BaseException("任务taskID:" + systask.getId() + "修改失败!");
					}
				} catch (SchedulerException e) {
					e.printStackTrace();
					throw new BaseException("修改失败!"+e.getMessage());
				}
			}
		});
	}

	public int deleteTimeTask(String taskId) {
		SysTimeTask record = sysTimeTaskMapper.selectByPrimaryKey(taskId);
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(record.getName(), record.getGroupName());
		try {
			scheduler.deleteJob(jobKey);
		} catch (SchedulerException e1) {
			e1.printStackTrace();
			throw new BaseException("taskId:" + taskId + "系统异常导致任务计划仍在执行,请联系管理员!");
		}
		return sysTimeTaskMapper.delete(record);
	}

	public static void checkparam(String cron, String jobData) {
		if (StringUtils.isEmpty(cron) || StringUtils.isEmpty(jobData)) {
			throw new BaseException("必要参数为空!");
		}
		if ((!isValidExpression(cron))) {
			throw new BaseException("参数异常!");
		}
	}

	private static boolean isValidExpression(final String cronExpression) {
		CronTriggerImpl trigger = new CronTriggerImpl();
		try {
			trigger.setCronExpression(cronExpression);
			Date date = trigger.computeFirstFireTime(null);
			return date != null && date.after(new Date());
		} catch (Exception e) {
			log.error("[TaskUtils.isValidExpression]:failed. throw ex:" + e);
		}
		return false;
	}
	
}
