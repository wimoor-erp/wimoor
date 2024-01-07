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
import com.wimoor.erp.stock.pojo.dto.InWarehouseFormDTO;
import com.wimoor.erp.stock.pojo.dto.InWarehouseItemDTO;
import com.wimoor.erp.stock.pojo.entity.InWarehouseForm;
import com.wimoor.erp.stock.service.IInWarehouseFormEntryService;
import com.wimoor.erp.stock.service.IInWarehouseFormService;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "入库单接口")
@RestController
@RequestMapping("/api/v1/inventory/inwform")
@RequiredArgsConstructor
public class InWarehouseFormController{
	@Resource
	IInWarehouseFormService inWarehouseFormService;
	@Resource
	IInWarehouseFormEntryService inWarehouseFormEntryService;
	@Resource
	ISerialNumService serialNumService;
	@Resource
	IWarehouseService warehouseService;


	@GetMapping("/getData")
	public Result<Map<String, Object>> getData(String id) {
		Map<String, Object> data = inWarehouseFormService.findById(id);
		List<Map<String, Object>> inFormEntryList = inWarehouseFormEntryService.findFormDetailByFormid(data.get("id").toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("warehouseform", data);
		map.put("inFormEntryList", inFormEntryList);
		return Result.success(map);
	}

	@Transactional
	@PostMapping("/saveData")
	public Result<Map<String, Object>> addDataAction(@RequestBody InWarehouseItemDTO dto)  {
		UserInfo user = UserInfoContext.get();
		InWarehouseForm inWarehouseForm = new InWarehouseForm();
		String shopid = user.getCompanyid();
		inWarehouseForm.setShopid(shopid);
		String id = dto.getId();
		if (StrUtil.isNotEmpty(id)) {
			inWarehouseForm.setId(id);
		} else {
			try {
				inWarehouseForm.setNumber(serialNumService.readSerialNumber(shopid, "IN"));
			} catch (Exception e) {
				e.printStackTrace();
				try {
					inWarehouseForm.setNumber(serialNumService.readSerialNumber(shopid, "IN"));
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new BizException("编码获取失败,请联系管理员");
				}
			}
			inWarehouseForm.setId(warehouseService.getUUID());
			inWarehouseForm.setCreator(user.getId());
			inWarehouseForm.setCreatedate(new Date());
			inWarehouseForm.setAuditor(user.getId());
			inWarehouseForm.setAuditstatus(2);// 0：未提交，1：提交未审核，2：已审核
		}
		inWarehouseForm.setOperator(user.getId());
		inWarehouseForm.setOpttime(new Date());
		String warehouseid = dto.getWarehouseid();
		String remark = dto.getRemark();
		inWarehouseForm.setWarehouseid(warehouseid);
		inWarehouseForm.setRemark(remark);
		inWarehouseForm.setAudittime(new Date());
		String sku = dto.getSkumapstr();// {"b1662cd7-b3c1-4e93-9389-44e704f9f542":"5","04585a54-364e-4066-b790-ad6a70e1cf34":"10"}
		Map<String, Object> map = inWarehouseFormService.saveAction(inWarehouseForm, sku, user);
		return Result.success(map);
	}

	@PostMapping("/list")
	public Result<IPage<Map<String, Object>>> getListData(@RequestBody InWarehouseFormDTO dto){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo user = UserInfoContext.get();
		String search = dto.getSearch();
		String fromDate = dto.getFromDate();
		String toDate = dto.getToDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isEmpty(search)) {
			search = null;
		} else {
			search = "%" + search.trim() + "%";
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
		map.put("search", search);
		map.put("shopid", user.getCompanyid());
		IPage<Map<String, Object>> list = inWarehouseFormService.findByCondition(dto.getPage(),map);
		return Result.success(list);
	}

	@PostMapping(value = "/uploadExcel",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<String> uploadExcelAction(@RequestParam("file")MultipartFile file){
		UserInfo user = UserInfoContext.get();
		if (file != null) {
			try {
				InputStream inputStream = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(inputStream);
				Sheet sheet = workbook.getSheetAt(0);
				String result = inWarehouseFormService.uploadInStockByExcel(sheet, user);
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

	@GetMapping("/downExcelTemp")
	public void downExcelTempAction(HttpServletResponse response) {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=stock-in-template.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			Sheet sheet = workbook.createSheet("inventory");
			sheet.setColumnWidth(0, 40 * 256);
			sheet.setColumnWidth(1, 40 * 256);
			// 在索引0的位置创建行（最顶端的行）
			Row row = sheet.createRow(0);
			row.setHeight((short) (25 * 20));
			Cell cell = row.createCell(0);
			cell.setCellValue("仓库名称:");
			CellStyle cs = workbook.createCellStyle();
			cs.setAlignment(HorizontalAlignment.RIGHT);// 水平居中
			cs.setWrapText(true);//自动换行
            //设置自定义颜色  

			cs.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
			cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		 	 cell.setCellStyle(cs);
			
			Cell cell2 = row.createCell(1);
			CellStyle style = workbook.createCellStyle();
		 
			style.setBorderBottom(BorderStyle.MEDIUM);
			style.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cell2.setCellStyle(style);
			cell2.setCellValue("");

			 
			CellStyle cstitle = workbook.createCellStyle();
			cstitle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cstitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			Row titleRow = sheet.createRow(1);
			titleRow.setHeight((short) (25 * 15));
			Cell title1 = titleRow.createCell(0);
			title1.setCellStyle(cstitle);
			title1.setCellValue("SKU");
			Cell title2 = titleRow.createCell(1);
			title2.setCellStyle(cstitle);
			title2.setCellValue("数量");
			Sheet sheet2 = workbook.createSheet("templateExample");
			
			sheet2.setColumnWidth(0, 40 * 256);
			sheet2.setColumnWidth(1, 40 * 256);

			Row erow = sheet2.createRow(0);
			erow.setHeight((short) (25 * 20));
			Cell ecell = erow.createCell(0);
			ecell.setCellValue("仓库名称");
			ecell.setCellStyle(cs);
			Cell ecell2 = erow.createCell(1);
			ecell2.setCellStyle(style);
			ecell2.setCellValue("你的仓库A-正品仓");
			Row etitleRow = sheet2.createRow(1);
			etitleRow.setHeight((short) (25 * 15));
			Cell etitle1 = etitleRow.createCell(0);
			
			etitle1.setCellStyle(cstitle);
			etitle1.setCellValue("SKU");
			Cell etitle2 = etitleRow.createCell(1);
			etitle2.setCellStyle(cstitle);
			etitle2.setCellValue("数量");
			Row ecotentRow = sheet2.createRow(2);
			Cell ece1 = ecotentRow.createCell(0);
			ece1.setCellValue("SKU001");
			Cell ece22 = ecotentRow.createCell(1);
			ece22.setCellValue("22");
			Row ecotentRow2 = sheet2.createRow(3);
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
			inWarehouseFormService.deleteOtherInInventory(user, id);
			inWarehouseFormEntryService.deleteByFormid(id);
			inWarehouseFormService.removeById(id);
		}
		return Result.success("操作成功！");
	}



	 

}
