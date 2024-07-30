package com.wimoor.amazon.adv.sp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.base.BaseMapper;

@Mapper
public interface AmzAdvCampaignsMapper  extends BaseMapper<AmzAdvCampaigns>{

	int insertBatch(List<AmzAdvCampaigns> list);
	
	PageList<Map<String,Object>> getCampaignList(Map<String,Object> map, PageBounds pageBounds);
	
	PageList<Map<String,Object>> getAllCampaignList(Map<String,Object> map, PageBounds pageBounds);
	
	List<Map<String,Object>> getCampaignChart(Map<String,Object> map);
	
	List<Map<String,Object>> getCampaignPlacement(Map<String,Object> map);
	
	Map<String,Object> getCampaignByRemind(Map<String,Object> map);
	
	Map<String,Object> getCampaignByRemindlast(Map<String,Object> map);
	
	Map<String, Object> getSumCampaigns(Map<String, Object> map);
	
	PageList<AmzAdvCampaigns> getCampaignsNotArchived(Map<String,Object> map, PageBounds pageBounds);
}