package com.wimoor.amazon.adv.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wimoor.amazon.adv.common.pojo.AmzAdvSerchHistory;
import com.wimoor.amazon.adv.common.service.IAmzAdvSerchHistoryService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
 

@Controller
@RequestMapping("/searchHistory")
public class AmzAdvSerchHistoryController {

 
	@Resource
	IAmzAdvSerchHistoryService amzAdvSerchHistoryService;
	
	@ResponseBody
	@RequestMapping("/getSerchHistory")
	public List<AmzAdvSerchHistory> getSerchHistoryAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String ftype = request.getParameter("ftype");
		return amzAdvSerchHistoryService.getSerchHistoryAction(user.getId(), ftype);
	}
	
	@ResponseBody
	@RequestMapping("/addSerchHistory")
	public String addSerchHistoryAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String condition = request.getParameter("condition");
		String ftype = request.getParameter("ftype");
		int i = amzAdvSerchHistoryService.addSerchHistoryAction(user.getId(), condition, ftype);
		if(i == 1) {
			return "SUCCESS"; 
		}
		else {
			return "ERROR";
		}
	}
	
	@ResponseBody
	@RequestMapping("/deleteSerchHistory")
	public String deleteSerchHistoryAction(HttpServletRequest request, Model model){
		String id = request.getParameter("id");
		int i = amzAdvSerchHistoryService.deleteSerchHistoryAction(id);
		if(i == 1) {
			return "SUCCESS"; 
		}
		else {
			return "ERROR";
		}
	}
	
}
