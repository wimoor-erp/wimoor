package com.wimoor.amazon.notifications.service;

import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.notifications.pojo.entity.AmzNotificationsSubscriptions;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订阅消息对象 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-27
 */
public interface IAmzNotificationsSubscriptionsService extends IService<AmzNotificationsSubscriptions>,IRunAmazonService  {
	public void deleteSubscriptions(String destinationid);
}
