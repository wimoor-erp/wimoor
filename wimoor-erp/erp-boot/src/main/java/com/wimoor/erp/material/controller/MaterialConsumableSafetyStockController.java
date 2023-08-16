package com.wimoor.erp.material.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.material.pojo.entity.MaterialConsumableSafetyStock;
import com.wimoor.erp.material.service.IMaterialConsumableSafetyStockService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-09
 */
 

@Api(tags = "本地产品辅料安全库存比例")
@RestController
@RequestMapping("/api/v1/material/consumable/safety")
@SystemControllerLog("本地产品辅料安全库存比例")
@RequiredArgsConstructor
public class MaterialConsumableSafetyStockController {
  final IMaterialConsumableSafetyStockService iIMaterialConsumableSafetyStockService;
  
  @ApiOperation(value = "修改辅料安全库存比例")
  @SystemControllerLog("修改辅料安全库存比例")
  @PostMapping("/save")
  public Result<String> saveAction(@RequestBody MaterialConsumableSafetyStock item) {
	  UserInfo userinfo = UserInfoContext.get();
	  item.setOperator(userinfo.getId());
	  item.setOpttime(new Date());
	  item.setShopid(userinfo.getCompanyid());
	  MaterialConsumableSafetyStock oldone = iIMaterialConsumableSafetyStockService.lambdaQuery()
	  .eq(MaterialConsumableSafetyStock::getShopid, userinfo.getCompanyid()).one();
	  if(oldone==null) {
		  iIMaterialConsumableSafetyStockService.save(item);
	  }else {
		  item.setId(oldone.getId());
		  iIMaterialConsumableSafetyStockService.updateById(item);
	  }
	  return Result.success();
  }
  
  @ApiOperation(value = "查看辅料安全库存比例")
  @GetMapping("/show")
  public Result<MaterialConsumableSafetyStock> showAction() {
	  UserInfo userinfo = UserInfoContext.get();
	  return Result.success(iIMaterialConsumableSafetyStockService.lambdaQuery()
			  .eq(MaterialConsumableSafetyStock::getShopid, userinfo.getCompanyid()).one() );
  }
}

