package com.wimoor.erp.material.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
 
@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/list")
    public String list() {
    	UserInfo userinfo = UserInfoContext.get();
        return "order/list"+userinfo;
    }

}
