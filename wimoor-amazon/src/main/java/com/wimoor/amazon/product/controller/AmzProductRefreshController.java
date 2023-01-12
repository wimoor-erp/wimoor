package com.wimoor.amazon.product.controller;


import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.spapi.model.listings.Item;
import com.amazon.spapi.model.productpricing.GetPricingResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.service.IAmzProductRefreshService;
import com.wimoor.amazon.product.service.IProductCaptureCatalogItemService;
import com.wimoor.amazon.product.service.IProductCaptureListingsItemService;
import com.wimoor.amazon.product.service.IProductCaptureProductPriceService;
import com.wimoor.common.result.Result;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Component("amzProductRefreshController")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/product/amzProductRefresh")
public class AmzProductRefreshController {

	final IAmzProductRefreshService iAmzProductRefreshService;
	final IProductCaptureListingsItemService iProductCaptureListingsItemService;
	final IAmazonAuthorityService amazonAuthorityService;
	final IProductCaptureCatalogItemService iProductCaptureCatalogItemService;
	final IProductCaptureProductPriceService iProductCaptureProductPriceService;
	
    @ApiOperation(value = "创建对象")
    @GetMapping("/insert")
    public Result<Boolean> insertAction() {
    	new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				iAmzProductRefreshService.insert();
			}
    	}).start();
        return Result.judge(true);
    }
	
    //
    @ApiOperation(value = "更新商品对象")
    @GetMapping("/refresh")
    public Result<Boolean> refreshAction() {
    	log.info("refreshProductAction-----"+new Date());
    	amazonAuthorityService.executTask(iProductCaptureListingsItemService);
        return Result.judge(true);
    }
    
    @ApiOperation(value = "更新商品对象")
    @GetMapping("/refreshByAuth")
    public Result<Boolean> refreshAction(String authid) {
    	AmazonAuthority auth = amazonAuthorityService.getById(authid);
    	iProductCaptureListingsItemService.runApi(auth);
        return Result.judge(true);
    }
    
    @ApiOperation(value = "更新")
    @GetMapping("/refreshCatalog")
    public Result<Boolean> refreshCatalogAction() {
    	log.info("refreshCatalogAction-----"+new Date());
    	amazonAuthorityService.executTask(iProductCaptureCatalogItemService);
        return Result.judge(true);
    }
    
    @ApiOperation(value = "更新")
    @GetMapping("/refreshPrice")
    public Result<Boolean> refreshPriceAction() {
    	log.info("refreshPriceAction-----"+new Date());
    	amazonAuthorityService.executTask(iProductCaptureProductPriceService);
        return Result.judge(true);
    }
    
    @ApiOperation(value = "刷新产品价格")
    @GetMapping("/refreshPriceBySKU")
    public Result<GetPricingResponse> refreshPriceBySKUAction(String groupid,String marketplaceid,String sku) {
    	log.info("refreshPriceAction-----"+new Date());
    	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
    	GetPricingResponse reponse = iProductCaptureProductPriceService.captureProductPrice(auth, sku,marketplaceid);
    	iProductCaptureProductPriceService.handlerResult(reponse, auth, marketplaceid);
        return Result.success(reponse);
    }
    @ApiOperation(value = "刷新产品")
    @GetMapping("/refreshItemBySKU")
    public Result<?> refreshItemBySKUAction(String groupid,String marketplaceid,String sku) {
    	log.info("refreshPriceAction-----"+new Date());
    	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
    	Item item = iProductCaptureListingsItemService.captureListMatchingProduct(auth, sku, Arrays.asList(marketplaceid));
    	iProductCaptureListingsItemService.handlerResult(item, auth,null);
    	this.refreshPriceBySKUAction(groupid, marketplaceid, sku);
    	if(item!=null&&item.getSummaries()!=null&&item.getSummaries().size()>0) {
    		String asin=item.getSummaries().get(0).getAsin();
    		if(StrUtil.isNotBlank(asin)) {
    			this.refreshCatalogBySKUAction(groupid, marketplaceid, asin, sku);
    		}
    	}
    	return Result.success(item);
    }
    
    @ApiOperation(value = "刷新产品关系")
    @GetMapping("/refreshCatalogBySKU")
    public Result<?> refreshCatalogBySKUAction(String groupid,String marketplaceid,String asin,String sku) {
    	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
    	AmzProductRefresh skuRefresh=new AmzProductRefresh();
    	skuRefresh.setAmazonauthid(new BigInteger(auth.getId()));
    	skuRefresh.setSku(sku);
    	skuRefresh.setAsin(asin);
		com.amazon.spapi.model.catalogitems.Item item = iProductCaptureCatalogItemService.captureCatalogProduct(auth, skuRefresh, Arrays.asList(marketplaceid));
		iProductCaptureCatalogItemService.handlerResult(auth, skuRefresh, item); 
        return Result.success(item);
    }  
}

