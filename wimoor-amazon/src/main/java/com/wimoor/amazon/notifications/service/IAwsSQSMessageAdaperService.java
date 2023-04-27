package com.wimoor.amazon.notifications.service;


public interface IAwsSQSMessageAdaperService {
	boolean handlerMessage(String body);
}
