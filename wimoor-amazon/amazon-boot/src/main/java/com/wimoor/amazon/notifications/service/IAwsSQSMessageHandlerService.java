package com.wimoor.amazon.notifications.service;

import com.alibaba.fastjson.JSONObject;

public interface IAwsSQSMessageHandlerService {

	boolean handlerMessage(JSONObject body);

}
