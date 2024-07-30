package com.wimoor.amazon.inbound.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundShipmenSummaryDTO;
import com.wimoor.amazon.inbound.pojo.dto.ShipmentReportByLogisticsDTO;
import com.wimoor.amazon.inbound.service.IShipInboundItemService;
import com.wimoor.amazon.inbound.service.IShipInboundPlanService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "发货报表")
@RestController
@RequestMapping("/api/v1/ship/report")
@SystemControllerLog("发货报表")
@RequiredArgsConstructor
public class ShipInboundReportController {
    final IShipInboundItemService iShipInboundItemService;
	final IShipInboundPlanService shipInboundPlanService;
	
	@PostMapping(value = "/downExcelShipmentReportByLoistics")
	public void downExcelShipmentReportByLoisticsAction(@RequestBody ShipmentReportByLogisticsDTO dto, HttpServletResponse response)  {
		// 创建新的Excel工作薄
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		// 将数据写入Excel
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		param.put("shopid", shopid);
		String companyid =dto.getCompanyid();
		if (StrUtil.isEmpty(companyid)) {
			companyid = null;
		}
		param.put("companyid", companyid);
		String channelid =dto.getChannelid();
		if (StrUtil.isEmpty(channelid)) {
			channelid = null;
		}
		param.put("channelid", channelid);
		String warehouseid =dto.getWarehouseid();
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		}
		param.put("warehouseid", warehouseid);
		String type =dto.getType() ;
		param.put("type", type);
		String search =dto.getSearch();
		if (StrUtil.isNotEmpty(search)) {
			param.put("search", search + "%");
		}
		String datetype =dto.getDatetype();
		param.put("datetype", datetype);
		String fromDate =dto.getFromDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		String toDate =dto.getToDate();
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim().substring(0,10)+" 23:59:59");
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate+" 23:59:59");
		}
		iShipInboundItemService.setShipmentReportByLoisticsExcelBook(workbook, param);
		try {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=ShipmentReportByLoistics" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping(value = "/getShipmentReportByLoistics")
	public Result<IPage<Map<String, Object>>> getShipmentReportByLoistics(@RequestBody ShipmentReportByLogisticsDTO dto)  {
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		param.put("shopid", shopid);
		String companyid = dto.getCompanyid();
		if (StrUtil.isEmpty(companyid)) {
			companyid = null;
		}
		param.put("companyid", companyid);
		String channelid = dto.getChannelid();
		if (StrUtil.isEmpty(channelid)) {
			channelid = null;
		}
		param.put("channelid", channelid);
		String warehouseid = dto.getWarehouseid();
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		}
		param.put("warehouseid", warehouseid);
		String type = dto.getType();
		param.put("type", type);
		String search = dto.getSearch();
		if (StrUtil.isNotEmpty(search)) {
			param.put("search", search + "%");
		}
		String datetype = dto.getDatetype();
		param.put("datetype", datetype);
		String searchtype = dto.getSearchtype();
		param.put("searchtype", searchtype);
		String fromDate = dto.getFromDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		String toDate =dto.getToDate();
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim().substring(0,10)+" 23:59:59");
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate+" 23:59:59");
		}

		IPage<Map<String, Object>> list = iShipInboundItemService.shipmentReportByLoistics(dto.getPage(),param);
		 
		if (list != null && list.getRecords().size() > 0 && dto.getCurrentpage()==1) {
			Map<String, Object> map = iShipInboundItemService.shipmentReportByLoisticsTotal(param);
			if(map!=null) {
				list.getRecords().get(0).put("summary", map);
			}
		}
		return Result.success(list);
	}

	@PostMapping(value = "/getShipmentReportByWarehouseLoistics")
	public Result<Map<String, List<Map<String, Object>>>> shipmentReportByWarhouseCHType(@RequestBody ShipmentReportByLogisticsDTO dto)  {
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		param.put("shopid", shopid);
		String companyid = dto.getCompanyid();
		if (StrUtil.isEmpty(companyid)) {
			companyid = null;
		}
		param.put("companyid", companyid);
		String channelid = dto.getChannelid();
		if (StrUtil.isEmpty(channelid)) {
			channelid = null;
		}
		param.put("channelid", channelid);
		String warehouseid = dto.getWarehouseid();
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		}
		param.put("warehouseid", warehouseid);
		String type = dto.getType();
		param.put("type", type);
		String search = dto.getSearch();
		if (StrUtil.isNotEmpty(search)) {
			param.put("search", search + "%");
		}
		String datetype = dto.getDatetype();
		param.put("datetype", datetype);
		String searchtype = dto.getSearchtype();
		param.put("searchtype", searchtype);
		String fromDate = dto.getFromDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		String toDate =dto.getToDate();
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim().substring(0,10)+" 23:59:59");
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate+" 23:59:59");
		}
		Map<String, List<Map<String, Object>>> result = iShipInboundItemService.shipmentReportByWarhouseCHType(param);
		return Result.success(result);
	}
	
	
	@PostMapping(value = "/getShipmentDetailReport")
	public Result<IPage<Map<String, Object>>> getShipmentDetailReport(@RequestBody ShipInboundShipmenSummaryDTO dto, HttpServletResponse response)  {
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		param.put("shopid", shopid);
		String marketplaceid = dto.getMarketplaceid();
		if (StrUtil.isEmpty(marketplaceid)) {
			marketplaceid = null;
		}
		param.put("marketplaceid", marketplaceid);
		String groupid = dto.getGroupid();
		if (StrUtil.isEmpty(groupid)) {
			groupid = null;
		}
		param.put("groupid", groupid);
		String search =dto.getSearch();
		if (StrUtil.isNotEmpty(search)) {
			param.put("search", search + "%");
		} else {
			param.put("search", null);
		}
		String datetype = dto.getDatetype();
		param.put("datetype", datetype);
		String fromDate = dto.getFromdate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		String toDate = dto.getEnddate();
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim().substring(0,10)+" 23:59:59");
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate+" 23:59:59");
		}
		String warehouseid =dto.getWarehouseid();
		if (StrUtil.isEmpty(warehouseid) || "all".equals(warehouseid)) {
			warehouseid = null;
		}
		param.put("warehouseid", warehouseid);
		String company = dto.getCompany();
		if (StrUtil.isEmpty(company)) {
			param.put("company", null);
		}else {
			param.put("company", company+"%");
		}
		String iserror =dto.getHasexceptionnum();
		if (StrUtil.isEmpty(iserror) || "all".equals(iserror)) {
			iserror = null;
		}
		param.put("iserror", iserror);
		IPage<Map<String, Object>> pagelist = shipInboundPlanService.getShipmentDetailReport(dto.getPage(),param);
		return Result.success(pagelist);
	}
	
	@PostMapping(value = "/getShipmentReport")
	public  Result<IPage<Map<String, Object>>> getShipmentReport(@RequestBody ShipInboundShipmenSummaryDTO dto)   {
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user=UserInfoContext.get();
		param.put("shopid", user.getCompanyid());
		String marketplaceid = dto.getMarketplaceid();
		if (StrUtil.isEmpty(marketplaceid)) {
			marketplaceid = null;
		}
		param.put("marketplaceid", marketplaceid);
		String groupid = dto.getGroupid();
		if (StrUtil.isEmpty(groupid)) {
			groupid = null;
		}
		param.put("groupid", groupid);
		String search = dto.getSearch();
		if (StrUtil.isNotEmpty(search)) {
			param.put("search", search + "%");
		} else {
			param.put("search", null);
		}
		String datetype = dto.getDatetype();
		param.put("datetype", datetype);
		String fromDate = dto.getFromdate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		String toDate = dto.getEnddate();
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim().substring(0,10)+" 23:59:59");
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate+" 23:59:59");
		}
		String warehouseid =dto.getWarehouseid();
		if (StrUtil.isEmpty(warehouseid) || "all".equals(warehouseid)) {
			warehouseid = null;
		}
		param.put("warehouseid", warehouseid);
		String company = dto.getCompany();
		if (StrUtil.isEmpty(company)) {
			param.put("company", null);
		}else {
			param.put("company", company+"%");
		}
		String companyid = dto.getCompanyid();
		if (StrUtil.isEmpty(companyid)) {
			param.put("companyid", null);
		}else {
			param.put("companyid",companyid);
		}
		String iserror = dto.getHasexceptionnum();
		if (StrUtil.isEmpty(iserror) || "all".equals(iserror)) {
			iserror = null;
		}
		param.put("iserror", iserror);
		IPage<Map<String, Object>> pagelist = shipInboundPlanService.getShipmentReport(dto.getPage(),param);
		return Result.success(pagelist) ;
	}

	@PostMapping(value = "/downShipmentExcel")
	public void downShipmentExcelAction(@RequestBody ShipInboundShipmenSummaryDTO dto, HttpServletResponse response)  {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=Shipmenttemplate.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			UserInfo user=UserInfoContext.get();
			String shopid = user.getCompanyid();
			// 将数据写入Excel
			Map<String, Object> params = new HashMap<String, Object>();
			String marketplaceid = dto.getMarketplaceid();
			String datetype=dto.getDatetype();
	        params.put("datetype", datetype);
			if (StrUtil.isEmpty(marketplaceid)) {
				marketplaceid = null;
			}
			params.put("marketplaceid", marketplaceid);
			String groupid =dto.getGroupid();
			if (StrUtil.isEmpty(groupid)) {
				groupid = null;
			}
			params.put("groupid", groupid);
			String search = dto.getSearch();
			if (StrUtil.isNotEmpty(search)) {
				params.put("search", search + "%");
			} else {
				params.put("search", null);
			}
			String fromDate = dto.getFromdate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				params.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -7);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				params.put("fromDate", fromDate);
			}
			String toDate = dto.getEnddate();
			if (StrUtil.isNotEmpty(toDate)) {
				params.put("endDate", toDate.trim().substring(0,10)+" 23:59:59");
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				params.put("endDate", toDate+" 23:59:59");
			}
		 
			params.put("shopid", shopid);
			params.put("ftype", dto.getDownloadType());
			shipInboundPlanService.setExcelBookByType(workbook, params);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
