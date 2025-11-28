package com.wimoor.amazon.adv.sb.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;



import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AmzAdvCampaignsHsaMapper extends BaseMapper<AmzAdvCampaignsHsa>{
	int insertBatch(List<AmzAdvCampaignsHsa> list);
	
	PageList<Map<String,Object>> getCampaignList(Map<String,Object> map, PageBounds pageBounds);
	
	List<Map<String,Object>> getCampaignChart(Map<String,Object> map);
	
	List<Map<String,Object>> getCampaignPlacement(Map<String,Object> map);
	
	Map<String,Object> getCampaignByRemind(Map<String,Object> map);
	
	Map<String,Object> getCampaignByRemindlast(Map<String,Object> map);

	Map<String, Object> getSumCampaigns(Map<String, Object> map);
	
	List<Map<String,Object>> getCampaignPlaceDetail(Map<String,Object> map);
	
	Map<String,Object> getCampaignBrandDetail(Map<String,Object> map);
	
	Map<String,Object> getCampaignVideoDetail(Map<String,Object> map);

	Date getOldDateSBCampaigns(BigInteger profileid);

	PageList<AmzAdvCampaignsHsa> getCampaignsNotArchived(Map<String,Object> map, PageBounds pageBounds);

	Map<String, Object> getSBCampaignVideo(@Param("campaignId")String campaignId,@Param("bydate")String bydate);

	List<String> getIdsByProfile(BigInteger profileid);
}