package com.wimoor.amazon.adv.common.service;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.OperationLog;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

public interface IOperationLogService extends IService<OperationLog> {
		List<Map<String,Object>> getLoglist(String userid);
	    int recordLogin(UserInfo user,String ip,String isautoLogin)throws BizException;
	    Map<String,Object> getLogCount(String userid);
		List<OperationLog> getOperateRecoedLast5(String userid,String description);
}
