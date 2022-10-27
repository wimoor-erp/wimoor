package com.wimoor.amazon.adv.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wimoor.amazon.adv.common.pojo.AmzRegion;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzRegionMapper extends BaseMapper<AmzRegion>{
	
	List<Map<String, Object>> getNoBindAdvRegion(@Param("shopid") String shopid, @Param("groupid") String groupid);
}