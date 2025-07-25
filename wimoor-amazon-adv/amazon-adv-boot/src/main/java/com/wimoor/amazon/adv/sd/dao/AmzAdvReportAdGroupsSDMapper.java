package com.wimoor.amazon.adv.sd.dao;

import java.util.List;

import com.wimoor.amazon.adv.sd.pojo.*;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportAdGroupsSDMapper extends BaseMapper<AmzAdvReportAdGroupsSD>{
 
	void insertBatch(List<AmzAdvReportAdGroupsSD> list);

	void insertBatchAttributed(List<AmzAdvReportAdgroupsSDAttributedAll> listAttributed);

}