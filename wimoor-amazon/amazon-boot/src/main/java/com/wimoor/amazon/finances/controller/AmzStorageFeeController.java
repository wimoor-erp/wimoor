package com.wimoor.amazon.finances.controller;

import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.finances.pojo.dto.AmzStorageFeeListDTO;
import com.wimoor.amazon.finances.service.IAmzStorageFeeService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "亚马逊订单财务接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/amzStorageFee")
public class AmzStorageFeeController {

	   final IAmazonAuthorityService iAmazonAuthorityService;
	   final IAmzStorageFeeService iAmzStorageFeeService;
	   final IMarketplaceService iMarketplaceService;
	   
	   @ApiOperation(value = "长期仓储费list")
	   @PostMapping("/list")
	   public Result<?> listAction(@RequestBody AmzStorageFeeListDTO dto) {
		   UserInfo user = UserInfoContext.get();
		   Map<String, Object> parameter=new HashedMap<String, Object>();
		   parameter.put("shopid", user.getCompanyid());
		   if (StrUtil.isEmpty(dto.getGroupid())) {
				parameter.put("groupid", null);
		   } else {
				parameter.put("groupid", dto.getGroupid());
		   }
		   if (StrUtil.isEmpty(dto.getMarketplaceid())) {
				parameter.put("marketplaceid", null);
		   } else {
				parameter.put("marketplaceid", dto.getMarketplaceid());
		   }
		   if (StrUtil.isEmpty(dto.getSearch())) {
				parameter.put("search", null);
		   } else {
				parameter.put("search", "%"+dto.getSearch()+"%");
		   }
		   parameter.put("fromDate", dto.getFromDate());
		   parameter.put("toDate", dto.getToDate());
		   if(StrUtil.isNotEmpty(dto.getGroupid())&&StrUtil.isNotEmpty(dto.getMarketplaceid())) {
			   AmazonAuthority auth = iAmazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceid());
			   if(auth!=null) {
				   parameter.put("amazonauthid", auth.getId());
			   }
		   }
		   Page<Map<String, Object>> list = iAmzStorageFeeService.findByCondition(dto.getPage(), parameter);
		   return Result.success(list);
	   }
	   
	   
	   @ApiOperation(value = "仓储费list")
	   @PostMapping("/storageList")
	   public Result<?> storagelistAction(@RequestBody AmzStorageFeeListDTO dto) {
		   UserInfo user = UserInfoContext.get();
		   Map<String, Object> parameter=new HashedMap<String, Object>();
		   parameter.put("shopid", user.getCompanyid());
		   if (StrUtil.isEmpty(dto.getGroupid())) {
				parameter.put("groupid", null);
		   } else {
				parameter.put("groupid", dto.getGroupid());
		   }
		   if (StrUtil.isEmpty(dto.getMarketplaceid())) {
				parameter.put("marketplaceid", null);
		   } else {
				parameter.put("marketplaceid", dto.getMarketplaceid());
				parameter.put("country", iMarketplaceService.findMapByMarketplaceId().get(dto.getMarketplaceid()).getMarket());
		   }
		   if (StrUtil.isEmpty(dto.getSearch())) {
				parameter.put("search", null);
		   } else {
				parameter.put("search", "%"+dto.getSearch()+"%");
		   }
		   parameter.put("searchDate",dto.getSearchDate());
		   if(StrUtil.isNotEmpty(dto.getGroupid())&&StrUtil.isNotEmpty(dto.getMarketplaceid())) {
			   AmazonAuthority auth = iAmazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceid());
			   if(auth!=null) {
				   parameter.put("amazonauthid", auth.getId());
			   }
		   }
		   Page<Map<String, Object>> list = iAmzStorageFeeService.findStorageFeeList(dto.getPage(), parameter);
		   return Result.success(list);
	   }
	   
	   @PostMapping("/downloadList")
	   public void downDataExcelByRateAction(@RequestBody AmzStorageFeeListDTO dto, HttpServletResponse response)  {
	   	// 创建新的Excel工作薄
	   	SXSSFWorkbook workbook = new SXSSFWorkbook();
	   	// 将数据写入Excel
	   	UserInfo user = UserInfoContext.get();
		   Map<String, Object> parameter=new HashedMap<String, Object>();
		   parameter.put("shopid", user.getCompanyid());
		   if (StrUtil.isEmpty(dto.getGroupid())) {
				parameter.put("groupid", null);
		   } else {
				parameter.put("groupid", dto.getGroupid());
		   }
		   if (StrUtil.isEmpty(dto.getMarketplaceid())) {
				parameter.put("marketplaceid", null);
		   } else {
				parameter.put("marketplaceid", dto.getMarketplaceid());
				parameter.put("country", iMarketplaceService.findMapByMarketplaceId().get(dto.getMarketplaceid()).getMarket());
		   }
		   if (StrUtil.isEmpty(dto.getSearch())) {
				parameter.put("search", null);
		   } else {
				parameter.put("search", "%"+dto.getSearch()+"%");
		   }
		   parameter.put("searchDate",dto.getSearchDate());
		   if(StrUtil.isNotEmpty(dto.getGroupid())&&StrUtil.isNotEmpty(dto.getMarketplaceid())) {
			   AmazonAuthority auth = iAmazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceid());
			   if(auth!=null) {
				   parameter.put("amazonauthid", auth.getId());
			   }
		   }
		   iAmzStorageFeeService.getDownloadList(workbook, parameter);
		   	try {
		   		response.setContentType("application/force-download");// 设置强制下载不打开
		   		response.addHeader("Content-Disposition", "attachment;fileName=CommodityRevenueFinRate" + System.currentTimeMillis() + ".xlsx");// 设置文件名
		   		ServletOutputStream fOut = response.getOutputStream();
		   		workbook.write(fOut);
		   		workbook.close();
		   		fOut.flush();
		   		fOut.close();
		   	} catch (Exception e) {
		   		e.printStackTrace();
		   	}
	   }
	   
	   @PostMapping("/downloadLongList")
	   public void downloadLongListAction(@RequestBody AmzStorageFeeListDTO dto, HttpServletResponse response)  {
	   	// 创建新的Excel工作薄
	   	SXSSFWorkbook workbook = new SXSSFWorkbook();
	   	// 将数据写入Excel
	    UserInfo user = UserInfoContext.get();
		   Map<String, Object> parameter=new HashedMap<String, Object>();
		   parameter.put("shopid", user.getCompanyid());
		   if (StrUtil.isEmpty(dto.getGroupid())) {
				parameter.put("groupid", null);
		   } else {
				parameter.put("groupid", dto.getGroupid());
		   }
		   if (StrUtil.isEmpty(dto.getMarketplaceid())) {
				parameter.put("marketplaceid", null);
		   } else {
				parameter.put("marketplaceid", dto.getMarketplaceid());
		   }
		   if (StrUtil.isEmpty(dto.getSearch())) {
				parameter.put("search", null);
		   } else {
				parameter.put("search", "%"+dto.getSearch()+"%");
		   }
		   parameter.put("fromDate", dto.getFromDate());
		   parameter.put("toDate", dto.getToDate());
		   if(StrUtil.isNotEmpty(dto.getGroupid())&&StrUtil.isNotEmpty(dto.getMarketplaceid())) {
			   AmazonAuthority auth = iAmazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceid());
			   if(auth!=null) {
				   parameter.put("amazonauthid", auth.getId());
			   }
		   }
		   iAmzStorageFeeService.getDownloadLongList(workbook, parameter);
		   	try {
		   		response.setContentType("application/force-download");// 设置强制下载不打开
		   		response.addHeader("Content-Disposition", "attachment;fileName=CommodityRevenueFinRate" + System.currentTimeMillis() + ".xlsx");// 设置文件名
		   		ServletOutputStream fOut = response.getOutputStream();
		   		workbook.write(fOut);
		   		workbook.close();
		   		fOut.flush();
		   		fOut.close();
		   	} catch (Exception e) {
		   		e.printStackTrace();
		   	}
	   }
	   
	   
}
