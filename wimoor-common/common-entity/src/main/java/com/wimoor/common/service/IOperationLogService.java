package com.wimoor.common.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.OperationLog;
import com.wimoor.common.user.UserInfo;

public interface IOperationLogService extends IService<OperationLog> {
		List<Map<String,Object>> getLoglist(String userid);
	    int recordLogin(UserInfo user,String ip,String isautoLogin)throws BizException;
	    Map<String,Object> getLogCount(String userid);
		List<OperationLog> getOperateRecoedLast5(String userid,String description);
}
