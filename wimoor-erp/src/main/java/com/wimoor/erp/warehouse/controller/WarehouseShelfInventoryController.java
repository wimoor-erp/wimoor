package com.wimoor.erp.warehouse.controller;


import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.api.erp.warehouse.pojo.vo.WarehouseShelfInventoryVo;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.warehouse.pojo.dto.ShelfInvListDto;
import com.wimoor.erp.warehouse.pojo.entity.ErpWarehouseAddress;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelf;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptPro;
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
    public Result<String> add(@RequestBody List<WarehouseShelfInventoryOptPro> invoptList) {
     	int count=0;
     	UserInfo user = UserInfoContext.get();
     	String randomId=warehouseService.getUUID();
     	for(int i=0;i<invoptList.size();i++) {
     		WarehouseShelfInventoryOptPro invopt=invoptList.get(i);
     		invopt.setShopid(new BigInteger(user.getCompanyid()));
     		invopt.setOperator(new BigInteger(user.getId()));
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
    public Result<String> sub(@RequestBody List<WarehouseShelfInventoryOptPro> invoptList) {
     	int count=0;
     	UserInfo user = UserInfoContext.get();
     	String randomId=warehouseService.getUUID();
    	for(int i=0;i<invoptList.size();i++) {
	     	WarehouseShelfInventoryOptPro invopt=invoptList.get(i);
	     	invopt.setOperator(new BigInteger(user.getId()));
	     	invopt.setShopid(new BigInteger(user.getCompanyid()));
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
    	Page<WarehouseShelfInventoryVo> page = condition.getPage();
		page.addOrder(OrderItem.desc("orderitem"));
		IPage<WarehouseShelfInventoryVo> list = iWarehouseShelfInventoryService.getUnShelfInventoryList(page,param);
		if(StrUtil.isNotBlank(condition.getAddressid())) {
			  ErpWarehouseAddress address = iErpWarehouseAddressService.getById(condition.getAddressid());
			for(WarehouseShelfInventoryVo item:list.getRecords()) {
				item.setWarehousename(address.getName());
				item.setWarehouseid(condition.getAddressid());
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
    		search="%"+condition.getSearch()+"%";
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
		IPage<WarehouseShelfInventoryVo> list = iWarehouseShelfInventoryService.getShelfInventoryList(condition.getPage(),param);
		return Result.success(list);
    }
    
    @ApiOperation("查询当前库位中已上架的情况")
    @PostMapping("/getShelfInventoryByShipment")
    public Result<ShipInboundShipmenSummarytVo> getShelfInvAction(@ApiParam("查询DTO")@RequestBody ShipInboundShipmenSummarytVo itemsum){
           return  Result.success(iWarehouseShelfInventoryService.formInvAssemblyShelf(itemsum));
    }
}

