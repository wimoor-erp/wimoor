package com.wimoor.amazon.adv.common.dao;



import org.apache.ibatis.annotations.Param;

import com.wimoor.amazon.adv.common.pojo.AmzAdvWarningDate;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvWarningDateMapper extends BaseMapper<AmzAdvWarningDate>{
	
	public AmzAdvWarningDate getWarningDateNumForFtype 
		(@Param("recordType")String recordType, @Param("ftype")String ftype, @Param("shopid")String shopid);
}