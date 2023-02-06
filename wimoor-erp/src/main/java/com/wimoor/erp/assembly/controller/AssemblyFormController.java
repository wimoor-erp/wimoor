package com.wimoor.erp.assembly.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ibm.icu.util.Calendar;
import com.wimoor.api.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.api.AdminClientOneFeign;
import com.wimoor.erp.assembly.pojo.dto.AssemblyFormListDTO;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.assembly.service.IAssemblyFormEntryService;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.customer.pojo.entity.Customer;
import com.wimoor.erp.customer.service.ICustomerService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

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
   final IMaterialService iMaterialService;
   final ICustomerService iCustomerService;
   final IPurchaseFormEntryService  iPurchaseFormEntryService;
   final FileUpload fileUpload;
   final AdminClientOneFeign adminClientOneFeign;
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
		if ("1".equals(dto.getAuditstatus())) {
			dto.setOperate(StrUtil.isEmpty(dto.getOperate()) ? null : dto.getOperate().trim());
		}
		if(StrUtil.isEmpty(dto.getWarehouseid())) {
			dto.setWarehouseid(null);
		}
		dto.setShopid(userinfo.getCompanyid());
		IPage<Map<String, Object>> list = assemblyFormService.findByCondition(dto);
		return Result.success(list);
	}
}
