package com.wimoor.erp.stock.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/view")
	public Result<Map<String,Object>> viewAction(String id){
		Map<String,Object> maps=new HashMap<String, Object>();
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String warehouseid = null;// 仓位id
		String warehouse = "";
		boolean isNew = true;
		if (id != null && id != "") {
			isNew = false;
			StockTaking data = stockTakingService.getById(id);
			warehouseid = data.getWarehouseid();
			warehouse = warehouseService.getById(warehouseid).getName();
			maps.put("overprice", data.getOverprice() == null ? 0 : data.getOverprice().floatValue());
			maps.put("lossprice", data.getLossprice() == null ? 0 : data.getLossprice().floatValue());
			maps.put("number", data.getNumber());
			maps.put("remark", data.getRemark());
			maps.put("isworking", data.isIsworking());
		} else {
			id = warehouseService.getUUID();
			String pwarehouseid = warehouseService.getSelfAllByShopIdOrderByDefault(shopid).get(0).getId();
			List<Warehouse> wlist = warehouseService.findWarehouselistByParent(pwarehouseid);
			if(wlist!=null && wlist.size()>0) {
				warehouseid=wlist.get(0).getId();
				warehouse=wlist.get(0).getName();
			}
		}
		Warehouse warehose =  warehouseService.getById(warehouseid);
		if (!isNew) {
			List<Map<String, Object>> list = stockTakingService.getItemList(id, warehose.getId(),null);
			for (int j = 0; j < list.size(); j++) {// 对price进行格式化
				double price = 0.0;
				if(list.get(j).get("price")!=null)
					price=Double.parseDouble(list.get(j).get("price").toString());
				list.get(j).put("price", price);
				list.get(j).put("image", fileUpload.getPictureImage(list.get(j).get("image")));
			}
			Map<String, Object> sum_map = stockTakingService.getSumQuantity(id, warehose.getId());
			maps.put("itemList", list);
			maps.put("sum_map", sum_map);
		}
		maps.put("isNew", isNew);
		maps.put("id", id);
		maps.put("warehouseid", warehouseid);
		maps.put("warehouse", warehouse);
		return Result.success(maps);
	}
	
	@GetMapping("/edit")
	public Result<Map<String,Object>> editAction(String id){
		Map<String,Object> maps=new HashMap<String, Object>();
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String warehouseid = null;// 自有仓
		boolean isNew = true;
		if (id != null && id != "") {
			isNew = false;
			StockTaking data = stockTakingService.getById(id);
			warehouseid = data.getWarehouseid();
			maps.put("overprice", data.getOverprice() == null ? 0 : data.getOverprice().floatValue());
			maps.put("lossprice", data.getLossprice() == null ? 0 : data.getLossprice().floatValue());
			maps.put("number", data.getNumber());
			maps.put("remark", data.getRemark());
			maps.put("isworking", data.isIsworking());
		} else {
			id = warehouseService.getUUID();
			List<Warehouse> warehouselist = warehouseService.getSelfAllByShopIdOrderByDefault(shopid);
			if (warehouselist != null && warehouselist.size() > 0) {
				String pwarehouseid = warehouselist.get(0).getId();
				List<Warehouse> wlist = warehouseService.findWarehouselistByParent(pwarehouseid);
				if(wlist!=null && wlist.size()>0) {
					warehouseid=wlist.get(0).getId();
				}
			}
		}
		if(warehouseid!=null){
			Warehouse warehose = warehouseService.getById(warehouseid);
			maps.put("warehouse", warehose);
			maps.put("warehouseid", warehouseid);
		}
		maps.put("isNew", isNew);
		maps.put("id", id);
		return Result.success(maps);
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
	public Result<IPage<Map<String, Object>>> searchConditionAction(@RequestBody StockTakingDTO dto) {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String warehouseid =dto.getWarehouseid();
		String hasInv = dto.getHasInv();
		String id=dto.getId();
		String search=dto.getSearch();
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		}
		if (StrUtil.isEmpty(dto.getSearch())) {
			search = null;
		}else{
			search="%"+search+"%";
		}
		Map<String,Object> param=new HashMap<String,Object>();
		if (user.isLimit(UserLimitDataType.owner)) {
			param.put("myself", user.getId());
		}
		param.put("warehouseid", warehouseid);
		param.put("shopid", shopid);
		param.put("hasInv",hasInv);
		param.put("search", search);
		IPage<Map<String, Object>> inventoryList = inventoryService.findLocalInventory(dto.getPage(),param);
		if(dto.getPage().getCurrent()==1) {
			List<Map<String, Object>> list = stockTakingService.getItemList(id, warehouseid,search);
			HashMap<String,Integer> indexmap=new HashMap<String,Integer>();
			if(list!=null && list.size()>0) {
				for (int j = 0; j < list.size(); j++) {// 对price进行格式化
					double price = 0.0;
					if(list.get(j).get("price")!=null)
						price=Double.parseDouble(list.get(j).get("price").toString());
					list.get(j).put("price", price);
					String mmaterialid= list.get(j).get("materialid").toString();
					indexmap.put(mmaterialid, null);
				}
			
			    for(int j=0;j<inventoryList.getRecords().size();j++) {
			    	String mmaterialid=inventoryList.getRecords().get(j).get("materialid").toString();
			    	if(indexmap.containsKey(mmaterialid)) {
			    		indexmap.put(mmaterialid, j);
			    	}
			    }
				
			    for(Entry<String, Integer> entry:indexmap.entrySet()) {
			    	Integer index = entry.getValue();
			    	if(index!=null) {
			    		if(inventoryList.getRecords().size()>index) {
			    			inventoryList.getRecords().remove(index.intValue());
			    		}
			    	}
			    }
			}
		    if(list!=null&&list.size()>0) {
		    	if(inventoryList!=null&&inventoryList.getRecords().size()>0) {
		    		inventoryList.getRecords().addAll(0, list);
		    	}else if(inventoryList!=null){
		    		inventoryList.getRecords().addAll(list);
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
		return Result.success(inventoryList);
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
	@GetMapping("/startAction")
	public Result<Map<String, Object>> startAction(String id,String warehouseid)throws BizException {
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
		Map<String, Object> invmap = warehouseService.getTotalInvAndWorth(warehouseid);
		StockTaking stocktaking = new StockTaking();
		stocktaking.setId(id);
		stocktaking.setNumber(number);
		stocktaking.setWarehouseid(warehouseid);
		stocktaking.setIsworking(true);// 正在盘点
		stocktaking.setShopid(user.getCompanyid());
		stocktaking.setCreator(user.getId());
		stocktaking.setCreatedate(new Date());
		stocktaking.setOperator(user.getId());
		if(invmap!=null&&invmap.get("totalqty")!=null){
			stocktaking.setWhtotalamount(Integer.parseInt(invmap.get("totalqty").toString()));
		}else{
			stocktaking.setWhtotalamount(0);
		}
		if(invmap!=null&&invmap.get("worth")!=null){
			stocktaking.setWhtotalprice(new BigDecimal(invmap.get("worth").toString()));
		}else{
			stocktaking.setWhtotalprice(new BigDecimal("0"));
		}
          
		boolean result = stockTakingService.save(stocktaking);
		if (result==true) {
			map.put("isok", true);
		}
		return Result.success(map);
	}


	@ApiOperation(value = "盘点完成")
	@GetMapping("/endAction")
	public Result<Map<String, Object>> endAction(String id,String warehouseid,String remark)
			throws BizException {
		UserInfo user = UserInfoContext.get();
		if(user==null){
			throw new BizException("请重新登录账号！");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		StockTaking stocktaking = stockTakingService.getById(id);
		stocktaking.setIsworking(false);// 盘点结束
		stocktaking.setRemark(remark);

		boolean result = stockTakingService.endAction(stocktaking, warehouseid, user);
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
	public Result<Map<String, Object>> cancelAction(String id,String warehouseid)
			throws BizException {
		Map<String, Object> map = new HashMap<String, Object>();
		StockTaking stocktaking = stockTakingService.getById(id);
		boolean result = stockTakingService.cancelAction(stocktaking, warehouseid);
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

}
