package com.wimoor.amazon.adv.common.service;




import com.wimoor.amazon.adv.common.pojo.AmzAdvWarningDate;
 

public interface IAmzAdvWarningDateService extends IService<AmzAdvWarningDate>{

	public AmzAdvWarningDate getWarningDateNumForFtype(String recordType, String ftype, String shopid);
	
	public int saveAmzAdvWarningDate(AmzAdvWarningDate amzAdvWarningDate);
}
