package com.wimoor.amazon.orders.controller;


import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersReturnDTO;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersReturnVo;
import com.wimoor.amazon.orders.service.IAmzOrderItemService;
import com.wimoor.amazon.orders.service.IAmzOrderMainService;
import com.wimoor.amazon.orders.service.IAmzOrderSolicitationsService;
import com.wimoor.amazon.orders.service.IOrderManagerService;
import com.wimoor.amazon.report.service.IReportAmzOrderInvoiceService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.OSSApiService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 订单抓取 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Api(tags = "订单退货接口")
@RestController
@RequestMapping("/api/v0/orders/return")
public class OrdersReturnController{
	 
	@Autowired
	IAmzOrderMainService amzOrderMainService;
	@Autowired
	IAmzOrderItemService amzOrderItemService;
	@Autowired
	IMarketplaceService marketplaceService;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Resource
	IOrderManagerService orderManagerService;
	@Resource
	IAmazonGroupService amazonGroupService;
	@Resource
	ISerialNumService serialNumService;
	@Resource
	IPictureService  pictureService;
	@Resource
	OSSApiService ossApiService;
	@Resource
	IAmzOrderSolicitationsService iAmzOrderSolicitationsService;
    @Resource
    IReportAmzOrderInvoiceService iReportAmzOrderInvoiceService;

  	@ApiOperation("订单退货列表")
  	@PostMapping("/returnlist")
    public  Result<IPage<AmazonOrdersReturnVo>> returnlistAction(
    		@RequestBody AmazonOrdersReturnDTO condition){
		IPage<AmazonOrdersReturnVo> list=orderManagerService.selectReturnsList(condition);
		return Result.success(list);
	}
  	
  	@ApiOperation("订单退货列表导出")
  	@GetMapping("/downloadReturnlist")
    public  void downloadlistAction(
    		 AmazonOrdersReturnDTO condition,
    		 HttpServletResponse response){
  		try {
	  		SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=ReturnsReport.xlsx");// 设置文件名
			orderManagerService.downloadReturnlist(workbook,condition);
			ServletOutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
  	
	

}

