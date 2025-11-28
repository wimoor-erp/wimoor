package com.wimoor.amazon.adv.sd.dao;

import java.util.List;

import com.wimoor.amazon.adv.sd.pojo.*;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportProductTargeSDMapper extends BaseMapper<AmzAdvReportProductTargetsSD>{

	void insertBatch(List<AmzAdvReportProductTargetsSD> list);

	void insertBatchAttributed(List<AmzAdvReportProductTargetsSDAttributedAll> listAttributed);
     
}