package com.wimoor.erp.inventory.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;
import com.wimoor.erp.assembly.mapper.AssemblyMapper;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.inventory.pojo.dto.InventoryQueryDTO;
import com.wimoor.erp.inventory.pojo.vo.MaterialInventoryVo;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.mapper.MaterialConsumableMapper;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
	final IAssemblyService assemblyService;
	
	@GetMapping("/list")
	public Result<List<Map<String, Object>>> getListData(String id,String ftype)  {
		UserInfo userinfo = UserInfoContext.get();
		List<Map<String, Object>> inventoryList = inventoryService.findByTypeWithStockCycle(id, ftype,userinfo.getCompanyid());
		return Result.success(inventoryList);
	}

	@GetMapping("getInventoryField")
	public Result<Map<String,Object>> getInventoryFieldAction() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		List<Warehouse> warehouseList = wareService.findByType("self_usable", shopid);
		Map<String, Object> warehouseMap = new HashMap<String, Object>();
		warehouseMap.put("warehouseList", warehouseList);
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
		List<Warehouse> warehouseList = wareService.findByType("self_usable", shopid);
		if (warehouseList == null || warehouseList.size() == 0) {
			warehouseList = null;
		}
		Map<String, Object> warehouseMap = new HashMap<String, Object>();
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
		List<Warehouse> warehouseList = wareService.findByType("self_usable", shopid);
		if (warehouseList == null || warehouseList.size() == 0) {
			warehouseList = null;
		}
		Map<String, Object> warehouseMap = new HashMap<String, Object>();
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
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			for(Warehouse item:warehouseList) {
						list.add(BeanUtil.beanToMap(item));
			}
			inventoryService.setExcelBookInventoryReport(workbook,  list, warehouseDetailList);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	 
	@PostMapping(value = "getWarehouse")
	public Result<IPage<Map<String, Object>>> getWareHouseLsit(@ApiParam("查询DTO")@RequestBody  InventoryQueryDTO dto)  {
		UserInfo user = UserInfoContext.get();
		String fType = dto.getFType();
		String groupid = dto.getGroupid();
		String warehouseid =dto.getWarehouseid();
		String sku =dto.getSku();
		String conssku = dto.getConssku();
		String itemsku = dto.getItemsku();
		String category = dto.getCategory();
		String ftypes = dto.getFtypes();
		String search=dto.getSearch();
		if (StrUtil.isEmpty(category) || category.equals("all")) {
			category = null;
		}
		if (StrUtil.isBlank(ftypes) || ftypes.equals("all")) {
			ftypes = null;
		}
		if (StrUtil.isBlank(itemsku) || itemsku.equals("all")) {
			itemsku = null;
		}
		if (StrUtil.isBlank(conssku) || conssku.equals("all")) {
			conssku = null;
		}
		if (StrUtil.isBlank(sku) || sku.equals("all")) {
			sku = null;
		}
		if (StrUtil.isBlank(warehouseid)) {
			warehouseid = null;
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
		if(dto.getHasinv()!=null&&dto.getHasinv()==true) {
			param.put("hasinv", true);
		}else {
			param.put("hasinv", null);
		}
		if(StrUtil.isNotBlank(warehouseid)) {
			param.put("warehouseid", warehouseid);
		}else {
			param.put("warehouseid", null);
		}
		param.put("skuid", sku);
		param.put("sku", sku);
		param.put("shopid", user.getCompanyid());
		param.put("groupid", groupid);
		param.put("itemsku", itemsku);
		param.put("conssku", conssku);
		param.put("category", category);
		param.put("ftypes", ftypes);
		param.put("search", search);
		param.put("ftype", fType);
		IPage<Map<String, Object>> result = inventoryService.findLocalInventory(dto.getPage(),param);
		if (result != null && result.getRecords().size() > 0) {
			List<Map<String, Object>> inventoryList=result.getRecords();
			if(dto.getCurrentpage()==1) {
				Map<String, Object> summap = inventoryService.findSum(param);
				inventoryList.get(0).put("allinbound", summap.get("inbound"));
				inventoryList.get(0).put("allfulfillable", summap.get("fulfillable"));
				inventoryList.get(0).put("alloutbound", summap.get("outbound"));
			}
		}
		return Result.success(result);
	}
	
	@PostMapping("getWarehouseExport")
	public void getWareHouseExport(@ApiParam("查询DTO")@RequestBody InventoryQueryDTO dto, HttpServletResponse response)  {
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
		if(dto.getHasinv()!=null&&dto.getHasinv()==true) {
			param.put("hasinv", true);
		}else {
			param.put("hasinv", null);
		}
		List<Map<String, Object>> inventoryList = inventoryService.findLocalInventory(param);
		Map<String, Object> summap = inventoryService.findSum(param);
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
	        inventoryService.getExcelBookInventoryReport(workbook, inventoryList);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/findInboundDetail")
	public Result<List<Map<String, Object>>> findInboundDetail(String mid,String warehouseid){
		List<Map<String, Object>> result = null;
		UserInfo user = UserInfoContext.get();
		if (StrUtil.isEmpty(mid)) {
			mid = null;
		}
		result = inventoryService.findInboundDetail(mid, warehouseid, user.getCompanyid());
		return Result.success(result);
	}
	
	@GetMapping("/findOutboundDetail")
	public Result<List<Map<String, Object>>> findOutboundDetail(String mid,String warehouseid) {
		List<Map<String, Object>> result = null;
		UserInfo user = UserInfoContext.get();
		if (StrUtil.isEmpty(mid)) {
			mid = null;
		}
		result = inventoryService.findOutboundDetail(mid, warehouseid, user.getCompanyid());
		return Result.success(result);
	}

	/*
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


	



*/	
	
	@GetMapping(value = "/getInventoryByMaterial")
	public Result<List<Map<String, Object>>> getInventoryByMaterialByIdAction(String mid){
		return Result.success(inventoryService.findFulByMaterial(mid));
	}
	 
	@GetMapping(value = "/getInventoryByMaterialSKU")
	public Result<List<MaterialInventoryVo>> getInventoryByMaterialAction(String sku)  {
		UserInfo userinfo = UserInfoContext.get();
		Material material = materialService.findBySKU(sku, userinfo.getCompanyid());
		if(material==null) {
			throw new BizException("未找到本地SKU"+sku);
		}
		return Result.success(inventoryService.findLocalWarehouseInventory(userinfo.getCompanyid(),material.getId()));
	}
	
	
    @ApiOperation("根据本地产品ID查询产品详情")
    @GetMapping("/getInventory")
	public Result<MaterialInventoryVo> getInventoryAction(@ApiParam("本地SKU id")@RequestParam String materialid,@ApiParam("仓库ID")@RequestParam String warehouseid) {
    	MaterialInventoryVo vo=new MaterialInventoryVo();
    	UserInfo userinfo = UserInfoContext.get();
    	  String shopid=userinfo.getCompanyid();
    		vo.setFulfillable(0);
    		vo.setOutbound(0);
    		vo.setInbound(0);
    		vo.setCanassembly(0);
    		vo.setCanconsumable(null);
		if(StrUtil.isNotBlank(warehouseid)) {
			Integer canconsumable = materialConsumableMapper.findCanConsumableByInventory(materialid, warehouseid, shopid);
			if(canconsumable!=null) {
				vo.setCanconsumable(canconsumable);
			}
		}
		return Result.success(vo);
	} 
    
    @ApiOperation("根据本地产品ID查询产品详情")
    @GetMapping("/findInventoryNowCostByShopId")
	public Result<List<Map<String, Object>>> findInventoryNowCostByShopIdAction(@RequestParam String shopid) {
			List<Map<String, Object>> result = inventoryService.findInventoryNowCostByShopId(shopid);
		    return Result.success(result);
	} 
    
}
