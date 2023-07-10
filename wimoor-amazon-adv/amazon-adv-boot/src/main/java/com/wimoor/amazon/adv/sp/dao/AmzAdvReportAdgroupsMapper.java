package com.wimoor.amazon.adv.sp.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportAdgroups;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportAdgroupsAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportAdgroupsAttributedSame;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportAdgroupsMapper extends BaseMapper<AmzAdvReportAdgroups>{

	void insertBatch(List<AmzAdvReportAdgroups> list);
	List<Map<String,Object>> getAdgroups(Map<String, Object> param);
	List<Map<String,Object>> top5(BigInteger profileid);
	void insertBatchAttributed(List<AmzAdvReportAdgroupsAttributed> listAttributed);
	void insertBatchAttributedSame(List<AmzAdvReportAdgroupsAttributedSame> listAttributedSame);
}