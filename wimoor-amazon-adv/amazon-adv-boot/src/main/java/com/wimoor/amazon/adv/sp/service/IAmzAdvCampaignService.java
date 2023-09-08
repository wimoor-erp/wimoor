package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.common.user.UserInfo;
import tk.mybatis.mapper.entity.Example;
	
public interface IAmzAdvCampaignService extends IService<AmzAdvCampaigns>{
	/**
	 * capture Campaigns where targetingtype="manual" and profileid =profileid;
	 * @param profileid
	 * @return
	 */
	public List<AmzAdvCampaigns> getSpCampaignsByprofile(BigInteger profileid);
	
	public List<AmzAdvCampaigns> getManualSpCampaignsByprofile(BigInteger profileid);
   
	public List<AmzAdvCampaigns> getSpCampaignsNotArchivedByprofile(BigInteger profileid, String name) ;
	/**
	 * A list of up to 100 campaigns to be created. 
	 * Required fields for campaign creation are: 
	 * name, targetingType, state, dailyBudget, and startDate 
	 * Note: You cannot create new SB campaigns with the API.
	 * @param profileid
	 * @param compaignType {sp or hsa} enum Campaigntype must be abbreviate
	 * @param campaigns
	 */
	public List<AmzAdvCampaigns> amzCreateCampaigns(UserInfo user,BigInteger  profileid,List<AmzAdvCampaigns> campaigns);
	/**
	 * 	 
	 * GET /v2/{sp or hsa}/campaigns?
	 * startIndex={startIndex}
	 * &count={count}
	 * &stateFilter={stateFilter}
	 * &name={name}
	 * &campaignIdFilter={campaignIdFilter}
	 * startIndex起始位置
	 * count 每页显示数量
	 * stateFilter 状态【 enabled, paused, or archived】
	 * campaignIdFilter 活动ID
	 * @param profileId
	 * @param compaignType{sp or hsa}
	 * @param campaignsParam[startIndex,count,stateFilter,name,campaignIdFilter]
	 * @return
	 */
	public List<AmzAdvCampaigns>  amzListSpCampaigns(UserInfo user,BigInteger  profileId, Map<String,Object> campaignsParam); 
	/**
	 * 	 
	 * GET /v2/sp/campaigns/extended?
	 * startIndex={startIndex}
	 * &count={count}
	 * &stateFilter={stateFilter}
	 * &name={name}
	 * &campaignIdFilter={campaignIdFilter}
	 * startIndex起始位置
	 * count 每页显示数量
	 * stateFilter 状态【 enabled, paused, or archived】
	 * campaignIdFilter 活动ID
	 * @param profileId
	 * @param compaignType{sp or hsa}
	 * @param campaignsParam[startIndex,count,stateFilter,name,campaignIdFilter]
	 * @return
	 */
	public List<AmzAdvCampaigns> amzListCampaignsEx(UserInfo user,BigInteger  profileId,Map<String,Object> campaignsParam); 
	/**
     * Updates one or more campaigns. 
     * Campaigns are identified using their campaignId. 
     * Sponsored Products uses the field: dailyBudget. 
     * Sponsored Brands uses the field: budget. 
     * For SB you can only update the following 
     * parameters: endDate, and budget for a maximum of 10 campaigns per request.
     * A list of up to 100 updates containing campaignId and the mutable fields to be modified.
     * Mutable fields for Sponsored Products: 
     * name, state, dailyBudget, startDate, endDate, premiumBidAdjustment. 
     * For Sponsored Brands use the field budget , 
     * instead of the field dailyBudget which is only used with Sponsored Products. 
     * Immutable fields for Sponsored Brands: 
     * name, budgetType, startDate 
     * Note: Premium bid adjustment is not supported for Sponsored Brands.
     * @param profileId
     * @param compaignType{sp or hsa}
     * @param campaignsList
	 * @return 
     */
	public List<AmzAdvCampaigns> amzUpdateSpCampaigns(UserInfo user,BigInteger  profileId,  List<AmzAdvCampaigns> campaignsList);
	/**
	 * Sets the campaign status to archived. 
	 * Archived entities cannot be made active again. 
	 * See Developer Notes for more information.
	 * @param profileId
	 * @param compaignType
	 * @param campaignId
	 */
	public AmzAdvCampaigns amzArchiveSpCampaigns(UserInfo user,BigInteger  profileId, String  campaignId); 
	/**
	 * Retrieves a campaign by campaignId. 
	 * Note that this call returns the minimal set of campaign fields, 
	 * but is more efficient than getCampaignEx.
	 * @param profileId
	 * @param compaignType
	 * @param campaignId
	 * @return
	 */
	public AmzAdvCampaigns amzGetSpCampaigns(UserInfo user,BigInteger  profileId,String campaignId);
	/**
	 * Retrieves a list of Sponsored Products campaigns with 
	 * extended fields satisfying optional filtering criteria. 
	 * Extended campaigns interface is not supported for Sponsored Brands.
	 * Retrieves a campaign and its extended fields by campaignId. 
	 * Note that this call returns the complete set of campaign fields 
	 * (including serving status and other read-only fields), 
	 * but is less efficient than getCampaign
	 * @param profileId
	 * @param compaignType
	 * @param campaignId
	 * @return
	 */
	public AmzAdvCampaigns amzGetCampaignEx(UserInfo user,BigInteger  profileId,String campaignId);
	
	public PageList<Map<String,Object>> getCampaignList(Map<String,Object> map, PageBounds pageBounds);
	
	public Map<String,Object> getCampaignChart(Map<String,Object> map);
	
	public List<Map<String,Object>> getCampaignPlacement(Map<String,Object> map);
	
	public AmzAdvCampaigns selectOneByExample(Example example);
	
	public List<Map<String,Object>> getSumCampaigns(Map<String,Object> map);
	
	public Map<String, Object> getChartData(List<Map<String, Object>> list, List<Map<String, Object>> listHsa,List<Map<String, Object>> listSD, Map<String, Object> map);

}
