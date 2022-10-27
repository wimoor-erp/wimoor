package com.wimoor.amazon.adv.common.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvSBMedia;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvBrandService extends IService<AmzAdvBrand>{
	
	List<AmzAdvBrand> amzGetBrands(UserInfo user,BigInteger profileId);
	
	List<AmzAdvBrand> getBrandForProfileId(BigInteger profileId, String brandEntityId);

	JSONObject getMediaUrl(BigInteger bigInteger);

	JSONObject completeMedia(UserInfo user,Map<String,String> param);

	JSONObject mediaDescribe(UserInfo user, Map<String, String> param);

	AmzAdvSBMedia loadOldMedia(UserInfo user, Map<String, String> param);
}
