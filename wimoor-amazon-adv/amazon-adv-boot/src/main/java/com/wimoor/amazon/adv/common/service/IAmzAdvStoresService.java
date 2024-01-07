package com.wimoor.amazon.adv.common.service;

import java.math.BigInteger;
import java.util.List;

import com.wimoor.amazon.adv.common.pojo.AmzAdvStores;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvStoresService extends IService<AmzAdvStores>{
	
	List<AmzAdvStores> getStoresForProfileId(BigInteger profileId,String entityid);

	List<AmzAdvStores> amzGetlistStores(UserInfo user,BigInteger profileId);
	
	List<AmzAdvStores> amzGetStoresBybrandEntityId(UserInfo user,BigInteger profileId, String brandEntityId);
	
	List<String> amzGetPageAsins(UserInfo user,BigInteger profileId, String storePageUrl);
}
