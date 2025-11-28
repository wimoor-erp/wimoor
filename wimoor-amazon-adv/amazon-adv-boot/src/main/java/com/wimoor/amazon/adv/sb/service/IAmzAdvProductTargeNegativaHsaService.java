package com.wimoor.amazon.adv.sb.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvProductTargeNegativaHsa;
import com.wimoor.common.user.UserInfo;

public interface IAmzAdvProductTargeNegativaHsaService extends IService<AmzAdvProductTargeNegativaHsa>{
	
	public List<AmzAdvProductTargeNegativaHsa> amzCreateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvProductTargeNegativaHsa> productTargeList);
	
	public List<AmzAdvProductTargeNegativaHsa> amzUpdateNegativeTargetingClauses(UserInfo user,BigInteger  profileId,List<AmzAdvProductTargeNegativaHsa> productTargeList);
	
	public AmzAdvProductTargeNegativaHsa amzArchiveNegativeTargetingClause(UserInfo user,BigInteger  profileId,String targetId);

	AmzAdvProductTargeNegativaHsa amzNegativeTargetingClauses(UserInfo user, BigInteger profileId, String id);

	public List<AmzAdvProductTargeNegativaHsa>  amzUpdateNegativeTargetingClauses(UserInfo user, String profileid, JSONArray keywords);

	public List<AmzAdvProductTargeNegativaHsa>  amzCreateNegativeTargetingClauses(UserInfo user, String profileid, JSONArray keywords);

	public PageList<Map<String, Object>> getProductNegativaTargeList(Map<String, Object> map, PageBounds pageBounds);
}
