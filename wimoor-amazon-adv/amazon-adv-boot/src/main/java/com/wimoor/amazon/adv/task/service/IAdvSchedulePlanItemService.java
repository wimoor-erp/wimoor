package com.wimoor.amazon.adv.task.service;


import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.task.pojo.AmzAdvSchedulePlanItem;

public interface IAdvSchedulePlanItemService extends IService<AmzAdvSchedulePlanItem>{
	List<Map<String, Object>> findPlanIdForProfile(BigInteger profileid); 
	
}
