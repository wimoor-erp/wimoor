package com.wimoor.amazon.adv.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.task.service.IAdvSchedulePlanItemService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import tk.mybatis.mapper.util.StringUtil;

@Controller
@RequestMapping("/advSchedule")
public class AdvertScheduleManageController {
 
 
 
	@Resource
	IAdvSchedulePlanItemService advSchedulePlanItemService;

	@ResponseBody
	@RequestMapping("/addplan")
	public String addSchedulePlanAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String campaignId = request.getParameter("campaignId");
		String adGroupId = request.getParameter("adGroupId");
		String adId = request.getParameter("adId");
		String keywordId = request.getParameter("keywordId");
		String profileid = request.getParameter("profileid");
		String taskName = request.getParameter("taskName");
		String recordType = request.getParameter("recordType");
		String campaignType = request.getParameter("campaignType");
		String type = request.getParameter("type");
		String weekdays = request.getParameter("weekdays");
		String startDatestr = request.getParameter("startDate");
		String range = request.getParameter("range");
		String bid = request.getParameter("bid");
		String status = request.getParameter("status");
		String country = request.getParameter("country");
		String remark = request.getParameter("remark");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("profileid", profileid);
		map.put("campaignId", campaignId);
		map.put("adGroupId", adGroupId);
		map.put("adId", adId);
		map.put("keywordId", keywordId);
		map.put("taskName", taskName);
		map.put("recordType", recordType);
		map.put("campaignType", campaignType);
		map.put("type", type);
		map.put("weekdays", weekdays);
		map.put("range", range);
        map.put("startDatestr", startDatestr );
		map.put("bid", bid);
		map.put("status", status);
		map.put("remark", remark);
		map.put("country", country);
		map.put("userId", user.getId());
	  
		return "SUCESS";
	}

	@ResponseBody
	@RequestMapping("/updateplan")
	public String updateSchedulePlanAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String planId = request.getParameter("planId");
		String taskName = request.getParameter("taskName");
		String campaignId = request.getParameter("campaignId");
		String adGroupId = request.getParameter("adGroupId");
		String adId = request.getParameter("adId");
		String keywordId = request.getParameter("keywordId");
		BigInteger profileid = null;
		if(request.getParameter("profileid") != null) {
			profileid = new BigInteger(request.getParameter("profileid"));
		}
		String recordType = request.getParameter("recordType");
		String campaignType = request.getParameter("campaignType");
		String type = request.getParameter("type");
		String weekdays = request.getParameter("weekdays");
		String startDatestr = request.getParameter("startDate");
		String range = request.getParameter("range");
		String bid = request.getParameter("bid");
		String status = request.getParameter("status");
		String planStatus = request.getParameter("planStatus");
		String country = request.getParameter("country");
		String remark = request.getParameter("remark");
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("startDatestr", startDatestr );
		map.put("planId", planId);
		map.put("profileid", profileid);
		map.put("taskName", taskName);
		map.put("campaignId", campaignId);
		map.put("adGroupId", adGroupId);
		map.put("adId", adId);
		map.put("range", range);
		map.put("keywordId", keywordId);
		map.put("recordType", recordType);
		map.put("campaignType", campaignType);
		map.put("type", type);
		map.put("weekdays", weekdays);
		map.put("bid", bid);
		map.put("status", status);
		map.put("planStatus", planStatus);
		map.put("country", country);
		map.put("remark", remark);
		map.put("userId", user.getId());
		
	 
		return "SUCCESS";
	}
	
	@ResponseBody
	@RequestMapping("/deletePlan")
	public String deletePlanAction(HttpServletRequest request, Model model) {
		 
		return "SUCCESS";
	}
	
	@ResponseBody
	@RequestMapping("/changePlanStatus")
	public String changeSchedulePlanAction(HttpServletRequest request, Model model) {
		String planId = request.getParameter("planId");
		String status = request.getParameter("status");
		String type = request.getParameter("type");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("planId", planId);
		map.put("status", status);
		map.put("type", type);
		return "SUCCESS";
	}
	
	@ResponseBody
	@RequestMapping("/changePlanRemark")
	public String changePlanRemarkAction(HttpServletRequest request, Model model) {
		String planId = request.getParameter("planId");
		String remark = request.getParameter("remark");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("planId", planId);
		map.put("remark", remark);
	 
		return "SUCCESS";
	}

	@ResponseBody
	@RequestMapping("/getSchedulePlan")
	public PageList<Map<String,Object>> getSchedulePlanAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String name = request.getParameter("name");
		String profileid = request.getParameter("profileid");
		String marketplaceid = request.getParameter("marketplaceid");
		String groupid = request.getParameter("groupid");
		String state = request.getParameter("state");
		String fromDate = request.getParameter("fromDate");
		String endDate = request.getParameter("endDate");
		if (StringUtil.isEmpty(name)) {
			name = null; 
		} else {
			name =name + "%";
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("profileid", profileid);
		map.put("shopid", user.getCompanyid());
		map.put("userid", user.getId());
		map.put("marketplaceid", marketplaceid);
		map.put("groupid", groupid);
		map.put("state", state);
		map.put("name", name);
		map.put("fromDate", fromDate);
		map.put("endDate", endDate);
		return new PageList<Map<String, Object>>();
	}
	
	 

}
