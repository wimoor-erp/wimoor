package com.wimoor.sys.email.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.admin.pojo.dto.SysEmailDTO;
import com.wimoor.admin.pojo.entity.SysCompany;
import com.wimoor.admin.service.ISysCompanyService;
import com.wimoor.common.result.Result;
import com.wimoor.sys.email.service.impl.MailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "邮件处理")
@RestController
@RequestMapping("/api/v1/email")
@Slf4j
@RequiredArgsConstructor
public class SendEmailController {
	 private final ISysCompanyService iSysCompanyService;
	 private final MailService mailService;
	    /**
	     * 提供用于用户登录认证信息
	     */
	    @ApiOperation(value = "根据登录用户ID获取用户名称等信息")
	    @GetMapping("/sendSimple")
	    public Result<?> sendSimpleMail(String to,String cc,String subject,String content) {
	    	mailService.sendSimpleMail(to, cc, subject,content);
	    	return Result.success();
	    }
	    
	    /**
	     * 提供用于用户登录认证信息
	     */
	    @ApiOperation(value = "根据登录用户ID获取用户名称等信息")
	    @PostMapping("/sendBoss")
	    public Result<?> sendBoss(@RequestBody SysEmailDTO dto) {
	    	SysCompany company = iSysCompanyService.getById(dto.getToCompany());
	    	String bossEmail = company.getBossEmail();
	    	log.debug("sendEmail");
	    	mailService.sendHtmlMail(bossEmail,  dto.getSubject(),dto.getContent());
	    	return Result.success();
	    }
	    
	    
}
