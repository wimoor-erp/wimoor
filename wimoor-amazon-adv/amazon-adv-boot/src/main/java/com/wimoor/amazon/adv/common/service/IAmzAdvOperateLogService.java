package com.wimoor.amazon.adv.common.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmzAdvOperateLog;
 

public interface IAmzAdvOperateLogService extends IService<AmzAdvOperateLog>{
	public void saveOperateLog(String beanClasz,String userId,BigInteger profileId,Object afterObject,Object beforeObject);
	
	public void saveBatchOperateLog(String beanClasz,String userId,BigInteger profileId,Map<BigInteger, List<Object>> map,Map<BigInteger, List<Object>> oldmap);
	
	public PageList<Map<String,Object>> getOperateLogList(Map<String, Object> map, PageBounds pageBounds);
	
	public void insertList(List<AmzAdvOperateLog> operateLogList);
	
	public int updateOperateLogRemark(String id, String remark);
}
