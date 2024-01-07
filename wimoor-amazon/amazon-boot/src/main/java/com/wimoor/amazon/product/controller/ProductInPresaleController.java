package com.wimoor.amazon.product.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.pojo.vo.Chart;
import com.wimoor.amazon.product.pojo.dto.ProductPresaleListDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInPresale;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanService;
import com.wimoor.amazon.product.service.IProductInPresaleService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/product/presale")
public class ProductInPresaleController {
	final IProductInPresaleService iProductInPresaleService;
    final IAmzProductSalesPlanService iAmzProductSalesPlanService;
    final IAmazonGroupService amazonGroupService;
    final IMarketplaceService marketplaceService;
    @GetMapping("/getSales")
    Result<Chart> getSales(String marketplaceid,String amazonauthid,String sku) {
    	UserInfo user = UserInfoContext.get();
    	String shopid=user.getCompanyid();
    	return Result.success(iProductInPresaleService.getSales(shopid, marketplaceid, amazonauthid,sku)) ;
    }
    
    @PostMapping("/getProductMonthSales")
    public Result<IPage<Map<String, Object>>> listProductAction(@RequestBody ProductPresaleListDTO dto){
    	UserInfo user = UserInfoContext.get();
    	dto.setShopid(user.getCompanyid());
    	if(user.isLimit(UserLimitDataType.operations)||user.isLimit(UserLimitDataType.owner)) {
    		dto.setOwner(user.getId());
    	}
    	return Result.success(iProductInPresaleService.listProduct(dto));
    }
    @GetMapping("/convert")
    Result<Chart> convertEstimatToPresaleAction() {
    	iProductInPresaleService.convertEstimatToPresale();
    	return Result.success() ;
    }
 
	@PostMapping(value = "getProductPreSalesByMonth")
	public Result<?> getProductPreSalesByMonthAction(@RequestBody ProductPresaleListDTO dto) {
		String sku = dto.getSku();
		String groupid =dto.getGroupid();
		String marketplaceid = dto.getMarketplaceid();
		List<Map<String,Object>> result = iProductInPresaleService.getProductPreSalesByMonth(sku, marketplaceid, groupid);
		return Result.success(result) ;
	}
    
 
	@PostMapping(value = "getProductPreSales")
	public Result<?> getProductPreSalesAction(@RequestBody ProductPresaleListDTO dto)  {
		String sku = dto.getSku();
		String groupid =dto.getGroupid();
		String marketplaceid = dto.getMarketplaceid();
		String month =dto.getMonth();
		List<Map<String, Object>> result = iProductInPresaleService.getProductPreSales(sku, marketplaceid, groupid,month);
		return Result.success(result) ;
	}
	
    @PostMapping("/save")
	@Transactional
    Result<?> save(@RequestBody List<ProductInPresale> preList) {
    	UserInfo user = UserInfoContext.get();
    	String sku=null;
    	String groupid=null;
    	String marketplaceid=null;
    	if(preList==null||preList.size()<=0) {
    		throw new BizException("提交内容为空，请检查参数是否正确");
    	}
    	for(ProductInPresale item:preList) {
    		item.setOperator(user.getId());
    		item.setOpttime(new Date());
    		item.getAmazonauthid();
			if(item.getQuantity()<0) {
				throw new BizException("预估销量不能是负数");
			}
			if(sku==null) {
				sku=item.getSku();
				groupid=item.getGroupid();
				marketplaceid=item.getMarketplaceid();
			}
    	}
    	
    	iProductInPresaleService.replaceBatch(preList);
		iAmzProductSalesPlanService.refreshData(groupid,marketplaceid,sku);
    	return Result.success() ;
    }
    @PostMapping("/clear")
    Result<?> remove(@RequestBody ProductInPresale pre) {
		LambdaQueryWrapper<ProductInPresale> query=new LambdaQueryWrapper<ProductInPresale>();
		query.eq(ProductInPresale::getSku, pre.getSku());
		query.eq(ProductInPresale::getMarketplaceid, pre.getMarketplaceid());
		query.eq(ProductInPresale::getGroupid, pre.getGroupid());
		iProductInPresaleService.remove(query);
		iAmzProductSalesPlanService.refreshData(pre.getGroupid(),pre.getMarketplaceid(),pre.getSku());
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
		cell0.setCellValue("平台SKU");
		cell0 = row2.createCell(0);
		cell0.setCellValue("平台SKU");
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("店铺");
		cell1 = row2.createCell(1);
		cell1.setCellValue("店铺");
		Cell cell2 = row.createCell(2);
		cell2.setCellValue("站点");
		cell2 = row2.createCell(2);
		cell2.setCellValue("站点");
		Cell cell3 = row.createCell(3);
		cell3.setCellValue("年份");
		cell3 = row2.createCell(3);
		cell3.setCellValue("年份");
		
		Cell cell4 = row2.createCell(4);
		cell4.setCellValue("月份");
		
		Cell cell = null;
		for(int i=1;i<=12;i++) {
		    cell = row.createCell(i+3);
		    String month = formatDigit(i);
		    cell.setCellValue(month+"月");
		}
		for(int i=1;i<=31;i++) {
		    cell = row2.createCell(i+4);
		    cell.setCellValue(i+"日");
		} 
 

		List<AmazonGroup> alllist = amazonGroupService.selectByShopId(user.getCompanyid());
		List<Marketplace> marketlist = marketplaceService.findMarketplaceByShopId(user.getCompanyid());
		List<String> listgroup = new ArrayList<String>();
		List<String> listmarket = new ArrayList<String>();
 
		for (AmazonGroup group : alllist) {
			listgroup.add(group.getName());
		}
		listmarket.add("欧洲");
		for (Marketplace market : marketlist) {
			if(!market.getRegion().equals("EU")) {
				listmarket.add(market.getName());
			}
		}

		String[] agroup = listgroup.toArray(new String[listgroup.size()]);
		String[] amarket = listmarket.toArray(new String[listmarket.size()]);
		DataValidationHelper helper = sheet.getDataValidationHelper();
 
		if (agroup != null && agroup.length > 0) {
			CellRangeAddressList dstAddrList0 = new CellRangeAddressList(1, 700, 1, 1);// 规则一单元格范围
			DataValidation dstDataValidation0 = helper.createValidation(helper.createExplicitListConstraint(agroup), dstAddrList0);
			dstDataValidation0.createPromptBox("提示头", "填入店铺");
			dstDataValidation0.setShowErrorBox(true);
			dstDataValidation0.setShowPromptBox(true);
			dstDataValidation0.setEmptyCellAllowed(false);
			sheet.addValidationData(dstDataValidation0);
			sheet2.addValidationData(dstDataValidation0);
		}
		if (amarket != null && amarket.length > 0) {
			CellRangeAddressList dstAddrList1 = new CellRangeAddressList(1, 600, 2, 2);// 规则一单元格范围
			DataValidation dstDataValidation1 = helper.createValidation(helper.createExplicitListConstraint(amarket), dstAddrList1);
			dstDataValidation1.createPromptBox("提示头", "填入站点");
			dstDataValidation1.setShowErrorBox(true);
			dstDataValidation1.setShowPromptBox(true);
			dstDataValidation1.setEmptyCellAllowed(false);
			sheet.addValidationData(dstDataValidation1);
			sheet2.addValidationData(dstDataValidation1);
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
		cell0.setCellValue("平台SKU");
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("店铺");
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
		cell0.setCellValue("平台SKU");
		cell1 = row2.createCell(1);
		cell1.setCellValue("店铺");
		cell2 = row2.createCell(2);
		cell2.setCellValue("站点");
		cell3 = row2.createCell(3);
		cell3.setCellValue("年份");
		Cell cell4 = row2.createCell(4);
		cell4.setCellValue("月份");
		for(int i=1;i<=31;i++) {
		    cell = row2.createCell(i+4);
		    cell.setCellValue(i+"日");
		} 
		iProductInPresaleService.downloadPreSalesByExcel(sheet,sheet2,groupid, user);
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
						iProductInPresaleService.uploadPreSalesByExcel(workbook, user);
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
