package com.wimoor.amazon.inventory.controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.inbound.service.IFBAShipCycleService;
import com.wimoor.amazon.inventory.pojo.dto.InventoryCostDTO;
import com.wimoor.amazon.inventory.pojo.dto.InventoryPlanningDTO;
import com.wimoor.amazon.inventory.pojo.vo.AmzInventoryPlanningVo;
import com.wimoor.amazon.inventory.pojo.vo.ProductInventoryVo;
import com.wimoor.amazon.inventory.service.IFBAInventoryService;
import com.wimoor.amazon.report.pojo.dto.InvDayDetailDTO;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;

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
   

	@PostMapping(value = "getFBAInvDayDetailField")
	public Result<List<Map<String, String>>> getFBAInvDayDetailFieldAction(@ApiParam("查询DTO")@RequestBody InvDayDetailDTO query) {
		Map<String, Date> parameter = new HashMap<String, Date>();
		String fromdatestr = query.getFromdate();
		String enddatestr = query.getEnddate();
		Date beginDate = null;
		Date endDate = null;
		if (StrUtil.isEmpty(fromdatestr) || StrUtil.isEmpty(enddatestr)) {
			Calendar cTime = Calendar.getInstance();
			endDate = cTime.getTime();
			cTime.add(Calendar.DATE, -7);
			beginDate = cTime.getTime();
		} else {
			beginDate = GeneralUtil.getDatez(fromdatestr);
			endDate = GeneralUtil.getDatez(enddatestr);
		}
		parameter.put("beginDate", beginDate);
		parameter.put("endDate",endDate);
		List<Map<String, String>> fieldlist = iFBAInventoryService.getInvDaySumField(parameter);
		return Result.success(fieldlist);
	}
	
	@PostMapping(value = "getFBAInvDayDetail")
	public Result<IPage<Map<String, Object>>> getFBAInvDayDetailAction(@ApiParam("查询DTO")@RequestBody InvDayDetailDTO query) {
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
		IPage<Map<String, Object>> list = iFBAInventoryService.getFBAInvDayDetail(query,parameter);
		 if(query.getCurrentpage()==1) {
			 Map<String, Object> summary = iFBAInventoryService.getFBAInvDayDetailTotal(parameter);
			 if(list!=null&&list.getRecords().size()>0&&summary!=null) {
				 list.getRecords().get(0).put("summary", summary);
			 }
		 }
		return Result.success(list);
	}
	
	@PostMapping("getFBAInvDayDetailExport")
	public void getFBAInvDayDetailExport(@ApiParam("查询DTO")@RequestBody InvDayDetailDTO query, HttpServletResponse response)  {
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
		
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=FBAInvDayDetail"+System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			iFBAInventoryService.downloadFBAInvDayDetail(workbook, parameter);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		if(userinfo.isLimit(UserLimitDataType.operations)) {
			dto.setOwner(userinfo.getId());
		} 
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
			if(userinfo.isLimit(UserLimitDataType.operations)) {
				condition.setOwner(userinfo.getId());
			} 
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
	
	@PostMapping(value = "getFBACountry")
	public Result<IPage<Map<String, Object>>> getFBACountry(@ApiParam("查询DTO")@RequestBody  com.wimoor.amazon.inventory.pojo.dto.InventoryQueryDTO dto)  {
		UserInfo user = UserInfoContext.get();
		String groupid = dto.getGroupid();
		String search=dto.getSearch();
		if (StrUtil.isEmpty(groupid)) {
			groupid = null;
		}		
		if (StrUtil.isNotBlank(search)) {
			search = "%"+search.trim()+"%";
		}else {
			search=null;
		}
		Map<String,Object> param=new HashMap<String,Object>();
		if (user.isLimit(UserLimitDataType.owner)) {
			param.put("myself", user.getId());
		}
		param.put("search", search);
		param.put("shopid", user.getCompanyid());
		param.put("groupid", groupid);
	    IPage<Map<String, Object>> inventoryList = iFBAInventoryService.findFbaCountry(dto.getPage(),param);
	 
		return Result.success(inventoryList);
	}
	
	@PostMapping(value = "getWarehouse")
	public Result<IPage<ProductInventoryVo>> getWareHouseLsit(@ApiParam("查询DTO")@RequestBody  com.wimoor.amazon.inventory.pojo.dto.InventoryQueryDTO dto)  {
		UserInfo user = UserInfoContext.get();
		String groupid = dto.getGroupid();
		String marketplaceid =dto.getMarketplaceid();
		String search=dto.getSearch();
		if (StrUtil.isEmpty(marketplaceid)) {
			marketplaceid = null;
		}
		if (StrUtil.isEmpty(groupid)) {
			groupid = null;
		}		
		if (StrUtil.isNotBlank(search)) {
			search = "%"+search.trim()+"%";
		}else {
			search=null;
		}
		Map<String,Object> param=new HashMap<String,Object>();
		if (user.isLimit(UserLimitDataType.owner)) {
			param.put("myself", user.getId());
		}
		if(StrUtil.isNotEmpty(marketplaceid) && "IEU".equals(marketplaceid)) {
			marketplaceid="EU";
		}
		param.put("marketplaceid", marketplaceid);
		param.put("search", search);
		param.put("shopid", user.getCompanyid());
		param.put("groupid", groupid);
		IPage<ProductInventoryVo> inventoryList = iFBAInventoryService.findFba(dto.getPage(),param);
		if (inventoryList != null && inventoryList.getRecords().size()> 0) {
			if(dto.getCurrentpage()==1) {
				Map<String, Object> summap = iFBAInventoryService.findSum(param);
				ProductInventoryVo summary=new ProductInventoryVo();
				summary.setAfnWarehouseQuantity(summap.get("afnWarehouseQuantity")!=null?summap.get("afnWarehouseQuantity").toString():null);
				summary.setAfnFulfillableQuantity(summap.get("allafnFulfillableQuantity")!=null?summap.get("allafnFulfillableQuantity").toString():null);
				summary.setAfnReservedQuantity(summap.get("afnReservedQuantity")!=null?summap.get("afnReservedQuantity").toString():null);
				summary.setAfnInboundWorkingQuantity(summap.get("afnInboundWorkingQuantity")!=null?summap.get("afnInboundWorkingQuantity").toString():null);
		        summary.setAfnInboundShippedQuantity(summap.get("afnInboundShippedQuantity")!=null?summap.get("afnInboundShippedQuantity").toString():null);
                summary.setAfnInboundReceivingQuantity(summap.get("afnInboundReceivingQuantity")!=null?summap.get("afnInboundReceivingQuantity").toString():null);
                summary.setAfnUnsellableQuantity(summap.get("afnUnsellableQuantity")!=null?summap.get("afnUnsellableQuantity").toString():null);
				summary.setAfnResearchingQuantity(summap.get("afnResearchingQuantity")!=null?summap.get("afnResearchingQuantity").toString():null);
				inventoryList.getRecords().get(0).setSummary(summary);	 
			}
		}
		return Result.success(inventoryList);
	}
	@PostMapping("getWarehouseExport")
	public void getWareHouseExport(@ApiParam("查询DTO")@RequestBody  com.wimoor.amazon.inventory.pojo.dto.InventoryQueryDTO dto, HttpServletResponse response)  {
		UserInfo user = UserInfoContext.get();
		String groupid = dto.getGroupid();
		String marketplaceid =dto.getMarketplaceid();
		String search=dto.getSearch();
		if (StrUtil.isEmpty(marketplaceid)) {
			marketplaceid = null;
		}
		if (StrUtil.isEmpty(groupid)) {
			groupid = null;
		}		
		if (StrUtil.isNotBlank(search)) {
			search = "%"+search.trim()+"%";
		}else {
			search=null;
		}
		Map<String,Object> param=new HashMap<String,Object>();
		if (user.isLimit(UserLimitDataType.owner)) {
			param.put("myself", user.getId());
		}
		param.put("marketplaceid", marketplaceid);
		param.put("search", search);
		param.put("shopid", user.getCompanyid());
		param.put("groupid", groupid);
		List<ProductInventoryVo> inventoryList = iFBAInventoryService.findFba(param);
		Map<String, Object> summap = iFBAInventoryService.findSum(param);
		String fileName = "";
		ProductInventoryVo summary=new ProductInventoryVo();
		fileName = "FBAInventoryReport";
		summap.put("groupname", "");
		summary.setGroupname("合计");
		summary.setAfnWarehouseQuantity(summap.get("afnWarehouseQuantity")!=null?summap.get("afnWarehouseQuantity").toString():null);
		summary.setAfnFulfillableQuantity(summap.get("allafnFulfillableQuantity")!=null?summap.get("allafnFulfillableQuantity").toString():null);
		summary.setAfnReservedQuantity(summap.get("afnReservedQuantity")!=null?summap.get("afnReservedQuantity").toString():null);
		summary.setAfnInboundWorkingQuantity(summap.get("afnInboundWorkingQuantity")!=null?summap.get("afnInboundWorkingQuantity").toString():null);
        summary.setAfnInboundShippedQuantity(summap.get("afnInboundShippedQuantity")!=null?summap.get("afnInboundShippedQuantity").toString():null);
        summary.setAfnInboundReceivingQuantity(summap.get("afnInboundReceivingQuantity")!=null?summap.get("afnInboundReceivingQuantity").toString():null);
        summary.setAfnUnsellableQuantity(summap.get("allafnUnsellableQuantity")!=null?summap.get("allafnUnsellableQuantity").toString():null);
		summary.setAfnResearchingQuantity(summap.get("allafnResearchingQuantity")!=null?summap.get("allafnResearchingQuantity").toString():null);
		inventoryList.add(summary);
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=" + fileName + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			iFBAInventoryService.getExcelBookBAInventoryReport(workbook, inventoryList);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping(value = "inventoryCostFBA")
	public Result<IPage<Map<String, Object>>> getInventoryCostFBAAction(@RequestBody InventoryCostDTO dto)  {
		String marketplaceid = dto.getMarketplaceid();
		String sku =dto.getSku();
		String groupid =dto.getGroupid();
		String byday = dto.getByday();
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		if(dto.getMarketplaceid()!=null&&dto.getMarketplaceid().equals("IEU")) {
			marketplaceid="EU";
		}
		return Result.success(iFBAInventoryService.selectInventoryCost(dto.getPage(),groupid, marketplaceid, sku, shopid, byday));
	}

	@PostMapping(value = "downloadInvCostFBA")
	public void getInventoryCostFBAReportAction(@RequestBody InventoryCostDTO dto, HttpServletResponse response)  {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=inventrtyFBACostReport.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			String marketplaceid = dto.getMarketplaceid();
			String sku =dto.getSku();
			String groupid =dto.getGroupid();
			String byday = dto.getByday();
			UserInfo user = UserInfoContext.get();
			String shopid = user.getCompanyid();
			List<Map<String, Object>> list = iFBAInventoryService.selectInventoryCostAll(groupid, marketplaceid, sku, shopid, byday);
			Sheet sheet = workbook.createSheet("sheet1");
			Map<String, Integer> titlemap = new HashMap<String, Integer>();
			Row row = sheet.createRow(0);
			Cell cell = null;
			titlemap.put("sku", 0);
			cell = row.createCell(0);
			cell.setCellValue("SKU");

			titlemap.put("fulfillable", 1);
			cell = row.createCell(1);
			cell.setCellValue("可用库存");

			titlemap.put("reserved", 2);
			cell = row.createCell(2);
			cell.setCellValue("预留库存");

			titlemap.put("inbound", 3);
			cell = row.createCell(3);
			cell.setCellValue("待入库库存");

			titlemap.put("price", 4);
			cell = row.createCell(4);
			cell.setCellValue("采购单价");
			
			titlemap.put("firstLegCharges", 5);
			cell = row.createCell(5);
			cell.setCellValue("头程运费");
			
			titlemap.put("otherFeer", 6);
			cell = row.createCell(6);
			cell.setCellValue("其他采购成本");

			titlemap.put("totalprice", 7);
			cell = row.createCell(7);
			cell.setCellValue("货值");

			titlemap.put("remark", 8);
			cell = row.createCell(8);
			cell.setCellValue("备注");
			// 在索引0的位置创建行（最顶端的行）
			for (int i = 0; i < list.size(); i++) {
				Row skurow = sheet.createRow(i + 1);
				Map<String, Object> skumap = list.get(i);
				for (String key : skumap.keySet()) {
					if (titlemap.get(key) == null) {
						continue;
					}
					cell = skurow.createCell(titlemap.get(key));
					cell.setCellValue(skumap.get(key).toString());
				}
			}
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

