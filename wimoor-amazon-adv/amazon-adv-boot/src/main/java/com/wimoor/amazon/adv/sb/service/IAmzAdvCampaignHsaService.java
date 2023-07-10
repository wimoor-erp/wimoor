package com.wimoor.amazon.adv.sb.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvKeywordsNegativa;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTarge;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.common.user.UserInfo;

import tk.mybatis.mapper.entity.Example;

public interface IAmzAdvCampaignHsaService extends IService<AmzAdvCampaignsHsa> {

	/**
	 * capture Campaigns where targetingtype="manual" and profileid =profileid;
	 * 
	 * @param profileid
	 * @return
	 */
	List<AmzAdvCampaignsHsa> getHsaCampaignsByprofile(BigInteger profileid);

	List<AmzAdvCampaignsHsa> getHsaCampaignsNotArchivedByprofile(BigInteger profileid);

	//List<AmzAdvCampaignsHsa> amzListHsaCampaigns(UserInfo user, BigInteger profileId, Map<String, Object> campaignsParam);

	/**
	 * Updates one or more campaigns. Campaigns are identified using their
	 * campaignId. Sponsored Products uses the field: dailyBudget. Sponsored Brands
	 * uses the field: budget. For SB you can only update the following parameters:
	 * endDate, and budget for a maximum of 10 campaigns per request. A list of up
	 * to 100 updates containing campaignId and the mutable fields to be modified.
	 * Mutable fields for Sponsored Products: name, state, dailyBudget, startDate,
	 * endDate, premiumBidAdjustment. For Sponsored Brands use the field budget ,
	 * instead of the field dailyBudget which is only used with Sponsored Products.
	 * Immutable fields for Sponsored Brands: name, budgetType, startDate Note:
	 * Premium bid adjustment is not supported for Sponsored Brands.
	 * 
	 * @param profileId
	 * @param               compaignType{sp or hsa}
	 * @param campaignsList
	 * @return
	 */
	//List<AmzAdvCampaignsHsa> amzUpdateHsaCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaignsHsa> campaignsList);

	/**
	 * Sets the campaign status to archived. Archived entities cannot be made active
	 * again. See Developer Notes for more information.
	 * 
	 * @param profileId
	 * @param compaignType
	 * @param campaignId
	 */
	//AmzAdvCampaignsHsa amzArchiveHsaCampaigns(UserInfo user, BigInteger profileId, String campaignId);

	/**
	 * Retrieves a campaign by campaignId. Note that this call returns the minimal
	 * set of campaign fields, but is more efficient than getCampaignEx.
	 * 
	 * @param profileId
	 * @param compaignType
	 * @param campaignId
	 * @return
	 */
	//AmzAdvCampaignsHsa amzGetHsaCampaigns(UserInfo user, BigInteger profileId, String campaignId);

	/**
	 * Retrieves a list of Sponsored Products campaigns with extended fields
	 * satisfying optional filtering criteria. Extended campaigns interface is not
	 * supported for Sponsored Brands. Retrieves a campaign and its extended fields
	 * by campaignId. Note that this call returns the complete set of campaign
	 * fields (including serving status and other read-only fields), but is less
	 * efficient than getCampaign
	 * 
	 * @param profileId
	 * @param compaignType
	 * @param campaignId
	 * @return
	 */
	PageList<Map<String, Object>> getCampaignList(Map<String, Object> map, PageBounds pageBounds);

	List<Map<String, Object>> getCampaignChart(Map<String, Object> map);

	List<Map<String, Object>> getCampaignPlacement(Map<String, Object> map);

	AmzAdvCampaignsHsa getHsaCampaignsByName(BigInteger profileId, String name);

	AmzAdvCampaignsHsa selectOneByExample(Example example);

	AmzAdvCampaignsHsa amzGetSBCampaigns(UserInfo user, BigInteger profileId, String campaignId);
	
	List<AmzAdvCampaignsHsa> amzUpdateSBCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaignsHsa> campaignsList);
	
	AmzAdvCampaignsHsa amzArchiveSBCampaigns(UserInfo user, BigInteger profileId, String campaignId);
	
	List<AmzAdvCampaignsHsa> amzListSBCampaigns(UserInfo user, BigInteger profileId, Map<String, Object> campaignsParam);
	
	List<AmzAdvCampaignsHsa> amzCreateDraftsSBCampaigns(UserInfo user,BigInteger  profileId, List<AmzAdvCampaignsHsa> campaignsList);
	
	AmzAdvCampaignsHsa amzGetDraftsSBCampaigns(UserInfo user, BigInteger profileId, String campaignId);
	
	AmzAdvCampaignsHsa amzArchiveDraftsSBCampaigns(UserInfo user, BigInteger profileId, String campaignId);
	
	List<AmzAdvCampaignsHsa> amzUpdateDraftsSBCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaignsHsa> campaignsList);
	
	int amzCreateSBCampaignsWithKeyword(UserInfo user,BigInteger profileId, List<AmzAdvCampaignsHsa> campaignsList, 
			List<AmzAdvKeywords> biddableKeywords, List<AmzAdvKeywordsNegativa> negetivaKeywords); 
	
	int amzCreateSBCampaignsWithTarget(UserInfo user,BigInteger profileId, List<AmzAdvCampaignsHsa> campaignsList, 
			List<AmzAdvProductTarge> productTargeList,List<AmzAdvProductTargeNegativa> negetivaProductTargeList);
	
	Map<String, Object> getSumCampaigns(Map<String, Object> map);
	
	public List<AmzAdvCampaignsHsa> amzListSBCampaigns(AmzAdvProfile profileId, Map<String, Object> campaignsParam);

	public Map<String, Object> getCampaignDetail(Map<String, Object> map);

	Date getOldDateSBCampaigns(AmzAdvProfile profile);
}
