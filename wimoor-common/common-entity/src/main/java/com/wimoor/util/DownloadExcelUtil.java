package com.wimoor.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;

import com.wimoor.common.mvc.FileUpload;


public class DownloadExcelUtil {
	
public static void setWorkbook(HttpServletResponse response,Map<String, Object> titlemap, List<Map<String, Object>> list) throws IOException {
	SXSSFWorkbook workbook = new SXSSFWorkbook();
	setWorkbook( workbook,titlemap,list);
	response.setContentType("application/force-download");// 设置强制下载不打开
	response.addHeader("Content-Disposition", "attachment;fileName=CommodityRevenue" + System.currentTimeMillis() + ".xlsx");// 设置文件名
	ServletOutputStream fOut = response.getOutputStream();
	workbook.write(fOut);
	workbook.close();
	fOut.flush();
	fOut.close();
}

public static void insertImageToCell(Workbook workbook,Sheet sheet, int rowNum,int colNum, String imageName, String fileType) throws IOException {
				//Loading image from application resource
                Drawing<?> drawing = sheet.createDrawingPatriarch();
				InputStream is  = new URL(imageName).openStream(); //ReportBase.class.getClassLoader().getResourceAsStream(imageName);
				//Converting input stream into byte array
				byte[] inputImageBytes = IOUtils.toByteArray(is);
				int inputImagePictureID = workbook.addPicture(inputImageBytes, Workbook.PICTURE_TYPE_PNG);
				is.close();
				ClientAnchor anchor = null;
				//Creating the client anchor based on file format
				if (fileType.equals("xls")) {
				anchor = new HSSFClientAnchor();
				} else {
				anchor = new XSSFClientAnchor();
				}
				anchor.setCol1(colNum);
				anchor.setCol2(colNum+1);
				anchor.setRow1(rowNum );
				anchor.setRow2(rowNum+ 1);
				drawing.createPicture(anchor, inputImagePictureID);
}
 
public static void setWorkbook(HttpServletResponse response, List<Map<String, Object>> list) throws IOException {
	Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
	if(list.size()>0) {
		Map<String,Object> map=list.get(0);
		for(Entry<String, Object> item:map.entrySet()) {
			titlemap.put(item.getKey(), item.getKey());
		}
	}
	setWorkbook(response,titlemap,list);
}

public static void setWorkbookWithImage(FileUpload fileUpload,SXSSFWorkbook workbook,Map<String, Object> titlemap, List<Map<String, Object>> list) {
	if (list.size() > 0 && list != null) {
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Cell cell = null;
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		for (int i = 0; i < list.size(); i++) {
			Row row = sheet.createRow(i + 1);
			Map<String, Object> map = list.get(i);
			for (int j = 0; j < titlearray.length; j++) {
				cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
				String field=titlearray[j].toString();
				Object value = map.get(field);
				if (value != null) {
					if(field.equals("image")&&value!=null) {
						 String image=fileUpload.getPictureImage(value.toString());
						try {
							DownloadExcelUtil.insertImageToCell(workbook, sheet, i+1, j, image, "xlsx");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if(value instanceof BigDecimal) {
						cell.setCellValue(Double.parseDouble(value.toString()));
					}else {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
	} 
}
 
public static void setWorkbook(SXSSFWorkbook workbook,Map<String, Object> titlemap, List<Map<String, Object>> list) {
	if (list.size() > 0 && list != null) {
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Cell cell = null;
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		for (int i = 0; i < list.size(); i++) {
			Row row = sheet.createRow(i + 1);
			Map<String, Object> map = list.get(i);
			for (int j = 0; j < titlearray.length; j++) {
				cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
				Object value = map.get(titlearray[j].toString());
				if (value != null) {
					if(value instanceof BigDecimal) {
						cell.setCellValue(Double.parseDouble(value.toString()));
					}else {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
	} 
}
}
