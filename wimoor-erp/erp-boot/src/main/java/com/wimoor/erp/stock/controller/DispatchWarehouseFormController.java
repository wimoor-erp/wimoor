package com.wimoor.erp.stock.controller;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.ui.Model;
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
import com.wimoor.common.mvc.DictSerializer;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.stock.pojo.dto.DispatchWarehouseFormDTO;
import com.wimoor.erp.stock.pojo.dto.DispatchWarehouseItemDTO;
import com.wimoor.erp.stock.pojo.entity.DispatchForm;
import com.wimoor.erp.stock.service.IDispatchFormEntryService;
import com.wimoor.erp.stock.service.IDispatchFormService;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "调库单接口")
@RestController
@RequestMapping("/api/v1/inventory/dispatchform")
@RequiredArgsConstructor
public class DispatchWarehouseFormController{
		@Resource
		IDispatchFormService dispatchFormService;
		@Resource
		IDispatchFormEntryService dispatchFormEntryService;
		@Resource
		ISerialNumService serialNumService;
		@Resource
		IMaterialService materialService;
		@Resource
		IInventoryService inventoryService;
	    @Resource
	    IWarehouseService warehouseService;

	    @PostMapping("/list")
		public Result<IPage<Map<String, Object>>> getListData(@RequestBody DispatchWarehouseFormDTO dto){
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			String search = dto.getSearch();
			String auditstatus =dto.getAuditstatus();
			if (StrUtil.isEmpty(search)) {
				search = null;
			} else {
				search = search.trim() + "%";
			}
			List<Integer> statusList = new ArrayList<Integer>();
			if (StrUtil.isEmpty(auditstatus)) {
				statusList = null;
			} else if (auditstatus.equals("weichuli")) {
				statusList.add(0);
				statusList.add(1);
			} else if (auditstatus.equals("yichuli")) {
				statusList.add(2);
				statusList.add(3);
			} else {
				statusList.add(Integer.parseInt(auditstatus));
			}
			String fromDate = dto.getFromDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				map.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -30);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				map.put("fromDate", fromDate);
			}
			String toDate = dto.getToDate();
			if (StrUtil.isNotEmpty(toDate)) {
				map.put("endDate", toDate.trim());
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				map.put("endDate", toDate);
			}
			map.put("shopid", shopid);
			map.put("search", search);
			map.put("statusList", statusList);
			IPage<Map<String, Object>> list = dispatchFormService.findByCondition(dto.getPage(),map);
			return 	Result.success(list);	
		}

	    @Transactional
		@GetMapping("/deleteData")
		public Result<Integer> deleteAction(String ids){
			UserInfo user = UserInfoContext.get();
			return  Result.success(dispatchFormService.submitAction(user, ids, "5"));
		}


		@GetMapping("/downExcelTemp")
		public void downExcelTempAction(HttpServletResponse response) {
			try {
				// 创建新的Excel工作薄
				SXSSFWorkbook workbook = new SXSSFWorkbook();
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=stock-dispatch-template.xlsx");// 设置文件名
				ServletOutputStream fOut = response.getOutputStream();
				// 将数据写入Excel
				Sheet sheet = workbook.createSheet("模板格式");
				sheet.setColumnWidth(0, 40 * 256);
				sheet.setColumnWidth(1, 40 * 256);
				// 第一行
				Row row = sheet.createRow(0);
				row.setHeight((short) (25 * 20));
				Cell cell = row.createCell(0);
				cell.setCellValue("调出仓库:");
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
				Row row2 = sheet.createRow(1);
				row2.setHeight((short) (25 * 20));
				Cell cell21 = row2.createCell(0);
				cell21.setCellValue("调入仓库:");
				cell21.setCellStyle(cs);
			 	
				Cell cell22 = row2.createCell(1);
				cell22.setCellValue("");
				cell22.setCellStyle(style);
				
				//第三行
				Row row3 = sheet.createRow(2);
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
				
				//第四行
				Row titleRow = sheet.createRow(3);
				titleRow.setHeight((short) (25 * 15));
				Cell title1 = titleRow.createCell(0);
				title1.setCellStyle(cstitle);
				title1.setCellValue("SKU");
				Cell title2 = titleRow.createCell(1);
				title2.setCellStyle(cstitle);
				title2.setCellValue("数量");
				
				Sheet sheet2 = workbook.createSheet("模板示例");
				sheet2.setColumnWidth(0, 40 * 256);
				sheet2.setColumnWidth(1, 40 * 256);

				// 第一行
				Row erow = sheet2.createRow(0);
				erow.setHeight((short) (25 * 20));
				Cell ecell = erow.createCell(0);
				ecell.setCellValue("调出仓库:");
			 	ecell.setCellStyle(cs);
			 	
				Cell ecell2 = erow.createCell(1);
				ecell2.setCellValue("你的仓库A-测试仓");
				ecell2.setCellStyle(style);
				
				//第二行
				Row erow2 = sheet2.createRow(1);
				erow2.setHeight((short) (25 * 20));
				Cell ecell21 = erow2.createCell(0);
				ecell21.setCellValue("调入仓库:");
				ecell21.setCellStyle(cs);
			 	
				Cell ecell22 = erow2.createCell(1);
				ecell22.setCellValue("你的仓库A-正品仓");
				ecell22.setCellStyle(style);
				
				//第三行
				Row erow3 = sheet2.createRow(2);
				erow3.setHeight((short) (25 * 20));
				Cell ecell31 = erow3.createCell(0);
				ecell31.setCellValue("调库单备注:");
			 	ecell31.setCellStyle(cs2);
			 	
				Cell ecell32 = erow3.createCell(1);
				ecell32.setCellValue("");
				ecell32.setCellStyle(style2);
				
				//第四行
				Row etitleRow = sheet2.createRow(3);
				etitleRow.setHeight((short) (25 * 15));
				Cell etitle1 = etitleRow.createCell(0);
				etitle1.setCellStyle(cstitle);
				etitle1.setCellValue("SKU");
				Cell etitle2 = etitleRow.createCell(1);
				etitle2.setCellStyle(cstitle);
				etitle2.setCellValue("数量");
				
				//内容
				Row ecotentRow = sheet2.createRow(4);
				Cell ece1 = ecotentRow.createCell(0);
				ece1.setCellValue("SKU001");
				Cell ece22 = ecotentRow.createCell(1);
				ece22.setCellValue("22");
				Row ecotentRow2 = sheet2.createRow(5);
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
		
		@PostMapping(value = "/uploadExcel",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public Result<String> uploadExcelAction(@RequestParam("file")MultipartFile file){
			UserInfo user = UserInfoContext.get();
			if (file != null) {
				try {
					InputStream inputStream = file.getInputStream();
					Workbook workbook = WorkbookFactory.create(inputStream);
					Sheet sheet = workbook.getSheetAt(0);
					String result = dispatchFormService.uploadDispatchStockByExcel(sheet, user);
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
		
		@GetMapping("/getData")
		public Result<Map<String, Object>> getDataAction(HttpServletRequest request, HttpServletResponse response, Model model) {
			String id = request.getParameter("id");
			Map<String, Object> data = dispatchFormService.findById(id);
			String ftype=data!=null&&data.get("ftype")!=null?  data.get("ftype").toString():"0";
			String ftypeName=DictSerializer.getDictDataOptions("dispatch_form_type", ftype);
			data.put("ftypeName", ftypeName);
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			List<Map<String, Object>> dispatchFormEntryList = dispatchFormEntryService.findFormDetailByFormid(id,
					data.get("to_warehouseid").toString(), data.get("from_warehouseid").toString(), shopid);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("warehouseform", data);
			map.put("dispatchFormEntryList", dispatchFormEntryList);
			return Result.success(map);
		}

		//提交调库
		@Transactional
		@PostMapping("/saveData")
		public Result<Map<String, Object>> addDataAction(@RequestBody DispatchWarehouseItemDTO dto) {
			UserInfo user = UserInfoContext.get();
			DispatchForm dispatchForm = new DispatchForm();
			String shopid =user.getCompanyid();
			dispatchForm.setShopid(shopid);
			String id = dto.getId();
			String action = dto.getAction();
			if (action.equals("save")) {
				dispatchForm.setAuditstatus(0);//0：未提交，1：提交未审核，2：已审核 ，3：已驳回
			} else if(action.equals("passdone")) {
				dispatchForm.setAuditstatus(4);
			} else {
				dispatchForm.setAuditstatus(1);
			}
			if (StrUtil.isNotEmpty(id)) {
				dispatchForm.setId(id);
			} else {
				try {
					dispatchForm.setNumber(serialNumService.readSerialNumber(shopid, "PD"));
				} catch (Exception e) {
					e.printStackTrace();
					try {
						dispatchForm.setNumber(serialNumService.readSerialNumber(shopid, "PD"));
					} catch (Exception e1) {
						e1.printStackTrace();
						throw new BizException("编码获取失败,请联系管理员");
					}
				}
				dispatchForm.setAuditor(user.getId());
				dispatchForm.setCreator(user.getId());
				dispatchForm.setCreatedate(new Date());
				dispatchForm.setId(warehouseService.getUUID());
			}
			dispatchForm.setOperator(user.getId());
			String fromwarehouseid = dto.getFromwarehouseid();
			String towarehouseid = dto.getTowarehouseid();
			String arrivalTime = dto.getArrivaltime();
			String remark = dto.getRemark();
			dispatchForm.setFromWarehouseid(fromwarehouseid);
			dispatchForm.setToWarehouseid(towarehouseid);
			dispatchForm.setRemark(remark);
			dispatchForm.setFtype(dto.getFtype());
			dispatchForm.setAudittime(new Date());
			dispatchForm.setOpttime(new Date());
			if(!StrUtil.isEmpty(arrivalTime)) {
				dispatchForm.setArrivalTime(GeneralUtil.getDatez(arrivalTime));
			}
			String skumapstr = dto.getSkumapstr();// {"b1662cd7-b3c1-4e93-9389-44e704f9f542":"5","04585a54-364e-4066-b790-ad6a70e1cf34":"10"}
			if (StrUtil.isEmpty(skumapstr)) {
				throw new BizException("SKU调库列表不能为空");
			}
			Map<String, Object> map = dispatchFormService.saveAction(user,dispatchForm, skumapstr);
//			if(StrUtil.isNotEmpty(dto.getPlanitemidstr())) {
//				String[] planitemArr = dto.getPlanitemidstr().split(",");
//				if(planitemArr.length>0) {
//					String itemid = planitemArr[0];
//					if(StrUtil.isNotEmpty(itemid)) {
//						ShipPlanItem sitem = shipPlanItemMapper.selectById(itemid);
//						ShipPlanSub plansub = shipPlanSubService.getById(sitem.getPlansubid());
//						shipPlanService.afterShipPlanItemShiped(plansub, "self",null);
//					 }
//				   }
//			}
			return Result.success(map);
		}

		@Transactional
		@GetMapping("/examine")
		public Result<String> examineAction(String status,String ids){
			UserInfo user = UserInfoContext.get();
			int result = dispatchFormService.submitAction(user,ids,status);
			String msg = "";
			if (result > 0) {
				msg += "操作成功！";
			} else {
				msg += "操作失败！";
			}
			return Result.success(msg);
		}
		


}
