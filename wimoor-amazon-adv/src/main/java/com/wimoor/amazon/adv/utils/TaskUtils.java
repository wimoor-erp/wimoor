package com.wimoor.amazon.adv.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.wimoor.amazon.adv.task.pojo.ScheduleJob;
import com.wimoor.amazon.adv.task.pojo.SysTimeTaskLog;
import com.wimoor.amazon.adv.task.service.ISysTimeTaskLogService;
import com.wimoor.util.SpringUtil;

import cn.hutool.core.util.StrUtil;

public class TaskUtils {
	 
	private static ISysTimeTaskLogService sTimetaskLogService = (ISysTimeTaskLogService) SpringUtil
			.getBean("sysTimeTaskLogService");

	/**
	 * 通过反射调用scheduleJob中定义的方法
	 * 
	 * @param scheduleJob
	 */
	@SuppressWarnings("unchecked")
	public static void invokMethod(ScheduleJob scheduleJob) {
		Object object = null;
		Class clazz = null;
		boolean flag = true;
		if (!StringUtils.isEmpty(scheduleJob.getSpringId())) {
			object = SpringUtil.getBean(scheduleJob.getSpringId());
		} 
		if (object==null&&!StringUtils.isEmpty(scheduleJob.getBeanClass())) {
			try {
				System.out.println(scheduleJob.getBeanClass());
				clazz = Class.forName(scheduleJob.getBeanClass());
				object = clazz.newInstance();
			} catch (Exception e) {
				flag = false;
				SysTimeTaskLog tlog = new SysTimeTaskLog();
				tlog.setCreatedate(new Date());
				tlog.setJobId(scheduleJob.getJobId().toString());
				tlog.setReason("未找到" + scheduleJob.getBeanClass() + "对应的class");
				tlog.setState("fail");
				sTimetaskLogService.save(tlog);
				e.printStackTrace();
			}

		}
		if (object == null) {
			flag = false;
			SysTimeTaskLog tlog = new SysTimeTaskLog();
			tlog.setCreatedate(new Date());
			tlog.setJobId(scheduleJob.getJobId().toString());
			tlog.setReason("未找到" + scheduleJob.getBeanClass() + "对应的class");
			tlog.setState("fail");
			sTimetaskLogService.save(tlog);
			return;
		}
		clazz = object.getClass();
		Method method = null;
		try {
			if(scheduleJob.getJobData()!=null&&StrUtil.isNotBlank(scheduleJob.getJobData())) {
				method = clazz.getDeclaredMethod(scheduleJob.getMethodName(), new Class[] { String.class });
			}else {
				method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
			}
		} catch (NoSuchMethodException e) {
			flag = false;
			SysTimeTaskLog tlog = new SysTimeTaskLog();
			tlog.setCreatedate(new Date());
			tlog.setJobId(scheduleJob.getJobId().toString());
			tlog.setReason("未找到" + scheduleJob.getBeanClass() + "类下" + scheduleJob.getMethodName() + "对应的方法");
			tlog.setState("fail");
			sTimetaskLogService.save(tlog);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (method != null) {
			try {
				if(scheduleJob.getJobData()!=null&&StrUtil.isNotBlank(scheduleJob.getJobData())) {
					method.invoke(object, scheduleJob.getJobData());
				}else {
					method.invoke(object);
				}
				
			
			} catch (IllegalAccessException e) {
				flag = false;
				SysTimeTaskLog tlog = new SysTimeTaskLog();
				tlog.setCreatedate(new Date());
				tlog.setJobId(scheduleJob.getJobId().toString());
				tlog.setReason("未找到" + scheduleJob.getBeanClass() + "类下" + scheduleJob.getMethodName() + "对应的方法参数设置错误");
				tlog.setState("fail");
				sTimetaskLogService.save(tlog);
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				flag = false;
				SysTimeTaskLog tlog = new SysTimeTaskLog();
				tlog.setCreatedate(new Date());
				tlog.setJobId(scheduleJob.getJobId().toString());
				tlog.setReason("未找到" + scheduleJob.getBeanClass() + "类下" + scheduleJob.getMethodName() + "对应的方法参数设置错误");
				tlog.setState("fail");
				sTimetaskLogService.save(tlog);
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				flag = false;
				SysTimeTaskLog tlog = new SysTimeTaskLog();
				tlog.setCreatedate(new Date());
				tlog.setJobId(scheduleJob.getJobId().toString());
				tlog.setReason("未找到" + scheduleJob.getBeanClass() + "类下" + scheduleJob.getMethodName() + "对应的方法参数设置错误");
				tlog.setState("fail");
				sTimetaskLogService.save(tlog);
				e.printStackTrace();
			}
		}
		if (flag) {
			// System.out.println("任务名称 = [" + scheduleJob.getJobName() +
			// "]----------启动成功");
			SysTimeTaskLog tlog = new SysTimeTaskLog();
			tlog.setCreatedate(new Date());
			tlog.setJobId(scheduleJob.getJobId().toString());
			tlog.setState("success");
			sTimetaskLogService.save(tlog);
		}

	}
}
