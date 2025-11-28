package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampKeywordsNegativa;
import com.wimoor.common.user.UserInfo;
 

import tk.mybatis.mapper.entity.Example;

public interface IAmzAdvCampKeywordsNegativaService extends IService<AmzAdvCampKeywordsNegativa> {

	public List<AmzAdvCampKeywordsNegativa> getCampKeywordsNegativaByCampaignsId(BigInteger profileId, String campaignsId);
	
	
	public List<AmzAdvCampKeywordsNegativa> amzCreateCampaignNegativeKeywords(UserInfo user,BigInteger  profileId,List<AmzAdvCampKeywordsNegativa> campKeywordsNegativa);
	
	public List<AmzAdvCampKeywordsNegativa> amzUpdateCampaignNegativeKeywords(UserInfo user,String  profileid,JSONArray keywordArray);
	
	public List<AmzAdvCampKeywordsNegativa> amzUpdateCampaignNegativeKeywords(UserInfo user,BigInteger  profileId,List<AmzAdvCampKeywordsNegativa> campKeywordsNegativa);
	
	public List<AmzAdvCampKeywordsNegativa> amzDeleteCampaignNegativaKeywords(UserInfo user,BigInteger profileId,JSONObject param);
	
	public List<Map<String, Object>> getCampaignNegativaKeywordsList(Map<String, Object> map);
	
	public AmzAdvCampKeywordsNegativa selectOneByExample(Example example);

	public  List<AmzAdvCampKeywordsNegativa> amzCreateCampaignNegativeKeywords(UserInfo user, String profileid, JSONArray nkeywords);

	public List<AmzAdvCampKeywordsNegativa> amzListCampaignNegativeKeywords(UserInfo user, BigInteger bigInteger, JSONObject param);
}
