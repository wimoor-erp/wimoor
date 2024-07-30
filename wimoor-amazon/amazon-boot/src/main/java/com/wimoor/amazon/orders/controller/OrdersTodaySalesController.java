package com.wimoor.amazon.orders.controller;

import java.text.ParseException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.orders.pojo.dto.AmazonTodayProductOrdersDTO;
import com.wimoor.amazon.orders.service.IOrdersTodaySalesService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "今日订单接口")
@RestController
@RequestMapping("/api/v0/orders/today")
public class OrdersTodaySalesController {
	@Autowired
	IOrdersTodaySalesService iOrdersTodaySalesService;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
 
	@Resource
	IAmazonGroupService amazonGroupService;
	@Autowired
	IMarketplaceService marketplaceService;
	
	@PostMapping("/getParamOfTodayOrder")
	public Result<Object> getParamOfTodayOrderAction(@RequestBody AmazonTodayProductOrdersDTO dto) {
		UserInfo userinfo = UserInfoContext.get();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String groupid = dto.getGroupid();
		String marketplaceid = dto.getMarketplaceid();
		String channel = dto.getChannel();
		String status =dto.getStatus();
		String color =dto.getColor();
		String searchtype = dto.getSearchtype();
		String search = dto.getSearch();
		String pointname =dto.getPointname();
		String isbusiness = dto.getIsbusiness();
		String isorder = dto.getIsorder();
		if (StrUtil.isNotEmpty(isorder) && "isproduct".equals(isorder)) {
			paramMap.put("isproduct", isorder);
		}
		if (StrUtil.isNotEmpty(isbusiness)) {
			if ("0".equals(isbusiness)) {
				paramMap.put("isbusiness", false);
			} else {
				paramMap.put("isbusiness", true);
			}
		}
		if (StrUtil.isNotEmpty(search) && "sku".equals(searchtype))
			paramMap.put("sku", "%"+search.trim()+"%");
		if (StrUtil.isNotEmpty(search) && "asin".equals(searchtype))
			paramMap.put("asin", search);
		if (StrUtil.isNotEmpty(search) && "number".equals(searchtype))
			paramMap.put("orderid", search);
		if (StrUtil.isNotEmpty(color))
			paramMap.put("color", color);
		if (StrUtil.isNotEmpty(status))
			paramMap.put("status", status);
		if (StrUtil.isNotEmpty(channel))
			paramMap.put("channel", channel);
		if (StrUtil.isNotEmpty(pointname))
			paramMap.put("pointname", pointname);
		if (StrUtil.isNotEmpty(groupid)) {
			if("all".equals(groupid)) {
				List<AmazonGroup> groupList =amazonGroupService.getGroupByUser(userinfo);
				paramMap.put("groupList", groupList);
			}else {
				paramMap.put("groupid", groupid);
			}
		}
		if(userinfo.isLimit(UserLimitDataType.operations)) {
			paramMap.put("owner",userinfo.getId());
		} 
		Map<String, Marketplace> map = marketplaceService.findMapByMarketplaceId();
		Marketplace market = map.get(marketplaceid);
		if(market!=null){
			Calendar c = Calendar.getInstance();
			String counrtynow = GeneralUtil.getTimezone(market.getMarket(), c.getTime());
			paramMap.put("endDate", counrtynow);
			paramMap.put("startDate", GeneralUtil.getTimezone(market.getMarket(), c.getTime()).substring(0, 10));
			paramMap.put("marketplaceid", marketplaceid.trim());
			List<Map<String, Object>> result = iOrdersTodaySalesService.getParamOfTodayOrder(paramMap);
			StringBuffer fcuurency = new StringBuffer();
			if (result != null && result.get(0)!=null && result.get(0).get("currency") != null) {
				fcuurency.append(GeneralUtil.formatCurrency(result.get(0).get("currency").toString()));
			} else {
				fcuurency.append(GeneralUtil.formatCurrency(market.getCurrency()));
			}
			if (result == null) {
				result = new ArrayList<Map<String,Object>>();
			}
			Map<String,Object> maps=new HashMap<String,Object>();
			maps.put("currency", fcuurency);
			maps.put("counrtyname",  market.getName() + "时间：");
			maps.put("counrtynow", counrtynow);
			maps.put("counrtyyes",  paramMap.get("startDate")+" 23:59:59");
			maps.put("list", result);
			return Result.success(maps);
		}
		return Result.success();
	}
	
	@PostMapping("/todaylist")
	public Result<IPage<Map<String, Object>>> getOrderTodaylistAction(@RequestBody AmazonTodayProductOrdersDTO dto) {
		UserInfo userinfo = UserInfoContext.get();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String groupid = dto.getGroupid();
		String marketplaceid = dto.getMarketplaceid();
		String channel = dto.getChannel();
		String status =dto.getStatus();
		String color =dto.getColor();
		String searchtype = dto.getSearchtype();
		String search = dto.getSearch();
		String pointname =dto.getPointname();
		String isbusiness = dto.getIsbusiness();
		String isorder = dto.getIsorder();
		if (StrUtil.isNotEmpty(isorder) && "isproduct".equals(isorder)) {
			paramMap.put("isproduct", isorder);
		}
		if (StrUtil.isNotEmpty(isbusiness)) {
			if ("0".equals(isbusiness)) {
				paramMap.put("isbusiness", false);
			} else {
				paramMap.put("isbusiness", true);
			}
		}
		if (searchtype.equals("sku") && StrUtil.isNotEmpty(search))
			paramMap.put("sku", "%"+search.trim()+"%");
		if (searchtype.equals("asin") && StrUtil.isNotEmpty(search))
			paramMap.put("asin", search);
		if (searchtype.equals("number") && StrUtil.isNotEmpty(search))
			paramMap.put("orderid", search);
		if (StrUtil.isNotEmpty(color))
			paramMap.put("color", color);
		if (StrUtil.isNotEmpty(status))
			paramMap.put("status", status);
		if (StrUtil.isNotEmpty(channel))
			paramMap.put("channel", channel);
		if (StrUtil.isNotEmpty(pointname))
			paramMap.put("pointname", pointname);
		if (StrUtil.isNotEmpty(groupid)) {
			if("all".equals(groupid)) {
				List<AmazonGroup> groupList =amazonGroupService.getGroupByUser(userinfo);
				paramMap.put("groupList", groupList);
			}else {
				paramMap.put("groupid", groupid);
			}
		}
		if (StrUtil.isEmpty(groupid)) {
			return Result.success();
		}
		Map<String, Marketplace> map = marketplaceService.findMapByMarketplaceId();
		Marketplace market = map.get(marketplaceid);
		if(market==null) {
			return null;
		}
		String counrtynow = GeneralUtil.getTimezone(market.getMarket(), new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		try {
			today = sdf.parse(counrtynow);
		} catch (ParseException e) {
			e.printStackTrace();
		}
 
		paramMap.put("endDate", counrtynow);
		paramMap.put("startDate", counrtynow.substring(0, 10));
		paramMap.put("yyendDate", today);
		paramMap.put("yystartDate", GeneralUtil.getYesterday(today));
		paramMap.put("lwendDate", GeneralUtil.getNextDate(GeneralUtil.getSevenDayBefore(today)));
		paramMap.put("lwstartDate", GeneralUtil.getSevenDayBefore(today));
		paramMap.put("lwsDate", sdf.format(GeneralUtil.getSevenDayBefore(today)));
		paramMap.put("marketplaceid", marketplaceid.trim());
		paramMap.put("market", market);
		paramMap.put("pointname", market.getPointName());
		if(userinfo.isLimit(UserLimitDataType.operations)) {
			paramMap.put("owner",userinfo.getId());
		} 
		if(groupid!=null&&marketplaceid!=null) {
			AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
			if(auth!=null) {
				paramMap.put("amazonAuthId", auth.getId());
			}
		}
		IPage<Map<String, Object>> list = iOrdersTodaySalesService.selectOrderTodayList(dto.getPage() ,paramMap);
		if (list != null && list.getRecords().size() > 0) {
			for (Map<String, Object> temp : list.getRecords()) {
				if (temp.get("currency") == null) {
					temp.put("currency", market.getCurrency());
				}
			}
		}
		return Result.success(list);
	}
	
	@ApiOperation("今日订单列表导出")
	@PostMapping("/downExcelTodayOrdersData")
	public void downExcelTodayOrdersDataAction(@RequestBody AmazonTodayProductOrdersDTO dto,HttpServletResponse response){
		UserInfo userinfo = UserInfoContext.get();
		// 创建新的Excel工作薄
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		// 将数据写入Excel
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String groupid = dto.getGroupid();
		String marketplaceid = dto.getMarketplaceid();
		String channel = dto.getChannel();
		String status = dto.getStatus();
		String color = dto.getColor();
		String searchtype = dto.getSearchtype();
		String search = dto.getSearch();
		String pointname = dto.getPointname();
		String isbusiness = dto.getIsbusiness();
		String isorder = dto.getIsorder();
		if (StrUtil.isNotEmpty(isorder) && "isproduct".equals(isorder)) {
			paramMap.put("isproduct", isorder);
		}
		if (StrUtil.isNotEmpty(isbusiness)) {
			if ("0".equals(isbusiness)) {
				paramMap.put("isbusiness", false);
			} else {
				paramMap.put("isbusiness", true);
			}
		}
		if (searchtype.equals("sku") && StrUtil.isNotEmpty(search))
			paramMap.put("sku", "%"+search+"%");
		if (searchtype.equals("asin") && StrUtil.isNotEmpty(search))
			paramMap.put("asin", search);
		if (searchtype.equals("number") && StrUtil.isNotEmpty(search))
			paramMap.put("orderid", search);
		if (StrUtil.isNotEmpty(color))
			paramMap.put("color", color);
		if (StrUtil.isNotEmpty(status))
			paramMap.put("status", status);
		if (StrUtil.isNotEmpty(channel))
			paramMap.put("channel", channel);
		if (StrUtil.isNotEmpty(pointname))
			paramMap.put("pointname", pointname);
		if (StrUtil.isNotEmpty(groupid)) {
			if("all".equals(groupid)) {
				List<AmazonGroup> groupList =  amazonGroupService.getGroupByUser(userinfo);
				paramMap.put("groupList", groupList);
			}else {
				paramMap.put("groupid", groupid);
			}
		}
		if(userinfo.isLimit(UserLimitDataType.operations)) {
			paramMap.put("owner",userinfo.getId());
		} 
		Map<String, Marketplace> map = marketplaceService.findMapByMarketplaceId();
		Marketplace market = map.get(marketplaceid);
		Calendar c = Calendar.getInstance();
		String counrtynow = GeneralUtil.getTimezone(market.getMarket(), c.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		try {
			today = sdf.parse(counrtynow);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		paramMap.put("endDate", counrtynow);
		paramMap.put("startDate", counrtynow.substring(0, 10));
		paramMap.put("yyendDate", today);
		paramMap.put("yystartDate", GeneralUtil.getYesterday(today));
		paramMap.put("lwendDate", GeneralUtil.getNextDate(GeneralUtil.getSevenDayBefore(today)));
		paramMap.put("lwstartDate", GeneralUtil.getSevenDayBefore(today));
		paramMap.put("lwsDate", sdf.format(GeneralUtil.getSevenDayBefore(today)));
		paramMap.put("marketplaceid", marketplaceid.trim());
		paramMap.put("market", market);
	    AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
	    paramMap.put("amazonAuthId", auth.getId());
		if(userinfo.isLimit(UserLimitDataType.operations)) {
			paramMap.put("owner",userinfo.getId());
		} 
	    iOrdersTodaySalesService.setProductSalesTodayExcelBook(workbook, paramMap);
		try {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=OrdersTodayReport" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
