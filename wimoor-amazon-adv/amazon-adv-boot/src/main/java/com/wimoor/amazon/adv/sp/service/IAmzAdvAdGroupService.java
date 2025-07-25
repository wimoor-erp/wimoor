package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.common.user.UserInfo;

import tk.mybatis.mapper.entity.Example;
/**
 * Used to create, read, update or delete ad groups. 
 * The following table describes the service behavior in terms of the URL space 
 * and HTTP methods supported by the service-managed resources.
 * Note: Ad groups are only managed for Sponsored Products 
 * and do not apply to Sponsored Brands.
 * @author Administrator
 *
 */
public interface IAmzAdvAdGroupService extends IService<AmzAdvAdgroups>{
	public List<AmzAdvAdgroups> getAdGroupByCampaignsId(UserInfo user,BigInteger profileid, String campaignsid);
	/**
	 * Create one or more ad groups
	 * A list of up to 100 ad groups to be created. 
	 * Required fields for ad group creation are: 
	 * campaignId, name, state, and defaultBid
	 * @param profileId
	 * @param adGroupArray
	 * @return
	 */
	public List<AmzAdvAdgroups> amzCreateAdGroups(UserInfo user,BigInteger profileId,JSONArray adGroupArray);
	
	/**
	 * A list of up to 100 updates containing 
	 * adGroupId and the mutable fields to be modified.
	 *  Mutable fields are: name, and state
	 * @param profileId
	 * @param adgroup
	 * @return
	 */
	public List<AmzAdvAdgroups> amzUpdateAdGroups(UserInfo user,BigInteger profileId,List<AmzAdvAdgroups> adgroup);
	
 
	/**
	 * GET /v2/sp/adGroups/?startIndex={startIndex}
     * &count={count}
     * &stateFilter={stateFilter}
     * &campaignIdFilter={campaignIdFilter}
     * &adGroupIdFilter={adGroupIdFilter}
     * &name={name} 
     * startIndex起始位置
	 * count 每页显示数量
	 * stateFilter 状态【 enabled, paused, or archived】
	 * adGroupIdFilter 活动组ID	
	 * name 活动名称
	 * @param profileId
	 * @param param
	 * @return
	 */
	public List<AmzAdvAdgroups> amzListAdGroups(BigInteger profileId, JSONObject campaignsParam);
	 
	
	public PageList<Map<String, Object>> getAdGroupList(Map<String,Object> map, PageBounds pageBounds);
	
	public Map<String,Object> getAdGroupChart(Map<String,Object> map);
	
	public AmzAdvAdgroups selectOneByExample(Example example);
	
	public List<Map<String,Object>> getSumAdGroup(Map<String,Object> map);
	
	public List<AmzAdvAdgroups> amzUpdateAdGroups(UserInfo user, BigInteger profileId, JSONArray adGroupArray);
	
	public List<AmzAdvAdgroups> amzDeleteAdGroups(UserInfo user, BigInteger profileId, JSONObject param) ;
	public String getAdGroupsBid(UserInfo user, BigInteger profileid, BigInteger adgroupid);
}
