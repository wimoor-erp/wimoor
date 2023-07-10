package com.wimoor.amazon.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import cn.hutool.core.util.StrUtil;

public class ExcelUtil {

	public static Double getNumeric(Cell cell) {
		 Double result=null;
		 if(cell==null)return null;
		 if(cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
			 result=cell.getNumericCellValue();
		    }
	    if(cell.getCellTypeEnum().equals(CellType.STRING)) {
	    	String str=cell.getStringCellValue();
	    	if(StrUtil.isAllNotBlank(str))
	    	result=Double.parseDouble(str);
	    }
	    return result;
	}
}
