package com.wimoor.erp.purchase.service.impl;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.customer.mapper.CustomerMapper;
import com.wimoor.erp.customer.pojo.entity.Customer;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("purchaseFormEntryService")
@RequiredArgsConstructor
public class PurchaseFormEntryServiceImpl extends  ServiceImpl<PurchaseFormEntryMapper,PurchaseFormEntry> implements IPurchaseFormEntryService {

	final PurchaseFormMapper purchaseFormMapper;
	final IWarehouseService warehouseService;
	final CustomerMapper customerMapper;
	   @Autowired
	    FileUpload fileUpload;
	@Override
	public Map<String, Object> summaryBySupplier(String shopid,String supplier) {
		return this.baseMapper.summaryBySupplier(shopid,supplier);
	}

	@Override
	public void downloadPurchaseInfoWord(HttpServletResponse response, Map<String, Object> map,UserInfo userinfo) {
		Workbook workbook = null;
		ServletOutputStream fOut = null;
		String supplierid=map.get("supplierid").toString();
		PurchaseForm from = purchaseFormMapper.selectById(map.get("formid").toString());
		List<Map<String, Object>> entrylist = purchaseFormMapper.findEntryByIdAndSupplier(map.get("formid").toString(),supplierid);
		if(entrylist.size()<=0 || entrylist==null) {
			throw new BizException("无产品列表信息!");
		}
		Warehouse warehouse = warehouseService.getById(map.get("warehouseid").toString());
		Customer customer=customerMapper.selectById(supplierid);
		String myCompany=map.get("company").toString();
		String myAddress=warehouse.getAddress();
		String myName=map.get("buyerName").toString();
		 
		String myPhone="";
		if(userinfo.getUserinfo().get("tel")!=null) {
			myPhone=userinfo.getUserinfo().get("tel").toString();
		}else {
			myPhone=userinfo.getAccount();
		}
		String date=map.get("buyerDate").toString();
		String remark="无";
		if(from!=null) {
			remark=from.getRemark();
			if(StrUtil.isEmpty(remark)) {
				remark="无";
			}
		}
		if(StrUtil.isEmpty(remark.trim()))remark="无";
		String deliverydate="        ";
		if(entrylist.get(0).get("deliverydate")!=null)deliverydate=entrylist.get(0).get("deliverydate").toString().substring(0, 10);
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
			 response.addHeader("Content-Disposition", "attachment;fileName=downloadPurchaseInfoWord" + System.currentTimeMillis() +".xlsx");// 设置文件名
			 InputStream is = new ClassPathResource("template/PurchaseFormContract.xlsx").getInputStream();
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
					 cell4.setCellValue("$specification");
					 org.apache.poi.ss.usermodel.Cell cell5 = addrow.createCell(6,CellType.STRING);
					 cell5.setCellValue("$num");
					 org.apache.poi.ss.usermodel.Cell cell6 = addrow.createCell(7,CellType.STRING);
					 cell6.setCellValue("$itemprice");
					 org.apache.poi.ss.usermodel.Cell cell7 = addrow.createCell(8,CellType.STRING);
					 cell7.setCellValue("$orderprice");
					 org.apache.poi.ss.usermodel.Cell cell8 = addrow.createCell(9,CellType.STRING);
					 cell8.setCellValue("$remark");
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
									localImg=fileUpload.getPictureImage( item.get("image").toString()) ;
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
							if("$specification".equals(tabcellValue)) {
								if(item.get("specification")!=null)tabcell.setCellValue(item.get("specification").toString());
								else tabcell.setCellValue("暂无数据");
							}
							if("$remark".equals(tabcellValue)) {
								tabcell.setCellStyle(style);
								if(item.get("notice")!=null)tabcell.setCellValue(item.get("notice").toString());
								else tabcell.setCellValue("无");
							}
							if("$skuname".equals(tabcellValue)) {
								tabcell.setCellStyle(style);
								if(item.get("mname")!=null)tabcell.setCellValue(item.get("mname").toString());
								else tabcell.setCellValue("暂无数据");
							}
							if("$num".equals(tabcellValue)) {
								if(item.get("amount")!=null) {
									double value=Double.parseDouble(item.get("amount").toString());
									tabcell.setCellValue(value);
								}
								else tabcell.setCellValue("暂无数据");
							}
							if("$itemprice".equals(tabcellValue)) {
								if(item.get("itemprice")!=null) {
									double value=Double.parseDouble(item.get("itemprice").toString());
									tabcell.setCellValue(value);
								}
								else tabcell.setCellValue("暂无数据");
							}
							if("$orderprice".equals(tabcellValue)) {
								if(item.get("orderprice")!=null) {
									double value=Double.parseDouble(item.get("orderprice").toString());
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

	@Override
	public PurchaseFormEntry getByNumberSku(String shopid, String number, String sku) {
		// TODO Auto-generated method stub
		return this.baseMapper.getByNumberSku(shopid,number,sku);
	}



}
