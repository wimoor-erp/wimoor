package com.wimoor.amazon.product.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.product.service.IProductRankService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;

import io.swagger.annotations.Api;
 

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-07-21
 */
@Api(tags = "产品排名接口")
@RestController
@SystemControllerLog("商品排名")
@RequestMapping("/api/v1/report/product/productRank")
public class ProductRankController {
	@Autowired
	IProductRankService iProductRankService;
	
	@GetMapping("/rank")
	public Result<?> productRankAction(String id) {
		List<Map<String, Object>> map = iProductRankService.findProductRank(id);
		return Result.success(map);
	}
}

