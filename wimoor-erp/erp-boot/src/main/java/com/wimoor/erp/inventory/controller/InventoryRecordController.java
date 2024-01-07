package com.wimoor.erp.inventory.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.common.pojo.entity.FormType;
import com.wimoor.erp.inventory.pojo.dto.InventoryRecordDTO;
import com.wimoor.erp.inventory.service.IInventoryHisService;
import com.wimoor.erp.inventory.service.IInventoryRecordService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "仓存库存历史接口")
@RestController
@RequestMapping("/api/v1/inventoryRecord")
@RequiredArgsConstructor
public class InventoryRecordController {

	final IInventoryHisService inventoryHisService;
	
	final IInventoryRecordService inventoryRecordService;
	
	final IMaterialService iMaterialService;
	
	
	@PostMapping("/list")
	public Result<IPage<Map<String, Object>>> getListData(@RequestBody InventoryRecordDTO dto)  {
		Map<String,Object> maps=new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String warehouseid=dto.getWarehouseid();
		if(StrUtil.isNotEmpty(warehouseid)) {
			maps.put("warehouseid", warehouseid);
		}else {
			maps.put("warehouseid", null);
		}
		String search=dto.getSearch();
		if(StrUtil.isNotEmpty(search)) {
			maps.put("search", "%"+search+"%");
//			if("SKU".equals(dto.getSearchtype())) {
//				Material material = iMaterialService.getBySku(userinfo.getCompanyid(),search);
//				if(material!=null) {
//					maps.put("materialid", material.getId());
//				}
//			}
		}else {
			maps.put("search", null);
		}
		String operator=dto.getOperator();
		if(StrUtil.isNotEmpty(operator)) {
			maps.put("operator", operator);
		}else {
			maps.put("operator", null);
		}
		List<String> formtypes=dto.getFormtype();
		if(formtypes!=null && formtypes.size()>0) {
			maps.put("formtypeList", formtypes);
		}else {
			maps.put("formtypeList", null);
		}
		String searchtype=dto.getSearchtype();
		if(StrUtil.isNotEmpty(searchtype)) {
			maps.put("searchtype", searchtype);
		}else {
			maps.put("searchtype", null);
		}
		String fromdatestr=dto.getFromDate();
		String enddatestr=dto.getToDate();
		if (StrUtil.isNotEmpty(enddatestr)) {
			maps.put("fromDate",fromdatestr );
			maps.put("toDate", enddatestr);
		} else {
			maps.put("fromDate",null );
			maps.put("toDate", null);
		}
		maps.put("shopid", userinfo.getCompanyid());
		IPage<Map<String, Object>> inventoryList =  inventoryRecordService.findRecordList(dto.getPage(),maps);
		return Result.success(inventoryList);
	}
	
	@GetMapping("/getFormTypeList")
	public Result<List<FormType>> getFormTypeListAction() {
		return Result.success(inventoryRecordService.getFormTypeList());
	}
	
	@PostMapping("/downloadRecordsExcel")
	public void downloadRecordsExcelAction(@RequestBody InventoryRecordDTO dto, HttpServletResponse response)  {
		Map<String,Object> maps=new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String warehouseid=dto.getWarehouseid();
		if(StrUtil.isNotEmpty(warehouseid)) {
			maps.put("warehouseid", warehouseid);
		}else {
			maps.put("warehouseid", null);
		}
		String search=dto.getSearch();
		if(StrUtil.isNotEmpty(search)) {
			maps.put("search", "%"+search+"%");
		}else {
			maps.put("search", null);
		}
		String operator=dto.getOperator();
		if(StrUtil.isNotEmpty(operator)) {
			maps.put("operator", operator);
		}else {
			maps.put("operator", null);
		}
		List<String> formtypes=dto.getFormtype();
		if(formtypes!=null && formtypes.size()>0) {
			maps.put("formtypeList", formtypes);
		}else {
			maps.put("formtypeList", null);
		}
		String searchtype=dto.getSearchtype();
		if(StrUtil.isNotEmpty(searchtype)) {
			maps.put("searchtype", searchtype);
		}else {
			maps.put("searchtype", null);
		}
		String fromdatestr=dto.getFromDate();
		String enddatestr=dto.getToDate();
		if (StrUtil.isNotEmpty(enddatestr)) {
			maps.put("fromDate",fromdatestr );
			maps.put("toDate", enddatestr);
		} else {
			maps.put("fromDate",null );
			maps.put("toDate", null);
		}
		maps.put("shopid", userinfo.getCompanyid());
		List<Map<String, Object>> recordlist = inventoryRecordService.findDownloadList(maps);
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=inventoryRecords" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			inventoryRecordService.setExcelBookInventoryRecReport(workbook,  recordlist);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
    @ApiOperation(value = "本地产品库存每天结余计算")
    @GetMapping("/summaryInventoryDay")
    public Result<?> summaryInvEveryDayAction() {
    	inventoryHisService.runTask();
    	return Result.success();
    }
    
    @ApiOperation(value = "sku的 时间段的历史库存")
    @PostMapping("/findInvDayList")
    public Result<Map<String, Object>> findInvDayListAction(@RequestBody InventoryRecordDTO dto) {
    	String fromDate=dto.getFromDate();
    	String toDate=dto.getToDate();
    	String warehouseid=dto.getWarehouseid();
    	UserInfo userinfo = UserInfoContext.get();
    	Material material = iMaterialService.findBySKU(dto.getSearch(), userinfo.getCompanyid());
    	if(material!=null) {
    		Map<String, Object> result=new HashMap<String, Object>();
    		Map<String, Object> param=new HashMap<String, Object>();
    		Date day = GeneralUtil.getDatez(fromDate); 
    		Calendar calendar=Calendar.getInstance();
    		calendar.setTime(day);
    		calendar.add(Calendar.DATE, -1);
        	param.put("fromDate", GeneralUtil.formatDate(calendar.getTime()));
        	param.put("toDate", toDate);
        	if(StrUtil.isEmpty(warehouseid)) {
        		param.put("warehouseid", null);
        	}else {
        		param.put("warehouseid",warehouseid);
        	}
    		param.put("materialid", material.getId());
    		param.put("shopid", userinfo.getCompanyid());
    		List<Map<String, Object>> list = inventoryHisService.findInvDayList(param);
    		Map<String,Object> recordMaps=inventoryRecordService.findSkuInvHistory( material.getId(),fromDate,toDate,warehouseid,userinfo.getCompanyid());
    		result.put("list", list);
    		result.put("record", recordMaps);
        	return Result.success(result);
    	}else {
    		return Result.success(null);
    	}
    }
    
    
	
	
	
	
	
	
	
}
