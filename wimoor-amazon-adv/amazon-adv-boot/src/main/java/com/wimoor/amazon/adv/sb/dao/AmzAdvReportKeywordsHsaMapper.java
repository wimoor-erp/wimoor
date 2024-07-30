package com.wimoor.amazon.adv.sb.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsHsaVideo;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportKeywordsHsaMapper extends BaseMapper<AmzAdvReportKeywordsHsa>{
	void insertBatch(List<AmzAdvReportKeywordsHsa> list);
	List<Map<String,Object>> getKeywordsHsa(Map<String, Object> param);
	void insertBatchVideo(List<AmzAdvReportKeywordsHsaVideo> listVideo);
	void insertBatchBrand(List<AmzAdvReportKeywordsHsaBrand> listBrand);
	void insertBatchAttributed(List<AmzAdvReportKeywordsHsaAttributed> listAttributed);
}