package com.wimoor.erp.inventory.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;
import com.wimoor.erp.assembly.mapper.AssemblyMapper;
import com.wimoor.erp.inventory.pojo.dto.InvDayDetailDTO;
import com.wimoor.erp.inventory.pojo.dto.InventoryQueryDTO;
import com.wimoor.erp.inventory.pojo.dto.WarehouseExportDTO;
import com.wimoor.erp.inventory.pojo.vo.MaterialInventoryVo;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.mapper.MaterialConsumableMapper;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
 
@Api(tags = "仓存接口")
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController   {
	@Resource
	IInventoryService inventoryService;
	@Resource
	IMaterialService materialService;
	@Resource
	IWarehouseService wareService;
    @Resource
    AssemblyMapper assemblyMapper;
    @Resource
    MaterialConsumableMapper materialConsumableMapper;

	@GetMapping("/list")
	public Result<Map<String,Object>> getListData(String id)  {
		UserInfo userinfo = UserInfoContext.get();
		List<Map<String, Object>> fbainventoryList = null;
		List<Map<String, Object>> inventoryList = null;
		fbainventoryList = inventoryService.findByTypeWithStockCycle("FBA", id, userinfo.getCompanyid());
		inventoryList = inventoryService.findByTypeWithStockCycle("notFBA", id, userinfo.getCompanyid());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("inventoryList", inventoryList);
		map.put("fbainventoryList", fbainventoryList);
		return Result.success(map);
	}

	@GetMapping("getInventoryField")
	public Result<Map<String,Object>> getInventoryFieldAction() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		List<Map<String, Object>> warehouseList_FBA = wareService.findByType("FBA", shopid);
		List<Map<String, Object>> warehouseList = wareService.findByType("notFBA", shopid);
		if (warehouseList_FBA == null || warehouseList_FBA.size() == 0) {
			warehouseList_FBA = null;
		}
		if (warehouseList == null || warehouseList.size() == 0) {
			warehouseList = null;
		}
		if (warehouseList_FBA == null && warehouseList == null) {
			map.put("msg", "暂无库存详情，请先添加仓库！");
			return Result.success(map);
		}
		Map<String, Object> warehouseMap = new HashMap<String, Object>();
		warehouseMap.put("warehouseList_FBA", warehouseList_FBA);
		warehouseMap.put("warehouseList", warehouseList);
		map.put("warehouseList_FBA", warehouseList_FBA);
		map.put("warehouseList", warehouseList);
		return Result.success(map);
	}
	
	@PostMapping( "getInventory")
	public IPage<Map<String, Object>> getInventoryDetail(@ApiParam("查询DTO")@RequestBody InventoryQueryDTO query)  {
		String skuid = query.getMaterialid();
		if (StrUtil.isEmpty(skuid)) {
			skuid = null;
		}
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		List<Map<String, Object>> warehouseList_FBA = wareService.findByType("FBA", shopid);
		List<Map<String, Object>> warehouseList = wareService.findByType("notFBA", shopid);
		if (warehouseList_FBA == null || warehouseList_FBA.size() == 0) {
			warehouseList_FBA = null;
		}
		if (warehouseList == null || warehouseList.size() == 0) {
			warehouseList = null;
		}
		Map<String, Object> warehouseMap = new HashMap<String, Object>();
		warehouseMap.put("warehouseList_FBA", warehouseList_FBA);
		warehouseMap.put("warehouseList", warehouseList);
		warehouseMap.put("shopid", shopid);
		warehouseMap.put("skuid", skuid);
		if (userinfo.isLimit(UserLimitDataType.owner)) {
		   warehouseMap.put("myself", userinfo.getId());
		}else {
		   warehouseMap.put("myself", null);
		}
		IPage<Map<String, Object>> warehouseDetailList = inventoryService.findInventoryDetail(query.getPage(),warehouseMap);
		return warehouseDetailList;
	}

	@GetMapping( "getInventoryExport")
	public void getInventoryDetailForExport(String skuid, HttpServletResponse response)  {
		if (StrUtil.isEmpty(skuid)) {
			skuid = null;
		}
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		List<Map<String, Object>> warehouseList_FBA = wareService.findByType("FBA", shopid);
		List<Map<String, Object>> warehouseList = wareService.findByType("notFBA", shopid);
		if (warehouseList_FBA == null || warehouseList_FBA.size() == 0) {
			warehouseList_FBA = null;
		}
		if (warehouseList == null || warehouseList.size() == 0) {
			warehouseList = null;
		}
		if (warehouseList_FBA == null && warehouseList == null) {
			throw new BizException("暂无库存详情，请先添加仓库！");
		}
		Map<String, Object> warehouseMap = new HashMap<String, Object>();
		warehouseMap.put("warehouseList_FBA", warehouseList_FBA);
		warehouseMap.put("warehouseList", warehouseList);
		warehouseMap.put("shopid", shopid);
		warehouseMap.put("skuid", skuid);
		List<Map<String, Object>> warehouseDetailList = inventoryService.findInventoryDetailForExport(warehouseMap);
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=inventoryReport" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			inventoryService.setExcelBookInventoryReport(workbook, warehouseList_FBA, warehouseList, warehouseDetailList);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("getWareHouseExport")
	public void getWareHouseExport(@ApiParam("查询DTO")@RequestBody WarehouseExportDTO dto, HttpServletResponse response)  {
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		String fType = dto.getFType();
		String groupid = dto.getGroupid();
		String warehouseid = dto.getWarehouseid();
		String sku = dto.getSku();
		String conssku =dto.getConssku();
		String itemsku =dto.getItemsku();
		String category = dto.getCategory();
		String ftypes = dto.getFtypes();
		if (StrUtil.isEmpty(category) || category.equals("all")) {
			category = null;
		}
		if (StrUtil.isEmpty(ftypes) || ftypes.equals("all")) {
			ftypes = null;
		}
		if (StrUtil.isEmpty(itemsku) || itemsku.equals("all")) {
			itemsku = null;
		}
		if (StrUtil.isEmpty(conssku) || conssku.equals("all")) {
			conssku = null;
		}
		if (StrUtil.isEmpty(sku) || sku.equals("all")) {
			sku = null;
		}
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		}
		if (StrUtil.isEmpty(groupid)) {
			groupid = null;
		}
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("warehouseid", warehouseid);
		param.put("skuid", sku);
		param.put("sku", sku);
		param.put("shopid", shopid);
		param.put("groupid", groupid);
		param.put("itemsku", itemsku);
		param.put("conssku", conssku);
		param.put("category", category);
		param.put("ftypes", ftypes);
		param.put("ftype", fType);
		List<Map<String, Object>> inventoryList = inventoryService.findByType(param);
		Map<String, Object> summap = inventoryService.findSumByType(param);
		String fileName = "";
		if ("FBA".equals(fType)) {
			fileName = "FBAInventoryReport";
			summap.put("groupname", "合计");
		} else {
			fileName = "LocalInventoryReport";
			summap.put("warehouse", "合计");
		}
		inventoryList.add(summap);
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=" + fileName + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			if ("FBA".equals(fType)) {
				inventoryService.getExcelBookFBAInventoryReport(workbook, inventoryList);
			} else {
				inventoryService.getExcelBookNotFBAInventoryReport(workbook, inventoryList);
			}
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping( "getInvDayField")
	public Object getInvDayFieldAction(String fromdatestr,String enddatestr) {
		Map<String, Date> parameter = new HashMap<String, Date>();
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
		parameter.put("endDate", endDate);
		return inventoryService.getInvDayField(parameter);
	}

	@PostMapping("getInvDayDetail")
	public Object getInvDayDetailAction(@ApiParam("查询DTO")@RequestBody  InvDayDetailDTO query) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		parameter.put("shopid", shopid);
		String sku =query.getSku();
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
		if(userinfo.isLimit(UserLimitDataType.owner)) {
			parameter.put("myself", userinfo.getId());
		}
		parameter.put("beginDate", fm.format(beginDate));
		parameter.put("endDate", fm.format(endDate));
		IPage<Map<String, Object>> list = inventoryService.getInvDayDetail(query,parameter);
		return list;
	}

	
/*
	@ResponseBody
	@RequestMapping(value = "getWareHouse")
	public Object getWareHouseLsit(HttpServletRequest request, Model model) throws UserException {
		String shopid = super.userService.getCurrentUserShopID(request);
		User user=userService.getCurrentUser(request);
		String fType = request.getParameter("fType");
		String groupid = request.getParameter("groupid");
		String warehouseid = request.getParameter("warehouseid");
		String sku = request.getParameter("sku");
		String conssku = request.getParameter("conssku");
		String itemsku = request.getParameter("itemsku");
		String category = request.getParameter("category");
		String ftypes = request.getParameter("ftypes");
		if (StrUtil.isEmpty(category) || category.equals("all")) {
			category = null;
		}
		if (StrUtil.isEmpty(ftypes) || ftypes.equals("all")) {
			ftypes = null;
		}
		if (StrUtil.isEmpty(itemsku) || itemsku.equals("all")) {
			itemsku = null;
		}
		if (StrUtil.isEmpty(conssku) || conssku.equals("all")) {
			conssku = null;
		}
		if (StrUtil.isEmpty(sku) || sku.equals("all")) {
			sku = null;
		}
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		}
		if (StrUtil.isEmpty(groupid)) {
			groupid = null;
		}
		Map<String,Object> param=new HashMap<String,Object>();
		if (userService.isOwnerSku(user.getId())) {
			param.put("myself", user.getId());
		}
		param.put("warehouseid", warehouseid);
		param.put("skuid", sku);
		param.put("sku", sku);
		param.put("shopid", shopid);
		param.put("groupid", groupid);
		param.put("itemsku", itemsku);
		param.put("conssku", conssku);
		param.put("category", category);
		param.put("ftypes", ftypes);
		param.put("ftype", fType);
		PageList<Map<String, Object>> inventoryList = inventoryService.findByType(param, super.getPageBounds(request));
		if (inventoryList != null && inventoryList.size() > 0) {
			Map<String, Object> summap = inventoryService.findSumByType(param);
			inventoryList.get(0).put("allinbound", summap.get("inbound"));
			inventoryList.get(0).put("allfulfillable", summap.get("fulfillable"));
			inventoryList.get(0).put("alloutbound", summap.get("outbound"));
			inventoryList.get(0).put("allafnWarehouseQuantity", summap.get("afnWarehouseQuantity"));
			inventoryList.get(0).put("allafnFulfillableQuantity", summap.get("afnFulfillableQuantity"));
			inventoryList.get(0).put("allafnReservedQuantity", summap.get("afnReservedQuantity"));
			inventoryList.get(0).put("allafnInboundWorkingQuantity", summap.get("afnInboundWorkingQuantity"));
			inventoryList.get(0).put("allafnInboundShippedQuantity", summap.get("afnInboundShippedQuantity"));
			inventoryList.get(0).put("allafnInboundReceivingQuantity", summap.get("afnInboundReceivingQuantity"));
			inventoryList.get(0).put("allafnUnsellableQuantity", summap.get("afnUnsellableQuantity"));
			inventoryList.get(0).put("allafnResearchingQuantity", summap.get("afnResearchingQuantity"));
		}
		return inventoryList;
	}

	@ResponseBody
	@RequestMapping(value = "findInvDetailById")
	public Object findInvDetailById(HttpServletRequest request, Model model) throws UserException {
		Map<String, Object> map = null;
		String shopid = super.userService.getCurrentUserShopID(request);
		String warehouseid = request.getParameter("warehouseid");
		String materialid = request.getParameter("skuid");
		map = inventoryService.findInvDetailById(materialid, warehouseid, shopid);
		
		Material m = materialService.selectByKey(materialid);
		if(m!=null&&"1".equals(m.getIssfg())) {
		    Integer map2 = assemblyMapper.findCanAssemblyByInventory(materialid, warehouseid, shopid);
		    if(map2!=null) {
		    	if(map==null) {
		    		map=new HashMap<String,Object>();
		    	}
		    	map.put("canassembly", map2);
		    }
		} 
		List<Map<String, Object>> list = materialConsumableMapper.selectConsumableByMainMmid(materialid);
		if(list!=null&&list.size()>0) {
			Integer map3 = materialConsumableMapper.findCanConsumableByInventory(materialid, warehouseid, shopid);
			map.put("canconsumable", map3);
		}
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "findInvTUDetailByParentId")
	public Object findInvTUDetailByParentIdAction(HttpServletRequest request, Model model) throws UserException {
		Map<String, Object> map = null;
		String shopid = super.userService.getCurrentUserShopID(request);
		String warehouseid = request.getParameter("warehouseid");
		String materialid = request.getParameter("skuid");
		map = inventoryService.findInvTUDetailByParentId(materialid, warehouseid, shopid);
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "findFBAInvDetailById")
	public Object findFBAInvDetailById(HttpServletRequest request, Model model) throws UserException {
		Map<String, Object> map = null;
		String shopid = super.userService.getCurrentUserShopID(request);
		String warehouseid = request.getParameter("warehouseid");
		String sku = request.getParameter("sku").trim();
		String groupid = request.getParameter("groupid");
		if(StrUtils.isEmpty(groupid)) {
			groupid=null;
		}
		map = inventoryService.findFBAInvDetailById(sku, warehouseid, shopid,groupid);
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "findInboundDetail")
	public Object findInboundDetail(HttpServletRequest request, Model model) throws UserException {
		List<Map<String, Object>> result = null;
		String shopid = super.userService.getCurrentUserShopID(request);
		String warehouseid = request.getParameter("warehouseid");
		String materialid = request.getParameter("skuid");
		if (StrUtil.isEmpty(materialid)) {
			materialid = null;
		}
		result = inventoryService.findInboundDetail(materialid, warehouseid, shopid);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "findOutboundDetail")
	public Object findOutboundDetail(HttpServletRequest request, Model model) throws UserException {
		List<Map<String, Object>> result = null;
		String shopid = super.userService.getCurrentUserShopID(request);
		String warehouseid = request.getParameter("warehouseid");
		String materialid = request.getParameter("skuid");
		if (StrUtil.isEmpty(materialid)) {
			materialid = null;
		}
		result = inventoryService.findOutboundDetail(materialid, warehouseid, shopid);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "inventoryReport")
	public PageList<Map<String, Object>> selectInvReportAction(HttpServletRequest request) throws UserException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", userService.getCurrentUserShopID(request));
		String search = request.getParameter("search");
		if (StrUtil.isNotEmpty(search)) {
			param.put("search", search + "%");
		}
		String fromDate = request.getParameter("fromDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		String toDate = request.getParameter("toDate");
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("toDate", toDate.trim()+" 23:59:59");;
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("toDate", toDate+" 23:59:59");
		}
		String ftypeid = request.getParameter("ftypeid");
		if (StrUtil.isNotEmpty(ftypeid)) {
			param.put("ftypeid", ftypeid);
		}
		String wparentid = request.getParameter("wparentid");
		if (StrUtil.isNotEmpty(wparentid)) {
			param.put("wparentid", wparentid);
		}
		String skuid = request.getParameter("skuid");
		if (StrUtil.isNotEmpty(skuid)) {
			if (skuid.equals("all")) {
				param.put("skuid", null);
			} else {
				param.put("skuid", skuid);
			}
		}
		String formnum = request.getParameter("formnum");
		if (StrUtil.isNotEmpty(formnum)) {
			param.put("formnum", formnum);
		}
		PageList<Map<String, Object>> list = inventoryService.selectInventoryDetail(param, super.getPageBounds(request));
		return list;
	}

	@ResponseBody
	@RequestMapping(value = "inventoryBySku")
	public List<Map<String, Object>> getNotFBAAction(HttpServletRequest request) throws UserException {
		String warehouseid = request.getParameter("wareparentid");
		String shopid = userService.getCurrentUserShopID(request);
		String skuid = request.getParameter("skuid");
		List<Map<String, Object>> inventoryList = inventoryService.findNotFbaBySku(warehouseid, skuid, shopid);
		return inventoryList;
	}

	@ResponseBody
	@RequestMapping(value = "inventoryCost")
	public PageList<Map<String, Object>> getInventoryCostAction(HttpServletRequest request) throws UserException {
		String warehouseid = request.getParameter("warehouseid");
		String sku = request.getParameter("sku");
		String shopid = userService.getCurrentUserShopID(request);
		String byday = request.getParameter("byday");
		return inventoryService.selectInventoryCost(warehouseid, sku, shopid, byday, super.getPageBounds(request));
	}

	@RequestMapping(value = "downLoadInvCost")
	public void getInventoryCostReportAction(HttpServletRequest request, HttpServletResponse response, Model model) throws BaseException {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=inventrtyCostReport.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			String warehouseid = request.getParameter("warehouseid");
			String sku = request.getParameter("sku");
			String byday = request.getParameter("byday");
			String shopid = userService.getCurrentUserShopID(request);
			List<Map<String, Object>> list = inventoryService.selectInventoryCostAll(warehouseid, sku, shopid, byday);
			Sheet sheet = workbook.createSheet("sheet1");
			Map<String, Integer> titlemap = new HashMap<String, Integer>();
			Row row = sheet.createRow(0);
			Cell cell = null;
			titlemap.put("sku", 0);
			cell = row.createCell(0);
			cell.setCellValue("SKU");

			titlemap.put("fulfillable", 1);
			cell = row.createCell(1);
			cell.setCellValue("现有库存");

			titlemap.put("inbound", 2);
			cell = row.createCell(2);
			cell.setCellValue("待入库库存");

			titlemap.put("price", 3);
			cell = row.createCell(3);
			cell.setCellValue("采购单价");

			titlemap.put("otherFeer", 4);
			cell = row.createCell(4);
			cell.setCellValue("其他采购成本");
			
			titlemap.put("totalprice", 5);
			cell = row.createCell(5);
			cell.setCellValue("库存货值");

			titlemap.put("inpasscost", 6);
			cell = row.createCell(6);
			cell.setCellValue("在途物资货值");

			titlemap.put("inpasspay", 7);
			cell = row.createCell(7);
			cell.setCellValue("应付账款-采购货物");

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

	@ResponseBody
	@RequestMapping(value = "inventoryCostFBA")
	public PageList<Map<String, Object>> getInventoryCostFBAAction(HttpServletRequest request) throws UserException {
		String marketplaceid = request.getParameter("marketplaceid");
		String sku = request.getParameter("sku");
		String groupid = request.getParameter("groupid");
		String byday = request.getParameter("byday");
		String shopid = userService.getCurrentUserShopID(request);
		return inventoryService.selectInventoryCostFBA(groupid, marketplaceid, sku, shopid, byday, super.getPageBounds(request));
	}

	@RequestMapping(value = "downloadInvCostFBA")
	public void getInventoryCostFBAReportAction(HttpServletRequest request, HttpServletResponse response, Model model) throws BaseException {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=inventrtyFBACostReport.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			String marketplaceid = request.getParameter("marketplaceid");
			String sku = request.getParameter("sku");
			String groupid = request.getParameter("groupid");
			String byday = request.getParameter("byday");
			String shopid = userService.getCurrentUserShopID(request);
			List<Map<String, Object>> list = inventoryService.selectInventoryCostFBAall(groupid, marketplaceid, sku, shopid, byday);
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
	
	@ResponseBody
	@RequestMapping(value = "getInventoryByMaterial")
	public List<Map<String, Object>> getInventoryByMaterialAction(HttpServletRequest request) throws UserException {
		String materialid = request.getParameter("materialid");
		return inventoryService.findFulByMaterial(materialid);
	}

	@ResponseBody
	@RequestMapping(value = "getInventorydetail")
	public List<Map<String, Object>> getInventorydetailAction(HttpServletRequest request) throws UserException {
		String materialid = request.getParameter("materialid");
		String warehouseid=request.getParameter("warehouseid");
		return inventoryService.getInventorydetail(materialid,warehouseid);
	}
*/	
	
	 
	@GetMapping(value = "/getInventoryByMaterialSKU")
	public Result<List<MaterialInventoryVo>> getInventoryByMaterialAction(String sku)  {
		UserInfo userinfo = UserInfoContext.get();
		Material material = materialService.findBySKU(sku, userinfo.getCompanyid());
		return Result.success(inventoryService.findLocalWarehouseInventory(userinfo.getCompanyid(),material.getId()));
	}
	
	
}
