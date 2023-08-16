package com.wimoor.amazon.inbound.controller;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.admin.api.AdminClientOneFeign;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundshipmentTraceupload;
import com.wimoor.amazon.inbound.service.IShipInboundshipmentTraceuploadService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-26
 */
@Api(tags = "发货报表")
@RestController
@RequestMapping("/api/v1/ship/shipInboundshipmentTraceupload")
@SystemControllerLog("发货报表")
@RequiredArgsConstructor
public class ShipInboundshipmentTraceuploadController {
  
	final IShipInboundshipmentTraceuploadService iShipInboundshipmentTraceuploadService;
	final AdminClientOneFeign adminClientOneFeign;
	
	@GetMapping(value = "/downloadTraceuploadTemp")
	public void downloadTraceuploadTempAction(HttpServletResponse response) {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=" + "downloadTraceuploadTemp" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			iShipInboundshipmentTraceuploadService.getTempExcelReport(workbook);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 @PostMapping(value = "/uploadTraceuploadFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public Result<String> uploadWarhouseSKUFileAction(@RequestParam("file")MultipartFile file)  {
		       UserInfo user=UserInfoContext.get();
				if (file != null) {
					try {
						InputStream inputStream = file.getInputStream();
						Workbook workbook = WorkbookFactory.create(inputStream);
						Sheet sheet = workbook.getSheetAt(0);
						for (int i = 1; i <= sheet.getLastRowNum(); i++) {
							Row info=sheet.getRow(i);
							if(info==null || info.getCell(0)==null) {
								continue;
							}
							iShipInboundshipmentTraceuploadService.uploadTraceuploadFile(user, info);
						}
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
			return Result.success("ok");
		}
	 
	 @GetMapping(value = "recordList")
		public Result<?> findrecordListAction(String shipmentid)   {
		    UserInfo user=UserInfoContext.get();
		    LambdaQueryWrapper<ShipInboundshipmentTraceupload> queryWrapper=new LambdaQueryWrapper<ShipInboundshipmentTraceupload>();
		    queryWrapper.eq(ShipInboundshipmentTraceupload::getShopid, user.getCompanyid());
		    if(StrUtil.isNotEmpty(shipmentid)) {
		    	queryWrapper.eq(ShipInboundshipmentTraceupload::getShipmentid, shipmentid);
		    }
		    List<ShipInboundshipmentTraceupload> list = iShipInboundshipmentTraceuploadService.list(queryWrapper);
		    if(list!=null && list.size()>0) {
		    	for (ShipInboundshipmentTraceupload item:list) {
		    		 Result<UserInfo> userdata = adminClientOneFeign.getUserByUserId(item.getCreator());
		    		 String creator = userdata.getData().getAccount();
		    		 item.setCreator(creator);
				}
		    }
			return Result.success(list);
		}
}

