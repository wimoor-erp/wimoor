package com.wimoor.amazon.adv.report.service;

import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportPlacement;
 

public interface IAmzAdvReportPlacementService   extends IService<AmzAdvReportPlacement>{
	AmzAdvReportPlacement loadIDByPlacementName(String name);
}
