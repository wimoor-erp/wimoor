package com.wimoor.amazon.adv.sd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.base.BaseMapper;

@Mapper
public interface AmzAdvCampaignsSDMapper extends BaseMapper<AmzAdvCampaignsSD>{

	void insertBatch(List<AmzAdvCampaignsSD> list);

	PageList<Map<String, Object>> getCampaignList(Map<String, Object> map, PageBounds pageBounds);

	Map<String, Object> getSumCampaigns(Map<String, Object> map);

	List<Map<String, Object>> getCampaignChart(Map<String, Object> map);
	
	Map<String,Object> getCampaignByRemind(Map<String,Object> map);

	Map<String, Object> getCampaignByRemindlast(Map<String, Object> param);

	PageList<AmzAdvCampaignsSD> getCampaignsNotArchived(Map<String, Object> param, PageBounds pageBounds);
}