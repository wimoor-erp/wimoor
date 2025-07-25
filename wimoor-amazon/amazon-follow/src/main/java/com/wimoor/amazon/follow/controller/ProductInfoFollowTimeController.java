package com.wimoor.amazon.follow.controller;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.follow.pojo.entity.ProductInfoFollowTime;
import com.wimoor.amazon.follow.service.IProductInfoFollowTimeService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-07
 */
@Api(tags = "跟卖设置时间接口")
@RestController
@SystemControllerLog("跟卖设置时间")
@RequestMapping("/api/v1/follow/productInfoFollowTime")
public class ProductInfoFollowTimeController {

	@Autowired
	IProductInfoFollowTimeService iProductInfoFollowTimeService;
	
	@GetMapping("/list")
	public Result<List<ProductInfoFollowTime>> getListDataAction() {
		UserInfo user = UserInfoContext.get();
		List<ProductInfoFollowTime> list=iProductInfoFollowTimeService.findByCondition(user.getCompanyid());
		return Result.success(list);
	}
	
	@PostMapping("/add")
	public Result<?> addAction(@RequestBody ProductInfoFollowTime item) {
		UserInfo user = UserInfoContext.get();
		item.setShopid(user.getCompanyid());
		item.setCreator(user.getId());
		item.setOperator(user.getId());
		item.setCreatetime(new Date());
		item.setOpttime(new Date());
		return Result.success(iProductInfoFollowTimeService.addItem(item)>0);
	}
	
	@PostMapping("/update")
	public Result<?> updateAction(@RequestBody ProductInfoFollowTime item) {
		UserInfo user = UserInfoContext.get();
		item.setShopid(user.getCompanyid());
		item.setOperator(user.getId());
		item.setOpttime(new Date());
		return Result.success(iProductInfoFollowTimeService.updateItem(item)>0);
	}
	
	@GetMapping("/delete")
	public Result<?> deleteAction(String id) {
		return Result.success(iProductInfoFollowTimeService.deleteItem(id)>0);
	}
}

