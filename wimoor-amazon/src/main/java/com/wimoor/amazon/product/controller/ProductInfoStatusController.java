package com.wimoor.amazon.product.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.product.pojo.entity.ProductInfoStatusDefine;
import com.wimoor.amazon.product.service.IProductInfoStatusService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Component("productInfoStatusController")
@RequestMapping("/api/v1/report/product/productInfoStatus")
public class ProductInfoStatusController {

	final IProductInfoStatusService productInfoStatusService;
	
	
	@GetMapping("/getProStatusByShop")
	public Result<List<ProductInfoStatusDefine> > getProStatusByShopAction() {
		UserInfo user = UserInfoContext.get();
		List<ProductInfoStatusDefine> result = productInfoStatusService.getProStatusByShop(user.getCompanyid());
		return Result.success(result);
	}
	
	@GetMapping("/updateProductInfoStatus")
	public Result<Map<String,Object>> updateProductInfoStatusAction(String id,String name,String remark,String color) {
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map=new HashMap<String,Object>();
		boolean isupdate=false;
		if(StrUtil.isEmpty(id)) {
			isupdate=false;
		}else {
			isupdate=true;
		}
		if(StrUtil.isEmpty(color)) {
			color="blue";
		}
		if(StrUtil.isEmpty(remark)) {
			remark=null;
		}
		int result = productInfoStatusService.updateProductInfoStatus(isupdate,user,id,name,remark,color);
		if (result > 0) {
			map.put("isOK", "true");
		} else {
			map.put("isOK", "fasle");
		}
		return Result.success(map);
	}
	
	@GetMapping("/deleteProductInfoStatus")
	public Result<Map<String,Object>> deleteProductInfoStatusAction(String id) {
		UserInfo user = UserInfoContext.get();
		Map<String,Object> map=new HashMap<String,Object>();
		int result = productInfoStatusService.deleteProductInfoStatus(id,user.getCompanyid());
		if (result > 0) {
			map.put("isOK", "true");
		} else {
			map.put("isOK", "fasle");
		}
		return Result.success(map);
	}
	
	
}


