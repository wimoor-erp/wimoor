package com.wimoor.common.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mapper.OperationLogMapper;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.OperationLog;
import com.wimoor.common.service.IOperationLogService;
import com.wimoor.common.user.UserInfo;

@Service("operationLogService")
public class OperationLogServiceImpl  extends ServiceImpl<OperationLogMapper,OperationLog> implements IOperationLogService {
 
 

	@Resource
	OperationLogMapper  operationLogMapper;
	public List<Map<String,Object>> getLoglist(String userid) {
		return operationLogMapper.getLoglist(userid);
	}
	
	
	public int recordLogin(UserInfo user,String ip,String isautoLogin) throws BizException{
		 if(!"yes".equals(isautoLogin)){
			OperationLog log=new OperationLog();
			log.setDescription("用户模块-用户登录");
			log.setExceptiondetail(null);
			log.setIp(ip);
			log.getId();
			log.setLogtype(null);
			log.setMethod("login");
			log.setParam("{\"userid\":[\""+user.getId()+"\"],\"username\":[\""+user.getAccount()+"\"]}");
			log.setUserid(user.getId());
			log.setTime(new Date());
		    return this.save(log)?1:0;
		}
		else{
			return 0;
		}
		
	}
 

	public Map<String, Object> getLogCount(String userid) {
		return operationLogMapper.getLogCount(userid);
	}


	public List<OperationLog> getOperateRecoedLast5(String userid,String description) {
		return operationLogMapper.getOperateRecoedLast5(userid,description);
	}

	 
}
