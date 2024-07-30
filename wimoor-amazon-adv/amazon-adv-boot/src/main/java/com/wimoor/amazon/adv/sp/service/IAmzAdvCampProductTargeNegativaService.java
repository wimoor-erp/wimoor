package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampProductTargeNegativa;
import com.wimoor.common.user.UserInfo;
 

public interface IAmzAdvCampProductTargeNegativaService extends IService<AmzAdvCampProductTargeNegativa>{
	
	public List<AmzAdvCampProductTargeNegativa> amzCreateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvCampProductTargeNegativa> productTargeList) ;
	
	public List<AmzAdvCampProductTargeNegativa> amzCreateNegativeTargetingClauses(UserInfo user,String  profileid,JSONArray keywords);
	
	public List<AmzAdvCampProductTargeNegativa> amzUpdateNegativeTargetingClauses(UserInfo user, String profileid, JSONArray keywords);
	
	public List<AmzAdvCampProductTargeNegativa> amzUpdateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvCampProductTargeNegativa> productTargeList);
	
	public List<AmzAdvCampProductTargeNegativa> amzListNegativeTargetingClauses(UserInfo user,BigInteger  profileId,JSONObject param);
	
	public AmzAdvCampProductTargeNegativa getNegativaTargetforQuery(Map<String,Object> map);

	public PageList<Map<String, Object>> getProductNegativaTargeList(Map<String, Object> map, PageBounds pageBounds);

	public AmzAdvCampProductTargeNegativa amzArchiveNegativeTargetingClause(UserInfo user, BigInteger bigInteger,String id);
	
 
}
