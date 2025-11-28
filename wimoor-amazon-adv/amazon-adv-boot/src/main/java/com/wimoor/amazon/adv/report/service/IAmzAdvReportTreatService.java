package com.wimoor.amazon.adv.report.service;

 

import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.pojo.AmzAdvReportRequestType;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;

import java.util.Date;

public interface IAmzAdvReportTreatService {
	void treatReport( AmzAdvProfile profile, AmzAdvRequest request,JSONReader a);
	void requestReport(AmzAdvAuth advauth, AmzAdvProfile profile, AmzAdvReportRequestType type);
	void requestProductAdsReport(AmzAdvAuth advauth, AmzAdvProfile profile, AmzAdvReportRequestType type);
	void requestReportParamBuildV3(AmzAdvAuth advauth, AmzAdvProfile profile, AmzAdvReportRequestType type, Date startDate, Date endDate, Boolean isold) ;
	}
