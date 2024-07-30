package com.wimoor.amazon.orders.controller;


import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersReturnDTO;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersReturnVo;
import com.wimoor.amazon.orders.service.IOrderReturnService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;

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
 
    @Resource
    IOrderReturnService orderReturnService;

  	@ApiOperation("订单退货列表")
  	@PostMapping("/returnlist")
    public  Result<IPage<AmazonOrdersReturnVo>> returnlistAction(
    		@RequestBody AmazonOrdersReturnDTO condition){
		UserInfo userinfo = UserInfoContext.get();
		if(userinfo.isLimit(UserLimitDataType.operations)) {
			condition.setOwner(userinfo.getId());
		}
		IPage<AmazonOrdersReturnVo> list=orderReturnService.selectReturnsList(condition);
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
			UserInfo userinfo = UserInfoContext.get();
			if(userinfo.isLimit(UserLimitDataType.operations)) {
				condition.setOwner(userinfo.getId());
			}
			orderReturnService.downloadReturnlist(workbook,condition);
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

