package com.wimoor.amazon.adv.common.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wimoor.amazon.adv.common.dao.AmzAdvWarningDateMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvWarningDate;
import com.wimoor.amazon.adv.common.service.IAmzAdvWarningDateService;
import com.wimoor.amazon.base.BaseService;
 

@Service("amzAdvWarningDateService")
public class AmzAdvWarningDateServiceImpl extends BaseService<AmzAdvWarningDate> implements IAmzAdvWarningDateService{
	@Resource
	AmzAdvWarningDateMapper amzAdvWarningDateMapper;
	
	public AmzAdvWarningDate getWarningDateNumForFtype(String recordType, String ftype, String shopid) {
		return amzAdvWarningDateMapper.getWarningDateNumForFtype(recordType, ftype, shopid);
	}
	
	public int saveAmzAdvWarningDate(AmzAdvWarningDate amzAdvWarningDate) {
		AmzAdvWarningDate oldAmzAdvWarningDate = amzAdvWarningDateMapper.selectByPrimaryKey(amzAdvWarningDate);
		if(oldAmzAdvWarningDate != null) {
			return amzAdvWarningDateMapper.updateByPrimaryKey(amzAdvWarningDate);
		}else {
			return amzAdvWarningDateMapper.insert(amzAdvWarningDate);
		}
	}
	
}
