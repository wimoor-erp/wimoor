package com.wimoor.amazon.notifications.mapper;

import com.wimoor.amazon.notifications.pojo.entity.AmzNotificationsSubscriptions;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 订阅消息对象 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-27
 */
@Mapper
public interface AmzNotificationsSubscriptionsMapper extends BaseMapper<AmzNotificationsSubscriptions> {

}
