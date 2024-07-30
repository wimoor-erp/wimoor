package com.wimoor.amazon.adv.report.dao;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.report.pojo.AmzAdvSumProductAds;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSumProductAdsKey;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvSumProductAdsMapper extends BaseMapper<AmzAdvSumProductAds>{
	  AmzAdvSumProductAds selectByKey(AmzAdvSumProductAdsKey key);
	  int updateByKey(AmzAdvSumProductAds record);
/*    int deleteByPrimaryKey(AmzAdvSumProductAdsKey key);

    int insert(AmzAdvSumProductAds record);

    int insertSelective(AmzAdvSumProductAds record);

    int updateByPrimaryKeySelective(AmzAdvSumProductAds record);

    int updateByPrimaryKey(AmzAdvSumProductAds record);*/
	  
	List<Map<String, Object>> getSumProduct(Map<String,Object> param);
	List<Map<String, Object>> getDaysSumProduct(Map<String,Object> param);
	List<Map<String, Object>> getMonthSumProduct(Map<String, Object> param);
	Map<String, Object> getAllSumProduct(Map<String, Object> map);
}