package com.wimoor.amazon.adv.sd.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sd.pojo.*;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportProductAdsSDMapper extends BaseMapper<AmzAdvReportProductAdsSD>{
     
	void insertBatch(List<AmzAdvReportProductAdsSD> list);

	void insertBatchAttributed(List<AmzAdvReportProductadsSDAttributedAll> listAttributed);

    List<Map<String, Object>> getProductAds(Map<String, Object> param);
}