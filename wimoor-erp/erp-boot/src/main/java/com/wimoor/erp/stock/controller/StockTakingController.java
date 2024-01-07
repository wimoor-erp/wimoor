package com.wimoor.erp.stock.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.stock.pojo.dto.StockTakingDTO;
import com.wimoor.erp.stock.pojo.dto.StockTakingFormDTO;
import com.wimoor.erp.stock.pojo.entity.StockTaking;
import com.wimoor.erp.stock.pojo.entity.StockTakingItem;
import com.wimoor.erp.stock.service.IStockTakingItemService;
import com.wimoor.erp.stock.service.IStockTakingService;
import com.wimoor.erp.stock.service.IStocktakingShelfService;
import com.wimoor.erp.stock.service.IStocktakingWarehouseService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "盘点单接口")
@RestController
@RequestMapping("/api/v1/inventory/stockTaking")
@RequiredArgsConstructor
public class StockTakingController {
	@Resource
	IStockTakingService stockTakingService;
	@Resource
	IStockTakingItemService stockTakingItemService;
	@Resource
	ISerialNumService serialNumService;
	@Resource
	IWarehouseService warehouseService;
	@Resource
	IInventoryService inventoryService;
	
	final FileUpload fileUpload;
    final IStocktakingShelfService iStocktakingShelfService;
    final IStocktakingWarehouseService iStocktakingWarehouseService;

	@GetMapping("/view")
	public Result<Map<String,Object>> viewAction(String id){
		Map<String,Object> maps=new HashMap<String, Object>();
		boolean isNew = true;
		if (id == null || id == "") {
			throw new BizException("没有对应的盘点单");
		}
		StockTaking data = stockTakingService.getById(id);
		if(data==null) {
			throw new BizException("没有对应的盘点单");
		}
		maps.put("overprice", data.getOverprice() == null ? 0 : data.getOverprice().floatValue());
		maps.put("lossprice", data.getLossprice() == null ? 0 : data.getLossprice().floatValue());
		maps.put("number", data.getNumber());
		maps.put("remark", data.getRemark());
		maps.put("ftype", data.getFtype());
		maps.put("isworking", data.isIsworking());
		maps.put("isNew", isNew);
		maps.put("id", id);
		if(data.getFtype()==1) {
			maps.put("warehouselist", this.iStocktakingWarehouseService.listData(id));
		}else {
			maps.put("shelflist", this.iStocktakingShelfService.listData(id));
		}
		return Result.success(maps);
	}
	
	@GetMapping("/edit")
	public Result<StockTaking> editAction(String id){
		UserInfo user = UserInfoContext.get();
		StockTaking data = null;
		if (id != null && id != "") {
			  data = stockTakingService.getById(id);
		}
		if (data != null  ) {
			data.setNew(false);
			data.setWarehouselist(this.iStocktakingWarehouseService.listData(id));
			data.setShelflist(this.iStocktakingShelfService.listData(id));
			return Result.success(data);
		} else {
			id = warehouseService.getUUID();
			data=new StockTaking();
			data.setId(id);
			String number="";
			try {
				number = (serialNumService.findSerialNumber(user.getCompanyid(), "K"));
			} catch (Exception e) {
				e.printStackTrace();
				throw new BizException("编码获取失败,请联系管理员");
			}
			data.setFtype(1);
			data.setNumber(number);
			data.setNew(true);
			return Result.success(data);
		}
		 
		
	}

	@ApiOperation(value = "变更仓库")
	@GetMapping("/changeWarehouse")
	public Result<Map<String, Object>> changeWarehouse(String warehouseid) throws BizException {
		Map<String, Object> map = new HashMap<String, Object>();
		Warehouse warehouse = warehouseService.getById(warehouseid);
		if (warehouse.getIsstocktaking()!=null&&warehouse.getIsstocktaking()==true) {
			map.put("isok", false);
			throw new BizException("该仓库正在盘点，请重新选择！");
		} else {
			map.put("isok", true);
			List<Warehouse> warehouseList = warehouseService.getSubWarehouse(warehouseid);// 获取仓库下面的仓位
			for (int i = 0; i < warehouseList.size(); i++) {
				map.put("warehouse_" + warehouseList.get(i).getFtype(), warehouseList.get(i).getId());
			}
			QueryWrapper<Warehouse> queryWrapper=new QueryWrapper<Warehouse>();
			queryWrapper.eq("parentid", warehouseid);
			queryWrapper.eq("disabled", false);
			queryWrapper.orderByDesc("isdefault");
			queryWrapper.orderByDesc("ftype");
			queryWrapper.orderByAsc("findex");
			List<Warehouse> list = warehouseService.list(queryWrapper);
			map.put("list", list);
		}
		return Result.success(map);
	}

	@PostMapping("/searchCondition")
	public Result<?> searchConditionAction(@RequestBody StockTakingDTO dto) {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String warehouseid =dto.getWarehouseid();
		String hasInv = dto.getHasInv();
		String id=dto.getId();
		String search=dto.getSearch();
		Map<String,Object> param=new HashMap<String,Object>();
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		}
		if (StrUtil.isEmpty(dto.getSearch())) {
			search = null;
		}else{
			search="%"+search+"%";
		}
		if (StrUtil.isNotEmpty(dto.getStocktakingid())) {
			param.put("stocktakingid", dto.getStocktakingid());
			id=dto.getStocktakingid();
		}
		if (user.isLimit(UserLimitDataType.owner)) {
			param.put("myself", user.getId());
		}
		if(StrUtil.isNotBlank(dto.getMaterialid())) {
			param.put("skuid", dto.getMaterialid());
		}
		if(StrUtil.isNotBlank(dto.getAddressid())) {
			param.put("addressid", dto.getAddressid());
		}
		param.put("warehouseid", warehouseid);
		param.put("shopid", shopid);
		param.put("hasInv",hasInv);
		param.put("search", search);
		if(StrUtil.isNotBlank(dto.getSelected())) {
			param.put("selected","true");
				List<Map<String, Object>> inventoryList = stockTakingService.getItemList(id, warehouseid,search);
				if(inventoryList!=null && inventoryList.size()>0) {
					for (int j = 0; j < inventoryList.size(); j++) {// 对price进行格式化
						double price = 0.0;
						if(inventoryList.get(j).get("price")!=null) {
							price=Double.parseDouble(inventoryList.get(j).get("price").toString());
						}
						inventoryList.get(j).put("price", price);
					}
			   }
			    Map<String, Object> sum_map = stockTakingService.getSumQuantity(id,warehouseid);
			    if(inventoryList!=null&&inventoryList.size()>0) {
			    	 Map<String,Object> topone=inventoryList.get(0);
			    	 topone.put("sum_fulfillable",sum_map.get("fulfillable"));
			    	 topone.put("sum_outbound",sum_map.get("outbound"));
			    	 topone.put("sum_amount",sum_map.get("amount"));
			    	 topone.put("sum_overamount",sum_map.get("overamount"));
			    	 topone.put("sum_lossamount",sum_map.get("lossamount"));
			    }
			return Result.success(inventoryList);
		}else {
			IPage<Map<String, Object>> inventoryList = stockTakingItemService.findLocalInventory(dto.getPage(),param);
			if(dto.getPage().getCurrent()==1) {
				List<Map<String, Object>> list = stockTakingService.getItemList(id, warehouseid,search);
				if(list!=null && list.size()>0) {
					for (int j = 0; j < list.size(); j++) {// 对price进行格式化
						double price = 0.0;
						if(list.get(j).get("price")!=null) {
							price=Double.parseDouble(list.get(j).get("price").toString());
						}
						list.get(j).put("price", price);
					}
			    if(list!=null&&list.size()>0) {
			    	if(inventoryList!=null&&inventoryList.getRecords().size()>0) {
			    		inventoryList.getRecords().addAll(0, list);
			    	}else if(inventoryList!=null){
			    		inventoryList=dto.getListPage(list);
			    	}
			    }
			}
			    Map<String, Object> sum_map = stockTakingService.getSumQuantity(id,warehouseid);
			    if(inventoryList!=null&&inventoryList.getRecords().size()>0) {
			    	 Map<String,Object> topone=inventoryList.getRecords().get(0);
			    	 topone.put("sum_fulfillable",sum_map.get("fulfillable"));
			    	 topone.put("sum_outbound",sum_map.get("outbound"));
			    	 topone.put("sum_amount",sum_map.get("amount"));
			    	 topone.put("sum_overamount",sum_map.get("overamount"));
			    	 topone.put("sum_lossamount",sum_map.get("lossamount"));
			    }
			}
			return Result.success(inventoryList);
		}
		
	}

	@ApiOperation(value = "保存")
	@GetMapping("/saveItem")
	public Result<Map<String, Object>> saveItemAction(String id,String warehouseid,String materialid,String amount,String overamount,
			String itemid)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo user = UserInfoContext.get();
		boolean isnew = true;
		StockTakingItem item = new StockTakingItem();
		if (StrUtil.isNotEmpty(itemid)&&!"undefined".equals(itemid)) {
			item.setId(itemid);
			isnew=false;
		} else {
			itemid = item.getId();
		}
		item.setStocktakingid(id);
		item.setMaterialid(materialid);
		item.setWarehouseid(warehouseid);
		item.setAmount(Integer.parseInt(amount));
		if (Integer.parseInt(overamount) > 0) {
			item.setOveramount(Integer.parseInt(overamount));
			item.setLossamount(0);
		} else {
			int lossamount = Integer.parseInt(overamount) * -1;
			item.setLossamount(lossamount);
			item.setOveramount(0);
		}
		
		StockTaking stockTaking = stockTakingService.getById(id);
		if(stockTaking==null) {
			throw new BizException("找不到对应的盘点单！");
		}
		stockTaking.setOperator(user.getId());
		stockTaking.setOpttime(new Date());
		stockTaking = stockTakingService.saveStockTakingAndItem(stockTaking, item,isnew);
		if (stockTaking.getOverprice() != null || stockTaking.getLossprice() != null) {
			map.put("isok", true);
			map.put("itemid", itemid);
			map.put("item", item);
			map.put("overprice", stockTaking.getOverprice());
			map.put("lossprice", stockTaking.getLossprice());
		} else {
			map.put("isok", false);
		}
		return Result.success(map);
	}

	@ApiOperation(value = "删除")
	@GetMapping("/removeItem")
	public Result<Map<String, Object>> removeItemAction(String id,String itemid) throws BizException {
		Map<String, Object> map = new HashMap<String, Object>();
		StockTaking stockTaking = stockTakingService.updateStockTakingAndItem(id, itemid);
		if (stockTaking.getOverprice() != null || stockTaking.getLossprice() != null) {
			map.put("msg", "删除成功！");
			map.put("isok", true);
			map.put("overprice", stockTaking.getOverprice());
			map.put("lossprice", stockTaking.getLossprice());
		} else {
			map.put("msg", "删除失败！");
			map.put("isok", false);
		}
		return Result.success(map);
	}

	@ApiOperation(value = "开始盘点")
	@PostMapping("/startAction")
	public Result<Map<String, Object>> startAction(@RequestBody StockTaking stocktaking)throws BizException {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo user = UserInfoContext.get();
		String number = null;
		try {
			number = (serialNumService.readSerialNumber(user.getCompanyid(), "K"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException("编码获取失败,请联系管理员");
		}
		// 获取仓库总库存和库存总值
		stocktaking.setId(warehouseService.getUUID());
		stocktaking.setNumber(number);
		stocktaking.setIsworking(true);// 正在盘点
		stocktaking.setShopid(user.getCompanyid());
		stocktaking.setCreator(user.getId());
		stocktaking.setCreatedate(new Date());
		stocktaking.setOperator(user.getId());
		boolean result = stockTakingService.save(stocktaking);
		if (result==true) {
			map.put("isok", true);
			map.put("id", stocktaking.getId());
		}
		return Result.success(map);
	}


	@ApiOperation(value = "盘点完成")
	@GetMapping("/endAction")
	public Result<Map<String, Object>> endAction(String id,String remark)
			throws BizException {
		UserInfo user = UserInfoContext.get();
		if(user==null){
			throw new BizException("请重新登录账号！");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		StockTaking stocktaking = stockTakingService.getById(id);
		stocktaking.setIsworking(false);// 盘点结束
		stocktaking.setRemark(remark);

		boolean result = stockTakingService.endAction(stocktaking, user);
		if (result==true) {
			map.put("isok", true);
			map.put("msg", "盘点完成！");
		} else {
			map.put("msg", "抱歉，盘点出错，请联系管理员！");
		}
		return Result.success(map);
	}

	@ApiOperation(value = "取消盘点")
	@GetMapping("/cancelAction")
	public Result<Map<String, Object>> cancelAction(String id)
			throws BizException {
		Map<String, Object> map = new HashMap<String, Object>();
		StockTaking stocktaking = stockTakingService.getById(id);
        if(stocktaking.isIsworking()==false) {
        	throw new BizException("当前盘点已经完成无法取消");
        }
		boolean result = stockTakingService.cancelAction(stocktaking);
		if (result==true) {
			map.put("isok", true);
			map.put("msg", "操作成功！");
		} else {
			map.put("msg", "操作失败！");
		}
		return Result.success(map);
	}

	@PostMapping("/list")
	public Result<IPage<Map<String, Object>>> getListData(@RequestBody StockTakingFormDTO dto){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo user = UserInfoContext.get();
		String search = dto.getSearch();
		String warehouseid = dto.getWarehouseid();
		String isworking = dto.getIsworking();
		String fromDate = dto.getFromDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String toDate = dto.getToDate();
		if (StrUtil.isEmpty(search)) {
			search = null;
		} else {
			search = search.trim() + "%";
		}
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		}else {
			warehouseid="%"+warehouseid+"%";
		}
		if (StrUtil.isEmpty(isworking)) {
			isworking = null;
		}
		if (StrUtil.isNotEmpty(fromDate)) {
			map.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -30);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			map.put("fromDate", fromDate);
		}
		if (StrUtil.isNotEmpty(toDate)) {
			map.put("endDate", toDate.trim());
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			map.put("endDate", toDate);
		}
		map.put("search", search);
		map.put("warehouseid", warehouseid);
		map.put("isworking", isworking);
		map.put("shopid", user.getCompanyid());
		IPage<Map<String, Object>> list = stockTakingService.findByCondition(dto.getPage(),map);
		return Result.success(list);
	}

	  @PostMapping(value = "/uploadBaseInfoFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	  public Result<String> uploadExcelAction(@RequestParam("file")MultipartFile file,@RequestParam("stockid")String stockid)  {
			       UserInfo user=UserInfoContext.get();
					if (file != null) {
						try {
							InputStream inputStream = file.getInputStream();
							Workbook workbook = WorkbookFactory.create(inputStream);
							stockTakingService.uploadStockTakingFile(user, workbook, stockid);
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
		    
	
	@GetMapping(value = "/downloadStockingList")
	public void downloadPurchaseInfoAction(String stockid, HttpServletResponse response) {
		Map<String, Object> maps = stockTakingService.selectAllStocktakingBySKU(stockid);
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=" + "AllStockTaking" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			stockTakingService.getExcelStockAllInfoReport(workbook, maps);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
