package com.wimoor.manager.controller;

import cn.hutool.core.util.StrUtil;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.manager.pojo.entity.SysCustomerInvoice;
import com.wimoor.manager.pojo.entity.SysCustomerOrder;
import com.wimoor.manager.service.ISysCustomerInvoiceService;
import com.wimoor.manager.service.ISysCustomerOrderService;
import com.wimoor.sys.tool.pojo.entity.LargeFile;
import com.wimoor.sys.tool.service.ILargeFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 客户发票表 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Api(tags = "客户发票接口")
@RestController
@RequestMapping("/api/v1/invoice")
@RequiredArgsConstructor
public class SysCustomerInvoiceController {
	final ISysCustomerInvoiceService iSysCustomerInvoiceService;
	final ILargeFileService iLargeFileService;
	final FileUpload fileUpload;
	final ISysCustomerOrderService iSysCustomerOrderService;
	@GetMapping("/detail")
	public Result<SysCustomerInvoice> detail(String orderid) {
		SysCustomerInvoice invoice = iSysCustomerInvoiceService.findByOrderId(new BigInteger(orderid));
		if(invoice !=null&&invoice.getInvoiceFile()!=null){
			LargeFile largefile=iLargeFileService.getById(invoice.getInvoiceFile());
			String url = fileUpload.getPictureImage(largefile.getLocation());
			invoice.setLocation(url);
		}
		return Result.success(invoice);
	}
	
	@ApiOperation("保存发票信息")
	@PostMapping("/save")
	public Result<?> save(@RequestBody SysCustomerInvoice invoice) {
		UserInfo userInfo = UserInfoContext.get();
		// 如果shopid为空，自动从订单表获取
		invoice.setShopid(new BigInteger(userInfo.getCompanyid()));
		SysCustomerInvoice existing = iSysCustomerInvoiceService.findByOrderId(invoice.getOrderid());
		if(invoice.getInvoiceFile()!=null){
			SysCustomerOrder order = iSysCustomerOrderService.getById(invoice.getOrderid());
			if(order!=null&&!"invoiced".equals(order.getIvcstatus())){
				order.setIvcstatus("invoiced");
				iSysCustomerOrderService.updateById(order);
			}
		}
		if (existing != null) {
			invoice.setId(existing.getId());
			return Result.success(iSysCustomerInvoiceService.updateById(invoice));
		} else {
			return Result.success(iSysCustomerInvoiceService.save(invoice));
		}
	}
	
	@PostMapping("/upload")
	public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file,String orderid) {
		if (file.isEmpty()) {
			return Result.failed("请选择文件");
		}
		UserInfo userInfo = UserInfoContext.get();
		return uploadPrivate(file, userInfo,orderid);
	}
	
	public Result<Map<String, String>> uploadPrivate(MultipartFile file, UserInfo userInfo,String orderid) {
		if (file.isEmpty()) {
			return Result.failed("请选择文件");
		}
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
			InputStream inputStream = file.getInputStream();
			String fileName = file.getOriginalFilename();
			String fileNameWithoutExtension = fileName;
			String fileExtension = "";
			
			if (fileName.contains(".")) {
				String[] fileInfo = fileName.split("\\.");
				fileNameWithoutExtension = fileInfo[0];
				fileExtension = fileInfo[1];
			}
			
			String timekey = format.format(new Date());
			if (fileExtension.equals("xml") && fileNameWithoutExtension.contains(timekey)) {
				fileName = fileNameWithoutExtension + "." + fileExtension;
			} else {
				SimpleDateFormat TIMESTAMP_MS_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				fileName = fileNameWithoutExtension + TIMESTAMP_MS_FORMAT.format(new Date()) + "." + fileExtension;
			}
			SysCustomerInvoice invoice =null;
			if(StrUtil.isNotBlank(orderid)){
				  invoice = iSysCustomerInvoiceService.findByOrderId(new BigInteger(orderid));
				if (invoice!=null&&invoice.getInvoiceFile() != null ) {
					LargeFile largfile = iLargeFileService.getById(invoice.getInvoiceFile());
				    iLargeFileService.deleteLargeFile("invoice", largfile.getLocation(), userInfo.getCompanyid());
				}
			}
			LargeFile largefile = iLargeFileService.uploadLargeFile(inputStream, "invoice", userInfo.getCompanyid(), fileName);
			String url = fileUpload.getPictureImage(largefile.getLocation());
			if(invoice !=null){
				invoice.setInvoiceFile( largefile.getId());
				SysCustomerOrder order = iSysCustomerOrderService.getById(invoice.getOrderid());
				if(order!=null&&!"invoiced".equals(order.getIvcstatus())){
					order.setIvcstatus("invoiced");
					iSysCustomerOrderService.updateById(order);
				}
				iSysCustomerInvoiceService.updateById(invoice);
			}
			Map<String, String> result = new HashMap<>();
			result.put("invoiceFile", largefile.getId());
			result.put("location", largefile.getLocation());
			return Result.success(result);
		} catch (IOException e) {
			return Result.failed("上传失败");
		}
	}
	
	public Result<?> savePrivate(SysCustomerInvoice invoice, UserInfo userInfo) {
		SysCustomerInvoice existing = iSysCustomerInvoiceService.findByOrderId(invoice.getOrderid());
		if (existing != null) {
			if (existing.getLocation() != null && !existing.getLocation().isEmpty()) {
				String[] oldLocations = existing.getLocation().split(";");
				for (String oldLocation : oldLocations) {
					if (!oldLocation.isEmpty()) {
						iLargeFileService.deleteLargeFile("invoice", oldLocation, userInfo.getCompanyid());
					}
				}
			}
			invoice.setId(existing.getId());
			return Result.success(iSysCustomerInvoiceService.updateById(invoice));
		} else {
			return Result.success(iSysCustomerInvoiceService.save(invoice));
		}
	}
}