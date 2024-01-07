package com.wimoor.amazon.profit.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.wimoor.common.user.UserInfo;

 

public interface IProfitParameterService {

	Map<String, String> findInplacefee(String country);
	
	Map<String, BigDecimal> findManualProcessingFee();
	
	List<BigDecimal> getStorageFeeMap(String country);
	 
	Map<String, String> findNoBindPointNameByGroup(UserInfo user, String groupid);
	
}
