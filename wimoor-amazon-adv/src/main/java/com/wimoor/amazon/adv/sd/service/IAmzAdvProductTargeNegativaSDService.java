package com.wimoor.amazon.adv.sd.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeNegativaSD;
import com.wimoor.common.user.UserInfo;
 

public interface IAmzAdvProductTargeNegativaSDService  extends IService<AmzAdvProductTargeNegativaSD>{

	PageList<Map<String, Object>> getProductNegativaTargeList(Map<String, Object> map, PageBounds pageBounds);
	public List<AmzAdvProductTargeNegativaSD> amzListNegativeTargetingClauses(UserInfo user,BigInteger  profileId, Map<String, Object> param);
	public List<AmzAdvProductTargeNegativaSD> amzUpdateNegativeTargetingClauses_V3(UserInfo user,BigInteger  profileId,
			List<AmzAdvProductTargeNegativaSD> productTargeNegativaList);
	public List<AmzAdvProductTargeNegativaSD> amzCreateNegativeTargetingClauses_V3(UserInfo user,BigInteger  profileId,
			List<AmzAdvProductTargeNegativaSD> productTargeList) ;
	public AmzAdvProductTargeNegativaSD amzArchiveNegativeTargetingClause_V3(UserInfo user,BigInteger  profileId, String targetId) ;
}
