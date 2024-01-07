package com.wimoor.amazon.notifications.service;

import com.wimoor.amazon.notifications.pojo.entity.AmzNotificationsDestination;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 亚马逊Destination 亚马逊消息接受对象 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-27
 */
public interface IAmzNotificationsDestinationService extends IService<AmzNotificationsDestination>  {

	void executTask();
    public String getSqsName();
	void deleteDestination(String id);
}
