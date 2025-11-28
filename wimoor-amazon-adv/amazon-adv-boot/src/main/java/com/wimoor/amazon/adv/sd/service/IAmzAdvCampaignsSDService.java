package com.wimoor.amazon.adv.sd.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.common.user.UserInfo;

import tk.mybatis.mapper.entity.Example;

public interface IAmzAdvCampaignsSDService  extends IService<AmzAdvCampaignsSD>{

	PageList<Map<String, Object>> getCampaignList(Map<String, Object> map, PageBounds pageBounds);

	List<AmzAdvCampaignsSD> amzListCampaignsEx(UserInfo user, BigInteger bigInteger, Map<String, Object> param);
	
	public AmzAdvCampaignsSD amzGetCampaigns(UserInfo user, BigInteger profileId, String campaignId) ;
	
	Map<String, Object> getSumCampaigns(Map<String, Object> map);

	List<Map<String, Object>> getCampaignChart(Map<String, Object> map);

	public AmzAdvCampaignsSD amzArchiveSpCampaigns(UserInfo user, BigInteger profileId, String campaignId);
	
	AmzAdvCampaignsSD selectOneByExample(Example example);
	
	public List<AmzAdvCampaignsSD> amzCreateCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaignsSD> campaignsList) ;

	public List<AmzAdvCampaignsSD> amzUpdateSDCampaigns(UserInfo user, BigInteger profileId,JSONArray campaignsList);
	
	public List<AmzAdvCampaignsSD> amzUpdateSDCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaignsSD> campaignsList) ;

	PageList<AmzAdvCampaignsSD> getSDCampaignsNotArchivedByprofile(QueryForList query);

	public List<AmzAdvProductadsSD> createCampaigns(JSONObject jsonobject, UserInfo user);

	List<AmzAdvCampaignsSD> amzCreateSDCampaigns(UserInfo user, BigInteger key, JSONArray jsonArray);

	String amzGetCampaignBudgetUsage(UserInfo user, BigInteger bigInteger, JSONObject param);

}
