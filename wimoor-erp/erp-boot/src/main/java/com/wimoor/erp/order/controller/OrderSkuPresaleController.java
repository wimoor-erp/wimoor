package com.wimoor.erp.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;
import com.wimoor.erp.order.pojo.dto.PresaleListDTO;
import com.wimoor.erp.order.pojo.entity.OrderSkuPresale;
import com.wimoor.erp.order.service.IOrderSkuPresaleService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order/presale")
public class OrderSkuPresaleController {
    final IOrderSkuPresaleService iOrderSkuPresaleService;
    @GetMapping("/getSales")
    Result<com.wimoor.erp.common.pojo.entity.Chart> getSales(String warehouseid,String sku) {
    	UserInfo user = UserInfoContext.get();
    	String shopid=user.getCompanyid();
    	return Result.success(iOrderSkuPresaleService.getSales(shopid, warehouseid,sku)) ;
    }
    
    @PostMapping("/getProductMonthSales")
    public Result<IPage<Map<String, Object>>> listProductAction(@RequestBody PresaleListDTO dto){
    	UserInfo user = UserInfoContext.get();
    	dto.setShopid(user.getCompanyid());
    	if(user.isLimit(UserLimitDataType.operations)||user.isLimit(UserLimitDataType.owner)) {
    		dto.setOwner(user.getId());
    	}
    	return Result.success(iOrderSkuPresaleService.listProduct(dto));
    }
    @GetMapping("/convert")
    Result<Chart> convertEstimatToPresaleAction() {
		iOrderSkuPresaleService.convertEstimatToPresale();
    	return Result.success() ;
    }
 
	@PostMapping(value = "getProductPreSalesByMonth")
	public Result<?> getProductPreSalesByMonthAction(@RequestBody PresaleListDTO dto) {
		String sku = dto.getSku();
		String warehouseid =dto.getWarehouseid();
		List<Map<String,Object>> result = iOrderSkuPresaleService.getProductPreSalesByMonth(sku, warehouseid);
		return Result.success(result) ;
	}
    
 
	@PostMapping(value = "getProductPreSales")
	public Result<?> getProductPreSalesAction(@RequestBody PresaleListDTO dto)  {
		String sku = dto.getSku();
		String warehouseid =dto.getWarehouseid();
		String month =dto.getMonth();
		List<Map<String, Object>> result = iOrderSkuPresaleService.getProductPreSales(sku, warehouseid,month);
		return Result.success(result) ;
	}
	
    @PostMapping("/save")
	@Transactional
	public Result<?> saveAction(@RequestBody List<OrderSkuPresale> preList) {
    	UserInfo user = UserInfoContext.get();
    	String sku=null;
    	String warehouseid=null;
    	if(preList==null||preList.size()<=0) {
    		throw new BizException("提交内容为空，请检查参数是否正确");
    	}
    	for(OrderSkuPresale item:preList) {
    		item.setOperator(user.getId());
    		item.setOpttime(new Date());
			if(item.getQuantity()<0) {
				throw new BizException("预估销量不能是负数");
			}
			if(sku==null) {
				sku=item.getSku();
				warehouseid=item.getWarehouseid();
			}
    	}

		iOrderSkuPresaleService.replaceBatch(preList);
		iOrderSkuPresaleService.refreshData(warehouseid);
    	return Result.success() ;
    }
    @PostMapping("/clear")
    Result<?> remove(@RequestBody OrderSkuPresale pre) {
		LambdaQueryWrapper<OrderSkuPresale> query=new LambdaQueryWrapper<OrderSkuPresale>();
		query.eq(OrderSkuPresale::getSku, pre.getSku());
		query.eq(OrderSkuPresale::getWarehouseid, pre.getWarehouseid());
		iOrderSkuPresaleService.remove(query);
		iOrderSkuPresaleService.refreshData(pre.getWarehouseid());
    	return Result.success() ;
    }
	public static String formatDigit(int sign){
        if(sign==1) return "一";
        if(sign==2) return "二";
        if(sign==3) return "三";
        if(sign==4) return "四";
        if(sign==5) return "五";
        if(sign==6) return "六";
        if(sign==7) return "七";
        if(sign==8) return "八";
        if(sign==9) return "九";
        if(sign==10) return "十";
        if(sign==11) return "十一";
        if(sign==12) return "十二";
        return "";
    }
	
    @GetMapping(value = "/downExcelTemp")
	public String downExcelTempAction(  HttpServletResponse response )  {
    	UserInfo user = UserInfoContext.get();
		try {
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition", "attachment;fileName=SalesPlanTemplate.xlsx");// 设置文件名
		ServletOutputStream fOut = response.getOutputStream();
		// 将数据写入Excel
		Sheet sheet = workbook.createSheet("按月销量");
		Sheet sheet2 = workbook.createSheet("按日销量");
		// 在索引0的位置创建行（最顶端的行）
		Row row2 =sheet2.createRow(0);
		Row row = sheet.createRow(0);
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("SKU");
		cell0 = row2.createCell(0);
		cell0.setCellValue("SKU");
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("仓库");
		cell1 = row2.createCell(1);
		cell1.setCellValue("仓库");
		Cell cell3 = row.createCell(2);
		cell3.setCellValue("年份");
		cell3 = row2.createCell(2);
		cell3.setCellValue("年份");
		Cell cell4 = row2.createCell(3);
		cell4.setCellValue("月份");
		
		Cell cell = null;
		for(int i=1;i<=12;i++) {
		    cell = row.createCell(i+2);
		    String month = formatDigit(i);
		    cell.setCellValue(month+"月");
		}
		for(int i=1;i<=31;i++) {
		    cell = row2.createCell(i+3);
		    cell.setCellValue(i+"日");
		}

		workbook.write(fOut);
		workbook.close();
		fOut.flush();
		fOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    @GetMapping(value = "/downExcelSales")
	public String downExcelSalesAction(String groupid,  HttpServletResponse response )  {
    	UserInfo user = UserInfoContext.get();
		try {
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition", "attachment;fileName=SalesPlanTemplate.xlsx");// 设置文件名
		ServletOutputStream fOut = response.getOutputStream();
		// 将数据写入Excel
		Sheet sheet = workbook.createSheet("按月销量");
		Row row = sheet.createRow(0);
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("SKU");
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("仓库");
		Cell cell2 = row.createCell(2);
		cell2.setCellValue("站点");
		Cell cell3 = row.createCell(3);
		cell3.setCellValue("年份");
		Cell cell = null;
		for(int i=1;i<=12;i++) {
		    cell = row.createCell(i+3);
		    String month = formatDigit(i);
		    cell.setCellValue(month+"月");
		}
		
		Sheet sheet2 = workbook.createSheet("按日销量");
		// 在索引0的位置创建行（最顶端的行）
		Row row2 =sheet2.createRow(0);
		cell0 = row2.createCell(0);
		cell0.setCellValue("SKU");
		cell1 = row2.createCell(1);
		cell1.setCellValue("仓库");
		cell2 = row2.createCell(2);
		cell3.setCellValue("年份");
		Cell cell4 = row2.createCell(3);
		cell4.setCellValue("月份");
		for(int i=1;i<=31;i++) {
		    cell = row2.createCell(i+3);
		    cell.setCellValue(i+"日");
		}
		iOrderSkuPresaleService.downloadPreSalesByExcel(sheet,sheet2,groupid, user);
		workbook.write(fOut);
		workbook.close();
		fOut.flush();
		fOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    
    @RequestMapping(value = "/uploadSalesExcel")
	public Result<String> uploadExcelAction(HttpServletRequest request, HttpServletResponse response) {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		UserInfo user = UserInfoContext.get();
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					try {
						InputStream inputStream = file.getInputStream();
						Workbook workbook = WorkbookFactory.create(inputStream);
						iOrderSkuPresaleService.uploadPreSalesByExcel(workbook, user);
						workbook.close();
						return Result.success();
					} catch (IOException e) {
						e.printStackTrace();
						return Result.failed();
					} catch (EncryptedDocumentException e) {
						e.printStackTrace();
					} catch (InvalidFormatException e) {
						e.printStackTrace();
					} catch (Exception e) {
						 
						e.printStackTrace();
					}
				}
			}
		}
		return Result.failed();
	}
}
