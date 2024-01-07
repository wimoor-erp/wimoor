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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import com.wimoor.erp.inventory.pojo.dto.InventoryValueReportDTO;
import com.wimoor.erp.inventory.service.IInventoryHisService;
import com.wimoor.erp.inventory.service.IInventoryMonthSummaryService;
import com.wimoor.erp.inventory.service.IInventoryRecordService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWhseReportService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    final IInventoryRecordService iInventoryRecordService;
	@Resource
	IInventoryService inventoryService;
	 
    @GetMapping("/monthsummary")
	public Result<Object> monthSummaryAction() throws BizException {
		try {
			 whseReportService.generateReprot();
		}catch(Exception e) {
			e.printStackTrace();
		}
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
	
	
	 
	@PostMapping(value = "inventoryCost")
	public Result<IPage<Map<String, Object>>> getInventoryCostAction(@RequestBody InventoryValueReportDTO dto)  {
		String sku =dto.getSku();
		String byday =dto.getByday();
		UserInfo user = UserInfoContext.get();
		return Result.success(inventoryService.selectInventoryCost(dto.getPage(),dto.getWarehouseid(), sku, user.getCompanyid(), byday));
	}
	
	@PostMapping(value = "downLoadInvCost")
	public void getInventoryCostReportAction(@RequestBody InventoryValueReportDTO dto, HttpServletResponse response)  {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=inventrtyCostReport.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			UserInfo user = UserInfoContext.get();
			String warehouseid =dto.getWarehouseid();
			String sku =dto.getSku();
			String byday =dto.getByday();
			String shopid = user.getCompanyid();
			List<Map<String, Object>> list = inventoryService.selectInventoryCostAll(warehouseid, sku, shopid, byday);
			Sheet sheet = workbook.createSheet("sheet1");
			Map<String, Integer> titlemap = new HashMap<String, Integer>();
			Row row = sheet.createRow(0);
			Cell cell = null;
			titlemap.put("sku", 0);
			cell = row.createCell(0);
			cell.setCellValue("SKU");

			titlemap.put("fulfillable", 1);
			cell = row.createCell(1);
			cell.setCellValue("现有库存");

			titlemap.put("inbound", 2);
			cell = row.createCell(2);
			cell.setCellValue("待入库库存");

			titlemap.put("price", 3);
			cell = row.createCell(3);
			cell.setCellValue("采购单价");

			titlemap.put("otherFeer", 4);
			cell = row.createCell(4);
			cell.setCellValue("其他采购成本");
			
			titlemap.put("totalprice", 5);
			cell = row.createCell(5);
			cell.setCellValue("库存货值");

			titlemap.put("inpasscost", 6);
			cell = row.createCell(6);
			cell.setCellValue("在途物资货值");

			titlemap.put("inpasspay", 7);
			cell = row.createCell(7);
			cell.setCellValue("应付账款-采购货物");

			titlemap.put("remark", 8);
			cell = row.createCell(8);
			cell.setCellValue("备注");
			// 在索引0的位置创建行（最顶端的行）
			for (int i = 0; i < list.size(); i++) {
				Row skurow = sheet.createRow(i + 1);
				Map<String, Object> skumap = list.get(i);
				for (String key : skumap.keySet()) {
					if (titlemap.get(key) == null) {
						continue;
					}
					cell = skurow.createCell(titlemap.get(key));
					cell.setCellValue(skumap.get(key).toString());
				}
			}
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@ApiOperation("导出")
  	@GetMapping("/downloadOutstockformOut")
    public  void downloadOutstockformOutAction(
    		 String fromdate,String enddate,
    		 HttpServletResponse response){
  		try {
  			UserInfo userinfo = UserInfoContext.get();
	  		SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=planningReport.xlsx");// 设置文件名
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("shopid", userinfo.getCompanyid());
			map.put("company", userinfo.getUserinfo().get("companyname"));
			map.put("fromdate", fromdate);
			map.put("enddate", enddate);
			iInventoryRecordService.downloadOutstockformOut(workbook, map);
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
