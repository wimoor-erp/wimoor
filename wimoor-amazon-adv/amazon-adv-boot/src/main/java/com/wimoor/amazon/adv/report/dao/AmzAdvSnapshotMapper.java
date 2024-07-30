package com.wimoor.amazon.adv.report.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wimoor.amazon.adv.report.pojo.AmzAdvSnapshot;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvSnapshotMapper extends BaseMapper<AmzAdvSnapshot> {

	AmzAdvSnapshot selectByProfileAndType(@Param("profileid") String profileid, @Param("recordType") String recordType,
			@Param("campaignType") String campaignType);

	//处理状态为空或者error,处理次数小于3的记录,请求时间大于15分钟
	List<AmzAdvSnapshot> selectAvailableByAdType(@Param("recordType")String recordType, @Param("campaignType")String campaignType);
}