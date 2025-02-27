package com.wimoor.amazon.adv.sp.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductTargeNegativa;
import com.wimoor.common.user.UserInfo;
 

public interface IAmzAdvProductTargeNegativaService extends IService<AmzAdvProductTargeNegativa>{
	
	
	public List<AmzAdvProductTargeNegativa> amzCreateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvProductTargeNegativa> productTargeList) ;
	
	public List<AmzAdvProductTargeNegativa> amzCreateNegativeTargetingClauses(UserInfo user,String  profileid,JSONArray keywords);
	
	public List<AmzAdvProductTargeNegativa> amzUpdateNegativeTargetingClauses(UserInfo user, String profileid, JSONArray keywords);
	
	public List<AmzAdvProductTargeNegativa> amzUpdateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvProductTargeNegativa> productTargeList);
	
	public List<AmzAdvProductTargeNegativa> amzListNegativeTargetingClauses(UserInfo user,BigInteger  profileId,JSONObject param);
	
	public List<AmzAdvProductTargeNegativa> amzArchiveNegativeTargetingClause(UserInfo user,BigInteger  profileId, JSONObject param);
	
	public AmzAdvProductTargeNegativa getNegativaTargetforQuery(Map<String,Object> map);
	
	public PageList<Map<String,Object>> getProductNegativaTargeList(Map<String,Object> map, PageBounds pageBounds);
}
