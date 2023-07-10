package com.wimoor.amazon.adv.sd.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeNegativaSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.common.user.UserInfo;

import tk.mybatis.mapper.entity.Example;

public interface IAmzAdvCampaignsSDService  extends IService<AmzAdvCampaignsSD>{

	PageList<Map<String, Object>> getCampaignList(Map<String, Object> map, PageBounds pageBounds);

	List<AmzAdvCampaignsSD> amzListCampaignsEx(UserInfo user, BigInteger bigInteger, Map<String, Object> param);
	public AmzAdvCampaignsSD amzGetSpCampaigns(UserInfo user, BigInteger profileId, String campaignId) ;
	Map<String, Object> getSumCampaigns(Map<String, Object> map);

	List<Map<String, Object>> getCampaignChart(Map<String, Object> map);


	public int amzCreateSDCampaignsWithTarget(UserInfo user, 
			BigInteger profileid, 
			AmzAdvCampaignsSD campaigns,
			AmzAdvAdgroupsSD adgroups,
			List<AmzAdvProductadsSD> productAdsList, 
			List<AmzAdvProductTargeSD> productTargeList, 
			List<AmzAdvProductTargeNegativaSD> negetivaProductTargeList) ;
	
	AmzAdvCampaignsSD selectOneByExample(Example example);
	public List<AmzAdvCampaignsSD> amzCreateCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaignsSD> campaignsList) ;

	public List<AmzAdvCampaignsSD> amzUpdateSDCampaigns(UserInfo user, BigInteger profileId, List<AmzAdvCampaignsSD> campaignsList);

	List<AmzAdvCampaignsSD> getSDCampaignsNotArchivedByprofile(BigInteger profileId);

}
