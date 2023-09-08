package com.wimoor.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

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
						cell.setCellValue(new Double(value.toString()));
					}else {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
	} 
}
}
