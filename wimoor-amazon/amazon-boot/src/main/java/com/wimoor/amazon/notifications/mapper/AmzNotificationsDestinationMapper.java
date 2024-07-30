package com.wimoor.amazon.notifications.mapper;

import com.wimoor.amazon.notifications.pojo.entity.AmzNotificationsDestination;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 亚马逊Destination 亚马逊消息接受对象 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-27
 */
@Mapper
public interface AmzNotificationsDestinationMapper extends BaseMapper<AmzNotificationsDestination> {

}
