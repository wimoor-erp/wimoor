package com.wimoor.amazon.adv.report.service;

 

import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.pojo.AmzAdvReportRequestType;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;

public interface IAmzAdvReportTreatService {
	void treatReport( AmzAdvProfile profile, AmzAdvRequest request,JSONReader a);
	void requestReport(AmzAdvAuth advauth, AmzAdvProfile profile, AmzAdvReportRequestType type);
	}
