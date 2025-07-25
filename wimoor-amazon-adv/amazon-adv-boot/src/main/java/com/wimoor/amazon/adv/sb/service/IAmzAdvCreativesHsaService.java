package com.wimoor.amazon.adv.sb.service;

import java.math.BigInteger;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.user.UserInfo;

/**
 * 对应API文档 Product Targeting
 * @author admin
 *
 */
public interface IAmzAdvCreativesHsaService{
	public String updatesCreatives(UserInfo user,BigInteger profileId,JSONObject myParam);
	public String createBrandVideo(UserInfo user,BigInteger profileId,JSONObject myParam);
	public String createVideo(UserInfo user,BigInteger profileId,JSONObject myParam) ;
	public String createProductCollection(UserInfo user,BigInteger profileId,JSONObject myParam);
	public String createStoreSpotlight(UserInfo user,BigInteger profileId,JSONObject myParam);
	public String getListOfCreatives(UserInfo user,BigInteger profileId,JSONObject myParam);

}
