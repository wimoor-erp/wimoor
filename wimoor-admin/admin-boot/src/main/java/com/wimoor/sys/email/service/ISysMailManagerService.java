package com.wimoor.sys.email.service;

public interface ISysMailManagerService {
	void sendWelcome(String account);
	void sendCode(String subject,String account,String code);
}
