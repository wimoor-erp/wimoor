package com.wimoor.amazon.adv.sb.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsPlaceHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsPlaceHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsPlaceHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsPlaceHsaVideo;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportCampaignsPlaceHsaMapper extends BaseMapper<AmzAdvReportCampaignsPlaceHsa>{
	void insertBatch(List<AmzAdvReportCampaignsPlaceHsa> list);
	List<Map<String,Object>> getCampaignsPlaceHsa(Map<String, Object> param);
	void insertBatchVideo(List<AmzAdvReportCampaignsPlaceHsaVideo> listVideo);
	void insertBatchBrand(List<AmzAdvReportCampaignsPlaceHsaBrand> listBrand);
	void insertBatchAttributed(List<AmzAdvReportCampaignsPlaceHsaAttributed> listAttributed);
}