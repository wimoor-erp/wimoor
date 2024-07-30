package com.wimoor.amazon.adv.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.api.AdminClientOneFeignManager;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvOperateLogService;
import com.wimoor.amazon.adv.controller.pojo.dto.OperationLogQuery;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.util.StringUtil;

@RestController 
@RequestMapping("/api/v1/advOperateLogManager") 
public class AdvertOperateLogManagerController {
	
    @Resource
    AdminClientOneFeignManager adminClientOneFeignManager;
	@Resource
	IAmzAdvOperateLogService amzAdvOperateLogService;
	
	@PostMapping("/getOperateLogList")
	public Result<PageList<Map<String,Object>>> getOperateLogListAction(@RequestBody OperationLogQuery dto) {
		UserInfo user = UserInfoContext.get();
		String profileid =dto.getProfileid();
		String marketplaceid = dto.getMarketplaceid();
		String groupid = dto.getGroupid();
		String search =dto.getSearch();
		String fromDate = dto.getFromDate();
		String endDate = dto.getEndDate();
		if (StringUtil.isEmpty(search)) {
			search = null; 
		} else {
			search =  search + "%";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", user.getId());
		map.put("shopid", user.getCompanyid());
		if(StrUtil.isNotBlank(profileid)) {
			map.put("profileid", profileid);
		}else {
			map.put("profileid", null);
		}
		if(StrUtil.isNotBlank(marketplaceid)) {
			map.put("marketplaceid", marketplaceid);
		}else {
			map.put("marketplaceid", null);
		}
		if(StrUtil.isNotBlank(groupid)) {
			map.put("groupid", groupid);
		}else {
			map.put("groupid", null);
		}
		
		map.put("search", search);
		map.put("fromDate", fromDate);
		map.put("endDate", endDate);
		PageList<Map<String,Object>> list= amzAdvOperateLogService.getOperateLogList(map, dto.getPageBounds());
		Map<String, String> nameMap = adminClientOneFeignManager.getAllUserName();
		if(list == null) {
			return Result.success(new PageList<Map<String,Object>>()) ;
		}else {
			for(Map<String,Object> item:list) {
				String operator=item.get("operator")!=null?item.get("operator").toString():"";
				item.put("username", nameMap.get(operator));
			}
		}
	    return Result.success(list) ;
	}
	
	@GetMapping("/updateOperateLogRemark")
	public Result<?> updateOperateLogRemarkAction(String id,String remark) {
		if(StringUtil.isEmpty(id)) {
			throw new BaseException("请选择一条历史记录来增加备注！");
		}
		int i = amzAdvOperateLogService.updateOperateLogRemark(id, remark);
		if(i > 0) {
			return Result.success();
		}else {
			return Result.failed();
		}
	}

}
