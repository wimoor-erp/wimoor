package com.wimoor.amazon.adv.sd.dao;

import java.util.List;

import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSDAttributed;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSDAttributedNew;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSDAttributedSame;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSDAttributedView;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportCampaignsSDMapper extends BaseMapper<AmzAdvReportCampaignsSD>{

	void insertBatch(List<AmzAdvReportCampaignsSD> list);

	void insertBatchAttributed(List<AmzAdvReportCampaignsSDAttributed> listAttributed);

	void insertBatchAttributedView(List<AmzAdvReportCampaignsSDAttributedView> listAttributedView);

	void insertBatchAttributedSame(List<AmzAdvReportCampaignsSDAttributedSame> listAttributedSame);

	void insertBatchAttributedNew(List<AmzAdvReportCampaignsSDAttributedNew> listAttributedNew);
}