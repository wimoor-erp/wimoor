package com.wimoor.amazon.report.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.report.pojo.dto.SalesDTO;
import com.wimoor.amazon.summary.service.IOrdersSumService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;

import cn.hutool.core.util.StrUtil;

@RestController
@RequestMapping("/api/v1/salessum")
public class SalesSummaryController {
    @Resource
    IOrdersSumService  ordersSumService;
    @Resource
    ErpClientOneFeignManager erpClientOneFeign;
	@PostMapping("/getOrderSumField")
	public Result<List<Map<String,String>>> getOrderSumFieldAction(@RequestBody SalesDTO dto)  {
		Map<String, Object> parameter = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		parameter.put("shopid", userinfo.getCompanyid());
		Date beginDate = null;
		Date endDate = null;
		DateFormat fm = new SimpleDateFormat("yyyy/MM/dd");
		if (StrUtil.isEmpty(dto.getFromdatestr()) || StrUtil.isEmpty(dto.getEnddatestr())) {
			Calendar cTime = Calendar.getInstance();
			endDate = cTime.getTime();
			cTime.add(Calendar.DATE, -7);
			beginDate = cTime.getTime();
		} else {
			beginDate = GeneralUtil.getDatez(dto.getFromdatestr());
			endDate = GeneralUtil.getDatez(dto.getEnddatestr());
		}
		parameter.put("summaryType", dto.getSummaryType());
		parameter.put("beginDate", fm.format(beginDate));
		parameter.put("endDate", fm.format(endDate));
		if (StrUtil.isEmpty(dto.getGroupid())) {
			dto.setGroupid(null);
		}
		parameter.put("groupid", dto.getGroupid());
		if (StrUtil.isEmpty(dto.getRegion())) {
			dto.setRegion(null);
		}
		parameter.put("region", dto.getRegion());
		if (StrUtil.isEmpty(dto.getOwner())) {
			dto.setOwner(null);
		}
 
		if (StrUtil.isEmpty(dto.getColor())) {
			dto.setColor(null);
		}
		parameter.put("color", dto.getColor());
		if (userinfo.isLimit(UserLimitDataType.owner)) {
			if (dto.getOwner() == null || userinfo.getId().equals(dto.getOwner())) {
				parameter.put("owner", userinfo.getId());
			} else {
				parameter.put("owner", "#");
			}
		} else {
			parameter.put("owner", dto.getOwner());
		}
		parameter.put("search", dto.getSearch() != null && !dto.getSearch().isEmpty() ? "%" + dto.getSearch().trim() + "%" : null);
		parameter.put("marketplaceid", dto.getMarketplace() != null && !dto.getMarketplace().isEmpty() ? dto.getMarketplace().trim() : null);
		return Result.success(ordersSumService.getSalesField(parameter)) ;
	}
	
	
 
	@PostMapping("/getOrderData")
	public Result<IPage<Map<String, Object>>> getOrderDataAction(@RequestBody SalesDTO dto)  {
		UserInfo userinfo = UserInfoContext.get();
		String search =dto.getSearch();
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("shopid",userinfo.getCompanyid());
		String marketplace = dto.getMarketplace();
		parameter.put("marketplaceid", marketplace != null && !marketplace.isEmpty() ? marketplace.trim() : null);
		String region =dto.getRegion();
		if (StrUtil.isEmpty(region)) {
			region = null;
		}
		parameter.put("region", region);
		parameter.put("userid", userinfo.getId());
		String endDate =dto.getEnddatestr();
		String beginDate =dto.getFromdatestr();
		if (endDate != null && endDate.length() > 10) {
			endDate = endDate.substring(0, 10);
		}
		if (beginDate != null && endDate.length() > 10) {
			beginDate = beginDate.substring(0, 10);
		}
		parameter.put("summaryType", dto.getSummaryType());
		Date _beginDate = GeneralUtil.getDatez(beginDate);
		Date _endDate = GeneralUtil.getDatez(endDate);
		double days = GeneralUtil.distanceOfDay(_beginDate, _endDate);
		if("day".equals(dto.getSummaryType())) {
			if(days>90) {
				if(StrUtil.isEmpty(search)) {
					throw new BizException("需输入完整的SKU才能查询大于90天数据");
				}else {
					parameter.put("search", search != null && !search.isEmpty() ?  search.trim().replaceAll("%", ""): null);
				}
			}else {
				parameter.put("search", search != null && !search.isEmpty() ? "%" + search.trim() + "%" : null);
			}
		}else if("week".equals(dto.getSummaryType())) {
			if(days>365) {
				if(StrUtil.isEmpty(search)) {
					throw new BizException("需输入完整的SKU才能查询大于365天数据");
				}else {
					parameter.put("search", search != null && !search.isEmpty() ?  search.trim().replaceAll("%", ""): null);
				}
			}else {
				parameter.put("search", search != null && !search.isEmpty() ? "%" + search.trim() + "%" : null);
			}
		}else {
				if(days>1095) {
					if(StrUtil.isEmpty(search)) {
						throw new BizException("需输入完整的SKU才能查询大于1095天数据");
					}else {
						parameter.put("search", search != null && !search.isEmpty() ?  search.trim().replaceAll("%", ""): null);
					}
				}else {
					parameter.put("search", search != null && !search.isEmpty() ? "%" + search.trim() + "%" : null);
				}
		}
		
		parameter.put("endDate", endDate);
		parameter.put("beginDate", beginDate);
		String groupid = dto.getGroupid();
		if (StrUtil.isEmpty(groupid)) {
			groupid = null;
		}
		parameter.put("groupid", groupid);
		String color = dto.getColor();
		if (StrUtil.isEmpty(color)) {
			color = null;
		}
		parameter.put("color", color);
		String owner = dto.getOwner();
		if (StrUtil.isEmpty(owner)) {
			owner = null;
		}
		if (userinfo.isLimit(UserLimitDataType.owner)) {
			if (owner == null || userinfo.getId().equals(owner)) {
				parameter.put("owner", userinfo.getId());
			} else {
				parameter.put("owner", "#");
			}
		} else {
			parameter.put("owner", owner);
		}
		List<Map<String, Object>> list = ordersSumService.findordershopReport(parameter);
		if (list != null && list.size() > 0) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) list.get(0).get("sumData");
		    IPage<Map<String, Object>> pageList=dto.getListPage(list);
			pageList.getRecords().get(0).putAll(map);
			return Result.success(pageList);
		}else {
			 list=new ArrayList<Map<String,Object>>();
			 IPage<Map<String, Object>> pageList=dto.getListPage(list);
			 return Result.success(pageList);
		}
		  
	}

	@PostMapping("/downloadDate")
	public Object downloadDateAction(@RequestBody SalesDTO dto, HttpServletResponse response)  {
		UserInfo userinfo = UserInfoContext.get();
		String search = dto.getSearch();
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("shopid", userinfo.getCompanyid());
		String marketplace = dto.getMarketplace();
		parameter.put("summaryType", dto.getSummaryType());
		parameter.put("marketplaceid", marketplace != null && !marketplace.isEmpty() ? marketplace.trim() : null);
		String region =dto.getRegion();
		if (StrUtil.isEmpty(region)) {
			region = null;
		}
		parameter.put("region", region);
		parameter.put("userid", userinfo.getId());
		String endDate = dto.getEnddatestr();
		String beginDate = dto.getFromdatestr();
		if (endDate != null) {
			endDate = endDate.substring(0, 10);
		}
		if (beginDate != null) {
			beginDate = beginDate.substring(0, 10);
		}
		Date _beginDate = GeneralUtil.getDatez(beginDate);
		Date _endDate = GeneralUtil.getDatez(endDate);
		double days = GeneralUtil.distanceOfDay(_beginDate, _endDate);
		if(days>90&&dto.getSummaryType().equals("day")) {
			if(StrUtil.isEmpty(search)) {
				throw new BizException("需输入完整的SKU才能查询90天数据");
			} 
		} 
		parameter.put("search", search != null && !search.isEmpty() ?  "%"+search.trim() + "%" : null);
		parameter.put("endDate", endDate);
		parameter.put("beginDate", beginDate);
		String groupid = dto.getGroupid();
		if (StrUtil.isEmpty(groupid)) {
			groupid = null;
		}
		parameter.put("groupid", groupid);
		String color = dto.getColor();
		if (StrUtil.isEmpty(color)) {
			color = null;
		}
		parameter.put("color", color);
		String owner = dto.getOwner();
		if (StrUtil.isEmpty(owner)) {
			owner = null;
		}
		if (userinfo.isLimit(UserLimitDataType.owner)) {
			if (owner == null || userinfo.getId().equals(owner)) {
				parameter.put("owner", userinfo.getId());
			} else {
				parameter.put("owner", "#");
			}
		} else {
			parameter.put("owner", owner);
		}

		SXSSFWorkbook workbook = null;
		ServletOutputStream fOut = null;
		try {
			List<Map<String, Object>> list = ordersSumService.findordershopReport(parameter);
			if (list == null || list.size() == 0) {
				throw new BizException("没有销售数据！");
			}
			list.get(0).put("beginDate", beginDate);
			list.get(0).put("endDate", endDate);
			  List<Map<String, String>> field = ordersSumService.getSalesField(parameter);
			workbook = ordersSumService.setProductSalasExcelBook(list,field);
		} catch (BizException e) {
			 e.printStackTrace();
		}
		try {
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=ProductSalas" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fOut != null) {
				try {
					fOut.flush();
					fOut.close();
					if (workbook != null) {
						workbook.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
}
