package com.wimoor.amazon.adv.sp.dao;

import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportPlacement;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportPlacementMapper extends BaseMapper<AmzAdvReportPlacement>{
	   int myinsert(String name);
	   AmzAdvReportPlacement selectByPlacement(String name);
}