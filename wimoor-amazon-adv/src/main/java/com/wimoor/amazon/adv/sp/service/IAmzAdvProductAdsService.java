package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductads;
import com.wimoor.common.user.UserInfo;

import tk.mybatis.mapper.entity.Example;


public interface IAmzAdvProductAdsService extends IService<AmzAdvProductads>{
	public List<AmzAdvProductads> amzGetProductAdBysku(UserInfo user,BigInteger profileId,String sku);
	
	public List<AmzAdvProductads> amzGetProductAdByasin(UserInfo user,BigInteger profileId,String asin);
	
	public AmzAdvProductads amzGetProductAd(UserInfo user,BigInteger profileId,String adId);
	
	public AmzAdvProductads amzGetProductAdEx(UserInfo user,BigInteger profileId,String adId);
	
	public List<AmzAdvProductads> amzCreateProductAds(UserInfo user,BigInteger profileId,List<AmzAdvProductads> productAdsList);
	
	public List<AmzAdvProductads> amzUpdateProductAds(UserInfo user,BigInteger profileId,List<AmzAdvProductads> productAdsList);
	
	public AmzAdvProductads amzArchiveProductAd(UserInfo user,BigInteger profileId,String adId);
	
	public List<AmzAdvProductads> amzListProductAds(UserInfo user,BigInteger profileId,Map<String,Object> param);
	
	public List<AmzAdvProductads> amzListProductAdsEx(UserInfo user,BigInteger profileId,Map<String,Object> param);
	
	public List<Map<String,Object>> amzGetProductInfoByAsin(BigInteger profileid,List<String> asinList);
	
	public List<Map<String,Object>> ListMatchingProducts(BigInteger profileid,String name);

	public List<AmzAdvProductads> getProductadByAdgroupId(UserInfo user, BigInteger bigInteger, String adgroupid);
	
	public PageList<Map<String, Object>> getProductAdList(Map<String,Object> map, PageBounds pageBounds);
	
	public Map<String,Object> getProductAdChart(Map<String,Object> map);
	
	public List<Map<String, Object>> getProductAdotherAsin(Map<String, Object> map);
	
	public List<Map<String, Object>> selectByAdgroupid(BigInteger profileids, BigInteger campaignids, BigInteger adgroupids);
	
	public AmzAdvProductads selectOne(AmzAdvProductads amzAdvProductads);
	
	public AmzAdvProductads selectOneByExample(Example example);
	
	public List<Map<String, Object>> getSumProductAd(Map<String, Object> map);

}
