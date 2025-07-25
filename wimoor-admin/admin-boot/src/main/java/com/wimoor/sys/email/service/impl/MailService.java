package com.wimoor.sys.email.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;

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
	     String[] array = to.split(",");
	     List<Address> tolist=new ArrayList<Address>();
	     for (int i = 0; i < array.length; i++) {
				String account = array[i];
				if (StrUtil.isEmpty(account)) {
					continue;
				}
	            Address  toadd = new InternetAddress(account);    
	            tolist.add(toadd);
	     }
	     if(tolist.size()==0)return;
	      Address[] toarray =  tolist.toArray(new Address[tolist.size()]);
	      // Message.RecipientType.TO属性表示接收者的类型为TO    
	      
		MimeMessage message=javaMailSender.createMimeMessage();
	    message.setRecipients(Message.RecipientType.TO,toarray);   
		MimeMessageHelper helper =new MimeMessageHelper(message,true);
		helper.setFrom(username);
		helper.setSubject(subject);
		helper.setText(content,true);
		javaMailSender.send(message);
	}catch(MessagingException e) {
		System.out.println("error");
	}
}
}
