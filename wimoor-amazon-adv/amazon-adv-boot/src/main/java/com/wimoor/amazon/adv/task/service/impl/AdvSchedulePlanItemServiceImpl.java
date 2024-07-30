package com.wimoor.amazon.adv.task.service.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.task.dao.AmzAdvSchedulePlanItemMapper;
import com.wimoor.amazon.adv.task.pojo.AmzAdvSchedulePlanItem;
import com.wimoor.amazon.adv.task.service.IAdvSchedulePlanItemService;
import com.wimoor.amazon.base.BaseService;
 

@Service("advSchedulePlanItemService")
public class AdvSchedulePlanItemServiceImpl extends BaseService<AmzAdvSchedulePlanItem> implements IAdvSchedulePlanItemService{
	@Resource
	AmzAdvSchedulePlanItemMapper amzAdvSchedulePlanItemMapper;
	
	public List<Map<String, Object>> findPlanIdForProfile(BigInteger profileid) {
		// TODO Auto-generated method stub
		return amzAdvSchedulePlanItemMapper.findPlanIdForProfile(profileid);
	}

}
