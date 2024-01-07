package com.wimoor.amazon.inbound.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
 

@Api(tags = "物流渠道接口")
@SystemControllerLog("物流渠道")
@RestController
@RequestMapping("/api/v1/shipTransCompany")
@RequiredArgsConstructor
public class ShipTransCompanyController  {
	final IShipInboundTransService shipInboundTransService;
	@PostMapping(value = "/getShipmentFeeReport")
	public Result<IPage<Map<String, Object>>> getShipmentFeeReportAction(@RequestBody ShipInboundShipmenSummaryDTO dto) {
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		param.put("shopid", shopid);
		String marketplaceid =dto.getMarketplaceid();
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
			param.put("endDate", toDate.trim());
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate);
		}
		String company=dto.getCompany();
		String channel=dto.getChannel();
		if (StrUtil.isNotEmpty(company)) {
			param.put("company", company);
		} else {
			param.put("company", null);
		}
		if (StrUtil.isNotEmpty(channel)) {
			param.put("channel", channel);
		} else {
			param.put("channel", null);
		}
		IPage<Map<String, Object>> pagelist = shipInboundTransService.getShipmentFeeReport(dto.getPage(),param);
		return Result.success(pagelist);
	}
	
	
	 
	@PostMapping(value = "/getShipmentSkuFeeReport")
	public Result<?> getShipmentSkuFeeReportAction(@RequestBody ShipInboundShipmenSummaryDTO dto){
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
		String search = dto.getSearch();
		if (StrUtil.isNotEmpty(search)) {
			param.put("search", search + "%");
		} else {
			param.put("search", null);
		}
		 
		String fromDate =dto.getFromdate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		String toDate =dto.getEnddate() ;
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim());
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate);
		}
		String company=dto.getCompany();
		String channel=dto.getChannel();
		if (StrUtil.isNotEmpty(company)) {
			param.put("company", company);
		} else {
			param.put("company", null);
		}
		if (StrUtil.isNotEmpty(channel)) {
			param.put("channel", channel);
		} else {
			param.put("channel", null);
		}
	 
		IPage<Map<String, Object>> pagelist = shipInboundTransService.transSKUFeeShared(dto.getPage(),param);		
		return Result.success(pagelist);
	}
	
	
	@PostMapping(value = "/downShipmentFeeReportExcel")
	public void downShipmentFeeReportExcelAction(@RequestBody ShipInboundShipmenSummaryDTO dto, HttpServletResponse response) {
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
			String marketplaceid =dto.getMarketplaceid();
			if (StrUtil.isEmpty(marketplaceid)) {
				marketplaceid = null;
			}
			params.put("marketplaceid", marketplaceid);
			String groupid =dto.getGroupid();
			if (StrUtil.isEmpty(groupid)) {
				groupid = null;
			}
			params.put("groupid", groupid);
			String search =dto.getSearch();
			if (StrUtil.isNotEmpty(search)) {
				params.put("search", search + "%");
			} else {
				params.put("search", null);
			}
			String fromDate =dto.getFromdate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				params.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -7);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				params.put("fromDate", fromDate);
			}
			String toDate =dto.getEnddate();
			if (StrUtil.isNotEmpty(toDate)) {
				params.put("endDate", toDate.trim());
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				params.put("endDate", toDate);
			}
			String isShip =dto.getIsShip();
			params.put("shopid", shopid);
			params.put("isShip", isShip);
			shipInboundTransService.setShipmentFeeReport(workbook, params);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping(value = "/getShipmentFeeDetailReport")
	public Result<?> getShipmentFeeDetailReportAction(@RequestBody ShipInboundShipmenSummaryDTO dto)  {
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		param.put("shopid", shopid);
		String marketplaceid =dto.getMarketplaceid() ;
		if (StrUtil.isEmpty(marketplaceid)) {
			marketplaceid = null;
		}
		param.put("marketplaceid", marketplaceid);
		String groupid =dto.getGroupid();
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
		String fromDate =dto.getFromdate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		String toDate =dto.getEnddate();
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim());
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate);
		}
		String company=dto.getCompany();
		String channel=dto.getCompany();
		if (StrUtil.isNotEmpty(company)) {
			param.put("company", company);
		} else {
			param.put("company", null);
		}
		if (StrUtil.isNotEmpty(channel)) {
			param.put("channel", channel);
		} else {
			param.put("channel", null);
		}
		IPage<Map<String, Object>> pagelist = shipInboundTransService.getShipmentFeeDetailReport(dto.getPage(),param);
		return Result.success(pagelist);
	}

	@PostMapping(value = "/downShipmentFeeDetailReportExcel")
	public void downShipmentFeeDetailReportExcelAction(@RequestBody ShipInboundShipmenSummaryDTO dto, HttpServletResponse response)   {
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
			String marketplaceid =dto.getMarketplaceid();
			if (StrUtil.isEmpty(marketplaceid)) {
				marketplaceid = null;
			}
			params.put("marketplaceid", marketplaceid);
			String groupid =dto.getGroupid();
			if (StrUtil.isEmpty(groupid)) {
				groupid = null;
			}
			params.put("groupid", groupid);
			String search =dto.getSearch();
			if (StrUtil.isNotEmpty(search)) {
				params.put("search", search + "%");
			} else {
				params.put("search", null);
			}
			String fromDate =dto.getFromdate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				params.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -7);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				params.put("fromDate", fromDate);
			}
			String toDate =dto.getEnddate();
			if (StrUtil.isNotEmpty(toDate)) {
				params.put("endDate", toDate.trim());
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				params.put("endDate", toDate);
			}
			String isShip =dto.getIsShip();
			params.put("shopid", shopid);
			params.put("isShip", isShip);
			shipInboundTransService.setShipmentFeeDetailReport(workbook, params);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
