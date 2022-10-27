package com.wimoor.amazon.adv.sp.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaigns;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaignsAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaignsAttributedSame;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportCompaignsMapper extends BaseMapper<AmzAdvReportCompaigns>{

	void insertBatch(List<AmzAdvReportCompaigns> list);
	List<Map<String,Object>> getCampaigns(Map<String, Object> param);
	void insertBatchAttributed(List<AmzAdvReportCompaignsAttributed> listAttributed);
	void insertBatchAttributedSame(List<AmzAdvReportCompaignsAttributedSame> listAttributedSame);
}