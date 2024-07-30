package com.wimoor.erp.stock.controller;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.api.AmazonClientOneFeignManager;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.DimensionsInfo;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IDimensionsInfoService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.ship.mapper.ShipPlanItemMapper;
import com.wimoor.erp.ship.pojo.dto.ShipInboundItemVo;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import com.wimoor.erp.ship.service.IShipAmazonFormService;
import com.wimoor.erp.stock.pojo.dto.DispatchOverseaWarehouseItemDTO;
import com.wimoor.erp.stock.pojo.dto.DispatchWarehouseFormDTO;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaForm;
import com.wimoor.erp.stock.service.IErpDispatchOverseaFormEntryService;
import com.wimoor.erp.stock.service.IErpDispatchOverseaFormService;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
@Api(tags = "海外仓调库单接口")
@RestController
@RequestMapping("/api/v1/inventory/dispatch/oversea")
@RequiredArgsConstructor
public class ErpDispatchOverseaFormController {
	@Resource
	IErpDispatchOverseaFormService iErpDispatchOverseaFormService;
	@Resource
	IErpDispatchOverseaFormEntryService iErpDispatchOverseaFormEntryService;
	@Resource
	IWarehouseShelfInventoryService iWarehouseShelfInventoryService;
	@Resource
	ISerialNumService serialNumService;
	@Resource
	IMaterialService materialService;
	@Resource
	IInventoryService inventoryService;
	@Resource
	ShipPlanItemMapper shipPlanItemMapper;
    @Resource
    IWarehouseService warehouseService;
    @Resource
    AmazonClientOneFeignManager amazonClientOneFeign;
    @Resource
	IShipAmazonFormService iShipAmazonFormService;
    @Resource
    IDimensionsInfoService iDimensionsInfoService;
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
		if(StrUtil.isNotBlank(dto.getFromwid())) {
			map.put("fromwid", dto.getFromwid());
		}
        if(StrUtil.isNotBlank(dto.getTowid())) {
        	map.put("towid",dto.getTowid());
        }
		map.put("statusList", statusList);
		IPage<Map<String, Object>> list = iErpDispatchOverseaFormService.findByCondition(dto.getPage(),map);
		for(Map<String, Object> item:list.getRecords()) {
			try {
				Object groupid=item.get("groupid");
				if(groupid!=null) {
					Result<?> result = amazonClientOneFeign.getAmazonGroupByIdAction(groupid.toString());
					if(Result.isSuccess(result)&&result.getData()!=null) {
			            Map<String, Object> groupmap = BeanUtil.beanToMap(result.getData());	
			            if(groupmap!=null) {
			            	item.put("groupname",groupmap.get("name"));
			            }
					}
				}
			}catch(FeignException e) {
				BizException.getMessage(e, "获取店铺名称失败");
			}
		}
		return 	Result.success(list);	
	}

    @Transactional
	@GetMapping("/deleteData")
	public Result<Integer> deleteAction(String ids){
		UserInfo user = UserInfoContext.get();
		return  Result.success(iErpDispatchOverseaFormService.submitAction(user, ids, "5"));
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
				String result = iErpDispatchOverseaFormService.uploadDispatchStockByExcel(sheet, user);
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
	public Result<Map<String, Object>> getDataAction(String id) {
		Map<String, Object> data = iErpDispatchOverseaFormService.findById(id);
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		List<Map<String, Object>> dispatchFormEntryList = iErpDispatchOverseaFormEntryService.findFormDetailByFormid(id,
				data.get("to_warehouseid").toString(), data.get("from_warehouseid").toString(), shopid);
		Map<String, Object> map = new HashMap<String, Object>();
		String groupid=null;
	
		try {
			if(data.get("groupid")!=null) {
				groupid=data.get("groupid").toString();
				Result<?> result = amazonClientOneFeign.getAmazonGroupByIdAction(groupid);
				if(Result.isSuccess(result)&&result.getData()!=null) {
		            Map<String, Object> groupmap = BeanUtil.beanToMap(result.getData());	
		            if(groupmap!=null) {
		            	data.put("groupname",groupmap.get("name"));
		            }
				}
			}
			
			Object country=data.get("country");
			if(country!=null) {
				Result<?> result2 = amazonClientOneFeign.getMarketplaceByCountryAction(country.toString());
				if(Result.isSuccess(result2)&&result2.getData()!=null) {
		            Map<String, Object> itemmap = BeanUtil.beanToMap(result2.getData());	
		            if(itemmap!=null) {
		            	data.put("marketplaceid",itemmap.get("marketplaceid"));
		            }
				}
			}
		}catch(FeignException e) {
			BizException.getMessage(e, "获取店铺名称失败");
		}
		map.put("warehouseform", data);
		map.put("dispatchFormEntryList", dispatchFormEntryList);
		return Result.success(map);
	}

	//提交调库
	@Transactional
	@PostMapping("/saveData")
	public Result<Map<String, Object>> addDataAction(@RequestBody DispatchOverseaWarehouseItemDTO dto) {
		UserInfo user = UserInfoContext.get();
		ErpDispatchOverseaForm dispatchForm = new ErpDispatchOverseaForm();
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
		dispatchForm.setFromWarehouseid(new BigInteger(fromwarehouseid));
		dispatchForm.setToWarehouseid(new BigInteger(towarehouseid));
		if(StrUtil.isNotBlank(dto.getGroupid())) {
			dispatchForm.setGroupid(new BigInteger(dto.getGroupid()));
		}
		if(StrUtil.isNotBlank(dto.getCountry())) {
			dispatchForm.setCountry(dto.getCountry());
		}
		dispatchForm.setRemark(remark);
		dispatchForm.setAudittime(new Date());
		dispatchForm.setOpttime(new Date());
		if(!StrUtil.isEmpty(arrivalTime)) {
			dispatchForm.setArrivalTime(GeneralUtil.getDatez(arrivalTime));
		}
		String skumapstr = dto.getSkumapstr();// {"b1662cd7-b3c1-4e93-9389-44e704f9f542":"5","04585a54-364e-4066-b790-ad6a70e1cf34":"10"}
		if (StrUtil.isEmpty(skumapstr)) {
			throw new BizException("SKU调库列表不能为空");
		}
		Map<String, Object> map = iErpDispatchOverseaFormService.saveAction(user,dispatchForm, skumapstr);
		return Result.success(map);
	}

	@Transactional
	@GetMapping("/updateRemark")
	public Result<Boolean> updateRemarkAction(String formid,String remark) {
		boolean result = iErpDispatchOverseaFormService.lambdaUpdate().set(ErpDispatchOverseaForm::getRemark, remark).eq(ErpDispatchOverseaForm::getId, formid).update();
		return Result.success(result);
	}

	
	@Transactional
	@GetMapping("/examine")
	public Result<String> examineAction(String status,String ids){
		UserInfo user = UserInfoContext.get();
		int result = iErpDispatchOverseaFormService.submitAction(user,ids,status);
		String msg = "";
		if (result > 0) {
			msg += "操作成功！";
		} else {
			msg += "操作失败！";
		}
		return Result.success(msg);
	}
	
	@ApiOperation(value = "下载配货单")
	@GetMapping("/downPDFShipForm")
	public void downPDFShipFormAction( @ApiParam("货件ID")@RequestParam String  id,HttpServletResponse response) {
		UserInfo user=UserInfoContext.get();
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition", "attachment;fileName=shipForm" + System.currentTimeMillis() + ".pdf");// 设置文件名
		Document document = new Document(PageSize.A4);
		// step 2
		try {
	 
		    ShipInboundShipmenSummarytVo itemsum=new ShipInboundShipmenSummarytVo();
		    Map<String, Object> data = iErpDispatchOverseaFormService.findById(id);
			String shopid = user.getCompanyid();
			List<Map<String, Object>> dispatchFormEntryList = iErpDispatchOverseaFormEntryService.findFormDetailByFormid(id, data.get("to_warehouseid").toString(), data.get("from_warehouseid").toString(), shopid);
			String groupid=data.get("groupid").toString();
			try {
				Result<?> result = amazonClientOneFeign.getAmazonGroupByIdAction(groupid);
				if(Result.isSuccess(result)&&result.getData()!=null) {
		            Map<String, Object> groupmap = BeanUtil.beanToMap(result.getData());	
		            if(groupmap!=null&&groupmap.get("name")!=null) {
		            	 itemsum.setGroupname(groupmap.get("name").toString());
		            }
				}
			}catch(FeignException e) {
				BizException.getMessage(e, "获取店铺名称失败");
			}
			if(data.get("country")!=null) {
				itemsum.setCountryCode(data.get("country").toString());
				itemsum.setCountry(data.get("country").toString());
			}
			if(data.get("number")!=null) {
				itemsum.setShipmentid(data.get("number").toString());
			}
			if(data.get("remark")!=null) {
				itemsum.setRemark(data.get("remark").toString());
			}
			if(data.get("fromwarehouse")!=null) {
				itemsum.setWarehouse(data.get("fromwarehouse").toString());
			}
			if(data.get("from_warehouseid")!=null) {
				itemsum.setWarehouseid(data.get("from_warehouseid").toString());
			}
			if(data.get("towarehouse")!=null) {
				itemsum.setCenter(data.get("towarehouse").toString());
			}
			if(data.get("audittime")!=null) {
				itemsum.setAuditime(GeneralUtil.getDate(data.get("audittime")));
			}
			if(data.get("createdate")!=null) {
				itemsum.setCreatedate(GeneralUtil.getDate(data.get("createdate")));
			}
			List<ShipInboundItemVo> list=new ArrayList<ShipInboundItemVo>();
			long sum=0;
			for(Map<String, Object> item:dispatchFormEntryList) {
				ShipInboundItemVo vo=new ShipInboundItemVo();
				vo.setSellersku(item.get("sellersku").toString());
				vo.setSku(item.get("sku").toString());
				vo.setName(item.get("name").toString());
				vo.setImage(item.get("image")!=null?item.get("image").toString():null);
				vo.setFNSKU(item.get("fnsku")!=null?item.get("fnsku").toString():null);
				vo.setQuantity(Integer.parseInt(item.get("amount").toString()));
				vo.setQuantityShipped(Integer.parseInt(item.get("amount").toString()));
				vo.setInvquantity(Long.parseLong(item.get("from_warehouse_fulfillable").toString()));
				vo.setPname(item.get("name").toString());
				vo.setMsku(item.get("sku").toString());
				sum=sum+vo.getQuantity();
				Material material = materialService.getById(item.get("materialid").toString());
				vo.setBoxnum(material.getBoxnum());
				vo.setMaterialid(item.get("materialid").toString());
				if(material.getBoxdimensions()!=null) {
					DimensionsInfo box = iDimensionsInfoService.getById(material.getBoxdimensions());
					vo.setBoxheight(box.getHeight());
					vo.setBoxlength(box.getLength());
					vo.setWeight(box.getWeight());
					vo.setBoxwidth(box.getWidth());
				}
				list.add(vo);
			}
			itemsum.setShopid(user.getCompanyid());
			itemsum.setSkuamount(Long.parseLong(dispatchFormEntryList.size()+""));
			itemsum.setSumQuantity(sum);
			itemsum.setItemList(list);
		    ShipInboundShipmenSummarytVo datas = iWarehouseShelfInventoryService.formInvAssemblyShelf(itemsum);
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			iShipAmazonFormService.setPDFDocShipForm(writer,user,document, datas);
		} catch(FeignException  e) {
			 throw new BizException(BizException.getMessage(e, "获取货件失败"));
	     } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (document != null && document.isOpen()) {
				document.close();
			}
		}
	}
	
	@GetMapping("/getShipArrivalTimeRecord")
	public Result<List<Map<String, Object>>> getShipArrivalTimeRecord(String shopid,String country,String groupid,String sku){
		return Result.success(iErpDispatchOverseaFormService.getShipArrivalTimeRecord(shopid, country, sku, groupid));
	}
	
}

