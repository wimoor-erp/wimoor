package com.wimoor.amazon.adv.sb.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdsHsa;
import com.wimoor.common.user.UserInfo;


public interface IAmzAdvAdsHsaService extends IService<AmzAdvAdsHsa> {
	List<AmzAdvAdsHsa> createAds(UserInfo user, String profileid, String adsType, JSONObject params);
	List<AmzAdvAdsHsa>  updateAds(UserInfo user, String profileid, JSONObject adsArray);
	List<AmzAdvAdsHsa> amzGetAdsList(AmzAdvProfile profile, JSONObject adgroupParam);
	List<AmzAdvAdsHsa> amzDeleteAdsList(UserInfo user, AmzAdvProfile profile, JSONObject adgroupParam);
	PageList<Map<String, Object>> getAdsList(Map<String, Object> map, PageBounds pageBounds);
	Map<String, Object> getSumAds(Map<String, Object> map);
	List<Map<String, Object>> getAdsChart(Map<String, Object> map);
}
