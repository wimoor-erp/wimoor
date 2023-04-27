package com.wimoor.amazon.adv.common.service;

import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.AmzAdvRequest;

public interface IAmzAdvHttpClientResponseHandler {
	public Boolean treatReport(AmzAdvProfile profile, AmzAdvRequest request,JSONReader	 jsonReader) ;
}
