package com.wimoor.erp.purchase.controller;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanItem;
import com.wimoor.erp.purchase.pojo.entity.PurchaseWareHouseMaterial;
import com.wimoor.erp.purchase.service.IPurchasePlanItemService;
import com.wimoor.erp.purchase.service.IPurchaseWareHouseMaterialService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "采购计划item接口")
@RestController
@SystemControllerLog( "采购计划item接口")
@RequestMapping("/api/v1/purchase/plan/item")
@RequiredArgsConstructor
public class PlanItemController {
	final IPurchasePlanItemService iPurchasePlanItemService;
	final protected ISerialNumService serialNumService;
	final IPurchaseWareHouseMaterialService iPurchaseWareHouseMaterialService;
	
	@PostMapping("/save")
    @Transactional
	public Result<PurchasePlanItem> savePlanItemAction(@RequestBody PurchasePlanItem item) throws ERPBizException {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		item.setShopid(shopid);
		item.setOpttime(new Date());
		item.setOperator(userinfo.getId());
		if(StrUtil.isBlankOrUndefined(item.getWarehouseid())){
			throw new BizException("入库仓库不能为空");
		}
		LambdaQueryWrapper<PurchasePlanItem> query=new LambdaQueryWrapper<PurchasePlanItem>();
		query.eq(PurchasePlanItem::getMaterialid, item.getMaterialid());
		query.eq(PurchasePlanItem::getPlanid, item.getPlanid());
		PurchasePlanItem old = iPurchasePlanItemService.getOne(query);
		if(old!=null) {
			item.setId(old.getId());
			iPurchasePlanItemService.updateById(item);
		}else {
			iPurchasePlanItemService.save(item);
		}
		return Result.success(item);
	}
	
	@GetMapping("/delete")
    @Transactional
	public Result<?> deletePlanItemAction(String planid,String materialid) throws ERPBizException {
		LambdaQueryWrapper<PurchasePlanItem> query=new LambdaQueryWrapper<PurchasePlanItem>();
		query.eq(PurchasePlanItem::getMaterialid, materialid);
		query.eq(PurchasePlanItem::getPlanid, planid);
		iPurchasePlanItemService.remove(query);
		return Result.success();
	}
	
	@PostMapping("/changewh")
    @Transactional
	public Result<?> deletePlanItemAction(@RequestBody PurchaseWareHouseMaterial pwm) throws ERPBizException {
		 iPurchaseWareHouseMaterialService.savePurchaseWareHouseMaterial(pwm);
		 return Result.success();
	}
	
	@ApiOperation(value = "计划清除")
	@GetMapping("/clear")
	public Result<?> clearShipPlanItem(String planid) {
		UserInfo user = UserInfoContext.get();
			LambdaQueryWrapper<PurchasePlanItem> query=new LambdaQueryWrapper<PurchasePlanItem>();
			query.eq(PurchasePlanItem::getShopid, user.getCompanyid());
			query.eq(PurchasePlanItem::getPlanid,planid);
			iPurchasePlanItemService.remove(query);
		return Result.success();
	}

	@ApiOperation(value = "计划刷新")
	@GetMapping("/getSummary")
	public Result<?> getSummary(String planid) {
		UserInfo user = UserInfoContext.get();
		return Result.success(iPurchasePlanItemService.getSummary(user.getCompanyid(),planid));
	}

	@ApiOperation(value = "计划列表")
	@GetMapping("/list")
	public Result<?> list(String planid) {
		UserInfo user = UserInfoContext.get();
		return Result.success(iPurchasePlanItemService.getList(user.getCompanyid(),planid));
	}
	
	@ApiOperation(value = "计划列表")
	@GetMapping("/listHis")
	public Result<?> listHis(String warehouseid) {
		UserInfo user = UserInfoContext.get();
		return Result.success(iPurchasePlanItemService.getHisList(user.getCompanyid(),warehouseid));
	}
	
	@ApiOperation(value = "计划刷新")
	@GetMapping("/getBatchList")
	public Result<?> getBatch(String planid,String batchnumber) {
		UserInfo user = UserInfoContext.get();
		return Result.success(iPurchasePlanItemService.getList(user.getCompanyid(),planid,batchnumber));
	}


	@ApiOperation(value = "计划打包")
	@PostMapping("/batch")
	public Result<String> batchShipPlanItem(@RequestBody List<PurchasePlanItem> list) {
		UserInfo user = UserInfoContext.get();
		String number=null;
		 try {
			 number=serialNumService.readSerialNumber(user.getCompanyid(), "DP");
			} catch (Exception e) {
				e.printStackTrace();
				try {
					 number=serialNumService.readSerialNumber(user.getCompanyid(), "DP");
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new BizException("编码获取失败,请联系管理员");
				}
			}
	    for(PurchasePlanItem item:list) {
			iPurchasePlanItemService.lambdaUpdate()
			.set(PurchasePlanItem::getBatchnumber, number)
			.eq(PurchasePlanItem::getMaterialid, item.getMaterialid())
			.eq(PurchasePlanItem::getWarehouseid, item.getWarehouseid())
			.eq(PurchasePlanItem::getPlanid, item.getPlanid());
	    }
		return Result.success(number);
	}

    @ApiOperation(value = "计划打包")
    @GetMapping("/removeBatch")
    public Result<?> moveBatchShipPlanItem(String batchnumber) {
    	UserInfo user = UserInfoContext.get();
    	iPurchasePlanItemService.moveBatch(user.getCompanyid(),batchnumber);
    	return Result.success();
    }

 
}
