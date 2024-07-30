package com.wimoor.amazon.adv.sp.dao;

 
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductAds;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductAdsAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductAdsAttributedSame;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvReportProductAdsMapper extends BaseMapper<AmzAdvReportProductAds>{
	void insertBatch(List<AmzAdvReportProductAds> list);
	
	List<Map<String,Object>> getProductAds(Map<String, Object> param);
	
	Map<String,Object> getWarningIndicatorForYesterday(Map<String, Object> param);
	
	Map<String,Object> getWarningIndicatorForSequent(Map<String, Object> param);
	
	Map<String,Object> getWarningIndicatorForCo(Map<String, Object> param);
	
	PageList<Map<String,Object>> getWarningIndicatorDetail(Map<String, Object> param, PageBounds pagebounds);
	
	List<Map<String,Object>> top5(BigInteger profileid);
	
	void refreshAdvertWarningData(Map<String, Object> param);

	void insertBatchAttributed(List<AmzAdvReportProductAdsAttributed> listAttributed);

	void insertBatchAttributedSame(List<AmzAdvReportProductAdsAttributedSame> listAttributedSame);

}