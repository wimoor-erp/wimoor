package com.wimoor.amazon.adv.sp.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportSDPurchaseProduct;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportPurchaseProduct;
import org.apache.ibatis.annotations.Param;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductads;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvProductadsMapper extends BaseMapper<AmzAdvProductads>{
	int insertBatch(List<AmzAdvProductads> list);
	
	PageList<Map<String, Object>> getProductAdList(Map<String,Object> map, PageBounds pageBounds);
	
	List<Map<String,Object>> getProductAdChart(Map<String,Object> map);
	
	List<Map<String,Object>> getProductAdotherAsin(Map<String,Object> map);
	
	Map<String,Object> getProductAdByRemind(Map<String,Object> map);
	
	Map<String,Object> getProductAdByRemindlast(Map<String,Object> map);
	
	List<Map<String, Object>> selectByAdgroupid(@Param("profileid")BigInteger profileid,
			@Param("campaignid")BigInteger campaignid, @Param("adgroupid")BigInteger adgroupid);
	
	Map<String, Object> getSumProductAd(Map<String, Object> map);

    PageList<AmzAdvReportPurchaseProduct> getPurchaseProductAdList(Map<String, Object> map, PageBounds pageBounds);

	PageList<AmzAdvReportSDPurchaseProduct> getSDPurchaseProductAdList(Map<String, Object> map, PageBounds pageBounds);

}