package com.wimoor.amazon.adv.sd.dao;

import java.util.List;

import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportAsinsSD;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportAsinsSDMapper extends BaseMapper<AmzAdvReportAsinsSD>{

	void insertBatch(List<AmzAdvReportAsinsSD> list);
 
}