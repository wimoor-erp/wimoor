package com.wimoor.amazon.adv.sd.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvProductAdsSDService  extends IService<AmzAdvProductadsSD>{

	PageList<Map<String, Object>> getProductAdList(Map<String, Object> map, PageBounds pageBounds);

	Map<String, Object> getSumProductAd(Map<String, Object> map);
	
	public List<AmzAdvProductadsSD> amzCreateProductAds(UserInfo user,BigInteger profileId, List<AmzAdvProductadsSD> productAdsList) ;

	AmzAdvProductadsSD amzArchiveProductAd(UserInfo user, BigInteger bigInteger, String adId);

	List<AmzAdvProductadsSD>  amzUpdateProductAds(UserInfo user, BigInteger key, List<AmzAdvProductadsSD> list);

	List<AmzAdvProductadsSD> getProductadByAdgroupId(UserInfo user, BigInteger bigInteger, String adgroupid);

	List<AmzAdvProductadsSD> amzUpdateProductAds(UserInfo user, String profileid, JSONArray productAdArray);

	List<AmzAdvProductadsSD> amzCreateProductAds(UserInfo user, String profileid, JSONArray productAdArray);

	AmzAdvProductadsSD amzListProductAds(UserInfo user, BigInteger profileId, String adid);
}
