package com.wimoor.erp.stock.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.stock.pojo.dto.ChangeWarehouseFormDTO;
import com.wimoor.erp.stock.pojo.dto.ChangeWarehouseItemDTO;
import com.wimoor.erp.stock.pojo.entity.ChangeWhForm;
import com.wimoor.erp.stock.service.IChangeWhFormEntryService;
import com.wimoor.erp.stock.service.IChangeWhFormService;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "互调单接口")
@RestController
@RequestMapping("/api/v1/inventory/changeform")
@RequiredArgsConstructor
public class ChangeWarehouseFormController{
	@Resource
	IChangeWhFormService changeWhFormService;
	@Resource
	IChangeWhFormEntryService changeWhFormEntryService;
	@Resource
	ISerialNumService serialNumService;
	@Resource
	IWarehouseService warehouseService;

	@PostMapping("/list")
	public Result<IPage<Map<String, Object>>> getListData(@RequestBody ChangeWarehouseFormDTO dto){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String search = dto.getSearch();
		String fromDate = dto.getFromDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String toDate = dto.getToDate();
		if (StrUtil.isEmpty(search)) {
			search = null;
		} else {
			search = "%"+search.trim() + "%";
		}
		if (StrUtil.isNotEmpty(fromDate)) {
			map.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -30);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			map.put("fromDate", fromDate);
		}
		if (StrUtil.isNotEmpty(toDate)) {
			map.put("endDate", toDate.trim());
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			map.put("endDate", toDate);
		}
		map.put("shopid", shopid);
		map.put("search", search);
		return Result.success(changeWhFormService.findByCondition(dto.getPage(),map));
	}

	@Transactional
	@GetMapping("/deleteData")
	public Result<String> deleteDataAction(String ids) {
		String[] idlist = ids.split(",");
		UserInfo user = UserInfoContext.get();
		for (int i = 0; i < idlist.length; i++) {
			String id = idlist[i];
			changeWhFormService.deleteChangeWhForm(user,id);
			changeWhFormEntryService.deleteByFormid(id);
			changeWhFormService.removeById(id);
		}
		return Result.success("操作成功！");
	}

	@GetMapping("/getData")
	public Result<Map<String, Object>> getDataAction(String id) {
		Map<String, Object> data = changeWhFormService.findById(id);
		List<Map<String, Object>> formEntryList = changeWhFormEntryService.findFormDetailByFormid(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("warehouseform", data);
		map.put("formEntryList", formEntryList);
		return Result.success(map);
	}

	@Transactional
	@PostMapping("/saveData")
	public Result<Map<String, Object>> addDataAction(@RequestBody ChangeWarehouseItemDTO dto) {
		UserInfo user = UserInfoContext.get();
		String skumapstr = dto.getSkumapstr();
		if(StrUtil.isEmpty(skumapstr)){
			throw new BizException("SKU列表不能为空");
		}
		ChangeWhForm form = new ChangeWhForm();
		String shopid = user.getCompanyid();
		form.setShopid(shopid);
		String id = dto.getId();
		if (StrUtil.isNotEmpty(id)) {
			form.setId(id);
		} else {
			try {
				form.setNumber(serialNumService.readSerialNumber(shopid, "CH"));
			} catch (Exception e) {
				e.printStackTrace();
				try {
					form.setNumber(serialNumService.readSerialNumber(shopid, "CH"));
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new BizException("编码获取失败,请联系管理员");
				}
			}
			form.setCreator(user.getId());
			form.setCreatedate(new Date());
			form.setAuditstatus(1);
			form.setId(warehouseService.getUUID());
		}
		String warehouseid = dto.getWarehouseid();
		String remark = dto.getRemark();
		form.setWarehouseid(warehouseid);
		form.setRemark(remark);
		form.setOperator(user.getId());
		form.setOpttime(new Date());
		List<Map<String, Object>> skuList = GeneralUtil.jsonStringToMapList(skumapstr);
		Map<String, Object> map = changeWhFormService.saveAction(form, skuList, user);
		return Result.success(map);
	}

	@GetMapping("/downExcelTemp")
	public void downExcelTempAction(HttpServletResponse response) {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=stock-change-template.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			Sheet sheet = workbook.createSheet("模板格式");
			sheet.setColumnWidth(0, 40 * 256);
			sheet.setColumnWidth(1, 40 * 256);
			sheet.setColumnWidth(2, 40 * 256);
			// 第一行
			Row row = sheet.createRow(0);
			row.setHeight((short) (25 * 20));
			Cell cell = row.createCell(0);
			cell.setCellValue("操作仓库:");
			CellStyle cs = workbook.createCellStyle();
			cs.setAlignment(HorizontalAlignment.RIGHT);
			cs.setWrapText(true);//自动换行
            //设置自定义颜色  
			cs.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
			cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		 	cell.setCellStyle(cs);
		 	
			Cell cell2 = row.createCell(1);
			cell2.setCellValue("");
			CellStyle style = workbook.createCellStyle();
			style.setBorderBottom(BorderStyle.MEDIUM);
			style.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cell2.setCellStyle(style);
			
			//第二行
			Row row3 = sheet.createRow(1);
			row3.setHeight((short) (25 * 20));
			Cell cell31 = row3.createCell(0);
			cell31.setCellValue("调库单备注:");
			CellStyle cs2 = workbook.createCellStyle();
			cs2.setAlignment(HorizontalAlignment.RIGHT);
			cs2.setWrapText(true);//自动换行
			//设置自定义颜色  
			cs2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cell31.setCellStyle(cs2);
			
			Cell cell32 = row3.createCell(1);
			cell32.setCellValue("");
			CellStyle style2 = workbook.createCellStyle();
			style2.setBorderBottom(BorderStyle.MEDIUM);
			style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cell32.setCellStyle(style2);
			
			CellStyle cstitle = workbook.createCellStyle();
			cstitle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cstitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			//第三行
			Row titleRow = sheet.createRow(2);
			titleRow.setHeight((short) (25 * 15));
			Cell title1 = titleRow.createCell(0);
			title1.setCellStyle(cstitle);
			title1.setCellValue("调出SKU");
			Cell title2 = titleRow.createCell(1);
			title2.setCellStyle(cstitle);
			title2.setCellValue("调入SKU");
			Cell title3 = titleRow.createCell(2);
			title3.setCellStyle(cstitle);
			title3.setCellValue("数量");
			
			Sheet sheet2 = workbook.createSheet("模板示例");
			sheet2.setColumnWidth(0, 40 * 256);
			sheet2.setColumnWidth(1, 40 * 256);
			sheet2.setColumnWidth(2, 40 * 256);
			
			// 第一行
			Row erow = sheet2.createRow(0);
			erow.setHeight((short) (25 * 20));
			Cell ecell = erow.createCell(0);
			ecell.setCellValue("操作仓库:");
		 	ecell.setCellStyle(cs);
		 	
			Cell ecell2 = erow.createCell(1);
			ecell2.setCellValue("你的仓库A-正品仓");
			ecell2.setCellStyle(style);
			
			//第二行
			Row erow3 = sheet2.createRow(1);
			erow3.setHeight((short) (25 * 20));
			Cell ecell31 = erow3.createCell(0);
			ecell31.setCellValue("调库单备注:");
			ecell31.setCellStyle(cs2);
			
			Cell ecell32 = erow3.createCell(1);
			ecell32.setCellValue("");
			ecell32.setCellStyle(style2);
			
			//第三行
			Row etitleRow = sheet2.createRow(2);
			etitleRow.setHeight((short) (25 * 15));
			Cell etitle1 = etitleRow.createCell(0);
			etitle1.setCellStyle(cstitle);
			etitle1.setCellValue("调出SKU");
			Cell etitle2 = etitleRow.createCell(1);
			etitle2.setCellStyle(cstitle);
			etitle2.setCellValue("调入SKU");
			Cell etitle3 = etitleRow.createCell(2);
			etitle3.setCellStyle(cstitle);
			etitle3.setCellValue("数量");
			
			//内容
			Row ecotentRow = sheet2.createRow(3);
			Cell ece1 = ecotentRow.createCell(0);
			ece1.setCellValue("SKU001");
			Cell ece2 = ecotentRow.createCell(1);
			ece2.setCellValue("SKU002");
			Cell ece3 = ecotentRow.createCell(2);
			ece3.setCellValue("20");
			Row ecotentRow2 = sheet2.createRow(4);
			Cell eceSKU002 = ecotentRow2.createCell(0);
			eceSKU002.setCellValue("SKU003");
			Cell eceSKU0022 = ecotentRow2.createCell(1);
			eceSKU0022.setCellValue("SKU004");
			Cell ecell3 = ecotentRow2.createCell(2);
			ecell3.setCellValue("15");
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping(value = "/uploadExcel",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<String> uploadExcelAction(@RequestParam("file")MultipartFile file) {
		UserInfo user = UserInfoContext.get();
		if (file != null) {
			try {
				InputStream inputStream = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(inputStream);
				Sheet sheet = workbook.getSheetAt(0);
				String result = changeWhFormService.uploadchangeStockByExcel(sheet, user);
				workbook.close();
				return Result.success(result);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncryptedDocumentException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (BizException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Result.failed();
	}
	
	@Transactional
	@GetMapping("/submitData")
	public Result<String> submitDataAction(String id){
		ChangeWhForm form = new ChangeWhForm();
		form.setId(id);
		form.setAuditstatus(1);// 0：未提交，1：提交未审核，2：已审核
		boolean result = changeWhFormService.updateById(form);
		String msg = "更新";
		if (result==true) {
			msg += "成功！";
		} else {
			msg += "失败！";
		}
		return Result.success(msg);
	}



}
