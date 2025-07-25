package com.wimoor.amazon.adv.common.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.AmzAdvPortfolios;
import com.wimoor.common.user.UserInfo;
 

public interface IAmzAdvPortfoliosService extends IService<AmzAdvPortfolios>{
	
	List<AmzAdvPortfolios> getPortfoliosForProfileId(Map<String, Object> param);
	
	List<AmzAdvPortfolios> amzGetListPortfolios(UserInfo user, BigInteger profileId, Map<String, Object> portfoliosParam);
	
	List<AmzAdvPortfolios> amzGetListPortfoliosEx(UserInfo user, BigInteger profileId, Map<String, Object> portfoliosParam);

	AmzAdvPortfolios amzGetPortfolios(UserInfo user, BigInteger profileId, String id);
	
	AmzAdvPortfolios amzGetPortfoliosEx(UserInfo user, BigInteger profileId, String id);
	
	List<AmzAdvPortfolios> amzCreatePortfolios(UserInfo user,BigInteger  profileId, List<AmzAdvPortfolios> portfoliosList);
	
	List<AmzAdvPortfolios> amzUpdatePortfolios(UserInfo user,BigInteger  profileId,  List<AmzAdvPortfolios> portfoliosList);
}
