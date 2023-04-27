package com.wimoor.erp.inventory.controller;
 
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;
import com.wimoor.erp.inventory.pojo.dto.InvDayDetailDTO;
import com.wimoor.erp.inventory.pojo.dto.InventoryMonthChgDTO;
import com.wimoor.erp.inventory.pojo.dto.InventoryUnsaleableDTO;
import com.wimoor.erp.inventory.service.IInventoryHisService;
import com.wimoor.erp.inventory.service.IInventoryMonthSummaryService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWhseReportService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
@Api(tags = "仓存接口")
@RestController
@RequestMapping("/api/v1/inventory/report")
@RequiredArgsConstructor
public class InventoryReportController {
	@Resource
	IInventoryHisService iInventoryHisService;
	@Resource
	IWhseReportService whseReportService;
	final IWarehouseService iWarehouseService;
    final IInventoryMonthSummaryService inventoryMonthSummaryService;
    
    @GetMapping("/monthsummary")
	public Result<Object> refreshAlibabaOrderAction() throws BizException {
//		try {
//			whseReportService.generateReprot();
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
		try {
			inventoryMonthSummaryService.generateReprot();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return Result.success();
	}
    
     
	@GetMapping( "getInvDayField")
	public Result<?> getInvDayFieldAction(String fromdatestr,String enddatestr) {
		Map<String, Date> parameter = new HashMap<String, Date>();
		Date beginDate = null;
		Date endDate = null;
		if (StrUtil.isEmpty(fromdatestr) || StrUtil.isEmpty(enddatestr)) {
			Calendar cTime = Calendar.getInstance();
			endDate = cTime.getTime();
			cTime.add(Calendar.DATE, -7);
			beginDate = cTime.getTime();
		} else {
			beginDate = GeneralUtil.getDatez(fromdatestr);
			endDate = GeneralUtil.getDatez(enddatestr);
		}
		parameter.put("beginDate", beginDate);
		parameter.put("endDate", endDate);
		return Result.success(iInventoryHisService.getInvDayField(parameter)) ;
	}

	
	@PostMapping("getInvDayDetail")
	public Result<?> getInvDayDetailAction(@ApiParam("查询DTO")@RequestBody  InvDayDetailDTO query) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		parameter.put("shopid", shopid);
		String sku =query.getSku();
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
		if(userinfo.isLimit(UserLimitDataType.owner)) {
			parameter.put("myself", userinfo.getId());
		}
		parameter.put("beginDate", fm.format(beginDate));
		parameter.put("endDate", fm.format(endDate));
		IPage<Map<String, Object>> list = iInventoryHisService.getInvDayDetail(query,parameter);
		return Result.success(list);
	}

	@PostMapping("getInvDayDetailExport")
	public void getInvDayDetailExport(@ApiParam("查询DTO")@RequestBody InvDayDetailDTO query, HttpServletResponse response)  {
		Map<String, Object> parameter = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		parameter.put("shopid", shopid);
		String sku =query.getSku();
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
		if(userinfo.isLimit(UserLimitDataType.owner)) {
			parameter.put("myself", userinfo.getId());
		}
		parameter.put("beginDate", fm.format(beginDate));
		parameter.put("endDate", fm.format(endDate));
		
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=FBAInvDayDetail"+System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			iInventoryHisService.downloadFBAInvDayDetail(workbook, parameter);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("findUnsalableReport")
	public Result<?> findUnsalableReport(@RequestBody InventoryUnsaleableDTO dto)   {
		Map<String, Object> param = new HashMap<String, Object>();
		String wid = dto.getWarehouseid();
		 UserInfo user = UserInfoContext.get();
		if (StrUtil.isNotEmpty(wid)) {
			Warehouse warehouse = iWarehouseService.getById(wid);
			if(warehouse!=null) {
				param.put("wid", warehouse.getParentid());
			}
		}
		String search = dto.getSearch();
		if (StrUtil.isEmpty(search))
			search = null;
		else
			search = "%"+search.trim() + "%";
		param.put("search", search);
		param.put("shopid", user.getCompanyid());
		 if(user.isLimit(UserLimitDataType.owner)) {
			 param.put("myself", user.getId());
		 }
		IPage<Map<String, Object>> list = whseReportService.findUnsalableReportByCondition(dto.getPage(),param);
		return Result.success(list);
	}
	
 
	
	@SuppressWarnings("resource")
	@PostMapping("/findUnsalableReportByDayExcel")
	public void findUnsalableReportByDayAction(@ApiParam("查询DTO")@RequestBody InventoryUnsaleableDTO dto, HttpServletResponse response) {
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=LocalInventoryDeadRpt.xlsx");// 设置文件名
			 ServletOutputStream fOut;
			try {
				 fOut = response.getOutputStream();
				 Map<String, Object> param = new HashMap<String, Object>();
					String wid = dto.getWarehouseid();
					String days =dto.getDays();
					if(StrUtil.isBlankOrUndefined(days)) {
						throw new BizException("库存天数不能为空");
					}
					 UserInfo user = UserInfoContext.get();
					if (StrUtil.isNotEmpty(wid)) {
							Warehouse warehouse = iWarehouseService.getById(wid);
							if(warehouse!=null) {
								param.put("parentid", warehouse.getParentid());
							}
					}
					String search = dto.getSearch();
					if (StrUtil.isEmpty(search))
						search = null;
					else
						search = "%"+search.trim() + "%";
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DATE, -1*Integer.parseInt(days));
					Date date = c.getTime();
					String fromDate = GeneralUtil.formatDate(date, sdf);
					param.put("byday", fromDate);
					param.put("search", search);
					param.put("shopid", user.getCompanyid());
					List<Map<String, Object>> list = whseReportService.findUnsalableReportByDay(param);
				 whseReportService.setUnsalableReportByDayExcel(workbook,list);
				 workbook.write(fOut);
				 workbook.close();
				 fOut.flush();
				 fOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
 
	}
	
	@PostMapping("/findUnsalableReportExcel")
	public void findUnsalableReportExcelAction(@ApiParam("查询DTO")@RequestBody InventoryUnsaleableDTO query, HttpServletResponse response) {
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=LocalInventoryDeadRpt.xlsx");// 设置文件名
			 ServletOutputStream fOut;
			try {
				 fOut = response.getOutputStream();
				 UserInfo userinfo = UserInfoContext.get();
				 String warehouseid=query.getWarehouseid();
				 String search = query.getSearch();
				 if (StrUtil.isEmpty(search)) {
					 search = null;
				 } else {
					 search = "%"+search.trim() + "%";
				 }
				 if (StrUtil.isNotEmpty(warehouseid)) {
						Warehouse warehouse = iWarehouseService.getById(warehouseid);
						if(warehouse!=null) {
							warehouseid= warehouse.getParentid();
						}
				}
				 Map<String,Object> params=new HashMap<String,Object>();
				 params.put("shopid", userinfo.getCompanyid());
				 params.put("warehouseid", warehouseid);
				 params.put("search", search);
				 if(userinfo.isLimit(UserLimitDataType.owner)) {
						params.put("myself", userinfo.getId());
				 }
				 whseReportService.findLocalInvDead(workbook,params);
				 workbook.write(fOut);
				 workbook.close();
				 fOut.flush();
				 fOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
 
	}

	
	@PostMapping(value = "/downExcelInventorySummaryMonth")
	public void downExcelInventorySummaryMonthAction(@RequestBody InventoryMonthChgDTO dto, HttpServletResponse response) {
		// 创建新的Excel工作薄
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		// 将数据写入Excel
		Map<String, Object> param = new HashMap<String, Object>();
		String sku = dto.getSku();
		if(StrUtil.isNotBlank(sku)) {
			param.put("sku","%"+sku.trim()+"%");
		}else {
			param.put("sku",null);
		}
		String warehouseid=dto.getWarehouseid();
		String monthDate = dto.getMonthDate();
		UserInfo userinfo = UserInfoContext.get();
		param.put("shopid", userinfo.getCompanyid());
		param.put("month", monthDate+"-01");
		if(StrUtil.isEmpty(warehouseid)){
			warehouseid=null;
		}
		param.put("warehouseid", warehouseid);
		inventoryMonthSummaryService.setExcelfindReport(workbook, param);
		try {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=monthInvRate("+monthDate+")" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
 
	@PostMapping("/getAllInventorySummaryMonth")
	public Result<IPage<Map<String, Object>>> getAllInventorySummaryMonthAction(@RequestBody InventoryMonthChgDTO dto)  {
		Map<String, Object> param = new HashMap<String, Object>();
		String sku = dto.getSku();
		if(StrUtil.isNotBlank(sku)) {
			param.put("sku","%"+sku.trim()+"%");
		}else {
			param.put("sku",null);
		}
		String warehouseid=dto.getWarehouseid();
		String monthDate = dto.getMonthDate();
		UserInfo userinfo = UserInfoContext.get();
		param.put("shopid", userinfo.getCompanyid());
		param.put("month", monthDate+"-01");
		if(StrUtil.isEmpty(warehouseid)){
			warehouseid=null;
		}
		param.put("warehouseid", warehouseid);
		IPage<Map<String, Object>> list =this.inventoryMonthSummaryService.findReport(dto.getPage(),param);
		return Result.success(list);
	}
}
