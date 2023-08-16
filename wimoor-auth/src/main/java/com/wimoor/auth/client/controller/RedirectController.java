package com.wimoor.auth.client.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@EnableAutoConfiguration
@Controller
public class RedirectController {
	
	@RequestMapping("/")
	String showLoginAction(HttpServletRequest request,HttpServletResponse response, Model model) {
			return "open1688";  
	}
 
}
