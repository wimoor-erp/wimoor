package com.wimoor.erp.assembly.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.wimoor.erp.material.pojo.vo.MaterialVO;
import com.wimoor.erp.ship.pojo.dto.ShipInboundItemVo;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryService;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;
import com.wimoor.erp.api.AdminClientOneFeignManager;
import com.wimoor.erp.assembly.pojo.dto.AssemblyFormListDTO;
import com.wimoor.erp.assembly.pojo.dto.AssemblySaveDTO;
import com.wimoor.erp.assembly.pojo.entity.AssemblyEntryInstock;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.assembly.pojo.entity.AssemblyFormEntry;
import com.wimoor.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.erp.assembly.service.IAssemblyEntryInstockService;
import com.wimoor.erp.assembly.service.IAssemblyFormEntryService;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.customer.pojo.entity.Customer;
import com.wimoor.erp.customer.service.ICustomerService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.purchase.pojo.dto.PurchaseSaveDTO;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "组装接口")
@RestController
@RequestMapping("/api/v1/assembly")
@RequiredArgsConstructor
public class AssemblyFormController {
   final IAssemblyService assemblyService;
   final IAssemblyFormService assemblyFormService;
   final IAssemblyFormEntryService assemblyFormEntryService;
   final IInventoryService inventoryService;
   final IWarehouseService warehouseService;
   @Lazy
   @Autowired
   IMaterialService iMaterialService;
   final ICustomerService iCustomerService;
   final IPurchaseFormEntryService  iPurchaseFormEntryService;
   final FileUpload fileUpload;
   final AdminClientOneFeignManager adminClientOneFeign;
   final ISerialNumService serialNumService;
   final IAssemblyEntryInstockService assemblyEntryInstockService;
	final IWarehouseShelfInventoryService iWarehouseShelfInventoryService;
	@GetMapping("/sublit")
	public Result<List<AssemblyVO>> getSubList(String materialid,String warehouseid){
		UserInfo userinfo = UserInfoContext.get();
		List<AssemblyVO> assemblyList = assemblyService.selectByMainmid(materialid);
		if(StrUtil.isBlankOrUndefined(warehouseid)) {
			warehouseid=null;
		}
		if(assemblyList!=null&&assemblyList.size()>0) {
			for(AssemblyVO items:assemblyList) {
	              Map<String, Object> submap = inventoryService.findInvByWarehouseId(items.getSubmid(),warehouseid, userinfo.getCompanyid());
	              if(submap!=null&&submap.size()>0) {
	            	  if(submap.get("inbound")!=null) {
	            		  items.setInbound(Integer.parseInt(submap.get("inbound").toString()));
	            	  }
	            	  if(submap.get("outbound")!=null) {
	            		  items.setOutbound(Integer.parseInt(submap.get("outbound").toString()));
	            	  }
	            	  if(submap.get("fulfillable")!=null) {
	            		  items.setFulfillable(Integer.parseInt(submap.get("fulfillable").toString()));
	            	  }
	              }
			}
		}
		return Result.success(assemblyList);
	}
	
	
	@GetMapping("/assemblyFormNeedInv")
	public Result<?> assemblyFormNeedInv(String materialid,String warehouseid)  {
		UserInfo userinfo = UserInfoContext.get();
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("shopid", userinfo.getCompanyid());
		param.put("materialid", materialid);
		param.put("warehouseid", warehouseid);
		return Result.success(assemblyFormService.assemblyFormNeedInv(param));
	}
	
	@GetMapping("/getCountNum")
	public Result<?> getCountNum()  {
		UserInfo userinfo = UserInfoContext.get();
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("shopid", userinfo.getCompanyid());
		return Result.success(assemblyFormService.getCountNum(param));
	}
	
	//下载采购详情报表（采购订单执行） 新改版改成发货信息放至表头里。
	@GetMapping("/downloadInfoWord")
	public void downloadInfoWord(String formid,HttpServletResponse response) {
		Workbook workbook = null;
		ServletOutputStream fOut = null;
		UserInfo userinfo = UserInfoContext.get();
		AssemblyForm asform = assemblyFormService.getById(formid);
		Material material = iMaterialService.getById(asform.getMainmid());
		Warehouse warehouse = warehouseService.getById(asform.getWarehouseid());
		String myCompany="";
		String myName="";
		if(userinfo.getUserinfo().get("name")!=null) {
			myName=userinfo.getUserinfo().get("name").toString();
		}
		try {
			Result<UserInfo> infoResult = adminClientOneFeign.getUserAllByUserId(userinfo.getId());
			if(Result.isSuccess(infoResult)&&infoResult.getData()!=null) {
				UserInfo umap = infoResult.getData();
				if(umap!=null) {
					Map<String, Object> imap =umap.getUserinfo();
					if(imap.get("companyname")!=null) {
						myCompany=imap.get("companyname").toString();
					}
					if(imap.get("name")!=null) {
						myName=imap.get("name").toString();
					}
				}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		String supplier=null;
		String myAddress=warehouse.getAddress();

		
		List<Map<String, Object>> entrylist = assemblyFormEntryService.selectByFormid(formid);
		String myPhone="";
		if(userinfo.getUserinfo().get("tel")!=null) {
			myPhone=userinfo.getUserinfo().get("tel").toString();
		}
		String date=GeneralUtil.formatDate(new Date());
		String remark="无";
		if(asform!=null) {
			remark=asform.getRemark();
			if(StrUtil.isEmpty(remark)) {
				remark="无";
			}
		}
		if(StrUtil.isEmpty(remark.trim()))remark="无";
		String deliverydate=null;
		int totalamount=0;
		if(entrylist!=null&&entrylist.size()>0) {
			for(Map<String, Object> map:entrylist) {
				if(map.get("totalamount")!=null) {
					totalamount=Integer.parseInt(map.get("totalamount").toString());
				}
				if(map.get("purchase_from_entry_id")!=null) {
					PurchaseFormEntry entryp = iPurchaseFormEntryService.getById(map.get("purchase_from_entry_id").toString());
					if(entryp!=null) {
						supplier=entryp.getSupplier();
					}
					if(entryp.getDeliverydate()!=null) {
						deliverydate=GeneralUtil.formatDate(entryp.getDeliverydate());
						break;
					}
				}
				if(map.get("supplier")!=null&&supplier==null) {
					supplier=map.get("supplier").toString();
				}
				 if(map.get("delivery_cycle")!=null) {
					 int delivery_cycle= Integer.parseInt(map.get("delivery_cycle").toString());
					 Calendar c=Calendar.getInstance();
					 c.add(Calendar.DATE, delivery_cycle);
					 deliverydate=GeneralUtil.formatDate(c.getTime());
					 break;
				 }
			}
		 }
		if(deliverydate==null) {
			deliverydate="        ";
		}
		if(supplier==null) {
			supplier=material.getSupplier();
		}
		Customer customer=iCustomerService.getById(supplier);
		String heCompany="";
		String heName="";
		String heAddress="";
		String hePhone="";
		if(customer!=null) {
			heCompany=customer.getName();
			heName=customer.getContacts();
			heAddress=customer.getAddress();
			hePhone=customer.getPhone_num();
		}
		 try {
			 response.setContentType("application/force-download");// 设置强制下载不打开
			 response.addHeader("Content-Disposition", "attachment;fileName=AssemblyInfoWord" + System.currentTimeMillis() +".xlsx");// 设置文件名
			 InputStream is = new ClassPathResource("template/AssemblyFormContract.xlsx").getInputStream();
			// 创建新的Excel工作薄
			 workbook = WorkbookFactory.create(is);
			 Sheet sheet = workbook.getSheet("采购合同");
			 //先添加表格需要的行
			 if(entrylist.size()>1) {
				 int startRowindex=19;
				 for(int k=0;k<entrylist.size()-1;k++) {
					 sheet.shiftRows(startRowindex, sheet.getLastRowNum(), 1, true, false);
					 Row addrow = sheet.createRow(startRowindex);
					 org.apache.poi.ss.usermodel.Cell cell = addrow.createCell(2,CellType.STRING);
					 cell.setCellValue("$image");
					 org.apache.poi.ss.usermodel.Cell cell2 = addrow.createCell(3,CellType.STRING);
					 cell2.setCellValue("$sku");
					 org.apache.poi.ss.usermodel.Cell cell3 = addrow.createCell(4,CellType.STRING);
					 cell3.setCellValue("$skuname");
					 org.apache.poi.ss.usermodel.Cell cell4 = addrow.createCell(5,CellType.STRING);
					 cell4.setCellValue("$num");
					 org.apache.poi.ss.usermodel.Cell cell5 = addrow.createCell(6,CellType.STRING);
					 cell5.setCellValue("$amount");
				 }
			 }
			 //提前生成样式
			 	CellStyle style=workbook.createCellStyle();
				style.setAlignment(HorizontalAlignment.LEFT);
				org.apache.poi.ss.usermodel.Font font = workbook.createFont();
				font.setFontHeightInPoints((short)10);
				style.setFont(font);
				style.setWrapText(true);
			  //遍历行row
			 for (int i=0;i<=sheet.getLastRowNum();i++){
				 Row sheetRow = sheet.getRow(i);
				 if (sheetRow == null) {
					 continue;
				 }
				//遍历列cell
				for (int cellnum = 0; cellnum <= sheetRow.getLastCellNum(); cellnum++) {
					org.apache.poi.ss.usermodel.Cell cell = sheetRow.getCell(cellnum);
					if (cell == null) {
						continue;
					}
					cell.setCellType(CellType.STRING);
					String cellValue = cell.getStringCellValue().trim();
					if(StrUtil.isNotEmpty(cellValue)) {
						if("$myCompany".equals(cellValue)) {
							cell.setCellValue(myCompany);
						}
						if("$myAddress".equals(cellValue)) {
							cell.setCellValue(myAddress);
						}
						if("$myName".equals(cellValue)) {
							cell.setCellValue(myName);
						}
						if("$myPhone".equals(cellValue)) {
							cell.setCellValue(myPhone);
						}
						if("$date".equals(cellValue)) {
							cell.setCellValue(date);
						}
						if("$heCompany".equals(cellValue)) {
							cell.setCellValue(heCompany);
						}
						if("$heName".equals(cellValue)) {
							cell.setCellValue(heName);
						}
						if("$heAddress".equals(cellValue)) {
							cell.setCellValue(heAddress);
						}
						if("$hePhone".equals(cellValue)) {
							cell.setCellValue(hePhone);
						}
						if("$number".equals(cellValue)) {
							cell.setCellValue(asform.getNumber());
						}
						if("$totalamount".equals(cellValue)) {
							cell.setCellValue(totalamount);
						}
						if("$orderremark".equals(cellValue)) {
							if(remark.contains(";;")) {
								CellStyle cs = workbook.createCellStyle();  
							    cs.setWrapText(true);  //关键
								cell.setCellStyle(cs);
								remark=remark.replaceAll(";;", "\n\r");
								cell.setCellValue(new XSSFRichTextString(remark));
							}else {
								cell.setCellValue(remark);
							}
						}
						if("交货期：【$deliverDate】".equals(cellValue)) {
							cell.setCellValue("交货期：【"+deliverydate+"】");
						}
					}
				}
				//处理生成表格数据。。。。 遍历表格的行
				int startIndex=19;
				for(int j=0;j<entrylist.size();j++) {
					 Map<String, Object> item = entrylist.get(j);
						 Row tableRow = sheet.getRow(startIndex-1);
						 if (tableRow == null) {
							 continue;
						 }
						//遍历列cell
						for (int tablecell = 0; tablecell <= tableRow.getLastCellNum(); tablecell++) {
							org.apache.poi.ss.usermodel.Cell tabcell=tableRow.getCell(tablecell);
							if (tabcell == null) {
								continue;
							}
							tabcell.setCellType(CellType.STRING);
							String tabcellValue = tabcell.getStringCellValue();
							if("$image".equals(tabcellValue)) {
								tableRow.setHeightInPoints(60);
								if(item.get("image")!=null) {
									BufferedImage bufferImg = null;
									String localImg=item.get("image").toString();
										localImg= fileUpload.getPictureImage(item.get("image").toString());
										Drawing<?> draw = sheet.createDrawingPatriarch();
										ClientAnchor anchor = draw.createAnchor(50, 50, 255, 255, tabcell.getColumnIndex(), tabcell.getRowIndex(), tabcell.getColumnIndex()+1, tabcell.getRowIndex()+1);
										ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
										bufferImg = ImageIO.read(new URL(localImg));
										ImageIO.write(bufferImg, "PNG", byteArrayOut);
										Picture pic = draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_PNG));
										pic.setLineStyleColor(1, 1, 1);
										tabcell.setCellValue("");
								}else {
									tabcell.setCellValue("暂无图片");
								}
							}
							if("$sku".equals(tabcellValue)) {
								if(item.get("sku")!=null)tabcell.setCellValue(item.get("sku").toString());
								else tabcell.setCellValue("暂无数据");
							}
							if("$skuname".equals(tabcellValue)) {
								tabcell.setCellStyle(style);
								if(item.get("mname")!=null)tabcell.setCellValue(item.get("mname").toString());
								else tabcell.setCellValue("暂无数据");
							}
							if("$num".equals(tabcellValue)) {
								if(item.get("num")!=null) {
									int value=Integer.parseInt(item.get("num").toString());
									tabcell.setCellValue(value);
								}
								else tabcell.setCellValue("暂无数据");
							}
							if("$amount".equals(tabcellValue)) {
								if(item.get("amount")!=null) {
									int value=Integer.parseInt(item.get("amount").toString());
									tabcell.setCellValue(value);
								}
								else tabcell.setCellValue("暂无数据");
							}
						}
					 startIndex++;
				}
			 }
			 fOut = response.getOutputStream();
			 workbook.write(fOut);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fOut != null) {
					fOut.flush();
					fOut.close();
				}
				if(workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	 
}


	@PostMapping("/list")
	public Result<?> getListData(@RequestBody AssemblyFormListDTO dto)  {
		UserInfo userinfo = UserInfoContext.get();
 
		if (StrUtil.isEmpty(dto.getSearch())) {
			dto.setSearch(null);
		} else {
			if("shipment".equals(dto.getSearchtype())) {
				dto.setSearch(dto.getSearch().trim());
			}else {
				dto.setSearch("%"+dto.getSearch().trim()+ "%");
			}
		}
		if (StrUtil.isNotEmpty(dto.getFtype())) {
			dto.setFtype(dto.getFtype().trim());
			
		} else {
			dto.setFtype(null);
		}
		 
		if (StrUtil.isEmpty(dto.getAuditstatus())) {
			dto.setAuditstatus(null);
		}
		if ("1".equals(dto.getAuditstatus())||"2".equals(dto.getAuditstatus())) {
			dto.setOperate(StrUtil.isEmpty(dto.getOperate()) ? null : dto.getOperate().trim());
		}
		if(StrUtil.isEmpty(dto.getWarehouseid())) {
			dto.setWarehouseid(null);
		}
		if(userinfo.isLimit(UserLimitDataType.owner)) {
			dto.setOwner(userinfo.getId());;
		}
		if(dto.getToDate()!=null) {
			Date todate = dto.getToDate();
			Calendar c = Calendar.getInstance();
			c.setTime(todate);
			c.add(Calendar.DATE, 1);
			dto.setToDate(c.getTime());
		}
		dto.setShopid(userinfo.getCompanyid());
		if(StrUtil.isNotBlank(dto.getSearch())&&("number".equals(dto.getSearchtype()))) {
			dto.setToDate(null);
			dto.setFromDate(null);
		}
		if(StrUtil.isNotBlank(dto.getSearch())&&("shipment".equals(dto.getSearchtype()))) {
			dto.setToDate(null);
			dto.setFromDate(null);
		}
		if(StrUtil.isBlank(dto.getPreprocessingid())){
			dto.setPreprocessingid(null);
		}
		IPage<Map<String, Object>> list = assemblyFormService.findByCondition(dto);
		return Result.success(list);
	}
	
	@PostMapping("/downloadDetailList")
	public void downloadDetailListData(@RequestBody AssemblyFormListDTO dto, HttpServletResponse response)  {
		UserInfo userinfo = UserInfoContext.get();
 
		if (StrUtil.isEmpty(dto.getSearch())) {
			dto.setSearch(null);
		} else {
			if("shipment".equals(dto.getSearchtype())) {
				dto.setSearch(dto.getSearch().trim());
			}else {
				dto.setSearch("%"+dto.getSearch().trim()+ "%");
			}
		}
		if (StrUtil.isNotEmpty(dto.getFtype())) {
			dto.setFtype(dto.getFtype().trim());
			
		} else {
			dto.setFtype(null);
		}
		 
		if (StrUtil.isEmpty(dto.getAuditstatus())) {
			dto.setAuditstatus(null);
		}
		if ("1".equals(dto.getAuditstatus())||"2".equals(dto.getAuditstatus())) {
			dto.setOperate(StrUtil.isEmpty(dto.getOperate()) ? null : dto.getOperate().trim());
		}
		if(StrUtil.isEmpty(dto.getWarehouseid())) {
			dto.setWarehouseid(null);
		}
		if(userinfo.isLimit(UserLimitDataType.owner)) {
			dto.setOwner(userinfo.getId());;
		}
		if(dto.getToDate()!=null) {
			Date todate = dto.getToDate();
			Calendar c = Calendar.getInstance();
			c.setTime(todate);
			c.add(Calendar.DATE, 1);
			dto.setToDate(c.getTime());
		}
		dto.setShopid(userinfo.getCompanyid());
		if(StrUtil.isNotBlank(dto.getSearch())&&("number".equals(dto.getSearchtype()))) {
			dto.setToDate(null);
			dto.setFromDate(null);
		}
		if(StrUtil.isNotBlank(dto.getSearch())&&("shipment".equals(dto.getSearchtype()))) {
			dto.setToDate(null);
			dto.setFromDate(null);
		}
			Workbook workbook = null;
			ServletOutputStream fOut = null;
			try {
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=Records.xlsx");// 设置文件名
				fOut = response.getOutputStream();
				//插入记录条
				workbook=assemblyFormService.downloadDetailList(workbook,dto,userinfo);
				workbook.write(fOut);
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(fOut != null) {
						fOut.flush();
						fOut.close();
					}
					if(workbook != null) {
						workbook.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	
	@PostMapping("/downloadFormList")
	public void downloadFormListData(@RequestBody AssemblyFormListDTO dto, HttpServletResponse response)  {
		UserInfo userinfo = UserInfoContext.get();
 
		if (StrUtil.isEmpty(dto.getSearch())) {
			dto.setSearch(null);
		} else {
			if("shipment".equals(dto.getSearchtype())) {
				dto.setSearch(dto.getSearch().trim());
			}else {
				dto.setSearch("%"+dto.getSearch().trim()+ "%");
			}
		}
		if (StrUtil.isNotEmpty(dto.getFtype())) {
			dto.setFtype(dto.getFtype().trim());
			
		} else {
			dto.setFtype(null);
		}
		 
		if (StrUtil.isEmpty(dto.getAuditstatus())) {
			dto.setAuditstatus(null);
		}
		if ("1".equals(dto.getAuditstatus())||"2".equals(dto.getAuditstatus())) {
			dto.setOperate(StrUtil.isEmpty(dto.getOperate()) ? null : dto.getOperate().trim());
		}
		if(StrUtil.isEmpty(dto.getWarehouseid())) {
			dto.setWarehouseid(null);
		}
		if(userinfo.isLimit(UserLimitDataType.owner)) {
			dto.setOwner(userinfo.getId());;
		}
		if(dto.getToDate()!=null) {
			Date todate = dto.getToDate();
			Calendar c = Calendar.getInstance();
			c.setTime(todate);
			c.add(Calendar.DATE, 1);
			dto.setToDate(c.getTime());
		}
		dto.setShopid(userinfo.getCompanyid());
		if(StrUtil.isNotBlank(dto.getSearch())&&("number".equals(dto.getSearchtype()))) {
			dto.setToDate(null);
			dto.setFromDate(null);
		}
		if(StrUtil.isNotBlank(dto.getSearch())&&("shipment".equals(dto.getSearchtype()))) {
			dto.setToDate(null);
			dto.setFromDate(null);
		}
			Workbook workbook = null;
			ServletOutputStream fOut = null;
			try {
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=Records.xlsx");// 设置文件名
				fOut = response.getOutputStream();
				//插入记录条
				workbook=assemblyFormService.downloadFormList(workbook,dto,userinfo);
				workbook.write(fOut);
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(fOut != null) {
						fOut.flush();
						fOut.close();
					}
					if(workbook != null) {
						workbook.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	// 删除组装单据
	@ApiOperation(value = "取消组装单")
	@Transactional
	@GetMapping("/deleteFrom")
	public Result<Map<String, Object>> deleteFormAction(String ids) {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> result = new HashMap<String, Object>();
		String[] idlist = ids.split(",");
		result.put("msg", "取消成功");
		result.put("type", "info");
		for (int i = 0; i < idlist.length; i++) {
			String id = idlist[i];
			if (StrUtil.isNotEmpty(id)) {
				AssemblyForm assemblyForm = assemblyFormService.getById(id);
				if (assemblyForm.getAuditstatus() == 3 || assemblyForm.getAmountHandle()>0) {
					result.put("msg", assemblyForm.getNumber()+",表单已经有入库记录或者已完成");
					throw new BizException(assemblyForm.getNumber()+",表单已经有入库记录或者已完成");
				}
				assemblyFormService.deleteAssemblyForm(id, user);
			}
		}
		return Result.success(result);
	}
	
	@GetMapping("/getSubForm")
	public Result<Map<String, Object>> getSubFormAction(String materialid,String warehouseid){
		UserInfo user = UserInfoContext.get();
		List<AssemblyVO> sublist = assemblyService.selectByMainmid(materialid);
		Map<String, Object> msgMap = new HashMap<String, Object>();
		String shopid = user.getCompanyid();
		if (sublist != null && sublist.size() > 0) {
			for (int i = 0; i < sublist.size(); i++) {
				AssemblyVO item = sublist.get(i);
				String submid = item.getSubmid();
				Map<String, Object> map = null;
				if (StrUtil.isNotEmpty(warehouseid)) {
					Map<String, Object> inv = inventoryService.findInvDetailById(submid, warehouseid, shopid);
					map = new HashMap<String, Object>();
					map.put("warehouseid", warehouseid);
					map.put("warehouse", warehouseService.getById(warehouseid).getName());
					Integer needprocess=assemblyFormEntryService.selectNeedProcess(submid, warehouseid,shopid);
					map.put("needprocess", needprocess);
					if (inv == null) {
						map.put("amount", 0);
						map.put("inbound", 0);
					} else {
						map.put("amount", inv.get("fulfillable"));
						map.put("inbound", inv.get("inbound"));
					}
					Integer amount=inv!=null&&inv.get("fulfillable")!=null?Integer.parseInt(inv.get("fulfillable").toString()):0;
					Integer inbound=inv!=null&&inv.get("inbound")!=null?Integer.parseInt(inv.get("inbound").toString()):0;
					if (needprocess!= null ) {
						if(amount+inbound<needprocess){
							map.put("moreqty", 0);
						}else{
							map.put("moreqty", amount+inbound-needprocess);
						}
					}else{
						map.put("moreqty", amount+inbound);
					}
				} else {
					map = assemblyFormService.selectSubASList(null, submid == null ? null : submid.toString(), shopid);
					if (map != null && map.get("warehouseid") != null) {
						Map<String, Object> inv = 
								inventoryService.findInvDetailById(submid == null ? null : submid.toString(), map.get("warehouseid").toString(), shopid);
						if (inv != null){
							map.put("inbound", inv.get("inbound"));
						}
						Integer needprocess=assemblyFormEntryService.selectNeedProcess(submid, map.get("warehouseid").toString(),shopid);
						map.put("needprocess", needprocess);
						Integer amount=inv.get("fulfillable")!=null?Integer.parseInt(inv.get("fulfillable").toString()):0;
						Integer inbound=inv.get("inbound")!=null?Integer.parseInt(inv.get("inbound").toString()):0;
						if (needprocess!= null ) {
							if(amount+inbound<needprocess){
								map.put("moreqty", 0);
							}else{
								map.put("moreqty", amount+inbound-needprocess);
							}
						}else{
							map.put("moreqty", amount+inbound);
						}
					}
				}
				item.setSubmap(map);
			}
			msgMap.put("sublist", sublist);
		} else {
			msgMap.put("msg", "请确认组装成品是否含有组装清单！");
		}
		return Result.success(msgMap);
	}
	
	@ResponseBody
	@RequestMapping("/getwareInv")
	public Result<Map<String, Object>> getWareInvAction(String subsku,String warehouseid,String warehousename){
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		 
		QueryWrapper<Material> queryWrapper=new QueryWrapper<Material>();
		queryWrapper.eq("sku", subsku);
		queryWrapper.eq("shopid", shopid);
		queryWrapper.eq("isDelete", false);
		List<Material> mList = iMaterialService.list(queryWrapper);
		String skuid = mList.get(0).getId();
		Map<String, Object> msgMap = new HashMap<String, Object>();
		Map<String, Object> map = inventoryService.findInvDetailById(skuid, warehouseid, shopid);
		if (map == null) {
			msgMap.put("msg", "产品" + subsku + "在【" + warehousename + "】没有找到库存！");
		} else {
			if (map.size() > 0) {
				msgMap.put("warehouse", warehousename);
				msgMap.put("amount", map.get("fulfillable"));
				msgMap.put("inbound", map.get("inbound"));
				msgMap.put("msg", "仓库更换成功");
			}
		}
		return Result.success(msgMap);
	}

	@GetMapping("/assemblyShipment")
	public Result<Integer> assemblyProduct(String shipmentid) {
		UserInfo user = UserInfoContext.get();
		return Result.success(assemblyFormService.findhasAssemblyFromShipment(user.getCompanyid(),shipmentid));
	}

	@ApiOperation(value = "重置组装")
	@GetMapping("/resetAssForm")
	@Transactional
	public Result<Map<String, Object>> resetAssFormAction(String id) {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> maps = assemblyFormService.resetAssForm(user,id);
		return Result.success(maps);
	}
	
	
	@ApiOperation(value = "规划提交")
	@PostMapping("/saveMutilData")
	@Transactional
	public Result<Map<String, Object>> saveMutilDataAction(@RequestBody PurchaseSaveDTO dto) throws Exception {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> msgMap = new HashMap<String, Object>();
		List<AssemblyForm> formlist = new ArrayList<AssemblyForm>();
		String warehouseid = null;
		if(StrUtil.isNotEmpty(dto.getAssList())) {
		List<Map<String, Object>> asslistmap = GeneralUtil.jsonStringToMultMapList(dto.getAssList());
		if(asslistmap.size()>0) {
		for (int step = 0; step < asslistmap.size(); step++) {
			Map<String, Object> parameter = asslistmap.get(step);
			AssemblyForm asform = new AssemblyForm();
			String shopid = user.getCompanyid();
			try {
				asform.setNumber(serialNumService.readSerialNumber(shopid, "MO"));
			} catch (Exception e) {
				e.printStackTrace();
				try {
					asform.setNumber(serialNumService.readSerialNumber(shopid, "MO"));
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new BizException("编码获取失败,请联系管理员");
				}
			}
			asform.setShopid(shopid);
			String id = GeneralUtil.getStr(parameter.get("id"));
			if (StrUtil.isNotEmpty(id)) {
				asform.setId(id);
			}
			String materialid = GeneralUtil.getStr(parameter.get("materialid"));
			String remark = GeneralUtil.getStr(parameter.get("remark"));
			warehouseid = GeneralUtil.getStr(parameter.get("warehouseid"));
			String planitemid = GeneralUtil.getStr(parameter.get("planitemid"));////////////
			asform.setAudittime(new Date());
			asform.setOpttime(new Date());
			try {
				if (StrUtil.isNotEmpty(GeneralUtil.getStr(parameter.get("amount")))) {
					asform.setAmount(Integer.parseInt(GeneralUtil.getStr(parameter.get("amount"))));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new BizException("请输入合法的【组装数量】");
			}
			asform.setPlanitem(planitemid);
			asform.setAuditstatus(1);// 1:待组装
			asform.setCreator(user.getId());
			asform.setAuditor(user.getId());
			asform.setOperator(user.getId());
			asform.setWarehouseid(warehouseid);
			asform.setMainmid(materialid);
			asform.setRemark(remark);
			String subAssList = (String) parameter.get("SubAssList");
			List<Map<String, Object>> listmap = GeneralUtil.jsonStringToMapList(subAssList);
			for (int i = 0; i < listmap.size(); i++) {
				AssemblyFormEntry entry = new AssemblyFormEntry();
				String skuname = listmap.get(i).get("subsku").toString();
				String wareid = listmap.get(i).get("warehouseid").toString();
				QueryWrapper<Material> queryWrapper=new QueryWrapper<Material>();
				queryWrapper.eq("sku", skuname);
				queryWrapper.eq("shopid", shopid);
				queryWrapper.eq("isDelete", false);
				List<Material> mList = iMaterialService.list(queryWrapper);
				if(mList.size()==0) {
					Material mainsku = iMaterialService.getById(materialid);
					throw new BizException("主SKU【"+mainsku.getSku()+"】的子SKU["+skuname+"]无法找到，请确认是否已经归档，您可以恢复归档的SKU或者修改组装清单去掉该子SKU");
				}
				String skuid = mList.get(0).getId();
				entry.setMaterialid(skuid);
				entry.setWarehouseid(wareid);
				entry.setAmount(Integer.parseInt((listmap.get(i).get("amount").toString())));
				entry.setFormid(asform.getId());
				asform.addEntry(entry);
			}
			formlist.add(asform);
		}
		}
		}
		msgMap = assemblyFormService.saveMutilForm(formlist, user,dto);
		msgMap.put("assmblyType", "end");
		return Result.success(msgMap);
	}
	
	@ApiOperation(value = "添加组装/拆分单")
	@PostMapping("/saveData")
	@Transactional
	public Result<Map<String, Object>> saveAssemblyAction(@RequestBody AssemblySaveDTO dto) {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> msgMap = new HashMap<String, Object>();
		AssemblyForm asform = new AssemblyForm();
		String shopid = user.getCompanyid();
		try {
			asform.setNumber(serialNumService.readSerialNumber(shopid, "MO"));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				asform.setNumber(serialNumService.readSerialNumber(shopid, "MO"));
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new BizException("编码获取失败,请联系管理员");
			}
		}
		asform.setShopid(shopid);
		String id = dto.getId();
		if (StrUtil.isNotEmpty(id)) {
			asform.setId(id);
		}else {
			//新增
			asform.setId(warehouseService.getUUID());
		}
		String ftype =dto.getFtype();
		String materialid = dto.getMaterialid();
		String remark = dto.getRemark();
		String warehouseid = dto.getWarehouseid();
		asform.setCreatedate(new Date());
		asform.setAudittime(new Date());
		asform.setOpttime(new Date());
		if (StrUtil.isNotEmpty(dto.getAmount()) && !dto.getAmount().equals("0")) {
			asform.setAmount(Integer.parseInt(dto.getAmount()));
			asform.setAmountHandle(0);
		}else {
			throw new BizException("操作数量必须大于0");
		}
		if (ftype != null) {
			asform.setFtype(ftype);
		}
		//asform.setAuditstatus(1);// 1:待组装
		asform.setAuditstatus(2);
		asform.setCreator(user.getId());
		asform.setAuditor(user.getId());
		asform.setOperator(user.getId());
		asform.setWarehouseid(warehouseid);
		asform.setMainmid(materialid);
		asform.setRemark(remark);
		String subAssList = dto.getSubAssList();
		subAssList.replace("\\\"", "");
		List<Map<String, Object>> listmap = GeneralUtil.jsonStringToMapList(subAssList);
		if (listmap == null) {
			throw new BizException("请输入正确的参数！");
		}
		for (int i = 0; i < listmap.size(); i++) {
			AssemblyFormEntry entry = new AssemblyFormEntry();
			String skuname = listmap.get(i).get("sku").toString();
			String wareid = warehouseid;
			Object amount = listmap.get(i).get("amount");
			if(amount==null || "".equals(amount.toString().trim()) || "NAN".equals(amount.toString())){
				throw new BizException("请输入正确的数量！");
			}
			QueryWrapper<Material> query=new QueryWrapper<Material>();
			query.eq("sku", skuname);
			query.eq("shopid", shopid);
			query.eq("isDelete", false);
			List<Material> mList = iMaterialService.list(query);
			if (mList.size() == 0) {
					Material mainsku = iMaterialService.getById(materialid);
					throw new BizException("主SKU【"+mainsku.getSku()+"】的子SKU["+skuname+"]无法找到，请确认是否已经归档，您可以恢复归档的SKU或者修改组装清单去掉该子SKU");
			}
			String skuid = mList.get(0).getId();
			entry.setAmount(Integer.parseInt(amount.toString().trim()));
			entry.setMaterialid(skuid);
			entry.setWarehouseid(wareid);
			entry.setFormid(asform.getId());
			asform.addEntry(entry);
		}
		msgMap = assemblyFormService.saveAssemblyForm(asform, user);
		return Result.success(msgMap);
	}
	
	@ApiOperation(value = "保存组装记录")
	@GetMapping("/saveRecord")
	@Transactional
	public Result<Map<String, Object>> saveRecordAction(String remark,String formid,String amount) throws BizException {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> msgMap = new HashMap<String, Object>();
		AssemblyEntryInstock assembRecord = new AssemblyEntryInstock();
		assembRecord.setAmount(Integer.parseInt(amount));
		assembRecord.setFormid(formid);
		assembRecord.setOperator(user.getId());
		assembRecord.setOpttime(new Date());
		if(StrUtil.isNotEmpty(remark)) {
			assembRecord.setRemark(remark);
		}
		msgMap = assemblyEntryInstockService.saveMineAndinStock(assembRecord, user);
		return Result.success(msgMap);
	}

	@ApiOperation(value = "终止组装")
	@GetMapping("/stopAssemblyEvent")
	@Transactional
	public Result<Map<String, Object>> stopAssemblyEventAction(String formid) throws BizException {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> msgMap = new HashMap<String, Object>();
		msgMap = assemblyFormService.updateToStopOperateEvent(formid, user);
		return Result.success(msgMap);
	}

	@ApiOperation(value = "开始组装")
	@GetMapping("/startAssemblyEvent")
	@Transactional
	public Result<Map<String, Object>> startAssemblyEventAction(String formid,String amounthandle) throws BizException {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> msgMap = new HashMap<String, Object>();
		int amount = 0;
		if (amounthandle != null) {
			amount = Integer.parseInt(amounthandle);
		}
		msgMap = assemblyFormService.updateAssemblyEvent(formid, amount, user);
		return Result.success(msgMap);
	}

	@ApiOperation(value = "修改组装数量")
	@GetMapping("/changeAssAmount")
	@Transactional
	public Result<Map<String, Object>> changeAssAmountAction(String formid,String value) throws BizException {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> msgMap = assemblyFormService.updateAssemblyAmount(formid, value, user);
		return Result.success(msgMap);
	}

	@GetMapping("/getData")
	public Result<Map<String, Object>> getData(String formid) {
		Map<String, Object> assemblyForm = assemblyFormService.findById(formid);
		String count = GeneralUtil.getStr(assemblyForm.get("amount"));
		String skuname = GeneralUtil.getStr(assemblyForm.get("skuname"));
		List<Map<String, Object>> inFormEntryList = assemblyFormEntryService.selectByFormid(GeneralUtil.getStr(assemblyForm.get("id")));
		List<Map<String, Object>> recordList = assemblyEntryInstockService.selectByFormId(formid);
		List<Map<String, Object>> instockList = assemblyEntryInstockService.selectByFormId(formid);
		Integer inAmount = 0;
		if (instockList != null && instockList.size() > 0) {
			for (int i = 0; i < instockList.size(); i++) {
				inAmount = inAmount + Integer.parseInt(instockList.get(i).get("amount").toString());
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String aduistatus = assemblyForm.get("auditstatus").toString();
		assemblyForm.put("inAmount", inAmount);
		map.put("count", count);
		map.put("status", aduistatus);
		map.put("warehouseform", assemblyForm);
		map.put("skuname", skuname);
		map.put("asFormEntryList", inFormEntryList);
		map.put("recordList", recordList);
		return Result.success(map);
	}
	
	@ApiOperation(value = "撤销组装入库")
	@GetMapping("/assemblyCancelInstock")
	@Transactional
	public Result<String> assemblyCancelInstockAction(String instockid) {
		UserInfo user = UserInfoContext.get();
		 AssemblyEntryInstock record = assemblyEntryInstockService.getById(instockid);
		 assemblyEntryInstockService.cancelInStock(user, record);
		return Result.success(instockid);
	}
	
	@ApiOperation(value = "加工单更换仓库")
	@GetMapping("/changeAssemblyWarehouse")
	@Transactional
	public Result<String> changeAssemblyWarehouseAction(String number,String warehouseid) {
		UserInfo user = UserInfoContext.get();
		assemblyEntryInstockService.changeAssemblyFormWarehouse(user, number,warehouseid);
		return Result.success();
	}
	
	@PostMapping("/assemblyCompareToSku/{sku}")
	public Result<String> assemblyCompareToSkuAction(@PathVariable String sku,@RequestBody JSONArray sublist) {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String result = assemblyFormService.assemblyCompareToSku(sku, shopid, sublist);
		return Result.success(result);
	}
	
	@ApiOperation("导出")
  	@GetMapping("/findProcessHandle")
    public  void findProcessHandleAction(
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
			assemblyEntryInstockService.findProcessHandle(workbook,map);
			ServletOutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
			Row row2 = sheet.createRow(1);
			row2.setHeight((short) (25 * 20));
			Cell cell21 = row2.createCell(0);
			cell21.setCellValue("操作类型【组装,拆分】:");
			cell21.setCellStyle(cs);

			Cell cell22 = row2.createCell(1);
			cell22.setCellValue("");
			cell22.setCellStyle(style);

			//第三行
			Row row3 = sheet.createRow(2);
			row3.setHeight((short) (25 * 20));
			Cell cell31 = row3.createCell(0);
			cell31.setCellValue("加工单备注:");
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
			title1.setCellValue("主SKU");
			Cell title2 = titleRow.createCell(1);
			title2.setCellStyle(cstitle);
			title2.setCellValue("加工数量");

			Sheet sheet2 = workbook.createSheet("模板示例");
			sheet2.setColumnWidth(0, 40 * 256);
			sheet2.setColumnWidth(1, 40 * 256);

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
			Row erow2 = sheet2.createRow(1);
			erow2.setHeight((short) (25 * 20));
			Cell ecell21 = erow2.createCell(0);
			ecell21.setCellValue("操作类型【组装,拆分】:");
			ecell21.setCellStyle(cs);

			Cell ecell22 = erow2.createCell(1);
			ecell22.setCellValue("组装");
			ecell22.setCellStyle(style);

			//第三行
			Row erow3 = sheet2.createRow(2);
			erow3.setHeight((short) (25 * 20));
			Cell ecell31 = erow3.createCell(0);
			ecell31.setCellValue("加工单备注:");
			ecell31.setCellStyle(cs2);

			Cell ecell32 = erow3.createCell(1);
			ecell32.setCellValue("我的备注");
			ecell32.setCellStyle(style2);

			//第四行
			Row etitleRow = sheet2.createRow(3);
			etitleRow.setHeight((short) (25 * 15));
			Cell etitle1 = etitleRow.createCell(0);
			etitle1.setCellStyle(cstitle);
			etitle1.setCellValue("主SKU");
			Cell etitle2 = etitleRow.createCell(1);
			etitle2.setCellStyle(cstitle);
			etitle2.setCellValue("加工数量");

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
		} catch (IOException e) {
			e.printStackTrace();
			throw new BizException("文件读写错误");
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
			throw new BizException("文件解码错误");
		}
	}


	@PostMapping(value = "/uploadExcel",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Transactional
	public Result<String> uploadExcelAction(@RequestParam("file") MultipartFile file){
		UserInfo user = UserInfoContext.get();
		if (file != null) {
			try {
				InputStream inputStream = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(inputStream);
				Sheet sheet = workbook.getSheetAt(0);
				String result = assemblyFormService.uploadAssemblyStockByExcel(sheet, user);
				workbook.close();
				return Result.success(result);
			} catch (IOException e) {
				e.printStackTrace();
				throw new BizException("文件读写错误");
			} catch (EncryptedDocumentException e) {
				e.printStackTrace();
				throw new BizException("文件解码错误");
			} catch (InvalidFormatException e) {
				e.printStackTrace();
				throw new BizException("文件解析错误");
			}
		}
		return Result.failed();
	}


	@PostMapping("/assemblyQuotainfos")
	public Result<?> assemblyQuotainfosAction(@RequestBody List<String> formids) {
		ShipInboundShipmenSummarytVo itemsum=this.assemblyFormService.assemblyQuotainfos(formids);
		for(ShipInboundItemVo item:itemsum.getItemList()) {
			MaterialVO material = this.iMaterialService.findMaterialById(item.getMaterialid());
			if(material!=null&&material.getImage()!=null) {
				itemsum.setShopid(material.getShopid());
				item.setImage(material.getImage());
			}
		}
		itemsum.setFormType("assembly");
		ShipInboundShipmenSummarytVo data = iWarehouseShelfInventoryService.formInvAssemblyShelf(itemsum);
		if(StrUtil.isBlank(data.getCheckinv())||data.getCheckinv().equals("0")) {
			data.setCheckinv(warehouseService.getUUID());
		}
		return Result.success(data);
	}
	@GetMapping("/assemblyQuotainfosById")
	public Result<?> assemblyQuotainfosByIdAction(String formid) {
		ShipInboundShipmenSummarytVo itemsum=this.assemblyFormService.assemblyQuotainfos(formid);
		for(ShipInboundItemVo item:itemsum.getItemList()) {
			MaterialVO material = this.iMaterialService.findMaterialById(item.getMaterialid());
			if(material!=null&&material.getImage()!=null) {
				itemsum.setShopid(material.getShopid());
				item.setImage(material.getImage());
			}
		}
		itemsum.setFormType("assembly");
		ShipInboundShipmenSummarytVo data = iWarehouseShelfInventoryService.formInvAssemblyShelf(itemsum);
		if(StrUtil.isBlank(data.getCheckinv())||data.getCheckinv().equals("0")) {
			data.setCheckinv(warehouseService.getUUID());
		}
		return Result.success(data);
	}

	@ApiOperation(value = "加工单checkinv")
	@PostMapping("/assemblyCheckInv/{checkinv}")
	@Transactional
	public Result<?> assemblyCheckInvAction(@PathVariable("checkinv") String checkinv,@RequestBody List<String> formids) {
		UserInfo user = UserInfoContext.get();
		Integer i=0;
		for(String formId:formids) {
			AssemblyForm assform = this.assemblyFormService.getById(formId);
			assform.setCheckInv(checkinv);
			if(this.assemblyFormService.updateById(assform)) {
				i=i+1;
			}
		}
		return Result.success(i);
	}

	@ApiOperation(value = "加工单调库加载")
	@PostMapping("/getItemByFormids")
	@Transactional
	public Result<?> getItemByFormidsAction(@RequestBody List<String> formids) {
		UserInfo user = UserInfoContext.get();
		List<Map<String,Object>> skulist=this.assemblyFormService.getItemByFormids(formids,user.getCompanyid());
		return Result.success(skulist);
	}




}
