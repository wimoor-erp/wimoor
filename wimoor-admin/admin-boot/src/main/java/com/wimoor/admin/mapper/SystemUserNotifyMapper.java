package com.wimoor.admin.mapper;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.admin.pojo.entity.SystemUserNotify;
 
@Mapper
public interface SystemUserNotifyMapper extends BaseMapper<SystemUserNotify>{


	IPage<Map<String, Object>> findNotifyByUser(Page<?> page,@Param("param")Map<String,Object> param);
	
	void updateRead(@Param("notify")String notifyid,@Param("userid") String userid);
	
	/**
	 * 从UserNotify中获取最近的一条公告信息的创建时间: lastTime 用lastTime作为过滤条件，查询Notify的公告信息
	 * 新建UserNotify并关联查询出来的公告信息
	 * 
	 * @param user
	 * @return 
	 */
	Date findLasttimeByUser(String userid);
	
	List<Map<String, Object>> selectAllTargetsByUser(@Param("userid")String userid);
	
}