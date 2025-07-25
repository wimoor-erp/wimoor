package com.wimoor.amazon.adv.sb.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sb.pojo.*;
import org.apache.ibatis.annotations.Mapper;

import com.wimoor.amazon.base.BaseMapper;

@Mapper
public interface AmzAdvReportAdsHsaMapper extends BaseMapper<AmzAdvReportAdsHsa>{
	void insertBatch(List<AmzAdvReportAdsHsa> list);
	void insertBatchBrand(List<AmzAdvReportAdsHsaBrand> list);
	void insertBatchVideo(List<AmzAdvReportAdsHsaVideo> list);
	void insertBatchAttributed(List<AmzAdvReportAdsHsaAttributedAll> listAttributed);
	List<Map<String,Object>> getAdgroupsHsa(Map<String, Object> param);
}