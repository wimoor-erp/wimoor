package com.wimoor.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import com.wimoor.common.mvc.FileUpload;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.streaming.SXSSFSheet;

public class ExcelExportUtil {

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
				anchor = new XSSFClientAnchor();
				anchor.setCol1(colNum);
				anchor.setCol2(colNum+1);
				anchor.setRow1(rowNum );
				anchor.setRow2(rowNum+ 1);
				drawing.createPicture(anchor, inputImagePictureID);
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
							ExcelExportUtil.insertImageToCell(workbook, sheet, i+1, j, image, "xlsx");
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
 
public static void setWorkbook(Workbook workbook,Map<String, Object> titlemap, List<Map<String, Object>> list) {
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


	/**
	 * 通用Excel导出方法
	 * @param workbook SXSSFWorkbook对象
	 * @param sheetName 工作表名称
	 * @param headers 表头配置（LinkedHashMap保持顺序）
	 *                key: 数据字段名, value: 表头显示名称
	 * @param dataList 数据列表
	 * @param valueConverters 字段值转换器（可选）
	 *                        key: 数据字段名, value: 转换函数
	 */
	public static void exportToExcel(SXSSFWorkbook workbook,
									 String sheetName,
									 LinkedHashMap<String, String> headers,
									 List<Map<String, Object>> dataList,
									 Map<String, Function<Object, Object>> valueConverters) {

		// 创建Sheet
		Sheet sheet = workbook.createSheet(sheetName != null ? sheetName : "Sheet1");

		// 创建表头行
		Row headerRow = sheet.createRow(0);
		String[] fieldNames = headers.keySet().toArray(new String[0]);

		// 设置表头
		for (int i = 0; i < fieldNames.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers.get(fieldNames[i]));

			// 可选：设置表头样式
			setHeaderStyle(workbook, cell);
		}

		// 填充数据行
		for (int i = 0; i < dataList.size(); i++) {
			Row row = sheet.createRow(i + 1);
			Map<String, Object> data = dataList.get(i);

			for (int j = 0; j < fieldNames.length; j++) {
				String fieldName = fieldNames[j];
				Cell cell = row.createCell(j);
				Object value = data.get(fieldName);

				// 应用值转换器
				if (valueConverters != null && valueConverters.containsKey(fieldName)) {
					value = valueConverters.get(fieldName).apply(value);
				}

				// 设置单元格值
				setCellValue(cell, value);
			}
		}

		// 自动调整列宽
		autoSizeColumns(sheet, fieldNames.length);
	}

	/**
	 * 简化的导出方法（无值转换器）
	 */
	public static void exportToExcel(SXSSFWorkbook workbook,
									 String sheetName,
									 LinkedHashMap<String, String> headers,
									 List<Map<String, Object>> dataList) {
		exportToExcel(workbook, sheetName, headers, dataList, null);
	}

	/**
	 * 设置单元格值（支持不同类型）
	 */
	private static void setCellValue(Cell cell, Object value) {
		if (value == null) {
			cell.setCellValue("");
			return;
		}

		if (value instanceof Number) {
			if (value instanceof Integer || value instanceof Long) {
				cell.setCellValue(((Number) value).doubleValue());
			} else {
				cell.setCellValue(((Number) value).doubleValue());
			}
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof java.util.Date) {
			CellStyle dateStyle = cell.getSheet().getWorkbook().createCellStyle();
			CreationHelper createHelper = cell.getSheet().getWorkbook().getCreationHelper();
			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
			cell.setCellStyle(dateStyle);
			cell.setCellValue((java.util.Date) value);
		} else if (value instanceof java.time.LocalDate) {
			cell.setCellValue(value.toString());
		} else if (value instanceof java.time.LocalDateTime) {
			cell.setCellValue(value.toString());
		} else {
			cell.setCellValue(value.toString());
		}
	}

	/**
	 * 设置表头样式
	 */
	private static void setHeaderStyle(SXSSFWorkbook workbook, Cell cell) {
		CellStyle headerStyle = workbook.createCellStyle();

		// 设置字体加粗
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerStyle.setFont(headerFont);

		// 设置背景色
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// 设置边框
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);

		cell.setCellStyle(headerStyle);
	}

	/**
	 * 自动调整列宽（实用版）- 包含最小宽度和边距
	 */
	private static void autoSizeColumns(Sheet sheet, int columnCount) {
		final int MIN_WIDTH = 12;     // 最小12个字符
		final int PADDING = 3;        // 加3个字符边距

		if (sheet instanceof SXSSFSheet) {
			SXSSFSheet sxssfSheet = (SXSSFSheet) sheet;
			for (int i = 0; i < columnCount; i++) {
				try {
					sxssfSheet.trackColumnForAutoSizing(i);
					sxssfSheet.autoSizeColumn(i);

					// 确保最小宽度并添加边距
					int width = sxssfSheet.getColumnWidth(i);
					int chars = width / 256;
					int targetChars = Math.max(chars + PADDING, MIN_WIDTH);

					// 可选：限制最大宽度
					if (targetChars > 100) targetChars = 100;

					sxssfSheet.setColumnWidth(i, targetChars * 256);

				} catch (Exception e) {
					sxssfSheet.setColumnWidth(i, MIN_WIDTH * 256);
				}
			}
		} else {
			for (int i = 0; i < columnCount; i++) {
				try {
					sheet.autoSizeColumn(i);

					// 确保最小宽度并添加边距
					int width = sheet.getColumnWidth(i);
					int chars = width / 256;
					int targetChars = Math.max(chars + PADDING, MIN_WIDTH);

					// 可选：限制最大宽度
					if (targetChars > 100) targetChars = 100;

					sheet.setColumnWidth(i, targetChars * 256);

				} catch (Exception e) {
					sheet.setColumnWidth(i, MIN_WIDTH * 256);
				}
			}
		}
	}
}
