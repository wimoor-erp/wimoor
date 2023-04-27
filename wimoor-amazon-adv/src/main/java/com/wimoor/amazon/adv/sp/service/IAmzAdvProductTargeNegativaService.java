package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.common.user.UserInfo;
 

public interface IAmzAdvProductTargeNegativaService extends IService<AmzAdvProductTargeNegativa>{
	
	public AmzAdvProductTargeNegativa amzGetNegativeTargetingClause(UserInfo user,BigInteger  profileId,String targetId);
	
	public AmzAdvProductTargeNegativa amzGetNegativeTargetingClauseEx(UserInfo user,BigInteger  profileId,String targetId);
	
	public List<AmzAdvProductTargeNegativa> amzCreateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvProductTargeNegativa> productTargeList);
	
	public List<AmzAdvProductTargeNegativa> amzUpdateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvProductTargeNegativa> productTargeList);
	
	public AmzAdvProductTargeNegativa amzArchiveNegativeTargetingClause(UserInfo user,BigInteger  profileId,String targetId);
	
	public List<AmzAdvProductTargeNegativa> amzListNegativeTargetingClauses(UserInfo user,BigInteger  profileId,Map<String,Object> param);
	
	public List<AmzAdvProductTargeNegativa> amzListNegativeTargetingClausesEx(UserInfo user,BigInteger  profileId,Map<String,Object> param);
	
	public List<AmzAdvProductTargeNegativa> amzCreateNegativeTargetingClauses_V3Hsa(UserInfo user,BigInteger  profileId,List<AmzAdvProductTargeNegativa> productTargeList);
	
	public List<AmzAdvProductTargeNegativa> amzUpdateNegativeTargetingClauses_V3Hsa(UserInfo user,BigInteger  profileId,List<AmzAdvProductTargeNegativa> productTargeList);
	
	public AmzAdvProductTargeNegativa amzArchiveNegativeTargetingClause_V3Hsa(UserInfo user,BigInteger  profileId,String targetId);
	
	public AmzAdvProductTargeNegativa getNegativaTargetforQuery(Map<String,Object> map);
	
	public PageList<Map<String,Object>> getProductNegativaTargeList(Map<String,Object> map, PageBounds pageBounds);
}
