package com.wimoor.amazon.adv.sd.dao;

import java.util.List;

import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsT0001SD;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportCampaignsT0001SDMapper extends BaseMapper<AmzAdvReportCampaignsT0001SD>{

	void insertBatch(List<AmzAdvReportCampaignsT0001SD> list);
 
}