package com.wimoor.amazon.report.controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.report.pojo.dto.InvDayDetailDTO;
import com.wimoor.amazon.report.service.IInventoryService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-12
 */
@Api(tags = "授权接口")
//@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/report/inventory")
public class InventoryReportController {
   final IInventoryService reportAmzInventoryService;
   
	@PostMapping(value = "getFBAInvDayDetail")
	public Object getFBAInvDayDetailAction(@ApiParam("查询DTO")@RequestBody InvDayDetailDTO query) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		parameter.put("shopid", shopid);
		String sku = query.getSku();
		if (StrUtil.isEmpty(sku)) {
			sku = null;
		} else {
			sku = "%" + sku.trim() + "%";
		}
		parameter.put("sku", sku);
		String warehouse = query.getWarehouse();
		if (StrUtil.isEmpty(warehouse)) {
			warehouse = null;
		}
		parameter.put("warehouse", warehouse);
		String groupid = query.getGroupid();
		if (StrUtil.isEmpty(groupid)) {
			groupid = null;
		}
		parameter.put("groupid", groupid);
		String fromdatestr = query.getFromdate();
		String enddatestr = query.getEnddate();
		Date beginDate = null;
		Date endDate = null;
		DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isEmpty(fromdatestr) || StrUtil.isEmpty(enddatestr)) {
			Calendar cTime = Calendar.getInstance();
			endDate = cTime.getTime();
			cTime.add(Calendar.DATE, -7);
			beginDate = cTime.getTime();
		} else {
			beginDate = GeneralUtil.getDatez(fromdatestr);
			endDate = GeneralUtil.getDatez(enddatestr);
		}
		parameter.put("beginDate", fm.format(beginDate));
		parameter.put("endDate", fm.format(endDate));
		IPage<Map<String, Object>> list = reportAmzInventoryService.getFBAInvDayDetail(query.getPage(),parameter);
		return list;
	}
}

