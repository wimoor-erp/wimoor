package com.wimoor.sys.email.service.impl;

import org.springframework.stereotype.Service;

import com.wimoor.sys.email.pojo.entity.SysMailTemplate;
import com.wimoor.sys.email.service.ISysMailManagerService;
import com.wimoor.sys.email.service.ISysMailTemplateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SysMailManagerServiceImpl implements ISysMailManagerService {
	   
		private final MailService mailService;
		private final ISysMailTemplateService iSysMailTemplateService;
		public void sendWelcome(String account){
			SysMailTemplate mail = iSysMailTemplateService.getById(2);
			mailService.sendHtmlMail(account, mail.getMailSubject(), mail.getContent());
		}
		
		public void sendCode(String subject,String account,String code){
			SysMailTemplate mail = iSysMailTemplateService.getById(1);
			String content = mail.getContent();
			content=content.replace("{code}", code);
			content=content.replace("{title}", subject);
			mailService.sendHtmlMail(account, subject,content );
		}
}
