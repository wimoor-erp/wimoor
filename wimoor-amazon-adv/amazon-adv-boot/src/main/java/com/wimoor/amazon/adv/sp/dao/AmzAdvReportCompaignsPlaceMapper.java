package com.wimoor.amazon.adv.sp.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaignsPlace;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaignsPlaceAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaignsPlaceAttributedSame;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportCompaignsPlaceMapper extends BaseMapper<AmzAdvReportCompaignsPlace>{
	void insertBatch(List<AmzAdvReportCompaignsPlace> list);
	List<Map<String,Object>> getCampaignsPlace(Map<String, Object> param);
	void insertBatchAttributed(List<AmzAdvReportCompaignsPlaceAttributed> listAttributed);
	void insertBatchAttributedSame(List<AmzAdvReportCompaignsPlaceAttributedSame> listAttributedSame);
}