package com.wimoor.amazon.adv.sd.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.common.user.UserInfo;


public interface IAmzAdvAdgroupsSDService extends IService<AmzAdvAdgroupsSD>{
	
	AmzAdvAdgroupsSD amzGetSDAdGroupExt(UserInfo user,BigInteger profileId, String adgroupid);
	
	List<AmzAdvAdgroupsSD> amzGetSDAdGroupList(UserInfo user,BigInteger profileId, Map<String, Object> campaignsParam);

	PageList<Map<String, Object>> getAdgroupList(Map<String, Object> map, PageBounds pageBounds);

	Map<String, Object> getSumAdGroup(Map<String, Object> map);

	List<Map<String, Object>> getAdgroupChart(Map<String, Object> map);
	
	public List<AmzAdvAdgroupsSD> amzCreateAdGroups(UserInfo user,BigInteger profileId, List<AmzAdvAdgroupsSD> adgroups);

	AmzAdvAdgroupsSD archiveAdGroup(UserInfo user, BigInteger bigInteger, String adgroupId);

	List<AmzAdvAdgroupsSD> amzUpdateAdGroups(UserInfo user, BigInteger key, List<AmzAdvAdgroupsSD> list);

	List<AmzAdvAdgroupsSD> amzListAdGroupsExt(UserInfo user, BigInteger bigInteger, Map<String, Object> param);

	public List<AmzAdvAdgroupsSD> getAdGroupByCampaignsId(UserInfo user,BigInteger profileid, String campaignsid) ;

	public List<AmzAdvAdgroupsSD> amzCreateAdGroups(UserInfo user, String profileid, JSONArray adGroupArray);

	public List<AmzAdvAdgroupsSD> amzUpdateAdGroups(UserInfo user, BigInteger key, JSONArray adGroupArray);
}
