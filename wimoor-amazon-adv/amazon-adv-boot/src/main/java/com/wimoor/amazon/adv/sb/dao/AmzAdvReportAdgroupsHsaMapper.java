package com.wimoor.amazon.adv.sb.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportAdgroupsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportAdgroupsHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportAdgroupsHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportAdgroupsHsaVideo;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportAdgroupsHsaMapper extends BaseMapper<AmzAdvReportAdgroupsHsa>{
	void insertBatch(List<AmzAdvReportAdgroupsHsa> list);
	void insertBatchBrand(List<AmzAdvReportAdgroupsHsaBrand> list);
	void insertBatchVideo(List<AmzAdvReportAdgroupsHsaVideo> list);
	void insertBatchAttributed(List<AmzAdvReportAdgroupsHsaAttributed> listAttributed);
	List<Map<String,Object>> getAdgroupsHsa(Map<String, Object> param);
}