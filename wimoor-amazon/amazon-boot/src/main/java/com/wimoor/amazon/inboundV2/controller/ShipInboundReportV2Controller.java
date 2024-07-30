package com.wimoor.amazon.inboundV2.controller;

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
@RequestMapping("/api/v2/ship/report")
@SystemControllerLog("发货报表")
@RequiredArgsConstructor
public class ShipInboundReportV2Controller {
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
	
	
  
	
}
