package com.wimoor.amazon.adv.sb.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdgroupsHsa;
import com.wimoor.common.user.UserInfo;
 

public interface IAmzAdvAdgroupsHsaService extends IService<AmzAdvAdgroupsHsa>{
	
	AmzAdvAdgroupsHsa amzGetSBAdGroup(UserInfo user,BigInteger profileId, String adgroupid);
	
	List<AmzAdvAdgroupsHsa> amzGetSBAdGroupList(UserInfo user,BigInteger profileId, Map<String, Object> campaignsParam);
}
