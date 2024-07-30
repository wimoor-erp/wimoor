package com.wimoor.amazon.adv.sb.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdgroupsHsa;
import com.wimoor.common.user.UserInfo;
 

public interface IAmzAdvAdgroupsHsaService extends IService<AmzAdvAdgroupsHsa>{
	
	public List<AmzAdvAdgroupsHsa> amzGetSBAdGroupList(AmzAdvProfile profile, JSONObject adgroupParam);

	List<AmzAdvAdgroupsHsa> getAdGroupByCampaignsId(UserInfo user, BigInteger profileid, String campaignsid);

	PageList<Map<String, Object>> getAdgroupList(Map<String, Object> map, PageBounds pageBounds);
	
	Map<String, Object> getSumAdGroup(Map<String, Object> map);
	
    List<AmzAdvAdgroupsHsa> amzCreateAdGroups(UserInfo user, BigInteger profileid, JSONArray adGroupArray);

	List<AmzAdvAdgroupsHsa> amzUpdateAdGroups(UserInfo user, BigInteger profileid, JSONArray adGroupArray);

	List<AmzAdvAdgroupsHsa> amzListAdGroups(BigInteger profileid, Map<String, Object> param);

	List<AmzAdvAdgroupsHsa> amzDeleteAdGroups(UserInfo user,BigInteger profileid, JSONObject param);

	public List<Map<String, Object>> getAdgroupChart(Map<String, Object> map);
}
