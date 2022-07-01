package com.wimoor.amazon.product.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.product.service.IAmzProductRefreshService;
import com.wimoor.amazon.product.service.IProductCaptureCatalogItemService;
import com.wimoor.amazon.product.service.IProductCaptureListingsItemService;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-17
 */
@Api(tags = "产品任务接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/product/amzProductRefresh")
public class AmzProductRefreshController {

	final IAmzProductRefreshService iAmzProductRefreshService;
	final IProductCaptureListingsItemService iProductCaptureListingsItemService;
	final IAmazonAuthorityService amazonAuthorityService;
	final IProductCaptureCatalogItemService iProductCaptureCatalogItemService;
	
    @ApiOperation(value = "创建对象")
    @GetMapping("/insert")
    public Result<Boolean> insertAction() {
    	iAmzProductRefreshService.insert();
        return Result.judge(true);
    }
	
    @ApiOperation(value = "更新商品对象")
    @GetMapping("/refresh")
    public Result<Boolean> refreshAction() {
    	amazonAuthorityService.executTask(iProductCaptureListingsItemService);
        return Result.judge(true);
    }
    
    @ApiOperation(value = "更新")
    @GetMapping("/refreshCatalog")
    public Result<Boolean> refreshCatalogAction() {
    	amazonAuthorityService.executTask(iProductCaptureCatalogItemService);
        return Result.judge(true);
    }
}

