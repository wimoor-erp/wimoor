package com.wimoor.erp.warehouse.controller;


import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventoryVo;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import com.wimoor.erp.warehouse.pojo.dto.ShelfInvListDto;
import com.wimoor.erp.warehouse.pojo.entity.ErpWarehouseAddress;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelf;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptRecord;
import com.wimoor.erp.warehouse.service.IErpWarehouseAddressService;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 货架产品库存 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Api(tags = "库位库存接口")
@SystemControllerLog("库位库存模块")
@RestController
@RequestMapping("/api/v1/warehoue/shelfInventory")
@RequiredArgsConstructor
public class WarehouseShelfInventoryController {
	final IWarehouseShelfInventoryService iWarehouseShelfInventoryService;
	final IWarehouseShelfService iWarehouseShelfService;
	final IWarehouseService warehouseService;
	final IErpWarehouseAddressService iErpWarehouseAddressService;
    @ApiOperation(value = "增加库位中特定SKU的数量")
    @SystemControllerLog("增加库位中特定SKU的数量")
    @PostMapping("/add")
    @Transactional
    public Result<String> add(@RequestBody List<WarehouseShelfInventoryOptRecord> invoptList) {
     	int count=0;
     	UserInfo user = UserInfoContext.get();
     	String randomId=warehouseService.getUUID();
     	for(int i=0;i<invoptList.size();i++) {
     		WarehouseShelfInventoryOptRecord invopt=invoptList.get(i);
     		invopt.setShopid(new BigInteger(user.getCompanyid()));
     		invopt.setOperator(new BigInteger(user.getId()));
     		invopt.setOpttime(LocalDateTime.now());
     		if(invopt.getQuantity()<=0) {
	     		throw new BizException("上架数量必须大于0");
	     	}
     		if(invopt.getFormtype()!=null&&StrUtil.isBlankOrUndefined(invopt.getFormid())&&invopt.getFormtype().equals("direct")) {
     			invopt.setFormid(randomId);
     		}else {
     			randomId=invopt.getFormid();
     		}
     		WarehouseShelfInventoryOptRecord record = iWarehouseShelfInventoryService.add(invopt);
     		if(record!=null) {
     			count++;
     		}
     	}
     	if(count>0) {
    		return Result.success(randomId);
     	}else {
     		return Result.failed();
     	}
    }
    
	
    @ApiOperation(value = "减少仓位中特定SKU的数量")
    @SystemControllerLog(value="减少仓位中特定SKU的数量")
    @PostMapping("/sub")
    @Transactional
    public Result<String> sub(@RequestBody List<WarehouseShelfInventoryOptRecord> invoptList) {
     	int count=0;
     	UserInfo user = UserInfoContext.get();
     	String randomId=warehouseService.getUUID();
    	for(int i=0;i<invoptList.size();i++) {
    		WarehouseShelfInventoryOptRecord invopt=invoptList.get(i);
	     	invopt.setOperator(new BigInteger(user.getId()));
	     	invopt.setShopid(new BigInteger(user.getCompanyid()));
	     	invopt.setOpttime(LocalDateTime.now());
	     	if(invopt.getQuantity()<=0) {
	     		throw new BizException("下架数量必须大于0");
	     	}
	     	if(invopt.getFormtype()!=null&&StrUtil.isBlankOrUndefined(invopt.getFormid())&&invopt.getFormtype().equals("direct")) {
	 			invopt.setFormid(randomId);
	 		}else {
	 			randomId=invopt.getFormid();
	 		}
	     	WarehouseShelfInventoryOptRecord record = iWarehouseShelfInventoryService.sub(invopt);
	     	if(record!=null) {
	     		count++;
	     	}
    	}
    	if(count>0) {
    		return Result.success(randomId);
     	}else {
     		return Result.failed();
     	}
        
    }
    
    @ApiOperation("查询当前仓库中暂存库存的情况")
    @PostMapping("/getShelfList")
    public Result<IPage<WarehouseShelfInventoryVo>> getShelfListAction(@ApiParam("查询DTO")@RequestBody ShelfInvListDto condition){
    	UserInfo user = UserInfoContext.get();
    	Map<String, Object> param=new HashMap<String, Object>();
    	String shopid = user.getCompanyid();
    	String search=null;
    	param.put("shopid", new BigInteger(shopid));
    	if(StrUtil.isBlank(condition.getSearch())) {
    		search=null;
    	}else {
    		search="%"+condition.getSearch()+"%";
    	}
    	if(StrUtil.isBlank(condition.getMtype())) {
    		param.put("mtype",null);
    	}else {
    		if(condition.getMtype().equals("product")) {
    			param.put("mtype",0);
    		}
    	}
    	if(StrUtil.isBlank(condition.getShelfid())) {
    		param.put("shelfid",null);
    	}else {
    		param.put("shelfid",new BigInteger(condition.getShelfid()));
    		WarehouseShelf self = iWarehouseShelfService.getById(condition.getShelfid());
    		if(self!=null&&self.getAddressid()!=null) {
    			condition.setAddressid(self.getAddressid().toString());
    		}
    	}
    	if(StrUtil.isBlank(condition.getAddressid())) {
    		param.put("addressid", null);
    	}else { 
    		param.put("addressid", new BigInteger(condition.getAddressid()));
    	}
    	param.put("search",search);
    	if(StrUtil.isNotEmpty(condition.getMaterialid())) {
    		param.put("materialid",condition.getMaterialid());
    	}
    	if(StrUtil.isNotEmpty(condition.getWarehouseid())) {
    		param.put("warehouseid",condition.getWarehouseid());
    	}
    	Page<WarehouseShelfInventoryVo> page = condition.getPage();
		page.addOrder(OrderItem.desc("orderitem"));
		IPage<WarehouseShelfInventoryVo> list = iWarehouseShelfInventoryService.getUnShelfInventoryList(page,param);
		if(StrUtil.isNotBlank(condition.getAddressid())) {
			  for(WarehouseShelfInventoryVo item:list.getRecords()) {
				item.setShelfid(condition.getShelfid());
			}
		}
    	return Result.success(list);
    }
    
    @ApiOperation("查询当前库位中已上架的情况")
    @PostMapping("/getShelfInventoryList")
    public Result<IPage<WarehouseShelfInventoryVo>> getShelfInventoryListAction(@ApiParam("查询DTO")@RequestBody ShelfInvListDto condition){
    	UserInfo user = UserInfoContext.get();
    	Map<String, Object> param=new HashMap<String, Object>();
    	String shopid = user.getCompanyid();
    	String search=null;
    	param.put("shopid", new BigInteger(shopid));
    	if(StrUtil.isBlank(condition.getSearch())) {
    		search=null;
    	}else {
    		search="%"+condition.getSearch().trim()+"%";
    	}
    	if(StrUtil.isBlank(condition.getShelfid())) {
    		param.put("shelfid",null);
    	}else {
    		param.put("shelfid",new BigInteger(condition.getShelfid()));
    		WarehouseShelf shelf = iWarehouseShelfService.getById(condition.getShelfid());
    		if(shelf!=null&&shelf.getAddressid()!=null) {
    			condition.setAddressid(shelf.getAddressid().toString());
    		}
    	}
    	if(StrUtil.isNotBlank(condition.getWarehouseid())) {
    		param.put("warehouseid",condition.getWarehouseid());
    		Warehouse warehouse = warehouseService.getById(condition.getWarehouseid());
    		if(warehouse!=null&&warehouse.getAddressid()!=null) {
    			condition.setAddressid(warehouse.getAddressid());
    		}
    	} 
    	if(StrUtil.isBlank(condition.getAddressid())) {
    		param.put("addressid",null);
    	}else {
    		param.put("addressid",new BigInteger(condition.getAddressid()));
    	}
    	if(StrUtil.isBlank(condition.getAllchildren())) {
    		param.put("allchildren","true");
    	}else {
    		param.put("allchildren",condition.getAllchildren());
    	}
    	if(StrUtil.isNotEmpty(condition.getMaterialid())) {
    		param.put("materialid",condition.getMaterialid());
    	}
    	param.put("search",search);
    	if(StrUtil.isNotBlank(condition.getSku()) ) {
    		param.put("sku",condition.getSku().trim());
    	}
    	if(StrUtil.isNotBlank(condition.getIsstocking())) {
    		param.put("isstocking", "true");
    	}
    	if(StrUtil.isNotBlank(condition.getStocktakingid()) ) {
    		param.put("stocktakingid",condition.getStocktakingid().trim());
    	}
		IPage<WarehouseShelfInventoryVo> list = iWarehouseShelfInventoryService.getShelfInventoryList(condition.getPage(),param);
		return Result.success(list);
    }
    
    @ApiOperation("查询当前库位中已上架的情况")
    @PostMapping("/getShelfInventoryStockingList")
    public Result<?> getShelfInventoryStockingListAction(@ApiParam("查询DTO")@RequestBody ShelfInvListDto condition){
    	UserInfo user = UserInfoContext.get();
    	Map<String, Object> param=new HashMap<String, Object>();
    	String shopid = user.getCompanyid();
    	String search=null;
    	param.put("shopid", new BigInteger(shopid));
    	if(StrUtil.isBlank(condition.getSearch())) {
    		search=null;
    	}else {
    		search="%"+condition.getSearch().trim()+"%";
    	}
    	if(StrUtil.isBlank(condition.getShelfid())) {
    		param.put("shelfid",null);
    	}else {
    		param.put("shelfid",new BigInteger(condition.getShelfid()));
    		WarehouseShelf shelf = iWarehouseShelfService.getById(condition.getShelfid());
    		if(shelf!=null&&shelf.getAddressid()!=null) {
    			condition.setAddressid(shelf.getAddressid().toString());
    		}
    	}
    	if(StrUtil.isNotBlank(condition.getWarehouseid())) {
    		param.put("warehouseid",condition.getWarehouseid());
    		Warehouse warehouse = warehouseService.getById(condition.getWarehouseid());
    		if(warehouse!=null&&warehouse.getAddressid()!=null) {
    			condition.setAddressid(warehouse.getAddressid());
    		}
    	} 
    	if(StrUtil.isBlank(condition.getAddressid())) {
    		param.put("addressid",null);
    	}else {
    		param.put("addressid",new BigInteger(condition.getAddressid()));
    	}
    	if(condition.getHasInv()!=null&&condition.getHasInv()==true) {
    		param.put("hasinv",true);
    	}
    	if(StrUtil.isNotEmpty(condition.getMaterialid())) {
    		param.put("materialid",condition.getMaterialid());
    	}
    	param.put("search",search);
    	if(StrUtil.isNotBlank(condition.getSku()) ) {
    		param.put("sku",condition.getSku().trim());
    	}
    	if(StrUtil.isNotBlank(condition.getStocktakingid()) ) {
    		param.put("stocktakingid",condition.getStocktakingid().trim());
    	}
    	if(StrUtil.isNotBlank(condition.getSelected())) {
    		param.put("selected","true");
    		return Result.success(iWarehouseShelfInventoryService.getShelfInventoryStockList(param));
    	}else {
    		IPage<WarehouseShelfInventoryVo> list = iWarehouseShelfInventoryService.getShelfInventoryStockList(condition.getPage(),param);
    		return Result.success(list);
    	}
		
    }
    
    //导出库位中的库存情况
    @ApiOperation("导出库位中的库存情况")
    @PostMapping("/downloadShelfInventoryStockingList")
    public void downloadShelfInventoryStockingListAction(@ApiParam("查询DTO")@RequestBody ShelfInvListDto condition, HttpServletResponse response){
    	// 创建新的Excel工作薄
       	SXSSFWorkbook workbook = new SXSSFWorkbook();
    	UserInfo user = UserInfoContext.get();
    	Map<String, Object> param=new HashMap<String, Object>();
    	String shopid = user.getCompanyid();
    	String search=null;
    	param.put("shopid", new BigInteger(shopid));
    	if(StrUtil.isBlank(condition.getSearch())) {
    		search=null;
    	}else {
    		search="%"+condition.getSearch().trim()+"%";
    	}
    	if(StrUtil.isBlank(condition.getShelfid())) {
    		param.put("shelfid",null);
    	}else {
    		param.put("shelfid",new BigInteger(condition.getShelfid()));
    		WarehouseShelf shelf = iWarehouseShelfService.getById(condition.getShelfid());
    		if(shelf!=null&&shelf.getAddressid()!=null) {
    			condition.setAddressid(shelf.getAddressid().toString());
    		}
    	}
    	if(StrUtil.isNotBlank(condition.getWarehouseid())) {
    		param.put("warehouseid",condition.getWarehouseid());
    		Warehouse warehouse = warehouseService.getById(condition.getWarehouseid());
    		if(warehouse!=null&&warehouse.getAddressid()!=null) {
    			condition.setAddressid(warehouse.getAddressid());
    		}
    	} 
    	if(StrUtil.isBlank(condition.getAddressid())) {
    		param.put("addressid",null);
    	}else {
    		param.put("addressid",new BigInteger(condition.getAddressid()));
    	}
        param.put("hasinv",true);
    	if(StrUtil.isNotEmpty(condition.getMaterialid())) {
    		param.put("materialid",condition.getMaterialid());
    	}
    	param.put("search",search);
    	if(StrUtil.isNotBlank(condition.getSku()) ) {
    		param.put("sku",condition.getSku().trim());
    	}
    	if(StrUtil.isNotBlank(condition.getStocktakingid()) ) {
    		param.put("stocktakingid",condition.getStocktakingid().trim());
    	}
    	List<WarehouseShelfInventoryVo> list = iWarehouseShelfInventoryService.getShelfInventoryStockList(param);
    	iWarehouseShelfInventoryService.downloadShelfStockList(workbook, list);
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
    
    
    
    
    @ApiOperation("查询当前库位中已上架的情况")
    @PostMapping("/getShelfInventoryByShipment")
    public Result<ShipInboundShipmenSummarytVo> getShelfInvAction(@ApiParam("查询DTO")@RequestBody ShipInboundShipmenSummarytVo itemsum){
           return  Result.success(iWarehouseShelfInventoryService.formInvAssemblyShelf(itemsum));
    }
}

