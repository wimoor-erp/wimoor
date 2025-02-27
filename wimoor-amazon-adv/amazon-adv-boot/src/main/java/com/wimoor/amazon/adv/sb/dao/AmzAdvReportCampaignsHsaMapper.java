package com.wimoor.amazon.adv.sb.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsHsaVideo;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportCampaignsHsaMapper extends BaseMapper<AmzAdvReportCampaignsHsa>{
	void insertBatch(List<AmzAdvReportCampaignsHsa> list);
	void insertBatchBrand(List<AmzAdvReportCampaignsHsaBrand> list);
	void insertBatchVideo(List<AmzAdvReportCampaignsHsaVideo> list);
	void insertBatchAttributed(List<AmzAdvReportCampaignsHsaAttributed> listAttributed);
	
	List<Map<String,Object>> getCampaignsHsa(Map<String, Object> param);
}