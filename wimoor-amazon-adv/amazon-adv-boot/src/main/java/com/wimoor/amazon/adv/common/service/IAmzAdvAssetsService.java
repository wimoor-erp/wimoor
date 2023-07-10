package com.wimoor.amazon.adv.common.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.AmzAdvAssets;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvAssetsService extends IService<AmzAdvAssets>{
	
	List<AmzAdvAssets> amzGetStoresAssets(UserInfo user,BigInteger profileId, String brandEntityId, String mediaType);
	
	AmzAdvAssets amzCreateStoresAssets(UserInfo user,BigInteger profileId, Map<String, Object> assetParams);
	
	List<AmzAdvAssets> getAssetsForBrandEntityId(BigInteger profileId, String brandEntityId);
}
