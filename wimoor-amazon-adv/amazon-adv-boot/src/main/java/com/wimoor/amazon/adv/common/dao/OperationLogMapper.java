package com.wimoor.amazon.adv.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wimoor.amazon.adv.common.pojo.OperationLog;
import com.wimoor.amazon.base.BaseMapper;


@Mapper
public interface OperationLogMapper  extends BaseMapper<OperationLog> {
		List<Map<String,Object>> getLoglist(@Param("userid")String userid);
		Map<String,Object> getLogCount(@Param("userid")String userid);
		List<OperationLog> getOperateRecoedLast5(@Param("userid")String userid,@Param("description")String description);
}