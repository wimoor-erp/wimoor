package com.wimoor.amazon.adv.sb.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportProductTargetsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportProductTargetsHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportProductTargetsHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportProductTargetsHsaVideo;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportProductTargeHsaMapper extends BaseMapper<AmzAdvReportProductTargetsHsa>{
	void insertBatch(List<AmzAdvReportProductTargetsHsa> list);
	
	List<Map<String,Object>> getTargetHsaReport(Map<String, Object> param);

	void insertBatchAttributed(List<AmzAdvReportProductTargetsHsaAttributed> listAttributed);

	void insertBatchVideo(List<AmzAdvReportProductTargetsHsaVideo> listVideo);

	void insertBatchBrand(List<AmzAdvReportProductTargetsHsaBrand> listBrand);
}