package com.wimoor.erp.material.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;


@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("/sayHello")
    public String list() {
    	UserInfo userinfo = UserInfoContext.get();
    	if(userinfo!=null) {    		
    		return "hello/sayHello"+userinfo.getUserinfo().get("name");
    	}else {
    		return "hello/sayHello";
    	}
    }
}
