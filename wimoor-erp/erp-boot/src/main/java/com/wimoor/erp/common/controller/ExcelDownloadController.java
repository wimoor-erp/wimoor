package com.wimoor.erp.common.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.common.pojo.entity.DownloadReport;
import com.wimoor.erp.common.service.IDownloadReportService;
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
	final IDownloadReportService downloadReportService;
	@GetMapping("/downloadlist")
	public Result<List<DownloadReport>> downloadlistAction(String ftype) {
		UserInfo userinfo = UserInfoContext.get();
		List<DownloadReport> result = downloadReportService.lambdaQuery().eq(DownloadReport::getFtype, ftype).eq(DownloadReport::getUserid, userinfo.getId()).eq(DownloadReport::getShopid, userinfo.getCompanyid()).list();
		return Result.success(result);
	}

	@GetMapping("/deletedownload")
	public Result<Boolean> deletedownloadAction(String id) {
		return Result.success(downloadReportService.removeById(id));
	}
	@GetMapping(value = "/downExcel")
	public void downExcelByIdAction(String id,HttpServletRequest request, HttpServletResponse response) {
		Workbook workbook = null;
		ServletOutputStream fOut = null;
		try {
			DownloadReport report = downloadReportService.getById(id);
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=" + report.getFtype() +".xlsx");// 设置文件名
			fOut = response.getOutputStream();
			InputStream is = new ByteArrayInputStream(report.getContent());
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
