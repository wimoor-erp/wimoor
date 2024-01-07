package com.wimoor.amazon.adv.common.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAssets;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvAssetsService extends IService<AmzAdvAssets>{
	
	public String amzAssetsUpload(UserInfo user,BigInteger profileId, JSONObject param) ;
	
	public String amzAssetsSearch(UserInfo user,BigInteger profileId, JSONObject param) ;
	
	public String amzAssetsRegister(UserInfo user,BigInteger profileId, JSONObject param) ;
	
	public String amzAssets(UserInfo user,BigInteger profileId);
	
	List<AmzAdvAssets> amzGetStoresAssets(UserInfo user,BigInteger profileId, String brandEntityId, String mediaType);
	
	AmzAdvAssets amzCreateStoresAssets(UserInfo user,BigInteger profileId, Map<String, Object> assetParams);
	
	List<AmzAdvAssets> getAssetsForBrandEntityId(BigInteger profileId, String brandEntityId);
}
