package com.wimoor.amazon.inbound.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundItemQueryDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inbound.service.IShipInboundItemService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "发货单item")
@RestController
@RequestMapping("/api/v1/shipInboundItem")
@SystemControllerLog("发货单item")
@RequiredArgsConstructor
public class ShipInboundItemController {
	
	final IShipInboundItemService iShipInboundItemService;
	
	
	@PostMapping(value = "getInboundItemBatchList")
	public Result<IPage<Map<String, Object>>> getShipRecordByMarket(@RequestBody ShipInboundItemQueryDTO dto)   {
		UserInfo user=UserInfoContext.get();
		Map<String,Object> param=new HashMap<String, Object>(); 
		String fromDate=dto.getFromDate();
		String toDate=dto.getToDate();
		String search=dto.getSearch();
		String searchtype=dto.getSearchType();
		String status=dto.getStatus();
		String groupid=dto.getGroupid();
		String marketplaceid=dto.getMarketplaceid();
		if(StrUtil.isNotEmpty(groupid)) {
			param.put("groupid", groupid);
		}else {
			param.put("groupid", null);
		}
		if(StrUtil.isNotEmpty(marketplaceid)) {
			param.put("marketplaceid", marketplaceid);
		}else {
			param.put("marketplaceid", null);
		}
		if(StrUtil.isNotEmpty(search)) {
			param.put("search", "%"+search+"%");
		}else {
			param.put("search", null);
		}
		if(StrUtil.isNotEmpty(searchtype)) {
			param.put("searchtype", searchtype);
		}else {
			param.put("searchtype", null);
		}
		if(StrUtil.isNotEmpty(status)) {
			param.put("status", status);
		}else {
			param.put("status", null);
		}
		if(StrUtil.isNotEmpty(dto.getOwner())) {
			param.put("owner", dto.getOwner());
		}else {
			param.put("owner", null);
		}
		param.put("fromDate", fromDate);
		param.put("toDate", toDate);
		param.put("shopid", user.getCompanyid());
		IPage<Map<String, Object>> list=iShipInboundItemService.getShipinboundItemBatchCondition(dto.getPage(),param);
		return Result.success(list);
	}
	
	@PostMapping(value = "downloadList")
	public void downloadListAction(@RequestBody ShipInboundItemQueryDTO dto,HttpServletResponse response)   {
		UserInfo user=UserInfoContext.get();
		Map<String,Object> param=new HashMap<String, Object>(); 
		String fromDate=dto.getFromDate();
		String toDate=dto.getToDate();
		String search=dto.getSearch();
		String searchtype=dto.getSearchType();
		String status=dto.getStatus();
		String groupid=dto.getGroupid();
		String marketplaceid=dto.getMarketplaceid();
		if(StrUtil.isNotEmpty(groupid)) {
			param.put("groupid", groupid);
		}else {
			param.put("groupid", null);
		}
		if(StrUtil.isNotEmpty(marketplaceid)) {
			param.put("marketplaceid", marketplaceid);
		}else {
			param.put("marketplaceid", null);
		}
		if(StrUtil.isNotEmpty(search)) {
			param.put("search", "%"+search+"%");
		}else {
			param.put("search", null);
		}
		if(StrUtil.isNotEmpty(searchtype)) {
			param.put("searchtype", searchtype);
		}else {
			param.put("searchtype", null);
		}
		if(StrUtil.isNotEmpty(status)) {
			param.put("status", status);
		}else {
			param.put("status", null);
		}
		if(StrUtil.isNotEmpty(dto.getOwner())) {
			param.put("owner", dto.getOwner());
		}else {
			param.put("owner", null);
		}
		param.put("fromDate", fromDate);
		param.put("toDate", toDate);
		param.put("shopid", user.getCompanyid());
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=FBAInvDayDetail"+System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			iShipInboundItemService.downloadBatchListList(workbook, param);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping(value = "findDetailByShipment")
	public Result<List<Map<String, Object>>> findDetailByShipment(String shipmentid,String sku)   {
		List<Map<String, Object>> list=iShipInboundItemService.findDetailByShipment(shipmentid,sku);
		return Result.success(list);
	}
	
	@PostMapping(value = "updateFeeByShipment")
	@Transactional
	public Result<?> updateFeeByShipment(@RequestBody List<ShipInboundItem> list)   {
		UserInfo user=UserInfoContext.get();
		for(ShipInboundItem item:list) {
			item.setOperator(user.getId());
			item.setOpttime(new Date());
		}
		iShipInboundItemService.updateFeeByShipment(list);
		return Result.success();
	}
	
	
	
}
