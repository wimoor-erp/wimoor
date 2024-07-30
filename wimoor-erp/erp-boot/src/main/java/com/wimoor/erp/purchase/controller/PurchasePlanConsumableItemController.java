package com.wimoor.erp.purchase.controller;


import java.util.Date;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanConsumableItem;
import com.wimoor.erp.purchase.service.IPurchasePlanConsumableItemService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-08
 */

@Api(tags = "耗材采购计划")
@RestController
@SystemControllerLog( "耗材采购计划接口")
@RequestMapping("/api/v1/purchase/plan/consumable")
@RequiredArgsConstructor
public class PurchasePlanConsumableItemController {
	final IPurchasePlanConsumableItemService purchasePlanConsumableItemService;
	
	@PostMapping("/save")
    @Transactional
	public Result<PurchasePlanConsumableItem> savePlanItemAction(@RequestBody PurchasePlanConsumableItem item) throws ERPBizException {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		item.setShopid(shopid);
		item.setOpttime(new Date());
		item.setOperator(userinfo.getId());
		if(StrUtil.isBlankOrUndefined(item.getWarehouseid())){
			throw new BizException("入库仓库不能为空");
		}
		LambdaQueryWrapper<PurchasePlanConsumableItem> query=new LambdaQueryWrapper<PurchasePlanConsumableItem>();
		query.eq(PurchasePlanConsumableItem::getMaterialid, item.getMaterialid());
		query.eq(PurchasePlanConsumableItem::getWarehouseid, item.getWarehouseid());
		PurchasePlanConsumableItem old = purchasePlanConsumableItemService.getOne(query);
		if(old!=null) {
			item.setId(old.getId());
			purchasePlanConsumableItemService.updateById(item);
		}else {
			purchasePlanConsumableItemService.save(item);
		}
		return Result.success(item);
	}
	
	@GetMapping("/delete")
    @Transactional
	public Result<?> deletePlanItemAction(String materialid,String warehouseid) throws ERPBizException {
		LambdaQueryWrapper<PurchasePlanConsumableItem> query=new LambdaQueryWrapper<PurchasePlanConsumableItem>();
		query.eq(PurchasePlanConsumableItem::getMaterialid, materialid);
		query.eq(PurchasePlanConsumableItem::getWarehouseid, warehouseid);
		purchasePlanConsumableItemService.remove(query);
		return Result.success();
	}
	
 
	@ApiOperation(value = "计划清除")
	@GetMapping("/clear")
	@CacheEvict(value = { "inventoryByMskuCache"  }, allEntries = true)
	public Result<?> clearShipPlanItem(String warehouseid) {
		    UserInfo user = UserInfoContext.get();
			LambdaQueryWrapper<PurchasePlanConsumableItem> query=new LambdaQueryWrapper<PurchasePlanConsumableItem>();
			query.eq(PurchasePlanConsumableItem::getShopid, user.getCompanyid());
			query.eq(PurchasePlanConsumableItem::getWarehouseid, warehouseid);
			purchasePlanConsumableItemService.remove(query);
		return Result.success();
	}

	@ApiOperation(value = "计划刷新")
	@GetMapping("/getSummary")
	public Result<?> getSummary(String warehouseid) {
		UserInfo user = UserInfoContext.get();
		return Result.success(purchasePlanConsumableItemService.getSummary(user.getCompanyid(),warehouseid));
	}

	@ApiOperation(value = "计划列表")
	@GetMapping("/list")
	public Result<?> list(String warehouseid) {
		UserInfo user = UserInfoContext.get();
		return Result.success(purchasePlanConsumableItemService.getList(user.getCompanyid(),warehouseid));
	}
}

