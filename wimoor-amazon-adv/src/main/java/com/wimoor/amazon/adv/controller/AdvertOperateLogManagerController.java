package com.wimoor.amazon.adv.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import tk.mybatis.mapper.util.StringUtil;

@Controller 
@RequestMapping("/advOperateLogManager") 
public class AdvertOperateLogManagerController {
	
 
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	
	@ResponseBody
	@RequestMapping("/getOperateLogList")
	public PageList<Map<String,Object>> getOperateLogListAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String profileid = request.getParameter("profileid");
		String marketplaceid = request.getParameter("marketplaceid");
		String groupid = request.getParameter("groupid");
		String search = request.getParameter("search");
		String fromDate = request.getParameter("fromDate");
		String endDate = request.getParameter("endDate");
		if (StringUtil.isEmpty(search)) {
			search = null; 
		} else {
			search =  search + "%";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", user.getId());
		map.put("shopid", user.getCompanyid());
		map.put("profileid", profileid);
		map.put("marketplaceid", marketplaceid);
		map.put("groupid", groupid);
		map.put("search", search);
		map.put("fromDate", fromDate);
		map.put("endDate", endDate);
		PageList<Map<String,Object>> list= amzAdvOperateLogService.getOperateLogList(map, ListController.getPageBounds(request));
		if(list == null) {
			return new PageList<Map<String,Object>>();
		}
	    return list;
	}
	
	@ResponseBody
	@RequestMapping("/updateOperateLogRemark")
	public Object updateOperateLogRemarkAction(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		String remark = request.getParameter("remark");
		if(StringUtil.isEmpty(id)) {
			throw new BaseException("请选择一条历史记录来增加备注！");
		}
		int i = amzAdvOperateLogService.updateOperateLogRemark(id, remark);
		if(i > 0) {
			return "SUCCESS";
		}else {
			return "ERROR";
		}
	}

}
