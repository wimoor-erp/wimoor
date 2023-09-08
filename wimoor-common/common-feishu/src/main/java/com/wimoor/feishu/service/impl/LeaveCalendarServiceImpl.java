package com.wimoor.feishu.service.impl;

import com.wimoor.feishu.pojo.entity.LeaveCalendar;
import com.wimoor.common.GeneralUtil;
import com.wimoor.feishu.config.FeiShuClientBuilder;
import com.wimoor.feishu.mapper.LeaveCalendarMapper;
import com.wimoor.feishu.service.ILeaveCalendarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.internal.LinkedTreeMap;
import com.lark.oapi.Client;
import com.lark.oapi.core.response.RawResponse;
import com.lark.oapi.core.token.AccessTokenType;
import com.lark.oapi.core.utils.Jsons;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-09-01
 */
@Service
public class LeaveCalendarServiceImpl extends ServiceImpl<LeaveCalendarMapper, LeaveCalendar> implements ILeaveCalendarService {
	 @Autowired 
	 FeiShuClientBuilder clientBuilder;
	 
		 
		@SuppressWarnings("unchecked")
		public  void addLeaveCalandar(String appid,String uuid,String user_id, String leave_start_time, String leave_end_time,String log) {
			Client client=clientBuilder.getClient(appid); 
			List<LeaveCalendar> entitys = lambdaQuery()
                    .eq(LeaveCalendar::getUuid,uuid)
                    .eq(LeaveCalendar::getAppid, appid)
                    .eq(LeaveCalendar::getIsdelete, false).list();
			if(entitys.size()>0) {
				deleteLeaveCalandar(appid,uuid);
			}
			    // 发起请求
			    RawResponse resp;
				try {
					SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Calendar cstart=Calendar.getInstance();
					cstart.setTime(fmt.parse(leave_start_time));
					String timestart = cstart.getTime().getTime()+"";
					Calendar cend=Calendar.getInstance();
					cend.setTime(fmt.parse(leave_end_time));
					String timeend = cend.getTime().getTime()+"";
					Map<String, Object> body = new HashMap<>();
				    body.put("user_id",user_id);
				    body.put("timezone", "Asia/Shanghai");
				    body.put("calendar_id","timeoff:"+uuid);
				    body.put("start_time", timestart.substring(0, 10));
				    body.put("end_time", timeend.substring(0, 10));
				    body.put("title", "请假中");
					body.put("description", "若删除此日程，飞书中相应的“请假”标签将自动消失，而请假系统中的休假申请不会被撤销。");
					resp = client.post(
					    "https://open.feishu.cn/open-apis/calendar/v4/timeoff_events?user_id_type=user_id"
					    , body
					    , AccessTokenType.Tenant);
			    // 处理结果
			    if(resp.getStatusCode()==200) {
			    	Map<String,Object> json = Jsons.DEFAULT.fromJson(new String(resp.getBody()),Map.class);
			    	Map<String,Object> resultData=(LinkedTreeMap<String,Object>)json.get("data");
			    	LeaveCalendar entity=new LeaveCalendar();
			    	entity.setUuid(uuid);
			    	entity.setAppid(appid);
			    	entity.setIsdelete(false);
			    	entity.setUserid(user_id);
			    	entity.setOpttime(new Date());
			    	entity.setEventContentType("leave_approval");
			    	entity.setTimeoffEventId(resultData.get("timeoff_event_id").toString());
			    	entity.setStartDate(cstart.getTime());
			    	entity.setEndDate(cend.getTime());
			    	entity.setLogs(log);
			        save(entity);
			    }else if(resp.getStatusCode()==400) {
			    	    System.out.println(Jsons.DEFAULT.toJson(resp.getHeaders()));
					    System.out.println(new String(resp.getBody()));
					    System.out.println(resp.getRequestID());
			    }
			   
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	 
		public  void deleteLeaveCalandar(String appid,String uuid) {
			List<LeaveCalendar> entitys = lambdaQuery()
					                        .eq(LeaveCalendar::getUuid,uuid)
					                        .eq(LeaveCalendar::getAppid, appid)
					                        .eq(LeaveCalendar::getIsdelete, false).list();
			for(LeaveCalendar entity:entitys) {
				RawResponse resp = this.removeLeaveCalandar(appid,entity.getTimeoffEventId());
				if(resp.getStatusCode()==200) {
					entity.setIsdelete(true);
					updateById(entity);
				}else {
					 System.out.println(resp.getStatusCode());
					    System.out.println(Jsons.DEFAULT.toJson(resp.getHeaders()));
					    System.out.println(new String(resp.getBody()));
					    System.out.println(resp.getRequestID());
				}
			}
			
	    }
		public void checkCalandar() {
			List<LeaveCalendar> entitys = lambdaQuery().eq(LeaveCalendar::getIsdelete, false).list();
			for(LeaveCalendar item:entitys) {
				Client client=clientBuilder.getClient(item.getAppid());
				 RawResponse resp = null;
					try {
						Map<String, Object> body = new HashMap<>();
					    body.put("user_id",item.getUserid());
					    body.put("instance_code", item.getUuid());
					    body.put("locale","zh-CN");
						resp = client.post(
						    "https://open.feishu.cn/open-apis/approval/v4/instances/query?user_id_type=user_id"
						    , body
						    , AccessTokenType.Tenant);
						    System.out.println(Jsons.DEFAULT.toJson(resp.getHeaders()));
						    System.out.println(new String(resp.getBody()));
						    System.out.println(resp.getRequestID());
				    // 处理结果
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		
		
		public  RawResponse removeLeaveCalandar(String appid,String calendarid) {
			Client client=clientBuilder.getClient(appid);
			    Map<String, Object> body = new HashMap<>();
			    // 发起请求
			    RawResponse resp = null;
				try {
					resp = client.delete(
					    "https://open.feishu.cn/open-apis/calendar/v4/timeoff_events/"+calendarid
					    , body
					    , AccessTokenType.Tenant);
			    // 处理结果
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return resp;
		}
}
