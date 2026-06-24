package com.wimoor.feishu.service;

import com.wimoor.feishu.pojo.entity.LeaveCalendar;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lark.oapi.core.response.RawResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-09-01
 */
public interface ILeaveCalendarService extends IService<LeaveCalendar> {
	public  void addLeaveCalandar(String appid,String uuid,String user_id, String leave_start_time, String leave_end_time,String log) ;
	public  RawResponse removeLeaveCalandar(String appid,String calendarid);
	public  void deleteLeaveCalandar(String appid,String uuid) ;
	public void checkCalandar();
}
