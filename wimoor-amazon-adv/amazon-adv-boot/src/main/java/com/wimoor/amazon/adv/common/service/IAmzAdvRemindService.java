package com.wimoor.amazon.adv.common.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmzAdvRemind;
import com.wimoor.common.user.UserInfo;
 

public interface IAmzAdvRemindService extends IService<AmzAdvRemind>{

	public PageList<Map<String,Object>> selectByCondition(Map<String,Object> param,PageBounds pagebounds);
	
	public void refreshRemaind(Map<String,Object> param);
	
	public void refreshRemaind();
	
	public List<Map<String, Object>> getRemindByMarketplace(Map<String, Object> map);
	
	public List<Map<String, Object>> getRemaindUserList(UserInfo user);
	
	public void deleteReminForProfileId(BigInteger profileid);
	
	public void deleteRemaindForInvalid();
}
