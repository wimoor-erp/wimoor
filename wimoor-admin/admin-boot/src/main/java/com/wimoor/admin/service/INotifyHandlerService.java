package com.wimoor.admin.service;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.admin.pojo.entity.NotifyTarget;
import com.wimoor.admin.pojo.entity.NotifyType;
import com.wimoor.admin.pojo.entity.SystemNotify;
import com.wimoor.admin.pojo.entity.SystemSubscription;
import com.wimoor.admin.pojo.entity.SystemTarget;
import com.wimoor.common.user.UserInfo;
 

public interface INotifyHandlerService extends IService<SystemNotify> {

	
	 IPage<Map<String,Object>>  getUserNotify(Page<?> page,UserInfo user,NotifyType type,String ftype);
	
	 void read(UserInfo user,List<String> ids);
	 
	 /**
	  * 往Notify表中插入一条提醒记录
	 * @return 
	  */
	public Integer createRemind(NotifyTarget target,String action, String sender,String shopid, String content,String title) ;
	
	//订阅消息
	public Integer subscribe(String userid,String target,String action,Boolean disable);
	
	//查询msg
	List<SystemNotify> findMsgLimit();
	
	List<SystemSubscription> getSubsrciptionByUser(String userid);

	public List<SystemNotify> findNoReadByUser(String userid,String islimit);
	public Integer findNoReadNumberByUser(String userid) ;
	List<SystemTarget> findAllTarget();
	
	Integer createMessage(String receiver,String sender,String shopid, String content,String title);
	
	List<Map<String,Object>> findNotifyNums(String userid);

	public void pullAnnounceByNewUSer(String userid);

	public List<Map<String, Object>> selectAllTargetsByUser(String userid);

	int updateMessage(String userid, String content, String id);

	/**
	 * 查询用户的订阅表，得到用户的一系列订阅记录
	 * 通过每一条的订阅记录的target、targetType、action、createdAt去查询Notify表，获取订阅的Notify记录。（注意订阅时间必须早于提醒创建时间）
	 * 查询用户的配置文件SubscriptionConfig，如果没有则使用默认的配置DefaultSubscriptionConfig
	 * 使用订阅配置，过滤查询出来的Notify
	 * 使用过滤好的Notify作为关联新建UserNotify
	 */
	public void pullMessage(UserInfo user);


	public String findMessageTemplate(String id);


	public List<SystemNotify> findNoReadByUserAll(UserInfo user);


	public boolean findHasNotifyToday(NotifyTarget shipment, String string, String sysuser, String shopid);


	boolean findHasNotifyTwoHour(NotifyTarget notifytype, String subtype, String sysuser, String shopid);


	public Integer createAnnounce(SystemNotify notify);
	
}
