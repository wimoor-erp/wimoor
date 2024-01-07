package com.wimoor.amazon.product.controller;


import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlanShipItem;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanService;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanShipItemService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-12-10
 */
@Api(tags = "fba发货计划Items接口")
@RestController
@SystemControllerLog("发货计划")
@RequestMapping("/api/v1/product/salesplan/shipItem")
@RequiredArgsConstructor
public class AmzProductSalesPlanShipItemController {
final IAmzProductSalesPlanShipItemService iAmzProductSalesPlanShipItemService;
final IAmzProductSalesPlanService iAmzProductSalesPlanService;
final ISerialNumService serialNumService;

@ApiOperation(value = "保存计划SKU")
@SystemControllerLog("保存计划SKU")
@PostMapping("/save")
@Transactional
public Result<Integer> saveShipPlanItem(@RequestBody List<AmzProductSalesPlanShipItem> list) {
	UserInfo user = UserInfoContext.get();
	Map<String,List<AmzProductSalesPlanShipItem>> oldmap=new HashMap<String,List<AmzProductSalesPlanShipItem>>();
	int count=0;
	if(list!=null&&list.size()>0) {
		AmzProductSalesPlanShipItem first=list.get(0);
		LambdaQueryWrapper<AmzProductSalesPlanShipItem> query=new LambdaQueryWrapper<AmzProductSalesPlanShipItem>();
		query.eq(AmzProductSalesPlanShipItem::getShopid, user.getCompanyid());
		query.eq(AmzProductSalesPlanShipItem::getGroupid, 	first.getGroupid());
		query.eq(AmzProductSalesPlanShipItem::getMsku, 	first.getMsku());
		query.eq(AmzProductSalesPlanShipItem::getWarehouseid,first.getWarehouseid());
		List<AmzProductSalesPlanShipItem> oldlist = iAmzProductSalesPlanShipItemService.list(query);
		for(AmzProductSalesPlanShipItem item:oldlist) {
			String key=item.getMarketplaceid();
			List<AmzProductSalesPlanShipItem> mylist = oldmap.get(key);
			if(mylist==null) {
				mylist=new ArrayList<AmzProductSalesPlanShipItem>();
			}
			mylist.add(item);
			oldmap.put(key, mylist);
		}
	}
    for(AmzProductSalesPlanShipItem item:list) {
    	if(item.getAmount()>0) {
        	 List<AmzProductSalesPlanShipItem> oldlist = oldmap.get(item.getMarketplaceid());
        	 if(item.getSubnum()==null||item.getSubnum()<=1) {
        		if(oldlist!=null) {
        			for(AmzProductSalesPlanShipItem reitem:oldlist) {
            			iAmzProductSalesPlanShipItemService.removeById(reitem.getId());
            		}
        			oldmap.remove(item.getMarketplaceid());
        		}
        		item.setShopid(new BigInteger(user.getCompanyid()));
            	item.setOperator(new BigInteger(user.getId()));
            	item.setOpttime(LocalDateTime.now());
            	item.setAftersalesday(iAmzProductSalesPlanService.getAfterSales(item));	
            	count++;
            	if(item.getOverseaid()==null) {
            		item.setOverseaid(new BigInteger("0"));
            	}
            	iAmzProductSalesPlanShipItemService.save(item);
        	}else if(item.getSubnum()>1) {
        		oldmap.remove(item.getMarketplaceid());
        	}
    	}
    }
    for(Entry<String, List<AmzProductSalesPlanShipItem>> entry:oldmap.entrySet()) {
    	List<AmzProductSalesPlanShipItem> oldlist = entry.getValue();
    	for(AmzProductSalesPlanShipItem reitem:oldlist) {
			iAmzProductSalesPlanShipItemService.removeById(reitem.getId());
		}
    }
	return Result.success(count);
}

@ApiOperation(value = "删除计划SKU")
@SystemControllerLog("删除计划SKU")
@DeleteMapping("/remove")
public Result<?> saveShipPlanItem(String groupid,String warehouseid,String msku) {
	UserInfo user = UserInfoContext.get();
		LambdaQueryWrapper<AmzProductSalesPlanShipItem> query=new LambdaQueryWrapper<AmzProductSalesPlanShipItem>();
		query.eq(AmzProductSalesPlanShipItem::getShopid, user.getCompanyid());
		query.eq(AmzProductSalesPlanShipItem::getGroupid,groupid);
		query.eq(AmzProductSalesPlanShipItem::getMsku, msku);
		query.eq(AmzProductSalesPlanShipItem::getWarehouseid,warehouseid);
		iAmzProductSalesPlanShipItemService.remove(query);
	return Result.success();
}

@ApiOperation(value = "计划分割")
@PostMapping("/subsplit")
public Result<List<AmzProductSalesPlanShipItem>> subsplit(@RequestBody  AmzProductSalesPlanShipItem dto) {
	UserInfo user = UserInfoContext.get();
		LambdaQueryWrapper<AmzProductSalesPlanShipItem> query=new LambdaQueryWrapper<AmzProductSalesPlanShipItem>();
		query.eq(AmzProductSalesPlanShipItem::getShopid, user.getCompanyid());
		query.eq(AmzProductSalesPlanShipItem::getGroupid, 	dto.getGroupid());
		query.eq(AmzProductSalesPlanShipItem::getMsku, 	dto.getMsku());
		query.eq(AmzProductSalesPlanShipItem::getWarehouseid,dto.getWarehouseid());
		query.eq(AmzProductSalesPlanShipItem::getMarketplaceid,dto.getMarketplaceid());
		List<AmzProductSalesPlanShipItem> oldlist = iAmzProductSalesPlanShipItemService.list(query);
     return Result.success(oldlist);	 
 }

@ApiOperation(value = "计划清除")
@SystemControllerLog("计划清除")
@GetMapping("/clear")
public Result<?> clearShipPlanItem(String groupid,String warehouseid) {
	UserInfo user = UserInfoContext.get();
		LambdaQueryWrapper<AmzProductSalesPlanShipItem> query=new LambdaQueryWrapper<AmzProductSalesPlanShipItem>();
		query.eq(AmzProductSalesPlanShipItem::getShopid, user.getCompanyid());
		query.eq(AmzProductSalesPlanShipItem::getGroupid,groupid);
		query.eq(AmzProductSalesPlanShipItem::getWarehouseid,warehouseid);
		iAmzProductSalesPlanShipItemService.remove(query);
	return Result.success();
}

@ApiOperation(value = "计划刷新")
@GetMapping("/getSummary")
public Result<?> getSummary(String groupid,String warehouseid) {
	UserInfo user = UserInfoContext.get();
	return Result.success(iAmzProductSalesPlanShipItemService.getSummary(user.getCompanyid(),groupid,warehouseid));
}

@ApiOperation(value = "计划列表")
@GetMapping("/list")
public Result<?> list(String groupid,String warehouseid) {
	UserInfo user = UserInfoContext.get();
    return Result.success(iAmzProductSalesPlanShipItemService.getList(user.getCompanyid(),groupid,warehouseid,null));
}

@ApiOperation(value = "计划刷新")
@GetMapping("/getBatchList")
public Result<?> getBatch(String groupid,String warehouseid,String batchnumber) {
	UserInfo user = UserInfoContext.get();
	return Result.success(iAmzProductSalesPlanShipItemService.getList(user.getCompanyid(),groupid,warehouseid,batchnumber));
}


@ApiOperation(value = "计划打包")
@PostMapping("/batch")
@Transactional
public Result<String> batchShipPlanItem(@RequestBody List<AmzProductSalesPlanShipItem> list) {
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
    for(AmzProductSalesPlanShipItem item:list) {
    	iAmzProductSalesPlanShipItemService.updateBatch( item.getId(), number)  ;
    }
	return Result.success(number);
}

    @ApiOperation(value = "计划归档")
    @PostMapping("/removeBatch")
	@Transactional
    public Result<?> moveBatchShipPlanItem(String batchnumber) {
    	UserInfo user = UserInfoContext.get();
    	iAmzProductSalesPlanShipItemService.moveBatch(user.getCompanyid(),batchnumber);
    	return Result.success();
    }
 
}

