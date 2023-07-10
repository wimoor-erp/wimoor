package com.wimoor.amazon.adv.common.service;

import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportPlacement;
 

public interface IAmzAdvReportPlacementService   extends IService<AmzAdvReportPlacement>{
	AmzAdvReportPlacement loadIDByPlacementName(String name);
}
