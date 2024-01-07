package com.wimoor.amazon.adv.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvRemark;
import com.wimoor.amazon.adv.common.service.IAmzAdvRemarkService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

@Controller 
@RequestMapping("/advRemarkManager") 
public class AdvertRemarkManagerController {
 
	@Resource
	IAmzAdvRemarkService amzAdvRemarkService;
	
	@ResponseBody
	@RequestMapping("/addRemark")
	public String addRemarkAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String json = request.getParameter("jsonstr");
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		BigInteger profileid = jsonobject.getBigInteger("profileid");
		JSONArray remarkArray = jsonobject.getJSONArray("remarkArray");
		List<AmzAdvRemark> list = new ArrayList<AmzAdvRemark>();
		for(int i = 0; i < remarkArray.size(); i++) {
			JSONObject remark = remarkArray.getJSONObject(i);
			BigInteger campaignid = remark.getBigInteger("campaignId");
			BigInteger adgroupid = remark.getBigInteger("adGroupId");
			BigInteger keywordid = remark.getBigInteger("keywordId");
			BigInteger adid = remark.getBigInteger("adId");
			BigInteger targetid = remark.getBigInteger("targetid");
			String remarkstr = remark.getString("remark");

			AmzAdvRemark amzAdvRemark = new AmzAdvRemark();
			amzAdvRemark.setCampaignid(campaignid);
			amzAdvRemark.setAdgroupid(adgroupid);
			amzAdvRemark.setAdid(adid);
			amzAdvRemark.setKeywordid(keywordid);
			amzAdvRemark.setTargetid(targetid);
			amzAdvRemark.setRemark(remarkstr);
			amzAdvRemark.setProfileid(profileid);
			amzAdvRemark.setOperator(user.getId());
			amzAdvRemark.setOpttime(new Date());
			list.add(amzAdvRemark);
		}
		if(amzAdvRemarkService.addRemark(list) == 1) {
			return "SUCCESS";
		}else {
			return "ERROR";
		}
	}
	
	@ResponseBody
	@RequestMapping("/updateRemark")
	public String updateRemarkAction(HttpServletRequest request, Model model){
		UserInfo user = UserInfoContext.get();
		String json = request.getParameter("jsonstr");
		JSONObject jsonobject = GeneralUtil.getJsonObject(json);
		JSONArray remarkArray = jsonobject.getJSONArray("remarkArray");
		List<AmzAdvRemark> list = new ArrayList<AmzAdvRemark>();
		for(int i = 0; i < remarkArray.size(); i++) {
			JSONObject remark = remarkArray.getJSONObject(i);
			BigInteger profileid = remark.getBigInteger("profileid");
			BigInteger campaignid = remark.getBigInteger("campaignId");
			BigInteger adgroupid = remark.getBigInteger("adGroupId");
			if(remark.getBigInteger("adGroupId") == null) {
				adgroupid = new BigInteger("0");
			}
			BigInteger keywordid = remark.getBigInteger("keywordId");
			if(remark.getBigInteger("keywordId") == null) {
				keywordid = new BigInteger("0");
			}
			BigInteger adid = remark.getBigInteger("adId");
			if(remark.getBigInteger("adId") == null) {
				adid = new BigInteger("0");
			}
			BigInteger targetid = remark.getBigInteger("targetid");
			if(remark.getBigInteger("targetid") == null) {
				targetid = new BigInteger("0");
			}
			String remarkstr = remark.getString("remark");

			AmzAdvRemark amzAdvRemark = new AmzAdvRemark();
			amzAdvRemark.setCampaignid(campaignid);
			amzAdvRemark.setAdgroupid(adgroupid);
			amzAdvRemark.setAdid(adid);
			amzAdvRemark.setKeywordid(keywordid);
			amzAdvRemark.setTargetid(targetid);
			amzAdvRemark.setRemark(remarkstr);
			amzAdvRemark.setProfileid(profileid);
			amzAdvRemark.setOperator(user.getId());
			amzAdvRemark.setOpttime(new Date());
			list.add(amzAdvRemark);
		}
		if(amzAdvRemarkService.updateRemark(list) == list.size()) {
			return "SUCCESS";
		}else {
			return "ERROR";
		}
	}
}
