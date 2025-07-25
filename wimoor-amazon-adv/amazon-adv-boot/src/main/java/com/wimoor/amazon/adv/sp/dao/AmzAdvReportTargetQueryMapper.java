package com.wimoor.amazon.adv.sp.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportTargetQuery;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportTargetQueryAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportTargetQueryAttributedSame;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportTargetQueryMapper extends BaseMapper<AmzAdvReportTargetQuery>{
	
	void insertBatch(List<AmzAdvReportTargetQuery> list);
	
	List<Map<String,Object>> getTargetQueryReport(Map<String, Object> param);
	
	PageList<Map<String, Object>> getProductTargeQueryList(Map<String, Object> map, PageBounds pageBounds);
	
	List<Map<String,Object>> getProductTargeQueryChart(Map<String, Object> map);

   void insertBatchAttributed(List<AmzAdvReportTargetQueryAttributed> listAttributed);

void insertBatchAttributedSame(List<AmzAdvReportTargetQueryAttributedSame> listAttributedSame);
   
}