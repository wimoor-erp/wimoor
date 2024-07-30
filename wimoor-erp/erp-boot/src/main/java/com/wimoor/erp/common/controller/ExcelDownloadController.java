package com.wimoor.erp.common.controller;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.mybatisplus.MysqlGenerator;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

 
@Api(tags = "模板文件下载")
@RestController
@RequestMapping("/api/v1/common/excelDownload")
@RequiredArgsConstructor
public class ExcelDownloadController {
 
	
	@GetMapping(value = "/downExcelTemp")
	public void downExcelTempAction(String ftype,HttpServletRequest request, HttpServletResponse response) {
		Workbook workbook = null;
		ServletOutputStream fOut = null;
		try {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=" + ftype +".xlsx");// 设置文件名
			fOut = response.getOutputStream();
			InputStream is = new ClassPathResource("template/"+ftype +".xlsx").getInputStream();
			// 创建新的Excel工作薄
			workbook = WorkbookFactory.create(is);
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
	
	 @GetMapping("/createpojo")
	 public Result<String> createPojoAction(String table,String pkg) {
	    	MysqlGenerator.autoGenerator( table, pkg, "t_erp_");
	        return Result.success("true");
	 }
}
