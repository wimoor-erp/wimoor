package com.wimoor.common.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.common.pojo.entity.OperationLog;

@Mapper
public interface OperationLogMapper  extends BaseMapper<OperationLog> {
		List<Map<String,Object>> getLoglist(@Param("userid")String userid);
		Map<String,Object> getLogCount(@Param("userid")String userid);
		List<OperationLog> getOperateRecoedLast5(@Param("userid")String userid,@Param("description")String description);
}