package com.wimoor.amazon.inventory.controller;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.inbound.service.IFBAShipCycleService;
import com.wimoor.amazon.inventory.pojo.dto.InventoryPlanningDTO;
import com.wimoor.amazon.inventory.pojo.vo.AmzInventoryPlanningVo;
import com.wimoor.amazon.inventory.service.IFBAInventoryService;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersReturnDTO;
import com.wimoor.amazon.report.pojo.dto.InvDayDetailDTO;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-12
 */
@Api(tags = "授权接口")
//@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/inventoryRpt")
public class InventoryReportController {
   final IFBAInventoryService iFBAInventoryService;
   final IFBAShipCycleService iFBAShipCycleService;
   
	@PostMapping(value = "getFBAInvDayDetail")
	public Object getFBAInvDayDetailAction(@ApiParam("查询DTO")@RequestBody InvDayDetailDTO query) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		parameter.put("shopid", shopid);
		String sku = query.getSku();
		if (StrUtil.isEmpty(sku)) {
			sku = null;
		} else {
			sku = "%" + sku.trim() + "%";
		}
		parameter.put("sku", sku);
		String warehouse = query.getWarehouse();
		if (StrUtil.isEmpty(warehouse)) {
			warehouse = null;
		}
		parameter.put("warehouse", warehouse);
		String groupid = query.getGroupid();
		if (StrUtil.isEmpty(groupid)) {
			groupid = null;
		}
		parameter.put("groupid", groupid);
		String fromdatestr = query.getFromdate();
		String enddatestr = query.getEnddate();
		Date beginDate = null;
		Date endDate = null;
		DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isEmpty(fromdatestr) || StrUtil.isEmpty(enddatestr)) {
			Calendar cTime = Calendar.getInstance();
			endDate = cTime.getTime();
			cTime.add(Calendar.DATE, -7);
			beginDate = cTime.getTime();
		} else {
			beginDate = GeneralUtil.getDatez(fromdatestr);
			endDate = GeneralUtil.getDatez(enddatestr);
		}
		parameter.put("beginDate", fm.format(beginDate));
		parameter.put("endDate", fm.format(endDate));
		IPage<Map<String, Object>> list = iFBAInventoryService.getFBAInvDayDetail(query.getPage(),parameter);
		return list;
	}
	
	@GetMapping("/list")
	public Result<List<Map<String, Object>>> getListData(String sku)  {
		UserInfo userinfo = UserInfoContext.get();
		List<Map<String, Object>> inventoryList = iFBAInventoryService.findByTypeWithStockCycle(sku, userinfo.getCompanyid());
		return Result.success(inventoryList);
	}
	
	@ApiOperation("滞销报表")
	@PostMapping("/planninglist")
	public Result<IPage<AmzInventoryPlanningVo>> getplanninglistAction(@RequestBody InventoryPlanningDTO dto)  {
		UserInfo userinfo = UserInfoContext.get();
		dto.setShopid(userinfo.getCompanyid());
		IPage<AmzInventoryPlanningVo> planningList = iFBAInventoryService.selectPlanPageList(dto);
		return Result.success(planningList);
	}
	
	@ApiOperation("滞销报表导出")
  	@GetMapping("/downloadPlanList")
    public  void downloadlistAction(
    		 InventoryPlanningDTO condition,
    		 HttpServletResponse response){
  		try {
  			UserInfo userinfo = UserInfoContext.get();
  			condition.setShopid(userinfo.getCompanyid());
	  		SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=planningReport.xlsx");// 设置文件名
			iFBAInventoryService.downloadPlanList(workbook,condition);
			ServletOutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@ApiOperation("滞销报表")
	@PostMapping("/summaryPlanning")
	public Result<AmzInventoryPlanningVo> summaryPlanningAction(@RequestBody InventoryPlanningDTO dto)  {
		UserInfo userinfo = UserInfoContext.get();
		dto.setShopid(userinfo.getCompanyid());
		AmzInventoryPlanningVo planningList = iFBAInventoryService.summaryPlanning(dto);
		return Result.success(planningList);
	}
	
	
	
}

