package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.common.user.UserInfo;
/**
 * 对应API文档 Product Targeting
 * @author admin
 *
 */
public interface IAmzAdvRecommendationsService{
	/**
	 * /sp/negativeTargets/brands/search
	 * @param user
	 * @param profileId
	 * @param param
	 * @return
	 */
	public JSONArray amzAdvRecommendationsNegativeTargetsBrandsSearch(UserInfo user,BigInteger profileId,JSONObject param);
	
	/**
	 * /sp/negativeTargets/brands/recommendations
	 * @param user
	 * @param profileId
	 * @param param
	 * @return
	 */
	public JSONArray amzAdvRecommendationsNegativeTargetsBrands(UserInfo user,BigInteger profileId,JSONObject param);
	
	/**
	 * /sp/targets/categories
	 * @param user
	 * @param profileId
	 * @param param
	 * @return
	 */
	public String amzAdvRecommendationsTargetsCategories(UserInfo user,BigInteger profileId,JSONObject param);
	
	/**
	 * /sp/targets/products/recommendations
	 * @param user
	 * @param profileId
	 * @param param
	 * @return
	 */
	public String amzAdvRecommendationsTargetsProducts(UserInfo user,BigInteger profileId,JSONObject param);
	
	/**
	 * /sp/targets/keywords/recommendations
	 * @param user
	 * @param profileId
	 * @param param
	 * @return
	 */
	public String amzAdvRecommendationsTargetsKeywords(UserInfo user,BigInteger profileId,JSONObject param);
	
	/**
	 * /sp/targets/bid/recommendations
	 * @param user
	 * @param profileId
	 * @param param
	 * @return
	 */
	public String amzAdvRecommendationsTargetsBid(UserInfo user,BigInteger profileId,JSONObject param);
	public String amzAdvRecommendationsProductCount(UserInfo user,BigInteger profileId,JSONObject param);
	public String amzAdvRecommendationsTargetsCategoriesAll(UserInfo user,BigInteger profileId,JSONObject param);
	public String amzAdvRecommendationsTargetsRefine(UserInfo user,BigInteger profileId,JSONObject param) ;
	
	public String initialBudgetRecommendation(UserInfo user,BigInteger profileId,JSONObject param);
	public String budgetRulesRecommendations(UserInfo user,BigInteger profileId,String campaignId);
	public String budgetRecommendations(UserInfo user,BigInteger profileId,JSONObject param);
	public String campaignRecommendations(UserInfo user,BigInteger profileId,JSONObject param);
	
	public String budgetRulesRecommendation(AmzAdvProfile profile ,JSONObject param) ; 
}
