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
import org.apache.poi.ss.usermodel.CellType;
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
import com.wimoor.erp.stock.pojo.dto.OutWarehouseFormDTO;
import com.wimoor.erp.stock.pojo.dto.OutWarehouseItemDTO;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseForm;
import com.wimoor.erp.stock.service.IOutWarehouseFormEntryService;
import com.wimoor.erp.stock.service.IOutWarehouseFormService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "出库单接口")
@RestController
@RequestMapping("/api/v1/inventory/outwform")
@RequiredArgsConstructor
public class OutWarehouseFormController {
	@Resource
	IOutWarehouseFormService outWarehouseFormService;
	@Resource
	IOutWarehouseFormEntryService outWarehouseFormEntryService;
	@Resource
	ISerialNumService serialNumService;
	 
	@PostMapping("/list")
	public Result<IPage<Map<String, Object>>> getListData(@RequestBody OutWarehouseFormDTO dto){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo user = UserInfoContext.get();
		String search = dto.getSearch();
		String warehouseid = dto.getWarehouseid();
		String fromDate = dto.getFromDate();
		String toDate = dto.getToDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isEmpty(search)) {
			search = null;
		} else {
			search = "%" + search.trim() + "%";
		}
		map.put("search", search);
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		} 
		map.put("warehouseid", warehouseid);
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
		map.put("shopid", user.getCompanyid());
		IPage<Map<String, Object>> list = outWarehouseFormService.findByCondition(dto.getPage(),map);
		return Result.success(list);
	}
	
	@GetMapping("/getlast")
	public Result<Map<String,Object>> getlastAction(String warehouseid,String materialid){
		UserInfo user = UserInfoContext.get();
	    return Result.success(outWarehouseFormEntryService.findLast(user.getCompanyid(), warehouseid, materialid));
	}
	
	@GetMapping("/getListDetailExport")
	public void getListDetailExportAction(String search,String warehouseid,String fromDate,String toDate,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo user = UserInfoContext.get();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isEmpty(search)) {
			search = null;
		} else {
			search = "%" + search.trim() + "%";
		}
		map.put("search", search);
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		} 
		map.put("warehouseid", warehouseid);
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
		map.put("shopid", user.getCompanyid());
		try {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=stockOutDetail" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			outWarehouseFormService.getListDetailForExport(workbook, map);
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping(value = "/uploadExcel",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<String> uploadExcelAction(@RequestParam("file")MultipartFile file){
		UserInfo user = UserInfoContext.get();
		if (file != null) {
			try {
				InputStream inputStream = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(inputStream);
				Sheet sheet = workbook.getSheetAt(0);
				String result = outWarehouseFormService.uploadOutStockByExcel(sheet, user);
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
			}
		}
		return Result.failed();
	}

	@GetMapping("/downExcelTemp")
	public void downExcelTempAction(HttpServletResponse response) {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=stock-out-template.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			Sheet sheet = workbook.createSheet("template");
			// 在索引0的位置创建行（最顶端的行）
			sheet.setColumnWidth(0, 40 * 256);
			sheet.setColumnWidth(1, 40 * 256);
			CellStyle formleft = workbook.createCellStyle();
			formleft.setAlignment(HorizontalAlignment.RIGHT);// 水平居中
			formleft.setWrapText(true);//自动换行
			formleft.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
			formleft.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			CellStyle formright = workbook.createCellStyle();
			formright.setBorderBottom(BorderStyle.MEDIUM);
			formright.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
			formright.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			CellStyle cstitle = workbook.createCellStyle();
			cstitle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cstitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			Row row = sheet.createRow(0);
			row.setHeight((short) (25 * 15));
			Cell cell = row.createCell(0);
			cell.setCellStyle(formleft);
			cell.setCellValue("仓库名称(必填):");
			Cell cellright1 = row.createCell(1);
			cellright1.setCellStyle(formright);
			cellright1.setCellValue("");
			
			Row row1 = sheet.createRow(1);
			row1.setHeight((short) (25 * 15));
			Cell cell1 = row1.createCell(0);
			cell1.setCellStyle(formleft);
			cell1.setCellValue("客户简称:");
			cellright1 = row1.createCell(1);
			cellright1.setCellStyle(formright);
			cellright1.setCellValue("");
			Row row2 = sheet.createRow(2);
			row2.setHeight((short) (25 * 15));
			Cell cell2 = row2.createCell(0);
			cell2.setCellStyle(formleft);
			cell2.setCellValue("发货地址:");
			cellright1 = row2.createCell(1);
			cellright1.setCellStyle(formright);
			cellright1.setCellValue("");
			Row row3 = sheet.createRow(3);
			row3.setHeight((short) (25 * 15));
			Cell cell3 = row3.createCell(0);
			cell3.setCellStyle(formleft);
			cell3.setCellValue("物流快递:");
			cellright1 = row3.createCell(1);
			cellright1.setCellStyle(formright);
			cellright1.setCellValue("");
			Row row4 = sheet.createRow(4);
			row4.setHeight((short) (25 * 15));
			Cell cell4 = row4.createCell(0);
			cell4.setCellValue("快递单号:");
			cell4.setCellStyle(formleft);
			cellright1 = row4.createCell(1);
			cellright1.setCellStyle(formright);
			cellright1.setCellValue("");
			cellright1.setCellType(CellType.STRING);
			Row row5 = sheet.createRow(5);
			row5.setHeight((short) (25 * 15));
			Cell cell5 = row5.createCell(0);
			cell5.setCellStyle(formleft);
			cell5.setCellValue("备注:");
			cellright1 = row5.createCell(1);
			cellright1.setCellStyle(formright);
			cellright1.setCellValue("");
			cellright1.setCellType(CellType.STRING);
			Row titleRow = sheet.createRow(6);
			titleRow.setHeight((short) (25 * 15));
			Cell title1 = titleRow.createCell(0);
			title1.setCellStyle(cstitle);
			title1.setCellValue("SKU(必填)");
			Cell title2 = titleRow.createCell(1);
			title2.setCellStyle(cstitle);
			title2.setCellValue("数量(必填)");

			Sheet sheet2 = workbook.createSheet("templateExample");
			sheet2.setColumnWidth(0, 40 * 256);
			sheet2.setColumnWidth(1, 40 * 256);
			Row erow = sheet2.createRow(0);
			erow.setHeight((short) (25 * 15));
			Cell ecell = erow.createCell(0);
			ecell.setCellStyle(formleft);
			ecell.setCellValue("仓库名称:");
			Cell ecell_ = erow.createCell(1);
			ecell_.setCellStyle(formright);
			ecell_.setCellValue("你的仓库A");
			Row erow1 = sheet2.createRow(1);
			erow1.setHeight((short) (25 * 15));
			Cell ecell1 = erow1.createCell(0);
			ecell1.setCellStyle(formleft);
			ecell1.setCellValue("客户简称:");
			Cell ecell1_ = erow1.createCell(1);
			ecell1_.setCellStyle(formright);
			ecell1_.setCellValue("通用客户A");
			Row erow2 = sheet2.createRow(2);
			erow2.setHeight((short) (25 * 15));
			Cell ecell2 = erow2.createCell(0);
			ecell2.setCellStyle(formleft);
			ecell2.setCellValue("发货地址:");
			Cell ecell2_ = erow2.createCell(1);
			ecell2_.setCellStyle(formright);
			ecell2_.setCellValue("你的发货地址");
			Row erow3 = sheet2.createRow(3);
			erow3.setHeight((short) (25 * 15));
			Cell ecell3 = erow3.createCell(0);
			ecell3.setCellValue("物流快递:");
			ecell3.setCellStyle(formleft);
			Cell ecell3_ = erow3.createCell(1);
			ecell3_.setCellStyle(formright);
			ecell3_.setCellValue("XXX");
			Row erow4 = sheet2.createRow(4);
			erow4.setHeight((short) (25 * 15));
			Cell ecell4 = erow4.createCell(0);
			ecell4.setCellValue("快递单号:");
			ecell4.setCellStyle(formleft);
			Cell ecell4_ = erow4.createCell(1);
			ecell4_.setCellStyle(formright);
			ecell4_.setCellValue("000");
			Row erow5 = sheet2.createRow(5);
			erow5.setHeight((short) (25 * 15));
			Cell ecell5 = erow5.createCell(0);
			ecell5.setCellStyle(formleft);
			ecell5.setCellValue("备注:");
			Cell ecell5_ = erow5.createCell(1);
			ecell5_.setCellStyle(formright);
			ecell5_.setCellValue("你的备注");
			Row etitleRow = sheet2.createRow(6);
			etitleRow.setHeight((short) (25 * 15));
			Cell etitle1 = etitleRow.createCell(0);
			etitle1.setCellValue("SKU");
			etitle1.setCellStyle(cstitle);
			Cell etitle2 = etitleRow.createCell(1);
			etitle2.setCellValue("数量");
			etitle2.setCellStyle(cstitle);
			Row ecotentRow = sheet2.createRow(7);
			Cell ece1 = ecotentRow.createCell(0);
			ece1.setCellValue("SKU001");
			Cell ece22 = ecotentRow.createCell(1);
			ece22.setCellValue("22");
			Row ecotentRow2 = sheet2.createRow(8);
			Cell eceSKU002 = ecotentRow2.createCell(0);
			eceSKU002.setCellValue("SKU002");
			Cell ece15 = ecotentRow2.createCell(1);
			ece15.setCellValue("15");
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional 
	@GetMapping("/deleteData")
	public Result<String> deleteDataAction(String ids){
		UserInfo user = UserInfoContext.get();
		String[] idlist = ids.split(",");
		for (int i = 0; i < idlist.length; i++) {
			String id = idlist[i];
			outWarehouseFormService.deleteOtherOutInventory(user,id);
			outWarehouseFormEntryService.deleteByFormid(id);
			outWarehouseFormService.removeById(id);
		}
		return Result.success("操作成功！");
	}

	@GetMapping("getData")
	public Result<Map<String, Object>> getData(String id) {
		Map<String, Object> data = outWarehouseFormService.findById(id);
		List<Map<String, Object>> outFormEntryList = outWarehouseFormEntryService.findFormDetailByFormid(data.get("id").toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("warehouseform", data);
		map.put("outFormEntryList", outFormEntryList);
		return Result.success(map);
	}

	@Transactional
	@PostMapping("/saveData")
	public Result<Map<String, Object>> addData(@RequestBody OutWarehouseItemDTO dto) {
		UserInfo user = UserInfoContext.get();
		OutWarehouseForm outWarehouseForm = new OutWarehouseForm();
		outWarehouseForm.setShopid(user.getCompanyid());
		String id = dto.getId();
		String warehouseid = dto.getWarehouseid();
		String purchaser = dto.getPurchaser();
		String toaddress = dto.getToaddress();
		String express = dto.getExpress();
		String expressno = dto.getExpressno();
		String remark = dto.getRemark();
		String skumapstr = dto.getSkumapstr();
		String customer=dto.getCustomer();
		Integer ftype=dto.getFtype();
		if (StrUtil.isEmpty(skumapstr)) {
			throw new BizException("出库商品不能为空！");
		}
		if (StrUtil.isNotEmpty(id)) {
			outWarehouseForm.setId(id);
		} else {
			try {
				outWarehouseForm.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "O"));
			} catch (Exception e) {
				e.printStackTrace();
				try {
					outWarehouseForm.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "O"));
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new BizException("编码获取失败,请联系管理员");
				}
			}
			outWarehouseForm.setAuditor(user.getId());
			outWarehouseForm.setAuditstatus(2);// 0：未提交，1：提交未审核，2：已审核
			outWarehouseForm.setCreator(user.getId());
			outWarehouseForm.setCreatedate(new Date());
		}
		outWarehouseForm.setAudittime(new Date());
		outWarehouseForm.setOpttime(new Date());
		outWarehouseForm.setOperator(user.getId());
		outWarehouseForm.setWarehouseid(warehouseid);
		outWarehouseForm.setFtype(ftype);
		if (StrUtil.isNotEmpty(purchaser)) {
			outWarehouseForm.setPurchaser(purchaser);
		}
		if (StrUtil.isNotEmpty(toaddress)) {
			outWarehouseForm.setToaddress(toaddress);
		}
		if (StrUtil.isNotEmpty(customer)) {
			outWarehouseForm.setCustomer(customer);
		}
		outWarehouseForm.setExpress(express);
		outWarehouseForm.setExpressno(expressno);
		outWarehouseForm.setRemark(remark);
		Map<String, Object> map = outWarehouseFormService.saveAction(outWarehouseForm, skumapstr, user);
		return Result.success(map);
	}
}
