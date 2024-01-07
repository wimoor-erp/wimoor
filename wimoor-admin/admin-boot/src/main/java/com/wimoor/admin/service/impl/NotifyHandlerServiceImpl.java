package com.wimoor.admin.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.mapper.SystemMessageTemplateMapper;
import com.wimoor.admin.mapper.SystemNotifyMapper;
import com.wimoor.admin.mapper.SystemSubscriptionMapper;
import com.wimoor.admin.mapper.SystemTargetMapper;
import com.wimoor.admin.mapper.SystemUserNotifyMapper;
import com.wimoor.admin.pojo.entity.NotifyTarget;
import com.wimoor.admin.pojo.entity.NotifyType;
import com.wimoor.admin.pojo.entity.SystemMessageTemplate;
import com.wimoor.admin.pojo.entity.SystemNotify;
import com.wimoor.admin.pojo.entity.SystemSubscription;
import com.wimoor.admin.pojo.entity.SystemTarget;
import com.wimoor.admin.pojo.entity.SystemUserNotify;
import com.wimoor.admin.service.INotifyHandlerService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
 
@Service("notifyHandlerService")
public class NotifyHandlerServiceImpl extends ServiceImpl<SystemNotifyMapper,SystemNotify> implements INotifyHandlerService {
 
	@Resource
	private SystemUserNotifyMapper systemUserNotifyMapper;

	@Resource
	private SystemSubscriptionMapper systemSubscriptionMapper;
	@Resource
	private SystemTargetMapper systemTargetMapper;
	
	@Resource
	private SystemMessageTemplateMapper systemMessageTemplateMapper;

	public Integer createAnnounce(SystemNotify notify ) {
		// TODO Auto-generated method stub
		notify.setCreatedat(new Date());
		notify.setType(NotifyType.Announce.getValue());
		int result = this.baseMapper.insert(notify);
		return result;
	}


	public void pullAnnounceByNewUSer(String userid) {
		SystemNotify obj = this.baseMapper.findAnnounceByNewUser();
		if (obj != null) {
			SystemUserNotify userNotify = new SystemUserNotify();
			userNotify.setCreatedat(new Date());
			userNotify.setNotify(obj.getId());
			userNotify.setUserid(userid);
			userNotify.setIsread(false);
			systemUserNotifyMapper.insert(userNotify);
		}
	}
	 

	public IPage<Map<String, Object>> getUserNotify(Page<?>page,UserInfo user, NotifyType type, String ftype) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userid", user.getId());
		map.put("ftype", type.getValue());
		map.put("targettype", ftype);
		return systemUserNotifyMapper.findNotifyByUser(page,map);
	}

	public void read(UserInfo user,List<String> notifyIDs) {
		for (String notifyid : notifyIDs) {
			if (StrUtil.isNotEmpty(notifyid)) {
				systemUserNotifyMapper.updateRead(notifyid, user.getId());
			}
		}
	}

	/**
	 * 往Notify表中插入一条提醒记录
	 */
	public Integer createRemind(NotifyTarget target, String action, String sender, String shopid, String content, String title) {
		if (NotifyTarget.amazonAuth.getValue().equals(target.getValue())) {
			SystemNotify notify_old = this.baseMapper.findRemindByLastOne(shopid, target.getValue());
			// 如果内容相同且上一次发送时间在24小时以内，则不用发送
			if (notify_old != null && notify_old.getCreatedat() != null && notify_old.getContent() != null
					&& notify_old.getContent().equals(content)) {
				long hour = (new Date().getTime() - notify_old.getCreatedat().getTime()) / (60 * 60 * 1000);
				if (hour <= 24) {
					return 1;
				}
			}
		}
		SystemNotify notify = new SystemNotify();
		notify.setContent(content);
		notify.setCreatedat(new Date());
		notify.setSender(sender);
		notify.setTarget(target.getValue());
		notify.setAction(action);
		notify.setShopid(shopid);
		notify.setTitle(title);
		notify.setType(NotifyType.Remind.getValue());
		return this.baseMapper.insert(notify);
	}

	/**
	 * 往Notify表中插入一条信息记录 往UserNotify表中插入一条记录，并关联新建的Notify
	 */
	public Integer createMessage(String receiver, String sender, String shopid, String content, String title) {
		SystemNotify notify = new SystemNotify();
		notify.setContent(content);
		notify.setCreatedat(new Date());
		notify.setSender(sender);
		notify.setShopid(shopid);
		notify.setReceiver(receiver);
		notify.setTitle(title);
		notify.setType(NotifyType.Message.getValue());
		int res = this.baseMapper.insert(notify);
		// 关联userNotify
		Integer notifyid = null;
		Map<String, Object> maps = this.baseMapper.findMaxKey();
		Integer maxid = Integer.parseInt(maps.get("id").toString());
		if (res > 0) {
			notifyid = maxid;
		} else {
			new BizException("创建私信消息失败!");
		}
		SystemUserNotify record = new SystemUserNotify();
		record.setIsread(false);
		record.setCreatedat(new Date());
		record.setNotify(notifyid);
		record.setUserid(receiver);
		int result = systemUserNotifyMapper.insert(record);
		return result;
	}
	
	/**
	 * 从UserNotify中获取最近的一条公告信息的创建时间: lastTime 用lastTime作为过滤条件，查询Notify的公告信息
	 * 新建UserNotify并关联查询出来的公告信息
	 * 
	 * @param user
	 */
	public List<String> getTarget(UserInfo user) {
		Date lasttime = systemUserNotifyMapper.findLasttimeByUser(user.getId());
		List<SystemSubscription> subscriptionlist = systemSubscriptionMapper.findByUser(user.getId());
		List<String> targetlist=new ArrayList<String>();
		if (lasttime == null)
			lasttime = GeneralUtil.getDatez("2016-01-01");
		if (lasttime != null) {
			targetlist.add("AL");
			 targetlist.add("PA");
		     targetlist.add("FR");
		}
		targetlist.add("SY");
		if(subscriptionlist!=null) {
			for(SystemSubscription item:subscriptionlist) {
				targetlist.add(item.getTarget());
			}
		}
		return targetlist;
	}

 

	// 订阅的动作 往Subscription中插入一条数据
	public Integer subscribe(String userid, String target, String action, Boolean disable) {
		int result = 0;
		// update订阅为disable=true
		if (disable == true) {
			QueryWrapper<SystemSubscription> query = new QueryWrapper<SystemSubscription>();
			query.eq("userid", userid);
			query.eq("target", target);
			SystemSubscription sub = systemSubscriptionMapper.selectOne(query);
			sub.setCreatedat(new Date());
			sub.setDisable(true);
			result = systemSubscriptionMapper.update(sub,query);
		} else {
			// 添加订阅 第一次是insert 以后是update
			QueryWrapper<SystemSubscription> query = new QueryWrapper<SystemSubscription>();
			query.eq("userid", userid);
			query.eq("target", target);
			SystemSubscription sub = systemSubscriptionMapper.selectOne(query);
			if (sub == null ) {
			   sub = new SystemSubscription();
				sub.setTarget(target);
				sub.setUserid(userid);
				sub.setDisable(false);
				sub.setCreatedat(new Date());
				sub.setAction(action);
				result = systemSubscriptionMapper.insert(sub);
			} else {
				// update
				sub.setCreatedat(new Date());
				sub.setDisable(false);
				result = systemSubscriptionMapper.update(sub,query);
			}
		}
		return result;
	}

	public List<SystemNotify> findMsgLimit() {
		return this.baseMapper.findMsgLimit();
	}
 

	public List<SystemSubscription> getSubsrciptionByUser(String userid) {
		return systemSubscriptionMapper.findByUser(userid);
	}

	public List<SystemNotify> findNoReadByUser(String userid,String islimit) {
		List<SystemNotify> list = this.baseMapper.findNoReadByUser(userid);
		if (list.size() > 0 && list != null) {
			return list;
		} else {
			return null;
		}
	}
	public Integer findNoReadNumberByUser(String userid) {
		Integer result = this.baseMapper.findNoReadByUserCount(userid);
		if (result != null) {
			return result;
		} else {
			return 0;
		}
	}
	
	public List<SystemTarget> findAllTarget() {
		return systemTargetMapper.findAllTarget();
	}

	public List<Map<String, Object>> findNotifyNums(String userid) {
		if (StrUtil.isNotEmpty(userid)) {
			List<Map<String, Object>> list = this.baseMapper.findNotifyNums(userid);
			return list;
		} else {
			return null;
		}
	}

	public List<Map<String, Object>> selectAllTargetsByUser(String userid) {
		List<Map<String, Object>> list = systemUserNotifyMapper.selectAllTargetsByUser(userid);
		return list;
	}

	public int updateMessage(String userid, String content, String id) {
		SystemMessageTemplate entity = systemMessageTemplateMapper.selectById(id);
		if(entity!=null) {
			entity.setContent(content);
			entity.setOperator(new BigInteger(userid));
			entity.setOpttime(new Date());
			return systemMessageTemplateMapper.updateById(entity);
		}else {
			return 0;
		}
	}


	/**
	 * 查询用户的订阅表，得到用户的一系列订阅记录
	 * 通过每一条的订阅记录的target、targetType、action、createdAt去查询Notify表，获取订阅的Notify记录。（注意订阅时间必须早于提醒创建时间）
	 * 查询用户的配置文件SubscriptionConfig，如果没有则使用默认的配置DefaultSubscriptionConfig
	 * 使用订阅配置，过滤查询出来的Notify 使用过滤好的Notify作为关联新建UserNotify
	 */
	public void pullMessage(UserInfo user) {
		Date lasttime = systemUserNotifyMapper.findLasttimeByUser(user.getId());
		List<String> targetlist= getTarget(user);
		Map<String,Object> maps=new HashMap<String, Object>();
		maps.put("lasttime", lasttime);
		maps.put("targetlist", targetlist);
		maps.put("userid", user.getId());
		maps.put("shopid", user.getCompanyid());
		List<SystemNotify> notifylist = this.baseMapper.findAnnounceAndRemaind(maps);
		if (notifylist.size() > 0) {
			for (SystemNotify notify : notifylist) {
				SystemUserNotify userNotify = new SystemUserNotify();
				userNotify.setCreatedat(new Date());
				userNotify.setNotify(notify.getId());
				userNotify.setUserid(user.getId());
				userNotify.setIsread(false);
				systemUserNotifyMapper.insert(userNotify);
			}
		}
	}


	public String findMessageTemplate(String id) {
		SystemMessageTemplate template = systemMessageTemplateMapper.selectById(id);
		if(template!=null) {
			String content=template.getContent();
			content=content.replaceAll("\\$\\{contextPath\\}","");
			return content;
		}else {
			return null;
		}
	}


	public List<SystemNotify> findNoReadByUserAll(UserInfo user) {
		List<SystemNotify> list = this.baseMapper.findNoReadByUserAll(user.getId());
		List<String> nids=new LinkedList<String>();
		for(SystemNotify item:list) {
			nids.add(item.getId().toString());
		}
		read(user,nids) ;
		return list;
	}


	@Override
	public boolean findHasNotifyToday(NotifyTarget notifytype, String subtype, String sysuser, String shopid) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("action",subtype);
		map.put("target",notifytype.getValue());
		map.put("shopid",shopid);
		map.put("sender",sysuser);
		SystemNotify notify = this.baseMapper.findHasNotifyToday(map);
		if(notify!=null ) {
			String todayStr = GeneralUtil.getStrDate(new Date());
			Date today = GeneralUtil.getDatez(todayStr);
			Date createAt = notify.getCreatedat();
			if(createAt.after(today)) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	
	@Override
	public boolean findHasNotifyTwoHour(NotifyTarget notifytype, String subtype, String sysuser, String shopid) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("action",subtype);
		map.put("target",notifytype.getValue());
		map.put("shopid",shopid);
		map.put("sender",sysuser);
		SystemNotify notify = this.baseMapper.findHasNotifyToday(map);
		if(notify!=null ) {
			String todayStr = GeneralUtil.getStrDate(new Date());
			Date today = GeneralUtil.getDatez(todayStr);
			Date createAt = notify.getCreatedat();
			if(GeneralUtil.distanceOfMinutes(createAt, today)>120) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
}
