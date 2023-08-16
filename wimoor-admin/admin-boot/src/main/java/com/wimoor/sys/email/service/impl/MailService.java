package com.wimoor.sys.email.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailService {
@Autowired
JavaMailSender javaMailSender;

@Value("${spring.mail.username}")
String username;

public void sendSimpleMail(String to,String cc,String subject,String content) {
	SimpleMailMessage simpMsg=new SimpleMailMessage();
	simpMsg.setFrom(username);
	simpMsg.setTo(to);
	simpMsg.setCc(cc);
	simpMsg.setSubject(subject);
	simpMsg.setText(content);
	javaMailSender.send(simpMsg);
}
	
public void sendHtmlMail(String to,String subject,String content) {
	try {
		MimeMessage message=javaMailSender.createMimeMessage();
		MimeMessageHelper helper =new MimeMessageHelper(message,true);
		helper.setTo(to);
		helper.setFrom(username);
		helper.setSubject(subject);
		helper.setText(content,true);
		javaMailSender.send(message);
	}catch(MessagingException e) {
		System.out.println("error");
	}
}
}
