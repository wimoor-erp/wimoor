package com.wimoor.amazon.adv.sd.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sd.pojo.*;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportCampaignsSDMapper extends BaseMapper<AmzAdvReportCampaignsSD>{

	void insertBatch(List<AmzAdvReportCampaignsSD> list);

	void insertBatchAttributed(List<AmzAdvReportCampaignsSDAttributedAll> listAttributed);

//	void insertBatchAttributedView(List<AmzAdvReportCampaignsSDAttributedView> listAttributedView);
//
//	void insertBatchAttributedSame(List<AmzAdvReportCampaignsSDAttributedSame> listAttributedSame);
//
//	void insertBatchAttributedNew(List<AmzAdvReportCampaignsSDAttributedNew> listAttributedNew);

	List<Map<String,Object>> getCampaigns(Map<String, Object> param);
}