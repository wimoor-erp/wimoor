package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

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
	 * 从亚马逊获取一个活动组通过组ID
	 * @param profileId 国家授权ID
	 * @param adgroupid 活动组ID
	 * @return
	 */
	public AmzAdvAdgroups amzGetAdGroup(UserInfo user,BigInteger profileId,String adgroupid);
	
	/**
	 * 获取一个带有创建时间，更新时间和当前状态的活动组，注意会比简单的获取时效要差
	 * @param profileId 国家授权ID
	 * @param adgroupid 活动组ID
	 * @return
	 */
	public AmzAdvAdgroups amzGetAdGroupEx(UserInfo user,BigInteger profileId,String adgroupid);
	
	/**
	 * Create one or more ad groups
	 * A list of up to 100 ad groups to be created. 
	 * Required fields for ad group creation are: 
	 * campaignId, name, state, and defaultBid
	 * @param profileId
	 * @param adgroup
	 * @return
	 */
	public List<AmzAdvAdgroups> amzCreateAdGroups(UserInfo user,BigInteger profileId,List<AmzAdvAdgroups> adgroup);
	
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
	 * Sets the ad group status to archived. This same operation can be performed via an update, 
	 * but is included for completeness. 
	 * Archived entities cannot be made active again
	 * @param profileId
	 * @param adgroupid
	 * @return
	 */
	public AmzAdvAdgroups archiveAdGroup(UserInfo user,BigInteger profileId,String adgroupid);
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
	public List<AmzAdvAdgroups> amzListAdGroups(UserInfo user,BigInteger profileId,Map<String,Object> param);
	
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
	public List<AmzAdvAdgroups> amzListAdGroupsExt(UserInfo user,BigInteger profileId,Map<String,Object> param);
	
	public PageList<Map<String, Object>> getAdGroupList(Map<String,Object> map, PageBounds pageBounds);
	
	public Map<String,Object> getAdGroupChart(Map<String,Object> map);
	
	public Map<String, Object> catchAdGroupSuggestBid(UserInfo user,Map<String, Object> map);
	
	public AmzAdvAdgroups selectOneByExample(Example example);
	
	public List<Map<String,Object>> getSumAdGroup(Map<String,Object> map);
}
