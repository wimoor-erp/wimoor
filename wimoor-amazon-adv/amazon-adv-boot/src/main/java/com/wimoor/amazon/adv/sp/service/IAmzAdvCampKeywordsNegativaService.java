package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampKeywordsNegativa;
import com.wimoor.common.user.UserInfo;
 

import tk.mybatis.mapper.entity.Example;

public interface IAmzAdvCampKeywordsNegativaService extends IService<AmzAdvCampKeywordsNegativa> {

	public List<AmzAdvCampKeywordsNegativa> getCampKeywordsNegativaByCampaignsId(BigInteger profileId, String campaignsId);
	
	public AmzAdvCampKeywordsNegativa amzGetCampaignNegativeKeyword(UserInfo user,BigInteger  profileId,String keywordId);
	
	public AmzAdvCampKeywordsNegativa amzGetCampaignNegativeKeywordEx(UserInfo user,BigInteger  profileId,String keywordId);
	
	public List<AmzAdvCampKeywordsNegativa> amzCreateCampaignNegativeKeywords(UserInfo user,BigInteger  profileId,List<AmzAdvCampKeywordsNegativa> campKeywordsNegativa);
	
	public List<AmzAdvCampKeywordsNegativa> amzUpdateCampaignNegativeKeywords(UserInfo user,BigInteger  profileId,List<AmzAdvCampKeywordsNegativa> campKeywordsNegativa);
	
	public AmzAdvCampKeywordsNegativa amzRemoveCampaignNegativeKeyword(UserInfo user,BigInteger  profileId,String keywordId);
	
	public List<AmzAdvCampKeywordsNegativa> amzListCampaignNegativeKeywords(UserInfo user,BigInteger  profileId,Map<String,Object> param);
	
	public List<AmzAdvCampKeywordsNegativa> amzListCampaignNegativeKeywordsEx(UserInfo user,BigInteger  profileId,Map<String,Object> param);
	
	public List<Map<String, Object>> getCampaignNegativaKeywordsList(Map<String, Object> map);
	
	public AmzAdvCampKeywordsNegativa selectOneByExample(Example example);
}
